package com.moutimid.vellarentapp.activities.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.moutamid.vellarentapp.R;

public class NotificationWorker extends Worker {

    private static final String TAG = "NotificationWorker";
    private static final String CHANNEL_ID = "MyWorkerChannel";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Log.d(TAG, "doWork: Background work started");

            // Show a foreground notification
            Notification notification = createNotification();
            ForegroundInfo foregroundInfo = new ForegroundInfo(1, notification);
            setForegroundAsync(foregroundInfo);

            // Your background work logic here

            Log.d(TAG, "doWork: Background work completed successfully");
            return Result.success();
        } catch (Exception e) {
            Log.e(TAG, "doWork: Background work failed", e);
            return Result.failure();
        }
    }

    private Notification createNotification() {
        createNotificationChannel();

        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle("My Worker")
                .setContentText("Doing important work in the background")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "My Worker Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getApplicationContext().getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
