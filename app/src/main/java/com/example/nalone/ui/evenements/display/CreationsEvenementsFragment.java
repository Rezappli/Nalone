package com.example.nalone.ui.evenements.display;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.Evenement;
import com.example.nalone.R;
import com.example.nalone.User;
import com.example.nalone.Visibility;
import com.example.nalone.ui.amis.display.CreateGroupFragment;
import com.example.nalone.ui.evenements.CreateEventFragment;
import com.example.nalone.ui.evenements.EvenementsFragment;
import com.example.nalone.util.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.mStoreBase;


public class CreationsEvenementsFragment extends Fragment {

    private View rootView;

    private LinearLayout linearSansEvent;
    private ImageView addEvent;

    private RecyclerView mRecyclerView;
    private FirestoreRecyclerAdapter adapter;

    private int iterator = 0;
    private List<String> events = new ArrayList<>();

    private ArrayList<Evenement> itemEvents = new ArrayList<>();
    private NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_creations_evenements, container, false);

        createFragment();

        return rootView;
    }

    public void createFragment(){
        linearSansEvent = rootView.findViewById(R.id.linearSansEvent);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewMesEventList);
        addEvent = rootView.findViewById(R.id.create_event_button);
        navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CreateEventFragment.class));
            }
        });

    }
    
    @Override
    public void onResume(){

        DocumentReference ref = mStoreBase.document("users/"+USER_ID);
        mStoreBase.collection("users").document(USER_ID).collection("events").whereEqualTo("ownerDoc", ref)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                events.add(document.getId());
                                Log.w("event", " Ajout list");
                            }
                            DocumentReference ref = mStoreBase.document("users/"+USER_ID);
                            mStoreBase.collection("events").whereNotEqualTo("ownerDoc", ref)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    iterator++;
                                                }
                                                Log.w("event", " Check list");
                                                adapterEvents();
                                                if(iterator == 0){
                                                    linearSansEvent.setVisibility(View.VISIBLE);
                                                }else{
                                                    linearSansEvent.setVisibility(View.GONE);
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });
        super.onResume();
    }

    private void adapterEvents() {
        DocumentReference ref = mStoreBase.document("users/"+USER_ID);
        Query query;
        if(!events.isEmpty()){
            query = mStoreBase.collection("events").whereIn("uid", events);
        }else{
            query = mStoreBase.collection("events").whereEqualTo("ownerDoc", ref);
        }
        FirestoreRecyclerOptions<Evenement> options = new FirestoreRecyclerOptions.Builder<Evenement>().setQuery(query, Evenement.class).build();

        adapter = new FirestoreRecyclerAdapter<Evenement, EventViewHolder>(options) {
            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mes_evenements_list,parent,false);
                return new EventViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final EventViewHolder holder, int i, @NonNull final Evenement e) {
                final Evenement event = e;

                //holder.mImageView.setImageResource(e.getImage());
                holder.mTitle.setText((e.getName()));
                holder.mDate.setText((e.getDate().toDate().toString()));
                holder.mVille.setText((e.getCity()));
                holder.mDescription.setText((e.getDescription()));
                holder.mProprietaire.setText(e.getOwner());
                holder.textViewNbMembers.setText(e.getNbMembers() + " membres inscrits");


                mStoreBase.collection("users").whereEqualTo("uid", e.getOwnerDoc().getId())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        User u = document.toObject(User.class);
                                        if(u.getCursus().equalsIgnoreCase("Informatique")){
                                            holder.mCarwViewOwner.setCardBackgroundColor(Color.RED);
                                        }

                                        if(u.getCursus().equalsIgnoreCase("TC")){
                                            holder.mCarwViewOwner.setCardBackgroundColor(Color.parseColor("#00E9FD"));
                                        }

                                        if(u.getCursus().equalsIgnoreCase("MMI")){
                                            holder.mCarwViewOwner.setCardBackgroundColor(Color.parseColor("#FF1EED"));
                                        }

                                        if(u.getCursus().equalsIgnoreCase("GB")){
                                            holder.mCarwViewOwner.setCardBackgroundColor(Color.parseColor("#41EC57"));
                                        }

                                        if(u.getCursus().equalsIgnoreCase("LP")){
                                            holder.mCarwViewOwner.setCardBackgroundColor((Color.parseColor("#EC9538")));
                                        }

                                    }

                                }
                            }});


                holder.mImageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Vous êtes sur le point de supprimer un évènement ! Cette action est irréversible ! \n Voulez-vous continuez ?")
                                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mStoreBase.collection("events").document(event.getUid()).delete();
                                        mStoreBase.collection("users").document(USER_ID).collection("events").document(event.getUid()).delete();
                                        Toast.makeText(getContext(), "Vous avez supprimé(e) un évènement !", Toast.LENGTH_SHORT).show();
                                        createFragment();
                                    }
                                })
                                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.create();
                        builder.show();

                    }
                });

                holder.mImageViewDisplay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Constants.targetZoom = new LatLng(event.getLatitude(), event.getLongitude());
                        EvenementsFragment.viewPager.setCurrentItem(0);
                    }
                });

                holder.mImageViewEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CreateEventFragment.EVENT_LOAD = e;
                        CreateEventFragment.edit = true;
                        navController.navigate(R.id.action_navigation_creations_evenements_to_navigation_create_event);
                    }
                });
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setAdapter(adapter);
        adapter.startListening();


    }


    private class EventViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView, mImageViewEdit, mImageViewDisplay, mImageViewDelete;
        public TextView mTitle;
        public TextView mDate;
        public TextView mTime;
        public TextView mVille;
        public TextView mDescription;
        public TextView mProprietaire;
        public CardView mCarwViewOwner;
        public TextView textViewNbMembers;


        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageOwnerEventList);
            mTitle = itemView.findViewById(R.id.titleEventList);
            mDate = itemView.findViewById(R.id.dateEventList);
            mTime = itemView.findViewById(R.id.timeEventList);
            mVille = itemView.findViewById(R.id.villeEventList);
            mDescription = itemView.findViewById(R.id.descriptionEventList);
            mProprietaire = itemView.findViewById(R.id.ownerEventList);
            mImageViewDelete = itemView.findViewById(R.id.imageViewDeleteMesEvent);
            mImageViewEdit = itemView.findViewById(R.id.imageViewEdit);
            mImageViewDisplay = itemView.findViewById(R.id.imageViewAfficher);
            textViewNbMembers = itemView.findViewById(R.id.textViewNbMembers);
            mCarwViewOwner = itemView.findViewById(R.id.backGroundOwner);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null){
            adapter.startListening();
        }
    }

}