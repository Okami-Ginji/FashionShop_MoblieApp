package com.example.fashionshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fashionshop.databinding.ViewholderPiclistBinding;
import com.example.fashionshop.databinding.ViewholderPopularBinding;

import java.util.List;

public class picListAdapter extends RecyclerView.Adapter<picListAdapter.ViewHolder> {
    private List<String> items;
    private ImageView picMain;
    private Context context;
    public picListAdapter(List<String> items,ImageView picMain){
        this.items = items;
        this.picMain = picMain;
    }
    @NonNull
    @Override
    public picListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context =  parent.getContext();
        ViewholderPiclistBinding binding = ViewholderPiclistBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull picListAdapter.ViewHolder holder, int position) {
        Glide.with(context)
                .load(items.get(position))
                .into(holder.binding.pic);
        holder.binding.getRoot().setOnClickListener((View.OnClickListener) v -> Glide.with(context)
                .load(items.get(position))
                .into(picMain));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderPiclistBinding binding;
        public ViewHolder(ViewholderPiclistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
