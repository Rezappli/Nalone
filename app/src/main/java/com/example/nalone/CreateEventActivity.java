package com.example.nalone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nalone.ui.evenements.EvenementsFragment;
import com.google.android.material.textfield.TextInputEditText;
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
    private ArrayList<ItemPerson> itemsAdd = new ArrayList<>();
    private RecyclerView mRecyclerViewAdd;
    private ItemAddPersonAdapter mAdapterAdd;
    private RecyclerView.LayoutManager mLayoutManagerAdd;
    private Button buttonValidEvent;

    private TextInputEditText eventName;
    private TextInputEditText eventAdress;
    private TextInputEditText eventCity;
    private TextInputEditText eventResume;
    private Visibilite eventVisibilite;




    final ArrayList<ItemPerson> items = new ArrayList<>();


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
        mRecyclerViewAdd = findViewById(R.id.recyclerView1);

        eventAdress = findViewById(R.id.eventAdress);
        eventCity = findViewById(R.id.eventCity);
        eventName = findViewById(R.id.eventName);
        eventResume = findViewById(R.id.eventResume);
        buttonValidEvent = findViewById(R.id.button);



        mLayoutManagerAdd = new LinearLayoutManager(getBaseContext());
        mAdapterAdd = new ItemAddPersonAdapter(itemsAdd);





        cardViewPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonAddInvit.setVisibility(View.VISIBLE);
                textViewListe.setVisibility(View.VISIBLE);
                imageViewPrivate.setImageResource(R.drawable.ic_baseline_lock_focused);
                imageViewPublic.setImageResource(R.drawable.ic_baseline_public_24);
                eventVisibilite = Visibilite.PRIVE;

            }
        });

        cardViewPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonAddInvit.setVisibility(View.GONE);
                textViewListe.setVisibility(View.GONE);
                mRecyclerViewAdd.setVisibility(View.GONE);
                imageViewPublic.setImageResource(R.drawable.ic_baseline_public_focused);
                imageViewPrivate.setImageResource(R.drawable.ic_baseline_lock_24);
                eventVisibilite = Visibilite.PUBLIC;
            }
        });

        mAdapterAdd.setOnItemClickListener(new ItemAddPersonAdapter.OnItemClickListener() {
            @Override
            public void onRemoveClick(int position) {
                itemsAdd.remove(position);
                mAdapterAdd.notifyItemRemoved(position);
            }
        });

        buttonValidEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataEvent();
            }
        });





    }

    public void addPerson(View v){
        dialogAddPerson.dismiss();
        mRecyclerViewAdd.setLayoutManager(mLayoutManagerAdd);
        mRecyclerViewAdd.setAdapter(mAdapterAdd);

    }

    public void saveDataEvent(){

        final DatabaseReference id_events = FirebaseDatabase.getInstance().getReference("id_evenements");
        id_events.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String id = snapshot.getValue(String.class);
                DatabaseReference dbEventName = FirebaseDatabase.getInstance().getReference("evenements/" + id +"/nom");
                dbEventName.setValue(eventName.getText().toString());
                DatabaseReference dbEventAdresse = FirebaseDatabase.getInstance().getReference("evenements/" + id +"/adresse");
                dbEventAdresse.setValue(eventAdress.getText().toString());
                DatabaseReference dbEventVille = FirebaseDatabase.getInstance().getReference("evenements/" + id +"/ville");
                dbEventVille.setValue(eventCity.getText().toString());
                DatabaseReference dbEventDescription = FirebaseDatabase.getInstance().getReference("evenements/" + id +"/description");
                dbEventDescription.setValue(eventResume.getText().toString());
                if(eventVisibilite.equals(Visibilite.PUBLIC)){
                    DatabaseReference dbEventVisibility = FirebaseDatabase.getInstance().getReference("evenements/" + id +"/visibilite");
                    dbEventVisibility.setValue("PUBLIC");
                }else{
                    DatabaseReference dbEventVisibility = FirebaseDatabase.getInstance().getReference("evenements/" + id +"/visibilite");
                    dbEventVisibility.setValue("PRIVE");
                    String memberSignIn = "";
                    for (int i = 0; i < itemsAdd.size(); i++){
                        if(i == itemsAdd.size()-1){
                            memberSignIn+=""+i;
                        }else{
                            memberSignIn+=""+i+",";
                        }

                    }

                    final DatabaseReference dbEventMembersEnAttente = FirebaseDatabase.getInstance().getReference("evenements/" + id +"/membres_en_attente");
                    dbEventMembersEnAttente.setValue(memberSignIn);

                    final DatabaseReference dbEventMembersSignIn = FirebaseDatabase.getInstance().getReference("evenements/" + id +"/membres_inscrits");

                    DatabaseReference id_users = FirebaseDatabase.getInstance().getReference("id_users");
                    id_users.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int nb_user = Integer.parseInt(snapshot.getValue(String.class));
                            for(int i = 0; i < nb_user; i++){
                                DatabaseReference user = FirebaseDatabase.getInstance().getReference("users/"+i);
                                final int finalI = i;
                                user.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String mail = snapshot.child("mail").getValue(String.class);
                                        if(mail.equalsIgnoreCase(HomeActivity.user_mail)){
                                            dbEventMembersSignIn.setValue(finalI+"");
                                            DatabaseReference dbEventProprietaire = FirebaseDatabase.getInstance().getReference("evenements/" + id +"/proprietaire");
                                            dbEventProprietaire.setValue(finalI+"");

                                            int t = Integer.parseInt(id);
                                            t++;
                                            id_events.setValue(t+"");

                                            Intent intent = new Intent(getBaseContext(), EvenementsFragment.class);
                                            startActivity(intent);

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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

    }


    public void showPopUp(View v){

        dialogAddPerson.setContentView(R.layout.popup_add_invit);
        dialogAddPerson.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final RecyclerView[] mRecyclerView = new RecyclerView[1];
        final ItemPersonAdapter[] mAdapter = new ItemPersonAdapter[1];
        final RecyclerView.LayoutManager[] mLayoutManager = new RecyclerView.LayoutManager[1];

        final ArrayList<String> adds = new ArrayList<>();


        DatabaseReference id_users_ref = FirebaseDatabase.getInstance().getReference("id_users");
        id_users_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String id_user = snapshot.getValue(String.class);
                final int nb_users = Integer.parseInt(id_user);
                for(final int[] i = {0}; i[0] < nb_users; i[0]++){
                    DatabaseReference user = FirebaseDatabase.getInstance().getReference("users/"+ i[0]);
                    final int finalI = i[0];
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
                                    for(final int[] j = {0}; j[0] < nb_users; j[0]++){
                                        Log.w("Liste", "J :"+ j[0]);
                                        if(liste_amis.contains(j[0] +"")){
                                            DatabaseReference user_found = FirebaseDatabase.getInstance().getReference("users/"+ j[0]);
                                            final int finalJ = j[0];
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

                                                  // if(items.size() == liste_amis.size()){
                                                        dialogAddPerson.show();
                                                    //}





                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }



                                    mAdapter[0] = new ItemPersonAdapter(items);

                                    mAdapter[0].setOnItemClickListener(new ItemPersonAdapter.OnItemClickListener() {
                                        @Override
                                        public void onAddClick(int position) {
                                            String person = items.get(position).getNom();
                                            int remove = R.drawable.ic_baseline_remove_24;
                                            int add = R.drawable.ic_baseline_add_24;
                                            if(adds.contains(person) == false){
                                                items.get(position).changerPlus(remove);
                                                adds.add(person);
                                                itemsAdd.add(items.get(position));
                                            }else{
                                                items.get(position).changerPlus(add);
                                                adds.remove(person);
                                                itemsAdd.remove(items.get(position));
                                            }


                                            mAdapter[0].notifyItemChanged(position);


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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}