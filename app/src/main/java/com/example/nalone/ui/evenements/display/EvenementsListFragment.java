package com.example.nalone.ui.evenements.display;

import android.graphics.Color;
import android.icu.text.IDNA;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nalone.Evenement;
import com.example.nalone.ModelDataEvent;
import com.example.nalone.R;
import com.example.nalone.User;
import com.example.nalone.ModelData;
import com.example.nalone.adapter.ItemFiltreAdapter;
import com.example.nalone.items.ItemFiltre;
import com.example.nalone.items.ItemImagePerson;
import com.example.nalone.ui.evenements.EvenementsFragment;
import com.example.nalone.ui.evenements.InfosEvenementsActivity;
import com.example.nalone.util.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.USER_REFERENCE;
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
    private NavController navController;

    private int iterator = 0;
    private List<String> events;
    private ProgressBar loading;



    public EvenementsListFragment() {
        // Required empty public constructor
    }

    public void reload(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
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

        events = new ArrayList<>();
        loading = rootView.findViewById(R.id.loading);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        filtres.add(new ItemFiltre("Art"));
        filtres.add(new ItemFiltre("Sport"));
        filtres.add(new ItemFiltre("Musique"));
        filtres.add(new ItemFiltre("Fête"));
        filtres.add(new ItemFiltre("Danse"));
        filtres.add(new ItemFiltre("Numérique"));
        filtres.add(new ItemFiltre("Informatique"));
        filtres.add(new ItemFiltre("Manifestation"));

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

        adapterEvents();


    }

    private void adapterEvents() {

        mStoreBase.collection("users").document(USER_ID).collection("events")
                .get()
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        linearSansEvent.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                    }
                });
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
                                Query query = mStoreBase.collection("events").whereNotIn("uid",events);
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
                                        if(eventTermine(new Date(),e.getDate().toDate())){
                                            //holder.linearTermine.setVisibility(View.VISIBLE);
                                            mStoreBase.collection("events").document(e.getUid())
                                                    .collection("members")
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if(task.isSuccessful()){
                                                                for (QueryDocumentSnapshot document : task.getResult()){
                                                                    mStoreBase.collection("users").document(document.getId()).collection("events").document(e.getUid()).delete();
                                                                }
                                                                mStoreBase.collection("events").document(e.getUid()).delete();
                                                            }
                                                        }
                                                    });
                                            //createFragment();
                                        }else{
                                            holder.mTitle.setText((e.getName()));
                                            holder.mDate.setText((dateFormat.format(e.getDate().toDate())));
                                            holder.mTime.setText((timeFormat.format(e.getDate().toDate())));
                                            holder.mVille.setText((e.getCity()));
                                            //holder.mDescription.setText((e.getDescription()));
                                            holder.mProprietaire.setText(e.getOwner());
                                            holder.textViewNbMembers.setText(e.getNbMembers()+"");


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
                                                    InfosEvenementsActivity.EVENT_LOAD = e;
                                                    InfosEvenementsActivity.type = "nouveau";
                                                    navController.navigate(R.id.action_navigation_evenements_to_navigation_infos_events);
                                                }
                                            });

                                            holder.mInscrire.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    e.setNbMembers(e.getNbMembers()+1);
                                                    mStoreBase.collection("events").document(e.getUid()).set(e);
                                                    ModelDataEvent owner = new ModelDataEvent(false,"add", e.getOwnerDoc());
                                                    ModelDataEvent m = new ModelDataEvent(false,"add", mStoreBase.collection("users").document(USER_ID));
                                                    mStoreBase.collection("events").document(e.getUid()).collection("members").document(USER.getUid()).set(m);
                                                    mStoreBase.collection("users").document(USER_ID).collection("events").document(e.getUid()).set(owner);
                                                    Toast.makeText(getContext(), "Vous êtes inscrit à l'évènement " + e.getName() + " !", Toast.LENGTH_SHORT).show();
                                                    createFragment();
                                                }
                                            });
                                            loading.setVisibility(View.GONE);
                                        }

                                        }
                                };
                                //mRecyclerView.setHasFixedSize(true);
                                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                                mRecyclerView.setAdapter(adapter);
                                adapter.startListening();

                                Log.w("count", iterator + "");

                        }
                    }
                });

        }


    public boolean eventTermine(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        Log.w("date","startDate : " + startDate);
        Log.w("date","endDate : "+ endDate);
        Log.w("date","different : " + different);

        if(different < -86400000){
            return true;
        }else{
            return false;
        }
    }
    private class EventViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTitle;
        public TextView mDate;
        public TextView mTime;
        public TextView mVille;
        //public TextView mDescription;
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
            //mDescription = itemView.findViewById(R.id.descriptionEventList);
            mProprietaire = itemView.findViewById(R.id.ownerEventList);
            mAfficher = itemView.findViewById(R.id.buttonAfficherEventList);
            mInscrire = itemView.findViewById(R.id.buttonInscrirEventList);
            mCarwViewOwner = itemView.findViewById(R.id.backGroundOwner);
            textViewNbMembers = itemView.findViewById(R.id.textViewNbMembers);

        }
    }
}