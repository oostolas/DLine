package com.oostolas.dline;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView textTimer;
    private TextView textName;
    private ListAdapter adapter;
    private ArrayList<ListItem> listItems = new ArrayList<>();
    private DbHelper dbHelper = new DbHelper(this);
    private int primaryTimeIndex = -1;

    private void synchDatabase(){
        listItems.clear();
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DbHelper.TABLE_NAME, null, null, null, null,null,DbHelper.DATE + " ASC");
        if(cursor.moveToFirst()) {
            int dateColumnIndex = cursor.getColumnIndex(DbHelper.DATE);
            int commentColumnIndex = cursor.getColumnIndex(DbHelper.NAME);
            for(int id = 0; id == 0 || cursor.moveToNext(); id++)
                listItems.add(new ListItem(
                        id,
                        new Date(cursor.getLong(dateColumnIndex) - System.currentTimeMillis()),
                        cursor.getString(commentColumnIndex)
                ));
            cursor.close();
        }
        database.close();
        for(int i = 0; i < listItems.size(); i++) {
            if(listItems.get(i).date.getTime() > 0) {
                primaryTimeIndex = i;
                textName.setText(listItems.get(i).name);
                break;
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textName = findViewById(R.id.textComment);
        textTimer = findViewById(R.id.textTimer);
        synchDatabase();
        ListView listView = findViewById(R.id.listView);
        adapter = new ListAdapter(this, listItems);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder
                        .setMessage("Delete?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog dialog = builder.create();
                dialog.show();


                Toast.makeText(getApplicationContext(), "long clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                Intent intent = new Intent(MainActivity.this, DialogActivity.class);
                startActivityForResult(intent, 1);
                Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
            }
        });
        adapter.notifyDataSetChanged();


        //запуск таймера

        Timer timer = new Timer();
        PrimaryTimerTask primaryTimerTask = new PrimaryTimerTask();
        ListTimerTask listTimerTask = new ListTimerTask();

        timer.schedule(primaryTimerTask, 0, 30);
        timer.schedule(listTimerTask, 0, 1000);
    }

    class PrimaryTimerTask extends TimerTask {
        @Override
        public void run() {
            if(listItems.isEmpty() || primaryTimeIndex == -1) return;
            Date date = listItems.get(primaryTimeIndex).date;
            long time = date.getTime();
            time -= 30;
            if(time < 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        synchDatabase();
                    }
                });
                return;
            }
            date.setTime(time);
            final String out = ListItem.timeFormat(time);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textTimer.setText(out);
                }
            });
        }
    }


    class ListTimerTask extends  TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < listItems.size(); i++) {
                        if(i == primaryTimeIndex) continue;
                        ListItem listItem = listItems.get(i);
                        Date date = listItems.get(i).date;
                        date.setTime(date.getTime() - 1000);
                        listItems.set(i, listItem);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null) return;
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Date date = new Date(data.getLongExtra("date", 0L));
        String name = data.getStringExtra("name");
        contentValues.put(DbHelper.DATE, date.getTime());
        contentValues.put(DbHelper.NAME, name);
        database.insert(DbHelper.TABLE_NAME, null, contentValues);
        database.close();
        synchDatabase();
        adapter.notifyDataSetChanged();
    }


    public void onFabClick(View view) {
        Intent intent = new Intent(this, DialogActivity.class);
        startActivityForResult(intent, 1);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds listItems to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}