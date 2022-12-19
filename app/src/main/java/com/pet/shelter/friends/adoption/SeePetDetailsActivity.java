package com.pet.shelter.friends.adoption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pet.shelter.friends.R;

public class SeePetDetailsActivity extends AppCompatActivity {

    private RelativeLayout mainContainer;
    private ImageView petImage;
    private TextView petName, petAge, petWeight, petLocation, petSize, petBreed, petGender,
            petDescription, veterinarianData, descriptionTextView;
    private Button adoptPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_pet_details);

        mainContainer = findViewById(R.id.petDetailsContainer_relativeLayout);
        petImage = findViewById(R.id.petDetailsContentPet_imageView);
        petName = findViewById(R.id.petDetailsContentName_textView);
        petAge = findViewById(R.id.petDetailsContentAge_textView);
        petWeight = findViewById(R.id.petDetailsContentWeight_textView);
        petLocation = findViewById(R.id.petDetailsContentLocation_textView);
        petSize = findViewById(R.id.petDetailsContentSize_textView);
        petBreed = findViewById(R.id.petDetailsContentBreed_textView);
        petGender = findViewById(R.id.petDetailsContentGender_textView);
        petDescription = findViewById(R.id.petDetailsDescriptionContent_textView);
        veterinarianData = findViewById(R.id.petDetailsContentVeterinarianData_textView);
        descriptionTextView = findViewById(R.id.petDetailsDescription_textView);
        adoptPet = findViewById(R.id.petDetailsAdopt_button);

        Bundle bundle = getIntent().getExtras();

        int color = bundle.getInt("backgroundColor", 0);
        String age = "" + bundle.getInt("petAge") + " years";
        String weight = "" + bundle.getDouble("petWeight") + " KG";

        mainContainer.setBackgroundColor(color);
        petImage.setImageResource(bundle.getInt("imageId", 0));
        petName.setText(bundle.getString("petName"));
        petAge.setText(age);
        petWeight.setText(weight);
        petLocation.setText(bundle.getString("petLocation"));
        petSize.setText(bundle.getString("petSize"));
        petBreed.setText(bundle.getString("petBreed"));
        petGender.setText(bundle.getString("petGender"));
        petDescription.setText(bundle.getString("petDescription"));

        petName.setTextColor(color);
        petAge.setTextColor(color);
        petWeight.setTextColor(color);
        petLocation.setTextColor(color);
        petSize.setTextColor(color);
        petBreed.setTextColor(color);
        petGender.setTextColor(color);
        petDescription.setTextColor(color);
        descriptionTextView.setTextColor(color);
        veterinarianData.setTextColor(color);
        adoptPet.setBackgroundColor(color);

        adoptPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeePetDetailsActivity.this, AdoptPetActivity.class);
                startActivity(intent);
            }
        });
    }
}