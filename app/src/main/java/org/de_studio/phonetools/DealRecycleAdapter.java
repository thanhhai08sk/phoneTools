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
 * Created by hai on 11/13/2015.
 */
public class DealRecycleAdapter extends RecyclerView.Adapter<DealRecycleAdapter.ViewHolder>{
    Context mContext;
    CursorAdapter mCursorAdapter;
    private static final String LOG_TAG = DealRecycleAdapter.class.getSimpleName();
    public DealRecycleAdapter(Context context,Cursor cursor){
        mContext = context;
        mCursorAdapter = new CursorAdapter(context, cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.deal_recycle_item, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                String titleText = cursor.getString(DealFragment.COL_TITLE);
                TextView title = (TextView) view.findViewById(R.id.deal_recycle_item_text_view);
                title.setText(titleText);
                String dateText = cursor.getString(DealFragment.COL_DATE);
                TextView date = (TextView) view.findViewById(R.id.deal_recycle_item_date);
                date.setText(dateText);

            }
        };
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView textView;
        TextView date;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            textView = (TextView) itemView.findViewById(R.id.deal_recycle_item_text_view);
            date = (TextView) itemView.findViewById(R.id.deal_recycle_item_date);
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

        mCursorAdapter.bindView(holder.view, mContext, cursor);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new ViewHolder(view);
    }
    public void swapCursor(Cursor cursor){
        mCursorAdapter.swapCursor(cursor);
        mCursorAdapter.notifyDataSetChanged();
    }
}
