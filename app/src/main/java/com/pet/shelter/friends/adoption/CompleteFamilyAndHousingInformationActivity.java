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

public class CompleteFamilyAndHousingInformationActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference familyAndHousingReference;
    private EditText adultsNumberEditText, childrenNumberEditText, homeTypeEditText,
            homeDescriptionEditText, rentingRulesRegardingPetOwnership;
    private RadioButton knownAllergy, notKnownAllergy, familyAgrees, familyDoesNotAgree,
        adequateLoveAndAttention, notAdequateLoveAndAttention;
    private ImageView backButton, nextButton;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_family_and_housing_information);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        familyAndHousingReference = firebaseDatabase.getReference("family_and_housing");

        adultsNumberEditText = findViewById(R.id.familyAndHousingAdultsNumber_editText);
        childrenNumberEditText = findViewById(R.id.familyAndHousingChildrenNumber_editText);
        homeTypeEditText = findViewById(R.id.familyAndHousingHomeType_editText);
        homeDescriptionEditText = findViewById(R.id.familyAndHousingHomeDescription_editText);
        rentingRulesRegardingPetOwnership = findViewById(R.id.rentingRulesRegardingPetOwnership_editText);

        knownAllergy = findViewById(R.id.knownAllergyYes_radioButton);
        notKnownAllergy = findViewById(R.id.knownAllergyNo_radioButton);
        familyAgrees = findViewById(R.id.agreementDecisionYes_radioButton);
        familyDoesNotAgree = findViewById(R.id.agreementDecisionNo_radioButton);
        adequateLoveAndAttention = findViewById(R.id.adequateLoveAndAttentionYes_radioButton);
        notAdequateLoveAndAttention = findViewById(R.id.adequateLoveAndAttentionNo_radioButton);

        backButton = findViewById(R.id.completeFamilyAndHousingInformationBack_button);
        nextButton = findViewById(R.id.completeFamilyAndHousingInformationNext_button);
        submitButton = findViewById(R.id.completeFamilyAndHousingInformationSubmit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currentFirebaseUserUid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

                familyAndHousingReference.child(currentFirebaseUserUid).child("adults_number").setValue(adultsNumberEditText.getText().toString());
                familyAndHousingReference.child(currentFirebaseUserUid).child("children_number").setValue(childrenNumberEditText.getText().toString());
                familyAndHousingReference.child(currentFirebaseUserUid).child("home_type").setValue(homeTypeEditText.getText().toString());
                familyAndHousingReference.child(currentFirebaseUserUid).child("home_description").setValue(homeDescriptionEditText.getText().toString());
                familyAndHousingReference.child(currentFirebaseUserUid).child("renting_rules_regarding_pet_ownership").setValue(rentingRulesRegardingPetOwnership.getText().toString());

                if (knownAllergy.isChecked()) {
                    familyAndHousingReference.child(currentFirebaseUserUid).child("known_allergy").setValue("yes");
                }
                if (notKnownAllergy.isChecked()) {
                    familyAndHousingReference.child(currentFirebaseUserUid).child("known_allergy").setValue("no");
                }

                if (familyAgrees.isChecked()) {
                    familyAndHousingReference.child(currentFirebaseUserUid).child("family_agreement").setValue("yes");
                }
                if (familyDoesNotAgree.isChecked()) {
                    familyAndHousingReference.child(currentFirebaseUserUid).child("family_agreement").setValue("no");
                }

                if (adequateLoveAndAttention.isChecked()) {
                    familyAndHousingReference.child(currentFirebaseUserUid).child("provide_adequate_love_and_attention").setValue("yes");
                }
                if (notAdequateLoveAndAttention.isChecked()) {
                    familyAndHousingReference.child(currentFirebaseUserUid).child("provide_adequate_love_and_attention").setValue("no");
                }

                sendToNextActivity();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompleteFamilyAndHousingInformationActivity.this, CompleteContactInformationActivity.class);
                startActivity(intent);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToNextActivity();
            }
        });
    }

    private void sendToNextActivity() {
        Intent intent = new Intent(CompleteFamilyAndHousingInformationActivity.this, CompleteOtherPetsInformationActivity.class);
        startActivity(intent);
    }
}