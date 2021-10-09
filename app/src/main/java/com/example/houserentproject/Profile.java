package com.example.houserentproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    private Button profileEmailVerifyButton, editProfileButton;
    private BottomSheetDialog sheetDialog;
    private FirebaseAuth fAuth;
    private ImageView profileImage;
    private StorageReference storageReference, profileStorageRef;
    private FirebaseFirestore firebaseFirestore;
    private TextView profileName, profileEmail, proEditableName, proEditablePhnNum, proEditableEmail, checkIsEmailVerified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileEmailVerifyButton = (Button) findViewById(R.id.verifiedEmailButtonId);
        editProfileButton = (Button) findViewById(R.id.editProfileButtonId);
        profileImage = (ImageView) findViewById(R.id.editableProfileImageViewId);
        profileName = (TextView) findViewById(R.id.profileNameId);
        profileEmail = (TextView) findViewById(R.id.profileEmailId);
        proEditableName = (TextView) findViewById(R.id.editableProfileNameId);
        proEditablePhnNum = (TextView) findViewById(R.id.editableProfilePhnNumId);
        proEditableEmail = (TextView) findViewById(R.id.editableProfileEmailId);
        checkIsEmailVerified = (TextView) findViewById(R.id.checkIsEmailVerifiedId);

        fAuth = FirebaseAuth.getInstance();

        FirebaseUser user = fAuth.getCurrentUser();

        firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = firebaseFirestore.collection("users").document(user.getUid());

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                profileName.setText(value.getString("fName"));
                profileEmail.setText(value.getString("email"));
                proEditableName.setText(value.getString("fName"));
                proEditablePhnNum.setText(value.getString("PhnNumber"));
                proEditableEmail.setText(value.getString("email"));

            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();
        profileStorageRef = storageReference.child("Users/"+user.getUid()+"/profile.jpg");
        profileStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });


        if (user != null)
            user.reload();

        if (!user.isEmailVerified()){
            Log.d("profile_", "onCreate: email is not verified");
            profileEmailVerifyButton.setVisibility(View.VISIBLE);
            checkIsEmailVerified.setText("Email Unverified");

        }else{
            checkIsEmailVerified.setText("Email Verified");
            Log.d("profile_", "onCreate: email is verified");
        }

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

                EditText profileNameBottomSheet, profilePhnNumberBottomSheet, profileEmailBottomSheet;
                profileNameBottomSheet = (EditText) view.findViewById(R.id.editNameId);
                profilePhnNumberBottomSheet =(EditText) view.findViewById(R.id.editPhnNumId);
                profileEmailBottomSheet = (EditText) view.findViewById(R.id.editEmailId);


                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        profileNameBottomSheet.setText(value.getString("fName"));
                        profilePhnNumberBottomSheet.setText(value.getString("PhnNumber"));
                        profileEmailBottomSheet.setText(value.getString("email"));

                    }
                });

                Button updateProfileData = (Button) view.findViewById(R.id.updateButtonId);
                updateProfileData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (profileNameBottomSheet.getText().toString().isEmpty() || profilePhnNumberBottomSheet.getText().toString().isEmpty() || profileEmailBottomSheet.getText().toString().isEmpty()){
                            Toast.makeText(Profile.this, "One or Many Fields are Empty", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String email = profileEmailBottomSheet.getText().toString();
                        user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Map<String, Object> edited = new HashMap<>();
                                edited.put("email", email);
                                edited.put("fName", profileNameBottomSheet.getText().toString());
                                edited.put("PhnNumber", profilePhnNumberBottomSheet.getText().toString());
                                documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(Profile.this, "Profile Updated Successfully", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(Profile.this, MainActivity.class));
                                        finish();
                                    }
                                });
                                //Toast.makeText(Profile.this, "Email is Changed", Toast.LENGTH_LONG).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Profile.this, e.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                });

                sheetDialog.setContentView(view);
                sheetDialog.show();

            }
        });


    }

    public void fabChangeProPic(View view) {

        ImagePicker.with(Profile.this)
                .cameraOnly()	//User can only capture image using Camera
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start(1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                //profileImage.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);
            }
        }

    }

    private void uploadImageToFirebase(Uri imageUri) {
        // upload image to firebase storage
        final StorageReference fileRef = storageReference.child("Users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Picasso.get().load(uri).into(profileImage);

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}