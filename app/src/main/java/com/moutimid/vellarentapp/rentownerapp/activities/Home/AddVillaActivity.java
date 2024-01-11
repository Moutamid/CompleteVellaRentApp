package com.moutimid.vellarentapp.rentownerapp.activities.Home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.icu.lang.UCharacter;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.rentownerapp.adapter.ImageAdapter;
import com.moutimid.vellarentapp.rentownerapp.model.Property;
import com.moutimid.vellarentapp.rentownerapp.model.Rules;
import com.moutimid.vellarentapp.rentownerapp.model.VillaAmenities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddVillaActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_GALLERY = 111;
    ImageView profile_pic;
    Uri image_profile_str = null;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText bedroomEditText;
    private EditText bathroomEditText;
    private EditText latEditText;
    private EditText lngEditText;
    private EditText locationTitleEditText;
    private EditText billEditText;
    private EditText person_edit_text;
    private CheckBox petFriendlyCheckbox;
    private CheckBox smokerFriendlyCheckbox;
    private CheckBox billsIncludedCheckbox;
    private CheckBox airConditioningCheckbox;
    private CheckBox dryerCheckbox;
    private CheckBox kitchenCheckbox;
    private CheckBox furnishedCheckbox;
    private CheckBox gardenCheckbox;
    private CheckBox heatingCheckbox;
    private CheckBox parkingCheckbox;
    private CheckBox tvCheckbox;
    private CheckBox washingMachineCheckbox;
    private CheckBox wifiCheckbox;
    private Spinner fullBathroomSpinner;
    private Spinner steamShowerSpinner;
    private Spinner toiletSpinner;
    private Spinner roomTypeSpinner;
    private Button addButton;
    String name;

    Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button selectImageButton;
    //    private ImageView selectedImageView;
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference propertyRef;
    String propertyKey;


    public static final int PICK_IMAGES_REQUEST = 1;

    private List<Uri> selectedImageUris;
    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteAdapter adapter;
    PlacesClient placesClient;
    public static String address;
    public static double lat;
    public static double lng;
    String token;
    private LinearLayout linearLayout;
    private Button addButtonRule, getAdress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_villa);
        propertyRef = database.getReference("RentApp").child("Villas");
        propertyKey = propertyRef.push().getKey();
        getAdress = findViewById(R.id.getAdress);
        linearLayout = findViewById(R.id.linearLayout);
        addButtonRule = findViewById(R.id.addButton);

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("images");

        name = Stash.getString("userModel");

        String apiKey = "AIzaSyAuIxeEpQQgN84bBitDRksZTcLHtIKSAeY";
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                    return;
                }
                token = task.getResult();
            }
        });

        placesClient = Places.createClient(this);
        initAutoCompleteTextView();
        profile_pic = findViewById(R.id.image_view);
        nameEditText = findViewById(R.id.name_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        bedroomEditText = findViewById(R.id.bedroom_edit_text);
        bathroomEditText = findViewById(R.id.bathroom_edit_text);
        latEditText = findViewById(R.id.lat_edit_text);
        lngEditText = findViewById(R.id.lng_edit_text);
        locationTitleEditText = findViewById(R.id.location_title_edit_text);
        billEditText = findViewById(R.id.bill_edit_text);
        petFriendlyCheckbox = findViewById(R.id.pet_friendly_checkbox);
        smokerFriendlyCheckbox = findViewById(R.id.smoker_friendly_checkbox);
        billsIncludedCheckbox = findViewById(R.id.bills_included_checkbox);
        airConditioningCheckbox = findViewById(R.id.air_conditioning_checkbox);
        dryerCheckbox = findViewById(R.id.dryer_checkbox);
        kitchenCheckbox = findViewById(R.id.kitchen_checkbox);
        furnishedCheckbox = findViewById(R.id.furnished_checkbox);
        gardenCheckbox = findViewById(R.id.garden_checkbox);
        heatingCheckbox = findViewById(R.id.heating_checkbox);
        parkingCheckbox = findViewById(R.id.parking_checkbox);
        tvCheckbox = findViewById(R.id.tv_checkbox);
        washingMachineCheckbox = findViewById(R.id.washing_machine_checkbox);
        wifiCheckbox = findViewById(R.id.wifi_checkbox);
        fullBathroomSpinner = findViewById(R.id.full_bathroom_spinner);
        steamShowerSpinner = findViewById(R.id.steam_shower_spinner);
        toiletSpinner = findViewById(R.id.toilet_spinner);
        roomTypeSpinner = findViewById(R.id.room_type_spinner);
        addButton = findViewById(R.id.btnAddOwner);
        person_edit_text = findViewById(R.id.person_edit_text);
        selectImageButton = findViewById(R.id.button_choose_image);
//        selectedImageView = findViewById(R.id.selected_image_view);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        selectedImageUris = new ArrayList<>();
        imageAdapter = new ImageAdapter(selectedImageUris);
        recyclerView.setAdapter(imageAdapter);
        getAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!latEditText.getText().toString().isEmpty() && !lngEditText.getText().toString().isEmpty()) {
                    location_data();
                } else {
                    Toast.makeText(AddVillaActivity.this, "Please Enter Latitude and Longitude", Toast.LENGTH_SHORT).show();
                }
            }
        });
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if no checkbox is unchecked
//                if (!petFriendlyCheckbox.isChecked() || !smokerFriendlyCheckbox.isChecked() || !billsIncludedCheckbox.isChecked() ||
//                        !airConditioningCheckbox.isChecked() || !dryerCheckbox.isChecked() || !kitchenCheckbox.isChecked() ||
//                        !furnishedCheckbox.isChecked() || !gardenCheckbox.isChecked() || !heatingCheckbox.isChecked() ||
//                        !parkingCheckbox.isChecked() || !tvCheckbox.isChecked() || !washingMachineCheckbox.isChecked() ||
//                        !wifiCheckbox.isChecked()) {
//                    Toast.makeText(AddVillaActivity.this, "Please give complete details", Toast.LENGTH_SHORT).show();
//                    return;
//                    // Handle the case when a checkbox is unchecked
//                    // Add your logic here
//                }

                // Check if no EditText is empty
                if (nameEditText.getText().toString().isEmpty() || descriptionEditText.getText().toString().isEmpty() || bedroomEditText.getText().toString().isEmpty() || bathroomEditText.getText().toString().isEmpty() || latEditText.getText().toString().isEmpty() || lngEditText.getText().toString().isEmpty() || locationTitleEditText.getText().toString().isEmpty() || billEditText.getText().toString().isEmpty()) {
                    Toast.makeText(AddVillaActivity.this, "Please give complete details", Toast.LENGTH_SHORT).show();
                    return;
                    // Handle the case when an EditText is empty
                    // Add your logic here
                }

                // Store each value in each variable and assign values to variables of checkboxes
                String name = nameEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String bedroom = bedroomEditText.getText().toString();
                String bathroom = bathroomEditText.getText().toString();
                String lat = latEditText.getText().toString();
                String lng = lngEditText.getText().toString();
                String locationTitle = locationTitleEditText.getText().toString();
                String bill = billEditText.getText().toString();
                boolean isPetFriendly = petFriendlyCheckbox.isChecked();
                boolean isSmokerFriendly = smokerFriendlyCheckbox.isChecked();
                boolean areBillsIncluded = billsIncludedCheckbox.isChecked();
                boolean hasAirConditioning = airConditioningCheckbox.isChecked();
                boolean hasDryer = dryerCheckbox.isChecked();
                boolean hasKitchen = kitchenCheckbox.isChecked();
                boolean isFurnished = furnishedCheckbox.isChecked();
                boolean hasGarden = gardenCheckbox.isChecked();
                boolean hasHeating = heatingCheckbox.isChecked();
                boolean hasParking = parkingCheckbox.isChecked();
                boolean hasTV = tvCheckbox.isChecked();
                boolean hasWashingMachine = washingMachineCheckbox.isChecked();
                boolean hasWiFi = wifiCheckbox.isChecked();
                String fullBathroom = fullBathroomSpinner.getSelectedItem().toString();
                String steamShower = steamShowerSpinner.getSelectedItem().toString();
                String toilet = toiletSpinner.getSelectedItem().toString();
                String roomType = roomTypeSpinner.getSelectedItem().toString();
                if (selectedImageUris.isEmpty()) {
                    Toast.makeText(AddVillaActivity.this, "No images selected", Toast.LENGTH_SHORT).show();
                    return;
                }
