package com.android.egg.neko;

import android.content.Intent;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

public class NekoTile extends TileService implements PrefState.PrefsListener {
    private PrefState mPrefs;

    private void showNekoDialog() {
        Log.d("NekoTile", "showNekoDialog");
        this.showDialog(new NekoDialog(this));
    }

    private void updateState() {
        Tile var1 = this.getQsTile();
        int var2 = this.mPrefs.getFoodState();
        Food var3 = new Food(var2);
        var1.setIcon(var3.getIcon(this));
        var1.setLabel(var3.getName(this));
        byte var4;
        if (var2 != 0) {
            var4 = 2;
        } else {
            var4 = 1;
        }

        var1.setState(var4);
        var1.updateTile();
    }

    public void onClick() {
        if (this.mPrefs.getFoodState() != 0) {
            this.mPrefs.setFoodState(0);
            NekoService.cancelJob(this);
        } else if (this.isLocked()) {
            if (this.isSecure()) {
                Log.d("NekoTile", "startActivityAndCollapse");
                Intent var2 = new Intent(this, NekoLockedActivity.class);
                var2.addFlags(268435456);
                this.startActivityAndCollapse(var2);
            } else {
                this.unlockAndRun(new Runnable() {
                    public void run() {
                        NekoTile.this.showNekoDialog();
                    }
                });
            }
        } else {
            this.showNekoDialog();
        }
    }

    public void onCreate() {
        super.onCreate();
        this.mPrefs = new PrefState(this);
    }

    public void onPrefsChanged() {
        this.updateState();
    }

    public void onStartListening() {
        super.onStartListening();
        this.mPrefs.setListener(this);
        this.updateState();
    }

    public void onStopListening() {
        super.onStopListening();
        this.mPrefs.setListener((PrefState.PrefsListener) null);
    }
}
