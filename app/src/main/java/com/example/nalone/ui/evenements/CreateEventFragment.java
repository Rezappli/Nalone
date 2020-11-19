package com.example.nalone.ui.evenements;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nalone.Evenement;
import com.example.nalone.R;
import com.example.nalone.SelectDateFragment;
import com.example.nalone.TimePickerFragment;
import com.example.nalone.Visibility;
import com.example.nalone.adapter.ItemAddPersonAdapter;
import com.example.nalone.adapter.ItemProfilAdapter;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.ui.evenements.display.MesEvenementsListFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.heightScreen;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.widthScreen;

public class CreateEventFragment extends Fragment {
    private List<ItemPerson> items = new ArrayList<>();
    private List<ItemPerson> itemsAdd = new ArrayList<>();
    private List<ItemPerson> adds = new ArrayList<>();
    private RecyclerView mRecyclerViewAdd;
    private ItemAddPersonAdapter mAdapterAdd;
    private RecyclerView.LayoutManager mLayoutManagerAdd;

    private TextInputEditText event_name;
    private TextInputEditText event_adresse;
    private TextInputEditText event_city;
    private TextInputEditText event_resume;
    private Visibility event_visibilite;
    public static TextView event_date;

    private Dialog dialogCalendrier;
    private CalendarView calendarDate;
    private Button buttonCalendrier;

    private Dialog dialogTimePicker;
    private Calendar calendar;

    private int newDay;
    private int newMonth;
    private int newYears;

    private TimePicker picker;
    public static TextView event_horaire;

    private Dialog dialogAddPerson;
    private CardView cardViewPrivate;
    private ImageView imageViewPrivate;
    private ImageButton imageButtonAddInvit;
    private TextView textViewListe;

    private CardView cardViewPublic;
    private ImageView imageViewPublic;

    private Button buttonValidEvent;

    private String mHour = "";
    private String mMin = "";

    private ImageView locationValidImageView;

    private boolean locationValid = false;