// Create a new Property object
                Property property = new Property();

// Set the property details
                property.name = nameEditText.getText().toString();
                property.description = descriptionEditText.getText().toString();
                property.bedroom = Integer.parseInt(bedroomEditText.getText().toString());
                property.bathRoom = Integer.parseInt("87");
                property.lat = Double.parseDouble(latEditText.getText().toString());
                property.lng = Double.parseDouble(lngEditText.getText().toString());
                property.title = locationTitleEditText.getText().toString();
                property.bill = Integer.parseInt(billEditText.getText().toString());
                property.token = token;

                Rules houseRules = new Rules();

// Set the house rules
//                HouseRules houseRules = new HouseRules();
                houseRules.petFriendly = petFriendlyCheckbox.isChecked();
                houseRules.smokerFriendly = smokerFriendlyCheckbox.isChecked();

// Set the property amenities
                VillaAmenities propertyAmenities = new VillaAmenities();
                propertyAmenities.airConditioning = airConditioningCheckbox.isChecked();
                propertyAmenities.dryer = dryerCheckbox.isChecked();
                propertyAmenities.equippedKitchen = kitchenCheckbox.isChecked();
                propertyAmenities.furnished = furnishedCheckbox.isChecked();
                propertyAmenities.garden = gardenCheckbox.isChecked();
                propertyAmenities.heating = heatingCheckbox.isChecked();
                propertyAmenities.parking = parkingCheckbox.isChecked();
                propertyAmenities.tv = tvCheckbox.isChecked();
                propertyAmenities.washingMachine = washingMachineCheckbox.isChecked();
                propertyAmenities.wifi = wifiCheckbox.isChecked();


