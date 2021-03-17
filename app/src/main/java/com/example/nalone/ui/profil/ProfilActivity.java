package com.example.nalone.ui.profil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.nalone.enumeration.ImageType;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.ui.evenements.creation.MainCreationEventActivity;
import com.example.nalone.qrcode.QRCodeFragment;
import com.example.nalone.R;
import com.example.nalone.util.Constants;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.example.nalone.util.Constants.USER;

public class ProfilActivity extends AppCompatActivity {

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
    private ImageView buttonBack;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profil);
        initView();
        cardViewQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQRCode();
            }
        });
       /* buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        userConnectText.setText(USER.getFirst_name() + " " + USER.getLast_name());
        userConnectVille.setText(USER.getCity());
        userConnectNbC.setText(USER.getNumber_events_create());
        userConnectNbP.setText(USER.getNumber_events_attend());
        userConnectDesc.setText(USER.getDescription());
        userConnectDesc.setClickable(false);
        userConnectDesc.setEnabled(false);

        cardViewPhotoEditDesc.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
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
                startActivity(new Intent(getBaseContext(), ParametresActivity.class));
            }
        });

        cardViewProfilEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), EditActivity.class));

            }
        });

        cardViewProfilAide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), AideActivity.class));

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
                Glide.with(getBaseContext()).load(uri).fitCenter().centerCrop().into(imageUser);
            }
        });
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

        JSONController.getJsonObjectFromUrl(Constants.URL_UPDATE_ME, getBaseContext(), params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.update_description), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                Log.w("Response","Erreur:"+volleyError.toString());
            }
        });
    }

    public void showQRCode() {
        DialogFragment qrcode = new QRCodeFragment();
        qrcode.show(this.getSupportFragmentManager(), "QR_CODE");
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
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Glide.with(this).load(resultUri).fitCenter().centerCrop().into(imageUser);
                MainCreationEventActivity.image = resultUri;
                try {
                    String extension = resultUri.getPath().substring(resultUri.getPath().lastIndexOf("."));
                    String imageData = new String(getBytes(getBaseContext(), MainCreationEventActivity.image), StandardCharsets.UTF_8);
                    Constants.uploadImageOnServer(ImageType.USER, USER.getUid()+extension, imageData, getBaseContext()); //upload image on web server
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


    private void initView(){
        userConnectDesc = findViewById(R.id.userConnectDescription);
        userConnectText = findViewById(R.id.userConnectText);
        userConnectVille = findViewById(R.id.useConnectVille);
        userConnectNbC = findViewById(R.id.userConnectNbCreation);
        userConnectNbP = findViewById(R.id.userConnectNbParticipation);
        cardViewProfilParametres = findViewById(R.id.cardViewProfilParametres);
        cardViewProfilAide = findViewById(R.id.cardViewProfilAide);
        cardViewProfilEdit = findViewById(R.id.cardViewProfilEdit);
        cardViewPhotoEdit = findViewById(R.id.cardViewPhotoEdit);
        cardViewPhotoEditDesc = findViewById(R.id.cardViewPhotoEditDesc);
        imageViewEditDescription = findViewById(R.id.imageViewEditDescription);
        imageUser = findViewById(R.id.imageUser);
        imageViewEditPhoto = findViewById(R.id.imageViewEditPhoto);
        cardViewPhotoPerson = findViewById(R.id.cardViewUser);
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cardViewQR = findViewById(R.id.cardViewQR);
    }
}