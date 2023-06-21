package com.pet.shelter.friends.fragments.bottom_app_bar.pets.tabs;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.pets.PetData;
import com.pet.shelter.friends.pets.sheltered.ShelteredPetDetailsActivity;
import com.pet.shelter.friends.pets.sheltered.ShelteredPetsCustomAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class BottomAppBarPetsFavoritesTabFragment extends Fragment {


    private DatabaseReference shelteredPetsReference;
    private String loggedUserId;
    private RelativeLayout petsRelativeLayout;
    private MaterialTextView materialTextView;
    private ListView listView;

    private final ArrayList<PetData> shelteredPetsList = new ArrayList<>();
    private ShelteredPetsCustomAdapter shelteredPetsCustomAdapter;


    public BottomAppBarPetsFavoritesTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_bottom_app_bar_pets_favorites_tab, container, false);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        shelteredPetsReference = firebaseDatabase.getReference("pets");

        listView = layout.findViewById(R.id.bottomAppBarPetsFavoritesTab_listView);
        petsRelativeLayout = layout.findViewById(R.id.bottomAppBarPetsFavoritesTab_relativeLayout);
        materialTextView = layout.findViewById(R.id.bottomAppBarPetsFavoritesTabNothing_materialTextView);

        loggedUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        getDataFromDatabase();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PetData petData = shelteredPetsCustomAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), ShelteredPetDetailsActivity.class);
                intent.putExtra("shelterAdministratorId", petData.getShelterAdministratorId());
                intent.putExtra("petImage1DownloadLink", petData.getPetImage1DownloadLink());
                intent.putExtra("petType", petData.getPetType());
                intent.putExtra("petName", petData.getPetName());
                intent.putExtra("petBreed", petData.getPetBreed());
                intent.putExtra("petAge", petData.getPetAge());
                intent.putExtra("petSize", petData.getPetSize());
                intent.putExtra("petSex", petData.getPetSex());
                intent.putExtra("petDescription", petData.getPetDescription());
                intent.putExtra("spayedOrNeutered", petData.getSpayedOrNeutered());
                intent.putExtra("dewormed", petData.getDewormed());
                intent.putExtra("vaccines", petData.getVaccines());
                intent.putExtra("fitForChildren", petData.getFitForChildren());
                intent.putExtra("fitForGuarding", petData.getFitForGuarding());
                intent.putExtra("friendlyWithPets", petData.getFriendlyWithPets());
                intent.putExtra("isFavorite", petData.getFavorite());
                startActivity(intent);
            }
        });

        return layout;
    }

    private void getDataFromDatabase() {
        shelteredPetsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Sheltered")) {
                    petsRelativeLayout.setVisibility(View.VISIBLE);
                    materialTextView.setVisibility(View.GONE);

                    shelteredPetsList.clear();
                    for (DataSnapshot newsArticleSnapshot : snapshot.child("Sheltered").getChildren()) {
                        PetData petData = newsArticleSnapshot.getValue(PetData.class);
                        if (petData != null) {
                            if (petData.getFavorite().equals("yes")) {
                                shelteredPetsList.add(petData);
                            }
                        }
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

    public void refresh() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                shelteredPetsCustomAdapter.notifyDataSetChanged();
                listView.invalidate();
            }
        });

    }
}