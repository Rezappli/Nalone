package com.example.nalone.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.nalone.HomeActivity;
import com.example.nalone.R;
import com.example.nalone.adapter.NotificationAdapter;
import com.example.nalone.listeners.JSONArrayListener;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.objects.Evenement;
import com.example.nalone.objects.Notification;
import com.example.nalone.objects.UserInvitation;
import com.example.nalone.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView mRecycler;
    private NotificationAdapter mAdapter;
    private ImageView buttonBack;
    private List<Notification> notificationList;
    private CardView cardViewInvitsFriend,cardViewInvitsEvent;
    private TextView textViewNbInvitFriend,textViewNbInvitEvent;
    private List<UserInvitation> invitationsFriend;
    private List<Evenement> invitationsEvent;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mRecycler = findViewById(R.id.recyclerNotif);
        buttonBack = findViewById(R.id.buttonBack);
        cardViewInvitsFriend = findViewById(R.id.cardViewInvitsFriend);
        cardViewInvitsEvent = findViewById(R.id.cardViewInvitsEvent);

        cardViewInvitsEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navController.navigate(R.id.action_navigation_amis_to_navigation_invitations);
            }
        });
        cardViewInvitsFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // navController.navigate(R.id.action_navigation_amis_to_navigation_invitations);
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        HomeActivity.buttonNotif.setImageDrawable(getResources().getDrawable(R.drawable.notification_none));
        adapterNotif();
        getInvitationsFriend();
        getInvitationsEvent();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void adapterNotif() {

        notificationList = new ArrayList<>();

        JSONObjectCrypt params = new JSONObjectCrypt();
        params.addParameter("uid", USER.getUid());

        JSONController.getJsonArrayFromUrl(Constants.URL_NOTIFICATIONS, NotificationActivity.this, params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                   try {
                       for(int i = 0; i < jsonArray.length(); i++) {
                           notificationList.add((Notification) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Notification.class));
                       }
                       configureRecyclerViewNotifications();
                   } catch (JSONException e){
                       Log.w("Response", "Erreur:"+e.getMessage());
                       Toast.makeText(NotificationActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                   }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:"+volleyError.toString());
                Toast.makeText(NotificationActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configureRecyclerViewNotifications() {
        this.mAdapter = new NotificationAdapter(this.notificationList);
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.mRecycler.setAdapter(this.mAdapter);
        // 3.4 - Set layout manager to position the items
        final LinearLayoutManager llm = new LinearLayoutManager(this);
        this.mRecycler.setLayoutManager(llm);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getInvitationsFriend() {
        invitationsFriend = new ArrayList<>();

        JSONObjectCrypt params = new JSONObjectCrypt();
        params.addParameter("uid", USER.getUid());

        JSONController.getJsonArrayFromUrl(Constants.URL_FRIENDS_INVITATIONS, this, params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        invitationsFriend.add((UserInvitation) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), UserInvitation.class));
                    }

                    if(invitationsFriend.size() != 0){
                        cardViewInvitsFriend.setVisibility(View.VISIBLE);
                        textViewNbInvitFriend.setText(invitationsFriend.size());
                    }else{
                        cardViewInvitsFriend.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    Log.w("Response", "Erreur:"+e.getMessage());
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:"+volleyError.toString());
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getInvitationsEvent() {
        invitationsEvent = new ArrayList<>();

        JSONObjectCrypt params = new JSONObjectCrypt();
        params.addParameter("uid", USER.getUid());

        JSONController.getJsonArrayFromUrl(Constants.URL_EVENT_INVITATIONS, this, params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        invitationsEvent.add((Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Evenement.class));
                    }

                    if(invitationsEvent.size() != 0){
                        cardViewInvitsEvent.setVisibility(View.VISIBLE);
                        textViewNbInvitEvent.setText(invitationsEvent.size());
                    }else{
                        cardViewInvitsEvent.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    Log.w("Response", "Erreur:"+e.getMessage());
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:"+volleyError.toString());
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
            }
        });
    }
}