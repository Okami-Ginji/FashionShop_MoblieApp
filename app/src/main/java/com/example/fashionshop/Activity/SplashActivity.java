package com.example.fashionshop.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fashionshop.databinding.ActivitySplash2Binding;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplash2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        String role = prefs.getString("role", null);

        if (isLoggedIn) {
            if("admin".equalsIgnoreCase(role)){
                startActivity(new Intent(this, AdminActivity.class));
            }
            else {
                startActivity(new Intent(this, MainActivity.class));
            }
            finish();
            return;
        }

        binding = ActivitySplash2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.startBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        binding.textView3.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
    }
}
