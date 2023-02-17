package com.pet.shelter.friends.adoption.form;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pet.shelter.friends.R;

import java.util.Objects;

public class CompleteOtherPetsInformationActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference adoptionFormReference;
    private EditText otherPetsDetailsEditText, surrenderedPetDescription, euthanizedPetDescription;
    private RadioButton vaccinesUpToDate, vaccinesNotUpToDate, surrenderedPet, notSurrenderedPet,
        euthanizedPet, notEuthanizedPet;
    private ImageView backButton, nextButton;
    private Button submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_other_pets_information);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        adoptionFormReference = firebaseDatabase.getReference("adoptionForms");

        otherPetsDetailsEditText = findViewById(R.id.otherPetsDetails_editText);
        surrenderedPetDescription = findViewById(R.id.surrenderedPetDescription_editText);
        euthanizedPetDescription = findViewById(R.id.euthanizedPetDescription_editText);
        vaccinesUpToDate = findViewById(R.id.vaccinesUpToDateYes_radioButton);
        vaccinesNotUpToDate = findViewById(R.id.vaccinesUpToDateNo_radioButton);
        surrenderedPet = findViewById(R.id.surrenderedPetYes_radioButton);
        notSurrenderedPet = findViewById(R.id.surrenderedPetNo_radioButton);
        euthanizedPet = findViewById(R.id.euthanizedYes_radioButton);
        notEuthanizedPet = findViewById(R.id.euthanizedNo_radioButton);

        backButton = findViewById(R.id.completeOtherPetsInformationBack_button);
        nextButton = findViewById(R.id.completeOtherPetsInformationNext_button);
        submitButton = findViewById(R.id.completeOtherPetsInformationSubmit_button);

        setOnClickListeners();


    }

    private void setOnClickListeners() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToDatabase();
                sendToNextActivity();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToNextActivity();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompleteOtherPetsInformationActivity.this, CompleteFamilyAndHouseholdInformationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void writeToDatabase() {
        String currentFirebaseUserUid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        adoptionFormReference.child(currentFirebaseUserUid).child("otherOwnedPets").child("otherPetsDetails").setValue(otherPetsDetailsEditText.getText().toString());
        adoptionFormReference.child(currentFirebaseUserUid).child("otherOwnedPets").child("surrenderedPetDetails").setValue(surrenderedPetDescription.getText().toString());
        adoptionFormReference.child(currentFirebaseUserUid).child("otherOwnedPets").child("euthanizedPetDetails").setValue(euthanizedPetDescription.getText().toString());

        if (vaccinesUpToDate.isChecked()) {
            adoptionFormReference.child(currentFirebaseUserUid).child("otherOwnedPets").child("vaccinesUpToDate").setValue("yes");
        }
        if (vaccinesNotUpToDate.isChecked()) {
            adoptionFormReference.child(currentFirebaseUserUid).child("otherOwnedPets").child("vaccinesUpToDate").setValue("no");
        }

        if (surrenderedPet.isChecked()) {
            adoptionFormReference.child(currentFirebaseUserUid).child("otherOwnedPets").child("surrenderedPet").setValue("yes");
        }
        if (notSurrenderedPet.isChecked()) {
            adoptionFormReference.child(currentFirebaseUserUid).child("otherOwnedPets").child("surrenderedPet").setValue("no");
        }

        if (euthanizedPet.isChecked()) {
            adoptionFormReference.child(currentFirebaseUserUid).child("otherOwnedPets").child("euthanizedPet").setValue("yes");
        }
        if (notEuthanizedPet.isChecked()) {
            adoptionFormReference.child(currentFirebaseUserUid).child("otherOwnedPets").child("euthanizedPet").setValue("no");
        }
    }

    private void sendToNextActivity() {
        Intent intent = new Intent(CompleteOtherPetsInformationActivity.this, CompleteVeterinarianInformationActivity.class);
        startActivity(intent);
    }
}