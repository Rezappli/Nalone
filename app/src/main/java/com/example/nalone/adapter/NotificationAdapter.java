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
import com.example.nalone.objects.Evenement;
import com.example.nalone.objects.Notification;
import com.example.nalone.util.Horloge;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotifViewHolder> {

    private List<Notification> notificationList;

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
    }


    public interface OnItemClickListener {
        void onDisplayClick(int position);
    }

    public NotificationAdapter(List<Notification> list) {
        this.notificationList = list;
    }

    @NonNull
    @Override
    public NotifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notif, parent, false);
        NotifViewHolder evh = new NotifViewHolder(view, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull NotifViewHolder holder, int position) {
        holder.update(this.notificationList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.notificationList.size();
    }


    public class NotifViewHolder extends RecyclerView.ViewHolder {

        private TextView nomOwner;
        private TextView descNotif, dateNotif;
        private LinearLayout layoutProfil;
        private ImageView imagePerson;
        private ImageView button;

        public NotifViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);

            nomOwner = itemView.findViewById(R.id.nomOwner);
            descNotif = itemView.findViewById(R.id.descNotif);
            layoutProfil = itemView.findViewById(R.id.layoutProfil);
            imagePerson = itemView.findViewById(R.id.imagePerson);
            button = itemView.findViewById(R.id.buttonImage);
            dateNotif = itemView.findViewById(R.id.dateNotif);

        }

        public void update(final Notification n){
            descNotif.setText(n.getMessage());
            dateNotif.setText(Horloge.verifDay(n.getDate()));

        }
    }


}
