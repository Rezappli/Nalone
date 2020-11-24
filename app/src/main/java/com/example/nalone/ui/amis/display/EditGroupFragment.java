package com.example.nalone.ui.amis.display;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.nalone.Group;
import com.example.nalone.R;
import com.example.nalone.ResetPassword;
import com.example.nalone.Visibility;
import com.google.android.material.textfield.TextInputEditText;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.mStoreBase;


public class EditGroupFragment extends Fragment {


    TextInputEditText groupName;
    Button profilEditValider, buttonDeleteGroup;
    private ImageView imageViewPublic, imageViewPrive;
    private NavController navController;
    public static Group GROUP_LOAD;
    private Visibility visibility;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_edit_group, container, false);

        navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        buttonBack.setVisibility(View.VISIBLE);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // navController.navigate(R.id.action_navigation_edit_profil_to_navigation_profil);
            }
        });

        groupName = root.findViewById(R.id.groupEditNom);
        profilEditValider = root.findViewById(R.id.profilEditValider);
        imageViewPublic = root.findViewById(R.id.imageViewPublic);
        imageViewPrive = root.findViewById(R.id.imageViewPrivate);
        buttonDeleteGroup = root.findViewById(R.id.buttonDeleteGroup);

        buttonDeleteGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStoreBase.collection("groups").document(GROUP_LOAD.getUid()).delete();
                navController.navigate(R.id.action_navigation_edit_group_to_navigation_mes_groupes);
            }
        });
        groupName.setText(GROUP_LOAD.getName());

        if(GROUP_LOAD.getVisibility() == Visibility.PUBLIC){
            modePublic();
        }else{
            modePrive();
        }


        imageViewPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modePublic();
            }
        });

        imageViewPrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modePrive();
            }
        });

        profilEditValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GROUP_LOAD.setName(groupName.getText().toString());
                boolean error = false;
                if(groupName.getText().toString().equalsIgnoreCase("")){
                    groupName.setError("Entrez un nom");
                    error = true;
                }

                GROUP_LOAD.setVisibility(visibility);

                mStoreBase.collection("groups").document(GROUP_LOAD.getUid()).set(GROUP_LOAD);
                Toast.makeText(getContext(), "Vous avez mis à jour le groupe", Toast.LENGTH_SHORT).show();

            }
        });


        return root;
    }

    private void modePrive() {
        imageViewPrive.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_lock_focused));
        imageViewPublic.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_public_24));
        visibility = Visibility.PRIVATE;
    }

    private void modePublic() {
        imageViewPublic.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_public_focused));
        imageViewPrive.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_lock_24));
        visibility = Visibility.PUBLIC;
    }
}