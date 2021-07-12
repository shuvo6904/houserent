package com.example.houserentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {

    TextView rentedAmount , homeLocation, buildingName, floorNumber, detailsAddress , genderValue, rentTypeValue, rentDate, postedBy;
    ImageView homeImage;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private HomePageData model;
    private CheckBox checkBoxFavourite;
    private Session session;

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
        postedBy = (TextView) findViewById(R.id.postedById);

        //Bundle mBundle = getIntent().getExtras();

        model = (HomePageData) getIntent().getSerializableExtra("model");

        if (model != null){

            rentedAmount.setText("Rented Amount : " + model.getRentAmount());
            homeLocation.setText("Location : "+ model.getLocation());
            //homeImage.setImageResource(mBundle.getInt("Image"));

            buildingName.setText("Building Name : " + model.getBuildingName());
            floorNumber.setText("Floor Number : " + model.getFloorNumber());
            detailsAddress.setText("Details Address : " + model.getDetailsAddress());
            genderValue.setText("Gender Type : " + model.getValueOfGender());
            rentTypeValue.setText("Rent Type : " + model.getValueOfRentType());
            rentDate.setText("Rent Date : " + model.getDatePick());
            postedBy.setText("Posted By : " + model.getNameOfUser() + "\n\n" + "Phone Number : " + model.getPhnNumOfUser());

            Glide.with(this)
                    .load(model.getImage())
                    .into(homeImage);

//            rentedAmount.setText("Rented Amount : " + mBundle.getString("RentedAmount"));
//            homeLocation.setText("Location : "+ mBundle.getString("Location"));
//            //homeImage.setImageResource(mBundle.getInt("Image"));
//
//            buildingName.setText("Building Name : " + mBundle.getString("BuildingName"));
//            floorNumber.setText("Floor Number : " + mBundle.getString("FloorNumber"));
//            detailsAddress.setText("Details Address : " + mBundle.getString("DetailsAddress"));
//            genderValue.setText("Gender Type : " + mBundle.getString("GenderType"));
//            rentTypeValue.setText("Rent Type : " + mBundle.getString("RentType"));
//            rentDate.setText("Rent Date : " + mBundle.getString("DatePicker"));
//            postedBy.setText("Posted By : " + mBundle.getString("NameOfUser") + "\n\n" + "Phone Number : " + mBundle.getString("UserPhnNumber"));
//
//            Glide.with(this)
//                    .load(mBundle.getString("Image"))
//                    .into(homeImage);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        checkBoxFavourite = findViewById(R.id.checkboxFavourite);
        checkBoxFavourite.setOnCheckedChangeListener(favListener);

        setFavourite();
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
}