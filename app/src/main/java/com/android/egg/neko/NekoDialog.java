package com.android.egg.neko;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.egg.android7decompiled.R;

import java.util.ArrayList;

public class NekoDialog extends Dialog {
    private final Adapter mAdapter;

    public NekoDialog(@NonNull Context var1) {
        super(var1, android.R.style.Theme_Material_Dialog_NoActionBar);// Theme.Material.Dialog.NoActionBar
        RecyclerView var2 = new RecyclerView(this.getContext());
        this.mAdapter = new Adapter(this.getContext());
        var2.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        var2.setAdapter(this.mAdapter);
        int var3 = (int) (16.0F * var1.getResources().getDisplayMetrics().density);
        var2.setPadding(var3, var3, var3, var3);
        this.setContentView(var2);
    }

    private void onFoodSelected(Food var1) {
        PrefState var2 = new PrefState(this.getContext());
        if (var2.getFoodState() == 0 && var1.getType() != 0) {
            NekoService.registerJob(this.getContext(), var1.getInterval(this.getContext()));
        }

        var2.setFoodState(var1.getType());
        this.dismiss();
    }

    private class Adapter extends RecyclerView.Adapter<Holder> {
        private final Context mContext;
        private final ArrayList<Food> mFoods = new ArrayList();

        public Adapter(Context var2) {
            this.mContext = var2;
            int[] var3 = var2.getResources().getIntArray(R.array.food_names);

            for (int var4 = 1; var4 < var3.length; ++var4) {
                this.mFoods.add(new Food(var4));
            }

        }

        public int getItemCount() {
            return this.mFoods.size();
        }

        public void onBindViewHolder(final Holder var1, int var2) {
            Food var3 = (Food) this.mFoods.get(var2);
            ((ImageView) var1.itemView.findViewById(R.id.icon)).setImageIcon(var3.getIcon(this.mContext));
            ((TextView) var1.itemView.findViewById(R.id.text)).setText(var3.getName(this.mContext));
            var1.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View var1x) {
                    NekoDialog.this.onFoodSelected((Food) Adapter.this.mFoods.get(var1.getAdapterPosition()));
                }
            });
        }

        public Holder onCreateViewHolder(ViewGroup var1, int var2) {
            return new Holder(LayoutInflater.from(var1.getContext()).inflate(R.layout.food_layout, var1, false));
        }
    }

    public static class Holder extends RecyclerView.ViewHolder {
        public Holder(View var1) {
            super(var1);
        }
    }
}
