package com.pet.shelter.friends.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pet.shelter.friends.HomeActivity;
import com.pet.shelter.friends.R;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog progressDialog;

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

        progressDialog = new ProgressDialog(this);

        registerButton = findViewById(R.id.register_button);


        registerButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                registerNewlyCreatedUser();
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
                        sendUserToNextActivity();
                        Toast.makeText(RegisterActivity.this, "Registration Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }


    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}