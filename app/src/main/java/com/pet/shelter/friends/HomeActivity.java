package com.pet.shelter.friends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pet.shelter.friends.adoption.AddNewPetActivity;
import com.pet.shelter.friends.adoption.FilterPetPreferencesActivity;
import com.pet.shelter.friends.adoption.SeeListOfPetsActivity;
import com.pet.shelter.friends.profile.CreateProfileActivity;
import com.pet.shelter.friends.profile.ViewProfileActivity;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference activeUsersReference, shelterAdminReference, databaseReference;
    private ImageView menuImageView, notificationsImageView;
    private TextView userBottomBarHomeTextView, userBottomBarAdoptTextView, userBottomBarDonateTextView,
            userBottomBarSupplyTextView, userBottomBarProfileTextView, adminBottomBarHomeTextView,
            adminBottomBarPetsTextView, adminBottomBarDonationRequestsTextView,
            adminBottomBarSupplyRequestsTextView, adminBottomBarProfileTextView;
    private LinearLayout userHomeScreenContentLinearLayout, adminHomeScreenContentLinearLayout,
            userHomeScreenBottomBarLinearLayout, adminHomeScreenBottomBarLinearLayout;
    private RelativeLayout userContentTopLeftAdoptRelativeLayout, userContentTopRightSponsorRelativeLayout,
            userContentBottomLeftVolunteerRelativeLayout, userContentBottomRightMaterialSupportRelativeLayout,
            adminContentTopLeftAddRelativeLayout, adminContentTopRightDonationRequestRelativeLayout,
            adminContentBottomLeftVolunteerRequestRelativeLayout, adminContentBottomRightMaterialSupportRelativeLayout;

    private String loggedUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initialization();


        saveConnectedUserIdToDatabase();

        setOnCLickListeners();

    }

    private void initialization() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        activeUsersReference = firebaseDatabase.getReference("activeUsers");
        shelterAdminReference = firebaseDatabase.getReference("shelterAdmin");
        databaseReference = firebaseDatabase.getReference();

        loggedUid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        menuImageView = findViewById(R.id.homeScreenTopBarOpenMenu_imageView);

        userHomeScreenContentLinearLayout = findViewById(R.id.userHomeScreenContent_linearLayout);
        userContentTopLeftAdoptRelativeLayout = findViewById(R.id.userHomeScreenContentTopLeft_relativeLayout);
        userContentTopRightSponsorRelativeLayout = findViewById(R.id.userHomeScreenContentTopRight_relativeLayout);
        userContentBottomLeftVolunteerRelativeLayout = findViewById(R.id.userHomeScreenContentBottomLeft_relativeLayout);
        userContentBottomRightMaterialSupportRelativeLayout = findViewById(R.id.userHomeScreenContentBottomRight_relativeLayout);

        userHomeScreenBottomBarLinearLayout = findViewById(R.id.userHomeScreenBottomBar_linearLayout);
        userBottomBarHomeTextView = findViewById(R.id.userHomeScreenBottomBarHome_textView);
        userBottomBarAdoptTextView = findViewById(R.id.userHomeScreenBottomBarAdopt_textView);
        userBottomBarDonateTextView = findViewById(R.id.userHomeScreenBottomBarDonate_textView);
        userBottomBarSupplyTextView = findViewById(R.id.userHomeScreenBottomBarMaterialSupport_textView);
        userBottomBarProfileTextView = findViewById(R.id.userHomeScreenBottomBarProfile_textView);

        adminHomeScreenContentLinearLayout = findViewById(R.id.adminHomeScreenContent_linearLayout);
        adminContentTopLeftAddRelativeLayout = findViewById(R.id.adminHomeScreenContentTopLeft_relativeLayout);
        adminContentTopRightDonationRequestRelativeLayout = findViewById(R.id.adminHomeScreenContentTopRight_relativeLayout);
        adminContentBottomLeftVolunteerRequestRelativeLayout = findViewById(R.id.adminHomeScreenContentBottomLeft_relativeLayout);
        adminContentBottomRightMaterialSupportRelativeLayout = findViewById(R.id.adminHomeScreenContentBottomRight_relativeLayout);

        adminHomeScreenBottomBarLinearLayout = findViewById(R.id.adminHomeScreenBottomBar_linearLayout);
        adminBottomBarHomeTextView = findViewById(R.id.adminHomeScreenBottomBarHome_textView);
        adminBottomBarPetsTextView = findViewById(R.id.adminHomeScreenBottomBarViewPets_textView);
        adminBottomBarDonationRequestsTextView = findViewById(R.id.adminHomeScreenBottomBarViewDonations_textView);
        adminBottomBarSupplyRequestsTextView = findViewById(R.id.adminHomeScreenBottomBarViewNeededMaterialSupport_textView);
        adminBottomBarProfileTextView = findViewById(R.id.adminHomeScreenBottomBarProfile_textView);
    }

    private void saveConnectedUserIdToDatabase() {
        String uId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        shelterAdminReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String shelterAdminId = Objects.requireNonNull(snapshot.child("uId").getValue()).toString();
                if (shelterAdminId.equals(uId)) { // admin is connected
                    adminHomeScreenContentLinearLayout.setVisibility(View.VISIBLE);
                    adminHomeScreenBottomBarLinearLayout.setVisibility(View.VISIBLE);
                    userHomeScreenContentLinearLayout.setVisibility(View.GONE);
                    userHomeScreenBottomBarLinearLayout.setVisibility(View.GONE);
                } else { // user is connected
                    activeUsersReference.child(uId).child("uId").setValue(uId);
                    userHomeScreenContentLinearLayout.setVisibility(View.VISIBLE);
                    userHomeScreenBottomBarLinearLayout.setVisibility(View.VISIBLE);
                    adminHomeScreenContentLinearLayout.setVisibility(View.GONE);
                    adminHomeScreenBottomBarLinearLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setOnCLickListeners() {
        final boolean[] filtersNodeHasLoggedUIdChild = {false};

        databaseReference.child("filters").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                filtersNodeHasLoggedUIdChild[0] = snapshot.hasChild(loggedUid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        userContentTopLeftAdoptRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filtersNodeHasLoggedUIdChild[0]) {
                    sendUserToSeeListOfPetsActivity();
                } else {
                    sendUserToFilterActivity();
                }
            }
        });

        userContentTopRightSponsorRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        userContentBottomLeftVolunteerRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        userContentBottomRightMaterialSupportRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        userBottomBarHomeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadScreen();
            }
        });

        userBottomBarAdoptTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filtersNodeHasLoggedUIdChild[0]) {
                    sendUserToSeeListOfPetsActivity();
                } else {
                    sendUserToFilterActivity();
                }
            }
        });

        userBottomBarDonateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        userBottomBarSupplyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        userBottomBarProfileTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("profiles").hasChild(loggedUid)) {
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



        adminContentTopLeftAddRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(HomeActivity.this, AddNewPetActivity.class));
                }
        });

        adminBottomBarPetsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, SeeListOfPetsActivity.class));
            }
        });

        adminBottomBarProfileTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("profiles").hasChild(loggedUid)) {
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


    }

    private void sendUserToSeeListOfPetsActivity() {
        Intent intent = new Intent(HomeActivity.this, SeeListOfPetsActivity.class);
        startActivity(intent);
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