package com.pet.shelter.friends.profile.services;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.pet.shelter.friends.fragments.services.ActiveServicesPetTrainersTabFragment;
import com.pet.shelter.friends.fragments.services.ActiveServicesPetWalkersTabFragment;
import com.pet.shelter.friends.fragments.services.ActiveServicesVeterinariansTabFragment;

public class ActiveServicesViewPager2Adapter extends FragmentStateAdapter {

    public ActiveServicesViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ActiveServicesVeterinariansTabFragment();
            case 1:
                return new ActiveServicesPetTrainersTabFragment();
            case 2:
                return new ActiveServicesPetWalkersTabFragment();
            default:
                return new ActiveServicesVeterinariansTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


}
