package com.nolonely.mobile.ui.message.display;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nolonely.mobile.R;

import java.util.List;

public class MessagesAmisFragment extends Fragment {

    private SearchView search_bar;

    private NavController navController;
    private View rootView;
    private RecyclerView mRecyclerView;
    private String search = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView addMessage;
    private List<String> uid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_messages_amis, container, false);

        createFragment();

        return rootView;
    }

    private void createFragment() {

    }
}