package com.example.nalone.ui.message;


import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.nalone.Chat;
import com.example.nalone.Message;
import com.example.nalone.R;
import com.example.nalone.User;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.MissingResourceException;
import java.util.UUID;

import static com.example.nalone.util.Constants.ID_NOTIFICATION_MESSAGES;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.allTimeFormat;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.sendNotification;
import static com.example.nalone.util.Constants.ON_MESSAGE_ACTIVITY;

public class ChatActivity extends AppCompatActivity {

    private ImageView buttonSend;
    private TextInputEditText messageEditText;
    private ImageView profile_view;
    private TextView username;
    public static User USER_LOAD;
    private TextView nameUser;
    private DocumentReference chatRef;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView mRecyclerView;

    private LinearLayout.LayoutParams myLayoutMessages;
    private LinearLayout.LayoutParams otherLayoutMessages;
    private CardView newMessagePopUp;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private int limit = 10;

    private Message lastMessage = null;

    private String NOTIFICATION_TITLE;
    private String NOTIFICATION_MESSAGE;
    private String TOPIC;
    private final String TAG = "NOTIFICATION TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);

        ON_MESSAGE_ACTIVITY = true;
        mSwipeRefreshLayout = findViewById(R.id.messageSwipeRefreshLayout);
        newMessagePopUp = findViewById(R.id.newMessagePopUp);
        buttonSend = findViewById(R.id.buttonSend);
        messageEditText = findViewById(R.id.messageEditText);
        nameUser = findViewById(R.id.nameUser);
        nameUser.setText(USER_LOAD.getFirst_name() + " " + USER_LOAD.getLast_name());
        mRecyclerView = findViewById(R.id.messagesRecyclerView);

        messageEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageEditText.getText().length() > 0) {
                    sendMessage(new Message(USER_REFERENCE, messageEditText.getText().toString()));
                }
            }
        });

        myLayoutMessages = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myLayoutMessages.setMargins(80, 1, 10, 1);

        otherLayoutMessages = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        otherLayoutMessages.setMargins(10, 1, 80, 1);

        mStoreBase.collection("users").document(USER.getUid()).collection("chat_friends").whereEqualTo("uid", USER_LOAD.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot doc : task.getResult()) {
                    Chat c = doc.toObject(Chat.class);
                    chatRef = c.getChatRef();
                }

                if(chatRef != null) {
                    adapterMessages();
                    mRecyclerView.getLayoutManager().scrollToPosition(0);
                }
            }
        });



        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                limit += 10;
                adapterMessages();
                mRecyclerView.scrollToPosition(mRecyclerView.getAdapter().getItemCount());
            }
        });

        newMessagePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMessagePopUp.setVisibility(View.GONE);
                mRecyclerView.scrollToPosition(0);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLastVisible()){
                    if(newMessagePopUp.getVisibility() == View.VISIBLE){
                        newMessagePopUp.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void sendMessage(Message msg) {
        if(msg != null || msg.getSender() != null || msg.getMessage() != null || msg.getTime() != null) {
            mStoreBase.collection("chat").document(chatRef.getId()).collection("messages").document(UUID.randomUUID().toString()).set(msg);
            messageEditText.setText("");
            sendMessageNotification(msg.getMessage());
        }
    }

    private void adapterMessages() {
        Query query = mStoreBase.collection("chat").document(chatRef.getId()).collection("messages").limit(limit).orderBy("time", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>().setQuery(query, Message.class).build();

        adapter = new FirestoreRecyclerAdapter<Message, ChatActivity.MessageViewHolder>(options) {
            @NonNull
            @Override
            public ChatActivity.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
                return new ChatActivity.MessageViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final MessageViewHolder messageViewHolder, int i, @NonNull Message m) {
                if(m.getSender().equals(USER_REFERENCE)){
                    messageViewHolder.messageLayout.setLayoutParams(myLayoutMessages);
                    messageViewHolder.backgroundItem.setCardBackgroundColor(Color.parseColor("#18ECC5"));
                }else{
                    messageViewHolder.messageLayout.setLayoutParams(otherLayoutMessages);
                    messageViewHolder.backgroundItem.setCardBackgroundColor(Color.LTGRAY);
                }
                messageViewHolder.messageText.setText(m.getMessage());
                messageViewHolder.dateText.setText(allTimeFormat.format(m.getTime().toDate()));

                messageViewHolder.messageLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(messageViewHolder.dateText.getVisibility() == View.GONE) {
                            messageViewHolder.dateText.setVisibility(View.VISIBLE);
                        }else{
                            messageViewHolder.dateText.setVisibility(View.GONE);
                        }
                    }
                });
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this, RecyclerView.VERTICAL, true));
        mRecyclerView.setAdapter(adapter);
        adapter.startListening();

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                boolean newMessage = false;
                super.onItemRangeInserted(positionStart, itemCount);

                if(positionStart == 0) {
                    try {
                        Message m = (Message) adapter.getItem(0);
                        if (lastMessage == null) {
                            if (!m.getSender().equals(USER_REFERENCE)) {
                                newMessage = true;
                            }
                        } else {
                            if (!lastMessage.equals(m)) {
                                if (!m.getSender().equals(USER_REFERENCE)) {
                                    newMessage = true;
                                }
                            }
                        }
                        if (newMessage) {
                            lastMessage = m;
                            if (!isLastVisible()) {
                                newMessagePopUp.setVisibility(View.VISIBLE);
                            } else {
                                mRecyclerView.scrollToPosition(0);
                            }
                        } else {
                            mRecyclerView.scrollToPosition(0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        clearNotifications();
    }

    private class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView nomInvit;
        private TextView villePers;
        private LinearLayout layoutProfil;
        private ImageView imagePerson;
        private ImageView button;
        private LinearLayout messageLayout;
        private TextView messageText;
        private TextView dateText;
        private CardView backgroundItem;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomInvit);
            villePers = itemView.findViewById(R.id.villePers);
            layoutProfil = itemView.findViewById(R.id.layoutProfil);
            imagePerson = itemView.findViewById(R.id.imagePerson);
            button = itemView.findViewById(R.id.buttonImage);
            messageLayout = itemView.findViewById(R.id.messageLayout);
            messageText = itemView.findViewById(R.id.messageText);
            dateText = itemView.findViewById(R.id.dateText);
            backgroundItem = itemView.findViewById(R.id.bgMessage);
        }
    }

    private boolean isLastVisible() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager)mRecyclerView.getLayoutManager());
        int pos = layoutManager.findFirstCompletelyVisibleItemPosition();
        int numItems = mRecyclerView.getAdapter().getItemCount();
        return (pos == 0);
    }

    public void sendMessageNotification(String msg){
        TOPIC = "/topics/"+ USER_LOAD.getUid(); //topic must match with what the receiver subscribed to
        Log.w("TOPIC", "Topic : " + TOPIC);
        NOTIFICATION_TITLE = "Message de " + USER.getFirst_name() + " " + USER.getLast_name();
        NOTIFICATION_MESSAGE = msg;

        JSONObject notification = new JSONObject();
        JSONObject notificationBody = new JSONObject();
        try {
            notificationBody.put("title", NOTIFICATION_TITLE);
            notificationBody.put("message", NOTIFICATION_MESSAGE);
            notificationBody.put("sender", USER.getUid());
            notificationBody.put("type", "message");

            notification.put("to", TOPIC);
            notification.put("data", notificationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage());
        }
        sendNotification(notification, ChatActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ON_MESSAGE_ACTIVITY = false;
    }

    private void clearNotifications(){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(USER_LOAD.getUid(), ID_NOTIFICATION_MESSAGES);
    }

}