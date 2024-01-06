package com.moutimid.vellarentapp.vellarentappadmin.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.vellarentappadmin.adapter.PendingVillaAdapter;
import com.moutimid.vellarentapp.vellarentappadmin.helper.Config;
import com.moutimid.vellarentapp.vellarentappadmin.helper.Constants;
import com.moutimid.vellarentapp.vellarentappadmin.model.HouseRules;
import com.moutimid.vellarentapp.vellarentappadmin.model.PropertyAmenities;
import com.moutimid.vellarentapp.vellarentappadmin.model.Villa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PendingVillaActivity extends AppCompatActivity {

    PendingVillaAdapter ownVillaAdapter;
    RecyclerView content_rcv1;
    public List< com.moutimid.vellarentapp.model.Villa> productModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_villa);
        // Fetching views from XML file
        content_rcv1 = findViewById(R.id.content_rcv1);
        content_rcv1.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        ownVillaAdapter = new PendingVillaAdapter(this, productModelList);
        content_rcv1.setAdapter(ownVillaAdapter);
        if (Config.isNetworkAvailable(this)) {
            getRecommendedProducts();
        } else {
            Toast.makeText(this, "No network connection available.", Toast.LENGTH_SHORT).show();
        }

    }
    private void getRecommendedProducts() {
//        Config.showProgressDialog(getContext());
        Constants.databaseReference().child(Config.villa).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productModelList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    com.moutimid.vellarentapp.model.Villa villaModel = ds.getValue( com.moutimid.vellarentapp.model.Villa.class);
                    Log.d("dataa", villaModel.isBills_included() + "");
                    if (!villaModel.verified) {
                        DataSnapshot propertyAmenities1 = ds.child("PropertyAmenities");
                        DataSnapshot houseRules1 = ds.child("HouseRules");
                        com.moutimid.vellarentapp.model.PropertyAmenities propertyAmenities = propertyAmenities1.getValue( com.moutimid.vellarentapp.model.PropertyAmenities.class);
                        com.moutimid.vellarentapp.model.HouseRules houseRules = houseRules1.getValue( com.moutimid.vellarentapp.model.HouseRules.class);
                        villaModel.setPropertyAmenities(propertyAmenities);
                        villaModel.setHouseRules(houseRules);
                        DataSnapshot imagesSnapshot = ds.child("images");
                        Map<String, String> imagesMap = new HashMap<>();
                        for (DataSnapshot imageSnapshot : imagesSnapshot.getChildren()) {
                            String imageKey = imageSnapshot.getKey();
                            String imageUrl = imageSnapshot.getValue(String.class);
                            imagesMap.put(imageKey, imageUrl);
                        }

                        villaModel.setImages(imagesMap);
                        productModelList.add(villaModel);
                    }
                    ownVillaAdapter.notifyDataSetChanged();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });


    }
    public void BackPress(View view) {
        onBackPressed();
    }
}