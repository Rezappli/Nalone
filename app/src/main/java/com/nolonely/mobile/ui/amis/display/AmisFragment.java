package com.nolonely.mobile.ui.amis.display;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.nolonely.mobile.R;
import com.nolonely.mobile.adapter.user.MyFriendsAdapter;
import com.nolonely.mobile.bdd.json.JSONController;
import com.nolonely.mobile.bdd.json.JSONFragment;
import com.nolonely.mobile.bdd.json.JSONObjectCrypt;
import com.nolonely.mobile.listeners.JSONArrayListener;
import com.nolonely.mobile.listeners.JSONObjectListener;
import com.nolonely.mobile.objects.User;
import com.nolonely.mobile.ui.message.ChatActivityFriend;
import com.nolonely.mobile.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.nolonely.mobile.util.Constants.USER;

public class AmisFragment extends JSONFragment {

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

    private void createFragment() {
        resultat = rootView.findViewById(R.id.resultatText_amis);
        resultat.setVisibility(View.GONE);
        linearSansMesAmis = rootView.findViewById(R.id.linearSansMesAmis);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewMesAmis);
        final SwipeRefreshLayout mSwipeRefreshLayout = rootView.findViewById(R.id.AmisSwipeRefreshLayout);
        loading = rootView.findViewById(R.id.loading);
        ImageView imagePerson = rootView.findViewById(R.id.imagePerson);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createFragment();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        launchJSONCall(true);
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
        mAdapter.setOnItemClickListener(new MyFriendsAdapter.OnItemClickListener() {
            @Override
            public void onDisplayClick(int position) {
                showProfil(friends.get(position));
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
                Intent intent = new Intent(getContext(), ChatActivityFriend.class);
                intent.putExtra("user", friends.get(position));
                intent.putExtra("newChat", false);
                startActivity(intent);
            }
        });
        this.mRecyclerView.setAdapter(mAdapter);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.mRecyclerView.setLayoutManager(llm);

    }

    public void showProfil(User u) {
        Intent intent = new Intent(getContext(), InfoUserActivity.class);
        intent.putExtra("user", u);
        intent.putExtra("isFriend", true);
        startActivity(intent);
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
        createFragment();
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void doInHaveInternetConnection() {
        getFriends();
    }

    public void search(String query) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}