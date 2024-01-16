package com.moutimid.vellarentapp;


import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.fxn.stash.Stash;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.Fragment.FavouriteFragment;
import com.moutimid.vellarentapp.Fragment.ProfileFragment;
import com.moutimid.vellarentapp.Fragment.VillaFragment;
import com.moutimid.vellarentapp.helper.Config;
import com.moutimid.vellarentapp.helper.GPSTracker;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;


public class MainActivity extends AppCompatActivity {
    final int PERMISSION_REQUEST_CODE = 112;

    SmoothBottomBar bottomBar;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    private GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(View);
        }

// Set the status bar background color to white
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#000000"));
        }
        Stash.clear("dates");
        if (Build.VERSION.SDK_INT > 32) {
            if (!shouldShowRequestPermissionRationale("112")) {
                getNotificationPermission();
            }
        }
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                getLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
         Config.checkApp(MainActivity.this);
//        replaceFragment(new VillaFragment());
        getLocation();
        bottomBar = findViewById(R.id.bottomBar);
        viewPager = findViewById(R.id.frame_layout);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(viewPagerAdapter);

        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int item) {
                viewPager.setCurrentItem(item);
                return true;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Not needed for your case
            }

            @Override
            public void onPageSelected(int position) {
                bottomBar.setItemActiveIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Not needed for your case
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            getLocation();
        }

        if(requestCode==PERMISSION_REQUEST_CODE)
        {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // allow

            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void getLocation() {
        gpsTracker = new GPSTracker(MainActivity.this);
        if (gpsTracker.canGetLocation()) {
            Config.lat = gpsTracker.getLatitude();
            Config.lng = gpsTracker.getLongitude();
        } else {
            gpsTracker.showSettingsAlert();
        }
    }
    public void getNotificationPermission() {
        try {
            if (Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE);
            }
        } catch (Exception e) {

        }
    }
    private static class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new VillaFragment();
                case 1:
                    return new FavouriteFragment();
                case 2:
                    return new ProfileFragment();
                default:
                    return new VillaFragment();
            }
        }

        @Override
        public int getCount() {
            return 3; // Number of tabs
        }
    }
    @Override
    public void onBackPressed() {
        Fragment currentFragment = viewPagerAdapter.getItem(viewPager.getCurrentItem());
        if (currentFragment instanceof VillaFragment) {
            finishAffinity();
        } else {
            viewPager.setCurrentItem(0, true);
        }
    }
}