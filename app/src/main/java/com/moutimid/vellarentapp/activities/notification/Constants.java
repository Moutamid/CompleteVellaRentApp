package com.moutimid.vellarentapp.activities.notification;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import com.fxn.stash.Stash;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class Constants {
    public static String FOOD_LIST_KEY="Food_List";
    public static final String REMINDERS_LIST = "REMINDERS_LIST";
    public static final String REMINDER_TYPE = "REMINDER_TYPE";
    public static final String MEDICINE_REMINDER = "MEDICINE_REMINDER";
    public static final String MESSAGE = "MESSAGE";
    private static final String NOTIFICATION_ID = "NOTIFICATION_ID";

    public static int getNewID(){
        int value = Stash.getInt(Constants.NOTIFICATION_ID, 0);
        value++;
        Stash.put(Constants.NOTIFICATION_ID, value);
        return value;
    }

    public static int getRandomNumber(){
        int value = Stash.getInt("randomNumber", 0);
        value++;
        Stash.put("randomNumber", value);
        return value;
    }

    public static void setAppLocale(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);

        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }
    public static void checkApp(Activity activity) {
        String appName = "Reminder App";

        new Thread(() -> {
            URL google = null;
            try {
                google = new URL("https://raw.githubusercontent.com/Moutamid/Moutamid/main/apps.txt");
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if ((input = in != null ? in.readLine() : null) == null) break;
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            try {
                JSONObject myAppObject = new JSONObject(htmlData).getJSONObject(appName);

                boolean value = myAppObject.getBoolean("value");
                String msg = myAppObject.getString("msg");

                if (value) {
                    activity.runOnUiThread(() -> {
                        new AlertDialog.Builder(activity)
                                .setMessage(msg)
                                .setCancelable(false)
                                .show();
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }

}
