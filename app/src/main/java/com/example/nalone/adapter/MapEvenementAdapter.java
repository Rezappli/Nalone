package com.example.nalone.adapter;

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

    private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdfTransform = new SimpleDateFormat("dd/MM/yyyy");

    private List<Evenement> evenementList;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evenements_list_, parent, false);
        EventViewHolder evh = new EventViewHolder(view,mListener);
        return evh;
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
        public TextView mProprietaire;
        public TextView textViewAfficher, textViewParticiper;
        public CardView mAfficher;
        public CardView mCarwViewOwner;
        public  TextView textViewNbMembers;

        public EventViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageOwnerEventList);
            mTitle = itemView.findViewById(R.id.titleEventList);
            mDate = itemView.findViewById(R.id.dateEventList);
            mTime = itemView.findViewById(R.id.timeEventList);
            mVille = itemView.findViewById(R.id.villeEventList);
            mProprietaire = itemView.findViewById(R.id.ownerEventList);
            mAfficher = itemView.findViewById(R.id.cardViewEventList);
            textViewAfficher = itemView.findViewById(R.id.textViewAfficher);
            textViewParticiper = itemView.findViewById(R.id.textViewParticiper);
            if(participate){
                textViewParticiper.setText("Désinscrire");
                textViewParticiper.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.desinscrire, //left
                        0, //top
                        0, //right
                        0);//bottom
            }
            mCarwViewOwner = itemView.findViewById(R.id.backGroundOwner);
            textViewNbMembers = itemView.findViewById(R.id.textViewNbMembers);

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

        public void updateWhithEvent(final Evenement e) throws ParseException {
            Date d = sdfDate.parse(cutString(e.getStartDate(), 10, -1));
            this.mTitle.setText(e.getName());
            this.mVille.setText(e.getCity());
            this.mDate.setText(Constants.getFullDate(d));
            this.mTime.setText(cutString(e.getStartDate(), 5, 11));
            this.mProprietaire.setText(e.getOwner_first_name()+" "+e.getOwner_last_name());
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
