package com.pet.shelter.friends.adoption;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.adoption.form.AdoptPetActivity;
import com.pet.shelter.friends.adoption.model.Pet;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class SeePetDetailsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference favoritePetsReference, petsReference, shelterAdminReference, toBeAdoptedPets;

    private LinearLayout mainContainer;
    private ImageView petImage, back, love, editPetProfileImageView;
    private TextView petName, petAge, petWeight, petLocation, petSize, petBreed, petSex,
            petDescription, veterinarianData, descriptionTextView;
    private Button adoptPet, seeActivePetAdoptionRequests;
    private Bundle bundle;

    private Pet pet;
    private String currentLoggedUserId;
    private String petKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_pet_details);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        favoritePetsReference = firebaseDatabase.getReference("favoritePets");
        petsReference = firebaseDatabase.getReference("pets");
        shelterAdminReference = firebaseDatabase.getReference("shelterAdmin");
        toBeAdoptedPets = firebaseDatabase.getReference("toBeAdoptedPets");

        currentLoggedUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        back = findViewById(R.id.petDetailsTopBarBack_imageView);
        love = findViewById(R.id.petDetailsTopBarLove_imageView);
        editPetProfileImageView = findViewById(R.id.petDetailsTopBarEdit_imageView);
        adoptPet = findViewById(R.id.petDetailsAdopt_button);
        seeActivePetAdoptionRequests = findViewById(R.id.petDetailsSeeActivePetAdoptionRequests_button);

        mainContainer = findViewById(R.id.petDetailsContainer_linearLayout);
        petImage = findViewById(R.id.petDetailsContentPet_imageView);
        petName = findViewById(R.id.petDetailsContentName_textView);
        petAge = findViewById(R.id.petDetailsContentAge_textView);
        petWeight = findViewById(R.id.petDetailsContentWeight_textView);
        petLocation = findViewById(R.id.petDetailsContentLocation_textView);
        petSize = findViewById(R.id.petDetailsContentSize_textView);
        petBreed = findViewById(R.id.petDetailsContentBreed_textView);
        petSex = findViewById(R.id.petDetailsContentSex_textView);
        petDescription = findViewById(R.id.petDetailsDescriptionContent_textView);
        veterinarianData = findViewById(R.id.petDetailsContentVeterinarianData_textView);
        descriptionTextView = findViewById(R.id.petDetailsDescription_textView);

        bundle = getIntent().getExtras();

        String backgroundColor = bundle.getString("backgroundColor");
        String downloadLink = bundle.getString("imageDownloadLink");
        String name = bundle.getString("petName");
        int age = bundle.getInt("petAge");
        double weight = bundle.getDouble("petWeight");
        String locationString = bundle.getString("petLocation");
        String size = bundle.getString("petSize");
        String breed = bundle.getString("petBreed");
        String sex = bundle.getString("petSex");
        String description = bundle.getString("petDescription");
        String favorite = bundle.getString("favorite");
        String selected = bundle.getString("selected");
        String petType = bundle.getString("petType");
        petKey = bundle.getString("petKey");
        boolean fitForChildren = bundle.getBoolean("fitForChildren");
        boolean fitForGuarding = bundle.getBoolean("fitForGuarding");


        pet = new Pet(backgroundColor, downloadLink, name, age, weight, locationString, size, breed, sex, description, favorite, selected, petType, fitForChildren, fitForGuarding);


        String ageString = "" + bundle.getInt("petAge") + " years";
        String weightString = "" + bundle.getDouble("petWeight") + " KG";

        Drawable birthdayCake, fullSize, gender, location, pawsPrint, weightingScale;

        Picasso.get().load(downloadLink).into(petImage);
        petName.setText(name);
        petAge.setText(ageString);
        petWeight.setText(weightString);
        petLocation.setText(locationString);
        petSize.setText(size);
        petBreed.setText(breed);
        petSex.setText(sex);
        petDescription.setText(description);

        favoritePetsReference.child(currentLoggedUserId).child(petKey).child("favorite").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null && Boolean.parseBoolean(snapshot.getValue().toString())) {
                    love.setImageResource(R.drawable.filled_heart_64);
                }
                else {
                    love.setImageResource(R.drawable.heart_64);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        if (Boolean.parseBoolean(bundle.getString("favorite")))
//            love.setImageResource(R.drawable.filled_heart_64);

        switch (backgroundColor) {
            case "linen":
                mainContainer.setBackgroundColor(getResources().getColor(R.color.linen));
                petName.setTextColor(getResources().getColor(R.color.linen));
                petAge.setTextColor(getResources().getColor(R.color.linen));
                petWeight.setTextColor(getResources().getColor(R.color.linen));
                petLocation.setTextColor(getResources().getColor(R.color.linen));
                petSize.setTextColor(getResources().getColor(R.color.linen));
                petBreed.setTextColor(getResources().getColor(R.color.linen));
                petSex.setTextColor(getResources().getColor(R.color.linen));
                petDescription.setTextColor(getResources().getColor(R.color.linen));
                descriptionTextView.setTextColor(getResources().getColor(R.color.linen));
                veterinarianData.setTextColor(getResources().getColor(R.color.linen));

                birthdayCake = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card1_birthday_cake);
                petAge.setCompoundDrawablesWithIntrinsicBounds(birthdayCake, null, null, null);
                weightingScale = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card1_weighting_scale);
                petWeight.setCompoundDrawablesWithIntrinsicBounds(weightingScale, null, null, null);
                location = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card1_location);
                petLocation.setCompoundDrawablesWithIntrinsicBounds(location, null, null, null);
                fullSize = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card1_full_size);
                petSize.setCompoundDrawablesWithIntrinsicBounds(fullSize, null, null, null);
                pawsPrint = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card1_paws_print);
                petBreed.setCompoundDrawablesWithIntrinsicBounds(pawsPrint, null, null, null);
                gender = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card1_gender);
                petSex.setCompoundDrawablesWithIntrinsicBounds(gender, null, null, null);
                break;
            case "water":
                mainContainer.setBackgroundColor(getResources().getColor(R.color.water));
                petName.setTextColor(getResources().getColor(R.color.water));
                petAge.setTextColor(getResources().getColor(R.color.water));
                petWeight.setTextColor(getResources().getColor(R.color.water));
                petLocation.setTextColor(getResources().getColor(R.color.water));
                petSize.setTextColor(getResources().getColor(R.color.water));
                petBreed.setTextColor(getResources().getColor(R.color.water));
                petSex.setTextColor(getResources().getColor(R.color.water));
                petDescription.setTextColor(getResources().getColor(R.color.water));
                descriptionTextView.setTextColor(getResources().getColor(R.color.water));
                veterinarianData.setTextColor(getResources().getColor(R.color.water));

                birthdayCake = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card2_birthday_cake);
                petAge.setCompoundDrawablesWithIntrinsicBounds(birthdayCake, null, null, null);
                weightingScale = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card2_weighting_scale);
                petWeight.setCompoundDrawablesWithIntrinsicBounds(weightingScale, null, null, null);
                location = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card2_location);
                petLocation.setCompoundDrawablesWithIntrinsicBounds(location, null, null, null);
                fullSize = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card2_full_size);
                petSize.setCompoundDrawablesWithIntrinsicBounds(fullSize, null, null, null);
                pawsPrint = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card2_paws_print);
                petBreed.setCompoundDrawablesWithIntrinsicBounds(pawsPrint, null, null, null);
                gender = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card2_gender);
                petSex.setCompoundDrawablesWithIntrinsicBounds(gender, null, null, null);
                break;
            case "magic_mint":
                mainContainer.setBackgroundColor(getResources().getColor(R.color.magic_mint));
                petName.setTextColor(getResources().getColor(R.color.magic_mint));
                petAge.setTextColor(getResources().getColor(R.color.magic_mint));
                petWeight.setTextColor(getResources().getColor(R.color.magic_mint));
                petLocation.setTextColor(getResources().getColor(R.color.magic_mint));
                petSize.setTextColor(getResources().getColor(R.color.magic_mint));
                petBreed.setTextColor(getResources().getColor(R.color.magic_mint));
                petSex.setTextColor(getResources().getColor(R.color.magic_mint));
                petDescription.setTextColor(getResources().getColor(R.color.magic_mint));
                descriptionTextView.setTextColor(getResources().getColor(R.color.magic_mint));
                veterinarianData.setTextColor(getResources().getColor(R.color.magic_mint));

                birthdayCake = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card3_birthday_cake);
                petAge.setCompoundDrawablesWithIntrinsicBounds(birthdayCake, null, null, null);
                weightingScale = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card3_weighting_scale);
                petWeight.setCompoundDrawablesWithIntrinsicBounds(weightingScale, null, null, null);
                location = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card3_location);
                petLocation.setCompoundDrawablesWithIntrinsicBounds(location, null, null, null);
                fullSize = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card3_full_size);
                petSize.setCompoundDrawablesWithIntrinsicBounds(fullSize, null, null, null);
                pawsPrint = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card3_paws_print);
                petBreed.setCompoundDrawablesWithIntrinsicBounds(pawsPrint, null, null, null);
                gender = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card3_gender);
                petSex.setCompoundDrawablesWithIntrinsicBounds(gender, null, null, null);
                break;
            case "yellow_crayola":
                mainContainer.setBackgroundColor(getResources().getColor(R.color.yellow_crayola));
                petName.setTextColor(getResources().getColor(R.color.yellow_crayola));
                petAge.setTextColor(getResources().getColor(R.color.yellow_crayola));
                petWeight.setTextColor(getResources().getColor(R.color.yellow_crayola));
                petLocation.setTextColor(getResources().getColor(R.color.yellow_crayola));
                petSize.setTextColor(getResources().getColor(R.color.yellow_crayola));
                petBreed.setTextColor(getResources().getColor(R.color.yellow_crayola));
                petSex.setTextColor(getResources().getColor(R.color.yellow_crayola));
                petDescription.setTextColor(getResources().getColor(R.color.yellow_crayola));
                descriptionTextView.setTextColor(getResources().getColor(R.color.yellow_crayola));
                veterinarianData.setTextColor(getResources().getColor(R.color.yellow_crayola));

                birthdayCake = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card4_birthday_cake);
                petAge.setCompoundDrawablesWithIntrinsicBounds(birthdayCake, null, null, null);
                weightingScale = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card4_weighting_scale);
                petWeight.setCompoundDrawablesWithIntrinsicBounds(weightingScale, null, null, null);
                location = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card4_location);
                petLocation.setCompoundDrawablesWithIntrinsicBounds(location, null, null, null);
                fullSize = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card4_full_size);
                petSize.setCompoundDrawablesWithIntrinsicBounds(fullSize, null, null, null);
                pawsPrint = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card4_paws_print);
                petBreed.setCompoundDrawablesWithIntrinsicBounds(pawsPrint, null, null, null);
                gender = AppCompatResources.getDrawable(SeePetDetailsActivity.this, R.drawable.card4_gender);
                petSex.setCompoundDrawablesWithIntrinsicBounds(gender, null, null, null);
                break;
            default:
                break;
        }


        adoptPet.setBackgroundColor(getResources().getColor(R.color.aero_blue));
        seeActivePetAdoptionRequests.setBackgroundColor(getResources().getColor(R.color.aero_blue));

        checkIfCurrentUserIsShelterAdmin(currentLoggedUserId);

        setOnClickListeners(currentLoggedUserId);

    }

    private void setOnClickListeners(String currentLoggedUserUid) {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeePetDetailsActivity.this, SeeListOfPetsActivity.class);
                startActivity(intent);
            }
        });

        love.setOnClickListener(new View.OnClickListener() {
            boolean isPressed = false;
            @Override
            public void onClick(View v) {
                if (isPressed) {
                    love.setImageResource(R.drawable.filled_heart_64);
                    pet.setFavorite("true");
                    petsReference.child(bundle.getString("petKey")).child("favorite").setValue("true");
                    favoritePetsReference.child(currentLoggedUserUid).child(petKey).setValue(pet);
                }
                else {
                    love.setImageResource(R.drawable.heart_64);
                    pet.setFavorite("false");
                    petsReference.child(bundle.getString("petKey")).child("favorite").setValue("false");
                    favoritePetsReference.child(currentLoggedUserUid).child(petKey).removeValue();
                }
                isPressed = !isPressed;
            }
        });

        adoptPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toBeAdoptedPets.child(currentLoggedUserUid).child(bundle.getString("petKey"))
                        .child("petImageLink").setValue(bundle.getString("imageDownloadLink"));
                toBeAdoptedPets.child(currentLoggedUserUid).child(bundle.getString("petKey"))
                        .child("petName").setValue(bundle.getString("petName"));
                toBeAdoptedPets.child(currentLoggedUserUid).child(bundle.getString("petKey"))
                        .child("toBeAdopted").setValue("true");
                Intent intent = new Intent(SeePetDetailsActivity.this, AdoptPetActivity.class);
                startActivity(intent);
            }
        });

        seeActivePetAdoptionRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void checkIfCurrentUserIsShelterAdmin(String loggedUid) {
        shelterAdminReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uId = Objects.requireNonNull(snapshot.child("uId").getValue()).toString();
                if (loggedUid.equals(uId)) {
                    love.setVisibility(View.VISIBLE);
                    editPetProfileImageView.setVisibility(View.GONE);
                    adoptPet.setVisibility(View.VISIBLE);
                    seeActivePetAdoptionRequests.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}