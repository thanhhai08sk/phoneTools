package org.de_studio.phonetools;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by hai on 11/9/2015.
 */
public  class MyPreferenceFragment extends Fragment {
    private static final String LOG_TAG = MyPreferenceFragment.class.getSimpleName();
    public static final String defaultSharedPreferenceName = "org.de_studio.phonetools_preferences";
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static MyPreferenceFragment newInstance(int sectionNumber) {
        MyPreferenceFragment fragment = new MyPreferenceFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    public MyPreferenceFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_preference, container, false);
        final LinearLayout carrierPreference = (LinearLayout) rootView.findViewById(R.id.carrier_preference_item);
        final TextView carrierSummary = (TextView) carrierPreference.findViewById(R.id.my_preference_item_carrier_summary_text);
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SettingActivity.defaultSharedPreferenceName, 0);
        String carrier = sharedPreferences.getString("carrier","viettel");
        carrierSummary.setText(carrier);
        carrierPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentCarrier = sharedPreferences.getString("carrier", "viettel");
                int checkedChoice;
                switch (currentCarrier){
                    case "viettel": checkedChoice =0;
                        break;
                    case "vinaphone": checkedChoice =1;
                        break;
                    case "mobifone": checkedChoice =2;
                        break;
                    case "vietnamobile": checkedChoice =3;
                        break;
                    default: checkedChoice =0;
                }
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.dialog_choose_carrier_title)
                        .setSingleChoiceItems(R.array.pref_carriers_options, checkedChoice, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] carrierArray = getResources().getStringArray(R.array.pref_carriers_values);
                                sharedPreferences.edit().putString("carrier", carrierArray[which]).putBoolean("fistLaunch",false).commit();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                carrierSummary.setText(sharedPreferences.getString("carrier","viettel"));
                            }
                        });
                builder.show();
            }
        });
        return rootView;
    }

    private LinearLayout findPreference(String name){
        if (name.equals("carrier")){
            return (LinearLayout) getView().findViewById(R.id.carrier_preference_item);
        }else return null;
    }




}