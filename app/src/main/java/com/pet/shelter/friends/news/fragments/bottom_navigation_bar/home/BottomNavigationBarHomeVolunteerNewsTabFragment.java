package com.pet.shelter.friends.news.fragments.bottom_navigation_bar.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pet.shelter.friends.R;

public class BottomNavigationBarHomeVolunteerNewsTabFragment extends Fragment {

    public BottomNavigationBarHomeVolunteerNewsTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_navigation_bar_home_volunteer_news_tab, container, false);
    }
}