package com.example.nalone.listeners;

import com.example.nalone.Evenement;
import com.example.nalone.User;

import java.io.UnsupportedEncodingException;

public interface FireStoreUsersListeners {
    void onDataUpdate(User u);
}
