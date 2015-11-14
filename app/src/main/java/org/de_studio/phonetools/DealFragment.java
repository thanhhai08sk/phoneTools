package org.de_studio.phonetools;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


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
            PhoneToolsContract.DealEntry.COLUMN_IS_NEW
    };
    static final int COL_ID = 0;
    static final int COL_DATE = 1;
    static final int COL_TITLE = 2;
    static final int COL_IS_NEW = 3;
    Handler handler = new Handler(){
        @Override
         public void handleMessage(Message msg) {
            try {
                String[] text = msg.getData().getStringArray("result");
                for (int i =9; i>=0;i--) {
                    String title = text[i];
                    Cursor cursor = getActivity().getContentResolver().query(PhoneToolsContract.DealEntry.CONTENT_URI,
                            null,
                            PhoneToolsContract.DealEntry.COLUMN_TITLE + " = ? ",
                            new String[]{title},
                            null);
//                    Log.e(LOG_TAG, "number of the same is: " + cursor.getCount());
                    if (cursor.getCount() == 0) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(PhoneToolsContract.DealEntry.COLUMN_TITLE, title);
                        contentValues.put(PhoneToolsContract.DealEntry.COLUMN_IS_NEW, 1);
                        getActivity().getContentResolver().insert(PhoneToolsContract.DealEntry.CONTENT_URI, contentValues);

                    }
                }
            }catch (NullPointerException e){
                Log.e(LOG_TAG,"nullPointerException" + e);
            }
        }
    };

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
        Runnable runnable = new Runnable() {
            @Override
            synchronized public void run() {
                String[] testResultString = new String[10];
                try {
                    Log.e(LOG_TAG, "get information from website");
                    String url = "https://vienthong.com.vn/tin-tuc/tin-khuyen-mai/";
                    Document document = Jsoup.connect(url).get();
                    Elements elements = document.select("li.clearfix a[href] [title]");
                    for (int i=0; i< testResultString.length ; i++){
                        Element element = elements.get(i);
                        String title = element.attr("title");
                        testResultString[i] = title;
                    }
                }catch (IOException e){
                    Log.e(LOG_TAG, "err get information from website");
                }
                Bundle bundle = new Bundle();
                bundle.putStringArray("result", testResultString);
                Message message = new Message();
                message.setData(bundle);
                handler.sendMessage(message);

            }
        };
        if (savedInstanceState==null) {
            Thread thread = new Thread(runnable);
            thread.start();
        }
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
