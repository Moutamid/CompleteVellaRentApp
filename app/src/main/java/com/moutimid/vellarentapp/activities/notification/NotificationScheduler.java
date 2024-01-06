package com.moutimid.vellarentapp.activities.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


import java.util.Calendar;

public class NotificationScheduler {

    public static void scheduleNotification(Context context, Calendar calendar, String message, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Create an intent to trigger the notification
        Intent intent = new Intent(context, NotificationReceiver.class);

        intent.putExtra(Constants.REMINDER_TYPE, type);
        intent.putExtra(Constants.MESSAGE, message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                Constants.getRandomNumber(), intent,
                PendingIntent.FLAG_IMMUTABLE);//FLAG_UPDATE_CURRENT

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

          }
}
