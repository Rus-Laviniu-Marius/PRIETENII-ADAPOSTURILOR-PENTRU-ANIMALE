package com.pet.shelter.friends.fragments.bottom_app_bar.pets.tabs.details;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pet.shelter.friends.pets.sheltered.ShelteredPetDetailsActivity;

public class ShelteredPetDetailsViewPager2Adapter extends FragmentStateAdapter {

    public ShelteredPetDetailsViewPager2Adapter(@NonNull ShelteredPetDetailsActivity shelteredPetDetailsActivity) {
        super(shelteredPetDetailsActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ShelteredPetDetailsPetTabFragment();
            case 1:
                return new ShelteredPetDetailsShelterTabFragment();
            default:
                return new ShelteredPetDetailsPetTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }



}