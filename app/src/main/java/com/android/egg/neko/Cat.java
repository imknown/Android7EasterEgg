package com.android.egg.neko;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;

import com.android.egg.android7decompiled.R;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Cat extends Drawable {
    public static final long[] PURR = new long[]{0L, 40L, 20L, 40L, 20L, 40L, 20L, 40L, 20L, 40L, 20L, 40L};
    public static final int[] P_BELLY_COLORS = new int[]{750, 0, 250, -1};
    public static final int[] P_BODY_COLORS = new int[]{180, -14606047, 180, -1, 140, -10395295, 140, -8825528, 100, -7297874, 100, -1596, 100, -28928, 5, -14043402, 5, -12846, 5, -3238952, 4, -12345273, 1, 0};
    public static final int[] P_COLLAR_COLORS = new int[]{250, -1, 250, -16777216, 250, -769226, 50, -15108398, 50, -141259, 50, -291840, 50, -749647, 50, -11751600};
    public static final int[] P_DARK_SPOT_COLORS = new int[]{700, 0, 250, -14606047, 50, -9614271};
    public static final int[] P_LIGHT_SPOT_COLORS = new int[]{700, 0, 300, -1};
    private CatParts D;
    private Bitmap mBitmap;
    private int mBodyColor;
    private String mName;
    private Random mNotSoRandom;
    private long mSeed;

    public Cat(Context var1, long var2) {
        this.D = new CatParts(var1);
        this.mSeed = var2;
        Object[] var4 = new Object[]{String.valueOf(this.mSeed).substring(0, 3)};
        this.setName(var1.getString(R.string.default_cat_name, var4));
        Random var5 = this.notSoRandom(var2);
        this.mBodyColor = chooseP(var5, P_BODY_COLORS);
        if (this.mBodyColor == 0) {
            float[] var32 = new float[]{360.0F * var5.nextFloat(), frandrange(var5, 0.5F, 1.0F), frandrange(var5, 0.5F, 1.0F)};
            this.mBodyColor = Color.HSVToColor(var32);
        }

        int var6 = this.mBodyColor;
        Drawable[] var7 = new Drawable[]{this.D.body, this.D.head, this.D.leg1, this.D.leg2, this.D.leg3, this.D.leg4, this.D.tail, this.D.leftEar, this.D.rightEar, this.D.foot1, this.D.foot2, this.D.foot3, this.D.foot4, this.D.tailCap};
        tint(var6, var7);
        Drawable[] var8 = new Drawable[]{this.D.leg2Shadow, this.D.tailShadow};
        tint(536870912, var8);
        if (isDark(this.mBodyColor)) {
            Drawable[] var31 = new Drawable[]{this.D.leftEye, this.D.rightEye, this.D.mouth, this.D.nose};
            tint(-1, var31);
        }

        int var9;
        if (isDark(this.mBodyColor)) {
            var9 = -1074534;
        } else {
            var9 = 550830080;
        }

        Drawable[] var10 = new Drawable[]{this.D.leftEarInside, this.D.rightEarInside};
        tint(var9, var10);
        int var11 = chooseP(var5, P_BELLY_COLORS);
        Drawable[] var12 = new Drawable[]{this.D.belly};
        tint(var11, var12);
        int var13 = chooseP(var5, P_BELLY_COLORS);
        Drawable[] var14 = new Drawable[]{this.D.back};
        tint(var13, var14);
        int var15 = chooseP(var5, P_BELLY_COLORS);
        Drawable[] var16 = new Drawable[]{this.D.faceSpot};
        tint(var15, var16);
        if (!isDark(var15)) {
            Drawable[] var30 = new Drawable[]{this.D.mouth, this.D.nose};
            tint(-16777216, var30);
        }

        if (var5.nextFloat() < 0.25F) {
            Drawable[] var29 = new Drawable[]{this.D.foot1, this.D.foot2, this.D.foot3, this.D.foot4};
            tint(-1, var29);
        } else if (var5.nextFloat() < 0.25F) {
            Drawable[] var28 = new Drawable[]{this.D.foot1, this.D.foot2};
            tint(-1, var28);
        } else if (var5.nextFloat() < 0.25F) {
            Drawable[] var27 = new Drawable[]{this.D.foot3, this.D.foot4};
            tint(-1, var27);
        } else if (var5.nextFloat() < 0.1F) {
            Drawable[] var17 = new Drawable[1];
            Object[] var18 = new Object[]{this.D.foot1, this.D.foot2, this.D.foot3, this.D.foot4};
            var17[0] = (Drawable) choose(var5, var18);
            tint(-1, var17);
        }

        int var19;
        if (var5.nextFloat() < 0.333F) {
            var19 = -1;
        } else {
            var19 = this.mBodyColor;
        }

        Drawable[] var20 = new Drawable[]{this.D.tailCap};
        tint(var19, var20);
        int[] var21;
        if (isDark(this.mBodyColor)) {
            var21 = P_LIGHT_SPOT_COLORS;
        } else {
            var21 = P_DARK_SPOT_COLORS;
        }

        int var22 = chooseP(var5, var21);
        Drawable[] var23 = new Drawable[]{this.D.cap};
        tint(var22, var23);
        int var24 = chooseP(var5, P_COLLAR_COLORS);
        Drawable[] var25 = new Drawable[]{this.D.collar};
        tint(var24, var25);
        if (var5.nextFloat() >= 0.1F) {
            var24 = 0;
        }

        Drawable[] var26 = new Drawable[]{this.D.bowtie};
        tint(var24, var26);
    }

    public static final Object choose(Random var0, Object... var1) {
        return var1[var0.nextInt(var1.length)];
    }

    public static final int chooseP(Random var0, int[] var1) {
        int var2 = var0.nextInt(1000);
        int var3 = -2 + var1.length;

        int var4;
        for (var4 = 0; var4 < var3; var4 += 2) {
            var2 -= var1[var4];
            if (var2 < 0) {
                break;
            }
        }

        return var1[var4 + 1];
    }

    public static Cat create(Context var0) {
        return new Cat(var0, (long) Math.abs(ThreadLocalRandom.current().nextInt()));
    }

    public static final float frandrange(Random var0, float var1, float var2) {
        return var1 + (var2 - var1) * var0.nextFloat();
    }

    public static boolean isDark(int var0) {
        int var1 = (16711680 & var0) >> 16;
        int var2 = ('\uff00' & var0) >> 8;
        return (var0 & 255) + var1 + var2 < 128;
    }

    private Random notSoRandom(long var1) {
        synchronized (this) {
        }

        Random var4;
        try {
            if (this.mNotSoRandom == null) {
                this.mNotSoRandom = new Random();
                this.mNotSoRandom.setSeed(var1);
            }

            var4 = this.mNotSoRandom;
        } finally {
            ;
        }

        return var4;
    }

    private void slowDraw(Canvas var1, int var2, int var3, int var4, int var5) {
        for (int var6 = 0; var6 < this.D.drawingOrder.length; ++var6) {
            Drawable var7 = this.D.drawingOrder[var6];
            if (var7 != null) {
                var7.setBounds(var2, var3, var2 + var4, var3 + var5);
                var7.draw(var1);
            }
        }

    }

    public static void tint(int var0, Drawable... var1) {
        int var2 = 0;

        for (int var3 = var1.length; var2 < var3; ++var2) {
            Drawable var4 = var1[var2];
            if (var4 != null) {
                var4.mutate().setTint(var0);
            }
        }

    }

    public Builder buildNotification(Context var1) {
        Bundle var2 = new Bundle();
        var2.putString("android.substName", var1.getString(R.string.notification_name));
        Intent var3 = (new Intent("android.intent.action.MAIN")).setClass(var1, NekoLand.class).addFlags(268435456);
        return (new Builder(var1)).setSmallIcon(Icon.createWithResource(var1, R.drawable.stat_icon)).setLargeIcon(this.createLargeIcon(var1)).setColor(this.getBodyColor()).setPriority(Notification
                .PRIORITY_LOW).setContentTitle(var1.getString(R.string.notification_title)).setShowWhen(true).setCategory("status").setContentText(this.getName()).setContentIntent(PendingIntent.getActivity(var1, 0,
                var3,
                0)).setAutoCancel(true).setVibrate(PURR).addExtras(var2);
    }

    public Bitmap createBitmap(int var1, int var2) {
        if (this.mBitmap != null && this.mBitmap.getWidth() == var1 && this.mBitmap.getHeight() == var2) {
            return this.mBitmap.copy(this.mBitmap.getConfig(), true);
        } else {
            Bitmap var3 = Bitmap.createBitmap(var1, var2, Config.ARGB_8888);
            this.slowDraw(new Canvas(var3), 0, 0, var1, var2);
            return var3;
        }
    }

    public Icon createLargeIcon(Context var1) {
        Resources var2 = var1.getResources();
        int var3 = var2.getDimensionPixelSize(android.R.dimen.notification_large_icon_width);
        int var4 = var2.getDimensionPixelSize(android.R.dimen.notification_large_icon_height);
        Bitmap var5 = Bitmap.createBitmap(var3, var4, Config.ARGB_8888);
        Canvas var6 = new Canvas(var5);
        Paint var7 = new Paint();
        float[] var8 = new float[3];
        Color.colorToHSV(this.mBodyColor, var8);
        float var9;
        if (var8[2] > 0.5F) {
            var9 = var8[2] - 0.25F;
        } else {
            var9 = 0.25F + var8[2];
        }

        var8[2] = var9;
        var7.setColor(Color.HSVToColor(var8));
        float var10 = (float) (var3 / 2);
        var6.drawCircle(var10, var10, var10, var7);
        int var11 = var3 / 10;
        this.slowDraw(var6, var11, var11, var3 - var11 - var11, var4 - var11 - var11);
        return Icon.createWithBitmap(var5);
    }

    public void draw(Canvas var1) {
        int var2 = Math.min(var1.getWidth(), var1.getHeight());
        if (this.mBitmap == null || this.mBitmap.getWidth() != var2 || this.mBitmap.getHeight() != var2) {
            this.mBitmap = Bitmap.createBitmap(var2, var2, Config.ARGB_8888);
            this.slowDraw(new Canvas(this.mBitmap), 0, 0, var2, var2);
        }

        var1.drawBitmap(this.mBitmap, 0.0F, 0.0F, (Paint) null);
    }

    public int getBodyColor() {
        return this.mBodyColor;
    }

    public String getName() {
        return this.mName;
    }

    public int getOpacity() {
        return -3;
    }

    public long getSeed() {
        return this.mSeed;
    }

    public void setAlpha(int var1) {
    }

    public void setColorFilter(ColorFilter var1) {
    }

    public void setName(String var1) {
        this.mName = var1;
    }

    public static class CatParts {
        public Drawable back;
        public Drawable belly;
        public Drawable body;
        public Drawable bowtie;
        public Drawable cap;
        public Drawable collar;
        public Drawable[] drawingOrder;
        public Drawable faceSpot;
        public Drawable foot1;
        public Drawable foot2;
        public Drawable foot3;
        public Drawable foot4;
        public Drawable head;
        public Drawable leftEar;
        public Drawable leftEarInside;
        public Drawable leftEye;
        public Drawable leg1;
        public Drawable leg2;
        public Drawable leg2Shadow;
        public Drawable leg3;
        public Drawable leg4;
        public Drawable mouth;
        public Drawable nose;
        public Drawable rightEar;
        public Drawable rightEarInside;
        public Drawable rightEye;
        public Drawable tail;
        public Drawable tailCap;
        public Drawable tailShadow;

        public CatParts(Context context) {
            this.body = context.getDrawable(R.drawable.body);
            this.head = context.getDrawable(R.drawable.head);
            this.leg1 = context.getDrawable(R.drawable.leg1);
            this.leg2 = context.getDrawable(R.drawable.leg2);
            this.leg3 = context.getDrawable(R.drawable.leg3);
            this.leg4 = context.getDrawable(R.drawable.leg4);
            this.tail = context.getDrawable(R.drawable.tail);
            this.leftEar = context.getDrawable(R.drawable.left_ear);
            this.rightEar = context.getDrawable(R.drawable.right_ear);
            this.rightEarInside = context.getDrawable(R.drawable.right_ear_inside);
            this.leftEarInside = context.getDrawable(R.drawable.left_ear_inside);
            this.faceSpot = context.getDrawable(R.drawable.face_spot);
            this.cap = context.getDrawable(R.drawable.cap);
            this.mouth = context.getDrawable(R.drawable.mouth);
            this.foot4 = context.getDrawable(R.drawable.foot4);
            this.foot3 = context.getDrawable(R.drawable.foot3);
            this.foot1 = context.getDrawable(R.drawable.foot1);
            this.foot2 = context.getDrawable(R.drawable.foot2);
            this.leg2Shadow = context.getDrawable(R.drawable.leg2_shadow);
            this.tailShadow = context.getDrawable(R.drawable.tail_shadow);
            this.tailCap = context.getDrawable(R.drawable.tail_cap);
            this.belly = context.getDrawable(R.drawable.belly);
            this.back = context.getDrawable(R.drawable.back);
            this.rightEye = context.getDrawable(R.drawable.right_eye);
            this.leftEye = context.getDrawable(R.drawable.left_eye);
            this.nose = context.getDrawable(R.drawable.nose);
            this.collar = context.getDrawable(R.drawable.collar);
            this.bowtie = context.getDrawable(R.drawable.bowtie);
            this.drawingOrder = this.getDrawingOrder();
        }

        private Drawable[] getDrawingOrder() {
            Drawable[] var1 = new Drawable[]{this.collar, this.leftEar, this.leftEarInside, this.rightEar, this.rightEarInside, this.head, this.faceSpot, this.cap, this.leftEye, this.rightEye, this.nose, this.mouth, this.tail, this.tailCap, this.tailShadow, this.foot1, this.leg1, this.foot2, this.leg2, this.foot3, this.leg3, this.foot4, this.leg4, this.leg2Shadow, this.body, this.belly, this.bowtie};
            return var1;
        }
    }
}
