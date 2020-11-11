package com.example.nalone.listeners;

import com.example.nalone.Evenement;
import com.example.nalone.User;

public interface FireStoreUsersListeners {
    void onDataUpdate(User u);
}
