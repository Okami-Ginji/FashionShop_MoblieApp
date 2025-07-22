package com.example.fashionshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.request.RequestOptions;
import com.example.fashionshop.Activity.DetailActivity;
import com.example.fashionshop.Domain.ItemModel;
import com.example.fashionshop.databinding.ViewholderPopularBinding;

import java.util.ArrayList;

public class PoppularAdapter extends RecyclerView.Adapter<PoppularAdapter.ViewHolder> implements Filterable {
    private ArrayList<ItemModel> items;
    private ArrayList<ItemModel> itemListFull;
    private Context context;

    public PoppularAdapter(ArrayList<ItemModel> itemList) {
        this.items = itemList;
        itemListFull = new ArrayList<>(itemList);
    }

    @NonNull
    @Override
    public PoppularAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderPopularBinding binding =  ViewholderPopularBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PoppularAdapter.ViewHolder holder, int position) {
        ItemModel currentItem = items.get(position);

        holder.binding.titleTxt.setText(currentItem.getTitle());
        holder.binding.priceTxt.setText("$" + currentItem.getPrice());
        holder.binding.ratingTxt.setText("(" + currentItem.getRating() + ")");
        holder.binding.offPercentTxt.setText(currentItem.getOffPercent() + "Off");
        holder.binding.oldPriceTxt.setText("$" + currentItem.getOldPrice());
        holder.binding.oldPriceTxt.setPaintFlags(holder.binding.oldPriceTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        Glide.with(context)
                .load(currentItem.getPicUrl().get(0))
                .apply(new RequestOptions().transform(new CenterInside()))
                .into(holder.binding.pic);

        holder.itemView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("Object", items.get(currentPosition));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ItemModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(itemListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ItemModel item : itemListFull) {
                    String title = item.getTitle();
                    if (title != null && title.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items.clear();
            if (results.values != null) {
                items.addAll((ArrayList<ItemModel>) results.values);
            }
            notifyDataSetChanged();
        }
    };




    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewholderPopularBinding binding;
        public ViewHolder(ViewholderPopularBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void updateList(ArrayList<ItemModel> newList) {
        items.clear();
        items.addAll(newList);
        itemListFull.clear();
        itemListFull.addAll(newList);
        notifyDataSetChanged();
    }

}
