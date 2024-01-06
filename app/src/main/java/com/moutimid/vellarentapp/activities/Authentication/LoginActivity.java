package com.moutimid.vellarentapp.activities.Authentication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.lang.UCharacter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.MainActivity;
import com.moutimid.vellarentapp.helper.Constants;
import com.moutimid.vellarentapp.model.UserModel;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {
    EditText email, password;

    String email_str, password_str;
    String name, emailstr, image_gmail;
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference mDatabase;
    Button login;
    TextView signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(View);
        }

// Set the status bar background color to white
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#202020"));
        }
        initComponent();
        firebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initValues();
                loginRequest();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(com.moutimid.vellarentapp.activities.Authentication.LoginActivity.this, SignupActivity.class));
            }
        });
    }

    public void initComponent() {
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }

    public void initValues() {
        email_str = email.getText().toString();
        password_str = password.getText().toString();
    }

    public void sign_up(View view) {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }


    private void loginRequest() {
        if (email_str.length() == 0) {
            email.setError("Please Error");
        } else if (password_str.length() == 0) {
            password.setError("Please Error");
        } else {

            Dialog lodingbar = new Dialog(LoginActivity.this);
            lodingbar.setContentView(R.layout.loading);
            Objects.requireNonNull(lodingbar.getWindow()).setBackgroundDrawable(new ColorDrawable(UCharacter.JoiningType.TRANSPARENT));
            lodingbar.setCancelable(false);
            lodingbar.show();

            Constants.auth().signInWithEmailAndPassword(
                    email.getText().toString(),
                    password.getText().toString()
            ).addOnSuccessListener(authResult -> {

                Constants.UserReference.child(authResult.getUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            Log.d("data", userModel.toString() + "");
                            Stash.put("UserDetails", userModel);
                            Stash.put("userID", authResult.getUser().getUid());
                            Stash.put("type", userModel.getType());

                            lodingbar.dismiss();
                            if (Stash.getString("type").equals("user")) {
                                Stash.put("username", userModel.name);
                                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                finishAffinity();
                            } else if (Stash.getString("type").equals("owner")) {

                                Intent mainIntent = new Intent(LoginActivity.this, com.moutimid.vellarentapp.rentownerapp.MainActivity.class);
                                startActivity(mainIntent);
                                finishAffinity();

                            } else if (Stash.getString("type").equals("admin")) {
                                Intent mainIntent = new Intent(LoginActivity.this, com.moutimid.vellarentapp.vellarentappadmin.MainActivity.class);
                                startActivity(mainIntent);
                                finishAffinity();
                            }


                        }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            lodingbar.dismiss();
                            Toast.makeText(LoginActivity.this, "error" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }).addOnFailureListener(e -> {
                    lodingbar.dismiss();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
    }

}