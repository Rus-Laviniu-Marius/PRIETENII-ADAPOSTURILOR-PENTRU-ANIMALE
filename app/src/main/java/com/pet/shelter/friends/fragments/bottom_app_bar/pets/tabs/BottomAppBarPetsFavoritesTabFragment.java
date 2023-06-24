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
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.pet.shelter.friends.pets.sheltered.ShelteredPetData;
import com.pet.shelter.friends.pets.sheltered.ShelteredPetDetailsActivity;
import com.pet.shelter.friends.pets.sheltered.ShelteredPetsCustomAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class BottomAppBarPetsFavoritesTabFragment extends Fragment {


    private DatabaseReference shelteredPetsReference, filters;
    private String loggedUserId;
    private ConstraintLayout petsConstraintLayout;
    private MaterialTextView materialTextView, nothingFound;
    private ListView listView;
    private SearchBar searchBar;
    private SearchView searchView;
    private final ArrayList<ShelteredPetData> favoritePetsList = new ArrayList<>();
    private final ArrayList<ShelteredPetData> originalFavoritePetsList = new ArrayList<>();
    private ArrayList<ShelteredPetData> textSearchedFavoritePetsList = new ArrayList<>();
    private ShelteredPetsCustomAdapter favoritePetsCustomAdapter;
    private FilterData filterData;

    public BottomAppBarPetsFavoritesTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_bottom_app_bar_pets_favorites_tab, container, false);
        loggedUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        shelteredPetsReference = FirebaseDatabase.getInstance().getReference("pets");
        filters = FirebaseDatabase.getInstance().getReference("filters");
        listView = layout.findViewById(R.id.bottomAppBarPetsFavoritesTab_listView);
        petsConstraintLayout = layout.findViewById(R.id.bottomAppBarPetsFavoritesTab_constraintLayout);
        materialTextView = layout.findViewById(R.id.bottomAppBarPetsFavoritesTabNothing_materialTextView);
        nothingFound = layout.findViewById(R.id.bottomAppBarPetsFavoritesTabNothingFound_materialTextView);
        searchBar = layout.findViewById(R.id.bottomAppBarPetsFavoritesTab_searchBar);
        searchView = layout.findViewById(R.id.bottomAppBarPetsFavoritesTab_searchView);

        getFavoritePetDataFromDatabase();
        getFiltersDataFromDatabase();

        searchBar.setHint("Search favorite pets by name");

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
                ShelteredPetData shelteredPetData = favoritePetsCustomAdapter.getItem(position);
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

    private void getFavoritePetDataFromDatabase() {
        shelteredPetsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Sheltered")) {
                    petsConstraintLayout.setVisibility(View.VISIBLE);
                    materialTextView.setVisibility(View.GONE);

                    favoritePetsList.clear();
                    for (DataSnapshot shelteredPetsSnapshot : snapshot.child("Sheltered").getChildren()) {
                        ShelteredPetData shelteredPetData = shelteredPetsSnapshot.getValue(ShelteredPetData.class);
                        if (shelteredPetData != null) {
                            if (shelteredPetData.getFavorite() != null) {
                                if (shelteredPetData.getFavorite().equals("yes")) {
                                    favoritePetsList.add(shelteredPetData);
                                }
                            }
                        }
                    }

                    favoritePetsCustomAdapter = new ShelteredPetsCustomAdapter(getApplicationContext(),
                            R.layout.sheltered_and_favorite_pet_list_item,
                            favoritePetsList);
                    favoritePetsCustomAdapter.notifyDataSetChanged();
                    refresh();
                    listView.setAdapter(favoritePetsCustomAdapter);
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
            favoritePetsCustomAdapter.clear();
            favoritePetsCustomAdapter.addAll(originalFavoritePetsList);
            favoritePetsCustomAdapter.notifyDataSetChanged();
            listView.setAdapter(favoritePetsCustomAdapter);
            listView.setVisibility(View.VISIBLE);
            nothingFound.setVisibility(View.GONE);
        } else {
            for (ShelteredPetData shelteredPetData : favoritePetsList) {
                if (shelteredPetData.getPetName().toLowerCase().contains(searchBarText.toLowerCase())) {
                    filteredList.add(shelteredPetData);
                    nothingFound.setVisibility(View.GONE);
                } else {
                    nothingFound.setVisibility(View.VISIBLE);
                }
            }

            favoritePetsCustomAdapter.clear();
            favoritePetsCustomAdapter.addAll(filteredList);
            favoritePetsCustomAdapter.notifyDataSetChanged();
            textSearchedFavoritePetsList.clear();
            textSearchedFavoritePetsList = filteredList;
            refresh();
        }
    }

    private void filterListByFilterChipsValue() {
        ArrayList<ShelteredPetData> filteredList = new ArrayList<>();
        String category, size, sex, age, friendly, guarding;

        for (ShelteredPetData shelteredPetData : favoritePetsList) {
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
                filteredList = originalFavoritePetsList;
            } else {
                filteredList = textSearchedFavoritePetsList;
            }
        }
        favoritePetsCustomAdapter.clear();
        favoritePetsCustomAdapter.addAll(filteredList);
        favoritePetsCustomAdapter.notifyDataSetChanged();
        refresh();
        listView.setAdapter(favoritePetsCustomAdapter);
    }

    public void refresh() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                favoritePetsCustomAdapter.notifyDataSetChanged();
                listView.invalidate();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
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