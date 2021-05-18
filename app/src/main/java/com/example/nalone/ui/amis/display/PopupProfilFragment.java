package com.example.nalone.ui.amis.display;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.objects.User;
import com.example.nalone.util.Constants;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.USER;

public class PopupProfilFragment extends Fragment {

    public static User USER_LOAD;
    public static int button = 0;
    private NavController navController;
    public static String type;

    private ImageView imagePerson;
    private Button buttonAdd;

    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    int nbEventFirt, nbEventSecond, nbEventThird, nbEventFourth;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_popup_profil, container, false);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        buttonBack.setVisibility(View.VISIBLE);
        if (type == "amis") {
            buttonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navController.navigate(R.id.action_navigation_popup_profil_to_navigation_amis2);
                }
            });
        }
        if (type == "recherche") {
            buttonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navController.navigate(R.id.action_navigation_popup_profil_to_navigation_recherche_amis);
                }
            });
        }

        TextView tvR = root.findViewById(R.id.tvR);
        TextView tvPython = root.findViewById(R.id.tvPython);
        TextView tvCPP = root.findViewById(R.id.tvCPP);
        TextView tvJava = root.findViewById(R.id.tvJava);
        PieChart pieChart = root.findViewById(R.id.piechart);

        nbEventFirt = 30;
        nbEventSecond = 10;
        nbEventThird = 5;
        nbEventFourth = 2;

        pieChart.addPieSlice(
                new PieModel(
                        "R",
                        nbEventFirt,
                        getResources().getColor(R.color.colorPrimary)));
        pieChart.addPieSlice(
                new PieModel(
                        "Python",
                        nbEventSecond,
                        getResources().getColor(R.color.colorSecond)));
        pieChart.addPieSlice(
                new PieModel(
                        "C++",
                        nbEventThird,
                        getResources().getColor(R.color.colorThird)));

        pieChart.startAnimation();
        TextView nameProfil;
        TextView descriptionProfil;
        TextView nbCreateProfil;
        TextView nbParticipateProfil;
        TextView villeProfil;

        CardView cardViewPhotoPerson;

        nameProfil = root.findViewById(R.id.profilName);
        descriptionProfil = root.findViewById(R.id.profilDescription);
        nbCreateProfil = root.findViewById(R.id.nbEventCreate);
        nbParticipateProfil = root.findViewById(R.id.nbEventParticipe);
        imagePerson = root.findViewById(R.id.imagePerson);
        buttonAdd = root.findViewById(R.id.buttonAdd);
        cardViewPhotoPerson = root.findViewById(R.id.cardViewPhotoPerson);
        villeProfil = root.findViewById(R.id.userConnectVille);

        nameProfil.setText(USER_LOAD.getName());
        villeProfil.setText(USER_LOAD.getCity());
        nbCreateProfil.setText(USER_LOAD.getNumber_events_create());
        nbParticipateProfil.setText(USER_LOAD.getNumber_events_attend());

        Constants.setUserImage(USER_LOAD, imagePerson);

        List<ImageView> imageCentreInteret = new ArrayList<>();

        ImageView img_centre1 = root.findViewById(R.id.imageViewCI1);
        ImageView img_centre2 = root.findViewById(R.id.imageViewCI2);
        ImageView img_centre3 = root.findViewById(R.id.imageViewCI3);
        ImageView img_centre4 = root.findViewById(R.id.imageViewCI4);
        ImageView img_centre5 = root.findViewById(R.id.imageViewCI5);

        imageCentreInteret.add(img_centre1);
        imageCentreInteret.add(img_centre2);
        imageCentreInteret.add(img_centre3);
        imageCentreInteret.add(img_centre4);
        imageCentreInteret.add(img_centre5);


        nameProfil.setText(USER_LOAD.getName());
        descriptionProfil.setText(USER_LOAD.getDescription());
        nbCreateProfil.setText(USER_LOAD.getNumber_events_create());
        nbParticipateProfil.setText(USER_LOAD.getNumber_events_attend());
        //buttonAdd.setImageResource(button);

        if (USER_LOAD.getDescription().matches("")) {
            descriptionProfil.setVisibility(View.GONE);
        }

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Log.w("Button", "Button : " + button);
                if (button == R.drawable.ic_round_mail_24) {
                    Toast.makeText(getContext(), getResources().getString(R.string.invit_received), Toast.LENGTH_SHORT).show();
                } else if (button == R.drawable.ic_round_hourglass_top_24) {
                    Toast.makeText(getContext(), getResources().getString(R.string.invit_in_progress), Toast.LENGTH_SHORT).show();
                    buttonAdd.setText(getResources().getString(R.string.button_invit_waiting));
                    buttonAdd.setBackground(getResources().getDrawable(R.drawable.custom_input));
                } else {
                    addFriend();
                }
            }
        });


        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addFriend() {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("uid_friend", USER_LOAD.getUid());
        params.putCryptParameter("notification_sender", getResources().getString(R.string.invit_received_notifications) + " " + USER.getName());
        params.putCryptParameter("notification_receiver", getResources().getString(R.string.invit_send_notifications) + " " + USER_LOAD.getName());

        JSONController.getJsonObjectFromUrl(Constants.URL_SEND_FRIEND_REQUEST, getContext(), params, new JSONObjectListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                if (jsonObject.length() == 3) {
                    Toast.makeText(getContext(), getResources().getString(R.string.invit_send), Toast.LENGTH_SHORT).show();
                    buttonAdd.setText(getResources().getString(R.string.button_invit_waiting));
                    buttonAdd.setBackground(getResources().getDrawable(R.drawable.custom_input));
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    Log.w("Response", "Error:" + jsonObject.toString());
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                Log.w("Response", "Error:" + volleyError.toString());
            }
        });
    }

}