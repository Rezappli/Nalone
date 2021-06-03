package com.nolonely.mobile;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

public abstract class NoLonelyFragment extends Fragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected Handler handler;

    protected boolean haveInternetConnection() {
        // Fonction haveInternetConnection : return true si connecté, return false dans le cas contraire
        NetworkInfo network = ((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (network == null || !network.isConnected()) {
            // Le périphérique n'est pas connecté à Internet
            return false;
        }
        // Le périphérique est connecté à Internet
        return true;
    }

    protected void checkInternetConnection() {
        handler = new Handler();
        handler.postDelayed(runnable, 0);
    }

    private final Runnable runnable = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run() {
            if (!haveInternetConnection()) {
                handler.postDelayed(this, 0);
            } else {
                doInHaveInternetConnection();
                handler.removeCallbacks(this);
            }
        }
    };

    protected abstract void doInHaveInternetConnection();
}
