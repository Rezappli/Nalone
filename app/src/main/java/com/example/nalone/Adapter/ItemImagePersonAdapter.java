package com.example.nalone.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.ItemImagePerson;
import com.example.nalone.ItemPerson;
import com.example.nalone.R;

import java.util.List;

public class ItemImagePersonAdapter extends RecyclerView.Adapter<ItemImagePersonAdapter.ItemImagePersonViewHolder> {
    private List<ItemImagePerson> mItemPersonList;
    public OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onAddClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
    }


    public static class ItemImagePersonViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public CardView mPerson;

        public ItemImagePersonViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageUser);
            mPerson = itemView.findViewById(R.id.cardViewUser);

            mPerson.setOnClickListener(new View.OnClickListener() {
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

        holder.mImageView.setImageResource(currentItem.getImageResource());
    }

    @Override
    public int getItemCount() {
        return mItemPersonList.size();
    }

}