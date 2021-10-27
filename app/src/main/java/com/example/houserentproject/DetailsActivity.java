package com.example.houserentproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {

    TextView rentedAmount, homeLocation, buildingName, floorNumber, detailsAddress, genderValue, rentTypeValue, rentDate, advertiserUsrName, advertiserPhnNum;
    ImageView homeImage, userImage;
    ImageButton callButton;
    private StorageReference adStorageReference, adProfileStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String currentUserId;
    private HomePageData model;
    private CheckBox checkBoxFavourite;
    private Session session;
    String advertiserUserId, userName, userPhnNumber;

    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getUserId();
        session = new Session(this);
        rentedAmount = findViewById(R.id.rentedAmountId);
        homeLocation = (TextView) findViewById(R.id.homeLocationId);
        homeImage = (ImageView) findViewById(R.id.ivImage2Id);
        buildingName = (TextView) findViewById(R.id.buildingNameId);
        floorNumber = (TextView) findViewById(R.id.floorNumberId);
        detailsAddress = (TextView) findViewById(R.id.detailsAddressId);
        genderValue = (TextView) findViewById(R.id.genderValueId);
        rentTypeValue = (TextView) findViewById(R.id.rentTypeValueId);
        rentDate = (TextView) findViewById(R.id.datePickerId);
        userImage = (ImageView) findViewById(R.id.userProfileImageViewId);
        advertiserUsrName = (TextView) findViewById(R.id.userNameId);
        advertiserPhnNum = (TextView) findViewById(R.id.userNumberId);
        callButton = (ImageButton) findViewById(R.id.callId);
        fStore = FirebaseFirestore.getInstance();

        model = (HomePageData) getIntent().getSerializableExtra("model");

        if (model != null) {
            rentedAmount.setText("Rented Amount : " + model.getRentAmount());
            homeLocation.setText("Location : " + model.getLocation());
            buildingName.setText("Building Name : " + model.getBuildingName());
            floorNumber.setText("Floor Number : " + model.getFloorNumber());
            detailsAddress.setText("Details Address : " + model.getDetailsAddress());
            genderValue.setText("Gender Type : " + model.getValueOfGender());
            rentTypeValue.setText("Rent Type : " + model.getValueOfRentType());
            rentDate.setText("Rent Date : " + model.getDatePick());
            advertiserUserId = model.getAdUserId().trim();
            latitude = model.getHostelLat();
            longitude = model.getHostelLon();
            Glide.with(this)
                    .load(model.getImage())
                    .into(homeImage);
        }

        adStorageReference = FirebaseStorage.getInstance().getReference();
        adProfileStorageRef = adStorageReference.child("Users/" + advertiserUserId + "/profile.jpg");

        adProfileStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(userImage);
            }
        });

        DocumentReference documentReference = fStore.collection("users").document(advertiserUserId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userName = value.getString("fName");
                userPhnNumber = value.getString("PhnNumber");
                advertiserUsrName.setText(userName);
                advertiserPhnNum.setText(userPhnNumber);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        checkBoxFavourite = findViewById(R.id.checkboxFavourite);
        checkBoxFavourite.setOnCheckedChangeListener(favListener);

        setFavourite();

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(Uri.parse("tel:" + userPhnNumber));
                startActivity(intentCall);
            }
        });
    }



    CompoundButton.OnCheckedChangeListener favListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            favouritePost(model, isChecked);
        }
    };

    private void setFavourite() {
        if (session.isFavourite(model.getId())) {
            checkBoxFavourite.setOnCheckedChangeListener (null);
            checkBoxFavourite.setChecked(true);
            checkBoxFavourite.setOnCheckedChangeListener (favListener);
        }
    }

    private void getUserId() {
        mAuth = FirebaseAuth.getInstance();
        currentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    }

    private void favouritePost(HomePageData model,boolean isChecked) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        if (isChecked){
            rootRef.child("favourite").child(currentUserId).child(model.getId()).setValue(model)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                session.addToFavourite(model.getId());
                                Toast.makeText(DetailsActivity.this, "Favourite", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {
            rootRef.child("favourite").child(currentUserId).child(model.getId()).removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                session.removeFromFavourite(model.getId());
                                Toast.makeText(DetailsActivity.this, "Removed from favourite", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


    }



    public void showHostelMap(View view) {

        Intent intent = new Intent(DetailsActivity.this, MapsActivity.class);
        intent.putExtra("lat",latitude);
        intent.putExtra("lon", longitude);
        startActivity(intent);
    }
}