package com.example.nalone.ui.recherche;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.Adapter.ItemFiltreAdapter;
import com.example.nalone.Adapter.ItemImagePersonAdapter;
import com.example.nalone.CustomToast;
import com.example.nalone.ItemFiltre;
import com.example.nalone.ItemImagePerson;
import com.example.nalone.ItemPerson;
import com.example.nalone.Adapter.ItemProfilAdapter;
import com.example.nalone.R;
import com.example.nalone.UserData;
import com.example.nalone.ui.message.ChatActivity;
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

public class RechercheFragment extends Fragment {


    private RechercheViewModel rechercheViewModel;
    private SearchView search_bar;

    private RecyclerView mRecyclerView;
    private ItemProfilAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView resultat;
    private final List<ItemPerson> tempList = new ArrayList<>();
    private Dialog dialogProfil;

    private RecyclerView mRecyclerViewFiltre;
    private ItemFiltreAdapter mAdapterFiltre;
    private RecyclerView.LayoutManager mLayoutManagerFiltre;
    private final List<ItemPerson> items = new ArrayList<>();
    private final List<ItemFiltre> filtres = new ArrayList<>();
    private View rootView;
    private String amis_envoye = "";
    private String amis_recu = "";



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rechercheViewModel =
                ViewModelProviders.of(this).get(RechercheViewModel.class);
        rootView = inflater.inflate(R.layout.fragment_recherche, container, false);

        search_bar = rootView.findViewById(R.id.search_bar);
        resultat = rootView.findViewById(R.id.resultatText);
        resultat.setVisibility(View.GONE);
        dialogProfil = new Dialog(getContext());

        filtres.add(new ItemFiltre("TC"));
        filtres.add(new ItemFiltre("MMI"));
        filtres.add(new ItemFiltre("INFO"));
        filtres.add(new ItemFiltre("GB"));

        mAdapterFiltre = new ItemFiltreAdapter(filtres);

