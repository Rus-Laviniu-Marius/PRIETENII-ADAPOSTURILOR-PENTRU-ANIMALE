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
    private DatabaseReference profiles, roles;
    private MaterialToolbar materialToolbar;
    private MaterialTextView nameMaterialTextView;
    private ShapeableImageView profileShapeImageView;
    private MaterialButton settingsMaterialButton, personalDataMaterialButton, logoutMaterialButton,
            pastAttendedEventsMaterialButton, activeServicesMaterialButton, changeRoleMaterialButton;
    private String loggedUserId;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        profiles = FirebaseDatabase.getInstance().getReference("profiles");
        roles = FirebaseDatabase.getInstance().getReference("roles");

        materialToolbar = findViewById(R.id.viewProfileScreenTop_materialToolbar);
        profileShapeImageView = findViewById(R.id.viewProfileImage_shapeImageView);
        nameMaterialTextView = findViewById(R.id.viewProfileName_materialTextView);
        settingsMaterialButton = findViewById(R.id.viewProfileSettings_materialButton);
        personalDataMaterialButton = findViewById(R.id.viewProfilePersonalData_materialButton);
        pastAttendedEventsMaterialButton = findViewById(R.id.viewProfilePastAttendedEvents_materialButton);
        activeServicesMaterialButton = findViewById(R.id.viewProfileActiveServices_materialButton);
        logoutMaterialButton = findViewById(R.id.viewProfileLogout_materialButton);
        changeRoleMaterialButton = findViewById(R.id.viewProfileChangeRole_materialButton);

        loggedUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        readDataFromDatabase();

        setOnClickListeners();
    }

    private void readDataFromDatabase() {

        roles.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(loggedUserId).hasChild("user")) {
                    role = "user";
                } else {
                    changeRoleMaterialButton.setVisibility(View.GONE);
                    role = "shelterAdministrator";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profiles.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String profileImageDownloadLink, name;
                if (role.equals("user")) {
                    profileImageDownloadLink = Objects.requireNonNull(snapshot.child("users").child(loggedUserId).child("profileImageDownloadLink").getValue()).toString();
                    name = Objects.requireNonNull(snapshot.child("users").child(loggedUserId).child("name").getValue()).toString();
                } else {
                    profileImageDownloadLink = Objects.requireNonNull(snapshot.child("sheltersAdministrators").child(loggedUserId).child("profileImageDownloadLink").getValue()).toString();
                    name = Objects.requireNonNull(snapshot.child("sheltersAdministrators").child(loggedUserId).child("name").getValue()).toString();
                }
                Picasso.get().load(profileImageDownloadLink).centerInside().fit().into(profileShapeImageView);
                nameMaterialTextView.setText(name);
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
                finish();
            }
        });

        personalDataMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewProfileActivity.this, UserPersonalDataActivity.class));
                finish();
            }
        });

        pastAttendedEventsMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewProfileActivity.this, PastAttendedEventsActivity.class));
                finish();
            }
        });

        activeServicesMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewProfileActivity.this, ActiveServicesActivity.class));
                finish();
            }
        });

        logoutMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(ViewProfileActivity.this, LoginActivity.class));
                finish();
            }
        });

        changeRoleMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewProfileActivity.this, WhoAreYouActivity.class));
                finish();
            }
        });

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewProfileActivity.this, HomeActivity.class));
                finish();
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
                                    if (role.equals("shelterAdministrator")) {
                                        profiles.child("sheltersAdministrators").child(loggedUserId).removeValue();
                                    } else {
                                        profiles.child("users").child(loggedUserId).removeValue();
                                    }
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
}