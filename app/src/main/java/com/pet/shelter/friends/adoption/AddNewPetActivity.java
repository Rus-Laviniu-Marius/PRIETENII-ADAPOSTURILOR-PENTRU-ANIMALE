package com.pet.shelter.friends.adoption;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pet.shelter.friends.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class AddNewPetActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference petReference;
    private Spinner cardBackgroundColorSpinner;
    private EditText nameEditText, ageEditText, weightEditText, locationEditText, sexEditText,
        breedEditText, descriptionEditText, sizeEditText;
    private ImageView petImageView;
    private Button cancelButton, addButton;

    private ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    // doSomeOperations();
                    Intent data = result.getData();
                    Uri selectedImage = Objects.requireNonNull(data).getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    BitmapFactory.decodeStream(imageStream);

                    petImageView.setImageURI(selectedImage);// To display selected image in image view
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_pet);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        petReference = firebaseDatabase.getReference("pets");

        nameEditText = findViewById(R.id.addNewPetName_editText);
        ageEditText = findViewById(R.id.addNewPetAge_editText);
        weightEditText = findViewById(R.id.addNewPetWeight_editText);
        locationEditText = findViewById(R.id.addNewPetLocation_editText);
        sexEditText = findViewById(R.id.addNewPetSex_editText);
        breedEditText = findViewById(R.id.addNewPetBreed_editText);
        sizeEditText = findViewById(R.id.addNewPetSize_editText);
        descriptionEditText = findViewById(R.id.addNewPetDescription_editText);

        petImageView = findViewById(R.id.addNewPet_imageView);

        addButton = findViewById(R.id.addNewPetAdd_button);
        cancelButton = findViewById(R.id.addNewPetCancel_button);

        cardBackgroundColorSpinner = findViewById(R.id.addNewPetCardBackgroundColor_spinner);
        ArrayAdapter<CharSequence> colorsAdapter = ArrayAdapter.createFromResource(this,
                R.array.card_background_colors_array,
                android.R.layout.simple_spinner_item);
        colorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cardBackgroundColorSpinner.setAdapter(colorsAdapter);


        setOnClickListeners();

    }

    private void setOnClickListeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                someActivityResultLauncher.launch(photoPickerIntent);
            }
        });
    }

    private void sendToSeeListOfPetsActivity() {
        Intent intent = new Intent(AddNewPetActivity.this, SeeListOfPetsActivity.class);
        startActivity(intent);
    }

}
