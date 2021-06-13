package com.nolonely.mobile.ui.evenements.display;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nolonely.mobile.R;

public class EventFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PlanningAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_event, container, false);
        tabLayout = view.findViewById(R.id.tab_layout_map);
        viewPager = view.findViewById(R.id.view_pager_map);
        adapter = new PlanningAdapter(getChildFragmentManager());
        adapter.addFragment(new EventMapFragment(), getString(R.string.title_home_map));
        adapter.addFragment(new EventListFragment(), getString(R.string.title_home_list));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}