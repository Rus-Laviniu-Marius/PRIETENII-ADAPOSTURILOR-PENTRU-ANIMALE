package com.pet.shelter.friends.pets.lost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.pet.shelter.friends.R;
import com.squareup.picasso.Picasso;


public class LostPetDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_pet_details);

        MaterialToolbar materialToolbar = findViewById(R.id.lostPetDetails_materialToolbar);
        ShapeableImageView shapeableImageView = findViewById(R.id.lostPetDetailsImage_shapeImageView);
        MaterialTextView name = findViewById(R.id.lostPetDetailsName_materialTextView);;
        MaterialTextView breed = findViewById(R.id.lostPetDetailsBreed_materialTextView);;
        MaterialTextView age = findViewById(R.id.lostPetDetailsAge_materialTextView);;
        MaterialTextView size = findViewById(R.id.lostPetDetailsSize_materialTextView);;
        MaterialTextView sex = findViewById(R.id.lostPetDetailsSex_materialTextView);;
        MaterialTextView description = findViewById(R.id.lostPetDetailsDescription_materialTextView);;
        MaterialButton found = findViewById(R.id.lostPetDetails_materialButton);
        Chip spayedOrNeuteredChip = findViewById(R.id.lostPetDetailsSpayedOrNeutered_chip);
        Chip dewormedChip = findViewById(R.id.lostPetDetailsDewormed_chip);
        Chip vaccinesChip = findViewById(R.id.lostPetDetailsVaccines_chip);

        String petImage1DownloadLink = getIntent().getStringExtra("petImage1DownloadLink");
        String petName = getIntent().getStringExtra("petName");
        String petType = getIntent().getStringExtra("petType");
        String petBreed = getIntent().getStringExtra("petBreed");
        String petAge = getIntent().getStringExtra("petAge");
        String petSize = getIntent().getStringExtra("petSize");
        String petSex = getIntent().getStringExtra("petSex");
        String petDescription = getIntent().getStringExtra("petDescription");
        String ownerEmail = getIntent().getStringExtra("ownerEmail");
        String ownerPhoneNumber = getIntent().getStringExtra("ownerPhoneNumber");
        String spayedOrNeutered = getIntent().getStringExtra("spayedOrNeutered");
        String dewormed = getIntent().getStringExtra("dewormed");
        String vaccines = getIntent().getStringExtra("vaccines");

        Picasso.get().load(petImage1DownloadLink).into(shapeableImageView);

        name.setText(petName);
        breed.setText(petBreed);
        age.setText(petAge);
        size.setText(petSize);
        sex.setText(petSex);
        description.setText(petDescription);

        if (spayedOrNeutered == null) {
            spayedOrNeuteredChip.setVisibility(View.GONE);
        } else if (spayedOrNeutered.contentEquals("yes")) {
            spayedOrNeuteredChip.setVisibility(View.VISIBLE);
        } else if (spayedOrNeutered.contentEquals("no")) {
            spayedOrNeuteredChip.setVisibility(View.GONE);
        }

        if (dewormed == null) {
            dewormedChip.setVisibility(View.GONE);
        } else if (dewormed.contentEquals("yes")) {
            dewormedChip.setVisibility(View.VISIBLE);
        } else if (dewormed.contentEquals("no")) {
            dewormedChip.setVisibility(View.GONE);
        }

        if (vaccines == null) {
            vaccinesChip.setVisibility(View.GONE);
        } else if (vaccines.contentEquals("yes")) {
            vaccinesChip.setVisibility(View.VISIBLE);
        } else if (vaccines.contentEquals("no")) {
            vaccinesChip.setVisibility(View.GONE);
        }

        found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LostPetBottomSheetDialog lostPetBottomSheetDialog = new LostPetBottomSheetDialog(ownerEmail, ownerPhoneNumber, petName);
                lostPetBottomSheetDialog.show(getSupportFragmentManager(), "LostPetBottomSheet");
            }
        });


        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_share) {
                    String message = petName + " is a " + petSex + " " + petType + " of " + petBreed +
                            " breed and " + petSize + " size. Is at his " + petAge +
                            " ages. This is pet's description: " + "\"" + petDescription + "\"";
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                    sendIntent.setType("text/plain");
                    Intent shareIntent = Intent.createChooser(sendIntent,"");
                    startActivity(shareIntent);
                }

                return true;
            }
        });
    }
}