package com.example.fashionshop.Activity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.fashionshop.Domain.UserModel;
import com.example.fashionshop.ViewModel.MainViewModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.example.fashionshop.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding.btnRegister.setOnClickListener(v -> handleRegister());
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void handleRegister() {
        String fullName = binding.edtFullName.getText().toString().trim();
        String email = binding.edtEmail.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();
        String confirmPassword = binding.edtConfirmPassword.getText().toString().trim();

        if (fullName.isEmpty()) {
            binding.edtFullName.setError("Full name is required");
            binding.edtFullName.requestFocus();
            return;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.setError("Invalid email format");
            binding.edtEmail.requestFocus();
            return;
        }

        if (password.length() < 6) {
            binding.edtPassword.setError("Password must be at least 6 characters");
            binding.edtPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            binding.edtConfirmPassword.setError("Passwords do not match");
            binding.edtConfirmPassword.requestFocus();
            return;
        }

        viewModel.checkEmailExists(email).observe(this, exists -> {
            if (exists) {
                Toast.makeText(this, "Email is already registered", Toast.LENGTH_SHORT).show();
            } else {
                String createdAt = getCurrentDateTime();
                UserModel newUser = new UserModel(fullName, email, password, createdAt, "user");

                viewModel.registerUser(newUser).observe(this, success -> {
                    if (success) {
                        Toast.makeText(this, "Register success!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Register failed!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(new Date());
    }
}

