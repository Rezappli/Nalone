package com.example.nalone.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.R;
import com.example.nalone.User;
import com.example.nalone.UserFriendData;
import com.example.nalone.listeners.FireStoreUsersListeners;
import com.example.nalone.ui.message.ChatActivity;
import com.example.nalone.ui.message.MessagesFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.nalone.util.Constants.getUserData;

public class FirestoreRecyclerAdapterUserFriendData extends FirestoreRecyclerAdapter<UserFriendData, FirestoreRecyclerAdapterUserFriendData.PersonViewHolder> {

    public static class PersonViewHolder extends RecyclerView.ViewHolder{
        public TextView nomInvit;
        public TextView villePers;
        public LinearLayout layoutProfil;

        public PersonViewHolder(@NonNull View itemView, final FirestoreRecyclerAdapterUserFriendData.OnItemClickListener listener) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomInvit);
            villePers = itemView.findViewById(R.id.villePers);
            layoutProfil = itemView.findViewById(R.id.layoutProfil);

            layoutProfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onClickItem(position);
                        }
                    }
                }
            });

        }
    }

    private Context context;
    private OnItemClickListener mListener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FirestoreRecyclerAdapterUserFriendData(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }


    public interface OnItemClickListener {
        void onClickItem(int position);
    }

    public void setOnItemClickListener(FirestoreRecyclerAdapterUserFriendData.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        PersonViewHolder ipvh = new PersonViewHolder(v, mListener);
        return ipvh;
    }

    @Override
    protected void onBindViewHolder(@NonNull final PersonViewHolder personViewHolder, int i, @NonNull UserFriendData data) {
        Log.w("DOc", "Chargement de : " + data.getUser().getId());
        getUserData(data.getUser().getId(), new FireStoreUsersListeners() {
            @Override
            public void onDataUpdate(User u) {
                personViewHolder.villePers.setText(u.getCity());
                personViewHolder.nomInvit.setText(u.getFirst_name() + " " + u.getLast_name());

            }
        });
    }
}





