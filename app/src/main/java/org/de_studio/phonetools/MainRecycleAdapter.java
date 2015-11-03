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
                View view;

                view = LayoutInflater.from(context).inflate(R.layout.main_list_item,parent,false);
                return view;
            }
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                String titleText = cursor.getString(MainFragment.COL_TITLE);
                TextView title = (TextView)view.findViewById(R.id.item_title);
                final TextView description = (TextView) view.findViewById(R.id.item_description);
                String descriptionText = cursor.getString(MainFragment.COL_DESCRIPTION);
                String shortDescriptionText = cursor.getString(MainFragment.COL_SHORT_DESCRIPTION);
                if (descriptionText==null||descriptionText.equals("")){
                    description.setText(shortDescriptionText);
                }else description.setText(descriptionText);
                title.setText(titleText);
                title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (description.isShown()) {
                            description.setVisibility(View.GONE);
                        } else description.setVisibility(View.VISIBLE);
                    }
                });
            }
        };
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView title;
        public ViewHolder(View itemView){
            super(itemView);
            view =itemView;
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
        mCursorAdapter.bindView(holder.view,mContext,cursor);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType>=11){
            view= LayoutInflater.from(mContext).inflate(R.layout.main_list_item_with_title,parent,false);
            TextView categoryTitle = (TextView) view.findViewById(R.id.category_title_text);
            if (viewType==11) categoryTitle.setText(mContext.getResources().getString(R.string.main_kiem_tra_tai_khoan_title));
            if (viewType ==12) categoryTitle.setText(mContext.getResources().getString(R.string.main_dich_vu_3g_title));
            if (viewType ==13) categoryTitle.setText(mContext.getResources().getString(R.string.main_tien_ich_title));
        }else {
            view = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        }
        return new ViewHolder(view);
    }
    public void swapCursor(Cursor cursor){
        mCursorAdapter.swapCursor(cursor);
    }
    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return 11;
        }else {
            Cursor cursor = mCursorAdapter.getCursor();
            cursor.moveToPosition(position);
            int nowInMain = cursor.getInt(MainFragment.COL_IN_MAIN);
            cursor.moveToPrevious();
            int previousInMain = cursor.getInt(MainFragment.COL_IN_MAIN);
            if (nowInMain>previousInMain){
                if (nowInMain ==2){
                    return 12;
                }else return 13;
            }
        }
        return super.getItemViewType(position);
    }
}
