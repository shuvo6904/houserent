package com.example.houserentproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class AdminHomePageDetails extends AppCompatActivity {

    TextView adminPageRentedAmount, adminPageHomeLocation, adminPageBuildingName, adminPageFloorNumber, adminPageDetailsAddress, adminPageGenderValue, adminPageRentTypeValue, adminPageRentDate,adminAdvertiserUsrName, adminAdvertiserPhnNum;
    ImageView homeImageAdminPage, adminPageUserImage;
    ImageButton adminPageCallButton;
    private StorageReference adminPageAdStorageReference , adminPageAdProfileStorageRef;
    private FirebaseAuth adminPageAuth;
    private FirebaseFirestore adminPageFStore;
    private String adminPageCurrentUserId;
    private HomePageData adminModelPost;
    String adminPageAdvertiserUserId, adminPageUserName, adminPageUserPhnNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page_details);

        adminPageRentedAmount = (TextView) findViewById(R.id.adminPageRentedAmountId);
        adminPageHomeLocation = (TextView) findViewById(R.id.adminPageHomeLocationId);
        homeImageAdminPage = (ImageView) findViewById(R.id.adminPageIvImage2Id);
        adminPageBuildingName = (TextView) findViewById(R.id.adminPageBuildingNameId);
        adminPageFloorNumber = (TextView) findViewById(R.id.adminPageFloorNumberId);
        adminPageDetailsAddress = (TextView) findViewById(R.id.adminPageDetailsAddressId);
        adminPageGenderValue = (TextView) findViewById(R.id.adminPageGenderValueId);
        adminPageRentTypeValue = (TextView) findViewById(R.id.adminPageRentTypeValueId);
        adminPageRentDate = (TextView) findViewById(R.id.adminPageDatePickerId);

        adminPageUserImage = (ImageView) findViewById(R.id.adminPageUserProfileImageViewId);
        adminAdvertiserUsrName= (TextView) findViewById(R.id.adminPageUserNameId);
        adminAdvertiserPhnNum = (TextView) findViewById(R.id.adminPageUserNumberId);
        adminPageCallButton = (ImageButton) findViewById(R.id.adminPageCallId);
        adminPageFStore = FirebaseFirestore.getInstance();

        adminModelPost = (HomePageData) getIntent().getSerializableExtra("adminPostModel");

        if (adminModelPost != null){

            adminPageRentedAmount.setText("Rented Amount : " + adminModelPost.getRentAmount());
            adminPageHomeLocation.setText("Location : "+ adminModelPost.getLocation());
            adminPageBuildingName.setText("Building Name : " + adminModelPost.getBuildingName());
            adminPageFloorNumber.setText("Floor Number : " + adminModelPost.getFloorNumber());
            adminPageDetailsAddress.setText("Details Address : " + adminModelPost.getDetailsAboutHostel());
            adminPageGenderValue.setText("Gender Type : " + adminModelPost.getValueOfGender());
            adminPageRentTypeValue.setText("Rent Type : " + adminModelPost.getValueOfRentType());
            adminPageRentDate.setText("Rent Date : " + adminModelPost.getDatePick());
            adminPageAdvertiserUserId = adminModelPost.getAdUserId().trim();

            Glide.with(this)
                    .load(adminModelPost.getImage())
                    .into(homeImageAdminPage);
        }

        adminPageAdStorageReference = FirebaseStorage.getInstance().getReference();
        adminPageAdProfileStorageRef = adminPageAdStorageReference.child("Users/"+adminPageAdvertiserUserId+"/profile.jpg");

        adminPageAdProfileStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(adminPageUserImage);
            }
        });

        DocumentReference documentReference = adminPageFStore.collection("users").document(adminPageAdvertiserUserId);
        documentReference.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                adminPageUserName = value.getString("fName");
                adminPageUserPhnNumber = value.getString("PhnNumber");
                adminAdvertiserUsrName.setText(adminPageUserName);
                adminAdvertiserPhnNum.setText(adminPageUserPhnNumber);
            }
        });

    }
}