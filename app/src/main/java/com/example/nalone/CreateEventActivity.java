package com.example.nalone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CreateEventActivity extends AppCompatActivity {
    private CardView cardViewPrivate;
    private CardView cardViewPublic;
    private ImageButton imageButtonAddInvit;
    private TextView textViewListe;
    private ImageView imageViewPublic;
    private ImageView imageViewPrivate;
    private Dialog dialogAddPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        cardViewPrivate = findViewById(R.id.cardViewPrivate);
        cardViewPublic = findViewById(R.id.cardViewPublic);
        imageButtonAddInvit = findViewById(R.id.buttonMoreInvit);
        textViewListe = findViewById(R.id.textViewListe);
        imageViewPublic = findViewById(R.id.imageViewPublic);
        imageViewPrivate = findViewById(R.id.imageViewPrivate);
        dialogAddPerson = new Dialog(this);


        cardViewPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonAddInvit.setVisibility(View.VISIBLE);
                textViewListe.setVisibility(View.VISIBLE);
                imageViewPrivate.setImageResource(R.drawable.ic_baseline_lock_focused);
                imageViewPublic.setImageResource(R.drawable.ic_baseline_public_24);

            }
        });

        cardViewPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonAddInvit.setVisibility(View.GONE);
                textViewListe.setVisibility(View.GONE);
                imageViewPublic.setImageResource(R.drawable.ic_baseline_public_focused);
                imageViewPrivate.setImageResource(R.drawable.ic_baseline_lock_24);
            }
        });




        RecyclerView mRecyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager mLayoutManager;

        ArrayList<ItemPerson> items = new ArrayList<>();

        items.add(new ItemPerson(R.drawable.ic_baseline_account_circle_24, "User 1"));
        items.add(new ItemPerson(R.drawable.baseline_alternate_email_focused, "User 2"));
        items.add(new ItemPerson(R.drawable.ic_baseline_lock_24, "User 3"));

        mRecyclerView = findViewById(R.id.recyclerView1);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ItemPersonAdapter(items);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }



    public void showPopUp(View v){


        dialogAddPerson.setContentView(R.layout.popup_add_invit);
        dialogAddPerson.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAddPerson.show();
    }
}