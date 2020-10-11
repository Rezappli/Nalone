package com.example.nalone.ui.amis;

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

import com.example.nalone.Adapter.SectionPageAdapter;
import com.example.nalone.R;
import com.example.nalone.ui.amis.display.AmisFragment;
import com.example.nalone.ui.amis.display.GroupeFragment;
import com.example.nalone.ui.amis.display.Fragment_3;
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

        adapter.addFragment(new AmisFragment(), "Fragment 1");
        adapter.addFragment(new GroupeFragment(), "Fragment 2");
        adapter.addFragment(new Fragment_3(), "Fragment 3");

        viewPager.setAdapter(adapter);
    }


}