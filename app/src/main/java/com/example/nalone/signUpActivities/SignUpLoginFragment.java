package com.example.nalone.signUpActivities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nalone.R;
import com.google.android.material.tabs.TabLayout;

public class SignUpLoginFragment extends Fragment {

    public static SignUpLoginFragment newInstance() {
        return new SignUpLoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_sign_up_login, container, false);
        ViewPager pager = (ViewPager)view.findViewById(R.id.viewPagerLogin);
        //Set Adapter PageAdapter and glue it together
        pager.setAdapter(new SignUpLoginPageAdapter(getActivity().getSupportFragmentManager()));

        // 1 - Get TabLayout from layout
        TabLayout tabs= (TabLayout)view.findViewById(R.id.tobLayoutLogin);
        // 2 - Glue TabLayout and ViewPager together
        tabs.setupWithViewPager(pager);
        // 3 - Design purpose. Tabs have the same width
        tabs.setTabMode(TabLayout.MODE_FIXED);
        return view;
    }
}