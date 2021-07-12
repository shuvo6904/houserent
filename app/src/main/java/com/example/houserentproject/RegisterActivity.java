package com.example.houserentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText regFullName, regPhnNum, regEmail, regPass, regConPass;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regFullName = findViewById(R.id.regFullNameId);
        regPhnNum = findViewById(R.id.regPhnNumId);
        regEmail = findViewById(R.id.regEmailId);
        regPass = findViewById(R.id.regPassId);
        regConPass = findViewById(R.id.regConPassId);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.regProgressBarId);

        fStore = FirebaseFirestore.getInstance();

    }

    public void tvAlreadyRegistered(View view) {

        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void register(View view) {

        String strFullName = regFullName.getText().toString().trim();
        String strPhnNum = regPhnNum.getText().toString().trim();
        String strEmail = regEmail.getText().toString().trim();
        String strPass = regPass.getText().toString().trim();
        String strConPass = regConPass.getText().toString().trim();

        if (strFullName.isEmpty()){
            regFullName.setError("Full Name is Required");
            regFullName.requestFocus();
            return;
        }

        if (strPhnNum.isEmpty()){
            regPhnNum.setError("Phone Number is Required");
            regPhnNum.requestFocus();
            return;
        }

        if (strEmail.isEmpty()){
            regEmail.setError("Email is Required");
            regEmail.requestFocus();
            return;
        }

        if (strPass.isEmpty()){
            regPass.setError("Password is Required");
            regPass.requestFocus();
            return;
        }

        if (strPass.length() < 6){
            regPass.setError("Password Must be >= 6 Characters");
            regPass.requestFocus();
            return;
        }

        if (strConPass.isEmpty()){
            regConPass.setError("Confirm Password is Required");
            regConPass.requestFocus();
            return;
        }

        if (!strPass.equals(strConPass)){
            regConPass.setError("Password Don't Match");
            regConPass.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        fAuth.createUserWithEmailAndPassword(strEmail, strPass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                userID = fAuth.getCurrentUser().getUid();

                DocumentReference documentReference = fStore.collection("users").document(userID);
                Map<String,Object> user = new HashMap<>();
                user.put("fName",strFullName);
                user.put("email",strEmail);
                user.put("PhnNumber",strPhnNum);
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                    }
                });

                Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });

    }
}