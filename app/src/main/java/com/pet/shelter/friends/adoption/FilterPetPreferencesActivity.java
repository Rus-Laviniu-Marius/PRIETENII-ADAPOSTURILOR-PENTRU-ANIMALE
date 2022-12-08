package com.pet.shelter.friends.adoption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.pet.shelter.friends.R;

public class FilterPetPreferencesActivity extends AppCompatActivity {

    private Button skipFilter, searchFilters;
    private RadioButton smallSize, betweenSizeFilter, bigSize, young, middleAge, old, female, male,
            notFitForChildren, fitForChildren, notFitForGuarding, fitForGuarding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_pet_preferences);

        skipFilter = findViewById(R.id.skipFilter_button);
        searchFilters = findViewById(R.id.searchFilters_button);

        smallSize = findViewById(R.id.filterSizeSmall_radioButton);
        betweenSizeFilter = findViewById(R.id.filterSizeBetween_radioButton);
        bigSize = findViewById(R.id.filterSizeBig_radioButton);

        young = findViewById(R.id.filterAgeYoung_radioButton);
        middleAge = findViewById(R.id.filterMiddleAge_radioButton);
        old = findViewById(R.id.filterAgeOld_radioButton);

        female = findViewById(R.id.filterGenderFemale_radioButton);
        male = findViewById(R.id.filterGenderMale_radioButton);

        notFitForChildren = findViewById(R.id.filterNotFitForChildren_radioButton);
        fitForChildren = findViewById(R.id.filterFitForChildren_radioButton);

        notFitForGuarding = findViewById(R.id.filterNotFitForGuarding_radioButton);
        fitForGuarding = findViewById(R.id.filterFitForGuarding_radioButton);

        // TODO: save filters to firebase realtime database when pressing any button

        skipFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToSeeListOfPetsActivity();
            }
        });

        searchFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToSeeListOfPetsActivity();
            }
        });
    }

    private void sendToSeeListOfPetsActivity() {
        Intent intent = new Intent(FilterPetPreferencesActivity.this, SeeListOfPetsActivity.class);
        startActivity(intent);
    }
}