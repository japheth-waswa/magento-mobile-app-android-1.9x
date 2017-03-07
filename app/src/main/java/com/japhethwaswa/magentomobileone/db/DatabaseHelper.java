package com.japhethwaswa.magentomobileone.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.japhethwaswa.magentomobileone.db.JumboContract.PagerEntry;
import com.japhethwaswa.magentomobileone.db.JumboContract.MainEntry;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "jumboapp.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_MAIN_CREATE=
            "CREATE TABLE " + MainEntry.TABLE_NAME + " (" +
                    MainEntry._ID + " INTEGER PRIMARY KEY, " +
                    MainEntry.COLUMN_CATEGORY_ID + " TEXT, " +
                    MainEntry.COLUMN_PRODUCT_ID + " TEXT, " +
                    MainEntry.COLUMN_IMAGE_URL + " TEXT, " +
                    MainEntry.COLUMN_KEY_HOME + " TEXT, " +
                    MainEntry.COLUMN_SECTION + " TEXT, " +
                    MainEntry.COLUMN_UPDATED_AT + " TEXT " +
                    ")";
    private static final String TABLE_PAGERS_CREATE=
            "CREATE TABLE " + PagerEntry.TABLE_NAME + " (" +
                    PagerEntry._ID + " INTEGER PRIMARY KEY, " +
                    PagerEntry.COLUMN_TITLE + " TEXT, " +
                    PagerEntry.COLUMN_BRIEF_DESCRIPTION + " TEXT, " +
                    PagerEntry.COLUMN_IMAGE_URL_LOCAL + " TEXT, " +
                    PagerEntry.COLUMN_IMAGE_URL_REMOTE + " TEXT, " +
                    PagerEntry.COLUMN_UPDATED_AT + " TEXT " +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL(TABLE_PAGERS_CREATE);
    db.execSQL(TABLE_MAIN_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PagerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MainEntry.TABLE_NAME);
        onCreate(db);
    }
}
