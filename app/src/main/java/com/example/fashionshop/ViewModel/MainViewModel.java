package com.example.fashionshop.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.fashionshop.Domain.BannerModel;
import com.example.fashionshop.Domain.CategoryModel;
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

    public LiveData<Boolean> registerUser(UserModel user) {
        return repository.registerUser(user);
    }

    public LiveData<UserModel> getUserById(String userId) {
        return repository.getUserById(userId);
    }

    public LiveData<String> getRole(String userId) {
        return repository.getRole(userId);
    }
}
