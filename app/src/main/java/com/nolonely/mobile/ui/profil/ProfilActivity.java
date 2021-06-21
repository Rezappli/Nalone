package com.nolonely.mobile.ui.profil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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
import com.nolonely.mobile.R;
import com.nolonely.mobile.bdd.json.JSONController;
import com.nolonely.mobile.bdd.json.JSONObjectCrypt;
import com.nolonely.mobile.bdd.sql_lite.DatabaseManager;
import com.nolonely.mobile.enumeration.ImageType;
import com.nolonely.mobile.listeners.JSONObjectListener;
import com.nolonely.mobile.qrcode.QRCodeFragment;
import com.nolonely.mobile.util.Constants;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import static com.nolonely.mobile.util.Constants.BASE_API_URL;
import static com.nolonely.mobile.util.Constants.USER;

public class ProfilActivity extends AppCompatActivity {

    private TextView userConnectText;
    private EditText userConnectDesc;
    private TextView userConnectVille, userConnectNbC, userConnectNbP;
    private CardView cardViewQR, cardViewPhotoPerson, cardViewProfilParametres, cardViewProfilEdit, cardViewProfilAide, cardViewPhotoEditDesc, cardViewPhotoEdit;
    private ImageView imageViewEditDescription, imageUser, imageViewEditPhoto;
    private boolean editDescription;
    private boolean editPhoto;
    private Uri imageUri = null;
    private boolean hasSelectedImage = false;
    private CardView cardViewBalance;

    private int RESULT_LOAD_IMG = 1;
    private ImageView buttonBack;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profil);
        initView();
        cardViewQR.setOnClickListener(v -> showQRCode());

        userConnectText.setText(USER.getName());
        userConnectVille.setText(USER.getCity() + "");
        userConnectNbC.setText(USER.getNumber_events_create() + "");
        userConnectNbP.setText(USER.getNumber_events_attend() + "");
        userConnectDesc.setText(USER.getDescription());
        userConnectDesc.setClickable(false);
        userConnectDesc.setEnabled(false);

        cardViewPhotoEditDesc.setOnClickListener(v -> {
            if (!editDescription) {
                userConnectDesc.setClickable(true);
                userConnectDesc.setEnabled(true);
                imageViewEditDescription.setImageResource(R.drawable.ic_baseline_check_24);
                editDescription = true;
            } else {
                userConnectDesc.setClickable(false);
                userConnectDesc.setEnabled(false);
                imageViewEditDescription.setImageResource(R.drawable.ic_baseline_edit_24);
                editDescription = false;
                if (!userConnectDesc.getText().toString().equalsIgnoreCase(USER.getDescription())) {
                    USER.setDescription(userConnectDesc.getText().toString());
                    updateUserInformations();
                }
            }

        });

        cardViewProfilParametres.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), ParametresActivity.class)));

        cardViewProfilEdit.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), EditActivity.class)));

        cardViewProfilAide.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), AideActivity.class)));

        imageUser.setOnClickListener(v -> {
            if (editPhoto) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        cardViewPhotoEdit.setOnClickListener(v -> {
            if (!editPhoto) {
                imageViewEditPhoto.setImageResource(R.drawable.ic_baseline_check_24);
                editPhoto = true;
                hasSelectedImage = false;

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);

            } else {
                imageViewEditPhoto.setImageResource(R.drawable.ic_baseline_edit_24);
                editPhoto = false;

                if (hasSelectedImage) {
                    try {
                        String imageDate = Constants.getDateDayHoursMinutesSeconds(new Date(System.currentTimeMillis())); //ne pas enlever et remplacer dans les fonctions sinon les temps ne seront pas les même au moment de l'execution
                        //et peut créer un décalage de 1s
                        String imageData = BitMapToString(MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri));
                        Constants.uploadImageOnServer(ImageType.USER, USER.getUid() + ";" + imageDate, imageData, getBaseContext()); //upload image on web server
                        USER.setImage_url(BASE_API_URL + "/images/" + ImageType.USER + "/" + USER.getUid() + ";" + imageDate + ".jpg");

                        Log.w("Image", "From profil activity new url : " + BASE_API_URL + "/images/" + ImageType.USER + "/" + USER.getUid() + ";" + imageDate + ".jpg");
                        Toast.makeText(ProfilActivity.this, getResources().getString(R.string.image_save), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(ProfilActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        Log.w("Response", "Erreur: " + e.getMessage());
                    }
                }
            }
        });

        Constants.setUserImage(USER, imageUser);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateUserInformations() {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("name", USER.getName());
        params.putCryptParameter("city", USER.getCity());
        params.putCryptParameter("description", USER.getDescription());
        params.putCryptParameter("latitude", USER.getLatitude());
        params.putCryptParameter("longitude", USER.getLongitude());

        JSONController.getJsonObjectFromUrl(Constants.URL_UPDATE_ME, getBaseContext(), params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.update_description), Toast.LENGTH_SHORT).show();
                DatabaseManager databaseManager = new DatabaseManager(getBaseContext());
                databaseManager.updateUser(USER);
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                Log.w("Response", "Erreur:" + volleyError.toString());
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
                    .setAspectRatio(1, 1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAllowFlipping(false)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setMinCropWindowSize(imageUser.getWidth(), imageUser.getHeight())
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                Glide.with(this).load(imageUri).fitCenter().centerCrop().into(imageUser);
                hasSelectedImage = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.w("Response", result.getError());
            }
        }
    }


    private void initView() {
        cardViewBalance = findViewById(R.id.cardViewBalance);
        userConnectDesc = findViewById(R.id.userConnectDescription);
        userConnectText = findViewById(R.id.userConnectText);
        userConnectVille = findViewById(R.id.userConnectVille);
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
        buttonBack.setOnClickListener(v -> onBackPressed());
        cardViewBalance.setOnClickListener(v -> startActivity(new Intent(ProfilActivity.this, SampleActivity.class)));
        cardViewQR = findViewById(R.id.cardViewQR);
    }

    private static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}