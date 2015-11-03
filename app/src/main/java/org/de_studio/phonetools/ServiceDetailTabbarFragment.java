package org.de_studio.phonetools;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ServiceDetailTabbarFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String LOG_TAG = ServiceDetailTabbarFragment.class.getSimpleName();
    private static final int LOADER = 0;

    public static ServiceDetailTabbarFragment newInstance(int sectionNumber) {
        ServiceDetailTabbarFragment fragment = new ServiceDetailTabbarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    public ServiceDetailTabbarFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_service_detail_tabbar, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.service_detail_tabbar_recycle_view);

        return rootView;
    }


}
