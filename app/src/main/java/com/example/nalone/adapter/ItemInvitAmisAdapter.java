package com.example.nalone.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nalone.Cache;
import com.example.nalone.User;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.R;
import com.example.nalone.listeners.FireStoreUsersListeners;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import static com.example.nalone.util.Constants.getUserData;
import static com.example.nalone.util.Constants.mStore;

public class ItemInvitAmisAdapter extends RecyclerView.Adapter<ItemInvitAmisAdapter.ItemInvitViewHolder> {
    private List<ItemPerson> mItemPersonList;
    public OnItemClickListener mListener;
    private Context context;

    public interface OnItemClickListener {
        void onAddClick(int position);
        void onRemoveClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
    }


    public static class ItemInvitViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mText;
        public ImageView mImageViewAdd;
        public ImageView mImageViewRemove;
        public TextView mVille;
        public CardView cardViewPhotoPerson;

        public ItemInvitViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageInvitAmis);
            mText = itemView.findViewById(R.id.nomAmisInvit);
            mImageViewAdd = itemView.findViewById(R.id.addInvitAmis);
            mImageViewRemove = itemView.findViewById(R.id.removeInvitAmis);
            mVille = itemView.findViewById(R.id.villeAmisInvit);
            cardViewPhotoPerson = itemView.findViewById(R.id.cardViewPhotoPerson);

            mImageViewAdd.setOnClickListener(new View.OnClickListener() {
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

            mImageViewRemove.setOnClickListener(new View.OnClickListener() {
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
    public ItemInvitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invit_amis, parent, false);
        ItemInvitViewHolder ipvh = new ItemInvitViewHolder(v, mListener);
        return ipvh;
    }
    public ItemInvitAmisAdapter(List<ItemPerson> itemlist, Context context){
        this.context = context;
        mItemPersonList = itemlist;
    }
    @Override
    public void onBindViewHolder(@NonNull final ItemInvitViewHolder holder, int position) {
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


        getUserData(currentItem.getUid(), new FireStoreUsersListeners() {
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
        });
    }

    @Override
    public int getItemCount() {
        return mItemPersonList.size();
    }

}