// Store the property data in Firebase Realtime Database
                Dialog lodingbar = new Dialog(AddVillaActivity.this);
                lodingbar.setContentView(R.layout.loading);
                Objects.requireNonNull(lodingbar.getWindow()).setBackgroundDrawable(new ColorDrawable(UCharacter.JoiningType.TRANSPARENT));
                lodingbar.setCancelable(false);
                lodingbar.show();
                String filePathName = "villas/";
                final String timestamp = "" + System.currentTimeMillis();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathName + timestamp);
                UploadTask urlTask = storageReference.putFile(image_profile_str);
                Task<Uri> uriTask = urlTask.continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(AddVillaActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    return storageReference.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadImageUri = task.getResult();
                        if (downloadImageUri != null) {
                            property.image = downloadImageUri.toString();
                            property.userName = name;

                            property.rules = String.valueOf(getData());
                            property.no_of_persons = 23;
                            property.available = "not_available";
                            property.available_dates = "";
                            property.ownerID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                            property.verified = false;
                            property.key = propertyKey;
                            propertyRef.child(propertyKey).setValue(property);
                            propertyRef.child(propertyKey).child("PropertyAmenities").setValue(propertyAmenities);

                            propertyRef.child(propertyKey).child("HouseRules").setValue(houseRules).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    saveImagesToFirebase(lodingbar);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddVillaActivity.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();

                                    lodingbar.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        });

        addButtonRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEditText();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Images"), PICK_IMAGES_REQUEST);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK) {
            image_profile_str = data.getData();
            profile_pic.setImageURI(image_profile_str);
            profile_pic.setVisibility(View.VISIBLE);
            selectedImageUris.add(image_profile_str);


        }
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    selectedImageUris.add(imageUri);
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                selectedImageUris.add(imageUri);
            }
            imageAdapter.notifyDataSetChanged();
        }
//        Toast.makeText(this, ""+selectedImageUris.size()+"  size", Toast.LENGTH_SHORT).show();
    }

    public void profile_image(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY);
    }

    public void BackPress(View view) {
        onBackPressed();
    }

    private void saveImagesToFirebase(Dialog loadingbar) {


        StorageReference storageRef = FirebaseStorage.getInstance().getReference("images");

        for (Uri imageUri : selectedImageUris) {
            // Create a unique filename for each image
            String imageName = System.currentTimeMillis() + "_" + imageUri.getLastPathSegment();

            // Upload image to Firebase Storage
            StorageReference imageRef = storageRef.child(imageName);
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded successfully, get download URL
                        imageRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                            // Save download URL to Firebase Database
                            String imageUrl = downloadUrl.toString();
                            propertyRef.child(propertyKey).child("images").push().setValue(imageUrl);
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle upload failure
                        Toast.makeText(AddVillaActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    });
        }

//        Toast.makeText(AddVillaActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
        loadingbar.dismiss();
        selectedImageUris.clear();
        imageAdapter.notifyDataSetChanged();
        finish();
    }

    private void initAutoCompleteTextView() {

        autoCompleteTextView = findViewById(R.id.location_title_edit_text);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setOnItemClickListener(autocompleteClickListener);
        adapter = new AutoCompleteAdapter(this, placesClient);
        autoCompleteTextView.setAdapter(adapter);
    }

    private final AdapterView.OnItemClickListener autocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            try {
                final AutocompletePrediction item = adapter.getItem(i);
                String placeID = null;
                if (item != null) {
                    placeID = item.getPlaceId();
                }

//                To specify which data types to return, pass an array of Place.Fields in your FetchPlaceRequest
//                Use only those fields which are required.

                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);

                FetchPlaceRequest request = null;
                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields)
                            .build();
                }

                if (request != null) {
                    placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(FetchPlaceResponse task) {
                            address = task.getPlace().getAddress();
                            lat = task.getPlace().getLatLng().latitude;
                            lng = task.getPlace().getLatLng().longitude;
                            latEditText.setVisibility(View.VISIBLE);
                            lngEditText.setVisibility(View.VISIBLE);
                            lngEditText.setText(lng + "");
                            latEditText.setText(lat + "");
                            locationTitleEditText.setText(address);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private void addEditText() {
        EditText editText = new EditText(this);
        linearLayout.addView(editText);

        location_data();
    }

    public void location_data() {
        try {
            double latitude = Double.parseDouble(latEditText.getText().toString()); // Replace with your latitude
            double longitude = Double.parseDouble(lngEditText.getText().toString()); // Replace with your longitude

            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());


            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            locationTitleEditText.setText(address);
        } catch (Exception e) {
            Toast.makeText(this, "error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private StringBuilder getData() {
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View view = linearLayout.getChildAt(i);
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                String text = editText.getText().toString();
                data.append(text).append(" , ");

            }
        }


        // Do something with the data
        return data;
    }
}


