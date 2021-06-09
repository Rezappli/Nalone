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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planning, container, false);
        // Inflate the layout for this fragmentù
        TabLayout tabLayout = view.findViewById(R.id.tab_layout_planning);
        ViewPager viewPager = view.findViewById(R.id.view_pager_planning);

        planningRegistrationsFragment = new PlanningRegistrationsFragment();
        PlanningAdapter planningAdapter = new PlanningAdapter(getChildFragmentManager());
        planningAdapter.addFragment(planningRegistrationsFragment, getString(R.string.title_planning_registrations));
        planningAdapter.addFragment(new PlanningCreationsFragment(), getString(R.string.title_planning_creations));
        viewPager.setAdapter(planningAdapter);

        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        planningRegistrationsFragment.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (planningRegistrationsFragment.handler != null) {
            planningRegistrationsFragment.onResume();
        }
    }
}