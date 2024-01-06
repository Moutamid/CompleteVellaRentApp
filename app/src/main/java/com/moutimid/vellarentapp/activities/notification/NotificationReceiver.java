package com.moutimid.vellarentapp.activities.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.moutimid.vellarentapp.MainActivity;

import java.util.Calendar;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String reminderType = intent.getStringExtra(Constants.REMINDER_TYPE);
        String message = intent.getStringExtra(Constants.MESSAGE);
        NotificationHelper notificationHelper = new NotificationHelper(context);
//        context.startActivity(new Intent(context, MainActivity.class));
        if (reminderType.equals(Constants.MEDICINE_REMINDER)) {
            // MEDICINE REMINDER

            notificationHelper.sendHighPriorityNotification("Reminder", message, MainActivity.class);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            calendar.add(Calendar.HOUR_OF_DAY, 9);

            NotificationScheduler.scheduleNotification(context, calendar,
                    message, Constants.MEDICINE_REMINDER);

        } else {

            notificationHelper.sendHighPriorityNotification("Reminder", message, MainActivity.class);
        }
    }
}
