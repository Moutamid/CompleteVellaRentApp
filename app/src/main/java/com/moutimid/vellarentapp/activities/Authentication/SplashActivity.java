package com.moutimid.vellarentapp.activities.Authentication;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.fxn.stash.Stash;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.MainActivity;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

// Set the status bar background color to white
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#202020"));
        }
        setContentView(R.layout.activity_splash);

        int splashInterval = 3000;
        ImageView imageView = findViewById(R.id.logo);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                imageView,
                PropertyValuesHolder.ofFloat("scaleX", 2.8F),

                PropertyValuesHolder.ofFloat("scaleY", 2.8F)
        );
        objectAnimator.setDuration(1000);
        objectAnimator.start();
        new Handler().postDelayed(this::goToApp, splashInterval);
    }

    public void goToApp() {
        if (Stash.getString("type").equals("user")) {
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        } else if (Stash.getString("type").equals("owner")) {

            Intent mainIntent = new Intent(SplashActivity.this, com.moutimid.vellarentapp.rentownerapp.MainActivity.class);
            startActivity(mainIntent);
            finish();

        } else if (Stash.getString("type").equals("admin")) {
            Intent mainIntent = new Intent(SplashActivity.this, com.moutimid.vellarentapp.vellarentappadmin.MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
        else
        {
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }
}