package com.example.nalone.ui.evenements;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.example.nalone.adapter.SectionPageAdapter;
import com.example.nalone.R;
import com.example.nalone.ui.evenements.display.EvenementsListFragment;
import com.example.nalone.ui.evenements.display.MapFragment;
import com.example.nalone.ui.evenements.display.MesEvenementsListFragment;
import com.google.android.material.tabs.TabLayout;

public class EvenementsFragment extends Fragment {

    private com.example.nalone.ui.evenements.EvenementsViewModel EvenementsViewModel;

    private View myFragment;

    public static ViewPager viewPager;
    private TabLayout tabLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        EvenementsViewModel =
                ViewModelProviders.of(this).get(EvenementsViewModel.class);
        myFragment = inflater.inflate(R.layout.fragment_evenements, container, false);

        viewPager = myFragment.findViewById(R.id.viewPagerEvenement);
        tabLayout = myFragment.findViewById(R.id.tabLayoutEvenement);


       // tabLayout.getTabAt(2).setIcon(tabIcons[2]);

        return myFragment;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceBundle){
        super.onActivityCreated(savedInstanceBundle);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_map_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_round_search_event);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_mesevent);
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPageAdapter adapter = new SectionPageAdapter(getChildFragmentManager(), 0);

        adapter.addFragment(new MapFragment());
        adapter.addFragment(new EvenementsListFragment());
        adapter.addFragment(new MesEvenementsListFragment());

        viewPager.setAdapter(adapter);
    }


}