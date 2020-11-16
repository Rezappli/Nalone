package com.example.nalone.ui.profil;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.nalone.Cache;
import com.example.nalone.R;
import com.example.nalone.items.ItemPerson;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_STORAGE_REF;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.nolonelyBundle;
import static com.example.nalone.util.Constants.updateUserData;

public class ProfilFragment extends Fragment  {

    private NavController navController;
    private TextView userConnectText;
    private EditText userConnectDesc;
    private TextView userConnectVille, userConnectNbC, userConnectNbP;
    private CardView cardViewPhotoPerson,cardViewProfilParametres,cardViewProfilEdit,cardViewProfilAide, cardViewPhotoEditDesc, cardViewPhotoEdit;
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
        cardViewPhotoPerson = root.findViewById(R.id.cardViewUser);

        userConnectText.setText(USER.getFirst_name() + " " + USER.getLast_name());
        userConnectVille.setText(USER.getCity());
        userConnectNbC.setText(USER.getNumber_events_create());
        userConnectNbP.setText(USER.getNumber_events_attend());
        userConnectDesc.setText(USER.getDescription());
        userConnectDesc.setClickable(false);
        userConnectDesc.setEnabled(false);

        if(USER.getCursus().equalsIgnoreCase("Informatique")){
            cardViewPhotoPerson.setCardBackgroundColor(Color.RED);
        }
        if(USER.getCursus().equalsIgnoreCase("TC")){
            cardViewPhotoPerson.setCardBackgroundColor(Color.GREEN);
        }
        if(USER.getCursus().equalsIgnoreCase("MMI")){
            cardViewPhotoPerson.setCardBackgroundColor(Color.parseColor("#4B0082"));
        }
        if(USER.getCursus().equalsIgnoreCase("GB")){
            cardViewPhotoPerson.setCardBackgroundColor(Color.BLUE);
        }
        if(USER.getCursus().equalsIgnoreCase("LP")){
            cardViewPhotoPerson.setCardBackgroundColor(Color.GRAY);
        }

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

        imageUser.post(new Runnable() {
            @Override
            public void run() {
                if(USER.getImage_url() != null) {
                    if(!Cache.fileExists(USER.getUid())) {
                        StorageReference imgRef = mStore.getReference("users/" + USER.getUid());
                        if (imgRef != null) {
                            imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri img = task.getResult();
                                        if (img != null) {
                                            Cache.saveUriFile(USER.getUid(), img);
                                            Glide.with(getContext()).load(img).fitCenter().centerCrop().into(imageUser);
                                        }
                                    }
                                }
                            });
                        }
                    }else{
                        Glide.with(getContext()).load(Cache.getUriFromUid(USER.getUid())).fitCenter().centerCrop().into(imageUser);
                    }
                }
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
                imageUri = data.getData();
                assert imageUri != null;
                Glide.with(getContext()).load(imageUri).fitCenter().centerCrop().into(imageUser);
                hasSelectedImage = true;
        }else {
            Toast.makeText(getContext(),"Vous n'avez pas choisi d'image", Toast.LENGTH_LONG).show();
            imageViewEditPhoto.setImageResource(R.drawable.ic_baseline_edit_24);
            editPhoto = false;
        }
    }


    public void uploadFile(Uri imagUri) {
        if (imagUri != null) {

            USER_STORAGE_REF.putFile(imagUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot snapshot) {
                            Toast.makeText(getContext(), "Vous avez changé votre photo de profil !", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.w("Error", exception.getMessage());
                        }
                    });
            USER.setImage_url(imagUri.getPath());
            updateUserData(USER);
        }
        Glide.with(getContext()).load(imagUri).fitCenter().centerCrop().into(imageUser);
    }



}