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

public class CompleteAboutThePetInformationActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference aboutPetInformationReference;
    private EditText daytimePlaceDescription, nightTimePlaceDescription, numberOfHoursPetIsAlone;
    private RadioButton regularHealthCare, noRegularHealthCare, contactForSurrender, noContactForSurrender;
    private ImageView backButton, nextButton;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_about_the_pet_information);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        aboutPetInformationReference = firebaseDatabase.getReference("about_pet");

        daytimePlaceDescription = findViewById(R.id.dayTimePlaceDescription_editText);
        nightTimePlaceDescription = findViewById(R.id.nightTimePlaceDescription_editText);
        numberOfHoursPetIsAlone = findViewById(R.id.numberOfHoursPetIsALone_editText);

        backButton = findViewById(R.id.completeAboutThePetInformationBack_button);
        nextButton = findViewById(R.id.completeAboutThePetInformationNext_button);
        submitButton = findViewById(R.id.completeAboutThePetInformationSubmit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currentFirebaseUserUid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

                aboutPetInformationReference.child(currentFirebaseUserUid).child("daytime_place").setValue(daytimePlaceDescription.getText().toString());
                aboutPetInformationReference.child(currentFirebaseUserUid).child("nighttime_place").setValue(nightTimePlaceDescription.getText().toString());
                aboutPetInformationReference.child(currentFirebaseUserUid).child("alone_hours").setValue(numberOfHoursPetIsAlone.getText().toString());

                if (regularHealthCare.isChecked()) {
                    aboutPetInformationReference.child(currentFirebaseUserUid).child("regular_health_care").setValue("yes");
                }
                if (noRegularHealthCare.isChecked()) {
                    aboutPetInformationReference.child(currentFirebaseUserUid).child("regular_health_care").setValue("no");
                }

                if (contactForSurrender.isChecked()) {
                    aboutPetInformationReference.child(currentFirebaseUserUid).child("contact_for_surrender").setValue("yes");
                }
                if (noContactForSurrender.isChecked()) {
                    aboutPetInformationReference.child(currentFirebaseUserUid).child("contact_for_surrender").setValue("no");
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
                Intent intent = new Intent(CompleteAboutThePetInformationActivity.this, CompleteVeterinarianInformationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendToNextActivity() {
        Intent intent = new Intent(CompleteAboutThePetInformationActivity.this, CompletePersonalReferencesInformationActivity.class);
        startActivity(intent);
    }
}