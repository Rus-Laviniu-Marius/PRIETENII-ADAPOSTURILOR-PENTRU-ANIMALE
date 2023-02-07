package com.pet.shelter.friends.adoption;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.pet.shelter.friends.adoption.model.Pet;
import com.pet.shelter.friends.adoption.profile.CreateProfileActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Objects;

public class AddNewPetActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private DatabaseReference petsReference;
    private Spinner cardBackgroundColorSpinner, petTypeSpinner;
    private EditText nameEditText, ageEditText, weightEditText, locationEditText, sexEditText,
            breedEditText, descriptionEditText, sizeEditText;
    private TextView nameTextView, ageTextView, weightTextView, locationTextView, sexTextView,
            breedTextView, descriptionTextView, sizeTextView;
    private ImageView petImageView, backImageView, cameraImageView;
    private Button cancelButton, addButton;
    private RadioButton fitForGuardingRadioButton, fitForChildrenRadioButton;
    private RadioGroup fitForGuardingRadioGroup;

    private Uri gallerySelectedImageUri, cameraCapturedImageUri;
    private Bitmap cameraCapturedImageBitmap;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher, cameraActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_pet);

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

                        petImageView.setImageURI(gallerySelectedImageUri);// To display selected image in image view
                    }
                });

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Bundle bundle = result.getData().getExtras();
                        cameraCapturedImageBitmap = (Bitmap) bundle.get("data");
