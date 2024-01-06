package com.moutimid.vellarentapp.vellarentappadmin.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.helper.Config;
import com.moutimid.vellarentapp.vellarentappadmin.adapter.OwnerAdapter;
import com.moutimid.vellarentapp.vellarentappadmin.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class OwnersListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOwners;
    private OwnerAdapter ownerAdapter;

    private DatabaseReference ownersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owners_list);

        ownersRef = FirebaseDatabase.getInstance().getReference("RentApp").child("users");

        recyclerViewOwners = findViewById(R.id.recyclerViewOwners);
        recyclerViewOwners.setLayoutManager(new LinearLayoutManager(this));

        // Initialize and set up the adapter
        ownerAdapter = new OwnerAdapter();
        recyclerViewOwners.setAdapter(ownerAdapter);
        Config.showProgressDialog(OwnersListActivity.this);

        // Retrieve owners from Firebase Realtime Database
        ownersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<UserModel> owners = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserModel owner = snapshot.getValue(UserModel.class);
                    if (owner.type.equals("owner")) {
                        owners.add(owner);
                    }
                }
                Config.dismissProgressDialog();
                ownerAdapter.setOwners(owners, OwnersListActivity.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Config.dismissProgressDialog();

            }
        });
    }

    public void BackPress(View view) {
        onBackPressed();
    }
}
