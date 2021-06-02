package com.example.nalone.signUpActivities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.nalone.R;
import com.example.nalone.listeners.CreationFragmentListener;
import com.example.nalone.listeners.CreationMainListener;

import java.util.ArrayList;
import java.util.List;

public class SignUpMainActivity extends AppCompatActivity implements CreationMainListener {

    private NavController navController;
    private static List<CreationFragmentListener> listeners;
    public static CreationMainListener listenerMain;

    private View viewStep2, viewStep3, viewStepFinal;
    private CardView cardViewStep2, cardViewStep3;
    private ImageView imageViewStep1, imageViewStep2, imageViewStep3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_main);
        navController = Navigation.findNavController(this, R.id.nav_host_sign_up);

        navController.setGraph(R.navigation.signup_person_navigation);

        viewStep2 = findViewById(R.id.viewStep2);
        viewStep3 = findViewById(R.id.viewStep3);
        viewStepFinal = findViewById(R.id.viewStepFinal);

        cardViewStep2 = findViewById(R.id.cardViewStep2);
        cardViewStep3 = findViewById(R.id.cardViewStep3);

        imageViewStep1 = findViewById(R.id.imageViewStep1);
        imageViewStep2 = findViewById(R.id.imageViewStep2);
        imageViewStep3 = findViewById(R.id.imageViewStep3);

        listenerMain = this;
    }

    public void next(View view) {
        notifySignUpListenersNext();
    }

    public static void registerSignUpListener(CreationFragmentListener listener) {
        if (listeners == null) listeners = new ArrayList<>();
        listeners.add(listener);
    }

    public static void unregisterSignUpListener(CreationFragmentListener listener) {
        listeners.remove(listener);
    }

    private void notifySignUpListenersNext() {
        for (CreationFragmentListener listener : listeners) {
            listener.onNextClicked();
        }
    }

    @Override
    public void onFragmentValidate(Fragment fragment) {
        // step 1
        if (fragment instanceof SignUpInfosFragment) {
            updateStep(imageViewStep1, cardViewStep2, viewStep2, false);
            navController.navigate(R.id.action_signUpInfosFragment_to_signUpLocationFragment);
        }
        if (fragment instanceof SignUpLocationFragment) {
            updateStep(imageViewStep2, cardViewStep3, viewStep3, false);
            navController.navigate(R.id.action_signUpLocationFragment_to_signUpLoginFragment);
        }

        if (fragment instanceof SignUpLoginFragment) {
            updateStep(imageViewStep3, null, viewStepFinal, false);
        }

    }

    @Override
    public void onBackPressed() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_sign_up);

        assert navHostFragment != null;
        for (Fragment fragment : navHostFragment.getChildFragmentManager().getFragments()) {
            if (fragment instanceof SignUpInfosFragment) {
                super.onBackPressed();
            }
            if (fragment instanceof SignUpLocationFragment) {
                updateStep(imageViewStep1, cardViewStep2, viewStep2, true);
                navController.navigate(R.id.action_signUpLocationFragment_to_signUpInfosFragment);
            }

            if (fragment instanceof SignUpLoginFragment) {
                updateStep(imageViewStep2, cardViewStep3, viewStep3, true);
                navController.navigate(R.id.action_signUpLoginFragment_to_signUpLocationFragment);
            }

        }
    }

    private void updateStep(ImageView imageView, CardView cardView, View view, boolean isBack) {
        int customBlue = isBack ? Color.parseColor("#aaaaaa") : getResources().getColor(R.color.colorPrimary);
        imageView.setColorFilter(customBlue);
        cardView.setCardBackgroundColor(customBlue);
        view.setBackground(new ColorDrawable(customBlue));
    }
}