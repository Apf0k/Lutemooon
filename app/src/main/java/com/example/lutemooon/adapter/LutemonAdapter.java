package com.example.lutemooon.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lutemooon.R;
import com.example.lutemooon.model.Lutemon;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LutemonAdapter extends RecyclerView.Adapter<LutemonAdapter.LutemonViewHolder> {
    private final List<Lutemon> lutemons;
    private final OnLutemonClickListener listener;
    private final Set<Lutemon> selectedLutemons = new HashSet<>();

    public interface OnLutemonClickListener {
        void onLutemonClick(int position, boolean isSelected);
    }

    public LutemonAdapter(List<Lutemon> lutemons, OnLutemonClickListener listener) {
        this.lutemons = lutemons;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LutemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lutemon, parent, false);
        return new LutemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LutemonViewHolder holder, int position) {
        Lutemon lutemon = lutemons.get(position);
        holder.tvName.setText(lutemon.getName());
        holder.tvColor.setText("Color: " + lutemon.getColor());
        holder.tvStats.setText(String.format("ATK: %d DEF: %d HP: %d/%d",
                lutemon.getAttack(), lutemon.getDefense(),
                lutemon.getCurrentHealth(), lutemon.getMaxHealth()));
        holder.tvExperience.setText("EXP: " + lutemon.getExperience());
        
        // 设置选择状态
        holder.cbSelect.setChecked(selectedLutemons.contains(lutemon));
        
        // 根据颜色设置图片
        switch (lutemon.getColor()) {
            case "White":
                holder.ivLutemon.setImageResource(R.drawable.lutemon_white);
                break;
            case "Green":
                holder.ivLutemon.setImageResource(R.drawable.lutemon_green);
                break;
            case "Pink":
                holder.ivLutemon.setImageResource(R.drawable.lutemon_pink);
                break;
            case "Orange":
                holder.ivLutemon.setImageResource(R.drawable.lutemon_orange);
                break;
            case "Black":
                holder.ivLutemon.setImageResource(R.drawable.lutemon_black);
                break;
        }
        
        // 设置点击事件
        View.OnClickListener clickListener = v -> {
            boolean newState = !selectedLutemons.contains(lutemon);
            if (newState) {
                selectedLutemons.clear(); // 只允许选择一个
                selectedLutemons.add(lutemon);
            } else {
                selectedLutemons.remove(lutemon);
            }
            notifyDataSetChanged();
            listener.onLutemonClick(position, newState);
        };
        
        holder.itemView.setOnClickListener(clickListener);
        holder.cbSelect.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return lutemons.size();
    }

    public void clearSelection() {
        selectedLutemons.clear();
        notifyDataSetChanged();
    }

    static class LutemonViewHolder extends RecyclerView.ViewHolder {
        ImageView ivLutemon;
        TextView tvName;
        TextView tvColor;
        TextView tvStats;
        TextView tvExperience;
        CheckBox cbSelect;

        LutemonViewHolder(View itemView) {
            super(itemView);
            ivLutemon = itemView.findViewById(R.id.ivLutemon);
            tvName = itemView.findViewById(R.id.tvName);
            tvColor = itemView.findViewById(R.id.tvColor);
            tvStats = itemView.findViewById(R.id.tvStats);
            tvExperience = itemView.findViewById(R.id.tvExperience);
            cbSelect = itemView.findViewById(R.id.cbSelect);
        }
    }
} 