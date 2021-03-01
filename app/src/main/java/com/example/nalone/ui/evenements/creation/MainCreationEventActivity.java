package com.example.nalone.ui.evenements.creation;

import androidx.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.enumeration.ImageType;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.objects.Evenement;
import com.example.nalone.ui.NotificationActivity;
import com.example.nalone.util.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.example.nalone.util.Constants.USER;

public class MainCreationEventActivity extends AppCompatActivity {

    public static NavController navController;
    public static ImageView buttonBack, buttonNotif;
    public static Evenement currentEvent;
    public static boolean photoValidate, dateValidate, nameValidate, membersValidate, addressValidate, costValidate;
    public static Uri image = null;

    private static Activity activity;

    private BottomSheetBehavior bottomSheetBehavior;
    private TextView textViewVisibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_creation_event);
        final View bottomSheet = findViewById(R.id.sheetCreateEvent);
        final View viewGrey = findViewById(R.id.viewGrey2);
        textViewVisibility = findViewById(R.id.textViewVisibility);

        activity = this;
        buttonBack = findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                switch (state){
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        viewGrey.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        viewGrey.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        viewGrey.setVisibility(View.GONE);
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        textViewVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
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
        String imageData;
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

        try {
            imageData = new String(getBytes(activity, image), StandardCharsets.UTF_8);
            Constants.uploadImageOnServer(ImageType.EVENT, currentEvent.getUid(), imageData, activity); //upload image on web server
        } catch (IOException e) {
            Log.w("Response", "Erreur: "+e.getMessage());
        }

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

    private static byte[] getBytes(Context context, Uri uri) throws IOException {
        InputStream iStream = context.getContentResolver().openInputStream(uri);
        try {
            return getBytes(iStream);
        } finally {
            // close the stream
            try {
                iStream.close();
            } catch (IOException ignored) { /* do nothing */ }
        }
    }

    private static byte[] getBytes(InputStream inputStream) throws IOException {

        byte[] bytesResult = null;
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        try {
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            bytesResult = byteBuffer.toByteArray();
        } finally {
            // close the stream
            try{ byteBuffer.close(); } catch (IOException ignored){ /* do nothing */ }
        }
        return bytesResult;
    }
}