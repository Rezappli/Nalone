package com.example.nalone.ui.recherche;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.ItemPerson;
import com.example.nalone.ItemPersonAdapter;
import com.example.nalone.R;

import java.util.ArrayList;

public class RechercheFragment extends Fragment {

    private RechercheViewModel rechercheViewModel;
    private SearchView search_bar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        final ArrayList<ItemPerson> items = new ArrayList<>();

        rechercheViewModel =
                ViewModelProviders.of(this).get(RechercheViewModel.class);
        View root = inflater.inflate(R.layout.fragment_recherche, container, false);

        search_bar = root.findViewById(R.id.search_bar);

        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    search_bar.onActionViewExpanded();
            }
        });

        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.w("Recherche", "Le texte change :"+newText);
                final ArrayList<ItemPerson> tempList = new ArrayList<>();
                if(newText.length() > 0) {
                    for (int i = 0; i < items.size(); i++) {
                        Log.w("Recherche", "On re"+newText);
                        if (items.get(i).getNom().contains(newText)) {
                            tempList.add(items.get(i));
                        }
                    }
                    Log.w("Recherche", "Refresh de la liste ");
                    mAdapter = new ItemPersonAdapter(tempList);
                    mRecyclerView.setAdapter(mAdapter);

                }else{
                    mAdapter = new ItemPersonAdapter(items);
                    mRecyclerView.setAdapter(mAdapter);
                }
                return false;
            }
        });

        items.add(new ItemPerson(R.drawable.ic_baseline_account_circle_24, "User 1", R.drawable.ic_baseline_add_24));
        items.add(new ItemPerson(R.drawable.ic_baseline_account_circle_24, "User 2", R.drawable.ic_baseline_add_24));
        items.add(new ItemPerson(R.drawable.ic_baseline_account_circle_24, "User 3", R.drawable.ic_baseline_add_24));
        items.add(new ItemPerson(R.drawable.ic_baseline_account_circle_24, "User 1", R.drawable.ic_baseline_add_24));
        items.add(new ItemPerson(R.drawable.ic_baseline_account_circle_24, "User 2", R.drawable.ic_baseline_add_24));
        items.add(new ItemPerson(R.drawable.ic_baseline_account_circle_24, "User 3", R.drawable.ic_baseline_add_24));
        items.add(new ItemPerson(R.drawable.ic_baseline_account_circle_24, "User 1", R.drawable.ic_baseline_add_24));
        items.add(new ItemPerson(R.drawable.ic_baseline_account_circle_24, "User 2", R.drawable.ic_baseline_add_24));
        items.add(new ItemPerson(R.drawable.ic_baseline_account_circle_24, "User 3", R.drawable.ic_baseline_add_24));
        items.add(new ItemPerson(R.drawable.ic_baseline_account_circle_24, "User 1", R.drawable.ic_baseline_add_24));
        items.add(new ItemPerson(R.drawable.ic_baseline_account_circle_24, "User 2", R.drawable.ic_baseline_add_24));
        items.add(new ItemPerson(R.drawable.ic_baseline_account_circle_24, "User 3", R.drawable.ic_baseline_add_24));
        items.add(new ItemPerson(R.drawable.ic_baseline_account_circle_24, "User 1", R.drawable.ic_baseline_add_24));
        items.add(new ItemPerson(R.drawable.ic_baseline_account_circle_24, "User 2", R.drawable.ic_baseline_add_24));
        items.add(new ItemPerson(R.drawable.ic_baseline_account_circle_24, "User 3", R.drawable.ic_baseline_add_24));


        mRecyclerView = root.findViewById(R.id.recyclerView);
        mAdapter = new ItemPersonAdapter(items);
        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return root;

    }
}