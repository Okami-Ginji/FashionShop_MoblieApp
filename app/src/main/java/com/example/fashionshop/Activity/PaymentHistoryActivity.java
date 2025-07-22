package com.example.fashionshop.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionshop.Adapter.PaymentItemAdapter;
import com.example.fashionshop.Domain.CheckoutRecord;
import com.example.fashionshop.Domain.ItemModel;
import com.example.fashionshop.R;
import com.example.fashionshop.databinding.ActivityPaymentHistoryBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PaymentHistoryActivity extends AppCompatActivity {
    private ActivityPaymentHistoryBinding binding;
    private RecyclerView recyclerView;
    private List<ItemModel> allPurchasedItems = new ArrayList<>(); // Sẽ chứa tất cả các sản phẩm từ các đơn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPaymentHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = findViewById(R.id.recyclerHistory);
        loadHistoryFromPrefs();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PaymentItemAdapter(allPurchasedItems, this));
        binding.backBtn.setOnClickListener(v->finish());
        bottomNavigation();
    }


    private void loadHistoryFromPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences("PaymentHistory", MODE_PRIVATE);
        String json = sharedPreferences.getString("history_json", "[]");

        Type type = new TypeToken<ArrayList<CheckoutRecord>>() {}.getType();
        List<CheckoutRecord> historyList = new Gson().fromJson(json, type);

        for (CheckoutRecord record : historyList) {
            allPurchasedItems.addAll(record.getItems());
        }
    }
    private void bottomNavigation() {
        binding.bottomNavigation.setItemSelected(R.id.profile, true);

        binding.bottomNavigation.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                if (i == R.id.profile) {
                    // Đang ở MainActivity nên không cần làm gì
                } else if (i == R.id.product) {
                    startActivity(new Intent(PaymentHistoryActivity.this, ProductListActitivy.class));
                    overridePendingTransition(0, 0);
                }
                else if (i == R.id.cart) {
                    startActivity(new Intent(PaymentHistoryActivity.this, CartActivity.class));
                    overridePendingTransition(0, 0);
                }
                else if (i == R.id.home) {
                    startActivity(new Intent(PaymentHistoryActivity.this, MainActivity.class));
                    overridePendingTransition(0, 0);
                }

            }
        });

    }
}
