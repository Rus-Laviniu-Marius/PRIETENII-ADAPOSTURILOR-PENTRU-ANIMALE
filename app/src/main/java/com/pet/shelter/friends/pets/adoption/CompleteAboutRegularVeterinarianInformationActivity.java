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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pet.shelter.friends.ErrorSetter;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.ValidationManager;

import java.util.Objects;

public class CompleteAboutRegularVeterinarianInformationActivity extends AppCompatActivity implements TextWatcher, ErrorSetter {

    private String loggedUserId;
    private DatabaseReference adoptionApplicationsReference;
    private TextInputLayout nameTextInputLayout, clinicNameTextInputLayout, clinicAddressTextInputLayout,
            clinicPhoneNumberTextInputLayout, clinicEmailTextInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_about_regular_veterinarian_information);

        loggedUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        adoptionApplicationsReference = FirebaseDatabase.getInstance().getReference("adoptionApplications");
        MaterialToolbar materialToolbar = findViewById(R.id.completeAboutRegularVeterinarianInformation_materialToolbar);
        MaterialButton materialButton = findViewById(R.id.completeAboutRegularVeterinarianInformation_materialButton);
        nameTextInputLayout = findViewById(R.id.completeAboutRegularVeterinarianInformationName_textInputLayout);
        clinicNameTextInputLayout = findViewById(R.id.completeAboutRegularVeterinarianInformationClinicName_textInputLayout);
        clinicAddressTextInputLayout = findViewById(R.id.completeAboutRegularVeterinarianInformationClinicAddress_textInputLayout);
        clinicPhoneNumberTextInputLayout = findViewById(R.id.completeAboutRegularVeterinarianInformationClinicPhoneNumber_textInputLayout);
        clinicEmailTextInputLayout = findViewById(R.id.completeAboutRegularVeterinarianInformationClinicEmail_textInputLayout);
        TextInputEditText nameTextInputEditText = findViewById(R.id.completeAboutRegularVeterinarianInformationName_textInputEditText);
        TextInputEditText clinicNameTextInputEditText = findViewById(R.id.completeAboutRegularVeterinarianInformationClinicName_textInputEditText);
        TextInputEditText clinicAddressTextInputEditText = findViewById(R.id.completeAboutRegulatVeterinarianInformationClinicAddress_textInputEditText);
        TextInputEditText clinicPhoneNumberTextInputEditText = findViewById(R.id.completeAboutRegularVeterinarianInformationClinicPhoneNumber_textInputEditText);
        TextInputEditText clinicEmailTextInputEditText = findViewById(R.id.completeAboutRegularVeterinarianInformationClinicEmail_textInputEditText);

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompleteAboutRegularVeterinarianInformationActivity.this, CompleteAboutOtherOwnedPetsActivity.class);
                intent.putExtra("ownedPetTypes", getIntent().getStringExtra("ownedPetTypes"));
                intent.putExtra("disciplineMode", getIntent().getStringExtra("disciplineMode"));
                intent.putExtra("vaccinesUpToDate", getIntent().getStringExtra("vaccinesUpToDate"));
                intent.putExtra("haveSurrenderedPet", getIntent().getStringExtra("haveSurrenderedPet"));
                intent.putExtra("haveSurrenderedPetDescription", getIntent().getStringExtra("haveSurrenderedPetDescription"));
                intent.putExtra("hadPetEuthanized", getIntent().getStringExtra("hadPetEuthanized"));
                intent.putExtra("hadEuthanizedPetDescription", getIntent().getStringExtra("hadEuthanizedPetDescription"));
                startActivity(intent);
                finish();
            }
        });

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_forward) {
                    Intent intent = new Intent(CompleteAboutRegularVeterinarianInformationActivity.this, CompleteAboutWishedPetAdoptionInformationActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, clinicName, clinicAddress, clinicPhoneNumber, clinicEmail;

                name = Objects.requireNonNull(nameTextInputEditText.getText()).toString().trim();
                clinicName = Objects.requireNonNull(clinicNameTextInputEditText.getText()).toString().trim();
                clinicAddress = Objects.requireNonNull(clinicAddressTextInputEditText.getText()).toString().trim();
                clinicPhoneNumber = Objects.requireNonNull(clinicPhoneNumberTextInputEditText.getText()).toString().trim();
                clinicEmail = Objects.requireNonNull(clinicEmailTextInputEditText.getText()).toString().trim();

                ValidationManager.getInstance().doValidation(CompleteAboutRegularVeterinarianInformationActivity.this,
                        nameTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CompleteAboutRegularVeterinarianInformationActivity.this,
                        clinicNameTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CompleteAboutRegularVeterinarianInformationActivity.this,
                        clinicAddressTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CompleteAboutRegularVeterinarianInformationActivity.this,
                        clinicPhoneNumberTextInputLayout).checkEmpty().checkPhoneNumber();
                ValidationManager.getInstance().doValidation(CompleteAboutRegularVeterinarianInformationActivity.this,
                        clinicEmailTextInputLayout).checkEmpty().checkEmail();



                if (ValidationManager.getInstance().arePhoneNumberAndEmailValidAndNothingEmpty()) {
                    Intent intent = new Intent(CompleteAboutRegularVeterinarianInformationActivity.this, CompleteAboutWishedPetAdoptionInformationActivity.class);

                    adoptionApplicationsReference.child(loggedUserId).child("regularVeterinarian").child("name").setValue(name);
                    adoptionApplicationsReference.child(loggedUserId).child("regularVeterinarian").child("clinicName").setValue(clinicName);
                    adoptionApplicationsReference.child(loggedUserId).child("regularVeterinarian").child("clinicAddress").setValue(clinicAddress);
                    adoptionApplicationsReference.child(loggedUserId).child("regularVeterinarian").child("clinicPhoneNumber").setValue(clinicPhoneNumber);
                    adoptionApplicationsReference.child(loggedUserId).child("regularVeterinarian").child("clinicEmail").setValue(clinicEmail);

                    intent.putExtra("name", name);
                    intent.putExtra("clinicName", clinicName);
                    intent.putExtra("clinicAddress", clinicAddress);
                    intent.putExtra("clinicPhoneNumber", clinicPhoneNumber);
                    intent.putExtra("clinicEmail", clinicEmail);
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
        Objects.requireNonNull(nameTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(clinicNameTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(clinicAddressTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(clinicPhoneNumberTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(clinicEmailTextInputLayout.getEditText()).addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.hashCode() == Objects.requireNonNull(nameTextInputLayout.getEditText()).getText().hashCode()) {
            nameTextInputLayout.setErrorEnabled(false);
        } else if (editable.hashCode() == Objects.requireNonNull(clinicNameTextInputLayout.getEditText()).getText().hashCode()) {
            clinicNameTextInputLayout.setErrorEnabled(false);
        } else if (editable.hashCode() == Objects.requireNonNull(clinicAddressTextInputLayout.getEditText()).getText().hashCode()) {
            clinicAddressTextInputLayout.setErrorEnabled(false);
        } else if (editable.hashCode() == Objects.requireNonNull(clinicPhoneNumberTextInputLayout.getEditText()).getText().hashCode()) {
            clinicPhoneNumberTextInputLayout.setErrorEnabled(false);
        } else if (editable.hashCode() == Objects.requireNonNull(clinicEmailTextInputLayout.getEditText()).getText().hashCode()) {
            clinicEmailTextInputLayout.setErrorEnabled(false);
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