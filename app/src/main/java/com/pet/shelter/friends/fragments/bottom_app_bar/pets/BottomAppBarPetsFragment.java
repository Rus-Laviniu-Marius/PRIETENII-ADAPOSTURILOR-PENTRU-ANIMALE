package com.pet.shelter.friends.fragments.bottom_app_bar.pets;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.SearchQueryEvent;
import com.pet.shelter.friends.pets.FilterActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

public class BottomAppBarPetsFragment extends Fragment {

    private SearchView searchView;

    public BottomAppBarPetsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_bottom_app_bar_pets, container, false);

        MaterialToolbar materialToolbar = layout.findViewById(R.id.pets_materialToolbar);
        TabLayout tabLayout = layout.findViewById(R.id.pets_tabLayout);
        ViewPager2 viewPager2 = layout.findViewById(R.id.pets_viewPager2);
        PetsTabLayoutViewPager2Adapter petsTabLayoutViewPager2Adapter = new PetsTabLayoutViewPager2Adapter(this);
        viewPager2.setAdapter(petsTabLayoutViewPager2Adapter);
        viewPager2.setSaveEnabled(false);

        tabLayout.addTab(tabLayout.newTab().setText("Sheltered"));
        tabLayout.addTab(tabLayout.newTab().setText("Abandoned"));
        tabLayout.addTab(tabLayout.newTab().setText("Lost"));
        tabLayout.addTab(tabLayout.newTab().setText("Favorites"));

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_filter) {
                    startActivity(new Intent(getContext(), FilterActivity.class));
                } else if (item.getItemId() == R.id.action_search) {
                    searchView = (SearchView) item.getActionView();
                    if (searchView != null) {
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                EventBus.getDefault().post(new SearchQueryEvent(newText));
                                return false;
                            }
                        });

                    }
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

        return layout;
    }
}