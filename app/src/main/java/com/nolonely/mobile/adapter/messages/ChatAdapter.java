package com.nolonely.mobile.adapter.messages;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nolonely.mobile.R;
import com.nolonely.mobile.objects.Message;

import java.util.List;

import static com.nolonely.mobile.util.Constants.USER;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<Message> messageList;

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public interface OnItemClickListener {
        void onDisplayClick(int position);
    }

    public ChatAdapter(List<Message> list) {
        this.messageList = list;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        ChatViewHolder evh = new ChatViewHolder(view, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.update(this.messageList.get(position));

    }

    @Override
    public int getItemCount() {
        return this.messageList.size();
    }


    public class ChatViewHolder extends RecyclerView.ViewHolder {

        private TextView messageText;
        private ImageView ownerImage;
        private CardView bgMessage;

        public ChatViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);

            messageText = itemView.findViewById(R.id.messageText);
            ownerImage = itemView.findViewById(R.id.ownerImage);
            bgMessage = itemView.findViewById(R.id.bgMessage);

        }

        public void update(final Message m) {
            if (m.getSender().equals(USER.getUid())) {
                bgMessage.setCardBackgroundColor(Color.parseColor("#BFECF3"));
            } else {
                bgMessage.setCardBackgroundColor(Color.WHITE);
            }
            messageText.setText(m.getMessage());
        }
    }


}
