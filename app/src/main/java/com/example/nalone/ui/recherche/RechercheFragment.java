package com.example.nalone.ui.recherche;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nalone.Adapter.ItemFiltreAdapter;
import com.example.nalone.CoreListener;
import com.example.nalone.CustomToast;
import com.example.nalone.HomeActivity;
import com.example.nalone.items.ItemFiltre;
import com.example.nalone.items.ItemImagePerson;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.Adapter.ItemProfilAdapter;
import com.example.nalone.R;
import com.example.nalone.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USERS_DB_REF;
import static com.example.nalone.util.Constants.USERS_LIST;
import static com.example.nalone.util.Constants.USERS_PICTURE_URI;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.USER_IMAGE_URI;
import static com.example.nalone.util.Constants.heightScreen;
import static com.example.nalone.util.Constants.listeners;
import static com.example.nalone.util.Constants.mProfilRef;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.widthScreen;

public class RechercheFragment extends Fragment implements CoreListener {


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


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listeners.add(this);
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

                        mAdapter = new ItemProfilAdapter(tempList, getContext());
                        mAdapter.setOnItemClickListener(new ItemProfilAdapter.OnItemClickListener() {
                            @Override
                            public void onAddClick(int position) {
                                if (USERS_LIST.get(USER_ID).getDemande_amis_envoye().contains(tempList.get(position).getId() + "")) {
                                    showPopUpProfil(tempList.get(position).getId(), tempList.get(position).getNom(), tempList.get(position).getmDescription(), tempList.get(position).getmNbCreate(), tempList.get(position).getmNbParticipate(), R.drawable.ic_round_hourglass_top_24, tempList.get(position).getCentresInterets());
                                } else if (USERS_LIST.get(USER_ID).getDemande_amis_recu().contains(tempList.get(position).getId() + "")) {
                                    showPopUpProfil(tempList.get(position).getId(), tempList.get(position).getNom(), tempList.get(position).getmDescription(), tempList.get(position).getmNbCreate(), tempList.get(position).getmNbParticipate(), R.drawable.ic_round_mail_24, tempList.get(position).getCentresInterets());
                                } else {
                                    showPopUpProfil(tempList.get(position).getId(), tempList.get(position).getNom(), tempList.get(position).getmDescription(), tempList.get(position).getmNbCreate(), tempList.get(position).getmNbParticipate(), R.drawable.ic_baseline_add_circle_outline_24, tempList.get(position).getCentresInterets());
                                }

                            }
                        });
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        resultat.setVisibility(View.GONE);
                        resultat.setText("");
                        mAdapter = new ItemProfilAdapter(items, getContext());
                        mAdapter.setOnItemClickListener(new ItemProfilAdapter.OnItemClickListener() {
                            @Override
                            public void onAddClick(int position) {
                                if (USERS_LIST.get(USER_ID).getDemande_amis_envoye().contains(tempList.get(position).getId() + "")) {
                                    showPopUpProfil(tempList.get(position).getId(), tempList.get(position).getNom(), tempList.get(position).getmDescription(), tempList.get(position).getmNbCreate(), tempList.get(position).getmNbParticipate(), R.drawable.ic_round_hourglass_top_24, tempList.get(position).getCentresInterets());
                                } else if (USERS_LIST.get(USER_ID).getDemande_amis_recu().contains(tempList.get(position).getId() + "")) {
                                    showPopUpProfil(tempList.get(position).getId(), tempList.get(position).getNom(), tempList.get(position).getmDescription(), tempList.get(position).getmNbCreate(), tempList.get(position).getmNbParticipate(), R.drawable.ic_round_mail_24, tempList.get(position).getCentresInterets());
                                } else {
                                    showPopUpProfil(tempList.get(position).getId(), tempList.get(position).getNom(), tempList.get(position).getmDescription(), tempList.get(position).getmNbCreate(), tempList.get(position).getmNbParticipate(), R.drawable.ic_baseline_add_circle_outline_24, tempList.get(position).getCentresInterets());
                                }


                            }
                        });
                        mRecyclerView.setAdapter(mAdapter);


                    }
                } else {
                    resultat.setVisibility(View.VISIBLE);
                    resultat.setText("Aucun amis à ajouter !");
                }


                return false;
            }
        });


        updateItems();

        if(items.size() == 0){
            resultat.setVisibility(View.VISIBLE);
            resultat.setText("Aucun amis à ajouter !");
        }else{
            resultat.setVisibility(View.GONE);
            resultat.setText("");
        }

        return rootView;

    }


    public void showPopUpProfil(final int id, String name, String desc, String nbCreate, String nbParticipate, final int button, List centresInteret) {
        TextView nameProfil;
        TextView descriptionProfil;
        TextView nbCreateProfil;
        TextView nbParticipateProfil;
        final ImageView imagePerson;
        ImageView buttonAdd;
        CardView cardViewPhotoPerson;

        dialogProfil.setContentView(R.layout.popup_profil);
        nameProfil = dialogProfil.findViewById(R.id.profilName);
        descriptionProfil = dialogProfil.findViewById(R.id.profilDescription);
        nbCreateProfil = dialogProfil.findViewById(R.id.nbEventCreate);
        nbParticipateProfil = dialogProfil.findViewById(R.id.nbEventParticipe);
        imagePerson = dialogProfil.findViewById(R.id.imagePerson);
        buttonAdd = dialogProfil.findViewById(R.id.buttonAdd);
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
            if(centresInteret.get(i).toString().equalsIgnoreCase("programmation")){
                imgResource = R.drawable.ci_programmation;
            }else if(centresInteret.get(i).toString().equalsIgnoreCase("musique")){
                imgResource = R.drawable.ci_musique;
            }else if(centresInteret.get(i).toString().equalsIgnoreCase("livre")){
                imgResource = R.drawable.ci_livre;
            }else if(centresInteret.get(i).toString().equalsIgnoreCase("film")){
                imgResource = R.drawable.ci_film;
            }else if(centresInteret.get(i).toString().equalsIgnoreCase("video")){
                imgResource = R.drawable.ci_jeuxvideo;
            }else if(centresInteret.get(i).toString().equalsIgnoreCase("peinture")){
                imgResource = R.drawable.ci_peinture;
            }else if(centresInteret.get(i).toString().equalsIgnoreCase("photo")){
                imgResource = R.drawable.ci_photo;
            }else if(centresInteret.get(i).toString().equalsIgnoreCase("sport")){
                imgResource = R.drawable.ci_sport;
            }
            
            imageCentreInteret.get(i).setImageResource(imgResource);
            imageCentreInteret.get(i).setVisibility(View.VISIBLE);

        }


        nameProfil.setText(name);
        descriptionProfil.setText(desc);
        nbCreateProfil.setText(nbCreate);
        nbParticipateProfil.setText(nbParticipate);
        buttonAdd.setImageResource(button);

        if (desc.matches("")) {
            descriptionProfil.setVisibility(View.GONE);
        }

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button == R.drawable.ic_round_mail_24) {
                    CustomToast t = new CustomToast(getContext(), "Vous avez reçu une demande d'amis de la part de cet utilisateur !", false, true);
                    t.show();
                } else if (button == R.drawable.ic_round_hourglass_top_24) {
                    CustomToast t = new CustomToast(getContext(), "Votre demande d'amis est en attente !", false, true);
                    t.show();
                } else {
                    Toast.makeText(getContext(), "Vous avez envoyé une demande d'amis !", Toast.LENGTH_SHORT).show();
                    addFriend(""+id);
                }
            }
        });

        dialogProfil.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogProfil.getWindow().setLayout(widthScreen, heightScreen);
        dialogProfil.show();

    }

    public void addFriend(final String id) {
        USERS_LIST.get(USER_ID).getDemande_amis_envoye().add(id+"");
        USERS_LIST.get(id).getDemande_amis_recu().add(USER_ID+"");

        USERS_DB_REF.setValue(USERS_LIST);

        dialogProfil.hide();

        updateItems();
    }

    public void updateItems() {
        items.clear();
        for(int i = 0; i < USERS_LIST.size(); i++){
            User u = USERS_LIST.get(i+"");
            if(u != null) {
                if (!USER_ID.equalsIgnoreCase(i + "")) {
                    if (!USERS_LIST.get(USER_ID).getAmis().contains(i+"")) {
                        ItemPerson it;
                        if (USERS_LIST.get(USER_ID).getDemande_amis_envoye().contains(i+"")) {
                            it = new ItemPerson(i, R.drawable.ic_baseline_account_circle_24, u.getPrenom() + " " + u.getNom(), R.drawable.ic_round_hourglass_top_24, u.getDescription(), u.getVille(), u.getCursus(), u.getNbCreation(), u.getNbParticipation(), u.getCentreInterets());
                        } else if (USERS_LIST.get(USER_ID).getDemande_amis_recu().contains(i+"")) {
                            it = new ItemPerson(i, R.drawable.ic_baseline_account_circle_24, u.getPrenom() + " " + u.getNom(), R.drawable.ic_round_mail_24, u.getDescription(), u.getVille(), u.getCursus(), u.getNbCreation(),u.getNbParticipation(), u.getCentreInterets());
                        } else {
                            it = new ItemPerson(i, R.drawable.ic_baseline_account_circle_24, u.getPrenom() + " " + u.getNom(), 0, u.getDescription(), u.getVille(), u.getCursus(), u.getNbCreation(),u.getNbParticipation(), u.getCentreInterets());
                        }

                        items.add(it);
                    }
                }
            }
        }

        mAdapter = new ItemProfilAdapter(items, getContext());
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                false);


        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter.setOnItemClickListener(new ItemProfilAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                if (USERS_LIST.get(USER_ID).getDemande_amis_envoye().contains(items.get(position).getId() + "")) {
                    showPopUpProfil(items.get(position).getId(), items.get(position).getNom(), items.get(position).getmDescription(), items.get(position).getmNbCreate(), items.get(position).getmNbParticipate(), R.drawable.ic_round_hourglass_top_24, items.get(position).getCentresInterets());
                } else if (USERS_LIST.get(USER_ID).getDemande_amis_recu().contains(items.get(position).getId() + "")) {
                    showPopUpProfil(items.get(position).getId(), items.get(position).getNom(), items.get(position).getmDescription(), items.get(position).getmNbCreate(), items.get(position).getmNbParticipate(), R.drawable.ic_round_mail_24, items.get(position).getCentresInterets());
                } else {
                    showPopUpProfil(items.get(position).getId(), items.get(position).getNom(), items.get(position).getmDescription(), items.get(position).getmNbCreate(), items.get(position).getmNbParticipate(), R.drawable.ic_baseline_add_circle_outline_24, items.get(position).getCentresInterets());
                }


            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDataChangeListener() {
        updateItems();
    }
}