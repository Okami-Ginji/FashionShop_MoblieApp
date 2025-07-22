package com.example.fashionshop.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fashionshop.Domain.BannerModel;
import com.example.fashionshop.Domain.CategoryModel;
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

}
