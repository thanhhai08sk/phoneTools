package org.de_studio.phonetools;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class DealFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String LOG_TAG = DealFragment.class.getSimpleName();
    DealRecycleAdapter mRecycleAdapter;
    private static final int DEAL_LOADER = 0;
    public static final String[] DEAL_COLUMNS = {
            PhoneToolsContract.DealEntry._ID,
            PhoneToolsContract.DealEntry.COLUMN_DATE,
            PhoneToolsContract.DealEntry.COLUMN_TITLE,
            PhoneToolsContract.DealEntry.COLUMN_DETAIL
    };
    static final int COL_ID = 0;
    static final int COL_DATE = 1;
    static final int COL_TITLE = 2;
    static final int COL_DETAIL = 3;


    public static DealFragment newInstance(int sectionNumber) {
        DealFragment fragment = new DealFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    public DealFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(DEAL_LOADER, null, this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deal,container,false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.deal_fragment_recycle_view);
        mRecycleAdapter = new DealRecycleAdapter(getActivity(),null);
        recyclerView.setAdapter(mRecycleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = PhoneToolsContract.DealEntry._ID + " DESC";
        return new CursorLoader(getActivity(),
                PhoneToolsContract.DealEntry.CONTENT_URI,
                DEAL_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mRecycleAdapter.swapCursor(data);
        mRecycleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecycleAdapter.swapCursor(null);
    }
}
