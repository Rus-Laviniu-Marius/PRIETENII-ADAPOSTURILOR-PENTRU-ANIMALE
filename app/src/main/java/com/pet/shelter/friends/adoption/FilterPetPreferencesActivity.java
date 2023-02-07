package com.pet.shelter.friends.adoption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pet.shelter.friends.R;

import java.util.Objects;

public class FilterPetPreferencesActivity extends AppCompatActivity {

    private Button skipFiltersButton, searchFiltersButton;
    private LinearLayout filterSizeLinearLayout, filterAgeLinearLayout, filterSexLinearLayout,
                        filterFitForChildrenLinearLayout, filterFitForGuardingLinearLayout;
    private RadioGroup sizeFilterRadioGroup, ageFilterRadioGroup, sexFilterRadioGroup,
            fitForChildrenFilterRadioGroup, fitForGuardingFilterRadioGroup;
    private RadioButton smallSize, mediumSize, bigSize, young, middleAge, old, female, male,
            notFitForChildren, fitForChildren, notFitForGuarding, fitForGuarding;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference filtersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_pet_preferences);

        initialization();

        setOnClickListeners();

        setOnCheckedChangeListeners();
    }

    private void initialization() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        filtersReference = firebaseDatabase.getReference("filters");

        skipFiltersButton = findViewById(R.id.skipFilter_button);
        searchFiltersButton = findViewById(R.id.searchFilters_button);

        filterSizeLinearLayout = findViewById(R.id.filterSize_linearLayout);
        filterAgeLinearLayout = findViewById(R.id.filterAge_linearLayout);
        filterSexLinearLayout = findViewById(R.id.filterSex_linearLayout);
        filterFitForChildrenLinearLayout = findViewById(R.id.filterFitForChildren_linearLayout);
        filterFitForGuardingLinearLayout = findViewById(R.id.filterFitForGuarding_linearLayout);

        sizeFilterRadioGroup = findViewById(R.id.filterSize_radioGroup);
        ageFilterRadioGroup = findViewById(R.id.filterAge_radioGroup);
        sexFilterRadioGroup = findViewById(R.id.filterSex_radioGroup);
        fitForChildrenFilterRadioGroup = findViewById(R.id.filterFitForChildren_radioGroup);
        fitForGuardingFilterRadioGroup = findViewById(R.id.filterFitForGuarding_radioGroup);

        smallSize = findViewById(R.id.filterSizeSmall_radioButton);
        mediumSize = findViewById(R.id.filterSizeMedium_radioButton);
        bigSize = findViewById(R.id.filterSizeBig_radioButton);

        young = findViewById(R.id.filterAgeYoung_radioButton);
        middleAge = findViewById(R.id.filterMiddleAge_radioButton);
        old = findViewById(R.id.filterAgeOld_radioButton);

        female = findViewById(R.id.filterSexFemale_radioButton);
        male = findViewById(R.id.filterSexMale_radioButton);

        notFitForChildren = findViewById(R.id.filterNotFitForChildren_radioButton);
        fitForChildren = findViewById(R.id.filterFitForChildren_radioButton);

        notFitForGuarding = findViewById(R.id.filterNotFitForGuarding_radioButton);
        fitForGuarding = findViewById(R.id.filterFitForGuarding_radioButton);
    }

    private void setOnCheckedChangeListeners() {
        sizeFilterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (filterAgeLinearLayout.getVisibility() == View.GONE) {
                    filterAgeLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        ageFilterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (filterSexLinearLayout.getVisibility() == View.GONE) {
                    filterSexLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        sexFilterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (filterFitForChildrenLinearLayout.getVisibility() == View.GONE) {
                    filterFitForChildrenLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        fitForChildrenFilterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (filterFitForGuardingLinearLayout.getVisibility() == View.GONE) {
                    filterFitForGuardingLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setOnClickListeners() {
        skipFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFiltersToDefaultValue();
                removeActiveFilters();
                sendToSeeListOfPetsActivity();
            }
        });

        searchFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFiltersToFirebaseRealtimeDatabase();
                sendToSeeListOfPetsActivity();
            }
        });
    }

    private void setFiltersToDefaultValue() {
        String uId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        if (!smallSize.isChecked() || !mediumSize.isChecked() || !bigSize.isChecked()) {
            filtersReference.child(uId).child("size").setValue("don't care");
        }

        if (!young.isChecked() || !middleAge.isChecked() || !old.isChecked()) {
            filtersReference.child(uId).child("age").setValue("don't care");
        }

        if (!female.isChecked() || !male.isChecked()) {
            filtersReference.child(uId).child("sex").setValue("don't care");
        }

        if (!fitForChildren.isChecked() || !notFitForChildren.isChecked() ) {
            filtersReference.child(uId).child("fitForChildren").setValue("don't care");
        }

        if (!fitForGuarding.isChecked() || !notFitForGuarding.isChecked()) {
            filtersReference.child(uId).child("fitForGuarding").setValue("don't care");
        }
    }

    private void removeActiveFilters() {
        String uId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        if (smallSize.isChecked() || mediumSize.isChecked() || bigSize.isChecked() || (!smallSize.isChecked() || !mediumSize.isChecked() || !bigSize.isChecked())) {
            filtersReference.child(uId).child("size").setValue("don't care");
        }

        if (young.isChecked() || middleAge.isChecked() || old.isChecked() || (!young.isChecked() || !middleAge.isChecked() || !old.isChecked())) {
            filtersReference.child(uId).child("age").setValue("don't care");
        }

        if (female.isChecked() || male.isChecked() || (!female.isChecked() || !male.isChecked())) {
            filtersReference.child(uId).child("sex").setValue("don't care");
        }

        if (fitForChildren.isChecked() || notFitForChildren.isChecked() ) {
            filtersReference.child(uId).child("fitForChildren").setValue("don't care");
        }

        if (fitForGuarding.isChecked() || notFitForGuarding.isChecked()) {
            filtersReference.child(uId).child("fitForGuarding").setValue("don't care");
        }
    }

    private void saveFiltersToFirebaseRealtimeDatabase() {
        String uId = "";

        if (firebaseAuth.getCurrentUser() != null) { uId = firebaseAuth.getCurrentUser().getUid(); }

        if (smallSize.isChecked()) {
            filtersReference.child(uId).child("size").setValue("Small size");
        }
        if (mediumSize.isChecked()) {
            filtersReference.child(uId).child("size").setValue("Medium size");
        }
        if (bigSize.isChecked()) {
            filtersReference.child(uId).child("size").setValue("Big size");
        }
        if (!smallSize.isChecked() && !mediumSize.isChecked() && !bigSize.isChecked()) {
            filtersReference.child(uId).child("size").setValue("don't care");
        }

        if (young.isChecked()) {
            filtersReference.child(uId).child("age").setValue("Young");
        }
        if (middleAge.isChecked()) {
            filtersReference.child(uId).child("age").setValue("Middle age");
        }
        if (old.isChecked()) {
            filtersReference.child(uId).child("age").setValue("Old");
        }
        if (!young.isChecked() && !middleAge.isChecked() && !old.isChecked()) {
            filtersReference.child(uId).child("age").setValue("don't care");
        }

        if (female.isChecked()) {
            filtersReference.child(uId).child("sex").setValue("Female");
        }
        if (male.isChecked()) {
            filtersReference.child(uId).child("sex").setValue("Male");
        }
        if (!female.isChecked() && !male.isChecked()) {
            filtersReference.child(uId).child("sex").setValue("don't care");
        }

        if (fitForChildren.isChecked()) {
            filtersReference.child(uId).child("fitForChildren").setValue("Fit for children");
        }
        if (notFitForChildren.isChecked()) {
            filtersReference.child(uId).child("fitForChildren").setValue("Not fit for children");
        }
        if (!fitForChildren.isChecked() && !notFitForChildren.isChecked()) {
            filtersReference.child(uId).child("fitForChildren").setValue("don't care");
        }

        if (fitForGuarding.isChecked()) {
            filtersReference.child(uId).child("fitForGuarding").setValue("Fit for guarding");
        }
        if (notFitForGuarding.isChecked()) {
            filtersReference.child(uId).child("fitForGuarding").setValue("Not fit for guarding");
        }
        if (!fitForGuarding.isChecked() && !notFitForGuarding.isChecked()) {
            filtersReference.child(uId).child("fitForGuarding").setValue("don't care");
        }

    }

    private void sendToSeeListOfPetsActivity() {
        Intent intent = new Intent(FilterPetPreferencesActivity.this, SeeListOfPetsActivity.class);
        startActivity(intent);
    }
}