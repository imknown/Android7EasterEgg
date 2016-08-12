package com.android.egg.neko;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Icon;

import com.android.egg.android7decompiled.R;

public class Food {
    private static int[] sIcons;
    private static String[] sNames;
    private final int mType;

    public Food(int var1) {
        this.mType = var1;
    }

    public Icon getIcon(Context var1) {
        if (sIcons == null) {
            TypedArray var2 = var1.getResources().obtainTypedArray(R.array.food_icons);
            sIcons = new int[var2.length()];

            for (int var3 = 0; var3 < sIcons.length; ++var3) {
                sIcons[var3] = var2.getResourceId(var3, 0);
            }

            var2.recycle();
        }

        return Icon.createWithResource(var1, sIcons[this.mType]);
    }

    public long getInterval(Context var1) {
        return (long) var1.getResources().getIntArray(R.array.food_intervals)[this.mType];
    }

    public String getName(Context var1) {
        if (sNames == null) {
            sNames = var1.getResources().getStringArray(R.array.food_names);
        }

        return sNames[this.mType];
    }

    public int getType() {
        return this.mType;
    }
}
