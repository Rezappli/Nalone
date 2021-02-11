package com.example.nalone.ui.evenements.creation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.nalone.R;
import com.example.nalone.objects.Evenement;
import com.example.nalone.ui.NotificationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.currentUser;
import static com.example.nalone.util.Constants.mStoreBase;

public class MainCreationEventActivity extends AppCompatActivity {

    public static NavController navController;
    public static ImageView buttonBack, buttonNotif;
    public static Evenement currentEvent;
    public static boolean photoValidate, dateValidate, nameValidate, membersValidate, adressValidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_creation_event);

        buttonBack = findViewById(R.id.buttonBack);
        buttonNotif = findViewById(R.id.buttonNotif);
        buttonNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), NotificationActivity.class));
            }
        });
        mStoreBase.collection("users").document(USER_ID).collection("notifications").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().size() > 0) {
                                buttonNotif.setImageDrawable(getResources().getDrawable(R.drawable.notification));
                            }
                        }
                    }
                });
        buttonBack.setVisibility(View.GONE);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment2);
    }

    public static boolean isAllValidate(){
        if (photoValidate && nameValidate && membersValidate && dateValidate && adressValidate){
            return true;
        }else{
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        currentEvent = null;
        photoValidate = false;
        dateValidate= false;
        nameValidate= false;
        membersValidate= false;
        adressValidate= false;
    }
}