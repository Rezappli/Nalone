package com.example.nalone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.Evenement;
import com.example.nalone.R;

import java.util.List;


public class ItemEventListAdapter extends RecyclerView.Adapter<ItemEventListAdapter.ItemEventViewHolder> {
    private List<Evenement> mItemEventList;
    public OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onDisplayClick(int position);
        void onSignInClick(int position);
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
        public Button mInscrire, mAfficher;


        public ItemEventViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageOwnerEventList);
            mTitle = itemView.findViewById(R.id.titleEventList);
            mDate = itemView.findViewById(R.id.dateEventList);
            mTime = itemView.findViewById(R.id.timeEventList);
            mVille = itemView.findViewById(R.id.villeEventList);
            mDescription = itemView.findViewById(R.id.descriptionEventList);
            mProprietaire = itemView.findViewById(R.id.ownerEventList);
            mAfficher = itemView.findViewById(R.id.buttonAfficherEventList);
            mInscrire = itemView.findViewById(R.id.buttonInscrirEventList);

            mAfficher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDisplayClick(position);
                        }
                    }
                }
            });

            mInscrire.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onSignInClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ItemEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evenements_list, parent, false);
        ItemEventViewHolder ipvh = new ItemEventViewHolder(v, mListener);
        return ipvh;
    }
    public ItemEventListAdapter(List<Evenement> itemlist){
        mItemEventList = itemlist;
    }
    @Override
    public void onBindViewHolder(@NonNull ItemEventViewHolder holder, int position) {
        Evenement currentItem = mItemEventList.get(position);

        holder.mImageView.setImageResource(currentItem.getImage());
        holder.mTitle.setText((currentItem.getName()));
        holder.mDate.setText((currentItem.toString()));
        holder.mTime.setText((currentItem.getDate().toString()));
        holder.mVille.setText((currentItem.getCity()));
        holder.mDescription.setText((currentItem.getDescription()));
        holder.mProprietaire.setText(currentItem.getOwner());

    }

    @Override
    public int getItemCount() {
        return mItemEventList.size();
    }

}