package com.example.fashionshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fashionshop.R;

public class PaymentNotification extends AppCompatActivity {

    TextView tvStatus, tvMessage;
    ImageView imgStatus;
    Button btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_notification);

        tvStatus = findViewById(R.id.tvPaymentStatus);
        tvMessage = findViewById(R.id.tvMessage);
        imgStatus = findViewById(R.id.imgStatusIcon);
        btnBackHome = findViewById(R.id.btnBackToHome);

        Intent intent = getIntent();
        String result = intent.getStringExtra("result");

        if ("Thanh toán thành công".equals(result)) {
            tvStatus.setText(result);
            tvMessage.setText("Cảm ơn bạn đã đặt hàng. Đơn hàng sẽ được xử lý sớm nhất.");
            imgStatus.setImageResource(R.drawable.ic_success);
        } else if ("Hủy thanh toán".equals(result)) {
            tvStatus.setText(result);
            tvMessage.setText("Bạn đã hủy thanh toán. Vui lòng thử lại nếu cần.");
            imgStatus.setImageResource(R.drawable.ic_cancel);
        } else {
            tvStatus.setText(result);
            tvMessage.setText("Đã xảy ra lỗi khi thanh toán. Vui lòng kiểm tra kết nối hoặc thử lại sau.");
            imgStatus.setImageResource(R.drawable.ic_error);
        }

        btnBackHome.setOnClickListener(v -> {
            Intent backIntent = new Intent(PaymentNotification.this, MainActivity.class);
            backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(backIntent);
            finish();
        });
    }
}
