package com.example.nalone.ui.evenements.creation;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.nalone.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CostEventFragment extends Fragment {
    private CardView cardViewPaying;
    private CardView cardViewFree;
    private ImageView imageViewPaying, imageViewFree;
    private boolean isTaping, isFree;
    private TextInputEditText textInputPrice;
    private TextInputLayout textInputLayoutPrice;


    public CostEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_cost_event, container, false);
        cardViewFree = root.findViewById(R.id.cardViewFree);
        cardViewPaying = root.findViewById(R.id.cardViewPaying);
        imageViewPaying = root.findViewById(R.id.imageViewPaying);
        imageViewFree = root.findViewById(R.id.imageViewFree);
        textInputPrice = root.findViewById(R.id.textInputPrice);
        textInputLayoutPrice = root.findViewById(R.id.textInputLayoutPrice);
        initialiserImageView(root);
        checkValidation();

        cardViewFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isTaping){
                    changeToFree();
                }
                isTaping = true;
                isFree = true;
            }
        });

        cardViewPaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isTaping){
                    changeToPaying();
                }
                isTaping = true;
                isFree = false;
            }
        });

        Button buttonNext = root.findViewById(R.id.buttonNextFragmentDate);

        final NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment2);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                validateCost();
            }
        });
        return root;
    }

    private void changeToPaying() {
        imageViewFree.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_money_off_24));
        imageViewPaying.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_attach_money_24_white));
        textInputLayoutPrice.setVisibility(View.VISIBLE);
    }

    private void changeToFree() {
        imageViewFree.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_money_off_24_white));
        imageViewPaying.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_attach_money_24));
        textInputLayoutPrice.setVisibility(View.INVISIBLE);
    }

    public static ImageView imageProgessCreationPhoto,imageProgessCreationDate,imageProgessCreationPosition,imageProgessCreationName,
            imageProgessCreationMembers, imageProgressCreationCost;

    private void initialiserImageView(View root) {
        imageProgessCreationDate = root.findViewById(R.id.imageProgessCreationDate);
        imageProgessCreationMembers = root.findViewById(R.id.imageProgessCreationMembers);
        imageProgessCreationName = root.findViewById(R.id.imageProgessCreationName);
        imageProgessCreationPosition = root.findViewById(R.id.imageProgessCreationPosition);
        imageProgessCreationPhoto = root.findViewById(R.id.imageProgessCreationPhoto);
        imageProgressCreationCost = root.findViewById(R.id.imageProgessCreationCost);
        imageProgessCreationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
goDate();            }
        });
        imageProgessCreationPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
goPhoto();            }
        });
        imageProgessCreationPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
goAdress();            }
        });
        imageProgessCreationMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
goMembers();            }
        });
        imageProgessCreationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goName();
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void checkValidation(){
        if (MainCreationEventActivity.addressValidate){
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
        if(MainCreationEventActivity.costValidate){
            imageProgressCreationCost.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_photo_focused));
            if(MainCreationEventActivity.currentEvent.getPrice() == 0){
                changeToFree();
            }else{
                changeToPaying();
                textInputPrice.setText(MainCreationEventActivity.currentEvent.getPrice()+"");
            }
        }
        if (MainCreationEventActivity.costValidate){
            imageProgressCreationCost.setImageDrawable(getResources().getDrawable(R.drawable.cost_event_focused));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void validateCost(){
        String value = textInputPrice.getText().toString();
        int finalValue=Integer.parseInt(value);
        if(!isTaping){
            imageViewPaying.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_attach_money_24_error));
            imageViewFree.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_money_off_24_error));
        }else{
            if(!isFree) {
                if (textInputPrice.getText().toString().matches("")) {
                    textInputPrice.setError(getResources().getString(R.string.required_field));
                }else if(finalValue <= 0){
                    textInputPrice.setError(getResources().getString(R.string.int_over_zero));
                }else{
                    MainCreationEventActivity.currentEvent.setPrice(finalValue);
                    subValidate();
                }
            }else{
                MainCreationEventActivity.currentEvent.setPrice(0);
                subValidate();
            }

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void subValidate(){
        MainCreationEventActivity.costValidate = true;
        if(MainCreationEventActivity.isAllValidate(getContext())){
            MainCreationEventActivity.createEvent(getContext());
        }else if(!MainCreationEventActivity.photoValidate){
            goPhoto();
        }else if(!MainCreationEventActivity.dateValidate){
            goDate();
        }else if(!MainCreationEventActivity.addressValidate) {
            goAdress();
        }else if(!MainCreationEventActivity.membersValidate){
            goMembers();
        }
    }

    private void goDate(){
        MainCreationEventActivity.navController.navigate(R.id.action_costEventFragment_to_dateEventFragment);
    }
    private void goAdress(){
        MainCreationEventActivity.navController.navigate(R.id.action_costEventFragment_to_adressEventFragment);
    }
    private void goMembers(){
        MainCreationEventActivity.navController.navigate(R.id.action_costEventFragment_to_membersEventFragment);
    }
    private void goPhoto(){
        MainCreationEventActivity.navController.navigate(R.id.action_costEventFragment_to_photoEventFragment);
    }
    private void goName(){
        MainCreationEventActivity.navController.navigate(R.id.action_costEventFragment_to_nameEventFragment);
    }

}