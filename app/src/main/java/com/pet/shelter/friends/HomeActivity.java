package com.pet.shelter.friends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.pet.shelter.friends.news.fragments.bottom_app_bar.BottomAppBarSupportFragment;
import com.pet.shelter.friends.news.fragments.bottom_app_bar.home.BottomAppBarHomeFragment;
import com.pet.shelter.friends.news.fragments.bottom_app_bar.BottomAppBarVolunteerFragment;
import com.pet.shelter.friends.news.fragments.bottom_app_bar.BottomAppBarPetsFragment;
import com.pet.shelter.friends.news.fragments.bottom_app_bar.BottomAppBarDonationsFragment;

public class HomeActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private final BottomAppBarHomeFragment bottomAppBarHomeFragment = new BottomAppBarHomeFragment();
    private final BottomAppBarPetsFragment bottomAppBarPetsFragment = new BottomAppBarPetsFragment();
    private final BottomAppBarDonationsFragment bottomAppBarDonationsFragment = new BottomAppBarDonationsFragment();
    private final BottomAppBarVolunteerFragment bottomAppBarVolunteerFragment = new BottomAppBarVolunteerFragment();
    private final BottomAppBarSupportFragment bottomAppBarSupportFragment = new BottomAppBarSupportFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.userHomeScreen_bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.bottomNavigationBar_home);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.bottomNavigationBar_home) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.userHomeScreenHomeFragment_frameLayout, bottomAppBarHomeFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.bottomNavigationBar_pets) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.userHomeScreenHomeFragment_frameLayout, bottomAppBarPetsFragment)
                    .commit();
            return true;
        } if (item.getItemId() == R.id.bottomNavigationBar_donation) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.userHomeScreenHomeFragment_frameLayout, bottomAppBarDonationsFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.bottomNavigationBar_volunteer) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.userHomeScreenHomeFragment_frameLayout, bottomAppBarVolunteerFragment)
                    .commit();
            return true;
        } if (item.getItemId() == R.id.bottomNavigationBar_support) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.userHomeScreenHomeFragment_frameLayout, bottomAppBarSupportFragment)
                    .commit();
            return true;
        }

        return false;
    }
}