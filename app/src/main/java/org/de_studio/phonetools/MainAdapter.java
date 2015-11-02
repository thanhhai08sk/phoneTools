package org.de_studio.phonetools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by hai on 10/15/2015.
 */
public class MainAdapter extends CursorAdapter {
    private static final int VIEW_TYPE_COUNT = 1;
    private static final String LOG_TAG = MainAdapter.class.getSimpleName();
    public static class ViewHolder {
        public final TextView titleView;
        public final Button actionView;

        public ViewHolder(View view) {
            titleView = (TextView) view.findViewById(R.id.item_title);
            actionView = (Button) view.findViewById(R.id.item_action);
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

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        MyWrapper myWrapper;
        Cursor cursor =this.getCursor();
        cursor.moveToPosition(position);
        View v = newView(parent.getContext(),cursor,parent);
        bindView(v,parent.getContext(),cursor);

        myWrapper = new MyWrapper(v);
            if (myWrapper.getButton() != null) {
                myWrapper.getButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(parent.getContext(), "Just click at position " + position, Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                final Button button = (Button) v;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                        builder.setTitle(R.string.choose_add_item_title)
                                .setItems(R.array.choose_add_item, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which==0){
                                            parent.getContext().startActivity(new Intent(parent.getContext(),ItemListActivity.class));
                                        }else {
                                            AddItemFragment addItemFragment = new AddItemFragment();
                                            try {
                                                final Activity activity = (Activity) parent.getContext();
                                                addItemFragment.show(activity.getFragmentManager(),"addItemFragment");
                                            }catch (ClassCastException e){
                                                Log.e(LOG_TAG,"can't get FragmentManager");
                                            }
                                        }
                                    }
                                });
                        builder.create().show();
                    }
                });
            }
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

            ViewHolder viewHolder = (ViewHolder) view.getTag(R.string.viewHolderTag);
            String title = cursor.getString(MainFragment.COL_TITLE);
            viewHolder.titleView.setText(title);


    }


}
