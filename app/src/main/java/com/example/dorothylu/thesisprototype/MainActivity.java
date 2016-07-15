package com.example.dorothylu.thesisprototype;



import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.dorothylu.thesisprototype.dummy.DummyContent;
import com.example.dorothylu.thesisprototype.dummy.Temp;

import java.util.ArrayList;
import java.util.List;

import data.FeedItem;

public class MainActivity extends AppCompatActivity implements Feed.OnListFragmentInteractionListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    /*
    private int[] tabIcons = {
            R.mipmap.newsfeed,
            R.mipmap.camera,
            R.mipmap.map,
            R.mipmap.notification,
            R.mipmap.settings

    };
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new Feed(), "Feed");
        adapter.addFragment(new Feed(), "Camera");
        adapter.addFragment(new Feed(), "Map");
        adapter.addFragment(new Feed(), "Notif");
        adapter.addFragment(new Feed(), "Settings");
        viewPager.setAdapter(adapter);
    }
    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("ONE");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.newsfeed, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("TWO");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.camera, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("THREE");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.map, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.setText("Four");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.notification, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);

        TextView tabFive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFive.setText("Five");
        tabFive.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.settings, 0, 0);
        tabLayout.getTabAt(4).setCustomView(tabFive);


    }

    @Override
    public void onListFragmentInteraction(FeedItem item) {

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }



        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}