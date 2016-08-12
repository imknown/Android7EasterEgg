package com.android.egg.neko;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

public class NekoLockedActivity extends Activity implements OnDismissListener {
    private NekoDialog mDialog;

    public void onCreate(@Nullable Bundle var1) {
        super.onCreate(var1);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        this.mDialog = new NekoDialog(this);
        this.mDialog.setOnDismissListener(this);
        this.mDialog.show();
    }

    public void onDismiss(DialogInterface var1) {
        this.finish();
    }
}
