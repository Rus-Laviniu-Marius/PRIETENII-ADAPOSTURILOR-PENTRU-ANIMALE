package com.pet.shelter.friends.pets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.pet.shelter.friends.R;

import java.util.List;

public class FilterActivity extends AppCompatActivity {

    private MaterialToolbar materialToolbar;
    private Chip dogCategory, catCategory, allCategory;
    private ChipGroup categoryChipGroup, sexChipGroup, sizeChipGroup, ageChipGroup, friendlyChipGroup,
            fitForGuardingChipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        materialToolbar = findViewById(R.id.filter_materialToolbar);
        dogCategory = findViewById(R.id.filterCategoryDog_chip);
        catCategory = findViewById(R.id.filterCategoryCat_chip);
        allCategory = findViewById(R.id.filterCategoryAll_chip);
        categoryChipGroup = findViewById(R.id.filterCategory_chipGroup);
        sexChipGroup = findViewById(R.id.filterSex_chipGroup);
        sizeChipGroup = findViewById(R.id.filterSize_chipGroup);
        ageChipGroup = findViewById(R.id.filterAge_chipGroup);
        friendlyChipGroup = findViewById(R.id.filterFriendly_chipGroup);
        fitForGuardingChipGroup = findViewById(R.id.filterFitForGuarding_chipGroup);

        categoryChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                Chip chip = group.findViewById(checkedIds.get(0));

            }
        });
    }
}