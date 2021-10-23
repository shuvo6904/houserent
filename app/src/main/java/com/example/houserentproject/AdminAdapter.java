package com.example.houserentproject;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminAdapter extends RecyclerView.Adapter<MyAdminHomePageViewHolder> {

    private Context adminContext;
    private List<HomePageData> adminHomePageDataList;

    public AdminAdapter(Context adminContext, List<HomePageData> adminHomePageDataList) {
        this.adminContext = adminContext;
        this.adminHomePageDataList = adminHomePageDataList;
    }

    @NonNull
    @Override
    public MyAdminHomePageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View adminView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_admin_home_row, parent, false);
        return new MyAdminHomePageViewHolder(adminView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdminHomePageViewHolder holder, int position) {

        HomePageData adminModel = adminHomePageDataList.get(position);

        Glide.with(adminContext)
                .load(adminModel.getImage())
                .into(holder.adminHomePageImageView);

        holder.adminRentAmount.setText(adminModel.getRentAmount());
        holder.adminLocation.setText(adminModel.getLocation());
        holder.adminPostStatus.setText(adminModel.getPostStatus());

        holder.adminCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent adminIntent = new Intent(adminContext, AdminHomePageDetails.class);
                adminIntent.putExtra("adminPostModel",adminModel);
                adminContext.startActivity(adminIntent);
            }
        });

        holder.adminApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();

                map.put("postStatus", "Approve");

                FirebaseDatabase.getInstance().getReference().child("Data")
                        .child(adminModel.getAdUserId()).child(adminModel.getId()).updateChildren(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(holder.adminApprove.getContext(), "Post Approved", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(holder.adminApprove.getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });


        holder.adminDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.adminDelete.getContext());
                builder.setTitle("Delete Post");
                builder.setMessage("Are You Sure Want to Delete This Post?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseDatabase.getInstance().getReference().child("Data")
                                .child(adminModel.getAdUserId()).child(adminModel.getId()).removeValue();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();

            }
        });



    }

    @Override
    public int getItemCount() {

        return adminHomePageDataList.size();
    }
}
