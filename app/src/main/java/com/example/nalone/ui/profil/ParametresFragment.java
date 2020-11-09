package com.example.nalone.ui.profil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.nalone.MainActivity;
import com.example.nalone.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static android.content.Context.MODE_PRIVATE;
import static com.example.nalone.util.Constants.range;

public class ParametresFragment extends Fragment {

    private Button sign_out;
    private GoogleSignInClient mGoogleSignInClient;
    private SeekBar seekBar;
    private TextView textViewRayon, textViewInternational;
    public static final String SHARED_PREFS = "sharedPrefs", sharedRange = "sharedRange", sharedIntern = "sharedIntern";
    private int rangeActual;
    private boolean international;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_parametres, container, false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        SharedPreferences settings = this.getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        international = settings.getBoolean(sharedIntern, false);

        textViewRayon = root.findViewById(R.id.textViewRayon);
        textViewRayon.setText(range/1000+" km");

        final SeekBar seekBar =  root.findViewById(R.id.seekBarRayon);

        seekBar.setMax(200000);
        seekBar.setProgress(range);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = range;

            // When Progress value changed.
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
                textViewRayon.setText(progressValue/1000 + " km");
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

        textViewInternational = root.findViewById(R.id.textViewInternational);

        if(international == true){
            textViewInternational.setText("Oui");
            seekBar.setEnabled(false);
        }else{
            textViewInternational.setText("Non");
        }

        Switch sw = (Switch) root.findViewById(R.id.switch1);

        sw.setChecked(international);


        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textViewInternational.setText("Oui");
                    rangeActual = range;
                    range = 10000000*100;
                    seekBar.setProgress(seekBar.getMax());
                    textViewRayon.setText(range/1000+" km");
                    seekBar.setEnabled(false);
                    international = true;
                } else {
                    textViewInternational.setText("Non");
                    range = 50000;
                    textViewRayon.setText(range/1000+" km");
                    seekBar.setProgress(range);
                    international = false;
                    seekBar.setEnabled(true);

                }
            }
        });

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        sign_out = root.findViewById(R.id.sign_out_button);

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.sign_out_button) {
                    signOut();
                }
            }
        });

        return root;
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        setData();
    }

    private void setData(){
        SharedPreferences settings = this.getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt(sharedRange, range);
        editor.putBoolean(sharedIntern, international);

        editor.apply();
    }

}