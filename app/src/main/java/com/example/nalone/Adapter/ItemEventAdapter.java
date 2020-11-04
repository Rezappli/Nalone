package com.example.nalone.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.Evenement;
import com.example.nalone.R;
import com.example.nalone.User;

import static com.example.nalone.util.Constants.USERS_LIST;

import java.util.List;

public class ItemEventAdapter extends RecyclerView.Adapter<ItemEventAdapter.ItemEventViewHolder> {
    private List<Evenement> mItemEventList;
    public OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onAddClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
    }


    public static class ItemEventViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTitle;
        public TextView mDate;
        public TextView mTime;
        public TextView mVille;
        public TextView mDescription;
        public TextView mProprietaire;


        public ItemEventViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageUser1);
            mTitle = itemView.findViewById(R.id.title1);
            mDate = itemView.findViewById(R.id.date1);
            mTime = itemView.findViewById(R.id.time1);
            mVille = itemView.findViewById(R.id.ville1);
            mDescription = itemView.findViewById(R.id.description1);
            mProprietaire = itemView.findViewById(R.id.owner1);


            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onAddClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ItemEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evenement, parent, false);
        ItemEventViewHolder ipvh = new ItemEventViewHolder(v, mListener);
        return ipvh;
    }
    public ItemEventAdapter(List<Evenement> itemlist){
        mItemEventList = itemlist;
    }
    @Override
    public void onBindViewHolder(@NonNull ItemEventViewHolder holder, int position) {
        Evenement currentItem = mItemEventList.get(position);

        holder.mImageView.setImageResource(currentItem.getImage());
        holder.mTitle.setText((currentItem.getNom()));
        holder.mDate.setText((currentItem.toString()));
        holder.mTime.setText((currentItem.getTime()));
        holder.mVille.setText((currentItem.getVille()));
        holder.mDescription.setText((currentItem.getDescription()));
        holder.mProprietaire.setText((USERS_LIST.get(currentItem.getProprietaire()).getPrenom()+" " + USERS_LIST.get(currentItem.getProprietaire()).getNom()));

    }

    @Override
    public int getItemCount() {
        return mItemEventList.size();
    }

}