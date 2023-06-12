package com.pet.shelter.friends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class WhoAreYouActivity extends AppCompatActivity {
    private DatabaseReference roles;
    private String loggedUserId;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_are_you);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        roles = firebaseDatabase.getReference("roles");
        constraintLayout = findViewById(R.id.whoAreYou_constraintLayout);

        loggedUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        MaterialButton userMaterialButton = findViewById(R.id.whoAreYouUser_materialButton);
        MaterialButton shelterAdminMaterialButton = findViewById(R.id.whoAreYouShelterAdmin_materialButton);
        MaterialButton petTrainerMaterialButton = findViewById(R.id.whoAreYouPetTrainer_materialButton);
        MaterialButton veterinarianMaterialButton = findViewById(R.id.whoAreYouVeterinarian_materialButton);
        MaterialButton shelterKeeperMaterialButton = findViewById(R.id.whoAreYouShelterKeeper_materialButton);

        userMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roles.child(loggedUserId).child("user").setValue(true);
                Intent intent = new Intent(WhoAreYouActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        shelterAdminMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roles.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(loggedUserId).hasChild("user")) {
                            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(WhoAreYouActivity.this)
                                    .setTitle("Warning!")
                                    .setMessage("As a shelter administrator, " +
                                            "you will no longer be able to switch accounts until you delete " +
                                            "your administrator account."+"\n"+ "Do you agree with this " +
                                            "condition?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            snapshot.child(loggedUserId).child("user").getRef().removeValue();
                                            Intent intent = new Intent(WhoAreYouActivity.this, HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Snackbar snackbar = Snackbar.make(constraintLayout, "Please select role", Snackbar.LENGTH_LONG);
                                            snackbar.show();
                                        }
                                    });
                            materialAlertDialogBuilder.create().show();
                        } else {
                            roles.child(loggedUserId).child("shelterAdministrator").setValue(true);
                            Intent intent = new Intent(WhoAreYouActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
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
}