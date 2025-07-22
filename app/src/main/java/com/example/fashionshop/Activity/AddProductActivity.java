package com.example.fashionshop.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.fashionshop.Domain.ItemModel;
import com.example.fashionshop.R;
import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView imgPreview;
    private Cloudinary cloudinary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        FlexboxLayout layoutColors = findViewById(R.id.layoutColors);
        EditText edtTitle = findViewById(R.id.edtTitle);
        EditText edtOffPercent = findViewById(R.id.edtOffPercent);
        EditText edtOldPrice = findViewById(R.id.edtOldPrice);
        EditText edtDescription = findViewById(R.id.edtDescription);
        TextView txtSizes = findViewById(R.id.txtSizes);
        imgPreview = findViewById(R.id.imgPreview);
        Button btnSelectImage = findViewById(R.id.btnSelectImage);
        Button btnUpload = findViewById(R.id.btnCreateProduct);

        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dxmglydea",
                "api_key", "591971737331297",
                "api_secret", "SjO-OC813BEoGf0iLTh_GD4teM0"
        ));

        String[] allSizes = {"S", "M", "L", "XL", "XXL"};
        boolean[] selectedSizes = new boolean[allSizes.length];
        ArrayList<String> chosenSizes = new ArrayList<>();

        txtSizes.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Chọn size");

            builder.setMultiChoiceItems(allSizes, selectedSizes, (dialog, which, isChecked) -> {
                if (isChecked) {
                    chosenSizes.add(allSizes[which]);
                } else {
                    chosenSizes.remove(allSizes[which]);
                }
            });

            builder.setPositiveButton("OK", (dialog, which) -> {
                txtSizes.setText(String.join(", ", chosenSizes));
            });

            builder.setNegativeButton("Hủy", null);
            builder.show();
        });

        String[] allColors = {"#006fc4", "#daa048", "#398d41", "#0c3c72", "#ff0000", "#000000", "#ffffff"};
        ArrayList<String> selectedColors = new ArrayList<>();

        for (String hex : allColors) {
            View colorCircle = new View(this);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(Color.parseColor(hex));
            drawable.setShape(GradientDrawable.OVAL);
            drawable.setSize(80, 80);
            drawable.setStroke(4, Color.TRANSPARENT); // default no border

            colorCircle.setBackground(drawable);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(120, 120);
            params.setMargins(20, 20, 20, 20);
            colorCircle.setLayoutParams(params);

            // Set sự kiện click một lần duy nhất
            colorCircle.setOnClickListener(v -> {
                if (selectedColors.contains(hex)) {
                    selectedColors.remove(hex);
                    drawable.setStroke(4, Color.TRANSPARENT);
                } else {
                    selectedColors.add(hex);
                    drawable.setStroke(4, Color.BLACK); // border to show selected
                }
                colorCircle.setBackground(drawable);

                Toast.makeText(this, "Bạn chọn: " + hex, Toast.LENGTH_SHORT).show();
            });

            layoutColors.addView(colorCircle);
        }


        btnSelectImage.setOnClickListener(v -> openFileChooser());

        btnUpload.setOnClickListener(v -> {
            if (imageUri != null) {
                uploadImageToCloudinary(imageUri, url -> {
                    ItemModel item = new ItemModel();
                    item.setId(null);
                    item.setTitle(edtTitle.getText().toString().trim());
                    item.setDescription(edtDescription.getText().toString().trim());

                    // Lấy old price
                    String oldPriceStr = edtOldPrice.getText().toString().trim();
                    if (oldPriceStr.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập giá gốc", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double oldPrice;
                    try {
                        oldPrice = Double.parseDouble(oldPriceStr);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Giá gốc không hợp lệ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Lấy off percent
                    String offPercentStr = edtOffPercent.getText().toString().trim();
                    if (offPercentStr.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập phần trăm khuyến mãi", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double offPercent;
                    try {
                        offPercent = Double.parseDouble(offPercentStr);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "% giảm giá không hợp lệ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (offPercent < 0 || offPercent > 100) {
                        Toast.makeText(this, "% giảm giá phải từ 0 đến 100", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Tính giá bán
                    double price = oldPrice * (1 - offPercent / 100);

                    // Set các thuộc tính vào model
                    item.setOldPrice(oldPrice);
                    item.setOffPercent(((int) offPercent) + "%");
                    item.setPrice(Math.round(price)); // làm tròn nếu cần

                    item.setSize(new ArrayList<>(chosenSizes));
                    item.setColor(new ArrayList<>(selectedColors));
                    item.setPicUrl(new ArrayList<>(Collections.singletonList(url)));

                    addItemToFirebase(item);
                });
            } else {
                Toast.makeText(this, "Vui lòng chọn ảnh!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imgPreview.setImageURI(imageUri);
        }
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

    private void addItemToFirebase(ItemModel item) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Items");
        String id = ref.push().getKey();
        item.setId(id);
        ref.child(id).setValue(item).addOnSuccessListener(aVoid ->
                Toast.makeText(this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show()
        );
    }

    interface Callback<T> {
        void onSuccess(T result);
    }
}
