package com.pet.shelter.friends.pets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.fragments.bottom_app_bar.pets.tabs.details.ShelteredPetDetailsPetTabFragment;
import com.pet.shelter.friends.fragments.bottom_app_bar.pets.tabs.details.ShelteredPetDetailsShelterTabFragment;
import com.pet.shelter.friends.fragments.bottom_app_bar.pets.tabs.details.ShelteredPetDetailsViewPager2Adapter;
import com.pet.shelter.friends.pets.adoption.AdoptionApplicationParagraphsActivity;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ShelteredPetDetailsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheltered_pet_details);

        MaterialToolbar materialToolbar = findViewById(R.id.petDetails_materialToolbar);
        ShapeableImageView shapeableImageView = findViewById(R.id.petDetailsImage_shapeImageView);
        MaterialTextView name = findViewById(R.id.petDetailsName_materialTextView);;
        MaterialTextView breed = findViewById(R.id.petDetailsBreed_materialTextView);;
        MaterialTextView age = findViewById(R.id.petDetailsAge_materialTextView);;
        MaterialTextView size = findViewById(R.id.petDetailsSize_materialTextView);;
        MaterialTextView sex = findViewById(R.id.petDetailsSex_materialTextView);;
        MaterialTextView description = findViewById(R.id.petDetailsDescription_materialTextView);;
        MaterialButton adopt = findViewById(R.id.petDetailsAdopt_materialButton);
        MaterialButton shelterDetails = findViewById(R.id.petDetailsShelter_materialButton);

        tabLayout = findViewById(R.id.petDetails_tabLayout);
        viewPager2 = findViewById(R.id.petDetails_viewPager2);
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

        Picasso.get().load(petImage1DownloadLink).into(shapeableImageView);

        name.setText(petName);
        breed.setText(petBreed);
        age.setText(petAge);
        size.setText(petSize);
        sex.setText(petSex);
        description.setText(petDescription);

        adopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShelteredPetDetailsActivity.this, AdoptionApplicationParagraphsActivity.class));
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
}