package org.de_studio.phonetools;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        public final Button actionView;
        public final ImageView menuView;




        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.item_drag);
            titleView = (TextView) view.findViewById(R.id.item_title);
            descriptionView = (TextView) view.findViewById(R.id.item_description);
            actionView = (Button) view.findViewById(R.id.item_action);
            menuView = (ImageView) view.findViewById(R.id.item_menu);
        }
    }

    public MainAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(final Context context, final Cursor cursor, ViewGroup parent) {
        if (cursor.getString(3).equals("title")){
            Button button = new Button(parent.getContext());
            button.setText("Click me baby");
            return button;
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.main_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            Log.e(LOG_TAG, " new view = " + cursor.getPosition());
            view.setTag(R.string.viewHolderTag, viewHolder);
            return view;
        }
    }



//    @Override
//    public View getView(final int position, View convertView, final ViewGroup parent) {
//
//
//            MyWrapper myWrapper = null;
//            View row = null;
//
//
//            row = super.getView(position, convertView, parent);
//            myWrapper = new MyWrapper(row);
//            if (myWrapper.getButton() != null) {
//                myWrapper.getButton().setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(parent.getContext(), "Just click at position " + position, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//
//            return row;
//        }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Cursor cursor =this.getCursor();
        cursor.moveToPosition(position);
        View v = newView(parent.getContext(),cursor,parent);
        bindView(v,parent.getContext(),cursor);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (view instanceof Button){
            return;
        }else {
            ViewHolder viewHolder = (ViewHolder) view.getTag(R.string.viewHolderTag);

            String title = cursor.getString(MainFragment.COL_MAIN_TITLE);
            viewHolder.titleView.setText(title);
            String description = "this is description";
            viewHolder.descriptionView.setText(description);
        }

    }


}
