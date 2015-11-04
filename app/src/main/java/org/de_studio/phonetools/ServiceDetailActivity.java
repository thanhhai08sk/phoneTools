package org.de_studio.phonetools;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.Locale;

public class ServiceDetailActivity extends AppCompatActivity {
    SectionsPagerAdapter mSectionsPagerAdapter;
    private static final int GN_ITEM =0;
    private static final int DV_3G_ITEM =1;
    private static final int TI_ITEM = 2;
    ViewPager mViewPager;
    private static final String LOG_TAG = ServiceDetailTabbarFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String category = getIntent().getExtras().getString(ServicesFragment.EXTRA_KEY,"");
        setContentView(R.layout.activity_service_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.service_detail_viewPager);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        switch (category){
            case "gn": mViewPager.setCurrentItem(0);
                break;
            case "3g": mViewPager.setCurrentItem(1);
                break;
            case "ti": mViewPager.setCurrentItem(2);
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.service_detail_tabbar);
        tabLayout.setupWithViewPager(mViewPager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    ServiceDetailTabbarFragment fragment1 = ServiceDetailTabbarFragment.newInstance(position + 1);
                    fragment1.setCategory(position+1);
                    return fragment1;
                case 1:
                    ServiceDetailTabbarFragment fragment2 = ServiceDetailTabbarFragment.newInstance(position + 1);
                    fragment2.setCategory(position+1);
                    return fragment2;
                case 2:
                    ServiceDetailTabbarFragment fragment3 = ServiceDetailTabbarFragment.newInstance(position + 1);
                    fragment3.setCategory(position+1);
                    return fragment3;

            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Gọi điện-nhắn tin".toLowerCase();
                case 1:
                    return "Dịch vụ 3G".toLowerCase();
                case 2:
                    return "Tiện ích".toLowerCase();

            }
            return null;
        }
    }

}
