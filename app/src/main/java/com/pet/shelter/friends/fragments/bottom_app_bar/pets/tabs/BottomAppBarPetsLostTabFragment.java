package com.pet.shelter.friends.fragments.bottom_app_bar.pets.tabs;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.pet.shelter.friends.pets.lost.LostPetData;
import com.pet.shelter.friends.pets.lost.AddLostPetActivity;
import com.pet.shelter.friends.pets.lost.LostPetDetailsActivity;
import com.pet.shelter.friends.pets.lost.LostPetsCustomAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class BottomAppBarPetsLostTabFragment extends Fragment {

    private DatabaseReference lostPetsReference, filters;
    private RelativeLayout addPetsRelativeLayout;
    private ConstraintLayout petsConstraintLayout;
    private MaterialTextView materialTextView, nothingFound;
    private SearchBar searchBar;
    private SearchView searchView;
    private ListView listView;
    private String loggedUserId;
    private final ArrayList<LostPetData> lostPetsList = new ArrayList<>();
    private final ArrayList<LostPetData> originalLostPetsList = new ArrayList<LostPetData>();
    private ArrayList<LostPetData> textSearchedLostPetsList = new ArrayList<>();
    private LostPetsCustomAdapter lostPetsCustomAdapter;
    private FilterData filterData;

    public BottomAppBarPetsLostTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_bottom_app_bar_pets_lost_tab, container, false);
        lostPetsReference = FirebaseDatabase.getInstance().getReference("pets");
        filters = FirebaseDatabase.getInstance().getReference("filters");
        loggedUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        nothingFound = layout.findViewById(R.id.bottomAppBarPetsLostTabNothingFound_materialTextView);
        materialTextView = layout.findViewById(R.id.bottomAppBarPetsLostTabNothing_materialTextView);
        searchBar = layout.findViewById(R.id.bottomAppBarPetsLostTab_searchBar);
        searchView = layout.findViewById(R.id.bottomAppBarPetsLostTab_searchView);
        listView = layout.findViewById(R.id.bottomAppBarPetsLostTab_listView);
        addPetsRelativeLayout = layout.findViewById(R.id.bottomAppBarPetsLostTabAdd_relativeLayout);
        petsConstraintLayout = layout.findViewById(R.id.bottomAppBarPetsLostTab_constraintLayout);

        ExtendedFloatingActionButton addPetsExtendedFloatingActionButton = layout.findViewById(R.id.bottomAppBarPetsLostTabAdd_extendedFloatingActionButton);

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                    }
                });

        getLostPetsDataFromDatabase();
        getFiltersDataFromDatabase();
        addPetsExtendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddLostPetActivity.class);
                someActivityResultLauncher.launch(intent);
            }
        });

        searchBar.setHint("Search lost pets by name");

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
                LostPetData lostPetData = lostPetsCustomAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), LostPetDetailsActivity.class);
                intent.putExtra("ownerEmail", lostPetData.getOwnerEmail());
                intent.putExtra("ownerPhoneNumber", lostPetData.getOwnerPhoneNumber());
                intent.putExtra("petImage1DownloadLink", lostPetData.getPetImage1DownloadLink());
                intent.putExtra("petType", lostPetData.getPetType());
                intent.putExtra("petName", lostPetData.getPetName());
                intent.putExtra("petBreed", lostPetData.getPetBreed());
                intent.putExtra("petAge", lostPetData.getPetAge());
                intent.putExtra("petSize", lostPetData.getPetSize());
                intent.putExtra("petSex", lostPetData.getPetSex());
                intent.putExtra("petDescription", lostPetData.getPetDescription());
                intent.putExtra("spayedOrNeutered", lostPetData.getSpayedOrNeutered());
                intent.putExtra("dewormed", lostPetData.getDewormed());
                intent.putExtra("vaccines", lostPetData.getVaccines());
                startActivity(intent);
            }
        });

        return layout;
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

    private void getLostPetsDataFromDatabase() {
        lostPetsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Lost")) {
                    petsConstraintLayout.setVisibility(View.VISIBLE);
                    materialTextView.setVisibility(View.GONE);

                    originalLostPetsList.clear();
                    lostPetsList.clear();
                    for (DataSnapshot lostPetSnapshot : snapshot.child("Lost").getChildren()) {
                        LostPetData lostPetData = lostPetSnapshot.getValue(LostPetData.class);
                        lostPetsList.add(lostPetData);
                        originalLostPetsList.add(lostPetData);
                    }

                    lostPetsCustomAdapter = new LostPetsCustomAdapter(getApplicationContext(),
                            R.layout.lost_pet_list_item,
                            lostPetsList);
                    lostPetsCustomAdapter.notifyDataSetChanged();
                    refresh();
                    listView.setAdapter(lostPetsCustomAdapter);
                } else {
                    listView.setVisibility(View.GONE);
                    materialTextView.setVisibility(View.VISIBLE);
                    addPetsRelativeLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getMessage());
            }
        });
    }

    private void filterListBySearchedName() {
        ArrayList<LostPetData> filteredList = new ArrayList<>();
        String searchBarText = String.valueOf(searchBar.getText());
        if (searchBarText.isEmpty()) {
            lostPetsCustomAdapter.clear();
            lostPetsCustomAdapter.addAll(originalLostPetsList);
            lostPetsCustomAdapter.notifyDataSetChanged();
            listView.setAdapter(lostPetsCustomAdapter);
            listView.setVisibility(View.VISIBLE);
            nothingFound.setVisibility(View.GONE);
        } else {
            for (LostPetData lostPetData : lostPetsList) {
                if (lostPetData.getPetName().toLowerCase().contains(searchBarText.toLowerCase())) {
                    filteredList.add(lostPetData);
                    nothingFound.setVisibility(View.GONE);
                } else {
                    nothingFound.setVisibility(View.VISIBLE);
                }
            }

            lostPetsCustomAdapter.clear();
            lostPetsCustomAdapter.addAll(filteredList);
            lostPetsCustomAdapter.notifyDataSetChanged();
            textSearchedLostPetsList.clear();
            textSearchedLostPetsList = filteredList;
            refresh();
        }
    }

    private void filterListByFilterChipsValue() {
        ArrayList<LostPetData> filteredList = new ArrayList<>();
        String category, size, sex, age, friendly, guarding;

        for (LostPetData lostPetData : originalLostPetsList) {
            if (filterData.getCategory() != null && filterData.getSex() != null) {
                category = filterData.getCategory().toLowerCase();
                sex = filterData.getSex().toLowerCase();
                if (lostPetData.getPetType().toLowerCase().matches(category.toLowerCase()) &&
                        lostPetData.getPetSex().toLowerCase().matches(sex.toLowerCase())) {
                    filteredList.add(lostPetData);
                }
            } else if (filterData.getCategory() != null) {
                category = filterData.getCategory().toLowerCase();
                if (lostPetData.getPetType().toLowerCase().matches(category.toLowerCase())) {
                    filteredList.add(lostPetData);
                }
            }
            else if (filterData.getSex() != null) {
                sex = filterData.getSex().toLowerCase();
                if (lostPetData.getPetSex().toLowerCase().matches(sex.toLowerCase())) {
                    filteredList.add(lostPetData);
                }
            } else if (filterData.getSize() != null) {
                size = filterData.getSize().toLowerCase();
                if (lostPetData.getPetSize().toLowerCase().matches(size.toLowerCase())) {
                    filteredList.add(lostPetData);
                }
            } else if (filterData.getAge() != null) {
                age = filterData.getAge().toLowerCase();
                if (lostPetData.getPetAge().toLowerCase().matches(age.toLowerCase())) {
                    filteredList.add(lostPetData);
                }
            } else if (filterData.getFriendly() != null) {
                friendly = filterData.getFriendly().toLowerCase();
                if (lostPetData.getFriendlyWithPets().toLowerCase().matches(friendly.toLowerCase()) ||
                        lostPetData.getFitForChildren().toLowerCase().matches(friendly.toLowerCase())) {
                    filteredList.add(lostPetData);
                }
            } else if (filterData.getGuarding() != null) {
                guarding = filterData.getGuarding().toLowerCase();
                if (lostPetData.getFitForGuarding().toLowerCase().matches(guarding.toLowerCase())) {
                    filteredList.add(lostPetData);
                }
            }
        }
        if (filteredList.isEmpty()) {
            if (String.valueOf(searchBar.getText()).isEmpty()) {
                filteredList = originalLostPetsList;
            } else {
                filteredList = textSearchedLostPetsList;
            }
        }
        lostPetsCustomAdapter.clear();
        lostPetsCustomAdapter.addAll(filteredList);
        lostPetsCustomAdapter.notifyDataSetChanged();
        refresh();
        listView.setAdapter(lostPetsCustomAdapter);
    }

    public void refresh() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                lostPetsCustomAdapter.notifyDataSetChanged();
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