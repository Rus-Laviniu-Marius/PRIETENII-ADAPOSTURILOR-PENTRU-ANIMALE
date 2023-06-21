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
import com.pet.shelter.friends.SearchQueryEvent;
import com.pet.shelter.friends.pets.sheltered.AddShelteredPetActivity;
import com.pet.shelter.friends.pets.sheltered.ShelteredPetData;
import com.pet.shelter.friends.pets.sheltered.ShelteredPetDetailsActivity;
import com.pet.shelter.friends.pets.sheltered.ShelteredPetsCustomAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Objects;

public class BottomAppBarPetsShelteredTabFragment extends Fragment {

    private DatabaseReference shelteredPetsReference, roles;
    private String loggedUserId;
    private RelativeLayout addPetsRelativeLayout, petsRelativeLayout;
    private MaterialTextView materialTextView;
    private ListView listView;

    private final ArrayList<ShelteredPetData> shelteredPetsList = new ArrayList<>();
    private ShelteredPetsCustomAdapter shelteredPetsCustomAdapter;

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

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchQuery(SearchQueryEvent event) {
        String query=event.getQuery();
        shelteredPetsCustomAdapter.getFilter().filter(query);
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
                        ShelteredPetData shelteredPetData = newsArticleSnapshot.getValue(ShelteredPetData.class);
                        shelteredPetsList.add(shelteredPetData);
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
                shelteredPetsCustomAdapter.notifyDataSetChanged();
                listView.invalidate();
            }
        });

    }
}