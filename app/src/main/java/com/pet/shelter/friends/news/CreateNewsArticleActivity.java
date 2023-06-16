package com.pet.shelter.friends.news;

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

import com.google.android.gms.auth.api.signin.internal.Storage;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pet.shelter.friends.ErrorSetter;
import com.pet.shelter.friends.HomeActivity;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.ValidationManager;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Locale;
import java.util.Objects;

public class CreateNewsArticleActivity extends AppCompatActivity implements TextWatcher, ErrorSetter {

    private TextInputLayout titleTextInputLayout, descriptionTextInputLayout;
    private TextInputEditText titleTextInputEditText, descriptionTextInputEditText;

    private DatabaseReference newsArticlesReference;
    private StorageReference newsArticlesMediaContentReference;

    private ShapeableImageView articleMediaImage;

    private Uri gallerySelectedImageUri, cameraCapturedImageUri;
    private Bitmap cameraCapturedImageBitmap;

    private static final int PICK_FROM_GALLERY = 1889;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private String newsCategorySelectedItem, newsSubcategorySelectedItem;

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher, cameraActivityResultLauncher;

    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_news_article);

        MaterialToolbar materialToolbar = findViewById(R.id.createNewsArticle_materialToolbar);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        newsArticlesReference = firebaseDatabase.getReference("newsArticles");
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        newsArticlesMediaContentReference = firebaseStorage.getReference();

        articleMediaImage = findViewById(R.id.createNewsArticle_shapeImageView);

        String loggedUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        MaterialAutoCompleteTextView newsCategory = findViewById(R.id.outlinedExposedDropdownNewsCategories_materialAutoCompleteTextView);
        MaterialAutoCompleteTextView newsSubcategory = findViewById(R.id.outlinedExposedDropdownNewsSubCategories_materialAutoCompleteTextView);
        titleTextInputLayout = findViewById(R.id.createNewsArticleNewsTitle_textInputLayout);
        descriptionTextInputLayout = findViewById(R.id.createNewsArticleNewsDescription_textInputLayout);
        titleTextInputEditText = findViewById(R.id.createNewsArticleNewsTitle_textInputEditText);
        descriptionTextInputEditText = findViewById(R.id.createNewsArticleNewsDescription_textInputEditText);

        MaterialButton postNews = findViewById(R.id.createNewsArticlePost_materialButton);

        // TODO: What are the news categories subcategories?
        String[] newsCategories = {"Pets", "Volunteer", "Material support", "Donation", "Caring"};
        String[] newsSubcategories = {"Adoption", "Lost", "Abandoned", "Sponsored", "Shelter",
                "Food", "Medicine", "Food", "Medicine", "Treatment", "Grooming", "Feeding", "Bathing"};

        String[] petsSubCategories = {"Adoption", "Lost", "Abandoned"};
        String[] volunteerSubCategories = {"Sponsored", "Shelter"};
        String[] materialSupportSubCategories = {"Food", "Medicine"};
        String[] donationSubCategories = {"Food", "Medicine", "Treatment"};
        String[] petCaringSubCategories = {"Grooming", "Feeding", "Bathing"};

        ArrayAdapter<String> newsCategoriesAdapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                newsCategories
        );

        ArrayAdapter<String> newsSubcategoriesAdapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                newsSubcategories
        );

        newsSubcategory.setAdapter(newsSubcategoriesAdapter);
        newsCategory.setAdapter(newsCategoriesAdapter);

        // TODO: Update newsSubcategory content by newsCategory selected item
        ArrayAdapter<String> petsSubCategoriesAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.drop_down_item,
                petsSubCategories
        );

        ArrayAdapter<String> volunteerSubCategoriesAdapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                volunteerSubCategories
        );

        ArrayAdapter<String> materialSupportSubCategoriesAdapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                materialSupportSubCategories
        );

        ArrayAdapter<String> donationSubCategoriesAdapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                donationSubCategories
        );

        ArrayAdapter<String> petCaringSubCategoriesAdapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                petCaringSubCategories
        );

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
                        articleMediaImage.setImageURI(gallerySelectedImageUri);// To display selected image in image view
                    }
                });

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Bundle bundle = result.getData().getExtras();
                        cameraCapturedImageBitmap = (Bitmap) bundle.get("data");
                        articleMediaImage.setImageBitmap(cameraCapturedImageBitmap);
                        cameraCapturedImageUri = getImageUri(this, cameraCapturedImageBitmap);
                    }
                });

        newsCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newsCategorySelectedItem = String.valueOf(parent.getItemAtPosition(position));
            }
        });

        newsSubcategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newsSubcategorySelectedItem = Objects.requireNonNull(String.valueOf(parent.getItemAtPosition(position)));
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
                            Toast.makeText(CreateNewsArticleActivity.this, "There is no app that supports this action", Toast.LENGTH_SHORT).show();
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

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateNewsArticleActivity.this, HomeActivity.class));
                finish();
            }
        });
        postNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String title = Objects.requireNonNull(titleTextInputEditText.getText()).toString();
                String description = Objects.requireNonNull(descriptionTextInputEditText.getText()).toString();

                String article = title + "_" + description;

                DatabaseReference shelterAdministratorName = FirebaseDatabase.getInstance().getReference("profiles").child("sheltersAdministrators").child(loggedUserId).child("name");

                shelterAdministratorName.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        name = snapshot.getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                ValidationManager.getInstance().doValidation(CreateNewsArticleActivity.this,
                        titleTextInputLayout).checkEmpty();
                ValidationManager.getInstance().doValidation(CreateNewsArticleActivity.this,
                        descriptionTextInputLayout).checkEmpty();

                if (ValidationManager.getInstance().isNothingEmpty()) {
                    StorageReference ref = newsArticlesMediaContentReference
                            .child("newsArticlesMediaFiles")
                            .child(loggedUserId)
                            .child("images/"+title+"_"+description);

                    if (gallerySelectedImageUri != null) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference("profiles").child("shelterAdministrators").child(loggedUserId);

                        storageReference.child(name+"_"+loggedUserId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                newsArticlesReference.child(newsCategorySelectedItem).child(article).child("newsArticleAuthorProfileImage").setValue(uri.toString());
                            }
                        });

                        newsArticlesReference.child(newsCategorySelectedItem).child(article).child("authorName").setValue(name);

                        // Adding listeners on upload or failure of image
                        UploadTask uploadTask = ref.putFile(gallerySelectedImageUri);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                Toast.makeText(CreateNewsArticleActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                                String fileLink = task.getResult().toString();

                                                newsArticlesReference.child(newsCategorySelectedItem).child(article).child("category").setValue(newsCategorySelectedItem);
                                                newsArticlesReference.child(newsCategorySelectedItem).child(article).child("subcategory").setValue(newsSubcategorySelectedItem);
                                                newsArticlesReference.child(newsCategorySelectedItem).child(article).child("title").setValue(title);
                                                newsArticlesReference.child(newsCategorySelectedItem).child(article).child("description").setValue(description);
                                                newsArticlesReference.child(newsCategorySelectedItem).child(article).child("mediaImageDownloadLink").setValue(fileLink);
                                            }
                                        });
                                        sendUserToNextActivity();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CreateNewsArticleActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                                    Toast.makeText(CreateNewsArticleActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                                    String fileLink = task.getResult().toString();

                                                    newsArticlesReference.child(newsCategorySelectedItem).child(article).child("category").setValue(newsCategorySelectedItem);
                                                    newsArticlesReference.child(newsCategorySelectedItem).child(article).child("subcategory").setValue(newsSubcategorySelectedItem);
                                                    newsArticlesReference.child(newsCategorySelectedItem).child(article).child("title").setValue(title);
                                                    newsArticlesReference.child(newsCategorySelectedItem).child(article).child("description").setValue(description);
                                                    newsArticlesReference.child(newsCategorySelectedItem).child(article).child("mediaImageDownloadLink").setValue(fileLink);
                                                }
                                            });
                                            sendUserToNextActivity();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CreateNewsArticleActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(CreateNewsArticleActivity.this, "Please select news article media image", Toast.LENGTH_SHORT).show();
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

    public void sendUserToNextActivity() {
        Intent intent = new Intent(CreateNewsArticleActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setTextWatcher() {
        Objects.requireNonNull(titleTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(descriptionTextInputLayout.getEditText()).addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.hashCode() == Objects.requireNonNull(titleTextInputLayout.getEditText()).getText().hashCode()) {
            titleTextInputLayout.setErrorEnabled(false);
        } else if (s.hashCode() == Objects.requireNonNull(descriptionTextInputLayout.getEditText()).getText().hashCode()) {
            descriptionTextInputLayout.setErrorEnabled(false);
        }
    }

    @Override
    public void setError(TextInputLayout textInputLayout, String errorMessage) {
        textInputLayout.setError(errorMessage);
    }
}