package com.nolonely.mobile.adapter.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nolonely.mobile.R;
import com.nolonely.mobile.objects.User;
import com.nolonely.mobile.util.Constants;

import java.util.List;

public class UserParticipantListAdapter extends RecyclerView.Adapter<UserParticipantListAdapter.UserViewHolder> {


    private List<User> userList;

    public UserParticipantListAdapter(List<User> list) {
        this.userList = list;
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public interface OnItemClickListener {
        void onDisplayClick(int position);

        void onRemoveClick(int position);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_participant, parent, false);
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
        private ImageView buttonAdd;

        public UserViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomAmisInvit);
            villePers = itemView.findViewById(R.id.villeAmisInvit);
            LinearLayout layoutProfil = itemView.findViewById(R.id.layoutInvitAmi);
            imagePerson = itemView.findViewById(R.id.imageInvitAmis);
            buttonAdd = itemView.findViewById(R.id.addInvitAmis);
            ImageView buttonRemove = itemView.findViewById(R.id.removeInvitAmis);

            layoutProfil.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDisplayClick(position);
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
            buttonAdd.setBackgroundResource(u.isValidate() ? R.drawable.ic_baseline_check_circle_24 : R.drawable.ic_baseline_check_circle_outline_24);
            Constants.setUserImage(u, imagePerson);
        }
    }


}
