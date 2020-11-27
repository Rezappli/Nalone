package com.example.nalone.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.User;
import com.example.nalone.items.ItemFiltre;
import com.example.nalone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import static com.example.nalone.util.Constants.mStoreBase;

public class EventFiltreAdapter extends RecyclerView.Adapter<EventFiltreAdapter.EventViewHolder> {
    public static List<ItemFiltre> mItemFiltreList;
    public OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onAddClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
    }


    public static class EventViewHolder extends RecyclerView.ViewHolder {

        public CardView mCardView;
        public ImageView mImageView;
        public TextView mTitle;
        public TextView mDate;
        public TextView mTime;
        public TextView mVille;
        public TextView mDescription;
        public TextView mProprietaire;
        public CardView mCarwViewOwner;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardView= itemView.findViewById(R.id.cardViewEvent);
            mImageView = itemView.findViewById(R.id.imageUser1);
            mTitle = itemView.findViewById(R.id.title1);
            mDate = itemView.findViewById(R.id.date1);
            mTime = itemView.findViewById(R.id.time1);
            mVille = itemView.findViewById(R.id.ville1);
            mDescription = itemView.findViewById(R.id.description1);
            mProprietaire = itemView.findViewById(R.id.owner1);
            mCarwViewOwner = itemView.findViewById(R.id.backGroundOwner);

        }
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evenement, parent, false);
        EventViewHolder ipvh = new EventViewHolder(v, mListener);
        return ipvh;
    }

    public void EventViewHolder(List<ItemFiltre> itemlist){
        mItemFiltreList = itemlist;
    }
    @Override
    public void onBindViewHolder(@NonNull final EventViewHolder holder, int position) {
        ItemFiltre e = mItemFiltreList.get(position);

        holder.mImageView.setImageResource(e.getImage());
        holder.mTitle.setText((e.getName()));
        holder.mDate.setText((e.getDate().toDate().toString()));
        holder.mVille.setText((e.getCity()));
        holder.mDescription.setText((e.getDescription()));
        holder.mProprietaire.setText(e.getOwner());

        mStoreBase.collection("users").whereEqualTo("uid", e.getOwnerDoc().getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User u = document.toObject(User.class);
                                if(u.getCursus().equalsIgnoreCase("Informatique")){
                                    holder.mCarwViewOwner.setCardBackgroundColor(Color.RED);
                                }

                                if(u.getCursus().equalsIgnoreCase("TC")){
                                    holder.mCarwViewOwner.setCardBackgroundColor(Color.parseColor("#00E9FD"));
                                }

                                if(u.getCursus().equalsIgnoreCase("MMI")){
                                    holder.mCarwViewOwner.setCardBackgroundColor(Color.parseColor("#FF1EED"));
                                }

                                if(u.getCursus().equalsIgnoreCase("GB")){
                                    holder.mCarwViewOwner.setCardBackgroundColor(Color.parseColor("#41EC57"));
                                }

                                if(u.getCursus().equalsIgnoreCase("LP")){
                                    holder.mCarwViewOwner.setCardBackgroundColor((Color.parseColor("#EC9538")));
                                }

                            }

                        }
                    }});


        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

               /* if(e.getImage_url() != null) {
                    if(!Cache.fileExists(e.getUid())) {
                        StorageReference imgRef = mStore.getReference("groups/" + g.getUid());
                        if (imgRef != null) {
                            imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri img = task.getResult();
                                        if (img != null) {
                                            Cache.saveUriFile(g.getUid(), img);
                                            g.setImage_url(Cache.getImageDate(g.getUid()));
                                            mStoreBase.collection("groups").document(g.getUid()).set(g);
                                            Glide.with(getContext()).load(img).fitCenter().centerCrop().into(userViewHolder.imageGroup);
                                        }
                                    }
                                }
                            });
                        }
                    }else{
                        Uri imgCache = Cache.getUriFromUid(g.getUid());
                        if(Cache.getImageDate(g.getUid()).equalsIgnoreCase(g.getImage_url())) {
                            Glide.with(getContext()).load(imgCache).fitCenter().centerCrop().into(userViewHolder.imageGroup);
                        }else{
                            StorageReference imgRef = mStore.getReference("groups/" + g.getUid());
                            if (imgRef != null) {
                                imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            Uri img = task.getResult();
                                            if (img != null) {
                                                Cache.saveUriFile(g.getUid(), img);
                                                g.setImage_url(Cache.getImageDate(g.getUid()));
                                                mStoreBase.collection("groups").document(g.getUid()).set(g);
                                                Glide.with(getContext()).load(img).fitCenter().centerCrop().into(userViewHolder.imageGroup);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
                */


    }

    @Override
    public int getItemCount() {
        return mItemFiltreList.size();
    }

}