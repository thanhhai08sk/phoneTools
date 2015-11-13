package org.de_studio.phonetools;

/**
 * Created by hai on 10/13/2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class ServicesFragment extends Fragment {
    private static final String LOG_TAG = ServicesFragment.class.getSimpleName();
    ArrayList<String> list;
    private String mCategory;
    public static final String EXTRA_KEY = "category";
    private static final String ARG_SECTION_NUMBER = "section_number";
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            TextView result =(TextView) getView().findViewById(R.id.service_teset_result);
            String text = msg.getData().getString("result","nothing");
            result.setText(text);
        }
    };

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

        Button testButton = (Button) v.findViewById(R.id.service_test_button);
        final TextView testResult = (TextView) v.findViewById(R.id.service_teset_result);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        String testResultString ="";
                        try {
                            String url = "https://vienthong.com.vn/tin-tuc/tin-khuyen-mai/";
                            Document document = Jsoup.connect(url).get();
                            Elements elements = document.select("li.clearfix:lt(3) a[href] [title]");
                            for (Element  element : elements){
                                String title = element.attr("title");
                                testResultString = testResultString + title;
                            }
                        }catch (IOException e){
                            Log.e(LOG_TAG,"err get information from website");
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("result",testResultString);
                        Message message = new Message();
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();

            }
        });
        return v;
    }



}

