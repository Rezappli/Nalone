package com.example.nalone.ui.amis.display;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.example.nalone.objects.ModelData;
import com.example.nalone.R;
import com.example.nalone.objects.User;
import com.example.nalone.ui.message.ChatActivityFriend;
import com.example.nalone.util.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.ON_FRIENDS_ACTIVITY;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.setUserImage;

public class AmisFragment extends Fragment {

    private SearchView search_bar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private NavController navController;
    private TextView resultat;
    private View rootView;
    private RecyclerView mRecyclerView;
    private List<String> friends;
    private int nbInvit;
    private CardView cardViewInvits;
    private TextView textViewNbInvit;
    private LinearLayout linearSansMesAmis;
    private ProgressBar loading;
    private boolean swipe;
    private ImageView imagePerson;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_amis, container, false);
        //createFragment();
        return rootView;
    }

    private void createFragment() {
        nbInvit = 0;
        search_bar = rootView.findViewById(R.id.search_bar_amis);
        resultat = rootView.findViewById(R.id.resultatText_amis);
        resultat.setVisibility(View.GONE);
        linearSansMesAmis = rootView.findViewById(R.id.linearSansMesAmis);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        buttonBack.setVisibility(View.GONE);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewMesAmis);
        cardViewInvits = rootView.findViewById(R.id.cardViewInvits);
        textViewNbInvit = rootView.findViewById(R.id.nbInvits);
        mSwipeRefreshLayout = rootView.findViewById(R.id.AmisSwipeRefreshLayout);
        loading = rootView.findViewById(R.id.loading);
        imagePerson = rootView.findViewById(R.id.imagePerson);

        cardViewInvits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_amis_to_navigation_invitations);
            }
        });

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

                return false;
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createFragment();
            }
        });
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView nomInvit;
        private TextView villePers;
        private LinearLayout layoutProfil;
        private ImageView imagePerson;
        private ImageView button;
        private CardView cardViewPhotoPerson;
        public SwipeLayout swipeLayout;
        public ImageView Delete;
        public ImageView Appel;
        public ImageView Share;
        public ImageButton btnLocation;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomInvit);
            villePers = itemView.findViewById(R.id.villePers);
            layoutProfil = itemView.findViewById(R.id.layoutProfil);
            imagePerson = itemView.findViewById(R.id.imagePerson);
            button = itemView.findViewById(R.id.buttonImage);
            cardViewPhotoPerson = itemView.findViewById(R.id.cardViewPhotoPerson);
            //
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            Delete = (ImageView) itemView.findViewById(R.id.Delete);
            Appel = (ImageView) itemView.findViewById(R.id.Appel);
            Share = (ImageView) itemView.findViewById(R.id.Share);
            btnLocation = (ImageButton) itemView.findViewById(R.id.btnLocation);
        }

    }

    private void removeFriend(final String uid) {

    }

    @Override
    public void onResume(){
        nbInvit = 0;
        ON_FRIENDS_ACTIVITY = true;
        createFragment();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ON_FRIENDS_ACTIVITY = false;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}