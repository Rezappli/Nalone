package com.example.nalone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CreateGroupFragment extends Fragment {

    ImageView buttonMoreGroup;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_create_group, container, false);
        navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        buttonMoreGroup = root.findViewById(R.id.buttonMoreGroup);
        buttonMoreGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_creat_group_to_navigation_list_amis);
            }
        });
        return root;
    }
}