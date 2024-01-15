package com.moutimid.vellarentapp.vellarentappadmin.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.activities.Home.MapActivity;
import com.moutimid.vellarentapp.adapter.ImageAdapter;
import com.moutimid.vellarentapp.dailogues.CalenderDialogClass;
import com.moutimid.vellarentapp.helper.Config;
import com.moutimid.vellarentapp.model.UserModel;
import com.moutimid.vellarentapp.model.Villa;
import com.moutimid.vellarentapp.rentownerapp.activities.Home.EditVillaActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.glailton.expandabletextview.ExpandableTextView;

public class VillaDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    Villa villaModel;
    Button map;
    ImageView image;
    String token_admin;
    RecyclerView recyclerView;
    private GoogleMap mMap;
    static DatabaseReference propertyRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ImageView edit, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_villa_details_edit);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(View);
        }

// Set the status bar background color to white
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#202020"));
        }
        fetch_data();

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetch_data();
    }

    public void fetch_data() {
        villaModel = (Villa) Stash.getObject(Config.currentModel, Villa.class);
        propertyRef = database.getReference("RentApp").child("Villas").child(villaModel.getKey());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        recyclerView = findViewById(R.id.recyclerView);
        edit = findViewById(R.id.edit);
        image = findViewById(R.id.image);
        map = findViewById(R.id.show_map);
        image = findViewById(R.id.image);
        // Assign IDs to views
        Toolbar toolbar = findViewById(R.id.toolbar_);
        ImageView backpress = findViewById(R.id.backpress);
        ImageView image = findViewById(R.id.image);
        TextView name = findViewById(R.id.name);
        TextView price = findViewById(R.id.price);
        name.setText(villaModel.getName());
        price.setText(villaModel.getBill() + " $/month");
        Glide.with(com.moutimid.vellarentapp.vellarentappadmin.activities.VillaDetailsActivity.this).load(villaModel.getImage()).into(image);
        TextView noOfBedroom = findViewById(R.id.no_of_bedroom);
        noOfBedroom.setText(villaModel.getBedroom() + "");
        TextView noOfBathroom = findViewById(R.id.no_of_bathroom);
        noOfBathroom.setText(villaModel.getBathRoom() + "");
        TextView descriptionTitle = findViewById(R.id.description_title);
        ExpandableTextView description = findViewById(R.id.description);
        description.setText(villaModel.getDescription());
        ImageView availability = findViewById(R.id.availability);
        TextView house_rules = findViewById(R.id.house_rules);
        TextView pet_friendly = findViewById(R.id.pet_friendly);
        TextView smoke_friendly = findViewById(R.id.smoke_friendly);
        TextView no_of_persons = findViewById(R.id.no_of_persons);
        TextView distance = findViewById(R.id.distance);
        distance.setText(Stash.getString("distance") + " km away from you");
        no_of_persons.setText("Available for " + villaModel.no_of_persons + " persons");
        no_of_persons.setVisibility(View.GONE);
        showImagesInRecyclerView();
        delete = findViewById(R.id.delete);

        if (!villaModel.rules.equals(",")) {
            house_rules.setVisibility(View.VISIBLE);
//            pet_friendly.setVisibility(View.VISIBLE);
//            pet_friendly.setText(villaModel.rules.trim());
        } else {
            house_rules.setVisibility(View.GONE);
            pet_friendly.setVisibility(View.GONE);
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new android.app.AlertDialog.Builder(com.moutimid.vellarentapp.vellarentappadmin.activities.VillaDetailsActivity.this)
                        .setTitle("Are you sure?")
                        .setMessage("You want to delete this Villa permanently")
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Config.showProgressDialog(com.moutimid.vellarentapp.vellarentappadmin.activities.VillaDetailsActivity.this);
                                propertyRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Config.dismissProgressDialog();
                                            finish();
                                        } else {
                                            Config.dismissProgressDialog();
                                            Toast.makeText(com.moutimid.vellarentapp.vellarentappadmin.activities.VillaDetailsActivity.this, "Something went worng", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setIcon(R.mipmap.ic_launcher_round)
                        .show();

            }
        });

        TextView propertyAmenitiesTitle = findViewById(R.id.property_amenities_title);
        LinearLayout dryerLayout = findViewById(R.id.dryer_layout);
        LinearLayout furnishedLayout = findViewById(R.id.furnished_layout);
        LinearLayout equippedKitchenLayout = findViewById(R.id.equipped_kitchen_layout);
        LinearLayout gardenLayout = findViewById(R.id.garden_layout);
        LinearLayout heaterLayout = findViewById(R.id.heater_layout);
        LinearLayout tvLayout = findViewById(R.id.tv_layout);
        LinearLayout wifiLayout = findViewById(R.id.wifi_layout);
        LinearLayout machine_layout = findViewById(R.id.machine_layout);
        LinearLayout parking_layout = findViewById(R.id.parking_layout);
        LinearLayout air_layout = findViewById(R.id.air_layout);
        TextView location = findViewById(R.id.location);
        location.setText(villaModel.getTitle());
