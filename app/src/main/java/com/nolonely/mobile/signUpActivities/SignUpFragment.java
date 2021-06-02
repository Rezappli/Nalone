package com.nolonely.mobile.signUpActivities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.nolonely.mobile.R;
import com.nolonely.mobile.listeners.CreationFragmentListener;
import com.nolonely.mobile.objects.User;

import java.util.UUID;

public abstract class SignUpFragment extends Fragment implements CreationFragmentListener {

    protected static User user;
    protected int rootView;
    protected View view;
    protected Drawable customErrorDrawable;
    public static boolean isMail;
    protected boolean isBusinessAccount;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(rootView, container, false);
        if (user == null) {
            user = new User(UUID.randomUUID().toString());
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
