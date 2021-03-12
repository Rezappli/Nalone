package com.example.nalone.ui.profil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.ResetPasswordActivity;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.util.Constants;

import org.json.JSONObject;

import static com.example.nalone.ui.profil.MainProfilActivity.buttonBack;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.mStoreBase;


public class EditFragment extends Fragment {


    TextView profilEditPassword, textProfilEditMail, profilEditMail;
    EditText profilEditNumero,profilEditPrenom,profilEditNom,profilEditVille,profilEditDate;
    Button profilEditValider;
    private NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_edit, container, false);

        navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment_profil);
        buttonBack.setVisibility(View.VISIBLE);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_editFragment_to_profilFragment);
            }
        });
        profilEditPassword = root.findViewById(R.id.profilEditPassword);
        profilEditDate = root.findViewById(R.id.profilEditNaissance);
        profilEditMail = root.findViewById(R.id.profilEditMail);
        textProfilEditMail = root.findViewById(R.id.textProfilEditMail);
        profilEditNom = root.findViewById(R.id.profilEditNom);
        profilEditPrenom = root.findViewById(R.id.profilEditPrenom);
        profilEditNumero = root.findViewById(R.id.profilEditNumero);
        profilEditVille = root.findViewById(R.id.profilEditVille);
        profilEditValider = root.findViewById(R.id.profilEditValider);

        profilEditNom.setText(USER.getLast_name());
        profilEditPrenom.setText(USER.getFirst_name());
        profilEditDate.setText(USER.getBirthday_date());
        textProfilEditMail.setText(USER.getMail());
        profilEditNumero.setText(USER.getNumber());
        profilEditVille.setText(USER.getCity());
        textProfilEditMail.setText(USER.getMail());
        Log.w("Data", "Mail:"+USER.getMail());

        profilEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ResetPasswordActivity.class));
            }
        });

        profilEditValider.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String nom = profilEditNom.getText().toString();
                String prenom = profilEditPrenom.getText().toString();
                String ville = profilEditVille.getText().toString();
                String date = profilEditDate.getText().toString();
                String num = profilEditNumero.getText().toString();
                boolean error = false;
                if(nom.equalsIgnoreCase("")){
                    profilEditNom.setError("Entrez votre nom");
                    error = true;
                }

                if(prenom.equalsIgnoreCase("")){
                    profilEditPrenom.setError("Entrez votre prénom");
                    error = true;
                }

                if(date.length() != 10 || date.charAt(4) != '-' || date.charAt(7) != '-'){
                    profilEditDate.setError("Entrez votre date de naissance");
                    error = true;
                }

                if(num.length() != 13){
                    profilEditNumero.setError("Entrez votre numéro");
                    error = true;
                }

                if(ville.equalsIgnoreCase("")){
                    profilEditVille.setError("Entrez votre ville");
                    error = true;
                }

                if(!error){
                    if(!USER.getLast_name().equalsIgnoreCase(nom) || !USER.getFirst_name().equalsIgnoreCase(prenom) ||
                        !USER.getCity().equalsIgnoreCase(ville)  || !USER.getNumber().equalsIgnoreCase(num)
                            || !USER.getBirthday_date().equalsIgnoreCase(date)){

                        if(!USER.getLast_name().equalsIgnoreCase(nom)){
                            USER.setLast_name(nom);
                        }

                        if(!USER.getFirst_name().equalsIgnoreCase(prenom)){
                            USER.setFirst_name(prenom);
                        }

                        if(!USER.getCity().equalsIgnoreCase(ville)){
                            USER.setCity(ville);
                        }

                        if(!USER.getNumber().equalsIgnoreCase(num)){
                            USER.setNumber(num);
                        }

                        if(!USER.getBirthday_date().equalsIgnoreCase(date)){
                            USER.setBirthday_date(date);
                        }

                        updateDescription();
                    }else{
                        navController.navigate(R.id.action_editFragment_to_profilFragment);
                    }
                }
            }
        });


        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateDescription() {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.addParameter("uid", USER.getUid());
        params.addParameter("first_name", USER.getFirst_name());
        params.addParameter("last_name", USER.getLast_name());
        params.addParameter("birthday_date", USER.getBirthday_date());
        params.addParameter("city", USER.getCity());
        params.addParameter("description", USER.getDescription());
        params.addParameter("latitude", USER.getLatitude());
        params.addParameter("longitude", USER.getLongitude());

        JSONController.getJsonObjectFromUrl(Constants.URL_UPDATE_ME, getContext(), params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                Toast.makeText(getContext(), getResources().getString(R.string.update_description), Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.action_editFragment_to_profilFragment);
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                Log.w("Response","Erreur:"+volleyError.toString());
            }
        });
    }
}