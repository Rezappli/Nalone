package com.example.nalone.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nalone.Cache;
import com.example.nalone.User;
import com.example.nalone.items.ItemImagePerson;
import com.example.nalone.R;
import com.example.nalone.listeners.FireStoreUsersListeners;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.List;


import static com.example.nalone.util.Constants.getUserData;
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

        getUserData(currentItem.getUid(), new FireStoreUsersListeners() {
            @Override
            public void onDataUpdate(User u) {
                if (u.getImage_url() != null) {
                    if(!Cache.fileExists(currentItem.getUid())) {
                        StorageReference imgRef = mStore.getReference("users/" + u.getUid());
                        if (imgRef != null) {
                            imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri img = task.getResult();
                                        if (img != null) {
                                            Log.w("image", "save image from cache");
                                            Cache.saveUriFile(currentItem.getUid(), img);
                                            Glide.with(context).load(img).fitCenter().centerCrop().into(holder.mImageView);
                                        }
                                    }
                                }
                            });
                        }
                    }else{
                        Log.w("image", "get image from cache");
                        Glide.with(context).load(Cache.getUriFromUid(currentItem.getUid())).fitCenter().centerCrop().into(holder.mImageView);
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemPersonList.size();
    }

}