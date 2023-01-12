package com.pet.shelter.friends.adoption.form;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pet.shelter.friends.R;

import java.util.Objects;

public class CompleteAboutThePetInformationActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference adoptionFormReference;
    private TextView petNameTextView, numberOfHoursPetIsAloneTextView;
    private EditText daytimePlaceDescription, nightTimePlaceDescription, numberOfHoursPetIsAloneEditText, petNameEditText;
    private RadioButton regularHealthCare, noRegularHealthCare, contactForSurrender, noContactForSurrender;
    private ImageView backButton, nextButton;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_about_the_pet_information);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        adoptionFormReference = firebaseDatabase.getReference("adoptionForm");

        daytimePlaceDescription = findViewById(R.id.dayTimePlaceDescription_editText);
        nightTimePlaceDescription = findViewById(R.id.nightTimePlaceDescription_editText);
        numberOfHoursPetIsAloneTextView = findViewById(R.id.numberOfHoursPetIsALone_textView);
        petNameTextView = findViewById(R.id.aboutPetName_textView);
        numberOfHoursPetIsAloneEditText = findViewById(R.id.numberOfHoursPetIsALone_editText);
        petNameEditText = findViewById(R.id.aboutPetName_editText);
        regularHealthCare = findViewById(R.id.regularHealthCareYes_radioButton);
        noRegularHealthCare = findViewById(R.id.regularHealthCareNo_radioButton);
        contactForSurrender = findViewById(R.id.contactForSurrenderYes_radioButton);
        noContactForSurrender = findViewById(R.id.contactForSurrenderNo_radioButton);

        backButton = findViewById(R.id.completeAboutThePetInformationBack_button);
        nextButton = findViewById(R.id.completeAboutThePetInformationNext_button);
        submitButton = findViewById(R.id.completeAboutThePetInformationSubmit_button);

        setOnFocusChangeListeners();
        setOnClickListeners();

    }

    private void setOnFocusChangeListeners() {
        petNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                petNameEditText.setHint("");
                petNameTextView.setVisibility(View.VISIBLE);
            }
        });

        numberOfHoursPetIsAloneEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                numberOfHoursPetIsAloneEditText.setHint("");
                numberOfHoursPetIsAloneTextView.setVisibility(View.VISIBLE);
            }
        });
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
                Intent intent = new Intent(CompleteAboutThePetInformationActivity.this, CompleteVeterinarianInformationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void writeToDatabase() {
        String currentFirebaseUserUid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        adoptionFormReference.child(currentFirebaseUserUid).child("aboutPetInformation").child("petName").setValue(petNameEditText.getText().toString().trim());
        adoptionFormReference.child(currentFirebaseUserUid).child("aboutPetInformation").child("daytimePlace").setValue(daytimePlaceDescription.getText().toString().trim());
        adoptionFormReference.child(currentFirebaseUserUid).child("aboutPetInformation").child("nighttimePlace").setValue(nightTimePlaceDescription.getText().toString().trim());
        adoptionFormReference.child(currentFirebaseUserUid).child("aboutPetInformation").child("aloneHours").setValue(numberOfHoursPetIsAloneEditText.getText().toString().trim());

        if (regularHealthCare.isChecked()) {
            adoptionFormReference.child(currentFirebaseUserUid).child("aboutPetInformation").child("regularHealthCare").setValue("yes");
        }
        if (noRegularHealthCare.isChecked()) {
            adoptionFormReference.child(currentFirebaseUserUid).child("aboutPetInformation").child("regularHealthCare").setValue("no");
        }

        if (contactForSurrender.isChecked()) {
            adoptionFormReference.child(currentFirebaseUserUid).child("aboutPetInformation").child("contactForSurrender").setValue("yes");
        }
        if (noContactForSurrender.isChecked()) {
            adoptionFormReference.child(currentFirebaseUserUid).child("aboutPetInformation").child("contactForSurrender").setValue("no");
        }
    }

    private void sendToNextActivity() {
        Intent intent = new Intent(CompleteAboutThePetInformationActivity.this, CompletePersonalReferencesInformationActivity.class);
        startActivity(intent);
    }
}