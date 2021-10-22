package com.example.houserentproject;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

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



    }

    @Override
    public int getItemCount() {

        return adminHomePageDataList.size();
    }
}
