package com.nolonely.mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nolonely.mobile.R;
import com.nolonely.mobile.objects.Evenement;
import com.nolonely.mobile.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SearchEvenementAdapter extends RecyclerView.Adapter<SearchEvenementAdapter.EventViewHolder> {

    private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdfTransform = new SimpleDateFormat("dd/MM/yyyy");

    private List<Evenement> evenementList;

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
    }


    public interface OnItemClickListener {
        void onDisplayClick(int position);
        void onParticipateClick(int position);
    }

    public SearchEvenementAdapter(List<Evenement> nearby_events) {
        this.evenementList = nearby_events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evenement_search, parent, false);
        EventViewHolder evh = new EventViewHolder(view,mListener);
        return evh;
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

        public ImageView mImageView;
        public TextView mTitle;
        public TextView mDate;
        public TextView mTime;
        public TextView mVille;
        //public TextView mDescription;
        public CardView cardViewAfficher;
        public  TextView textViewNbMembers;
        public TextView textViewAfficher;
        public TextView textViewParticipate;


        public EventViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageEvent);

            mTitle = itemView.findViewById(R.id.titleEventList1);
            mDate = itemView.findViewById(R.id.dateEventList1);
            mTime = itemView.findViewById(R.id.timeEventList1);
            mVille = itemView.findViewById(R.id.villeEventList);
            cardViewAfficher = itemView.findViewById(R.id.cardViewEvent);
            textViewAfficher = itemView.findViewById(R.id.textViewAfficher);
            textViewParticipate = itemView.findViewById(R.id.textViewParticiper);
            textViewNbMembers = itemView.findViewById(R.id.textViewNbMembers);

            this.cardViewAfficher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDisplayClick(position);
                        }
                    }
                }
            });

            this.textViewParticipate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onParticipateClick(position);
                        }
                    }
                }
            });
        }

        public void updateWhithEvent(final Evenement e) throws ParseException {
            Date d = sdfDate.parse(cutString(e.getStartDate(), 10, -1));
            this.mTitle.setText(e.getName());
            this.mVille.setText(e.getCity());
            this.mDate.setText(Constants.getFullDate(d));
            this.mTime.setText(cutString(e.getStartDate(), 5, 11));
            this.textViewNbMembers.setText(e.getNbMembers()+"");
        }
    }

    private String cutString(String s, int length, int start){
        if(length > s.length()){
            return null;
        }

        String temp = "";

        int i = 0;
        if(start != -1){
            for(i=start; i<length+start; i++){
                temp += s.charAt(i);
            }
        }else{
            for(i=0; i<length; i++){
                temp += s.charAt(i);
            }
        }
        return temp;

    }


}
