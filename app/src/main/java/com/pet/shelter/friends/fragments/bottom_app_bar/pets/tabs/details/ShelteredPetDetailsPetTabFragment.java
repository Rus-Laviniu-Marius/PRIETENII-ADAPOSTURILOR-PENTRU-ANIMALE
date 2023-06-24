package com.pet.shelter.friends.fragments.bottom_app_bar.pets.tabs.details;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.pets.adoption.AdoptionApplicationParagraphsActivity;

public class ShelteredPetDetailsPetTabFragment extends Fragment {

    private MaterialTextView petName, petBreed, petAge, petSize, petSex, petDescription;
    private MaterialButton addToFavorites, adopt;
    private boolean isFavorite = false;
    private String name, breed, age, size, sex, description;


    public ShelteredPetDetailsPetTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_pet_details_pet_tab, container, false);

        petName = layout.findViewById(R.id.petDetailsTabName_materialTextView);
        petBreed = layout.findViewById(R.id.petDetailsTabBreed_materialTextView);
        petAge = layout.findViewById(R.id.petDetailsTabAge_materialTextView);
        petSize = layout.findViewById(R.id.petDetailsTabSize_materialTextView);
        petSex = layout.findViewById(R.id.petDetailsTabSex_materialTextView);
        petDescription = layout.findViewById(R.id.petDetailsTabDescription_materialTextView);
        addToFavorites = layout.findViewById(R.id.petDetailsTabFavorites_materialButton);
        adopt = layout.findViewById(R.id.petDetailsTabAdopt_materialButton);

        Bundle petArguments = this.getArguments();
        if (petArguments != null) {
            name = petArguments.getString("petName");
            breed = petArguments.getString("petBreed");
            age = petArguments.getString("petAge");
            size = petArguments.getString("petSize");
            sex = petArguments.getString("petSex");
            description = petArguments.getString("petDescription");
        }

        petName.setText(name);
        petBreed.setText(breed);
        petAge.setText(age);
        petSize.setText(size);
        petSex.setText(sex);
        petDescription.setText(description);

        adopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AdoptionApplicationParagraphsActivity.class));
            }
        });

        addToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    addToFavorites.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.favorite_filled_24, getContext().getTheme()));
                } else {
                    addToFavorites.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.favorite_outlined_24, getContext().getTheme()));
                }
                isFavorite = !isFavorite;
            }
        });

        return layout;
    }

    public static ShelteredPetDetailsPetTabFragment newInstance(String petName,
                                                                String petBreed,
                                                                String petAge,
                                                                String petSize,
                                                                String petSex,
                                                                String petDescription) {
        ShelteredPetDetailsPetTabFragment fragment=new ShelteredPetDetailsPetTabFragment();
        Bundle bundle=new Bundle();
        bundle.putString("petName",petName);
        bundle.putString("petBreed",petBreed);
        bundle.putString("petAge",petAge);
        bundle.putString("petSize",petSize);
        bundle.putString("petSex",petSex);
        bundle.putString("petDescription",petDescription);
        fragment.setArguments(bundle);
        return fragment;
    }
}