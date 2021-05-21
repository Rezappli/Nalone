package com.example.nalone.adapter.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.R;
import com.example.nalone.enumeration.UserList;
import com.example.nalone.objects.User;
import com.example.nalone.util.Constants;

import java.util.List;

import static android.view.View.GONE;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {


    private List<User> userList;
    private UserList currentUserList;

    public UserListAdapter(List<User> list, UserList userList) {
        this.userList = list;
        this.currentUserList = userList;
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

        public UserViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomAmisInvit);
            villePers = itemView.findViewById(R.id.villeAmisInvit);
            LinearLayout layoutProfil = itemView.findViewById(R.id.layoutInvitAmi);
            imagePerson = itemView.findViewById(R.id.imageInvitAmis);
            ImageView buttonAdd = itemView.findViewById(R.id.addInvitAmis);
            ImageView buttonRemove = itemView.findViewById(R.id.removeInvitAmis);

            switch (currentUserList) {
                case INVIT_EVENT:
                    buttonRemove.setVisibility(GONE);
                    break;
                case MEMBERS:
                    buttonAdd.setVisibility(GONE);
                    break;
            }

            layoutProfil.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDisplayClick(position);
                    }
                }
            });

            buttonAdd.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onAddClick(position);
                    }
                }
            });

            buttonRemove.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onRemoveClick(position);
                    }
                }
            });
        }

        public void update(final User u) {
            villePers.setText(u.getCity());
            nomInvit.setText(u.getName());
            Constants.setUserImage(u, imagePerson);
        }
    }


}
