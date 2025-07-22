package com.example.fashionshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionshop.Adapter.ProductAdminAdapter;
import com.example.fashionshop.Domain.ItemModel;
import com.example.fashionshop.R;
import com.example.fashionshop.Repository.MainRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductListAdminActivity extends AppCompatActivity {

    private RecyclerView rvProductList;
    private ProductAdminAdapter adapter;
    private ArrayList<ItemModel> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_list);
        MainRepository mainRepository = new MainRepository();
        rvProductList = findViewById(R.id.rvProductList);
        productList = new ArrayList<>();

        rvProductList.setLayoutManager(new LinearLayoutManager(this)); // dọc
        adapter = new ProductAdminAdapter(this, productList,mainRepository);
        rvProductList.setAdapter(adapter);

        loadProductsFromFirebase();

    }

    private void loadProductsFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Items");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ItemModel item = ds.getValue(ItemModel.class);
                    if (item != null) productList.add(item);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductListAdminActivity.this, "Lấy dữ liệu thất bại: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
