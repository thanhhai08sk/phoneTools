package org.de_studio.phonetools;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.de_studio.phonetools.service.DealService;

import java.io.IOException;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private Intent mDealServiceIntent;
    ViewPager mViewPager;
    private static final int MAIN_LOADER = 0;
    MainFragment mainFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        final SharedPreferences sharedPreferences =getSharedPreferences(SettingActivity.defaultSharedPreferenceName, 0);
        if (!sharedPreferences.contains("fistLaunch")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.dialog_choose_carrier_title)
                    .setSingleChoiceItems(R.array.pref_carriers_options, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String[] carrierArray = getResources().getStringArray(R.array.pref_carriers_values);
                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SettingActivity.defaultSharedPreferenceName, 0);
                            sharedPreferences.edit().putString("carrier", carrierArray[which]).putBoolean("fistLaunch",false).commit();
                            Toast.makeText(getApplicationContext(), "Carrier is : " + carrierArray[which] + " and preference is: " + sharedPreferences.getString("carrier", ""), Toast.LENGTH_LONG).show();
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sharedPreferences.edit().putBoolean("fistLaunch",false).commit();
                            mainFragment.onCarrierChange();
                        }
                    });
            builder.show();
        }

        try{
            dataBaseHelper.createDataBase();
        }catch (IOException e){
            throw new Error("Unable to create database");
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabbar);
        tabLayout.setupWithViewPager(mViewPager);
        mDealServiceIntent = new Intent(this, DealService.class);
        startService(mDealServiceIntent);
        Intent alarmIntent = new Intent(this, DealService.AlarmReceiver2.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0,alarmIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2 * 60 * 60 * 1000, 2 * 60 * 60 * 1000, pi);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                     mainFragment = MainFragment.newInstance(position + 1);
                    return mainFragment;
                case 1:
                    return ServicesFragment.newInstance(position + 1);
                case 2:
                    return DealFragment.newInstance(position +1);
                case 3:
                    return MyPreferenceFragment.newInstance(position +1);

            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Trang chủ";
                case 1:
                    return "Dịch vụ";
                case 2:
                    return "Khuyến mãi";
                case 3:
                    return "Thiết lập";

            }
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.getContentResolver().notifyChange(PhoneToolsContract.ActionEntry.CONTENT_URI, null);

    }



}
