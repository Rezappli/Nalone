package com.nolonely.mobile.ui.amis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.nolonely.mobile.adapter.SectionPageAdapter;
import com.nolonely.mobile.R;
import com.nolonely.mobile.ui.amis.display.AmisFragment;
import com.nolonely.mobile.ui.amis.display.GroupeFragment;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.tabs.TabLayout;

public class MesAmisFragment extends Fragment {

    private AmisViewModel amisViewModel;
    private Button signOutButton;
    private GoogleSignInClient mGoogleSignInClient;


    View myFragment;

    ViewPager viewPager;
    TabLayout tabLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        amisViewModel =
                ViewModelProviders.of(this).get(AmisViewModel.class);
        myFragment = inflater.inflate(R.layout.fragment_mes_amis, container, false);

        viewPager = myFragment.findViewById(R.id.viewPager);
        tabLayout = myFragment.findViewById(R.id.tabLayout);

        return myFragment;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceBundle){
        super.onActivityCreated(savedInstanceBundle);
        
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_round_people_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_round_groups_24);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPageAdapter adapter = new SectionPageAdapter(getChildFragmentManager(), 0);

        adapter.addFragment(new AmisFragment());
        adapter.addFragment(new GroupeFragment());

        viewPager.setAdapter(adapter);
    }


}