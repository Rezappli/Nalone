package com.example.nalone.ui.message;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nalone.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    private Button buttonSend;
    private TextInputEditText messageEditText;
    private ImageView profile_view;
    private TextView username ;

    public DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);



       //affMessage = (TextView) findViewById(R.id.affMessage);
        buttonSend = (Button) findViewById(R.id.buttonSend);
        messageEditText = (TextInputEditText) findViewById(R.id.messageEditText);


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

        reference.child("0").push().setValue(hashMap);


    }




}