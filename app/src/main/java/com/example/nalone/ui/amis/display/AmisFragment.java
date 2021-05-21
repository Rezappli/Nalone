package com.example.nalone.ui.amis.display;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.adapter.user.MyFriendsAdapter;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONArrayListener;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.objects.User;
import com.example.nalone.ui.message.ChatActivityFriend;
import com.example.nalone.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.ON_FRIENDS_ACTIVITY;
import static com.example.nalone.util.Constants.USER;

public class AmisFragment extends Fragment {

    private SearchView search_bar;
    private NavController navController;
    private View rootView;
    private RecyclerView mRecyclerView;
    private List<User> friends;
    private TextView resultat;

    private LinearLayout linearSansMesAmis;
    private ProgressBar loading;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_amis, container, false);
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createFragment() {
        search_bar = rootView.findViewById(R.id.search_bar_amis);
        resultat = rootView.findViewById(R.id.resultatText_amis);
        resultat.setVisibility(View.GONE);
        linearSansMesAmis = rootView.findViewById(R.id.linearSansMesAmis);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        buttonBack.setVisibility(View.GONE);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewMesAmis);
        final SwipeRefreshLayout mSwipeRefreshLayout = rootView.findViewById(R.id.AmisSwipeRefreshLayout);
        loading = rootView.findViewById(R.id.loading);
        ImageView imagePerson = rootView.findViewById(R.id.imagePerson);


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
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        getFriends();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getFriends() {
        friends = new ArrayList<>();

        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("limit", 10); //fix a limit to 10 users

        JSONController.getJsonArrayFromUrl(Constants.URL_MY_FRIENDS, getContext(), params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                try {
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            friends.add((User) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), User.class));
                        }
                        linearSansMesAmis.setVisibility(View.GONE);

                        configureRecyclerViewAmis();
                    } else {
                        linearSansMesAmis.setVisibility(View.VISIBLE);
                    }

                    loading.setVisibility(View.GONE);
                } catch (JSONException e) {
                    Log.w("Response", "Erreur: " + e.getMessage());
                    Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur: " + volleyError.toString());
                Toast.makeText(getContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configureRecyclerViewAmis() {
        MyFriendsAdapter mAdapter = new MyFriendsAdapter(this.friends);
        this.mRecyclerView.setAdapter(mAdapter);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.mRecyclerView.setLayoutManager(llm);
        mAdapter.setOnItemClickListener(new MyFriendsAdapter.OnItemClickListener() {
            @Override
            public void onDisplayClick(int position) {
                showPopUpProfil(friends.get(position));
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDeleteClick(int position) {
                removeFriend(friends.get(position));
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
                callIntent.setData(Uri.parse("tel:" + friends.get(position).getNumber()));
                startActivity(callIntent);
            }

            @Override
            public void onMessageClick(int position) {
                ChatActivityFriend.USER_LOAD = friends.get(position);
                startActivity(new Intent(getContext(), ChatActivityFriend.class));
            }
        });
    }

    public void showPopUpProfil(User u) {

        PopupProfilFragment.USER_LOAD = u;
        //PopupProfilFragment.button = getResources(0);

        PopupProfilFragment.type = "amis";
        navController.navigate(R.id.action_navigation_amis_to_navigation_popup_profil);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void removeFriend(User user) {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("uid_friend", user.getUid());

        JSONController.getJsonObjectFromUrl(Constants.URL_DELETE_FRIEND, getContext(), params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                Log.w("Response", "Value:" + jsonObject);
                if (jsonObject.length() == 3) {
                    Toast.makeText(getContext(), getResources().getString(R.string.delete_friend), Toast.LENGTH_SHORT).show();
                    createFragment();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    Log.w("Response", "Erreur:" + jsonObject.toString());
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                Log.w("Response", "Erreur:" + volleyError.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
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