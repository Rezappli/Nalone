package com.example.nalone.ui.evenements.creation;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nalone.R;
import com.example.nalone.dialog.ListAmisFragment;
import com.example.nalone.objects.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.setUserImage;

public class MembersEventFragment extends Fragment {

    private List<String> adds = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private NavController navController;
    private FirestoreRecyclerAdapter adapter;
    private Button buttonMoreInvit;
    private boolean haveFriends;
    private BottomSheetBehavior bottomSheetBehavior;



    public MembersEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_members_event, container, false);
        // Inflate the layout for this fragment
        initialiserImageView(root);
        mRecyclerView= root.findViewById(R.id.recyclerView1);
        buttonMoreInvit = root.findViewById(R.id.buttonMoreInvit);
        navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment2);
        final View bottomSheet = root.findViewById(R.id.sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                switch (state){
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        buttonMoreInvit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStoreBase.collection("users").document(USER_ID).collection("friends").whereEqualTo("status", "add")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                haveFriends = true;
                                Log.w("event", " Ajout list");
                            }

                        }
                        if(haveFriends){
                            ListAmisFragment.type = "event";
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            ListAmisFragment.EVENT_LOAD = MainCreationEventActivity.currentEvent;
                        }else
                            Toast.makeText(getContext(), "Vous n'avez pas d'ami", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });


        checkValidation();
        return root;
    }

    private ImageView imageProgessCreationPhoto,imageProgessCreationDate,imageProgessCreationPosition,imageProgessCreationName,
            imageProgessCreationMembers;

    private void initialiserImageView(View root) {
        imageProgessCreationDate = root.findViewById(R.id.imageProgessCreationDate);
        imageProgessCreationMembers = root.findViewById(R.id.imageProgessCreationMembers);
        imageProgessCreationName = root.findViewById(R.id.imageProgessCreationName);
        imageProgessCreationPosition = root.findViewById(R.id.imageProgessCreationPosition);
        imageProgessCreationPhoto = root.findViewById(R.id.imageProgessCreationPhoto);
        imageProgessCreationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDate();
            }
        });
        imageProgessCreationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goName();
            }
        });
        imageProgessCreationPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAdress();
            }
        });
        imageProgessCreationPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPhoto();
            }
        });



    }

    private void initList(){
        Query query;
        if(!adds.isEmpty()) {
            query = mStoreBase.collection("users").whereIn("uid", adds);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        User u = doc.toObject(User.class);
                        Log.w("Add", u.getFirst_name());
                    }
                }
            });
        }else{
            query = mStoreBase.collection("usejkhdskjfhkjhrjdhfks");
        }

        //RecyclerOption
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();

        adapter = new FirestoreRecyclerAdapter<User, PersonViewHolder>(options) {
            @NonNull
            @Override
            public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.w("Add", "ViewHolder");
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person,parent,false);
                return new PersonViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final PersonViewHolder personViewHolder, final int i, @NonNull final  User u) {

                Log.w("Add","BindViewHolder");
                personViewHolder.villePers.setText(u.getCity());
                personViewHolder.nomInvit.setText(u.getFirst_name() + " "+ u.getLast_name());
                personViewHolder.button.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_remove_24));
                personViewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adds.remove(u.getUid());
                        Log.w("Add", "List : " + adds.isEmpty());
                        adapter.notifyDataSetChanged();
                        //createFragment();
                    }
                });

                if(u.getCursus().equalsIgnoreCase("Informatique")){
                    personViewHolder.cardView.setCardBackgroundColor(Color.RED);
                }
                if(u.getCursus().equalsIgnoreCase("TC")){
                    personViewHolder.cardView.setCardBackgroundColor(Color.parseColor("#00E9FD"));
                }

                if(u.getCursus().equalsIgnoreCase("MMI")){
                    personViewHolder.cardView.setCardBackgroundColor(Color.parseColor("#FF1EED"));
                }

                if(u.getCursus().equalsIgnoreCase("GB")){
                    personViewHolder.cardView.setCardBackgroundColor(Color.parseColor("#41EC57"));
                }

                if(u.getCursus().equalsIgnoreCase("LP")){
                    personViewHolder.cardView.setCardBackgroundColor((Color.parseColor("#EC9538")));
                }



                setUserImage(u,getContext(),personViewHolder.imagePerson);

            }

        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
        adapter.startListening();

        Log.w("Add", "Set adapter");

    }

    private class PersonViewHolder extends RecyclerView.ViewHolder {

        private TextView nomInvit;
        private TextView villePers;
        private ImageView button, imagePerson;
        private CardView cardView;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomInvit);
            villePers = itemView.findViewById(R.id.villePers);
            button = itemView.findViewById(R.id.buttonImage);
            imagePerson = itemView.findViewById(R.id.imagePerson);
            cardView = itemView.findViewById(R.id.cardViewProfil);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void checkValidation(){
        if (MainCreationEventActivity.adressValidate){
            imageProgessCreationPosition.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_adress_focused));
        }
        if (MainCreationEventActivity.dateValidate){
            imageProgessCreationDate.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_date_focused));
        }
        if (MainCreationEventActivity.membersValidate){
            imageProgessCreationMembers.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_members_focused));
        }
        if (MainCreationEventActivity.nameValidate){
            imageProgessCreationName.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_name_focused));
        }
        if (MainCreationEventActivity.photoValidate){
            imageProgessCreationPhoto.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_photo_focused));
        }

    }

    private void validateName(){
        MainCreationEventActivity.membersValidate = true;
        /*
        THIBAULT MET LA PHOTO DE L'EVENT
         */
        if(MainCreationEventActivity.isAllValidate()){
            Toast.makeText(getContext(), "Evenement créer", Toast.LENGTH_SHORT).show();
        }else if(!MainCreationEventActivity.nameValidate) {
            goName();
        }else if(!MainCreationEventActivity.dateValidate){
            goDate();
        }else if(!MainCreationEventActivity.photoValidate){
            goPhoto();
        }else if(!MainCreationEventActivity.adressValidate){
            goAdress();
        }
    }

    private void goDate(){
        MainCreationEventActivity.navController.navigate(R.id.action_membersEventFragment_to_dateEventFragment);    }
    private void goAdress(){
        MainCreationEventActivity.navController.navigate(R.id.action_membersEventFragment_to_adressEventFragment);
    }
    private void goName(){
        MainCreationEventActivity.navController.navigate(R.id.action_membersEventFragment_to_nameEventFragment);   }
    private void goPhoto(){
        MainCreationEventActivity.navController.navigate(R.id.action_membersEventFragment_to_photoEventFragment);   }
}