package com.example.nalone.ui.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.nalone.R;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class MessagesFragment extends Fragment {

    private MessagesViewModel messagesViewModel;
    private TextView affMessage;
    private Button buttonSend;
    private EditText messageEditText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {






        messagesViewModel =
                ViewModelProviders.of(this).get(MessagesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_messages, container, false);

        //instance
        affMessage = root.findViewById(R.id.affMessage);
        buttonSend = root.findViewById(R.id.buttonSend);


        messagesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {





            }
        });
        return root;
    }




}