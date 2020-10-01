package com.example.nalone;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemPersonAdapter extends RecyclerView.Adapter<ItemPersonAdapter.ItemPersonViewHolder> {


    public static class ItemPersonViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mText;
        public ImageView mImageView2;

        public ItemPersonViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.im)
        }
    }

    @NonNull
    @Override
    public ItemPersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemPersonViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}

