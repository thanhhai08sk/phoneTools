package org.de_studio.phonetools;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;


public class ItemDetailFragment extends ListFragment {

    public static final String ARG_ITEM_ID = "item_id";
    private static Integer mPosition;
    ArrayList<String> list;



    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            mPosition = ItemDetailActivity.mPosition;

        list = new ArrayList<String>();
        list.add(getString(R.string.list_tong_dai));
        list.add(getString(R.string.list_3g));
        list.add(getString(R.string.list_tien_ich));


        setListAdapter(new ArrayAdapter<String>(
                getActivity(),
                R.layout.fragment_item_detail,
                R.id.item_detail,
                list));


    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);
//
//        // Show the dummy content as text in a TextView.
//        if (mPosition != null) {
//            ((TextView) rootView.findViewById(R.id.item_detail)).setText("helllllo");
//        }
//
//        return rootView;
//    }
}
