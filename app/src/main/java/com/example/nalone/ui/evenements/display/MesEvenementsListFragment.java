package com.example.nalone.ui.evenements.display;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.Evenement;
import com.example.nalone.R;
import com.example.nalone.Visibility;
import com.example.nalone.ui.evenements.CreateEventFragment;
import com.example.nalone.ui.evenements.EvenementsFragment;
import com.example.nalone.util.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.mStoreBase;


public class MesEvenementsListFragment extends Fragment {

    private View rootView;

    public static String nameEvent;
    public static String cityEdit;
    public static String adresseEdit;
    public static String dateEdit;
    public static String timeEdit;
    public static String descEdit;
    public static Visibility visibiliteEdit;

    private LinearLayout linearSansEvent;
    private ImageView addEvent;

    private RecyclerView mRecyclerView;
    private FirestoreRecyclerAdapter adapter;

    private int iterator = 0;

    private ArrayList<Evenement> itemEvents = new ArrayList<>();

    public MesEvenementsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mes_evenements_lits, container, false);

        linearSansEvent = rootView.findViewById(R.id.linearSansEvent);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewMesEventList);
        addEvent = rootView.findViewById(R.id.create_event_button);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CreateEventFragment.class));
            }
        });


        DocumentReference ref = mStoreBase.document("users/"+USER_ID);
        mStoreBase.collection("events").whereEqualTo("ownerDoc", ref)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                iterator++;
                            }
                            if(iterator == 0){
                                linearSansEvent.setVisibility(View.VISIBLE);
                            }else{
                                linearSansEvent.setVisibility(View.GONE);
                            }
                            adapterEvents();

                        }
                    }
                });

        return rootView;
    }

    private void adapterEvents() {
        DocumentReference ref = mStoreBase.document("users/"+USER_ID);
        Query query = mStoreBase.collection("events");//.whereEqualTo("ownerDoc", ref);
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

                holder.mImageView.setImageResource(e.getImage());
                holder.mTitle.setText((e.getName()));
                holder.mDate.setText((e.getDate().toDate().toString()));
                holder.mVille.setText((e.getCity()));
                holder.mDescription.setText((e.getDescription()));
                holder.mProprietaire.setText(e.getOwner());

                holder.mImageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Vous êtes sur le point de supprimer un évènement ! Cette action est irréversible ! Voulez-vous continuez ?")
                                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mStoreBase.collection("event").document(event.getUid()).delete();
                                        Toast.makeText(getContext(), "Vous avez supprimé(e) un évènement !", Toast.LENGTH_SHORT).show();

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
                        Constants.targetZoom = new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude());
                        EvenementsFragment.viewPager.setCurrentItem(0);
                    }
                });

                holder.mImageViewEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

               /* if(e.getImage_url() != null) {
                    if(!Cache.fileExists(e.getUid())) {
                        StorageReference imgRef = mStore.getReference("groups/" + g.getUid());
                        if (imgRef != null) {
                            imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri img = task.getResult();
                                        if (img != null) {
                                            Cache.saveUriFile(g.getUid(), img);
                                            g.setImage_url(Cache.getImageDate(g.getUid()));
                                            mStoreBase.collection("groups").document(g.getUid()).set(g);
                                            Glide.with(getContext()).load(img).fitCenter().centerCrop().into(userViewHolder.imageGroup);
                                        }
                                    }
                                }
                            });
                        }
                    }else{
                        Uri imgCache = Cache.getUriFromUid(g.getUid());
                        if(Cache.getImageDate(g.getUid()).equalsIgnoreCase(g.getImage_url())) {
                            Glide.with(getContext()).load(imgCache).fitCenter().centerCrop().into(userViewHolder.imageGroup);
                        }else{
                            StorageReference imgRef = mStore.getReference("groups/" + g.getUid());
                            if (imgRef != null) {
                                imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            Uri img = task.getResult();
                                            if (img != null) {
                                                Cache.saveUriFile(g.getUid(), img);
                                                g.setImage_url(Cache.getImageDate(g.getUid()));
                                                mStoreBase.collection("groups").document(g.getUid()).set(g);
                                                Glide.with(getContext()).load(img).fitCenter().centerCrop().into(userViewHolder.imageGroup);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
                */
            }
        };
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));

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


        }
    }

    private void updateEvents() {
        /*itemEvents.clear();

        mStoreBase.collection("events")
                .whereEqualTo("ownerdoc", USER_REFERENCE)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().size() > 0) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Evenement e = document.toObject(Evenement.class);
                                    itemEvents.add(e);
                                }
                                onUpdateAdapter();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("SPLASH", "Erreur : " + e.getMessage());
            }
        });

        for(int i = 0; i < EVENTS_LIST.size(); i++){
            MarkerOptions m = new MarkerOptions();
            Evenement e = Constants.EVENTS_LIST.get(i+"");

            m.title(e.getNom());

            if (e.getProprietaire().equalsIgnoreCase(USER_ID)) {
                itemEvents.add(e);
            }
        }

        if(itemEvents.isEmpty()){
            mRecyclerViewEvent.setVisibility(View.GONE);
            linearSansEvent.setVisibility(View.VISIBLE);
        }else{
            mRecyclerViewEvent.setVisibility(View.VISIBLE);
            linearSansEvent.setVisibility(View.GONE);
        }

        mAdapterEventList = new ItemMesEventListAdapter(itemEvents);


        mLayoutManagerMesEvent = new LinearLayoutManager(
                rootView.getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        mRecyclerViewEvent.setLayoutManager(mLayoutManagerMesEvent);
        mRecyclerViewEvent.setAdapter(mAdapterEventList);

        mAdapterEventList.setOnItemClickListener(new ItemMesEventListAdapter.OnItemClickListener() {
            @Override
            public void onDisplayClick(int position) {
                Constants.targetZoom = MARKERS_EVENT.get(itemEvents.get(position).getId()+"").getPosition();
                EvenementsFragment.viewPager.setCurrentItem(0);
            }

            @Override
            public void onDeleteClick(final int position) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Vous êtes sur le point de supprimer un évènement ! Cette action est irréversible ! Voulez-vous continuez ?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getContext(), "Vous avez supprimé(e) un évènement !", Toast.LENGTH_SHORT).show();
                                removeEvent(position);
                                updateEvents();
                            }
                        })
                        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                builder.create();
                builder.show();
            }

            @Override
            public void onEditClick(int position) {

                /*Evenement e = EVENTS_LIST.get(itemEvents.get(position).getId()+"");

                nameEvent = e.getNom();
                cityEdit = e.getVille();
                adresseEdit = e.getAdresse();
                dateEdit = sdf.format(e.getDate());
                timeEdit = e.getTime();
                descEdit = e.getDescription();
                visibiliteEdit = e.getVisibilite();

                CreateEventActivity.edit = true;

                startActivity(new Intent(getContext(), CreateEventActivity.class));
                Toast.makeText(getContext(), "Edit event", Toast.LENGTH_SHORT).show();

            }
        });*/

    }

}