package com.nolonely.mobile.ui.message;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class MessageThread extends Thread {

    private ChatActivityFriend chatActivityFriend;
    private boolean stop = false;

    public MessageThread(ChatActivityFriend chatActivityFriend) {
        this.chatActivityFriend = chatActivityFriend;
    }

    @Override
    public void start() {
        super.start();
        stop = false;
        Log.w("ThreadMessage", "Thread start");
    }

    @Override
    public void interrupt() {
        super.interrupt();
        stop = true;
        Log.w("ThreadMessage", "Thread interrupt");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {
        Log.w("ThreadMessage", "Thread work normally");

        while (!stop) {
            Log.w("ThreadMessage", "Thread work");
            try {
                this.chatActivityFriend.updateMessages(this.chatActivityFriend.limit);
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
