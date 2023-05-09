package com.pet.shelter.friends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.pet.shelter.friends.fragments.bottom_navigation_bar.BottomNavigationBarSupportFragment;
import com.pet.shelter.friends.fragments.bottom_navigation_bar.home.BottomNavigationBarHomeFragment;
import com.pet.shelter.friends.fragments.bottom_navigation_bar.BottomNavigationBarVolunteerFragment;
import com.pet.shelter.friends.fragments.bottom_navigation_bar.BottomNavigationBarPetsFragment;
import com.pet.shelter.friends.fragments.bottom_navigation_bar.BottomNavigationBarDonationsFragment;

public class UserHomeActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private final BottomNavigationBarHomeFragment bottomNavigationBarHomeFragment = new BottomNavigationBarHomeFragment();
    private final BottomNavigationBarPetsFragment bottomNavigationBarPetsFragment = new BottomNavigationBarPetsFragment();
    private final BottomNavigationBarDonationsFragment bottomNavigationBarDonationsFragment = new BottomNavigationBarDonationsFragment();
    private final BottomNavigationBarVolunteerFragment bottomNavigationBarVolunteerFragment = new BottomNavigationBarVolunteerFragment();
    private final BottomNavigationBarSupportFragment bottomNavigationBarSupportFragment = new BottomNavigationBarSupportFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.userHomeScreen_bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.bottomNavigationBar_home);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.bottomNavigationBar_home) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.userHomeScreenHomeFragment_frameLayout, bottomNavigationBarHomeFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.bottomNavigationBar_pets) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.userHomeScreenHomeFragment_frameLayout, bottomNavigationBarPetsFragment)
                    .commit();
            return true;
        } if (item.getItemId() == R.id.bottomNavigationBar_donation) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.userHomeScreenHomeFragment_frameLayout, bottomNavigationBarDonationsFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.bottomNavigationBar_volunteer) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.userHomeScreenHomeFragment_frameLayout, bottomNavigationBarVolunteerFragment)
                    .commit();
            return true;
        } if (item.getItemId() == R.id.bottomNavigationBar_support) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.userHomeScreenHomeFragment_frameLayout, bottomNavigationBarSupportFragment)
                    .commit();
            return true;
        }

        return false;
    }
}