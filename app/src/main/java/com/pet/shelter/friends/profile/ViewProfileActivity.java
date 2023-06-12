package com.pet.shelter.friends.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pet.shelter.friends.HomeActivity;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.WhoAreYouActivity;
import com.pet.shelter.friends.authentication.LoginActivity;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class ViewProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference activeUsersReference, shelterAdminReference, usersReference, databaseReference;
    private MaterialToolbar materialToolbar;
    private MaterialTextView userNameMaterialTextView;
    private ShapeableImageView userProfileShapeImageView;
    private MaterialButton settingsMaterialButton, personalDataMaterialButton, logoutMaterialButton,
            pastAttendedEventsMaterialButton, activeServicesMaterialButton, changeRoleMaterialButton;
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

        materialToolbar = findViewById(R.id.viewUserProfileScreenTop_materialToolbar);
        userProfileShapeImageView = findViewById(R.id.viewUserProfileImage_imageView);
        userNameMaterialTextView = findViewById(R.id.viewUserProfileUserName_materialTextView);
        settingsMaterialButton = findViewById(R.id.viewUserProfileSettings_materialButton);
        personalDataMaterialButton = findViewById(R.id.viewUserProfilePersonalData_materialButton);
        pastAttendedEventsMaterialButton = findViewById(R.id.viewUserProfilePastAttendedEvents_materialButton);
        activeServicesMaterialButton = findViewById(R.id.viewUserProfileActiveServices_materialButton);
        logoutMaterialButton = findViewById(R.id.viewUserProfileLogout_materialButton);
        changeRoleMaterialButton = findViewById(R.id.viewUserProfileChangeRole_materialButton);

        loggedUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

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

                            Picasso.get().load(profileImageDownloadLink).into(userProfileShapeImageView);
                            userNameMaterialTextView.setText(name);
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

        settingsMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewProfileActivity.this, SettingsActivity.class));
            }
        });

        personalDataMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewProfileActivity.this, UserPersonalDataActivity.class));
            }
        });

        pastAttendedEventsMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewProfileActivity.this, PastAttendedEventsActivity.class));
            }
        });

        activeServicesMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewProfileActivity.this, ActiveServicesActivity.class));
            }
        });

        logoutMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                removeConnectedUserFromActiveUsers();
                firebaseAuth.signOut();
                startActivity(new Intent(ViewProfileActivity.this, LoginActivity.class));
            }
        });

        changeRoleMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewProfileActivity.this, WhoAreYouActivity.class));
            }
        });

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewProfileActivity.this, HomeActivity.class));
            }
        });

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_delete) {
                    MaterialAlertDialogBuilder deletingProfileDialog = new MaterialAlertDialogBuilder(ViewProfileActivity.this)
                            .setTitle("Deleting profile")
                            .setMessage("Are you sure you want to delete your profile? All your data and preferences will be deleted and can not be restored")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(ViewProfileActivity.this, "Deleting profile", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    deletingProfileDialog.create();
                    deletingProfileDialog.show();
                }
                return true;
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