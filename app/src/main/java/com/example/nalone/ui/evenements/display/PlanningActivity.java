package com.example.nalone.ui.evenements.display;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.nalone.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class PlanningActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    PlanningAdapter planningAdapter;

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

    private class PlanningAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String> stringArrayList = new ArrayList<>();

        public void addFragment(Fragment fragment, String s) {
            fragmentArrayList.add(fragment);
            stringArrayList.add(s);

        }

        public PlanningAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return stringArrayList.get(position);
        }
    }
}