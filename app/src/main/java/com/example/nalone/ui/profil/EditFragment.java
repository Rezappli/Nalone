package com.example.nalone.ui.profil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.nalone.R;
import com.example.nalone.ResetPassword;


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

       /* profilEditNom.setText(USERS_LIST.get(USER_ID).getNom()+"");
        profilEditPrenom.setText(USERS_LIST.get(USER_ID).getPrenom()+"");
        //profilEditDate.setText(USERS_LIST.get(USER_ID).getD);
        textProfilEditMail.setText(USERS_LIST.get(USER_ID).getMail()+"");
        profilEditDepartement.setText(USERS_LIST.get(USER_ID).getCursus()+"");
        profilEditNumero.setText(USERS_LIST.get(USER_ID).getNumero()+"");
        profilEditVille.setText(USERS_LIST.get(USER_ID).getVille()+"");*/

        profilEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ResetPassword.class));
            }
        });

        profilEditValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pour toi thiboule
            }
        });


        return root;
    }
}