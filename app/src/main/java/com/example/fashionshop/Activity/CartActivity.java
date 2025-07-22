package com.example.fashionshop.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fashionshop.Adapter.CartAdapter;
import com.example.fashionshop.Api.CreateOrder;
import com.example.fashionshop.Domain.CheckoutRecord;
import com.example.fashionshop.Helper.ChangeNumberItemsListener;
import com.example.fashionshop.Helper.ManagmentCart;
import com.example.fashionshop.R;
import com.example.fashionshop.databinding.ActivityCartBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

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



        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        managmentCart= new ManagmentCart(this);
        calculatorCart();
        setVariable();
        initCartList();

        binding.backBtn.setOnClickListener(v->finish());
        bottomNavigation();
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
            CreateOrder orderApi = new CreateOrder();
            try {
                double delivery = 10;
                double total = (Math.round((managmentCart.getTotalFee() + tax + delivery) * 100.0) / 100.0 ) * 26145.00;
                String totalString = String.format("%.0f", total);


                JSONObject data = orderApi.createOrder(totalString);
                String code = data.getString("return_code");
                if (code.equals("1")) {
                    String token = data.getString("zp_trans_token");
                    ZaloPaySDK.getInstance().payOrder(CartActivity.this, token, "demozpdk://app", new PayOrderListener() {
                        @Override
                        public void onPaymentSucceeded(String s, String s1, String s2) {

                            saveCheckoutToHistoryJson();
                            Intent intent1 = new Intent(CartActivity.this, PaymentNotification.class);
                            intent1.putExtra("result", "Thanh toán thành công");
                            startActivity(intent1);
                        }

                        @Override
                        public void onPaymentCanceled(String s, String s1) {
                            Intent intent1 = new Intent(CartActivity.this, PaymentNotification.class);
                            intent1.putExtra("result", "Hủy thanh toán");
                            startActivity(intent1);
                        }

                        @Override
                        public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                            Intent intent1 = new Intent(CartActivity.this, PaymentNotification.class);
                            intent1.putExtra("result", "Lỗi thanh toán");
                            startActivity(intent1);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Đảm bảo luôn set đúng tab khi quay lại màn hình
        binding.bottomNavigation.setItemSelected(R.id.cart, true); // false để không gọi listener
    }


    private void bottomNavigation() {
        binding.bottomNavigation.setItemSelected(R.id.cart, true);

        binding.bottomNavigation.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                if (i == R.id.home) {
                    startActivity(new Intent(CartActivity.this, MainActivity.class));
                    overridePendingTransition(0, 0);
                    // Đang ở MainActivity nên không cần làm gì
                } else if (i == R.id.product) {
                    startActivity(new Intent(CartActivity.this, ProductListActitivy.class));
                    overridePendingTransition(0, 0);
                }else if(i == R.id.cart){

                }
                else if(i == R.id.profile){
                    startActivity(new Intent(CartActivity.this, PaymentHistoryActivity.class));
                    overridePendingTransition(0, 0);
                }

            }
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


    }
    private void clearPaymentHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("PaymentHistory", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Toast.makeText(this, "Đã xóa lịch sử thanh toán", Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}