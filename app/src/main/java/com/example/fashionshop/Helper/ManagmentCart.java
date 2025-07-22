package com.example.fashionshop.Helper;

import android.widget.Toast;

import com.example.fashionshop.Domain.ItemModel;
import android.content.Context;

import java.util.ArrayList;

public class ManagmentCart {

    private Context context;
    private TinyDB tinyDB;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertItem(ItemModel item) {
        if (item.getId() == null || item.getId().isEmpty()) {
            Toast.makeText(context, "Sản phẩm thiếu ID, không thể thêm vào giỏ", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<ItemModel> listItem = getListCart();
        boolean existAlready = false;
        int n = 0;
        for (int y = 0; y < listItem.size(); y++) {
            String existingId = listItem.get(y).getId();
            if (existingId != null && existingId.equals(item.getId())) {
                existAlready = true;
                n = y;
                break;
            }
        }
        if (existAlready) {
            listItem.get(n).setNumberinCart(item.getNumberinCart());
        } else {
            listItem.add(item);
        }
        tinyDB.putListObject("CartList", listItem);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }



    public ArrayList<ItemModel> getListCart() {
        return tinyDB.getListObject("CartList");
    }

    public void minusItem(ArrayList<ItemModel> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (listItem.get(position).getNumberinCart() == 1) {
            listItem.remove(position);
        } else {
            listItem.get(position).setNumberinCart(listItem.get(position).getNumberinCart() - 1);
        }
        tinyDB.putListObject("CartList", listItem);
        changeNumberItemsListener.changed();
    }

    public void plusItem(ArrayList<ItemModel> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        listItem.get(position).setNumberinCart(listItem.get(position).getNumberinCart() + 1);
        tinyDB.putListObject("CartList", listItem);
        changeNumberItemsListener.changed();
    }

    public Double getTotalFee() {
        ArrayList<ItemModel> listItem2 = getListCart();
        double fee = 0;
        for (int i = 0; i < listItem2.size(); i++) {
            fee = fee + (listItem2.get(i).getPrice() * listItem2.get(i).getNumberinCart());
        }
        return fee;
    }
    public void clearCart() {
        tinyDB.remove("CartList");
    }
}
