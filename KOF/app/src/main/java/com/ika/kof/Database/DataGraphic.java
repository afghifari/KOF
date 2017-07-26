package com.ika.kof.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created and edited by Alhudaghifari on 7/25/2017.
 * with source code from Gulajava Ministudio.
 */

public class DataGraphic extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "GRAPHIC";
    private static final String ID = "id";
    private static final String DATE = "pointdate";
    private static final String SUMPRESS = "sumpress";

    private int sumpress;
    private boolean checktoday;
    private String currentTime;

    public DataGraphic(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        inisialisasiDataAwal();
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
               DATE + " TEXT, " + SUMPRESS + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private void inisialisasiDataAwal() {
        sumpress = 0;
        checktoday = false;
    }

    public void checkToday() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        currentTime = df.format(c.getTime());
    }

    //todo catatan : pas oncreate di fragmentgraph cek apakah sudah pernah menyimpan databas apa belum
    //todo, caranya adalah dengan membuat lagi boolean dalam sharedpreference yang berisi dia true jika sudah menyimpan
    // todo, null jika belum menyimpan

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
