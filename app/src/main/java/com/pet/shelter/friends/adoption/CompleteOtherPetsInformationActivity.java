package com.pet.shelter.friends.adoption;

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
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference otherPetsReference;
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
        firebaseDatabase = FirebaseDatabase.getInstance();
        otherPetsReference = firebaseDatabase.getReference("other_pets");

        otherPetsDetailsEditText = findViewById(R.id.otherPetsTypeAndNumber_editText);
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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currentFirebaseUserUid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

                otherPetsReference.child(currentFirebaseUserUid).child("other_pets_details").setValue(otherPetsDetailsEditText.getText().toString());
                otherPetsReference.child(currentFirebaseUserUid).child("surrendered_pet_details").setValue(surrenderedPetDescription.getText().toString());
                otherPetsReference.child(currentFirebaseUserUid).child("euthanized_pet_details").setValue(euthanizedPetDescription.getText().toString());

                if (vaccinesUpToDate.isChecked()) {
                    otherPetsReference.child(currentFirebaseUserUid).child("vaccines_up_to_date").setValue("yes");
                }
                if (vaccinesNotUpToDate.isChecked()) {
                    otherPetsReference.child(currentFirebaseUserUid).child("vaccines_up_to_date").setValue("no");
                }

                if (surrenderedPet.isChecked()) {
                    otherPetsReference.child(currentFirebaseUserUid).child("surrendered_pet").setValue("yes");
                }
                if (notSurrenderedPet.isChecked()) {
                    otherPetsReference.child(currentFirebaseUserUid).child("surrendered_pet").setValue("no");
                }

                if (euthanizedPet.isChecked()) {
                    otherPetsReference.child(currentFirebaseUserUid).child("euthanized_pet").setValue("yes");
                }
                if (notEuthanizedPet.isChecked()) {
                    otherPetsReference.child(currentFirebaseUserUid).child("euthanized_pet").setValue("no");
                }

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
                Intent intent = new Intent(CompleteOtherPetsInformationActivity.this, CompleteFamilyAndHousingInformationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendToNextActivity() {
        Intent intent = new Intent(CompleteOtherPetsInformationActivity.this, CompleteVeterinarianInformationActivity.class);
        startActivity(intent);
    }
}