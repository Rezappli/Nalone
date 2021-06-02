package com.nolonely.mobile.ui.evenements.display;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.nolonely.mobile.R;
import com.google.android.material.tabs.TabLayout;

public class PlanningActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PlanningAdapter planningAdapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning);

        tabLayout = findViewById(R.id.tab_layout_planning);
        viewPager = findViewById(R.id.view_pager_planning);

        planningAdapter = new PlanningAdapter(getSupportFragmentManager());
        planningAdapter.addFragment(new PlanningRegistrationsFragment(), getString(R.string.title_planning_registrations));
        planningAdapter.addFragment(new PlanningCreationsFragment(), getString(R.string.title_planning_creations));
        viewPager.setAdapter(planningAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }

}