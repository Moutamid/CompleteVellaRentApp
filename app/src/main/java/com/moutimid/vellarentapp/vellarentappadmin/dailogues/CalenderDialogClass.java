package com.moutimid.vellarentapp.vellarentappadmin.dailogues;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.fxn.stash.Stash;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.vellarentappadmin.activities.VillaDetailsActivity;
import com.moutimid.vellarentapp.vellarentappadmin.helper.Config;
import com.moutimid.vellarentapp.vellarentappadmin.helper.EventDecorator;
import com.moutimid.vellarentapp.vellarentappadmin.model.Villa;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.threeten.bp.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CalenderDialogClass extends Dialog {



    final String DATE_FORMAT = "dd-MM-yyyy";

    int pink = 0;
    int gray = 1;

    MaterialCalendarView calendarView;
    Context c;

    public CalenderDialogClass(Context a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.calender_dailogue);
        Button next_button = findViewById(R.id.next_button);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Stash.put("onetime", true);

                c.startActivity(new Intent(c, VillaDetailsActivity.class));
            }
        });
        calendarView = findViewById(R.id.calendarView);
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateFormat.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_YEAR, 7);
        String dateAfter7Days = dateFormat.format(calendar.getTime());

        final LocalDate min = getLocalDate(currentDate);
        final LocalDate max = getLocalDate(dateAfter7Days);
        Villa villaModel = (Villa) Stash.getObject(Config.currentModel, Villa.class);

        String dateString = villaModel.available_dates;
        String[] dateArray = dateString.split(",");
        List<String> dateList = Arrays.asList(dateArray);
        List<String> filteredDates = dateList.stream()
                .filter(date -> date.compareTo(currentDate) >= 0 && date.compareTo(dateAfter7Days) <= 0)
                .collect(Collectors.toList());
        calendarView.state().edit().setMinimumDate(min).setMaximumDate(max).commit();

        setEvent(filteredDates, pink);

        calendarView.invalidateDecorators();
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
