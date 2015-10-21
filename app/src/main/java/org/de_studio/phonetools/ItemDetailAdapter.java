package org.de_studio.phonetools;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by hai on 10/21/2015.
 */
public class ItemDetailAdapter extends CursorAdapter {
    private static final int VIEW_TYPE_COUNT = 1;
    private static final String LOG_TAG = MainAdapter.class.getSimpleName();



    public ItemDetailAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(final Context context, final Cursor cursor, ViewGroup parent) {

            return LayoutInflater.from(context).inflate(R.layout.fragment_item_list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

            TextView textView = (TextView) view.findViewById(R.id.item_list_item);
            String title = cursor.getString(ItemDetailFragment.COL_MAIN_TITLE);
            textView.setText(title);


    }
}
