package com.pet.shelter.friends.news.fragments.bottom_navigation_bar.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.pet.shelter.friends.R;

public class BottomNavigationBarHomeFragment extends Fragment {

    public TabLayout tabLayout;
    public ViewPager2 viewPager2;
    public HomeTabLayoutViewPager2Adapter homeTabLayoutViewPager2Adapter;

    public BottomNavigationBarHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_bottom_navigation_bar_home, container, false);

        tabLayout = layout.findViewById(R.id.bottomNavigationBarHome_tabLayout);
        viewPager2 = layout.findViewById(R.id.bottomNavigationBarHome_viewPager2);
        homeTabLayoutViewPager2Adapter = new HomeTabLayoutViewPager2Adapter(this);
        viewPager2.setAdapter(homeTabLayoutViewPager2Adapter);
        viewPager2.setSaveEnabled(false);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        tabLayout.getTabAt(position).select();
                }
                super.onPageSelected(position);
            }
        });

        // Inflate the layout for this fragment
        return layout;
    }
}

