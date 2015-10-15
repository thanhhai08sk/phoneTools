package org.de_studio.phonetools;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobeta.android.dslv.DragSortListView;

/**
 * Created by hai on 10/13/2015.
 */
public  class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = MainFragment.class.getSimpleName();
    private static final int MAIN_LOADER = 0;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private MainAdapter mMainAdapter;
    private DragSortListView mListView;
    private static final String[] PHONE_TOOLS_COLUMNS = {
            PhoneToolsContract.MainEntry.TABLE_NAME + "."+ PhoneToolsContract.MainEntry._ID,
            PhoneToolsContract.MainEntry.COLUMN_TYPE,
            PhoneToolsContract.MainEntry.COLUMN_DESTINATION,
            PhoneToolsContract.MainEntry.COLUMN_TITLE,
            PhoneToolsContract.MainEntry.COLUMN_DESCRIPTION,
            PhoneToolsContract.MainEntry.COLUMN_CARRIER_ID,
            PhoneToolsContract.MainEntry.COLUMN_TEXT,
            PhoneToolsContract.MainEntry.COLUMN_CANCEL,
            PhoneToolsContract.MainEntry.COLUMN_MONEY,
            PhoneToolsContract.MainEntry.COLUMN_CYCLE,
            PhoneToolsContract.MainEntry.COLUMN_IN_MAIN,

    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
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
        mListView = (DragSortListView) rootView.findViewById(R.id.listview);
        mMainAdapter = new MainAdapter(getActivity(),null,0);
        mListView.setAdapter(mMainAdapter);


        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MAIN_LOADER,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        Log.e(LOG_TAG,"oncreateloader ne");
        return new  CursorLoader(getActivity(),
                PhoneToolsContract.MainEntry.CONTENT_URI,
                PHONE_TOOLS_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        String count = data.getColumnName(0);
        Log.e(LOG_TAG,data.toString() +"  "+ count);
        mMainAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMainAdapter.swapCursor(null);
        Log.e(LOG_TAG,"onloaderreset ne");
    }
}
