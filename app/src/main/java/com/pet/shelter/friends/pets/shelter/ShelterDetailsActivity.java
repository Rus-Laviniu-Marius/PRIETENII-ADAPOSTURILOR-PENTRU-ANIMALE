package com.pet.shelter.friends.pets.shelter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.pets.sheltered.ShelteredPetDetailsActivity;
import com.squareup.picasso.Picasso;

public class ShelterDetailsActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private MaterialToolbar materialToolbar;
    private ShapeableImageView shelterLogo;
    private MaterialButton call, mail, visitWeb, directions, shelteredPets;
    private MaterialTextView name, ourMission, ourAdoptionPolicy;
    private DatabaseReference sheltersReference;
    private String shelterName, shelterPhone, shelterEmail, shelterWebPageLink, shelterAddress, shelterMission,
            shelterAdoptionPolicy, profileImageDownloadLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_details);

        constraintLayout = findViewById(R.id.shelterDetails_constraintLayout);
        materialToolbar = findViewById(R.id.shelterDetails_materialToolbar);
        shelterLogo = findViewById(R.id.shelterDetailsImage_shapeImageView);
        call = findViewById(R.id.shelterDetailsPhone_materialButton);
        mail = findViewById(R.id.shelterDetailsMail_materialButton);
        visitWeb = findViewById(R.id.shelterDetailsWeb_materialButton);
        directions = findViewById(R.id.shelterDetailsDirections_materialButton);
        shelteredPets = findViewById(R.id.shelteredDetailsShelterPets_materialButton);
        name = findViewById(R.id.shelterDetailsName_materialTextView);
        ourMission = findViewById(R.id.shelterDetailsOurMissionDescription_materialTextView);
        ourAdoptionPolicy = findViewById(R.id.shelterDetailsOurAdoptionPolicyDescription_materialTextView);
        sheltersReference = FirebaseDatabase.getInstance().getReference("registeredShelters");

        String shelterAdministratorId = getIntent().getStringExtra("shelterAdministratorId");

        sheltersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(shelterAdministratorId)) {
                    ShelterData shelterData = snapshot.child(shelterAdministratorId).getValue(ShelterData.class);
                    if (shelterData != null) {
                        shelterAddress = shelterData.getAddress();
                        shelterName = shelterData.getName();
                        shelterAdoptionPolicy = shelterData.getOurAdoptionPolicy();
                        shelterMission = shelterData.getOurMission();
                        shelterPhone = shelterData.getPhoneNumber();
                        shelterEmail = shelterData.getEmail();
                        shelterWebPageLink = shelterData.getWebPageLink();
                        profileImageDownloadLink = shelterData.getProfileImageDownloadLink();

                        Picasso.get().load(profileImageDownloadLink).into(shelterLogo);
                        name.setText(shelterName);
                        ourMission.setText(shelterMission);
                        ourAdoptionPolicy.setText(shelterAdoptionPolicy);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShelterDetailsActivity.this, ShelteredPetDetailsActivity.class);
                intent.putExtra("petName", getIntent().getStringExtra("petName"));
                intent.putExtra("petType", getIntent().getStringExtra("petType"));
                intent.putExtra("petBreed", getIntent().getStringExtra("petBreed"));
                intent.putExtra("petAge", getIntent().getStringExtra("petAge"));
                intent.putExtra("petSize", getIntent().getStringExtra("petSize"));
                intent.putExtra("petSex", getIntent().getStringExtra("petSex"));
                intent.putExtra("petDescription", getIntent().getStringExtra("petDescription"));
                intent.putExtra("petImage1DownloadLink", getIntent().getStringExtra("petImage1DownloadLink"));
                startActivity(intent);
                finish();
            }
        });

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_share) {
                    String message = "Shelter " + shelterName + " has the mission to " + shelterMission
                            + " and an adoption policy like this: " + shelterAdoptionPolicy;
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                }

                return true;
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + shelterPhone));
                startActivity(intent);
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailSelectorIntent = new Intent(Intent.ACTION_SENDTO);
                emailSelectorIntent.setData(Uri.parse("mailto:"));

                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {shelterEmail});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "PetShelterFriends " + shelterName + " more information");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Just want to know about the shelter");
                emailIntent.setSelector( emailSelectorIntent );
                startActivity(emailIntent);
            }
        });

        visitWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shelterWebPageLink.startsWith("http")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                    startActivity(browserIntent);
                } else {
                    Snackbar snackbar = Snackbar
                            .make(constraintLayout, "No linked web!", Snackbar.LENGTH_LONG)
                            .setText("Please contact shelter administrator to update shelter details")
                            .setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                    snackbar.show();
                }
            }
        });

        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q="+shelterAddress));
                startActivity(intent);
            }
        });

        shelteredPets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}