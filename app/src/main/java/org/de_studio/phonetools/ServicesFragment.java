package org.de_studio.phonetools;

/**
 * Created by hai on 10/13/2015.
 */

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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ServicesFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    ArrayList<String> list;
    private static final int SERVICE_LOADER =2;
    private int mListLevel;
    private String mCategory;
    private ItemDetailAdapter itemDetailAdapter;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static ServicesFragment newInstance(int sectionNumber) {
        ServicesFragment fragment = new ServicesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<String>();
        list.add(getString(R.string.list_goi_nhan_tin));
        list.add(getString(R.string.list_3g));
        list.add(getString(R.string.list_tien_ich));

        mListLevel = 1;
        setListAdapter(new ArrayAdapter<String>(
                getActivity(),
                R.layout.fragment_item_list,
                R.id.item_list_item,
                list));
    }
    //    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v =inflater.inflate(R.layout.fragment_services,container,false);
//        return v;
//    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (mListLevel == 1) {

            switch (position) {
                case 0: {
                    mCategory = "gn";
                    break;
                }
                case 1: {
                    mCategory = "3g";
                    break;
                }
                case 2: {
                    mCategory = "ti";
                    break;
                }
            }
            itemDetailAdapter = new ItemDetailAdapter(getActivity(),null,0);
            setListAdapter(itemDetailAdapter);
            mListLevel = 2;
            getLoaderManager().initLoader(SERVICE_LOADER,null,this);


        }else if (mListLevel==2){
            Cursor cursor = itemDetailAdapter.getCursor();
            cursor.moveToPosition(position);
            String message = cursor.getString(ItemDetailFragment.COL_MAIN_DESCRIPTION);
            String title = cursor.getString(ItemDetailFragment.COL_MAIN_TITLE);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message)
                    .setTitle(title)
                    .setPositiveButton("Đăng ký", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNeutralButton("Hủy DV", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.create().show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        itemDetailAdapter.swapCursor(null);
        String sortOrder = PhoneToolsContract.MainEntry.TABLE_NAME + "." + PhoneToolsContract.MainEntry._ID + " ASC";
        String selection = " category = ? AND carrier_name = ?  ";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String[] selectionAgrm = new String[]{mCategory ,prefs.getString("carrier", "viettel")};

        return new CursorLoader(getActivity(),
                PhoneToolsContract.MainEntry.CONTENT_URI,
                ItemDetailFragment.PHONE_TOOLS_COLUMNS,
                selection,
                selectionAgrm,
                sortOrder);    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        itemDetailAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        itemDetailAdapter.swapCursor(null);

    }
}

