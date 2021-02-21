package com.example.nalone.ui.evenements.display;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.objects.Evenement;
import com.example.nalone.R;
import com.example.nalone.ui.evenements.creation.CreateEventFragment;
import com.example.nalone.ui.evenements.EvenementsFragment;
import com.example.nalone.util.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.Query;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.USER_REFERENCE;
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
    private ProgressBar loading;


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
        loading = rootView.findViewById(R.id.loading);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateEventFragment.edit = false;
                CreateEventFragment.EVENT_LOAD = null;
                //navController.navigate(R.id.action_navigation_infos_events_to_navigation_create_event);
            }
        });

    }
    
    @Override
    public void onResume(){

        adapterEvents();
        super.onResume();
    }

    private void adapterEvents() {
        Query query = mStoreBase.collection("events").whereEqualTo("ownerDoc", USER_REFERENCE);
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
                //holder.mDate.setText((dateFormat.format(e.getStartDate().toDate())));
                //holder.mTime.setText((timeFormat.format(e.getStartDate().toDate())));
                holder.mVille.setText((e.getCity()));
                holder.mDescription.setText((e.getDescription()));
                holder.mProprietaire.setText(e.getOwner_uid());
                holder.textViewNbMembers.setText(e.getNbMembers() + " membres inscrits");


                holder.mImageViewDelete.setImageDrawable(getResources().getDrawable(R.drawable.ic_round_delete_24));
                holder.mImageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Vous êtes sur le point de supprimer un évènement ! Cette action est irréversible ! \n Voulez-vous continuez ?")
                                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mStoreBase.collection("events").document(event.getUid()).delete();
                                        mStoreBase.collection("users").document(USER_ID).collection("events_create").document(event.getUid()).delete();
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
                        //navController.navigate(R.id.action_navigation_creations_evenements_to_navigation_create_event);
                    }
                });

                loading.setVisibility(View.GONE);
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