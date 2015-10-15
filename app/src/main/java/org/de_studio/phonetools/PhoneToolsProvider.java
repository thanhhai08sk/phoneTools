package org.de_studio.phonetools;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.io.IOException;

/**
 * Created by hai on 10/15/2015.
 */
public class PhoneToolsProvider extends ContentProvider{
    static final int MAIN = 100;
    static final int MAIN_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DataBaseHelper mOpenHelper;
    private static final SQLiteQueryBuilder sMainByCarriersSettingQueryBuilder;

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

    private static final String sIdSelection =
            PhoneToolsContract.MainEntry.TABLE_NAME +
                    "." + PhoneToolsContract.MainEntry._ID + " = ? ";


    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case MAIN_WITH_ID:
                return PhoneToolsContract.MainEntry.CONTENT_ITEM_TYPE;
            case MAIN:
                return PhoneToolsContract.MainEntry.CONTENT_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DataBaseHelper(getContext());
        try {
            mOpenHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            mOpenHelper.openDataBase();

        } catch (SQLException sqle) {

            throw sqle;
        }
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
            case (MAIN_WITH_ID):{
                reCursor = getMainById(uri,projection,sortOrder);
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

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PhoneToolsContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,PhoneToolsContract.PATH_MAIN,MAIN);
        matcher.addURI(authority,PhoneToolsContract.PATH_MAIN + "/#",MAIN_WITH_ID);

        return matcher;
    }

}
