package com.pet.shelter.friends.adoption;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pet.shelter.friends.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SeeListOfPetsActivity extends AppCompatActivity {

    private TextView sizeActiveFilter, ageActiveFilter, genderActiveFilter,
            fitForChildrenActiveFilter, fitForGuardingActiveFilter;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference filtersReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_list_of_pets);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        filtersReference = firebaseDatabase.getReference("filters");

        sizeActiveFilter = findViewById(R.id.listOfPetsContentSizeActiveFilter_textView);
        ageActiveFilter = findViewById(R.id.listOfPetsContentAgeActiveFilter_textView);
        genderActiveFilter = findViewById(R.id.listOfPetsContentGenderActiveFilter_textView);
        fitForChildrenActiveFilter = findViewById(R.id.listOfPetsContentFitForChildrenActiveFilter_textView);
        fitForGuardingActiveFilter = findViewById(R.id.listOfPetsContentFitForGuardingActiveFilter_textView);

        String loggedUid = "";
        if (firebaseAuth.getCurrentUser() != null)
            loggedUid = firebaseAuth.getCurrentUser().getUid();
        filtersReference.child(loggedUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String size, age, gender, fitForChildren, fitForGuarding;

                size = Objects.requireNonNull(snapshot.child("size").getValue()).toString();
                if (size.equals("don't care"))
                    sizeActiveFilter.setVisibility(View.GONE);
                else
                    sizeActiveFilter.setText(size);

                age = Objects.requireNonNull(snapshot.child("age").getValue()).toString();
                if (age.equals("don't care"))
                    ageActiveFilter.setVisibility(View.GONE);
                else
                    ageActiveFilter.setText(age);

                gender = Objects.requireNonNull(snapshot.child("gender").getValue()).toString();
                if (gender.equals("don't care"))
                    genderActiveFilter.setVisibility(View.GONE);
                else
                    genderActiveFilter.setText(gender);

                fitForChildren = Objects.requireNonNull(snapshot.child("fitForChildren").getValue()).toString();
                if (fitForChildren.equals("don't care"))
                    fitForChildrenActiveFilter.setVisibility(View.GONE);
                else
                    fitForChildrenActiveFilter.setText(fitForChildren);

                fitForGuarding = Objects.requireNonNull(snapshot.child("fitForGuarding").getValue()).toString();
                if (fitForGuarding.equals("don't care"))
                    fitForGuardingActiveFilter.setVisibility(View.GONE);
                else
                    fitForGuardingActiveFilter.setText(fitForGuarding);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SeeListOfPetsActivity.this, "Data not get " + error, Toast.LENGTH_SHORT).show();
            }
        });

        List<Pets> petsList = new ArrayList<>();
        petsList.add(new Pets(R.drawable.milo_dog, "Milo", 3, getResources().getColor(R.color.card1_background), "medium"));
        petsList.add(new Pets(R.drawable.arthur_dog, "Arthur", 2, getResources().getColor(R.color.card2_background), "medium"));
        petsList.add(new Pets(R.drawable.maya_cat, "Maya", 1, getResources().getColor(R.color.card3_background),"small"));
        petsList.add(new Pets(R.drawable.oscar_dog, "Oscar",2, getResources().getColor(R.color.card4_background),"medium"));


        GridView gridView = findViewById(R.id.listOfPetsContentPets_gridView);
        CustomListOfPetsAdapter customAdapter = new CustomListOfPetsAdapter(this, R.layout.custom_pet_view_layout, petsList);
        gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // open milo (pet) dog details
                    Intent intent = new Intent(SeeListOfPetsActivity.this, SeePetDetailsActivity.class);
                    startActivity(intent);
                }
                switch (position) {
                    case 0:
                        Intent intent = new Intent(SeeListOfPetsActivity.this, SeePetDetailsActivity.class);
                        intent.putExtra("petName", "Milo");
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(SeeListOfPetsActivity.this, SeePetDetailsActivity.class);
                        startActivity(intent);
                        break;
                    default:

                }
            }
        });
    }
}