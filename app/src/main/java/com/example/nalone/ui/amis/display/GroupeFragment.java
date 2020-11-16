package com.example.nalone.ui.amis.display;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nalone.User;
import com.example.nalone.adapter.ItemGroupeAdapter;
import com.example.nalone.adapter.ItemProfilAdapter;
import com.example.nalone.items.ItemGroupe;
import com.example.nalone.listeners.CoreListener;
import com.example.nalone.R;
import com.example.nalone.listeners.FireStoreUsersListeners;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.getUserData;

public class GroupeFragment extends Fragment implements CoreListener {


    private RecyclerView mRecyclerView;
    private ItemGroupeAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public GroupeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.groupe_fragment, container, false);

        List<ItemGroupe> items = new ArrayList<>();

        items.add(new ItemGroupe("1873", R.drawable.ic_baseline_map_24, "Ay Caramba", R.id.buttonMoreInvit, "Gros groupe de bg"));
        items.add(new ItemGroupe("1873", R.drawable.ic_baseline_map_24, "Ay Caramba", R.id.buttonMoreInvit, "Gros groupe de bg"));
        items.add(new ItemGroupe("1873", R.drawable.ic_baseline_map_24, "Ay Caramba", R.id.buttonMoreInvit, "Gros groupe de bg"));
        items.add(new ItemGroupe("1873", R.drawable.ic_baseline_map_24, "Ay Caramba", R.id.buttonMoreInvit, "Gros groupe de bg"));
        items.add(new ItemGroupe("1873", R.drawable.ic_baseline_map_24, "Ay Caramba", R.id.buttonMoreInvit, "Gros groupe de bg"));
        items.add(new ItemGroupe("1873", R.drawable.ic_baseline_map_24, "Ay Caramba", R.id.buttonMoreInvit, "Gros groupe de bg"));

        mAdapter = new ItemGroupeAdapter(items, getContext());
        mRecyclerView = root.findViewById(R.id.recyclerViewGroupe);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                false);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter.setOnItemClickListener(new ItemGroupeAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {

            }
        });
        mRecyclerView.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onDataChangeListener() {
        //updateItems();
    }

    @Override
    public void onUpdateAdapter() {

    }
}