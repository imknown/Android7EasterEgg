package com.android.egg.neko;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.egg.android7decompiled.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class NekoLand extends Activity implements PrefState.PrefsListener {
    private static boolean CAT_GEN = false;
    public static boolean DEBUG = false;
    public static boolean DEBUG_NOTIFICATIONS = false;
    private CatAdapter mAdapter;
    private Cat mPendingShareCat;
    private PrefState mPrefs;

    private void onCatClick(Cat var1) {
        if (CAT_GEN) {
            this.mPrefs.addCat(var1);
            (new Builder(this)).setTitle("Cat added").setPositiveButton(android.R.string.ok, (OnClickListener) null).show();
        } else {
            this.showNameDialog(var1);
        }
    }

    private void onCatRemove(Cat var1) {
        this.mPrefs.removeCat(var1);
    }

    private void shareCat(Cat var1) {
        File var2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), this.getString(R.string.directory_name));
        if (!var2.exists() && !var2.mkdirs()) {
            Log.e("NekoLand", "save: error: can\'t create Pictures directory");
        } else {
            File var3 = new File(var2, var1.getName().replaceAll("[/ #:]+", "_") + ".png");
            Bitmap var4 = var1.createBitmap(512, 512);
            if (var4 != null) {
                try {
                    FileOutputStream var5 = new FileOutputStream(var3);
                    var4.compress(CompressFormat.PNG, 0, var5);
                    var5.close();
                    String[] var9 = new String[]{var3.toString()};
                    MediaScannerConnection.scanFile(this, var9, new String[]{"image/png"}, (OnScanCompletedListener) null);
                    Uri var10 = Uri.fromFile(var3);
                    Intent var11 = new Intent("android.intent.action.SEND");
                    var11.putExtra("android.intent.extra.STREAM", var10);
                    var11.putExtra("android.intent.extra.SUBJECT", var1.getName());
                    var11.setType("image/png");
                    this.startActivity(Intent.createChooser(var11, (CharSequence) null));
                } catch (IOException var12) {
                    Log.e("NekoLand", "save: error: " + var12);
                    return;
                }
            }

        }
    }

    private void showNameDialog(final Cat var1) {
        ContextThemeWrapper var2 = new ContextThemeWrapper(this, android.R.style.Theme_Material_Light_Dialog_NoActionBar);// Theme.Material.Light.Dialog.NoActionBar
        View var3 = LayoutInflater.from(var2).inflate(R.layout.edit_text, (ViewGroup) null);
        final EditText var4 = (EditText) var3.findViewById(android.R.id.edit);
        var4.setText(var1.getName());
        var4.setSelection(var1.getName().length());
        Drawable var5 = var1.createLargeIcon(this).loadDrawable(this);
        (new Builder(var2)).setTitle(" ").setIcon(var5).setView(var3).setPositiveButton(android.R.string.ok, new OnClickListener() {
            public void onClick(DialogInterface var1x, int var2) {
                var1.setName(var4.getText().toString().trim());
                NekoLand.this.mPrefs.addCat(var1);
            }
        }).show();
    }

    private void updateCats() {
        Cat[] var1;
        if (CAT_GEN) {
            var1 = new Cat[50];

            for (int var2 = 0; var2 < var1.length; ++var2) {
                var1[var2] = Cat.create(this);
            }
        } else {
            var1 = (Cat[]) this.mPrefs.getCats().toArray(new Cat[0]);
        }

        this.mAdapter.setCats(var1);
    }

    public void onCreate(Bundle var1) {
        super.onCreate(var1);
        this.setContentView(R.layout.neko_activity);
        ActionBar var2 = this.getActionBar();
        if (var2 != null) {
            var2.setLogo(Cat.create(this));
            var2.setDisplayUseLogoEnabled(false);
            var2.setDisplayShowHomeEnabled(true);
        }

        this.mPrefs = new PrefState(this);
        this.mPrefs.setListener(this);
        RecyclerView var3 = (RecyclerView) this.findViewById(R.id.holder);
        this.mAdapter = new CatAdapter((CatAdapter) null);
        var3.setAdapter(this.mAdapter);
        var3.setLayoutManager(new GridLayoutManager(this, 3));
        this.updateCats();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mPrefs.setListener((PrefState.PrefsListener) null);
    }

    public void onPrefsChanged() {
        this.updateCats();
    }

    public void onRequestPermissionsResult(int var1, String[] var2, int[] var3) {
        if (var1 == 123 && this.mPendingShareCat != null) {
            this.shareCat(this.mPendingShareCat);
            this.mPendingShareCat = null;
        }

    }

    private class CatAdapter extends RecyclerView.Adapter<CatHolder> {
        private Cat[] mCats;

        private CatAdapter() {
        }

        // $FF: synthetic method
        CatAdapter(CatAdapter var2) {
            this();
        }

        public int getItemCount() {
            return this.mCats.length;
        }

        public void onBindViewHolder(final CatHolder var1, int var2) {
            Context var3 = var1.itemView.getContext();
            var1.imageView.setImageIcon(this.mCats[var2].createLargeIcon(var3));
            var1.textView.setText(this.mCats[var2].getName());
            var1.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View var1x) {
                    NekoLand.this.onCatClick(CatAdapter.this.mCats[var1.getAdapterPosition()]);
                }
            });
            var1.itemView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View var1x) {
                    var1.contextGroup.removeCallbacks((Runnable) var1.contextGroup.getTag());
                    var1.contextGroup.setVisibility(View.VISIBLE);
                    Runnable var3 = new Runnable() {
                        public void run() {
                            var1.contextGroup.setVisibility(View.INVISIBLE);
                        }
                    };
                    var1.contextGroup.setTag(var3);
                    var1.contextGroup.postDelayed(var3, 5000L);
                    return true;
                }
            });
            var1.delete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View var1x) {
                    var1.contextGroup.setVisibility(View.INVISIBLE);
                    var1.contextGroup.removeCallbacks((Runnable) var1.contextGroup.getTag());
                    NekoLand.this.onCatRemove(CatAdapter.this.mCats[var1.getAdapterPosition()]);
                }
            });
            var1.share.setOnClickListener(new View.OnClickListener() {
                public void onClick(View var1x) {
                    Cat var2 = CatAdapter.this.mCats[var1.getAdapterPosition()];
                    if (NekoLand.this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                        NekoLand.this.mPendingShareCat = var2;
                        NekoLand.this.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 123);
                    } else {
                        NekoLand.this.shareCat(var2);
                    }
                }
            });
        }

        public CatHolder onCreateViewHolder(ViewGroup var1, int var2) {
            return new CatHolder(LayoutInflater.from(var1.getContext()).inflate(R.layout.cat_view, var1, false));
        }

        public void setCats(Cat[] var1) {
            this.mCats = var1;
            this.notifyDataSetChanged();
        }
    }

    private static class CatHolder extends RecyclerView.ViewHolder {
        private final View contextGroup;
        private final View delete;
        private final ImageView imageView;
        private final View share;
        private final TextView textView;

        public CatHolder(View var1) {
            super(var1);
            this.imageView = (ImageView) var1.findViewById(android.R.id.icon);
            this.textView = (TextView) var1.findViewById(android.R.id.title);
            this.contextGroup = var1.findViewById(R.id.contextGroup);
            this.delete = var1.findViewById(android.R.id.closeButton);
            this.share = var1.findViewById(android.R.id.shareText);
        }
    }
}
