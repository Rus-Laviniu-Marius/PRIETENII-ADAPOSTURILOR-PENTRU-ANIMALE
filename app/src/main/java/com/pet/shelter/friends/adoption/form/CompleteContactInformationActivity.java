package com.pet.shelter.friends.adoption.form;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pet.shelter.friends.R;

import java.util.Objects;

public class CompleteContactInformationActivity extends AppCompatActivity {

    private TextView fullNameTextView, occupationTextView, addressTextView, timeAtThisAddressTextView,
        phoneNumberTextView, emailTextView, bestTimeToCallTextView;
    private EditText fullNameEditText, occupationEditText, addressEditText, timeAtThisAddressEditText,
        phoneNumberEditText, emailEditText, bestTimeToCallEditText;
    private ImageView backButton, nextButton;
    private Button submitButton;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference adoptionFormReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_contact_information);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        adoptionFormReference = database.getReference().child("adoptionForm");

        fullNameTextView = findViewById(R.id.completeContactInformationFullName_textView);
        occupationTextView = findViewById(R.id.completeContactInformationOccupation_textView);
        addressTextView = findViewById(R.id.completeContactInformationAddress_textView);
        timeAtThisAddressTextView = findViewById(R.id.completeContactInformationSittingTime_textView);
        phoneNumberTextView = findViewById(R.id.completeContactInformationPhoneNumber_textView);
        emailTextView = findViewById(R.id.completeContactInformationEmailAddress_textView);
        bestTimeToCallTextView = findViewById(R.id.completeContactInformationBestTimeToCall_textView);

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

        setOnFocusChangeListeners();
        setOnClickListeners();
    }

    private void setOnFocusChangeListeners() {
        fullNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                fullNameEditText.setHint("");
                fullNameTextView.setVisibility(View.VISIBLE);
            }
        });
        occupationEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                occupationEditText.setHint("");
                occupationTextView.setVisibility(View.VISIBLE);
            }
        });
        addressEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                addressEditText.setHint("");
                addressTextView.setVisibility(View.VISIBLE);
            }
        });
        timeAtThisAddressEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                timeAtThisAddressEditText.setHint("");
                timeAtThisAddressTextView.setVisibility(View.VISIBLE);
            }
        });
        phoneNumberEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                phoneNumberEditText.setHint("");
                phoneNumberTextView.setVisibility(View.VISIBLE);
            }
        });
        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                emailEditText.setHint("");
                emailTextView.setVisibility(View.VISIBLE);
            }
        });
        bestTimeToCallEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                bestTimeToCallEditText.setHint("");
                bestTimeToCallTextView.setVisibility(View.VISIBLE);
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
                Intent intent = new Intent(CompleteContactInformationActivity.this, AdoptPetActivity.class);
                startActivity(intent);
            }
        });
    }

    private void writeToDatabase() {
        String currentFirebaseUserUid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        adoptionFormReference.child(currentFirebaseUserUid).child("applicantContactInformation").child("fullName").setValue(fullNameEditText.getText().toString());
        adoptionFormReference.child(currentFirebaseUserUid).child("applicantContactInformation").child("occupation").setValue(occupationEditText.getText().toString());
        adoptionFormReference.child(currentFirebaseUserUid).child("applicantContactInformation").child("address").setValue(addressEditText.getText().toString());
        adoptionFormReference.child(currentFirebaseUserUid).child("applicantContactInformation").child("sittingTime").setValue(timeAtThisAddressEditText.getText().toString());
        adoptionFormReference.child(currentFirebaseUserUid).child("applicantContactInformation").child("phoneNumber").setValue(phoneNumberEditText.getText().toString());
        adoptionFormReference.child(currentFirebaseUserUid).child("applicantContactInformation").child("emailAddress").setValue(emailEditText.getText().toString());
        adoptionFormReference.child(currentFirebaseUserUid).child("applicantContactInformation").child("bestTimeToCall").setValue(bestTimeToCallEditText.getText().toString());
    }

    private void sendToNextActivity() {
        Intent intent = new Intent(CompleteContactInformationActivity.this, CompleteFamilyAndHouseholdInformationActivity.class);
        startActivity(intent);
    }
}