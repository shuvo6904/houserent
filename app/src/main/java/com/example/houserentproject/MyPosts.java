package com.example.houserentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyPosts extends AppCompatActivity {


    FirebaseAuth fPostAuth;
    String userPostId;
    MyPostAdapter postAdapter;

    RecyclerView postRecyclerView;
    List<MyPostPageData> myPostPageDataList;

    private DatabaseReference postDatabaseReference;
    private ValueEventListener postEventListener;
    ProgressDialog postProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        fPostAuth = FirebaseAuth.getInstance();
        userPostId = fPostAuth.getCurrentUser().getUid();

        postRecyclerView = (RecyclerView)findViewById(R.id.postsRecyclerViewId);

        GridLayoutManager postsGridLayoutManager = new GridLayoutManager(MyPosts.this,1);
        postRecyclerView.setLayoutManager(postsGridLayoutManager);

        postProgressDialog = new ProgressDialog(this);
        postProgressDialog.setMessage("Loading Data...");

        myPostPageDataList = new ArrayList<>();

        postAdapter = new MyPostAdapter(MyPosts.this, myPostPageDataList);
        postRecyclerView.setAdapter(postAdapter);

        postDatabaseReference = FirebaseDatabase.getInstance().getReference("Data").child(userPostId);

        postProgressDialog.show();

        postEventListener = postDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                myPostPageDataList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                        MyPostPageData postHomePageData = dataSnapshot.getValue(MyPostPageData.class);
                        myPostPageDataList.add(postHomePageData);

                }

                postAdapter.notifyDataSetChanged();
                postProgressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                postProgressDialog.dismiss();

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
















