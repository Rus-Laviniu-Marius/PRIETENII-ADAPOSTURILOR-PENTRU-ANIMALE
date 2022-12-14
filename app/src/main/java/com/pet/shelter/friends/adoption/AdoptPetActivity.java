package com.pet.shelter.friends.adoption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pet.shelter.friends.R;

public class AdoptPetActivity extends AppCompatActivity {

    private Button completeContactInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt_pet);

        completeContactInformation = findViewById(R.id.adoptPetCompleteContactInformation_button);
        completeContactInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdoptPetActivity.this, CompleteContactInformationActivity.class);
                startActivity(intent);
            }
        });

    }
}