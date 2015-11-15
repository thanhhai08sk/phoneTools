package org.de_studio.phonetools;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;


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
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(defaultSharedPreferenceName, 0);
        String carrier = sharedPreferences.getString("carrier","viettel");
        carrierSummary.setText(carrier);
        carrierPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentCarrier = sharedPreferences.getString("carrier", "viettel");
                int checkedChoice;
                switch (currentCarrier) {
                    case "viettel":
                        checkedChoice = 0;
                        break;
                    case "vinaphone":
                        checkedChoice = 1;
                        break;
                    case "mobifone":
                        checkedChoice = 2;
                        break;
                    case "vietnamobile":
                        checkedChoice = 3;
                        break;
                    default:
                        checkedChoice = 0;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.dialog_choose_carrier_title)
                        .setSingleChoiceItems(R.array.pref_carriers_options, checkedChoice, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] carrierArray = getResources().getStringArray(R.array.pref_carriers_values);
                                sharedPreferences.edit().putString("carrier", carrierArray[which]).putBoolean("fistLaunch", false).commit();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                carrierSummary.setText(sharedPreferences.getString("carrier", "viettel"));
                            }
                        });
                builder.show();
            }
        });

        LinearLayout notiPreference =(LinearLayout) rootView.findViewById(R.id.carrier_to_noti_preference_item);
        final TextView notiSummary = (TextView) notiPreference.findViewById(R.id.my_preference_item_carrier_to_noti_summary_text);
        final Set<String> defaultNoti = new HashSet<String>();
        defaultNoti.add("mobifone");
        defaultNoti.add("vinaphone");
        defaultNoti.add("viettel");
        Set<String> set = sharedPreferences.getStringSet("noti", defaultNoti);
        String[] summaries = set.toArray(new String[set.size()]);
        String summaryText ="";
        for (String sum : summaries) {
            summaryText = summaryText + sum + " ";
        }
        notiSummary.setText(summaryText);

        notiPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] defaultNotiArray = defaultNoti.toArray(new String[defaultNoti.size()]);
                final Set<String> notiSet = sharedPreferences.getStringSet("noti",defaultNoti);
                boolean[] checked = new boolean[3];
                checked[0] = notiSet.contains(defaultNotiArray[0]);
                checked[1] = notiSet.contains(defaultNotiArray[1]);
                checked[2] = notiSet.contains(defaultNotiArray[2]);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.dialog_noti_carriers_title)
                        .setMultiChoiceItems(defaultNotiArray, checked, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    notiSet.add(defaultNotiArray[which]);
                                } else {
                                    notiSet.remove(defaultNotiArray[which]);
                                }
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sharedPreferences.edit().putStringSet("noti", notiSet).commit();
                                String[] summaryText = notiSet.toArray(new String[notiSet.size()]);
                                String summary = "";
                                for (String sum : summaryText) {
                                    summary = summary + sum + " ";
                                }
                                notiSummary.setText(summary);
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