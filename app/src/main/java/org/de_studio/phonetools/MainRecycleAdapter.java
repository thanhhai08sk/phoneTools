package org.de_studio.phonetools;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by hai on 11/2/2015.
 */
public class MainRecycleAdapter extends RecyclerView.Adapter<MainRecycleAdapter.ViewHolder>{
    CursorAdapter mCursorAdapter;
    Context mContext;
    public MainRecycleAdapter(Context context,Cursor cursor){
        mContext = context;
        mCursorAdapter = new CursorAdapter(context,cursor,0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = LayoutInflater.from(context).inflate(R.layout.main_list_item,parent,false);
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                String title = cursor.getString(MainFragment.COL_TITLE);
                TextView textView = (TextView)view;
                textView.setText(title);
            }
        };
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        public ViewHolder(View itemView){
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.item_title);

        }
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cursor cursor = mCursorAdapter.getCursor();
        cursor.moveToPosition(position);
        mCursorAdapter.bindView(holder.title,mContext,cursor);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mCursorAdapter.newView(mContext,mCursorAdapter.getCursor(),parent);
        return new ViewHolder(view);
    }
}
