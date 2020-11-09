package com.example.nalone.ui.amis.display;

import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nalone.Adapter.ItemProfilAdapter;
import com.example.nalone.CustomToast;
import com.example.nalone.R;
import com.example.nalone.User;
import com.example.nalone.items.ItemPerson;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USERS_DB_REF;
import static com.example.nalone.util.Constants.USERS_LIST;
import static com.example.nalone.util.Constants.USERS_PICTURE_URI;
import static com.example.nalone.util.Constants.USER_ID;

public class PopupProfilFragment extends Fragment {

    public static String name;
    public static String ville;
    public static int id;
    public static List centresInteret;
    public static String desc;
    public static String nbCreate;
    public static String nbParticipate;
    public static int button;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_popup_profil, container, false);

        TextView nameProfil;
        TextView descriptionProfil;
        TextView nbCreateProfil;
        TextView nbParticipateProfil;
        TextView villeProfil;

        final ImageView imagePerson;
        final ImageView buttonAdd;
        CardView cardViewPhotoPerson;

        nameProfil = root.findViewById(R.id.profilName);
        descriptionProfil = root.findViewById(R.id.profilDescription);
        nbCreateProfil = root.findViewById(R.id.nbEventCreate);
        nbParticipateProfil = root.findViewById(R.id.nbEventParticipe);
        imagePerson = root.findViewById(R.id.imagePerson);
        buttonAdd = root.findViewById(R.id.buttonAdd);
        cardViewPhotoPerson = root.findViewById(R.id.cardViewPhotoPerson);
        villeProfil = root.findViewById(R.id.userConnectVille);


        villeProfil.setText(ville);

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

        ImageView img_centre1 = root.findViewById(R.id.imageViewCI1);
        ImageView img_centre2 = root.findViewById(R.id.imageViewCI2);
        ImageView img_centre3 = root.findViewById(R.id.imageViewCI3);
        ImageView img_centre4 = root.findViewById(R.id.imageViewCI4);
        ImageView img_centre5 = root.findViewById(R.id.imageViewCI5);

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
                    buttonAdd.setImageResource(R.drawable.ic_round_hourglass_top_24);
                }
            }
        });



        return root;
    }

    public void addFriend(final String id) {
        USERS_LIST.get(USER_ID).getDemande_amis_envoye().add(id+"");
        USERS_LIST.get(id).getDemande_amis_recu().add(USER_ID+"");

        USERS_DB_REF.setValue(USERS_LIST);
    }
}