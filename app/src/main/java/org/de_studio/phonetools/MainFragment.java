package org.de_studio.phonetools;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobeta.android.dslv.DragSortController;
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
    public static final String[] PHONE_TOOLS_COLUMNS = {
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

    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int i, int i1) {
            if (i != i1){
                final Bundle bundle = new Bundle();
                bundle.putInt("i",i);
                bundle.putInt("i1", i1);
//                getActivity().getContentResolver().call(PhoneToolsContract.MainEntry.CONTENT_URI,
//                        "move",
//                        null,
//                        bundle);

                Thread background = new Thread(new Runnable() {
                     Bundle newBundle = bundle;
                    @Override
                    public void run() {
                        getActivity().getContentResolver().call(PhoneToolsContract.ActionEntry.CONTENT_URI,
                                "move",
                                null,
                                newBundle);
                    }
                });
                background.run();
            }
//            mMainAdapter.notifyDataSetChanged();

        }
    };



    public MainFragment() {
    }

    public void change(){
        getLoaderManager().restartLoader(0, null, this);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (DragSortListView) rootView.findViewById(R.id.listview);
        mMainAdapter = new MainAdapter(getActivity(),null,0);
        mListView.setAdapter(mMainAdapter);
        mListView.setDropListener(onDrop);
//        Button floatButton = (Button) rootView.findViewById(R.id.floating_button_1);
//        floatButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(),ItemListActivity.class));
//            }
//        });



        DragSortController controller = new DragSortController(mListView);
        controller.setDragHandleId(R.id.item_drag);
        //controller.setClickRemoveId(R.id.);
        controller.setRemoveEnabled(false);
        controller.setSortEnabled(true);
        controller.setDragInitMode(1);
        //controller.setRemoveMode(removeMode);

        mListView.setFloatViewManager(controller);
        mListView.setOnTouchListener(controller);
        mListView.setDragEnabled(true);


        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MAIN_LOADER,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mMainAdapter.swapCursor(null);


        String sortOrder = PhoneToolsContract.ActionEntry.TABLE_NAME + "." + PhoneToolsContract.ActionEntry._ID + " ASC";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String selection = " carrier_name = ? AND in_main = 1 ";
        Log.e(LOG_TAG, "preference = " + prefs.getString(getString(R.string.pref_carriers_key),""));

        String[] selectionAgrm = new String[]{prefs.getString("carrier", "vinaphone")};


        Log.e(LOG_TAG, "oncreateloader ne");
        return new  CursorLoader(getActivity(),
                PhoneToolsContract.ActionEntry.CONTENT_URI,
                PHONE_TOOLS_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        String[] addRowString = new String[] {"-1","type","090","title","short_description","description","3","carrier name","text","cancel","money","cyclee","1"};
        MatrixCursor extras = new MatrixCursor(data.getColumnNames());
        extras.addRow(addRowString);
        Cursor[] cursors = { data,extras };
        Cursor extendedCursor = new MergeCursor(cursors);
        int count = extendedCursor.getCount();
        Log.e(LOG_TAG, data.toString() + " count = " + count);
        mMainAdapter.swapCursor(extendedCursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMainAdapter.swapCursor(null);
        Log.e(LOG_TAG, "onloaderreset ne");
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.e(LOG_TAG, " onResume");
//        onCarrierChange();
    }
    void onCarrierChange( ) {

        getLoaderManager().restartLoader(MAIN_LOADER, null, this);
    }


}
