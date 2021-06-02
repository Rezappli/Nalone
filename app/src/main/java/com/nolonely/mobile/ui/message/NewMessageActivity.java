package com.nolonely.mobile.ui.message;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.nolonely.mobile.R;
import com.nolonely.mobile.adapter.user.SearchUserAdapter;
import com.nolonely.mobile.json.JSONController;
import com.nolonely.mobile.json.JSONObjectCrypt;
import com.nolonely.mobile.listeners.JSONArrayListener;
import com.nolonely.mobile.objects.User;
import com.nolonely.mobile.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.nolonely.mobile.util.Constants.USER;

public class NewMessageActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMessagesAmis;
    private List<User> friendList;
    private SearchUserAdapter searchUserAdapter;
    private ProgressBar loading;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        friendList = new ArrayList<>();
        recyclerViewMessagesAmis = findViewById(R.id.recyclerViewMessagesAmis);
        searchUserAdapter = new SearchUserAdapter(friendList);
        searchUserAdapter.setOnItemClickListener(position -> {
            friendList.get(position).launchChat(getBaseContext(), this);
        });
        recyclerViewMessagesAmis.setAdapter(searchUserAdapter);
        recyclerViewMessagesAmis.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        loading = findViewById(R.id.loading);
        initList();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initList() {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("limit", 10); //fix a limit to 10 users

        JSONController.getJsonArrayFromUrl(Constants.URL_MY_FRIENDS, getBaseContext(), params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                try {
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            friendList.add((User) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), User.class));
                        }

                        searchUserAdapter.notifyDataSetChanged();
                    } else {
                        //linearSansMesAmis.setVisibility(View.VISIBLE);
                    }

                    loading.setVisibility(View.GONE);
                } catch (JSONException e) {
                    Log.w("Response", "Erreur: " + e.getMessage());
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur: " + volleyError.toString());
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
    }
}