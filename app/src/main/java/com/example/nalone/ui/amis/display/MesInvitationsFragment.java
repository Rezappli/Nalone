package com.example.nalone.ui.amis.display;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nalone.Adapter.ItemInvitAmisAdapter;
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
import static com.example.nalone.util.Constants.getBytesFromBitmap;
import static com.example.nalone.util.Constants.user_id;
import static com.example.nalone.util.Constants.user_mail;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MesInvitationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MesInvitationsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ItemInvitAmisAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final List<ItemPerson> invits = new ArrayList<>();
    private View rootView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MesInvitationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_3.
     */
    // TODO: Rename and change types and number of parameters
    public static MesInvitationsFragment newInstance(String param1, String param2) {
        MesInvitationsFragment fragment = new MesInvitationsFragment();
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

       rootView = inflater.inflate(R.layout.fragment_3, container, false);

        updateItems();

        return rootView;
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
                                String invit_text = snapshot.child("demande_amis_recu").getValue(String.class);
                                final List<String> liste_amis = Arrays.asList(invit_text.split(","));

                                Log.w("Liste", "Liste amis:"+invit_text);
                                invits.clear();

                                if(liste_amis.size() == nb_users-1){
                                    Log.w("Recherche","Print aucun amis a ajouter");
                                    // resultat.setVisibility(View.VISIBLE);
                                    // resultat.setText("Aucun amis à ajouter !");
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
                                                mRecyclerView = rootView.findViewById(R.id.recyclerViewInvitAmis);
                                                mLayoutManager = new LinearLayoutManager(getContext());

                                                mRecyclerView.setLayoutManager(mLayoutManager);
                                                mRecyclerView.setAdapter(mAdapter);
                                                invits.add(new ItemPerson(finalJ,R.drawable.ic_baseline_account_circle_24, prenom+" "+nom, 0, desc, ville,nbCreate, nbParticipate));
                                                /*if(items.size() == liste_amis.size()){
                                                    dialogAddPerson.show();
                                                }*/
                                                mAdapter.setOnItemClickListener(new ItemInvitAmisAdapter.OnItemClickListener() {
                                                    @Override
                                                    public void onAddClick(int position) {
                                                        addFriend(invits.get(position).getId());
                                                    }

                                                    @Override
                                                    public void onRemoveClick(int position) {
                                                        removeFriend(invits.get(position).getId());
                                                    }
                                                });


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }

                                mAdapter = new ItemInvitAmisAdapter(invits);

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

    private void addFriend(final int id) {
        final DatabaseReference mDatabase = firebaseDatabase.getInstance().getReference("users").child(id+"").child("demande_amis_envoye");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String demande_amis_envoye = dataSnapshot.getValue(String.class);
                if(demande_amis_envoye.length() > 0){
                    if(demande_amis_envoye.length() == 1){
                        demande_amis_envoye = demande_amis_envoye.replace(user_id, "");
                    }else{
                        demande_amis_envoye = demande_amis_envoye.replace(","+user_id, "");
                    }
                }else{
                    demande_amis_envoye = "";
                }
                mDatabase.setValue(demande_amis_envoye);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final DatabaseReference mDatabase2 = firebaseDatabase.getInstance().getReference("users").child(user_id).child("demande_amis_recu");
        mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String demande_amis_recu = dataSnapshot.getValue(String.class);
                if(demande_amis_recu.length() > 0){
                    if(demande_amis_recu.length() == 1){
                        demande_amis_recu = demande_amis_recu.replace(id+"", "");
                    }else{
                        demande_amis_recu = demande_amis_recu.replace(","+id, "");
                    }
                }else{
                    demande_amis_recu = "";
                }
                mDatabase2.setValue(demande_amis_recu);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final DatabaseReference mDatabase3 = firebaseDatabase.getInstance().getReference("users").child(user_id).child("amis");
        mDatabase3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String amis_text = dataSnapshot.getValue(String.class);
                if(amis_text.length() > 0){
                    amis_text += ","+id;
                }else{
                    amis_text = id+"";
                }
                mDatabase3.setValue(amis_text);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final DatabaseReference mDatabase4 = firebaseDatabase.getInstance().getReference("users").child(""+id).child("amis");
        mDatabase4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String amis_text = dataSnapshot.getValue(String.class);
                if(amis_text.length() > 0){
                    if(amis_text.length() == 1){
                        amis_text += user_id;
                    }else{
                        amis_text += ","+user_id;
                    }
                }else{
                    amis_text = user_id;
                }
                mDatabase4.setValue(amis_text);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        updateItems();
        CustomToast t = new CustomToast(getContext(), "Vous avez ajouter cet utilisateur", false, true);
        t.show();
    }

    private void removeFriend(final int id) {
        final DatabaseReference mDatabase = firebaseDatabase.getInstance().getReference("users").child(id+"").child("demande_amis_envoye");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String demande_amis_envoye = dataSnapshot.getValue(String.class);
                if(demande_amis_envoye.length() > 0){
                    if(demande_amis_envoye.length() == 1){
                        demande_amis_envoye = demande_amis_envoye.replace(user_id, "");
                    }else{
                        demande_amis_envoye = demande_amis_envoye.replace(","+user_id, "");
                    }
                }else{
                    demande_amis_envoye = "";
                }
                mDatabase.setValue(demande_amis_envoye);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final DatabaseReference mDatabase2 = firebaseDatabase.getInstance().getReference("users").child(""+user_id).child("demande_amis_recu");
        mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String demande_amis_recu = dataSnapshot.getValue(String.class);
                if(demande_amis_recu.length() > 0){
                    if(demande_amis_recu.length() == 1){
                        demande_amis_recu = demande_amis_recu.replace(user_id, "");
                    }else{
                        demande_amis_recu = demande_amis_recu.replace(","+user_id, "");
                    }
                }else{
                    demande_amis_recu = "";
                }
                mDatabase2.setValue(demande_amis_recu);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        updateItems();
        CustomToast t = new CustomToast(getContext(), "Vous avez supprimer cet utilisateur", false, true);
        t.show();
    }
}