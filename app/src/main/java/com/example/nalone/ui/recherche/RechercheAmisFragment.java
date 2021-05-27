package com.example.nalone.ui.recherche;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.adapter.user.SearchUserAdapter;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONArrayListener;
import com.example.nalone.objects.User;
import com.example.nalone.ui.amis.display.InfoUserActivity;
import com.example.nalone.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER;

public class RechercheAmisFragment extends Fragment {

    private SearchView search_bar;
    private NavController navController;
    private RecyclerView mRecyclerView;
    private SearchUserAdapter mAdapter;

    private TextView resultat;

    private List<ItemPerson> items = new ArrayList<>();
    private View rootView;
    private ProgressBar loading;
    private ImageView qr_code;
    private List<User> friends;

    private SwipeRefreshLayout swipeContainer;

    private LinearLayout linearSansRechercheAmis;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recherche_amis, container, false);
        return rootView;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createFragment() {
        loading = rootView.findViewById(R.id.search_loading);

        linearSansRechercheAmis = rootView.findViewById(R.id.linearSansRechercheGroupe);
        swipeContainer = rootView.findViewById(R.id.AmisSwipeRefreshLayout);

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright);

        this.configureSwipeRefreshLayout();

        qr_code = rootView.findViewById(R.id.qr_code_image);

        qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_recherche_amis_to_navigation_camera2);
            }
        });

        search_bar = rootView.findViewById(R.id.search_bar);
        resultat = rootView.findViewById(R.id.resultatText);
        resultat.setVisibility(View.GONE);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        mRecyclerView = rootView.findViewById(R.id.recyclerView);

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

        getUsers();

    }

    private void configureRecyclerViewAmis() {
        this.mAdapter = new SearchUserAdapter(this.friends);
        this.mRecyclerView.setAdapter(this.mAdapter);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.mRecyclerView.setLayoutManager(llm);
        mAdapter.setOnItemClickListener(new SearchUserAdapter.OnItemClickListener() {
            @Override
            public void onDisplayClick(int position) {
                showPopUpProfil(friends.get(position));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getUsers() {
        friends = new ArrayList<>();

        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("limit", 10); //fix a limit to 10 users

        JSONController.getJsonArrayFromUrl(Constants.URL_USER_WHITHOUT_ME, getContext(), params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                try {
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            friends.add((User) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), User.class));
                        }
                        linearSansRechercheAmis.setVisibility(View.GONE);
                        configureRecyclerViewAmis();

                    } else {
                        linearSansRechercheAmis.setVisibility(View.VISIBLE);
                    }
                    loading.setVisibility(View.GONE);

                } catch (JSONException e) {
                    Log.w("Response", "Valeur" + jsonArray.toString());
                    Log.w("Response", "Erreur:" + e.getMessage());
                    Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:" + volleyError.toString());
                Toast.makeText(getContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
            }
        });
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


    public void showPopUpProfil(User u) {
        Intent intent = new Intent(getContext(), InfoUserActivity.class);
        intent.putExtra("user", u);
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        createFragment();
    }


}