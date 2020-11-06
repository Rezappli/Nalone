package com.example.nalone;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nalone.Adapter.ItemAddPersonAdapter;
import com.example.nalone.Adapter.ItemProfilAdapter;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.ui.evenements.display.MesEvenementsListFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.nalone.util.Constants.EVENTS_DB_REF;
import static com.example.nalone.util.Constants.EVENTS_LIST;
import static com.example.nalone.util.Constants.USERS_LIST;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.heightScreen;
import static com.example.nalone.util.Constants.widthScreen;

public class CreateEventActivity extends AppCompatActivity {
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
    private Visibilite event_visibilite;
    private TextView event_date;

    private Dialog dialogCalendrier;
    private CalendarView calendarDate;
    private Button buttonCalendrier;

    private Dialog dialogTimePicker;
    private Calendar calendar;
    private String newDate;
    private TimePicker picker;
    private TextView event_horaire;

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
    private Button validLocationButton;

    private boolean locationValid = false;

    public static boolean edit;
    private TextView titreCreateEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        cardViewPrivate = findViewById(R.id.cardViewPrivate);
        cardViewPublic = findViewById(R.id.cardViewPublic);
        imageButtonAddInvit = findViewById(R.id.buttonMoreInvit);
        textViewListe = findViewById(R.id.textViewListe);
        imageViewPublic = findViewById(R.id.imageViewPublic);
        imageViewPrivate = findViewById(R.id.imageViewPrivate);
        dialogAddPerson = new Dialog(this);
        dialogCalendrier = new Dialog(this);
        dialogTimePicker = new Dialog(this);
        mRecyclerViewAdd = findViewById(R.id.recyclerView1);

        event_adresse = findViewById(R.id.eventAdress);
        event_city = findViewById(R.id.eventCity);
        event_name = findViewById(R.id.eventName);
        event_resume = findViewById(R.id.eventResume);
        buttonValidEvent = findViewById(R.id.button);
        event_date = findViewById(R.id.eventDate);
        event_horaire = findViewById(R.id.eventHoraire);

        locationValidImageView = findViewById(R.id.validePositionImageView);

        mLayoutManagerAdd = new LinearLayoutManager(getBaseContext());
        mAdapterAdd = new ItemAddPersonAdapter(itemsAdd);

