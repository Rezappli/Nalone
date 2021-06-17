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

    private PlanningRegistrationsFragment planningRegistrationsFragment;
    private PlanningCreationsFragment planningCreationsFragment;
    private PlanningAdapter planningAdapter;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planning, container, false);
        // Inflate the layout for this fragmentù
        TabLayout tabLayout = view.findViewById(R.id.tab_layout_planning);
        viewPager = view.findViewById(R.id.view_pager_planning);

        planningRegistrationsFragment = new PlanningRegistrationsFragment();
        planningCreationsFragment = new PlanningCreationsFragment();
        planningAdapter = new PlanningAdapter(getChildFragmentManager());
        planningAdapter.addFragment(planningRegistrationsFragment, getString(R.string.title_planning_registrations));
        planningAdapter.addFragment(planningCreationsFragment, getString(R.string.title_planning_creations));
        viewPager.setAdapter(planningAdapter);


        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    public void search(String query) {
        if (planningAdapter.getItem(viewPager.getCurrentItem()) instanceof PlanningRegistrationsFragment) {
            planningRegistrationsFragment.search(query);
        } else {
            planningCreationsFragment.search(query);
        }
    }
}