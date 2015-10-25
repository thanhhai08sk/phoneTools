package org.de_studio.phonetools;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class ItemDetailFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = ItemDetailFragment.class.getSimpleName();
    private static final int DETAIL_LOADER =1;
    private ItemDetailAdapter itemDetailAdapter;
    public static final String ARG_ITEM_ID = "item_id";
    private static Integer mPosition =1;
    ArrayList<String> list;


    public static final String[] PHONE_TOOLS_COLUMNS = {
            PhoneToolsContract.MainEntry.TABLE_NAME + "."+ PhoneToolsContract.ActionEntry._ID,
            PhoneToolsContract.MainEntry.COLUMN_TYPE,
            PhoneToolsContract.MainEntry.COLUMN_DESTINATION,
            PhoneToolsContract.MainEntry.COLUMN_TITLE,
            PhoneToolsContract.MainEntry.COLUMN_DESCRIPTION,
            PhoneToolsContract.MainEntry.COLUMN_CARRIER_ID,
            PhoneToolsContract.CarriersEntry.TABLE_NAME + "." + PhoneToolsContract.CarriersEntry.COLUMN_CARRIER_NAME,
            PhoneToolsContract.MainEntry.COLUMN_TEXT,
            PhoneToolsContract.MainEntry.COLUMN_CANCEL,
            PhoneToolsContract.MainEntry.COLUMN_MONEY,
            PhoneToolsContract.MainEntry.COLUMN_CYCLE,
            PhoneToolsContract.MainEntry.COLUMN_IN_MAIN,
            PhoneToolsContract.MainEntry.COLUMN_CATEGORY

    };

    static final int COL_MAIN_ID = 0;
    static final int COL_MAIN_TYPE = 1;
    static final int COL_MAIN_DESTINATION = 2;
    static final int COL_MAIN_TITLE = 3;
    static final int COL_MAIN_DESCRIPTION = 4;
    static final int COL_MAIN_CARRIER_ID = 5;
    static final int COL_CARRIERS_CARRIER_NAME = 6;
    static final int COL_MAIN_TEXT = 7;
    static final int COL_MAIN_CANCEL = 8;
    static final int COL_MAIN_MONEY = 9;
    static final int COL_MAIN_CYCLE = 10;
    static final int COL_MAIN_IN_MAIN = 11;
    static final int COL_CATEGORY = 12;



    public ItemDetailFragment() {
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor cursor = itemDetailAdapter.getCursor();
        cursor.moveToPosition(position);
        String message = cursor.getString(COL_MAIN_DESCRIPTION);
        String title = cursor.getString(COL_MAIN_TITLE);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton("Ok button", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity()," Ok clicked",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel button", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity()," Cancel clicked",Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itemDetailAdapter = new ItemDetailAdapter(getActivity(),null,0);
        setListAdapter(itemDetailAdapter);


    }

//    @Override
//    public void onAttach(Context context) {
//        mPosition = getArguments().getInt("position");
//        super.onAttach(context);
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER,null,this);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        itemDetailAdapter.swapCursor(null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        itemDetailAdapter.swapCursor(data);

    }
    public void setPosition(int position){
        mPosition = position;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        itemDetailAdapter.swapCursor(null);
        String sortOrder = PhoneToolsContract.MainEntry.TABLE_NAME + "." + PhoneToolsContract.MainEntry._ID + " ASC";
        String selection = " category = ? AND carrier_name = ?  ";
        String category = "3g";
        switch (mPosition){
            case 1:{
                category = "gn";
                break;
            }
            case 2: {
                category = "3g";
                break;
            }
            case 3: {
                category ="ti";
                break;
            }
            default:{
                category = "td";
            }
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String[] selectionAgrm = new String[]{category ,prefs.getString("carrier", "viettel")};




        return new CursorLoader(getActivity(),
                PhoneToolsContract.MainEntry.CONTENT_URI,
                PHONE_TOOLS_COLUMNS,
                selection,
                selectionAgrm,
                sortOrder);
    }
}
