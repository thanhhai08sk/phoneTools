package org.de_studio.phonetools;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by hai on 11/3/2015.
 */
public class ServiceRecycleAdapter extends RecyclerView.Adapter<ServiceRecycleAdapter.ViewHolder> {
    CursorAdapter mCursorAdapter;
    Context mContext;

    public ServiceRecycleAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursorAdapter = new CursorAdapter(context, cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.service_tabbar_recycle_item, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                String titleText = cursor.getString(MainFragment.COL_TITLE);
                TextView title = (TextView) view.findViewById(R.id.service_tabbar_recycle_item_text_view);
                final TextView detail = (TextView) view.findViewById(R.id.service_tabbar_recycle_item_detail);
                final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.service_tabbar_recycle_item_buttons);
                title.setText(titleText);
                title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (detail.isShown()){
                            detail.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.GONE);
                        }else {
                            detail.setVisibility(View.VISIBLE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            title = (TextView) itemView.findViewById(R.id.service_tabbar_recycle_item_text_view);
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
        View view;
        view = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new ViewHolder(view);
    }
    public void swapCursor(Cursor cursor){
        mCursorAdapter.swapCursor(cursor);
    }

}
