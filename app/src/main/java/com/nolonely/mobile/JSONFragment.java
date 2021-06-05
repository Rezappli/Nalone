package com.nolonely.mobile;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.nolonely.mobile.JSONActivity.ACTION_RECEIVE_CONNECTION;
import static com.nolonely.mobile.JSONActivity.ACTION_RECEIVE_NO_CONNECTION;

public abstract class JSONFragment extends Fragment {

    protected Handler handler;
    private boolean broadcastSent;

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

    @Override
    public void onResume() {
        super.onResume();
        launchJSONCall();
    }

    protected void launchJSONCall() {
        broadcastSent = false;
        handler = new Handler();
        handler.postDelayed(runnable, 0);
    }

    private final Runnable runnable = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run() {
            if (!haveInternetConnection()) {
                if (!broadcastSent) {
                    sendNoConnectionBroadcast();
                    broadcastSent = true;
                }
                handler.postDelayed(this, 0);
            } else {
                sendConnectionBroadcast();
                doInHaveInternetConnection();
                handler.removeCallbacks(this);
            }
        }
    };

    private void sendNoConnectionBroadcast() {
        Intent intent = new Intent(ACTION_RECEIVE_NO_CONNECTION);
        LocalBroadcastManager localBctMgr = LocalBroadcastManager.getInstance(getContext());
        localBctMgr.sendBroadcast(intent);
    }

    private void sendConnectionBroadcast() {
        Intent intent = new Intent(ACTION_RECEIVE_CONNECTION);
        LocalBroadcastManager localBctMgr = LocalBroadcastManager.getInstance(getContext());
        localBctMgr.sendBroadcast(intent);
    }


    protected abstract void doInHaveInternetConnection();

}
