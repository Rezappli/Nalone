package com.example.nalone.ui.evenements.creation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nalone.R;
import com.example.nalone.objects.Evenement;

import static android.app.Activity.RESULT_OK;


public class PhotoEventFragment extends Fragment {

    private ImageView imagePhoto;
    static final int RESULT_LOAD_IMG = 1;

    //private NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment2);


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
            @Override
            public void onClick(View v) {
                if (MainCreationEventActivity.image == null) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Vous n'avez pas séléctionné de photo de profil ! Voulez-vous continuer ?")
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    validatePhoto();
                                }
                            })
                            .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    builder.create();
                    builder.show();
                }else{
                    validatePhoto();
                }

            }
        });

        if(MainCreationEventActivity.image != null){
            Glide.with(this).load(MainCreationEventActivity.image).fitCenter().centerCrop().into(imagePhoto);
        }
        // Inflate the layout for this fragment
        return root;
    }

    private ImageView imageProgessCreationPhoto,imageProgessCreationDate,imageProgessCreationPosition,imageProgessCreationName,
            imageProgessCreationMembers;

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

    private void validatePhoto(){
        MainCreationEventActivity.photoValidate = true;
        if(MainCreationEventActivity.isAllValidate()){
            Toast.makeText(getContext(), "Evenement créer", Toast.LENGTH_SHORT).show();
        }else if(!MainCreationEventActivity.adressValidate) {
            goAdress();
        }else if(!MainCreationEventActivity.dateValidate){
            goDate();
        }else if(!MainCreationEventActivity.nameValidate){
            goName();
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
        if (MainCreationEventActivity.adressValidate){
            imageProgessCreationPosition.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_adress_focused));
        }
        if (MainCreationEventActivity.dateValidate){
            imageProgessCreationDate.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_date_focused));
        }
        if (MainCreationEventActivity.membersValidate){
            imageProgessCreationMembers.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_members_focused));
        }
        if (MainCreationEventActivity.nameValidate){
            imageProgessCreationName.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_name_focused));
        }
        if (MainCreationEventActivity.photoValidate){
            imageProgessCreationPhoto.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_photo_focused));
        }

    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            assert imageUri != null;
            Glide.with(this).load(imageUri).fitCenter().centerCrop().into(imagePhoto);
            MainCreationEventActivity.image = imageUri;
        }else {
            Toast.makeText(getContext(),"Vous n'avez pas choisi d'image", Toast.LENGTH_LONG).show();

        }
    }

}