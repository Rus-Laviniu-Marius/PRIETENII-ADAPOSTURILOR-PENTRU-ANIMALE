package com.pet.shelter.friends.adoption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pet.shelter.friends.R;

import java.util.Objects;

public class CompleteContactInformationActivity extends AppCompatActivity {

    private EditText fullNameEditText, occupationEditText, addressEditText, timeAtThisAddressEditText,
        phoneNumberEditText, emailEditText, bestTimeToCallEditText;
    private ImageView backButton, nextButton;
    private Button submitButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference contactInformationReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_contact_information);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        contactInformationReference = database.getReference().child("contactInformation");

        fullNameEditText = findViewById(R.id.completeContactInformationFullName_editText);
        occupationEditText = findViewById(R.id.completeContactInformationOccupation_editText);
        addressEditText = findViewById(R.id.completeContactInformationAddress_editText);
        timeAtThisAddressEditText = findViewById(R.id.completeContactInformationSittingTime_editText);
        phoneNumberEditText = findViewById(R.id.completeContactInformationPhoneNumber_editText);
        emailEditText = findViewById(R.id.completeContactInformationEmailAddress_editText);
        bestTimeToCallEditText = findViewById(R.id.completeContactInformationBestTimeToCall_editText);
        backButton = findViewById(R.id.completeContactInformationBack_button);
        nextButton = findViewById(R.id.completeContactInformationNext_button);
        submitButton = findViewById(R.id.completeContactInformationSubmit_button);

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
                Intent intent = new Intent(CompleteContactInformationActivity.this, AdoptPetActivity.class);
                startActivity(intent);
            }
        });
    }

    private void writeToDatabase() {
        String currentFirebaseUserUid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        contactInformationReference.child(currentFirebaseUserUid).child("fullName").setValue(fullNameEditText.getText().toString());
        contactInformationReference.child(currentFirebaseUserUid).child("occupation").setValue(occupationEditText.getText().toString());
        contactInformationReference.child(currentFirebaseUserUid).child("address").setValue(addressEditText.getText().toString());
        contactInformationReference.child(currentFirebaseUserUid).child("sittingTime").setValue(timeAtThisAddressEditText.getText().toString());
        contactInformationReference.child(currentFirebaseUserUid).child("phoneNumber").setValue(phoneNumberEditText.getText().toString());
        contactInformationReference.child(currentFirebaseUserUid).child("emailAddress").setValue(emailEditText.getText().toString());
        contactInformationReference.child(currentFirebaseUserUid).child("bestTimeToCall").setValue(bestTimeToCallEditText.getText().toString());

    }

    private void sendToNextActivity() {
        Intent intent = new Intent(CompleteContactInformationActivity.this, CompleteFamilyAndHousingInformationActivity.class);
        startActivity(intent);
    }


}