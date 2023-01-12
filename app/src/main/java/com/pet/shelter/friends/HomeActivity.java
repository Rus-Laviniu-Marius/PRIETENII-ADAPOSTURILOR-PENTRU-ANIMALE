package com.pet.shelter.friends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pet.shelter.friends.adoption.FilterPetPreferencesActivity;
import com.pet.shelter.friends.adoption.profile.CreateProfileActivity;
import com.pet.shelter.friends.adoption.profile.ViewProfileActivity;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference activeUsersReference, shelterAdminReference, databaseReference;
    private TextView bottomBarHome, bottomBarAdopt, bottomBarDonate, bottomBarProfile;
    private RelativeLayout contentTopLeftAdopt, contentTopRightSponsor, contentBottomLeftVolunteer, contentBottomRightMaterialSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        activeUsersReference = firebaseDatabase.getReference("activeUsers");
        shelterAdminReference = firebaseDatabase.getReference("shelterAdmin");
        databaseReference = firebaseDatabase.getReference();

        bottomBarHome = findViewById(R.id.homeScreenBottomBarHome_textView);
        bottomBarAdopt = findViewById(R.id.homeScreenBottomBarAdopt_textView);
        bottomBarDonate = findViewById(R.id.homeScreenBottomBarDonate_textView);
        bottomBarProfile = findViewById(R.id.homeScreenBottomBarProfile_textView);
        contentTopLeftAdopt = findViewById(R.id.homeScreenContentTopLeft_button);
        contentTopRightSponsor = findViewById(R.id.homeScreenContentTopRight_button);
        contentBottomLeftVolunteer = findViewById(R.id.homeScreenContentBottomLeft_button);
        contentBottomRightMaterialSupport = findViewById(R.id.homeScreenContentBottomRight_button);

        saveConnectedUserIdToDatabase();

        setOnCLickListeners();


    }

    private void saveConnectedUserIdToDatabase() {
        String uId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        shelterAdminReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String shelterAdminId = Objects.requireNonNull(snapshot.child("uId").getValue()).toString();
                if (!shelterAdminId.equals(uId)) {
                    activeUsersReference.child(uId).child("uId").setValue(uId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setOnCLickListeners() {
        bottomBarProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("users")) {
                            Intent intent = new Intent(HomeActivity.this, ViewProfileActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(HomeActivity.this, CreateProfileActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        bottomBarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadScreen();
            }
        });

        bottomBarAdopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToFilterActivity();
            }
        });

        contentTopLeftAdopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToFilterActivity();
            }
        });
    }

    private void reloadScreen() {
        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private void sendUserToFilterActivity() {
        Intent intent = new Intent(HomeActivity.this, FilterPetPreferencesActivity.class);
        startActivity(intent);
    }
}