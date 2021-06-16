package com.nolonely.mobile.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.nolonely.mobile.R;
import com.nolonely.mobile.adapter.NotificationAdapter;
import com.nolonely.mobile.bdd.json.JSONController;
import com.nolonely.mobile.bdd.json.JSONObjectCrypt;
import com.nolonely.mobile.enumeration.UserList;
import com.nolonely.mobile.listeners.JSONArrayListener;
import com.nolonely.mobile.objects.Evenement;
import com.nolonely.mobile.objects.Notification;
import com.nolonely.mobile.objects.User;
import com.nolonely.mobile.objects.UserInvitation;
import com.nolonely.mobile.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.nolonely.mobile.ui.ListActivity.EXTRA_TYPE_LIST;
import static com.nolonely.mobile.ui.ListActivity.EXTRA_USERS_LIST;
import static com.nolonely.mobile.util.Constants.USER;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView mRecycler;
    private NotificationAdapter mAdapter;
    private ImageView buttonBack;
    private List<Notification> notificationList;
    private CardView cardViewInvitsFriend, cardViewInvitsEvent;
    private TextView textViewNbInvitFriend, textViewNbInvitEvent;
    private List<UserInvitation> invitationsFriend;
    private List<Evenement> invitationsEvent;
    private ProgressBar progressBar;
    private ArrayList<User> listInvitated;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // view = R.layout.activity_notification;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        createActivity();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createActivity() {
        mRecycler = findViewById(R.id.recyclerNotif);
        progressBar = findViewById(R.id.progressBar);
        buttonBack = findViewById(R.id.buttonBack);
        cardViewInvitsFriend = findViewById(R.id.cardViewInvitsFriend);

        cardViewInvitsEvent = findViewById(R.id.cardViewInvitsEvent);

        textViewNbInvitEvent = findViewById(R.id.nbInvitsEvent);
        textViewNbInvitFriend = findViewById(R.id.nbInvitsFriend);

        cardViewInvitsEvent.setOnClickListener(v -> {
            // navController.navigate(R.id.action_navigation_amis_to_navigation_invitations);
        });
        cardViewInvitsFriend.setOnClickListener(v -> {
            JSONObjectCrypt params = new JSONObjectCrypt();
            params.putCryptParameter("uid", USER.getUid());

            JSONController.getJsonArrayFromUrl(Constants.URL_GET_USER_INVITATIONS, getBaseContext(), params, new JSONArrayListener() {
                @Override
                public void onJSONReceived(JSONArray jsonArray) {
                    try {
                        listInvitated = new ArrayList<>();

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                listInvitated.add((User) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), User.class));
                            }
                            Intent intent = new Intent(getBaseContext(), ListFriendInvitationActivity.class);
                            intent.putExtra(EXTRA_USERS_LIST, listInvitated);
                            intent.putExtra(EXTRA_TYPE_LIST, UserList.INVIT_FRIEND.toString());
                            startActivity(intent);
                        } else {
                            Toast.makeText(NotificationActivity.this, getResources().getString(R.string.error_user_invitation), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.w("Response", "Erreur:" + e.getMessage());
                        Toast.makeText(NotificationActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onJSONReceivedError(VolleyError volleyError) {
                    Log.w("Response", "Erreur:" + volleyError.toString());
                    Toast.makeText(NotificationActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            });

        });

        buttonBack.setOnClickListener(v -> onBackPressed());
        //HomeActivity.buttonNotif.setImageDrawable(getResources().getDrawable(R.drawable.notification_none));
        adapterNotif();
        getInvitationsFriend();
        //getInvitationsEvent();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void adapterNotif() {

        notificationList = new ArrayList<>();

        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());

        JSONController.getJsonArrayFromUrl(Constants.URL_NOTIFICATIONS, NotificationActivity.this, params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                try {
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            notificationList.add((Notification) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Notification.class));
                        }
                        configureRecyclerViewNotifications();
                    } else {
                        //affiche menu sans notif
                    }
                } catch (JSONException e) {
                    Log.w("Response", "Erreur:" + e.getMessage());
                    Toast.makeText(NotificationActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:" + volleyError.toString());
                Toast.makeText(NotificationActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configureRecyclerViewNotifications() {
        this.mRecycler.setVisibility(View.VISIBLE);
        this.mAdapter = new NotificationAdapter(this.notificationList);
        this.mRecycler.setAdapter(this.mAdapter);
        this.mRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getInvitationsFriend() {
        invitationsFriend = new ArrayList<>();

        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());

        JSONController.getJsonArrayFromUrl(Constants.URL_FRIENDS_INVITATIONS, this, params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                try {
                    Log.w("Response", "Value" + jsonArray.toString());
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            invitationsFriend.add((UserInvitation) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), UserInvitation.class));
                        }

                        if (invitationsFriend.size() > 0) {
                            cardViewInvitsFriend.setVisibility(View.VISIBLE);
                            textViewNbInvitFriend.setText(invitationsFriend.size() + "");
                        } else {
                            cardViewInvitsFriend.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    Log.w("Response", "Erreur:" + e.getMessage());
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:" + volleyError.toString());
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getInvitationsEvent() {
        invitationsEvent = new ArrayList<>();

        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());

        JSONController.getJsonArrayFromUrl(Constants.URL_EVENT_INVITATIONS, this, params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        invitationsEvent.add((Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Evenement.class));
                    }

                    if (invitationsEvent.size() != 0) {
                        cardViewInvitsEvent.setVisibility(View.VISIBLE);
                        textViewNbInvitEvent.setText(invitationsEvent.size());
                    } else {
                        cardViewInvitsEvent.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    Log.w("Response", "Erreur:" + e.getMessage());
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:" + volleyError.toString());
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
    }
}