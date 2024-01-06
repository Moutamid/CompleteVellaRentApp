package com.moutimid.vellarentapp.rentownerapp.dailogues;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.fxn.stash.Stash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.rentownerapp.helper.Config;
import com.moutimid.vellarentapp.rentownerapp.model.Villa;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.DayFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalenderDialogClass extends Dialog {

    final List<String> pinkDateList = Arrays.asList(
            "2023-01-19",
            "2023-01-23",
            "2023-01-22");
//    final List<String> grayDateList = Arrays.asList(
//            "2023-01-02", "2023-01-06");

    final String DATE_FORMAT = "dd-MM-yyyy";

    int pink = 0;
    int gray = 1;

    MaterialCalendarView calendarView;
    Activity c;
    private Button selectButton;

    private List<CalendarDay> selectedDates;

    public CalenderDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.calender_dailogue);
        Button next_button = findViewById(R.id.next_button);
        calendarView = findViewById(R.id.calendarView);
        selectedDates = new ArrayList<>();
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if (selected) {
                    selectedDates.add(date);
                } else {
                    selectedDates.remove(date);
                }
                calendarView.invalidateDecorators();
            }
        });

        calendarView.setDayFormatter(new DayFormatter() {
            @Override
            public String format(CalendarDay day) {
                if (selectedDates.contains(day)) {
                    return "✓ " + day.getDay();
                } else {
                    return String.valueOf(day.getDay());
                }
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder selectedDatesString = new StringBuilder();
                for (CalendarDay date : selectedDates) {
                    selectedDatesString.append(date.getDate()).append(",");
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                Villa villaModel = (Villa) Stash.getObject(Config.currentModel, Villa.class);
                DatabaseReference propertyRef = database.getReference("RentApp").child("Villas");
                propertyRef.child(villaModel.getKey()).child("available_dates").setValue(selectedDatesString.toString());
                propertyRef.child(villaModel.getKey()).child("available").setValue("available".toString());
                dismiss();
            }
        });
    }
}