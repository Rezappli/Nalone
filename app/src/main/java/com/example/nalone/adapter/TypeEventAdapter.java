package com.example.nalone.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.R;
import com.example.nalone.items.ItemFiltre;
import com.example.nalone.objects.TypeEventObject;

import java.util.List;

public class TypeEventAdapter extends RecyclerView.Adapter<TypeEventAdapter.TypeEventViewHolder> {
    private List<TypeEventObject> typeEventObjectsList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onAddClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
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
    public TypeEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type_event, parent, false);
        TypeEventViewHolder tevh = new TypeEventViewHolder(v, mListener);
        return tevh;
    }

    public TypeEventAdapter(List<TypeEventObject> objectList){
        this.typeEventObjectsList = objectList;
    }

    @Override
    public void onBindViewHolder(@NonNull TypeEventViewHolder holder, int position) {
        holder.mText.setText(typeEventObjectsList.get(position).getmName());
        holder.mImage.setImageDrawable(typeEventObjectsList.get(position).getmDrawable());
    }

    @Override
    public int getItemCount() {
        return typeEventObjectsList.size();
    }

}