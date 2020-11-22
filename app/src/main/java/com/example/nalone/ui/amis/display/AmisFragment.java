package com.example.nalone.ui.amis.display;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nalone.Cache;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.R;
import com.example.nalone.User;
import com.example.nalone.ui.amis.MesAmisFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;

public class AmisFragment extends Fragment {

    private SearchView search_bar;

    private NavController navController;
    private TextView resultat;
    private ArrayList<ItemPerson> items = new ArrayList<>();
    private static String message = "null";
    private View rootView;
    private ProgressBar loading;

    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView mRecyclerView;
    private List<String> friends;

    private int nbInvit = 0;
    private TextView textViewNbInvit;
    private CardView cardViewInvits;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_amis, container, false);
        search_bar = rootView.findViewById(R.id.search_bar_amis);
        resultat = rootView.findViewById(R.id.resultatText_amis);
        resultat.setVisibility(View.GONE);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        loading = rootView.findViewById(R.id.amis_loading);
        textViewNbInvit = rootView.findViewById(R.id.nbInvits);
        cardViewInvits = rootView.findViewById(R.id.cardViewInvits);

        mRecyclerView = rootView.findViewById(R.id.recyclerViewMesAmis);
        buttonBack.setVisibility(View.GONE);

        adapterUsers();


        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_bar.onActionViewExpanded();
            }
        });

        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        cardViewInvits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_amis_to_navigation_invitations);
            }
        });

        mStoreBase.collection("users").document(USER.getUid()).collection("friends").whereEqualTo("status", "received")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.w("Invitations", document.getId());
                                nbInvit++;
                                Log.w("Invitations", nbInvit+"");
                            }
                        }
                        Log.w("Invitations", nbInvit+"");
                        if(nbInvit != 0){
                            Log.w("Invitations", "Pop up");
                            cardViewInvits.setVisibility(View.VISIBLE);
                            textViewNbInvit.setText(nbInvit+"");

                        }else{
                            cardViewInvits.setVisibility(View.GONE);
                        }
                        loading.setVisibility(View.GONE);


                    }

                });



        return rootView;
    }

    private void adapterUsers() {
        friends = new ArrayList<>();
        mStoreBase.collection("users").document(USER.getUid()).collection("friends").whereEqualTo("status", "add").limit(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                friends.add(document.getId());
                            }

                            //query
                            if (!friends.isEmpty()) {
                                Query query = mStoreBase.collection("users").whereIn("uid", friends);
                                FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();

                                adapter = new FirestoreRecyclerAdapter<User, UserViewHolder>(options) {
                                    @NonNull
                                    @Override
                                    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
                                        return new UserViewHolder(view);
                                    }

                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    protected void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, int i, @NonNull final User u) {
                                        userViewHolder.villePers.setText(u.getCity());
                                        userViewHolder.nomInvit.setText(u.getFirst_name() + " " + u.getLast_name());
                                        userViewHolder.button.setImageResource(R.drawable.ic_baseline_delete_24);

                                        if (u.getImage_url() != null) {
                                            if (!Cache.fileExists(u.getUid())) {
                                                Log.w("Cache", "Loading : " + u.getFirst_name());

                                            } else {
                                                Uri imgCache = Cache.getUriFromUid(u.getUid());
                                                if(Cache.getImageDate(USER.getUid()).equals(u.getImage_url())) {
                                                    Log.w("image", "get image from cache");
                                                    Glide.with(getContext()).load(imgCache).fitCenter().centerCrop().into(userViewHolder.imagePerson);
                                                }else{
                                                    StorageReference imgRef = mStore.getReference("users/" + u.getUid());
                                                    if (imgRef != null) {
                                                        imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Uri> task) {
                                                                if (task.isSuccessful()) {
                                                                    Uri img = task.getResult();
                                                                    if (img != null) {
                                                                        u.setImage_url(new Timestamp(new Date(System.currentTimeMillis())));
                                                                        mStoreBase.collection("users").document(u.getUid()).set(u);
                                                                        Cache.saveUriFile(u.getUid(), img);
                                                                        Glide.with(getContext()).load(img).fitCenter().centerCrop().into(userViewHolder.imagePerson);
                                                                    }
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        }


                                        userViewHolder.layoutProfil.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                showPopUpProfil(u);
                                            }
                                        });

                                        userViewHolder.button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                removeFriend(u.getUid());
                                                adapter.notifyDataSetChanged();
                                                adapterUsers();
                                            }
                                        });
                                        loading.setVisibility(View.GONE);


                                    }
                                };
                                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                mRecyclerView.setAdapter(adapter);

                                adapter.startListening();
                            }
                        }
                    }
                });

    }


    private class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView nomInvit;
        private TextView villePers;
        private LinearLayout layoutProfil;
        private ImageView imagePerson;
        private ImageView button;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomInvit);
            villePers = itemView.findViewById(R.id.villePers);
            layoutProfil = itemView.findViewById(R.id.layoutProfil);
            imagePerson = itemView.findViewById(R.id.imagePerson);
            button = itemView.findViewById(R.id.imageView19);

        }

    }
    private void removeFriend(final String uid) {
        mStoreBase.collection("users").document(USER.getUid()).
                collection("friends").document(uid).delete();

        mStoreBase.collection("users").document(uid).
                collection("friends").document(USER.getUid()).delete();

        Toast.makeText(getContext(), "Vous avez supprimé un amis !", Toast.LENGTH_SHORT).show();

        adapter.notifyDataSetChanged();
        adapterUsers();
    }


    public void showPopUpProfil(final User u) {
        PopupProfilFragment.USER_LOAD = u;
        mStoreBase.collection("users").document(USER.getUid()).collection("friends").whereEqualTo("status", "add").whereEqualTo("uid", u.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                PopupProfilFragment.button = 0;
            }
        });

        mStoreBase.collection("users").document(USER.getUid()).collection("friends").whereEqualTo("status", "send").whereEqualTo("uid", u.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                PopupProfilFragment.button = R.drawable.ic_round_hourglass_top_24;
            }
        });

        mStoreBase.collection("users").document(USER.getUid()).collection("friends").whereEqualTo("status", "received").whereEqualTo("uid", u.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                PopupProfilFragment.button = R.drawable.ic_round_mail_24;
            }
        });
        navController.navigate(R.id.action_navigation_amis_to_navigation_popup_profil);
    }


    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null) {
            adapter.stopListening();
        }
    }

}