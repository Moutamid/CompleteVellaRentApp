package com.moutimid.vellarentapp.rentownerapp.dailogues;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.helper.EventDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.DayFormatter;

import org.threeten.bp.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalenderDialogClass extends Dialog {

    MaterialCalendarView calendarView;
    Activity c;
    private Button selectButton;

    private List<CalendarDay> selectedDates;
    String key, dates;
    final String DATE_FORMAT = "dd-MM-yyyy";

    int pink = 0;

    public CalenderDialogClass(Activity a, String key, String dates) {
        super(a);
        this.c = a;
        this.key = key;
        this.dates = dates;
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
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateFormat.format(calendar.getTime());

        String dateString = dates;
        if (dates.length() > 1) {
            String[] dateArray = dateString.split(",");
            List<String> dateList = Arrays.asList(dateArray);

            setEvent(dateList, pink);

            calendarView.invalidateDecorators();
        }
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
                List<String> dateList = new ArrayList<>(); 
                StringBuilder selectedDatesString = new StringBuilder();
                for (CalendarDay date : selectedDates) {
                    selectedDatesString.append(date.getDate()).append(",");
                    dateList.add(date.getDate()+",");
                }
//                List<String> dateList = Arrays.asList(inputDates.split(","));

                // Define input and output date formats
                SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

                // Convert each date and append to the result
                StringBuilder resultDates = new StringBuilder();
                for (String dateStr : dateList) {
                    try {
                        // Parse the input date string
                        Date date = inputDateFormat.parse(dateStr);

                        // Format the date in the desired output format
                        String formattedDate = outputDateFormat.format(date);

                        // Append the formatted date to the result string
                        resultDates.append(formattedDate).append(", ");
                    } catch (ParseException e) {
                        e.printStackTrace(); // Handle the exception according to your needs
                    }
                }
                if(resultDates.length()<1)
                {
                    resultDates.append(dates);
                }

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference propertyRef = database.getReference("RentApp").child("Villas");
                propertyRef.child(key).child("available_dates").setValue(resultDates.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dismiss();

                    }
                });
            }
        });
    }

    void setEvent(List<String> dateList, int color) {
        List<LocalDate> localDateList = new ArrayList<>();

        for (String string : dateList) {
            LocalDate calendar = getLocalDate(string);
            if (calendar != null) {
                localDateList.add(calendar);
            }
        }

        List<CalendarDay> datesLeft = new ArrayList<>();
        List<CalendarDay> datesCenter = new ArrayList<>();
        List<CalendarDay> datesRight = new ArrayList<>();
        List<CalendarDay> datesIndependent = new ArrayList<>();

        for (LocalDate localDate : localDateList) {
            boolean right = false;
            boolean left = false;

            for (LocalDate day1 : localDateList) {
                if (localDate.isEqual(day1.plusDays(1))) {
                    left = true;
                }
                if (day1.isEqual(localDate.plusDays(1))) {
                    right = true;
                }
            }

            if (left && right) {
                datesCenter.add(CalendarDay.from(localDate));
            } else if (left) {
                datesLeft.add(CalendarDay.from(localDate));
            } else if (right) {
                datesRight.add(CalendarDay.from(localDate));
            } else {
                datesIndependent.add(CalendarDay.from(localDate));
            }
        }

        if (color == pink) {
            setDecor(datesCenter, R.drawable.p_center);
            setDecor(datesLeft, R.drawable.p_left);
            setDecor(datesRight, R.drawable.p_right);
            setDecor(datesIndependent, R.drawable.p_independent);
        } else {
            setDecor(datesCenter, R.drawable.g_center);
            setDecor(datesLeft, R.drawable.g_left);
            setDecor(datesRight, R.drawable.g_right);
            setDecor(datesIndependent, R.drawable.g_independent);
        }
    }

    void setDecor(List<CalendarDay> calendarDayList, int drawable) {
        calendarView.addDecorators(new EventDecorator(c, drawable, calendarDayList));
    }

    LocalDate getLocalDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        try {
            Date input = sdf.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(input);
            return LocalDate.of(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH));
        } catch (NullPointerException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }
}
