package com.pet.shelter.friends.pets.filtering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pet.shelter.friends.R;

import java.util.List;
import java.util.Objects;

public class FilterActivity extends AppCompatActivity {

    private Chip chip;
    private String category, sex, size, age, friendly, guarding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        DatabaseReference filters = FirebaseDatabase.getInstance().getReference("filters");
        String loggedUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        MaterialToolbar materialToolbar = findViewById(R.id.filter_materialToolbar);
        ChipGroup categoryChipGroup = findViewById(R.id.filterCategory_chipGroup);
        ChipGroup sexChipGroup = findViewById(R.id.filterSex_chipGroup);
        ChipGroup sizeChipGroup = findViewById(R.id.filterSize_chipGroup);
        ChipGroup ageChipGroup = findViewById(R.id.filterAge_chipGroup);
        ChipGroup friendlyChipGroup = findViewById(R.id.filterFriendly_chipGroup);
        ChipGroup fitForGuardingChipGroup = findViewById(R.id.filterFitForGuarding_chipGroup);
        MaterialButton materialButton = findViewById(R.id.filter_materialButton);
        categoryChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                chip = group.findViewById(group.getCheckedChipId());
                category = Objects.requireNonNull(String.valueOf(chip.getText()));
                Toast.makeText(FilterActivity.this, category, Toast.LENGTH_SHORT).show();

            }
        });
        sexChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {

            }
        });
        sizeChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
            }
        });
        ageChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {

            }
        });
        friendlyChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {

            }
        });
        fitForGuardingChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {

            }
        });
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                filters.child(loggedUserId).child("category").setValue(category);
//                filters.child(loggedUserId).child("sex").setValue(sex);
//                filters.child(loggedUserId).child("size").setValue(size);
//                filters.child(loggedUserId).child("age").setValue(age);
//                filters.child(loggedUserId).child("friendly").setValue(friendly);
//                filters.child(loggedUserId).child("guarding").setValue(guarding);
            }
        });
        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_delete) {
                    categoryChipGroup.clearCheck();
                    sexChipGroup.clearCheck();
                    sizeChipGroup.clearCheck();
                    ageChipGroup.clearCheck();
                    friendlyChipGroup.clearCheck();
                    fitForGuardingChipGroup.clearCheck();
//                    filters.removeValue();
                }
                return true;
            }
        });
    }
}