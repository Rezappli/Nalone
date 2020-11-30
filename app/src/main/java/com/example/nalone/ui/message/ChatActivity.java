package com.example.nalone.ui.message;


import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nalone.Cache;
import com.example.nalone.R;
import com.example.nalone.User;
import com.example.nalone.ui.message.display.MessagesAmisFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;

public class ChatActivity extends AppCompatActivity {

    private ImageView buttonSend;
    private TextInputEditText messageEditText;
    private ImageView profile_view;
    private TextView username;
    public static User USER_LOAD;
    public DatabaseReference databaseReference;
    private TextView nameUser;
    private Document currentChat;
    private DocumentReference refChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);

        buttonSend = (ImageView) findViewById(R.id.buttonSend);
        messageEditText = (TextInputEditText) findViewById(R.id.messageEditText);
        nameUser = findViewById(R.id.nameUser);
        nameUser.setText(USER_LOAD.getFirst_name() + " " + USER_LOAD.getLast_name());

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageEditText.getText().length()>0){
                    sendMessage("hugo","thibaulk",messageEditText.getText().toString());
                }
            }
        });
    }

    private void sendMessage(String sender , String recever , String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chat");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("recever",recever);
        hashMap.put("message",message);

        reference.child("").push().setValue(hashMap);


    }

    private void displayMessage (String sender, String recever, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chat");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.get(sender);
        hashMap.get(recever);
        hashMap.get(message);

        reference.child("0").push().setValue(hashMap);

    }




}
