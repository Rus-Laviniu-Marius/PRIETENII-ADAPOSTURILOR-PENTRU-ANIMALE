package com.pet.shelter.friends.fragments.bottom_app_bar.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pet.shelter.friends.fragments.bottom_app_bar.home.tabs.BottomAppBarHomeCaringNewsTabFragment;
import com.pet.shelter.friends.fragments.bottom_app_bar.home.tabs.BottomAppBarHomeDonationNewsTabFragment;
import com.pet.shelter.friends.fragments.bottom_app_bar.home.tabs.BottomAppBarHomePetsNewsTabFragment;
import com.pet.shelter.friends.fragments.bottom_app_bar.home.tabs.BottomAppBarHomeSupportNewsTabFragment;
import com.pet.shelter.friends.fragments.bottom_app_bar.home.tabs.BottomAppBarHomeVolunteerNewsTabFragment;

public class HomeTabLayoutViewPager2Adapter extends FragmentStateAdapter {
    public HomeTabLayoutViewPager2Adapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new BottomAppBarHomePetsNewsTabFragment();
            case 1:
                return new BottomAppBarHomeVolunteerNewsTabFragment();
            case 2:
                return new BottomAppBarHomeDonationNewsTabFragment();
            case 3:
                return new BottomAppBarHomeSupportNewsTabFragment();
            case 4:
                return new BottomAppBarHomeCaringNewsTabFragment();
            default:
                return new BottomAppBarHomePetsNewsTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
