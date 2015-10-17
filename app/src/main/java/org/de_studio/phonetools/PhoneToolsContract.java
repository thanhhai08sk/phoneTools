package org.de_studio.phonetools;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hai on 10/15/2015.
 */
public class PhoneToolsContract {
    public static final String CONTENT_AUTHORITY = "org.de_studio.phonetools";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MAIN = "main";
    public static final String PATH_ACTION = "action";
    public static final String PATH_CARRIERS = "carriers";

    public static final class MainEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MAIN).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MAIN;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MAIN;
        public static final String TABLE_NAME = "main";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_DESTINATION = "destination";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_CARRIER_ID = "carrier_id";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_CANCEL = "cancel";
        public static final String COLUMN_MONEY = "money";
        public static final String COLUMN_CYCLE = "_cycle";
        public static final String COLUMN_IN_MAIN = "in_main";

        public static long getIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }
        public static Uri buildMainUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }


    public static final class ActionEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ACTION).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ACTION;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ACTION;
        public static final String TABLE_NAME = "action";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_DESTINATION = "destination";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_CARRIER_ID = "carrier_id";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_CANCEL = "cancel";
        public static final String COLUMN_MONEY = "money";
        public static final String COLUMN_CYCLE = "_cycle";
        public static final String COLUMN_IN_MAIN = "in_main";

        public static long getIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }
        public static Uri buildMainUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }


    public static final class CarriersEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CARRIERS).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CARRIERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CARRIERS;
        public static final String TABLE_NAME = "carriers";
        public static final String COLUMN_CARRIER_NAME = "carrier_name";


    }


}
