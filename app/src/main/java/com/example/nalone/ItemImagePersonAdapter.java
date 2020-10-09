package com.example.nalone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemImagePersonAdapter extends RecyclerView.Adapter<ItemImagePersonAdapter.ItemImagePersonViewHolder> {
    private List<ItemImagePerson> mItemPersonList;
    public OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onRemoveClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
    }


    public static class ItemImagePersonViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;

        public ItemImagePersonViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageUser);

        }
    }

    @NonNull
    @Override
    public ItemImagePersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_user, parent, false);
        ItemImagePersonViewHolder ipvh = new ItemImagePersonViewHolder(v, mListener);
        return ipvh;
    }
    public ItemImagePersonAdapter(List<ItemImagePerson> itemlist){
        mItemPersonList = itemlist;
    }
    @Override
    public void onBindViewHolder(@NonNull ItemImagePersonViewHolder holder, int position) {
        ItemImagePerson currentItem = mItemPersonList.get(position);

        holder.mImageView.setImageResource(currentItem.getmImageResource());

    }

    @Override
    public int getItemCount() {
        return mItemPersonList.size();
    }

}