//                        petImageView.setImageBitmap(cameraCapturedImageBitmap);
                        cameraCapturedImageUri = getImageUri(this, cameraCapturedImageBitmap);
                        petImageView.setImageURI(cameraCapturedImageUri);
                    }
                });

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        petsReference = firebaseDatabase.getReference("pets");
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        petImageView = findViewById(R.id.addNewPet_imageView);
        backImageView = findViewById(R.id.addNewPetBack_imageView);
        cameraImageView = findViewById(R.id.addNewPetCamera_imageView);

        petTypeSpinner = findViewById(R.id.addNewPetType_spinner);
        ArrayAdapter<CharSequence> petsAdapter = ArrayAdapter.createFromResource(this,
                R.array.pet_types_array,
                android.R.layout.simple_spinner_item);
        petsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petTypeSpinner.setAdapter(petsAdapter);

        nameEditText = findViewById(R.id.addNewPetName_editText);
        ageEditText = findViewById(R.id.addNewPetAge_editText);
        weightEditText = findViewById(R.id.addNewPetWeight_editText);
        locationEditText = findViewById(R.id.addNewPetLocation_editText);
        sexEditText = findViewById(R.id.addNewPetSex_editText);
        breedEditText = findViewById(R.id.addNewPetBreed_editText);
        sizeEditText = findViewById(R.id.addNewPetSize_editText);
        descriptionEditText = findViewById(R.id.addNewPetDescription_editText);

        nameTextView = findViewById(R.id.addNewPetName_textView);
        ageTextView = findViewById(R.id.addNewPetAge_textView);
        weightTextView = findViewById(R.id.addNewPetWeight_textView);
        locationTextView = findViewById(R.id.addNewPetLocation_textView);
        sexTextView = findViewById(R.id.addNewPetSex_textView);
        breedTextView = findViewById(R.id.addNewPetBreed_textView);
        sizeTextView = findViewById(R.id.addNewPetSize_textView);
        descriptionTextView = findViewById(R.id.addNewPetDescription_textView);

        addButton = findViewById(R.id.addNewPetAdd_button);
        cancelButton = findViewById(R.id.addNewPetCancel_button);

        fitForGuardingRadioGroup = findViewById(R.id.addNewPetFitForGuarding_radioGroup);

        fitForChildrenRadioButton = findViewById(R.id.addNewPetFitForChildren_radioButton);
        fitForGuardingRadioButton = findViewById(R.id.addNewPetFitForGuarding_radioButton);

        if (!petTypeSpinner.getSelectedItem().toString().equals("Dog")) {
            fitForGuardingRadioGroup.setVisibility(View.GONE);
        }

        cardBackgroundColorSpinner = findViewById(R.id.addNewPetCardBackgroundColor_spinner);
        ArrayAdapter<CharSequence> colorsAdapter = ArrayAdapter.createFromResource(this,
                R.array.card_background_colors_array,
                android.R.layout.simple_spinner_item);
        colorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cardBackgroundColorSpinner.setAdapter(colorsAdapter);

        cardBackgroundColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = AddNewPetActivity.this;
                Resources res = context.getResources();
                String packageName = context.getPackageName();

                String selectedItem = cardBackgroundColorSpinner.getSelectedItem().toString().toLowerCase(Locale.ROOT);

                selectedItem = selectedItem.replace(" ", "_");

                int colorId = res.getIdentifier(selectedItem, "color", packageName);
                int desiredColor = ContextCompat.getColor(context, colorId);
                cardBackgroundColorSpinner.setBackgroundColor(desiredColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
        ageEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    ageTextView.setVisibility(View.VISIBLE);
                    ageEditText.setHint("");
                }
            }
        });
        weightEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    weightTextView.setVisibility(View.VISIBLE);
                    weightEditText.setHint("");
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
        sexEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    sexTextView.setVisibility(View.VISIBLE);
                    sexEditText.setHint("");
                }
            }
        });
        breedEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    breedTextView.setVisibility(View.VISIBLE);
                    breedEditText.setHint("");
                }
            }
        });
        sizeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    sizeTextView.setVisibility(View.VISIBLE);
                    sizeEditText.setHint("");
                }
            }
        });
        descriptionEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    descriptionTextView.setVisibility(View.VISIBLE);
                    descriptionEditText.setHint("");
                }
            }
        });

    }

    private void setOnClickListeners() {
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddNewPetActivity.this, SeeListOfPetsActivity.class));
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    savePetDataToDatabase();

                    sendToSeeListOfPetsActivity();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToSeeListOfPetsActivity();
            }
        });

        petImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                galleryActivityResultLauncher.launch(photoPickerIntent);
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
                            Toast.makeText(AddNewPetActivity.this, "There is no app that supports this action", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }

    private void savePetDataToDatabase() {
        String petType = petTypeSpinner.getSelectedItem().toString().trim();
        String petName = nameEditText.getText().toString().trim();
        String petAge = ageEditText.getText().toString().trim();
        String petWeight = weightEditText.getText().toString().trim();
        String petLocation = locationEditText.getText().toString().trim();
        String petSize = sizeEditText.getText().toString().trim();
        String petBreed = breedEditText.getText().toString().trim();
        String petSex = sexEditText.getText().toString().trim();
        String petDescription = descriptionEditText.getText().toString().trim();
        String petCardBackgroundColor = cardBackgroundColorSpinner.getSelectedItem().toString().toLowerCase(Locale.ROOT).trim().replace(" ", "_");
        boolean fitForChildren = fitForChildrenRadioButton.isChecked();
        boolean fitForGuarding = false;
        if (fitForGuardingRadioGroup.getVisibility() == View.VISIBLE)
            fitForGuarding = fitForGuardingRadioButton.isChecked();

        if (petName.isEmpty()) {
            nameEditText.setError("You need to enter a name");
            nameEditText.requestFocus();
        }
        if (petAge.isEmpty()) {
            ageEditText.setError("You need to enter an age");
            ageEditText.requestFocus();
        }
        if (petWeight.isEmpty()) {
            weightEditText.setError("You need to enter a weight");
            weightEditText.requestFocus();
        }
        if (petLocation.isEmpty()) {
            locationEditText.setError("You need to enter a location");
            locationEditText.requestFocus();
        }
        if (petSize.isEmpty()) {
            sizeEditText.setError("You need to enter a pet size");
            sizeEditText.requestFocus();
        }
        if (petBreed.isEmpty()) {
            breedEditText.setError("You need to enter a breed");
            breedEditText.requestFocus();
        }
        if (petSex.isEmpty()) {
            sexEditText.setError("You need to enter a sex");
            sexEditText.requestFocus();
        }
        if (petDescription.isEmpty()) {
            descriptionEditText.setError("You need to enter a description");
            descriptionEditText.requestFocus();
        }

        // Defining the child of storageReference
        StorageReference ref = storageReference
                .child("pet")
                .child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                .child("images/" + petName + "_" + petType);

        if (gallerySelectedImageUri != null) {
            // Code for showing progress dialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Adding listeners on upload or failure of image
            UploadTask uploadTask = ref.putFile(gallerySelectedImageUri);
            boolean finalFitForGuarding = fitForGuarding;
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddNewPetActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                    String fileLink = task.getResult().toString();
                                    petsReference.push().setValue(new Pet(
                                            petCardBackgroundColor,
                                            fileLink,
                                            petName,
                                            Integer.parseInt(petAge),
                                            Double.parseDouble(petWeight),
                                            petLocation,
                                            petSize,
                                            petBreed,
                                            petSex,
                                            petDescription,
                                            "false",
                                            "false",
                                            petType,
                                            fitForChildren,
                                            finalFitForGuarding));
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddNewPetActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void sendToSeeListOfPetsActivity() {
        Intent intent = new Intent(AddNewPetActivity.this, SeeListOfPetsActivity.class);
        startActivity(intent);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
