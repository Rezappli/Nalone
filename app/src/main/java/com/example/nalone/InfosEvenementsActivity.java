package com.example.nalone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.nalone.items.ItemImagePerson;
import com.example.nalone.util.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.mStoreBase;

public class InfosEvenementsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mTitle;
    private TextView mDate;
    private TextView mTimer;
    private TextView mOwner;
    private TextView mDescription;
    private final List<ItemImagePerson> membres_inscrits = new ArrayList<>();

    public static Evenement EVENT_LOAD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos_evenements);

        mTitle = findViewById(R.id.title);
        mDate = findViewById(R.id.date);
        mTimer = findViewById(R.id.time);
        mOwner = findViewById(R.id.owner);
        mDescription = findViewById(R.id.description);

        String date_text = Constants.formatD.format(EVENT_LOAD.getDate());
        String final_date_text = "";
        mTitle.setText(EVENT_LOAD.getName());
        mTimer.setText(EVENT_LOAD.getDate().toString());
        mOwner.setText(EVENT_LOAD.getOwner());
        mDescription.setText(EVENT_LOAD.getDescription());

        for (int i = 0; i < date_text.length() - 5; i++) {
            char character = date_text.charAt(i);
            if (i == 0) {
                character = Character.toUpperCase(character);
            }
            final_date_text += character;
        }

        mDate.setText(final_date_text);

        /*for (int i = 0; i < EVENT_LOAD.getRegister_users().size(); i++) {
            final int finalI = i;
            mStoreBase.collection("users").document(EVENT_LOAD.getRegister_users().get(i).getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            membres_inscrits.add(new ItemImagePerson(EVENT_LOAD.getRegister_users().get(finalI).getId(), R.drawable.ic_round_person_24));
                        } else {
                            Log.d("FIREBASE", "No such document");
                        }
                    } else {
                        Log.d("FIREBASE", "get failed with ", task.getException());
                    }
                }
            });
        }*/


        //mAdapter = new ItemImagePersonAdapter(membres_inscrits, this);

        mRecyclerView = findViewById(R.id.recyclerViewMembresInscrits);
        mLayoutManager = new LinearLayoutManager(
                getBaseContext(),
                LinearLayoutManager.HORIZONTAL,
                false);

        mRecyclerView.setLayoutManager(mLayoutManager);
        //mRecyclerView.setAdapter(mAdapter);
    }
}