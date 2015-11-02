package org.de_studio.phonetools;

import android.app.Activity;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * Created by hai on 11/2/2015.
 */
public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    public LayoutInflater layoutInflater;
    public Activity activity;
    public Cursor kiemTraCursor;
    public Cursor dv3gCursor;
    public Cursor tienIchCursor;
    public static final int kiemTraGroup =0;
    public static final int dv3gGroup =1;
    public static final int tienIchGroup =2;
    public static final int groupCount =3;
    public MyExpandableListAdapter(Activity activity){
        this.activity =activity;
        layoutInflater = activity.getLayoutInflater();
    }
    public void swapKiemTraCursor(Cursor cursor){
        kiemTraCursor =cursor;
    }
    public void swapDv3gCursor(Cursor cursor){
        dv3gCursor = cursor;
    }
    public void swapTienIchCursor(Cursor cursor){
        tienIchCursor = cursor;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Cursor reCursor;
        switch (groupPosition){
            case kiemTraGroup :
                kiemTraCursor.moveToPosition(childPosition);
                reCursor =kiemTraCursor;
                break;

            case dv3gGroup :{
                dv3gCursor.moveToPosition(childPosition);
                reCursor = dv3gCursor;
                break;
            }
            case tienIchGroup:{
                tienIchCursor.moveToPosition(childPosition);
                reCursor = tienIchCursor;
                break;
            }
            default:return null;
        };
        return reCursor;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = layoutInflater.inflate(R.layout.fragment_main_item,null);
        }
        Cursor childCursor = (Cursor) getChild(groupPosition,childPosition);
        String title = childCursor.getString(MainFragment.COL_TITLE);
        TextView titleTextView =(TextView) convertView.findViewById(R.id.kttk_1);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        switch (groupPosition){
            case kiemTraGroup: return kiemTraCursor.getCount();
            case dv3gGroup: return dv3gCursor.getCount();
            case tienIchGroup: return tienIchCursor.getCount();
            default: return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        switch (groupPosition){
            case kiemTraGroup: return kiemTraCursor;
            case dv3gGroup: return dv3gCursor;
            case tienIchGroup: return tienIchCursor;
            default:return null;
        }
    }

    @Override
    public int getGroupCount() {
        return groupCount;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = new TextView(activity);
        }
        ((TextView) convertView).setText("title");
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
