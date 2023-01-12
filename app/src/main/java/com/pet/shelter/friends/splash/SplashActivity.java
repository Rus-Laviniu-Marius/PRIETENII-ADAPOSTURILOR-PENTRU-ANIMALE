package com.pet.shelter.friends.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.pet.shelter.friends.R;
import com.pet.shelter.friends.authentication.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        LinearLayout splashContentLinearLayout = findViewById(R.id.splashScreenContent_linearLayout);
        Animation slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide);
        splashContentLinearLayout.startAnimation(slideAnimation);

        Button proceedButton = findViewById(R.id.proceed_button);

//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                proceedButton.setVisibility(View.VISIBLE);
//            }
//        };
//        Handler.postDelayed(
//                runnable
//        , 1000);

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
