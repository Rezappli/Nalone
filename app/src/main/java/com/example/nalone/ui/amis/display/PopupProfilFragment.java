package com.example.nalone.ui.amis.display;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nalone.Cache;
import com.example.nalone.R;
import com.example.nalone.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.updateUserData;

public class PopupProfilFragment extends Fragment {


    public static User USER_LOAD;
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

        nameProfil.setText(USER_LOAD.getFirst_name() + " " + USER_LOAD.getLast_name());
        villeProfil.setText(USER_LOAD.getCity());
        nbCreateProfil.setText(USER_LOAD.getNumber_events_create());
        nbParticipateProfil.setText(USER_LOAD.getNumber_events_attend());

        if(USER_LOAD.getCursus().equalsIgnoreCase("Informatique")){
            cardViewPhotoPerson.setCardBackgroundColor(Color.RED);
        }

        if(USER_LOAD.getCursus().equalsIgnoreCase("TC")){
            cardViewPhotoPerson.setCardBackgroundColor(Color.GREEN);
        }

        if(USER_LOAD.getCursus().equalsIgnoreCase("MMI")){
            cardViewPhotoPerson.setCardBackgroundColor(Color.parseColor("#4B0082"));
        }

        if(USER_LOAD.getCursus().equalsIgnoreCase("GB")){
            cardViewPhotoPerson.setCardBackgroundColor(Color.BLUE);
        }

        if(USER_LOAD.getCursus().equalsIgnoreCase("LP")){
            cardViewPhotoPerson.setCardBackgroundColor(Color.GRAY);
        }

        if (USER_LOAD.getImage_url() != null) {
            if(!Cache.fileExists(USER_LOAD.getUid())) {
                StorageReference imgRef = mStore.getReference("users/" + USER_LOAD.getUid());
                if (imgRef != null) {
                    imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri img = task.getResult();
                                if (img != null) {
                                    Log.w("image", "save image from cache");
                                    Cache.saveUriFile(USER_LOAD.getUid(), img);
                                    Glide.with(getContext()).load(img).fitCenter().centerCrop().into(imagePerson);
                                }
                            }
                        }
                    });
                }
            }else{
                Log.w("image", "get image from cache");
                Glide.with(getContext()).load(Cache.getUriFromUid(USER_LOAD.getUid())).fitCenter().centerCrop().into(imagePerson);
            }
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

        /*if(USER_LOAD.getCenters_interests() != null){
        for(int i = 0; i < USER_LOAD.getCenters_interests().size(); i++) {
            int imgResource = 0;
            if (USER_LOAD.getCenters_interests().get(i).toString().equalsIgnoreCase("programmation")) {
                imgResource = R.drawable.ci_programmation;
            } else if (USER_LOAD.getCenters_interests().get(i).toString().equalsIgnoreCase("musique")) {
                imgResource = R.drawable.ci_musique;
            } else if (USER_LOAD.getCenters_interests().get(i).toString().equalsIgnoreCase("livre")) {
                imgResource = R.drawable.ci_livre;
            } else if (USER_LOAD.getCenters_interests().get(i).toString().equalsIgnoreCase("film")) {
                imgResource = R.drawable.ci_film;
            } else if (USER_LOAD.getCenters_interests().get(i).toString().equalsIgnoreCase("video")) {
                imgResource = R.drawable.ci_jeuxvideo;
            } else if (USER_LOAD.getCenters_interests().get(i).toString().equalsIgnoreCase("peinture")) {
                imgResource = R.drawable.ci_peinture;
            } else if (USER_LOAD.getCenters_interests().get(i).toString().equalsIgnoreCase("photo")) {
                imgResource = R.drawable.ci_photo;
            } else if (USER_LOAD.getCenters_interests().get(i).toString().equalsIgnoreCase("sport")) {
                imgResource = R.drawable.ci_sport;
            }

            imageCentreInteret.get(i).setImageResource(imgResource);
            imageCentreInteret.get(i).setVisibility(View.VISIBLE);
        }

        }*/


        nameProfil.setText(USER_LOAD.getLast_name() + " " + USER_LOAD.getFirst_name());
        descriptionProfil.setText(USER_LOAD.getDescription());
        nbCreateProfil.setText(USER_LOAD.getNumber_events_create());
        nbParticipateProfil.setText(USER_LOAD.getNumber_events_attend());
        buttonAdd.setImageResource(button);

        if (USER_LOAD.getDescription().matches("")) {
            descriptionProfil.setVisibility(View.GONE);
        }

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("Button", "Button : " + button);
                if (button == R.drawable.ic_round_mail_24) {
                    Toast.makeText(getContext(), "Vous avez reçu une demande d'amis de la part de cet utilisateur !", Toast.LENGTH_SHORT).show();
                } else if (button == R.drawable.ic_round_hourglass_top_24) {
                    Toast.makeText(getContext(), "Votre demande d'amis est en attente !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Vous avez envoyé une demande d'amis !", Toast.LENGTH_SHORT).show();
                    addFriend();
                    buttonAdd.setImageResource(R.drawable.ic_round_hourglass_top_24);
                }
            }
        });



        return root;
    }

    public void addFriend() {
        //USER_LOAD.get_friends_requests_received().add(mStoreBase.collection("users").document(USER.getUid()));
        //USER.get_friends_requests_send().add(mStoreBase.collection("users").document(USER_LOAD.getUid()));

        updateUserData(USER_LOAD);
        updateUserData(USER);
    }
}