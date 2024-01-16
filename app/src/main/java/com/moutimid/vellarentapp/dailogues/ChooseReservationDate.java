package com.moutimid.vellarentapp.dailogues;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.fxn.stash.Stash;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.Fragment.VillaFragment;
import com.moutimid.vellarentapp.activities.Home.CheckoutActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.DayFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ChooseReservationDate extends Dialog {

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
    public static String date_now;
    private List<CalendarDay> selectedDates= new ArrayList<>();

    public ChooseReservationDate(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.calender_dailogue_reserve);
        Button next_button = findViewById(R.id.next_button);
        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {


                if (selected) {
                    Stash.put("dates11", date);
                    String inputDate = Stash.getString("dates11");

                    int year = Integer.parseInt(inputDate.substring(inputDate.indexOf("year") + 6, inputDate.indexOf("}")));
                    int month = Integer.parseInt(inputDate.substring(inputDate.indexOf("month") + 7, inputDate.lastIndexOf(",")));
                    int day = Integer.parseInt(inputDate.substring(inputDate.indexOf("day") + 5, inputDate.indexOf("month") - 2));

                    // Creating a Date object using the extracted values
                    Date date1 = new Date(year - 1900, month - 1, day);
                    String outputFormat = "dd-MM-yyyy";

                    // Formatting the date to the desired output format
                    SimpleDateFormat sdf = new SimpleDateFormat(outputFormat);
                    date_now = sdf.format(date1);
                    CheckoutActivity.etDate.setText(date_now);

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

                dismiss();

            }
        });
    }
}