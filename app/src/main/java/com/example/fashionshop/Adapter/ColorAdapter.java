package com.example.fashionshop.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionshop.R;
import com.example.fashionshop.databinding.ViewholderColorBinding;

import java.util.ArrayList;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.Viewholder> {
    ArrayList<String> items;
    Context context;
    int SelectedPosition = -1;
    int LastSelectedPosition = -1;

    public ColorAdapter(ArrayList<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ColorAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderColorBinding binding = ViewholderColorBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

        @Override
        public void onBindViewHolder(@NonNull ColorAdapter.Viewholder holder, int position) {
            // Bắt sự kiện click item màu
            holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LastSelectedPosition = SelectedPosition;
                    SelectedPosition = holder.getAdapterPosition();
                    notifyItemChanged(LastSelectedPosition); // cập nhật item cũ
                    notifyItemChanged(SelectedPosition);     // cập nhật item mới được chọn
                }
            });

            // Nếu item đang được chọn
            if (SelectedPosition == holder.getAdapterPosition()) {
                // Gán hình nền "được chọn"
                holder.binding.colorLayout.setImageResource(R.drawable.color_selected);
            } else {
                // Gán màu tương ứng với item bằng cách tint drawable
                Drawable baseDrawable = AppCompatResources.getDrawable(context, R.drawable.color_selected);
                if (baseDrawable != null) {
                    Drawable wrappedDrawable = DrawableCompat.wrap(baseDrawable.mutate());
                    DrawableCompat.setTint(wrappedDrawable, Color.parseColor(items.get(position)));
                    holder.binding.colorLayout.setImageDrawable(wrappedDrawable);
                }
            }
        }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderColorBinding binding;

        public Viewholder(@NonNull ViewholderColorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
