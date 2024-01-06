package com.moutimid.vellarentapp.vellarentappadmin.activities;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.model.UserModel;
import com.moutimid.vellarentapp.vellarentappadmin.model.Owner;

import java.util.Objects;

public class AddOwnerActivity extends AppCompatActivity {

    private EditText editTextName, editTextPhone, editTextEmail, editTextPassword;
    private Button btnAddOwner;

    private FirebaseAuth mAuth;
    private DatabaseReference ownersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_owner);

        mAuth = FirebaseAuth.getInstance();
        ownersRef = FirebaseDatabase.getInstance().getReference("RentApp").child("users");
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        btnAddOwner = findViewById(R.id.btnAddOwner);
        btnAddOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOwner();
            }
        });
    }

    private void addOwner() {
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please give complete details", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                UserModel userModel = new UserModel();
                                userModel.name = name;
                                userModel.email = email;
                                userModel.type = "owner";
                                userModel.id = user.getUid();


                                // Create Owner object and push to Firebase Realtime Database
                                            ownersRef.child(user.getUid()).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        Toast.makeText(AddOwnerActivity.this, "Owner added successfully", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(AddOwnerActivity.this, "Failed to add owner", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });


                            } else {
                                        Toast.makeText(AddOwnerActivity.this, "Failed to add owner", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddOwnerActivity.this, "Failed to add owner", Toast.LENGTH_SHORT).show();

                        }
                    });
        }

    }

    public void BackPress(View view) {
        onBackPressed();
    }
}
