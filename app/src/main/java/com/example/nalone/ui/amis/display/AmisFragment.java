package com.example.nalone.ui.amis.display;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.example.nalone.objects.ModelData;
import com.example.nalone.R;
import com.example.nalone.objects.User;
import com.example.nalone.ui.message.ChatActivityFriend;
import com.example.nalone.util.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.ON_FRIENDS_ACTIVITY;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.mStoreBase;

public class AmisFragment extends Fragment {

    private SearchView search_bar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private NavController navController;
    private TextView resultat;
    private View rootView;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView mRecyclerView;
    private List<String> friends;
    private int nbInvit;
    private CardView cardViewInvits;
    private TextView textViewNbInvit;
    private LinearLayout linearSansMesAmis;
    private ProgressBar loading;
    private boolean swipe;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_amis, container, false);
        //createFragment();
        return rootView;
    }

    private void createFragment() {
        nbInvit = 0;
        search_bar = rootView.findViewById(R.id.search_bar_amis);
        resultat = rootView.findViewById(R.id.resultatText_amis);
        resultat.setVisibility(View.GONE);
        linearSansMesAmis = rootView.findViewById(R.id.linearSansMesAmis);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        buttonBack.setVisibility(View.GONE);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewMesAmis);
        cardViewInvits = rootView.findViewById(R.id.cardViewInvits);
        textViewNbInvit = rootView.findViewById(R.id.nbInvits);
        mSwipeRefreshLayout = rootView.findViewById(R.id.AmisSwipeRefreshLayout);
        loading = rootView.findViewById(R.id.loading);

        cardViewInvits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_amis_to_navigation_invitations);
            }
        });

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

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createFragment();
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
                        //loading.setVisibility(View.GONE);
                    }
                });

        adapterUsers();
    }

    private void adapterUsers() {
        mSwipeRefreshLayout.setRefreshing(true);
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

                            if (!friends.isEmpty()) {
                                Query query = mStoreBase.collection("users").whereIn("uid", friends);
                                FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();

                                adapter = new FirestoreRecyclerAdapter<User, UserViewHolder>(options) {
                                    @NonNull
                                    @Override
                                    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_layout, parent, false);
                                        return new UserViewHolder(view);
                                    }

                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    protected void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, int i, @NonNull final User u) {
                                        userViewHolder.villePers.setText(u.getCity());
                                        userViewHolder.nomInvit.setText(u.getFirst_name() + " " + u.getLast_name());
                                        //userViewHolder.button.setImageResource(R.drawable.ic_baseline_delete_24);

                                        userViewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, userViewHolder.swipeLayout.findViewById(R.id.bottom_wrapper1));

                                        userViewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, userViewHolder.swipeLayout.findViewById(R.id.bottom_wraper));
                                        userViewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                                            @Override
                                            public void onStartOpen(SwipeLayout layout) {

                                            }

                                            @Override
                                            public void onOpen(SwipeLayout layout) {
                                                swipe = true;
                                            }

                                            @Override
                                            public void onStartClose(SwipeLayout layout) {

                                            }

                                            @Override
                                            public void onClose(SwipeLayout layout) {

                                            }

                                            @Override
                                            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

                                            }

                                            @Override
                                            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

                                            }
                                        });

                                        userViewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Toast.makeText(getContext(), " Click : " , Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        userViewHolder.btnLocation.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                showPopUpProfil(u);
                                            }
                                        });

                                        userViewHolder.Share.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                ChatActivityFriend.USER_LOAD = u;
                                                startActivity(new Intent(getContext(), ChatActivityFriend.class));
                                            }
                                        });

                                        userViewHolder.Appel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                                                        != PackageManager.PERMISSION_GRANTED) {
                                                    getActivity().requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 0);
                                                    return;
                                                }
                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                callIntent.setData(Uri.parse("tel:"+u.getNumber()));
                                                startActivity(callIntent);

                                            }
                                        });

                                        userViewHolder.Delete.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                removeFriend(u.getUid());
                                                createFragment();
                                            }
                                        });

                                        if(u.getCursus().equalsIgnoreCase("Informatique")){
                                            userViewHolder.cardViewPhotoPerson.setCardBackgroundColor(Color.RED);
                                        }

                                        if(u.getCursus().equalsIgnoreCase("TC")){
                                            userViewHolder.cardViewPhotoPerson.setCardBackgroundColor(Color.parseColor("#00E9FD"));
                                        }

                                        if(u.getCursus().equalsIgnoreCase("MMI")){
                                            userViewHolder.cardViewPhotoPerson.setCardBackgroundColor(Color.parseColor("#FF1EED"));
                                        }

                                        if(u.getCursus().equalsIgnoreCase("GB")){
                                            userViewHolder.cardViewPhotoPerson.setCardBackgroundColor(Color.parseColor("#41EC57"));
                                        }

                                        if(u.getCursus().equalsIgnoreCase("LP")){
                                            userViewHolder.cardViewPhotoPerson.setCardBackgroundColor((Color.parseColor("#EC9538")));
                                        }

                                        Constants.setUserImage(u, getContext(), userViewHolder.imagePerson);

                                        userViewHolder.layoutProfil.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                showPopUpProfil(u);
                                            }
                                        });

                                        userViewHolder.button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if(swipe == false) {
                                                    userViewHolder.swipeLayout.open();
                                                    swipe = true;
                                                }
                                                else{
                                                    userViewHolder.swipeLayout.close();
                                                    swipe = false;
                                                }



                                            }
                                        });
                                        loading.setVisibility(View.GONE);
                                    }
                                };
                                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                                mRecyclerView.setAdapter(adapter);

                                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                        super.onScrollStateChanged(recyclerView, newState);
                                        Log.e("RecyclerView", "onScrollStateChanged");
                                    }
                                    @Override
                                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                        super.onScrolled(recyclerView, dx, dy);
                                    }
                                });
                                adapter.startListening();


                            }else{
                                linearSansMesAmis.setVisibility(View.VISIBLE);
                                loading.setVisibility(View.GONE);
                            }
                        }
                    }
                });
        mSwipeRefreshLayout.setRefreshing(false);
        Log.w("Refresh", "Set refresing false");
    }


    private class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView nomInvit;
        private TextView villePers;
        private LinearLayout layoutProfil;
        private ImageView imagePerson;
        private ImageView button;
        private CardView cardViewPhotoPerson;
        public SwipeLayout swipeLayout;
        public ImageView Delete;
        public ImageView Appel;
        public ImageView Share;
        public ImageButton btnLocation;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomInvit);
            villePers = itemView.findViewById(R.id.villePers);
            layoutProfil = itemView.findViewById(R.id.layoutProfil);
            imagePerson = itemView.findViewById(R.id.imagePerson);
            button = itemView.findViewById(R.id.buttonImage);
            cardViewPhotoPerson = itemView.findViewById(R.id.cardViewPhotoPerson);
            //
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            Delete = (ImageView) itemView.findViewById(R.id.Delete);
            Appel = (ImageView) itemView.findViewById(R.id.Appel);
            Share = (ImageView) itemView.findViewById(R.id.Share);
            btnLocation = (ImageButton) itemView.findViewById(R.id.btnLocation);
        }

    }

    private void removeFriend(final String uid) {
        mStoreBase.collection("users").document(USER.getUid()).
                collection("friends").document(uid);
        mStoreBase.collection("users").document(USER.getUid()).
                collection("friends").document(uid).delete();

        mStoreBase.collection("users").document(uid).
                collection("friends").document(USER.getUid()).delete();

        Toast.makeText(getContext(), "Vous avez supprimé un amis !", Toast.LENGTH_SHORT).show();
    }


    public void showPopUpProfil(final User u) {
        PopupProfilFragment.USER_LOAD = u;
        mStoreBase.collection("users").document(USER.getUid()).collection("friends").document(u.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ModelData data = task.getResult().toObject(ModelData.class);
                    if(data.getStatus().equalsIgnoreCase("send")){
                        PopupProfilFragment.button = R.drawable.ic_round_hourglass_top_24;
                    }else if(data.getStatus().equalsIgnoreCase("received")){
                        PopupProfilFragment.button = R.drawable.ic_round_mail_24;
                    }
                    PopupProfilFragment.type = "amis";
                    navController.navigate(R.id.action_navigation_amis_to_navigation_popup_profil);
            }
        });
    }

    @Override
    public void onResume(){
        nbInvit = 0;
        ON_FRIENDS_ACTIVITY = true;
        createFragment();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ON_FRIENDS_ACTIVITY = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null) {
            adapter.stopListening();
        }
    }

}