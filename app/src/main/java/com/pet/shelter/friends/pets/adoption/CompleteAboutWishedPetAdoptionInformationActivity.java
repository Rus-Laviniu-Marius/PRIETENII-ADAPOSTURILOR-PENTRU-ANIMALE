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

public class CompleteAboutWishedPetAdoptionInformationActivity extends AppCompatActivity implements TextWatcher, ErrorSetter {

    private DatabaseReference adoptionApplicationsReference;
    private String loggedUserId, licensedVeterinarianValue, impossibilityToKeepValue;
    private TextInputLayout daytimePlaceDescriptionTextInputLayout, nighttimePlaceDescriptionTextInputLayout,
            aloneHoursNumberTextInputLayout;
    private MaterialRadioButton licensedVeterinarianYes, licensedVeterinarianNo, noLongerKeepYes, noLongerKeepNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_about_wished_pet_adoption_information);

        adoptionApplicationsReference = FirebaseDatabase.getInstance().getReference("adoptionApplications");
        loggedUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        MaterialToolbar materialToolbar = findViewById(R.id.completeAboutWishedPetAdoptionInformation_materialToolbar);
        daytimePlaceDescriptionTextInputLayout = findViewById(R.id.completeAboutWishedPetAdoptionInformationDayTimePlaceDescription_textInputLayout);
        TextInputEditText daytimePlaceDescriptionTextInputEditText = findViewById(R.id.completeAboutWishedPetAdoptionInformationDayTimePlaceDescription_textInputEditText);
        nighttimePlaceDescriptionTextInputLayout = findViewById(R.id.completeAboutWishedPetAdoptionInformationNightTimePlaceDescription_textInputLayout);
        TextInputEditText nighttimePlaceDescriptionTextInputEditText = findViewById(R.id.completeAboutWishedPetAdoptionInformationNightTimePlaceDescription_textInputEditText);
        aloneHoursNumberTextInputLayout = findViewById(R.id.completeAboutWishedPetAdoptionInformationAloneHoursNumber_textInputLayout);
        TextInputEditText aloneHoursNumberTextInputEditText = findViewById(R.id.completeAboutWishedPetAdoptionInformationAloneHoursNumber_textInputEditText);
        licensedVeterinarianYes = findViewById(R.id.completeAboutWishedPetAdoptionInformationAgreementForLicensedVeterinarianYes_materialRadioButton);
        licensedVeterinarianNo = findViewById(R.id.completeAboutWishedPetAdoptionInformationAgreementForLicensedVeterinarianNo_materialRadioButton);
        noLongerKeepYes = findViewById(R.id.completeAboutWishedPetAdoptionInformationAgreementToContactShelterIfCanNoLongerKeepYes_materialRadioButton);
        noLongerKeepNo = findViewById(R.id.completeAboutWishedPetAdoptionInformationAgreementToContactShelterIfCanNoLongerKeepNo_materialRadioButton);
        MaterialButton materialButton = findViewById(R.id.completeAboutWishedPetAdoptionInformation_materialButton);

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompleteAboutWishedPetAdoptionInformationActivity.this, CompleteAboutRegularVeterinarianInformationActivity.class);
                intent.putExtra("name", getIntent().getStringExtra("name"));
                intent.putExtra("clinicName", getIntent().getStringExtra("clinicName"));
                intent.putExtra("clinicAddress", getIntent().getStringExtra("clinicAddress"));
                intent.putExtra("clinicPhoneNumber", getIntent().getStringExtra("clinicPhoneNumber"));
                intent.putExtra("clinicEmail", getIntent().getStringExtra("clinicEmail"));
                startActivity(intent);
                finish();
            }
        });

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_forward) {
                    Intent intent = new Intent(CompleteAboutWishedPetAdoptionInformationActivity.this, CompleteAboutPersonalReferencesInformationActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String daytimePlaceDescription, nighttimePlaceDescription, aloneHoursNumber;

                daytimePlaceDescription = Objects.requireNonNull(daytimePlaceDescriptionTextInputEditText.getText()).toString().trim();
                nighttimePlaceDescription = Objects.requireNonNull(nighttimePlaceDescriptionTextInputEditText.getText()).toString().trim();
                aloneHoursNumber = Objects.requireNonNull(aloneHoursNumberTextInputEditText.getText()).toString().trim();

                ValidationManager.getInstance().doValidation(CompleteAboutWishedPetAdoptionInformationActivity.this,
                        daytimePlaceDescriptionTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CompleteAboutWishedPetAdoptionInformationActivity.this,
                        nighttimePlaceDescriptionTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CompleteAboutWishedPetAdoptionInformationActivity.this,
                        aloneHoursNumberTextInputLayout).checkEmpty();

                if (ValidationManager.getInstance().isNothingEmpty()) {
                    Intent intent = new Intent(CompleteAboutWishedPetAdoptionInformationActivity.this, CompleteAboutPersonalReferencesInformationActivity.class);

                    intent.putExtra("daytimePlaceDescription", daytimePlaceDescription);
                    intent.putExtra("nighttimePlaceDescription", nighttimePlaceDescription);
                    intent.putExtra("aloneHoursNumber", aloneHoursNumber);
                    adoptionApplicationsReference.child(loggedUserId).child("aboutWishedPet").child("daytimePlaceDescription").setValue(daytimePlaceDescription);
                    adoptionApplicationsReference.child(loggedUserId).child("aboutWishedPet").child("nighttimePlaceDescription").setValue(nighttimePlaceDescription);
                    adoptionApplicationsReference.child(loggedUserId).child("aboutWishedPet").child("aloneHoursNumber").setValue(aloneHoursNumber);

                    if (licensedVeterinarianYes.isChecked()) {
                        licensedVeterinarianValue = "yes";
                        adoptionApplicationsReference.child(loggedUserId).child("aboutWishedPet").child("regularHealthCare").setValue(licensedVeterinarianValue);
                        intent.putExtra("licensedVeterinarian", licensedVeterinarianValue);
                    } else if (licensedVeterinarianNo.isChecked()) {
                        licensedVeterinarianValue = "no";
                        adoptionApplicationsReference.child(loggedUserId).child("aboutWishedPet").child("regularHealthCare").setValue(licensedVeterinarianValue);
                        intent.putExtra("licensedVeterinarian", licensedVeterinarianValue);
                    }
                    if (noLongerKeepYes.isChecked()) {
                        impossibilityToKeepValue = "yes";
                        intent.putExtra("noLongerKeep", impossibilityToKeepValue);
                        adoptionApplicationsReference.child(loggedUserId).child("aboutWishedPet").child("contactForSurrender").setValue(impossibilityToKeepValue);
                    } else if (noLongerKeepNo.isChecked()) {
                        impossibilityToKeepValue = "no";
                        intent.putExtra("noLongerKeep", impossibilityToKeepValue);
                        adoptionApplicationsReference.child(loggedUserId).child("aboutWishedPet").child("contactForSurrender").setValue(impossibilityToKeepValue);
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
        Objects.requireNonNull(daytimePlaceDescriptionTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(nighttimePlaceDescriptionTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(aloneHoursNumberTextInputLayout.getEditText()).addTextChangedListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.hashCode() == Objects.requireNonNull(daytimePlaceDescriptionTextInputLayout.getEditText()).getText().hashCode()) {
            daytimePlaceDescriptionTextInputLayout.setErrorEnabled(false);
        } else if (editable.hashCode() == Objects.requireNonNull(nighttimePlaceDescriptionTextInputLayout.getEditText()).getText().hashCode()) {
            nighttimePlaceDescriptionTextInputLayout.setErrorEnabled(false);
        } else if (editable.hashCode() == Objects.requireNonNull(aloneHoursNumberTextInputLayout.getEditText()).getText().hashCode()) {
            aloneHoursNumberTextInputLayout.setErrorEnabled(false);
        }
    }

    @Override
    public void setError(TextInputLayout textInputLayout, String errorMessage) {
        textInputLayout.setError(errorMessage);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}