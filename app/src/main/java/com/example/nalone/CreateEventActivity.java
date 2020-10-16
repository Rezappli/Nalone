package com.example.nalone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.nalone.Adapter.ItemAddPersonAdapter;
import com.example.nalone.Adapter.ItemPersonAdapter;
import com.example.nalone.util.Constants;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static com.example.nalone.util.Constants.firebaseDatabase;
import static com.example.nalone.util.Constants.user_mail;
import static com.example.nalone.util.Constants.user_id;

public class CreateEventActivity extends AppCompatActivity {
    private List<ItemPerson> itemsAdd = new ArrayList<>();
    private List<ItemPerson> tempsList = new ArrayList<>();
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

    private int id_events;
    private Dialog dialogAddPerson;
    private CardView cardViewPrivate;
    private ImageView imageViewPrivate;
    private ImageButton imageButtonAddInvit;
    private TextView textViewListe;

    private CardView cardViewPublic;
    private ImageView imageViewPublic;

    private Button buttonValidEvent;

    final List<ItemPerson> items = new ArrayList<>();

    private String mHour = "";
    private String mMin = "";

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

        mLayoutManagerAdd = new LinearLayoutManager(getBaseContext());
        mAdapterAdd = new ItemAddPersonAdapter(itemsAdd);

        cardViewPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonAddInvit.setVisibility(View.VISIBLE);
                textViewListe.setVisibility(View.VISIBLE);
                imageViewPrivate.setImageResource(R.drawable.ic_baseline_lock_focused);
                imageViewPublic.setImageResource(R.drawable.ic_baseline_public_24);
                event_visibilite = Visibilite.PRIVE;

            }
        });

        cardViewPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonAddInvit.setVisibility(View.GONE);
                textViewListe.setVisibility(View.GONE);
                mRecyclerViewAdd.setVisibility(View.GONE);
                imageViewPublic.setImageResource(R.drawable.ic_baseline_public_focused);
                imageViewPrivate.setImageResource(R.drawable.ic_baseline_lock_24);
                event_visibilite = Visibilite.PUBLIC;
            }
        });

        buttonValidEvent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                saveDataEvent();
            }
        });

        DatabaseReference id_events_ref = Constants.firebaseDatabase.getReference("id_evenements");
        id_events_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                id_events = Integer.parseInt(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addPerson(View v){
        dialogAddPerson.dismiss();
        mRecyclerViewAdd.setLayoutManager(mLayoutManagerAdd);
        mRecyclerViewAdd.setAdapter(mAdapterAdd);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void saveDataEvent(){
            List<String> sign_in_members = new ArrayList<>();
            sign_in_members.add(user_id);

            for(int k = 0; k < itemsAdd.size(); k++){
                final DatabaseReference notification = firebaseDatabase.getReference("notifications/"+itemsAdd.get(k).getId());
                notification.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        /*List<NotificationData> listNotification = snapshot.getValue(List.class);

                        if(listNotification == null){
                            listNotification = new ArrayList<>();
                        }

                        if(event_visibilite.equals(Visibilite.PRIVE)){
                            listNotification.add(new NotificationData("Ajout d'un évènement privé", "Un évènement privé vient d'être crée et vous êtes invité ! "));
                        }else{
                            listNotification.add(new NotificationData("Ajout d'un évènement publique", "Un évènement public vient d'être crée ! "));
                        }
                        notification.setValue(listNotification);*/
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            Evenement e = new Evenement(id_events, event_name.getText().toString(), event_resume.getText().toString(),
                    event_adresse.getText().toString(), event_city.getText().toString(), event_visibilite, user_id, sign_in_members,
                    itemsAdd, calendar.getTime(), mHour+":"+mMin);
            DatabaseReference events = Constants.firebaseDatabase.getReference("evenements/" + id_events);
            events.setValue(e);

            DatabaseReference event_id = Constants.firebaseDatabase.getReference("id_evenements");
            id_events++;

            event_id.setValue(id_events + "");

            finish();
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
        tempsList.clear();

        dialogAddPerson.setContentView(R.layout.popup_add_invit);
        dialogAddPerson.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final RecyclerView[] mRecyclerView = new RecyclerView[1];
        final ItemPersonAdapter[] mAdapter = new ItemPersonAdapter[1];
        final RecyclerView.LayoutManager[] mLayoutManager = new RecyclerView.LayoutManager[1];

        final ArrayList<String> adds = new ArrayList<>();

        DatabaseReference id_users_ref = Constants.firebaseDatabase.getReference("id_users");
        id_users_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String id_user = snapshot.getValue(String.class);
                final int nb_users = Integer.parseInt(id_user);
                for(final int[] i = {0}; i[0] < nb_users; i[0]++){
                    DatabaseReference user = Constants.firebaseDatabase.getReference("users/"+ i[0]);
                    final int finalI = i[0];
                    user.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String mail = snapshot.child("mail").getValue(String.class);

                                if(mail.equalsIgnoreCase(user_mail)){
                                    int id_user_connect = finalI;
                                    String amis_text = snapshot.child("amis").getValue(String.class);
                                    final List<String> liste_amis = Arrays.asList(amis_text.split(","));

                                    Log.w("Liste", "Liste amis:"+amis_text);
                                    items.clear();
                                    for(final int[] j = {0}; j[0] < nb_users; j[0]++){
                                        Log.w("Liste", "J :"+ j[0]);
                                        if(liste_amis.contains(j[0] +"")){
                                            DatabaseReference user_found = Constants.firebaseDatabase.getReference("users/"+ j[0]);
                                            final int finalJ = j[0];
                                            user_found.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    String prenom = snapshot.child("prenom").getValue(String.class);
                                                    String nom = snapshot.child("nom").getValue(String.class);
                                                    String desc = snapshot.child("description").getValue(String.class);
                                                    String nbCreate = snapshot.child("nombre_creation").getValue(String.class);
                                                    String nbParticipate = snapshot.child("nombre_participation").getValue(String.class);

                                                    mRecyclerView[0] = dialogAddPerson.findViewById(R.id.recyclerView);
                                                    mLayoutManager[0] = new LinearLayoutManager(getBaseContext());

                                                    mRecyclerView[0].setLayoutManager(mLayoutManager[0]);
                                                    mRecyclerView[0].setAdapter(mAdapter[0]);

                                                    ItemPerson itemPerson = new ItemPerson(finalJ,R.drawable.ic_baseline_account_circle_24, prenom+" "+nom, R.drawable.ic_baseline_add_24, desc, nbCreate, nbParticipate);
                                                    boolean found = false;
                                                    for(int k = 0; k < itemsAdd.size(); k++){
                                                        if(itemPerson.getId() == itemsAdd.get(k).getId()){
                                                            found = true;
                                                        }
                                                    }

                                                    if(!found){
                                                        items.add(itemPerson);
                                                    }

                                                    dialogAddPerson.show();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }

                                    mAdapter[0] = new ItemPersonAdapter(items);

                                    mAdapter[0].setOnItemClickListener(new ItemPersonAdapter.OnItemClickListener() {
                                        @Override
                                        public void onAddClick(int position) {
                                            String person = items.get(position).getNom();
                                            int remove = R.drawable.ic_baseline_remove_24;
                                            int add = R.drawable.ic_baseline_add_24;
                                            if(adds.contains(person) == false){
                                                items.get(position).changerPlus(remove);
                                                adds.add(person);
                                                tempsList.add(items.get(position));
                                            }else{
                                                items.get(position).changerPlus(add);
                                                adds.remove(person);
                                                tempsList.remove(items.get(position));
                                            }

                                            mAdapter[0].notifyItemChanged(position);

                                        }
                                    });

                                    Button valid = dialogAddPerson.findViewById(R.id.buttonBack);
                                    valid.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            for(int i = 0; i < tempsList.size(); i++){
                                                itemsAdd.add(tempsList.get(i));
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
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}