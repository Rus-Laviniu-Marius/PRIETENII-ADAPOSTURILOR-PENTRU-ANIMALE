package com.pet.shelter.friends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.pet.shelter.friends.fragments.bottom_navigation_bar.BottomNavigationBarFirstFragment;

public class UserHomeActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

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
        setContentView(R.layout.activity_user_home);

        bottomNavigationView = findViewById(R.id.userHomeScreen_bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.bottomNavigationBar_home);

//        initialization();
//
//
//        saveConnectedUserIdToDatabase();
//
//        setOnCLickListeners();

    }
    BottomNavigationBarFirstFragment bottomNavigationBarFirstFragment = new BottomNavigationBarFirstFragment();

//    private void initialization() {
//        firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        activeUsersReference = firebaseDatabase.getReference("activeUsers");
//        shelterAdminReference = firebaseDatabase.getReference("shelterAdmin");
//        databaseReference = firebaseDatabase.getReference();
//
//        loggedUid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
//
//    }
//
//    private void saveConnectedUserIdToDatabase() {
//        String uId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
//        shelterAdminReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String shelterAdminId = Objects.requireNonNull(snapshot.child("uId").getValue()).toString();
//                if (shelterAdminId.equals(uId)) { // admin is connected
//                    adminHomeScreenContentLinearLayout.setVisibility(View.VISIBLE);
//                    adminHomeScreenBottomBarLinearLayout.setVisibility(View.VISIBLE);
//                    userHomeScreenContentLinearLayout.setVisibility(View.GONE);
//                    userHomeScreenBottomBarLinearLayout.setVisibility(View.GONE);
//                } else { // user is connected
//                    activeUsersReference.child(uId).child("uId").setValue(uId);
//                    userHomeScreenContentLinearLayout.setVisibility(View.VISIBLE);
//                    userHomeScreenBottomBarLinearLayout.setVisibility(View.VISIBLE);
//                    adminHomeScreenContentLinearLayout.setVisibility(View.GONE);
//                    adminHomeScreenBottomBarLinearLayout.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    private void setOnCLickListeners() {
//        final boolean[] filtersNodeHasLoggedUIdChild = {false};
//
//        databaseReference.child("filters").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                filtersNodeHasLoggedUIdChild[0] = snapshot.hasChild(loggedUid);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        menuImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(UserHomeActivity.this, SettingsActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        userContentTopLeftAdoptRelativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (filtersNodeHasLoggedUIdChild[0]) {
//                    sendUserToSeeListOfPetsActivity();
//                } else {
//                    sendUserToFilterActivity();
//                }
//            }
//        });
//
//        userContentTopRightSponsorRelativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        userContentBottomLeftVolunteerRelativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        userContentBottomRightMaterialSupportRelativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        userBottomBarHomeTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                reloadScreen();
//            }
//        });
//
//        userBottomBarAdoptTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (filtersNodeHasLoggedUIdChild[0]) {
//                    sendUserToSeeListOfPetsActivity();
//                } else {
//                    sendUserToFilterActivity();
//                }
//            }
//        });
//
//        userBottomBarDonateTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        userBottomBarSupplyTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        userBottomBarProfileTextView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    databaseReference.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.child("profiles").hasChild(loggedUid)) {
//                                Intent intent = new Intent(UserHomeActivity.this, ViewProfileActivity.class);
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent(UserHomeActivity.this, CreateUserProfileActivity.class);
//                                startActivity(intent);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//            });
//
//
//
//        adminContentTopLeftAddRelativeLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    startActivity(new Intent(UserHomeActivity.this, AddNewPetActivity.class));
//                }
//        });
//
//        adminBottomBarPetsTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(UserHomeActivity.this, SeeListOfPetsActivity.class));
//            }
//        });
//
//        adminBottomBarProfileTextView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                databaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.child("profiles").hasChild(loggedUid)) {
//                            Intent intent = new Intent(UserHomeActivity.this, ViewProfileActivity.class);
//                            startActivity(intent);
//                        } else {
//                            Intent intent = new Intent(UserHomeActivity.this, CreateUserProfileActivity.class);
//                            startActivity(intent);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });
//
//
//    }
//
//    private void sendUserToSeeListOfPetsActivity() {
//        Intent intent = new Intent(UserHomeActivity.this, SeeListOfPetsActivity.class);
//        startActivity(intent);
//    }
//
//    private void reloadScreen() {
//        Intent intent = new Intent(UserHomeActivity.this, UserHomeActivity.class);
//        startActivity(intent);
//    }

//    private void sendUserToFilterActivity() {
//        Intent intent = new Intent(UserHomeActivity.this, FilterPetPreferencesActivity.class);
//        startActivity(intent);
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottomNavigationBar_home:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, bottomNavigationBarFirstFragment);
        }
        return false;
    }
}