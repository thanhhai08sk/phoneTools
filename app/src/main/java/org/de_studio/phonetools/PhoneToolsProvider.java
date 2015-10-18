package org.de_studio.phonetools;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

/**
 * Created by hai on 10/15/2015.
 */
public class PhoneToolsProvider extends ContentProvider{
    static final int MAIN = 100;
    static final int MAIN_WITH_ID = 101;
    static final int ACTION = 200;
    static final int ACTION_WITH_ID = 201;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String LOG_TAG = PhoneToolsProvider.class.getSimpleName();
    private DataBaseHelper mOpenHelper;
    private static final SQLiteQueryBuilder sMainByCarriersSettingQueryBuilder;
    private static final SQLiteQueryBuilder sActionByCarriersSettingQueryBuilder;

    static{
        sMainByCarriersSettingQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sMainByCarriersSettingQueryBuilder.setTables(
                PhoneToolsContract.MainEntry.TABLE_NAME + " INNER JOIN " +
                        PhoneToolsContract.CarriersEntry.TABLE_NAME +
                        " ON " + PhoneToolsContract.MainEntry.TABLE_NAME +
                        "." + PhoneToolsContract.MainEntry.COLUMN_CARRIER_ID +
                        " = " + PhoneToolsContract.CarriersEntry.TABLE_NAME +
                        "." + PhoneToolsContract.CarriersEntry._ID);
    }
    static{
        sActionByCarriersSettingQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sActionByCarriersSettingQueryBuilder.setTables(
                PhoneToolsContract.ActionEntry.TABLE_NAME + " INNER JOIN " +
                        PhoneToolsContract.CarriersEntry.TABLE_NAME +
                        " ON " + PhoneToolsContract.ActionEntry.TABLE_NAME +
                        "." + PhoneToolsContract.ActionEntry.COLUMN_CARRIER_ID +
                        " = " + PhoneToolsContract.CarriersEntry.TABLE_NAME +
                        "." + PhoneToolsContract.CarriersEntry._ID);
    }

