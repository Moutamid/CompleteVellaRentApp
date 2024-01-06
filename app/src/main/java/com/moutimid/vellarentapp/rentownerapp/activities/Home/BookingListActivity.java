package com.moutimid.vellarentapp.rentownerapp.activities.Home;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.helper.Config;
import com.moutimid.vellarentapp.rentownerapp.adapter.BookingAdapter;
import com.moutimid.vellarentapp.rentownerapp.model.Booking;
import com.moutimid.vellarentapp.vellarentappadmin.adapter.OwnerAdapter;
import com.moutimid.vellarentapp.vellarentappadmin.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class BookingListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOwners;
    private BookingAdapter ownerAdapter;
    private DatabaseReference ownersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        ownersRef = FirebaseDatabase.getInstance().getReference("RentApp").child("Bookings").child(FirebaseAuth.getInstance().getUid());

        recyclerViewOwners = findViewById(R.id.recyclerViewOwners);
        recyclerViewOwners.setLayoutManager(new LinearLayoutManager(this));

        // Initialize and set up the adapter
        ownerAdapter = new BookingAdapter();
        recyclerViewOwners.setAdapter(ownerAdapter);
        Config.showProgressDialog(BookingListActivity.this);

        // Retrieve owners from Firebase Realtime Database
        ownersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Booking> owners = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Booking owner = snapshot.getValue(Booking.class);
                    if (!owner.isVerified()) {
                        owners.add(owner);
                    }
                }

                Config.dismissProgressDialog();
                ownerAdapter.setOwners(owners, BookingListActivity.this);
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
