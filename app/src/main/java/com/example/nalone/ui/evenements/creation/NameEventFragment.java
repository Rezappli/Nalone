package com.example.nalone.ui.evenements.creation;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nalone.R;
import com.example.nalone.objects.Evenement;
import com.google.android.material.textfield.TextInputEditText;

public class NameEventFragment extends Fragment {
    private TextInputEditText event_name;
    private TextInputEditText event_resume;


    public NameEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_name_event, container, false);
        event_name = root.findViewById(R.id.eventName);
        event_resume = root.findViewById(R.id.eventResume);

        initialiserImageView(root);
        checkValidation();


        Button buttonNext = root.findViewById(R.id.button2);

        final NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment2);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateName();
            }
        });
        return root;
    }

    public static ImageView imageProgessCreationPhoto,imageProgessCreationDate,imageProgessCreationPosition,imageProgessCreationName,
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
                MainCreationEventActivity.navController.navigate(R.id.action_nameEventFragment_to_dateEventFragment);
            }
        });
        imageProgessCreationPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainCreationEventActivity.navController.navigate(R.id.action_nameEventFragment_to_photoEventFragment);
            }
        });
        imageProgessCreationPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainCreationEventActivity.navController.navigate(R.id.action_nameEventFragment_to_adressEventFragment);
            }
        });
        imageProgessCreationMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainCreationEventActivity.navController.navigate(R.id.action_nameEventFragment_to_membersEventFragment);
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
            event_name.setText(MainCreationEventActivity.currentEvent.getName());
        }
        if (MainCreationEventActivity.photoValidate){
            imageProgessCreationPhoto.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_photo_focused));
        }

    }

    private void validateName(){
        if(event_name.getText().toString().matches("")){
            event_name.setError("Champs obligatoire");
        }else{
            MainCreationEventActivity.currentEvent.setName(event_name.getText().toString());
            MainCreationEventActivity.nameValidate = true;
        /*
        THIBAULT MET LA PHOTO DE L'EVENT
         */
            if(MainCreationEventActivity.isAllValidate()){
                Toast.makeText(getContext(), "Evenement créer", Toast.LENGTH_SHORT).show();
            }else if(!MainCreationEventActivity.adressValidate) {
                goAdress();
            }else if(!MainCreationEventActivity.dateValidate){
                goDate();
            }else if(!MainCreationEventActivity.photoValidate){
                goPhoto();
            }else if(!MainCreationEventActivity.membersValidate){
                goMembers();
            }
        }

    }

    private void goDate(){
        MainCreationEventActivity.navController.navigate(R.id.action_nameEventFragment_to_dateEventFragment);
    }
    private void goAdress(){
        MainCreationEventActivity.navController.navigate(R.id.action_nameEventFragment_to_adressEventFragment);
    }
    private void goMembers(){
        MainCreationEventActivity.navController.navigate(R.id.action_nameEventFragment_to_membersEventFragment);
    }
    private void goPhoto(){
        MainCreationEventActivity.navController.navigate(R.id.action_nameEventFragment_to_photoEventFragment);
    }

}