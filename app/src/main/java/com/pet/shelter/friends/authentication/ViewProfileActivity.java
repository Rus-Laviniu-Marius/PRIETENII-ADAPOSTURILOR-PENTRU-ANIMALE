package com.pet.shelter.friends.authentication;

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
import com.pet.shelter.friends.adoption.SeeListOfFavoritePetsActivity;

import java.util.Objects;


public class ViewProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference activeUsersReference, shelterAdminReference;

    private TextView userNameTextView, userPersonalDataTextView, favoritePetsTextView, historyEventsTextView, petPreferencesTextView;
    private ImageView userProfileImageView, userRatingImageView, back;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        activeUsersReference = firebaseDatabase.getReference("activeUsers");
        shelterAdminReference = firebaseDatabase.getReference("shelterAdmin");

        userNameTextView = findViewById(R.id.viewProfileUserName_textView);
        favoritePetsTextView = findViewById(R.id.viewProfileSeeFavoritePets_textView);
        userPersonalDataTextView = findViewById(R.id.viewProfileSeePersonalData_textView);
        historyEventsTextView = findViewById(R.id.viewProfileSeeHistoryEvents_textView);
        petPreferencesTextView = findViewById(R.id.viewProfileSeePetPreferences_textView);

        userProfileImageView = findViewById(R.id. viewProfileUser_imageView);
        userRatingImageView = findViewById(R.id.viewProfileRating_imageView);
        back = findViewById(R.id.viewProfileBack_imageView);

        logoutButton = findViewById(R.id.viewProfileLogout_button);

        setOnClickListeners();
    }

    private void setOnClickListeners() {

        favoritePetsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewProfileActivity.this, SeeListOfFavoritePetsActivity.class));
            }
        });

        userPersonalDataTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        historyEventsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        petPreferencesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeConnectedUserFromActiveUsers();
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

        String loggedUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
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
//        shelterAdminReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String shelterAdminId = Objects.requireNonNull(snapshot.child("uId").getValue()).toString();
//                if (!shelterAdminId.equals(loggedUserId)) {
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
}