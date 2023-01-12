package com.pet.shelter.friends.adoption.profile;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pet.shelter.friends.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class CreateProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference userProfileImageStorage;

    private ImageView profileImageView, galleryImageView, cameraImageView;
    private TextView nameTextView, emailTextView, locationTextView, phoneNumberTextView;
    private EditText nameEditText, emailEditText, locationEditText, phoneNumberEditText;
    private Button createButton;

    private Uri gallerySelectedImageUri, cameraCapturedImageUri;
    private Bitmap cameraCapturedImageBitmap;

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher, cameraActivityResultLauncher;

    private String loggedUserId;
    private static final int CAMERA_REQUEST = 1888;
    private static final int PICK_FROM_GALLERY = 1889;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = firebaseDatabase.getReference("users");
        firebaseStorage = FirebaseStorage.getInstance();
        userProfileImageStorage = firebaseStorage.getReference();

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

        profileImageView = findViewById(R.id.createProfileUser_imageView);
        galleryImageView = findViewById(R.id.createProfileOpenGallery_imageView);
        cameraImageView = findViewById(R.id.createProfileOpenCamera_imageView);

        nameTextView = findViewById(R.id.createProfileUserName_textView);
        emailTextView = findViewById(R.id.createProfileUserEmail_textView);
        locationTextView = findViewById(R.id.createProfileUserLocation_textView);
        phoneNumberTextView = findViewById(R.id.createProfileUserPhoneNumber_textView);

        nameEditText = findViewById(R.id.createProfileUserName_editText);
        emailEditText = findViewById(R.id.createProfileUserEmail_editText);
        locationEditText = findViewById(R.id.createProfileUserLocation_editText);
        phoneNumberEditText = findViewById(R.id.createProfileUserPhoneNumber_editText);

        createButton = findViewById(R.id.createProfileCreate_button);

        setOnFocusListeners();

        setOnClickListeners();

    }

    private void setOnFocusListeners() {
        nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    nameTextView.setVisibility(View.VISIBLE);
                    nameEditText.setHint("");
                }
            }
        });
        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    emailTextView.setVisibility(View.VISIBLE);
                    emailEditText.setHint("");
                }
            }
        });
        locationEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    locationTextView.setVisibility(View.VISIBLE);
                    locationEditText.setHint("");
                }
            }
        });
        phoneNumberEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    phoneNumberTextView.setVisibility(View.VISIBLE);
                    phoneNumberEditText.setHint("");
                }
            }
        });
    }

    private void setOnClickListeners() {
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, email, location, phoneNumber;

                name = nameEditText.getText().toString().trim();
                email = emailEditText.getText().toString().trim();
                location = locationEditText.getText().toString().trim();
                phoneNumber = phoneNumberEditText.getText().toString().trim();

                // Defining the child of storageReference
                StorageReference ref = userProfileImageStorage
                        .child("user")
                        .child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                        .child("images/" + name + "_" + loggedUserId);

                if (gallerySelectedImageUri != null) {
                    // Code for showing progress dialog while uploading
                    ProgressDialog progressDialog = new ProgressDialog(CreateProfileActivity.this);
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
                                            Toast.makeText(CreateProfileActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(CreateProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    ProgressDialog progressDialog = new ProgressDialog(CreateProfileActivity.this);
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
                                            Toast.makeText(CreateProfileActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(CreateProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(CreateProfileActivity.this, "There is no app that supports this action", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}