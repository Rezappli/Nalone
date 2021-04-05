package com.example.nalone.ui.evenements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.nalone.adapter.SectionPageAdapter;
import com.example.nalone.R;
import com.example.nalone.ui.evenements.display.MapFragment;
import com.example.nalone.ui.evenements.display.MesEvenementsListFragment;
import com.google.android.material.tabs.TabLayout;

public class EvenementsFragment extends Fragment {

    private View myFragment;

    public static ViewPager viewPager;
    private TabLayout tabLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        myFragment = inflater.inflate(R.layout.fragment_evenements, container, false);

        viewPager = myFragment.findViewById(R.id.viewPagerEvenement);
        tabLayout = myFragment.findViewById(R.id.tabLayoutEvenement);

        return myFragment;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceBundle){
        super.onActivityCreated(savedInstanceBundle);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_map_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_mesevent);
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPageAdapter adapter = new SectionPageAdapter(getChildFragmentManager(), 0);

        adapter.addFragment(new MapFragment());
        adapter.addFragment(new MesEvenementsListFragment());

        viewPager.setAdapter(adapter);
    }


}