package com.example.nalone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.nalone.Adapter.ItemImagePersonAdapter;
import com.example.nalone.items.ItemImagePerson;
import com.example.nalone.util.Constants;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.EVENTS_LIST;
import static com.example.nalone.util.Constants.USERS_LIST;
import static com.example.nalone.util.Constants.USER_LATLNG;
import static com.example.nalone.util.Constants.mFirebase;

public class InfosEvenementsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ItemImagePersonAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mTitle;
    private TextView mDate;
    private TextView mTimer;
    private TextView mOwner;
    private TextView mDescription;
    private static Marker mMarker;
    private final List<ItemImagePerson> membres_inscrits = new ArrayList<>();

    public static void initialise(Marker marker) {
        mMarker = marker;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos_evenements);

        mTitle = findViewById(R.id.title);
        mDate = findViewById(R.id.date);
        mTimer = findViewById(R.id.time);
        mOwner = findViewById(R.id.owner);
        mDescription = findViewById(R.id.description);

        for(Evenement e : EVENTS_LIST.values()){
            if(e.getId() == (int)mMarker.getTag()){
                String date_text = Constants.formatD.format(e.getDate());
                String final_date_text = "";
                mTitle.setText(e.getNom());
                mTimer.setText(e.getTime());
                mOwner.setText(USERS_LIST.get(e.getProprietaire()+"").getPrenom()+""+USERS_LIST.get(e.getProprietaire()+"").getNom());
                mDescription.setText(e.getDescription());

                for(int i = 0; i < date_text.length()-5; i++){
                    char character = date_text.charAt(i);
                    if(i == 0) {
                        character = Character.toUpperCase(character);
                    }
                    final_date_text += character;
                }

                mDate.setText(final_date_text);

                for(int i = 0; i < e.getMembres_inscrits().size(); i++){
                    membres_inscrits.add(new ItemImagePerson(R.drawable.ic_round_person_24));
                }
            }
        }


        mAdapter = new ItemImagePersonAdapter(membres_inscrits);

        mRecyclerView = findViewById(R.id.recyclerViewMembresInscrits);
        mLayoutManager = new LinearLayoutManager(
                getBaseContext(),
                LinearLayoutManager.HORIZONTAL,
                false);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}