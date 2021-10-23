package com.example.houserentproject;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdminHomePageViewHolder extends RecyclerView.ViewHolder {

    ImageView adminHomePageImageView;
    TextView adminRentAmount, adminLocation, adminPostStatus;
    CardView adminCardView;
    Button adminApprove, adminDelete;

    public MyAdminHomePageViewHolder(@NonNull View itemView) {
        super(itemView);

        adminHomePageImageView = itemView.findViewById(R.id.adminIvImageId);
        adminRentAmount = itemView.findViewById(R.id.adminTvRentAmountId);
        adminLocation = itemView.findViewById(R.id.adminTvLocationId);
        adminPostStatus = itemView.findViewById(R.id.tvAdminStatusId);
        adminCardView = itemView.findViewById(R.id.adminCardViewId);
        adminApprove = itemView.findViewById(R.id.adminApproveId);
        adminDelete = itemView.findViewById(R.id.adminDeleteId);

    }
}
