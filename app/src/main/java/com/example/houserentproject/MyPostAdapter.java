package com.example.houserentproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostsHomePageViewHolder> {

    private Context myPostContext;
    private List<MyPostPageData> myPostHomePageDataList;

    public MyPostAdapter(Context myPostContext, List<MyPostPageData> myPostHomePageDataList) {
        this.myPostContext = myPostContext;
        this.myPostHomePageDataList = myPostHomePageDataList;
    }

    @NonNull
    @Override
    public MyPostsHomePageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myPostView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_my_post_row_item , parent, false);
        return new MyPostsHomePageViewHolder(myPostView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostsHomePageViewHolder holder, int position) {

        MyPostPageData myPostModel = myPostHomePageDataList.get(position);

        Glide.with(myPostContext)
                .load(myPostModel.getImage())
                .into(holder.myPostImageView);
        holder.myPostRentAmount.setText(myPostModel.getRentAmount());
        holder.myPostLocation.setText(myPostModel.getLocation());
        holder.myPostStatus.setText(myPostModel.getPostStatus());

        holder.myPostCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myPostContext, PostDetailsActivity.class);
                intent.putExtra("postModel",myPostModel);
                myPostContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myPostHomePageDataList.size();
    }
}
