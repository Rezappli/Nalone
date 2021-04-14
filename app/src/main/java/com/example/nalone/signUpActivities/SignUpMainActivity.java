package com.example.nalone.signUpActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.nalone.NoLonelyActivity;
import com.example.nalone.R;
import com.example.nalone.objects.User;

import java.util.UUID;

public class SignUpMainActivity extends NoLonelyActivity {

    private NavController navController;
    public static User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.contentView = R.layout.activity_sign_up_main;
        navController = Navigation.findNavController(this,R.id.nav_host_sign_up);
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

    public void next(View view){
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_sign_up);

        for (Fragment fragment : navHostFragment.getChildFragmentManager().getFragments()){
            if (fragment.getClass().equals(SignUpInfosFragment.class)) {
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