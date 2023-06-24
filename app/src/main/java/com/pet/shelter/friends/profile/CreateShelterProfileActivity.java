package com.pet.shelter.friends.profile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pet.shelter.friends.ErrorSetter;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.ValidationManager;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class CreateShelterProfileActivity extends AppCompatActivity implements TextWatcher, ErrorSetter {

    private static final int PICK_MAP_POINT_REQUEST = 999;
    private DatabaseReference registeredShelters;
    private StorageReference sheltersLogos;
    private String loggedUserId;
    private TextInputLayout ibanTextInputLayout, nameTextInputLayout, addressTextInputLayout,
            latitudeTextInputLayout, longitudeTextInputLayout, phoneNumberTextInputLayout,
            emailTextInputLayout, webPageLinkTextInputLayout, ourMissionTextInputLayout,
            ourAdoptionPolicyTextInputLayout;

    private TextInputEditText ibanTextInputEditText, nameTextInputEditText, addressTextInputEditText,
            latitudeTextInputEditText, longitudeTextInputEditText, phoneNumberTextInputEditText,
            emailTextInputEditText, webPageLinkTextInputEditText, ourMissionTextInputEditText,
            ourAdoptionPolicyTextInputEditText;
    private ShapeableImageView shelterLogoShapeImageView;
    private Uri gallerySelectedImageUri;

    private static final int PICK_FROM_GALLERY = 1889;

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher, mapActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shelter_profile);

        registeredShelters = FirebaseDatabase.getInstance().getReference("registeredShelters");
        sheltersLogos = FirebaseStorage.getInstance().getReference();
        loggedUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        MaterialToolbar materialToolbar = findViewById(R.id.createShelterProfile_materialToolbar);
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
        MaterialButton addShelterMaterialButton = findViewById(R.id.createShelterProfile_materialButton);
        ConstraintLayout constraintLayout = findViewById(R.id.createShelterProfile_constraintLayout);

        shelterLogoShapeImageView = findViewById(R.id.createShelterProfileLogo_shapeImageView);

        mapActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        LatLng latLng = data.getParcelableExtra("picked_point");
                        Objects.requireNonNull(latitudeTextInputLayout.getEditText()).setText(String.valueOf(latLng.latitude));
                        Objects.requireNonNull(longitudeTextInputLayout.getEditText()).setText(String.valueOf(latLng.longitude));
                    }
                });

        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
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

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToViewProfileActivity();
            }
        });

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_help) {
                    Snackbar snackbar = Snackbar.make(constraintLayout, "Please fill required information", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackbar.show();
                }
                return false;
            }
        });

        latitudeTextInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPointOnMap();
            }
        });
        longitudeTextInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPointOnMap();
            }
        });

        addShelterMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String iban, name, address, latitude, longitude, phoneNumber, email, webPage,
                        ourMission, ourAdoptionPolicy, shelterToSave;

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
                shelterToSave = name + "_" + address;

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

                if (ValidationManager.getInstance().arePhoneNumberAndEmailValidAndNothingEmpty()) {
                    StorageReference ref = sheltersLogos
                            .child("sheltersLogos")
                            .child(Objects.requireNonNull(loggedUserId))
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

                                                registeredShelters.child(shelterToSave).child("iban").setValue(iban);
                                                registeredShelters.child(shelterToSave).child("name").setValue(name);
                                                registeredShelters.child(shelterToSave).child("address").setValue(address);
                                                registeredShelters.child(shelterToSave).child("latitude").setValue(latitude);
                                                registeredShelters.child(shelterToSave).child("longitude").setValue(longitude);
                                                registeredShelters.child(shelterToSave).child("phoneNumber").setValue(phoneNumber);
                                                registeredShelters.child(shelterToSave).child("email").setValue(email);
                                                registeredShelters.child(shelterToSave).child("webPageLink").setValue(webPage);
                                                registeredShelters.child(shelterToSave).child("ourMission").setValue(ourMission);
                                                registeredShelters.child(shelterToSave).child("ourAdoptionPolicy").setValue(ourAdoptionPolicy);
                                                registeredShelters.child(shelterToSave).child("profileImageDownloadLink").setValue(fileLink);
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CreateShelterProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                        sendUserToViewProfileActivity();
                    } else {
                        Snackbar snackbar = Snackbar.make(constraintLayout, "Please add a logo", Snackbar.LENGTH_SHORT);
                        snackbar.setAction("Dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(constraintLayout, "Please resolve error messages", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackbar.show();
                }
            }
        });

        setTextWatcher();
    }

    private void pickPointOnMap() {
        Intent pickPointIntent = new Intent(this, SelectShelterLocationMapActivity.class);
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)  {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PICK_MAP_POINT_REQUEST);
        } else {
            mapActivityResultLauncher.launch(pickPointIntent);
        }
    }

    public void sendUserToViewProfileActivity() {
        Intent intent = new Intent(CreateShelterProfileActivity.this, ViewProfileActivity.class);
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