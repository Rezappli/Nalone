package com.example.nalone.ui.recherche;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.nalone.adapter.RechercheGroupeAdapter;
import com.example.nalone.enumeration.Visibility;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.util.Cache;
import com.example.nalone.objects.Group;
import com.example.nalone.R;
import com.example.nalone.ui.amis.display.PopUpGroupFragment;
import com.example.nalone.util.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;

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
        root =  inflater.inflate(R.layout.fragment_recherche_groupe, container, false);
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createFragment(){
        groupList = new ArrayList<>();

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
    private void getGroups(){
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.addParameter("uid", USER.getUid());

        linearSansRechercheGroupe.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }

    private void configureSwipeRefreshLayout(){
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
    public void onResume(){
        super.onResume();
        createFragment();
    }



}