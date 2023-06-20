package com.pet.shelter.friends.pets.adoption;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pet.shelter.friends.ErrorSetter;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.ValidationManager;

import java.util.Objects;

public class CompleteAboutOtherOwnedPetsActivity extends AppCompatActivity implements TextWatcher, ErrorSetter {

    private DatabaseReference adoptionApplicationsReference;
    private String loggedUserId, vaccinesUpToDate, haveSurrenderedPetValue, hadPetEuthanizedValue,
            haveSurrenderedPetDescription, hadEuthanizedPetDescription;
    private TextInputLayout ownedPetTypesTextInputLayout, disciplineModeTextInputLayout,
            haveSurrenderedPetDescriptionTextInputLayout, hadEuthanizedPetDescriptionTextInputLayout;
    private TextInputEditText haveSurrenderedPetDescriptionTextInputEditText;
    private TextInputEditText hadEuthanizedPetDescriptionTextInputEditText;
    private MaterialRadioButton upToDateVaccines, notUpToDateVaccines, haveSurrenderedPet, haveNotSurrenderedPet,
            hadPetEuthanized, notHadPetEuthanized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_about_other_owned_pets);

        loggedUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        adoptionApplicationsReference = FirebaseDatabase.getInstance().getReference("adoptionApplications");
        MaterialToolbar materialToolbar = findViewById(R.id.completeAboutOtherOwnedPetsInformation_materialToolbar);
        MaterialButton materialButton = findViewById(R.id.completeAboutOtherOwnedPetsInformation_materialButton);
        ownedPetTypesTextInputLayout = findViewById(R.id.completeAboutOtherOwnedPetsInformationType_textInputLayout);
        TextInputEditText ownedPetTypesTextInputEditText = findViewById(R.id.completeAboutOtherOwnedPetsInformationType_textInputEditText);
        TextInputEditText disciplineModeTextInputEditText = findViewById(R.id.completeAboutOtherOwnedPetsInformationDiscipline_TextInputEditText);
        disciplineModeTextInputLayout = findViewById(R.id.completeAboutOtherOwnedPetsInformationDiscipline_textInputLayout);
        upToDateVaccines = findViewById(R.id.completeAboutOtherOwnedPetsInformationUpToDateVaccinesYes_materialRadioButton);
        notUpToDateVaccines = findViewById(R.id.completeAboutOtherOwnedPetsInformationUpToDateVaccinesNo_materialRadioButton);
        haveSurrenderedPet = findViewById(R.id.completeAboutOtherOwnedPetsInformationEverSurrenderedPetYes_materialRadioButton);
        haveNotSurrenderedPet = findViewById(R.id.completeAboutOtherOwnedPetsInformationEverSurrenderedPetNo_materialRadioButton);
        hadPetEuthanized = findViewById(R.id.completeAboutOtherOwnedPetsInformationEverHadPetEuthanizedYes_materialRadioButton);
        notHadPetEuthanized = findViewById(R.id.completeAboutOtherOwnedPetsInformationEverHadPetEuthanizedNo_materialRadioButton);
        haveSurrenderedPetDescriptionTextInputLayout = findViewById(R.id.completeAboutOtherOwnedPetsInformationEverSurrenderedPetWhatHappened_textInputLayout);
        haveSurrenderedPetDescriptionTextInputEditText = findViewById(R.id.completeAboutOtherOwnedPetsInformationEverSurrenderedPetWhatHappened_textInputEditText);
        hadEuthanizedPetDescriptionTextInputEditText = findViewById(R.id.completeAboutOtherOwnedPetsInformationEverHadPetEuthanizedWhatHappened_textInputEditText);
        hadEuthanizedPetDescriptionTextInputLayout = findViewById(R.id.completeAboutOtherOwnedPetsInformationEverHadPetEuthanizedWhatHappened_textInputLayout);

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompleteAboutOtherOwnedPetsActivity.this, CompleteFamilyAndHouseholdInformationActivity.class);
                intent.putExtra("anyKnownAllergy", getIntent().getStringExtra("anyKnownAllergy"));
                intent.putExtra("familyAgrees", getIntent().getStringExtra("familyAgrees"));
                intent.putExtra("provideAdequateLoveAndAttention", getIntent().getStringExtra("provideAdequateLoveAndAttention"));
                intent.putExtra("adultsNumber", getIntent().getStringExtra("adultsNumber"));
                intent.putExtra("childrenNumber", getIntent().getStringExtra("childrenNumber"));
                intent.putExtra("householdSelectedType", getIntent().getStringExtra("householdSelectedType"));
                intent.putExtra("householdDescription", getIntent().getStringExtra("householdDescription"));
                intent.putExtra("rentingRules", getIntent().getStringExtra("rentingRules"));
                startActivity(intent);
                finish();
            }
        });

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_forward) {
                    Intent intent = new Intent(CompleteAboutOtherOwnedPetsActivity.this, CompleteAboutRegularVeterinarianInformationActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ownedPetTypes, disciplineMode;

                ownedPetTypes = Objects.requireNonNull(ownedPetTypesTextInputEditText.getText()).toString().trim();
                disciplineMode = Objects.requireNonNull(disciplineModeTextInputEditText.getText()).toString().trim();

                ValidationManager.getInstance().doValidation(CompleteAboutOtherOwnedPetsActivity.this,
                        ownedPetTypesTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CompleteAboutOtherOwnedPetsActivity.this,
                        disciplineModeTextInputLayout).checkEmpty();

                if (ValidationManager.getInstance().isNothingEmpty()) {
                    Intent intent = new Intent(CompleteAboutOtherOwnedPetsActivity.this, CompleteAboutRegularVeterinarianInformationActivity.class);
                    intent.putExtra("ownedPetTypes", ownedPetTypes);
                    intent.putExtra("disciplineMode", disciplineMode);


                    adoptionApplicationsReference.child(loggedUserId).child("otherOwnedPets").child("ownedPetTypes").setValue(ownedPetTypes);
                    adoptionApplicationsReference.child(loggedUserId).child("otherOwnedPets").child("disciplineMode").setValue(disciplineMode);

                    if (upToDateVaccines.isChecked()) {
                        vaccinesUpToDate = "yes";
                        adoptionApplicationsReference.child(loggedUserId).child("otherOwnedPets").child("vaccinesUpToDate").setValue(vaccinesUpToDate);
                        intent.putExtra("vaccinesUpToDate", vaccinesUpToDate);
                    } else if (notUpToDateVaccines.isChecked()) {
                        vaccinesUpToDate = "no";
                        adoptionApplicationsReference.child(loggedUserId).child("otherOwnedPets").child("vaccinesUpToDate").setValue(vaccinesUpToDate);
                        intent.putExtra("vaccinesUpToDate", vaccinesUpToDate);
                    }
                    if (haveSurrenderedPet.isChecked()) {
                        haveSurrenderedPetValue = "yes";
                        intent.putExtra("haveSurrenderedPet", haveSurrenderedPetValue);
                        adoptionApplicationsReference.child(loggedUserId).child("otherOwnedPets").child("haveSurrenderedPet").setValue(haveSurrenderedPetValue);
                        haveSurrenderedPetDescriptionTextInputLayout.setVisibility(View.VISIBLE);
                        haveSurrenderedPetDescription = Objects.requireNonNull(haveSurrenderedPetDescriptionTextInputEditText.getText()).toString().trim();
                        intent.putExtra("haveSurrenderedPetDescription", haveSurrenderedPetDescription);
                        adoptionApplicationsReference.child(loggedUserId).child("otherOwnedPets").child("haveSurrenderedPetDescription").setValue(haveSurrenderedPetDescription);
                    } else if (haveNotSurrenderedPet.isChecked()) {
                        haveSurrenderedPetValue = "no";
                        haveSurrenderedPetDescriptionTextInputLayout.setVisibility(View.GONE);
                        intent.putExtra("haveSurrenderedPet", haveSurrenderedPetValue);
                        adoptionApplicationsReference.child(loggedUserId).child("otherOwnedPets").child("haveSurrenderedPet").setValue(haveSurrenderedPetValue);
                    }
                    if (hadPetEuthanized.isChecked()) {
                        hadPetEuthanizedValue = "yes";
                        intent.putExtra("hadPetEuthanized", hadPetEuthanizedValue);
                        adoptionApplicationsReference.child(loggedUserId).child("otherOwnedPets").child("hadPetEuthanized").setValue(hadPetEuthanizedValue);
                        hadEuthanizedPetDescriptionTextInputLayout.setVisibility(View.VISIBLE);
                        hadEuthanizedPetDescription = Objects.requireNonNull(hadEuthanizedPetDescriptionTextInputEditText.getText()).toString().trim();
                        intent.putExtra("hadEuthanizedPetDescription", hadEuthanizedPetDescription);
                        adoptionApplicationsReference.child(loggedUserId).child("otherOwnedPets").child("hadPetEuthanizedDescription").setValue(hadEuthanizedPetDescription);
                    } else if (notHadPetEuthanized.isChecked()) {
                        hadPetEuthanizedValue = "no";
                        intent.putExtra("hadPetEuthanized", hadPetEuthanizedValue);
                        adoptionApplicationsReference.child(loggedUserId).child("otherOwnedPets").child("hadPetEuthanized").setValue(hadPetEuthanizedValue);
                        hadEuthanizedPetDescriptionTextInputLayout.setVisibility(View.GONE);
                    }
                    intent.putExtra("petName", getIntent().getStringExtra("petName"));
                    intent.putExtra("petImage1DownloadLink", getIntent().getStringExtra("petImage1DownloadLink"));
                    startActivity(intent);
                    finish();
                }
            }
        });
        setTextWatcher();
    }

    private void setTextWatcher() {
        Objects.requireNonNull(ownedPetTypesTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(disciplineModeTextInputLayout.getEditText()).addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.hashCode() == Objects.requireNonNull(ownedPetTypesTextInputLayout.getEditText()).getText().hashCode()) {
            ownedPetTypesTextInputLayout.setErrorEnabled(false);
        } else if (editable.hashCode() == Objects.requireNonNull(disciplineModeTextInputLayout.getEditText()).getText().hashCode()) {
            disciplineModeTextInputLayout.setErrorEnabled(false);
        }
    }

    @Override
    public void setError(TextInputLayout textInputLayout, String errorMessage) {
        textInputLayout.setError(errorMessage);
    }
}