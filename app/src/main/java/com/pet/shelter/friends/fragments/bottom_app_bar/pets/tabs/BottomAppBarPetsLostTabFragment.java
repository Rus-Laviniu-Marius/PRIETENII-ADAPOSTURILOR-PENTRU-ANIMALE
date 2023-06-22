package com.pet.shelter.friends.fragments.bottom_app_bar.pets.tabs;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.pets.lost.LostPetData;
import com.pet.shelter.friends.pets.lost.LostPetData;
import com.pet.shelter.friends.pets.lost.AddLostPetActivity;
import com.pet.shelter.friends.pets.lost.LostPetDetailsActivity;
import com.pet.shelter.friends.pets.lost.LostPetsCustomAdapter;

import java.util.ArrayList;

public class BottomAppBarPetsLostTabFragment extends Fragment {

    private DatabaseReference lostPetsReference;
    private RelativeLayout addPetsRelativeLayout, petsRelativeLayout;
    private MaterialTextView materialTextView;
    private ListView listView;
    private final ArrayList<LostPetData> lostPetsList = new ArrayList<>();
    private LostPetsCustomAdapter lostPetsCustomAdapter;

    public BottomAppBarPetsLostTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_bottom_app_bar_pets_lost_tab, container, false);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        lostPetsReference = firebaseDatabase.getReference("pets");

        listView = layout.findViewById(R.id.bottomAppBarPetsLostTab_listView);
        addPetsRelativeLayout = layout.findViewById(R.id.bottomAppBarPetsLostTabAdd_relativeLayout);
        petsRelativeLayout = layout.findViewById(R.id.bottomAppBarPetsLostTab_relativeLayout);
        materialTextView = layout.findViewById(R.id.bottomAppBarPetsLostTabNothing_materialTextView);
        ExtendedFloatingActionButton addPetsExtendedFloatingActionButton = layout.findViewById(R.id.bottomAppBarPetsLostTabAdd_extendedFloatingActionButton);

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
//                            doSomeOperations();
                        }
                    }
                });

        getDataFromDatabase();

        addPetsExtendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddLostPetActivity.class);
                someActivityResultLauncher.launch(intent);
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

    private void getDataFromDatabase() {
        lostPetsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Lost")) {
                    petsRelativeLayout.setVisibility(View.VISIBLE);
                    materialTextView.setVisibility(View.GONE);

                    lostPetsList.clear();
                    for (DataSnapshot lostPetSnapshot : snapshot.child("Lost").getChildren()) {
                        LostPetData lostPetData = lostPetSnapshot.getValue(LostPetData.class);
                        lostPetsList.add(lostPetData);
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
        getDataFromDatabase();
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