package org.de_studio.phonetools;

/**
 * Created by hai on 10/13/2015.
 */
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class ServicesFragment extends ListFragment {
    ArrayList<String> list;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static ServicesFragment newInstance(int sectionNumber) {
        ServicesFragment fragment = new ServicesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
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
    //    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v =inflater.inflate(R.layout.fragment_services,container,false);
//        return v;
//    }
}

