package org.de_studio.phonetools;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class DealFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String LOG_TAG = DealFragment.class.getSimpleName();

    public DealFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deal, container, false);
    }


}
