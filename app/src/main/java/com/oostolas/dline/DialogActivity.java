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
    private EditText editTextComment;
    private String id;
    private long date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        timePicker = findViewById(R.id.timePicker);
        editTextName = findViewById(R.id.editTextName);
        editTextComment = findViewById(R.id.editTextComment);
        CalendarView calendarView = findViewById(R.id.calendarView);
        Calendar calendar = new GregorianCalendar();
        Intent data = getIntent();
        id = data.getStringExtra("id");
        String name = data.getStringExtra("name");
        String comment = data.getStringExtra("comment");
        long time = data.getLongExtra("date", 0L);
        if(time != 0) {
            calendar.setTimeInMillis(time);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePicker.setMinute(calendar.get(Calendar.MINUTE));
                timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            }  else {
                timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
                timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
            }
            calendarView.setDate(time);
        }
        if(name != null) editTextName.setText(name);
        if(comment != null) editTextComment.setText(comment);
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
        intent.putExtra("id", id);
        intent.putExtra("name", editTextName.getText().toString());
        intent.putExtra("date", date);
        intent.putExtra("comment", editTextComment.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
