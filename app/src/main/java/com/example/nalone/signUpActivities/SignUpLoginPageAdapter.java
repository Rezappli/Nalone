package com.example.nalone.signUpActivities;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SignUpLoginPageAdapter extends FragmentPagerAdapter {


    //Default Constructor
    public SignUpLoginPageAdapter(FragmentManager mgr) {
        super(mgr);
    }

    @Override
    public int getCount() {
        return(3);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: //Page number 1
                return SignUpLoginMailFragment.newInstance();
            case 1: //Page number 2
                return SignUpLoginPhoneFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: //Page number 1
                return "Mail";
            case 1: //Page number 2
                return "Telephone";
            default:
                return null;
        }
    }
}
