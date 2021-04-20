package com.example.nalone.signUpActivities;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.nalone.NoLonelyActivity;
import com.example.nalone.R;
import com.example.nalone.objects.User;

public class SignUpMainActivity extends NoLonelyActivity {

    public static String userName;
    public static String userSurname;
    public static String userCity;
    public static String userTel;
    public static String userMail;
    public static String userPassword;
    public static String userSex;

    private NavController navController;

    private User currentUser;

    private SignUpInfosFragment signUpInfosFragment;
    private SignUpContactFragment signUpContactFragment;
    private SignUpLocationFragment signUpLocationFragment;
    private SignUpLoginFragment signUpLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_main);
        navController = Navigation.findNavController(this, R.id.nav_host_sign_up);
        signUpContactFragment = SignUpContactFragment.newInstance();
        signUpInfosFragment = SignUpInfosFragment.newInstance();
        signUpLocationFragment = SignUpLocationFragment.newInstance();
        signUpLoginFragment = SignUpLoginFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_sign_up);

        for (Fragment fragment : navHostFragment.getChildFragmentManager().getFragments()) {
            if (fragment.getClass().equals(SignUpInfosFragment.class)) {
                super.onBackPressed();
            }
            if (fragment.getClass().equals(SignUpLocationFragment.class)) {
                navController.navigate(R.id.action_signUpLocationFragment_to_signUpInfosFragment);
            }
            if (fragment.getClass().equals(SignUpContactFragment.class)) {
                navController.navigate(R.id.action_signUpContactFragment_to_signUpLocationFragment);
            }
            if (fragment.getClass().equals(SignUpLoginFragment.class)) {
                navController.navigate(R.id.action_signUpLoginFragment_to_signUpContactFragment);
            }
        }

    }

    public void next(View view) {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_sign_up);

        for (Fragment fragment : navHostFragment.getChildFragmentManager().getFragments()) {
            if (fragment.getClass().equals(SignUpInfosFragment.class) && SignUpInfosFragment.checkValidity()) {
                navController.navigate(R.id.action_signUpInfosFragment_to_signUpLocationFragment);
            }
            if (fragment.getClass().equals(SignUpLocationFragment.class)) {
                navController.navigate(R.id.action_signUpLocationFragment_to_signUpContactFragment);
            }
            if (fragment.getClass().equals(SignUpContactFragment.class)) {
                navController.navigate(R.id.action_signUpContactFragment_to_signUpLoginFragment);
            }
            if (fragment.getClass().equals(SignUpLoginFragment.class)) {
            }
        }
    }
}