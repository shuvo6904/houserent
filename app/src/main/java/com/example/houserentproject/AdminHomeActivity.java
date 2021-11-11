package com.example.houserentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    AdminAdapter adminAdapter;
    RecyclerView adminRecyclerView;
    List<HomePageData> adminPageDataList;
    private DatabaseReference adminDatabaseReference;
    private ValueEventListener adminEventListener;
    ProgressDialog adminProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        firebaseAuth = FirebaseAuth.getInstance();

        adminRecyclerView = (RecyclerView)findViewById(R.id.adminRecyclerViewId);

        GridLayoutManager adminGridLayoutManager = new GridLayoutManager(AdminHomeActivity.this,1);
        adminRecyclerView.setLayoutManager(adminGridLayoutManager);

        adminProgressDialog = new ProgressDialog(this);
        adminProgressDialog.setMessage("Loading Data...");

        adminPageDataList = new ArrayList<>();

        adminAdapter = new AdminAdapter(AdminHomeActivity.this, adminPageDataList);
        adminRecyclerView.setAdapter(adminAdapter);

        adminDatabaseReference = FirebaseDatabase.getInstance().getReference("Data");

        adminProgressDialog.show();
        adminEventListener = adminDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                adminPageDataList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        HomePageData adminPageData = dataSnapshot1.getValue(HomePageData.class);
                        adminPageDataList.add(adminPageData);

                    }

                }

                adminAdapter.notifyDataSetChanged();
                adminProgressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                adminProgressDialog.dismiss();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.menuLogoutId:
                firebaseAuth.signOut();
                removeSharedPreference();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                return true;

            case R.id.menuResetPassId:
                startActivity(new Intent(getApplicationContext(), ResetPassActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);


        }

    }

    private void removeSharedPreference() {

        SharedPreferences sharedPreferences = getSharedPreferences("adminSharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("isAdmin");
        editor.clear();
        editor.apply();

    }
}