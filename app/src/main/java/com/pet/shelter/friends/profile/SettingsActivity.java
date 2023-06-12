package com.pet.shelter.friends.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pet.shelter.friends.R;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private MaterialToolbar materialToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

//        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.tertiary_90));

        initialization();

        setOnClickListeners();
    }

    private void initialization() {
        materialToolbar = findViewById(R.id.settings_materialToolbar);
    }

    private void setOnClickListeners() {
        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ViewProfileActivity.class));
            }
        });
    }


}