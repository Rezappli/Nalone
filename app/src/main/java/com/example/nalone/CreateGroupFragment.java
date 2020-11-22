package com.example.nalone;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nalone.adapter.ItemAddPersonAdapter;
import com.example.nalone.adapter.ItemProfilAdapter;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.ui.amis.display.AmisFragment;
import com.example.nalone.ui.evenements.display.MesEvenementsListFragment;
import com.example.nalone.ui.message.ChatActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.heightScreen;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.widthScreen;

public class CreateGroupFragment extends Fragment {

    private TextInputEditText event_name;
    private TextInputEditText event_resume;
    private Visibility event_visibilite;

    private CardView cardViewPrivate;
    private ImageView imageViewPrivate;
    private ImageButton imageButtonAddInvit;

    private CardView cardViewPublic;
    private ImageView imageViewPublic;

    private Button buttonValidEvent;

    public static List<String> adds = new ArrayList<>();
    public static boolean edit;

    private NavController navController;
    ImageView buttonMoreGroup;

    private FirestoreRecyclerAdapter adapter;
    private RecyclerView mRecyclerView;
    private View rootView;

    public static boolean save;

    public static GroupAttente groupAttente;



    public class GroupAttente extends Group{

        public GroupAttente(String uid,String owner,String name,
                            String description, Visibility visibility, DocumentReference ownerDoc){
            super(uid, owner, name, description, visibility, ownerDoc);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create_group, container, false);
        navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewCreateGroup);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        buttonBack.setVisibility(View.VISIBLE);

        if(groupAttente == null){
            Log.w("group", "Creation groupe vide");
            groupAttente  = new GroupAttente(UUID.randomUUID().toString(), USER.getFirst_name() + " " + USER.getLast_name(), "", "", null, USER_REFERENCE);
        }

        if(adds != null){
            initList();
        }

        cardViewPrivate = rootView.findViewById(R.id.cardViewPrivate);
        cardViewPublic = rootView.findViewById(R.id.cardViewPublic);
        imageButtonAddInvit = rootView.findViewById(R.id.buttonMoreGroup);
        imageViewPublic = rootView.findViewById(R.id.imageViewPublic);
        imageViewPrivate = rootView.findViewById(R.id.imageViewPrivate);

        event_name = rootView.findViewById(R.id.groupName);
        event_resume = rootView.findViewById(R.id.groupResume);
        buttonValidEvent = rootView.findViewById(R.id.buttonCreateGroup);

        getData();

       /* if(edit){
            event_name.setText(MesEvenementsListFragment.nameEvent);
            event_resume.setText(MesEvenementsListFragment.descEdit);

            if(MesEvenementsListFragment.visibiliteEdit == Visibility.PUBLIC){
                selectPublic();
            }else{
                selectPrivate();
            }
            edit = false;
        }*/

        cardViewPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPrivate();
                adapter.startListening();
            }
        });

        cardViewPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPublic();
            }
        });

        imageButtonAddInvit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("group"," Nom :" + event_name.getText().toString());

                refreshData();

                ListAmisFragment.group = groupAttente;
                ListAmisFragment.type = "group";
                navController.navigate(R.id.action_navigation_creat_group_to_navigation_list_amis);
            }
        });



        buttonValidEvent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                    saveEvent();
            }
        });

        return rootView;
    }

    private void refreshData() {
        groupAttente.setName(event_name.getText().toString());
        groupAttente.setDescription(event_resume.getText().toString());
        groupAttente.setVisibility(event_visibilite);
    }

    private void getData() {
        Log.w("Group après création", " Nom : "+ groupAttente.getName());
        if(!groupAttente.getName().matches(""))
            event_name.setText(groupAttente.getName());
        if(!groupAttente.getDescription().matches(""))
            event_resume.setText(groupAttente.getDescription());
        if(groupAttente.getVisibility() != null)
            event_visibilite = groupAttente.getVisibility();
    }


    public void initList(){
            adds.add("a");
            Query query = mStoreBase.collection("users").whereIn("uid", adds);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        User u = doc.toObject(User.class);
                        Log.w("Add", u.getFirst_name());
                    }
                }
            });

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
                protected void onBindViewHolder(@NonNull final PersonViewHolder personViewHolder,final int i, @NonNull final  User u) {

                    Log.w("Add","BindViewHolder");
                    personViewHolder.villePers.setText(u.getCity());
                    personViewHolder.nomInvit.setText(u.getFirst_name() + " "+ u.getLast_name());
                    personViewHolder.button.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_remove_24));
                    personViewHolder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adds.remove(u);
                            Log.w("Add", "List : " + adds.isEmpty());
                            adapter.notifyDataSetChanged();
                            initList();
                        }
                    });
                    if(u.getImage_url() != null) {
                        if (!Cache.fileExists(u.getUid())) {
                            StorageReference imgRef = mStore.getReference("users/" + u.getUid());
                            if (imgRef != null) {
                                imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            Uri img = task.getResult();
                                            if (img != null) {
                                                Cache.saveUriFile(u.getUid(), img);
                                                Glide.with(getContext()).load(img).fitCenter().centerCrop().into(personViewHolder.imagePerson);
                                            }
                                        }
                                    }
                                });
                            }
                        } else {
                            Glide.with(getContext()).load(Cache.getUriFromUid(u.getUid())).fitCenter().centerCrop().into(personViewHolder.imagePerson);
                        }

                    }
                }

            };

            //mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setAdapter(adapter);
            adapter.startListening();

            Log.w("Add", "Set adapter");


    }

    private class PersonViewHolder extends RecyclerView.ViewHolder {

        private TextView nomInvit;
        private TextView villePers;
        private ImageView button, imagePerson;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomInvit);
            villePers = itemView.findViewById(R.id.villePers);
            button = itemView.findViewById(R.id.imageView19);
            imagePerson = itemView.findViewById(R.id.imagePerson);
        }
    }



    private void selectPublic() {
        imageViewPublic.setImageResource(R.drawable.ic_baseline_public_focused);
        imageViewPrivate.setImageResource(R.drawable.ic_baseline_lock_24);
        event_visibilite = Visibility.PUBLIC;
    }

    private void selectPrivate() {
        imageButtonAddInvit.setVisibility(View.VISIBLE);
        imageViewPrivate.setImageResource(R.drawable.ic_baseline_lock_focused);
        imageViewPublic.setImageResource(R.drawable.ic_baseline_public_24);
        event_visibilite = Visibility.PRIVATE;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void saveEvent(){

        if(event_name.getText().toString().matches("")){
            event_name.setError("Champs obligatoire");
        }

        if(!event_name.getText().toString().matches("")){
            refreshData();
            Group g = new Group(groupAttente.getUid(), groupAttente.getOwner(), groupAttente.getName(), groupAttente.getDescription(), groupAttente.getVisibility(), groupAttente.getOwnerDoc());
            mStoreBase.collection("groups").document(g.getUid()).set(g);

            for (String user : adds){
                Log.d("Ajout", "Ajout de membre dans groupe");
                mStoreBase.collection("groups").document(g.getUid()).collection("members").document(user).set(g);
            }
            Toast.makeText(getContext(), "Vous avez créer votre groupe !", Toast.LENGTH_SHORT).show();
            navController.navigate(R.id.action_navigation_creat_group_to_navigation_amis);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onStart(){
        super.onStart();

      //  adapter.startListening();
    }

    }
