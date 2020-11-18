package com.example.nalone.ui.profil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.nalone.R;
import com.example.nalone.ResetPassword;
import com.example.nalone.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.mStoreBase;


public class EditFragment extends Fragment {


    TextView profilEditPassword, textProfilEditMail, profilEditMail;
    EditText profilEditNumero,profilEditPrenom,profilEditNom,profilEditVille,profilEditDepartement,profilEditDate;
    Button profilEditValider;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_edit, container, false);

        profilEditPassword = root.findViewById(R.id.profilEditPassword);
        profilEditDate = root.findViewById(R.id.profilEditNaissance);
        profilEditDepartement = root.findViewById(R.id.profilEditDepartement);
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
        profilEditDepartement.setText(USER.getCursus());
        profilEditNumero.setText(USER.getNumber());
        profilEditVille.setText(USER.getCity());

        profilEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ResetPassword.class));
            }
        });

        profilEditValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = profilEditNom.getText().toString();
                String prenom = profilEditPrenom.getText().toString();
                String ville = profilEditVille.getText().toString();
                String dep = profilEditDepartement.getText().toString();
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

                if(date.length() != 10 || date.charAt(2) != '/' || date.charAt(5) != '/'){
                    profilEditDate.setError("Entrez votre date de naissance");
                    error = true;
                }

                if(num.length() != 10){
                    profilEditNumero.setError("Entrez votre numéro");
                    error = true;
                }

                if(ville.equalsIgnoreCase("")){
                    profilEditVille.setError("Entrez votre ville");
                    error = true;
                }

                if(!error){
                    if(!USER.getLast_name().equalsIgnoreCase(nom) || !USER.getFirst_name().equalsIgnoreCase(prenom) ||
                        !USER.getCity().equalsIgnoreCase(ville) || !USER.getCursus().equalsIgnoreCase(dep)
                        || !USER.getNumber().equalsIgnoreCase(num) || !USER.getBirthday_date().equalsIgnoreCase(date)){

                        if(!USER.getLast_name().equalsIgnoreCase(nom)){
                            USER.setLast_name(nom);
                        }

                        if(!USER.getFirst_name().equalsIgnoreCase(prenom)){
                            USER.setFirst_name(prenom);
                        }

                        if(!USER.getCity().equalsIgnoreCase(ville)){
                            USER.setCity(ville);
                        }

                        if(!USER.getCursus().equalsIgnoreCase(dep)){
                            USER.setCursus(dep);
                        }

                        if(!USER.getNumber().equalsIgnoreCase(num)){
                            USER.setNumber(num);
                        }

                        if(!USER.getBirthday_date().equalsIgnoreCase(date)){
                            USER.setBirthday_date(date);
                        }

                        mStoreBase.collection("users").document(USER.getUid()).set(USER);
                        Toast.makeText(getContext(), "Vous avez mis à jour vos informations", Toast.LENGTH_SHORT).show();
                    }else{
                        //POUR TOI MATHIS -> RETOUR VERS PROFIL
                    }

                }


            }
        });


        return root;
    }
}