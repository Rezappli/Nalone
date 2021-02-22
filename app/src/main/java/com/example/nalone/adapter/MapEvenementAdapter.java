package com.example.nalone.adapter;

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

import java.text.SimpleDateFormat;
import java.util.List;

public class MapEvenementAdapter extends RecyclerView.Adapter<MapEvenementAdapter.EventViewHolder> {

    private List<Evenement> evenementList;

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
    }


    public interface OnItemClickListener {
        void onDisplayClick(int position);
    }

    public MapEvenementAdapter(List<Evenement> nearby_events) {
        this.evenementList = nearby_events;
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
        holder.updateWhithEvent(this.evenementList.get(position));
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
            //mDescription = itemView.findViewById(R.id.descriptionEventList);
            mProprietaire = itemView.findViewById(R.id.ownerEventList);
            mAfficher = itemView.findViewById(R.id.cardViewEventList);
            textViewAfficher = itemView.findViewById(R.id.textViewAfficher);
            textViewParticiper = itemView.findViewById(R.id.textViewParticiper);
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

        public void updateWhithEvent(final Evenement e){
            /* ---------  -     -  -   -------
                   -      -     -  -   -      -
                   -      -------  -   -------
                   -      -     -  -   -      -
                   -      -     -  -   -------
             */
            /*SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfTime = new SimpleDateFormat("hh : mm");*/
            this.mVille.setText((e.getCity()));
           /* this.mDate.setText(sdfDate.format(e.getStartDate()));
            this.mTime.setText(sdfTime.format(e.getStartDate()));*/
            //holder.mDescription.setText((e.getDescription()));
            this.mProprietaire.setText(e.getOwner_uid());
            this.textViewNbMembers.setText(e.getNbMembers()+"");



        }
    }


}
