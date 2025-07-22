package com.example.fashionshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.fashionshop.Activity.UpdateProductActivity;
import com.example.fashionshop.Domain.ItemModel;
import com.example.fashionshop.R;
import com.example.fashionshop.Repository.MainRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class ProductAdminAdapter extends RecyclerView.Adapter<ProductAdminAdapter.ProductViewHolder> {

    private Context context;
    private ArrayList<ItemModel> productList;
    private MainRepository mainRepository;
    public ProductAdminAdapter(Context context, ArrayList<ItemModel> productList,MainRepository mainRepository) {
        this.context = context;
        this.productList = productList;
        this.mainRepository = mainRepository;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_product_horizontal, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ItemModel product = productList.get(position);

        holder.tvProductName.setText(product.getTitle());

        // Hiển thị giá, giả sử product.getPrice() trả về double hoặc string
        holder.tvProductPrice.setText("$" + product.getPrice());

        // Load ảnh sản phẩm
        ArrayList<String> picUrls = product.getPicUrl();
        if (picUrls != null && !picUrls.isEmpty()) {
            String firstUrl = picUrls.get(0);
            Glide.with(context)
                    .load(firstUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.imgProduct);
        } else {
            holder.imgProduct.setImageResource(R.drawable.ic_launcher_background);
        }


        // Xử lý nút Edit
        holder.btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(context, UpdateProductActivity.class);
                intent.putExtra("item", productList.get(position));
                context.startActivity(intent);
        });

        // Xử lý nút Delete
        holder.btnDelete.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            ItemModel item = productList.get(pos);

            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này không?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        mainRepository.deleteItem(item.getId(), task -> {
                            if (task.isSuccessful()) {
                                if (pos < productList.size()) {
                                    productList.remove(pos);
                                    notifyItemRemoved(pos);
                                }
                                Toast.makeText(context, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Xóa thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct;
        TextView tvProductName, tvProductPrice;
        ImageButton btnEdit, btnDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
