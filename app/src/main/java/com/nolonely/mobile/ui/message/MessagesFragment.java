package com.nolonely.mobile.ui.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nolonely.mobile.R;

import java.util.List;
import java.util.concurrent.Executor;

public class MessagesFragment extends Fragment {


    private SearchView search_bar;

    private NavController navController;
    private View rootView;
    private RecyclerView mRecyclerView;
    private String search = "";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView addMessage;
    private List<String> uid;

    //bio
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_messages_amis, container, false);

        return rootView;
    }

    private void createFragment() {
        search_bar = rootView.findViewById(R.id.search_bar_amis);
        //navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewMessagesAmis);
        mSwipeRefreshLayout = rootView.findViewById(R.id.messageFriendSwipeRefreshLayout);
    }
}