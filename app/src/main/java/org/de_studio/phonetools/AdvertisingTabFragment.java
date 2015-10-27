package org.de_studio.phonetools;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class AdvertisingTabFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static AdvertisingTabFragment newInstance(int sectionNumber) {
        AdvertisingTabFragment fragment = new AdvertisingTabFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    public AdvertisingTabFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_advertising_tab, container, false);
    }


}
