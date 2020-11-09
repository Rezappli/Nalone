package com.example.nalone.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

public class ItemAddPersonAdapter extends RecyclerView.Adapter<ItemAddPersonAdapter.ItemAddPersonViewHolder> {
    private List<ItemPerson> mItemPersonList;
    public OnItemClickListener mListener;
    private Context context;

    public interface OnItemClickListener {
        void onRemoveClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
    }


    public static class ItemAddPersonViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mText;
        public ImageView mImageView2;

        public ItemAddPersonViewHolder(View itemView, final OnItemClickListener listener) {
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
                            listener.onRemoveClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ItemAddPersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        ItemAddPersonViewHolder ipvh = new ItemAddPersonViewHolder(v, mListener);
        return ipvh;
    }
    public ItemAddPersonAdapter(List<ItemPerson> itemlist, Context context){
        this.context = context;
        mItemPersonList = itemlist;
    }
    @Override
    public void onBindViewHolder(@NonNull final ItemAddPersonViewHolder holder, int position) {
        final ItemPerson currentItem = mItemPersonList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mText.setText((currentItem.getNom()));
        holder.mImageView2.setImageResource(currentItem.getImageResource2());

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