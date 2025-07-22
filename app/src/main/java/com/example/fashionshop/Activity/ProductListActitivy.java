package com.example.fashionshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionshop.Adapter.CategoryAdapter;
import com.example.fashionshop.Adapter.PoppularAdapter;
import com.example.fashionshop.Domain.ItemModel;
import com.example.fashionshop.R;
import com.example.fashionshop.ViewModel.MainViewModel;
import com.example.fashionshop.databinding.ActivityMainBinding;
import com.example.fashionshop.databinding.ActivityProductListBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

public class ProductListActitivy extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ActivityProductListBinding binding;
    private MainViewModel viewModel;
    private PoppularAdapter adapter;
    private CategoryAdapter categoryAdapter;
    private ArrayList<ItemModel> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewModel = new MainViewModel();
        binding = ActivityProductListBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initRecyclerView();
        initCategory();
        loadDataFromFirebase(); // Hoặc load dữ liệu mẫu nếu không dùng Firebase
        bottomNavigation();
        searchTitle();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        items = new ArrayList<>();
        adapter = new PoppularAdapter(items);
        recyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
    }
    private void bottomNavigation() {
        binding.bottomNavigation.setItemSelected(R.id.product, true);

        binding.bottomNavigation.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                if (i == R.id.home) {
                    startActivity(new Intent(ProductListActitivy.this, MainActivity.class));
                    overridePendingTransition(0, 0);
                    // Đang ở MainActivity nên không cần làm gì
                } else if (i == R.id.product) {

                }else if(i == R.id.cart){
                    startActivity(new Intent(ProductListActitivy.this, CartActivity.class));
                    overridePendingTransition(0, 0);
                }
                else if(i == R.id.profile){
                    startActivity(new Intent(ProductListActitivy.this, PaymentHistoryActivity.class));
                    overridePendingTransition(0, 0);
                }

            }
        });
    }
    private void searchTitle(){
        EditText searchEditText = findViewById(R.id.editTextText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }



    private void loadDataFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Items");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                items.clear();
                ArrayList<ItemModel> newList = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    ItemModel item = child.getValue(ItemModel.class);
                    if (item != null) {
                        newList.add(item);
                    }
                }
                adapter.updateList(newList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ProductListActitivy.this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initCategory() {
        viewModel.loadCategory().observeForever(categoryModels -> {
            binding.categoryView.setLayoutManager(new LinearLayoutManager(
                    ProductListActitivy.this, LinearLayoutManager.HORIZONTAL,false
            ));
            binding.categoryView.setAdapter(new CategoryAdapter(categoryModels));
            binding.categoryView.setNestedScrollingEnabled(true);
        });
    }
}
