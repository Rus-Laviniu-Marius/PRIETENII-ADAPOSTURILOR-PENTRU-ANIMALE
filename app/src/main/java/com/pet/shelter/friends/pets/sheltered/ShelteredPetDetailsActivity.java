package com.pet.shelter.friends.pets.sheltered;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.fragments.bottom_app_bar.pets.tabs.details.ShelteredPetDetailsViewPager2Adapter;
import com.pet.shelter.friends.pets.adoption.AdoptionApplicationParagraphsActivity;
import com.pet.shelter.friends.pets.shelter.ShelterDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ShelteredPetDetailsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheltered_pet_details);

        DatabaseReference petsReference = FirebaseDatabase.getInstance().getReference("pets");
        MaterialToolbar materialToolbar = findViewById(R.id.shelteredPetDetails_materialToolbar);
        ShapeableImageView shapeableImageView = findViewById(R.id.shelteredPetDetailsImage_shapeImageView);
        MaterialTextView name = findViewById(R.id.shelteredPetDetailsName_materialTextView);;
        MaterialTextView breed = findViewById(R.id.shelteredPetDetailsBreed_materialTextView);;
        MaterialTextView age = findViewById(R.id.shelteredPetDetailsAge_materialTextView);;
        MaterialTextView size = findViewById(R.id.shelteredPetDetailsSize_materialTextView);;
        MaterialTextView sex = findViewById(R.id.shelteredPetDetailsSex_materialTextView);;
        MaterialTextView description = findViewById(R.id.shelteredPetDetailsDescription_materialTextView);;
        MaterialButton adopt = findViewById(R.id.shelteredPetDetailsAdopt_materialButton);
        MaterialButton shelterDetails = findViewById(R.id.shelteredPetDetailsShelter_materialButton);
        MaterialButton favoriteMaterialButton = findViewById(R.id.shelteredPetDetailsFavorite_materialButton);
        Chip spayedOrNeuteredChip = findViewById(R.id.shelteredPetDetailsSpayedOrNeutered_chip);
        Chip dewormedChip = findViewById(R.id.shelteredPetDetailsDewormed_chip);
        Chip vaccinesChip = findViewById(R.id.shelteredPetDetailsVaccines_chip);
        Chip fitForChildrenChip = findViewById(R.id.shelteredPetDetailsFitForChildren_chip);
        Chip fitForGuardingChip = findViewById(R.id.shelteredPetDetailsFitForGuarding_chip);
        Chip friendlyWithPetsChip = findViewById(R.id.shelteredPetDetailsFriendlyWithPets_chip);

        tabLayout = findViewById(R.id.shelteredPetDetails_tabLayout);
        viewPager2 = findViewById(R.id.shelteredPetDetails_viewPager2);
        ShelteredPetDetailsViewPager2Adapter shelteredPetDetailsViewPager2Adapter = new ShelteredPetDetailsViewPager2Adapter(this);
        viewPager2.setAdapter(shelteredPetDetailsViewPager2Adapter);

        tabLayout.addTab(tabLayout.newTab().setText("Pet"));
        tabLayout.addTab(tabLayout.newTab().setText("Shelter"));

        String petImage1DownloadLink = getIntent().getStringExtra("petImage1DownloadLink");
        String petName = getIntent().getStringExtra("petName");
        String petType = getIntent().getStringExtra("petType");
        String petBreed = getIntent().getStringExtra("petBreed");
        String petAge = getIntent().getStringExtra("petAge");
        String petSize = getIntent().getStringExtra("petSize");
        String petSex = getIntent().getStringExtra("petSex");
        String petDescription = getIntent().getStringExtra("petDescription");
        String shelterAdministratorId = getIntent().getStringExtra("shelterAdministratorId");
        String spayedOrNeutered = getIntent().getStringExtra("spayedOrNeutered");
        String dewormed = getIntent().getStringExtra("dewormed");
        String vaccines = getIntent().getStringExtra("vaccines");
        String fitForChildren = getIntent().getStringExtra("fitForChildren");
        String fitForGuarding = getIntent().getStringExtra("fitForGuarding");
        String friendlyWithPets = getIntent().getStringExtra("friendlyWithPets");
        String favorite = getIntent().getStringExtra("isFavorite");

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

        if (fitForChildren == null) {
            fitForChildrenChip.setVisibility(View.GONE);
        } else if (fitForChildren.contentEquals("yes")) {
            fitForChildrenChip.setVisibility(View.VISIBLE);
        } else if (fitForChildren.contentEquals("no")) {
            fitForChildrenChip.setVisibility(View.GONE);
        }

        if (fitForGuarding == null) {
            fitForGuardingChip.setVisibility(View.GONE);
        } else if (fitForGuarding.contentEquals("yes")) {
            fitForGuardingChip.setVisibility(View.VISIBLE);
        } else if (fitForGuarding.contentEquals("no")) {
            fitForGuardingChip.setVisibility(View.GONE);
        }

        if (friendlyWithPets == null) {
            friendlyWithPetsChip.setVisibility(View.GONE);
        } else if (friendlyWithPets.contentEquals("yes")) {
            friendlyWithPetsChip.setVisibility(View.VISIBLE);
        } else if (friendlyWithPets.contentEquals("no")) {
            friendlyWithPetsChip.setVisibility(View.GONE);
        }

        if (favorite != null) {
            if (favorite.equals("yes")) {
                favoriteMaterialButton.setIcon(AppCompatResources.getDrawable(ShelteredPetDetailsActivity.this, R.drawable.favorite_filled_24));
            } else if (favorite.equals("no")) {
                favoriteMaterialButton.setIcon(AppCompatResources.getDrawable(ShelteredPetDetailsActivity.this, R.drawable.favorite_outlined_24));
            }
        }

        favoriteMaterialButton.setOnClickListener(new View.OnClickListener() {
            boolean isFavorite = false;
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    favoriteMaterialButton.setIcon(AppCompatResources.getDrawable(ShelteredPetDetailsActivity.this, R.drawable.favorite_outlined_24));
                    petsReference.child("Sheltered").child(petName+"_"+petDescription).child("favorite").setValue("no");
                } else {
                    favoriteMaterialButton.setIcon(AppCompatResources.getDrawable(ShelteredPetDetailsActivity.this, R.drawable.favorite_filled_24));
                    petsReference.child("Sheltered").child(petName+"_"+petDescription).child("favorite").setValue("yes");

                }
                isFavorite = !isFavorite;
            }
        });

        adopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShelteredPetDetailsActivity.this, AdoptionApplicationParagraphsActivity.class);
                intent.putExtra("shelterAdministratorId", shelterAdministratorId);
                intent.putExtra("petName", petName);
                intent.putExtra("petType", petType);
                intent.putExtra("petBreed", petBreed);
                intent.putExtra("petAge", petAge);
                intent.putExtra("petSize", petSize);
                intent.putExtra("petSex", petSex);
                intent.putExtra("petDescription", petDescription);
                intent.putExtra("petImage1DownloadLink", petImage1DownloadLink);
                intent.putExtra("spayedOrNeutered", spayedOrNeutered);
                intent.putExtra("dewormed", dewormed);
                intent.putExtra("vaccines", vaccines);
                intent.putExtra("fitForChildren", fitForChildren);
                intent.putExtra("fitForGuarding", fitForGuarding);
                intent.putExtra("friendlyWithPets", friendlyWithPets);
                intent.putExtra("isFavorite", favorite);
                startActivity(intent);
                finish();
            }
        });

        shelterDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShelteredPetDetailsActivity.this, ShelterDetailsActivity.class);
                intent.putExtra("shelterAdministratorId", shelterAdministratorId);
                intent.putExtra("petName", petName);
                intent.putExtra("petType", petType);
                intent.putExtra("petBreed", petBreed);
                intent.putExtra("petAge", petAge);
                intent.putExtra("petSize", petSize);
                intent.putExtra("petSex", petSex);
                intent.putExtra("petDescription", petDescription);
                intent.putExtra("petImage1DownloadLink", petImage1DownloadLink);
                startActivity(intent);
                finish();
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

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                    case 1:
                        Objects.requireNonNull(tabLayout.getTabAt(position)).select();
                }
                super.onPageSelected(position);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}