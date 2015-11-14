package org.de_studio.phonetools;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class DealFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String LOG_TAG = DealFragment.class.getSimpleName();
    DealRecycleAdapter mRecycleAdapter;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String[] text = msg.getData().getStringArray("result");
            mRecycleAdapter.setmStrings(text);
            mRecycleAdapter.notifyDataSetChanged();
        }
    };

    public static DealFragment newInstance(int sectionNumber) {
        DealFragment fragment = new DealFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    public DealFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String[] testResultString = new String[10];
                try {
                    String url = "https://vienthong.com.vn/tin-tuc/tin-khuyen-mai/";
                    Document document = Jsoup.connect(url).get();
                    Elements elements = document.select("li.clearfix a[href] [title]");
                    for (int i=0; i< testResultString.length ; i++){
                        Element element = elements.get(i);
                        String title = element.attr("title");
                        testResultString[i] = title;
                    }
                }catch (IOException e){
                    Log.e(LOG_TAG, "err get information from website");
                }
                Bundle bundle = new Bundle();
                bundle.putStringArray("result",testResultString);
                Message message = new Message();
                message.setData(bundle);
                handler.sendMessage(message);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deal,container,false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.deal_fragment_recycle_view);
        mRecycleAdapter = new DealRecycleAdapter(getActivity(),new String[10]);
        recyclerView.setAdapter(mRecycleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;
    }


}
