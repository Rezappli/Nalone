package com.example.nalone.ui.evenements.creation;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nalone.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.List;

public class AdressEventFragment extends Fragment {

    private TextInputEditText event_adresse,event_city;


    public AdressEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_adress_event, container, false);
        Button next = root.findViewById(R.id.buttonNextFragmentDate);
        event_adresse = root.findViewById(R.id.eventAdress);
        event_city = root.findViewById(R.id.eventCity);
        initialiserImageView(root);
        checkValidation();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAdress();
            }
        });
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
        imageProgessCreationPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPhoto();
            }
        });
        imageProgessCreationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goName();
            }
        });
        imageProgessCreationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDate();
            }
        });
        imageProgessCreationMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMembers();
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void checkValidation(){
        if (MainCreationEventActivity.adressValidate){
            imageProgessCreationPosition.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_adress_focused));
            event_adresse.setText(MainCreationEventActivity.currentEvent.getAddress());
            event_city.setText(MainCreationEventActivity.currentEvent.getCity());
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

    private void validateAdress(){
        try {
            if(getLocationFromAddress(event_adresse.getText().toString() + "," + event_city.getText().toString()) == null){
                event_adresse.setError("Adresse introuvable");
            }else if(event_adresse.getText().toString().matches("")){
                event_adresse.setError("Champs obligatoire");
            }else if(event_city.getText().toString().matches("")){
                event_city.setError("Champs obligatoire");
            }else {
                MainCreationEventActivity.adressValidate = true;
                MainCreationEventActivity.currentEvent.setAddress(event_adresse.getText().toString());
                MainCreationEventActivity.currentEvent.setCity(event_city.getText().toString());
                if(MainCreationEventActivity.isAllValidate()){
                    Toast.makeText(getContext(), "Evenement créer", Toast.LENGTH_SHORT).show();
                }else if(!MainCreationEventActivity.nameValidate) {
                    goName();
                }else if(!MainCreationEventActivity.dateValidate){
                    goDate();
                }else if(!MainCreationEventActivity.photoValidate){
                    goPhoto();
                }else if(!MainCreationEventActivity.membersValidate){
                    goMembers();
                }
            }
        }catch (Exception e){
            Log.w("Response", e.getMessage());
            Toast.makeText(getContext(), getResources().getString(R.string.error_address), Toast.LENGTH_SHORT).show();
        }


    }

    private void goDate(){
        MainCreationEventActivity.navController.navigate(R.id.action_adressEventFragment_to_dateEventFragment);
    }
    private void goMembers(){
        MainCreationEventActivity.navController.navigate(R.id.action_adressEventFragment_to_membersEventFragment);    }
    private void goName(){
        MainCreationEventActivity.navController.navigate(R.id.action_adressEventFragment_to_nameEventFragment);    }
    private void goPhoto(){
        MainCreationEventActivity.navController.navigate(R.id.action_adressEventFragment_to_photoEventFragment);    }

    private LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(getActivity());
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 10);
            if (address == null) {
                return null;
            }

            if(address.size() > 0) {
                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            }else{
                return null;
            }

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
}