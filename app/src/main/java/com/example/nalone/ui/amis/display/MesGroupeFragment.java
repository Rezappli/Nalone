package com.example.nalone.ui.amis.display;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.R;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.objects.Group;

public class MesGroupeFragment extends Fragment {


    private NavController navController;
    private RecyclerView mRecyclerView;
    private ImageView addGroup;
    private ProgressBar loading;
    private LinearLayout linearSansMesGroupes;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mes_groupe, container, false);
        return rootView;
    }

    public void createFragment() {
        Log.w("MesGroupes", "Call");
        mRecyclerView = rootView.findViewById(R.id.recyclerViewGroupe);
        addGroup = rootView.findViewById(R.id.create_group_button);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        loading = rootView.findViewById(R.id.loading);
        linearSansMesGroupes = rootView.findViewById(R.id.linearSansMesGroupes);

        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_mes_groupes_to_navigation_creat_group);
            }
        });
    }

    private void getMyGroups() {
        JSONObjectCrypt params = new JSONObjectCrypt();
    }

    public void showPopUpGroup(final Group g) {
        PopUpMesGroupesFragment.GROUP_LOAD = g;
        navController.navigate(R.id.action_navigation_mes_groupes_to_navigation_popup_mes_groupes);
    }

}