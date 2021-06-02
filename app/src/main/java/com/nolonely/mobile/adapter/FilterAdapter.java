package com.nolonely.mobile.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nolonely.mobile.R;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ItemFiltreViewHolder> {
    private List<String> filterName;
    private List<Drawable> filterImage;
    public OnItemClickListener mListener;

    private int position;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public static class ItemFiltreViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView imageView;
        public LinearLayout linearLayoutFilter;


        public ItemFiltreViewHolder(final View itemView, final OnItemClickListener listener) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewFilter);
            imageView = itemView.findViewById(R.id.imageViewFilter);
            linearLayoutFilter = itemView.findViewById(R.id.linearLayoutFilter);

            linearLayoutFilter.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ItemFiltreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filtre, parent, false);
        return new ItemFiltreViewHolder(v, mListener);
    }

    public FilterAdapter(List<String> names, List<Drawable> images, int position) {
        this.filterImage = images;
        this.filterName = names;
        this.position = position;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemFiltreViewHolder holder, int position) {
        holder.textView.setText(filterName.get(position));
        if (this.position != -1 && this.position == position) {
            holder.imageView.setColorFilter(Color.parseColor("#00D7F3"), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        holder.imageView.setImageDrawable(filterImage.get(position));
    }

    @Override
    public int getItemCount() {
        return filterName.size();
    }

}