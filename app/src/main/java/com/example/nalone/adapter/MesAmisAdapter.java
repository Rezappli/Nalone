package com.example.nalone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.example.nalone.R;
import com.example.nalone.objects.User;

import java.util.List;

public class MesAmisAdapter extends RecyclerView.Adapter<MesAmisAdapter.UserViewHolder> {


    private List<User> userList;
    private boolean swipe;

    public MesAmisAdapter(List<User> list) {
        this.userList = list;
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
    }


    public interface OnItemClickListener {
        void onDisplayClick(int position);
        void onDeleteClick(int position);
        void onCallClick(int position);
        void onMessageClick(int position);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_layout, parent, false);
        return new UserViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.update(this.userList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.userList.size();
    }



    public class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView nomInvit;
        private TextView villePers;
        private LinearLayout layoutProfil;
        private ImageView imagePerson;
        private ImageView button;
        private CardView cardViewPhotoPerson;
        public SwipeLayout swipeLayout;
        public ImageView Delete;
        public ImageView Appel;
        public ImageView Share;
        public ImageButton btnLocation;

        public UserViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomInvit);
            villePers = itemView.findViewById(R.id.villePers);
            layoutProfil = itemView.findViewById(R.id.layoutProfil);
            imagePerson = itemView.findViewById(R.id.imagePerson);
            button = itemView.findViewById(R.id.buttonImage);
            cardViewPhotoPerson = itemView.findViewById(R.id.cardViewPhotoPerson);
            //
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            Delete = (ImageView) itemView.findViewById(R.id.Delete);
            Appel = (ImageView) itemView.findViewById(R.id.Appel);
            Share = (ImageView) itemView.findViewById(R.id.Share);
            btnLocation = (ImageButton) itemView.findViewById(R.id.btnLocation);
            //userViewHolder.button.setImageResource(R.drawable.ic_baseline_delete_24);

            swipeLayout.addDrag(SwipeLayout.DragEdge.Left, swipeLayout.findViewById(R.id.bottom_wrapper1));

            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, swipeLayout.findViewById(R.id.bottom_wraper));
            swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onStartOpen(SwipeLayout layout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    button.setImageDrawable(button.getResources().getDrawable(R.drawable.arrow_back_white));
                    swipe = true;
                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onClose(SwipeLayout layout) {
                    button.setImageDrawable(button.getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_right_24));

                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

                }
            });

            swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getContext(), " Click : " , Toast.LENGTH_SHORT).show();
                }
            });

            btnLocation.setOnClickListener(new View.OnClickListener() {
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

            Share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onMessageClick(position);
                        }
                    }

                }
            });

            Appel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onCallClick(position);
                        }
                    }


                }
            });

            Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }

                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(swipe == false) {
                        swipeLayout.open();
                        swipe = true;
                    }
                    else{
                        swipeLayout.close();
                        swipe = false;
                    }
                }
            });

        }


        public void update(final User u){
            villePers.setText(u.getCity());
            nomInvit.setText(u.getFirst_name() + " "+ u.getLast_name());
        }
    }


}
