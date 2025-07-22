package com.example.fashionshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionshop.R;
import com.example.fashionshop.databinding.ViewholderSizeBinding;

import java.util.ArrayList;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.ViewHolder> {
    ArrayList<String> items;
    Context context;
    int SelectedPosition = -1;
    int LastSelectedPosition = -1;

    public SizeAdapter(ArrayList<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public SizeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderSizeBinding binding = ViewholderSizeBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeAdapter.ViewHolder holder, int position) {
        holder.binding.sizeTxt.setText(items.get(position));

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LastSelectedPosition = SelectedPosition;
                SelectedPosition = holder.getAdapterPosition();
                notifyItemChanged(LastSelectedPosition);
                notifyItemChanged(SelectedPosition);
            }
        });

        if (SelectedPosition == holder.getAdapterPosition()) {
            holder.binding.sizeLayout.setBackgroundResource(R.drawable.size_selected);
            holder.binding.sizeTxt.setTextColor(ContextCompat.getColor(context, R.color.purple));
        } else {
            holder.binding.sizeLayout.setBackgroundResource(R.drawable.size_unselected);
            holder.binding.sizeTxt.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderSizeBinding binding;

        public ViewHolder(@NonNull ViewholderSizeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
