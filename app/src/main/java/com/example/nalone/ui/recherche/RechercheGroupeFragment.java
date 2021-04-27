package com.example.nalone.ui.recherche;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.nalone.R;
import com.example.nalone.adapter.RechercheGroupeAdapter;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.objects.Group;
import com.example.nalone.ui.amis.display.PopUpGroupFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.USER;

public class RechercheGroupeFragment extends Fragment {


    private NavController navController;
    private RecyclerView mRecyclerView;
    private RechercheGroupeAdapter mAdapter;
    private ImageView addGroup;
    private SwipeRefreshLayout swipeContainer;
    private View root;
    private List<Group> groupList;
    private LinearLayout linearSansRechercheGroupe;
    private ProgressBar loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_recherche_groupe, container, false);
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createFragment() {
        groupList = new ArrayList<>();

        buttonBack.setVisibility(View.VISIBLE);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        linearSansRechercheGroupe = root.findViewById(R.id.linearSansRechercheGroupe);
        swipeContainer = root.findViewById(R.id.AmisSwipeRefreshLayout);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright);
        loading = root.findViewById(R.id.loading);
        this.configureSwipeRefreshLayout();

        initRecycler();
        addGroup = root.findViewById(R.id.create_group_button);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_recherche_groupe_to_navigation_creat_group);
            }
        });

        getGroups();

    }

    private void initRecycler() {
        mRecyclerView = root.findViewById(R.id.recyclerViewGroupe);
        mAdapter = new RechercheGroupeAdapter(groupList, getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getGroups() {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());

        linearSansRechercheGroupe.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }

    private void configureSwipeRefreshLayout() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onRefresh() {
                createFragment();
            }
        });
    }


    public void showPopUpGroup(final Group g) {
        PopUpGroupFragment.GROUP_LOAD = g;
        navController.navigate(R.id.action_navigation_recherche_groupe_to_navigation_popup_group);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onResume() {
        super.onResume();
        createFragment();
    }


}