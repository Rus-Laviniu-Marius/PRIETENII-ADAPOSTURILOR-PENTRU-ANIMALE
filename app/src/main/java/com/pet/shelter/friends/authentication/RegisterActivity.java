package com.pet.shelter.friends.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pet.shelter.friends.R;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private TextView login;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog progressDialog;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        nameEditText = findViewById(R.id.registerName_editText);
        emailEditText = findViewById(R.id.registerEmail_editText);
        passwordEditText = findViewById(R.id.registerPassword_editText);
        confirmPasswordEditText = findViewById(R.id.registerConfirmPassword_editText);
        login = findViewById(R.id.loginRegister_textView);

        progressDialog = new ProgressDialog(this);
        dialog = new Dialog(this);

        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewlyCreatedUser();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void registerNewlyCreatedUser() {

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if(!email.matches(emailPattern)) {
            emailEditText.setError("Enter a valid email address");
            emailEditText.requestFocus();
        } else if(password.isEmpty() || password.length() < 6) {
                passwordEditText.setError("Enter a solid password");
                passwordEditText.requestFocus();
        } else if (!password.equals(confirmPassword)){
            confirmPasswordEditText.setError("Passwords does not match");
            confirmPasswordEditText.requestFocus();
        } else {
            progressDialog.setMessage("Please wait while registration ...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        progressDialog.dismiss();
                        openSendConfirmRegistrationEmailPopUp();
                        Toast.makeText(RegisterActivity.this, "Registration Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void openSendConfirmRegistrationEmailPopUp() {
        dialog.setContentView(R.layout.popup_send_confirm_registration_email);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView closeImageViewButton = dialog.findViewById(R.id.popUpConfirmEmailClose_imageView);
        Button acceptButton = dialog.findViewById(R.id.popUpConfirmEmailAccept_button);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendConfirmRegistrationEmail();
                sendUserToLoginActivity();
            }
        });

        closeImageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                sendUserToLoginActivity();
                Toast.makeText(RegisterActivity.this, "Dialog Close", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
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
}