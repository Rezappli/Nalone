package com.example.nalone.ui.evenements.creation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nalone.R;
import com.example.nalone.enumeration.ImageProtocol;
import com.example.nalone.enumeration.ImageType;
import com.example.nalone.util.Constants;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.example.nalone.util.Constants.USER;


public class PhotoEventFragment extends Fragment {

    private ImageView imagePhoto;
    private final int RESULT_LOAD_IMG = 1;
    private ImageView imageProgessCreationPhoto,imageProgessCreationDate,imageProgessCreationPosition,imageProgessCreationName,
            imageProgessCreationMembers;

    public PhotoEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_photo_event, container, false);


        imagePhoto = root.findViewById(R.id.imagePhoto);
        imagePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });
        Button buttonNext = root.findViewById(R.id.buttonNextFragmentDate);
        initialiserImageView(root);
        checkValidation();
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (MainCreationEventActivity.image == null) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(getResources().getString(R.string.no_image_select))
                            .setPositiveButton(getResources().getString(R.string.button_yes), new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                public void onClick(DialogInterface dialog, int id) {
                                    validatePhoto();
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.button_no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create();
                    builder.show();
                }else{
                    validatePhoto();
                }
            }
        });

        return root;
    }

    private void initialiserImageView(View root) {
        imageProgessCreationDate = root.findViewById(R.id.imageProgessCreationDate);
        imageProgessCreationMembers = root.findViewById(R.id.imageProgessCreationMembers);
        imageProgessCreationName = root.findViewById(R.id.imageProgessCreationName);
        imageProgessCreationPosition = root.findViewById(R.id.imageProgessCreationPosition);
        imageProgessCreationPhoto = root.findViewById(R.id.imageProgessCreationPhoto);
        imageProgessCreationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
goDate();            }
        });
        imageProgessCreationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goName();
            }
        });
        imageProgessCreationPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAdress();
            }
        });
        imageProgessCreationMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMembers();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void validatePhoto(){
        MainCreationEventActivity.photoValidate = true;
        if(MainCreationEventActivity.isAllValidate(getContext())){
            MainCreationEventActivity.createEvent(getContext());
        }else if(!MainCreationEventActivity.nameValidate){
            goName();
        }else if(!MainCreationEventActivity.dateValidate){
            goDate();
        }else if(!MainCreationEventActivity.addressValidate) {
            goAdress();
        }else if(!MainCreationEventActivity.membersValidate){
            goMembers();
        }
    }

    private void goDate(){
        MainCreationEventActivity.navController.navigate(R.id.action_photoEventFragment_to_dateEventFragment);
    }
    private void goAdress(){
        MainCreationEventActivity.navController.navigate(R.id.action_photoEventFragment_to_adressEventFragment);
    }
    private void goMembers(){
        MainCreationEventActivity.navController.navigate(R.id.action_photoEventFragment_to_membersEventFragment);
    }
    private void goName(){
        MainCreationEventActivity.navController.navigate(R.id.action_photoEventFragment_to_nameEventFragment);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void checkValidation(){
        if(MainCreationEventActivity.image != null){
            Glide.with(this).load(MainCreationEventActivity.image).fitCenter().centerCrop().into(imagePhoto);
        }
        if (MainCreationEventActivity.nameValidate){
            imageProgessCreationName.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_name_focused));
        }
        if (MainCreationEventActivity.dateValidate){
            imageProgessCreationDate.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_date_focused));
        }
        if (MainCreationEventActivity.addressValidate){
            imageProgessCreationPosition.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_adress_focused));
        }

        if (MainCreationEventActivity.membersValidate){
            imageProgessCreationMembers.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_members_focused));
        }

        if (MainCreationEventActivity.photoValidate){
            imageProgessCreationPhoto.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_photo_focused));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMG) {

            CropImage.activity(data.getData())
                    .setMultiTouchEnabled(true)
                    .setAspectRatio(1,1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAllowFlipping(false)
                    .start(getContext(), this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Glide.with(this).load(resultUri).fitCenter().centerCrop().into(imagePhoto);
                MainCreationEventActivity.image = resultUri;
                try {
                    String extension = resultUri.getPath().substring(resultUri.getPath().lastIndexOf("."));
                    String imageData = new String(getBytes(getContext(), MainCreationEventActivity.image), StandardCharsets.UTF_8);
                    Constants.uploadImageOnServer(ImageType.EVENT, MainCreationEventActivity.currentEvent.getUid()+extension, imageData, getContext()); //upload image on web server
                } catch (IOException e) {
                    Log.w("Response", "Erreur: "+e.getMessage());
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.w("Response", result.getError());
            }
        }


    }

    public static byte[] getBytes(Context context, Uri uri) throws IOException {
        InputStream iStream = context.getContentResolver().openInputStream(uri);
        try {
            return getBytes(iStream);
        } finally {
            // close the stream
            try {
                iStream.close();
            } catch (IOException ignored) { /* do nothing */ }
        }
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {

        byte[] bytesResult = null;
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        try {
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            bytesResult = byteBuffer.toByteArray();
        } finally {
            // close the stream
            try{ byteBuffer.close(); } catch (IOException ignored){ /* do nothing */ }
        }
        return bytesResult;
    }
}