package com.example.nalone.ui.recherche;

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

import com.example.nalone.HomeActivity;
import com.example.nalone.ItemPerson;
import com.example.nalone.ItemPersonAdapter;
import com.example.nalone.R;
import com.google.android.gms.common.data.BitmapTeleporter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RechercheFragment extends Fragment {

    private RechercheViewModel rechercheViewModel;
    private SearchView search_bar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView resultat;
    final List<ItemPerson> tempList = new ArrayList<>();

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
                boolean check = false;
                if(items.size() > 0) {
                    if (newText.length() > 0) {
                        for (int i = 0; i < items.size(); i++) {
                            for (int j = 0; j < newText.length(); j++) {
                                    if(items.get(i).getmNomToLowerCase().length() > j) {
                                        if (newText.charAt(j) == items.get(i).getmNomToLowerCase().charAt(j)) {
                                            check = true;
                                        } else {
                                            check = false;
                                        }
                                        Log.w("Recherche", "Valeur de check : " + check);


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

                        mAdapter = new ItemPersonAdapter(tempList);
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        resultat.setVisibility(View.GONE);
                        resultat.setText("");
                        mAdapter = new ItemPersonAdapter(items);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }else{
                    resultat.setVisibility(View.VISIBLE);
                    resultat.setText("Aucun amis à ajouter !");
                }

                return false;
            }
        });

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

                                if(liste_amis.size() == nb_users-1){
                                    Log.w("Recherche","Print aucun amis a ajouter");
                                    resultat.setVisibility(View.VISIBLE);
                                    resultat.setText("Aucun amis à ajouter !");
                                }

                                for(int j = 0; j < nb_users; j++){
                                    Log.w("Liste", "J :"+j);
                                    if(!liste_amis.contains(j+"") && j != finalI){
                                        DatabaseReference user_found = FirebaseDatabase.getInstance().getReference("users/"+j);
                                        user_found.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String prenom = snapshot.child("prenom").getValue(String.class);
                                                String nom = snapshot.child("nom").getValue(String.class);
                                                Log.w("Liste", "Ajout de :"+prenom+ " " +nom);
                                                mRecyclerView = root.findViewById(R.id.recyclerView);
                                                mLayoutManager = new LinearLayoutManager(getContext());

                                                mRecyclerView.setLayoutManager(mLayoutManager);
                                                mRecyclerView.setAdapter(mAdapter);
                                                items.add(new ItemPerson(R.drawable.ic_baseline_account_circle_24, prenom+" "+nom, R.drawable.ic_baseline_add_24));
                                                /*if(items.size() == liste_amis.size()){
                                                    dialogAddPerson.show();
                                                }*/



                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }

                                mAdapter = new ItemPersonAdapter(items);

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
}