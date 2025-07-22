package com.example.fashionshop.Activity;

import android.content.SharedPreferences;
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

    Button btnCreateProduct,btnLogout, btnListProducts, btnChatManagement;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        btnCreateProduct = findViewById(R.id.btnCreateProduct);
        btnLogout = findViewById(R.id.btnLogout);
        btnListProducts = findViewById(R.id.btnListProducts);
        btnChatManagement = findViewById(R.id.btnChatManagement);

        btnCreateProduct.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, AddProductActivity.class);
            startActivity(intent);
        });

        btnListProducts.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ProductListAdminActivity.class);
            startActivity(intent);
        });

        btnChatManagement.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ChatListActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            handleLogout();
        });
    }

    private void handleLogout() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        Intent intent = new Intent(AdminActivity.this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // xóa backstack
        startActivity(intent);
        finish();
    }
}
