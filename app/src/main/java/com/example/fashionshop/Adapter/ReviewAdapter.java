package com.example.fashionshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionshop.Domain.ReviewModel;
import com.example.fashionshop.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private List<ReviewModel> reviews;
    private Context context;

    public ReviewAdapter(List<ReviewModel> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView reviewerNameTxt, ratingTxt, commentTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            reviewerNameTxt = itemView.findViewById(R.id.reviewerNameTxt);
            commentTxt = itemView.findViewById(R.id.commentTxt);
        }
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        ReviewModel review = reviews.get(position);
        holder.reviewerNameTxt.setText("Reivewer: " + review.getReviewer());
        holder.commentTxt.setText(review.getComment());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}

