package com.nolonely.mobile.ui.message;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.nolonely.mobile.R;
import com.nolonely.mobile.objects.User;

import static com.nolonely.mobile.util.Constants.ON_MESSAGE_ACTIVITY;

public class ChatActivityFriend extends AppCompatActivity {

    private ImageView buttonSend;
    private TextInputEditText messageEditText;
    private ImageView profile_view;
    private User USER_LOAD;
    private TextView nameUser;
    private RecyclerView mRecyclerView;

    private LinearLayout.LayoutParams myLayoutMessages;
    private LinearLayout.LayoutParams otherLayoutMessages;
    private CardView newMessagePopUp;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    public static boolean nouveau = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);

        if (getIntent() != null) {
            USER_LOAD = (User) getIntent().getSerializableExtra("user");
        }

        ON_MESSAGE_ACTIVITY = true;
        mSwipeRefreshLayout = findViewById(R.id.messageSwipeRefreshLayout);
        newMessagePopUp = findViewById(R.id.newMessagePopUp);
        buttonSend = findViewById(R.id.buttonSend);
        messageEditText = findViewById(R.id.messageEditText);
        nameUser = findViewById(R.id.nameUser);
        nameUser.setText(USER_LOAD.getName());
        mRecyclerView = findViewById(R.id.messagesRecyclerView);
        profile_view = findViewById(R.id.profile_view);
        CardView card_view = findViewById(R.id.card_view);


    }
}