        mRecyclerViewFiltre = rootView.findViewById(R.id.recyclerViewFiltre);
        mLayoutManagerFiltre = new LinearLayoutManager(
                rootView.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        mRecyclerViewFiltre.setLayoutManager(mLayoutManagerFiltre);
        mRecyclerViewFiltre.setAdapter(mAdapterFiltre);



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
                        mAdapter.setOnItemClickListener(new ItemProfilAdapter.OnItemClickListener() {
                            @Override
                            public void onAddClick(int position) {
                                Log.w("Amis", "Amis envoye : " +amis_envoye);
                                Log.w("Amis", "Amis recu : " +amis_recu);
                                Log.w("Amis", "Comparaison de :"+position);
                                if(amis_envoye.contains(tempList.get(position).getId()+"")){
                                    showPopUpProfil(tempList.get(position).getId(), tempList.get(position).getNom(), tempList.get(position).getmDescription(), tempList.get(position).getmNbCreate(), tempList.get(position).getmNbParticipate(),R.drawable.ic_round_hourglass_top_24);
                                }else if(amis_recu.contains(tempList.get(position).getId()+"")){
                                    showPopUpProfil(tempList.get(position).getId(), tempList.get(position).getNom(), tempList.get(position).getmDescription(), tempList.get(position).getmNbCreate(), tempList.get(position).getmNbParticipate(),R.drawable.ic_round_mail_24);
                                }else{
                                    showPopUpProfil(tempList.get(position).getId(), tempList.get(position).getNom(), tempList.get(position).getmDescription(), tempList.get(position).getmNbCreate(), tempList.get(position).getmNbParticipate(),R.drawable.ic_baseline_add_circle_outline_24);
                                }

                            }
                        });
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        resultat.setVisibility(View.GONE);
                        resultat.setText("");
                        mAdapter = new ItemProfilAdapter(items);
                        mAdapter.setOnItemClickListener(new ItemProfilAdapter.OnItemClickListener() {
                            @Override
                            public void onAddClick(int position) {
                                Log.w("Amis", "Amis envoye : " +amis_envoye);
                                Log.w("Amis", "Amis recu : " +amis_recu);
                                Log.w("Amis", "Comparaison de :"+position);
                                if(amis_envoye.contains(items.get(position).getId()+"")){
                                    showPopUpProfil(items.get(position).getId(), items.get(position).getNom(), items.get(position).getmDescription(), items.get(position).getmNbCreate(), items.get(position).getmNbParticipate(),R.drawable.ic_round_hourglass_top_24);
                                }else if(amis_recu.contains(items.get(position).getId()+"")){
                                    showPopUpProfil(items.get(position).getId(), items.get(position).getNom(), items.get(position).getmDescription(), items.get(position).getmNbCreate(), items.get(position).getmNbParticipate(),R.drawable.ic_round_mail_24);
                                }else{
                                    showPopUpProfil(items.get(position).getId(), items.get(position).getNom(), items.get(position).getmDescription(), items.get(position).getmNbCreate(), items.get(position).getmNbParticipate(),R.drawable.ic_baseline_add_circle_outline_24);
                                }

                            }
                        });
                        mRecyclerView.setAdapter(mAdapter);



                    }
                }else{
                    resultat.setVisibility(View.VISIBLE);
                    resultat.setText("Aucun amis à ajouter !");
                }



                return false;
            }
        });



        updateItems();

        return rootView;

    }



    public void showPopUpProfil(final int id, String name, String desc, String nbCreate, String nbParticipate, final int button){
         TextView nameProfil;
         TextView descriptionProfil;
         TextView nbCreateProfil;
         TextView nbParticipateProfil;
         ImageView buttonAdd;

        dialogProfil.setContentView(R.layout.popup_profil);
        nameProfil = dialogProfil.findViewById(R.id.profilName);
        descriptionProfil = dialogProfil.findViewById(R.id.profilDescription);
        nbCreateProfil = dialogProfil.findViewById(R.id.nbEventCreate);
        nbParticipateProfil = dialogProfil.findViewById(R.id.nbEventParticipe);
        buttonAdd = dialogProfil.findViewById(R.id.buttonAdd);


        nameProfil.setText(name);
        descriptionProfil.setText(desc);
        nbCreateProfil.setText(nbCreate);
        nbParticipateProfil.setText(nbParticipate);
        buttonAdd.setImageResource(button);

        if(desc.matches("")){
            descriptionProfil.setVisibility(View.GONE);
        }

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button == R.drawable.ic_round_mail_24){
                    CustomToast t = new CustomToast(getContext(), "Vous avez reçu une demande d'amis de la part de cet utilisateur !", false ,true);
                    t.show();
                }else if(button == R.drawable.ic_round_hourglass_top_24){
                    CustomToast t = new CustomToast(getContext(), "Votre demande d'amis est en attente !", false ,true);
                    t.show();
                }else {
                    addFriend(id);
                }
            }
        });

        dialogProfil.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogProfil.getWindow().setLayout(getResources().getDisplayMetrics().widthPixels - 100, getResources().getDisplayMetrics().heightPixels - 200);
        dialogProfil.show();

    }

    public void addFriend(final int id){
        final DatabaseReference mDatabase = firebaseDatabase.getInstance().getReference("users").child(user_id).child("demande_amis_envoye");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String demande_amis_envoye = dataSnapshot.getValue(String.class);
                if(demande_amis_envoye.length() > 0){
                    demande_amis_envoye += ","+id;
                }else{
                    demande_amis_envoye = ""+id;
                }
                mDatabase.setValue(demande_amis_envoye);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final DatabaseReference mDatabase2 = firebaseDatabase.getInstance().getReference("users").child(""+id).child("demande_amis_recu");
        mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String demande_amis_recu = dataSnapshot.getValue(String.class);
                if(demande_amis_recu.length() > 0){
                    demande_amis_recu += ","+user_id;
                }else{
                    demande_amis_recu = ""+user_id;
                }
                mDatabase2.setValue(demande_amis_recu);
                updateItems();

                Toast.makeText(getContext(), "Vous avez envoyer une demande à cet utilisateur !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dialogProfil.hide();
    }

    public void updateItems(){
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
                            final String maVille = snapshot.child("ville").getValue(String.class);
                            Log.w("Mail", "mail trouvé : "+mail);
                            Log.w("Mail", "user mail trouvé : "+user_mail);
                            if(mail.equalsIgnoreCase(user_mail)){
                                int id_user_connect = finalI;
                                String amis_text = snapshot.child("amis").getValue(String.class);
                                amis_envoye = snapshot.child("demande_amis_envoye").getValue(String.class);
                                amis_recu = snapshot.child("demande_amis_recu").getValue(String.class);
                                final List<String> liste_amis = Arrays.asList(amis_text.split(","));

                                Log.w("Liste", "Liste amis:"+amis_text);
                                items.clear();

                                if(liste_amis.size() == nb_users-1){
                                    Log.w("Recherche","Print aucun amis a ajouter");
                                    resultat.setVisibility(View.VISIBLE);
                                    resultat.setText("Aucun amis à ajouter !");
                                }

                                for(int j = 0; j < nb_users; j++){
                                        if (!liste_amis.contains(j + "") &&
                                                j != finalI) {
                                            DatabaseReference user_found = Constants.firebaseDatabase.getReference("users/" + j);
                                            final int finalJ = j;
                                            user_found.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    boolean presDeMoi = false;
                                                    String prenom = snapshot.child("prenom").getValue(String.class);
                                                    String nom = snapshot.child("nom").getValue(String.class);
                                                    String desc = snapshot.child("description").getValue(String.class);
                                                    String nbCreate = snapshot.child("nombre_creation").getValue(String.class);
                                                    String nbParticipate = snapshot.child("nombre_participation").getValue(String.class);
                                                    String ville = snapshot.child("ville").getValue(String.class);
                                                    Log.w("Liste", "Ajout de :" + prenom + " " + nom + " " + ville);

                                                    mRecyclerView = rootView.findViewById(R.id.recyclerView);
                                                    mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                                                            false);

                                                    mRecyclerView.setLayoutManager(mLayoutManager);
                                                    mRecyclerView.setAdapter(mAdapter);
                                                    if (maVille == ville) {
                                                        presDeMoi = true;
                                                    }
                                                    ItemPerson it;
                                                    if(amis_envoye.contains(finalJ+"") && finalJ != finalI){
                                                        it = new ItemPerson(finalJ, R.drawable.ic_baseline_account_circle_24, prenom + " " + nom, R.drawable.ic_round_hourglass_top_24, desc, ville, nbCreate, nbParticipate);
                                                    }else if(amis_recu.contains(finalJ+"") && finalJ != finalI){
                                                        it = new ItemPerson(finalJ, R.drawable.ic_baseline_account_circle_24, prenom + " " + nom, R.drawable.ic_round_mail_24, desc, ville, nbCreate, nbParticipate);
                                                    }else{
                                                        it = new ItemPerson(finalJ, R.drawable.ic_baseline_account_circle_24, prenom + " " + nom, 0, desc, ville, nbCreate, nbParticipate);
                                                    }

                                                    for(int i = 0; i < items.size(); i++){
                                                        if(items.get(i).getId() == it.getId()){
                                                            items.remove(it);
                                                        }
                                                    }
                                                    items.add(it);

                                                /*if(items.size() == liste_amis.size()){
                                                    dialogAddPerson.show();
                                                }*/


                                                    mAdapter.setOnItemClickListener(new ItemProfilAdapter.OnItemClickListener() {
                                                        @Override
                                                        public void onAddClick(int position) {
                                                            Log.w("Amis", "Amis envoye : " +amis_envoye);
                                                            Log.w("Amis", "Amis recu : " +amis_recu);
                                                            Log.w("Amis", "Comparaison de :"+position);
                                                            if(amis_envoye.contains(items.get(position).getId()+"") && finalJ != finalI){
                                                                showPopUpProfil(items.get(position).getId(), items.get(position).getNom(), items.get(position).getmDescription(), items.get(position).getmNbCreate(), items.get(position).getmNbParticipate(),R.drawable.ic_round_hourglass_top_24);
                                                            }else if(amis_recu.contains(items.get(position).getId()+"") && finalJ != finalI){
                                                                showPopUpProfil(items.get(position).getId(), items.get(position).getNom(), items.get(position).getmDescription(), items.get(position).getmNbCreate(), items.get(position).getmNbParticipate(),R.drawable.ic_round_mail_24);
                                                            }else{
                                                                showPopUpProfil(items.get(position).getId(), items.get(position).getNom(), items.get(position).getmDescription(), items.get(position).getmNbCreate(), items.get(position).getmNbParticipate(),R.drawable.ic_baseline_add_circle_outline_24);
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
    }

}