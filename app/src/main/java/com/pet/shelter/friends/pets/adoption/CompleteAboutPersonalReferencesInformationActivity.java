package com.pet.shelter.friends.pets.adoption;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
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

public class CompleteAboutPersonalReferencesInformationActivity extends AppCompatActivity implements TextWatcher, ErrorSetter {

    private String loggedUserId;
    private DatabaseReference adoptionApplicationsReference;
    private TextInputLayout nameTextInputLayout, addressTextInputLayout, phoneNumberTextInputLayout,
            emailTextInputLayout, relationshipTextInputLayout;
    private String selectedRelationship;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_about_personal_references_information);

        loggedUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        adoptionApplicationsReference = FirebaseDatabase.getInstance().getReference("adoptionApplications");
        MaterialToolbar materialToolbar = findViewById(R.id.completeAboutPersonalReferencesInformation_materialToolbar);
        MaterialButton materialButton = findViewById(R.id.completeAboutPersonalReferencesInformation_materialButton);
        nameTextInputLayout = findViewById(R.id.completeAboutPersonalReferencesInformationName_textInputLayout);
        addressTextInputLayout = findViewById(R.id.completeAboutPersonalReferencesInformationAddress_textInputLayout);
        phoneNumberTextInputLayout = findViewById(R.id.completeAboutPersonalReferencesInformationPhoneNumber_textInputLayout);
        emailTextInputLayout = findViewById(R.id.completeAboutPersonalReferencesInformationEmail_textInputLayout);
        relationshipTextInputLayout = findViewById(R.id.completeAboutPersonalReferencesInformationRelationship_textInputLayout);
        TextInputEditText nameTextInputEditText = findViewById(R.id.completeAboutPersonalReferencesInformationName_textInputEditText);
        TextInputEditText addressTextInputEditText = findViewById(R.id.completeAboutPersonalReferencesInformationAddress_textInputEditText);
        TextInputEditText phoneNumberTextInputEditText = findViewById(R.id.completeAboutPersonalReferencesInformationPhoneNumber_textInputEditText);
        TextInputEditText emailTextInputEditText = findViewById(R.id.completeAboutPersonalReferencesInformationEmail_textInputEditText);
        MaterialAutoCompleteTextView relationshipMaterialAutoCompleteTextView = findViewById(R.id.completeAboutPersonalReferencesAdoptionInformationRelationship_materialAutoCompleteTextView);
        ConstraintLayout constraintLayout = findViewById(R.id.completeAboutPersonalReferencesInformation_constraintLayout);

        String[] relationshipTypes = {"Friend", "Other Relative", "Mother", "Father", "Sister", "Brother"};

        ArrayAdapter<String> relationshipAdapter = new ArrayAdapter<>(this, R.layout.drop_down_item, relationshipTypes);
        relationshipMaterialAutoCompleteTextView.setAdapter(relationshipAdapter);

        relationshipMaterialAutoCompleteTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRelationship = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompleteAboutPersonalReferencesInformationActivity.this, CompleteAboutWishedPetAdoptionInformationActivity.class);
                intent.putExtra("daytimePlaceDescription", getIntent().getStringExtra("daytimePlaceDescription"));
                intent.putExtra("nighttimePlaceDescription", getIntent().getStringExtra("nighttimePlaceDescription"));
                intent.putExtra("aloneHoursNumber", getIntent().getStringExtra("aloneHoursNumber"));
                intent.putExtra("licensedVeterinarian", getIntent().getStringExtra("licensedVeterinarian"));
                intent.putExtra("noLongerKeep", getIntent().getStringExtra("noLongerKeep"));
                startActivity(intent);
                finish();
            }
        });

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Snackbar snackbar = Snackbar.make(constraintLayout, "Please save each form paragraph data!", Snackbar.LENGTH_LONG);
                snackbar.show();
                return true;
            }
        });

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, address, phoneNumber, email;

                name = Objects.requireNonNull(nameTextInputEditText.getText()).toString().trim();
                address = Objects.requireNonNull(addressTextInputEditText.getText()).toString().trim();
                phoneNumber = Objects.requireNonNull(phoneNumberTextInputEditText.getText()).toString().trim();
                email = Objects.requireNonNull(emailTextInputEditText.getText()).toString().trim();

                ValidationManager.getInstance().doValidation(CompleteAboutPersonalReferencesInformationActivity.this,
                        nameTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CompleteAboutPersonalReferencesInformationActivity.this,
                        addressTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CompleteAboutPersonalReferencesInformationActivity.this,
                        phoneNumberTextInputLayout).checkEmpty().checkPhoneNumber();
                ValidationManager.getInstance().doValidation(CompleteAboutPersonalReferencesInformationActivity.this,
                        emailTextInputLayout).checkEmpty().checkEmail();

                if (ValidationManager.getInstance().arePhoneNumberAndEmailValidAndNothingEmpty()) {
                    Intent intent = new Intent(CompleteAboutPersonalReferencesInformationActivity.this, GenerateAdoptionApplicationPDFActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("address", address);
                    intent.putExtra("phoneNumber", phoneNumber);
                    intent.putExtra("email", email);
                    intent.putExtra("relationshipType", selectedRelationship);
                    adoptionApplicationsReference.child(loggedUserId).child("personalReferences").child("name").setValue(name);
                    adoptionApplicationsReference.child(loggedUserId).child("personalReferences").child("address").setValue(address);
                    adoptionApplicationsReference.child(loggedUserId).child("personalReferences").child("phoneNumber").setValue(phoneNumber);
                    adoptionApplicationsReference.child(loggedUserId).child("personalReferences").child("email").setValue(email);
                    adoptionApplicationsReference.child(loggedUserId).child("personalReferences").child("relationshipType").setValue(selectedRelationship);
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
        Objects.requireNonNull(addressTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(phoneNumberTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(emailTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(relationshipTextInputLayout.getEditText()).addTextChangedListener(this);
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
        } else if (editable.hashCode() == Objects.requireNonNull(addressTextInputLayout.getEditText()).getText().hashCode()) {
            addressTextInputLayout.setErrorEnabled(false);
        } else if (editable.hashCode() == Objects.requireNonNull(phoneNumberTextInputLayout.getEditText()).getText().hashCode()) {
            phoneNumberTextInputLayout.setErrorEnabled(false);
        } else if (editable.hashCode() == Objects.requireNonNull(emailTextInputLayout.getEditText()).getText().hashCode()) {
            emailTextInputLayout.setErrorEnabled(false);
        } else if (editable.hashCode() == Objects.requireNonNull(relationshipTextInputLayout.getEditText()).getText().hashCode()) {
            relationshipTextInputLayout.setErrorEnabled(false);
        }
    }

    @Override
    public void setError(TextInputLayout textInputLayout, String errorMessage) {
        textInputLayout.setError(errorMessage);
    }
}