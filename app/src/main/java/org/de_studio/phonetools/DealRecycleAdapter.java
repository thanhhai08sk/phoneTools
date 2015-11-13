package org.de_studio.phonetools;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by hai on 11/13/2015.
 */
public class DealRecycleAdapter extends RecyclerView.Adapter<DealRecycleAdapter.ViewHolder>{
    Context mContext;
    String[] mStrings;
    public DealRecycleAdapter(Context context,String[] strings){
        mContext = context;
        mStrings = strings;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            textView = (TextView) itemView.findViewById(R.id.deal_recycle_item_text_view);
        }

    }

    @Override
    public int getItemCount() {
        return mStrings.length;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView textView = holder.textView;
        String text = mStrings[position];
        textView.setText(text);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.deal_recycle_item,parent,false);
        return new ViewHolder(view);
    }
}
