package com.example.nalone.ui.evenements;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nalone.objects.Evenement;
import com.example.nalone.objects.ModelDataEvent;
import com.example.nalone.R;
import com.example.nalone.enumeration.StatusEvent;
import com.example.nalone.objects.User;
import com.example.nalone.ui.evenements.creation.CreateEventFragment;
import com.example.nalone.util.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.mStoreBase;

public class InfosEvenementsActivity extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mTitle;
    private TextView mDate;
    private TextView mTimer;
    private TextView mOwner;
    private TextView mDescription;
    private FirestoreRecyclerAdapter adapter;
    private List<String> members = new ArrayList<>();

    public static Evenement EVENT_LOAD;

    public static String type;
    private View rootView;
    private NavController navController;
    private TextView textViewNbMembers;
    private TextView nbParticipants;
    private int participants;
    private ImageView buttonInscription, ownerImage;
    private TextView textViewInscription;
    private ImageView buttonPartager, buttonAnnuler;
    private TextView diffDate;
    private Handler handler = new Handler();
    private boolean inscrit;
    private TextView textViewPartager;
    private TextView textViewTitleDebut;
    private LinearLayout linearButton;
    private CardView cardViewTermine;
    private LinearLayout linearAnnuler;
    private ImageView imageEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_infos_evenements, container, false);
        createFragment();
        return rootView;
    }

    private void createFragment() {

        participants = 0;
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        mTitle = rootView.findViewById(R.id.title);
        mDate = rootView.findViewById(R.id.date);
        mTimer = rootView.findViewById(R.id.time);
        mOwner = rootView.findViewById(R.id.owner);
        mDescription = rootView.findViewById(R.id.description);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewMembresInscrits);
        textViewNbMembers = rootView.findViewById(R.id.textViewNbMembers);
        nbParticipants = rootView.findViewById(R.id.nbParticipants);
        buttonInscription = rootView.findViewById(R.id.buttonInscription);
        textViewInscription = rootView.findViewById(R.id.textViewParticiper);
        textViewPartager = rootView.findViewById(R.id.textViewPartager);
        textViewTitleDebut = rootView.findViewById(R.id.textViewTitleDebut);
        linearButton = rootView.findViewById(R.id.linearButton);
        cardViewTermine = rootView.findViewById(R.id.cardViewTermine);
        linearAnnuler = rootView.findViewById(R.id.linearAnnuler);
        buttonAnnuler = rootView.findViewById(R.id.buttonAnnuler);
        ownerImage = rootView.findViewById(R.id.ownerImage);
        imageEvent = rootView.findViewById(R.id.imageEvenement);

        buttonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incription();
            }
        });
        diffDate = rootView.findViewById(R.id.differenceDate);

        buttonPartager = rootView.findViewById(R.id.buttonPartager);
        buttonPartager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Clicked on Share " , Toast.LENGTH_SHORT).show();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        buttonAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suppression();
            }
        });

        //String date_text = formatD.format(EVENT_LOAD.getStartDate().toDate());
        String final_date_text = "";
        mTitle.setText(EVENT_LOAD.getName());
        //mTimer.setText(timeFormat.format(EVENT_LOAD.getStartDate().toDate()));
        mOwner.setText(EVENT_LOAD.getOwner_uid());

        if(EVENT_LOAD.getDescription().matches("")){
            mDescription.setVisibility(View.GONE);
        }else{
            mDescription.setText(EVENT_LOAD.getDescription());
        }

        handler = new Handler();
        handler.postDelayed(runnable,0);




        /*for (int i = 0; i < date_text.length() - 5; i++) {
            char character = date_text.charAt(i);
            if (i == 0) {
                character = Character.toUpperCase(character);
            }
            final_date_text += character;
        }*/

        mDate.setText(final_date_text);

        /*mStoreBase.collection("events").document(EVENT_LOAD.getUid())
                .collection("members").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot doc : task.getResult()){
                    //members.add(doc.toObject(ModelDataEvent.class).getUser().getId());
                    members.add(doc.getId());
                    participants ++;
                }
                nbParticipants.setText(participants+"");
                if(!members.isEmpty()){
                    Query query = mStoreBase.collection("users").whereIn("uid", members);
                    FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();

                    adapter = new FirestoreRecyclerAdapter<User, InfosEvenementsActivity.UserViewHolder>(options) {

                        @Override
                        public InfosEvenementsActivity.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_user, parent, false);
                            return new InfosEvenementsActivity.UserViewHolder(view);
                        }

                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        protected void onBindViewHolder(@NonNull final InfosEvenementsActivity.UserViewHolder userViewHolder, int i, @NonNull final User u) {
                            Constants.setUserImage(u, getContext(), userViewHolder.imagePerson);
                        }
                    };

                    mRecyclerView.setAdapter(adapter);
                    adapter.startListening();


                    mLayoutManager = new LinearLayoutManager(
                            getContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                }else{
                    mRecyclerView.setVisibility(View.GONE);
                }

            }
        });*/

        setData();

        //if(EVENT_LOAD.getImage_url() != null){
            //Constants.setEventImage(EVENT_LOAD, getContext(), imageEvent);
        //}
    }


    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //differenceDate(new Date(),EVENT_LOAD.getStartDate().toDate());
            handler.postDelayed(this,0);

        }
    };


    public void differenceDate(Date startDate, Date endDate) {
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        Log.w("date",
                elapsedDays + "d " + elapsedHours + "h " + elapsedMinutes +"m "+ elapsedSeconds+"secondes ");

        if(elapsedDays < 0 || elapsedHours < 0 || elapsedMinutes < 0 || elapsedSeconds < 0){
            if(elapsedDays < 0)
                elapsedDays = elapsedDays*-1;
            if(elapsedHours < 0 )
                elapsedHours = elapsedHours*-1;
            if(elapsedMinutes < 0)
                elapsedMinutes = elapsedMinutes*-1;
            if(elapsedSeconds < 0)
                elapsedSeconds = elapsedSeconds*-1;



            if(EVENT_LOAD.getStatusEvent() == StatusEvent.ENCOURS){
                textViewTitleDebut.setText("Commencé depuis : ");
            }
            if(EVENT_LOAD.getStatusEvent() == StatusEvent.FINI){
                textViewTitleDebut.setText("Terminé depuis : ");
                textViewTitleDebut.setTextColor(Color.GRAY);
                linearButton.setVisibility(View.GONE);
                cardViewTermine.setVisibility(View.VISIBLE);
                diffDate.setTextColor(Color.GRAY);
            }


        }
        diffDate.setText(elapsedDays + "j " + elapsedHours + "h " + elapsedMinutes +"m "+ elapsedSeconds+"s ");




    }

    private void suppression() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Vous êtes sur le point de supprimer un évènement ! Cette action est irréversible ! \n Voulez-vous continuez ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mStoreBase.collection("events").document(EVENT_LOAD.getUid()).delete();
                        mStoreBase.collection("users").document(USER_ID).collection("events").document(EVENT_LOAD.getUid()).delete();
                        Toast.makeText(getContext(), "Vous avez supprimé(e) un évènement !", Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.action_navigation_infos_events_to_navigation_evenements);
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }

    private void setData() {

        mStoreBase.collection("users").document(USER_ID).collection("events_join")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        if(doc.getId().equals(EVENT_LOAD.getUid())){
                            inscrit = true;
                            textViewInscription.setText("Se désinscrire");
                            //textViewInscription.setTextColor(Color.RED);
                            buttonInscription.setImageDrawable(getResources().getDrawable(R.drawable.inscrit_oui_50));
                            linearButton.setVisibility(View.VISIBLE);
                        }
                    }
                }

            }
        });

        mStoreBase.collection("users").document(USER_ID).collection("events_create")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                     if (task.isSuccessful()) {
                                                         for (QueryDocumentSnapshot doc : task.getResult()) {
                                                             if (doc.getId().equals(EVENT_LOAD.getUid())) {
                                                                 textViewInscription.setText("Modifier");
                                                                 buttonInscription.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_edit_50));
                                                                 buttonInscription.setOnClickListener(new View.OnClickListener() {
                                                                     @Override
                                                                     public void onClick(View v) {
                                                                         CreateEventFragment.EVENT_LOAD = EVENT_LOAD;
                                                                         CreateEventFragment.edit = true;
                                                                         //navController.navigate(R.id.action_navigation_infos_events_to_navigation_create_event);
                                                                     }
                                                                 });
                                                                 linearButton.setVisibility(View.VISIBLE);
                                                                 linearAnnuler.setVisibility(View.VISIBLE);
                                                             }
                                                         }
                                                     }

                                                 }
                                             });





        // if(type.equalsIgnoreCase("inscrit")){
    }

    private void incription() {
        if(!inscrit){
            //ModelDataEvent owner = new ModelDataEvent(false,"add", EVENT_LOAD.getOwnerDoc());
            ModelDataEvent m = new ModelDataEvent(false,"add", mStoreBase.collection("users").document(USER_ID));
            mStoreBase.collection("events").document(EVENT_LOAD.getUid()).collection("members").document(USER.getUid()).set(m);
            mStoreBase.collection("users").document(USER_ID).collection("events_join").document(EVENT_LOAD.getUid()).set(EVENT_LOAD);
            Toast.makeText(getContext(), "Vous êtes inscrit à l'évènement " + EVENT_LOAD.getName() + " !", Toast.LENGTH_SHORT).show();
            textViewInscription.setText("Se désinscrire");
            buttonInscription.setImageDrawable(getResources().getDrawable(R.drawable.inscrit_oui_50));
            inscrit = true;
            createFragment();
        }else{
            mStoreBase.collection("events").document(EVENT_LOAD.getUid()).collection("members").document(USER.getUid()).delete();
            mStoreBase.collection("users").document(USER_ID).collection("events_join").document(EVENT_LOAD.getUid()).delete();
            Toast.makeText(getContext(), "Vous ne participez plus à l'évènement " + EVENT_LOAD.getName() + " !", Toast.LENGTH_SHORT).show();
            textViewInscription.setText("Participer");
            buttonInscription.setImageDrawable(getResources().getDrawable(R.drawable.inscrit_50));
            inscrit = false;
            createFragment();
        }

    }

    private class UserViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagePerson;
        private CardView cardViewUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            imagePerson = itemView.findViewById(R.id.imageUser);
            cardViewUser = itemView.findViewById(R.id.cardViewUser);


        }

    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {

        handler.postDelayed(runnable,0);
        super.onResume();
    }
}