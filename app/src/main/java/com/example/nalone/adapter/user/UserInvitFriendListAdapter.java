package com.example.nalone.adapter.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.R;
import com.example.nalone.objects.User;
import com.example.nalone.util.Constants;

import java.util.List;

public class UserInvitFriendListAdapter extends RecyclerView.Adapter<UserInvitFriendListAdapter.UserViewHolder> {


    private List<User> userList;

    public UserInvitFriendListAdapter(List<User> list) {
        this.userList = list;
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public interface OnItemClickListener {
        void onDisplayClick(int position);

        void onAddClick(int position);

        void onRemoveClick(int position);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invit_amis, parent, false);
        UserViewHolder uvh = new UserViewHolder(view, mListener);
        return uvh;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.update(this.userList.get(position));
        Constants.setUserImage(this.userList.get(position), holder.imagePerson);
    }

    @Override
    public int getItemCount() {
        return this.userList.size();
    }


    public class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView nomInvit;
        private TextView villePers;
        private ImageView imagePerson;
        private CardView cardViewPhotoPerson;

        public UserViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomAmisInvit);
            villePers = itemView.findViewById(R.id.villeAmisInvit);
            LinearLayout layoutProfil = itemView.findViewById(R.id.layoutInvitAmi);
            imagePerson = itemView.findViewById(R.id.imageInvitAmis);
            ImageView buttonAdd = itemView.findViewById(R.id.addInvitAmis);

            layoutProfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDisplayClick(position);
                        }
                    }
                }
            });

            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onAddClick(position);
                        }
                    }
                }
            });
        }

        public void update(final User u) {
            villePers.setText(u.getCity());
            nomInvit.setText(u.getName());
        }
    }


}
