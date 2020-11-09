package com.example.nalone.ui.profil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.nalone.MainActivity;
import com.example.nalone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;
import static com.example.nalone.util.Constants.USERS_LIST;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.USER_IMAGE_URI;
import static com.example.nalone.util.Constants.mProfilRef;
import static com.example.nalone.util.Constants.mStore;

public class ProfilFragment extends Fragment  {

    private NavController navController;
    private TextView userConnectText;
    private EditText userConnectDesc;
    private TextView userConnectVille, userConnectNbC, userConnectNbP;
    private CardView cardViewProfilParametres,cardViewProfilEdit,cardViewProfilAide, cardViewPhotoEditDesc, cardViewPhotoEdit;
    private ImageView imageViewEditDescription, imageUser, imageViewEditPhoto;
    private boolean editDescription;
    private boolean editPhoto;
    private Uri imageUri = null;
    private boolean hasSelectedImage = false;

    private int RESULT_LOAD_IMG = 1;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_profil, container, false);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
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

        userConnectText.setText(USERS_LIST.get(USER_ID).getPrenom()+" "+USERS_LIST.get(USER_ID).getNom());
        userConnectVille.setText(USERS_LIST.get(USER_ID).getVille()+" ");
        userConnectNbC.setText(USERS_LIST.get(USER_ID).getNbCreation()+"");
        userConnectNbP.setText(USERS_LIST.get(USER_ID).getNbParticipation()+"");
        userConnectDesc.setText(USERS_LIST.get(USER_ID).getDescription());
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
                    }

                }
            });





        cardViewProfilParametres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_profil_to_navigation_parametres);
            }
        });

        cardViewProfilEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_profil_to_navigation_edit_profil);
            }
        });

        cardViewProfilAide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_profil_to_navigation_aide);
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
                        uploadFile(imageUri);
                    }
                }
            }
        });

        loadProfilImage();

        return root;
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                assert imageUri != null;
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap img  = BitmapFactory.decodeStream(imageStream);
                imageUser.setImageBitmap(img);
                hasSelectedImage = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Une erreur s'est produite",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(getContext(),"Vous n'avez pas choisi d'image", Toast.LENGTH_LONG).show();
        }
    }


    public void uploadFile(Uri imagUri) {
        if (imagUri != null) {


            mProfilRef.putFile(imagUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot snapshot) {
                            // Get the download URL
                            //Uri downloadUri = snapshot.getMetadata().
                            // use this download url with imageview for viewing & store this linke to firebase message data
                            Toast.makeText(getContext(), "Vous avez changé votre photo de profil !", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.w("Error", exception.getMessage());
                        }
                    });
        }
    }

    public void loadProfilImage(){
        if(USER_IMAGE_URI != null) {
            Glide.with(getContext()).load(USER_IMAGE_URI).into(imageUser);
        }
    }




}