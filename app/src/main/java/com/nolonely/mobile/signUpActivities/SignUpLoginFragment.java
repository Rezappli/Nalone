package com.nolonely.mobile.signUpActivities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.nolonely.mobile.R;
import com.google.android.material.tabs.TabLayout;

public class SignUpLoginFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_login, container, false);
        ViewPager pager = (ViewPager) view.findViewById(R.id.viewPagerLogin);
        //Set Adapter PageAdapter and glue it together
        pager.setAdapter(new SignUpLoginPageAdapter(getChildFragmentManager()));

        // 1 - Get TabLayout from layout
        final TabLayout tabs = (TabLayout) view.findViewById(R.id.tobLayoutLogin);
        // 2 - Glue TabLayout and ViewPager together
        tabs.setupWithViewPager(pager);
        // 3 - Design purpose. Tabs have the same width
        tabs.setTabMode(TabLayout.MODE_FIXED);
        SignUpFragment.isMail = true;
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabs.getSelectedTabPosition() == 0)
                    SignUpFragment.isMail = true;
                else
                    SignUpFragment.isMail = false;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }
}