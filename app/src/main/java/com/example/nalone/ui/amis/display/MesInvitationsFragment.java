package com.example.nalone.ui.amis.display;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.R;
import com.example.nalone.items.ItemPerson;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.HomeActivity.buttonBack;

public class MesInvitationsFragment extends Fragment {

    private NavController navController;
    private RecyclerView mRecyclerView;
    private ProgressBar loading;
    private View rootView;
    private ArrayList<ItemPerson> invits = new ArrayList<>();

    List<String> friends;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mes_invitations, container, false);
        loading = rootView.findViewById(R.id.invits_loading);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewInvitAmis);

        buttonBack.setVisibility(View.VISIBLE);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_invitations_to_navigation_amis);
            }
        });

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewInvitAmis);

        return rootView;
    }


}