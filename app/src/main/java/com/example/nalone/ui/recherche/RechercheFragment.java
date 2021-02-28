package com.example.nalone.ui.recherche;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.nalone.adapter.RechercheAmisAdapter;
import com.example.nalone.adapter.RechercheGroupeAdapter;
import com.example.nalone.listeners.JSONArrayListener;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.objects.Group;
import com.example.nalone.R;
import com.example.nalone.objects.User;
import com.example.nalone.ui.amis.display.PopUpGroupFragment;
import com.example.nalone.ui.amis.display.PopupProfilFragment;
import com.example.nalone.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER;

public class RechercheFragment extends Fragment {

    private RecyclerView recyclerAmis;
    private RechercheAmisAdapter adapterAmis;
    private RechercheGroupeAdapter adapterGroup;
    private RecyclerView recyclerGroup;
    private CardView cardViewRechercheAmis;
    private CardView cardViewRechercheGroupes;
    private View rootView;
    private NavController navController;

    private TextView textViewRechercheAmis;
    private TextView textViewRechercheGroupes;
    private ProgressBar loading;

    private List<User> friends;
    private List<User> myGroups;

    private RelativeLayout relativeAmis, relativeGroup;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recherche, container, false);
        createFragment();
        getFriends();
        return rootView;


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createFragment() {
        myGroups = new ArrayList<>();
        recyclerAmis = rootView.findViewById(R.id.recyclerViewRechercheAmis);
        recyclerGroup = rootView.findViewById(R.id.recyclerViewRechercheGroupes);
        cardViewRechercheAmis = rootView.findViewById(R.id.cardViewRechercheAmis);
        cardViewRechercheGroupes = rootView.findViewById(R.id.cardViewRechercheGroupes);
        textViewRechercheAmis = rootView.findViewById(R.id.textViewRechercheAmis);
        textViewRechercheGroupes = rootView.findViewById(R.id.textViewRechercheGroupes);
        loading = rootView.findViewById(R.id.search_loading);
        cardViewRechercheAmis.setVisibility(View.INVISIBLE);
        cardViewRechercheGroupes.setVisibility(View.INVISIBLE);
        relativeAmis = rootView.findViewById(R.id.relativeSansAmis);
        relativeGroup = rootView.findViewById(R.id.relativeSansGroup);

        textViewRechercheAmis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_recherche_to_navigation_recherche_amis);
            }
        });

        textViewRechercheGroupes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_recherche_to_navigation_rcherche_groupe);
            }
        });

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getFriends() {
        friends = new ArrayList<>();
        myGroups = new ArrayList<>();

        JSONObjectCrypt params = new JSONObjectCrypt();
        params.addParameter("uid", USER.getUid());
        params.addParameter("limit", 3); //fix a limit to 10 users

        JSONController.getJsonArrayFromUrl(Constants.URL_USER_WHITHOUT_ME, getContext(), params, new JSONArrayListener() {
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
                    cardViewRechercheAmis.setVisibility(View.VISIBLE);
                    cardViewRechercheGroupes.setVisibility(View.VISIBLE);
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

    private void configureRecyclerViewAmis() {
        this.adapterAmis = new RechercheAmisAdapter(this.friends);
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.recyclerAmis.setAdapter(this.adapterAmis);
        // 3.4 - Set layout manager to position the items
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.recyclerAmis.setLayoutManager(llm);
        adapterAmis.setOnItemClickListener(new RechercheAmisAdapter.OnItemClickListener() {
            @Override
            public void onDisplayClick(int position) {
                showPopUpProfil(friends.get(position));
            }
        });
    }

    private void configureRecyclerViewGroup() {
        //this.adapterGroup = new RechercheGroupeAdapter(this.groups);
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.recyclerGroup.setAdapter(this.adapterGroup);
        // 3.4 - Set layout manager to position the items
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.recyclerGroup.setLayoutManager(llm);
    }


    public void showPopUpGroup(final Group g) {
        PopUpGroupFragment.GROUP_LOAD = g;
        navController.navigate(R.id.action_navigation_recherche_to_navigation_popup_group);
    }

    public void showPopUpProfil(User u) {

        PopupProfilFragment.USER_LOAD = u;
        PopupProfilFragment.button = R.drawable.ic_baseline_add_circle_outline_24;

        PopupProfilFragment.type = "recherche";
        navController.navigate(R.id.action_navigation_recherche_to_navigation_popup_profil);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}