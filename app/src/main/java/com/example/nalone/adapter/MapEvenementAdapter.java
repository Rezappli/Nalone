package com.example.nalone.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.R;
import com.example.nalone.objects.Evenement;
import com.example.nalone.ui.evenements.InfosEvenementsActivity;
import com.example.nalone.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MapEvenementAdapter extends RecyclerView.Adapter<MapEvenementAdapter.EventViewHolder> {

    private final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdfTransform = new SimpleDateFormat("dd/MM/yyyy");

    private final List<Evenement> evenementList;

    private OnItemClickListener mListener;
    private boolean participate;

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
    }


    public interface OnItemClickListener {
        void onDisplayClick(int position);
    }

    public MapEvenementAdapter(List<Evenement> nearby_events, boolean participate) {
        this.evenementList = nearby_events;
        this.participate = participate;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evenement, parent, false);
        return new EventViewHolder(view,mListener);
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

        public ImageView mImageView;
        public TextView mTitle;
        public TextView mDate;
        public TextView mTime;
        public TextView mVille;
        //public TextView mDescription;
        public CardView textViewAfficher,  cardViewPrice;
        public  TextView textViewNbMembers,textViewPrice;
        public ImageView imageViewCategory;

        public EventViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageEvent);

            mTitle = itemView.findViewById(R.id.titleEventList1);
            mDate = itemView.findViewById(R.id.dateEventList1);
            mTime = itemView.findViewById(R.id.timeEventList1);
            mVille = itemView.findViewById(R.id.villeEventList);
            textViewAfficher = itemView.findViewById(R.id.cardViewEvent);
            textViewNbMembers = itemView.findViewById(R.id.textViewNbMembers);
            imageViewCategory = itemView.findViewById(R.id.imageTypeEvent);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            cardViewPrice = itemView.findViewById(R.id.cardViewPrice);


            this.textViewAfficher.setOnClickListener(new View.OnClickListener() {
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
        }

        @SuppressLint("SetTextI18n")
        public void updateWhithEvent(final Evenement e) throws ParseException {
            Date d = sdfDate.parse(cutString(e.getStartDate(), 10, -1));
            this.mTitle.setText(e.getName());
            this.mVille.setText(e.getCity());
            this.mDate.setText(Constants.getFullDate(d));
            this.mTime.setText(cutString(e.getStartDate(), 5, 11));
            this.textViewNbMembers.setText(e.getNbMembers()+"");
            if(e.getPrice() != 0){
                this.textViewPrice.setText(e.getPrice()+" €");
                this.cardViewPrice.setBackgroundColor(Color.parseColor("#335CDD"));
            }

            switch (e.getCategory()){
                case ART:this.imageViewCategory.setImageResource(R.drawable.event_art);
                    break;
                case CAR:this.imageViewCategory.setImageResource(R.drawable.event_car);
                    break;
                case GAME:this.imageViewCategory.setImageResource(R.drawable.event_game);
                    break;
                case SHOP:this.imageViewCategory.setImageResource(R.drawable.event_shop);
                    break;
                case SHOW:this.imageViewCategory.setImageResource(R.drawable.event_show);
                    break;
                case MOVIE:this.imageViewCategory.setImageResource(R.drawable.event_movie);
                    break;
                case MUSIC:this.imageViewCategory.setImageResource(R.drawable.event_music);
                    break;
                case PARTY:this.imageViewCategory.setImageResource(R.drawable.event_party);
                    break;
                case SPORT:this.imageViewCategory.setImageResource(R.drawable.event_sport);
                    break;
                case CONTEST:this.imageViewCategory.setImageResource(R.drawable.event_contest);
                    break;
                case SCIENCE:this.imageViewCategory.setImageResource(R.drawable.event_science);
                    break;
                case CONFERENCE:this.imageViewCategory.setImageResource(R.drawable.event_conference);
                    break;
                case GATHER:this.imageViewCategory.setImageResource(R.drawable.event_gather);
            }
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
