package com.example.nalone.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import static com.example.nalone.util.Constants.USERS_PICTURE_URI;
import static com.example.nalone.util.Constants.mStore;

public class ItemListAmisAdapter extends RecyclerView.Adapter<ItemListAmisAdapter.ItemListAmisViewHolder> {
    private List<ItemPerson> mItemPersonList;
    public OnItemClickListener mListener;
    private Context context;

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
        public CardView cardViewPhotoPerson;

        public ItemListAmisViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageAmi);
            mText = itemView.findViewById(R.id.nomAmi);
            mVille = itemView.findViewById(R.id.villeAmi);
            mPerson = itemView.findViewById(R.id.layoutAmi);
            delete = itemView.findViewById(R.id.imageDelete);
            cardViewPhotoPerson = itemView.findViewById(R.id.cardViewPhotoPerson);

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
    public ItemListAmisAdapter(List<ItemPerson> itemlist, Context context){
        this.context = context;
        mItemPersonList = itemlist;
    }
    @Override
    public void onBindViewHolder(@NonNull final ItemListAmisViewHolder holder, int position) {
        final ItemPerson currentItem = mItemPersonList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mText.setText((currentItem.getNom()));
        holder.mVille.setText(currentItem.getVille());

        if(currentItem.getCursus().equalsIgnoreCase("Informatique")){
            holder.cardViewPhotoPerson.setCardBackgroundColor(Color.RED);
        }

        if(currentItem.getCursus().equalsIgnoreCase("TC")){
            holder.cardViewPhotoPerson.setCardBackgroundColor(Color.GREEN);
        }
        if(currentItem.getCursus().equalsIgnoreCase("MMI")){
            holder.cardViewPhotoPerson.setCardBackgroundColor(Color.parseColor("#4B0082"));
        }
        if(currentItem.getCursus().equalsIgnoreCase("GB")){
            holder.cardViewPhotoPerson.setCardBackgroundColor(Color.BLUE);
        }
        if(currentItem.getCursus().equalsIgnoreCase("LP")){
            holder.cardViewPhotoPerson.setCardBackgroundColor(Color.GRAY);
        }

        if(USERS_PICTURE_URI.get(currentItem.getId()+"") != null){
            Glide.with(context).load(USERS_PICTURE_URI.get(currentItem.getId()+"")).fitCenter().centerCrop().into(holder.mImageView);
        }else {
            StorageReference imgRef = mStore.getReference("users/" + currentItem.getId());
            if (imgRef != null) {
                imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri img = task.getResult();
                            if (img != null) {
                                USERS_PICTURE_URI.put(currentItem.getId()+"", img);
                                Glide.with(context).load(img).fitCenter().centerCrop().into(holder.mImageView);
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mItemPersonList.size();
    }

}