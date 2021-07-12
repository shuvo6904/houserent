package com.example.houserentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    FirebaseAuth firebaseAuth;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;
    ProgressBar loginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.loginEmailId);
        password = findViewById(R.id.loginPassswordId);

        firebaseAuth = FirebaseAuth.getInstance();

        reset_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();

        loginProgressBar = findViewById(R.id.loginProgressBarId);

    }


    public void buttonRegister(View view) {

        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        finish();
    }

    public void buttonLogin(View view) {

        if (username.getText().toString().isEmpty()){
            username.setError("Email is Missing");
            username.requestFocus();
            return;
        }

        if (password.getText().toString().isEmpty()){
            password.setError("Password is Missing");
            password.requestFocus();
            return;
        }

        if (password.length() < 6){
            password.setError("Password Must be >= 6 Characters");
            password.requestFocus();
            return;
        }

        loginProgressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                loginProgressBar.setVisibility(View.GONE);

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loginProgressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    public void forgotPassword(View view) {
        View view1 = inflater.inflate(R.layout.reset_pop, null);

        reset_alert.setTitle("Reset Forgot Password ?")
                .setMessage("Enter Your Email to get Password Reset link.")
                .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //validate the email address
                        EditText email = view1.findViewById(R.id.forgotEmailId);
                        if (email.getText().toString().isEmpty()){
                            email.setError("Required Field");
                            return;
                        }
                        //send the reset link

                        firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this, "Reset Email sent", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                }).setNegativeButton("Cancel", null)
                .setView(view1)
                .create().show();
    }
}