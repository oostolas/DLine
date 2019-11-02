package com.oostolas.dline;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {private static final int DB_VERSION = 1;
    private static final String DB_NAME = "data";

    static final String TABLE_NAME = "tasks";
    static final String DATE = "date";
    static final String NAME = "name";
    static final String COMMENT = "comment";
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( _id integer primary key autoincrement, "
            + DATE + " TEXT, " + NAME + " TEXT, " + COMMENT + " TEXT)";

    DbHelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
