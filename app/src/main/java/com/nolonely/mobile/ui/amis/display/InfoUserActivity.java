package com.nolonely.mobile.ui.amis.display;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.VolleyError;
import com.nolonely.mobile.R;
import com.nolonely.mobile.bdd.json.JSONController;
import com.nolonely.mobile.bdd.json.JSONObjectCrypt;
import com.nolonely.mobile.listeners.JSONArrayListener;
import com.nolonely.mobile.listeners.JSONObjectListener;
import com.nolonely.mobile.objects.TopCI;
import com.nolonely.mobile.objects.User;
import com.nolonely.mobile.util.Constants;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.nolonely.mobile.util.Constants.USER;

public class InfoUserActivity extends AppCompatActivity {
    private User USER_LOAD;

    private ImageView imagePerson;
    private Button buttonAdd;

    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    int nbEventFirt, nbEventSecond, nbEventThird;

    boolean isFriend;

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);

        if (getIntent() != null) {
            USER_LOAD = (User) getIntent().getSerializableExtra("user");
            isFriend = getIntent().getBooleanExtra("isFriend", false);
        }

        ImageView buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setVisibility(View.VISIBLE);
        buttonBack.setOnClickListener(v -> onBackPressed());

        getTopCI();

        TextView nameProfil;
        TextView descriptionProfil;
        TextView nbCreateProfil;
        TextView nbParticipateProfil;
        TextView villeProfil;

        CardView cardViewPhotoPerson;

        nameProfil = findViewById(R.id.profilName);
        descriptionProfil = findViewById(R.id.profilDescription);
        nbCreateProfil = findViewById(R.id.nbEventCreate);
        nbParticipateProfil = findViewById(R.id.nbEventParticipe);
        imagePerson = findViewById(R.id.imagePerson);
        buttonAdd = findViewById(R.id.buttonAdd);
        cardViewPhotoPerson = findViewById(R.id.cardViewPhotoPerson);
        villeProfil = findViewById(R.id.userConnectVille);

        nameProfil.setText(USER_LOAD.getName());
        villeProfil.setText(USER_LOAD.getCity());
        nbCreateProfil.setText(USER_LOAD.getNumber_events_create() + "");
        nbParticipateProfil.setText(USER_LOAD.getNumber_events_attend() + "");

        Constants.setUserImage(USER_LOAD, imagePerson);

        if (isFriend) {
            buttonAdd.setOnClickListener(v -> deleteFriend());
            buttonAdd.setText(getResources().getString(R.string.to_delete));
            buttonAdd.setBackground(getResources().getDrawable(R.drawable.custom_input));
        } else {

            buttonAdd.setOnClickListener(v -> addFriend());
        }

        nameProfil.setText(USER_LOAD.getName());
        descriptionProfil.setText(USER_LOAD.getDescription());
        nbCreateProfil.setText(USER_LOAD.getNumber_events_create() + "");
        nbParticipateProfil.setText(USER_LOAD.getNumber_events_attend() + "");
        //buttonAdd.setImageResource(button);

        if (USER_LOAD.getDescription().matches("")) {
            descriptionProfil.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getTopCI() {
        LinearLayout ci1 = findViewById(R.id.linearCi1);
        LinearLayout ci2 = findViewById(R.id.linearCi2);
        LinearLayout ci3 = findViewById(R.id.linearCi3);
        PieChart pieChart = findViewById(R.id.piechart);
        TextView ciTotal = findViewById(R.id.textView68);

        LinearLayout linearCi = findViewById(R.id.linearTopCi);

        nbEventFirt = 0;
        nbEventSecond = 0;
        nbEventThird = 0;

        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER_LOAD.getUid());

        JSONController.getJsonArrayFromUrl(Constants.URL_GET_TOP_CI, getBaseContext(), params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                try {

                    if (jsonArray.length() > 0) {
                        ciTotal.setVisibility(View.GONE);
                        linearCi.setVisibility(View.VISIBLE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (i == 0) {
                                nbEventFirt = ((TopCI) JSONController.convertJSONToObject(jsonArray.getJSONObject(0), TopCI.class)).getNumber();
                                ci1.setVisibility(View.VISIBLE);
                                pieChart.addPieSlice(
                                        new PieModel(
                                                nbEventFirt,
                                                getResources().getColor(R.color.colorPrimary)));
                            }
                            if (i == 1) {
                                nbEventSecond = ((TopCI) JSONController.convertJSONToObject(jsonArray.getJSONObject(1), TopCI.class)).getNumber();
                                ci2.setVisibility(View.VISIBLE);
                                pieChart.addPieSlice(
                                        new PieModel(
                                                nbEventSecond,
                                                getResources().getColor(R.color.colorSecond)));
                            }
                            if (i == 2) {
                                nbEventThird = ((TopCI) JSONController.convertJSONToObject(jsonArray.getJSONObject(2), TopCI.class)).getNumber();
                                ci3.setVisibility(View.VISIBLE);
                                pieChart.addPieSlice(
                                        new PieModel(
                                                nbEventThird,
                                                getResources().getColor(R.color.colorThird)));
                            }
                        }

                    } else {
                        pieChart.addPieSlice(
                                new PieModel(
                                        100,
                                        getResources().getColor(R.color.grey)));
                    }

                    pieChart.startAnimation();

                } catch (JSONException e) {
                    Log.w("Response", "Valeur" + jsonArray.toString());
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
    private void deleteFriend() {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("uid_friend", USER_LOAD.getUid());


        Log.w("Response", "Params " + params.toString());

        JSONController.getJsonObjectFromUrl(Constants.URL_DELETE_FRIEND, getBaseContext(), params, new JSONObjectListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                if (jsonObject.length() == 3) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.delete_friend), Toast.LENGTH_SHORT).show();
                    buttonAdd.setText(getResources().getString(R.string.follow));
                    buttonAdd.setBackground(getResources().getDrawable(R.drawable.custom_button_simple));
                } else {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    Log.w("Response", "Error:" + jsonObject.toString());
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                Log.w("Response", "Error:" + volleyError.toString());
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void hasFriendRequest() {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER_LOAD.getUid());

        JSONController.getJsonArrayFromUrl(Constants.URL_FRIENDS_INVITATIONS, getBaseContext(), params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                if (jsonArray.length() > 0) {
                    
                } else {
                    buttonAdd.setOnClickListener(v -> addFriend());
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addFriend() {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("uid_friend", USER_LOAD.getUid());
        params.putCryptParameter("notification_receiver", getResources().getString(R.string.invit_received_notifications) + " " + USER.getName());
        params.putCryptParameter("notification_sender", getResources().getString(R.string.invit_send_notifications) + " " + USER_LOAD.getName());

        Log.w("Response", "Params " + params.toString());

        JSONController.getJsonObjectFromUrl(Constants.URL_SEND_FRIEND_REQUEST, getBaseContext(), params, new JSONObjectListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                if (jsonObject.length() == 3) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.invit_send), Toast.LENGTH_SHORT).show();
                    buttonAdd.setText(getResources().getString(R.string.button_invit_waiting));
                    buttonAdd.setBackground(getResources().getDrawable(R.drawable.custom_input));
                } else {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    Log.w("Response", "Error:" + jsonObject.toString());
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                Log.w("Response", "Error:" + volleyError.toString());
            }
        });
    }
}