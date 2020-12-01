package com.example.nalone.ui.message;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.nalone.Chat;
import com.example.nalone.Message;
import com.example.nalone.R;
import com.example.nalone.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.UUID;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.mStoreBase;

public class ChatActivity extends AppCompatActivity {

    private ImageView buttonSend;
    private TextInputEditText messageEditText;
    private ImageView profile_view;
    private TextView username;
    public static User USER_LOAD;
    private TextView nameUser;
    private DocumentReference chatRef;
    private FirestorePagingAdapter adapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private LinearLayout.LayoutParams myLayoutMessages;
    private LinearLayout.LayoutParams otherLayoutMessages;

    private int limit = 2;

    private int countAdapter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);

        mSwipeRefreshLayout = findViewById(R.id.messageSwipeRefreshLayout);
        buttonSend = findViewById(R.id.buttonSend);
        messageEditText = findViewById(R.id.messageEditText);
        nameUser = findViewById(R.id.nameUser);
        nameUser.setText(USER_LOAD.getFirst_name() + " " + USER_LOAD.getLast_name());
        mRecyclerView = findViewById(R.id.messagesRecyclerView);
        mRecyclerView.scrollToPosition(ScrollView.FOCUS_DOWN);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.smoothScrollToPosition(countAdapter);
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
                    Log.w("Message", "Message channel : " + c.getChatRef().toString());
                    chatRef = c.getChatRef();
                }

                if(chatRef != null) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    adapterMessages();
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.retry();
            }
        });

    }

    private void sendMessage(Message msg) {
        if(msg != null || msg.getSender() != null || msg.getMessage() != null || msg.getTime() != null) {
            mStoreBase.collection("chat").document(chatRef.getId()).collection("messages").document(UUID.randomUUID().toString()).set(msg);
            messageEditText.setText("");
            adapterMessages();
        }
    }

    private void adapterMessages() {

        Query query = mStoreBase.collection("chat").document(chatRef.getId()).collection("messages").orderBy("time").limit(limit);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(2)
                .setPageSize(limit)
                .build();

        FirestorePagingOptions<Message> options = new FirestorePagingOptions.Builder<Message>().setQuery(query, config, Message.class).build();

        adapter = new FirestorePagingAdapter<Message, MessageViewHolder>(options) {
            @NonNull
            @Override
            public ChatActivity.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
                return new ChatActivity.MessageViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i, @NonNull Message m) {
                if(m.getSender().equals(USER_REFERENCE)){
                    messageViewHolder.messageLayout.setLayoutParams(myLayoutMessages);
                    messageViewHolder.backgroundItem.setCardBackgroundColor(Color.parseColor("#18ECC5"));
                }else{
                    messageViewHolder.messageLayout.setLayoutParams(otherLayoutMessages);
                    messageViewHolder.backgroundItem.setCardBackgroundColor(Color.LTGRAY);
                }

                messageViewHolder.messageText.setText(m.getMessage());
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        mSwipeRefreshLayout.setRefreshing(true);
                        break;

                    case LOADED:
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        Toast.makeText(
                                getApplicationContext(),
                                "Error Occurred!",
                                Toast.LENGTH_SHORT
                        ).show();

                        mSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case FINISHED:
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }



        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        mRecyclerView.setAdapter(adapter);
        adapter.startListening();
        mRecyclerView.scrollToPosition(adapter.getItemCount()-1);


    }



    private class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView nomInvit;
        private TextView villePers;
        private LinearLayout layoutProfil;
        private ImageView imagePerson;
        private ImageView button;
        private LinearLayout messageLayout;
        private TextView messageText;
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
            backgroundItem = itemView.findViewById(R.id.bgMessage);
        }
    }


}
