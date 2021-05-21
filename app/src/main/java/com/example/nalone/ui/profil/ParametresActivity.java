package com.example.nalone.ui.profil;

import android.app.job.JobScheduler;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nalone.MainActivity;
import com.example.nalone.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.mAuth;
import static com.example.nalone.util.Constants.maPosition;
import static com.example.nalone.util.Constants.range;

public class ParametresActivity extends AppCompatActivity {

    private Button sign_out;
    private GoogleSignInClient mGoogleSignInClient;
    private SeekBar seekBar;
    private TextView textViewRayon, textViewLocationActuel, textViewMaPosition;
    public static final String SHARED_PREFS = "sharedPrefs", sharedRange = "sharedRange", sharedNotif = "sharedNotif", sharedPosition = "sharedPosition";
    private int rangeActual;
    private boolean notification = true, position;
    private ImageView buttonBack;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_parametres);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setVisibility(View.VISIBLE);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SharedPreferences settings = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        notification = settings.getBoolean(sharedNotif, false);
        position = settings.getBoolean(sharedPosition, false);
        range = settings.getInt(sharedRange, 0);


        textViewRayon = findViewById(R.id.textViewRayon);
        textViewLocationActuel = findViewById(R.id.textViewLocationActuel);
        textViewMaPosition = findViewById(R.id.textViewMaPosition);

        textViewLocationActuel.setText(USER.getCity());

        if (position) {
            Drawable img = getBaseContext().getResources().getDrawable(R.drawable.ic_baseline_my_location_focused);
            img.setBounds(0, 0, 100, 100);
            textViewMaPosition.setCompoundDrawables(img, null, null, null);

            Drawable img2 = getBaseContext().getResources().getDrawable(R.drawable.ic_baseline_location_edit);
            img2.setBounds(0, 0, 100, 100);
            textViewLocationActuel.setCompoundDrawables(img2, null, null, null);
        } else {
            Drawable img = getBaseContext().getResources().getDrawable(R.drawable.ic_baseline_my_location_24);
            img.setBounds(0, 0, 100, 100);
            textViewMaPosition.setCompoundDrawables(img, null, null, null);

            Drawable img2 = getBaseContext().getResources().getDrawable(R.drawable.ic_baseline_location_edit_focused);
            img2.setBounds(0, 0, 100, 100);
            textViewLocationActuel.setCompoundDrawables(img2, null, null, null);

        }

        textViewMaPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maPosition = true;
                Drawable img = getBaseContext().getResources().getDrawable(R.drawable.ic_baseline_my_location_focused);
                img.setBounds(0, 0, 100, 100);
                textViewMaPosition.setCompoundDrawables(img, null, null, null);

                Drawable img2 = getBaseContext().getResources().getDrawable(R.drawable.ic_baseline_location_edit);
                img2.setBounds(0, 0, 100, 100);
                textViewLocationActuel.setCompoundDrawables(img2, null, null, null);
            }
        });

        textViewLocationActuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable img = getBaseContext().getResources().getDrawable(R.drawable.ic_baseline_my_location_24);
                img.setBounds(0, 0, 100, 100);
                textViewMaPosition.setCompoundDrawables(img, null, null, null);

                Drawable img2 = getBaseContext().getResources().getDrawable(R.drawable.ic_baseline_location_edit_focused);
                img2.setBounds(0, 0, 100, 100);
                textViewLocationActuel.setCompoundDrawables(img2, null, null, null);
            }
        });

        textViewRayon.setText(range + " km");

        final SeekBar seekBar = findViewById(R.id.seekBarRayon);

        seekBar.setMax(200);
        seekBar.setMin(50);
        seekBar.setProgress(range);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = range;

            // When Progress value changed.
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
                textViewRayon.setText(progressValue + " km");
                range = progressValue;
            }

            // Notification that the user has started a touch gesture.
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            // Notification that the user has finished a touch gesture
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                range = progress;
            }
        });


        if (notification) {

        } else {

        }

        Switch swNotif = findViewById(R.id.switchNotif);

        swNotif.setChecked(notification);

        swNotif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notification = true;
                } else {
                    notification = false;

                }
            }
        });


        mGoogleSignInClient = GoogleSignIn.getClient(getBaseContext(), gso);

        sign_out = findViewById(R.id.sign_out_button);

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.sign_out_button) {
                    signOut();
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void signOut() {
        cancelJob();
        mAuth.signOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        final SharedPreferences loginPreferences = getSharedPreferences("login", MODE_PRIVATE);
                        final SharedPreferences.Editor editor = loginPreferences.edit();

                        editor.clear();
                        editor.apply();

                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        setData();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void cancelJob() {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
    }

    private void setData() {
        SharedPreferences settings = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(sharedRange, range);
        editor.putBoolean(sharedNotif, notification);
        editor.putBoolean(sharedPosition, maPosition);

        editor.apply();
    }

}