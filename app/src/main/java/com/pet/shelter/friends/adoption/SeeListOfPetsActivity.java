package com.pet.shelter.friends.adoption;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pet.shelter.friends.HomeActivity;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.adoption.adapter.CustomListOfPetsAdapter;
import com.pet.shelter.friends.adoption.model.Pet;

import java.util.ArrayList;
import java.util.Objects;

public class SeeListOfPetsActivity extends AppCompatActivity {

    private TextView sizeActiveFilter, ageActiveFilter, sexActiveFilter,
            fitForChildrenActiveFilter, fitForGuardingActiveFilter;
    private ImageView back, filter;
    private Button addNewPetButton;
    private GridView gridView;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, filtersReference, usersReference, petReference;

    private final ArrayList<Pet> petsList = new ArrayList<>();
    private Pet petToAdd;
    private CustomListOfPetsAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_list_of_pets);

        back = findViewById(R.id.listOfPetsTopBarBack_imageView);
        filter = findViewById(R.id.listOfPetsTopBarFilter_imageView);
        addNewPetButton = findViewById(R.id.listOfPetsContentAddNewPet_button);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        filtersReference = firebaseDatabase.getReference("filters");
        usersReference = firebaseDatabase.getReference("shelterAdmin");
        petReference = firebaseDatabase.getReference("pets");

        databaseReference = firebaseDatabase.getReference();

        sizeActiveFilter = findViewById(R.id.listOfPetsContentSizeActiveFilter_textView);
        ageActiveFilter = findViewById(R.id.listOfPetsContentAgeActiveFilter_textView);
        sexActiveFilter = findViewById(R.id.listOfPetsContentGenderActiveFilter_textView);
        fitForChildrenActiveFilter = findViewById(R.id.listOfPetsContentFitForChildrenActiveFilter_textView);
        fitForGuardingActiveFilter = findViewById(R.id.listOfPetsContentFitForGuardingActiveFilter_textView);

        gridView = findViewById(R.id.listOfPetsContentPets_gridView);
//        customAdapter = new CustomListOfPetsAdapter(this, R.layout.custom_pet_view_layout, petsList);
//        gridView.setAdapter(customAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SeeListOfPetsActivity.this, HomeActivity.class));
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SeeListOfPetsActivity.this, FilterPetPreferencesActivity.class));
            }
        });


        String loggedUid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        setFilters(loggedUid);


        checkIfCurrentUserIsShelterAdmin(loggedUid);

        addNewPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeListOfPetsActivity.this, AddNewPetActivity.class);
                startActivity(intent);
            }
        });

        removeActiveFilters();

        getPetDataFromDatabase();

//        initializePetsList();

        setOnItemClickListeners();
    }

    private void setFilters(String loggedUid) {
        filtersReference.child(loggedUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String size, age, sex, fitForChildren, fitForGuarding;

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

                sex = Objects.requireNonNull(snapshot.child("sex").getValue()).toString();
                if (sex.equals("don't care"))
                    sexActiveFilter.setVisibility(View.GONE);
                else
                    sexActiveFilter.setText(sex);

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
    }

    private void getPetDataFromDatabase() {
        petReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        petReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        petReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                petsList.clear();
                for (DataSnapshot petSnapshot : snapshot.getChildren()) {
                    Pet pet = petSnapshot.getValue(Pet.class);
                    petsList.add(pet);
                }
                customAdapter = new CustomListOfPetsAdapter(SeeListOfPetsActivity.this, R.layout.custom_pet_view_layout, petsList);
                gridView.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setOnItemClickListeners() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Pet pet = petsList.get(position);
                Intent intent = new Intent(SeeListOfPetsActivity.this, SeePetDetailsActivity.class);
                intent.putExtra("backgroundColor", pet.getBackgroundColor());
                intent.putExtra("imageDownloadLink", pet.getImageDownloadLink());
                intent.putExtra("petName", pet.getName());
                intent.putExtra("petAge", pet.getAge());
                intent.putExtra("petWeight", pet.getWeight());
                intent.putExtra("petLocation", pet.getLocation());
                intent.putExtra("petSize", pet.getSize());
                intent.putExtra("petBreed", pet.getBreed());
                intent.putExtra("petGender", pet.getSex());
                intent.putExtra("petDescription", pet.getDescription());
                startActivity(intent);
            }
        });
    }

    private void checkIfCurrentUserIsShelterAdmin(String loggedUid) {
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uId = Objects.requireNonNull(snapshot.child("uId").getValue()).toString();
                if (loggedUid.equals(uId)) {
                    addNewPetButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void removeActiveFilters() {
        sizeActiveFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sizeActiveFilter.setVisibility(View.GONE);
            }
        });
        ageActiveFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ageActiveFilter.setVisibility(View.GONE);
            }
        });
        sexActiveFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexActiveFilter.setVisibility(View.GONE);
            }
        });
        fitForChildrenActiveFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fitForChildrenActiveFilter.setVisibility(View.GONE);
            }
        });
        fitForGuardingActiveFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fitForGuardingActiveFilter.setVisibility(View.GONE);
            }
        });
    }

//    private void initializePetsList() {
//        petsList.add(new Pet(
//                getResources().getColor(R.color.linen),
//                R.drawable.milo_dog,
//                "Milo",
//                3,
//                7.4,
//                "Timisoara",
//                "Medium",
//                "Bulldog",
//                "Male",
//                "Milo is a friendly dog"));
//        petsList.add(new Pet(
//                getResources().getColor(R.color.water),
//                R.drawable.arthur_dog,
//                "Arthur",
//                2,
//                11.4,
//                "Arad",
//                "Small",
//                "Pekingese",
//                "Male",
//                "Arthur love short walks"));
//        petsList.add(new Pet(
//                getResources().getColor(R.color.magic_mint),
//                R.drawable.maya_cat,
//                "Maya",
//                1,
//                5.4,
//                "Oradea",
//                "Small",
//                "Mixed",
//                "Female",
//                "Maya does not like raining weather"));
//        petsList.add(new Pet(
//                getResources().getColor(R.color.yellow_crayola),
//                R.drawable.oscar_dog,
//                "Oscar",
//                2,
//                9.5,
//                "Timisoara",
//                "Medium",
//                "Fox Terrier",
//                "Male",
//                "Oscar is a very very energetic dog"));
//    }
}
