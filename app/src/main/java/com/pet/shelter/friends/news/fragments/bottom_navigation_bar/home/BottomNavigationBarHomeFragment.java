package com.pet.shelter.friends.news.fragments.bottom_navigation_bar.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pet.shelter.friends.NotificationsActivity;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.profile.CreateUserProfileActivity;
import com.pet.shelter.friends.profile.ViewProfileActivity;

import java.util.Objects;

public class BottomNavigationBarHomeFragment extends Fragment {

    public TabLayout tabLayout;
    public ViewPager2 viewPager2;
    public HomeTabLayoutViewPager2Adapter homeTabLayoutViewPager2Adapter;
    public MaterialToolbar materialToolbar;
    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference profilesReference;

    public BottomNavigationBarHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        profilesReference = firebaseDatabase.getReference("profiles");

        String loggedUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        View layout = inflater.inflate(R.layout.fragment_bottom_navigation_bar_home, container, false);

        tabLayout = layout.findViewById(R.id.bottomNavigationBarHome_tabLayout);
        viewPager2 = layout.findViewById(R.id.bottomNavigationBarHome_viewPager2);
        homeTabLayoutViewPager2Adapter = new HomeTabLayoutViewPager2Adapter(this);
        viewPager2.setAdapter(homeTabLayoutViewPager2Adapter);
        viewPager2.setSaveEnabled(false);
        materialToolbar = layout.findViewById(R.id.userHomeScreenTop_materialToolbar);

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilesReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("userProfiles")) {
                            Intent intent = new Intent(getActivity(), ViewProfileActivity.class);
                            startActivity(intent);
                        } else {
                            startActivity(new Intent(getActivity(), CreateUserProfileActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_open_notifications) {
                    Intent openNotificationsActivityIntent = new Intent(getActivity(), NotificationsActivity.class);
                    startActivity(openNotificationsActivityIntent);
                }
                return true;
            }
        });



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
                        Objects.requireNonNull(tabLayout.getTabAt(position)).select();
                }
                super.onPageSelected(position);
            }
        });

        // Inflate the layout for this fragment
        return layout;
    }
}

