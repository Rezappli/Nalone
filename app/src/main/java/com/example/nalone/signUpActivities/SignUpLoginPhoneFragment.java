package com.example.nalone.signUpActivities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.nalone.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpLoginPhoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpLoginPhoneFragment extends Fragment {


    public static SignUpLoginPhoneFragment newInstance() {
        return new SignUpLoginPhoneFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_login_phone, container, false);
    }
}