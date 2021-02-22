package com.example.nalone.adapter;

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

import java.util.List;

public class RechercheAmisAdapter extends RecyclerView.Adapter<RechercheAmisAdapter.UserViewHolder> {


    private List<User> userList;

    public RechercheAmisAdapter(List<User> list) {
        this.userList = list;
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
    }


    public interface OnItemClickListener {
        void onDisplayClick(int position);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        UserViewHolder uvh = new UserViewHolder(view, mListener);
        return uvh;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.update(this.userList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.userList.size();
    }



    public class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView nomInvit;
        private TextView villePers;
        private LinearLayout layoutProfil;
        private ImageView imagePerson;
        private ImageView button;
        private CardView cardViewPhotoPerson;

        public UserViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomInvit);
            villePers = itemView.findViewById(R.id.villePers);
            layoutProfil = itemView.findViewById(R.id.layoutProfil);
            imagePerson = itemView.findViewById(R.id.imagePerson);
            button = itemView.findViewById(R.id.buttonImage);
            cardViewPhotoPerson = itemView.findViewById(R.id.cardViewPhotoPerson);
            layoutProfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDisplayClick(position);
                        }
                    }
                }
            });
        }

        public void update(final User u){
            villePers.setText(u.getCity());
            nomInvit.setText(u.getFirst_name() + " "+ u.getLast_name());
            button.setImageResource(0);
        }
    }


}