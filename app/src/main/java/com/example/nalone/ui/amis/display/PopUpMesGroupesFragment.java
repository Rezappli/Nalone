package com.example.nalone.ui.amis.display;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.nalone.Cache;
import com.example.nalone.Group;
import com.example.nalone.R;
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


public class PopUpMesGroupesFragment extends Fragment {

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
    private EditText groupDesc;
    Button buttonPlus;
    private int nbMembers;
    private List<String> friends;
    RelativeLayout relativeLayout;

    ImageView img1,img2,img3,img4,img5,img6;
    CardView c1,c2,c3,c4,c5,c6;

    private CardView cardViewPhotoEdit, cardViewPhotoEditDesc,cardViewProfilEdit,cardViewProfilMembers, getCardViewPhotoEditDesc;
    private ImageView imageViewEditDescription;
    FrameLayout fenetrePrincipal;
    View root;
    private boolean editDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_pop_up_mes_groupes, container, false);

        createFragment(root);

        return root;
    }

    private void createFragment(final View root) {
        nbMembers = 0;
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        textViewNbMembers = root.findViewById(R.id.groupNbMembers);
        buttonPlus = root.findViewById(R.id.buttonPlus);
        buttonBack.setVisibility(View.VISIBLE);
        relativeLayout = root.findViewById(R.id.relativeImages);
        fenetrePrincipal = root.findViewById(R.id.fenetrePrincipal);
        fenetrePrincipal.setVisibility(View.GONE);

        cardViewProfilEdit = root.findViewById(R.id.cardViewEditGroup);
        cardViewPhotoEdit = root.findViewById(R.id.cardViewPhotoEditImg);
        cardViewPhotoEditDesc = root.findViewById(R.id.cardViewPhotoEditDesc);
        cardViewProfilMembers = root.findViewById(R.id.cardViewEditMembers);

        cardViewProfilMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListMembresFragment.GROUP_LOAD = GROUP_LOAD;
                navController.navigate(R.id.action_navigation_popup_mes_groupes_to_navigation_list_membres);
            }
        });
        groupDesc = root.findViewById(R.id.groupDesc);
        imageViewEditDescription = root.findViewById(R.id.imageViewEditDescription);

        nameGroup = root.findViewById(R.id.groupName);
        nbCreateGroup = root.findViewById(R.id.nbEventCreate);
        nbParticipateGroup = root.findViewById(R.id.nbEventParticipe);
        imageGroup = root.findViewById(R.id.imageViewGroup);
        ownerGroup = root.findViewById(R.id.groupOwner);
        visibilityGroup = root.findViewById(R.id.groupVisibility);

        if(GROUP_LOAD.getVisibility() == Visibility.PUBLIC)
         visibilityGroup.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_public_24, 0, 0, 0);
        else
            visibilityGroup.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_lock_24,0,0,0);

        cardViewPhotoPerson = root.findViewById(R.id.cardViewPhotoPerson);

        nameGroup.setText(GROUP_LOAD.getName());
        groupDesc.setText(GROUP_LOAD.getDescription());
        groupDesc.setText(GROUP_LOAD.getDescription());
        groupDesc.setClickable(false);
        groupDesc.setEnabled(false);
        ownerGroup.setText(GROUP_LOAD.getOwner());
        visibilityGroup.setText(GROUP_LOAD.getVisibility().toString());

        cardViewProfilEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditGroupFragment.GROUP_LOAD = GROUP_LOAD;
                navController.navigate(R.id.action_navigation_popup_mes_groupes_to_navigation_edit_group);
            }
        });

        cardViewPhotoEditDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editDescription){
                    groupDesc.setClickable(true);
                    groupDesc.setEnabled(true);
                    imageViewEditDescription.setImageResource(R.drawable.ic_baseline_check_24);
                    editDescription = true;
                }else{
                    groupDesc.setClickable(false);
                    groupDesc.setEnabled(false);
                    imageViewEditDescription.setImageResource(R.drawable.ic_baseline_edit_24);
                    editDescription = false;
                    if(!groupDesc.getText().toString().equalsIgnoreCase(USER.getDescription())){
                        GROUP_LOAD.setDescription(groupDesc.getText().toString());
                        mStoreBase.collection("groups").document(GROUP_LOAD.getUid()).set(GROUP_LOAD);
                        Toast.makeText(getContext(), "Vous avez mis à jour la description du groupe", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


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




        //nbCreateGroup.setText(GROUP_LOAD.get);

        if (GROUP_LOAD.getDescription().matches("")) {
            descriptionGroup.setVisibility(View.GONE);
        }


    }


    }
