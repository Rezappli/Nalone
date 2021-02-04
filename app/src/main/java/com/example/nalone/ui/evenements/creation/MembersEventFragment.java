package com.example.nalone.ui.evenements.creation;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nalone.R;

public class MembersEventFragment extends Fragment {

    public MembersEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_members_event, container, false);
        // Inflate the layout for this fragment
        initialiserImageView(root);
        checkValidation();
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
                goDate();
            }
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
        imageProgessCreationPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPhoto();
            }
        });
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

    private void validateName(){
        MainCreationEventActivity.membersValidate = true;
        /*
        THIBAULT MET LA PHOTO DE L'EVENT
         */
        if(MainCreationEventActivity.isAllValidate()){
            Toast.makeText(getContext(), "Evenement créer", Toast.LENGTH_SHORT).show();
        }else if(!MainCreationEventActivity.nameValidate) {
            goName();
        }else if(!MainCreationEventActivity.dateValidate){
            goDate();
        }else if(!MainCreationEventActivity.photoValidate){
            goPhoto();
        }else if(!MainCreationEventActivity.adressValidate){
            goAdress();
        }
    }

    private void goDate(){
        MainCreationEventActivity.navController.navigate(R.id.action_membersEventFragment_to_dateEventFragment);    }
    private void goAdress(){
        MainCreationEventActivity.navController.navigate(R.id.action_membersEventFragment_to_adressEventFragment);
    }
    private void goName(){
        MainCreationEventActivity.navController.navigate(R.id.action_membersEventFragment_to_nameEventFragment);   }
    private void goPhoto(){
        MainCreationEventActivity.navController.navigate(R.id.action_membersEventFragment_to_photoEventFragment);   }
}