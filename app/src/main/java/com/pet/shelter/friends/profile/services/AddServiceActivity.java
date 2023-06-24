package com.pet.shelter.friends.profile.services;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pet.shelter.friends.ErrorSetter;
import com.pet.shelter.friends.HomeActivity;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.ValidationManager;
import com.pet.shelter.friends.news.CreateNewsArticleActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class AddServiceActivity extends AppCompatActivity implements TextWatcher, ErrorSetter {

    private static final String TAG = "AddServiceActivity";
    private TextInputLayout nameTextInputLayout, emailTextInputLayout, phoneNumberTextInputLayout,
            cityStateCountryTextInputLayout, addressTextInputLayout, descriptionTextInputLayout,
            serviceCategoryTextInputLayout;
    private TextInputEditText nameTextInputEditText, emailTextInputEditText, phoneNumberTextInputEditText,
            webpageLinkTextInputEditText, cityStateCountryTextInputEditText, addressTextInputEditText,
            descriptionTextInputEditText;
    private DatabaseReference activeServicesReference;
    private String loggedUserId;
    private String serviceCategorySelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        loggedUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        activeServicesReference = FirebaseDatabase.getInstance().getReference("activeServices");

        MaterialToolbar materialToolbar = findViewById(R.id.addService_materialToolbar);
        serviceCategoryTextInputLayout = findViewById(R.id.addServiceCategory_textInputLayout);
        nameTextInputLayout = findViewById(R.id.addServiceProviderName_textInputLayout);
        emailTextInputLayout = findViewById(R.id.addServiceProviderEmail_textInputLayout);
        phoneNumberTextInputLayout = findViewById(R.id.addServiceProviderPhoneNumber_textInputLayout);
        TextInputLayout webpageLinkTextInputLayout = findViewById(R.id.addServiceProviderWebsite_textInputLayout);
        cityStateCountryTextInputLayout = findViewById(R.id.addServiceProviderCityStateCountry_textInputLayout);
        addressTextInputLayout = findViewById(R.id.addServiceProviderAddress_textInputLayout);
        descriptionTextInputLayout = findViewById(R.id.addServiceProviderDescription_textInputLayout);

        nameTextInputEditText = findViewById(R.id.addServiceProviderName_textInputEditText);
        emailTextInputEditText = findViewById(R.id.addServiceProviderEmail_textInputEditText);
        phoneNumberTextInputEditText = findViewById(R.id.addServiceProviderPhoneNumber_textInputEditText);
        webpageLinkTextInputEditText = findViewById(R.id.addServiceProviderWebsite_textInputEditText);
        cityStateCountryTextInputEditText = findViewById(R.id.addServiceProviderCityStateCountry_textInputEditText);
        addressTextInputEditText = findViewById(R.id.addServiceProviderAddress_textInputEditText);
        descriptionTextInputEditText = findViewById(R.id.addServiceProviderDescription_textInputEditText);

        Objects.requireNonNull(emailTextInputLayout.getEditText()).setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        MaterialButton addServiceMaterialButton = findViewById(R.id.addService_materialButton);

        MaterialAutoCompleteTextView servicesCategoriesMaterialAutoCompleteTextView = findViewById(R.id.addServicesCategories_materialAutoCompleteTextView);

        String[] servicesCategories = {"Veterinarian", "Pet trainer", "Pet walker"};

        ArrayAdapter<String> servicesCategoriesAdapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                servicesCategories
        );

        servicesCategoriesMaterialAutoCompleteTextView.setAdapter(servicesCategoriesAdapter);

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddServiceActivity.this, ActiveServicesActivity.class));
                finish();
            }
        });

        servicesCategoriesMaterialAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                serviceCategorySelectedItem = String.valueOf(parent.getItemAtPosition(position));
            }
        });

        addServiceMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = Objects.requireNonNull(nameTextInputEditText.getText()).toString().trim();
                String email = Objects.requireNonNull(emailTextInputEditText.getText()).toString().trim();
                String phoneNumber = Objects.requireNonNull(phoneNumberTextInputEditText.getText()).toString().trim();
                String cityStateCountry = Objects.requireNonNull(cityStateCountryTextInputEditText.getText()).toString().trim();
                String address = Objects.requireNonNull(addressTextInputEditText.getText()).toString().trim();
                String description = Objects.requireNonNull(descriptionTextInputEditText.getText()).toString().trim();
                String webpageLink = Objects.requireNonNull(webpageLinkTextInputEditText.getText()).toString().trim();
                String service = name + "_" + cityStateCountry + "_" + address;

                StorageReference storageReference = FirebaseStorage.getInstance().getReference("profiles").child("users").child(loggedUserId);
                storageReference.child(name+"_"+loggedUserId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        activeServicesReference.child(serviceCategorySelectedItem).child(service).child("providerUserProfileImage").setValue(uri.toString());
                    }
                });

                ValidationManager.getInstance().doValidation(AddServiceActivity.this,
                        nameTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(AddServiceActivity.this,
                        emailTextInputLayout).checkEmpty().checkEmail();
                ValidationManager.getInstance().doValidation(AddServiceActivity.this,
                        phoneNumberTextInputLayout).checkEmpty().checkPhoneNumber();
                ValidationManager.getInstance().doValidation(AddServiceActivity.this,
                        cityStateCountryTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(AddServiceActivity.this,
                        addressTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(AddServiceActivity.this,
                        descriptionTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(AddServiceActivity.this,
                        serviceCategoryTextInputLayout).checkEmpty();

                if (ValidationManager.getInstance().arePhoneNumberAndEmailValidAndNothingEmpty()) {

                    activeServicesReference.child(serviceCategorySelectedItem).child(service).child("serviceType").setValue(serviceCategorySelectedItem);
                    activeServicesReference.child(serviceCategorySelectedItem).child(service).child("name").setValue(name);
                    activeServicesReference.child(serviceCategorySelectedItem).child(service).child("email").setValue(email);
                    activeServicesReference.child(serviceCategorySelectedItem).child(service).child("phoneNumber").setValue(phoneNumber);
                    activeServicesReference.child(serviceCategorySelectedItem).child(service).child("cityStateCountry").setValue(cityStateCountry);
                    activeServicesReference.child(serviceCategorySelectedItem).child(service).child("address").setValue(address);
                    activeServicesReference.child(serviceCategorySelectedItem).child(service).child("description").setValue(description);
                    activeServicesReference.child(serviceCategorySelectedItem).child(service).child("webpageLink").setValue(webpageLink);

                    sendUserToNextActivity();

                } else {
                    Toast.makeText(AddServiceActivity.this, "Please fill the required information", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setTextWatcher();
    }

    public void sendUserToNextActivity() {
        Intent intent = new Intent(AddServiceActivity.this, ActiveServicesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void setTextWatcher() {
        Objects.requireNonNull(nameTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(emailTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(phoneNumberTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(cityStateCountryTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(addressTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(descriptionTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(serviceCategoryTextInputLayout.getEditText()).addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.hashCode() == Objects.requireNonNull(nameTextInputLayout.getEditText()).getText().hashCode()) {
            nameTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(emailTextInputLayout.getEditText()).getText().hashCode()) {
            emailTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(phoneNumberTextInputLayout.getEditText()).getText().hashCode()) {
            phoneNumberTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(cityStateCountryTextInputLayout.getEditText()).getText().hashCode()) {
            cityStateCountryTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(addressTextInputLayout.getEditText()).getText().hashCode()) {
            addressTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(descriptionTextInputLayout.getEditText()).getText().hashCode()) {
            descriptionTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(serviceCategoryTextInputLayout.getEditText()).getText().hashCode()) {
            serviceCategoryTextInputLayout.setErrorEnabled(false);
        }
    }

    @Override
    public void setError(TextInputLayout textInputLayout, String errorMessage) {
        textInputLayout.setError(errorMessage);
    }
}