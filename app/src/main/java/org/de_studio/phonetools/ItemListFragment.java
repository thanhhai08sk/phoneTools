package org.de_studio.phonetools;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ItemListFragment extends ListFragment {

    private Callbacks mCallbacks = sDummyCallbacks;
    ArrayList<String> list;

    public interface Callbacks {

        public void onItemSelected(int id);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(int id) {
        }
    };

    public ItemListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         list = new ArrayList<String>();
        list.add(getString(R.string.list_goi_nhan_tin));
        list.add(getString(R.string.list_3g));
        list.add(getString(R.string.list_tien_ich));
        list.add(getString(R.string.list_tong_dai));


        setListAdapter(new ArrayAdapter<String>(
                getActivity(),
                R.layout.fragment_item_list,
                R.id.item_list_item,
                list));
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
//        mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
        mCallbacks.onItemSelected(position +1);
    }





}
