package com.example.nalone.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.Adapter.ItemMessageAdapter;
import com.example.nalone.ItemPerson;
import com.example.nalone.R;
import com.example.nalone.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.nalone.util.Constants.user_mail;

public class MessagesFragment extends Fragment  {


    private static final String TAG = "ChatListAct";

    FirebaseDatabase database = FirebaseDatabase.getInstance();





    private List<ItemPerson> items = new ArrayList<>();
    private SearchView search_bar;

    private RecyclerView mRecyclerView;
    private ItemMessageAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView resultat;

    final List<ItemPerson> tempList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_messages, container, false);


        search_bar = root.findViewById(R.id.search_bar_message);
        resultat = root.findViewById(R.id.resul_message);

       loadData(root);
        //Log.w("valeur", s1[0]);

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

                        mAdapter = new ItemMessageAdapter(tempList);
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        resultat.setVisibility(View.GONE);
                        resultat.setText("");
                        mAdapter = new ItemMessageAdapter(items);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }else{
                    resultat.setVisibility(View.VISIBLE);
                    resultat.setText("Aucun amis à ajouter !");
                }

                return false;
            }
        });

        return root;
    }


    public void loadData(final View root) {
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
                                    if(liste_amis.contains(j+"") && j != finalI){
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
                                                String ville = snapshot.child("ville").getValue(String.class);
                                                Log.w("Liste", "Ajout de :"+prenom+ " " +nom);
                                                mRecyclerView = root.findViewById(R.id.recycler);
                                                mLayoutManager = new LinearLayoutManager(getContext());

                                                mRecyclerView.setLayoutManager(mLayoutManager);
                                                mRecyclerView.setAdapter(mAdapter);
                                                items.add(new ItemPerson(finalJ,R.drawable.ic_baseline_account_circle_24, prenom+" "+nom, 0, desc, ville, nbCreate, nbParticipate));
                                                /*if(items.size() == liste_amis.size()){
                                                    dialogAddPerson.show();
                                                }*/

                                                Log.w("Adapter", "Adapter : "+mAdapter.getItemCount());
                                                mAdapter.setOnItemClickListener(new ItemMessageAdapter.OnItemClickListener() {
                                                    @Override
                                                    public void onAddClick(int position) {
                                                        Log.w(TAG,"UI");
                                                        OnChatClick();
                                                    }
                                                });


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }

                                mAdapter = new ItemMessageAdapter(items);

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




    public void OnChatClick() {
        Intent intent = new Intent(getContext(),ChatActivity.class);
        Log.d(TAG, "onChatClick: clicked");
        startActivity(intent);
    }



}