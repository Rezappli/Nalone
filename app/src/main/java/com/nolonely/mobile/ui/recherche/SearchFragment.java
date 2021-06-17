package com.nolonely.mobile.ui.recherche;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nolonely.mobile.R;
import com.nolonely.mobile.ui.evenements.display.PlanningAdapter;

public class SearchFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PlanningAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        tabLayout = view.findViewById(R.id.tab_layout_map);
        viewPager = view.findViewById(R.id.view_pager_map);
        adapter = new PlanningAdapter(getChildFragmentManager());
        adapter.addFragment(new SearchEventFragment(), getString(R.string.title_event_search));
        adapter.addFragment(new SearchUserFragment(), getString(R.string.title_friend_serach));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

}
