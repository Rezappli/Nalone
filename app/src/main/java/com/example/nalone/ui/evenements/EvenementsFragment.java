package com.example.nalone.ui.evenements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.nalone.Adapter.SectionPageAdapter;
import com.example.nalone.R;
import com.example.nalone.ui.evenements.display.EvenementsListFragment;
import com.example.nalone.ui.evenements.display.MapFragment;
import com.google.android.material.tabs.TabLayout;

public class EvenementsFragment extends Fragment {

    private com.example.nalone.ui.evenements.EvenementsViewModel EvenementsViewModel;

    View myFragment;

    ViewPager viewPager;
    TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.round_group_24,
            R.drawable.round_group_24,

    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        EvenementsViewModel =
                ViewModelProviders.of(this).get(EvenementsViewModel.class);
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
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPageAdapter adapter = new SectionPageAdapter(getChildFragmentManager(), 0);

        adapter.addFragment(new MapFragment(), "La carte des évènements", R.drawable.add_photo);
        adapter.addFragment(new EvenementsListFragment(), "Les évènements", R.drawable.custom_adress_focused);
        //adapter.addFragment(new Fragment_3(), "Mes invitations");

        viewPager.setAdapter(adapter);
    }


}