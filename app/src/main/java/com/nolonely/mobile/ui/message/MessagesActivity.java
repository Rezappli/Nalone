package com.nolonely.mobile.ui.message;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.nolonely.mobile.R;
import com.nolonely.mobile.adapter.messages.MessageUserAdapter;
import com.nolonely.mobile.bdd.json.JSONController;
import com.nolonely.mobile.bdd.json.JSONObjectCrypt;
import com.nolonely.mobile.listeners.JSONArrayListener;
import com.nolonely.mobile.objects.Chat;
import com.nolonely.mobile.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import static com.nolonely.mobile.util.Constants.USER;

public class MessagesActivity extends AppCompatActivity {


    private SearchView search_bar;

    private NavController navController;
    private RecyclerView mRecyclerView;
    private String search = "";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView addMessage;
    private List<Chat> chatList;


    //bio
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        search_bar = findViewById(R.id.search_bar_amis);
        mRecyclerView = findViewById(R.id.recyclerViewMessagesAmis);
        mSwipeRefreshLayout = findViewById(R.id.messageFriendSwipeRefreshLayout);
        addMessage = findViewById(R.id.imageViewAddChat);
        addMessage.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), NewMessageActivity.class)));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onRefresh() {
                createFragment();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createFragment() {
        getDiscussions();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getDiscussions() {
        chatList = new ArrayList<>();
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("limit", "15");
        Log.w("Chat", "Params : " + params.toString());

        JSONController.getJsonArrayFromUrl(Constants.URL_GET_DISCUSSIONS, MessagesActivity.this, params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) throws JSONException {
                Log.w("Chat", "Discussions : " + jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    chatList.add((Chat) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Chat.class));
                }

                mRecyclerView.setAdapter(new MessageUserAdapter(chatList));
                mRecyclerView.setLayoutManager(new LinearLayoutManager(MessagesActivity.this));
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(MessagesActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        createFragment();
    }
}