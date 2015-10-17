package org.de_studio.phonetools;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by hai on 10/15/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH = "/data/data/org.de_studio.phonetools/databases/";
    private static String DB_NAME = "phonetools.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private static final String LOG_TAG = DataBaseHelper.class.getSimpleName();
    /**
          * Constructor
          * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
          * @param context
          */
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }
    /**
          * Creates a empty database on the system and rewrites it with your own database.
          * */
    public void createDataBase() throws IOException {
        SQLiteDatabase db;
        String myPath = DB_PATH + DB_NAME;
        final String SQL_INSERT_ACTION_TABLE = "INSERT INTO " + PhoneToolsContract.ActionEntry.TABLE_NAME +
                " SELECT * FROM "+
                PhoneToolsContract.MainEntry.TABLE_NAME + " ;"
                 ;
        final String SQL_CREATE_ACTION_TABLE = "CREATE TABLE " + PhoneToolsContract.ActionEntry.TABLE_NAME + " (" +

                PhoneToolsContract.ActionEntry._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +

                PhoneToolsContract.ActionEntry.COLUMN_TYPE + " INTEGER NOT NULL, " +
                PhoneToolsContract.ActionEntry.COLUMN_DESTINATION + " TEXT NOT NULL, " +
                PhoneToolsContract.ActionEntry.COLUMN_TITLE  + " TEXT NOT NULL, " +
                PhoneToolsContract.ActionEntry.COLUMN_DESCRIPTION + " TEXT DEFAULT null," +

                PhoneToolsContract.ActionEntry.COLUMN_CARRIER_ID + " INTEGER NOT NULL, " +
                PhoneToolsContract.ActionEntry.COLUMN_TEXT + " TEXT DEFAULT null, " +

                PhoneToolsContract.ActionEntry.COLUMN_CANCEL + " TEXT, " +
                PhoneToolsContract.ActionEntry.COLUMN_MONEY + " REAL, " +
                PhoneToolsContract.ActionEntry.COLUMN_CYCLE + " TEXT, " +
                PhoneToolsContract.ActionEntry.COLUMN_IN_MAIN + " INTEGER NOT NULL, " +

                " FOREIGN KEY (" + PhoneToolsContract.ActionEntry.COLUMN_CARRIER_ID + ") REFERENCES " +
                PhoneToolsContract.CarriersEntry.TABLE_NAME + " (" + PhoneToolsContract.CarriersEntry._ID + ")" + ");";

        boolean dbExist = checkDataBase();
        if(dbExist){
            //do nothing - database already exist
            Log.e(LOG_TAG,"dbexist ");

        }else{
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                copyDataBase();
                Log.e(LOG_TAG, "create and insert action table ");

                db = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
                db.execSQL(SQL_CREATE_ACTION_TABLE);
                db.execSQL(SQL_INSERT_ACTION_TABLE);


            } catch (IOException e) {
                Log.e(LOG_TAG,"copping database err");
            }
        }
    }
    /**
          * Check if the database already exist to avoid re-copying the file each time you open the application.
          * @return true if it exists, false if it doesn't
          */
    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            //database does't exist yet.
            Log.e(LOG_TAG,"database doesn't exist yet");
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null;
    }
    /**
          * Copies your database from your local assets-folder to the just created empty database in the
          * system folder, from where it can be accessed and handled.
          * This is done by transfering bytestream.
          * */
    private void copyDataBase() throws IOException{
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }
    public void openDataBase() throws SQLException {
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }
    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
            // Add your public helper methods to access and get content from the database.
            // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
            // to you to create adapters for your views.
}
