package com.nolonely.mobile.ui.evenements.display;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nolonely.mobile.R;
import com.nolonely.mobile.adapter.user.UserParticipantListAdapter;
import com.nolonely.mobile.objects.User;

import java.util.ArrayList;

public class ListParticipantActivity extends AppCompatActivity {


    private UserParticipantListAdapter participantListAdapter;
    private RecyclerView mRecyclerView;

    private LinearLayout linearNoResult;
    public static String EXTRA_PARTICIPATION_VALIDATED = "EXTRA_PARTICIPATION_VALIDATED";
    public static String EXTRA_PARTICIPATION_REGISTERED = "EXTRA_PARTICIPATION_REGISTERED";
    public static String EXTRA_PARTICIPATION_WAIT = "EXTRA_PARTICIPATION_WAIT";

    private ArrayList<User> usersWait = new ArrayList<>();
    private ArrayList<User> usersRegistered = new ArrayList<>();
    private ArrayList<User> usersValidated = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_participant_list);
        mRecyclerView = findViewById(R.id.recyclerViewInvitAmis);
        usersWait = new ArrayList<>();
        usersRegistered = new ArrayList<>();
        usersValidated = new ArrayList<>();
        if (getIntent() != null) {

            this.usersWait = (ArrayList<User>) getIntent().getSerializableExtra(EXTRA_PARTICIPATION_WAIT);
            this.usersRegistered = (ArrayList<User>) getIntent().getSerializableExtra(EXTRA_PARTICIPATION_REGISTERED);
            this.usersValidated = (ArrayList<User>) getIntent().getSerializableExtra(EXTRA_PARTICIPATION_VALIDATED);

            linearNoResult = findViewById(R.id.linearNoResult);

            if (usersValidated.isEmpty() && usersRegistered.isEmpty() && usersWait.isEmpty()) {
                linearNoResult.setVisibility(View.VISIBLE);
            } else {
                this.participantListAdapter = new UserParticipantListAdapter(this.usersValidated);
                participantListAdapter.setOnItemClickListener(new UserParticipantListAdapter.OnItemClickListener() {
                    @Override
                    public void onDisplayClick(int position) {

                    }

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onRemoveClick(int position) {
                        removeMember(position);
                    }
                });
                this.mRecyclerView.setAdapter(this.participantListAdapter);
                this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            }


        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void removeMember(int position) {
        /*JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("uid_friend", users.get(position));

        JSONController.getJsonArrayFromUrl(Constants.URL_DELETE_FIREND_INVITATION, this, params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                users.remove(users.get(position));
                if (users.isEmpty()) {
                    linearNoResult.setVisibility(View.VISIBLE);
                }
                userListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:" + volleyError.toString());
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });*/
    }

}