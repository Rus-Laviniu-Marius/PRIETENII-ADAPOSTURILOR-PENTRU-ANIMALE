package com.pet.shelter.friends.adoption;

import androidx.annotation.NonNull;
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
    private ImageView back, filter, search, favorites, send, home;
    private Button addNewPetButton;
    private GridView gridView;

    private DatabaseReference filtersReference, usersReference, petReference;

    private final ArrayList<Pet> petsList = new ArrayList<>();
    private CustomListOfPetsAdapter customAdapter;

    private Bundle bundle;
    private ArrayList<String> petKeys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_list_of_pets);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        filtersReference = firebaseDatabase.getReference("filters");
        usersReference = firebaseDatabase.getReference("shelterAdmin");
        petReference = firebaseDatabase.getReference("pets");

        back = findViewById(R.id.listOfPetsTopBarBack_imageView);
        filter = findViewById(R.id.listOfPetsTopBarFilter_imageView);
        search = findViewById(R.id.listOfPetsBottomBarSearch_imageView);
        favorites = findViewById(R.id.listOfPetsBottomBarHearth_imageView);
        home = findViewById(R.id.listOfPetsBottomBarHome_imageView);
        send = findViewById(R.id.listOfPetsBottomBarPaperAirplane_imageView);

        addNewPetButton = findViewById(R.id.listOfPetsContentAddNewPet_button);

        sizeActiveFilter = findViewById(R.id.listOfPetsContentSizeActiveFilter_textView);
        ageActiveFilter = findViewById(R.id.listOfPetsContentAgeActiveFilter_textView);
        sexActiveFilter = findViewById(R.id.listOfPetsContentGenderActiveFilter_textView);
        fitForChildrenActiveFilter = findViewById(R.id.listOfPetsContentFitForChildrenActiveFilter_textView);
        fitForGuardingActiveFilter = findViewById(R.id.listOfPetsContentFitForGuardingActiveFilter_textView);

        gridView = findViewById(R.id.listOfPetsContentPets_gridView);

        String loggedUid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        bundle = getIntent().getExtras();

        setOnClickListeners();

        setFilters(loggedUid);

        checkIfCurrentUserIsShelterAdmin(loggedUid);

        removeActiveFilters();

        getPetDataFromDatabase();

        setOnItemClickListeners();
    }

    private void setOnClickListeners() {
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

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeeListOfPetsActivity.this, SeeListOfFavoritePetsActivity.class);
                startActivity(intent);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SeeListOfPetsActivity.this, HomeActivity.class));
            }
        });

        addNewPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeListOfPetsActivity.this, AddNewPetActivity.class);
                startActivity(intent);
            }
        });
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
        petReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                petsList.clear();
                petKeys.clear();
                for (DataSnapshot petSnapshot : snapshot.getChildren()) {
                    Pet pet = petSnapshot.getValue(Pet.class);
                    petsList.add(pet);
                    petKeys.add(petSnapshot.getKey());
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
                intent.putExtra("favorite", pet.isFavorite());
                intent.putExtra("petKey", petKeys.get(position));
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

}
