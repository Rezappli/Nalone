package com.example.nalone.ui.evenements.display;

import android.graphics.Color;
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
import android.widget.Toast;

import com.example.nalone.Evenement;
import com.example.nalone.R;
import com.example.nalone.User;
import com.example.nalone.ModelData;
import com.example.nalone.adapter.ItemFiltreAdapter;
import com.example.nalone.items.ItemFiltre;
import com.example.nalone.items.ItemImagePerson;
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

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.dateFormat;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.timeFormat;

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
    private List<String> events = new ArrayList<>();


    public EvenementsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_evenements_list, container, false);
        createFragment();
        return rootView;
    }

    private void createFragment() {
        final List<ItemImagePerson> membres_inscrits = new ArrayList<>();


        filtres.add(new ItemFiltre("Art"));
        filtres.add(new ItemFiltre("Sport"));
        filtres.add(new ItemFiltre("Musique"));
        filtres.add(new ItemFiltre("Fête"));
        filtres.add(new ItemFiltre("Danse"));
        filtres.add(new ItemFiltre("Numérique"));
        filtres.add(new ItemFiltre("Informatique"));
        filtres.add(new ItemFiltre("Manifestation"));

        mStoreBase.collection("users").document(USER_ID).collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                events.add(document.getId());
                                Log.w("event", " Ajout list");
                            }
                            adapterEvents();
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

        if(adapter != null){
            if(adapter.getItemCount() == 0){
                linearSansEvent.setVisibility(View.VISIBLE);
            }else{
                linearSansEvent.setVisibility(View.GONE);
            }
        }

    }

    private void adapterEvents() {

        if(!events.isEmpty()) {
            Query query = mStoreBase.collection("events").whereNotIn("uid", events).limit(10);


            FirestoreRecyclerOptions<Evenement> options = new FirestoreRecyclerOptions.Builder<Evenement>().setQuery(query, Evenement.class).build();

            adapter = new FirestoreRecyclerAdapter<Evenement, EventViewHolder>(options) {
                @NonNull
                @Override
                public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evenements_list, parent, false);
                    return new EventViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull final EventViewHolder holder, int i, @NonNull final Evenement e) {
                    //holder.mImageView.setImageResource(e.getImage());
                    holder.mTitle.setText((e.getName()));
                    holder.mDate.setText((dateFormat.format(e.getDate().toDate())));
                    holder.mTime.setText((timeFormat.format(e.getDate().toDate())));
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
                                            if (u.getCursus().equalsIgnoreCase("Informatique")) {
                                                holder.mCarwViewOwner.setCardBackgroundColor(Color.RED);
                                            }

                                            if (u.getCursus().equalsIgnoreCase("TC")) {
                                                holder.mCarwViewOwner.setCardBackgroundColor(Color.parseColor("#00E9FD"));
                                            }

                                            if (u.getCursus().equalsIgnoreCase("MMI")) {
                                                holder.mCarwViewOwner.setCardBackgroundColor(Color.parseColor("#FF1EED"));
                                            }

                                            if (u.getCursus().equalsIgnoreCase("GB")) {
                                                holder.mCarwViewOwner.setCardBackgroundColor(Color.parseColor("#41EC57"));
                                            }

                                            if (u.getCursus().equalsIgnoreCase("LP")) {
                                                holder.mCarwViewOwner.setCardBackgroundColor((Color.parseColor("#EC9538")));
                                            }

                                            Constants.setUserImage(u, getContext(), holder.mImageView);
                                        }

                                    }
                                }
                            });


                    holder.mAfficher.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Constants.targetZoom = new LatLng(e.getLatitude(), e.getLongitude());
                            EvenementsFragment.viewPager.setCurrentItem(0);
                        }
                    });

                    holder.mInscrire.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ModelData owner = new ModelData("add", e.getOwnerDoc());
                            ModelData m = new ModelData("add", mStoreBase.collection("users").document(USER_ID));
                            mStoreBase.collection("events").document(e.getUid()).collection("members").document(USER.getUid()).set(m);
                            mStoreBase.collection("users").document(USER_ID).collection("events").document(e.getUid()).set(owner);
                            Toast.makeText(getContext(), "Vous êtes inscrit à l'évènement " + e.getName() + " !", Toast.LENGTH_SHORT).show();
                            createFragment();
                        }
                    });

                }
            };
            //mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            mRecyclerView.setAdapter(adapter);
            adapter.startListening();

            Log.w("count", iterator + "");
        }else{
            linearSansEvent.setVisibility(View.VISIBLE);
        }

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
        public CardView mCarwViewOwner;
        public  TextView textViewNbMembers;

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
            mCarwViewOwner = itemView.findViewById(R.id.backGroundOwner);
            textViewNbMembers = itemView.findViewById(R.id.textViewNbMembers);

        }
    }
}