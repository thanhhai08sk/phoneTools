package org.de_studio.phonetools;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by hai on 10/15/2015.
 */
public class MainAdapter extends CursorAdapter {
    private static final int VIEW_TYPE_COUNT = 1;
    private static final String LOG_TAG = MainAdapter.class.getSimpleName();

    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView titleView;
        public final TextView descriptionView;
        public final ImageView actionView;
        public final ImageView menuView;




        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.item_drag);
            titleView = (TextView) view.findViewById(R.id.item_title);
            descriptionView = (TextView) view.findViewById(R.id.item_description);
            actionView = (ImageView) view.findViewById(R.id.item_action);
            menuView = (ImageView) view.findViewById(R.id.item_menu);
        }
    }

    public MainAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layoutId = -1;
        View view = LayoutInflater.from(context).inflate(R.layout.main_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        Log.e(LOG_TAG,"newview ne");

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String title = cursor.getString(MainFragment.COL_MAIN_TITLE);
        viewHolder.titleView.setText(title);
        String description = "this is description";
        viewHolder.descriptionView.setText(description);

    }
}
