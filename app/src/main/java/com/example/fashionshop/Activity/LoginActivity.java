package com.example.fashionshop.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.fashionshop.ViewModel.MainViewModel;
import com.example.fashionshop.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private MainViewModel viewModel;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        boolean isRemembered = prefs.getBoolean("remember", false);
        if (isRemembered) {
            binding.edtUsername.setText(prefs.getString("savedUsername", ""));
            binding.edtPassword.setText(prefs.getString("savedPassword", ""));
            binding.cbRemember.setChecked(true);
        }

        binding.btnLogin.setOnClickListener(v -> handleLogin());
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void handleLogin() {
        String email = binding.edtUsername.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();
        CheckBox cbRemember = binding.cbRemember;

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtUsername.setError("Invalid email format");
            binding.edtUsername.requestFocus();
            return;
        }

        if (password.length() < 6) {
            binding.edtPassword.setError("Password must be at least 6 characters");
            binding.edtPassword.requestFocus();
            return;
        }

        viewModel.login(email, password).observe(this, user -> {
            if (user != null) {
                String role = user.getRole();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putString("userId", user.getId());
                editor.putString("role",role);
                if (cbRemember.isChecked()) {
                    editor.putBoolean("remember", true);
                    editor.putString("savedUsername", email);
                    editor.putString("savedPassword", password);
                } else {
                    editor.remove("remember");
                    editor.remove("savedUsername");
                    editor.remove("savedPassword");
                }


                editor.apply();
                 // <- giả sử user có hàm getRole()

                if ("admin".equalsIgnoreCase(role)) {
                    startActivity(new Intent(this, AdminActivity.class));
                } else {
                    startActivity(new Intent(this, MainActivity.class));
                }
                Toast.makeText(this, "Login success!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
