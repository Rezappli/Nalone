package com.nolonely.mobile.adapter.messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nolonely.mobile.R;
import com.nolonely.mobile.objects.Chat;

import java.util.List;

public class MessageUserAdapter extends RecyclerView.Adapter<MessageUserAdapter.MessageUserViewHolder> {

    private List<Chat> messageList;

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public interface OnItemClickListener {
        void onDisplayClick(int position);
    }

    public MessageUserAdapter(List<Chat> list) {
        this.messageList = list;
    }

    @NonNull
    @Override
    public MessageUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_message, parent, false);
        MessageUserViewHolder evh = new MessageUserViewHolder(view, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageUserViewHolder holder, int position) {
        holder.update(this.messageList.get(position));

    }

    @Override
    public int getItemCount() {
        return this.messageList.size();
    }


    public class MessageUserViewHolder extends RecyclerView.ViewHolder {

        private TextView lastMessage, dateText, ownerText;
        private ImageView ownerImage;

        public MessageUserViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);

            lastMessage = itemView.findViewById(R.id.lastMessage);
            ownerText = itemView.findViewById(R.id.ownerChat);
            ownerImage = itemView.findViewById(R.id.ownerImage);
            dateText = itemView.findViewById(R.id.dateText);

        }

        public void update(final Chat c) {
            //messageText.setText(m.get);
            // lastMessage.setText();
            //
            //
            // THIBOULE LE GROS BG A TOI DE JOUER
            //
            //
        }
    }


}
