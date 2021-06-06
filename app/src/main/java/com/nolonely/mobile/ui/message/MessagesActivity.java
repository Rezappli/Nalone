package com.nolonely.mobile.ui.message;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nolonely.mobile.R;
import com.nolonely.mobile.bdd.json.JSONObjectCrypt;

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
    private List<String> uid;


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
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("uid", USER.getUid());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        createFragment();
    }
}