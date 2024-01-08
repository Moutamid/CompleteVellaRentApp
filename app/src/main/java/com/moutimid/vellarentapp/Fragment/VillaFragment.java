package com.moutimid.vellarentapp.Fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.fxn.stash.Stash;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.adapter.OwnVillaAdapter;
import com.moutimid.vellarentapp.dailogues.ChooseAvailableCalenderDialogClass;
import com.moutimid.vellarentapp.helper.Config;
import com.moutimid.vellarentapp.helper.Constants;
import com.moutimid.vellarentapp.model.HouseRules;
import com.moutimid.vellarentapp.model.PropertyAmenities;
import com.moutimid.vellarentapp.model.Villa;
import com.moutimid.vellarentapp.rentownerapp.activities.Home.AutoCompleteAdapter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VillaFragment extends Fragment {

    public static RecyclerView content_rcv1;
    public static List<Villa> productModelList = new ArrayList<>();
    public static List<Villa> filterModelList = new ArrayList<>();

    public static TextView no_text, searched_date;
    public static OwnVillaAdapter ownVillaAdapter;

    public static LottieAnimationView loading;
    public static AutoCompleteTextView autoCompleteTextView;
    public static AutoCompleteAdapter adapter;
    public static PlacesClient placesClient;
    public static String address = "";
    public static double lat;
    public static double lng;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_villa, container, false);
        String apiKey = "AIzaSyAuIxeEpQQgN84bBitDRksZTcLHtIKSAeY";
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), apiKey);
        }

        placesClient = Places.createClient(getActivity());
        autoCompleteTextView = view.findViewById(R.id.location_title_edit_text);
        searched_date = view.findViewById(R.id.searched_date);
        initAutoCompleteTextView();
        loading = view.findViewById(R.id.loading);
        content_rcv1 = view.findViewById(R.id.content_rcv1);
        no_text = view.findViewById(R.id.no_text);
        content_rcv1.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        ownVillaAdapter = new OwnVillaAdapter(getContext(), productModelList);
        content_rcv1.setAdapter(ownVillaAdapter);

        if (Config.isNetworkAvailable(getContext())) {
            getRecommendedProducts();
        } else {
            Toast.makeText(getContext(), "Network not available", Toast.LENGTH_SHORT).show();
        }
        if (Stash.getString("dates").isEmpty()) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = currentDate.format(formatter);
