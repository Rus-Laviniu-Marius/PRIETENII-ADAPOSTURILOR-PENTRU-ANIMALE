package com.pet.shelter.friends;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class WhoAreYouActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_are_you);

        MaterialButton userMaterialButton = findViewById(R.id.whoAreYouUser_materialButton);
        MaterialButton shelterAdminMaterialButton = findViewById(R.id.whoAreYouShelterAdmin_materialButton);
        MaterialButton petTrainerMaterialButton = findViewById(R.id.whoAreYouPetTrainer_materialButton);
        MaterialButton veterinarianMaterialButton = findViewById(R.id.whoAreYouVeterinarian_materialButton);

        userMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WhoAreYouActivity.this, UserHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


    }
}