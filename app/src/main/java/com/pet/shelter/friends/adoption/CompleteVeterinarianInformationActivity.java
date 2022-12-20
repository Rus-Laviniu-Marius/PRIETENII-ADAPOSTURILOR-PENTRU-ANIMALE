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

public class CompleteVeterinarianInformationActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference veterinarianInformationReference;
    private RadioButton regularVeterinarian, noRegularVeterinarian;
    private EditText veterinarianName, veterinarianClinicName, veterinarianClinicAddress, veterinarianClinicPhoneNumber;
    private ImageView backButton, nextButton;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_veterinarian_information);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        veterinarianInformationReference = firebaseDatabase.getReference("regularVeterinarian");

        regularVeterinarian = findViewById(R.id.regularVeterinarianYes_radioButton);
        noRegularVeterinarian = findViewById(R.id.regularVeterinarianNo_radioButton);
        veterinarianName = findViewById(R.id.veterinarianInformationName_editText);
        veterinarianClinicName = findViewById(R.id.veterinarianInformationClinicName_editText);
        veterinarianClinicAddress = findViewById(R.id.veterinarianInformationClinicAddress_editText);
        veterinarianClinicPhoneNumber = findViewById(R.id.veterinarianInformationClinicPhone_editText);

        backButton = findViewById(R.id.completeVeterinarianInformationBack_button);
        nextButton = findViewById(R.id.completeVeterinarianInformationNext_button);
        submitButton = findViewById(R.id.completeVeterinarianInformationSubmit_button);

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
                Intent intent = new Intent(CompleteVeterinarianInformationActivity.this, CompleteOtherPetsInformationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void writeToDatabase() {
        String currentFirebaseUserUid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        if (regularVeterinarian.isChecked()) {
            veterinarianInformationReference.child(currentFirebaseUserUid).child("regularVeterinarian").setValue("yes");
        }
        if (noRegularVeterinarian.isChecked()) {
            veterinarianInformationReference.child(currentFirebaseUserUid).child("regularVeterinarian").setValue("no");
        }

        veterinarianInformationReference.child(currentFirebaseUserUid).child("veterinarianName").setValue(veterinarianName.getText().toString());
        veterinarianInformationReference.child(currentFirebaseUserUid).child("veterinarianClinicName").setValue(veterinarianClinicName.getText().toString());
        veterinarianInformationReference.child(currentFirebaseUserUid).child("veterinarianClinicAddress").setValue(veterinarianClinicAddress.getText().toString());
        veterinarianInformationReference.child(currentFirebaseUserUid).child("veterinarianClinicPhoneNumber").setValue(veterinarianClinicPhoneNumber.getText().toString());
    }

    private void sendToNextActivity() {
        Intent intent = new Intent(CompleteVeterinarianInformationActivity.this, CompleteAboutThePetInformationActivity.class);
        startActivity(intent);
    }
}