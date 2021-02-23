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

import com.android.volley.VolleyError;
import com.daimajia.swipe.SwipeLayout;
import com.example.nalone.adapter.MesAmisAdapter;
import com.example.nalone.adapter.RechercheAmisAdapter;
import com.example.nalone.json.JSONArrayListener;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
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

import org.json.JSONArray;
import org.json.JSONException;

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
    private MesAmisAdapter mAdapter;
    private List<User> friends;
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

        getUsers();
    }

    private void configureRecyclerViewAmis() {
        this.mAdapter = new MesAmisAdapter(this.friends);
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.mRecyclerView.setAdapter(this.mAdapter);
        // 3.4 - Set layout manager to position the items
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.mRecyclerView.setLayoutManager(llm);
        mAdapter.setOnItemClickListener(new MesAmisAdapter.OnItemClickListener() {
            @Override
            public void onDisplayClick(int position) {
                showPopUpProfil(friends.get(position));
            }

            @Override
            public void onDeleteClick(int position) {
                removeFriend(friends.get(position));
                createFragment();
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCallClick(int position) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    getActivity().requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 0);
                    return;
                }
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+friends.get(position).getNumber()));
                startActivity(callIntent);
            }

            @Override
            public void onMessageClick(int position) {
                ChatActivityFriend.USER_LOAD = friends.get(position);
                startActivity(new Intent(getContext(), ChatActivityFriend.class));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getUsers() {
        friends = new ArrayList<>();

        JSONObjectCrypt params = new JSONObjectCrypt();
        params.addParameter("uid", USER.getUid());
        params.addParameter("limit", 10); //fix a limit to 10 users

        JSONController.getJsonArrayFromUrl(Constants.URL_MY_FRIENDS, getContext(), params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {

                try {
                    Log.w("Response", jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        friends.add((User) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), User.class));
                    }

                    for (int i = 0; i < friends.size(); i++) {
                        Log.w("Recherche", friends.get(i).getFirst_name()+friends.get(i).getLast_name());
                    }

                    configureRecyclerViewAmis();
                    loading.setVisibility(View.GONE);
                } catch (JSONException e) {
                    Log.w("Response", "Erreur:"+e.getMessage());
                    Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:"+volleyError.toString());
                Toast.makeText(getContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
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
    public void showPopUpProfil(User u) {

        PopupProfilFragment.USER_LOAD = u;
        //PopupProfilFragment.button = getResources(0);

        PopupProfilFragment.type = "amis";
        navController.navigate(R.id.action_navigation_amis_to_navigation_popup_profil);
    }

    private void removeFriend(User user) {

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