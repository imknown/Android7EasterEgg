package com.android.egg.neko;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

public class NekoActivationActivity extends Activity {
    private void toastUp(String var1) {
        Toast var2 = Toast.makeText(this, var1, Toast.LENGTH_SHORT);
        var2.getView().setBackgroundDrawable((Drawable) null);
        var2.show();
    }

    public void onStart() {
        super.onStart();
        PackageManager var1 = this.getPackageManager();
        ComponentName var2 = new ComponentName(this, NekoTile.class);
        if (var1.getComponentEnabledSetting(var2) == 1) {
            if (NekoLand.DEBUG) {
                Log.v("Neko", "Disabling tile.");
            }

            var1.setComponentEnabledSetting(var2, 2, 1);
            this.toastUp("\ud83d\udeab");
        } else {
            if (NekoLand.DEBUG) {
                Log.v("Neko", "Enabling tile.");
            }

            var1.setComponentEnabledSetting(var2, 1, 1);
            this.toastUp("\ud83d\udc31");
        }

        this.finish();
    }
}
