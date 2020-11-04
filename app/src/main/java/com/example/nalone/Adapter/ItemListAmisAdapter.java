package com.example.nalone.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.items.ItemPerson;
import com.example.nalone.R;

import java.util.List;

public class ItemListAmisAdapter extends RecyclerView.Adapter<ItemListAmisAdapter.ItemListAmisViewHolder> {
    private List<ItemPerson> mItemPersonList;
    public OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onAddClick(int position);
        void onDelete(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
    }


    public static class ItemListAmisViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mText;
        public ImageView delete;
        public TextView mVille;
        public LinearLayout mPerson;

        public ItemListAmisViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageAmi);
            mText = itemView.findViewById(R.id.nomAmi);
            mVille = itemView.findViewById(R.id.villeAmi);
            mPerson = itemView.findViewById(R.id.layoutAmi);
            delete = itemView.findViewById(R.id.imageDelete);

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

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDelete(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ItemListAmisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_amis, parent, false);
        ItemListAmisViewHolder ipvh = new ItemListAmisViewHolder(v, mListener);
        return ipvh;
    }
    public ItemListAmisAdapter(List<ItemPerson> itemlist){
        mItemPersonList = itemlist;
    }
    @Override
    public void onBindViewHolder(@NonNull ItemListAmisViewHolder holder, int position) {
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