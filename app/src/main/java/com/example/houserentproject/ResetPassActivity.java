package com.example.houserentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPassActivity extends AppCompatActivity {

    EditText resetPassword, resetConPassword;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        resetPassword = findViewById(R.id.resetPassId);
        resetConPassword = findViewById(R.id.resetconPassId);
        user = FirebaseAuth.getInstance().getCurrentUser();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void btnResetPassSaveId(View view) {

        if (resetPassword.getText().toString().isEmpty()){
            resetPassword.setError("Required Field");
            return;
        }

        if (resetConPassword.getText().toString().isEmpty()){
            resetConPassword.setError("Required Field");
            return;
        }

        if (!resetConPassword.getText().toString().equals(resetConPassword.getText().toString())){
            resetConPassword.setError("Password Don't Match");
            return;
        }

        user.updatePassword(resetPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ResetPassActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ResetPassActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}