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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nalone.Evenement;
import com.example.nalone.ModelData;
import com.example.nalone.ModelDataEvent;
import com.example.nalone.R;
import com.example.nalone.StatusEvent;
import com.example.nalone.User;
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
import static com.example.nalone.util.Constants.formatD;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.timeFormat;

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
    private ImageView buttonInscription;
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

        buttonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incription();
            }
        });
        diffDate = rootView.findViewById(R.id.differenceDate);

        buttonPartager = rootView.findViewById(R.id.buttonPartager);
        buttonAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suppression();
            }
        });

        String date_text = formatD.format(EVENT_LOAD.getDate().toDate());
        String final_date_text = "";
        mTitle.setText(EVENT_LOAD.getName());
        mTimer.setText(timeFormat.format(EVENT_LOAD.getDate().toDate()));
        mOwner.setText(EVENT_LOAD.getOwner());
        if(EVENT_LOAD.getDescription().matches("")){
            mDescription.setVisibility(View.GONE);
        }else{
            mDescription.setText(EVENT_LOAD.getDescription());
        }




        handler = new Handler();
        handler.postDelayed(runnable,0);




        for (int i = 0; i < date_text.length() - 5; i++) {
            char character = date_text.charAt(i);
            if (i == 0) {
                character = Character.toUpperCase(character);
            }
            final_date_text += character;
        }

        mDate.setText(final_date_text);

        mStoreBase.collection("events").document(EVENT_LOAD.getUid())
                .collection("members").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot doc : task.getResult()){
                    members.add(doc.toObject(ModelDataEvent.class).getUser().getId());
                    participants ++;
                }
                nbParticipants.setText(participants+"");
                if(!members.isEmpty()){
                    Query query = mStoreBase.collection("users").whereIn("uid", members);
                    FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();

                    adapter = new FirestoreRecyclerAdapter<User, InfosEvenementsActivity.UserViewHolder>(options) {
                        @NonNull
                        @Override
                        public InfosEvenementsActivity.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_user, parent, false);
                            return new InfosEvenementsActivity.UserViewHolder(view);
                        }

                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        protected void onBindViewHolder(@NonNull final InfosEvenementsActivity.UserViewHolder userViewHolder, int i, @NonNull final User u) {
                            Constants.setUserImage(u, getContext(), userViewHolder.imagePerson);
                            if(u.getCursus().equalsIgnoreCase("Informatique")){
                                userViewHolder.cardViewUser.setCardBackgroundColor(Color.RED);
                            }

                            if(u.getCursus().equalsIgnoreCase("TC")){
                                userViewHolder.cardViewUser.setCardBackgroundColor(Color.parseColor("#00E9FD"));
                            }

                            if(u.getCursus().equalsIgnoreCase("MMI")){
                                userViewHolder.cardViewUser.setCardBackgroundColor(Color.parseColor("#FF1EED"));
                            }

                            if(u.getCursus().equalsIgnoreCase("GB")){
                                userViewHolder.cardViewUser.setCardBackgroundColor(Color.parseColor("#41EC57"));
                            }

                            if(u.getCursus().equalsIgnoreCase("LP")){
                                userViewHolder.cardViewUser.setCardBackgroundColor((Color.parseColor("#EC9538")));
                            }
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
        });

        setData(type);
    }


    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            differenceDate(new Date(),EVENT_LOAD.getDate().toDate());
            handler.postDelayed(this,0);

        }
    };


    public void differenceDate(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        Log.w("date","startDate : " + startDate);
        Log.w("date","endDate : "+ endDate);
        Log.w("date","different : " + different);

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

    private void setData(String type) {
        if(type.equalsIgnoreCase("inscrit")){
            textViewInscription.setText("Se désinscrire");
            textViewInscription.setTextColor(Color.RED);
            buttonInscription.setImageDrawable(getResources().getDrawable(R.drawable.inscrit_oui_50));
        }

        if(type.equalsIgnoreCase("creer")){
            textViewInscription.setText("Modifier");
            buttonInscription.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_edit_50));
            buttonInscription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CreateEventFragment.EVENT_LOAD = EVENT_LOAD;
                    CreateEventFragment.edit = true;
                    navController.navigate(R.id.action_navigation_infos_events_to_navigation_create_event);
                }
            });
            linearAnnuler.setVisibility(View.VISIBLE);

        }
    }

    private void incription() {
        if(!inscrit){
            ModelDataEvent owner = new ModelDataEvent(false,"add", EVENT_LOAD.getOwnerDoc());
            ModelDataEvent m = new ModelDataEvent(false,"add", mStoreBase.collection("users").document(USER_ID));
            mStoreBase.collection("events").document(EVENT_LOAD.getUid()).collection("members").document(USER.getUid()).set(m);
            mStoreBase.collection("users").document(USER_ID).collection("events").document(EVENT_LOAD.getUid()).set(owner);
            Toast.makeText(getContext(), "Vous êtes inscrit à l'évènement " + EVENT_LOAD.getName() + " !", Toast.LENGTH_SHORT).show();
            textViewInscription.setText("Se désinscrire");
            buttonInscription.setImageDrawable(getResources().getDrawable(R.drawable.inscrit_oui_50));
            inscrit = true;
            createFragment();
        }else{
            mStoreBase.collection("events").document(EVENT_LOAD.getUid()).collection("members").document(USER.getUid()).delete();
            mStoreBase.collection("users").document(USER_ID).collection("events").document(EVENT_LOAD.getUid()).delete();
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