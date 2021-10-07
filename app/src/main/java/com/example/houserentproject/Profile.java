package com.example.houserentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    private Button profileEmailVerifyButton, editProfileButton;
    private BottomSheetDialog sheetDialog;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileEmailVerifyButton = (Button) findViewById(R.id.verifiedEmailButtonId);
        editProfileButton = (Button) findViewById(R.id.editProfileButtonId);

        fAuth = FirebaseAuth.getInstance();

        FirebaseUser user = fAuth.getCurrentUser();

        if (user != null)
            user.reload();

        if (!user.isEmailVerified()){
            Log.d("profile_", "onCreate: email is not verified");
            profileEmailVerifyButton.setVisibility(View.VISIBLE);

        }else Log.d("profile_", "onCreate: email is verified");

        profileEmailVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(Profile.this, "Email verification link has been sent.\nPlease check your email", Toast.LENGTH_LONG).show();

                        // buttonEmailVerify.setVisibility(View.GONE);
                        // textViewEmailVerify.setVisibility(View.GONE);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d("tag", "onFailure : Email not sent " + e.getMessage());

                    }
                });

            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sheetDialog = new BottomSheetDialog(Profile.this,R.style.BottomSheetStyle);

                View view = LayoutInflater.from(Profile.this).inflate(R.layout.bottomsheet_dialog,
                        (LinearLayout)findViewById(R.id.sheetLayoutId));

                sheetDialog.setContentView(view);
                sheetDialog.show();

            }
        });


    }
}