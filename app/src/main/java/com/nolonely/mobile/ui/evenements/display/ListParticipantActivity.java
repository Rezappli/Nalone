package com.nolonely.mobile.ui.evenements.display;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nolonely.mobile.R;
import com.nolonely.mobile.adapter.user.UserParticipantListAdapter;
import com.nolonely.mobile.enumeration.StateUser;
import com.nolonely.mobile.objects.User;

import java.util.ArrayList;
import java.util.List;

public class ListParticipantActivity extends AppCompatActivity {


    private UserParticipantListAdapter participantListAdapter;
    private RecyclerView mRecyclerView;

    private LinearLayout linearNoResult;
    public static String EXTRA_PARTICIPATION_VALIDATED = "EXTRA_PARTICIPATION_VALIDATED";
    public static String EXTRA_PARTICIPATION_REGISTERED = "EXTRA_PARTICIPATION_REGISTERED";
    public static String EXTRA_PARTICIPATION_WAIT = "EXTRA_PARTICIPATION_WAIT";

    private ArrayList<User> usersWait;
    private ArrayList<User> usersRegistered;
    private ArrayList<User> usersValidated;
    private ArrayList<User> usersRecycler;

    private TextView tvValidated, tvRegistered, tvWaited;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_participant_list);
        mRecyclerView = findViewById(R.id.recyclerViewInvitAmis);
        usersWait = new ArrayList<>();
        usersRegistered = new ArrayList<>();
        usersValidated = new ArrayList<>();
        usersRecycler = new ArrayList<>();

        TextView nbValidated = findViewById(R.id.nbParticipantValidated);
        TextView nbRegistered = findViewById(R.id.nbParticipantRegistered);
        TextView nbWaited = findViewById(R.id.nbParticipantWaited);
        TextView nbTotal = findViewById(R.id.totalParticipants);

        tvValidated = findViewById(R.id.tvParticipantsValidate);
        tvRegistered = findViewById(R.id.tvParticipantsRegistered);
        tvWaited = findViewById(R.id.tvParticipantsWaited);

        tvValidated.setOnClickListener(v -> updateDrawableClicked(StateUser.VALIDATED));
        tvRegistered.setOnClickListener(v -> updateDrawableClicked(StateUser.REGISTERED));
        tvWaited.setOnClickListener(v -> updateDrawableClicked(StateUser.WAITED));

        linearNoResult = findViewById(R.id.linearNoResult);


        if (getIntent() != null) {
            this.usersWait = (ArrayList<User>) getIntent().getSerializableExtra(EXTRA_PARTICIPATION_WAIT);
            this.usersRegistered = (ArrayList<User>) getIntent().getSerializableExtra(EXTRA_PARTICIPATION_REGISTERED);
            this.usersValidated = (ArrayList<User>) getIntent().getSerializableExtra(EXTRA_PARTICIPATION_VALIDATED);

            nbValidated.setText(usersValidated.size() + "");
            nbRegistered.setText(usersRegistered.size() + "");
            nbWaited.setText(usersWait.size() + "");
            int sizeTotal = usersValidated.size() + usersRegistered.size() + usersWait.size();
            nbTotal.setText(getString(R.string.total) + " : " + usersValidated.size() + " / " + sizeTotal);


            this.participantListAdapter = new UserParticipantListAdapter(this.usersRecycler, false);
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


            if (!usersValidated.isEmpty()) {
                updateDrawableClicked(StateUser.VALIDATED);
            } else if (!usersRegistered.isEmpty()) {
                updateDrawableClicked(StateUser.REGISTERED);
            } else if (!usersWait.isEmpty()) {
                updateDrawableClicked(StateUser.WAITED);
            } else {
                linearNoResult.setVisibility(View.VISIBLE);
            }


        }


    }

    private void updateDrawableClicked(StateUser stateUser) {
        switch (stateUser) {
            case VALIDATED:
                changeTextViewBackground(tvValidated, true);
                changeTextViewBackground(tvRegistered, false);
                changeTextViewBackground(tvWaited, false);
                updateRecyclerView(usersValidated, false);
                break;
            case REGISTERED:
                changeTextViewBackground(tvValidated, false);
                changeTextViewBackground(tvRegistered, true);
                changeTextViewBackground(tvWaited, false);
                updateRecyclerView(usersRegistered, false);
                break;
            case WAITED:
                changeTextViewBackground(tvValidated, false);
                changeTextViewBackground(tvRegistered, false);
                changeTextViewBackground(tvWaited, true);
                updateRecyclerView(usersWait, true);
                break;
        }
    }

    private void changeTextViewBackground(TextView textView, boolean isClicked) {
        textView.setBackground(ContextCompat.getDrawable(getBaseContext(), isClicked ? R.drawable.tab_indicator : R.drawable.custom_button_signup));
        textView.setTextColor(isClicked ? Color.WHITE : getResources().getColor(R.color.black));
    }

    private void updateRecyclerView(List<User> users, boolean isWaited) {
        participantListAdapter.setWaited(isWaited);
        usersRecycler.clear();
        if (!users.isEmpty()) {
            linearNoResult.setVisibility(View.GONE);
            usersRecycler.addAll(users);
        } else {
            linearNoResult.setVisibility(View.VISIBLE);
        }
        participantListAdapter.notifyDataSetChanged();
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