    private static final String sIdSelection =
            PhoneToolsContract.MainEntry.TABLE_NAME +
                    "." + PhoneToolsContract.MainEntry._ID + " = ? ";
    private static final String sIdInActionSelection =
            PhoneToolsContract.ActionEntry.TABLE_NAME +
                    "." + PhoneToolsContract.ActionEntry._ID + " = ? ";
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case MAIN_WITH_ID:
                return PhoneToolsContract.MainEntry.CONTENT_ITEM_TYPE;
            case MAIN:
                return PhoneToolsContract.MainEntry.CONTENT_TYPE;
            case ACTION:
                return PhoneToolsContract.ActionEntry.CONTENT_TYPE;
            case ACTION_WITH_ID:
                return PhoneToolsContract.ActionEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MAIN:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(PhoneToolsContract.MainEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case (MAIN):{
                long _id = db.insert(PhoneToolsContract.MainEntry.TABLE_NAME, null, values);
                if (_id>0) {
                    returnUri = PhoneToolsContract.MainEntry.buildMainUri(_id);
                }else
                    throw new android.database.SQLException("Failed to insert row into " + uri);

                break;
            }
            case ACTION : {
                long _id = db.insert(PhoneToolsContract.ActionEntry.TABLE_NAME, null, values);
                if (_id>0) {
                    returnUri = PhoneToolsContract.ActionEntry.buildActionUri(_id);
                }else
                    throw new android.database.SQLException("Failed to insert row into " + uri);

                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DataBaseHelper(getContext());
        try {
            mOpenHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
//        try {
//            mOpenHelper.openDataBase();
//
//        } catch (SQLException sqle) {
//
//            throw sqle;
//        }
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor reCursor;
        switch (sUriMatcher.match(uri)){
            case (MAIN):{
//                reCursor = mOpenHelper.getReadableDatabase().query(
//                        PhoneToolsContract.MainEntry.TABLE_NAME,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        sortOrder
//                );
                reCursor =  sMainByCarriersSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case ACTION: {
                reCursor =  sActionByCarriersSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case (MAIN_WITH_ID):{
                reCursor = getMainById(uri,projection,sortOrder);
                break;
            }
            case ACTION_WITH_ID: {
                reCursor = getActionById(uri,projection,sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        reCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return reCursor;

    }


    private Cursor getMainById(
            Uri uri, String[] projection, String sortOrder) {
        long id = PhoneToolsContract.MainEntry.getIdFromUri(uri);

        return sMainByCarriersSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sIdSelection,
                new String[]{Long.toString(id)},
                null,
                null,
                sortOrder
        );
    }
    private Cursor getActionById(Uri uri, String[] projection, String sortOrder){
        long id = PhoneToolsContract.ActionEntry.getIdFromUri(uri);

        return sActionByCarriersSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sIdInActionSelection,
                new String[]{Long.toString(id)},
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PhoneToolsContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,PhoneToolsContract.PATH_MAIN,MAIN);
        matcher.addURI(authority,PhoneToolsContract.PATH_MAIN + "/#",MAIN_WITH_ID);
        matcher.addURI(authority,PhoneToolsContract.PATH_ACTION,ACTION);
        matcher.addURI(authority,PhoneToolsContract.PATH_ACTION + "/#",ACTION_WITH_ID);

        return matcher;
    }
    public boolean move(int i, int i1){
        i = i +1;
        i1 = i1 +1;
        Log.e(LOG_TAG, "i = "+ i + " i1 = "+ i1 );
        int rows;
        Cursor rowsCursor;
        rowsCursor = query(PhoneToolsContract.ActionEntry.CONTENT_URI,null,null,null,null);
        rows = rowsCursor.getCount();
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor cursor;
        cursor = query(PhoneToolsContract.ActionEntry.buildActionUri(i),MainFragment.PHONE_TOOLS_COLUMNS,null,null,null);
        Log.e(LOG_TAG, "count of cursor is " + cursor.getCount());
        ContentValues contentValues = new ContentValues();
        cursor.moveToFirst();
        DatabaseUtils.cursorRowToContentValues(cursor, contentValues);

        db.delete(PhoneToolsContract.ActionEntry.TABLE_NAME, "_id = ?", new String[]{i + ""});
//        db.rawQuery("UPDATE " + PhoneToolsContract.MainEntry.TABLE_NAME + " SET _id = _id - 1 WHERE _id > ? ", new String[]{i + ""});

        for (int t= i+1;t<= rows; t++){
            Cursor tempCursor = query(PhoneToolsContract.ActionEntry.buildActionUri(t),MainFragment.PHONE_TOOLS_COLUMNS,null,null,null);
            tempCursor.moveToFirst();
            ContentValues tempContent= new ContentValues();
            DatabaseUtils.cursorRowToContentValues(tempCursor, tempContent);
            tempContent.remove("_id");
            tempContent.remove("carrier_name");
            tempContent.put("_id",t-1);
            db.update(PhoneToolsContract.ActionEntry.TABLE_NAME,tempContent,"_id = ? ",new String[] {t+""});
        }
//        db.rawQuery("UPDATE " + PhoneToolsContract.MainEntry.TABLE_NAME + " SET _id = _id + 1 WHERE _id >= ? ", new String[]{i1 + ""});
        for (int t =rows - 1;t>= i1; t--){
            Cursor tempCursor = query(PhoneToolsContract.ActionEntry.buildActionUri(t),MainFragment.PHONE_TOOLS_COLUMNS,null,null,null);
            tempCursor.moveToFirst();
            ContentValues tempContent= new ContentValues();
            DatabaseUtils.cursorRowToContentValues(tempCursor, tempContent);
            tempContent.remove("_id");
            tempContent.remove("carrier_name");
            tempContent.put("_id",t+1);
            db.update(PhoneToolsContract.ActionEntry.TABLE_NAME,tempContent,"_id = ? ",new String[] {t+""});
        }


        Log.e(LOG_TAG, "_id = " + contentValues.getAsString("_id"));
//        contentValues.remove("_id");
        contentValues.remove("carrier_name");
        contentValues.remove("_id");
        contentValues.put("_id",i1);
//        Cursor cursor1 = db.rawQuery("SELECT seq FROM sqlite_sequence WHERE name=?",
//                new String[] { "main" });
//        int last = (cursor1.moveToFirst() ? cursor1.getInt(0) : 0);
//        Log.e(LOG_TAG, "number of rows is  " +last);
        Uri uri = insert(PhoneToolsContract.ActionEntry.CONTENT_URI, contentValues);
        Log.e(LOG_TAG, "Uri of last insert is  " + uri.toString());
//        db.rawQuery("UPDATE " + PhoneToolsContract.MainEntry.TABLE_NAME + " SET _id = ? WHERE _id = ? ", new String[]{i1 + "",last +""});

        getContext().getContentResolver().notifyChange(PhoneToolsContract.ActionEntry.CONTENT_URI, null);

        return true;

    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        if (method.equals("move")){
            move(extras.getInt("i"),extras.getInt("i1"));
        }
        return super.call(method, arg, extras);
    }
}
