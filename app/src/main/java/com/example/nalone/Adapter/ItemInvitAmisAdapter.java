package com.example.nalone.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.items.ItemPerson;
import com.example.nalone.R;

import java.util.List;

public class ItemInvitAmisAdapter extends RecyclerView.Adapter<ItemInvitAmisAdapter.ItemInvitViewHolder> {
    private List<ItemPerson> mItemPersonList;
    public OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onAddClick(int position);
        void onRemoveClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
    }


    public static class ItemInvitViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mText;
        public ImageView mImageViewAdd;
        public ImageView mImageViewRemove;
        public TextView mVille;

        public ItemInvitViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageInvitAmis);
            mText = itemView.findViewById(R.id.nomAmisInvit);
            mImageViewAdd = itemView.findViewById(R.id.addInvitAmis);
            mImageViewRemove = itemView.findViewById(R.id.removeInvitAmis);
            mVille = itemView.findViewById(R.id.villeAmisInvit);

            mImageViewAdd.setOnClickListener(new View.OnClickListener() {
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

            mImageViewRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onRemoveClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ItemInvitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invit_amis, parent, false);
        ItemInvitViewHolder ipvh = new ItemInvitViewHolder(v, mListener);
        return ipvh;
    }
    public ItemInvitAmisAdapter(List<ItemPerson> itemlist){
        mItemPersonList = itemlist;
    }
    @Override
    public void onBindViewHolder(@NonNull ItemInvitViewHolder holder, int position) {
        ItemPerson currentItem = mItemPersonList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mText.setText((currentItem.getNom()));
        holder.mVille.setText(currentItem.getVille());
    }

    @Override
    public int getItemCount() {
        return mItemPersonList.size();
    }

}