package org.de_studio.phonetools;

/**
 * Created by hai on 10/13/2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ServicesFragment extends Fragment {
    ArrayList<String> list;
    private String mCategory;
    public static final String EXTRA_KEY = "category";
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static ServicesFragment newInstance(int sectionNumber) {
        ServicesFragment fragment = new ServicesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_services, container, false);
        TextView goiDienNhanTin = (TextView)v.findViewById(R.id.service_goi_dien_nhan_tin_title);
        TextView dichVu3g = (TextView)v.findViewById(R.id.service_dich_vu_3g_title);
        TextView tienIch = (TextView)v.findViewById(R.id.service_tien_ich_khac_title);
        goiDienNhanTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ServiceDetailActivity.class);
                intent.putExtra(EXTRA_KEY,"gn");
                startActivity(intent);
            }
        });
        dichVu3g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ServiceDetailActivity.class);
                intent.putExtra(EXTRA_KEY,"3g");
                startActivity(intent);
            }
        });
        tienIch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ServiceDetailActivity.class);
                intent.putExtra(EXTRA_KEY,"ti");
                startActivity(intent);
            }
        });


        return v;
    }



}

