package com.ika.kof.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.joda.time.LocalDateTime;

/**
 * Created by Alhudaghifari on 7/25/2017.
 */
public class DataGraphic extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "GRAPHIC";
    private static final String ID = "id";
    private static final String DATE = "pointdate";
    private static final String SUMPRESS = "sumpress";

    public DataGraphic(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
               DATE + " DATE, " + SUMPRESS + " INTEGER)";
        db.execSQL(createTable);
        Log.i("Database : ","created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Returns all the data from database
     * @return
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getDataFromMonthYear(String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " +
                "STRFTIME('%d', " + DATE + ")" + ", " + SUMPRESS +
                " FROM " + TABLE_NAME +
                "WHERE STRFTIME('%M-%Y', " + DATE + ") = " + time;
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public Cursor getDataFromMonthYearNow() {
        LocalDateTime now = LocalDateTime.now();
        String time = "'" + now.getYear() + "-" + now.getMonthOfYear() + "'";

        Log.w("time : ",time + "");

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " + TABLE_NAME + " order by " + ID + " asc";
//                "SELECT " +
//                "STRFTIME('%d', " + DATE + ")" + ", " + SUMPRESS +
//                " FROM " + TABLE_NAME +
//                " WHERE STRFTIME('%y-%M', " + DATE + ") = " + time +
//                " ORDER BY " + ID + " ASC";
        Log.w("query:",query);
        Cursor data = db.rawQuery(query, null);
        Log.d("getDataGraphic:","enter");
        return data;
    }

    public boolean addData(String datevalue, String sumpress) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE, datevalue);
        contentValues.put(SUMPRESS,sumpress);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
}
