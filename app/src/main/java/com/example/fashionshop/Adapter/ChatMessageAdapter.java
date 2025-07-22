package com.example.fashionshop.Adapter;

import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionshop.Domain.ChatMessage;
import com.example.fashionshop.Domain.ChatRoom;
import com.example.fashionshop.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

// ChatMessageAdapter.java
public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private ArrayList<ChatMessage> messages;
    private String currentUserId;

    public ChatMessageAdapter(ArrayList<ChatMessage> messages, String currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messages.get(position);
        return message.getSenderId().equals(currentUserId) ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);

        if (holder instanceof SentMessageViewHolder) {
            ((SentMessageViewHolder) holder).bind(message);
        } else {
            ((ReceivedMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void updateMessages(ArrayList<ChatMessage> newMessages) {
        this.messages = newMessages;
        notifyDataSetChanged();
    }

    // ViewHolder cho tin nhắn đã gửi
    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
        }

        void bind(ChatMessage message) {
            messageText.setText(message.getMessage());
            timeText.setText(formatTime(message.getTimestamp()));
        }
    }

    // ViewHolder cho tin nhắn nhận được
    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, senderName;

        ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
            senderName = itemView.findViewById(R.id.senderName);
        }

        void bind(ChatMessage message) {
            messageText.setText(message.getMessage());
            timeText.setText(formatTime(message.getTimestamp()));
            senderName.setText(message.getSenderName());
        }
    }

    private static String formatTime(long timestamp) {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(timestamp));
    }
}

