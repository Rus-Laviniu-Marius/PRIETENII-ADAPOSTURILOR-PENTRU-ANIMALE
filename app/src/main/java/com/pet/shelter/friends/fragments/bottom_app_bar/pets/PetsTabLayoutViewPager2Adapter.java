package com.pet.shelter.friends.fragments.bottom_app_bar.pets;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pet.shelter.friends.fragments.bottom_app_bar.pets.tabs.BottomAppBarPetsAbandonedTabFragment;
import com.pet.shelter.friends.fragments.bottom_app_bar.pets.tabs.BottomAppBarPetsFavoritesTabFragment;
import com.pet.shelter.friends.fragments.bottom_app_bar.pets.tabs.BottomAppBarPetsLostTabFragment;
import com.pet.shelter.friends.fragments.bottom_app_bar.pets.tabs.BottomAppBarPetsShelteredTabFragment;

public class PetsTabLayoutViewPager2Adapter extends FragmentStateAdapter {

    public PetsTabLayoutViewPager2Adapter(@NonNull BottomAppBarPetsFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new BottomAppBarPetsShelteredTabFragment();
            case 1:
                return new BottomAppBarPetsAbandonedTabFragment();
            case 2:
                return new BottomAppBarPetsLostTabFragment();
            case 3:
                return new BottomAppBarPetsFavoritesTabFragment();
            default:
                return new BottomAppBarPetsShelteredTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}