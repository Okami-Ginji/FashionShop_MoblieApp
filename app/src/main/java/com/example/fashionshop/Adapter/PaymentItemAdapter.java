package com.example.fashionshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fashionshop.Activity.DetailActivity;
import com.example.fashionshop.Domain.ItemModel;
import com.example.fashionshop.Domain.ReviewModel;
import com.example.fashionshop.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PaymentItemAdapter extends RecyclerView.Adapter<PaymentItemAdapter.ViewHolder> {
    private List<ItemModel> itemList;
    private Context context;

    public PaymentItemAdapter(List<ItemModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView titleTxt, descTxt, quantityTxt, priceTxt;
        Button btnReviewProduct;

        public ViewHolder(View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            quantityTxt = itemView.findViewById(R.id.quantityTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            btnReviewProduct = itemView.findViewById(R.id.btnReviewProduct);
        }
    }
    private void showReviewDialog(ItemModel item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Đánh giá sản phẩm");

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_review, null);
        builder.setView(dialogView);

        TextView tvReviewerName = dialogView.findViewById(R.id.tvReviewerName);
        EditText editReviewText = dialogView.findViewById(R.id.editReviewText);

        // Lấy tên người đánh giá từ SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userName = prefs.getString("user_name", "Khách");
        tvReviewerName.setText("Reviewer: " + userName);

        builder.setPositiveButton("Send", (dialog, which) -> {
            String reviewText = editReviewText.getText().toString().trim();

            if (reviewText.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập nhận xét", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lưu lên Firebase
            String productId = item.getId();  // phải trả về "-OVYJfbi0MrFkYkNOh31"
            DatabaseReference reviewRef = FirebaseDatabase.getInstance().getReference("Reviews");
            String reviewId = reviewRef.child(productId).push().getKey();

            ReviewModel review = new ReviewModel(userName, reviewText);

            reviewRef.child(productId).child(reviewId).setValue(review)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Cảm ơn " + userName + " đã đánh giá!", Toast.LENGTH_SHORT).show();
                        Log.d("REVIEW_SAVE", "Đã lưu đánh giá vào Reviews/" + productId);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("REVIEW_ERROR", "Lỗi khi lưu: " + e.getMessage());
                    });
        });


        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @NonNull
    @Override
    public PaymentItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_payment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentItemAdapter.ViewHolder holder, int position) {
        ItemModel item = itemList.get(position);
        holder.titleTxt.setText(item.getTitle());
        holder.quantityTxt.setText(String.valueOf(item.getNumberinCart()));
        holder.priceTxt.setText("$" + item.getPrice());

        if (item.getPicUrl() != null && !item.getPicUrl().isEmpty()) {
            Glide.with(context).load(item.getPicUrl().get(0)).into(holder.imgProduct);
        }
        holder.itemView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("Object", itemList.get(currentPosition));
                context.startActivity(intent);
            }
        });
        holder.btnReviewProduct.setOnClickListener(v -> {
            showReviewDialog(item);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}

