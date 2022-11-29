package com.pet.shelter.friends.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

public class LoginActivity extends Activity {

    private FirebaseAuth mAuth;
    private final String TAG = "LoginActivity - ";
    private Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailEditText = findViewById(R.id.loginEmail_editText);
        EditText passwordEditText = findViewById(R.id.loginPassword_editText);
        loginButton = findViewById(R.id.login_button);

        CharSequence emailText = emailEditText.getText();
        CharSequence passwordText = passwordEditText.getText();

        String email = "";
        String password = "";

        if (emailText != null && passwordText != null) {
            email = emailText.toString();
            password = passwordText.toString();
        }

//        Toast.makeText(LoginActivity.this, "EMAIL = " + email + "\nPASSWORD = " + password, Toast.LENGTH_LONG).show();


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        String finalEmail = email;
        String finalPassword = password;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!finalEmail.isEmpty() && !finalPassword.isEmpty()) {
                    mAuth.signInWithEmailAndPassword(finalEmail, finalPassword)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "signInWithEmail:success");
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


    }
}