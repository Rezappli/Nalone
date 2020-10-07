package com.example.nalone.ui.recherche;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.ItemPerson;
import com.example.nalone.ItemProfilAdapter;
import com.example.nalone.R;
import com.example.nalone.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.nalone.util.Constants.user_mail;

public class RechercheFragment extends Fragment {

    private RechercheViewModel rechercheViewModel;
    private SearchView search_bar;

    private RecyclerView mRecyclerView;
    private ItemProfilAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView resultat;
    final List<ItemPerson> tempList = new ArrayList<>();
    private Dialog dialogProfil;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        final List<ItemPerson> items = new ArrayList<>();

        rechercheViewModel =
                ViewModelProviders.of(this).get(RechercheViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_recherche, container, false);

        search_bar = root.findViewById(R.id.search_bar);
        resultat = root.findViewById(R.id.resultatText);
        resultat.setVisibility(View.GONE);
        dialogProfil = new Dialog(getContext());


        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    search_bar.onActionViewExpanded();
            }
        });

        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                tempList.clear();
                boolean check = true;
                if(items.size() > 0) {
                    if (newText.length() > 0) {
                        for (int i = 0; i < items.size(); i++) {
                            for (int j = 0; j < newText.length(); j++) {
                                    if(items.get(i).getmNomToLowerCase().length() > j) {
                                        if (newText.charAt(j) == items.get(i).getmNomToLowerCase().charAt(j) && j == 0){
                                            check = true;
                                        }


                                        if (newText.charAt(j) == items.get(i).getmNomToLowerCase().charAt(j) && check) {
                                            check = true;
                                        } else {
                                            check = false;
                                        }


                                        if (check) {
                                            if (!tempList.contains(items.get(i))) {
                                                tempList.add(items.get(i));
                                                if (resultat.getVisibility() == View.VISIBLE) {
                                                    resultat.setVisibility(View.GONE);
                                                    resultat.setText("");
                                                }
                                            }
                                        } else {
                                            tempList.remove(items.get(i));
                                        }
                                    }else{
                                        tempList.remove(items.get(i));
                                    }
                            }
                        }


                        if (tempList.size() == 0) {
                            resultat.setVisibility(View.VISIBLE);
                            resultat.setText(R.string.aucun_resultat);
                        }

                        mAdapter = new ItemProfilAdapter(tempList);
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        resultat.setVisibility(View.GONE);
                        resultat.setText("");
                        mAdapter = new ItemProfilAdapter(items);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }else{
                    resultat.setVisibility(View.VISIBLE);
                    resultat.setText("Aucun amis à ajouter !");
                }

                return false;
            }
        });

        DatabaseReference id_users_ref = Constants.firebaseDatabase.getReference("id_users");
        id_users_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String id_user = snapshot.getValue(String.class);
                final int nb_users = Integer.parseInt(id_user);
                for(int i = 0; i < nb_users; i++){
                    DatabaseReference user = Constants.firebaseDatabase.getReference("users/"+i);
                    final int finalI = i;
                    user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String mail = snapshot.child("mail").getValue(String.class);

                            if(mail.equalsIgnoreCase(user_mail)){
                                int id_user_connect = finalI;
                                String amis_text = snapshot.child("amis").getValue(String.class);
                                final List<String> liste_amis = Arrays.asList(amis_text.split(","));

                                Log.w("Liste", "Liste amis:"+amis_text);
                                items.clear();

                                if(liste_amis.size() == nb_users-1){
                                    Log.w("Recherche","Print aucun amis a ajouter");
                                    resultat.setVisibility(View.VISIBLE);
                                    resultat.setText("Aucun amis à ajouter !");
                                }

                                for(int j = 0; j < nb_users; j++){
                                    Log.w("Liste", "J :"+j);
                                    if(!liste_amis.contains(j+"") && j != finalI){
                                        DatabaseReference user_found = Constants.firebaseDatabase.getReference("users/"+j);
                                        final int finalJ = j;
                                        user_found.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String prenom = snapshot.child("prenom").getValue(String.class);
                                                String nom = snapshot.child("nom").getValue(String.class);
                                                String desc = snapshot.child("description").getValue( String.class );
                                                String nbCreate = snapshot.child("nombre_creation").getValue(String.class);
                                                String nbParticipate = snapshot.child("nombre_participation").getValue(String.class);
                                                Log.w("Liste", "Ajout de :"+prenom+ " " +nom);
                                                mRecyclerView = root.findViewById(R.id.recyclerView);
                                                mLayoutManager = new LinearLayoutManager(getContext());

                                                mRecyclerView.setLayoutManager(mLayoutManager);
                                                mRecyclerView.setAdapter(mAdapter);
                                                items.add(new ItemPerson(finalJ,R.drawable.ic_baseline_account_circle_24, prenom+" "+nom, 0, desc, nbCreate, nbParticipate));
                                                /*if(items.size() == liste_amis.size()){
                                                    dialogAddPerson.show();
                                                }*/

                                                mAdapter.setOnItemClickListener(new ItemProfilAdapter.OnItemClickListener() {
                                                    @Override
                                                    public void onAddClick(int position) {
                                                        showPopUpProfil(items.get(position).getNom(), items.get(position).getmDescription(), items.get(position).getmNbCreate(), items.get(position).getmNbParticipate());
                                                    }
                                                });


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }

                                mAdapter = new ItemProfilAdapter(items);

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


        /*mRecyclerView = root.findViewById(R.id.recyclerView);
        mAdapter = new ItemPersonAdapter(items);
        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);*/

        return root;

    }

    public void showPopUpProfil(String name, String desc, String nbCreate, String nbParticipate){
         TextView nameProfil;
         TextView descriptionProfil;
         TextView nbCreateProfil;
         TextView nbParticipateProfil;

        dialogProfil.setContentView(R.layout.popup_profil);
        nameProfil = dialogProfil.findViewById(R.id.profilName);
        descriptionProfil = dialogProfil.findViewById(R.id.profilDescription);
        nbCreateProfil = dialogProfil.findViewById(R.id.nbEventCreate);
        nbParticipateProfil = dialogProfil.findViewById(R.id.nbEventParticipe);


        nameProfil.setText(name);
        descriptionProfil.setText(desc);
        nbCreateProfil.setText(nbCreate);
        nbParticipateProfil.setText(nbParticipate);

        if(desc.matches("")){
            descriptionProfil.setVisibility(View.GONE);
        }

        dialogProfil.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogProfil.getWindow().setLayout(900, 1500);
        dialogProfil.show();

    }

}