package com.example.nalone.ui.amis.display;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nalone.Adapter.ItemListAmisAdapter;
import com.example.nalone.Adapter.ItemProfilAdapter;
import com.example.nalone.CustomToast;
import com.example.nalone.ItemPerson;
import com.example.nalone.R;
import com.example.nalone.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.nalone.util.Constants.firebaseDatabase;
import static com.example.nalone.util.Constants.user_id;
import static com.example.nalone.util.Constants.user_mail;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AmisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AmisFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SearchView search_bar;
    private RecyclerView mRecyclerView;
    private ItemListAmisAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView resultat;
    private final List<ItemPerson> tempList = new ArrayList<>();
    private Dialog dialogProfil;
    private final List<ItemPerson> items = new ArrayList<>();
    private View rootView;

    public AmisFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_1.
     */
    // TODO: Rename and change types and number of parameters
    public static AmisFragment newInstance(String param1, String param2) {
        AmisFragment fragment = new AmisFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.amis_fragment, container, false);
        search_bar = rootView.findViewById(R.id.search_bar_amis);
        resultat = rootView.findViewById(R.id.resultatText_amis);
        resultat.setVisibility(View.GONE);
        dialogProfil = new Dialog(getContext());


        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_bar.onActionViewExpanded();
            }
        });

        updateItems();

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

                        mAdapter = new ItemListAmisAdapter(tempList);
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        resultat.setVisibility(View.GONE);
                        resultat.setText("");
                        mAdapter = new ItemListAmisAdapter(items);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }else{
                    resultat.setVisibility(View.VISIBLE);
                    resultat.setText("Aucun amis à ajouter !");
                }

                return false;
            }
        });


        return rootView;
    }

    private void removeFriend(final String id, final String prenom) {
        final DatabaseReference mDatabase = firebaseDatabase.getInstance().getReference("users").child(user_id).child("amis");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String amis = dataSnapshot.getValue(String.class);

                 amis = amis.replace(","+id, "");
                 amis = amis.replace(id, "");

                mDatabase.setValue(amis);
                CustomToast t = new CustomToast(getContext(), "Vous avez supprimer cet utilisateur", false, true);
                t.show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final DatabaseReference mDatabase1 = firebaseDatabase.getInstance().getReference("users").child(id+"").child("amis");

        mDatabase1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String amis = dataSnapshot.getValue(String.class);

                amis = amis.replace(","+user_id, "");
                amis = amis.replace(user_id, "");

                mDatabase1.setValue(amis);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        updateItems();

    }

    private void updateItems() {
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
                                                final String prenom = snapshot.child("prenom").getValue(String.class);
                                                final String nom = snapshot.child("nom").getValue(String.class);
                                                String desc = snapshot.child("description").getValue( String.class );
                                                String nbCreate = snapshot.child("nombre_creation").getValue(String.class);
                                                String nbParticipate = snapshot.child("nombre_participation").getValue(String.class);
                                                String ville = snapshot.child("ville").getValue(String.class);
                                                String cursus = snapshot.child("cursus").getValue(String.class);
                                                Log.w("Liste", "Ajout de :"+prenom+ " " +nom);
                                                mRecyclerView = rootView.findViewById(R.id.recyclerViewMesAmis);
                                                mLayoutManager = new LinearLayoutManager(getContext());

                                                mRecyclerView.setLayoutManager(mLayoutManager);
                                                mRecyclerView.setAdapter(mAdapter);
                                                items.add(new ItemPerson(finalJ,R.drawable.ic_baseline_account_circle_24, prenom+" "+nom, 0, desc, ville, cursus, nbCreate, nbParticipate));
                                                /*if(items.size() == liste_amis.size()){
                                                    dialogAddPerson.show();
                                                }*/

                                                mAdapter.setOnItemClickListener(new ItemListAmisAdapter.OnItemClickListener() {
                                                    @Override
                                                    public void onAddClick(int position) {
                                                        showPopUpProfil(items.get(position).getNom(), items.get(position).getmDescription(), items.get(position).getmNbCreate(), items.get(position).getmNbParticipate());
                                                    }

                                                    @Override
                                                    public void onDelete(int position) {
                                                        if(items.size() > 0){
                                                            removeFriend(items.get(position).getId()+"", items.get(position).getNom());
                                                        }
                                                    }
                                                });


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }

                                mAdapter = new ItemListAmisAdapter(items);

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

    public void showPopUpProfil(String name, String desc, String nbCreate, String nbParticipate){

        TextView nameProfil;
        TextView descriptionProfil;
        TextView nbCreateProfil;
        TextView nbParticipateProfil;
        ImageView button;

        dialogProfil.setContentView(R.layout.popup_profil);
        nameProfil = dialogProfil.findViewById(R.id.profilName);
        descriptionProfil = dialogProfil.findViewById(R.id.profilDescription);
        nbCreateProfil = dialogProfil.findViewById(R.id.nbEventCreate);
        nbParticipateProfil = dialogProfil.findViewById(R.id.nbEventParticipe);
        button = dialogProfil.findViewById(R.id.buttonAdd);
        button.setVisibility(View.GONE);


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