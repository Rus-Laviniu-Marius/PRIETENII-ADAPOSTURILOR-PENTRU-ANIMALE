package com.pet.shelter.friends.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.window.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;

import com.pet.shelter.friends.R;
import com.pet.shelter.friends.authentication.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Button proceedButton = findViewById(R.id.lets_start_materialButton);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
