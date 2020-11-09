package com.example.nalone.ui.amis.display;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

import com.bumptech.glide.Glide;
import com.example.nalone.Adapter.ItemListAmisAdapter;
import com.example.nalone.CoreListener;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.R;
import com.example.nalone.User;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USERS_DB_REF;
import static com.example.nalone.util.Constants.USERS_LIST;
import static com.example.nalone.util.Constants.USERS_PICTURE_URI;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.heightScreen;
import static com.example.nalone.util.Constants.listeners;
import static com.example.nalone.util.Constants.mProfilRef;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.widthScreen;

public class AmisFragment extends Fragment implements CoreListener{

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listeners.add(this);
        rootView = inflater.inflate(R.layout.amis_fragment, container, false);
        search_bar = rootView.findViewById(R.id.search_bar_amis);
        resultat = rootView.findViewById(R.id.resultatText_amis);
        resultat.setVisibility(View.GONE);
        dialogProfil = new Dialog(getContext());

        updateItems();



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
                if (items.size() > 0) {
                    if (newText.length() > 0) {
                        for (int i = 0; i < items.size(); i++) {
                            for (int j = 0; j < newText.length(); j++) {
                                if (items.get(i).getmNomToLowerCase().length() > j) {
                                    if (newText.charAt(j) == items.get(i).getmNomToLowerCase().charAt(j) && j == 0) {
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
                                } else {
                                    tempList.remove(items.get(i));
                                }
                            }
                        }


                        if (tempList.size() == 0) {
                            resultat.setVisibility(View.VISIBLE);
                            resultat.setText(R.string.aucun_resultat);
                        }

                        mAdapter = new ItemListAmisAdapter(tempList, getContext());
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        resultat.setVisibility(View.GONE);
                        resultat.setText("");
                        mAdapter = new ItemListAmisAdapter(items, getContext());
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } else {
                    resultat.setVisibility(View.VISIBLE);
                    resultat.setText("Aucun amis à ajouter !");
                }

                return false;
            }
        });


        return rootView;
    }

    private void removeFriend(int id) {
        if(USERS_LIST.get(USER_ID).getAmis().size() == 1) {
            USERS_LIST.get(USER_ID).getAmis().set(0, "");
        }else{
            USERS_LIST.get(USER_ID).getAmis().remove(id+"");
        }

        if(USERS_LIST.get(id+"").getAmis().size() == 1) {
            USERS_LIST.get(id+"").getAmis().set(0, "");
        }else{
            USERS_LIST.get(id+"").getAmis().remove(USER_ID);
        }

        USERS_DB_REF.setValue(USERS_LIST);

        Toast.makeText(getContext(), "Vous avez supprimé un amis !", Toast.LENGTH_SHORT).show();

        updateItems();

    }



    private void updateItems() {
        items.clear();
        for (int i = 0; i < USERS_LIST.get(USER_ID).getAmis().size(); i++) {
            User u = USERS_LIST.get(USERS_LIST.get(USER_ID).getAmis().get(i));
            if(u != null) {
                int id = Integer.parseInt(USERS_LIST.get(USER_ID).getAmis().get(i));
                items.add(new ItemPerson(id, R.drawable.ic_baseline_account_circle_24,
                        u.getPrenom() + " " + u.getNom(), 0, u.getDescription(),
                        u.getVille(), u.getCursus(), u.getNbCreation(), u.getNbParticipation(), u.getCentreInterets()));
            }
        }

        mAdapter = new ItemListAmisAdapter(items, getContext());

        mRecyclerView = rootView.findViewById(R.id.recyclerViewMesAmis);
        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ItemListAmisAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                showPopUpProfil(items.get(position).getId(), items.get(position).getNom(), items.get(position).getmDescription(), items.get(position).getmNbCreate(), items.get(position).getmNbParticipate(), items.get(position).getCentresInterets());
            }

            @Override
            public void onDelete(int position) {
                removeFriend(items.get(position).getId());
            }
        });

    }

    public void showPopUpProfil(int id, String name, String desc, String nbCreate, String nbParticipate, List<String> centresInteret) {

        TextView nameProfil;
        TextView descriptionProfil;
        TextView nbCreateProfil;
        TextView nbParticipateProfil;
        ImageView buttonAdd;
        final ImageView imagePerson;
        CardView cardViewPhotoPerson;

        dialogProfil.setContentView(R.layout.popup_profil);
        nameProfil = dialogProfil.findViewById(R.id.profilName);
        descriptionProfil = dialogProfil.findViewById(R.id.profilDescription);
        nbCreateProfil = dialogProfil.findViewById(R.id.nbEventCreate);
        nbParticipateProfil = dialogProfil.findViewById(R.id.nbEventParticipe);
        buttonAdd = dialogProfil.findViewById(R.id.buttonAdd);
        imagePerson = dialogProfil.findViewById(R.id.imagePerson);
        cardViewPhotoPerson = dialogProfil.findViewById(R.id.cardViewPhotoPerson);

        if(USERS_LIST.get(id+"").getCursus().equalsIgnoreCase("Informatique")){
            cardViewPhotoPerson.setCardBackgroundColor(Color.RED);
        }
        if(USERS_LIST.get(id+"").getCursus().equalsIgnoreCase("TC")){
            cardViewPhotoPerson.setCardBackgroundColor(Color.GREEN);
        }
        if(USERS_LIST.get(id+"").getCursus().equalsIgnoreCase("MMI")){
            cardViewPhotoPerson.setCardBackgroundColor(Color.parseColor("#4B0082"));
        }
        if(USERS_LIST.get(id+"").getCursus().equalsIgnoreCase("GB")){
            cardViewPhotoPerson.setCardBackgroundColor(Color.BLUE);
        }
        if(USERS_LIST.get(id+"").getCursus().equalsIgnoreCase("LP")){
            cardViewPhotoPerson.setCardBackgroundColor(Color.GRAY);
        }

        if(USERS_PICTURE_URI.get(id+"") != null) {
            Glide.with(getContext()).load(USERS_PICTURE_URI.get(id + "")).fitCenter().centerCrop().into(imagePerson);
        }

        List<ImageView> imageCentreInteret = new ArrayList<>();

        ImageView img_centre1 = dialogProfil.findViewById(R.id.imageViewCI1);
        ImageView img_centre2 = dialogProfil.findViewById(R.id.imageViewCI2);
        ImageView img_centre3 = dialogProfil.findViewById(R.id.imageViewCI3);
        ImageView img_centre4 = dialogProfil.findViewById(R.id.imageViewCI4);
        ImageView img_centre5 = dialogProfil.findViewById(R.id.imageViewCI5);

        imageCentreInteret.add(img_centre1);
        imageCentreInteret.add(img_centre2);
        imageCentreInteret.add(img_centre3);
        imageCentreInteret.add(img_centre4);
        imageCentreInteret.add(img_centre5);

        for(int i = 0; i < centresInteret.size(); i++){
            int imgResource = 0;
            if(centresInteret.get(i).equalsIgnoreCase("programmation")){
                imgResource = R.drawable.ci_programmation;
            }else if(centresInteret.get(i).equalsIgnoreCase("musique")){
                imgResource = R.drawable.ci_musique;
            }else if(centresInteret.get(i).equalsIgnoreCase("livre")){
                imgResource = R.drawable.ci_livre;
            }else if(centresInteret.get(i).equalsIgnoreCase("film")){
                imgResource = R.drawable.ci_film;
            }else if(centresInteret.get(i).equalsIgnoreCase("video")){
                imgResource = R.drawable.ci_jeuxvideo;
            }else if(centresInteret.get(i).equalsIgnoreCase("peinture")){
                imgResource = R.drawable.ci_peinture;
            }else if(centresInteret.get(i).equalsIgnoreCase("photo")){
                imgResource = R.drawable.ci_photo;
            }else if(centresInteret.get(i).equalsIgnoreCase("sport")){
                imgResource = R.drawable.ci_sport;
            }

            imageCentreInteret.get(i).setImageResource(imgResource);
            imageCentreInteret.get(i).setVisibility(View.VISIBLE);

        }


        nameProfil.setText(name);
        descriptionProfil.setText(desc);
        nbCreateProfil.setText(nbCreate);
        nbParticipateProfil.setText(nbParticipate);
        buttonAdd.setVisibility(View.GONE);

        if (desc.matches("")) {
            descriptionProfil.setVisibility(View.GONE);
        }

        dialogProfil.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogProfil.getWindow().setLayout(widthScreen - 100, heightScreen - 200);
        dialogProfil.show();
    }

    @Override
    public void onDataChangeListener() {
        updateItems();
    }
}