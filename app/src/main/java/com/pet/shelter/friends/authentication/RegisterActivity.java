package com.pet.shelter.friends.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pet.shelter.friends.ErrorSetter;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.ValidationManager;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements TextWatcher, ErrorSetter {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private TextInputEditText emailTextInputEditText, passwordTextInputEditText, confirmPasswordTextInputEditText;

    private TextInputLayout emailTextInputLayout, passwordTextInputLayout, confirmPasswordTextInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        emailTextInputEditText = findViewById(R.id.registerEmail_materialTextInputEditText);
        passwordTextInputEditText = findViewById(R.id.registerPassword_materialTextInputEditText);
        confirmPasswordTextInputEditText = findViewById(R.id.registerConfirmPassword_materialTextInputEditText);

        emailTextInputLayout = findViewById(R.id.registerEmail_materialTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.registerPassword_materialTextInputLayout);
        confirmPasswordTextInputLayout = findViewById(R.id.registerConfirmPassword_materialTextInputLayout);

        MaterialButton registerMaterialButton = findViewById(R.id.register_materialButton);
        MaterialButton loginMaterialButton = findViewById(R.id.registerLogin_materialButton);

        registerMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewlyCreatedUser();
            }
        });

        loginMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToNextActivity();
            }
        });
    }

    private void registerNewlyCreatedUser() {

        String email = Objects.requireNonNull(emailTextInputEditText.getText()).toString();
        String password = Objects.requireNonNull(passwordTextInputEditText.getText()).toString();

        ValidationManager.getInstance().doValidation(RegisterActivity.this,
                emailTextInputLayout).checkEmpty().checkEmail();
        ValidationManager.getInstance().doValidation(RegisterActivity.this,
                passwordTextInputLayout).checkEmpty().checkStrongPassword();
        ValidationManager.getInstance().doValidation(RegisterActivity.this,
                confirmPasswordTextInputLayout).checkEmpty().matchPassword(passwordTextInputLayout,
                confirmPasswordTextInputLayout);
        if (ValidationManager.getInstance().isAllValid()) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        openSendConfirmRegistrationEmailPopUp();
                        Toast.makeText(RegisterActivity.this, "Registration Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        setTextWatcher();
    }

    private void setTextWatcher() {
        Objects.requireNonNull(emailTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(passwordTextInputLayout.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(confirmPasswordTextInputLayout.getEditText()).addTextChangedListener(this);
    }


    private void openSendConfirmRegistrationEmailPopUp() {
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new
                MaterialAlertDialogBuilder(RegisterActivity.this)
                .setTitle("Register confirmation email")
                .setMessage("We have sent you an email to confirm your account. Please activate "
                        + "your account in order to login")
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mUser.sendEmailVerification();
                        sendUserToNextActivity();
                    }
                })
                .setNegativeButton("Cancel", null);
        materialAlertDialogBuilder.create().show();
    }

    private void sendConfirmRegistrationEmail() {
        if (mUser != null) {
            mUser.sendEmailVerification();
        }
    }

    private void sendUserToLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.hashCode() == Objects.requireNonNull(emailTextInputLayout.getEditText()).getText().hashCode()) {
            emailTextInputLayout.setErrorEnabled(false);
        } else if (editable.hashCode() == Objects.requireNonNull(passwordTextInputLayout.getEditText()).getText().hashCode()) {
            passwordTextInputLayout.setErrorEnabled(false);
        } else if (editable.hashCode() == Objects.requireNonNull(confirmPasswordTextInputLayout.getEditText()).getText().hashCode()) {
            confirmPasswordTextInputLayout.setErrorEnabled(false);
        }
    }

    @Override
    public void setError(TextInputLayout textInputLayout, String errorMessage) {
        textInputLayout.setError(errorMessage);
    }
}