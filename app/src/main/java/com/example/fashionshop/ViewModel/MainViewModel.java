package com.example.fashionshop.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.fashionshop.Domain.BannerModel;
import com.example.fashionshop.Domain.CategoryModel;
import com.example.fashionshop.Domain.ChatMessage;
import com.example.fashionshop.Domain.ChatRoom;
import com.example.fashionshop.Domain.ItemModel;
import com.example.fashionshop.Domain.UserModel;
import com.example.fashionshop.Repository.MainRepository;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    private final MainRepository repository = new MainRepository();
    public LiveData<ArrayList<CategoryModel>> loadCategory(){
        return repository.loadCategory();
    }

    public LiveData<ArrayList<BannerModel>> loadBanner(){
        return repository.loadBanner();
    }

    public LiveData<ArrayList<ItemModel>> loadPopular(){
        return  repository.loadPopular();
    }

    public LiveData<UserModel> login(String email, String password) {
        return repository.login(email, password);
    }

    public LiveData<Boolean> checkEmailExists(String email) {
        return repository.checkEmailExists(email);
    }

    public LiveData<String> getUserIdByEmail(String email) {
        return repository.getUserIdByEmail(email);
    }

    public LiveData<Boolean> registerUser(UserModel user) {
        return repository.registerUser(user);
    }

    public LiveData<UserModel> getUserById(String userId) {
        return repository.getUserById(userId);
    }

    public LiveData<String> getRole(String userId) {
        return repository.getRole(userId);
    }

    // CHAT METHODS
    public LiveData<String> getChatRoomIdByUserId(String userId) {
        return repository.getChatRoomIdByUserId(userId);
    }

    public LiveData<String> createChatRoom(String userId, String userName) {
        return repository.createChatRoom(userId, userName);
    }

    public LiveData<Boolean> sendMessage(String roomId, ChatMessage message) {
        return repository.sendMessage(roomId, message);
    }

    public LiveData<ArrayList<ChatMessage>> getChatMessages(String roomId) {
        return repository.getChatMessages(roomId);
    }

    public LiveData<ArrayList<ChatRoom>> getChatRoomsForAdmin() {
        return repository.getChatRoomsForAdmin();
    }

    public LiveData<String> getUserChatRoom(String userId) {
        return repository.getUserChatRoom(userId);
    }

    public void markMessagesAsRead(String roomId, String currentUserId) {
        repository.markMessagesAsRead(roomId, currentUserId);
    }
}
