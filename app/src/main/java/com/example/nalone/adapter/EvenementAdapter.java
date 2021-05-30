package com.example.nalone.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.R;
import com.example.nalone.objects.Evenement;
import com.example.nalone.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EvenementAdapter extends RecyclerView.Adapter<EvenementAdapter.EventViewHolder> {

    private final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdfTransform = new SimpleDateFormat("dd/MM/yyyy");

    private final List<Evenement> evenementList;
    private int item;

    private OnItemClickListener mListener;
    private boolean participate;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onDisplayClick(int position);
    }

    public EvenementAdapter(List<Evenement> nearby_events, int view, boolean participate) {
        this.evenementList = nearby_events;
        this.participate = participate;
        this.item = view;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(item, parent, false);
        return new EventViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        try {
            holder.updateWhithEvent(this.evenementList.get(position));
            Constants.setEventImage(this.evenementList.get(position), holder.mImageView);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.evenementList.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        private final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        private final SimpleDateFormat sdfTransform = new SimpleDateFormat("dd/MM/yyyy");

        public ImageView mImageView;
        public TextView mTitle;
        public TextView mDate;
        public TextView mTime;
        public TextView mCity;
        //public TextView mDescription;
        public CardView cardViewDisplay, cardViewPrice;
        public TextView textViewPrice;
        public ImageView imageViewCategory;
        private EvenementAdapter.OnItemClickListener mListener;

        public EventViewHolder(@NonNull View itemView, final EvenementAdapter.OnItemClickListener listener) {
            super(itemView);
            mListener = listener;
            mImageView = itemView.findViewById(R.id.imageEvent);

            mTitle = itemView.findViewById(R.id.titleEventList1);
            mDate = itemView.findViewById(R.id.dateEventList1);
            mTime = itemView.findViewById(R.id.timeEventList1);
            mCity = itemView.findViewById(R.id.villeEventList);
            cardViewDisplay = itemView.findViewById(R.id.cardViewEvent);
            imageViewCategory = itemView.findViewById(R.id.imageTypeEvent);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            cardViewPrice = itemView.findViewById(R.id.cardViewPrice);

            cardViewDisplay.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDisplayClick(position);
                    }
                }
            });

            if (item == R.layout.item_evenement_bis) {
                ImageView imageViewDisplay = itemView.findViewById(R.id.showMoreButton);
                imageViewDisplay.setOnClickListener(v -> {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDisplayClick(position);
                        }
                    }
                });
            } else {
                Button buttonDisplay = itemView.findViewById(R.id.buttonDisplay);
                buttonDisplay.setOnClickListener(v -> {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDisplayClick(position);
                        }
                    }
                });
            }
        }

        @SuppressLint("SetTextI18n")
        public void updateWhithEvent(final Evenement e) throws ParseException {
            Date d = sdfDate.parse(cutString(e.getStartDate(), 10, -1));
            this.mTitle.setText(e.getName());
            this.mCity.setText(e.getCity());
            this.mDate.setText(Constants.getFullDate(d));
            this.mTime.setText(cutString(e.getStartDate(), 5, 11));
            if (item != R.layout.item_evenement_bis) {
                if (e.getPrice() != 0) {
                    this.textViewPrice.setText(e.getPrice() + " €");
                    this.cardViewPrice.setBackgroundColor(Color.parseColor("#335CDD"));
                }
            }
            imageViewCategory.setImageResource(e.getImageCategory());
        }

        private String cutString(String s, int length, int start) {
            if (length > s.length()) {
                return null;
            }

            String temp = "";

            int i = 0;
            if (start != -1) {
                for (i = start; i < length + start; i++) {
                    temp += s.charAt(i);
                }
            } else {
                for (i = 0; i < length; i++) {
                    temp += s.charAt(i);
                }
            }
            return temp;

        }
    }
}
