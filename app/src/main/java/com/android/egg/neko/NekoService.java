package com.android.egg.neko;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobInfo.Builder;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.android.egg.android7decompiled.R;

import java.util.List;
import java.util.Random;

public class NekoService extends JobService {
    public static float CAT_CAPTURE_PROB = 1.0F;
    public static int CAT_NOTIFICATION = 1;
    public static long INTERVAL_FLEX;
    public static float INTERVAL_JITTER_FRAC;
    public static int JOB_ID = 42;
    public static long MINUTES;
    public static long SECONDS = 1000L;

    static {
        MINUTES = 60L * SECONDS;
        INTERVAL_FLEX = 5L * MINUTES;
        INTERVAL_JITTER_FRAC = 0.25F;
    }

    public static void cancelJob(Context var0) {
        JobScheduler var1 = (JobScheduler) var0.getSystemService(JobScheduler.class);
        Log.v("NekoService", "Canceling job");
        var1.cancel(JOB_ID);
    }

    public static void registerJob(Context var0, long var1) {
        JobScheduler var3 = (JobScheduler) var0.getSystemService(JobScheduler.class);
        var3.cancel(JOB_ID);
        long var4 = var1 * MINUTES;
        long var6 = (long) (INTERVAL_JITTER_FRAC * (float) var4);
        long var8 = var4 + ((long) (Math.random() * (double) (2L * var6)) - var6);
        JobInfo var10 = (new Builder(JOB_ID, new ComponentName(var0, NekoService.class))).setPeriodic(var8, INTERVAL_FLEX).build();
        Log.v("NekoService", "A cat will visit in " + var8 + "ms: " + var10);
        var3.schedule(var10);
        if (NekoLand.DEBUG_NOTIFICATIONS) {
            NotificationManager var13 = (NotificationManager) var0.getSystemService(NotificationManager.class);
            android.app.Notification.Builder var14 = (new android.app.Notification.Builder(var0)).setSmallIcon(R.drawable.stat_icon);
            Object[] var15 = new Object[]{Long.valueOf(var8 / MINUTES)};
            var13.notify(500, var14.setContentTitle(String.format("Job scheduled in %d min", var15)).setContentText(String.valueOf(var10)).setPriority(Notification.PRIORITY_MIN).setCategory
                    ("service").setShowWhen(true).build());
        }

    }

    public boolean onStartJob(JobParameters var1) {
        Log.v("NekoService", "Starting job: " + String.valueOf(var1));
        NotificationManager var3 = (NotificationManager) this.getSystemService(NotificationManager.class);
        if (NekoLand.DEBUG_NOTIFICATIONS) {
            (new Bundle()).putString("android.substName", this.getString(R.string.notification_name));
            this.getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_width);
            var3.notify(1, Cat.create(this).buildNotification(this).setContentTitle("DEBUG").setContentText("Ran job: " + var1).build());
        }

        PrefState var4 = new PrefState(this);
        int var5 = var4.getFoodState();
        if (var5 != 0) {
            var4.setFoodState(0);
            Random var6 = new Random();
            if (var6.nextFloat() <= CAT_CAPTURE_PROB) {
                List var7 = var4.getCats();
                int[] var8 = this.getResources().getIntArray(R.array.food_new_cat_prob);
                int var9;
                if (var5 < var8.length) {
                    var9 = var8[var5];
                } else {
                    var9 = 50;
                }

                float var10 = (float) var9 / 100.0F;
                Cat var11;
                if (var7.size() != 0 && var6.nextFloat() > var10) {
                    var11 = (Cat) var7.get(var6.nextInt(var7.size()));
                    Log.v("NekoService", "A cat has returned: " + var11.getName());
                } else {
                    var11 = Cat.create(this);
                    var4.addCat(var11);
                    Log.v("NekoService", "A new cat is here: " + var11.getName());
                }

                android.app.Notification.Builder var13 = var11.buildNotification(this);
                var3.notify(CAT_NOTIFICATION, var13.build());
            }
        }

        cancelJob(this);
        return false;
    }

    public boolean onStopJob(JobParameters var1) {
        return false;
    }
}
