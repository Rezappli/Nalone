package com.nolonely.mobile.ui.evenements.display;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nolonely.mobile.R;

public class PlanningFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PlanningAdapter planningAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planning, container, false);
        // Inflate the layout for this fragmentù
        tabLayout = view.findViewById(R.id.tab_layout_planning);
        viewPager = view.findViewById(R.id.view_pager_planning);

        planningAdapter = new PlanningAdapter(getChildFragmentManager());
        planningAdapter.addFragment(new PlanningRegistrationsFragment(), getString(R.string.title_planning_registrations));
        planningAdapter.addFragment(new PlanningCreationsFragment(), getString(R.string.title_planning_creations));
        viewPager.setAdapter(planningAdapter);

        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}