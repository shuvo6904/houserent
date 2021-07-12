package com.example.houserentproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView navHeaderUsername, navHeaderEmail, navHeaderPhone;
    EditText editTextSearch;
    View hView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String userId;

    MyAdapter myAdapter;

    private FloatingActionButton floatingActionButton;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;

    RecyclerView mRecyclerView;
    List<HomePageData> myHomePageDataList;
    HomePageData mhomePageData;

    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextSearch = (EditText)findViewById(R.id.searchId);

        floatingActionButton = findViewById(R.id.fabId);

        drawerLayout = findViewById(R.id.drawerId);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.navigationId);
        navigationView.setNavigationItemSelectedListener(this);
        hView = navigationView.getHeaderView(0);

        navHeaderUsername = hView.findViewById(R.id.navHeaderUserNameId);
        navHeaderEmail = hView.findViewById(R.id.navHeaderEmailId);
        navHeaderPhone= hView.findViewById(R.id.navHeaderPhoneId);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                navHeaderUsername.setText(value.getString("fName"));
                navHeaderEmail.setText(value.getString("email"));
                navHeaderPhone.setText(value.getString("PhnNumber"));
            }
        });

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewId);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");

        myHomePageDataList = new ArrayList<>();

        myAdapter = new MyAdapter(MainActivity.this, myHomePageDataList);
        mRecyclerView.setAdapter(myAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Data");

        progressDialog.show();
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                myHomePageDataList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        HomePageData homePageData = dataSnapshot1.getValue(HomePageData.class);
                        myHomePageDataList.add(homePageData);

                    }

                }

                myAdapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());

            }
        });




    }

    private void filter(String text) {

        ArrayList<HomePageData> filterList = new ArrayList<>();

        for (HomePageData item : myHomePageDataList){
            if (item.getLocation().toLowerCase().contains(text.toLowerCase())){
                filterList.add(item);
            }
        }

        myAdapter.filteredList(filterList);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (toggle.onOptionsItemSelected(item)){
            return true;
        }

        switch (item.getItemId()){
            case R.id.menuLogoutId:
                FirebaseAuth.getInstance().signOut();
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

    public void faButton(View view) {
        startActivity(new Intent(MainActivity.this, PostActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity,menu);

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.homeMenuId){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        if (item.getItemId() == R.id.accountVeriMenuId){
            startActivity(new Intent(this, AccountVerification.class));
            //finish();
        }

        if (item.getItemId() == R.id.myPostMenuId){
            startActivity(new Intent(this, MyPosts.class));
            //finish();
        }

        if (item.getItemId() == R.id.favMenuId){
            startActivity(new Intent(this, FavListActivity.class));
        }

        return false;
    }
}