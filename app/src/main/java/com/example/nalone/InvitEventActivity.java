package com.example.nalone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.ui.amis.display.MesInvitationsFragment;
import com.example.nalone.ui.amis.display.PopupProfilFragment;
import com.example.nalone.ui.evenements.InfosEvenementsActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.dateFormat;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.setUserImage;
import static com.example.nalone.util.Constants.timeFormat;

public class InvitEventActivity extends Fragment {

    private NavController navController;
    private RecyclerView mRecyclerView;
    private FirestoreRecyclerAdapter adapter;
    private View rootView;
    private ArrayList<ItemPerson> invits = new ArrayList<>();
    private ProgressBar loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_invit_event, container, false);
        buttonBack.setVisibility(View.VISIBLE);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_invitations_to_navigation_amis);
            }
        });

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewInvitEvent);
        adapterInvits();
        return rootView;
        }
    private void adapterInvits() {

        Query query = mStoreBase.collection("users").document(USER_ID).collection("events_received");
        FirestoreRecyclerOptions<Evenement> options = new FirestoreRecyclerOptions.Builder<Evenement>().setQuery(query, Evenement.class).build();

        adapter = new FirestoreRecyclerAdapter<Evenement, EventViewHolder>(options) {
            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evenements_list, parent, false);
                return new EventViewHolder(view);
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onBindViewHolder(@NonNull final EventViewHolder userViewHolder, int i, @NonNull final Evenement e) {
                userViewHolder.villePers.setText(e.getCity());
                userViewHolder.nomInvit.setText(e.getName());
                userViewHolder.dateInvit.setText((dateFormat.format(e.getDate().toDate())));
                userViewHolder.timeInvit.setText((timeFormat.format(e.getDate().toDate())));
                userViewHolder.ownerInvit.setText(e.getOwner());
                userViewHolder.nbParticipant.setText(e.getNbMembers()+"");

                //setUserImage(e.getOwnerDoc(),getContext(),userViewHolder.imagePerson);

                userViewHolder.layoutProfil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InfosEvenementsActivity.EVENT_LOAD = e;
                        //navController.navigate();
                    }
                });

                userViewHolder.buttonAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        acceptFriendRequest(e);
                    }
                });

                userViewHolder.buttonRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        declineFriendRequest(e);
                    }
                });
//                loading.setVisibility(View.GONE);

            }
        };
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }


        private class EventViewHolder extends RecyclerView.ViewHolder {

            private TextView nomInvit, dateInvit, timeInvit, ownerInvit, nbParticipant;
            private LinearLayout layoutProfil;
            private ImageView imagePerson;
            private TextView villePers;
            private ImageView buttonAdd, buttonRemove;

            public EventViewHolder(@NonNull View itemView) {
                super(itemView);

                nomInvit = itemView.findViewById(R.id.titleEventList);
                dateInvit = itemView.findViewById(R.id.dateEventList);
                timeInvit = itemView.findViewById(R.id.timeEventList);
                ownerInvit = itemView.findViewById(R.id.ownerEventList);
                villePers = itemView.findViewById(R.id.villeEventList);
                layoutProfil = itemView.findViewById(R.id.layoutInvit);
                imagePerson = itemView.findViewById(R.id.imageOwnerEventList);
                buttonAdd = itemView.findViewById(R.id.acceptInvit);
                buttonRemove = itemView.findViewById(R.id.declineInvit);
                nbParticipant = itemView.findViewById(R.id.textViewNbMembers);
            }

        }

        private void acceptFriendRequest(Evenement e) {
            ModelData data1 = new ModelData("add", mStoreBase.collection("users").document(USER.getUid()));
            ModelData data2 = new ModelData("add", mStoreBase.collection("users").document(USER_ID));
            mStoreBase.collection("events").whereEqualTo("uid", e.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            Evenement e = doc.toObject(Evenement.class);
                            e.setNbMembers(e.getNbMembers()+1);
                            mStoreBase.collection("events").document(e.getUid()).set(e);
                        }
                    }
                }
            });
            mStoreBase.collection("users").document(e.getOwnerDoc().getId()).collection("events_create").document(e.getUid()).collection("members").document(USER_ID).set(USER);
            mStoreBase.collection("events").document(e.getUid()).collection("members").document(USER_ID).set(USER);
            mStoreBase.collection("users").document(USER_ID).collection("events_join").document(e.getUid()).set(e);
            mStoreBase.collection("users").document(USER_ID).collection("events_received").document(e.getUid()).delete();
            Toast.makeText(getContext(), "Vous avez rejoint l'évenement "+ e.getName(), Toast.LENGTH_SHORT).show();
            //navController.navigate(R.id.action_navigation_invitations_self);
            adapterInvits();
        }

        private void declineFriendRequest(Evenement e) {
            mStoreBase.collection("users").document(USER_ID).collection("events_received").document(e.getUid()).delete();
            Toast.makeText(getContext(), "Invitation refusée", Toast.LENGTH_SHORT).show();
            adapterInvits();
        }

    }
