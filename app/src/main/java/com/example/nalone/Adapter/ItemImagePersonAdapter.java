package com.example.nalone.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nalone.items.ItemImagePerson;
import com.example.nalone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import static com.example.nalone.util.Constants.USERS_PICTURE_URI;
import static com.example.nalone.util.Constants.mStore;

public class ItemImagePersonAdapter extends RecyclerView.Adapter<ItemImagePersonAdapter.ItemImagePersonViewHolder> {
    private List<ItemImagePerson> mItemPersonList;
    public OnItemClickListener mListener;
    private Context context;

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
    public ItemImagePersonAdapter(List<ItemImagePerson> itemlist, Context context){
        this.context = context;
        mItemPersonList = itemlist;
    }
    @Override
    public void onBindViewHolder(@NonNull final ItemImagePersonViewHolder holder, int position) {
        final ItemImagePerson currentItem = mItemPersonList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());

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