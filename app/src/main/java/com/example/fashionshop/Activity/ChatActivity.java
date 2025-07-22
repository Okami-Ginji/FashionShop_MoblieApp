package com.example.fashionshop.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fashionshop.Adapter.ChatMessageAdapter;
import com.example.fashionshop.Domain.ChatMessage;
import com.example.fashionshop.ViewModel.MainViewModel;
import com.example.fashionshop.databinding.ActivityChatBinding;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private ChatMessageAdapter adapter;
    private MainViewModel viewModel;
    private SharedPreferences prefs;
    private String roomId;
    private String currentUserId;
    private String currentUserName;
    private String receiverId;
    private String currentUserRole;
    private ArrayList<ChatMessage> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        initUserInfo();
        setupRecyclerView();
        setupClickListeners();
        loadOrCreateChatRoom();
    }

    private void initUserInfo() {
        currentUserId = prefs.getString("userId", "");
        currentUserName = prefs.getString("user_name", "User");

        // Kiểm tra role để biết ai là người nhận
        viewModel.getRole(currentUserId).observe(this, role -> {
            currentUserRole = role != null ? role : "user";

            if ("admin".equals(currentUserRole)) {
                // Admin chat với user cụ thể (lấy từ Intent)
                roomId = getIntent().getStringExtra("roomId");
                receiverId = getIntent().getStringExtra("userId");
                String userName = getIntent().getStringExtra("userName");
                binding.chatTitle.setText("Chat với " + userName);

                if (roomId != null) {
                    loadMessages();
                }
            } else {
                // User chat với admin
                roomId = String.valueOf(viewModel.getUserChatRoom(currentUserId));
                receiverId = String.valueOf(viewModel.getUserIdByEmail("admin@gmail.com")); // Hoặc ID admin cụ thể
                binding.chatTitle.setText("Chat Support");

                if (roomId != null) {
                    loadMessages();
                }
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new ChatMessageAdapter(messages, currentUserId);
        binding.messagesRecyclerView.setAdapter(adapter);
        binding.messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickListeners() {
        binding.backBtn.setOnClickListener(v -> finish());

        binding.sendBtn.setOnClickListener(v -> sendMessage());

        binding.messageInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage();
                return true;
            }
            return false;
        });
    }

    private void loadOrCreateChatRoom() {
        if ("user".equals(currentUserRole)) {
            // User tạo hoặc tìm chat room
            viewModel.getUserChatRoom(currentUserId).observe(this, existingRoomId -> {
                if (existingRoomId != null) {
                    roomId = existingRoomId;
                    loadMessages();
                } else {
                    // Tạo chat room mới
                    viewModel.createChatRoom(currentUserId, currentUserName).observe(this, newRoomId -> {
                        roomId = (String) newRoomId;
                        if (roomId != null) {
                            loadMessages();
                        }
                    });
                }
            });
        }
    }

    private void loadMessages() {
        if (roomId == null) return;

        viewModel.getChatMessages(roomId).observe(this, chatMessages -> {
            if (chatMessages != null) {
                messages.clear();
                messages.addAll(chatMessages);
                adapter.updateMessages(messages);

                // Scroll to bottom
                if (!messages.isEmpty()) {
                    binding.messagesRecyclerView.scrollToPosition(messages.size() - 1);
                }

                // Đánh dấu tin nhắn đã đọc
                viewModel.markMessagesAsRead(roomId, currentUserId);
            }
        });
    }

    private void sendMessage() {
        String messageText = binding.messageInput.getText().toString().trim();
        if (messageText.isEmpty() || roomId == null) return;

        ChatMessage message = new ChatMessage(
                currentUserId,
                currentUserName,
                receiverId,
                messageText,
                currentUserRole
        );

        viewModel.sendMessage(roomId, message).observe(this, success -> {
            if (success != null && success) {
                binding.messageInput.setText("");
                // Tin nhắn sẽ được cập nhật tự động qua listener
            } else {
                Toast.makeText(this, "Gửi tin nhắn thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
