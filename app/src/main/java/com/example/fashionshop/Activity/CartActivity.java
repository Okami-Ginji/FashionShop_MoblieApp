package com.example.fashionshop.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fashionshop.Adapter.CartAdapter;
import com.example.fashionshop.Domain.CheckoutRecord;
import com.example.fashionshop.Helper.ChangeNumberItemsListener;
import com.example.fashionshop.Helper.ManagmentCart;
import com.example.fashionshop.R;
import com.example.fashionshop.databinding.ActivityCartBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding binding;
    private double tax;
    private ManagmentCart managmentCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart= new ManagmentCart(this);
        calculatorCart();
        setVariable();
        initCartList();
    }

    private void initCartList() {
        if(managmentCart.getListCart().isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollView2.setVisibility(View.GONE);
        }else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollView2.setVisibility(View.VISIBLE);
        }

        binding.CartView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.CartView.setAdapter(new CartAdapter(managmentCart.getListCart(), this, this::calculatorCart));
    }

    private void setVariable() {
        binding.checkoutBtn.setOnClickListener(v -> {
            saveCheckoutToHistoryJson();
        });

    }

    private void calculatorCart() {
        double percentTax = 0.02;
        double delivery = 10;
        tax=Math.round((managmentCart.getTotalFee()*percentTax*100.0))/100.0;

        double total=Math.round((managmentCart.getTotalFee()+tax + delivery)*100.0)/100.0;
        double itemTotal = Math.round((managmentCart.getTotalFee()*100.0))/100.0;

        binding.totalFeeTxt.setText("$"+itemTotal);
        binding.taxTxt.setText("$"+delivery);
        binding.deliveryTxt.setText("$"+delivery);
        binding.totalTxt.setText("$"+total);
    }
    private void saveCheckoutToHistoryJson() {
        SharedPreferences sharedPreferences = getSharedPreferences("PaymentHistory", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();

        // Lấy danh sách cũ nếu có
        String oldJson = sharedPreferences.getString("history_json", "[]");
        Type type = new TypeToken<ArrayList<CheckoutRecord>>() {}.getType();
        ArrayList<CheckoutRecord> oldList = gson.fromJson(oldJson, type);

        // Tạo bản ghi mới
        CheckoutRecord record = new CheckoutRecord();

        // ✅ Sử dụng SimpleDateFormat thay vì LocalDate.now()
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        record.setDate(currentDate);

        record.setItems(managmentCart.getListCart());
        record.setTotal(binding.totalTxt.getText().toString());

        // Thêm vào danh sách
        oldList.add(record);

        // Ghi lại vào SharedPreferences
        String newJson = gson.toJson(oldList);
        editor.putString("history_json", newJson);
        editor.apply();

        Toast.makeText(this, "Đã lưu lịch sử thanh toán!", Toast.LENGTH_SHORT).show();
    }
    private void clearPaymentHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("PaymentHistory", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Toast.makeText(this, "Đã xóa lịch sử thanh toán", Toast.LENGTH_SHORT).show();
    }




}