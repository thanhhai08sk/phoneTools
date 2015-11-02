package org.de_studio.phonetools;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hai on 10/13/2015.
 */
public  class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = MainFragment.class.getSimpleName();
    private static final int MAIN_LOADER = 0;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private MainRecycleAdapter mainRecycleAdapter;
    public static final String[] PHONE_TOOLS_COLUMNS = {
            PhoneToolsContract.MainEntry.TABLE_NAME + "."+ PhoneToolsContract.MainEntry._ID,
            PhoneToolsContract.MainEntry.COLUMN_TYPE,
            PhoneToolsContract.MainEntry.COLUMN_DESTINATION,
            PhoneToolsContract.MainEntry.COLUMN_TITLE,
            PhoneToolsContract.MainEntry.COLUMN_SHORT_DESCRIPTION,
            PhoneToolsContract.MainEntry.COLUMN_DESCRIPTION,
            PhoneToolsContract.MainEntry.COLUMN_CARRIER_ID,
            PhoneToolsContract.CarriersEntry.TABLE_NAME + "." + PhoneToolsContract.CarriersEntry.COLUMN_CARRIER_NAME,
            PhoneToolsContract.MainEntry.COLUMN_TEXT,
            PhoneToolsContract.MainEntry.COLUMN_CANCEL,
            PhoneToolsContract.MainEntry.COLUMN_MONEY,
            PhoneToolsContract.MainEntry.COLUMN_CYCLE,
            PhoneToolsContract.MainEntry.COLUMN_IN_MAIN,

    };


    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_ID = 0;
    static final int COL_TYPE = 1;
    static final int COL_DESTINATION = 2;
    static final int COL_TITLE = 3;
    static final int COL_SHORT_DESCRIPTION =4;
    static final int COL_DESCRIPTION = 5;
    static final int COL_CARRIER_ID = 6;
    static final int COL_CARRIERS_CARRIER_NAME = 7;
    static final int COL_TEXT = 8;
    static final int COL_CANCEL = 9;
    static final int COL_MONEY = 10;
    static final int COL_CYCLE = 11;
    static final int COL_IN_MAIN = 12;


    public static final String[] CUSTOM_COLUMNS = {
            PhoneToolsContract.ActionEntry.TABLE_NAME + "."+ PhoneToolsContract.ActionEntry._ID,
            PhoneToolsContract.ActionEntry.COLUMN_TYPE,
            PhoneToolsContract.ActionEntry.COLUMN_DESTINATION,
            PhoneToolsContract.ActionEntry.COLUMN_TITLE,
            PhoneToolsContract.ActionEntry.COLUMN_SHORT_DESCRIPTION,
            PhoneToolsContract.ActionEntry.COLUMN_DESCRIPTION,
            PhoneToolsContract.ActionEntry.COLUMN_CARRIER_ID,
            PhoneToolsContract.CarriersEntry.TABLE_NAME + "." + PhoneToolsContract.CarriersEntry.COLUMN_CARRIER_NAME,
            PhoneToolsContract.ActionEntry.COLUMN_TEXT,
            PhoneToolsContract.ActionEntry.COLUMN_CANCEL,
            PhoneToolsContract.ActionEntry.COLUMN_MONEY,
            PhoneToolsContract.ActionEntry.COLUMN_CYCLE,
            PhoneToolsContract.ActionEntry.COLUMN_IN_MAIN,
    };

    static final int CUS_ID = 0;
    static final int CUS_TYPE = 1;
    static final int CUS_DESTINATION = 2;
    static final int CUS_TITLE = 3;
    static final int CUS_SHORT_DESCRIPTION =4;
    static final int CUS_DESCRIPTION = 5;
    static final int CUS_CARRIER_ID = 6;
    static final int CUS_CARRIERS_CARRIER_NAME = 7;
    static final int CUS_TEXT = 8;
    static final int CUS_CANCEL = 9;
    static final int CUS_MONEY = 10;
    static final int CUS_CYCLE = 11;
    static final int CUS_IN_MAIN = 12;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainFragment newInstance(int sectionNumber) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view);
        mainRecycleAdapter = new MainRecycleAdapter(getActivity(),null);
        recyclerView.setAdapter(mainRecycleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        ListView listView =(ListView) rootView.findViewById(R.id.main_list_view);
//        listView.setAdapter(mMainAdapter);
//        Button button = (Button) rootView.findViewById(R.id.main_custom_button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AddItemFragment addItemFragment = new AddItemFragment();
//                addItemFragment.show(getActivity().getFragmentManager(), "addItemFragment");
//            }
//        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MAIN_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (id ==MAIN_LOADER) {
            String sortOrder = PhoneToolsContract.MainEntry.TABLE_NAME + "." + PhoneToolsContract.MainEntry._ID + " ASC";
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String selection = " carrier_name = ? AND in_main >= 1 ";
            String[] selectionAgrm = new String[]{prefs.getString("carrier", "viettel")};
            return new CursorLoader(getActivity(),
                    PhoneToolsContract.MainEntry.CONTENT_URI,
                    PHONE_TOOLS_COLUMNS,
                    selection,
                    selectionAgrm,
                    sortOrder);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.e(LOG_TAG,"onLoadFinished ne");
        mainRecycleAdapter.swapCursor(data);
        mainRecycleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mainRecycleAdapter.swapCursor(null);

    }

    @Override
    public void onResume() {

        super.onResume();
        onCarrierChange();

    }
    public void onCarrierChange( ) {

        getLoaderManager().restartLoader(MAIN_LOADER, null, this);
    }



}
