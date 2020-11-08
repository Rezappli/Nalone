package com.example.nalone.ui.profil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.nalone.MainActivity;
import com.example.nalone.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static com.example.nalone.util.Constants.USERS_LIST;
import static com.example.nalone.util.Constants.USER_ID;

public class ProfilFragment extends Fragment  {

    private NavController navController;
    private Button sign_out;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView userConnectText;
    private EditText userConnectDesc;
    private TextView userConnectVille, userConnectNbC, userConnectNbP;
    private CardView cardViewProfilParametres,cardViewProfilEdit,cardViewProfilAide, cardViewPhotoEditDesc;
    private ImageView imageViewEditDescription;
    private boolean edit;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_profil, container, false);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        //sign_out = root.findViewById(R.id.sign_out);
        userConnectDesc = root.findViewById(R.id.userConnectDescription);
        userConnectText = root.findViewById(R.id.userConnectText);
        userConnectVille = root.findViewById(R.id.useConnectVille);
        userConnectNbC = root.findViewById(R.id.userConnectNbCreation);
        userConnectNbP = root.findViewById(R.id.userConnectNbParticipation);
        cardViewProfilParametres = root.findViewById(R.id.cardViewProfilParametres);
        cardViewProfilAide = root.findViewById(R.id.cardViewProfilAide);
        cardViewProfilEdit = root.findViewById(R.id.cardViewProfilEdit);
        cardViewPhotoEditDesc = root.findViewById(R.id.cardViewPhotoEditDesc);
        imageViewEditDescription = root.findViewById(R.id.imageViewEditDescription);

        userConnectText.setText(USERS_LIST.get(USER_ID).getPrenom()+" "+USERS_LIST.get(USER_ID).getNom());
        userConnectVille.setText(USERS_LIST.get(USER_ID).getVille()+" ");
        userConnectNbC.setText(USERS_LIST.get(USER_ID).getNbCreation()+"");
        userConnectNbP.setText(USERS_LIST.get(USER_ID).getNbParticipation()+"");
        userConnectDesc.setText(USERS_LIST.get(USER_ID).getDescription());
        userConnectDesc.setClickable(false);
        userConnectDesc.setEnabled(false);

            cardViewPhotoEditDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(edit == false){
                        userConnectDesc.setClickable(true);
                        userConnectDesc.setEnabled(true);
                        imageViewEditDescription.setImageResource(R.drawable.ic_baseline_check_24);
                        edit = true;
                    }else{
                        userConnectDesc.setClickable(false);
                        userConnectDesc.setEnabled(false);
                        imageViewEditDescription.setImageResource(R.drawable.ic_baseline_edit_24);
                        edit = false;
                    }

                }
            });



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        cardViewProfilParametres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_profil_to_navigation_parametres);
            }
        });

        cardViewProfilEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_profil_to_navigation_edit_profil);
            }
        });

        cardViewProfilAide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_profil_to_navigation_aide);
            }
        });

       /* sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.sign_out) {
                    signOut();
                }
            }
        });*/



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





}