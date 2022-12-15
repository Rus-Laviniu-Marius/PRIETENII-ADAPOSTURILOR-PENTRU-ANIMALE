package com.pet.shelter.friends.adoption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pet.shelter.friends.R;

public class SeePetDetailsActivity extends AppCompatActivity {

    private Button adoptPet;
    private TextView petName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_pet_details);

        petName = findViewById(R.id.petDetailsContentName_textView);

        Intent intent = getIntent();
        String name = intent.getStringExtra("petName");
        petName.setText(name);

        adoptPet = findViewById(R.id.petDetailsAdopt_button);
        adoptPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeePetDetailsActivity.this, AdoptPetActivity.class);
                startActivity(intent);
            }
        });
    }
}