package com.pet.shelter.friends.adoption;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
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
    private SearchView searchView;

    private DatabaseReference filtersReference, usersReference, petsReference, favoritePetsReference;

    private final ArrayList<Pet> petsList = new ArrayList<>();
    private final ArrayList<Pet> favoritePetsList = new ArrayList<>();
    private CustomListOfPetsAdapter customAdapter;
    private final ArrayList<String> petKeys = new ArrayList<>();
    private String size = "", age = "", sex = "", fitForChildren = "", fitForGuarding = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_list_of_pets);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        filtersReference = firebaseDatabase.getReference("filters");
        usersReference = firebaseDatabase.getReference("shelterAdmin");
        petsReference = firebaseDatabase.getReference("pets");
        favoritePetsReference = firebaseDatabase.getReference("favoritePets");

        searchView = findViewById(R.id.listOfPetsName_searchView);

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

        setOnClickListeners();

        setFilters(loggedUid);

        checkIfCurrentUserIsShelterAdmin(loggedUid);

        removeActiveFilters();

        getPetDataFromDatabase(loggedUid);

        updateGridView();

        setOnItemClickListeners();
    }

    private void updateGridView() {

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
            boolean isPressed = false;
            @Override
            public void onClick(View view) {
                if (isPressed) {
                    searchView.setVisibility(View.VISIBLE);
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String s) {
                            String userInput = s.toLowerCase();
                            ArrayList<Pet> newPetArrayList = new ArrayList<>();
                            for (Pet pet : petsList) {
                                if (pet.getName().toLowerCase().contains(userInput)) {
                                    newPetArrayList.add(pet);
                                }
                            }
                            customAdapter.upToDate(newPetArrayList);
                            return true;
                        }

                        @Override
                        public boolean onQueryTextChange(String s) {
                            String userInput = s.toLowerCase();
                            ArrayList<Pet> newPetArrayList = new ArrayList<>();
                            for (Pet pet : petsList) {
                                if (pet.getName().toLowerCase().contains(userInput)) {
                                    newPetArrayList.add(pet);
                                }
                            }
                            customAdapter.upToDate(newPetArrayList);
                            return true;
                        }
                    });
                } else {
                    searchView.setVisibility(View.GONE);
                }
                isPressed = !isPressed;
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
                int position = -1;
                boolean selected = false;
                for (Pet pet : petsList) {
                    if (Boolean.parseBoolean(pet.isSelected())) {
                        position = petsList.indexOf(pet);
                        selected = true;
                    }
                }
                if (selected && position >= 0) {
                    Pet pet = petsList.get(position);

                    try {
                        Intent whatsappIntent = new Intent(Intent.ACTION_SEND).setType("text/plain").setPackage("com.whatsapp").putExtra(Intent.EXTRA_TEXT, pet.toString());
                        startActivity(whatsappIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(SeeListOfPetsActivity.this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(SeeListOfPetsActivity.this, "Select pet by long touching it", Toast.LENGTH_SHORT).show();
                }
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

    public void refresh()
    {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                customAdapter.notifyDataSetChanged();
                gridView.invalidate();
            }
        });

    }

    private void getPetDataFromDatabase(String loggedUid) {

        petsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                petsList.clear();
                petKeys.clear();
                for (DataSnapshot petSnapshot : snapshot.getChildren()) {
                    Pet pet = petSnapshot.getValue(Pet.class);
//                  based on selected filters the pets are added to the list
//                    if (size.equals("small") && !age.equals("young") && !sex.equals("female") && !fitForChildren.equals("no") && !fitForGuarding.equals("no")) {
//
//                    }
                    petsList.add(pet);
                    petKeys.add(petSnapshot.getKey());
                }

                customAdapter = new CustomListOfPetsAdapter(SeeListOfPetsActivity.this, R.layout.custom_pet_view_layout, petsList);
                customAdapter.notifyDataSetChanged();
                refresh();
                gridView.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getMessage());
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
                intent.putExtra("petSex", pet.getSex());
                intent.putExtra("petDescription", pet.getDescription());
                intent.putExtra("favorite", pet.isFavorite());
                intent.putExtra("selected", pet.isSelected());
                intent.putExtra("petType", pet.getType());
                intent.putExtra("petKey", petKeys.get(position));
                startActivity(intent);
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                boolean isSelected = Boolean.parseBoolean(petsList.get(i).isSelected());
                petsList.get(i).setSelected(String.valueOf(!isSelected));
                petsReference.child(petKeys.get(i)).child("selected").setValue(String.valueOf(isSelected));
                customAdapter.notifyDataSetChanged();
                Toast.makeText(SeeListOfPetsActivity.this, petsList.get(i).getName() + " is selected", Toast.LENGTH_SHORT).show();

                return true;
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
                    favorites.setVisibility(View.GONE);
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
