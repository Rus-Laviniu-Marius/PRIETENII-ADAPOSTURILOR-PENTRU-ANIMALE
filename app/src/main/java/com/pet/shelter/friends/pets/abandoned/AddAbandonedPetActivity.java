package com.pet.shelter.friends.pets.abandoned;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
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
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.ValidationManager;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class AddAbandonedPetActivity extends AppCompatActivity implements TextWatcher, ErrorSetter {


    private ShapeableImageView petImage;
    private TextInputLayout typeTextInputLayout, descriptionTextInputLayout, placeDescriptionTextInputLayout,
            latitudeTextInputLayout, longitudeTextInputLayout;
    private TextInputEditText placeDescriptionTextInputEditText, descriptionTextInputEditText,
            latitudeTextInputEditText, longitudeTextInputEditText;
    private Uri gallerySelectedImageUri, cameraCapturedImageUri;
    private Bitmap cameraCapturedImageBitmap;

    private static final int PICK_FROM_GALLERY = 1889;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int PICK_MAP_POINT_REQUEST = 999;  // The request code
    private String loggedUserId;
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher, cameraActivityResultLauncher, mapActivityResultLauncher;
    private String selectedType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_abandoned_pet);

        MaterialToolbar materialToolbar = findViewById(R.id.addAbandonedPet_materialToolbar);
        MaterialButton addPetMaterialButton = findViewById(R.id.addAbandonedPet_materialButton);
        petImage = findViewById(R.id.addAbandonedPet_shapeImageView);
        typeTextInputLayout = findViewById(R.id.addAbandonedPetType_textInputLayout);
        placeDescriptionTextInputLayout = findViewById(R.id.addAbandonedPetPlaceDescription_textInputLayout);
        descriptionTextInputLayout = findViewById(R.id.addAbandonedPetDescription_textInputLayout);
        latitudeTextInputLayout = findViewById(R.id.addAbandonedPetLatitude_textInputLayout);
        longitudeTextInputLayout = findViewById(R.id.addAbandonedPetLongitude_textInputLayout);
        descriptionTextInputEditText = findViewById(R.id.addAbandonedPetDescription_textInputEditText);
        placeDescriptionTextInputEditText = findViewById(R.id.addAbandonedPetPlaceDescription_textInputEditText);
        latitudeTextInputEditText = findViewById(R.id.addAbandonedPetLatitude_textInputEditText);
        longitudeTextInputEditText = findViewById(R.id.addAbandonedPetLongitude_textInputEditText);
        MaterialButton selectPetLocationMaterialButton = findViewById(R.id.addAbandonedPetSelectLocation_materialButton);
        MaterialAutoCompleteTextView typeMaterialAutoCompleteTextView = findViewById(R.id.addAbandonedPetTypes_materialAutoCompleteTextView);

        StorageReference petsImages = FirebaseStorage.getInstance().getReference("petsImages");
        DatabaseReference petsReference = FirebaseDatabase.getInstance().getReference("pets");
        loggedUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        String[] petsType = {"Dog", "Cat"};

        ArrayAdapter<String> petsTypeAdapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                petsType
        );

        typeMaterialAutoCompleteTextView.setAdapter(petsTypeAdapter);

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
                        petImage.setImageURI(gallerySelectedImageUri);// To display selected image in image view
                    }
                });

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Bundle bundle = result.getData().getExtras();
                        cameraCapturedImageBitmap = (Bitmap) bundle.get("data");
                        petImage.setImageBitmap(cameraCapturedImageBitmap);
                        cameraCapturedImageUri = getImageUri(this, cameraCapturedImageBitmap);
                    }
                });

        typeMaterialAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedType = String.valueOf(parent.getItemAtPosition(position));
            }
        });

        selectPetLocationMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPointOnMap();
            }
        });

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_open_camera) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)  {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    } else {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                            cameraActivityResultLauncher.launch(cameraIntent);
                        } else {
                            Toast.makeText(AddAbandonedPetActivity.this, "There is no app that supports this action", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (item.getItemId() == R.id.action_open_gallery) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                    }
                    Intent galleryPhotoPickerIntent = new Intent(Intent.ACTION_PICK);
                    galleryPhotoPickerIntent.setType("image/*");
                    galleryActivityResultLauncher.launch(galleryPhotoPickerIntent);
                }
                return true;
            }
        });

        addPetMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String petLocationLatitude = Objects.requireNonNull(latitudeTextInputEditText.getText()).toString().trim();
                String petLocationLongitude = Objects.requireNonNull(longitudeTextInputEditText.getText()).toString().trim();
                String placeDescription = Objects.requireNonNull(placeDescriptionTextInputEditText.getText()).toString().trim();
                String petDescription = Objects.requireNonNull(descriptionTextInputEditText.getText()).toString().trim();
                String petToSave = petDescription + "_" + placeDescription;

                ValidationManager.getInstance().doValidation(AddAbandonedPetActivity.this,
                        placeDescriptionTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(AddAbandonedPetActivity.this,
                        descriptionTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(AddAbandonedPetActivity.this,
                        latitudeTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(AddAbandonedPetActivity.this,
                        latitudeTextInputLayout).checkEmpty();

                if (ValidationManager.getInstance().isNothingEmpty()) {
                    StorageReference ref = petsImages
                            .child("newsArticlesMediaFiles")
                            .child(loggedUserId)
                            .child(petDescription + "_" + placeDescription);

                    if (gallerySelectedImageUri != null) {
                        // Adding listeners on upload or failure of image
                        UploadTask uploadTask = ref.putFile(gallerySelectedImageUri);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                Toast.makeText(AddAbandonedPetActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                                String fileLink = task.getResult().toString();

                                                petsReference.child("Abandoned").child(petToSave).child("petType").setValue(selectedType);
                                                petsReference.child("Abandoned").child(petToSave).child("petImage1DownloadLink").setValue(fileLink);
                                                petsReference.child("Abandoned").child(petToSave).child("petDescription").setValue(petDescription);
                                                petsReference.child("Abandoned").child(petToSave).child("placeDescription").setValue(placeDescription);
                                                petsReference.child("Abandoned").child(petToSave).child("petType").setValue(selectedType);
                                                petsReference.child("Abandoned").child(petToSave).child("petLocationLatitude").setValue(petLocationLatitude);
                                                petsReference.child("Abandoned").child(petToSave).child("petLocationLongitude").setValue(petLocationLongitude);
                                            }
                                        });
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddAbandonedPetActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                                Toast.makeText(AddAbandonedPetActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                                String fileLink = task.getResult().toString();

                                                petsReference.child("Abandoned").child(petToSave).child("petType").setValue(selectedType);
                                                petsReference.child("Abandoned").child(petToSave).child("petImage1DownloadLink").setValue(fileLink);
                                                petsReference.child("Abandoned").child(petToSave).child("petDescription").setValue(petDescription);
                                                petsReference.child("Abandoned").child(petToSave).child("placeDescription").setValue(placeDescription);
                                                petsReference.child("Abandoned").child(petToSave).child("petLocationLatitude").setValue(petLocationLatitude);
                                                petsReference.child("Abandoned").child(petToSave).child("petLocationLongitude").setValue(petLocationLongitude);
                                            }
                                        });
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddAbandonedPetActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(AddAbandonedPetActivity.this, "Please add pet image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        setTextWatcher();
    }
    private void pickPointOnMap() {
        Intent pickPointIntent = new Intent(this, AbandonedPetSelectLocationMapActivity.class);
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)  {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PICK_MAP_POINT_REQUEST);
        } else {
            mapActivityResultLauncher.launch(pickPointIntent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void setTextWatcher() {
        Objects.requireNonNull(placeDescriptionTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(descriptionTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(typeTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(latitudeTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(longitudeTextInputLayout.getEditText()).addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.hashCode() == Objects.requireNonNull(placeDescriptionTextInputLayout.getEditText()).getText().hashCode()) {
            placeDescriptionTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(descriptionTextInputLayout.getEditText()).getText().hashCode()) {
            descriptionTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(latitudeTextInputLayout.getEditText()).getText().hashCode()) {
            latitudeTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(longitudeTextInputLayout.getEditText()).getText().hashCode()) {
            longitudeTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(typeTextInputLayout.getEditText()).getText().hashCode()) {
            typeTextInputLayout.setErrorEnabled(false);
        }
    }

    @Override
    public void setError(TextInputLayout textInputLayout, String errorMessage) {
        textInputLayout.setError(errorMessage);
    }
}