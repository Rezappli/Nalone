package com.example.nalone.ui.evenements;

import com.example.nalone.Evenement;

import java.util.List;

public interface OnDataReceiveCallback {
    void onDataReceived(List<Evenement> listEvenements);
}
