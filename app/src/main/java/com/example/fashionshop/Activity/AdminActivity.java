package com.example.fashionshop.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fashionshop.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    Button btnCreateProduct, btnListProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnCreateProduct = findViewById(R.id.btnCreateProduct);
        btnListProducts = findViewById(R.id.btnListProducts);

        btnCreateProduct.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, AddProductActivity.class);
            startActivity(intent);
        });

        btnListProducts.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ProductListAdminActivity.class);
            startActivity(intent);
        });
    }
}
