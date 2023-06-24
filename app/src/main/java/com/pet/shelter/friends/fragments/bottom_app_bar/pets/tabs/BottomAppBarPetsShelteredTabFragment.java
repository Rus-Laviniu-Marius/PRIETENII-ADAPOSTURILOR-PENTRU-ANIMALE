package com.pet.shelter.friends.fragments.bottom_app_bar.pets.tabs;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.pets.filtering.FilterData;
import com.pet.shelter.friends.pets.sheltered.AddShelteredPetActivity;
import com.pet.shelter.friends.pets.sheltered.ShelteredPetData;
import com.pet.shelter.friends.pets.sheltered.ShelteredPetDetailsActivity;
import com.pet.shelter.friends.pets.sheltered.ShelteredPetsCustomAdapter;


import java.util.ArrayList;
import java.util.Objects;

public class BottomAppBarPetsShelteredTabFragment extends Fragment {

    private DatabaseReference shelteredPetsReference, roles, filters;
    private String loggedUserId;
    private SearchView searchView;
    private SearchBar searchBar;
    private ConstraintLayout petsConstraintLayout;
    private MaterialTextView materialTextView, nothingFound;
    private ListView listView;
    private ExtendedFloatingActionButton addPetsExtendedFloatingActionButton;
    private final ArrayList<ShelteredPetData> shelteredPetsList = new ArrayList<>();
    private final ArrayList<ShelteredPetData> originalShelteredPetsList = new ArrayList<>();
    private ArrayList<ShelteredPetData> textSearchedShelteredPetsList = new ArrayList<>();
    private ShelteredPetsCustomAdapter shelteredPetsCustomAdapter;
    private FilterData filterData;

    public BottomAppBarPetsShelteredTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_bottom_app_bar_pets_sheltered_tab, container, false);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        roles = firebaseDatabase.getReference("roles");
        shelteredPetsReference = firebaseDatabase.getReference("pets");
        filters = FirebaseDatabase.getInstance().getReference("filters");
        searchView = layout.findViewById(R.id.bottomAppBarPetsShelteredTab_searchView);
        searchBar = layout.findViewById(R.id.bottomAppBarPetsShelteredTab_searchBar);
        listView = layout.findViewById(R.id.bottomAppBarPetsShelteredTab_listView);
        petsConstraintLayout = layout.findViewById(R.id.bottomAppBarPetsShelteredTab_constraintLayout);
        materialTextView = layout.findViewById(R.id.bottomAppBarPetsShelteredTabNothing_materialTextView);
        nothingFound = layout.findViewById(R.id.bottomAppBarPetsShelteredTabNothingFound_materialTextView);
        addPetsExtendedFloatingActionButton = layout.findViewById(R.id.bottomAppBarPetsShelteredTabAdd_extendedFloatingActionButton);

        loggedUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        getShelteredPetsDataFromDatabase();
        getRolesFromDatabase();
        getFiltersDataFromDatabase();

        addPetsExtendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddShelteredPetActivity.class);
                startActivity(intent);
            }
        });

        searchBar.setHint("Search sheltered pets by name");

        searchView.setupWithSearchBar(searchBar);
        searchView.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchBar.setText(searchView.getText());
                filterListBySearchedName();
                searchView.hide();
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShelteredPetData shelteredPetData = shelteredPetsCustomAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), ShelteredPetDetailsActivity.class);
                intent.putExtra("shelterAdministratorId", shelteredPetData.getShelterAdministratorId());
                intent.putExtra("petImage1DownloadLink", shelteredPetData.getPetImage1DownloadLink());
                intent.putExtra("petType", shelteredPetData.getPetType());
                intent.putExtra("petName", shelteredPetData.getPetName());
                intent.putExtra("petBreed", shelteredPetData.getPetBreed());
                intent.putExtra("petAge", shelteredPetData.getPetAge());
                intent.putExtra("petSize", shelteredPetData.getPetSize());
                intent.putExtra("petSex", shelteredPetData.getPetSex());
                intent.putExtra("petDescription", shelteredPetData.getPetDescription());
                intent.putExtra("spayedOrNeutered", shelteredPetData.getSpayedOrNeutered());
                intent.putExtra("dewormed", shelteredPetData.getDewormed());
                intent.putExtra("vaccines", shelteredPetData.getVaccines());
                intent.putExtra("fitForChildren", shelteredPetData.getFitForChildren());
                intent.putExtra("fitForGuarding", shelteredPetData.getFitForGuarding());
                intent.putExtra("friendlyWithPets", shelteredPetData.getFriendlyWithPets());
                intent.putExtra("isFavorite", shelteredPetData.getFavorite());
                startActivity(intent);
            }
        });

        return layout;
    }

    private void getRolesFromDatabase() {
        roles.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(loggedUserId).hasChild("user")) {
                    addPetsExtendedFloatingActionButton.setVisibility(View.GONE);
                } else {
                    addPetsExtendedFloatingActionButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getShelteredPetsDataFromDatabase() {
        shelteredPetsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Sheltered")) {
                    petsConstraintLayout.setVisibility(View.VISIBLE);
                    materialTextView.setVisibility(View.GONE);

                    originalShelteredPetsList.clear();
                    shelteredPetsList.clear();
                    for (DataSnapshot shelteredPetSnapshot : snapshot.child("Sheltered").getChildren()) {
                        ShelteredPetData shelteredPetData = shelteredPetSnapshot.getValue(ShelteredPetData.class);
                        shelteredPetsList.add(shelteredPetData);
                        originalShelteredPetsList.add(shelteredPetData);
                    }


                    shelteredPetsCustomAdapter = new ShelteredPetsCustomAdapter(getApplicationContext(),
                            R.layout.sheltered_and_favorite_pet_list_item,
                            shelteredPetsList);
                    shelteredPetsCustomAdapter.notifyDataSetChanged();
                    refresh();
                    listView.setAdapter(shelteredPetsCustomAdapter);
                } else {
                    listView.setVisibility(View.GONE);
                    materialTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getMessage());
            }
        });
    }

    private void getFiltersDataFromDatabase() {

        filters.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(loggedUserId)) {
                    snapshot = snapshot.child(loggedUserId);
                    filterData = snapshot.getValue(FilterData.class);
                }
                if (filterData != null) {
                    filterListByFilterChipsValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filterListBySearchedName() {
        ArrayList<ShelteredPetData> filteredList = new ArrayList<>();
        String searchBarText = String.valueOf(searchBar.getText());
        if (searchBarText.isEmpty()) {
            shelteredPetsCustomAdapter.clear();
            shelteredPetsCustomAdapter.addAll(originalShelteredPetsList);
            shelteredPetsCustomAdapter.notifyDataSetChanged();
            listView.setAdapter(shelteredPetsCustomAdapter);
            listView.setVisibility(View.VISIBLE);
            nothingFound.setVisibility(View.GONE);
        } else {
            for (ShelteredPetData shelteredPetData : shelteredPetsList) {
                if (shelteredPetData.getPetName().toLowerCase().contains(searchBarText.toLowerCase())) {
                    filteredList.add(shelteredPetData);
                    nothingFound.setVisibility(View.GONE);
                } else {
                    nothingFound.setVisibility(View.VISIBLE);
                }
            }

            shelteredPetsCustomAdapter.clear();
            shelteredPetsCustomAdapter.addAll(filteredList);
            shelteredPetsCustomAdapter.notifyDataSetChanged();
            textSearchedShelteredPetsList.clear();
            textSearchedShelteredPetsList = filteredList;
            refresh();
        }
    }

    private void filterListByFilterChipsValue() {
        ArrayList<ShelteredPetData> filteredList = new ArrayList<>();
        String category, size, sex, age, friendly, guarding;

        for (ShelteredPetData shelteredPetData : originalShelteredPetsList) {
            if (filterData.getCategory() != null && filterData.getSex() != null) {
                category = filterData.getCategory().toLowerCase();
                sex = filterData.getSex().toLowerCase();
                if (shelteredPetData.getPetType().toLowerCase().matches(category.toLowerCase()) &&
                        shelteredPetData.getPetSex().toLowerCase().matches(sex.toLowerCase())) {
                    filteredList.add(shelteredPetData);
                }
            } else if (filterData.getCategory() != null) {
                category = filterData.getCategory().toLowerCase();
                if (shelteredPetData.getPetType().toLowerCase().matches(category.toLowerCase())) {
                    filteredList.add(shelteredPetData);
                }
            }
            else if (filterData.getSex() != null) {
                sex = filterData.getSex().toLowerCase();
                if (shelteredPetData.getPetSex().toLowerCase().matches(sex.toLowerCase())) {
                filteredList.add(shelteredPetData);
                }
            } else if (filterData.getSize() != null) {
                size = filterData.getSize().toLowerCase();
                if (shelteredPetData.getPetSize().toLowerCase().matches(size.toLowerCase())) {
                    filteredList.add(shelteredPetData);
                }
            } else if (filterData.getAge() != null) {
                age = filterData.getAge().toLowerCase();
                if (shelteredPetData.getPetAge().toLowerCase().matches(age.toLowerCase())) {
                    filteredList.add(shelteredPetData);
                }
            } else if (filterData.getFriendly() != null) {
                friendly = filterData.getFriendly().toLowerCase();
                if (shelteredPetData.getFriendlyWithPets().toLowerCase().matches(friendly.toLowerCase()) ||
                    shelteredPetData.getFitForChildren().toLowerCase().matches(friendly.toLowerCase())) {
                    filteredList.add(shelteredPetData);
                }
            } else if (filterData.getGuarding() != null) {
                guarding = filterData.getGuarding().toLowerCase();
                if (shelteredPetData.getFitForGuarding().toLowerCase().matches(guarding.toLowerCase())) {
                    filteredList.add(shelteredPetData);
                }
            }
        }
        if (filteredList.isEmpty()) {
            if (String.valueOf(searchBar.getText()).isEmpty()) {
                filteredList = originalShelteredPetsList;
            } else {
                filteredList = textSearchedShelteredPetsList;
            }
        }
        shelteredPetsCustomAdapter.clear();
        shelteredPetsCustomAdapter.addAll(filteredList);
        shelteredPetsCustomAdapter.notifyDataSetChanged();
        refresh();
        listView.setAdapter(shelteredPetsCustomAdapter);
    }

    public void refresh() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                shelteredPetsCustomAdapter.notifyDataSetChanged();
                listView.invalidate();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}