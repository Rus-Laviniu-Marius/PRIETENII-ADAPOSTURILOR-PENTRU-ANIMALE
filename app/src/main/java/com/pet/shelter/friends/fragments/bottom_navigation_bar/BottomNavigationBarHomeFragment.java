package com.pet.shelter.friends.fragments.bottom_navigation_bar;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.pet.shelter.friends.R;

import java.util.Objects;

public class BottomNavigationBarHomeFragment extends Fragment {

    public BottomNavigationBarHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_bottom_navigation_bar_home, container, false);

        // Inflate the layout for this fragment
        return layout;


    }
}

