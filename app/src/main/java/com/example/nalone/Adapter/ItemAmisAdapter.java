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

public class ItemAmisAdapter extends RecyclerView.Adapter<ItemAmisAdapter.ItemAmisViewHolder> {
    private List<ItemPerson> mItemPersonList;
    public OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onAddClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
    }


    public static class ItemAmisViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mText;
        public ImageView mImageView2;

        public ItemAmisViewHolder(View itemView, final OnItemClickListener listener) {
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
    public ItemAmisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        ItemAmisViewHolder ipvh = new ItemAmisViewHolder(v, mListener);
        return ipvh;
    }
    public ItemAmisAdapter(List<ItemPerson> itemlist){
        mItemPersonList = itemlist;
    }
    @Override
    public void onBindViewHolder(@NonNull ItemAmisViewHolder holder, int position) {
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