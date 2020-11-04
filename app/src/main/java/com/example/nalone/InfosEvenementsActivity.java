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

        DatabaseReference event =  mFirebase.getReference("evenements/"+mMarker.getTag());
        event.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                getSupportActionBar().setTitle("Informations sur un évènement");
                final List<ItemImagePerson> membres_inscrits = new ArrayList<>();
                final Evenement e = snapshot.getValue(Evenement.class);


                DatabaseReference user = mFirebase.getReference("users/"+e.getProprietaire());
                user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String date_text = Constants.formatD.format(e.getDate());
                        String final_date_text = "";
                        for(int i = 0; i < date_text.length(); i++){
                            char character = date_text.charAt(i);
                            if(i == 0) {
                                character = Character.toUpperCase(character);
                            }
                            final_date_text += character;
                        }
                        mTitle.setText(e.getNom());
                        mDate.setText(final_date_text);
                        mTimer.setText(e.getTime());
                        mOwner.setText(snapshot.child("prenom").getValue(String.class)+" "+snapshot.child("nom").getValue(String.class));
                        mDescription.setText(e.getDescription());

                        //users.add(new ItemImagePerson(R.drawable.background_image));
                        for(int i = 0; i < 10; i++){
                            membres_inscrits.add(new ItemImagePerson(R.drawable.ci_musique));
                        }

                        mAdapter = new ItemImagePersonAdapter(membres_inscrits);

                        mRecyclerView = findViewById(R.id.recyclerViewMembresInscrits);
                        mLayoutManager = new LinearLayoutManager(
                                getBaseContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false);

                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.setAdapter(mAdapter);


                        Log.w("membres", "membres : "+membres_inscrits.size());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });



    }
}