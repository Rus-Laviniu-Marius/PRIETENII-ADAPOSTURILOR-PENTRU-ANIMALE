package com.pet.shelter.friends.profile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pet.shelter.friends.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class UserPersonalDataActivity extends AppCompatActivity {

    private static final int PICK_FROM_GALLERY = 1889;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference profiles;
    private StorageReference profileImages;
    private MaterialToolbar materialToolbar;
    private MaterialButton openGallery, openCamera, update;
    private MaterialTextView aboutMe, aboutMeShortBio, contact, userName, emailUser, contactUser;
    private ShapeableImageView profileImage;
    private TextInputLayout nameTextInputLayout, ageTextInputLayout, addressTextInputLayout, phoneNumberTextInputLayout, descriptionTextInputLayout;
    private TextInputEditText nameTextInputEditText, ageTextInputEditText, addressTextInputEditText, phoneNumberTextInputEditText, descriptionTextInputEditText;
    private Uri gallerySelectedImageUri, cameraCapturedImageUri;
    private Bitmap cameraCapturedImageBitmap;
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher, cameraActivityResultLauncher;
    private String loggedUserId;

    private boolean isPressed = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_personal_data);

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
                        profileImage.setImageURI(gallerySelectedImageUri);// To display selected image in image view
                    }
                });

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Bundle bundle = result.getData().getExtras();
                        cameraCapturedImageBitmap = (Bitmap) bundle.get("data");
                        profileImage.setImageBitmap(cameraCapturedImageBitmap);
                        cameraCapturedImageUri = getImageUri(this, cameraCapturedImageBitmap);
                    }
                });

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        profiles = firebaseDatabase.getReference("profiles");
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        profileImages = firebaseStorage.getReference("profiles");

        loggedUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        materialToolbar = findViewById(R.id.viewUserPersonalData_materialToolbar);
        openGallery = findViewById(R.id.userPersonalDataOpenGallery_materialButton);
        openCamera = findViewById(R.id.userPersonalDataOpenCamera_materialButton);
        emailUser = findViewById(R.id.userPersonalDataContactEmail_materialTextView);
        contactUser = findViewById(R.id.userPersonalDataContactPhone_materialTextView);
        update = findViewById(R.id.updateUserPersonalData_materialButton);
        aboutMe = findViewById(R.id.userPersonalDataAboutMe_materialTextView);
        aboutMeShortBio = findViewById(R.id.userPersonalDataShortBio_materialTextView);
        contact = findViewById(R.id.userPersonalDataContact_materialTextView);
        userName = findViewById(R.id.userPersonalDataUserName_materialTextView);
        profileImage = findViewById(R.id.userPersonalDataImage_shapeImageView);
        nameTextInputLayout = findViewById(R.id.editUserPersonalDataName_textInputLayout);
        ageTextInputLayout = findViewById(R.id.editUserPersonalDataAge_textInputLayout);
        addressTextInputLayout = findViewById(R.id.editUserPersonalDataAddress_textInputLayout);
        phoneNumberTextInputLayout = findViewById(R.id.editUserPersonalDataPhoneNumber_textInputLayout);
        descriptionTextInputLayout = findViewById(R.id.editUserPersonalDataDescription_textInputLayout);
        nameTextInputEditText = findViewById(R.id.editUserPersonalDataName_textInputEditText);
        ageTextInputEditText = findViewById(R.id.editUserPersonalDataAge_textInputEditText);
        addressTextInputEditText = findViewById(R.id.editUserPersonalDataAddress_textInputEditText);
        phoneNumberTextInputEditText = findViewById(R.id.editUserPersonalDataPhoneNumber_textInputEditText);
        descriptionTextInputEditText = findViewById(R.id.editUserPersonalDataDescription_textInputEditText);

        readDataFromDatabase();
        setOnClickListeners();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void readDataFromDatabase() {
        profiles.child("users").child(loggedUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name, email, age, address, phoneNumber, profileImageDownloadLink, description;

                email = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();

                name = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                age = Objects.requireNonNull(snapshot.child("age").getValue()).toString();
                address = Objects.requireNonNull(snapshot.child("address").getValue()).toString();
                phoneNumber = Objects.requireNonNull(snapshot.child("phoneNumber").getValue()).toString();
                description = Objects.requireNonNull(snapshot.child("description").getValue()).toString();
                profileImageDownloadLink = Objects.requireNonNull(snapshot.child("profileImageDownloadLink").getValue()).toString();

                userName.setText(name);
                aboutMeShortBio.setText(description);
                emailUser.setText(email);
                contactUser.setText(phoneNumber);
                Picasso.get().load(profileImageDownloadLink).into(profileImage);

                Objects.requireNonNull(nameTextInputLayout.getEditText()).setText(name);
                Objects.requireNonNull(ageTextInputLayout.getEditText()).setText(age);
                Objects.requireNonNull(addressTextInputLayout.getEditText()).setText(address);
                Objects.requireNonNull(phoneNumberTextInputLayout.getEditText()).setText(phoneNumber);
                Objects.requireNonNull(descriptionTextInputLayout.getEditText()).setText(description);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setOnClickListeners() {

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserPersonalDataActivity.this, ViewProfileActivity.class));
            }
        });


        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_edit) {

                    if (isPressed) {

                    openCamera.setVisibility(View.VISIBLE);
                    openGallery.setVisibility(View.VISIBLE);

                    nameTextInputLayout.setVisibility(View.VISIBLE);
                    ageTextInputLayout.setVisibility(View.VISIBLE);
                    addressTextInputLayout.setVisibility(View.VISIBLE);
                    phoneNumberTextInputLayout.setVisibility(View.VISIBLE);
                    descriptionTextInputLayout.setVisibility(View.VISIBLE);

                    update.setVisibility(View.VISIBLE);

                    emailUser.setVisibility(View.GONE);
                    contactUser.setVisibility(View.GONE);
                    aboutMe.setVisibility(View.GONE);
                    aboutMeShortBio.setVisibility(View.GONE);
                    contact.setVisibility(View.GONE);
                    userName.setVisibility(View.GONE);
                    } else {
                        openCamera.setVisibility(View.GONE);
                        openGallery.setVisibility(View.GONE);

                        nameTextInputLayout.setVisibility(View.GONE);
                        ageTextInputLayout.setVisibility(View.GONE);
                        addressTextInputLayout.setVisibility(View.GONE);
                        phoneNumberTextInputLayout.setVisibility(View.GONE);
                        descriptionTextInputLayout.setVisibility(View.GONE);

                        update.setVisibility(View.GONE);

                        emailUser.setVisibility(View.VISIBLE);
                        contactUser.setVisibility(View.VISIBLE);
                        aboutMe.setVisibility(View.VISIBLE);
                        aboutMeShortBio.setVisibility(View.VISIBLE);
                        contact.setVisibility(View.VISIBLE);
                        userName.setVisibility(View.VISIBLE);
                    }
                    isPressed = !isPressed;
                }

                return true;
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, age, address, phoneNumber, description;

                name = Objects.requireNonNull(nameTextInputEditText.getText()).toString().trim();
                age = Objects.requireNonNull(ageTextInputEditText.getText()).toString().trim();
                address = Objects.requireNonNull(addressTextInputEditText.getText()).toString().trim();
                phoneNumber = Objects.requireNonNull(phoneNumberTextInputEditText.getText()).toString().trim();
                description = Objects.requireNonNull(descriptionTextInputEditText.getText()).toString().trim();

                // Defining the child of storageReference
                StorageReference ref = profileImages
                        .child("users")
                        .child(Objects.requireNonNull(loggedUserId))
                        .child(name + "_" + loggedUserId);

                if (gallerySelectedImageUri != null) {
                    // Adding listeners on upload or failure of image
                    UploadTask uploadTask = ref.putFile(gallerySelectedImageUri);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            Toast.makeText(UserPersonalDataActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                            String fileLink = task.getResult().toString();
                                                profiles.child("users").child(loggedUserId).child("name").setValue(name);
                                                profiles.child("users").child(loggedUserId).child("age").setValue(age);
                                                profiles.child("users").child(loggedUserId).child("address").setValue(address);
                                                profiles.child("users").child(loggedUserId).child("phoneNumber").setValue(phoneNumber);
                                                profiles.child("users").child(loggedUserId).child("description").setValue(description);
                                                profiles.child("users").child(loggedUserId).child("profileImageDownloadLink").setValue(fileLink);
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UserPersonalDataActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else if (cameraCapturedImageUri != null) {
                    // Adding listeners on upload or failure of image
                    UploadTask uploadTask = ref.putFile(cameraCapturedImageUri);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            Toast.makeText(UserPersonalDataActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                            String fileLink = task.getResult().toString();
                                                profiles.child("users").child(loggedUserId).child("name").setValue(name);
                                                profiles.child("users").child(loggedUserId).child("age").setValue(age);
                                                profiles.child("users").child(loggedUserId).child("address").setValue(address);
                                                profiles.child("users").child(loggedUserId).child("phoneNumber").setValue(phoneNumber);
                                                profiles.child("users").child(loggedUserId).child("description").setValue(description);
                                                profiles.child("users").child(loggedUserId).child("profileImageDownloadLink").setValue(fileLink);
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UserPersonalDataActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    profiles.child("users").child(loggedUserId).child("name").setValue(name);
                    profiles.child("users").child(loggedUserId).child("age").setValue(age);
                    profiles.child("users").child(loggedUserId).child("address").setValue(address);
                    profiles.child("users").child(loggedUserId).child("phoneNumber").setValue(phoneNumber);
                    profiles.child("users").child(loggedUserId).child("description").setValue(description);

                }

                openCamera.setVisibility(View.GONE);
                openGallery.setVisibility(View.GONE);

                nameTextInputLayout.setVisibility(View.GONE);
                ageTextInputLayout.setVisibility(View.GONE);
                addressTextInputLayout.setVisibility(View.GONE);
                phoneNumberTextInputLayout.setVisibility(View.GONE);
                descriptionTextInputLayout.setVisibility(View.GONE);

                update.setVisibility(View.GONE);

                emailUser.setVisibility(View.VISIBLE);
                contactUser.setVisibility(View.VISIBLE);
                aboutMe.setVisibility(View.VISIBLE);
                aboutMeShortBio.setVisibility(View.VISIBLE);
                contact.setVisibility(View.VISIBLE);
                userName.setVisibility(View.VISIBLE);
            }
        });

        openGallery.setOnClickListener(new View.OnClickListener() {
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

        openCamera.setOnClickListener(new View.OnClickListener() {
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
                        Toast.makeText(UserPersonalDataActivity.this, "There is no app that supports this action", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

}