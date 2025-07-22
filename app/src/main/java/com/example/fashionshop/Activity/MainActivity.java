package com.example.fashionshop.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.fashionshop.Adapter.CategoryAdapter;
import com.example.fashionshop.Adapter.PoppularAdapter;
import com.example.fashionshop.Adapter.SliderAdapter;
import com.example.fashionshop.Domain.BannerModel;
import com.example.fashionshop.R;
import com.example.fashionshop.ViewModel.MainViewModel;
import com.example.fashionshop.databinding.ActivityMainBinding;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new MainViewModel();
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        binding.logout.setOnClickListener(v -> handleLogout());
        binding.chatSupportBtn.setOnClickListener(v -> openChatSupport());
        initCategory();
        initSlider();
        initPopular();
        bottomNavigation();
        loadUserInfo();
    }

    private void bottomNavigation() {
        binding.bottomNavigation.setItemSelected(R.id.home, true);

        binding.bottomNavigation.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                if (i == R.id.home) {
                    // Đang ở MainActivity nên không cần làm gì
                } else if (i == R.id.product) {
                    startActivity(new Intent(MainActivity.this, ProductListActitivy.class));
                    overridePendingTransition(0, 0);
                }
                else if (i == R.id.cart) {
                    startActivity(new Intent(MainActivity.this, CartActivity.class));
                    overridePendingTransition(0, 0);
                }
                else if (i == R.id.profile) {
                    startActivity(new Intent(MainActivity.this, PaymentHistoryActivity.class));
                    overridePendingTransition(0, 0);
                }

            }
        });

        binding.cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,CartActivity.class)));
    }

    // Thêm method để mở chat support
    private void openChatSupport() {
        String userId = prefs.getString("userId", null);
        if (userId == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để sử dụng chat", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra role của user
        viewModel.getRole(userId).observe(this, role -> {
            if ("admin".equals(role)) {
                // Admin -> mở danh sách chat rooms
                startActivity(new Intent(MainActivity.this, ChatListActivity.class));
            } else {
                // User -> mở chat với admin
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initPopular() {
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        viewModel.loadPopular().observeForever (itemsModels -> {
            if(!itemsModels.isEmpty()){
                binding.popularView.setLayoutManager (
                        new LinearLayoutManager(MainActivity.this, LinearLayoutManager. HORIZONTAL,false));
                binding.popularView.setAdapter(new PoppularAdapter(itemsModels));
                binding.popularView.setNestedScrollingEnabled(true);
            }
            binding.progressBarPopular.setVisibility(View.GONE);
        });
        viewModel.loadPopular();
    }

    private void initSlider() {
        binding.progressBarSlider.setVisibility(View.VISIBLE);
        viewModel.loadBanner().observeForever(bannerModels -> {
            if (bannerModels != null && !bannerModels.isEmpty()) {
                banners(bannerModels);
                binding.progressBarSlider.setVisibility(View.GONE);
            }
        });

        viewModel.loadBanner();
    }

    private void banners(ArrayList<BannerModel> bannerModels) {
        binding.viewPagerSlider.setAdapter(new SliderAdapter(bannerModels, binding.viewPagerSlider));
        binding.viewPagerSlider.setClipToPadding(false);
        binding.viewPagerSlider.setClipChildren(false);
        binding.viewPagerSlider.setOffscreenPageLimit(3);
        binding.viewPagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        binding.viewPagerSlider.setPageTransformer(compositePageTransformer);
    }

    private void initCategory() {
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        viewModel.loadCategory().observeForever(categoryModels -> {
            binding.categoryView.setLayoutManager(new LinearLayoutManager(
                    MainActivity.this, LinearLayoutManager.HORIZONTAL,false
            ));
            binding.categoryView.setAdapter(new CategoryAdapter(categoryModels));
            binding.categoryView.setNestedScrollingEnabled(true);
            binding.progressBarCategory.setVisibility(View.GONE);
        });
    }

    private void handleLogout() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        Intent intent = new Intent(MainActivity.this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // xóa backstack
        startActivity(intent);
        finish();
    }
    private void loadUserInfo() {
        String userId = prefs.getString("userId", null);
        Log.d("USER_ID_CHECK", "userId: " + userId);
        if (userId == null) return;

        viewModel.getUserById(userId).observe(this, user -> {
            if (user != null) {
                String fullName = user.getFullName();
                Log.d("USER_INFO", "Fullname: " + fullName);

                binding.textView5.setText(fullName);

                // Lưu fullname vào SharedPreferences để dùng ở chỗ khác (ví dụ đánh giá)
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("user_name", fullName);
                editor.apply();

            } else {
                Log.e("USER_INFO", "User not found or null");
            }
        });
    }



}