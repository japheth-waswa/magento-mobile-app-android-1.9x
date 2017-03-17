package com.japhethwaswa.magentomobileone.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.japhethwaswa.magentomobileone.db.JumboContract.CONTENT_AUTHORITY;
import static com.japhethwaswa.magentomobileone.db.JumboContract.PATH_CATEGORY;
import static com.japhethwaswa.magentomobileone.db.JumboContract.PATH_MAIN;
import static com.japhethwaswa.magentomobileone.db.JumboContract.PATH_PAGER;
import static com.japhethwaswa.magentomobileone.db.JumboContract.PATH_PRODUCT;

public class JumboProvider extends ContentProvider {

    /**
     * constants for the operation
     **/
    private static final int PAGERS = 1;
    private static final int PAGER_ID = 2;
    private static final int MAINS = 3;
    private static final int MAIN_ID = 4;
    private static final int CATEGORY = 5;
    private static final int CATEGORY_ID = 6;
    private static final int PRODUCT = 7;
    private static final int PRODUCT_ID = 8;

    //Uri matcher
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        //pager data uri
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_PAGER, PAGERS);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_PAGER + "/#", PAGER_ID);

        //main data uri
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_MAIN, MAINS);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_MAIN + "/#", MAIN_ID);

        //category data uri
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_CATEGORY, CATEGORY);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_CATEGORY + "/#", CATEGORY_ID);

        //product data uri
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_PRODUCT, PRODUCT);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_PRODUCT + "/#", PRODUCT_ID);
    }

    private DatabaseHelper helper;

    @Override
    public boolean onCreate() {
        helper = new DatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor;
        int match = uriMatcher.match(uri);
        //4 possible scenarios
        switch (match) {
            case PAGERS:
                cursor = db.query(JumboContract.PagerEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PAGER_ID:
                selection = JumboContract.PagerEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(JumboContract.PagerEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MAINS:
                cursor = db.query(JumboContract.MainEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MAIN_ID:
                selection = JumboContract.MainEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(JumboContract.MainEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORY:
                cursor = db.query(JumboContract.CategoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORY_ID:
                selection = JumboContract.CategoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(JumboContract.CategoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PRODUCT:
                cursor = db.query(JumboContract.ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PRODUCT_ID:
                selection = JumboContract.ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(JumboContract.ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case PAGERS:
                return insertRecord(uri, values, JumboContract.PagerEntry.TABLE_NAME);
            case MAINS:
                return insertRecord(uri, values, JumboContract.MainEntry.TABLE_NAME);
            case CATEGORY:
                return insertRecord(uri, values, JumboContract.CategoryEntry.TABLE_NAME);
            case PRODUCT:
                return insertRecord(uri, values, JumboContract.ProductEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Unkwown uri: " + uri);

        }
    }

    private Uri insertRecord(Uri uri, ContentValues values, String tableName) {
        SQLiteDatabase db = helper.getWritableDatabase();
        long id = db.insert(tableName, null, values);
        //db.close();
        if (id == -1) {
            Log.e("Error", "insert error for uri: " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case PAGERS:
                return deleteRecord(uri, null, null, JumboContract.PagerEntry.TABLE_NAME);
            case PAGER_ID:
                return deleteRecord(uri, selection, selectionArgs, JumboContract.PagerEntry.TABLE_NAME);
            case MAINS:
                return deleteRecord(uri, null, null, JumboContract.MainEntry.TABLE_NAME);
            case MAIN_ID:
                return deleteRecord(uri, selection, selectionArgs, JumboContract.MainEntry.TABLE_NAME);
            case CATEGORY:
                return deleteRecord(uri, null, null, JumboContract.CategoryEntry.TABLE_NAME);
            case CATEGORY_ID:
                return deleteRecord(uri, selection, selectionArgs, JumboContract.CategoryEntry.TABLE_NAME);
            case PRODUCT:
                return deleteRecord(uri, null, null, JumboContract.ProductEntry.TABLE_NAME);
            case PRODUCT_ID:
                return deleteRecord(uri, selection, selectionArgs, JumboContract.ProductEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Insert unknown URI: " + uri);
        }
    }

    private int deleteRecord(Uri uri, String selection, String[] selectionArgs, String tableName) {
        //this time we need a writable database
        SQLiteDatabase db = helper.getWritableDatabase();
        int id = db.delete(tableName, selection, selectionArgs);
        //db.close();
        if (id == -1) {
            Log.e("Error", "delete unknown URI " + uri);
            return -1;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case PAGERS:
                return updateRecord(uri, values, selection, selectionArgs, JumboContract.PagerEntry.TABLE_NAME);

            case MAINS:
                return updateRecord(uri, values, selection, selectionArgs, JumboContract.MainEntry.TABLE_NAME);

            case CATEGORY:
                return updateRecord(uri, values, selection, selectionArgs, JumboContract.CategoryEntry.TABLE_NAME);

            case PRODUCT:
                return updateRecord(uri, values, selection, selectionArgs, JumboContract.ProductEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Update unknown URI: " + uri);
        }
    }

    private int updateRecord(Uri uri, ContentValues values, String selection, String[] selectionArgs, String tableName) {
        //this time we need a writable database
        SQLiteDatabase db = helper.getWritableDatabase();
        int id = db.update(tableName, values, selection, selectionArgs);
        //db.close();
        if (id == 0) {
            Log.e("Error", "update error for URI " + uri);
            return -1;
        }

        return id;
    }
}
