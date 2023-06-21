package com.pet.shelter.friends.pets.lost;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
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

public class AddLostPetActivity extends AppCompatActivity implements TextWatcher, ErrorSetter {

    private ShapeableImageView petImage;
    private TextInputLayout typeTextInputLayout, nameTextInputLayout, breedTextInputLayout,
            ageTextInputLayout, sexTextInputLayout, sizeTextInputLayout, colorTextInputLayout,
            descriptionTextInputLayout;
    private TextInputEditText nameTextInputEditText, breedTextInputEditText, descriptionTextInputEditText;
    private Uri gallerySelectedImageUri, cameraCapturedImageUri;
    private Bitmap cameraCapturedImageBitmap;

    private static final int PICK_FROM_GALLERY = 1889;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private String loggedUserId;
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher, cameraActivityResultLauncher;
    private String selectedType, selectedSize, selectedSex, selectedAge, spayedOrNeutered,
            dewormed, vaccines, fitForChildren, fitForGuarding, friendlyWithPets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lost_pet);

        MaterialToolbar materialToolbar = findViewById(R.id.addLostPet_materialToolbar);
        MaterialButton addPetMaterialButton = findViewById(R.id.addLostPet_materialButton);
        petImage = findViewById(R.id.addLostPet_shapeImageView);
        typeTextInputLayout = findViewById(R.id.addLostPetType_textInputLayout);
        nameTextInputLayout = findViewById(R.id.addLostPetName_textInputLayout);
        breedTextInputLayout = findViewById(R.id.addLostPetBreed_textInputLayout);
        ageTextInputLayout = findViewById(R.id.addLostPetAge_textInputLayout);
        sexTextInputLayout = findViewById(R.id.addLostPetSex_textInputLayout);
        sizeTextInputLayout = findViewById(R.id.addLostPetSize_textInputLayout);
        colorTextInputLayout = findViewById(R.id.addLostPetColor_textInputLayout);
        descriptionTextInputLayout = findViewById(R.id.addLostPetDescription_textInputLayout);
        MaterialAutoCompleteTextView typeMaterialAutoCompleteTextView = findViewById(R.id.addLostPetTypes_materialAutoCompleteTextView);
        MaterialAutoCompleteTextView sizeMaterialAutoCompleteTextView = findViewById(R.id.addLostPetSize_materialAutoCompleteTextView);
        MaterialAutoCompleteTextView sexMaterialAutoCompleteTextView = findViewById(R.id.addLostPetSex_materialAutoCompleteTextView);
        MaterialAutoCompleteTextView ageMaterialAutoCompleteTextView = findViewById(R.id.addLostPetAge_materialAutoCompleteTextView);
        nameTextInputEditText = findViewById(R.id.addLostPetName_textInputEditText);
        breedTextInputEditText = findViewById(R.id.addLostPetBreed_textInputEditText);
        descriptionTextInputEditText = findViewById(R.id.addLostPetDescription_textInputEditText);
        MaterialCheckBox spayedOrNeuteredMaterialCheckBox = findViewById(R.id.addLostPetSpayedOrNeutered_materialCheckBox);
        MaterialCheckBox dewormedMaterialCheckBox = findViewById(R.id.addLostPetDewormed_materialCheckBox);
        MaterialCheckBox vaccinesMaterialCheckBox = findViewById(R.id.addLostPetVaccines_materialCheckBox);
        MaterialCheckBox fitForChildrenMaterialCheckBox = findViewById(R.id.addLostPetFitForChildren_materialCheckBox);
        MaterialCheckBox fitForGuardingMaterialCheckBox = findViewById(R.id.addLostPetFitForGuarding_materialCheckBox);
        MaterialCheckBox friendlyWithPetsMaterialCheckBox = findViewById(R.id.addLostPetFriendlyWithOtherPets_materialCheckBox);

        StorageReference petsImages = FirebaseStorage.getInstance().getReference("petsImages");
        DatabaseReference petsReference = FirebaseDatabase.getInstance().getReference("pets");
        loggedUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        String[] petsType = {"Dog", "Cat"};
        String[] petsSize = {"XSmall", "Small", "Medium", "Large", "XLarge"};
        String[] petsSex = {"Male", "Female"};
        String[] petsAge = {"Puppy", "Kitten", "Junior", "Adult", "Mature", "Senior", "Old"};

        ArrayAdapter<String> petsTypeAdapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                petsType
        );
        ArrayAdapter<String> petsSizeAdapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                petsSize
        );
        ArrayAdapter<String> petsSexAdapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                petsSex
        );
        ArrayAdapter<String> petsAgeAdapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                petsAge
        );

        typeMaterialAutoCompleteTextView.setAdapter(petsTypeAdapter);
        sizeMaterialAutoCompleteTextView.setAdapter(petsSizeAdapter);
        ageMaterialAutoCompleteTextView.setAdapter(petsAgeAdapter);
        sexMaterialAutoCompleteTextView.setAdapter(petsSexAdapter);

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

        spayedOrNeuteredMaterialCheckBox.addOnCheckedStateChangedListener(new MaterialCheckBox.OnCheckedStateChangedListener() {
            @Override
            public void onCheckedStateChangedListener(@NonNull MaterialCheckBox checkBox, int state) {
                if (checkBox.isChecked()) {
                    spayedOrNeutered = "yes";
                } else {
                    spayedOrNeutered = "no";
                }
            }
        });

        dewormedMaterialCheckBox.addOnCheckedStateChangedListener(new MaterialCheckBox.OnCheckedStateChangedListener() {
            @Override
            public void onCheckedStateChangedListener(@NonNull MaterialCheckBox checkBox, int state) {
                if (checkBox.isChecked()) {
                    dewormed = "yes";
                } else {
                    dewormed = "no";
                }
            }
        });

        vaccinesMaterialCheckBox.addOnCheckedStateChangedListener(new MaterialCheckBox.OnCheckedStateChangedListener() {
            @Override
            public void onCheckedStateChangedListener(@NonNull MaterialCheckBox checkBox, int state) {
                if (checkBox.isChecked()) {
                    vaccines = "yes";
                } else {
                    vaccines = "no";
                }
            }
        });

        fitForChildrenMaterialCheckBox.addOnCheckedStateChangedListener(new MaterialCheckBox.OnCheckedStateChangedListener() {
            @Override
            public void onCheckedStateChangedListener(@NonNull MaterialCheckBox checkBox, int state) {
                if (checkBox.isChecked()) {
                    fitForChildren = "yes";
                } else {
                    fitForChildren = "no";
                }
            }
        });

        fitForGuardingMaterialCheckBox.addOnCheckedStateChangedListener(new MaterialCheckBox.OnCheckedStateChangedListener() {
            @Override
            public void onCheckedStateChangedListener(@NonNull MaterialCheckBox checkBox, int state) {
                if (checkBox.isChecked()) {
                    fitForGuarding = "yes";
                } else {
                    fitForGuarding = "no";
                }
            }
        });

        friendlyWithPetsMaterialCheckBox.addOnCheckedStateChangedListener(new MaterialCheckBox.OnCheckedStateChangedListener() {
            @Override
            public void onCheckedStateChangedListener(@NonNull MaterialCheckBox checkBox, int state) {
                if (checkBox.isChecked()) {
                    friendlyWithPets = "yes";
                } else {
                    friendlyWithPets = "no";
                }
            }
        });

        typeMaterialAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedType = String.valueOf(parent.getItemAtPosition(position));
            }
        });

        sizeMaterialAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedSize = String.valueOf(parent.getItemAtPosition(position));
            }
        });

        sexMaterialAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedSex = String.valueOf(parent.getItemAtPosition(position));
            }
        });

        ageMaterialAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedAge = String.valueOf(parent.getItemAtPosition(position));
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
                            Toast.makeText(AddLostPetActivity.this, "There is no app that supports this action", Toast.LENGTH_SHORT).show();
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

                String petName = Objects.requireNonNull(nameTextInputEditText.getText()).toString();
                String petDescription = Objects.requireNonNull(descriptionTextInputEditText.getText()).toString();
                String petBreed = Objects.requireNonNull(breedTextInputEditText.getText()).toString();
                String petToSave = petName + "_" + petDescription;

                ValidationManager.getInstance().doValidation(AddLostPetActivity.this,
                        nameTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(AddLostPetActivity.this,
                        descriptionTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(AddLostPetActivity.this,
                        breedTextInputLayout).checkEmpty();

                if (ValidationManager.getInstance().isNothingEmpty()) {
                    StorageReference ref = petsImages
                            .child("lostPets")
                            .child(loggedUserId)
                            .child(petName + "_" + petDescription);

                    if (gallerySelectedImageUri != null) {
                        // Adding listeners on upload or failure of image
                        UploadTask uploadTask = ref.putFile(gallerySelectedImageUri);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                Toast.makeText(AddLostPetActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                                String fileLink = task.getResult().toString();

                                                petsReference.child("Lost").child(petToSave).child("petType").setValue(selectedType);
                                                petsReference.child("Lost").child(petToSave).child("shelterAdministratorId").setValue(loggedUserId);
                                                petsReference.child("Lost").child(petToSave).child("petImage1DownloadLink").setValue(fileLink);
                                                petsReference.child("Lost").child(petToSave).child("petName").setValue(petName);
                                                petsReference.child("Lost").child(petToSave).child("petType").setValue(selectedType);
                                                petsReference.child("Lost").child(petToSave).child("petBreed").setValue(petBreed);
                                                petsReference.child("Lost").child(petToSave).child("petAge").setValue(selectedAge);
                                                petsReference.child("Lost").child(petToSave).child("petSize").setValue(selectedSize);
                                                petsReference.child("Lost").child(petToSave).child("petSex").setValue(selectedSex);
                                                petsReference.child("Lost").child(petToSave).child("petDescription").setValue(petDescription);
                                                petsReference.child("Lost").child(petToSave).child("spayedOrNeutered").setValue(spayedOrNeutered);
                                                petsReference.child("Lost").child(petToSave).child("dewormed").setValue(dewormed);
                                                petsReference.child("Lost").child(petToSave).child("vaccines").setValue(vaccines);
                                                petsReference.child("Lost").child(petToSave).child("fitForChildren").setValue(fitForChildren);
                                                petsReference.child("Lost").child(petToSave).child("fitForGuarding").setValue(fitForGuarding);
                                                petsReference.child("Lost").child(petToSave).child("friendlyWithPets").setValue(friendlyWithPets);
                                            }
                                        });
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddLostPetActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                                Toast.makeText(AddLostPetActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                                String fileLink = task.getResult().toString();

                                                petsReference.child("Lost").child(petToSave).child("petType").setValue(selectedType);
                                                petsReference.child("Lost").child(petToSave).child("petImage1DownloadLink").setValue(fileLink);
                                                petsReference.child("Lost").child(petToSave).child("petName").setValue(petName);
                                                petsReference.child("Lost").child(petToSave).child("petType").setValue(selectedType);
                                                petsReference.child("Lost").child(petToSave).child("petBreed").setValue(petBreed);
                                                petsReference.child("Lost").child(petToSave).child("petAge").setValue(selectedAge);
                                                petsReference.child("Lost").child(petToSave).child("petSize").setValue(selectedSize);
                                                petsReference.child("Lost").child(petToSave).child("petSex").setValue(selectedSex);
                                                petsReference.child("Lost").child(petToSave).child("petDescription").setValue(petDescription);
                                                petsReference.child("Lost").child(petToSave).child("spayedOrNeutered").setValue(spayedOrNeutered);
                                                petsReference.child("Lost").child(petToSave).child("dewormed").setValue(dewormed);
                                                petsReference.child("Lost").child(petToSave).child("vaccines").setValue(vaccines);
                                                petsReference.child("Lost").child(petToSave).child("fitForChildren").setValue(fitForChildren);
                                                petsReference.child("Lost").child(petToSave).child("fitForGuarding").setValue(fitForGuarding);
                                                petsReference.child("Lost").child(petToSave).child("friendlyWithPets").setValue(friendlyWithPets);
                                            }
                                        });
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddLostPetActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(AddLostPetActivity.this, "Please add pet image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        setTextWatcher();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void setTextWatcher() {
        Objects.requireNonNull(nameTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(descriptionTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(typeTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(breedTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(ageTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(sexTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(sizeTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(colorTextInputLayout.getEditText()).addTextChangedListener(this);

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
        } else if (s.hashCode() == Objects.requireNonNull(descriptionTextInputLayout.getEditText()).getText().hashCode()) {
            descriptionTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(breedTextInputLayout.getEditText()).getText().hashCode()) {
            breedTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(typeTextInputLayout.getEditText()).getText().hashCode()) {
            typeTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(ageTextInputLayout.getEditText()).getText().hashCode()) {
            ageTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(sexTextInputLayout.getEditText()).getText().hashCode()) {
            sexTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(sizeTextInputLayout.getEditText()).getText().hashCode()) {
            sizeTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(colorTextInputLayout.getEditText()).getText().hashCode()) {
            colorTextInputLayout.setErrorEnabled(false);
        }
    }

    @Override
    public void setError(TextInputLayout textInputLayout, String errorMessage) {
        textInputLayout.setError(errorMessage);
    }
}