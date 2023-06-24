package com.pet.shelter.friends.pets.adoption;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.pets.sheltered.ShelteredPetDetailsActivity;

public class AdoptionApplicationParagraphsActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adoption_application_paragraphs);

        MaterialToolbar materialToolbar = findViewById(R.id.petAdoptionApplication_materialToolbar);
        constraintLayout = findViewById(R.id.petAdoptionApplication_constraintLayout);
        MaterialButton aboutPetAdopter = findViewById(R.id.petAdoptionApplicationAboutPetAdopterInformationParagraph_materialButton);
        MaterialButton familyAndHousehold = findViewById(R.id.petAdoptionApplicationFamilyAndHouseholdInformationParagraph_materialButton);
        MaterialButton otherOwnedPets = findViewById(R.id.petAdoptionApplicationOtherOwnedPetsInformationParagraph_materialButton);
        MaterialButton veterinarian = findViewById(R.id.petAdoptionApplicationVeterinarianInformationParagraph_materialButton);
        MaterialButton wishedPetDetails = findViewById(R.id.petAdoptionApplicationAboutWishedPetInformationParagraph_materialButton);
        MaterialButton personalReferences = findViewById(R.id.petAdoptionApplicationPersonalReferencesInformationParagraph_materialButton);

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdoptionApplicationParagraphsActivity.this, ShelteredPetDetailsActivity.class);
                intent.putExtra("petName", getIntent().getStringExtra("petName"));
                intent.putExtra("petType", getIntent().getStringExtra("petType"));
                intent.putExtra("petBreed", getIntent().getStringExtra("petBreed"));
                intent.putExtra("petAge", getIntent().getStringExtra("petAge"));
                intent.putExtra("petSize", getIntent().getStringExtra("petSize"));
                intent.putExtra("petSex", getIntent().getStringExtra("petSex"));
                intent.putExtra("petDescription", getIntent().getStringExtra("petDescription"));
                intent.putExtra("petImage1DownloadLink", getIntent().getStringExtra("petImage1DownloadLink"));
                intent.putExtra("spayedOrNeutered", getIntent().getStringExtra("spayedOrNeutered"));
                intent.putExtra("dewormed", getIntent().getStringExtra("dewormed"));
                intent.putExtra("vaccines", getIntent().getStringExtra("vaccines"));
                intent.putExtra("fitForChildren", getIntent().getStringExtra("fitForChildren"));
                intent.putExtra("fitForGuarding", getIntent().getStringExtra("fitForGuarding"));
                intent.putExtra("friendlyWithPets", getIntent().getStringExtra("friendlyWithPets"));
                intent.putExtra("isFavorite", getIntent().getStringExtra("isFavorite"));
                startActivity(intent);
                finish();
            }
        });

        aboutPetAdopter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdoptionApplicationParagraphsActivity.this, CompleteAboutPetAdopterInformationActivity.class);
                intent.putExtra("petName", getIntent().getStringExtra("petName"));
                intent.putExtra("petImage1DownloadLink", getIntent().getStringExtra("petImage1DownloadLink"));
                startActivity(intent);
                finish();
            }
        });

        familyAndHousehold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar
                        .make(constraintLayout, "Not now", Snackbar.LENGTH_LONG)
                        .setText("Please complete the about pet adopter information")
                        .setAction("Dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                snackbar.show();
            }
        });

        otherOwnedPets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar
                        .make(constraintLayout, "Not now", Snackbar.LENGTH_LONG)
                        .setText("Please complete the about pet adopter information")
                        .setAction("Dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                snackbar.show();
            }
        });

        veterinarian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar
                        .make(constraintLayout, "Not now", Snackbar.LENGTH_LONG)
                        .setText("Please complete the about pet adopter information")
                        .setAction("Dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                snackbar.show();
            }
        });

        wishedPetDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar
                        .make(constraintLayout, "Not now", Snackbar.LENGTH_LONG)
                        .setText("Please complete the about pet adopter information")
                        .setAction("Dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                snackbar.show();
            }
        });

        personalReferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar
                        .make(constraintLayout, "Not now", Snackbar.LENGTH_LONG)
                        .setText("Please complete the about pet adopter information")
                        .setAction("Dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                snackbar.show();
            }
        });
    }
}