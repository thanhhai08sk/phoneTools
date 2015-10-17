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
        getLoaderManager().restartLoader(0,null,this);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (DragSortListView) rootView.findViewById(R.id.listview);
        mMainAdapter = new MainAdapter(getActivity(),null,0);
        mListView.setAdapter(mMainAdapter);
        mListView.setDropListener(onDrop);



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

        String sortOrder = PhoneToolsContract.ActionEntry.TABLE_NAME + "." + PhoneToolsContract.ActionEntry._ID + " ASC";

        Log.e(LOG_TAG,"oncreateloader ne");
        return new  CursorLoader(getActivity(),
                PhoneToolsContract.ActionEntry.CONTENT_URI,
                PHONE_TOOLS_COLUMNS,
                null,
                null,
                sortOrder);
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
