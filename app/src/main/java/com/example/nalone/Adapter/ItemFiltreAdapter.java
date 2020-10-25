package com.example.nalone.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.ItemFiltre;
import com.example.nalone.ItemPerson;
import com.example.nalone.R;

import java.util.List;

public class ItemFiltreAdapter extends RecyclerView.Adapter<ItemFiltreAdapter.ItemFiltreViewHolder> {
    private List<ItemFiltre> mItemFiltreList;
    public OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onAddClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
    }


    public static class ItemFiltreViewHolder extends RecyclerView.ViewHolder {

        public TextView mText;
        public CardView cardView;


        public ItemFiltreViewHolder(final View itemView, final OnItemClickListener listener) {
            super(itemView);
            mText = itemView.findViewById(R.id.textFiltre);
            cardView = itemView.findViewById(R.id.cardViewFiltre);

            cardView.setOnClickListener(new View.OnClickListener() {
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
    public ItemFiltreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filtre, parent, false);
        ItemFiltreViewHolder ipvh = new ItemFiltreViewHolder(v, mListener);
        return ipvh;
    }
    public ItemFiltreAdapter(List<ItemFiltre> itemlist){
        mItemFiltreList = itemlist;
    }
    @Override
    public void onBindViewHolder(@NonNull ItemFiltreViewHolder holder, int position) {
        ItemFiltre currentItem = mItemFiltreList.get(position);

        holder.mText.setText((currentItem.getFiltre()));
        holder.mText.setBackgroundResource(currentItem.getBackground());
    }

    @Override
    public int getItemCount() {
        return mItemFiltreList.size();
    }

}