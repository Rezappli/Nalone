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
import com.example.nalone.util.Cache;
import com.example.nalone.objects.Group;
import com.example.nalone.R;
import com.example.nalone.objects.ModelData;
import com.example.nalone.enumeration.Visibility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

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
    Button buttonVoirMembres;
    private int nbMembers;
    private List<String> friends;
    RelativeLayout relativeLayout;

    FrameLayout fenetrePrincipal;
    View root;
    private boolean buttonQuitter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_pop_up_group, container, false);

        createFragment(root);

        return root;
    }

    private void createFragment(final View root) {
        nbMembers = 0;
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        textViewNbMembers = root.findViewById(R.id.groupNbMembers);
        buttonVoirMembres = root.findViewById(R.id.buttonVoirMembres);
        buttonBack.setVisibility(View.VISIBLE);
        fenetrePrincipal = root.findViewById(R.id.fenetrePrincipal);
        fenetrePrincipal.setVisibility(View.GONE);

        mStoreBase.collection("groups").document(GROUP_LOAD.getUid()).collection("members").whereEqualTo("status", "add")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                nbMembers++;
                                if(document.getId().equals(USER_ID) || buttonQuitter){
                                    changeButtonQuitter();
                                }else{
                                    changeButtonRejoindre();
                                }
                            }
                        }
                        textViewNbMembers.setText(nbMembers+" membres");
                        if (nbMembers != 0){
                            buttonVoirMembres.setVisibility(View.VISIBLE);
                        }else{
                            buttonVoirMembres.setVisibility(View.GONE);
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

        if(buttonQuitter){
            changeButtonQuitter();
        }else{
            changeButtonRejoindre();
        }

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

        if(GROUP_LOAD.getVisibility() == Visibility.PUBLIC)
            visibilityGroup.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_public_24, 0, 0, 0);
        else
            visibilityGroup.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_lock_24,0,0,0);

        //nbCreateGroup.setText(GROUP_LOAD.get);

        if (GROUP_LOAD.getDescription().matches("")) {
            descriptionGroup.setVisibility(View.GONE);
        }

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GROUP_LOAD.getVisibility() == Visibility.PUBLIC){
                    if(!buttonQuitter){
                        joinPublic();
                        buttonQuitter = true;
                    } else{
                        quitterGroup();
                        buttonQuitter = false;
                    }


                }else {
                    joinPrive();

                }
                createFragment(root);
            }

        });

    }

    private void changeButtonRejoindre() {
        buttonAdd.setBackground(getResources().getDrawable(R.drawable.custom_button_simple));
        buttonAdd.setText("Rejoindre");
        buttonAdd.setTextColor(Color.WHITE);
        buttonQuitter = false;
    }

    private void quitterGroup() {
        mStoreBase.collection("groups").document(GROUP_LOAD.getUid()).collection("members").document(USER.getUid()).delete();
        mStoreBase.collection("users").document(USER_ID).collection("groups").document(GROUP_LOAD.getUid()).delete();
        Toast.makeText(getContext(), "Vous avez quitter le groupe " + GROUP_LOAD.getName() + " !", Toast.LENGTH_SHORT).show();

    }

    private void changeButtonQuitter() {
        buttonAdd.setBackground(getResources().getDrawable(R.drawable.custom_button_signup));
        buttonAdd.setText("Quitter");
        buttonAdd.setTextColor(Color.BLACK);
        buttonQuitter = true;
    }

    public void joinPublic() {
        ModelData data1 = new ModelData("add", mStoreBase.collection("users").document(USER.getUid()));
        ModelData data2 = new ModelData("add", GROUP_LOAD.getOwnerDoc());
        mStoreBase.collection("groups").document(GROUP_LOAD.getUid()).collection("members").document(USER.getUid()).set(data1);
        mStoreBase.collection("users").document(USER_ID).collection("groups").document(GROUP_LOAD.getUid()).set(data2);
        Toast.makeText(getContext(), "Vous avez rejoint le groupe " + GROUP_LOAD.getName() + " !", Toast.LENGTH_SHORT).show();
    }

    public void joinPrive() {
        ModelData data1 = new ModelData("received", mStoreBase.collection("users").document(USER.getUid()));
        mStoreBase.collection("groups").document(GROUP_LOAD.getUid()).collection("members").document(USER.getUid()).set(data1);
        Toast.makeText(getContext(), "Vous avez envoyé une demande pour rejoindre le groupe "+ GROUP_LOAD.getName() + " !", Toast.LENGTH_SHORT).show();
    }

    }
