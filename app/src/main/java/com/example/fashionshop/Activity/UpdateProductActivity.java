package com.example.fashionshop.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.fashionshop.Domain.ItemModel;
import com.example.fashionshop.R;
import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class UpdateProductActivity extends AppCompatActivity {

    private ImageView imgPreview;
    private Uri imageUri;
    private Cloudinary cloudinary;
    private ItemModel currentItem;

    // các EditText...
    private EditText edtTitle, edtOldPrice, edtOffPercent, edtDescription;
    private TextView txtSizes;
    private ArrayList<String> selectedSizes = new ArrayList<>();
    private ArrayList<String> selectedColors = new ArrayList<>();
    private FlexboxLayout layoutColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product); // dùng lại layout cũ

        // 1. Nhận dữ liệu từ Intent
        currentItem = (ItemModel) getIntent().getSerializableExtra("item");

        // 2. Ánh xạ view
        edtTitle = findViewById(R.id.edtTitle);
        edtOldPrice = findViewById(R.id.edtOldPrice);
        edtOffPercent = findViewById(R.id.edtOffPercent);
        edtDescription = findViewById(R.id.edtDescription);
        txtSizes = findViewById(R.id.txtSizes);
        layoutColors = findViewById(R.id.layoutColors);
        imgPreview = findViewById(R.id.imgPreview);
        Button btnSelectImage = findViewById(R.id.btnSelectImage);
        Button btnUpdate = findViewById(R.id.btnCreateProduct); // đổi tên ID nếu muốn

        // 3. Gán dữ liệu cũ
        edtTitle.setText(currentItem.getTitle());
        edtOldPrice.setText(String.valueOf(currentItem.getOldPrice()));
        edtOffPercent.setText(currentItem.getOffPercent().replace("%", ""));
        edtDescription.setText(currentItem.getDescription());
        txtSizes.setText(String.join(", ", currentItem.getSize()));
        selectedSizes = new ArrayList<>(currentItem.getSize());
        selectedColors = new ArrayList<>(currentItem.getColor());

        Glide.with(this).load(currentItem.getPicUrl().get(0)).into(imgPreview); // dùng Glide nếu có

        // 4. Hiển thị màu và size như AddActivity (code bạn đã có)
        // Sử dụng selectedColors để hiện border đen...

        // 5. Chọn ảnh mới nếu muốn
        btnSelectImage.setOnClickListener(v -> openFileChooser());

        // 6. Cập nhật khi nhấn nút
        btnUpdate.setText("Cập nhật sản phẩm"); // nếu dùng chung layout
        btnUpdate.setOnClickListener(v -> {
            if (imageUri != null) {
                uploadImageToCloudinary(imageUri, this::updateProductToFirebase);
            } else {
                updateProductToFirebase(currentItem.getPicUrl().get(0));
            }
        });

        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dmtswhlfw",
                "api_key", "875936791922828",
                "api_secret", "tTnMrJSi8qHnw-FACtTdqyrmmfE"
        ));
    }

    private void updateProductToFirebase(String imageUrl) {
        String title = edtTitle.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        double oldPrice = Double.parseDouble(edtOldPrice.getText().toString().trim());
        double offPercent = Double.parseDouble(edtOffPercent.getText().toString().trim());
        double price = oldPrice * (1 - offPercent / 100);

        currentItem.setTitle(title);
        currentItem.setDescription(description);
        currentItem.setOldPrice(oldPrice);
        currentItem.setOffPercent((int) offPercent + "%");
        currentItem.setPrice(Math.round(price));
        currentItem.setSize(new ArrayList<>(selectedSizes));
        currentItem.setColor(new ArrayList<>(selectedColors));
        currentItem.setPicUrl(new ArrayList<>(Collections.singletonList(imageUrl)));

        FirebaseDatabase.getInstance().getReference("Items")
                .child(currentItem.getId())
                .setValue(currentItem)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show());
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imgPreview.setImageURI(imageUri);
        }
    }

    private interface Callback<T> {
        void onSuccess(T result);
    }

    private void uploadImageToCloudinary(Uri uri, final Callback<String> callback) {
        new Thread(() -> {
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Map uploadResult = cloudinary.uploader().upload(inputStream, ObjectUtils.emptyMap());
                String imageUrl = (String) uploadResult.get("secure_url");
                runOnUiThread(() -> callback.onSuccess(imageUrl));
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Upload thất bại: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}
