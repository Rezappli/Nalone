package com.example.nalone.ui.message.display;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nalone.Cache;
import com.example.nalone.R;
import com.example.nalone.User;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.ui.amis.display.PopupProfilFragment;
import com.example.nalone.ui.message.ChatActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;

public class MessagesAmisFragment extends Fragment {

    private SearchView search_bar;

    private NavController navController;
    private ArrayList<ItemPerson> items = new ArrayList<>();
    private static String message = "null";
    private View rootView;
    private ProgressBar loading;

    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView mRecyclerView;
    private List<String> chats;
    private ImageView addMessage;
    private DocumentReference refChat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_messages_amis, container, false);

        createFragment(rootView);

        return rootView;
    }

    private void createFragment(View rootView) {
        search_bar = rootView.findViewById(R.id.search_bar_amis);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        loading = rootView.findViewById(R.id.amis_loading);
        buttonBack.setVisibility(View.GONE);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewMessagesAmis);

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

    }

    private void adapterUsers() {
        mStoreBase.collection("users").document(USER.getUid()).collection("chat_friends").limit(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Query query = mStoreBase.collection("users").whereEqualTo("uid", document.getId());
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
                                        userViewHolder.button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24);

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


                                        if(u.getImage_url() != null) {
                                            if(!Cache.fileExists(u.getUid())) {
                                                StorageReference imgRef = mStore.getReference("users/" + u.getUid());
                                                if (imgRef != null) {
                                                    imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Uri> task) {
                                                            if (task.isSuccessful()) {
                                                                Uri img = task.getResult();
                                                                if (img != null) {
                                                                    Cache.saveUriFile(u.getUid(), img);
                                                                    u.setImage_url(Cache.getImageDate(u.getUid()));
                                                                    mStoreBase.collection("users").document(u.getUid()).set(u);
                                                                    Glide.with(getContext()).load(img).fitCenter().centerCrop().into(userViewHolder.imagePerson);
                                                                }
                                                            }
                                                        }
                                                    });
                                                }
                                            }else{
                                                Uri imgCache = Cache.getUriFromUid(u.getUid());
                                                Log.w("Cache", "Image Cache : " + Cache.getImageDate(u.getUid()));
                                                Log.w("Cache", "Data Cache : " + u.getImage_url());
                                                if(Cache.getImageDate(u.getUid()).equalsIgnoreCase(u.getImage_url())) {
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
                                                                        Cache.saveUriFile(u.getUid(), img);
                                                                        u.setImage_url(Cache.getImageDate(u.getUid()));
                                                                        mStoreBase.collection("users").document(u.getUid()).set(u);
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
                                                showPopUpProfil(u);
                                            }
                                        });
                                        loading.setVisibility(View.GONE);


                                    }
                                };
                                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                mRecyclerView.setAdapter(adapter);
                                adapter.startListening();
                            }
                        }}});




    }


    private class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView nomInvit;
        private TextView villePers;
        private LinearLayout layoutProfil;
        private ImageView imagePerson;
        private ImageView button;
        private CardView cardViewPhotoPerson;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomInvit);
            villePers = itemView.findViewById(R.id.villePers);
            layoutProfil = itemView.findViewById(R.id.layoutProfil);
            imagePerson = itemView.findViewById(R.id.imagePerson);
            button = itemView.findViewById(R.id.buttonImage);
            cardViewPhotoPerson = itemView.findViewById(R.id.cardViewPhotoPerson);


        }

    }
    private void removeFriend(final String uid) {
        if(mStoreBase.collection("users").document(USER.getUid()).
                collection("friends").document(uid) != null){
            mStoreBase.collection("users").document(USER.getUid()).
                    collection("friends").document(uid).delete();

            mStoreBase.collection("users").document(uid).
                    collection("friends").document(USER.getUid()).delete();

            Toast.makeText(getContext(), "Vous avez supprimé un amis !", Toast.LENGTH_SHORT).show();

        }

    }


    public void showPopUpProfil(final User u) {
        ChatActivity.USER_LOAD = u;
        startActivity(new Intent(getContext(), ChatActivity.class));
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null) {
            adapter.stopListening();
        }
    }

}