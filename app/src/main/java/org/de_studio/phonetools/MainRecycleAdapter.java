package org.de_studio.phonetools;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by hai on 11/2/2015.
 */
public class MainRecycleAdapter extends RecyclerView.Adapter<MainRecycleAdapter.ViewHolder>{
    private static final String LOG_TAG = MainRecycleAdapter.class.getSimpleName();
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
                String buttonText = cursor.getString(MainFragment.COL_SHORT_DESCRIPTION);
                if (buttonText!=null){
                    Button MainButton = (Button) view.findViewById(R.id.item_action);
                    MainButton.setText(buttonText);
                }
                String titleText = cursor.getString(MainFragment.COL_TITLE);
                TextView title = (TextView)view.findViewById(R.id.item_title);
                final TextView description = (TextView) view.findViewById(R.id.item_description);
                String descriptionText = cursor.getString(MainFragment.COL_DESCRIPTION);
                String shortDescriptionText = cursor.getString(MainFragment.COL_SHORT_DESCRIPTION);
                final Button deleteButton = (Button)view.findViewById(R.id.main_item_action_delete_button);
                if (descriptionText==null||descriptionText.equals("")){
                    description.setText(shortDescriptionText);
                }else description.setText(descriptionText);
                title.setText(titleText);
                title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (description.isShown()) {
                            if (deleteButton!=null){
                                deleteButton.setVisibility(View.GONE);
                            }
                            description.setVisibility(View.GONE);
                        } else {
                            description.setVisibility(View.VISIBLE);
                            if (deleteButton!=null){
                                deleteButton.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
                Button themButton = (Button)view.findViewById(R.id.main_item_last_default_item_them_button);
                if (themButton!=null){
                    themButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AddItemFragment addItemFragment = new AddItemFragment();
                            try {
                                final Activity activity = (Activity) mContext;
                                addItemFragment.show(activity.getFragmentManager(),"addItemFragment");
                            }catch (ClassCastException e){
                                Log.e(LOG_TAG, "can't get FragmentManager");
                            }
                        }
                    });
                }
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
            if (viewType==14){
                view = LayoutInflater.from(mContext).inflate(R.layout.main_list_item_last_default_item,parent,false);
            }else view= LayoutInflater.from(mContext).inflate(R.layout.main_list_item_with_title,parent,false);

            TextView categoryTitle = (TextView) view.findViewById(R.id.category_title_text);
            if (viewType==11) categoryTitle.setText(mContext.getResources().getString(R.string.main_kiem_tra_tai_khoan_title));
            if (viewType ==12) categoryTitle.setText(mContext.getResources().getString(R.string.main_dich_vu_3g_title));
            if (viewType ==13) categoryTitle.setText(mContext.getResources().getString(R.string.main_tien_ich_title));
        }else if (viewType==4) {
            view= LayoutInflater.from(mContext).inflate(R.layout.action_list_item,parent,false);
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
            if (nowInMain==4) return 4;
            cursor.moveToPrevious();
            int previousInMain = cursor.getInt(MainFragment.COL_IN_MAIN);
            if ((getItemCount()-1==position)&(nowInMain==3)) return 14;
            if (position<getItemCount()-1){
                cursor.moveToPosition(position+1);
                int nextInMain = cursor.getInt(MainFragment.COL_IN_MAIN);
                if (nowInMain==3 & nextInMain ==4) return 14;
            }
            if (nowInMain>previousInMain){
                if (nowInMain ==2){
                    return 12;
                }else if (nowInMain==3) return 13;
            }
        }
        return super.getItemViewType(position);
    }
}
