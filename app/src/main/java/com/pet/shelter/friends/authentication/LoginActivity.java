package com.pet.shelter.friends.authentication;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
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
import com.pet.shelter.friends.HomeActivity;
import com.pet.shelter.friends.R;

public class LoginActivity extends Activity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialog progressDialog;
    private final String TAG = "LoginActivity - ";
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView createAccount, forgottenPassword;
    private ImageView googleSignIn, facebookSignIn;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private Dialog forgottenPasswordDialog, forgottenPasswordConfirmEmailSentDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.loginEmail_editText);
        passwordEditText = findViewById(R.id.loginPassword_editText);
        loginButton = findViewById(R.id.login_button);
        googleSignIn = findViewById(R.id.googleSignIn_imageView);
        facebookSignIn = findViewById(R.id.facebookSignIn_imageView);
        createAccount = findViewById(R.id.createAccount_textView);
        forgottenPassword = findViewById(R.id.forgottenPassword_textView);
        forgottenPasswordDialog = new Dialog(this);
        forgottenPasswordConfirmEmailSentDialog = new Dialog(this);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, GoogleSignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        facebookSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FacebookAuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        forgottenPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForgottenPasswordPopUp();
            }
        });
    }

    private void openForgottenPasswordPopUp() {
        forgottenPasswordDialog.setContentView(R.layout.popup_forgotten_password);
        forgottenPasswordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView closeImageViewButton = forgottenPasswordDialog.findViewById(R.id.popUpForgottenPasswordClose_imageView);
        Button sendButton = forgottenPasswordDialog.findViewById(R.id.popUpForgottenPasswordSend_button);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        closeImageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgottenPasswordDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Dialog Close", Toast.LENGTH_SHORT).show();
            }
        });
        forgottenPasswordDialog.show();
    }

    private void resetPassword() {
        EditText emailEditText = forgottenPasswordDialog.findViewById(R.id.popUpInputEmail_editText);

        String email = emailEditText.getText().toString();

        if (email.isEmpty()) {
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please provide valid email!");
            emailEditText.requestFocus();
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    openConfirmForgottenPasswordPopUp();
                }
            }
        });

    }

    private void openConfirmForgottenPasswordPopUp() {
        forgottenPasswordConfirmEmailSentDialog.setContentView(R.layout.popup_forgotten_password_send_email_confirmation);
        forgottenPasswordConfirmEmailSentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView closeImageViewButton = forgottenPasswordConfirmEmailSentDialog.findViewById(R.id.popUpForgottenPasswordConfirmationClose_imageView);
        Button acceptButton = forgottenPasswordConfirmEmailSentDialog.findViewById(R.id.popUpForgottenPasswordConfirmationAccept_button);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToLoginActivity();
            }
        });

        closeImageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgottenPasswordConfirmEmailSentDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Dialog Close", Toast.LENGTH_SHORT).show();
            }
        });
        forgottenPasswordConfirmEmailSentDialog.show();
    }

    private void sendUserToLoginActivity() {
        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (!email.matches(emailPattern)) {
            emailEditText.setError("Enter a valid email address");
            emailEditText.requestFocus();
        } else if (password.isEmpty() || password.length() < 6) {
            passwordEditText.setError("Enter a solid password");
            passwordEditText.requestFocus();
        } else {
            progressDialog.setMessage("Please wait while logging in ...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        mUser = mAuth.getCurrentUser();
                        if(mUser != null) {
                            if(mUser.isEmailVerified()) {
                                sendUserToNextActivity();
                            } else {
                                mUser.sendEmailVerification();
                                Toast.makeText(LoginActivity.this, "Check your email to verify your account!",Toast.LENGTH_LONG).show();
                            }
                        }
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}