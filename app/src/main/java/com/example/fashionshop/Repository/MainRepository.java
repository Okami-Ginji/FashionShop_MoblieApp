package com.example.fashionshop.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fashionshop.Domain.BannerModel;
import com.example.fashionshop.Domain.CategoryModel;
import com.example.fashionshop.Domain.ChatMessage;
import com.example.fashionshop.Domain.ChatRoom;
import com.example.fashionshop.Domain.ItemModel;
import com.example.fashionshop.Domain.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainRepository
{
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public LiveData<ArrayList<CategoryModel>> loadCategory(){
        MutableLiveData<ArrayList<CategoryModel>> listData = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("Category");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<CategoryModel> list = new ArrayList<>();
                for (DataSnapshot childSnapshot:snapshot.getChildren()){
                    CategoryModel item = childSnapshot.getValue(CategoryModel.class);
                    if (item !=null) list.add(item);
                }
                listData.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return listData;
    }

    public LiveData<ArrayList<BannerModel>> loadBanner(){
        MutableLiveData<ArrayList<BannerModel>> listData = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("Banner");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<BannerModel> list = new ArrayList<>();
                for (DataSnapshot childSnapshot:snapshot.getChildren()){
                    BannerModel item = childSnapshot.getValue(BannerModel.class);
                    if (item !=null) list.add(item);
                }
                listData.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return listData;
    }
    public LiveData<ArrayList<ItemModel>> loadPopular(){
        MutableLiveData<ArrayList<ItemModel>> listData = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("Items");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ItemModel> list = new ArrayList<>();
                for (DataSnapshot childSnapshot:snapshot.getChildren()){
                    ItemModel item = childSnapshot.getValue(ItemModel.class);
                    if (item !=null) list.add(item);
                }
                listData.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return listData;
    }

    //Module Users
    public LiveData<UserModel> login(String email, String password) {
        MutableLiveData<UserModel> userLiveData = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("Users");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    UserModel user = child.getValue(UserModel.class);
                    if (user != null && user.getEmail().equals(email) && user.getPassword().equals(password)) {
                        user.setId(child.getKey());
                        userLiveData.setValue(user);
                        return;
                    }
                }
                userLiveData.setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                userLiveData.setValue(null);
            }
        });

        return userLiveData;
    }

    public LiveData<Boolean> checkEmailExists(String email) {
        MutableLiveData<Boolean> existsLiveData = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("Users");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean exists = false;
                for (DataSnapshot child : snapshot.getChildren()) {
                    UserModel user = child.getValue(UserModel.class);
                    if (user != null && user.getEmail().equals(email)) {
                        exists = true;
                        break;
                    }
                }
                existsLiveData.setValue(exists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                existsLiveData.setValue(false);
            }
        });

        return existsLiveData;
    }

    public LiveData<String> getUserIdByEmail(String email) {
        MutableLiveData<String> userIdLiveData = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("Users");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    UserModel user = child.getValue(UserModel.class);
                    if (user != null && user.getEmail().equals(email)) {
                        userIdLiveData.setValue(child.getKey());
                        return;
                    }
                }
                userIdLiveData.setValue(null); // Email không tồn tại
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                userIdLiveData.setValue(null);
            }
        });

        return userIdLiveData;
    }

    public LiveData<Boolean> registerUser(UserModel newUser) {
        MutableLiveData<Boolean> registerResult = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("Users");

        String userId = ref.push().getKey();
        if (userId == null) {
            registerResult.setValue(false);
            return registerResult;
        }

        ref.child(userId).setValue(newUser).addOnCompleteListener(task -> {
            registerResult.setValue(task.isSuccessful());
        });

        return registerResult;
    }

    public LiveData<UserModel> getUserById(String userId) {
        MutableLiveData<UserModel> userLiveData = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("Users").child(userId);

        ref.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                UserModel user = dataSnapshot.getValue(UserModel.class);
                if (user != null) {
                    user.setId(dataSnapshot.getKey());
                    userLiveData.setValue(user);
                }
            } else {
                userLiveData.setValue(null);
            }
        }).addOnFailureListener(e -> userLiveData.setValue(null));

        return userLiveData;
    }

    public LiveData<String> getRole(String userId) {
        MutableLiveData<String> roleLiveData = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("Users").child(userId).child("role");

        ref.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                String role = snapshot.getValue(String.class);
                roleLiveData.setValue(role);
            } else {
                roleLiveData.setValue(null);
            }
        }).addOnFailureListener(e -> {
            roleLiveData.setValue(null);
        });

        return roleLiveData;
    }
    public void addItem(ItemModel item) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Items");
        String id = ref.push().getKey(); // Tạo ID mới
        item.setId(id);
        ref.child(id).setValue(item);
    }
    public void updateItem(ItemModel item) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Items");
        ref.child(item.getId()).setValue(item);
    }
    public void deleteItem(String itemId, OnCompleteListener<Void> onCompleteListener) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Items");
        ref.child(itemId).removeValue().addOnCompleteListener(onCompleteListener);
    }

    public LiveData<String> getChatRoomIdByUserId(String userId) {
        MutableLiveData<String> roomIdLiveData = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("ChatRooms");

        ref.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        // Trả về room ID đầu tiên tìm thấy
                        roomIdLiveData.setValue(child.getKey());
                        return;
                    }
                } else {
                    roomIdLiveData.setValue(null); // Không tìm thấy chat room cho user này
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                roomIdLiveData.setValue(null);
            }
        });

        return roomIdLiveData;
    }

    // Tạo chat room mới
    public LiveData<String> createChatRoom(String userId, String userName) {
        MutableLiveData<String> result = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("ChatRooms");

        // Kiểm tra xem user đã có chat room chưa
        ref.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Chat room đã tồn tại
                    for (DataSnapshot child : snapshot.getChildren()) {
                        result.setValue(child.getKey());
                        return;
                    }
                } else {
                    // Tạo chat room mới
                    String roomId = ref.push().getKey();
                    if (roomId != null) {
                        ChatRoom chatRoom = new ChatRoom(userId, userName);
                        chatRoom.setId(roomId);

                        ref.child(roomId).setValue(chatRoom).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                result.setValue(roomId);
                            } else {
                                result.setValue(null);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                result.setValue(null);
            }
        });

        return result;
    }

    // Gửi tin nhắn
    public LiveData<Boolean> sendMessage(String roomId, ChatMessage message) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        DatabaseReference messagesRef = firebaseDatabase.getReference("ChatMessages").child(roomId);
        String messageId = messagesRef.push().getKey();

        if (messageId != null) {
            message.setId(messageId);

            messagesRef.child(messageId).setValue(message).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Cập nhật last message trong chat room
                    updateChatRoomLastMessage(roomId, message.getMessage(), message.getTimestamp());
                    result.setValue(true);
                } else {
                    result.setValue(false);
                }
            });
        } else {
            result.setValue(false);
        }

        return result;
    }

    // Cập nhật tin nhắn cuối trong chat room
    private void updateChatRoomLastMessage(String roomId, String lastMessage, long timestamp) {
        DatabaseReference roomRef = firebaseDatabase.getReference("ChatRooms").child(roomId);
        roomRef.child("lastMessage").setValue(lastMessage);
        roomRef.child("lastMessageTime").setValue(timestamp);
    }

    // Lấy danh sách tin nhắn trong room
    public LiveData<ArrayList<ChatMessage>> getChatMessages(String roomId) {
        MutableLiveData<ArrayList<ChatMessage>> messagesLiveData = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("ChatMessages").child(roomId);

        ref.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ChatMessage> messages = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    ChatMessage message = child.getValue(ChatMessage.class);
                    if (message != null) {
                        message.setId(child.getKey());
                        messages.add(message);
                    }
                }
                messagesLiveData.setValue(messages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                messagesLiveData.setValue(new ArrayList<>());
            }
        });

        return messagesLiveData;
    }

    // Lấy danh sách chat rooms cho admin
    public LiveData<ArrayList<ChatRoom>> getChatRoomsForAdmin() {
        MutableLiveData<ArrayList<ChatRoom>> roomsLiveData = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("ChatRooms");

        ref.orderByChild("lastMessageTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ChatRoom> rooms = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    ChatRoom room = child.getValue(ChatRoom.class);
                    if (room != null) {
                        room.setId(child.getKey());
                        rooms.add(0, room); // Thêm vào đầu list (tin nhắn mới nhất)
                    }
                }
                roomsLiveData.setValue(rooms);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                roomsLiveData.setValue(new ArrayList<>());
            }
        });

        return roomsLiveData;
    }

    // Lấy chat room của user
    public LiveData<String> getUserChatRoom(String userId) {
        MutableLiveData<String> roomIdLiveData = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("ChatRooms");

        ref.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        roomIdLiveData.setValue(child.getKey());
                        return;
                    }
                } else {
                    roomIdLiveData.setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                roomIdLiveData.setValue(null);
            }
        });

        return roomIdLiveData;
    }

    // Đánh dấu tin nhắn đã đọc
    public void markMessagesAsRead(String roomId, String currentUserId) {
        DatabaseReference ref = firebaseDatabase.getReference("ChatMessages").child(roomId);

        ref.orderByChild("receiverId").equalTo(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    child.getRef().child("read").setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

}
