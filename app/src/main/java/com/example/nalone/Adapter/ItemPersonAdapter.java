package com.example.nalone.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.ItemPerson;
import com.example.nalone.R;

import java.util.List;

public class ItemPersonAdapter extends RecyclerView.Adapter<ItemPersonAdapter.ItemPersonViewHolder> {
    private List<ItemPerson> mItemPersonList;
    public OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onAddClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
    }


    public static class ItemPersonViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mText;
        public ImageView mImageView2;

        public ItemPersonViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imagePerson);
            mText = itemView.findViewById(R.id.nomInvit);
            mImageView2 = itemView.findViewById(R.id.imageView19);

            mImageView2.setOnClickListener(new View.OnClickListener() {
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
    public ItemPersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        ItemPersonViewHolder ipvh = new ItemPersonViewHolder(v, mListener);
        return ipvh;
    }
    public ItemPersonAdapter(List<ItemPerson> itemlist){
        mItemPersonList = itemlist;
    }
    @Override
    public void onBindViewHolder(@NonNull ItemPersonViewHolder holder, int position) {
        ItemPerson currentItem = mItemPersonList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mText.setText((currentItem.getNom()));
        holder.mImageView2.setImageResource(currentItem.getImageResource2());
    }

    @Override
    public int getItemCount() {
        return mItemPersonList.size();
    }

}