package com.example.nalone.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.User;
import com.example.nalone.R;
import com.example.nalone.UserFriendData;
import com.example.nalone.listeners.FireStoreUsersListeners;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.FirebaseFirestore;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.getUserData;


public class MessagesFragment extends Fragment {



    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mRecyclerView;
    private  FirestoreRecyclerAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_messages, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mRecyclerView = root.findViewById(R.id.recycler);

        //query
        Query query = firebaseFirestore.collection("users").document(USER.getUid()).collection("friends");

        //RecyclerOption
        FirestoreRecyclerOptions<UserFriendData> options = new FirestoreRecyclerOptions.Builder<UserFriendData>().setQuery(query, UserFriendData.class).build();

         adapter = new FirestoreRecyclerAdapter<UserFriendData, PersonViewHolder>(options) {
            @NonNull
            @Override
            public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person,parent,false);
                return new PersonViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final PersonViewHolder personViewHolder, int i, @NonNull UserFriendData data) {
                getUserData(data.getUser().getId(), new FireStoreUsersListeners() {
                    @Override
                    public void onDataUpdate(final User u) {
                        personViewHolder.villePers.setText(u.getCity());
                        personViewHolder.nomInvit.setText(u.getFirst_name() + " "+ u.getLast_name());

                        personViewHolder.layoutProfil.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChatActivity.USER_LOAD = u;
                                startActivity(new Intent(getContext(),ChatActivity.class));
                            }
                        });
                    }
                });


            }
        };
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
        return root;
    }

    private class PersonViewHolder extends RecyclerView.ViewHolder {

        private TextView nomInvit;
        private TextView villePers;
        private LinearLayout layoutProfil;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomInvit);
            villePers = itemView.findViewById(R.id.villePers);
            layoutProfil = itemView.findViewById(R.id.layoutProfil);

        }

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening(); 
    }

    /*search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                tempList.clear();
                boolean check = true;
                if(items.size() > 0) {
                    if (newText.length() > 0) {
                        for (int i = 0; i < items.size(); i++) {
                            for (int j = 0; j < newText.length(); j++) {
                                if(items.get(i).getmNomToLowerCase().length() > j) {
                                    if (newText.charAt(j) == items.get(i).getmNomToLowerCase().charAt(j) && j == 0){
                                        check = true;
                                    }


                                    if (newText.charAt(j) == items.get(i).getmNomToLowerCase().charAt(j) && check) {
                                        check = true;
                                    } else {
                                        check = false;
                                    }


                                    if (check) {
                                        if (!tempList.contains(items.get(i))) {
                                            tempList.add(items.get(i));
                                            if (resultat.getVisibility() == View.VISIBLE) {
                                                resultat.setVisibility(View.GONE);
                                                resultat.setText("");
                                            }
                                        }
                                    } else {
                                        tempList.remove(items.get(i));
                                    }
                                }else{
                                    tempList.remove(items.get(i));
                                }
                            }
                        }


                        if (tempList.size() == 0) {
                            resultat.setVisibility(View.VISIBLE);
                            resultat.setText(R.string.aucun_resultat);
                        }

                        mAdapter = new ItemMessageAdapter(tempList);
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        resultat.setVisibility(View.GONE);
                        resultat.setText("");
                        mAdapter = new ItemMessageAdapter(items);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }else{
                    resultat.setVisibility(View.VISIBLE);
                    resultat.setText("Aucun amis à ajouter !");
                }

                return false;
            }
        });

        return root;
    }


    public void loadData(final View root) {

    }

*/


      /*  public void OnChatClick () {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            Log.d(TAG, "onChatClick: clicked");
            startActivity(intent);
        }
*/


}


