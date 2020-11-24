package com.example.nalone.ui.message;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nalone.Cache;
import com.example.nalone.User;
import com.example.nalone.R;
import com.example.nalone.UserFriendData;
import com.example.nalone.listeners.FireStoreUsersListeners;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;


public class MessagesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private FirestoreRecyclerAdapter adapter;
    private List<String> friends;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_messages, container, false);
        mRecyclerView = root.findViewById(R.id.recycler);

        friends = new ArrayList<>();

        mStoreBase.collection("users").document(USER.getUid()).collection("friends").whereEqualTo("status", "add").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    friends.add(doc.toObject(User.class).getUid());
                }
                friends.add(USER.getUid());

                Query query = mStoreBase.collection("users").whereNotIn("uid", friends);

                //RecyclerOption
                FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();

                adapter = new FirestoreRecyclerAdapter<User, PersonViewHolder>(options) {
                    @NonNull
                    @Override
                    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
                        return new PersonViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final PersonViewHolder personViewHolder, int i, @NonNull final User u) {

                        personViewHolder.villePers.setText(u.getCity());
                        personViewHolder.nomInvit.setText(u.getFirst_name() + " " + u.getLast_name());
                        personViewHolder.buttonImage.setVisibility(View.GONE);

                        personViewHolder.layoutProfil.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChatActivity.USER_LOAD = u;
                                startActivity(new Intent(getContext(), ChatActivity.class));
                            }
                        });

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
                                                    Glide.with(getContext()).load(img).fitCenter().centerCrop().into(personViewHolder.imagePerson);
                                                }
                                            }
                                        }
                                    });
                                }
                            }else{
                                Uri imgCache = Cache.getUriFromUid(u.getUid());
                                if(Cache.getImageDate(u.getUid()).equalsIgnoreCase(u.getImage_url())) {
                                    Glide.with(getContext()).load(imgCache).fitCenter().centerCrop().into(personViewHolder.imagePerson);
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
                                                        Glide.with(getContext()).load(img).fitCenter().centerCrop().into(personViewHolder.imagePerson);
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                };
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.setAdapter(adapter);
                adapter.startListening();
            }
        });

        return root;
    }

    private class PersonViewHolder extends RecyclerView.ViewHolder {

        private TextView nomInvit;
        private TextView villePers;
        private LinearLayout layoutProfil;
        private ImageView imagePerson;
        private ImageView buttonImage;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomInvit);
            villePers = itemView.findViewById(R.id.villePers);
            layoutProfil = itemView.findViewById(R.id.layoutProfil);
            imagePerson = itemView.findViewById(R.id.imagePerson);
            buttonImage = itemView.findViewById(R.id.buttonImage);

        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null) {
            adapter.stopListening();
        }
    }

}