        if(edit){
            event_city.setText(MesEvenementsListFragment.cityEdit);
            event_name.setText(MesEvenementsListFragment.nameEvent);
            event_resume.setText(MesEvenementsListFragment.descEdit);
            event_horaire.setText(MesEvenementsListFragment.timeEdit);
            event_adresse.setText(MesEvenementsListFragment.adresseEdit);
            if(MesEvenementsListFragment.visibiliteEdit == Visibilite.PUBLIC){
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
                Log.w("Event", "Click sur le bouton");
                showPopUp(v);
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
    }

    private void selectPublic() {
        imageButtonAddInvit.setVisibility(View.GONE);
        textViewListe.setVisibility(View.GONE);
        mRecyclerViewAdd.setVisibility(View.GONE);
        imageViewPublic.setImageResource(R.drawable.ic_baseline_public_focused);
        imageViewPrivate.setImageResource(R.drawable.ic_baseline_lock_24);
        event_visibilite = Visibilite.PUBLIC;
    }

    private void selectPrivate() {
        imageButtonAddInvit.setVisibility(View.VISIBLE);
        textViewListe.setVisibility(View.VISIBLE);
        imageViewPrivate.setImageResource(R.drawable.ic_baseline_lock_focused);
        imageViewPublic.setImageResource(R.drawable.ic_baseline_public_24);
        event_visibilite = Visibilite.PRIVE;
    }

    public void updateItems(){
        for(int i = 0; i < USERS_LIST.get(USER_ID).getAmis().size(); i++){
            User u = USERS_LIST.get(USERS_LIST.get(USER_ID).getAmis().get(i));
            Log.w("Event", "Ajout de : " + u.getPrenom() + " dans la liste");
            ItemPerson it = new ItemPerson(i,R.drawable.ic_baseline_account_circle_24, u.getPrenom()+" "+u.getNom(), R.drawable.ic_baseline_add_24, u.getDescription(), u.getVille(),u.getCursus(), u.getNbCreation(), u.getNbParticipation(), u.getCentreInterets());

            boolean duplicate = false;

            for(int j = 0; j < itemsAdd.size(); i++){
                if(itemsAdd.get(j).getId() == it.getId()){
                    duplicate = true;
                    break;
                }
            }

            if(!duplicate){
                items.add(it);
            }
        }
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
            Toast.makeText(this, "Localisation de l'évènement non validée !", Toast.LENGTH_SHORT).show();
        }

        if(!event_name.getText().toString().matches("") && !event_adresse.getText().toString().matches("") &&
        !event_city.getText().toString().matches("") && !event_date.getText().toString().matches("") &&
        !event_horaire.getText().toString().matches("") && locationValid){
            List<String> sign_in_members = new ArrayList<>();
            sign_in_members.add(USER_ID);

            Evenement e = new Evenement(R.drawable.ic_baseline_account_circle_24, EVENTS_LIST.size(), event_name.getText().toString(), event_resume.getText().toString(),
                    event_adresse.getText().toString(), event_city.getText().toString(), event_visibilite, USER_ID, sign_in_members,
                    itemsAdd, calendar.getTime(), mHour + ":" + mMin);
            EVENTS_LIST.put(EVENTS_LIST.size() + "", e);

            EVENTS_DB_REF.setValue(EVENTS_LIST);

            Toast.makeText(getBaseContext(), "Vous avez créer votre évènement !", Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    public void showTimePicker(View v){
        dialogTimePicker.setContentView(R.layout.time_picker);
        picker = dialogTimePicker.findViewById(R.id.timePicker);

        calendar = Calendar.getInstance();

        picker.setIs24HourView(true);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        dialogTimePicker.show();


    }

    public void setTime(View view) {
        int hour = picker.getCurrentHour();
        int min = picker.getCurrentMinute();
        showTime(hour, min);
        dialogTimePicker.dismiss();

    }

    private void showTime(int hour, int min) {
        if(hour < 10){
            mHour = "0"+hour;
        }else{
            mHour = ""+hour;
        }

        if(min < 10){
            mMin = "0"+min;
        }else{
            mMin = ""+min;
        }
        event_horaire.setText(new StringBuilder().append(mHour).append(" : ").append(mMin));

    }

    public  void showCalendrier(View v){
        dialogCalendrier.setContentView(R.layout.calendrier);
        calendarDate = dialogCalendrier.findViewById(R.id.calendarView);
        buttonCalendrier = dialogCalendrier.findViewById(R.id.buttonCalendrier);

        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");

        String curDate = sdf.format(date.getTime());

        Log.d("CUR_DATE", curDate);

        CalendarView.OnDateChangeListener myCalendarListener = new CalendarView.OnDateChangeListener() {

            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {

                // add one because month starts at 0
                month = month + 1;
                // output to log cat **not sure how to format year to two places here**
                newDate = day + "-" + month + "-" + year;
                Log.d("NEW_DATE", newDate);
            }
        };

        calendarDate.setOnDateChangeListener(myCalendarListener);


        buttonCalendrier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event_date.setText(newDate);
                dialogCalendrier.dismiss();
            }
        });



        dialogCalendrier.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogCalendrier.show();
    }


    public void showPopUp(View v){
        items.clear();
        adds.clear();
        updateItems();

        dialogAddPerson.setContentView(R.layout.popup_add_invit);
        dialogAddPerson.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAddPerson.getWindow().setLayout(widthScreen, heightScreen);

        RecyclerView mRecyclerView = dialogAddPerson.findViewById(R.id.recyclerView);
        final ItemProfilAdapter mAdapter = new ItemProfilAdapter(items);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ItemProfilAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                ItemPerson person = items.get(position);
                int remove = R.drawable.ic_baseline_remove_24;
                int add = R.drawable.ic_baseline_add_24;
                if(adds.contains(person) == false){
                    items.get(position).changerPlus(remove);
                    adds.add(person);
                }else{
                    items.get(position).changerPlus(add);
                    adds.remove(person);
                }

                mAdapter.notifyItemChanged(position);

            }
        });



        mLayoutManagerAdd = new LinearLayoutManager(getBaseContext());
        mAdapterAdd = new ItemAddPersonAdapter(itemsAdd);

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
                mLayoutManagerAdd = new LinearLayoutManager(getBaseContext());
                mAdapterAdd = new ItemAddPersonAdapter(itemsAdd);

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
            Toast.makeText(getBaseContext(), "Aucun autre amis à ajouter !", Toast.LENGTH_SHORT).show();
        }

    }

    private LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
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