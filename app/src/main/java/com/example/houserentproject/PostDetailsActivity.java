package com.example.houserentproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class PostDetailsActivity extends AppCompatActivity {

    TextView rentedMyPostAmount, homeLocationMyPost, buildingNameMyPost, floorNumberMyPost, detailsAddressMyPost , genderValueMyPost, rentTypeValueMyPost, rentDateMyPost, postedByMyPost;
    ImageView homeImageMyPost;
    private MyPostPageData modelPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);


        rentedMyPostAmount = findViewById(R.id.rentedMyPostAmountId);
        homeLocationMyPost = (TextView) findViewById(R.id.homeLocationMyPostId);
        homeImageMyPost = (ImageView) findViewById(R.id.ivImage3Id);


        buildingNameMyPost = (TextView) findViewById(R.id.buildingNameMyPostId);
        floorNumberMyPost = (TextView) findViewById(R.id.floorNumberMyPostId);
        detailsAddressMyPost = (TextView) findViewById(R.id.detailsAddressMyPostId);
        genderValueMyPost = (TextView) findViewById(R.id.genderValueMyPostId);
        rentTypeValueMyPost = (TextView) findViewById(R.id.rentTypeValueMyPostId);
        rentDateMyPost = (TextView) findViewById(R.id.datePickerMyPostId);
        postedByMyPost = (TextView) findViewById(R.id.postedByMyPostId);

        modelPost = (MyPostPageData) getIntent().getSerializableExtra("postModel");

        if (modelPost != null){

            rentedMyPostAmount.setText("Rented Amount : " + modelPost.getRentAmount());
            homeLocationMyPost.setText("Location : "+ modelPost.getLocation());
            buildingNameMyPost.setText("Building Name : " + modelPost.getBuildingName());
            floorNumberMyPost.setText("Floor Number : " + modelPost.getFloorNumber());
            detailsAddressMyPost.setText("Details Address : " + modelPost.getDetailsAddress());
            genderValueMyPost.setText("Gender Type : " + modelPost.getValueOfGender());
            rentTypeValueMyPost.setText("Rent Type : " + modelPost.getValueOfRentType());
            rentDateMyPost.setText("Rent Date : " + modelPost.getDatePick());
            postedByMyPost.setText("Posted By : " + modelPost.getNameOfUser() + "\n\n" + "Phone Number : " + modelPost.getPhnNumOfUser());

            Glide.with(this)
                    .load(modelPost.getImage())
                    .into(homeImageMyPost);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}