//            filter_dates(formattedDate);

        } else {
            String inputDate = Stash.getString("dates");
            int year = Integer.parseInt(inputDate.substring(inputDate.indexOf("year") + 6, inputDate.indexOf("}")));
            int month = Integer.parseInt(inputDate.substring(inputDate.indexOf("month") + 7, inputDate.lastIndexOf(",")));
            int day = Integer.parseInt(inputDate.substring(inputDate.indexOf("day") + 5, inputDate.indexOf("month") - 2));

            // Creating a Date object using the extracted values
            Date date = new Date(year - 1900, month - 1, day);
            String outputFormat = "dd-MM-yyyy";

            // Formatting the date to the desired output format
            SimpleDateFormat sdf = new SimpleDateFormat(outputFormat);
            String formattedDate = sdf.format(date);
            filter_dates(formattedDate);

        }

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 1) {
                    initAutoCompleteTextView();
                    adapter.setDisableSuggestions(false);
                    if (Stash.getString("dates").isEmpty()) {
                        LocalDate currentDate = LocalDate.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        String formattedDate = currentDate.format(formatter);
//                        filter_dates(formattedDate);

                    } else {
                        String inputDate = Stash.getString("dates");
                        int year = Integer.parseInt(inputDate.substring(inputDate.indexOf("year") + 6, inputDate.indexOf("}")));
                        int month = Integer.parseInt(inputDate.substring(inputDate.indexOf("month") + 7, inputDate.lastIndexOf(",")));
                        int day = Integer.parseInt(inputDate.substring(inputDate.indexOf("day") + 5, inputDate.indexOf("month") - 2));

                        // Creating a Date object using the extracted values
                        Date date = new Date(year - 1900, month - 1, day);
                        String outputFormat = "dd-MM-yyyy";

                        // Formatting the date to the desired output format
                        SimpleDateFormat sdf = new SimpleDateFormat(outputFormat);
                        String formattedDate = sdf.format(date);
                        filter_dates(formattedDate);

                    }
                } else {
                    if (Stash.getString("dates").isEmpty()) {
                        LocalDate currentDate = LocalDate.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        String formattedDate = currentDate.format(formatter);
                        filter_locations(charSequence.toString());

                    } else {
                        String inputDate = Stash.getString("dates");
                        int year = Integer.parseInt(inputDate.substring(inputDate.indexOf("year") + 6, inputDate.indexOf("}")));
                        int month = Integer.parseInt(inputDate.substring(inputDate.indexOf("month") + 7, inputDate.lastIndexOf(",")));
                        int day = Integer.parseInt(inputDate.substring(inputDate.indexOf("day") + 5, inputDate.indexOf("month") - 2));

                        // Creating a Date object using the extracted values
                        Date date = new Date(year - 1900, month - 1, day);
                        String outputFormat = "dd-MM-yyyy";

                        // Formatting the date to the desired output format
                        SimpleDateFormat sdf = new SimpleDateFormat(outputFormat);
                        String formattedDate = sdf.format(date);
                        filter_both(formattedDate, charSequence.toString());
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searched_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseAvailableCalenderDialogClass availableCalenderDialogClass = new ChooseAvailableCalenderDialogClass(getActivity());
                availableCalenderDialogClass.show();
            }
        });

        return view;
    }

    public static void getRecommendedProducts() {
//        Config.showProgressDialog(getContext());
        Constants.databaseReference().child(Config.villa).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productModelList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Villa villaModel = ds.getValue(Villa.class);
                    Log.d("dataa", villaModel.verified + " "+ villaModel.getBedroom());
                    if (villaModel.verified && villaModel.getBedroom() != 0) {
                        DataSnapshot propertyAmenities1 = ds.child("PropertyAmenities");
                        DataSnapshot houseRules1 = ds.child("HouseRules");
                        PropertyAmenities propertyAmenities = propertyAmenities1.getValue(PropertyAmenities.class);
                        HouseRules houseRules = houseRules1.getValue(HouseRules.class);
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
                        loading.setVisibility(View.GONE);
                    }
//                    stringArray = new String[productModelList.size()];
//                    for (int i = 0; i <= productModelList.size(); i++) {
//                        stringArray[i] = productModelList.get(i).town_name;
//
//                    }
//                    LocalDate currentDate = LocalDate.now();
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//                    String formattedDate = currentDate.format(formatter);
//                    filter_dates(formattedDate);
                    ownVillaAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loading.setVisibility(View.GONE);

            }


        });


    }

    public static void getRecommendedProductsDate() {
//        Config.showProgressDialog(getContext());
        Constants.databaseReference().child(Config.villa).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productModelList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Villa villaModel = ds.getValue(Villa.class);
                    Log.d("dataa", villaModel.isBills_included() + "");
                    if (villaModel.verified && villaModel.getBedroom() != 0) {
                        DataSnapshot propertyAmenities1 = ds.child("PropertyAmenities");
                        DataSnapshot houseRules1 = ds.child("HouseRules");
                        PropertyAmenities propertyAmenities = propertyAmenities1.getValue(PropertyAmenities.class);
                        HouseRules houseRules = houseRules1.getValue(HouseRules.class);
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
                        loading.setVisibility(View.GONE);
//                        filter_dates(Stash.getString("dates"));
                    }
//                    stringArray = new String[productModelList.size()];
//                    for (int i = 0; i <= productModelList.size(); i++) {
//                        stringArray[i] = productModelList.get(i).town_name;
//
//                    }
//                    LocalDate currentDate = LocalDate.now();
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//                    String formattedDate = currentDate.format(formatter);
//                    filter_dates(formattedDate);
                    ownVillaAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loading.setVisibility(View.GONE);

            }


        });


    }

    public static void filter(String text) {
        ArrayList<Villa> filteredlist = new ArrayList<Villa>();
        for (Villa item : productModelList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }

        }
        if (filteredlist.isEmpty()) {
            content_rcv1.setVisibility(View.GONE);
            no_text.setVisibility(View.VISIBLE);
        } else {

            content_rcv1.setVisibility(View.VISIBLE);
            no_text.setVisibility(View.GONE);
            ownVillaAdapter.filterList(filteredlist);

        }
    }

    public static void filter_both(String dates, String location) {
        Log.d("data", dates + "  " + location);
        ArrayList<Villa> filteredlist = new ArrayList<Villa>();
        for (Villa item : productModelList) {
            if (item.available_dates.contains(dates.toLowerCase()) && item.getTitle().toLowerCase().contains(location.toLowerCase())) {
                filteredlist.add(item);
                Log.d("data1", dates + "  " + location);
            }
        }
        if (filteredlist.isEmpty()) {

            content_rcv1.setVisibility(View.GONE);
            no_text.setVisibility(View.VISIBLE);
        } else {

            content_rcv1.setVisibility(View.VISIBLE);
            no_text.setVisibility(View.GONE);
            ownVillaAdapter.filterList(filteredlist);

        }
    }

    public static void filter_locations(String text) {
        autoCompleteTextView.setFocusable(true);

        ArrayList<Villa> filteredlist = new ArrayList<Villa>();
        for (Villa item : productModelList) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                Log.d("place", item + "");

                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            content_rcv1.setVisibility(View.GONE);
            no_text.setVisibility(View.VISIBLE);
        } else {

            content_rcv1.setVisibility(View.VISIBLE);
            no_text.setVisibility(View.GONE);
            ownVillaAdapter.filterList(filteredlist);
        }
    }

    public static void filter_dates(String text) {
        ArrayList<Villa> filteredlist = new ArrayList<Villa>();
        for (Villa item : productModelList) {
            if (item.available_dates.contains(text.toLowerCase())) {
                filteredlist.add(item);

            }
        }
        if (filteredlist.isEmpty()) {
            content_rcv1.setVisibility(View.GONE);
            no_text.setVisibility(View.VISIBLE);
        } else {

            content_rcv1.setVisibility(View.VISIBLE);
            no_text.setVisibility(View.GONE);


            ownVillaAdapter.filterList(filteredlist);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void initAutoCompleteTextView() {
//        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setOnItemClickListener(autocompleteClickListener);
        adapter = new AutoCompleteAdapter(getContext(), placesClient);
        autoCompleteTextView.setAdapter(adapter);
    }

    private final AdapterView.OnItemClickListener autocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            try {
                final AutocompletePrediction item = adapter.getItem(i);
                adapter.setDisableSuggestions(true);

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
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(FetchPlaceResponse task) {
                            address = task.getPlace().getAddress();
                            lat = task.getPlace().getLatLng().latitude;
                            lng = task.getPlace().getLatLng().longitude;
                            autoCompleteTextView.setText(address);
                            LocalDate currentDate = LocalDate.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            String formattedDate = currentDate.format(formatter);
                            if (Stash.getString("dates").isEmpty()) {
                                filter_locations(address);
                            } else {
                                filter_both(formattedDate, address);
                            }
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


}