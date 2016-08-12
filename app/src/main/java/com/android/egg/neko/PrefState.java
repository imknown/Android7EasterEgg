package com.android.egg.neko;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PrefState implements OnSharedPreferenceChangeListener {
    private final Context mContext;
    private PrefsListener mListener;
    private final SharedPreferences mPrefs;

    public PrefState(Context var1) {
        this.mContext = var1;
        this.mPrefs = this.mContext.getSharedPreferences("mPrefs", 0);
    }

    public void addCat(Cat var1) {
        this.mPrefs.edit().putString("cat:" + String.valueOf(var1.getSeed()), var1.getName()).commit();
    }

    public List<Cat> getCats() {
        ArrayList var1 = new ArrayList();
        Map var2 = this.mPrefs.getAll();
        Iterator var3 = var2.keySet().iterator();

        while (var3.hasNext()) {
            String var4 = (String) var3.next();
            if (var4.startsWith("cat:")) {
                long var5 = Long.parseLong(var4.substring("cat:".length()));
                Cat var7 = new Cat(this.mContext, var5);
                var7.setName(String.valueOf(var2.get(var4)));
                var1.add(var7);
            }
        }

        return var1;
    }

    public int getFoodState() {
        return this.mPrefs.getInt("food", 0);
    }

    public void onSharedPreferenceChanged(SharedPreferences var1, String var2) {
        this.mListener.onPrefsChanged();
    }

    public void removeCat(Cat var1) {
        this.mPrefs.edit().remove("cat:" + String.valueOf(var1.getSeed())).commit();
    }

    public void setFoodState(int var1) {
        this.mPrefs.edit().putInt("food", var1).commit();
    }

    public void setListener(PrefsListener var1) {
        this.mListener = var1;
        if (this.mListener != null) {
            this.mPrefs.registerOnSharedPreferenceChangeListener(this);
        } else {
            this.mPrefs.unregisterOnSharedPreferenceChangeListener(this);
        }
    }

    public interface PrefsListener {
        void onPrefsChanged();
    }
}
