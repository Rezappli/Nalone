package com.example.nalone.signUpActivities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.nalone.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpLoginMailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpLoginMailFragment extends Fragment {


    public static SignUpLoginMailFragment newInstance() {
        return new SignUpLoginMailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

        return inflater.inflate(R.layout.fragment_sign_up_login_mail, container, false);
    }
}