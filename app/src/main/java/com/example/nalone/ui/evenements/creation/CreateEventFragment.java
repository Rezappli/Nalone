package com.example.nalone.ui.evenements.creation;

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
import com.example.nalone.objects.Notification;
import com.example.nalone.util.Cache;
import com.example.nalone.objects.Evenement;
import com.example.nalone.util.Horloge;
import com.example.nalone.objects.ModelDataEvent;
import com.example.nalone.enumeration.StatusEvent;
import com.example.nalone.dialog.ListAmisFragment;
import com.example.nalone.R;
import com.example.nalone.dialog.SelectDateFragment;
import com.example.nalone.dialog.TimePickerFragment;
import com.example.nalone.objects.User;
import com.example.nalone.enumeration.Visibility;
import com.example.nalone.ui.evenements.display.MesEvenementsListFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
//import com.koalap.geofirestore.GeoLocation;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.dateFormat;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.setUserImage;
import static com.example.nalone.util.Constants.timeFormat;

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

    public static boolean edit = false;

    private NavController navController;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView mRecyclerView;

    public static Evenement evenementAttente;

    public static Evenement EVENT_LOAD;

    private View rootView;
    private boolean haveFriends;
    private StatusEvent se;

   /* public class EventAttente extends Evenement {

        public EventAttente(String uid,StatusEvent se, String owner, int image, String name, String description, String address, String city,
                            Visibility visibility, DocumentReference ownerDoc, Timestamp startDate,Timestamp endDate){
            super(uid,se,  owner,  image,  name,  description,  address,  city,
                    visibility,  ownerDoc,  startDate, endDate, null, 0);
        }
    }*/

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_create_event, container, false);

        createFragment();

        return rootView;
    }

    private void createFragment(){

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
                //navController.navigate(R.id.action_navigation_create_event_to_navigation_evenements);
            }
        });

        if(evenementAttente== null){
            Log.w("group", "Creation groupe vide");
           // evenementAttente  = new EventAttente(UUID.randomUUID().toString(), StatusEvent.BIENTOT,USER.getFirst_name() + " " + USER.getLast_name(), 0, "", "", "","",null,USER_REFERENCE,null, null);
        }

        if(adds != null){
            initList();
        }



        if(edit){
            event_city.setText(EVENT_LOAD.getCity());
            event_name.setText(EVENT_LOAD.getName());
            event_resume.setText(EVENT_LOAD.getDescription());
            //event_date.setText((dateFormat.format(EVENT_LOAD.getStartDate().toDate())));
            //event_horaire.setText((timeFormat.format(EVENT_LOAD.getStartDate().toDate())));
            event_adresse.setText(EVENT_LOAD.getAddress());
            //event_date.setText(MesEvenementsListFragment.dateEdit);

            mStoreBase.collection("events").document(EVENT_LOAD.getUid()).collection("members").get().
                    addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    adds.add(document.getId());
                                }
                            }
                        }
                    });
            Query query = mStoreBase.collection("events").document(EVENT_LOAD.getUid()).collection("members");
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
                            mStoreBase.collection("events").document(EVENT_LOAD.getUid()).collection("members").document(u.getUid()).delete();
                            createFragment();
                        }
                    });


                        setUserImage(u,getContext(),personViewHolder.imagePerson);

                }

            };

            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setAdapter(adapter);
            adapter.startListening();

            Log.w("Add", "Set adapter");


            if(MesEvenementsListFragment.visibiliteEdit == Visibility.PUBLIC){
                selectPublic();
            }else{
                selectPrivate();
            }
            //edit = false;
        }else{
            getData();
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
                mStoreBase.collection("users").document(USER_ID).collection("friends").whereEqualTo("status", "add")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                haveFriends = true;
                                Log.w("event", " Ajout list");
                            }

                        }
                        if(haveFriends){
                            refreshData();
                            ListAmisFragment.type = "event";
                            //navController.navigate(R.id.action_navigation_create_event_to_navigation_list_amis);
                            ListAmisFragment.EVENT_LOAD = EVENT_LOAD;
                        }else
                            Toast.makeText(getContext(), "Vous n'avez pas d'ami", Toast.LENGTH_SHORT).show();

                    }
                });

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
                        if(edit)
                            setEvent();
                        else
                            saveEvent();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    event_adresse.setError("Adresse introuvable");
                }

            }
        });

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
        if(evenementAttente.getStartDate() != null){
            //event_date.setText((dateFormat.format(EVENT_LOAD.getStartDate().toDate())));
            //event_horaire.setText((timeFormat.format(EVENT_LOAD.getStartDate().toDate())));
        }
        if(evenementAttente.getVisibility() == Visibility.PUBLIC){
            selectPublic();
        }
        if(evenementAttente.getVisibility() == Visibility.PRIVATE){
            selectPrivate();
        }
    }


    public void initList(){
        Query query;
        if(!adds.isEmpty()) {
            query = mStoreBase.collection("users").whereIn("uid", adds);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        User u = doc.toObject(User.class);
                        Log.w("Add", u.getFirst_name());
                    }
                }
            });
        }else{
            query = mStoreBase.collection("usejkhdskjfhkjhrjdhfks");
        }

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
                        createFragment();
                    }
                });

                setUserImage(u,getContext(),personViewHolder.imagePerson);

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
        private CardView cardView;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomInvit);
            villePers = itemView.findViewById(R.id.villePers);
            button = itemView.findViewById(R.id.buttonImage);
            imagePerson = itemView.findViewById(R.id.imagePerson);
            cardView = itemView.findViewById(R.id.cardViewProfil);
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

    private void setEvent() throws ParseException {
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


               /* final Evenement e = new Evenement(EVENT_LOAD.getUid(), EVENT_LOAD.getStatusEvent(), USER.getFirst_name() + " " + USER.getLast_name(),
                        R.drawable.ic_baseline_account_circle_24, EVENT_LOAD.getName(), EVENT_LOAD.getDescription(),
                        event_adresse.getText().toString(), EVENT_LOAD.getCity(), EVENT_LOAD.getVisibility(),
                        USER_REFERENCE, new Timestamp(sdf.parse(event_date.getText().toString()+" "+final_event_time))
                        , new Timestamp(sdf.parse(event_date.getText().toString()+" "+final_event_time)),new GeoPoint(l.latitude, l.longitude), adds.size());

                mStoreBase.collection("events").document(e.getUid()).set(e);
                ModelDataEvent m1 = new ModelDataEvent(true,"add", mStoreBase.collection("users").document(USER_ID));
                mStoreBase.collection("users").document(USER_ID).collection("events_create").document(e.getUid()).set(e);
*/
                for (String user : adds) {
                    String status = "received";
                   // ModelDataEvent m = new ModelDataEvent(false,status, mStoreBase.collection("users").document(user));
                   // mStoreBase.collection("events").document(e.getUid()).collection("members").document(user).set(m);
                  //  mStoreBase.collection("users").document(user).collection("events_received").document(e.getUid()).set(e);
                }
                Toast.makeText(getContext(), "Vous avez créer votre évènement !", Toast.LENGTH_SHORT).show();
                //navController.navigate(R.id.action_navigation_create_event_to_navigation_evenements);
            }else{
                event_adresse.setError("Adresse non trouvée");
                event_city.setError("Adresse non trouvée");
            }
        }
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


                Timestamp ts = new Timestamp(sdf.parse(event_date.getText().toString()+" "+final_event_time));
                se = Horloge.verifStatut(new Date(),ts.toDate());
                if(se == StatusEvent.FINI || se == StatusEvent.EXPIRE){
                    Toast.makeText(getContext(), "Veuillez créer un évènement dans le futur", Toast.LENGTH_SHORT).show();
                }else{
               /*     final Evenement e = new Evenement(evenementAttente.getUid(),se,USER.getFirst_name() + " " + USER.getLast_name(),
                            R.drawable.ic_baseline_account_circle_24, evenementAttente.getName(), evenementAttente.getDescription(),
                            evenementAttente.getAddress(), evenementAttente.getCity(), evenementAttente.getVisibility(),
                            USER_REFERENCE, new Timestamp(sdf.parse(event_date.getText().toString()+" "+final_event_time))
                            , new Timestamp(sdf.parse(event_date.getText().toString()+" "+final_event_time)), new GeoPoint(l.latitude, l.longitude), adds.size());
*/
                  //  mStoreBase.collection("events").document(e.getUid()).set(e);

                    ModelDataEvent m1 = new ModelDataEvent(true,"add", mStoreBase.collection("users").document(USER_ID));
                 //   mStoreBase.collection("users").document(USER_ID).collection("events_create").document(e.getUid()).set(e);

                    for (String user : adds) {
                        /*String status = "received";
                        ModelDataEvent m = new ModelDataEvent(false,status, mStoreBase.collection("users").document(user));
                        mStoreBase.collection("events").document(e.getUid()).collection("members").document(user).set(m);
                        mStoreBase.collection("users").document(USER_ID).collection("events_create").document(e.getUid()).collection("members").document(user).set(m);
                        mStoreBase.collection("users").document(user).collection("events_received").document(e.getUid()).set(e);*/
                  //      mStoreBase.collection("users").document(user).collection("events_received").document(e.getUid()).set(e);
                        mStoreBase.collection("users").document(user).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            //Notification.createNotif(task.getResult().toObject(User.class),Notification.demandeEvent());
                                        }
                                    }
                                });

                    }

                    Toast.makeText(getContext(), "Vous avez créer votre évènement !", Toast.LENGTH_SHORT).show();
                    //navController.navigate(R.id.action_navigation_create_event_to_navigation_evenements);
                }

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
        if(adapter != null)
            adapter.stopListening();
        edit = false;
        EVENT_LOAD = null;
    }

    @Override
    public void onResume(){
        super.onResume();
    }
}