package com.example.nalone.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.R;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.objects.Evenement;
import com.example.nalone.objects.Group;
import com.example.nalone.objects.User;
import com.example.nalone.ui.amis.display.CreateGroupFragment;
import com.example.nalone.ui.message.ChatActivityFriend;
import com.example.nalone.ui.message.ChatActivityGroup;
import com.example.nalone.util.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;

public class ListAmisFragment extends Fragment {

    private SearchView search_bar;

    private NavController navController;
    private TextView resultat;
    private ArrayList<ItemPerson> items = new ArrayList<>();
    private static String message = "null";
    private View rootView;
    private ProgressBar loading;

    private RecyclerView mRecyclerView;
    private List<String> friends;
    private List<String> groups;
    private List<String> adds;
    private List<String> adds_group;

    private Button valider;
    private int remove, add;

    public static String type;
    public static Evenement EVENT_LOAD;
    public static Group GROUP_LOAD;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list_amis, container, false);
        createFragment();
        return rootView;
    }

    private void createFragment() {
        adds = new ArrayList<>();
        search_bar = rootView.findViewById(R.id.search_bar_amis);
        resultat = rootView.findViewById(R.id.resultatText_amis);
        resultat.setVisibility(View.GONE);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        loading = rootView.findViewById(R.id.amis_loading);
        valider = rootView.findViewById(R.id.validerListAmi);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewAmisAdd);
        remove = R.drawable.ic_baseline_remove_24;
        add = R.drawable.ic_baseline_add_24;
        buttonBack.setVisibility(View.VISIBLE);
    }
}