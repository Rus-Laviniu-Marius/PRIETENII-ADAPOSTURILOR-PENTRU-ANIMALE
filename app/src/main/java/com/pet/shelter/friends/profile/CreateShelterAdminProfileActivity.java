package com.pet.shelter.friends.profile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.ValidationManager;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class CreateShelterAdminProfileActivity extends AppCompatActivity implements TextWatcher, ErrorSetter {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference shelterAdministratorsReference;

    private StorageReference shelterAdministratorsProfileImages;

    private TextInputLayout nameTextInputLayout, ageTextInputLayout, addressTextInputLayout, phoneNumberTextInputLayout;
    private TextInputEditText nameTextInputEditText, ageTextInputEditText, addressTextInputEditText, phoneNumberTextInputEditText;
    private ShapeableImageView profileImageView;

    private Uri gallerySelectedImageUri, cameraCapturedImageUri;

    private Bitmap cameraCapturedImageBitmap;

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher, cameraActivityResultLauncher;

    private String loggedUserId;
    private static final int PICK_FROM_GALLERY = 1889;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shelter_admin_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        shelterAdministratorsProfileImages = firebaseStorage.getReference("shelterAdministratorsProfiles");
        shelterAdministratorsReference = firebaseDatabase.getReference("shelterAdministrators");

        MaterialButton openGalleryMaterialButton = findViewById(R.id.createShelterAdminProfileOpenGallery_materialButton);
        MaterialButton openCameraMaterialButton = findViewById(R.id.createShelterAdminProfileOpenCamera_materialButton);
        MaterialButton createProfileMaterialButton = findViewById(R.id.createShelterAdminProfile_materialButton);

        nameTextInputLayout = findViewById(R.id.createShelterAdminProfileName_textInputLayout);
        ageTextInputLayout = findViewById(R.id.createShelterAdminProfileAge_textInputLayout);
        addressTextInputLayout = findViewById(R.id.createShelterAdminProfileAddress_textInputLayout);
        phoneNumberTextInputLayout = findViewById(R.id.createShelterAdminProfilePhoneNumber_textInputLayout);

        nameTextInputEditText = findViewById(R.id.createShelterAdminProfileName_textInputEditText);
        ageTextInputEditText = findViewById(R.id.createShelterAdminProfileAge_textInputEditText);
        addressTextInputEditText = findViewById(R.id.createShelterAdminProfileAddress_textInputEditText);
        phoneNumberTextInputEditText = findViewById(R.id.createShelterAdminProfilePhoneNumber_textInputEditText);

        profileImageView = findViewById(R.id.createShelterAdminProfileImage_shapeableImageView);

        loggedUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

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
                        profileImageView.setImageURI(gallerySelectedImageUri);// To display selected image in image view
                    }
                });

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Bundle bundle = result.getData().getExtras();
                        cameraCapturedImageBitmap = (Bitmap) bundle.get("data");
                        profileImageView.setImageBitmap(cameraCapturedImageBitmap);
                        cameraCapturedImageUri = getImageUri(this, cameraCapturedImageBitmap);
                    }
                });

        createProfileMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, age, address, phoneNumber;

                name = Objects.requireNonNull(nameTextInputEditText.getText()).toString().trim();
                age = Objects.requireNonNull(ageTextInputEditText.getText()).toString().trim();
                address = Objects.requireNonNull(addressTextInputEditText.getText()).toString().trim();
                phoneNumber = Objects.requireNonNull(phoneNumberTextInputEditText.getText()).toString().trim();

                ValidationManager.getInstance().doValidation(CreateShelterAdminProfileActivity.this,
                        phoneNumberTextInputLayout).checkEmpty().checkPhoneNumber();
                ValidationManager.getInstance().doValidation(CreateShelterAdminProfileActivity.this,
                        ageTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CreateShelterAdminProfileActivity.this,
                        nameTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CreateShelterAdminProfileActivity.this,
                        addressTextInputLayout).checkEmpty();
                if (ValidationManager.getInstance().isPhoneNumberValidAndNothingEmpty()) {
                    StorageReference ref = shelterAdministratorsProfileImages
                            .child("shelterAdministrators")
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
                                                Toast.makeText(CreateShelterAdminProfileActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                                String fileLink = task.getResult().toString();

                                                shelterAdministratorsReference.child(loggedUserId).child("name").setValue(name);
                                                shelterAdministratorsReference.child(loggedUserId).child("age").setValue(age);
                                                shelterAdministratorsReference.child(loggedUserId).child("address").setValue(address);
                                                shelterAdministratorsReference.child(loggedUserId).child("phoneNumber").setValue(phoneNumber);
                                                shelterAdministratorsReference.child(loggedUserId).child("profileImageDownloadLink").setValue(fileLink);
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CreateShelterAdminProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                    }
                                });
                    }

                    if (cameraCapturedImageUri != null) {
                        // Adding listeners on upload or failure of image
                        UploadTask uploadTask = ref.putFile(cameraCapturedImageUri);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                Toast.makeText(CreateShelterAdminProfileActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                                String fileLink = task.getResult().toString();

                                                shelterAdministratorsReference.child(loggedUserId).child("name").setValue(name);
                                                shelterAdministratorsReference.child(loggedUserId).child("age").setValue(age);
                                                shelterAdministratorsReference.child(loggedUserId).child("address").setValue(address);
                                                shelterAdministratorsReference.child(loggedUserId).child("phoneNumber").setValue(phoneNumber);
                                                shelterAdministratorsReference.child(loggedUserId).child("profileImageDownloadLink").setValue(fileLink);
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CreateShelterAdminProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                // Defining the child of storageReference
        });

        openGalleryMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                }
                Intent galleryPhotoPickerIntent = new Intent(Intent.ACTION_PICK);
                galleryPhotoPickerIntent.setType("image/*");
                galleryActivityResultLauncher.launch(galleryPhotoPickerIntent);
            }
        });

        openCameraMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                        cameraActivityResultLauncher.launch(cameraIntent);
                    } else {
                        Toast.makeText(CreateShelterAdminProfileActivity.this, "There is no app that supports this action", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        setTextWatcher();

    }

    public void sendUserToNextActivity() {
        Intent intent = new Intent(CreateShelterAdminProfileActivity.this, CreateShelterProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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
        } else if (editable.hashCode() == Objects.requireNonNull(ageTextInputLayout.getEditText()).getText().hashCode()) {
            ageTextInputLayout.setErrorEnabled(false);
        } else if (editable.hashCode() == Objects.requireNonNull(addressTextInputLayout.getEditText()).getText().hashCode()) {
            addressTextInputLayout.setErrorEnabled(false);
        }
        else if (editable.hashCode() == Objects.requireNonNull(phoneNumberTextInputLayout.getEditText()).getText().hashCode()) {
            phoneNumberTextInputLayout.setErrorEnabled(false);
        }
    }

    private void setTextWatcher() {
        Objects.requireNonNull(nameTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(ageTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(addressTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(phoneNumberTextInputLayout.getEditText()).addTextChangedListener(this);
    }

    @Override
    public void setError(TextInputLayout textInputLayout, String errorMessage) {
        textInputLayout.setError(errorMessage);
    }
}