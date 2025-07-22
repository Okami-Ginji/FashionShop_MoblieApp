package com.example.fashionshop.Activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.fashionshop.Adapter.ColorAdapter;
import com.example.fashionshop.Adapter.ReviewAdapter;
import com.example.fashionshop.Adapter.SizeAdapter;
import com.example.fashionshop.Adapter.picListAdapter;
import com.example.fashionshop.Domain.ItemModel;
import com.example.fashionshop.Domain.ReviewModel;
import com.example.fashionshop.Helper.ManagmentCart;
import com.example.fashionshop.R;
import com.example.fashionshop.databinding.ActivityDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
private ActivityDetailBinding binding;
private ArrayList<ReviewModel> reviewList = new ArrayList<>();
private ReviewAdapter reviewAdapter;
private ItemModel object;
private int numberOrder = 1;
private ManagmentCart managmentCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        managmentCart = new ManagmentCart(this);
        getBundles();
        initPicList();
        initSize();
        initColor();
        loadReviews();
    }

    private void initColor() {
        binding.recyclerColor.setAdapter(new ColorAdapter(object.getColor()));
        binding.recyclerColor.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }

    private void initSize() {
        binding.recyclerSize.setAdapter(new SizeAdapter(object.getSize()));
        binding.recyclerSize.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true));
    }
    private void loadReviews() {
        reviewAdapter = new ReviewAdapter(reviewList, this);
        binding.reviewRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.reviewRecycler.setAdapter(reviewAdapter);

        FirebaseDatabase.getInstance()
                .getReference("Reviews")
                .child(object.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        reviewList.clear();
                        if (snapshot.exists()) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                ReviewModel review = data.getValue(ReviewModel.class);
                                if (review != null) {
                                    reviewList.add(review);
                                }
                            }
                        }

                        if (reviewList.isEmpty()) {
                            binding.noReviewTxt.setVisibility(View.VISIBLE);
                            binding.reviewRecycler.setVisibility(View.GONE);
                        } else {
                            binding.noReviewTxt.setVisibility(View.GONE);
                            binding.reviewRecycler.setVisibility(View.VISIBLE);
                            reviewAdapter.notifyDataSetChanged();
                        }
                        Log.d("REVIEW_DEBUG", "Số lượng review tải về: " + reviewList.size());
                        for (ReviewModel r : reviewList) {
                            Log.d("REVIEW_DEBUG", "Reviewer: " + r.getReviewer() + ", Comment: " + r.getComment());
                        }
                        Log.d("OBJECT_ID", "Product ID: " + object.getId());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(DetailActivity.this, "Không thể tải đánh giá", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void initPicList() {
        ArrayList<String> picList = new ArrayList<>(object.getPicUrl());
        Glide.with(this)
                .load(picList.get(0))
                .into(binding.pic);
        binding.picList.setAdapter(new picListAdapter(picList,binding.pic));
        binding.picList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

    }

    private void getBundles() {
        object =(ItemModel) getIntent().getSerializableExtra("Object");
        binding.titleTxt.setText(object.getTitle());
        binding.PriceTxt.setText("$"+object.getPrice());
        binding.oldPriceTxt.setText("$"+object.getOldPrice());
        binding.oldPriceTxt.setPaintFlags(binding.oldPriceTxt.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        binding.descriptionTxt.setText(object.getDescription());
        binding.addToCartBtn.setOnClickListener(v -> {
            object.setNumberinCart(numberOrder);
            managmentCart.insertItem(object);
        });
        binding.backBtn.setOnClickListener(v->finish());
    }
}