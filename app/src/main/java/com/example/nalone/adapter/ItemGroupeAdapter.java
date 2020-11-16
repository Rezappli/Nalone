package com.example.nalone.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
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
import com.example.nalone.Cache;
import com.example.nalone.R;
import com.example.nalone.User;
import com.example.nalone.items.ItemGroupe;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.listeners.FireStoreUsersListeners;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import static com.example.nalone.util.Constants.getUserData;
import static com.example.nalone.util.Constants.mStore;

public class ItemGroupeAdapter extends RecyclerView.Adapter<ItemGroupeAdapter.ItemGroupeViewHolder> {
    private List<ItemGroupe> mItemGroupeList;
    public OnItemClickListener mListener;
    private Context context;

    public interface OnItemClickListener {
        void onAddClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public static class ItemGroupeViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mText;
        public LinearLayout mPerson;


        public ItemGroupeViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageGroupe);
            mText = itemView.findViewById(R.id.nomGroupe);
            mPerson = itemView.findViewById(R.id.layoutGroupe);

            mPerson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onAddClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ItemGroupeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_groupe, parent, false);
        ItemGroupeViewHolder ipvh = new ItemGroupeViewHolder(v, mListener);
        return ipvh;
    }

    public ItemGroupeAdapter(List<ItemGroupe> itemlist, Context context) {
        this.context = context;
        mItemGroupeList = itemlist;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemGroupeViewHolder holder, int position) {
        final ItemGroupe currentItem = mItemGroupeList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mText.setText((currentItem.getNom()));


        /*getUserData(currentItem.getUid(), new FireStoreUsersListeners() {
            @Override
            public void onDataUpdate(final User u) {
                if (u.getImage_url() != null) {
                    if(!Cache.fileExists(u.getUid())) {
                        StorageReference imgRef = mStore.getReference("users/" + u.getUid());
                        if (imgRef != null) {
                            imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri img = task.getResult();
                                        if (img != null) {
                                            Log.w("image", "save image from cache");
                                            Cache.saveUriFile(u.getUid(), img);
                                            Glide.with(context).load(img).fitCenter().centerCrop().into(holder.mImageView);
                                        }
                                    }
                                }
                            });
                        }
                    }else{
                        Log.w("image", "get image from cache");
                        Glide.with(context).load(Cache.getUriFromUid(u.getUid())).fitCenter().centerCrop().into(holder.mImageView);
                    }
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mItemGroupeList.size();
    }

}