package com.example.nalone.ui.evenements.creation;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateEventFragment extends Fragment {
    public static TextView eventStartDate, eventStartHoraire, eventEndDate, eventEndHoraire;
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
        next = root.findViewById(R.id.buttonNextFragmentDate);
        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {
                    validateDate();
                } catch (ParseException e) {
                    Log.w("Response", "Erreur:" + e);
                    Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
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

    private ImageView imageProgessCreationPhoto, imageProgessCreationDate, imageProgessCreationPosition, imageProgessCreationName,
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
    private void checkValidation() {
        if (MainCreationEventActivity.addressValidate) {
            imageProgessCreationPosition.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_adress_focused));
        }
        if (MainCreationEventActivity.dateValidate) {
            imageProgessCreationDate.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_date_focused));
            eventStartDate.setText(cutString(MainCreationEventActivity.currentEvent.getStartDate(), 10, -1));
            eventEndDate.setText(cutString(MainCreationEventActivity.currentEvent.getStartDate(), 10, -1));
            eventStartHoraire.setText(cutString(MainCreationEventActivity.currentEvent.getStartDate(), 5, 11));
            eventEndHoraire.setText(cutString(MainCreationEventActivity.currentEvent.getStartDate(), 5, 11));
        }
        if (MainCreationEventActivity.membersValidate) {
            imageProgessCreationMembers.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_members_focused));
        }
        if (MainCreationEventActivity.nameValidate) {
            imageProgessCreationName.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_name_focused));
        }
        if (MainCreationEventActivity.photoValidate) {
            imageProgessCreationPhoto.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_photo_focused));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void validateDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        if (eventStartDate.getText().toString().equalsIgnoreCase("date")) {
            eventStartDate.setError(getResources().getString(R.string.required_field));
            return;
        } else {
            eventStartDate.setError(null);
        }

        if (eventEndDate.getText().toString().equalsIgnoreCase("date")) {
            eventEndDate.setError(getResources().getString(R.string.required_field));
            return;
        } else {
            eventEndDate.setError(null);
        }

        if (eventStartHoraire.getText().toString().equalsIgnoreCase("horaire")) {
            eventStartHoraire.setError(getResources().getString(R.string.required_field));
            return;
        } else {
            eventStartHoraire.setError(null);
        }

        if (eventEndHoraire.getText().toString().equalsIgnoreCase("horaire")) {
            eventEndHoraire.setError(getResources().getString(R.string.required_field));
            return;
        } else {
            eventEndHoraire.setError(null);
        }


        String tsStart = eventStartDate.getText().toString() + " " + eventStartHoraire.getText().toString();
        String tsEnd = eventEndDate.getText().toString() + " " + eventEndHoraire.getText().toString();
        StatusEvent seStart = Horloge.verifStatut(new Date(), sdf.parse(tsStart));
        StatusEvent seEnd = Horloge.verifStatut(new Date(), sdf.parse(tsEnd));
        if (seStart == StatusEvent.FINI || seStart == StatusEvent.EXPIRE || seEnd == StatusEvent.FINI || seEnd == StatusEvent.EXPIRE) {
            Toast.makeText(getContext(), getResources().getString(R.string.date_in_futur), Toast.LENGTH_SHORT).show();
        } else {

            tsStart = tsStart.replaceAll("/", "-"); //convertion de date pour sql
            tsEnd = tsEnd.replaceAll("/", "-");

            MainCreationEventActivity.dateValidate = true;
            MainCreationEventActivity.currentEvent.setStartDate(tsStart);
            MainCreationEventActivity.currentEvent.setEndDate(tsEnd);
            if (MainCreationEventActivity.isAllValidate(getContext())){
                MainCreationEventActivity.createEvent(getContext());
            } else if (!MainCreationEventActivity.photoValidate) {
                goPhoto();
            }else if (!MainCreationEventActivity.nameValidate) {
                goName();
            } else if (!MainCreationEventActivity.addressValidate) {
                goAdress();
            }  else if (!MainCreationEventActivity.membersValidate) {
                goMembers();
            }
        }

    }

    private void goAdress() {
        MainCreationEventActivity.navController.navigate(R.id.action_dateEventFragment_to_adressEventFragment);
    }

    private void goMembers() {
        MainCreationEventActivity.navController.navigate(R.id.action_dateEventFragment_to_membersEventFragment);
    }

    private void goName() {
        MainCreationEventActivity.navController.navigate(R.id.action_dateEventFragment_to_nameEventFragment);
    }

    private void goPhoto() {
        MainCreationEventActivity.navController.navigate(R.id.action_dateEventFragment_to_photoEventFragment);
    }

    private String cutString(String s, int length, int start){
        if(length > s.length()){
            return null;
        }

        String temp = "";

        int i = 0;
        if(start != -1){
            for(i=start; i<length+start; i++){
                temp += s.charAt(i);
            }
        }else{
            for(i=0; i<length; i++){
                temp += s.charAt(i);
            }
        }
        return temp;

    }
}