package com.pet.shelter.friends;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pet.shelter.friends.adoption.FilterPetPreferencesActivity;

public class HomeActivity extends AppCompatActivity {

    private TextView bottomBarHome, bottomBarAdopt, bottomBarDonate, bottomBarProfile;
    private RelativeLayout contentTopLeftAdopt, contentTopRightSponsor, contentBottomLeftVolunteer, contentBottomRightMaterialSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomBarHome = findViewById(R.id.homeScreenBottomBarHome_textView);
        bottomBarAdopt = findViewById(R.id.homeScreenBottomBarAdopt_textView);
        bottomBarDonate = findViewById(R.id.homeScreenBottomBarDonate_textView);
        bottomBarProfile = findViewById(R.id.homeScreenBottomBarProfile_textView);
        contentTopLeftAdopt = findViewById(R.id.homeScreenContentTopLeft_button);
        contentTopRightSponsor = findViewById(R.id.homeScreenContentTopRight_button);
        contentBottomLeftVolunteer = findViewById(R.id.homeScreenContentBottomLeft_button);
        contentBottomRightMaterialSupport = findViewById(R.id.homeScreenContentBottomRight_button);

        bottomBarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadScreen();
            }
        });

        bottomBarAdopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToFilterActivity();
            }
        });

        contentTopLeftAdopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToFilterActivity();
            }
        });
    }

    private void reloadScreen() {
        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private void sendUserToFilterActivity() {
        Intent intent = new Intent(HomeActivity.this, FilterPetPreferencesActivity.class);
        startActivity(intent);
    }
}