package com.example.nalone.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
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
import com.example.nalone.R;
import com.example.nalone.adapter.NotificationAdapter;
import com.example.nalone.adapter.RechercheAmisAdapter;
import com.example.nalone.json.JSONArrayListener;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.objects.Notification;
import com.example.nalone.objects.User;
import com.example.nalone.util.Constants;
import com.example.nalone.util.Horloge;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.setUserImage;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView mRecycler;
    private NotificationAdapter mAdapter;
    private ImageView buttonBack;
    private List<Notification> notificationList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mRecycler = findViewById(R.id.recyclerNotif);
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        HomeActivity.buttonNotif.setImageDrawable(getResources().getDrawable(R.drawable.notification_none));
        adapterNotif();
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
                   } catch (JSONException e){
                       Log.w("Response", "Erreur:"+e.getMessage());
                       Toast.makeText(NotificationActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                   }
                   configureRecyclerViewAmis();
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:"+volleyError.toString());
                Toast.makeText(NotificationActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configureRecyclerViewAmis() {
        this.mAdapter = new NotificationAdapter(this.notificationList);
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.mRecycler.setAdapter(this.mAdapter);
        // 3.4 - Set layout manager to position the items
        final LinearLayoutManager llm = new LinearLayoutManager(this);
        this.mRecycler.setLayoutManager(llm);
    }



}