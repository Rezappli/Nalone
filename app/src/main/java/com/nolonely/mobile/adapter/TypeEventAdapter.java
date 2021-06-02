package com.nolonely.mobile.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nolonely.mobile.R;

import java.util.List;

public class TypeEventAdapter extends RecyclerView.Adapter<TypeEventAdapter.TypeEventViewHolder> {
    private List<Drawable> imageTypeEventObjectsList;
    private List<String> nameTypeEventList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onAddClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public static class TypeEventViewHolder extends RecyclerView.ViewHolder {

        public TextView mText;
        public ImageView mImage;
        public CardView mCardView;


        public TypeEventViewHolder(final View itemView, final OnItemClickListener listener) {
            super(itemView);
            mText = itemView.findViewById(R.id.titleTypeEvent);
            mImage = itemView.findViewById(R.id.imageTypeEvent);
            mCardView = itemView.findViewById(R.id.cardTypeEvent);

            mCardView.setOnClickListener(new View.OnClickListener() {
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
    public TypeEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type_event, parent, false);
        TypeEventViewHolder tevh = new TypeEventViewHolder(v, mListener);
        return tevh;
    }

    public TypeEventAdapter(List<String> nameList, List<Drawable> imagesList) {
        this.nameTypeEventList = nameList;
        this.imageTypeEventObjectsList = imagesList;
    }

    @Override
    public void onBindViewHolder(@NonNull TypeEventViewHolder holder, int position) {
        holder.mText.setText(nameTypeEventList.get(position));
        holder.mImage.setImageDrawable(imageTypeEventObjectsList.get(position));
    }

    @Override
    public int getItemCount() {
        return nameTypeEventList.size();
    }

}