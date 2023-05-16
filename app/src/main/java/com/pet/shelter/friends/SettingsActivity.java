package com.pet.shelter.friends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private ImageView backImageView, nextContentAccountImageView;
    private CircleImageView userProfileCircleImageView;
    private TextView userNameTextView, activeSelectedLanguageTextView, notificationsTextView, helpTextView;
    private SwitchCompat darkOrLightModeSwitchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialization();

        setOnClickListeners();
    }

    private void initialization() {
        backImageView = findViewById(R.id.settingsScreenTopBarBack_imageView);
        userProfileCircleImageView = findViewById(R.id.settingsScreenContentAccountUserProfile_circleImageView);
        userNameTextView = findViewById(R.id.settingsScreenContentAccountUserName_textView);
        nextContentAccountImageView = findViewById(R.id.settingsScreenContentAccountNext_imageView);
        activeSelectedLanguageTextView = findViewById(R.id.settingsScreenContentSettingsActiveSelectedLanguage_textView);
        notificationsTextView = findViewById(R.id.settingsScreenContentSettingsNotifications_textView);
        darkOrLightModeSwitchCompat = findViewById(R.id.settingsScreenContentSettingsDarkOrLightMode_switchCompat);
        helpTextView = findViewById(R.id.settingsScreenContentSettingsHelp_textView);
    }

    private void setOnClickListeners() {
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }


}