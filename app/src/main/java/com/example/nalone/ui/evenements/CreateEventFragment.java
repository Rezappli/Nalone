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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nalone.Cache;
import com.example.nalone.Evenement;
import com.example.nalone.dialog.ListAmisFragment;
import com.example.nalone.R;
import com.example.nalone.dialog.SelectDateFragment;
import com.example.nalone.dialog.TimePickerFragment;
import com.example.nalone.User;
import com.example.nalone.Visibility;
import com.example.nalone.ui.evenements.display.MesEvenementsListFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.koalap.geofirestore.GeoLocation;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.geoFirestore;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;

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

    private boolean locationValid = false;

    public static boolean edit;

    private NavController navController;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView mRecyclerView;

    public static Evenement evenementAttente;

    public class EventAttente extends Evenement {

        public EventAttente(String uid, String owner, int image, String name, String description, String address, String city,
                            Visibility visibility, DocumentReference ownerDoc, Timestamp date){
            super(uid,  owner,  image,  name,  description,  address,  city,
                     visibility,  ownerDoc,  date, null);
        }
    }

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

        if(evenementAttente== null){
            Log.w("group", "Creation groupe vide");
            evenementAttente  = new EventAttente(UUID.randomUUID().toString(), USER.getFirst_name() + " " + USER.getLast_name(), 0, "", "", "","",null,USER_REFERENCE,null);
        }

        if(adds != null){
            initList();
        }

        getData();

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
                refreshData();
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
                    try {
                        saveEvent();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    event_adresse.setError("Adresse introuvable");
                }

            }
        });

        return rootView;
    }

    private void refreshData() {
        evenementAttente.setName(event_name.getText().toString());
        evenementAttente.setDescription(event_resume.getText().toString());
        evenementAttente.setVisibility(event_visibilite);
        evenementAttente.setCity(event_city.getText().toString());
        evenementAttente.setAddress(event_adresse.getText().toString());
        //evenementAttente.setDate(event_date);
    }

    private void getData() {
        Log.w("Group après création", " Nom : "+ evenementAttente.getName());
        if(!evenementAttente.getName().matches(""))
            event_name.setText(evenementAttente.getName());
        if(!evenementAttente.getDescription().matches(""))
            event_resume.setText(evenementAttente.getDescription());
        if(evenementAttente.getVisibility() != null)
            event_visibilite = evenementAttente.getVisibility();
        if(!evenementAttente.getAddress().matches(""))
            event_adresse.setText(evenementAttente.getAddress());
        if(!evenementAttente.getCity().matches(""))
            event_city.setText(evenementAttente.getCity());
        if(evenementAttente.getDate() != null)
            event_date.setText(evenementAttente.getDate().toString());
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
                    if(!Cache.fileExists(u.getUid())) {
                        StorageReference imgRef = mStore.getReference("users/" + u.getUid());
                        if (imgRef != null) {
                            imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri img = task.getResult();
                                        if (img != null) {
                                            Cache.saveUriFile(u.getUid(), img);
                                            u.setImage_url(Cache.getImageDate(u.getUid()));
                                            mStoreBase.collection("users").document(u.getUid()).set(u);
                                            Glide.with(getContext()).load(img).fitCenter().centerCrop().into(personViewHolder.imagePerson);
                                        }
                                    }
                                }
                            });
                        }
                    }else{
                        Uri imgCache = Cache.getUriFromUid(u.getUid());
                        Log.w("Cache", "Image Cache : " + Cache.getImageDate(u.getUid()));
                        Log.w("Cache", "Data Cache : " + u.getImage_url());
                        if(Cache.getImageDate(u.getUid()).equalsIgnoreCase(u.getImage_url())) {
                            Log.w("image", "get image from cache");
                            Glide.with(getContext()).load(imgCache).fitCenter().centerCrop().into(personViewHolder.imagePerson);
                        }else{
                            StorageReference imgRef = mStore.getReference("users/" + u.getUid());
                            if (imgRef != null) {
                                imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            Uri img = task.getResult();
                                            if (img != null) {
                                                Cache.saveUriFile(u.getUid(), img);
                                                u.setImage_url(Cache.getImageDate(u.getUid()));
                                                mStoreBase.collection("users").document(u.getUid()).set(u);
                                                Glide.with(getContext()).load(img).fitCenter().centerCrop().into(personViewHolder.imagePerson);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }

        };

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
            button = itemView.findViewById(R.id.buttonImage);
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


    private void selectPrivate() {
        imageButtonAddInvit.setVisibility(View.VISIBLE);
        textViewListe.setVisibility(View.VISIBLE);
        imageViewPrivate.setImageResource(R.drawable.ic_baseline_lock_focused);
        imageViewPublic.setImageResource(R.drawable.ic_baseline_public_24);
        event_visibilite = Visibility.PRIVATE;
    }


    private void saveEvent() throws ParseException {

        if(event_name.getText().toString().matches("")){
            event_name.setError("Champs obligatoire");
        }

        if(event_adresse.getText().toString().matches("")){
            event_adresse.setError("Champs obligatoire");
        }

        if(event_city.getText().toString().matches("")){
            event_city.setError("Champs obligatoire");
        }

        if(event_date.getText().toString().equalsIgnoreCase("date")){
            event_date.setError("Champs obligatoire");
        }

        if(event_horaire.getText().toString().equalsIgnoreCase("horaire")){
            event_horaire.setError("Champs obligatoire");
        }

        if(!locationValid){
            Toast.makeText(getContext(), "Localisation de l'évènement non validée !", Toast.LENGTH_SHORT).show();
        }

        if(!event_name.getText().toString().matches("") && !event_adresse.getText().toString().matches("") &&
        !event_city.getText().toString().matches("") && !event_date.getText().toString().equalsIgnoreCase("date") &&
        !event_horaire.getText().toString().equalsIgnoreCase("horaire") && locationValid){
            refreshData();
            final LatLng l = getLocationFromAddress(evenementAttente.getAddress() + "," + evenementAttente.getCity());
            if(l != null) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");

                String new_event_time = event_horaire.getText().toString().replaceAll(" ", "");
                String final_event_time = "";
                for(char c : new_event_time.toCharArray()) {
                    if (c != ' ') {
                        final_event_time += c;
                    }
                }

                final Evenement e = new Evenement(evenementAttente.getUid(), USER.getFirst_name() + " " + USER.getLast_name(),
                        R.drawable.ic_baseline_account_circle_24, evenementAttente.getName(), evenementAttente.getDescription(),
                        evenementAttente.getAddress(), evenementAttente.getCity(), evenementAttente.getVisibility(),
                        USER_REFERENCE, new Timestamp(sdf.parse(event_date.getText().toString()+" "+final_event_time))
                        , new GeoPoint(l.latitude, l.longitude));

                mStoreBase.collection("events").document(e.getUid()).set(e);
                mStoreBase.collection("users").document(USER_ID).collection("events").document(e.getUid()).set(e);

                for (String user : adds) {
                    Log.d("Ajout", "Ajout de membre dans evenement");
                    mStoreBase.collection("events").document(e.getUid()).collection("members").document(user).set(e);
                }
                Toast.makeText(getContext(), "Vous avez créer votre évènement !", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.action_navigation_create_event_to_navigation_evenements);
            }else{
                event_adresse.setError("Adresse non trouvée");
                event_city.setError("Adresse non trouvée");
            }
        }
    }


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

    @Override
    public void onStop(){
        super.onStop();
        adapter.stopListening();
        adds.clear();
    }

    @Override
    public void onResume(){
        super.onResume();
    }
}