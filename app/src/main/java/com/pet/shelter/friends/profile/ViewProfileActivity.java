package com.pet.shelter.friends.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pet.shelter.friends.HomeActivity;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.authentication.LoginActivity;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class ViewProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference activeUsersReference, shelterAdminReference, usersReference, databaseReference;

    private TextView userNameTextView, userPersonalDataTextView, favoritePetsTextView, historyEventsTextView, petPreferencesTextView;
    private ImageView userProfileImageView, userRatingImageView, back;
    private Button logoutButton;

    private String loggedUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        activeUsersReference = firebaseDatabase.getReference("activeUsers");
        shelterAdminReference = firebaseDatabase.getReference("shelterAdmin");
        usersReference = firebaseDatabase.getReference("profiles");
        databaseReference = firebaseDatabase.getReference();

        loggedUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        userNameTextView = findViewById(R.id.viewProfileUserName_textView);
        favoritePetsTextView = findViewById(R.id.viewProfileSeeFavoritePets_textView);
        userPersonalDataTextView = findViewById(R.id.viewProfileSeePersonalData_textView);
        historyEventsTextView = findViewById(R.id.viewProfileSeeHistoryEvents_textView);
        petPreferencesTextView = findViewById(R.id.viewProfileSeePetPreferences_textView);

        userProfileImageView = findViewById(R.id. viewProfileUser_imageView);
        userRatingImageView = findViewById(R.id.viewProfileRating_imageView);
        back = findViewById(R.id.viewProfileBack_imageView);

        logoutButton = findViewById(R.id.viewProfileLogout_button);

        readDataFromDatabase();

        setOnClickListeners();
    }

    private void readDataFromDatabase() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("profiles")) {
                    usersReference.child(loggedUserId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String profileImageDownloadLink = Objects.requireNonNull(snapshot.child("profileImageDownloadLink").getValue()).toString();
                            String name = Objects.requireNonNull(snapshot.child("name").getValue()).toString();

                            Picasso.get().load(profileImageDownloadLink).into(userProfileImageView);
                            userNameTextView.setText(name);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void setOnClickListeners() {

        userPersonalDataTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewProfileActivity.this, UserPersonalDataActivity.class));
            }
        });

        historyEventsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                removeConnectedUserFromActiveUsers();
                firebaseAuth.signOut();
                startActivity(new Intent(ViewProfileActivity.this, LoginActivity.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewProfileActivity.this, HomeActivity.class));
            }
        });
    }

    private void removeConnectedUserFromActiveUsers() {

        firebaseDatabase.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("activeUsers"))
                    activeUsersReference.child(loggedUserId).removeValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}