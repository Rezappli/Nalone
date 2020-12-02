package com.example.nalone.ui.profil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
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
import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.maPosition;
import static com.example.nalone.util.Constants.range;

public class ParametresFragment extends Fragment {

    private Button sign_out;
    private GoogleSignInClient mGoogleSignInClient;
    private SeekBar seekBar;
    private TextView textViewRayon, textViewLocationActuel, textViewMaPosition;
    public static final String SHARED_PREFS = "sharedPrefs", sharedRange = "sharedRange",sharedNotif = "sharedNotif",sharedPosition = "sharedPosition";
    private int rangeActual;
    private boolean  notification = true, position;
    private NavController navController;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_parametres, container, false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        buttonBack.setVisibility(View.VISIBLE);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_parametres_to_navigation_profil);
            }
        });

        SharedPreferences settings = this.getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        notification = settings.getBoolean(sharedNotif, false);
        position = settings.getBoolean(sharedPosition, false);


        textViewRayon = root.findViewById(R.id.textViewRayon);
        textViewLocationActuel = root.findViewById(R.id.textViewLocationActuel);
        textViewMaPosition = root.findViewById(R.id.textViewMaPosition);

        textViewLocationActuel.setText(USER.getCity());

        if(position) {
            Drawable img = getContext().getResources().getDrawable(R.drawable.ic_baseline_my_location_focused);
            img.setBounds(0, 0, 100, 100);
            textViewMaPosition.setCompoundDrawables(img, null, null, null);

            Drawable img2 = getContext().getResources().getDrawable(R.drawable.ic_baseline_location_edit);
            img2.setBounds(0, 0, 100, 100);
            textViewLocationActuel.setCompoundDrawables(img2, null, null, null);
        }else{
            Drawable img = getContext().getResources().getDrawable(R.drawable.ic_baseline_my_location_24);
            img.setBounds(0, 0, 100, 100);
            textViewMaPosition.setCompoundDrawables(img, null, null, null);

            Drawable img2 = getContext().getResources().getDrawable(R.drawable.ic_baseline_location_edit_focused);
            img2.setBounds(0, 0, 100, 100);
            textViewLocationActuel.setCompoundDrawables(img2, null, null, null);

        }

        textViewMaPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maPosition = true;
                Drawable img = getContext().getResources().getDrawable(R.drawable.ic_baseline_my_location_focused);
                img.setBounds(0, 0, 100, 100);
                textViewMaPosition.setCompoundDrawables(img, null, null, null);

                Drawable img2 = getContext().getResources().getDrawable(R.drawable.ic_baseline_location_edit);
                img2.setBounds(0, 0, 100, 100);
                textViewLocationActuel.setCompoundDrawables(img2, null, null, null);
            }
        });

        textViewLocationActuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable img = getContext().getResources().getDrawable(R.drawable.ic_baseline_my_location_24);
                img.setBounds(0, 0, 100, 100);
                textViewMaPosition.setCompoundDrawables(img, null, null, null);

                Drawable img2 = getContext().getResources().getDrawable(R.drawable.ic_baseline_location_edit_focused);
                img2.setBounds(0, 0, 100, 100);
                textViewLocationActuel.setCompoundDrawables(img2, null, null, null);
            }
        });

        textViewRayon.setText(range+" km");

        final SeekBar seekBar =  root.findViewById(R.id.seekBarRayon);

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



        if(notification){

        }else{

        }

        Switch swNotif = (Switch) root.findViewById(R.id.switchNotif);

        swNotif.setChecked(notification);

        swNotif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    notification = true;
                }else{
                    notification = false;

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

        Log.w("Range", "Range : " + range/1000);
        editor.putInt(sharedRange, range/1000);
        editor.putBoolean(sharedNotif, notification);
        editor.putBoolean(sharedPosition, maPosition);

        editor.apply();
    }

}