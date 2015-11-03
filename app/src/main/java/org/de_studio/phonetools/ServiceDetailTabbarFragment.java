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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ServiceDetailTabbarFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String LOG_TAG = ServiceDetailTabbarFragment.class.getSimpleName();
    private static final int LOADER = 0;
    private  String category;
    ServiceRecycleAdapter mServiceRecycleAdapter;

    public static ServiceDetailTabbarFragment newInstance(int sectionNumber) {
        ServiceDetailTabbarFragment fragment = new ServiceDetailTabbarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        category = getArguments().getString("category","3g");
//    }

    public ServiceDetailTabbarFragment() {

    }
    public void setCategory (int sectionNumber){
        switch (sectionNumber){
            case 1: category = "gn";
                break;
            case 2: category ="3g";
                break;
            case 3: category = "ti";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_service_detail_tabbar, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.service_detail_tabbar_recycle_view);
        mServiceRecycleAdapter = new ServiceRecycleAdapter(getActivity(), null);
        recyclerView.setAdapter(mServiceRecycleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        String sortOrder = PhoneToolsContract.MainEntry.TABLE_NAME + "." + PhoneToolsContract.MainEntry._ID + " ASC";
        String selection = " category = ? AND carrier_name = ?  ";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String[] selectionAgrm = new String[]{category, prefs.getString("carrier", "viettel")};
        return new CursorLoader(getActivity(),
                PhoneToolsContract.MainEntry.CONTENT_URI,
                MainFragment.PHONE_TOOLS_COLUMNS,
                selection,
                selectionAgrm,
                sortOrder);

    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mServiceRecycleAdapter.swapCursor(data);
        mServiceRecycleAdapter.notifyDataSetChanged();
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mServiceRecycleAdapter.swapCursor(null);
    }
    @Override
    public void onResume() {
        super.onResume();
        onCarrierChange();
    }
    public void onCarrierChange( ) {
        getLoaderManager().restartLoader(LOADER, null, this);
    }

}
