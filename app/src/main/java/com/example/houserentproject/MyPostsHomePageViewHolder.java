package com.example.houserentproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyPostsHomePageViewHolder extends RecyclerView.ViewHolder {

    ImageView myPostImageView;
    TextView myPostRentAmount, myPostLocation, myPostStatus;
    CardView myPostCardView;

    public MyPostsHomePageViewHolder(@NonNull View itemView) {
        super(itemView);

        myPostImageView = itemView.findViewById(R.id.myPostIvImageId);
        myPostRentAmount = itemView.findViewById(R.id.myPostTvRentAmountId);
        myPostLocation = itemView.findViewById(R.id.MyPostTvLocationId);
        myPostStatus = itemView.findViewById(R.id.tvPostStatusId);
        myPostCardView = itemView.findViewById(R.id.myPostCardViewId);

    }
}
