package com.nolonely.mobile.ui.message;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.nolonely.mobile.R;
import com.nolonely.mobile.adapter.messages.ChatAdapter;
import com.nolonely.mobile.bdd.json.JSONController;
import com.nolonely.mobile.bdd.json.JSONObjectCrypt;
import com.nolonely.mobile.listeners.JSONArrayListener;
import com.nolonely.mobile.listeners.JSONObjectListener;
import com.nolonely.mobile.objects.Chat;
import com.nolonely.mobile.objects.Message;
import com.nolonely.mobile.objects.User;
import com.nolonely.mobile.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.nolonely.mobile.util.Constants.ON_MESSAGE_ACTIVITY;
import static com.nolonely.mobile.util.Constants.USER;

public class ChatActivityFriend extends AppCompatActivity {

    private ImageView buttonSend;
    private TextInputEditText messageEditText;
    private ImageView chatUserImageView;
    private TextView nameUser;
    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private List<Message> messages;
    private ImageView chatImageBack;
    private Chat chatChannel = null;
    private CardView newMessagePopUp;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean b = false;
    private User userLoad;
    public int limit = 15;
    private boolean newChat = false;
    private MessageThread messageThread = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);

        if (getIntent() != null) {
            newChat = (Boolean) getIntent().getSerializableExtra("newChat");
            if (newChat) {
                userLoad = (User) getIntent().getSerializableExtra("userLoad");
                Log.w("Chat", " User load : " + userLoad.toString());
            } else {
                chatChannel = (Chat) getIntent().getSerializableExtra("chatChannel");
            }
        }

        ON_MESSAGE_ACTIVITY = true;
        chatImageBack = findViewById(R.id.chatImageBack);
        mSwipeRefreshLayout = findViewById(R.id.messageSwipeRefreshLayout);
        chatUserImageView = findViewById(R.id.chatUserImageView);
        newMessagePopUp = findViewById(R.id.newMessagePopUp);
        buttonSend = findViewById(R.id.buttonSend);
        messageEditText = findViewById(R.id.messageEditText);
        mRecyclerView = findViewById(R.id.messagesRecyclerView);
        nameUser = findViewById(R.id.nameUser);

        chatImageBack.setOnClickListener(v -> finish());
        buttonSend.setOnClickListener(v -> listenChannel(messageEditText.getText().toString()));

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            limit += 10;
            createActivity();
            mSwipeRefreshLayout.setRefreshing(false);
        });

        if (chatChannel != null) {
            messageThread = new MessageThread(this);
            messageThread.start();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createActivity() {
        if (chatChannel != null) {
            nameUser.setText(chatChannel.getName());
            Constants.setUserImageWithUrl(chatChannel.getImage(), chatUserImageView);
            updateMessages(limit);
        } else {
            nameUser.setText(userLoad.getName());
            Constants.setUserImageWithUrl(userLoad.getImage_url(), chatUserImageView);
        }

        if (messageThread != null) {
            if (messageThread.getState() == Thread.State.NEW) {
                messageThread.start();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void listenChannel(String message) {
        if (newChat) {
            createMessageChannel(message);
        } else {
            sendMessage(message);
        }

        messageEditText.setText("");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createMessageChannel(String message) {
        String uidChannel = UUID.randomUUID().toString();
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("uid_2", userLoad.getUid());
        params.putCryptParameter("uid_channel", uidChannel);

        JSONController.getJsonObjectFromUrl(Constants.URl_CREATE_MESSAGE_CHANNEL, this, params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                if (jsonObject.length() == 3) {
                    chatChannel = new Chat(uidChannel, Constants.formatDayHoursMinutesSeconds.format(new Date(System.currentTimeMillis())), USER.getName(), message);
                    newChat = false;
                    messages = new ArrayList<>();
                    sendMessage(message);
                    messageThread = new MessageThread(ChatActivityFriend.this);
                    messageThread.start();
                } else {
                    Toast.makeText(ChatActivityFriend.this, getResources().getString(R.string.error_channel_already_exist), Toast.LENGTH_SHORT).show();
                    Log.w("Chat", "Response : " + jsonObject.toString());
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(ChatActivityFriend.this, getResources().getString(R.string.error_creating_channel), Toast.LENGTH_SHORT).show();
                Log.w("Chat", "Erreur : " + volleyError.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendMessage(String message) {
        if (chatChannel != null) {
            JSONObjectCrypt params = new JSONObjectCrypt();
            params.putCryptParameter("uid", USER.getUid());
            params.putCryptParameter("uid_channel", chatChannel.getUidChannel());
            params.putCryptParameter("message", message);

            JSONController.getJsonObjectFromUrl(Constants.URl_SEND_MESSAGES, ChatActivityFriend.this, params, new JSONObjectListener() {
                @Override
                public void onJSONReceived(JSONObject jsonObject) {
                    if (jsonObject.length() == 3) {
                        Log.w("Chat", "Message have been sent");
                        updateMessages(limit);
                    }
                }

                @Override
                public void onJSONReceivedError(VolleyError volleyError) {
                    Toast.makeText(ChatActivityFriend.this, getResources().getString(R.string.error_send_message), Toast.LENGTH_SHORT).show();
                    Log.w("Chat", "Erreur : " + volleyError.toString());
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateMessages(int limite) {
        messages = new ArrayList<>();
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("uid_channel", chatChannel.getUidChannel());
        params.putCryptParameter("limit", limite);

        Log.w("Chat", "Params:" + params.toString());

        JSONController.getJsonArrayFromUrl(Constants.URl_GET_MESSAGES, this, params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) throws JSONException {
                for (int i = 0; i < jsonArray.length(); i++) {
                    Log.w("Chat", "Received : " + jsonArray.toString());
                    messages.add((Message) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Message.class));
                }

                if (messages.size() > 0) {
                    setupRecyclerViewWithList(messages);
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(ChatActivityFriend.this, getResources().getString(R.string.error_get_messages), Toast.LENGTH_SHORT).show();
                Log.w("Chat", "Response:" + volleyError.toString());
            }
        });
    }

    private void setupRecyclerViewWithList(List<Message> list) {
        mAdapter = new ChatAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager lm = new LinearLayoutManager(ChatActivityFriend.this);
        lm.setReverseLayout(true);
        mRecyclerView.setLayoutManager(lm);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        createActivity();
    }

    @Override
    public void onStop() {
        super.onStop();
        messageThread.interrupt();
        messageThread = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (messageThread != null) {
            messageThread.interrupt();
        }
    }
}