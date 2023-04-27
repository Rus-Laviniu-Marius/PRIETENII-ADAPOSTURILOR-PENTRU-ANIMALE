package com.pet.shelter.friends.authentication;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
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
import com.pet.shelter.friends.WhoAreYouActivity;


import java.util.Objects;

public class LoginActivity extends Activity implements TextWatcher, ErrorSetter {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private TextInputLayout emailTextInputLayout, passwordTextInputLayout;
    private TextInputEditText emailTextInputEditText, passwordTextInputEditText;
    private MaterialButton loginMaterialButton, googleSignInMaterialButton, facebookSignInMaterialButton,
            createAccountMaterialButton, forgottenPasswordMaterialButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTextInputEditText = findViewById(R.id.loginEmail_TextInputEditText);
        passwordTextInputEditText = findViewById(R.id.loginPassword_TextInputEditText);
        emailTextInputLayout = findViewById(R.id.loginEmail_TextInputLayout);
        passwordTextInputLayout = findViewById(R.id.loginPassword_TextInputLayout);
        loginMaterialButton = findViewById(R.id.login_materialButton);
        googleSignInMaterialButton = findViewById(R.id.googleSignIn_button);
        facebookSignInMaterialButton = findViewById(R.id.facebookSignIn_button);
        createAccountMaterialButton = findViewById(R.id.createAccount_textView);
        forgottenPasswordMaterialButton = findViewById(R.id.forgottenPassword_materialButton);

        mAuth = FirebaseAuth.getInstance();

        setOnClickListeners();

    }

    private void setOnClickListeners() {
        googleSignInMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, GoogleSignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        facebookSignInMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FacebookAuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        createAccountMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        forgottenPasswordMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View viewTextInput = LayoutInflater.from(LoginActivity.this).inflate(R.layout.popup_forgotten_password_text_field, null, false);
                final TextInputEditText emailTextInputEditTextPopUp = viewTextInput.findViewById(R.id.popUpInputEmail_textInputEditText);
                final TextInputLayout emailTextInputLayoutPopUp = viewTextInput.findViewById(R.id.popUpInputEmail_textInputLayout);

                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this)
                        .setTitle("Forgotten password?")
                        .setMessage("Please enter your email address to receive the required steps " +
                                "for resetting your password. Please enter a solid password which " +
                                "has at least 14 characters and contains at least one digit " +
                                "one lower character, one upper character and one special character. "+
                                "Otherwise you have to keep resetting your password!")
                        .setView(viewTextInput)
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String email = Objects.requireNonNull(emailTextInputEditTextPopUp.getText()).toString();

                                if (!TextUtils.isEmpty(email)) {
                                    emailTextInputLayoutPopUp.setError("Enter an email address");
                                    emailTextInputLayoutPopUp.requestFocus();
                                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                    emailTextInputLayoutPopUp.setError("Please provide a valid email!");
                                    emailTextInputLayoutPopUp.requestFocus();
                                }

                                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(LoginActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                                                dialogInterface.dismiss();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(LoginActivity.this, "Please provide a valid email!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                materialAlertDialogBuilder.create();
                materialAlertDialogBuilder.show();
            }
        });
    }

    private void loginUser() {
        String email = Objects.requireNonNull(emailTextInputEditText.getText()).toString();
        String password = Objects.requireNonNull(passwordTextInputEditText.getText()).toString();

        ValidationManager.getInstance().doValidation(LoginActivity.this,
                emailTextInputLayout).checkEmpty().checkEmail();
        ValidationManager.getInstance().doValidation(LoginActivity.this,
                passwordTextInputLayout).checkEmpty().checkStrongPassword();
        if (ValidationManager.getInstance().isEmailAndPasswordValid()) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mUser = mAuth.getCurrentUser();
                        if (mUser != null) {
                            if (mUser.isEmailVerified()) {
                                sendUserToNextActivity();
                            } else {
                                Toast.makeText(LoginActivity.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                            }
                        }
                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private void sendUserToNextActivity() {
        Intent intent = new Intent(LoginActivity.this, WhoAreYouActivity.class);
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
        }
    }

    @Override
    public void setError(TextInputLayout textInputLayout, String errorMessage) {
        textInputLayout.setError(errorMessage);
    }
}