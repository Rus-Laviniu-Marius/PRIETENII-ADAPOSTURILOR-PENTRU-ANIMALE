package com.pet.shelter.friends.profile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pet.shelter.friends.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class UserPersonalDataActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int PICK_FROM_GALLERY = 1889;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private ImageView backImageView, editImageView, profileImageView, galleryImageView, cameraImageView;
    private TextView nameTextView, emailTextView, locationTextView, phoneTextView;
    private EditText nameEditText, emailEditText, locationEditText, phoneEditText;

    private Button updateButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersReference, databaseReference;
    private StorageReference userProfileImageStorage;

    private Uri gallerySelectedImageUri, cameraCapturedImageUri;
    private Bitmap cameraCapturedImageBitmap;

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher, cameraActivityResultLauncher;

    private String loggedUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_personal_data);

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

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = firebaseDatabase.getReference("profiles");
        databaseReference = firebaseDatabase.getReference();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        userProfileImageStorage = firebaseStorage.getReference();

        loggedUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        backImageView = findViewById(R.id.userPersonalDataBack_imageView);
        editImageView = findViewById(R.id.userPersonalDataEdit_imageView);
        profileImageView = findViewById(R.id.userPersonalDataProfile_imageView);
        galleryImageView = findViewById(R.id.userPersonalDataOpenGallery_imageView);
        cameraImageView = findViewById(R.id.userPersonalDataOpenCamera_imageView);

        nameTextView = findViewById(R.id.userPersonalDataName_textView);
        emailTextView = findViewById(R.id.userPersonalDataEmail_textView);
        locationTextView = findViewById(R.id.userPersonalDataLocation_textView);
        phoneTextView = findViewById(R.id.userPersonalDataPhoneNumber_textView);

        nameEditText = findViewById(R.id.userPersonalDataName_editText);
        emailEditText = findViewById(R.id.userPersonalDataEmail_editText);
        locationEditText = findViewById(R.id.userPersonalDataLocation_editText);
        phoneEditText = findViewById(R.id.userPersonalDataPhoneNumber_editText);

        updateButton = findViewById(R.id.userPersonalDataUpdate_button);

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
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("users")) {
                    usersReference.child(loggedUserId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name, email, location, phoneNumber;

                            name = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                            email = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                            location = Objects.requireNonNull(snapshot.child("location").getValue()).toString();
                            phoneNumber = Objects.requireNonNull(snapshot.child("phoneNumber").getValue()).toString();
                            String profileImageDownloadLink = Objects.requireNonNull(snapshot.child("profileImageDownloadLink").getValue()).toString();

                            nameTextView.setText(name);
                            emailTextView.setText(email);
                            locationTextView.setText(location);
                            phoneTextView.setText(phoneNumber);
                            Picasso.get().load(profileImageDownloadLink).into(profileImageView);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setOnClickListeners() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserPersonalDataActivity.this, ViewUserProfileActivity.class));
            }
        });

        editImageView.setOnClickListener(new View.OnClickListener() {
            boolean isPressed = false;
            @Override
            public void onClick(View view) {
                if (isPressed) {
                    nameEditText.setVisibility(View.VISIBLE);
                    emailEditText.setVisibility(View.VISIBLE);
                    locationEditText.setVisibility(View.VISIBLE);
                    phoneEditText.setVisibility(View.VISIBLE);
                    updateButton.setVisibility(View.VISIBLE);

                } else {
                    nameEditText.setVisibility(View.GONE);
                    emailEditText.setVisibility(View.GONE);
                    locationEditText.setVisibility(View.GONE);
                    phoneEditText.setVisibility(View.GONE);
                    updateButton.setVisibility(View.GONE);
                }
                isPressed = !isPressed;
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, email, location, phoneNumber;

                name = nameEditText.getText().toString().trim();
                email = emailEditText.getText().toString().trim();
                location = locationEditText.getText().toString().trim();
                phoneNumber = phoneEditText.getText().toString().trim();

                // Defining the child of storageReference
                StorageReference ref = userProfileImageStorage
                        .child("user")
                        .child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                        .child("images/" + name + "_" + loggedUserId);

                if (gallerySelectedImageUri != null) {
                    // Code for showing progress dialog while uploading
                    ProgressDialog progressDialog = new ProgressDialog(UserPersonalDataActivity.this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    // Adding listeners on upload or failure of image
                    UploadTask uploadTask = ref.putFile(gallerySelectedImageUri);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            progressDialog.dismiss();
                                            Toast.makeText(UserPersonalDataActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                            String fileLink = task.getResult().toString();
                                            usersReference.child(loggedUserId).child("name").setValue(name);
                                            usersReference.child(loggedUserId).child("email").setValue(email);
                                            usersReference.child(loggedUserId).child("location").setValue(location);
                                            usersReference.child(loggedUserId).child("phoneNumber").setValue(phoneNumber);
                                            usersReference.child(loggedUserId).child("profileImageDownloadLink").setValue(fileLink);
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UserPersonalDataActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });
                }

                if (cameraCapturedImageUri != null) {
                    // Code for showing progress dialog while uploading
                    ProgressDialog progressDialog = new ProgressDialog(UserPersonalDataActivity.this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    // Adding listeners on upload or failure of image
                    UploadTask uploadTask = ref.putFile(cameraCapturedImageUri);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            progressDialog.dismiss();
                                            Toast.makeText(UserPersonalDataActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                            String fileLink = task.getResult().toString();
                                            usersReference.child(loggedUserId).child("name").setValue(name);
                                            usersReference.child(loggedUserId).child("email").setValue(email);
                                            usersReference.child(loggedUserId).child("location").setValue(location);
                                            usersReference.child(loggedUserId).child("phoneNumber").setValue(phoneNumber);
                                            usersReference.child(loggedUserId).child("profileImageDownloadLink").setValue(fileLink);
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UserPersonalDataActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });
                }
            }
        });

        galleryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                    }
                }
                Intent galleryPhotoPickerIntent = new Intent(Intent.ACTION_PICK);
                galleryPhotoPickerIntent.setType("image/*");
                galleryActivityResultLauncher.launch(galleryPhotoPickerIntent);
            }
        });

        cameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
            }
        });
    }

}