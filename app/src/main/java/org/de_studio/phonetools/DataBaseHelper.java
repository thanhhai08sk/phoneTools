package org.de_studio.phonetools;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
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
    private static String mMyPath = DB_PATH + DB_NAME;
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private static final String LOG_TAG = DataBaseHelper.class.getSimpleName();
    private static final String selectColumnToInsert = " "+
            PhoneToolsContract.ActionEntry.COLUMN_TYPE +", "+
            PhoneToolsContract.ActionEntry.COLUMN_DESTINATION + ", "+
            PhoneToolsContract.ActionEntry.COLUMN_TITLE + ", " +
            PhoneToolsContract.ActionEntry.COLUMN_DESCRIPTION + ", "+
            PhoneToolsContract.ActionEntry.COLUMN_CARRIER_ID + ", "+
            PhoneToolsContract.ActionEntry.COLUMN_TEXT + ", " +
            PhoneToolsContract.ActionEntry.COLUMN_CANCEL + ", " +
            PhoneToolsContract.ActionEntry.COLUMN_MONEY + ", " +
            PhoneToolsContract.ActionEntry.COLUMN_CYCLE + ", " +
            PhoneToolsContract.ActionEntry.COLUMN_IN_MAIN + " ";
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
//        SQLiteDatabase db;
//        String myPath = DB_PATH + DB_NAME;



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

//                db = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
                createActionTable();
                insertActionTable();


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
    public void changeCarrier(int carrierId){

    }

    public void createActionTable(){
        SQLiteDatabase db;

        db = SQLiteDatabase.openDatabase(mMyPath,null,SQLiteDatabase.OPEN_READWRITE);

        final String SQL_CREATE_ACTION_TABLE = "CREATE TABLE " + PhoneToolsContract.ActionEntry.TABLE_NAME + " (" +

                PhoneToolsContract.ActionEntry._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +

                PhoneToolsContract.ActionEntry.COLUMN_TYPE + " INTEGER NOT NULL, " +
                PhoneToolsContract.ActionEntry.COLUMN_DESTINATION + " TEXT NOT NULL, " +
                PhoneToolsContract.ActionEntry.COLUMN_TITLE  + " TEXT NOT NULL, " +
                PhoneToolsContract.ActionEntry.COLUMN_SHORT_DESCRIPTION + " TEXT DEFAULT null," +
                PhoneToolsContract.ActionEntry.COLUMN_DESCRIPTION + " TEXT DEFAULT null," +

                PhoneToolsContract.ActionEntry.COLUMN_CARRIER_ID + " INTEGER NOT NULL, " +
                PhoneToolsContract.ActionEntry.COLUMN_TEXT + " TEXT DEFAULT null, " +

                PhoneToolsContract.ActionEntry.COLUMN_CANCEL + " TEXT, " +
                PhoneToolsContract.ActionEntry.COLUMN_MONEY + " REAL, " +
                PhoneToolsContract.ActionEntry.COLUMN_CYCLE + " TEXT, " +
                PhoneToolsContract.ActionEntry.COLUMN_IN_MAIN + " INTEGER NOT NULL, " +
                PhoneToolsContract.ActionEntry.COLUMN_CATEGORY + " TEXT, " +
                " FOREIGN KEY (" + PhoneToolsContract.ActionEntry.COLUMN_CARRIER_ID + ") REFERENCES " +
                PhoneToolsContract.CarriersEntry.TABLE_NAME + " (" + PhoneToolsContract.CarriersEntry._ID + ")" + ");";
        db.execSQL(SQL_CREATE_ACTION_TABLE);
    }

    public void insertActionTable (){
        SQLiteDatabase db;
        int carrierId = 3;

        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SettingActivity.defaultSharedPreferenceName, 0);



        if (sharedPreferences.contains("carrier")){
            Log.e(LOG_TAG,"there are a carrier preference  " + sharedPreferences.toString());
        }
        switch (sharedPreferences.getString("carrier","")){
            case "viettel" :{
                carrierId=1;
                break;
            }
            case "vinaphone":{
                carrierId = 2;
                break;
            }
            case "mobifone":{
                carrierId = 3;
                break;
            }
            case "vietnamobile":{
                carrierId= 4;
                break;
            }

        }
        Log.e(LOG_TAG," insert database nay");

        db = SQLiteDatabase.openDatabase(mMyPath,null,SQLiteDatabase.OPEN_READWRITE);

        final String SQL_INSERT_ACTION_TABLE = "INSERT INTO " + PhoneToolsContract.ActionEntry.TABLE_NAME +
                " SELECT * FROM "+
                PhoneToolsContract.MainEntry.TABLE_NAME + " WHERE "+ PhoneToolsContract.MainEntry.COLUMN_CARRIER_ID
                + " = " + carrierId + " AND " + PhoneToolsContract.MainEntry.COLUMN_IN_MAIN +
                " = " + 1 + " ;"
                ;
//        final String SQL_INSERT_ID_COLUMN = "ALTER TABLE " + PhoneToolsContract.ActionEntry.TABLE_NAME + " ADD COLUMN " +
//                 PhoneToolsContract.ActionEntry._ID + " INTEGER NOT NULL AUTOINCREMENT  ;";
        db.execSQL(SQL_INSERT_ACTION_TABLE);
//        db.execSQL(SQL_INSERT_ID_COLUMN);
        String sql = "SELECT COUNT(*) FROM " + PhoneToolsContract.ActionEntry.TABLE_NAME;
        SQLiteStatement statement = db.compileStatement(sql);
        Long count = statement.simpleQueryForLong();
        Cursor cursor = db.query(PhoneToolsContract.ActionEntry.TABLE_NAME,null,null,null,null,null,null);
        cursor.moveToFirst();
        int i =1;
        do {
            int oldIndex = cursor.getInt(0);
            ContentValues contentValues = new ContentValues();
            contentValues.put("_id",i);
            db.update(PhoneToolsContract.ActionEntry.TABLE_NAME,contentValues," _id = ? ", new String[]{oldIndex + ""});
            i++;
        }while (cursor.moveToNext());

    }
    public void deleteActionTable(){
        SQLiteDatabase db;
        db = SQLiteDatabase.openDatabase(mMyPath,null,SQLiteDatabase.OPEN_READWRITE);
        db.execSQL("DROP TABLE "+ PhoneToolsContract.ActionEntry.TABLE_NAME);
    }

}
