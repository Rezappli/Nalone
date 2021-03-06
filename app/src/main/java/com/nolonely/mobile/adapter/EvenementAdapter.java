package com.nolonely.mobile.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.nolonely.mobile.R;
import com.nolonely.mobile.enumeration.StatusEvent;
import com.nolonely.mobile.objects.Evenement;
import com.nolonely.mobile.util.Constants;

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
    private Context context;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onDisplayClick(int position);
    }

    public EvenementAdapter(List<Evenement> nearby_events, int view, boolean participate, Context context) {
        this.evenementList = nearby_events;
        this.participate = participate;
        this.item = view;
        this.context = context;
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
        public ConstraintLayout linearEvent;
        public CardView cardViewPrice;
        public TextView textViewPrice, textViewNbMembers, textViewOwnerName;
        public ImageView imageViewCategory;
        public ImageView ivQRCode;
        private final EvenementAdapter.OnItemClickListener mListener;

        public EventViewHolder(@NonNull View itemView, final EvenementAdapter.OnItemClickListener listener) {
            super(itemView);
            mListener = listener;
            mImageView = itemView.findViewById(R.id.imageEvent);

            mTitle = itemView.findViewById(R.id.titleEventList1);
            mDate = itemView.findViewById(R.id.dateEventList1);
            mTime = itemView.findViewById(R.id.timeEventList1);
            mCity = itemView.findViewById(R.id.villeEventList);
            linearEvent = itemView.findViewById(R.id.linearEvent);
            imageViewCategory = itemView.findViewById(R.id.imageTypeEvent);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            cardViewPrice = itemView.findViewById(R.id.cardViewPrice);
            textViewNbMembers = itemView.findViewById(R.id.textViewNbMembers);

            linearEvent.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDisplayClick(position);
                    }
                }
            });


            if (item != R.layout.item_evenement) {
                ImageView imageViewDisplay = itemView.findViewById(R.id.imageViewAfficher);
                imageViewDisplay.setOnClickListener(v -> {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDisplayClick(position);
                        }
                    }
                });
            } else {
                textViewOwnerName = itemView.findViewById(R.id.textViewNameOwner);
                ConstraintLayout linearEvent = itemView.findViewById(R.id.linearEvent);
                linearEvent.setOnClickListener(v -> {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDisplayClick(position);
                        }
                    }
                });
            }

            if (item == R.layout.item_evenement_registration || item == R.layout.item_evenement_creation) {
                ivQRCode = itemView.findViewById(R.id.imageQRCode);
            }
        }

        @SuppressLint("SetTextI18n")
        public void updateWhithEvent(final Evenement e) throws ParseException {
            Date d = sdfDate.parse(cutString(e.getStartDate(), 10, -1));
            this.mTitle.setText(e.getName());
            this.mCity.setText(e.getCity());
            this.mDate.setText(Constants.getFullDate(d));
            this.mTime.setText(cutString(e.getStartDate(), 5, 11));
            if (item == R.layout.item_evenement) {
                textViewOwnerName.setText(e.getOwnerName());

                if (e.getPrice() != 0) {
                    this.textViewPrice.setText(e.getPrice() + " €");
                    this.cardViewPrice.setBackgroundColor(Color.parseColor("#0C6FE6"));
                } else {
                    this.textViewPrice.setText("Free");
                    this.cardViewPrice.setBackgroundColor(Color.parseColor("#00D7F3"));

                }
            }

            if (item == R.layout.item_evenement_registration || item == R.layout.item_evenement_creation) {
                if (e.getStatusEvent() == StatusEvent.FINI) {
                    ivQRCode.setVisibility(View.GONE);
                } else {
                    ivQRCode.setVisibility(View.VISIBLE);
                }
            }

            textViewNbMembers.setText(e.getNbMembers() + "");
            imageViewCategory.setImageResource(e.getImageCategory());
            Constants.setEventImage(e, mImageView);


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
