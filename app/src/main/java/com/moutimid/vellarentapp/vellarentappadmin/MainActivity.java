package com.moutimid.vellarentapp.vellarentappadmin;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.fxn.stash.Stash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.activities.Authentication.LoginActivity;
import com.moutimid.vellarentapp.vellarentappadmin.activities.AddOwnerActivity;
import com.moutimid.vellarentapp.vellarentappadmin.activities.AllVillaActivity;
import com.moutimid.vellarentapp.vellarentappadmin.activities.OwnersListActivity;
import com.moutimid.vellarentapp.vellarentappadmin.activities.PendingVillaActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    final int PERMISSION_REQUEST_CODE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        if (Build.VERSION.SDK_INT > 32) {
            if (!shouldShowRequestPermissionRationale("112")) {
                getNotificationPermission();
            }
        }
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                    return;
                }
                String token = task.getResult();
                Log.d("TAG", "FCM Token: " + token);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("token", token);
                DatabaseReference z = FirebaseDatabase.getInstance().getReference().child("RentApp");
                z.child("Admin").setValue(hashMap);


            }
        });

        findViewById(R.id.btnAddOwner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddOwnerActivity.class));
            }
        });

        findViewById(R.id.btnViewOwners).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, OwnersListActivity.class));
            }
        });

        findViewById(R.id.btnViewVerified).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AllVillaActivity.class));
            }
        });

        findViewById(R.id.btnViewPending).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PendingVillaActivity.class));
            }
        });
        findViewById(R.id.btnViewLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Stash.clearAll();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finishAffinity();
            }
        });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // allow

                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;

        }

    }

}
