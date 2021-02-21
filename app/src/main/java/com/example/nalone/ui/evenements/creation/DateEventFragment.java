package com.example.nalone.ui.evenements.creation;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nalone.R;
import com.example.nalone.dialog.SelectDateFragment;
import com.example.nalone.dialog.TimePickerFragment;
import com.example.nalone.enumeration.StatusEvent;
import com.example.nalone.util.Horloge;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateEventFragment extends Fragment {
    public static TextView eventStartDate,eventStartHoraire,eventEndDate,eventEndHoraire;
    private Button next;
    public DateEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_date_event, container, false);
        eventStartDate = root.findViewById(R.id.eventDate);
        eventEndDate = root.findViewById(R.id.eventEndDate);
        eventStartHoraire = root.findViewById(R.id.eventHoraire);
        eventEndHoraire = root.findViewById(R.id.eventEndHoraire);
        next = root.findViewById(R.id.button2);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validateDate();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        eventStartHoraire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                TimePickerFragment.isStart = true;
                newFragment.show(getActivity().getSupportFragmentManager(), "DIALOG_TIME");
            }
        });

        eventStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment();
                SelectDateFragment.isStart = true;
                newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
            }
        });

        eventEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment();
                SelectDateFragment.isStart = false;
                newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
            }
        });

        eventEndHoraire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                TimePickerFragment.isStart = false;
                newFragment.show(getActivity().getSupportFragmentManager(), "DIALOG_TIME");
            }
        });


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

    @SuppressLint("UseCompatLoadingForDrawables")
    private void checkValidation(){
        if (MainCreationEventActivity.adressValidate){
            imageProgessCreationPosition.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_adress_focused));
        }
        if (MainCreationEventActivity.dateValidate){
            imageProgessCreationDate.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_date_focused));
            //eventStartDate.setText(MainCreationEventActivity.currentEvent.getStartDate().toDate().toString());
            //eventEndDate.setText(MainCreationEventActivity.currentEvent.getStartDate().toDate().toString());
            //eventStartHoraire.setText(MainCreationEventActivity.currentEvent.getStartDate().toDate().toString());
            //eventEndHoraire.setText(MainCreationEventActivity.currentEvent.getStartDate().toDate().toString());
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

    private void validateDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        String new_event_time_start = eventStartDate.getText().toString().replaceAll(" ", "");
        String new_event_time_end = eventEndDate.getText().toString().replaceAll(" ", "");
        String final_event_time_start = "";
        String final_event_time_end = "";
        for(char c : new_event_time_start.toCharArray()) {
            if (c != ' ') {
                final_event_time_start += c;
            }
        }

        for(char c : new_event_time_end.toCharArray()) {
            if (c != ' ') {
                final_event_time_end += c;
            }
        }

        Timestamp tsStart = new Timestamp(sdf.parse(eventStartDate.getText().toString()+" "+final_event_time_start));
        Timestamp tsEnd = new Timestamp(sdf.parse(eventEndDate.getText().toString()+" "+final_event_time_end));
        StatusEvent seStart = Horloge.verifStatut(new Date(),tsStart.toDate());
        StatusEvent seEnd = Horloge.verifStatut(new Date(),tsEnd.toDate());
        if(seStart == StatusEvent.FINI || seStart == StatusEvent.EXPIRE || seEnd == StatusEvent.FINI || seEnd == StatusEvent.EXPIRE ){
            Toast.makeText(getContext(), "Veuillez selectionner des dates dans le futur", Toast.LENGTH_SHORT).show();
        }else if(eventStartDate.getText().toString().equalsIgnoreCase("date")){
            eventStartDate.setError("Champs obligatoire");
        }else if(eventEndDate.getText().toString().equalsIgnoreCase("date")){
            eventEndDate.setError("Champs obligatoire");
        }else if(eventStartHoraire.getText().toString().equalsIgnoreCase("horaire")){
            eventStartHoraire.setError("Champs obligatoire");
        }else if(eventEndHoraire.getText().toString().equalsIgnoreCase("horaire")){
            eventEndHoraire.setError("Champs obligatoire");
        }else{
                MainCreationEventActivity.dateValidate = true;
                 //MainCreationEventActivity.currentEvent.setStartDate(tsStart);
                 //MainCreationEventActivity.currentEvent.setEndDate(tsEnd);
                if(MainCreationEventActivity.isAllValidate()){
                    Toast.makeText(getContext(), "Evenement créer", Toast.LENGTH_SHORT).show();
                }else if(!MainCreationEventActivity.adressValidate) {
                    goAdress();
                }else if(!MainCreationEventActivity.nameValidate){
                    goName();
                }else if(!MainCreationEventActivity.photoValidate){
                    goPhoto();
                }else if(!MainCreationEventActivity.membersValidate){
                    goMembers();
                }
            }
        }

    private void goAdress(){
        MainCreationEventActivity.navController.navigate(R.id.action_dateEventFragment_to_adressEventFragment);
    }
    private void goMembers(){
        MainCreationEventActivity.navController.navigate(R.id.action_dateEventFragment_to_membersEventFragment);
    }
    private void goName(){
        MainCreationEventActivity.navController.navigate(R.id.action_dateEventFragment_to_nameEventFragment);
    }
    private void goPhoto(){
        MainCreationEventActivity.navController.navigate(R.id.action_dateEventFragment_to_photoEventFragment);
    }
}