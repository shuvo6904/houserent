package com.example.houserentproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

public class PostActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    String[] locationSpinnerArray;
    Spinner locationSpinner;

    ImageView homeImage;
    Uri uri;
    EditText txtRentedAmount, txtBuildingName, txtFloorNumber, txtDetailsAddress, datePicker;
    //TextInputLayout datePickerLayout;

    ChipGroup genderChipGroup, rentTypeChipGroup;
    Chip genderChip, rentTypeChip;

    String imageUrl;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String userId, userName, userPhnNumber;

    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();


        locationSpinnerArray = getResources().getStringArray(R.array.location_spinner);
        locationSpinner = (Spinner) findViewById(R.id.locationSpinnerId);

        ArrayAdapter<String> locaSpiArrayAdapter = new ArrayAdapter<>(PostActivity.this,R.layout.sample_location_spinner_view, R.id.sampleViewLocationSpinnerId, locationSpinnerArray);
        locationSpinner.setAdapter(locaSpiArrayAdapter);


        homeImage = (ImageView) findViewById(R.id.postHomeImageId);

        txtRentedAmount = (EditText) findViewById(R.id.rentedAmountId);

        txtBuildingName = (EditText) findViewById(R.id.buildingNameId);
        txtFloorNumber = (EditText) findViewById(R.id.floorNumberId);
        txtDetailsAddress = (EditText) findViewById(R.id.detailsAddressId);
        genderChipGroup = (ChipGroup) findViewById(R.id.genderChipGroupId);
        rentTypeChipGroup = (ChipGroup) findViewById(R.id.rentTypeChipGroupId);
        datePicker = (EditText) findViewById(R.id.selectDateId);
        //datePickerLayout = (TextInputLayout) findViewById(R.id.selectDateLayoutId);


        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");

            }
        });

        userInfo();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void userInfo() {

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userName = value.getString("fName");
                userPhnNumber = value.getString("PhnNumber");
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        datePicker.setText(currentDateString);
    }

    public void btnSelectImage(View view) {

        Intent photoPicker = new Intent(Intent.ACTION_PICK);
        photoPicker.setType("image/*");
        startActivityForResult(photoPicker, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){

            uri = data.getData();
            homeImage.setImageURI(uri);

        }

        else Toast.makeText(this, "You Haven't Picked Any Image", Toast.LENGTH_SHORT).show();

    }

    public void uploadImage(){
        StorageReference storageReference = FirebaseStorage.getInstance()
                .getReference().child("HomeImage").child(uri.getLastPathSegment());

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Data Uploading....");
        progressDialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();

                submitData();
                progressDialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });
    }

    public void btnSubmitId(View view) {

        if (txtRentedAmount.getText().toString().isEmpty()){
            txtRentedAmount.setError("Required Field");
            txtRentedAmount.requestFocus();
            return;
        }

        int genderSelectedId = genderChipGroup.getCheckedChipId();
        genderChip = (Chip) findViewById(genderSelectedId);

        int rentTypeSelectedId = rentTypeChipGroup.getCheckedChipId();
        rentTypeChip = (Chip) findViewById(rentTypeSelectedId);



            uploadImage();


    }

    public void submitData(){

        String myCurrentDateTime = DateFormat.getDateTimeInstance()
                .format(Calendar.getInstance().getTime());

        HomePageData homePageData = new HomePageData(
                txtRentedAmount.getText().toString(),
                locationSpinner.getSelectedItem().toString(),
                imageUrl,
                txtBuildingName.getText().toString(),
                txtFloorNumber.getText().toString(),
                txtDetailsAddress.getText().toString(),
                genderChip.getText().toString(),
                rentTypeChip.getText().toString(),
                datePicker.getText().toString(),
                userName,
                userPhnNumber,
                myCurrentDateTime

        );


        rootRef.child("Data")
                .child(userId)
                .child(myCurrentDateTime).setValue(homePageData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(PostActivity.this, "Data Uploaded",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(PostActivity.this, e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });


    }

}