    public static boolean edit;

    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_create_event, container, false);

        cardViewPrivate = rootView.findViewById(R.id.cardViewPrivate);
        cardViewPublic = rootView.findViewById(R.id.cardViewPublic);
        imageButtonAddInvit = rootView.findViewById(R.id.buttonMoreInvit);
        textViewListe = rootView.findViewById(R.id.textViewListe);
        imageViewPublic = rootView.findViewById(R.id.imageViewPublic);
        imageViewPrivate = rootView.findViewById(R.id.imageViewPrivate);
        dialogAddPerson = new Dialog(getActivity());
        mRecyclerViewAdd = rootView.findViewById(R.id.recyclerView1);

        event_adresse = rootView.findViewById(R.id.eventAdress);
        event_city = rootView.findViewById(R.id.eventCity);
        event_name = rootView.findViewById(R.id.eventName);
        event_resume = rootView.findViewById(R.id.eventResume);
        buttonValidEvent = rootView.findViewById(R.id.button);
        event_date = rootView.findViewById(R.id.eventDate);
        event_horaire = rootView.findViewById(R.id.eventHoraire);

        mLayoutManagerAdd = new LinearLayoutManager(getContext());
        mAdapterAdd = new ItemAddPersonAdapter(itemsAdd, getContext());

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        if(edit){
            event_city.setText(MesEvenementsListFragment.cityEdit);
            event_name.setText(MesEvenementsListFragment.nameEvent);
            event_resume.setText(MesEvenementsListFragment.descEdit);
            event_horaire.setText(MesEvenementsListFragment.timeEdit);
            event_adresse.setText(MesEvenementsListFragment.adresseEdit);
            event_date.setText(MesEvenementsListFragment.dateEdit);

            if(MesEvenementsListFragment.visibiliteEdit == Visibility.PUBLIC){
                selectPublic();
            }else{
                selectPrivate();
            }
            edit = false;
        }

        cardViewPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPrivate();
            }
        });

        cardViewPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPublic();
            }
        });

        imageButtonAddInvit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if(USER.getFriends() == null){
                    Toast.makeText(CreateEventActivity.this, "Vous n'avez pas d'amis à ajouter", Toast.LENGTH_SHORT).show();
                }else{
                    showPopUp(v);
                }*/
            }
        });

        event_adresse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                locationValidImageView.setVisibility(View.GONE);
                locationValid = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        event_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                locationValidImageView.setVisibility(View.GONE);
                locationValid = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        event_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
            }
        });

        event_horaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                //newFragment.show(getActivity().getFragmentManager(), DIALOG_TIME);
                newFragment.show(getActivity().getSupportFragmentManager(), "DIALOG_TIME");
                // if you are using the nested fragment then user the
                //newFragment.show(getChildFragmentManager(), DIALOG_TIME);*/
            }
        });


        buttonValidEvent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(getLocationFromAddress(event_adresse.getText().toString()+","+event_city.getText().toString()) != null){
                    locationValid = true;
                    saveEvent();
                }else{
                    event_adresse.setError("Adresse introuvable");
                }

            }
        });

        return rootView;
    }

    private void selectPublic() {
        imageButtonAddInvit.setVisibility(View.GONE);
        textViewListe.setVisibility(View.GONE);
        mRecyclerViewAdd.setVisibility(View.GONE);
        imageViewPublic.setImageResource(R.drawable.ic_baseline_public_focused);
        imageViewPrivate.setImageResource(R.drawable.ic_baseline_lock_24);
        event_visibilite = Visibility.PUBLIC;
    }

    private void selectPrivate() {
        imageButtonAddInvit.setVisibility(View.VISIBLE);
        textViewListe.setVisibility(View.VISIBLE);
        imageViewPrivate.setImageResource(R.drawable.ic_baseline_lock_focused);
        imageViewPublic.setImageResource(R.drawable.ic_baseline_public_24);
        event_visibilite = Visibility.PRIVATE;
    }

    public void updateItems(){
        /*for(int i=0; i < USER.get_friends().size(); i++){
            getUserData(USER.get_friends().get(i).getId(), new FireStoreUsersListeners() {
                @Override
                public void onDataUpdate(User u) {
                    ItemPerson it = new ItemPerson(u.getUid(),R.drawable.ic_baseline_account_circle_24, u.getFirst_name()+" "+u.getLast_name(), R.drawable.ic_baseline_add_24, u.getDescription(), u.getCity(),u.getCursus(), u.get_number_events_create(), u.get_number_events_attend(), u.getCenters_interests());

                    boolean duplicate = false;

                    for(int j = 0; j < itemsAdd.size(); j++){
                        if(itemsAdd.get(j).getUid().equalsIgnoreCase(it.getUid())){
                            duplicate = true;
                            break;
                        }
                    }

                    if(!duplicate){
                        items.add(it);
                    }
                }
            });


        }*/
    }

    public void addPerson(View v){
        dialogAddPerson.dismiss();
        mRecyclerViewAdd.setLayoutManager(mLayoutManagerAdd);
        mRecyclerViewAdd.setAdapter(mAdapterAdd);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void saveEvent(){

        if(event_name.getText().toString().matches("")){
            event_name.setError("Champs obligatoire");
        }

        if(event_adresse.getText().toString().matches("")){
            event_adresse.setError("Champs obligatoire");
        }

        if(event_city.getText().toString().matches("")){
            event_city.setError("Champs obligatoire");
        }

        if(event_date.getText().toString().matches("")){
            event_date.setError("Champs obligatoire");
        }

        if(event_horaire.getText().toString().matches("")){
            event_horaire.setError("Champs obligatoire");
        }

        if(!locationValid){
            Toast.makeText(getContext(), "Localisation de l'évènement non validée !", Toast.LENGTH_SHORT).show();
        }

        if(!event_name.getText().toString().matches("") && !event_adresse.getText().toString().matches("") &&
        !event_city.getText().toString().matches("") && !event_date.getText().toString().matches("") &&
        !event_horaire.getText().toString().matches("") && locationValid){

            calendar.set(newYears, newMonth, newDay);
            LatLng l = getLocationFromAddress(event_adresse+","+event_adresse);
            Evenement e = new Evenement(UUID.randomUUID().toString(),USER.getFirst_name() + " " + USER.getLast_name(), R.drawable.ic_baseline_account_circle_24, event_name.getText().toString(), event_resume.getText().toString(),
                    event_adresse.getText().toString(), event_city.getText().toString(), event_visibilite, USER_REFERENCE, new Timestamp(calendar.getTime()), new GeoPoint(l.latitude, l.longitude));

            mStoreBase.collection("events").document(e.getUid()).set(e);

            Toast.makeText(getContext(), "Vous avez créer votre évènement !", Toast.LENGTH_SHORT).show();

            navController.navigate(R.id.action_navigation_create_event_to_navigation_evenements);

        }
    }

    public void showPopUp(View v){
        items.clear();
        adds.clear();
        updateItems();

        dialogAddPerson.setContentView(R.layout.popup_add_invit);
        dialogAddPerson.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAddPerson.getWindow().setLayout(widthScreen, heightScreen);

        RecyclerView mRecyclerView = dialogAddPerson.findViewById(R.id.recyclerView);
        final ItemProfilAdapter mAdapter = new ItemProfilAdapter(items, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ItemProfilAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                ItemPerson person = items.get(position);
                int remove = R.drawable.ic_baseline_remove_24;
                int add = R.drawable.ic_baseline_add_24;
                if(!adds.contains(person)){
                    items.get(position).changerPlus(remove);
                    adds.add(person);
                }else{
                    items.get(position).changerPlus(add);
                    adds.remove(person);
                }

                mAdapter.notifyItemChanged(position);

            }
        });



        mLayoutManagerAdd = new LinearLayoutManager(getContext());
        mAdapterAdd = new ItemAddPersonAdapter(itemsAdd, getContext());

        mAdapterAdd.setOnItemClickListener(new ItemAddPersonAdapter.OnItemClickListener() {
            @Override
            public void onRemoveClick(int position) {
                itemsAdd.remove(position);
                mAdapterAdd.notifyItemRemoved(position);
            }
        });

        Button valid = dialogAddPerson.findViewById(R.id.buttonBack);
        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < adds.size(); i++){
                    itemsAdd.add(adds.get(i));
                }
                dialogAddPerson.dismiss();
                mLayoutManagerAdd = new LinearLayoutManager(getContext());
                mAdapterAdd = new ItemAddPersonAdapter(itemsAdd, getContext());

                mAdapterAdd.setOnItemClickListener(new ItemAddPersonAdapter.OnItemClickListener() {
                    @Override
                    public void onRemoveClick(int position) {
                        itemsAdd.remove(position);
                        mAdapterAdd.notifyItemRemoved(position);
                    }
                });

                mRecyclerViewAdd.setLayoutManager(mLayoutManagerAdd);
                mRecyclerViewAdd.setAdapter(mAdapterAdd);
            }
        });

        if(items.size() > 0) {
            dialogAddPerson.show();
        }else{
            Toast.makeText(getContext(), "Aucun autre amis à ajouter !", Toast.LENGTH_SHORT).show();
        }

    }

    private LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
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