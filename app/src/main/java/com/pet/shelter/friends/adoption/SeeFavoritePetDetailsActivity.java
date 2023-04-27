package com.pet.shelter.friends.adoption;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pet.shelter.friends.UserHomeActivity;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.adoption.form.AdoptPetActivity;
import com.squareup.picasso.Picasso;

public class SeeFavoritePetDetailsActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference favoritePetsReference, petsReference;

    private LinearLayout mainContainer;
    private ImageView petImage, back, home;
    private TextView petName, petAge, petWeight, petLocation, petSize, petBreed, petGender,
            petDescription, veterinarianData, descriptionTextView;
    private Button adoptPet;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_favorite_pet_details);

        firebaseDatabase = FirebaseDatabase.getInstance();
        favoritePetsReference = firebaseDatabase.getReference("favoritePets");
        petsReference = firebaseDatabase.getReference("pets");

        back = findViewById(R.id.favoritePetDetailsTopBarBack_imageView);
        home = findViewById(R.id.favoritePetDetailsTopBarHome_imageView);
        adoptPet = findViewById(R.id.favoritePetDetailsAdopt_button);

        mainContainer = findViewById(R.id.favoritePetDetailsContainer_linearLayout);
        petImage = findViewById(R.id.favoritePetDetailsContentPet_imageView);
        petName = findViewById(R.id.favoritePetDetailsContentName_textView);
        petAge = findViewById(R.id.favoritePetDetailsContentAge_textView);
        petWeight = findViewById(R.id.favoritePetDetailsContentWeight_textView);
        petLocation = findViewById(R.id.favoritePetDetailsContentLocation_textView);
        petSize = findViewById(R.id.favoritePetDetailsContentSize_textView);
        petBreed = findViewById(R.id.favoritePetDetailsContentBreed_textView);
        petGender = findViewById(R.id.favoritePetDetailsContentGender_textView);
        petDescription = findViewById(R.id.favoritePetDetailsDescriptionContent_textView);
        veterinarianData = findViewById(R.id.favoritePetDetailsContentVeterinarianData_textView);
        descriptionTextView = findViewById(R.id.favoritePetDetailsDescription_textView);

        bundle = getIntent().getExtras();

        String color = bundle.getString("backgroundColor");
        String age = "" + bundle.getInt("petAge") + " years";
        String weight = "" + bundle.getDouble("petWeight") + " KG";

        Drawable birthdayCake, fullSize, gender, location, pawsPrint, weightingScale;

        Picasso.get().load(bundle.getString("imageDownloadLink")).into(petImage);
        petName.setText(bundle.getString("petName"));
        petAge.setText(age);
        petWeight.setText(weight);
        petLocation.setText(bundle.getString("petLocation"));
        petSize.setText(bundle.getString("petSize"));
        petBreed.setText(bundle.getString("petBreed"));
        petGender.setText(bundle.getString("petGender"));
        petDescription.setText(bundle.getString("petDescription"));


        switch (color) {
            case "linen":
                mainContainer.setBackgroundColor(getResources().getColor(R.color.linen));
                petName.setTextColor(getResources().getColor(R.color.linen));
                petAge.setTextColor(getResources().getColor(R.color.linen));
                petWeight.setTextColor(getResources().getColor(R.color.linen));
                petLocation.setTextColor(getResources().getColor(R.color.linen));
                petSize.setTextColor(getResources().getColor(R.color.linen));
                petBreed.setTextColor(getResources().getColor(R.color.linen));
                petGender.setTextColor(getResources().getColor(R.color.linen));
                petDescription.setTextColor(getResources().getColor(R.color.linen));
                descriptionTextView.setTextColor(getResources().getColor(R.color.linen));
                veterinarianData.setTextColor(getResources().getColor(R.color.linen));

                birthdayCake = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card1_birthday_cake);
                petAge.setCompoundDrawablesWithIntrinsicBounds(birthdayCake, null, null, null);
                weightingScale = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card1_weighting_scale);
                petWeight.setCompoundDrawablesWithIntrinsicBounds(weightingScale, null, null, null);
                location = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card1_location);
                petLocation.setCompoundDrawablesWithIntrinsicBounds(location, null, null, null);
                fullSize = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card1_full_size);
                petSize.setCompoundDrawablesWithIntrinsicBounds(fullSize, null, null, null);
                pawsPrint = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card1_paws_print);
                petBreed.setCompoundDrawablesWithIntrinsicBounds(pawsPrint, null, null, null);
                gender = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card1_gender);
                petGender.setCompoundDrawablesWithIntrinsicBounds(gender, null, null, null);
                break;
            case "water":
                mainContainer.setBackgroundColor(getResources().getColor(R.color.water));
                petName.setTextColor(getResources().getColor(R.color.water));
                petAge.setTextColor(getResources().getColor(R.color.water));
                petWeight.setTextColor(getResources().getColor(R.color.water));
                petLocation.setTextColor(getResources().getColor(R.color.water));
                petSize.setTextColor(getResources().getColor(R.color.water));
                petBreed.setTextColor(getResources().getColor(R.color.water));
                petGender.setTextColor(getResources().getColor(R.color.water));
                petDescription.setTextColor(getResources().getColor(R.color.water));
                descriptionTextView.setTextColor(getResources().getColor(R.color.water));
                veterinarianData.setTextColor(getResources().getColor(R.color.water));

                birthdayCake = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card2_birthday_cake);
                petAge.setCompoundDrawablesWithIntrinsicBounds(birthdayCake, null, null, null);
                weightingScale = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card2_weighting_scale);
                petWeight.setCompoundDrawablesWithIntrinsicBounds(weightingScale, null, null, null);
                location = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card2_location);
                petLocation.setCompoundDrawablesWithIntrinsicBounds(location, null, null, null);
                fullSize = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card2_full_size);
                petSize.setCompoundDrawablesWithIntrinsicBounds(fullSize, null, null, null);
                pawsPrint = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card2_paws_print);
                petBreed.setCompoundDrawablesWithIntrinsicBounds(pawsPrint, null, null, null);
                gender = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card2_gender);
                petGender.setCompoundDrawablesWithIntrinsicBounds(gender, null, null, null);
                break;
            case "magic_mint":
                mainContainer.setBackgroundColor(getResources().getColor(R.color.magic_mint));
                petName.setTextColor(getResources().getColor(R.color.magic_mint));
                petAge.setTextColor(getResources().getColor(R.color.magic_mint));
                petWeight.setTextColor(getResources().getColor(R.color.magic_mint));
                petLocation.setTextColor(getResources().getColor(R.color.magic_mint));
                petSize.setTextColor(getResources().getColor(R.color.magic_mint));
                petBreed.setTextColor(getResources().getColor(R.color.magic_mint));
                petGender.setTextColor(getResources().getColor(R.color.magic_mint));
                petDescription.setTextColor(getResources().getColor(R.color.magic_mint));
                descriptionTextView.setTextColor(getResources().getColor(R.color.magic_mint));
                veterinarianData.setTextColor(getResources().getColor(R.color.magic_mint));

                birthdayCake = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card3_birthday_cake);
                petAge.setCompoundDrawablesWithIntrinsicBounds(birthdayCake, null, null, null);
                weightingScale = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card3_weighting_scale);
                petWeight.setCompoundDrawablesWithIntrinsicBounds(weightingScale, null, null, null);
                location = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card3_location);
                petLocation.setCompoundDrawablesWithIntrinsicBounds(location, null, null, null);
                fullSize = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card3_full_size);
                petSize.setCompoundDrawablesWithIntrinsicBounds(fullSize, null, null, null);
                pawsPrint = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card3_paws_print);
                petBreed.setCompoundDrawablesWithIntrinsicBounds(pawsPrint, null, null, null);
                gender = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card3_gender);
                petGender.setCompoundDrawablesWithIntrinsicBounds(gender, null, null, null);
                break;
            case "yellow_crayola":
                mainContainer.setBackgroundColor(getResources().getColor(R.color.yellow_crayola));
                petName.setTextColor(getResources().getColor(R.color.yellow_crayola));
                petAge.setTextColor(getResources().getColor(R.color.yellow_crayola));
                petWeight.setTextColor(getResources().getColor(R.color.yellow_crayola));
                petLocation.setTextColor(getResources().getColor(R.color.yellow_crayola));
                petSize.setTextColor(getResources().getColor(R.color.yellow_crayola));
                petBreed.setTextColor(getResources().getColor(R.color.yellow_crayola));
                petGender.setTextColor(getResources().getColor(R.color.yellow_crayola));
                petDescription.setTextColor(getResources().getColor(R.color.yellow_crayola));
                descriptionTextView.setTextColor(getResources().getColor(R.color.yellow_crayola));
                veterinarianData.setTextColor(getResources().getColor(R.color.yellow_crayola));

                birthdayCake = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card4_birthday_cake);
                petAge.setCompoundDrawablesWithIntrinsicBounds(birthdayCake, null, null, null);
                weightingScale = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card4_weighting_scale);
                petWeight.setCompoundDrawablesWithIntrinsicBounds(weightingScale, null, null, null);
                location = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card4_location);
                petLocation.setCompoundDrawablesWithIntrinsicBounds(location, null, null, null);
                fullSize = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card4_full_size);
                petSize.setCompoundDrawablesWithIntrinsicBounds(fullSize, null, null, null);
                pawsPrint = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card4_paws_print);
                petBreed.setCompoundDrawablesWithIntrinsicBounds(pawsPrint, null, null, null);
                gender = AppCompatResources.getDrawable(SeeFavoritePetDetailsActivity.this, R.drawable.card4_gender);
                petGender.setCompoundDrawablesWithIntrinsicBounds(gender, null, null, null);
                break;
            default:
                break;
        }

        adoptPet.setBackgroundColor(getResources().getColor(R.color.aero_blue));

        setOnClickListeners();

    }

    private void setOnClickListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeFavoritePetDetailsActivity.this, SeeListOfFavoritePetsActivity.class);
                startActivity(intent);
            }
        });


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeFavoritePetDetailsActivity.this, UserHomeActivity.class);
                startActivity(intent);
            }
        });

        adoptPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeFavoritePetDetailsActivity.this, AdoptPetActivity.class);
                startActivity(intent);
            }
        });
    }
}