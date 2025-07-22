package com.example.fashionshop.Adapter;

import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionshop.Domain.ChatRoom;
import com.example.fashionshop.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {
    private ArrayList<ChatRoom> chatRooms;
    private OnChatRoomClickListener listener;

    public interface OnChatRoomClickListener {
        void onChatRoomClick(ChatRoom chatRoom);
    }

    public ChatRoomAdapter(ArrayList<ChatRoom> chatRooms, OnChatRoomClickListener listener) {
        this.chatRooms = chatRooms;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_room, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatRoom chatRoom = chatRooms.get(position);
        holder.bind(chatRoom, listener);
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    public void updateChatRooms(ArrayList<ChatRoom> newChatRooms) {
        this.chatRooms = newChatRooms;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNameText, lastMessageText, timeText;
        View unreadIndicator;

        ViewHolder(View itemView) {
            super(itemView);
            userNameText = itemView.findViewById(R.id.userNameText);
            lastMessageText = itemView.findViewById(R.id.lastMessageText);
            timeText = itemView.findViewById(R.id.timeText);
            unreadIndicator = itemView.findViewById(R.id.unreadIndicator);
        }

        void bind(ChatRoom chatRoom, OnChatRoomClickListener listener) {
            userNameText.setText(chatRoom.getUserName());
            lastMessageText.setText(chatRoom.getLastMessage() != null ?
                    chatRoom.getLastMessage() : "Chưa có tin nhắn");
            timeText.setText(formatTime(chatRoom.getLastMessageTime()));

            unreadIndicator.setVisibility(chatRoom.getUnreadCount() > 0 ?
                    View.VISIBLE : View.GONE);

            itemView.setOnClickListener(v -> listener.onChatRoomClick(chatRoom));
        }

        private String formatTime(long timestamp) {
            return new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault()).format(new Date(timestamp));
        }
    }
}
