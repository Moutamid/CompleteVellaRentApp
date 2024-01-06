package com.moutimid.vellarentapp.activities.Authentication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.lang.UCharacter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.fxn.stash.Stash;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.MainActivity;
import com.moutimid.vellarentapp.helper.Config;
import com.moutimid.vellarentapp.helper.Constants;
import com.moutimid.vellarentapp.model.UserModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class SignupActivity extends AppCompatActivity {
      EditText name, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(View);
        }

// Set the status bar background color to white
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#202020"));
        }
        initComponent();
      }

    public void initComponent() {
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }

    public void login(View view) {
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
    }

    public void sign_up(View view) {
        if (validation()) {
            registerRequest();
        }
    }



    private void registerRequest() {
        Dialog lodingbar = new Dialog(SignupActivity.this);
        lodingbar.setContentView(R.layout.loading);
        Objects.requireNonNull(lodingbar.getWindow()).setBackgroundDrawable(new ColorDrawable(UCharacter.JoiningType.TRANSPARENT));
        lodingbar.setCancelable(false);
        lodingbar.show();

                    Constants.auth().createUserWithEmailAndPassword(
                            email.getText().toString(),
                            password.getText().toString()
                    ).addOnCompleteListener(authResult -> {
                        UserModel userModel = new UserModel();
                        userModel.name = name.getText().toString();
                        userModel.email = email.getText().toString();
                        userModel.type = "user";
                        userModel.id = authResult.getResult().getUser().getUid();
                        Stash.put("userID", authResult.getResult().getUser().getUid());
                        Stash.put("UserDetails", userModel);
                        Stash.put("username", userModel.name);
                        Stash.put("type", userModel.getType());
                        Constants.UserReference.child(Objects.requireNonNull(authResult.getResult().getUser().getUid())).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Stash.put("UserDetails", userModel);
                                lodingbar.dismiss();
                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                finishAffinity();
                            }
                        });
                    }).addOnFailureListener(e -> {
                        lodingbar.dismiss();
                        Toast.makeText(this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });


    }

    private boolean validation() {
     if (name.getText().toString().isEmpty()) {
            name.setError("Enter Name");
            name.requestFocus();
            Config.openKeyboard(this);

            return false;
        }  else if (email.getText().toString().isEmpty()) {
            email.setError("Enter Email");
            email.requestFocus();
            Config.openKeyboard(this);
            return false;

        } else if (password.getText().toString().isEmpty()) {
            password.setError("Enter Password");
            password.requestFocus();
            Config.openKeyboard(this);

            return false;

        }else if (!Config.isNetworkAvailable(this)) {
            Config.showToast(this, "You are not connected to network");

            return false;
        } else {

            return true;

        }

    }

}