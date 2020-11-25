package com.example.nalone.ui.evenements.display;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nalone.Evenement;
import com.example.nalone.InfosEvenementsActivity;
import com.example.nalone.R;
import com.example.nalone.adapter.ItemFiltreAdapter;
import com.example.nalone.items.ItemFiltre;
import com.example.nalone.items.ItemImagePerson;
import com.example.nalone.ui.evenements.EvenementsFragment;
import com.example.nalone.util.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.mStoreBase;

public class EvenementsListFragment extends Fragment {

    private ArrayList<Evenement> itemEvents = new ArrayList<>();

    private View rootView;

    private final List<ItemFiltre> filtres = new ArrayList<>();
    private RecyclerView mRecyclerViewFiltre;
    private RecyclerView.LayoutManager mLayoutManagerFiltre;

    private RecyclerView mRecyclerView;
    private FirestoreRecyclerAdapter adapter;
    private LinearLayout linearSansEvent;
    private ItemFiltreAdapter mAdapterFiltre;

    private int iterator = 0;


    public EvenementsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_evenements_list, container, false);

        final List<ItemImagePerson> membres_inscrits = new ArrayList<>();


        filtres.add(new ItemFiltre("Art"));
        filtres.add(new ItemFiltre("Sport"));
        filtres.add(new ItemFiltre("Musique"));
        filtres.add(new ItemFiltre("Fête"));
        filtres.add(new ItemFiltre("Danse"));
        filtres.add(new ItemFiltre("Numérique"));
        filtres.add(new ItemFiltre("Informatique"));
        filtres.add(new ItemFiltre("Manifestation"));

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
                            adapterEvents();
                            if(iterator == 0){
                                linearSansEvent.setVisibility(View.VISIBLE);
                            }else{
                                linearSansEvent.setVisibility(View.GONE);
                            }
                        }
                    }
                });

        linearSansEvent = rootView.findViewById(R.id.linearSansEvent);

        linearSansEvent.setVisibility(View.GONE);

        mAdapterFiltre = new ItemFiltreAdapter(filtres);

        mRecyclerViewFiltre = rootView.findViewById(R.id.recyclerViewFiltre);
        mLayoutManagerFiltre = new LinearLayoutManager(
                rootView.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        mRecyclerViewFiltre.setLayoutManager(mLayoutManagerFiltre);
        mRecyclerViewFiltre.setAdapter(mAdapterFiltre);

        mRecyclerView = rootView.findViewById(R.id.recyclerViewEventList);


        return rootView;
    }

    private void adapterEvents() {
        DocumentReference ref = mStoreBase.document("users/"+USER_ID);
        Query query = mStoreBase.collection("events").whereNotEqualTo("ownerDoc", ref);
        FirestoreRecyclerOptions<Evenement> options = new FirestoreRecyclerOptions.Builder<Evenement>().setQuery(query, Evenement.class).build();

        adapter = new FirestoreRecyclerAdapter<Evenement, EventViewHolder>(options) {
            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evenements_list,parent,false);
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


                holder.mAfficher.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Constants.targetZoom = new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude());
                        EvenementsFragment.viewPager.setCurrentItem(0);
                    }
                });

                holder.mInscrire.setOnClickListener(new View.OnClickListener() {
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setAdapter(adapter);
        adapter.startListening();

        Log.w("count", iterator + "");

        }


    private class EventViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTitle;
        public TextView mDate;
        public TextView mTime;
        public TextView mVille;
        public TextView mDescription;
        public TextView mProprietaire;
        public Button mInscrire, mAfficher;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageOwnerEventList);
            mTitle = itemView.findViewById(R.id.titleEventList);
            mDate = itemView.findViewById(R.id.dateEventList);
            mTime = itemView.findViewById(R.id.timeEventList);
            mVille = itemView.findViewById(R.id.villeEventList);
            mDescription = itemView.findViewById(R.id.descriptionEventList);
            mProprietaire = itemView.findViewById(R.id.ownerEventList);
            mAfficher = itemView.findViewById(R.id.buttonAfficherEventList);
            mInscrire = itemView.findViewById(R.id.buttonInscrirEventList);

        }
    }

    private void updateItems(){
        itemEvents.clear();
        /*float[] results = new float[1];
        for(int i = 0; i < EVENTS_LIST.size(); i++){
            Evenement e = EVENTS_LIST.get(i+"");
            MarkerOptions m = MARKERS_EVENT.get(e.getId()+"");

            m.title(e.getNom());

            if(e.getVisibilite().equals(Visibility.PRIVE)){
                if(e.getMembres_inscrits().contains(USER_ID)){
                    Location.distanceBetween(USER_LATLNG.latitude, USER_LATLNG.longitude,
                            m.getPosition().latitude, m.getPosition().longitude, results);
                    if(results[0] < range) {
                        itemEvents.add(e);
                    }
                }
            }else{
                Location.distanceBetween(USER_LATLNG.latitude, USER_LATLNG.longitude,
                        m.getPosition().latitude, m.getPosition().longitude, results);
                if(results[0] < 50000) {
                    itemEvents.add(e);
                }
            }

        }*/

        //mAdapterEventList = new ItemEventListAdapter(itemEvents);

        /*mRecyclerViewEvent = rootView.findViewById(R.id.recyclerViewEventList);
        mLayoutManagerEvent = new LinearLayoutManager(
                rootView.getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        mRecyclerViewEvent.setLayoutManager(mLayoutManagerEvent);*/
        //mRecyclerViewEvent.setAdapter(mAdapterEventList);

        /*mAdapterEventList.setOnItemClickListener(new ItemEventListAdapter.OnItemClickListener() {
            @Override
            public void onDisplayClick(int position) {
                Constants.targetZoom = new LatLng(itemEvents.get(position).getLocation().getLatitude(), itemEvents.get(position).getLocation().getLongitude());
                EvenementsFragment.viewPager.setCurrentItem(0);
            }

            @Override
            public void onSignInClick(int position) {
                InfosEvenementsActivity.EVENT_LOAD = itemEvents.get(position);
                startActivity(new Intent(getContext(), InfosEvenementsActivity.class));
            }
        });*/
    }


    //@Override
    public void onUpdateAdapter() {

    }

}