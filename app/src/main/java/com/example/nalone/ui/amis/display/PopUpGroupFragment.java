package com.example.nalone.ui.amis.display;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nalone.Cache;
import com.example.nalone.Group;
import com.example.nalone.R;
import com.example.nalone.User;
import com.example.nalone.UserFriendData;
import com.example.nalone.Visibility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;


public class PopUpGroupFragment extends Fragment {

    public static Group GROUP_LOAD;
    NavController navController;

    public static String type;
    TextView nameGroup, ownerGroup;
    TextView descriptionGroup;
    TextView nbCreateGroup;
    TextView nbParticipateGroup;

    ImageView imageGroup;
    TextView visibilityGroup, textViewNbMembers;
    Button buttonAdd;
    CardView cardViewPhotoPerson;
    Button buttonPlus;
    private int nbMembers;
    private List<String> friends;
    RelativeLayout relativeLayout;

    ImageView img1,img2,img3,img4,img5,img6;
    CardView c1,c2,c3,c4,c5,c6;

    FrameLayout fenetrePrincipal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_pop_up_group, container, false);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        textViewNbMembers = root.findViewById(R.id.groupNbMembers);
        buttonPlus = root.findViewById(R.id.buttonPlus);
        buttonBack.setVisibility(View.VISIBLE);
        relativeLayout = root.findViewById(R.id.relativeImages);
        fenetrePrincipal = root.findViewById(R.id.fenetrePrincipal);
        fenetrePrincipal.setVisibility(View.GONE);

        img1 = root.findViewById(R.id.image1);
        img2 = root.findViewById(R.id.image2);
        img3 = root.findViewById(R.id.image3);
        img4 = root.findViewById(R.id.image4);
        img5 = root.findViewById(R.id.image5);
        img6 = root.findViewById(R.id.image6);

        c1 = root.findViewById(R.id.img1);
        c2 = root.findViewById(R.id.img2);
        c3 = root.findViewById(R.id.img3);
        c4 = root.findViewById(R.id.img4);
        c5 = root.findViewById(R.id.img5);
        c6 = root.findViewById(R.id.img6);


        final List<ImageView> images = new ArrayList<>();
        images.add(img1);
        images.add(img2);
        images.add(img3);
        images.add(img4);
        images.add(img5);
        images.add(img6);

        final List<CardView> cardViews = new ArrayList<>();
        cardViews.add(c1);
        cardViews.add(c2);
        cardViews.add(c3);
        cardViews.add(c4);
        cardViews.add(c5);
        cardViews.add(c6);

        mStoreBase.collection("groups").document(GROUP_LOAD.getUid()).collection("members").whereEqualTo("status", "add")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                nbMembers++;
                            }
                        }
                        textViewNbMembers.setText(nbMembers+" membres");
                        if(nbMembers == 0){
                            relativeLayout.setVisibility(View.GONE);
                        }
                        if(nbMembers <= 6){
                            buttonPlus.setVisibility(View.GONE);
                        }
                        for(int i = 0; i < nbMembers && i < 6; i++){
                            cardViews.get(i).setVisibility(View.VISIBLE);
                        }
                        fenetrePrincipal.setVisibility(View.VISIBLE);
                    }
                });

        nameGroup = root.findViewById(R.id.groupName);
        descriptionGroup = root.findViewById(R.id.groupDescription);
        nbCreateGroup = root.findViewById(R.id.nbEventCreate);
        nbParticipateGroup = root.findViewById(R.id.nbEventParticipe);
        imageGroup = root.findViewById(R.id.imageViewGroup);
        buttonAdd = root.findViewById(R.id.buttonJoin);
        ownerGroup = root.findViewById(R.id.groupOwner);
        visibilityGroup = root.findViewById(R.id.groupVisibility);

        cardViewPhotoPerson = root.findViewById(R.id.cardViewPhotoPerson);

        if (GROUP_LOAD.getImage_url() != null) {
            if(!Cache.fileExists(GROUP_LOAD.getUid())) {
                StorageReference imgRef = mStore.getReference("users/" + GROUP_LOAD.getUid());
                if (imgRef != null) {
                    imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri img = task.getResult();
                                if (img != null) {
                                    Log.w("image", "save image from cache");
                                    Cache.saveUriFile(GROUP_LOAD.getUid(), img);
                                    Glide.with(getContext()).load(img).fitCenter().centerCrop().into(imageGroup);
                                }
                            }
                        }
                    });
                }
            }else{
                Log.w("image", "get image from cache");
                Glide.with(getContext()).load(Cache.getUriFromUid(GROUP_LOAD.getUid())).fitCenter().centerCrop().into(imageGroup);
            }
        }


        nameGroup.setText(GROUP_LOAD.getName());
        descriptionGroup.setText(GROUP_LOAD.getDescription());
        ownerGroup.setText(GROUP_LOAD.getOwner());
        visibilityGroup.setText(GROUP_LOAD.getVisibility().toString());

        //nbCreateGroup.setText(GROUP_LOAD.get);

        if (GROUP_LOAD.getDescription().matches("")) {
            descriptionGroup.setVisibility(View.GONE);
        }

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GROUP_LOAD.getVisibility() == Visibility.PUBLIC){
                    Toast.makeText(getContext(), "Vous avez rejoint " + GROUP_LOAD.getName() + " !", Toast.LENGTH_SHORT).show();
                    joinPublic();

                }else {
                    Toast.makeText(getContext(), "Vous avez envoyé une demande pour rejoindre le groupe "+ GROUP_LOAD.getName() + " !", Toast.LENGTH_SHORT).show();
                    joinPrive();

                }
            }
        });



        return root;
    }

    public void joinPublic() {
        UserFriendData data1 = new UserFriendData("add", mStoreBase.collection("users").document(USER.getUid()));
        mStoreBase.collection("groups").document(GROUP_LOAD.getUid()).collection("members").document(USER.getUid()).set(data1);
        buttonAdd.setBackground(getResources().getDrawable(R.drawable.custom_button_signup));
        buttonAdd.setText("Quitter");
        buttonAdd.setTextColor(Color.BLACK);
        navController.navigate(R.id.action_navigation_popup_group_self);
    }

    public void joinPrive() {
        UserFriendData data1 = new UserFriendData("received", mStoreBase.collection("users").document(USER.getUid()));
        mStoreBase.collection("groups").document(GROUP_LOAD.getUid()).collection("members").document(USER.getUid()).set(data1);
        navController.navigate(R.id.action_navigation_popup_group_self);

    }

    }