// Request for Rent button
        Button requestButton = findViewById(R.id.request_button);
        if (Stash.getBoolean("onetime")) {
            displayTextInTextViews(villaModel.rules);
        }
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VillaDetailsActivity.this, EditVillaActivity.class));
            }
        });
        if (villaModel.getPropertyAmenities() != null) {
            propertyAmenitiesTitle.setVisibility(View.VISIBLE);
        } else {
            propertyAmenitiesTitle.setVisibility(View.GONE);

        }
        if (villaModel.getPropertyAmenities().isDryer()) {
            dryerLayout.setVisibility(View.VISIBLE);
        } else {
            dryerLayout.setVisibility(View.GONE);
        }
        if (villaModel.getPropertyAmenities().isFurnished()) {
            furnishedLayout.setVisibility(View.VISIBLE);
        } else {
            furnishedLayout.setVisibility(View.GONE);
        }
        if (villaModel.getPropertyAmenities().isGarden()) {
            gardenLayout.setVisibility(View.VISIBLE);
        } else {
            gardenLayout.setVisibility(View.GONE);
        }
        if (villaModel.getPropertyAmenities().isEquippedKitchen()) {
            equippedKitchenLayout.setVisibility(View.VISIBLE);
        } else {
            equippedKitchenLayout.setVisibility(View.GONE);
        }
        if (villaModel.getPropertyAmenities().isHeating()) {
            heaterLayout.setVisibility(View.VISIBLE);
        } else {
            heaterLayout.setVisibility(View.GONE);
        }
        if (villaModel.getPropertyAmenities().isWifi()) {
            wifiLayout.setVisibility(View.VISIBLE);
        } else {
            wifiLayout.setVisibility(View.GONE);
        }
        if (villaModel.getPropertyAmenities().isTv()) {
            tvLayout.setVisibility(View.VISIBLE);
        } else {
            tvLayout.setVisibility(View.GONE);
        }
        if (villaModel.getPropertyAmenities().isWashingMachine()) {
            machine_layout.setVisibility(View.VISIBLE);
        } else {
            machine_layout.setVisibility(View.GONE);
        }
        if (villaModel.getPropertyAmenities().isParking()) {
            parking_layout.setVisibility(View.VISIBLE);
        } else {
            parking_layout.setVisibility(View.GONE);
        }
        if (villaModel.getPropertyAmenities().isAirConditioning()) {
            air_layout.setVisibility(View.VISIBLE);
        } else {
            air_layout.setVisibility(View.GONE);
        }

        availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalenderDialogClass cdd = new CalenderDialogClass(com.moutimid.vellarentapp.vellarentappadmin.activities.VillaDetailsActivity.this);
                cdd.show();
            }
        });
        DatabaseReference z = FirebaseDatabase.getInstance().getReference().child("RentApp").child("Admin");
        z.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get the value of the "token" field
                token_admin = dataSnapshot.child("token").getValue().toString();

                // Use the token as needed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Failed to read value.", databaseError.toException());
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("latlng", villaModel.getLat() + "   " + villaModel.getLng());

                if (villaModel.getLat() > -90 && villaModel.getLat() < 90 && villaModel.getLng() > -180 && villaModel.getLng() < 180) {
                    Intent intent = new Intent(com.moutimid.vellarentapp.vellarentappadmin.activities.VillaDetailsActivity.this, com.moutimid.vellarentapp.activities.Home.MapActivity.class);
                    intent.putExtra("lat", villaModel.getLat());
                    intent.putExtra("lng", villaModel.getLng());
                    intent.putExtra("name", villaModel.getName());
                    startActivity(intent);

                } else {
                    Toast.makeText(com.moutimid.vellarentapp.vellarentappadmin.activities.VillaDetailsActivity.this, "Invalid Coordinates to show marker", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void onBack(View view) {
        onBackPressed();
    }

    public void booking(View view) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            sendFCMPush(token_admin);
        } else {

        }

    }

    private void sendFCMPush(String token) {
        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();

        try {
            UserModel userModel = (UserModel) Stash.getObject("UserDetails", UserModel.class);

            notifcationBody.put("title", "Booking Alert");
            notifcationBody.put("message", userModel.name + " wants to book this Villa");
            notification.put("to", token); // Use the FCM token of the recipient device
            notification.put("data", notifcationBody);

            Log.e("DATAAAAAA", notification.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.POST,
                Config.NOTIFICATIONAPIURL,
                notification,
                response -> {
                    Log.e("True", response + "");
                    Log.d("Response", response.toString());
                    Toast.makeText(this, "Rent request is successfully send to Owner", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Log.e("False", error + "");
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "key=" + Config.SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(com.moutimid.vellarentapp.vellarentappadmin.activities.VillaDetailsActivity.this);
        int socketTimeout = 1000 * 60; // 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }

    private void showImagesInRecyclerView() {
        // Retrieve Villa models from Stash

        if (villaModel != null) {
            // Process the Villa models and display images in RecyclerView
            List<String> imageUrls = new ArrayList<>();

            Map<String, String> images = villaModel.getImages();
            if (images != null) {
                for (String imageUrl : images.values()) {
                    imageUrls.add(imageUrl);
                }
            }
            Log.d("dataaaa", imageUrls.toString());
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

            // Now you can use imageUrls to display images in your RecyclerView
            // Use your RecyclerView adapter to show images (similar to the ImageAdapter in your previous code)
            ImageAdapter imageAdapter = new ImageAdapter(imageUrls);
            recyclerView.setAdapter(imageAdapter);
            imageAdapter.notifyDataSetChanged();


            ViewPager viewPager = findViewById(R.id.viewPager);

            // Get image URLs and position from the intent

            // Set up the ViewPager with the ImagePagerAdapter
            com.moutamid.vellarentapp.adapter.ImagePagerAdapter imagePagerAdapter = new com.moutamid.vellarentapp.adapter.ImagePagerAdapter(this, imageUrls);
            viewPager.setAdapter(imagePagerAdapter);

            // Set the starting position
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker at the specific LatLng and set the title
        LatLng markerLatLng = new LatLng(villaModel.getLat(), villaModel.getLng());
        mMap.addMarker(new MarkerOptions().position(markerLatLng).title(villaModel.getTitle()).icon(BitmapDescriptorFactory.fromResource(R.drawable.small_logo))).showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, 13.0f));

    }

    public void map_(View view) {
        Log.d("latlng", villaModel.getLat() + "   " + villaModel.getLng());

        if (villaModel.getLat() > -90 && villaModel.getLat() < 90 && villaModel.getLng() > -180 && villaModel.getLng() < 180) {
            Intent intent = new Intent(com.moutimid.vellarentapp.vellarentappadmin.activities.VillaDetailsActivity.this, MapActivity.class);
            intent.putExtra("lat", villaModel.getLat());
            intent.putExtra("lng", villaModel.getLng());
            intent.putExtra("name", villaModel.getName());
            startActivity(intent);

        } else {
            Toast.makeText(com.moutimid.vellarentapp.vellarentappadmin.activities.VillaDetailsActivity.this, "Invalid Coordinates to show marker", Toast.LENGTH_SHORT).show();
        }
    }

    public void showCustomDialogue(Context context, Villa villa) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialogue_layout, null);
        builder.setView(view);

        final EditText editText1 = view.findViewById(R.id.editText1);
        final EditText editText2 = view.findViewById(R.id.editText2);

        editText1.setText(villa.getBill() + "");
        editText2.setText(villa.getBedroom() + "");

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle button click
                String text1 = editText1.getText().toString();
                String text2 = editText2.getText().toString();
                propertyRef.child("bill").setValue(Integer.parseInt(text1));
                propertyRef.child("bedroom").setValue(Integer.parseInt(text2)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        finish();
                    }
                });
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle button click
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void displayTextInTextViews(String inputString) {
        // Split the input string by commas, considering optional spaces
        String[] strings = inputString.split("\\s*,\\s*");
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();
        Log.d("data", "st" + strings);
        // Create TextViews for non-empty extracted strings and add them to the LinearLayout
        for (String str : strings) {
            // Skip empty strings

            if (!str.trim().isEmpty()) {
                TextView textView = new TextView(this);
                Log.d("data", "str" + str);
                textView.setText(str);
                linearLayout.addView(textView);
                Stash.put("onetime", false);

            }
        }
    }

}