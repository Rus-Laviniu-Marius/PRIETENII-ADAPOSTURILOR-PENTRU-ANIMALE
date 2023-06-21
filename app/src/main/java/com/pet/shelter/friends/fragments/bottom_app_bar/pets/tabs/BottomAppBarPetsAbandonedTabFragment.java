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
import com.pet.shelter.friends.pets.abandoned.AbandonedPetData;
import com.pet.shelter.friends.pets.abandoned.AbandonedPetDetailsActivity;
import com.pet.shelter.friends.pets.abandoned.AddAbandonedPetActivity;
import com.pet.shelter.friends.pets.abandoned.AbandonedPetsCustomAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class BottomAppBarPetsAbandonedTabFragment extends Fragment {
    private DatabaseReference shelteredPetsReference, roles;
    private RelativeLayout addPetsRelativeLayout, petsRelativeLayout;
    private MaterialTextView materialTextView;
    private ListView listView;
    private final ArrayList<AbandonedPetData> abandonedPetsList = new ArrayList<>();
    private AbandonedPetsCustomAdapter abandonedPetsCustomAdapter;

    public BottomAppBarPetsAbandonedTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_bottom_app_bar_pets_abandoned_tab, container, false);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        shelteredPetsReference = firebaseDatabase.getReference("pets");

        listView = layout.findViewById(R.id.bottomAppBarPetsAbandonedTab_listView);
        addPetsRelativeLayout = layout.findViewById(R.id.bottomAppBarPetsAbandonedTabAdd_relativeLayout);
        petsRelativeLayout = layout.findViewById(R.id.bottomAppBarPetsAbandonedTab_relativeLayout);
        materialTextView = layout.findViewById(R.id.bottomAppBarPetsAbandonedTabNothing_materialTextView);
        ExtendedFloatingActionButton addPetsExtendedFloatingActionButton = layout.findViewById(R.id.bottomAppBarPetsAbandonedTabAdd_extendedFloatingActionButton);

        getDataFromDatabase();

        addPetsExtendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddAbandonedPetActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AbandonedPetData abandonedPetData = abandonedPetsCustomAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), AbandonedPetDetailsActivity.class);
                intent.putExtra("petImage1DownloadLink", abandonedPetData.getPetImage1DownloadLink());
                intent.putExtra("petDescription", abandonedPetData.getPetDescription());
                intent.putExtra("placeDescription", abandonedPetData.getPlaceDescription());
                intent.putExtra("petLocationLatitude", abandonedPetData.getPetLocationLatitude());
                intent.putExtra("petLocationLongitude", abandonedPetData.getPetLocationLongitude());

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
        abandonedPetsCustomAdapter.getFilter().filter(query);
    }

    private void getDataFromDatabase() {
        shelteredPetsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Abandoned")) {
                    petsRelativeLayout.setVisibility(View.VISIBLE);
                    materialTextView.setVisibility(View.GONE);

                    abandonedPetsList.clear();
                    for (DataSnapshot newsArticleSnapshot : snapshot.child("Abandoned").getChildren()) {
                        AbandonedPetData abandonedPetData = newsArticleSnapshot.getValue(AbandonedPetData.class);
                        abandonedPetsList.add(abandonedPetData);
                    }

                    abandonedPetsCustomAdapter = new AbandonedPetsCustomAdapter(getApplicationContext(),
                            R.layout.abandoned_pet_list_item,
                            abandonedPetsList);
                    abandonedPetsCustomAdapter.notifyDataSetChanged();
                    refresh();
                    listView.setAdapter(abandonedPetsCustomAdapter);
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

    public void refresh() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                abandonedPetsCustomAdapter.notifyDataSetChanged();
                listView.invalidate();
            }
        });

    }
}