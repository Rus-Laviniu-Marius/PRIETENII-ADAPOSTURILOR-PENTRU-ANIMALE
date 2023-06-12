package com.pet.shelter.friends.news.fragments.bottom_navigation_bar.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class HomeTabLayoutViewPager2Adapter extends FragmentStateAdapter {
    public HomeTabLayoutViewPager2Adapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new BottomNavigationBarHomePetsNewsTabFragment();
            case 1:
                return new BottomNavigationBarHomeVolunteerNewsTabFragment();
            case 2:
                return new BottomNavigationBarHomeDonationNewsTabFragment();
            case 3:
                return new BottomNavigationBarHomeSupportNewsTabFragment();
            case 4:
                return new BottomNavigationBarHomeCaringNewsTabFragment();
            default:
                return new BottomNavigationBarHomePetsNewsTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
