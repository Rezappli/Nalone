package com.example.nalone.ui.profil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.nalone.enumeration.ImageType;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.GetImageListener;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.ui.evenements.creation.MainCreationEventActivity;
import com.example.nalone.util.Cache;
import com.example.nalone.dialog.LoadFragment;
import com.example.nalone.qrcode.QRCodeFragment;
import com.example.nalone.R;
import com.example.nalone.util.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

import static com.example.nalone.ui.profil.MainProfilActivity.buttonBack;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_STORAGE_REF;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;

public class ProfilFragment extends Fragment  {

    private NavController navController;
    private TextView userConnectText;
    private EditText userConnectDesc;
    private TextView userConnectVille, userConnectNbC, userConnectNbP;
    private CardView cardViewQR,cardViewPhotoPerson,cardViewProfilParametres,cardViewProfilEdit,cardViewProfilAide, cardViewPhotoEditDesc, cardViewPhotoEdit;
    private ImageView imageViewEditDescription, imageUser, imageViewEditPhoto;
    private boolean editDescription;
    private boolean editPhoto;
    private Uri imageUri = null;
    private boolean hasSelectedImage = false;
    private DialogFragment load;

    private int RESULT_LOAD_IMG = 1;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_profil, container, false);
        initView(root);
        cardViewQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQRCode();
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        userConnectText.setText(USER.getFirst_name() + " " + USER.getLast_name());
        userConnectVille.setText(USER.getCity());
        userConnectNbC.setText(USER.getNumber_events_create());
        userConnectNbP.setText(USER.getNumber_events_attend());
        userConnectDesc.setText(USER.getDescription());
        userConnectDesc.setClickable(false);
        userConnectDesc.setEnabled(false);

        cardViewPhotoEditDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editDescription){
                    userConnectDesc.setClickable(true);
                    userConnectDesc.setEnabled(true);
                    imageViewEditDescription.setImageResource(R.drawable.ic_baseline_check_24);
                    editDescription = true;
                }else{
                    userConnectDesc.setClickable(false);
                    userConnectDesc.setEnabled(false);
                    imageViewEditDescription.setImageResource(R.drawable.ic_baseline_edit_24);
                    editDescription = false;
                    if(!userConnectDesc.getText().toString().equalsIgnoreCase(USER.getDescription())){
                        USER.setDescription(userConnectDesc.getText().toString());
                        updateDescription();
                    }
                }

            }
        });

        cardViewProfilParametres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_profilFragment_to_parametresFragment);
            }
        });

        cardViewProfilEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_profilFragment_to_editFragment);
            }
        });

        cardViewProfilAide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_profilFragment_to_aideFragment);
            }
        });

        imageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editPhoto){
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                }
            }
        });

        cardViewPhotoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editPhoto){
                    imageViewEditPhoto.setImageResource(R.drawable.ic_baseline_check_24);
                    editPhoto = true;

                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);

                }else{
                    imageViewEditPhoto.setImageResource(R.drawable.ic_baseline_edit_24);
                    editPhoto = false;

                    if(hasSelectedImage){
                        //uploadFile(imageUri);
                    }
                }
            }
        });

        imageUser.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Uri uri =  Uri.parse("http://api.nolonely.fr:53000/images/EVENT/test.jpeg");
                Glide.with(getContext()).load(uri).fitCenter().centerCrop().into(imageUser);
            }
        });

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateDescription() {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.addParameter("uid", USER.getUid());
        params.addParameter("first_name", USER.getFirst_name());
        params.addParameter("last_name", USER.getLast_name());
        params.addParameter("birthday_date", USER.getBirthday_date());
        params.addParameter("city", USER.getCity());
        params.addParameter("description", USER.getDescription());
        params.addParameter("latitude", USER.getLatitude());
        params.addParameter("longitude", USER.getLongitude());

        JSONController.getJsonObjectFromUrl(Constants.URL_UPDATE_ME, getContext(), params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                Toast.makeText(getContext(), getResources().getString(R.string.update_description), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                Log.w("Response","Erreur:"+volleyError.toString());
            }
        });
    }

    public void showQRCode() {
        DialogFragment qrcode = new QRCodeFragment();
        qrcode.show(getActivity().getSupportFragmentManager(), "QR_CODE");
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
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setMinCropWindowSize(imageUser.getWidth(), imageUser.getHeight())
                    .start(getContext(), this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Glide.with(this).load(resultUri).fitCenter().centerCrop().into(imageUser);
                MainCreationEventActivity.image = resultUri;
                try {
                    String extension = resultUri.getPath().substring(resultUri.getPath().lastIndexOf("."));
                    String imageData = new String(getBytes(getContext(), MainCreationEventActivity.image), StandardCharsets.UTF_8);
                    Constants.uploadImageOnServer(ImageType.USER, USER.getUid()+extension, imageData, getContext()); //upload image on web server
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


    private void initView(View root){
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_profil);
        userConnectDesc = root.findViewById(R.id.userConnectDescription);
        userConnectText = root.findViewById(R.id.userConnectText);
        userConnectVille = root.findViewById(R.id.useConnectVille);
        userConnectNbC = root.findViewById(R.id.userConnectNbCreation);
        userConnectNbP = root.findViewById(R.id.userConnectNbParticipation);
        cardViewProfilParametres = root.findViewById(R.id.cardViewProfilParametres);
        cardViewProfilAide = root.findViewById(R.id.cardViewProfilAide);
        cardViewProfilEdit = root.findViewById(R.id.cardViewProfilEdit);
        cardViewPhotoEdit = root.findViewById(R.id.cardViewPhotoEdit);
        cardViewPhotoEditDesc = root.findViewById(R.id.cardViewPhotoEditDesc);
        imageViewEditDescription = root.findViewById(R.id.imageViewEditDescription);
        imageUser = root.findViewById(R.id.imageUser);
        imageViewEditPhoto = root.findViewById(R.id.imageViewEditPhoto);
        cardViewPhotoPerson = root.findViewById(R.id.cardViewUser);

        cardViewQR = root.findViewById(R.id.cardViewQR);
    }
}