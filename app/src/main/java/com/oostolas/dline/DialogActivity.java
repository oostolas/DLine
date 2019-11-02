package com.oostolas.dline;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DialogActivity extends Activity {

    private TimePicker timePicker;
    private EditText editTextName;
    private long date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        timePicker = findViewById(R.id.timePicker);
        editTextName = findViewById(R.id.editTextName);
        CalendarView calendarView = findViewById(R.id.calendarView);
        Calendar calendar = new GregorianCalendar();
        date = (new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))).getTime().getTime();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = (new GregorianCalendar(year, month,dayOfMonth)).getTime().getTime();
            }
        });

    }

    public void onButtonClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            date =  date + (timePicker.getHour() * 3600000) + (timePicker.getMinute() * 60000);
        else
            date = date + (timePicker.getCurrentHour() * 3600000) + (timePicker.getCurrentMinute() * 60000);
        Intent intent = new Intent();
        intent.putExtra("name", editTextName.getText().toString());
        intent.putExtra("date", date);
        setResult(RESULT_OK, intent);
        finish();
    }
}