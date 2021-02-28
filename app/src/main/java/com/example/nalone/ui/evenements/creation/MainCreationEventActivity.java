package com.example.nalone.ui.evenements.creation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.json.JSONObjectListener;
import com.example.nalone.objects.Evenement;
import com.example.nalone.ui.NotificationActivity;
import com.example.nalone.util.Constants;

import org.json.JSONObject;

import static com.example.nalone.util.Constants.USER;

public class MainCreationEventActivity extends AppCompatActivity {

    public static NavController navController;
    public static ImageView buttonBack, buttonNotif;
    public static Evenement currentEvent;
    public static boolean photoValidate, dateValidate, nameValidate, membersValidate, addressValidate;
    public static Uri image = null;

    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_creation_event);
        activity = this;
        buttonBack = findViewById(R.id.buttonBack);
        buttonNotif = findViewById(R.id.buttonNotif);
        buttonNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), NotificationActivity.class));
            }
        });

        buttonBack.setVisibility(View.GONE);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment2);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean isAllValidate(Context context){
        if (photoValidate && nameValidate && membersValidate && dateValidate && addressValidate){
            return true;
        }else{
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createEvent(final Context context) {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.addParameter("uid", USER.getUid());
        params.addParameter("uid_event", currentEvent.getUid());
        params.addParameter("address", currentEvent.getAddress());
        params.addParameter("city", currentEvent.getCity());
        params.addParameter("description", currentEvent.getDescription());
        params.addParameter("startDate", currentEvent.getStartDate());
        params.addParameter("endDate", currentEvent.getEndDate());
        params.addParameter("visibility", currentEvent.getVisibility());
        params.addParameter("latitude", currentEvent.getLatitude());
        params.addParameter("longitude", currentEvent.getLongitude());
        params.addParameter("name", currentEvent.getName());
        params.addParameter("ownerFirstName", USER.getFirst_name());
        params.addParameter("ownerLastName", USER.getLast_name());
        params.addParameter("nbMembers", currentEvent.getNbMembers());

        JSONController.getJsonObjectFromUrl(Constants.URL_ADD_EVENT, context, params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                if(jsonObject.length() == 3){
                    Toast.makeText(context, context.getResources().getString(R.string.event_create), Toast.LENGTH_SHORT).show();
                    activity.finish();
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.event_error_create), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(context, context.getResources().getString(R.string.event_error_create),Toast.LENGTH_SHORT).show();
                Log.w("Response", "Erreur: "+volleyError.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.event_not_save))
                .setPositiveButton(getResources().getString(R.string.button_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clearEvent();
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.button_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }

    private void clearEvent(){
        currentEvent = null;
        photoValidate = false;
        dateValidate= false;
        nameValidate= false;
        membersValidate= false;
        addressValidate = false;
    }
}