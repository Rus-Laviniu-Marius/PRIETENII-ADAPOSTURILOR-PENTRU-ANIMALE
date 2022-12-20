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

public class CompletePersonalReferencesInformationActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference personalReference;
    private EditText referenceNameEditText, referenceAddressEditText, referencePhoneNumberEditText, referenceRelationshipEditText;
    private ImageView backButton, nextButton;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_personal_references_information);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        personalReference = firebaseDatabase.getReference("personalReference");

        referenceNameEditText = findViewById(R.id.personalReferenceName_editText);
        referenceAddressEditText = findViewById(R.id.personalReferenceAddress_editText);
        referencePhoneNumberEditText = findViewById(R.id.personalReferencePhoneNumber_editText);
        referenceRelationshipEditText = findViewById(R.id.personalReferenceRelationship_editText);

        backButton = findViewById(R.id.completePersonalReferencesInformationBack_button);
        nextButton = findViewById(R.id.completePersonalReferencesInformationNext_button);
        submitButton = findViewById(R.id.completePersonalReferencesInformationSubmit_button);

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
                Intent intent = new Intent(CompletePersonalReferencesInformationActivity.this, CompleteAboutThePetInformationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void writeToDatabase() {
        String currentFirebaseUserUid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        personalReference.child(currentFirebaseUserUid).child("referenceName").setValue(referenceNameEditText.getText().toString());
        personalReference.child(currentFirebaseUserUid).child("referenceAddress").setValue(referenceAddressEditText.getText().toString());
        personalReference.child(currentFirebaseUserUid).child("referencePhoneNumber").setValue(referencePhoneNumberEditText.getText().toString());
        personalReference.child(currentFirebaseUserUid).child("referenceRelationship").setValue(referenceRelationshipEditText.getText().toString());


    }

    private void sendToNextActivity() {
        Intent intent = new Intent(CompletePersonalReferencesInformationActivity.this, GenerateAdoptionPapersPDFActivity.class);
        startActivity(intent);
    }
}