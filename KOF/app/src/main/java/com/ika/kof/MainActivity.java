package com.ika.kof;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.ika.kof.Aktifitas.AboutPage;
import com.ika.kof.Aktifitas.SettingPage;
import com.ika.kof.fragment.FragmentBluetooth;
import com.ika.kof.fragment.FragmentContact;
import com.ika.kof.fragment.FragmentGraph;
import com.ika.kof.fragment.FragmentNews;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;

    private static final int PAGE_GRAPH = 0;
    private static final int PAGE_BLUETOOTH = 1;
    private static final int PAGE_NEWS = 2;
    private static final int PAGE_CONTACT = 3;

    private String tabGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.blueskyNom));

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOffscreenPageLimit(4);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.linechart);
        tabLayout.getTabAt(1).setIcon(R.drawable.bluetooth2);
        tabLayout.getTabAt(2).setIcon(R.drawable.foldednewspaper);
        tabLayout.getTabAt(3).setIcon(R.drawable.contact);

        inisialisasiListener();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_graph, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            aboutPage();
            return true;
        } else if (id == R.id.action_setting){
            settingPage();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void inisialisasiListener() {
        DetailOnPageChangeListener pageListener = new DetailOnPageChangeListener();
        mViewPager.setOnPageChangeListener(pageListener);
    }

    private void aboutPage() {
        Intent intent = new Intent(MainActivity.this, AboutPage.class);
        MainActivity.this.startActivity(intent);
    }

    private void settingPage() {
        Intent intent = new Intent(MainActivity.this, SettingPage.class);
        MainActivity.this.startActivity(intent);
    }

    /**
     * Get the current view position from the ViewPager by
     * extending SimpleOnPageChangeListener class and adding your method
     */
    public class DetailOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        private int currentPage;

        @Override
        public void onPageSelected(int position) {
            int page = position;
            Log.d("pageeee : ",page + "");
            switch (page) {
                case 0:
                    toolbar.setTitle("Graph");
                    break;
                case 1:
                    toolbar.setTitle("Bluetooth");
                    break;
                case 2:
                    toolbar.setTitle("Article & News");
                    break;
                case 3:
                    toolbar.setTitle("Laboratory Contact");
                    break;
            }
            currentPage = position;
        }

        public final int getCurrentPage() {
            return currentPage;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        FragmentGraph fragmentGraph = new FragmentGraph();
        FragmentBluetooth fragmentBluetooth = new FragmentBluetooth();
        FragmentNews fragmentNews = new FragmentNews();
        FragmentContact fragmentContact = new FragmentContact();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case PAGE_GRAPH :
                    return fragmentGraph;
                case PAGE_BLUETOOTH :
                    return fragmentBluetooth;
                case PAGE_NEWS :
                    return fragmentNews;
                case PAGE_CONTACT :
                    return fragmentContact;
                default:
                    return fragmentGraph;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                default:
                    return "";
            }
        }
    }

    public void setTabGraph(String a) {
        tabGraph = a;
    }

    public String getTabGraph() {
        return tabGraph;
    }
}
