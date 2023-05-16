package com.pet.shelter.friends.profile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pet.shelter.friends.ErrorSetter;
import com.pet.shelter.friends.HomeActivity;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.ValidationManager;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class CreateShelterProfileActivity extends AppCompatActivity implements TextWatcher, ErrorSetter {

    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference registeredShelters;

    public FirebaseStorage firebaseStorage;

    public StorageReference sheltersLogos;

    private String loggedUserId;

    public TextInputLayout ibanTextInputLayout, nameTextInputLayout, addressTextInputLayout,
            latitudeTextInputLayout, longitudeTextInputLayout, phoneNumberTextInputLayout,
            emailTextInputLayout, webPageLinkTextInputLayout, ourMissionTextInputLayout,
            ourAdoptionPolicyTextInputLayout;

    public TextInputEditText ibanTextInputEditText, nameTextInputEditText, addressTextInputEditText,
            latitudeTextInputEditText, longitudeTextInputEditText, phoneNumberTextInputEditText,
            emailTextInputEditText, webPageLinkTextInputEditText, ourMissionTextInputEditText,
            ourAdoptionPolicyTextInputEditText;

    public MaterialButton addShelterMaterialButton;
    public ShapeableImageView shelterLogoShapeImageView;
    private Uri gallerySelectedImageUri;

    private static final int PICK_FROM_GALLERY = 1889;

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shelter_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        registeredShelters = firebaseDatabase.getReference("registeredShelters");
        firebaseStorage = FirebaseStorage.getInstance();
        sheltersLogos = firebaseStorage.getReference();

        loggedUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        ibanTextInputLayout = findViewById(R.id.createShelterProfileIBAN_textInputLayout);
        nameTextInputLayout = findViewById(R.id.createShelterProfileName_textInputLayout);
        addressTextInputLayout = findViewById(R.id.createShelterProfileAddress_textInputLayout);
        latitudeTextInputLayout = findViewById(R.id.createShelterProfileLatitude_textInputLayout);
        longitudeTextInputLayout = findViewById(R.id.createShelterProfileLongitude_textInputLayout);
        phoneNumberTextInputLayout = findViewById(R.id.createShelterProfilePhoneNumber_textInputLayout);
        emailTextInputLayout = findViewById(R.id.createShelterProfileEmail_textInputLayout);
        webPageLinkTextInputLayout = findViewById(R.id.createShelterProfileWebPageLink_textInputLayout);
        ourMissionTextInputLayout = findViewById(R.id.createShelterProfileOurMission_textInputLayout);
        ourAdoptionPolicyTextInputLayout = findViewById(R.id.createShelterProfileOurAdoptionPolicy_textInputLayout);

        ibanTextInputEditText = findViewById(R.id.createShelterProfileIBAN_textInputEditText);
        nameTextInputEditText = findViewById(R.id.createShelterProfileName_textInputEditText);
        addressTextInputEditText = findViewById(R.id.createShelterProfileAddress_textInputEditText);
        latitudeTextInputEditText = findViewById(R.id.createShelterProfileLatitude_textInputEditText);
        longitudeTextInputEditText = findViewById(R.id.createShelterProfileLongitude_textInputEditText);
        phoneNumberTextInputEditText = findViewById(R.id.createShelterProfilePhoneNumber_textInputEditText);
        emailTextInputEditText = findViewById(R.id.createShelterProfileEmail_textInputEditText);
        webPageLinkTextInputEditText = findViewById(R.id.createShelterProfileWebPageLink_textInputEditText);
        ourMissionTextInputEditText = findViewById(R.id.createShelterProfileOurMission_textInputEditText);
        ourAdoptionPolicyTextInputEditText = findViewById(R.id.createShelterProfileOurAdoptionPolicy_textInputEditText);

        addShelterMaterialButton = findViewById(R.id.createShelterProfile_materialButton);

        shelterLogoShapeImageView = findViewById(R.id.createShelterProfileLogo_shapeImageView);

        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        // doSomeOperations();
                        Intent data = result.getData();
                        gallerySelectedImageUri = Objects.requireNonNull(data).getData();
                        InputStream imageStream = null;
                        try {
                            imageStream = getContentResolver().openInputStream(gallerySelectedImageUri);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        BitmapFactory.decodeStream(imageStream);
                        shelterLogoShapeImageView.setImageURI(gallerySelectedImageUri);// To display selected image in image view
                    }
                });

        shelterLogoShapeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                }
                Intent galleryPhotoPickerIntent = new Intent(Intent.ACTION_PICK);
                galleryPhotoPickerIntent.setType("image/*");
                galleryActivityResultLauncher.launch(galleryPhotoPickerIntent);
            }
        });

        addShelterMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String iban, name, address, latitude, longitude, phoneNumber, email, webPage, ourMission, ourAdoptionPolicy;

                iban = Objects.requireNonNull(ibanTextInputEditText.getText()).toString().trim();
                name = Objects.requireNonNull(nameTextInputEditText.getText()).toString().trim();
                address = Objects.requireNonNull(addressTextInputEditText.getText()).toString().trim();
                latitude = Objects.requireNonNull(latitudeTextInputEditText.getText()).toString().trim();
                longitude = Objects.requireNonNull(longitudeTextInputEditText.getText()).toString().trim();
                phoneNumber = Objects.requireNonNull(phoneNumberTextInputEditText.getText()).toString().trim();
                email = Objects.requireNonNull(emailTextInputEditText.getText()).toString().trim();
                webPage = Objects.requireNonNull(webPageLinkTextInputEditText.getText()).toString().trim();
                ourMission = Objects.requireNonNull(ourMissionTextInputEditText.getText()).toString().trim();
                ourAdoptionPolicy = Objects.requireNonNull(ourAdoptionPolicyTextInputEditText.getText()).toString().trim();

                // TODO: VALIDATE FIELDS
                ValidationManager.getInstance().doValidation(CreateShelterProfileActivity.this,
                        ibanTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CreateShelterProfileActivity.this,
                        nameTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CreateShelterProfileActivity.this,
                        addressTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CreateShelterProfileActivity.this,
                        latitudeTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CreateShelterProfileActivity.this,
                        longitudeTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CreateShelterProfileActivity.this,
                        phoneNumberTextInputLayout).checkEmpty().checkPhoneNumber();
                ValidationManager.getInstance().doValidation(CreateShelterProfileActivity.this,
                        emailTextInputLayout).checkEmpty().checkEmail();
                ValidationManager.getInstance().doValidation(CreateShelterProfileActivity.this,
                        webPageLinkTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CreateShelterProfileActivity.this,
                        ourMissionTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CreateShelterProfileActivity.this,
                        ourAdoptionPolicyTextInputLayout).checkEmpty();

                if (ValidationManager.getInstance().isNothingEmpty()) {
                    StorageReference ref = sheltersLogos
                            .child("sheltersLogos")
                            .child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                            .child("images/" + name + "_" + loggedUserId);

                    if (gallerySelectedImageUri != null) {
                        // Adding listeners on upload or failure of image
                        UploadTask uploadTask = ref.putFile(gallerySelectedImageUri);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                Toast.makeText(CreateShelterProfileActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                                String fileLink = task.getResult().toString();

                                                registeredShelters.child(loggedUserId).child("iban").setValue(iban);
                                                registeredShelters.child(loggedUserId).child("name").setValue(name);
                                                registeredShelters.child(loggedUserId).child("address").setValue(address);
                                                registeredShelters.child(loggedUserId).child("latitude").setValue(latitude);
                                                registeredShelters.child(loggedUserId).child("longitude").setValue(longitude);
                                                registeredShelters.child(loggedUserId).child("phoneNumber").setValue(phoneNumber);
                                                registeredShelters.child(loggedUserId).child("email").setValue(email);
                                                registeredShelters.child(loggedUserId).child("webPageLink").setValue(webPage);
                                                registeredShelters.child(loggedUserId).child("ourMission").setValue(ourMission);
                                                registeredShelters.child(loggedUserId).child("ourAdoptionPolicy").setValue(ourAdoptionPolicy);
                                                registeredShelters.child(loggedUserId).child("profileImageDownloadLink").setValue(fileLink);
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CreateShelterProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                    }
                                });
                    }
                    sendUserToNextActivity();
                }


            }
        });

        setTextWatcher();
    }

    public void sendUserToNextActivity() {
        Intent intent = new Intent(CreateShelterProfileActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setTextWatcher() {
        Objects.requireNonNull(ibanTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(nameTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(addressTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(latitudeTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(longitudeTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(phoneNumberTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(emailTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(webPageLinkTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(ourMissionTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(ourAdoptionPolicyTextInputLayout.getEditText()).addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.hashCode() == Objects.requireNonNull(ibanTextInputLayout.getEditText()).getText().hashCode()) {
            ibanTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(nameTextInputLayout.getEditText()).getText().hashCode()) {
            nameTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(addressTextInputLayout.getEditText()).getText().hashCode()) {
            addressTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(latitudeTextInputLayout.getEditText()).getText().hashCode()) {
            latitudeTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(longitudeTextInputLayout.getEditText()).getText().hashCode()) {
            longitudeTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(phoneNumberTextInputLayout.getEditText()).getText().hashCode()) {
            phoneNumberTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(emailTextInputLayout.getEditText()).getText().hashCode()) {
            emailTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(webPageLinkTextInputLayout.getEditText()).getText().hashCode()) {
            webPageLinkTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(ourMissionTextInputLayout.getEditText()).getText().hashCode()) {
            ourMissionTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(ourAdoptionPolicyTextInputLayout.getEditText()).getText().hashCode()) {
            ourAdoptionPolicyTextInputLayout.setErrorEnabled(false);
        }
    }

    @Override
    public void setError(TextInputLayout textInputLayout, String errorMessage) {
        textInputLayout.setError(errorMessage);
    }
}