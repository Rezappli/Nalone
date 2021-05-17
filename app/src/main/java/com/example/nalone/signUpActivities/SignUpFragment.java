package com.example.nalone.signUpActivities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.nalone.R;
import com.example.nalone.objects.User;

public abstract class SignUpFragment extends Fragment implements SignUpListener {

    protected static User user;
    protected int rootView;
    protected View view;
    protected Drawable customErrorDrawable;
    public static boolean isMail;
    protected boolean isBusinessAccount;
    private final BroadcastReceiver receiverTypeAccount = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.w("SIGNUP", "BROADCAST");

            if (intent != null) {
                Log.w("SIGNUP", "BROADCAST");
                isBusinessAccount = intent.getBooleanExtra(ChoiceTypeAccountActivity.EXTRA_TYPE_ACCOUNT, false);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ChoiceTypeAccountActivity.ACTION_GET_TYPE_ACCOUNT);
        getActivity().registerReceiver(receiverTypeAccount, intentFilter);
        Log.w("SIGNUP", String.valueOf(isBusinessAccount));
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(receiverTypeAccount);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(rootView, container, false);
        if (user == null) {
            user = new User();
            Log.w("SIGNUP", "New user");
        }

        SignUpMainActivity.registerSignUpListener(this);
        customErrorDrawable = ContextCompat.getDrawable(getContext(), R.drawable.error_icon);
        customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SignUpMainActivity.unregisterSignUpListener(this);
    }

}
