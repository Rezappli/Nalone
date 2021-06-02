package com.nolonely.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nolonely.mobile.R;
import com.nolonely.mobile.enumeration.Visibility;
import com.nolonely.mobile.objects.Group;
import com.nolonely.mobile.util.Constants;

import java.util.List;

public class RechercheGroupeAdapter extends RecyclerView.Adapter<RechercheGroupeAdapter.GroupViewHolder> {

    private List<Group> groupList;
    private Context context;

    public RechercheGroupeAdapter(List<Group> list, Context mContext) {
        this.groupList = list;
        this.context = mContext;
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
    }


    public interface OnItemClickListener {
        void onDisplayClick(int position);
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_groupe, parent, false);
        GroupViewHolder gvh = new GroupViewHolder(view, mListener);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        holder.update(this.groupList.get(position));
        Constants.setGroupImage(this.groupList.get(position), holder.imageGroup);
    }

    @Override
    public int getItemCount() {
        return this.groupList.size();
    }



    public class GroupViewHolder extends RecyclerView.ViewHolder {

        private TextView nomGroup, visibilityGroup;
        private LinearLayout layoutGroup;
        private ImageView imageGroup;
        private ImageView button;

        public GroupViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);

            nomGroup = itemView.findViewById(R.id.nomGroupe);
            layoutGroup = itemView.findViewById(R.id.layoutGroupe);
            imageGroup = itemView.findViewById(R.id.imageGroupe);
            visibilityGroup = itemView.findViewById(R.id.visibiliteGroup);
            layoutGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDisplayClick(position);
                        }
                    }
                }
            });

        }

        public void update(final Group g){
            nomGroup.setText(g.getName());
            if(g.getVisibility() == Visibility.PRIVATE){
                visibilityGroup.setText(context.getResources().getString(R.string.location_private));
                visibilityGroup.setCompoundDrawables(context.getResources().getDrawable(R.drawable.ic_baseline_lock_15_grey),null,null,null);
            }else{
                visibilityGroup.setText(context.getResources().getString(R.string.location_public));
                visibilityGroup.setCompoundDrawables(context.getResources().getDrawable(R.drawable.ic_baseline_public_15),null,null,null);

            }

        }

    }


}
