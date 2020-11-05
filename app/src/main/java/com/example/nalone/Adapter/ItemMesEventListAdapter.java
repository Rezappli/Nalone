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

import java.util.List;

import static com.example.nalone.util.Constants.USERS_LIST;

public class ItemMesEventListAdapter extends RecyclerView.Adapter<ItemMesEventListAdapter.ItemEventViewHolder> {
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
            mImageView = itemView.findViewById(R.id.imageOwnerEventList);
            mTitle = itemView.findViewById(R.id.titleEventList);
            mDate = itemView.findViewById(R.id.dateEventList);
            mTime = itemView.findViewById(R.id.timeEventList);
            mVille = itemView.findViewById(R.id.villeEventList);
            mDescription = itemView.findViewById(R.id.descriptionEventList);
            mProprietaire = itemView.findViewById(R.id.ownerEventList);

        }
    }

    @NonNull
    @Override
    public ItemEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mes_evenements_list, parent, false);
        ItemEventViewHolder ipvh = new ItemEventViewHolder(v, mListener);
        return ipvh;
    }
    public ItemMesEventListAdapter(List<Evenement> itemlist){
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