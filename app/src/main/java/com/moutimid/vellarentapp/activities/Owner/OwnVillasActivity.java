package com.moutimid.vellarentapp.activities.Owner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.adapter.AllVillaAdapter;
import com.moutimid.vellarentapp.adapter.OwnVillaAdapter;
import com.moutimid.vellarentapp.helper.Config;
import com.moutimid.vellarentapp.helper.Constants;
import com.moutimid.vellarentapp.model.Villa;

import java.util.ArrayList;
import java.util.List;

public class OwnVillasActivity extends AppCompatActivity {
    RecyclerView content_rcv;
    public List<Villa> productModelList = new ArrayList<>();
    OwnVillaAdapter villaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_villas);

        content_rcv = findViewById(R.id.content_rcv);
        content_rcv.setLayoutManager(new GridLayoutManager(OwnVillasActivity.this, 1));
        villaAdapter = new OwnVillaAdapter(OwnVillasActivity.this, productModelList);
        content_rcv.setAdapter(villaAdapter);
        if (Config.isNetworkAvailable(OwnVillasActivity.this)) {
            getProducts();
        } else {
            Toast.makeText(OwnVillasActivity.this, "No network connection available.", Toast.LENGTH_SHORT).show();
        }
    }
    private void getProducts() {
//        Config.showProgressDialog(getContext());
        Constants.databaseReference().child(Config.villa).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productModelList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Villa villaModel = ds.getValue(Villa.class);
                    productModelList.add(villaModel);
                }
                villaAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
//        Constants.databaseReference().child("Locations").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    Villa villaModel1 = ds.getValue(Villa.class);
//                    userArrayList.add(new LocationModel(villaModel1.getLat(), villaModel1.getLng(), villaModel1.getName()));
//                    Config.dismissProgressDialog();
//
//                }
//                Stash.put("Locations", userArrayList);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        Config.dismissProgressDialog();

    }
}