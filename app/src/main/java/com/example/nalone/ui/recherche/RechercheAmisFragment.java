package com.example.nalone.ui.recherche;

import android.content.Context;
import android.graphics.Color;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.example.nalone.adapter.ItemFiltreAdapter;
import com.example.nalone.adapter.RechercheAmisAdapter;
import com.example.nalone.items.ItemFiltre;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.R;
import com.example.nalone.json.JSONArrayListener;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.objects.User;
import com.example.nalone.ui.amis.display.PopupProfilFragment;
import com.example.nalone.util.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.mStoreBase;

public class RechercheAmisFragment extends Fragment {

    private RechercheViewModel rechercheViewModel;
    private SearchView search_bar;
    private NavController navController;
    private RecyclerView mRecyclerView;
    private RechercheAmisAdapter mAdapter;

    private TextView resultat;
    private Context context;

    private List<ItemPerson> items = new ArrayList<>();
    private View rootView;
    private ProgressBar loading;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mRecyclerViewFiltre;
    private ItemFiltreAdapter mAdapterFiltre;
    private RecyclerView.LayoutManager mLayoutManagerFiltre;
    private ImageView qr_code;
    private List<User> friends;

    private SwipeRefreshLayout swipeContainer;

    private LinearLayout linearSansRechercheAmis;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rechercheViewModel =
                ViewModelProviders.of(this).get(RechercheViewModel.class);
        rootView = inflater.inflate(R.layout.fragment_recherche_amis, container, false);

        createFragment();
        getUsers();

        return rootView;

    }

    private void configureRecyclerViewAmis() {
        this.mAdapter = new RechercheAmisAdapter(this.friends);
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.mRecyclerView.setAdapter(this.mAdapter);
        // 3.4 - Set layout manager to position the items
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.mRecyclerView.setLayoutManager(llm);
        mAdapter.setOnItemClickListener(new RechercheAmisAdapter.OnItemClickListener() {
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
        params.addParameter("uid", USER.getUid());
        params.addParameter("limit", 10); //fix a limit to 10 users

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

    public void createFragment(){
        loading = rootView.findViewById(R.id.search_loading);
        buttonBack.setVisibility(View.GONE);
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

    }

    private void configureSwipeRefreshLayout(){
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createFragment();
            }
        });
    }


    /*private void updateUI(){
        // 3 - Stop refreshing and clear actual list of users

        adapter.notifyDataSetChanged();
        createFragment();
    }*/

    private void addFilters() {

        final List<ItemFiltre> filtres = new ArrayList<>();

        filtres.add(new ItemFiltre("TC"));
        filtres.add(new ItemFiltre("MMI"));
        filtres.add(new ItemFiltre("INFO"));
        filtres.add(new ItemFiltre("GB"));
        filtres.add(new ItemFiltre("LP"));

        mAdapterFiltre = new ItemFiltreAdapter(filtres);

        mRecyclerViewFiltre = rootView.findViewById(R.id.recyclerViewFiltre);
        mLayoutManagerFiltre = new LinearLayoutManager(
                rootView.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        mRecyclerViewFiltre.setLayoutManager(mLayoutManagerFiltre);
        mRecyclerViewFiltre.setAdapter(mAdapterFiltre);

        mAdapterFiltre.setOnItemClickListener(new ItemFiltreAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                for (int i = 0; i < filtres.size(); i++){
                    if(i != position)
                        filtres.get(i).setBackground(R.drawable.custom_input);
                }

                if(filtres.get(position).getBackground() == R.color.colorPrimary){
                    filtres.get(position).setBackground(R.drawable.custom_input);
                    //QUERY
                }
                else{
                    filtres.get(position).setBackground(R.color.colorPrimary);
                    for (int i = 0; i < friends.size(); i++){
                        Log.w("filtres", "Friends : " + friends.get(i));
                    }
                    //QUERY
                }
                mAdapterFiltre.notifyDataSetChanged();
            }
            // adapterUsers(new FirestoreRecyclerOptions.Builder<User>().setQuery(queryFiltreLP, User.class).build());
        });
    }


    public void showPopUpProfil(User u) {

        PopupProfilFragment.USER_LOAD = u;
        PopupProfilFragment.button = R.drawable.ic_baseline_add_circle_outline_24;

        PopupProfilFragment.type = "recherche";
        navController.navigate(R.id.action_navigation_recherche_amis_to_navigation_popup_profil);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onResume(){
        super.onResume();
    }


}