package com.japhethwaswa.magentomobileone.db;


import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.app.SplashActivity;
import com.japhethwaswa.magentomobileone.db.JumboContract.PagerEntry;
import com.japhethwaswa.magentomobileone.db.JumboContract.MainEntry;
import com.japhethwaswa.magentomobileone.db.JumboContract.CategoryEntry;
import com.japhethwaswa.magentomobileone.db.JumboContract.ProductEntry;

import static java.security.AccessController.getContext;

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
                    MainEntry.COLUMN_TITLE + " TEXT, " +
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

    private static final String TABLE_CATEGORY_CREATE=
            "CREATE TABLE " + CategoryEntry.TABLE_NAME + " (" +
                    CategoryEntry._ID + " INTEGER PRIMARY KEY, " +
                    CategoryEntry.COLUMN_ENTITY_ID + " TEXT, " +
                    CategoryEntry.COLUMN_CONTENT_TYPE + " TEXT, " +
                    CategoryEntry.COLUMN_LABEL + " TEXT, " +
                    CategoryEntry.COLUMN_PARENT_ID + " TEXT, " +
                    CategoryEntry.COLUMN_MY_PARENT_ID + " TEXT, " +
                    CategoryEntry.COLUMN_ICON + " TEXT, " +
                    CategoryEntry.COLUMN_MODIFICATION_TIME + " TEXT " +
                    ")";

    private static final String TABLE_PRODUCT_CREATE=
            "CREATE TABLE " + ProductEntry.TABLE_NAME + " (" +
                    ProductEntry._ID + " INTEGER PRIMARY KEY, " +
                    ProductEntry.COLUMN_NAME + " TEXT, " +
                    ProductEntry.COLUMN_ENTITY_ID + " TEXT, " +
                    ProductEntry.COLUMN_ENTITY_TYPE + " TEXT, " +
                    ProductEntry.COLUMN_SHORT_DESCRIPTION + " TEXT, " +
                    ProductEntry.COLUMN_DESCRIPTION + " TEXT, " +
                    ProductEntry.COLUMN_LINK + " TEXT, " +
                    ProductEntry.COLUMN_ICON + " TEXT, " +
                    ProductEntry.COLUMN_MODIFICATION_TIME + " TEXT, " +
                    ProductEntry.COLUMN_IN_STOCK + " TEXT, " +
                    ProductEntry.COLUMN_IS_SALABLE + " TEXT, " +
                    ProductEntry.COLUMN_HAS_GALLERY + " TEXT, " +
                    ProductEntry.COLUMN_HAS_OPTIONS + " TEXT, " +
                    ProductEntry.COLUMN_RATING_SUMMARY + " TEXT, " +
                    ProductEntry.COLUMN_REVIEWS_COUNT + " TEXT, " +
                    ProductEntry.COLUMN_PRICE_REGULAR + " TEXT, " +
                    ProductEntry.COLUMN_PRICE_SPECIAL + " TEXT, " +
                    ProductEntry.COLUMN_CATEGORY_IDS + " TEXT " +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL(TABLE_PAGERS_CREATE);
    db.execSQL(TABLE_MAIN_CREATE);
    db.execSQL(TABLE_CATEGORY_CREATE);
    db.execSQL(TABLE_PRODUCT_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PagerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MainEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME);
        onCreate(db);
    }

}
