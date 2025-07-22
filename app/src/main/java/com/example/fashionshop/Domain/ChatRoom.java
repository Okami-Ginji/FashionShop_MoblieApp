package com.example.fashionshop.Domain;

public class ChatRoom {
    private String id;
    private String userId;
    private String userName;
    private String adminId;
    private String adminName;
    private String lastMessage;
    private long lastMessageTime;
    private int unreadCount;

    public ChatRoom() {}

    public ChatRoom(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.lastMessageTime = System.currentTimeMillis();
        this.unreadCount = 0;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getAdminId() { return adminId; }
    public void setAdminId(String adminId) { this.adminId = adminId; }

    public String getAdminName() { return adminName; }
    public void setAdminName(String adminName) { this.adminName = adminName; }

    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }

    public long getLastMessageTime() { return lastMessageTime; }
    public void setLastMessageTime(long lastMessageTime) { this.lastMessageTime = lastMessageTime; }

    public int getUnreadCount() { return unreadCount; }
    public void setUnreadCount(int unreadCount) { this.unreadCount = unreadCount; }
}
