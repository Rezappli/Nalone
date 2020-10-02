package com.example.nalone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity {
    private CardView cardViewPrivate;
    private CardView cardViewPublic;
    private ImageButton imageButtonAddInvit;
    private TextView textViewListe;
    private ImageView imageViewPublic;
    private ImageView imageViewPrivate;
    private Dialog dialogAddPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        cardViewPrivate = findViewById(R.id.cardViewPrivate);
        cardViewPublic = findViewById(R.id.cardViewPublic);
        imageButtonAddInvit = findViewById(R.id.buttonMoreInvit);
        textViewListe = findViewById(R.id.textViewListe);
        imageViewPublic = findViewById(R.id.imageViewPublic);
        imageViewPrivate = findViewById(R.id.imageViewPrivate);
        dialogAddPerson = new Dialog(this);


        cardViewPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonAddInvit.setVisibility(View.VISIBLE);
                textViewListe.setVisibility(View.VISIBLE);
                imageViewPrivate.setImageResource(R.drawable.ic_baseline_lock_focused);
                imageViewPublic.setImageResource(R.drawable.ic_baseline_public_24);

            }
        });

        cardViewPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonAddInvit.setVisibility(View.GONE);
                textViewListe.setVisibility(View.GONE);
                imageViewPublic.setImageResource(R.drawable.ic_baseline_public_focused);
                imageViewPrivate.setImageResource(R.drawable.ic_baseline_lock_24);
            }
        });





    }



    public void showPopUp(View v){

        dialogAddPerson.setContentView(R.layout.popup_add_invit);
        dialogAddPerson.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        final RecyclerView[] mRecyclerView = new RecyclerView[1];
        final RecyclerView.Adapter[] mAdapter = new RecyclerView.Adapter[1];
        final RecyclerView.LayoutManager[] mLayoutManager = new RecyclerView.LayoutManager[1];

        final ArrayList<ItemPerson> items = new ArrayList<>();

        DatabaseReference id_users_ref = FirebaseDatabase.getInstance().getReference("id_users");
        id_users_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String id_user = snapshot.getValue(String.class);
                final int nb_users = Integer.parseInt(id_user);
                for(int i = 0; i < nb_users; i++){
                    DatabaseReference user = FirebaseDatabase.getInstance().getReference("users/"+i);
                    final int finalI = i;
                    user.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String mail = snapshot.child("mail").getValue(String.class);

                                if(mail.equalsIgnoreCase(HomeActivity.user_mail)){
                                    int id_user_connect = finalI;
                                    String amis_text = snapshot.child("amis").getValue(String.class);
                                    final List<String> liste_amis = Arrays.asList(amis_text.split(","));

                                    Log.w("Liste", "Liste amis:"+amis_text);
                                    items.clear();
                                    for(int j = 0; j < nb_users; j++){
                                        Log.w("Liste", "J :"+j);
                                        if(liste_amis.contains(j+"")){
                                            DatabaseReference user_found = FirebaseDatabase.getInstance().getReference("users/"+j);
                                            user_found.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    String prenom = snapshot.child("prenom").getValue(String.class);
                                                    String nom = snapshot.child("nom").getValue(String.class);
                                                    Log.w("Liste", "Ajout de :"+prenom+ " " +nom);
                                                    mRecyclerView[0] = dialogAddPerson.findViewById(R.id.recyclerView);
                                                    mLayoutManager[0] = new LinearLayoutManager(getBaseContext());

                                                    mRecyclerView[0].setLayoutManager(mLayoutManager[0]);
                                                    mRecyclerView[0].setAdapter(mAdapter[0]);
                                                    items.add(new ItemPerson(R.drawable.ic_baseline_account_circle_24, prenom+" "+nom, R.drawable.ic_baseline_add_24));
                                                    if(items.size() == liste_amis.size()){
                                                        dialogAddPerson.show();
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }

                                    mAdapter[0] = new ItemPersonAdapter(items);

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}