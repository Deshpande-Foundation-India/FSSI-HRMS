package com.fssihrms;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import static com.fssihrms.Constant_webservice.URL;
//import hrms.com.hrm.R;

public class AnnouncementActivity extends AppCompatActivity {

    TabLayout tabLayout_announcement;
    int navbutton=0;

    // Titles of the individual pages (displayed in tabs)
    private final String[] PAGE_TITLES = new String[] {
            "NEWS",
            "EVENTS",
            "BIRTHDAY BUDDY"
            //"Page 3"
    };
    private int[] tabIcons = {
            R.drawable.news,
            R.drawable.event,
            R.drawable.birthdaybuddy
    };

    // The fragments that are used as the individual pages
    private final Fragment[] PAGES = new Fragment[] {
            new NewsFragment(),
            new EventsFragment(),
            new BirthdaybuddyFragment()
           /* new Page1Fragment(),
            new Page2Fragment()*/

            //  new Page3Fragment()
    };

    // The ViewPager is responsible for sliding pages (fragments) in and out upon user input
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        Toolbar toolbar = (Toolbar) findViewById(R.id.announcement_toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.announcement_viewpager);
        mViewPager.setAdapter(new AnnouncementActivity.MyPagerAdapter(getFragmentManager()));




        tabLayout_announcement = (TabLayout) findViewById(R.id.announcement_tablayout);
        tabLayout_announcement.setupWithViewPager(mViewPager);
        setupTabIcons();

        toolbar.setTitle("FSSI HRMS");
        toolbar.setTitleTextColor(Color.WHITE);

        toolbar.setNavigationIcon(R.drawable.lef);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getActivity().onBackPressed();
                Intent i = new Intent(AnnouncementActivity.this,HomeActivity.class);
                startActivity(i);
                finish();
            }
        });





    }// end of onCreate

    private void setupTabIcons() {
        tabLayout_announcement.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout_announcement.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout_announcement.getTabAt(2).setIcon(tabIcons[2]);

      /*  tabLayout_announcement.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               // tab.setIcon(R.drawable.leave);



                Log.d("Tab pos:",Integer.toString(tab.getPosition()));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/


    }// end of setupTabIcons


 //PagerAdapter for supplying the ViewPager with the pages (fragments) to display.

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            // return PAGES[position];
            switch (position) {
                case 0:
                    // Applyleave fragment activity
                    return new NewsFragment();
                case 1:
                    // Games fragment activity
                    return new EventsFragment();
                case 2:
                    return new BirthdaybuddyFragment();

            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {


            return PAGE_TITLES[position];
        }

    }

    public void onBackPressed()
    {
        //if(navbutton >= 1)
        {
            Intent i = new Intent(AnnouncementActivity.this,HomeActivity.class);
            startActivity(i);
            finish();
        }
        /*else
        {
            Toast.makeText(this, "Press the back button once again to go back.", Toast.LENGTH_SHORT).show();
            navbutton++;
        }*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(AnnouncementActivity.this,MainActivity.class);
            startActivity(i);
            finish();
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }



}// end of class
