package com.pet.shelter.friends.profile.services.fragments;

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
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.profile.services.ActiveServiceData;
import com.pet.shelter.friends.profile.services.ActiveServicesCustomAdapter;
import com.pet.shelter.friends.profile.services.AddServiceActivity;

import java.util.ArrayList;
import java.util.Objects;

public class ActiveServicesPetWalkersTabFragment extends Fragment {

    private DatabaseReference petWalkersActiveServicesReference, roles;
    private String loggedUserId;
    private ListView petWalkerServicesListView;
    private RelativeLayout addActiveServicesPetWalkerRelativeLayout;
    private MaterialTextView materialTextView;

    private final ArrayList<ActiveServiceData> activePetWalkersServicesList = new ArrayList<>();
    private ActiveServicesCustomAdapter activeServicesCustomAdapter;

    public ActiveServicesPetWalkersTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_active_services_pet_walkers_tab, container, false);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        roles = firebaseDatabase.getReference("roles");
        petWalkersActiveServicesReference = firebaseDatabase.getReference("activeServices");

        loggedUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        petWalkerServicesListView = layout.findViewById(R.id.activeServicesPetWalkers_listView);
        addActiveServicesPetWalkerRelativeLayout = layout.findViewById(R.id.activeServicesAddPetWalkers_relativeLayout);
        ExtendedFloatingActionButton addServiceFloatingActionButton = layout.findViewById(R.id.activeServicesAddPetWalkers_floatingActionButton);
        materialTextView = layout.findViewById(R.id.activeServicesPetWalkersNoServices_materialTextView);

        getDataFromDatabase();

        addServiceFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddServiceActivity.class));
                requireActivity().finish();
            }
        });

        return layout;
    }

    private void getDataFromDatabase() {
        petWalkersActiveServicesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Pet walker")) {
                    addActiveServicesPetWalkerRelativeLayout.setVisibility(View.VISIBLE);
                    petWalkerServicesListView.setVisibility(View.VISIBLE);
                    materialTextView.setVisibility(View.GONE);

                    activePetWalkersServicesList.clear();
                    for (DataSnapshot activeServiceSnapshot : snapshot.child("Pet walker").getChildren()) {

                        ActiveServiceData activeServiceData = activeServiceSnapshot.getValue(ActiveServiceData.class);
                        activePetWalkersServicesList.add(activeServiceData);
                    }

                    activeServicesCustomAdapter = new ActiveServicesCustomAdapter(getApplicationContext(),
                            R.layout.active_service,
                            activePetWalkersServicesList);
                    activeServicesCustomAdapter.notifyDataSetChanged();
                    refresh();
                    petWalkerServicesListView.setAdapter(activeServicesCustomAdapter);
                } else {
                    petWalkerServicesListView.setVisibility(View.GONE);
                    addActiveServicesPetWalkerRelativeLayout.setVisibility(View.VISIBLE);
                    materialTextView.setVisibility(View.VISIBLE);
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
                    addActiveServicesPetWalkerRelativeLayout.setVisibility(View.VISIBLE);
                } else {
                    addActiveServicesPetWalkerRelativeLayout.setVisibility(View.GONE);
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
                activeServicesCustomAdapter.notifyDataSetChanged();
                petWalkerServicesListView.invalidate();
            }
        });
    }
}