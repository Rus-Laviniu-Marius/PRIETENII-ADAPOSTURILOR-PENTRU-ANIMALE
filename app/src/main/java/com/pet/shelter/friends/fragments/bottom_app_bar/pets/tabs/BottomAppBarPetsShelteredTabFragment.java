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

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.pets.AddShelteredPetActivity;
import com.pet.shelter.friends.pets.PetData;
import com.pet.shelter.friends.pets.ShelteredPetDetailsActivity;
import com.pet.shelter.friends.pets.PetsCustomAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class BottomAppBarPetsShelteredTabFragment extends Fragment {

    private DatabaseReference shelteredPetsReference, roles;
    private String loggedUserId;
    private RelativeLayout addPetsRelativeLayout, petsRelativeLayout;
    private MaterialTextView materialTextView;
    private ListView listView;

    private final ArrayList<PetData> shelteredPetsList = new ArrayList<>();
    private PetsCustomAdapter petsCustomAdapter;

    public BottomAppBarPetsShelteredTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_bottom_app_bar_pets_sheltered_tab, container, false);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        roles = firebaseDatabase.getReference("roles");
        shelteredPetsReference = firebaseDatabase.getReference("pets");

        listView = layout.findViewById(R.id.bottomAppBarPetsShelteredTab_listView);
        addPetsRelativeLayout = layout.findViewById(R.id.bottomAppBarPetsShelteredTabAdd_relativeLayout);
        petsRelativeLayout = layout.findViewById(R.id.bottomAppBarPetsShelteredTab_relativeLayout);
        materialTextView = layout.findViewById(R.id.bottomAppBarPetsShelteredTabNothing_materialTextView);
        ExtendedFloatingActionButton addPetsExtendedFloatingActionButton = layout.findViewById(R.id.bottomAppBarPetsShelteredTabAdd_extendedFloatingActionButton);

        loggedUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        getDataFromDatabase();

        addPetsExtendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddShelteredPetActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PetData petData = petsCustomAdapter.getItem(position);
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
                        shelteredPetsList.add(petData);
                    }

                    petsCustomAdapter = new PetsCustomAdapter(getApplicationContext(),
                            R.layout.pet_list_item,
                            shelteredPetsList);
                    petsCustomAdapter.notifyDataSetChanged();
                    refresh();
                    listView.setAdapter(petsCustomAdapter);
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

        roles.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(loggedUserId).hasChild("user")) {
                    addPetsRelativeLayout.setVisibility(View.GONE);
                } else {
                    addPetsRelativeLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void refresh() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                petsCustomAdapter.notifyDataSetChanged();
                listView.invalidate();
            }
        });

    }
}