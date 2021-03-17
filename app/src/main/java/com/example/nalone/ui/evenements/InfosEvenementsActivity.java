package com.example.nalone.ui.evenements;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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
import android.graphics.drawable.Drawable;
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

import com.android.volley.VolleyError;
import com.example.nalone.HomeActivity;
import com.example.nalone.enumeration.Visibility;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONObjectListener;
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

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.mStoreBase;

public class InfosEvenementsActivity extends AppCompatActivity {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mTitle;
    private TextView mDate;
    private TextView mTimer;
    private TextView mOwner;
    private TextView mDescription;
    private List<String> members = new ArrayList<>();

    public static Evenement EVENT_LOAD;

    public static String type;
    private View rootView;
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
    private ImageView buttonBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos_evenements);
        createFragment();
        setData();

    }

    private void createFragment() {
        participants = 0;
        mTitle = findViewById(R.id.title);
        mDate = findViewById(R.id.date);
        mTimer = findViewById(R.id.time);
        mOwner = findViewById(R.id.owner);
        mDescription = findViewById(R.id.description);
        mRecyclerView = findViewById(R.id.recyclerViewMembresInscrits);
        textViewNbMembers = findViewById(R.id.textViewNbMembers);
        nbParticipants = findViewById(R.id.nbParticipants);
        buttonInscription = findViewById(R.id.buttonInscription);
        textViewInscription = findViewById(R.id.textViewParticiper);
        textViewPartager = findViewById(R.id.textViewPartager);
        textViewTitleDebut = findViewById(R.id.textViewTitleDebut);
        linearButton = findViewById(R.id.linearButton);
        cardViewTermine = findViewById(R.id.cardViewTermine);
        linearAnnuler = findViewById(R.id.linearAnnuler);
        buttonAnnuler = findViewById(R.id.buttonAnnuler);
        ownerImage = findViewById(R.id.ownerImage);
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        buttonInscription.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                incription();
            }
        });
        diffDate = findViewById(R.id.differenceDate);

        buttonPartager = findViewById(R.id.buttonPartager);
        buttonPartager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Clicked on Share " , Toast.LENGTH_SHORT).show();
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

        String final_date_text = "";
        mTitle.setText(EVENT_LOAD.getName());
        mOwner.setText(EVENT_LOAD.getOwner_first_name()+" "+EVENT_LOAD.getOwner_last_name());
        nbParticipants.setText(EVENT_LOAD.getNbMembers()+"");

        if(EVENT_LOAD.getDescription().matches("")){
            mDescription.setVisibility(View.GONE);
        }else{
            mDescription.setText(EVENT_LOAD.getDescription());
        }

        handler = new Handler();
        handler.postDelayed(runnable,0);

        try {
            Date date = sdf.parse(EVENT_LOAD.getStartDate());
            String date_text = Constants.formatD.format(date);
            for (int i = 0; i < date_text.length() - 5; i++) {
                char character = date_text.charAt(i);
                if (i == 0) {
                    character = Character.toUpperCase(character);
                }
                final_date_text += character;
            }

            mDate.setText(final_date_text);
        } catch (ParseException e) {
            Log.w("Response", "Erreur:"+e.getMessage());
            Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
        }

        mTimer.setText(cutString(EVENT_LOAD.getStartDate(), 5, 11));
        Log.w("STATUS", EVENT_LOAD.getStatusEvent()+"");
        if(EVENT_LOAD.getStatusEvent() == StatusEvent.ENCOURS) {
            textViewTitleDebut.setText(getResources().getString(R.string.event_start_from));
        }else if(EVENT_LOAD.getStatusEvent() == StatusEvent.FINI){
            textViewTitleDebut.setText(getResources().getString(R.string.event_end_from));
            textViewTitleDebut.setTextColor(Color.GRAY);
            linearButton.setVisibility(View.GONE);
            cardViewTermine.setVisibility(View.VISIBLE);
            diffDate.setTextColor(Color.GRAY);
        }else if(EVENT_LOAD.getStatusEvent() == StatusEvent.BIENTOT){
            textViewTitleDebut.setText(getResources().getString(R.string.event_start_in));
        }
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
                            View view = LayoutInflater.from(parent.getBaseContext()()).inflate(R.layout.image_user, parent, false);
                            return new InfosEvenementsActivity.UserViewHolder(view);
                        }

                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        protected void onBindViewHolder(@NonNull final InfosEvenementsActivity.UserViewHolder userViewHolder, int i, @NonNull final User u) {
                            Constants.setUserImage(u, getBaseContext()(), userViewHolder.imagePerson);
                        }
                    };

                    mRecyclerView.setAdapter(adapter);
                    adapter.startListening();


                    mLayoutManager = new LinearLayoutManager(
                            getBaseContext()(),
                            LinearLayoutManager.HORIZONTAL,
                            false);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                }else{
                    mRecyclerView.setVisibility(View.GONE);
                }

            }
        });*/

        //if(EVENT_LOAD.getImage_url() != null){
            //Constants.setEventImage(EVENT_LOAD, getBaseContext()(), imageEvent);
        //}
    }


    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                differenceDate(new Date(),sdf.parse(EVENT_LOAD.getStartDate()));
                handler.postDelayed(this,0);
            } catch (ParseException e) {
                Log.w("Response", "Erreur:"+e.getMessage());
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
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


        }
        diffDate.setText(elapsedDays + "j " + elapsedHours + "h " + elapsedMinutes +"m "+ elapsedSeconds+"s ");
    }

    private void suppression() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
        builder.setMessage(getResources().getString(R.string.alert_dialog_delete_event))
                .setPositiveButton(getResources().getText(R.string.button_yes), new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onClick(DialogInterface dialog, int id) {
                        JSONObjectCrypt params = new JSONObjectCrypt();
                        params.addParameter("uid", USER.getUid());
                        params.addParameter("uid_event", EVENT_LOAD.getUid());

                        JSONController.getJsonObjectFromUrl(Constants.URL_EVENT_DELETE, getBaseContext(), params, new JSONObjectListener() {
                            @Override
                            public void onJSONReceived(JSONObject jsonObject) {
                                if(jsonObject.length() == 3) {
                                    Toast.makeText(getBaseContext(), getResources().getString(R.string.event_delete), Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }else{
                                    Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onJSONReceivedError(VolleyError volleyError) {
                                Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                                Log.w("Response", "Erreur:"+volleyError.toString());
                            }
                        });
                    }
                })
                .setNegativeButton(getResources().getText(R.string.button_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }

    private void setData() {
        if(EVENT_LOAD.getOwner_uid().equalsIgnoreCase(USER.getUid())){
            inscrit = true;
            textViewInscription.setText(getResources().getString(R.string.unregister));
            buttonInscription.setImageDrawable(getResources().getDrawable(R.drawable.inscrit_oui_50));
            linearAnnuler.setVisibility(View.VISIBLE);
            linearButton.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void incription() {
        Drawable resources = null;
        String inscriptionText = "";
        if(!inscrit){
            Toast.makeText(getBaseContext(), getResources().getString(R.string.register_message)+" "+ EVENT_LOAD.getName(), Toast.LENGTH_SHORT).show();
            inscriptionText = getResources().getString(R.string.unregister);
            resources = getResources().getDrawable(R.drawable.inscrit_oui_50);
            inscrit = true;
            onRegisterUser();
        }else{
            Toast.makeText(getBaseContext(), getResources().getString(R.string.unregister_messge)+" "+ EVENT_LOAD.getName(), Toast.LENGTH_SHORT).show();
            inscriptionText = getResources().getString(R.string.register);
            resources = getResources().getDrawable(R.drawable.inscrit_50);
            inscrit = false;
            onUnregisterUser();
        }
        textViewInscription.setText(inscriptionText);
        buttonInscription.setImageDrawable(resources);
        createFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onRegisterUser(){
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.addParameter("uid", USER.getUid());
        params.addParameter("uid_event", EVENT_LOAD.getUid());
        if(EVENT_LOAD.getVisibility().equals(Visibility.PRIVATE)){
            params.addParameter("status", "waiting");
        }else{
            params.addParameter("status", "add");
        }

        JSONController.getJsonObjectFromUrl(Constants.URL_ADD_USER_TO_EVENT, getBaseContext(), params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.register_message), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                Log.w("Response", "Erreur:"+volleyError.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onUnregisterUser(){
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.addParameter("uid", USER.getUid());
        params.addParameter("uid_event", EVENT_LOAD.getUid());

        JSONController.getJsonObjectFromUrl(Constants.URL_DELETE_USER_TO_EVENT, getBaseContext(), params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.register_message), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                Log.w("Response", "Erreur:"+volleyError.toString());
            }
        });
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