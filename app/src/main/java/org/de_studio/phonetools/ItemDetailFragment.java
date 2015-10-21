package org.de_studio.phonetools;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;


public class ItemDetailFragment extends ListFragment {

    public static final String ARG_ITEM_ID = "item_id";
    private static Integer mPosition;
    ArrayList<String> list;


    public static final String[] PHONE_TOOLS_COLUMNS = {
            PhoneToolsContract.MainEntry.TABLE_NAME + "."+ PhoneToolsContract.ActionEntry._ID,
            PhoneToolsContract.MainEntry.COLUMN_TYPE,
            PhoneToolsContract.MainEntry.COLUMN_DESTINATION,
            PhoneToolsContract.MainEntry.COLUMN_TITLE,
            PhoneToolsContract.MainEntry.COLUMN_DESCRIPTION,
            PhoneToolsContract.MainEntry.COLUMN_CARRIER_ID,
            PhoneToolsContract.MainEntry.TABLE_NAME + "." + PhoneToolsContract.CarriersEntry.COLUMN_CARRIER_NAME,
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            mPosition = ItemDetailActivity.mPosition;

        list = new ArrayList<String>();
        list.add(getString(R.string.list_tong_dai));
        list.add(getString(R.string.list_3g));
        list.add(getString(R.string.list_tien_ich));


        setListAdapter(new ArrayAdapter<String>(
                getActivity(),
                R.layout.fragment_item_detail,
                R.id.item_detail,
                list));


    }

}
