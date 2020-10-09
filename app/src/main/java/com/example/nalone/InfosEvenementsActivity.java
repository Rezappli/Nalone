package com.example.nalone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class InfosEvenementsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ItemImagePersonAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos_evenements);

        final List<ItemImagePerson> users = new ArrayList<>();

        mRecyclerView = findViewById(R.id.recyclerViewMembresInscrits);
        mLayoutManager = new LinearLayoutManager(getBaseContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        users.add(new ItemImagePerson(R.drawable.background_image));
        users.add(new ItemImagePerson(R.drawable.background_image));
        users.add(new ItemImagePerson(R.drawable.background_image));
        users.add(new ItemImagePerson(R.drawable.background_image));
        users.add(new ItemImagePerson(R.drawable.background_image));
        users.add(new ItemImagePerson(R.drawable.background_image));

        mAdapter = new ItemImagePersonAdapter(users);

    }
}