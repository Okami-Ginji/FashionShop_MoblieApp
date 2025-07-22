package com.example.fashionshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fashionshop.Adapter.ChatRoomAdapter;
import com.example.fashionshop.Domain.ChatRoom;
import com.example.fashionshop.ViewModel.MainViewModel;
import com.example.fashionshop.databinding.ActivityChatListBinding;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {
    private ActivityChatListBinding binding;
    private ChatRoomAdapter adapter;
    private MainViewModel viewModel;
    private ArrayList<ChatRoom> chatRooms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setupRecyclerView();
        setupClickListeners();
        loadChatRooms();
    }

    private void setupRecyclerView() {
        adapter = new ChatRoomAdapter(chatRooms, this::onChatRoomClick);
        binding.chatRoomsRecyclerView.setAdapter(adapter);
        binding.chatRoomsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickListeners() {
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void loadChatRooms() {
        viewModel.getChatRoomsForAdmin().observe(this, rooms -> {
            if (rooms != null && !rooms.isEmpty()) {
                chatRooms.clear();
                chatRooms.addAll(rooms);
                adapter.updateChatRooms(chatRooms);
                binding.emptyStateLayout.setVisibility(View.GONE);
                binding.chatRoomsRecyclerView.setVisibility(View.VISIBLE);
            } else {
                binding.emptyStateLayout.setVisibility(View.VISIBLE);
                binding.chatRoomsRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void onChatRoomClick(ChatRoom chatRoom) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("roomId", chatRoom.getId());
        intent.putExtra("userId", chatRoom.getUserId());
        intent.putExtra("userName", chatRoom.getUserName());
        startActivity(intent);
    }
}