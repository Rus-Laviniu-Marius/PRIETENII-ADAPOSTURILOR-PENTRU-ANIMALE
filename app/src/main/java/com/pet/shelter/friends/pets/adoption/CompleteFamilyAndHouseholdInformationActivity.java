package com.pet.shelter.friends.pets.adoption;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pet.shelter.friends.ErrorSetter;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.ValidationManager;

import java.util.Objects;

public class CompleteFamilyAndHouseholdInformationActivity extends AppCompatActivity implements TextWatcher, ErrorSetter {

    private TextInputLayout adultsNumberTextInputLayout, childrenNumberTextInputLayout,
            householdTextInputLayout, householdDescriptionTextInputLayout, rentingRulesTextInputLayout;
    private MaterialRadioButton knownAllergy, notKnownAllergy, allAgree, notAllAgree,
            adequateLoveAndAttention, notAdequateLoveAndAttention;
    private String householdSelectedType, loggedUserId;
    private DatabaseReference adoptionApplicationsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_family_and_household_information);

        adultsNumberTextInputLayout = findViewById(R.id.completeAboutFamilyAndHouseholdInformationAdultsNumber_materialTextInputLayout);
        childrenNumberTextInputLayout = findViewById(R.id.completeAboutFamilyAndHouseholdInformationChildrenNumber_materialTextInputLayout);
        householdTextInputLayout = findViewById(R.id.completeAboutFamilyAndHouseholdInformationHomeType_materialTextInputLayout);
        householdDescriptionTextInputLayout = findViewById(R.id.completeAboutFamilyAndHouseholdInformationHouseholdDescription_materialTextInputLayout);
        rentingRulesTextInputLayout = findViewById(R.id.completeAboutFamilyAndHouseholdInformationRentingRulesRegardingPetOwnerShip_materialTextInputLayout);
        MaterialAutoCompleteTextView householdMaterialAutoCompleteTextView = findViewById(R.id.completeAboutFamilyAndHouseholdInformationHomeType_materialAutoCompleteTextView);
        TextInputEditText adultsNumberTextInputEditText = findViewById(R.id.completeAboutFamilyAndHouseholdInformationAdultsNumber_materialTextInputEditText);
        TextInputEditText childrenNumberTextInputEditText = findViewById(R.id.completeAboutFamilyAndHouseholdInformationChildrenNumber_materialTextInputEditText);
        TextInputEditText householdDescriptionTextInputEditText = findViewById(R.id.completeAboutFamilyAndHouseholdInformationHouseholdDescription_materialTextInputEditText);
        TextInputEditText rentingRulesTextInputEditText = findViewById(R.id.completeAboutFamilyAndHouseholdInformationRentingRulesRegardingPetOwnerShip_materialTextInputEditText);
        MaterialToolbar materialToolbar = findViewById(R.id.completeAboutFamilyAndHouseholdInformation_materialToolbar);
        MaterialButton materialButton = findViewById(R.id.completeAboutFamilyAndHouseholdInformation_materialButton);
        knownAllergy = findViewById(R.id.completeAboutFamilyAndHouseholdInformationKnownAllergyYes_materialRadioButton);
        notKnownAllergy = findViewById(R.id.completeAboutFamilyAndHouseholdInformationKnownAllergyNo_materialRadioButton);
        allAgree = findViewById(R.id.completeAboutFamilyAndHouseholdInformationAllAgreeYes_materialRadioButton);
        notAllAgree = findViewById(R.id.completeAboutFamilyAndHouseholdInformationAllAgreeNo_materialRadioButton);
        adequateLoveAndAttention = findViewById(R.id.completeAboutFamilyAndHouseholdInformationAdequateLoveAndAttentionYes_materialRadioButton);
        notAdequateLoveAndAttention = findViewById(R.id.completeAboutFamilyAndHouseholdInformationAdequateLoveAndAttentionNo_materialRadioButton);
        adoptionApplicationsReference = FirebaseDatabase.getInstance().getReference("adoptionApplications");
        loggedUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        String[] householdTypes = {"Single-family", "Townhouse", "Multi-family", "Modular home", "Bungalow",
                "Ranch home", "Condors & Co-ops", "Farm home"};

        ArrayAdapter<String> householdAdapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                householdTypes
        );

        householdMaterialAutoCompleteTextView.setAdapter(householdAdapter);
        householdMaterialAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                householdSelectedType = String.valueOf(parent.getItemAtPosition(position));
            }
        });

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompleteFamilyAndHouseholdInformationActivity.this, CompleteAboutPetAdopterInformationActivity.class);
                intent.putExtra("fullName", getIntent().getStringExtra("fullName"));
                intent.putExtra("email", getIntent().getStringExtra("email"));
                intent.putExtra("address", getIntent().getStringExtra("address"));
                intent.putExtra("phoneNumber", getIntent().getStringExtra("phoneNumber"));
                intent.putExtra("occupation", getIntent().getStringExtra("occupation"));
                intent.putExtra("sittingTimeAtCurrentAddress", getIntent().getStringExtra("sittingTimeAtCurrentAddress"));
                intent.putExtra("bestTimeToCall", getIntent().getStringExtra("bestTimeToCall"));
                startActivity(intent);
                finish();
            }
        });

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_forward) {
                    Intent intent = new Intent(CompleteFamilyAndHouseholdInformationActivity.this, CompleteAboutOtherOwnedPetsActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String anyKnownAllergy, familyAgrees, provideAdequateLoveAndAttention, adultsNumber,
                        childrenNumber, householdDescription, rentingRules;

                adultsNumber = Objects.requireNonNull(adultsNumberTextInputEditText.getText()).toString().trim();
                childrenNumber = Objects.requireNonNull(childrenNumberTextInputEditText.getText()).toString().trim();
                householdDescription = Objects.requireNonNull(householdDescriptionTextInputEditText.getText()).toString().trim();
                rentingRules = Objects.requireNonNull(rentingRulesTextInputEditText.getText()).toString().trim();

                ValidationManager.getInstance().doValidation(CompleteFamilyAndHouseholdInformationActivity.this,
                        adultsNumberTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CompleteFamilyAndHouseholdInformationActivity.this,
                        childrenNumberTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CompleteFamilyAndHouseholdInformationActivity.this,
                        householdDescriptionTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CompleteFamilyAndHouseholdInformationActivity.this,
                        rentingRulesTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CompleteFamilyAndHouseholdInformationActivity.this,
                        householdTextInputLayout).checkEmpty();

                if (ValidationManager.getInstance().isNothingEmpty()) {
                    Intent intent = new Intent(CompleteFamilyAndHouseholdInformationActivity.this, CompleteAboutOtherOwnedPetsActivity.class);

                    adoptionApplicationsReference.child(loggedUserId).child("familyAndHouseholdInformation").child("adultsNumber").setValue(adultsNumber);
                    adoptionApplicationsReference.child(loggedUserId).child("familyAndHouseholdInformation").child("childrenNumber").setValue(childrenNumber);
                    adoptionApplicationsReference.child(loggedUserId).child("familyAndHouseholdInformation").child("householdSelectedType").setValue(householdSelectedType);
                    adoptionApplicationsReference.child(loggedUserId).child("familyAndHouseholdInformation").child("householdDescription").setValue(householdDescription);
                    adoptionApplicationsReference.child(loggedUserId).child("familyAndHouseholdInformation").child("rentingRules").setValue(rentingRules);
                    if (knownAllergy.isChecked()) {
                        anyKnownAllergy = "yes";
                        adoptionApplicationsReference.child(loggedUserId).child("familyAndHouseholdInformation").child("knownAllergy").setValue(anyKnownAllergy);
                        intent.putExtra("knownAllergy", anyKnownAllergy);
                    } else if (notKnownAllergy.isChecked()) {
                        anyKnownAllergy = "no";
                        adoptionApplicationsReference.child(loggedUserId).child("familyAndHouseholdInformation").child("knownAllergy").setValue(anyKnownAllergy);
                        intent.putExtra("knownAllergy", anyKnownAllergy);
                    }
                    if (allAgree.isChecked()) {
                        familyAgrees = "yes";
                        adoptionApplicationsReference.child(loggedUserId).child("familyAndHouseholdInformation").child("allAgree").setValue(familyAgrees);
                        intent.putExtra("allAgree", familyAgrees);
                    } else if (notAllAgree.isChecked()) {
                        familyAgrees = "no";
                        adoptionApplicationsReference.child(loggedUserId).child("familyAndHouseholdInformation").child("allAgree").setValue(familyAgrees);
                        intent.putExtra("allAgree", familyAgrees);
                    }
                    if (adequateLoveAndAttention.isChecked()) {
                        provideAdequateLoveAndAttention = "yes";
                        adoptionApplicationsReference.child(loggedUserId).child("familyAndHouseholdInformation").child("adequateLoveAndAttention").setValue(provideAdequateLoveAndAttention);
                        intent.putExtra("adequateLoveAndAttention", provideAdequateLoveAndAttention);
                    } else if (notAdequateLoveAndAttention.isChecked()) {
                        provideAdequateLoveAndAttention = "no";
                        adoptionApplicationsReference.child(loggedUserId).child("familyAndHouseholdInformation").child("adequateLoveAndAttention").setValue(provideAdequateLoveAndAttention);
                        intent.putExtra("adequateLoveAndAttention", provideAdequateLoveAndAttention);
                    }

                    intent.putExtra("adultsNumber", adultsNumber);
                    intent.putExtra("childrenNumber", childrenNumber);
                    intent.putExtra("householdSelectedType", householdSelectedType);
                    intent.putExtra("householdDescription", householdDescription);
                    intent.putExtra("rentingRules", rentingRules);
                    intent.putExtra("petName", getIntent().getStringExtra("petName"));
                    intent.putExtra("petImage1DownloadLink", getIntent().getStringExtra("petImage1DownloadLink"));
                    startActivity(intent);
                    finish();
                }
            }
        });

        setTextWatcher();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.hashCode() == Objects.requireNonNull(adultsNumberTextInputLayout.getEditText()).getText().hashCode()) {
            adultsNumberTextInputLayout.setErrorEnabled(false);
        } else if (editable.hashCode() == Objects.requireNonNull(childrenNumberTextInputLayout.getEditText()).getText().hashCode()) {
            childrenNumberTextInputLayout.setErrorEnabled(false);
        } else if (editable.hashCode() == Objects.requireNonNull(householdTextInputLayout.getEditText()).getText().hashCode()) {
            householdTextInputLayout.setErrorEnabled(false);
        } else if (editable.hashCode() == Objects.requireNonNull(householdDescriptionTextInputLayout.getEditText()).getText().hashCode()) {
            householdDescriptionTextInputLayout.setErrorEnabled(false);
        } else if (editable.hashCode() == Objects.requireNonNull(rentingRulesTextInputLayout.getEditText()).getText().hashCode()) {
            rentingRulesTextInputLayout.setErrorEnabled(false);
        }
    }

    private void setTextWatcher() {
        Objects.requireNonNull(adultsNumberTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(childrenNumberTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(householdTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(householdDescriptionTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(rentingRulesTextInputLayout.getEditText()).addTextChangedListener(this);
    }

    @Override
    public void setError(TextInputLayout textInputLayout, String errorMessage) {
        textInputLayout.setError(errorMessage);
    }
}