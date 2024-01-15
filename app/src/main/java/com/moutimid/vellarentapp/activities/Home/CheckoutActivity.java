package com.moutimid.vellarentapp.activities.Home;



import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fxn.stash.Stash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.MainActivity;
import com.moutimid.vellarentapp.activities.notification.Constants;
import com.moutimid.vellarentapp.activities.notification.NotificationScheduler;
import com.moutimid.vellarentapp.helper.Config;
import com.moutimid.vellarentapp.model.Villa;
import com.moutimid.vellarentapp.rentownerapp.model.Booking;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {
    // Declare the variables
    private EditText etCardNumber;
    private EditText etName;
    int j = 0;
    private EditText etNumberOfPersons;
    private EditText etDate;
    private Button btnContinue;

    // Declare the Firebase database reference
    private DatabaseReference mDatabase;
    Villa villaModel;
    String token;

    static DatabaseReference propertyRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Initialize the Firebase database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize the views
        etCardNumber = findViewById(R.id.et_card_number);
        etName = findViewById(R.id.et_name);
        etNumberOfPersons = findViewById(R.id.et_number_of_persons);
        etDate = findViewById(R.id.et_date);
        btnContinue = findViewById(R.id.btn_continue);
        villaModel = (Villa) Stash.getObject(Config.currentModel, Villa.class);
        // Set the click listener for the continue button
        etName.setText(Stash.getString("username"));
        etNumberOfPersons.setText(Stash.getInt("numbers") + "");
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

        if (Stash.getString("dates").isEmpty()) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate1 = currentDate.format(formatter);
            etDate.setText(formattedDate1);
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
            etDate.setText(formattedDate);
        }
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCardNumber.getText().toString().isEmpty()) {
                    etCardNumber.setError("Please Enter");
                } else if (etName.getText().toString().isEmpty()) {
                    etName.setError("Please Enter");

                } else if (etNumberOfPersons.getText().toString().isEmpty()) {
                    etNumberOfPersons.setError("Please Enter");

                } else if (etDate.getText().toString().isEmpty()) {
                    etDate.setError("Please Enter");
                } else if (etDate.getText().toString().isEmpty()) {
                    etDate.setError("Please Enter");
                } else {
                    ArrayList<String> villaModels = Stash.getArrayList(Config.count, String.class);
                    Log.d("dtaes", villaModels.toString() + "  nm");

                    if (villaModels != null) {
                        for (int i = 0; i < villaModels.size(); i++) {
                            if (villaModels.get(i).toString().equals(etDate.getText().toString())) {
                                j = 1;
                                Config.alertDialogue(CheckoutActivity.this, "Villa Rent App", "You already booked villa for this date (" + etDate.getText().toString() + ")\"", true);
                                break;
                            }
                        }
                        if (j == 0) {
                            Config.showProgressDialog(CheckoutActivity.this);
                            String cardNumber = etCardNumber.getText().toString();
                            String name = etName.getText().toString();
                            int numberOfPersons = Integer.parseInt(etNumberOfPersons.getText().toString());
                            String date = etDate.getText().toString();
                            // Create a new booking object
                            Booking booking = new Booking(cardNumber, name, numberOfPersons, date, token, FirebaseAuth.getInstance().getCurrentUser().getUid(), false);
                            // Save the booking object to the Firebase database
                            mDatabase.child("RentApp").child("Bookings").child(villaModel.ownerID).push().setValue(booking).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    ArrayList<String> villaModelArrayList = Stash.getArrayList(Config.count, String.class);
                                    villaModelArrayList.add(date);
                                    setNotificationFor48HoursBeforeExpiry(date, villaModel.getName());
                                    Stash.put(Config.count, villaModelArrayList);
                                    propertyRef = database.getReference("RentApp").child("Villas").child(villaModel.getKey());
                                    int numbers = villaModel.getBedroom() - Stash.getInt("numbers");
                                    propertyRef.child("bedroom").setValue(numbers).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Config.alertDialogue(CheckoutActivity.this, "Thank you for your reservation.", "You will receive a confirmation 48h before your booking date", true);
                                            Config.dismissProgressDialog();
                                        }
                                    });

                                }
                            });
//                                sendFCMPush(villaModel.token);


                        }

                    }
                    // Get the input values

                    // Show a success message to the user
//                    Toast.makeText(CheckoutActivity.this, "Booking saved successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendFCMPush(String token) {
        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();

        try {

            notifcationBody.put("title", "Booking Alert");
            notifcationBody.put("message", villaModel.getName() + " send request for booking");
            notification.put("to", token); // Use the FCM token of the recipient device
            notification.put("data", notifcationBody);
            Log.e("DATAAAAAA", notification.toString());
        } catch (JSONException e) {
            Log.e("DATAAAAAA", e.toString());
            Config.dismissProgressDialog();

            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.POST,
                Config.NOTIFICATIONAPIURL,
                notification,
                response -> {

//                    Config.alertDialogue(CheckoutActivity.this, "Thank you for your option, you will have the confirmation of your booking at 8am on the day of your option.", true);
                    Config.dismissProgressDialog();

                    Log.e("True", response + "");
                    Log.d("Response", response.toString());
                },
                error -> {
                    Config.dismissProgressDialog();
//                    Config.alertDialogue(CheckoutActivity.this, "Something went wrong. Please try again later", true);

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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 1000 * 60; // 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }

    private void setNotificationFor48HoursBeforeExpiry(String expiryDate, String name) {

        Calendar expiryCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        try {
            Date parsedExpiryDate = dateFormat.parse(expiryDate);
            expiryCalendar.setTime(parsedExpiryDate);

            // Set the notification date to 48 hours before expiry
            expiryCalendar.add(Calendar.HOUR_OF_DAY, -48);
            Log.d("date", expiryCalendar + "  ");

            // Check if the notification time is in the past, if yes, schedule it for the next day
            if (Calendar.getInstance().after(expiryCalendar)) {
                expiryCalendar.add(Calendar.DAY_OF_YEAR, 1);
            }

            // Schedule the notification
            String reminderMsg = "Booking for " + name + " is confirmed at " + expiryDate;

            NotificationScheduler.scheduleNotification(
                    CheckoutActivity.this, expiryCalendar,
                    reminderMsg, Constants.MEDICINE_REMINDER);

            // Save the reminder to the list
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("date", e.toString() + "  error");

            // Handle parsing exception
        }
    }


}
