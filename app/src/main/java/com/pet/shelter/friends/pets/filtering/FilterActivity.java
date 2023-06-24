package com.pet.shelter.friends.pets.filtering;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Consumer;

import android.content.Intent;
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
import com.google.android.material.internal.MaterialCheckable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.fragments.bottom_app_bar.pets.tabs.BottomAppBarPetsShelteredTabFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilterActivity extends AppCompatActivity {

    private Chip chip;
    private FilterData filterData;
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

                    filters.removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            categoryChipGroup.clearCheck();
                            sexChipGroup.clearCheck();
                            sizeChipGroup.clearCheck();
                            ageChipGroup.clearCheck();
                            friendlyChipGroup.clearCheck();
                            fitForGuardingChipGroup.clearCheck();
                        }
                    });
                }
                return true;
            }
        });

        filters.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(loggedUserId)) {
                    snapshot = snapshot.child(loggedUserId);
                    filterData = snapshot.getValue(FilterData.class);
                }
                if (filterData != null) {
                    if (filterData.getCategory() != null) {
                        category = filterData.getCategory();
                        for (int i = 0; i < categoryChipGroup.getChildCount(); i++) {
                            Chip categoryChip = (Chip) categoryChipGroup.getChildAt(i);
                            if (categoryChip.getText().toString().matches(category)) {
                                categoryChip.setChecked(true);
                            }
                        }
                    }
                    if (filterData.getSex() != null) {
                        sex = filterData.getSex();
                        for (int i = 0; i < sexChipGroup.getChildCount(); i++) {
                            Chip sexChip = (Chip) sexChipGroup.getChildAt(i);
                            if (sexChip.getText().toString().matches(sex)) {
                                sexChip.setChecked(true);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        categoryChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                chip = group.findViewById(group.getCheckedChipId());
                if (chip != null) {
                    category = Objects.requireNonNull(String.valueOf(chip.getText()));
                }
            }
        });
        sexChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                chip = group.findViewById(group.getCheckedChipId());
                if (chip != null) {
                    sex = Objects.requireNonNull(String.valueOf(chip.getText()));
                }
            }
        });
        sizeChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                chip = group.findViewById(group.getCheckedChipId());
                if (chip != null) {
                    size = Objects.requireNonNull(String.valueOf(chip.getText()));
                }
            }
        });
        ageChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                chip = group.findViewById(group.getCheckedChipId());
                if (chip != null) {
                    age = Objects.requireNonNull(String.valueOf(chip.getText()));
                }
            }
        });
        friendlyChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                chip = group.findViewById(group.getCheckedChipId());
                if (chip != null) {
                    friendly = Objects.requireNonNull(String.valueOf(chip.getText()));
                }
            }
        });
        fitForGuardingChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                chip = group.findViewById(group.getCheckedChipId());
                if (chip != null) {
                    guarding = Objects.requireNonNull(String.valueOf(chip.getText()));
                }
            }
        });


        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filters.child(loggedUserId).child("category").setValue(category);
                filters.child(loggedUserId).child("sex").setValue(sex);
                filters.child(loggedUserId).child("size").setValue(size);
                filters.child(loggedUserId).child("age").setValue(age);
                filters.child(loggedUserId).child("friendly").setValue(friendly);
                filters.child(loggedUserId).child("guarding").setValue(guarding);
                finish();
            }
        });

    }


}