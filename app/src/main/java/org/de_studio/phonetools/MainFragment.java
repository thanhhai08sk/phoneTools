package org.de_studio.phonetools;

import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.TextView;

/**
 * Created by hai on 10/13/2015.
 */
public  class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = MainFragment.class.getSimpleName();
    private static final int MAIN_LOADER = 0;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private MainAdapter mMainAdapter;
    private ViewHolder mViewHolder;
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

    private static   class ViewHolder{

        public final TextView kttk1;
        public final TextView kttk2;
        public final TextView kttk3;
        public final TextView dv3g1;
        public final TextView dv3g2;
        public final TextView dv3g3;
        public final View kttk1Card;
        public final View kttk2Card;
        public final View kttk3Card;
        public final View dv3gCard1;
        public final View dv3gCard2;
        public final View dv3gCard3;
        public final View dv3gCard4;
        public final View dv3gCard5;
        public final TextView dv3g4;
        public final TextView dv3g5;




        public ViewHolder(View view) {
             kttk1 =(TextView) view.findViewById(R.id.kttk_1);
             kttk2 =(TextView) view.findViewById(R.id.kttk_2);
             kttk3 =(TextView) view.findViewById(R.id.kttk_3);
             dv3g1 =(TextView) view.findViewById(R.id.dich_vu_3g_text_1);
             dv3g2 =(TextView) view.findViewById(R.id.dich_vu_3g_text_2);
             dv3g3 =(TextView) view.findViewById(R.id.dich_vu_3g_text_3);
            dv3g4 =(TextView) view.findViewById(R.id.dich_vu_3g_text_4);
            dv3g5 =(TextView) view.findViewById(R.id.dich_vu_3g_text_5);
            kttk1Card = view.findViewById(R.id.kttk_1_card);
            kttk2Card = view.findViewById(R.id.kttk_2_card);
            kttk3Card = view.findViewById(R.id.kttk_3_card);
            dv3gCard1 = view.findViewById(R.id.dv3g_1_card);
            dv3gCard2 = view.findViewById(R.id.dv3g_2_card);
            dv3gCard3 = view.findViewById(R.id.dv3g_3_card);
            dv3gCard4 = view.findViewById(R.id.dv3g_4_card);
            dv3gCard5 = view.findViewById(R.id.dv3g_5_card);

        }

    }





    public MainFragment() {
    }

    public void change(){
        getLoaderManager().restartLoader(0, null, this);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mMainAdapter = new MainAdapter(getActivity(),null,0);
        mViewHolder = new ViewHolder(rootView);





        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MAIN_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mMainAdapter.swapCursor(null);


        String sortOrder = PhoneToolsContract.ActionEntry.TABLE_NAME + "." + PhoneToolsContract.ActionEntry._ID + " ASC";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String selection = " carrier_name = ? AND in_main >= 1 ";
        Log.e(LOG_TAG, "preference = " + prefs.getString(getString(R.string.pref_carriers_key), ""));

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
        data.moveToFirst();
        Boolean ok =false;
        Integer kttkPosition =0;
        int kttkCount =0;
        int dv3gPosition =0;
        mViewHolder.kttk1Card.setVisibility(View.GONE);
        mViewHolder.kttk2Card.setVisibility(View.GONE);
        mViewHolder.kttk3Card.setVisibility(View.GONE);
        mViewHolder.dv3gCard1.setVisibility(View.GONE);
        mViewHolder.dv3gCard2.setVisibility(View.GONE);
        mViewHolder.dv3gCard3.setVisibility(View.GONE);
        mViewHolder.dv3gCard4.setVisibility(View.GONE);
        mViewHolder.dv3gCard5.setVisibility(View.GONE);


        do {
            if (data.getInt(COL_IN_MAIN)==1){
                if (kttkPosition ==0){
                    mViewHolder.kttk1.setText(data.getString(COL_TITLE));
                    mViewHolder.kttk1Card.setVisibility(View.VISIBLE);
                    kttkPosition++;
                }else if (kttkPosition==1){
                    mViewHolder.kttk2.setText(data.getString(COL_TITLE));
                    mViewHolder.kttk2Card.setVisibility(View.VISIBLE);
                    kttkPosition ++;
                }else if (kttkPosition==2){
                    mViewHolder.kttk3.setText(data.getString(COL_TITLE));
                    mViewHolder.kttk3Card.setVisibility(View.VISIBLE);
                }
            }else if (data.getInt(COL_IN_MAIN)==2){
                if (dv3gPosition==0){
                    mViewHolder.dv3gCard1.setVisibility(View.VISIBLE);
                    mViewHolder.dv3g1.setText(data.getString(COL_TITLE));
                    dv3gPosition++;
                }else if (dv3gPosition==1){
                    mViewHolder.dv3gCard2.setVisibility(View.VISIBLE);

                    mViewHolder.dv3g2.setText(data.getString(COL_TITLE));
                    dv3gPosition++;
                }else if (dv3gPosition ==2){
                    mViewHolder.dv3gCard3.setVisibility(View.VISIBLE);

                    mViewHolder.dv3g3.setText(data.getString(COL_TITLE));
                    dv3gPosition++;
                }else if (dv3gPosition ==3){
                    mViewHolder.dv3gCard4.setVisibility(View.VISIBLE);

                    mViewHolder.dv3g4.setText(data.getString(COL_TITLE));
                    dv3gPosition++;
                }else if (dv3gPosition ==4){
                    mViewHolder.dv3gCard5.setVisibility(View.VISIBLE);

                    mViewHolder.dv3g5.setText(data.getString(COL_TITLE));
                    dv3gPosition++;
                }
            }
        }while (data.moveToNext());
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
