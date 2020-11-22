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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.example.nalone.Cache;
import com.example.nalone.CreateGroupFragment;
import com.example.nalone.Evenement;
import com.example.nalone.ListAmisFragment;
import com.example.nalone.R;
import com.example.nalone.SelectDateFragment;
import com.example.nalone.TimePickerFragment;
import com.example.nalone.User;
import com.example.nalone.Visibility;
import com.example.nalone.adapter.ItemAddPersonAdapter;
import com.example.nalone.adapter.ItemProfilAdapter;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.ui.evenements.display.MesEvenementsListFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.heightScreen;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.widthScreen;

public class CreateEventFragment extends Fragment {
    public static List<String> adds = new ArrayList<>();

    private TextInputEditText event_name;
    private TextInputEditText event_adresse;
    private TextInputEditText event_city;
    private TextInputEditText event_resume;
    private Visibility event_visibilite;
    public static TextView event_date;

    public static TextView event_horaire;

    private Dialog dialogAddPerson;
    private CardView cardViewPrivate;
    private ImageView imageViewPrivate;
    private ImageButton imageButtonAddInvit;
    private TextView textViewListe;

    private CardView cardViewPublic;
    private ImageView imageViewPublic;

    private Button buttonValidEvent;

    private ImageView locationValidImageView;

    private boolean locationValid = false;

    public static boolean edit;

    private NavController navController;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView mRecyclerView;

    public static String nom;
    public static String adresse;
    public static String ville;
    public static String description;
    public static String date;
    public static String horaire;
    public static Visibility visibility;

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
        mRecyclerView= rootView.findViewById(R.id.recyclerView1);

        event_adresse = rootView.findViewById(R.id.eventAdress);
        event_city = rootView.findViewById(R.id.eventCity);
        event_name = rootView.findViewById(R.id.eventName);
        event_resume = rootView.findViewById(R.id.eventResume);
        buttonValidEvent = rootView.findViewById(R.id.button);
        event_date = rootView.findViewById(R.id.eventDate);
        event_horaire = rootView.findViewById(R.id.eventHoraire);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        buttonBack.setVisibility(View.VISIBLE);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_create_event_to_navigation_evenements);
            }
        });

        if(adds != null){
            initList();
        }

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
                ListAmisFragment.type = "event";
                navController.navigate(R.id.action_navigation_create_event_to_navigation_list_amis);
            }
        });

        event_adresse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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
                newFragment.show(getActivity().getSupportFragmentManager(), "DIALOG_TIME");
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

    private void initWidjets() {
        event_name.setText(nom);
        event_adresse.setText(adresse);
        event_date.setText(date);
        event_horaire.setText(horaire);
        event_resume.setText(description);
        if(visibility == Visibility.PRIVATE){
            selectPrivate();
        }
        if(visibility == Visibility.PUBLIC){
            selectPublic();
        }
    }

    public void initList(){
        adds.add("a");
        Query query = mStoreBase.collection("users").whereIn("uid", adds);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc : task.getResult()){
                    User u = doc.toObject(User.class);
                    Log.w("Add", u.getFirst_name());
                }
            }
        });

        //RecyclerOption
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();

        adapter = new FirestoreRecyclerAdapter<User, PersonViewHolder>(options) {
            @NonNull
            @Override
            public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.w("Add", "ViewHolder");
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person,parent,false);
                return new PersonViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final PersonViewHolder personViewHolder, final int i, @NonNull final  User u) {

                Log.w("Add","BindViewHolder");
                personViewHolder.villePers.setText(u.getCity());
                personViewHolder.nomInvit.setText(u.getFirst_name() + " "+ u.getLast_name());
                personViewHolder.button.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_remove_24));
                personViewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adds.remove(u.getUid());
                        Log.w("Add", "List : " + adds.isEmpty());
                        adapter.notifyDataSetChanged();
                        initList();
                    }
                });
                if(u.getImage_url() != null) {
                    if (!Cache.fileExists(u.getUid())) {
                        StorageReference imgRef = mStore.getReference("users/" + u.getUid());
                        if (imgRef != null) {
                            imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri img = task.getResult();
                                        if (img != null) {
                                            Cache.saveUriFile(u.getUid(), img);
                                            Glide.with(getContext()).load(img).fitCenter().centerCrop().into(personViewHolder.imagePerson);
                                        }
                                    }
                                }
                            });
                        }
                    } else {
                        Glide.with(getContext()).load(Cache.getUriFromUid(u.getUid())).fitCenter().centerCrop().into(personViewHolder.imagePerson);
                    }

                }
            }

        };

        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
        adapter.startListening();

        Log.w("Add", "Set adapter");


    }

    private class PersonViewHolder extends RecyclerView.ViewHolder {

        private TextView nomInvit;
        private TextView villePers;
        private ImageView button, imagePerson;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomInvit);
            villePers = itemView.findViewById(R.id.villePers);
            button = itemView.findViewById(R.id.imageView19);
            imagePerson = itemView.findViewById(R.id.imagePerson);
        }
    }

    private void selectPublic() {
        imageButtonAddInvit.setVisibility(View.GONE);
        textViewListe.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        imageViewPublic.setImageResource(R.drawable.ic_baseline_public_focused);
        imageViewPrivate.setImageResource(R.drawable.ic_baseline_lock_24);
        event_visibilite = Visibility.PUBLIC;
    }

    public static void chargeInfos(String nomI, String adresseI, String villeI, String descriptionI, String dateI, String horaireI, Visibility visibilityI){
        nom = nomI;
        adresse = adresseI;
        ville = villeI;
        description = descriptionI;
        date = dateI;
        horaire = horaireI;
        visibility = visibilityI;

    }

    private void selectPrivate() {
        imageButtonAddInvit.setVisibility(View.VISIBLE);
        textViewListe.setVisibility(View.VISIBLE);
        imageViewPrivate.setImageResource(R.drawable.ic_baseline_lock_focused);
        imageViewPublic.setImageResource(R.drawable.ic_baseline_public_24);
        event_visibilite = Visibility.PRIVATE;
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

            LatLng l = getLocationFromAddress(event_adresse+","+event_adresse);
            Evenement e = new Evenement(UUID.randomUUID().toString(),USER.getFirst_name() + " " + USER.getLast_name(), R.drawable.ic_baseline_account_circle_24, event_name.getText().toString(), event_resume.getText().toString(),
                    event_adresse.getText().toString(), event_city.getText().toString(), event_visibilite, USER_REFERENCE, new Timestamp(null), new GeoPoint(l.latitude, l.longitude));

            mStoreBase.collection("events").document(e.getUid()).set(e);

            Toast.makeText(getContext(), "Vous avez créer votre évènement !", Toast.LENGTH_SHORT).show();

            navController.navigate(R.id.action_navigation_create_event_to_navigation_evenements);

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

    @Override
    public void onStop(){
        super.onStop();
        adapter.stopListening();
        adds.clear();
    }

    @Override
    public void onResume(){
        super.onResume();
        initWidjets();
    }
}