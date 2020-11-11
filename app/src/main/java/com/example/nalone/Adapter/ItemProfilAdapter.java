package com.example.nalone.Adapter;

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

public class ItemProfilAdapter extends RecyclerView.Adapter<ItemProfilAdapter.ItemProfilViewHolder> {
    private List<ItemPerson> mItemPersonList;
    public OnItemClickListener mListener;
    private Context context;

    public interface OnItemClickListener {
        void onAddClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public static class ItemProfilViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mText;
        public ImageView mImageView2;
        public TextView mVille;
        public LinearLayout mPerson;
        public Drawable d;
        public CardView cardViewPhotoPerson;

        public ItemProfilViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imagePerson);
            mText = itemView.findViewById(R.id.nomInvit);
            mVille = itemView.findViewById(R.id.villePers);
            mImageView2 = itemView.findViewById(R.id.imageView19);
            mPerson = itemView.findViewById(R.id.layoutProfil);
            d = itemView.getResources().getDrawable(R.drawable.ic_baseline_add_location_24);
            cardViewPhotoPerson = itemView.findViewById(R.id.cardViewPhotoPerson);

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
    public ItemProfilViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        ItemProfilViewHolder ipvh = new ItemProfilViewHolder(v, mListener);
        return ipvh;
    }

    public ItemProfilAdapter(List<ItemPerson> itemlist, Context context) {
        this.context = context;
        mItemPersonList = itemlist;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemProfilViewHolder holder, int position) {
        final ItemPerson currentItem = mItemPersonList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mText.setText((currentItem.getNom()));
        holder.mImageView2.setImageResource(currentItem.getImageResource2());
        holder.mVille.setText(currentItem.getVille());

        if (currentItem.getCursus().equalsIgnoreCase("Informatique")) {
            holder.cardViewPhotoPerson.setCardBackgroundColor(Color.RED);
        }
        if (currentItem.getCursus().equalsIgnoreCase("TC")) {
            holder.cardViewPhotoPerson.setCardBackgroundColor(Color.GREEN);
        }
        if (currentItem.getCursus().equalsIgnoreCase("MMI")) {
            holder.cardViewPhotoPerson.setCardBackgroundColor(Color.parseColor("#4B0082"));
        }
        if (currentItem.getCursus().equalsIgnoreCase("GB")) {
            holder.cardViewPhotoPerson.setCardBackgroundColor(Color.BLUE);
        }
        if (currentItem.getCursus().equalsIgnoreCase("LP")) {
            holder.cardViewPhotoPerson.setCardBackgroundColor(Color.GRAY);
        }

        getUserData(currentItem.getUid(), new FireStoreUsersListeners() {
            @Override
            public void onDataUpdate(User u) {
                if (u.getImage_url() != null) {
                    StorageReference imgRef = mStore.getReference("users/" + u.getUid());
                    if (imgRef != null) {
                        imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri img = task.getResult();
                                    if (img != null) {
                                        Glide.with(context).load(img).fitCenter().centerCrop().into(holder.mImageView);
                                    }
                                }
                            }
                        });
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