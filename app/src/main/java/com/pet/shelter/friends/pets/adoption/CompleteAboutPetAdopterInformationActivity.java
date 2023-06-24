package com.pet.shelter.friends.pets.adoption;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pet.shelter.friends.ErrorSetter;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.ValidationManager;

import java.util.Objects;

public class CompleteAboutPetAdopterInformationActivity extends AppCompatActivity implements TextWatcher, ErrorSetter {

    private TextInputLayout fullNameTextInputLayout, addressTextInputLayout, phoneNumberTextInputLayout,
            emailTextInputLayout;
    private TextInputEditText fullNameTextInputEditText, addressTextInputEditText,
            phoneNumberTextInputEditText, emailTextInputEditText;
    private DatabaseReference adoptionApplicationsReference;
    private String loggedUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_about_pet_adopter_information);

        DatabaseReference profilesReference = FirebaseDatabase.getInstance().getReference("profiles");
        MaterialToolbar materialToolbar = findViewById(R.id.completeAboutPetAdopterInformation_materialToolbar);
        fullNameTextInputLayout = findViewById(R.id.completeAboutPetAdopterInformationFullName_materialTextInputLayout);
        addressTextInputLayout = findViewById(R.id.completeAboutPetAdopterInformationAddress_materialTextInputLayout);
        phoneNumberTextInputLayout = findViewById(R.id.completeAboutPetAdopterInformationPhoneNumber_materialTextInputLayout);
        emailTextInputLayout = findViewById(R.id.completeAboutPetAdopterInformationEmail_materialTextInputLayout);
        fullNameTextInputEditText = findViewById(R.id.completeAboutPetAdopterInformationFullName_materialTextInputEditText);
        addressTextInputEditText = findViewById(R.id.completeAboutPetAdopterInformationAddress_materialTextInputEditText);
        phoneNumberTextInputEditText = findViewById(R.id.completeAboutPetAdopterInformationPhoneNumber_materialTextInputEditText);
        emailTextInputEditText = findViewById(R.id.completeAboutPetAdopterInformationEmail_materialTextInputEditText);
        TextInputEditText occupationTextInputEditText = findViewById(R.id.completeAboutPetAdopterInformationOccupation_materialTextInputEditText);
        TextInputEditText sittingTimeAtCurrentAddressTextInputEditText = findViewById(R.id.completeAboutPetAdopterInformationSittingAtCurrentAddress_materialTextInputEditText);
        TextInputEditText bestTimeToCallTextInputEditText = findViewById(R.id.completeAboutPetAdopterInformationBestTimeToCall_materialTextInputEditText);
        MaterialButton materialButton = findViewById(R.id.completeAboutPetAdopterInformation_materialButton);
        adoptionApplicationsReference = FirebaseDatabase.getInstance().getReference("adoptionApplications");
        loggedUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        profilesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("users")) {
                    snapshot = snapshot.child("users");
                    if (snapshot.hasChild(loggedUserId)) {
                        snapshot = snapshot.child(loggedUserId);
                        fullNameTextInputEditText.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                        addressTextInputEditText.setText(Objects.requireNonNull(snapshot.child("address").getValue()).toString());
                        phoneNumberTextInputEditText.setText(Objects.requireNonNull(snapshot.child("phoneNumber").getValue()).toString());
                        emailTextInputEditText.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        fullNameTextInputLayout.setPlaceholderText(getIntent().getStringExtra("fullName"));
//        addressTextInputLayout.setPlaceholderText(getIntent().getStringExtra("address"));
//        emailTextInputLayout.setPlaceholderText(getIntent().getStringExtra("email"));
//        phoneNumberTextInputLayout.setPlaceholderText(getIntent().getStringExtra("phoneNumber"));
        fullNameTextInputEditText.setText(getIntent().getStringExtra("fullName"));
        addressTextInputEditText.setText(getIntent().getStringExtra("address"));
        phoneNumberTextInputEditText.setText(getIntent().getStringExtra("phoneNumber"));
        emailTextInputEditText.setText(getIntent().getStringExtra("email"));
        occupationTextInputEditText.setText(getIntent().getStringExtra("occupation"));
        sittingTimeAtCurrentAddressTextInputEditText.setText(getIntent().getStringExtra("sittingTimeAtCurrentAddress"));
        bestTimeToCallTextInputEditText.setText(getIntent().getStringExtra("bestTimeToCall"));

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CompleteAboutPetAdopterInformationActivity.this, AdoptionApplicationParagraphsActivity.class));
                finish();
            }
        });

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_forward) {
                    Intent intent = new Intent(CompleteAboutPetAdopterInformationActivity.this, CompleteFamilyAndHouseholdInformationActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, email, address, phoneNumber, occupation, sittingTimeAtCurrentAddress, bestTimeToCall;

                name = Objects.requireNonNull(fullNameTextInputEditText.getText()).toString().trim();
                email = Objects.requireNonNull(emailTextInputEditText.getText()).toString().trim();
                address = Objects.requireNonNull(addressTextInputEditText.getText()).toString().trim();
                phoneNumber = Objects.requireNonNull(phoneNumberTextInputEditText.getText()).toString().trim();
                occupation = Objects.requireNonNull(occupationTextInputEditText.getText()).toString().trim();
                sittingTimeAtCurrentAddress = Objects.requireNonNull(sittingTimeAtCurrentAddressTextInputEditText.getText()).toString().trim();
                bestTimeToCall = Objects.requireNonNull(bestTimeToCallTextInputEditText.getText()).toString().trim();

                ValidationManager.getInstance().doValidation(CompleteAboutPetAdopterInformationActivity.this,
                        phoneNumberTextInputLayout).checkEmpty().checkPhoneNumber();
                ValidationManager.getInstance().doValidation(CompleteAboutPetAdopterInformationActivity.this,
                        emailTextInputLayout).checkEmpty().checkEmail();
                ValidationManager.getInstance().doValidation(CompleteAboutPetAdopterInformationActivity.this,
                        fullNameTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CompleteAboutPetAdopterInformationActivity.this,
                        addressTextInputLayout).checkEmpty();
                if (ValidationManager.getInstance().arePhoneNumberAndEmailValidAndNothingEmpty()) {
                    adoptionApplicationsReference.child(loggedUserId).child("aboutPetAdopterInformation").child("fullName").setValue(name);
                    adoptionApplicationsReference.child(loggedUserId).child("aboutPetAdopterInformation").child("email").setValue(email);
                    adoptionApplicationsReference.child(loggedUserId).child("aboutPetAdopterInformation").child("address").setValue(address);
                    adoptionApplicationsReference.child(loggedUserId).child("aboutPetAdopterInformation").child("phoneNumber").setValue(phoneNumber);
                    adoptionApplicationsReference.child(loggedUserId).child("aboutPetAdopterInformation").child("occupation").setValue(occupation);
                    adoptionApplicationsReference.child(loggedUserId).child("aboutPetAdopterInformation").child("sittingTimeAtCurrentAddress").setValue(sittingTimeAtCurrentAddress);
                    adoptionApplicationsReference.child(loggedUserId).child("aboutPetAdopterInformation").child("bestTimeToCall").setValue(bestTimeToCall);

                    Intent intent = new Intent(CompleteAboutPetAdopterInformationActivity.this, CompleteFamilyAndHouseholdInformationActivity.class);
                    intent.putExtra("fullName", name);
                    intent.putExtra("email", email);
                    intent.putExtra("address", address);
                    intent.putExtra("phoneNumber", phoneNumber);
                    intent.putExtra("occupation", occupation);
                    intent.putExtra("sittingTimeAtCurrentAddress", sittingTimeAtCurrentAddress);
                    intent.putExtra("bestTimeToCall", bestTimeToCall);
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
        if (editable.hashCode() == Objects.requireNonNull(fullNameTextInputLayout.getEditText()).getText().hashCode()) {
            fullNameTextInputLayout.setErrorEnabled(false);
        } else if (editable.hashCode() == Objects.requireNonNull(emailTextInputLayout.getEditText()).getText().hashCode()) {
            emailTextInputLayout.setErrorEnabled(false);
        } else if (editable.hashCode() == Objects.requireNonNull(addressTextInputLayout.getEditText()).getText().hashCode()) {
            addressTextInputLayout.setErrorEnabled(false);
        } else if (editable.hashCode() == Objects.requireNonNull(phoneNumberTextInputLayout.getEditText()).getText().hashCode()) {
            phoneNumberTextInputLayout.setErrorEnabled(false);
        }
    }

    private void setTextWatcher() {
        Objects.requireNonNull(fullNameTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(emailTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(addressTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(phoneNumberTextInputLayout.getEditText()).addTextChangedListener(this);
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