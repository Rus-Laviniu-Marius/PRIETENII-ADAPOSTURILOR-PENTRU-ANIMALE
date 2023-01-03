package com.pet.shelter.friends.adoption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pet.shelter.friends.R;

public class AdoptPetActivity extends AppCompatActivity {

    private TextView completeContactInformation, completeFamilyAndHousingInformation,
            completeOtherPetsInformation, completeVeterinarianInformation,
            completeAboutThePetInformation, completePersonalReferencesInformation;
    private ImageView buttonNext, buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt_pet);

        completeContactInformation = findViewById(R.id.completeContactInformation_textView);
        completeFamilyAndHousingInformation = findViewById(R.id.completeFamilyAndHousingInformation_textView);
        completeOtherPetsInformation = findViewById(R.id.completeOtherPetsInformation_textView);
        completeVeterinarianInformation = findViewById(R.id.completeVeterinarianInformation_textView);
        completeAboutThePetInformation = findViewById(R.id.completeAboutThePetInformation_textView);
        completePersonalReferencesInformation = findViewById(R.id.completePersonalReferencesInformation_textView);
        buttonBack = findViewById(R.id.adoptPetBack_button);
        buttonNext = findViewById(R.id.adoptPetNext_button);

        completeContactInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdoptPetActivity.this, CompleteContactInformationActivity.class);
                startActivity(intent);
            }
        });

        completeFamilyAndHousingInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdoptPetActivity.this, CompleteFamilyAndHousingInformationActivity.class);
                startActivity(intent);
            }
        });

        completeOtherPetsInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdoptPetActivity.this, CompleteOtherPetsInformationActivity.class);
                startActivity(intent);
            }
        });

        completeVeterinarianInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdoptPetActivity.this, CompleteVeterinarianInformationActivity.class);
                startActivity(intent);
            }
        });

        completeAboutThePetInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdoptPetActivity.this, CompleteAboutThePetInformationActivity.class);
                startActivity(intent);
            }
        });

        completePersonalReferencesInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdoptPetActivity.this, CompletePersonalReferencesInformationActivity.class);
                startActivity(intent);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdoptPetActivity.this, CompleteContactInformationActivity.class);
                startActivity(intent);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdoptPetActivity.this, SeeListOfPetsActivity.class);
                startActivity(intent);
            }
        });

    }
}