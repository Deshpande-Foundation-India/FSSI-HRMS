package com.fssihrms;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import static com.fssihrms.Constant_webservice.URL;
//import hrms.com.hrm.R;

public class demoleave3 extends AppCompatActivity {

    TabLayout tabLayout;
    int navbutton=0;
    // Titles of the individual pages (displayed in tabs)
    private final String[] PAGE_TITLES = new String[] {
            "Apply Leave",
            "History"
            //"Page 3"
    };
    private int[] tabIcons = {
            R.drawable.apply,
            R.drawable.history
    };

    // The fragments that are used as the individual pages
    private final Fragment[] PAGES = new Fragment[]
            {
            new Applyleave(),
            new Leavehistoryfragment()
          /*new Page1Fragment(),
            new Page2Fragment()*/
            //  new Page3Fragment()
    };

    // The ViewPager is responsible for sliding pages (fragments) in and out upon user input
    private ViewPager mViewPager;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demoleave3);

        // Set the Toolbar as the activity's app bar (instead of the default ActionBar)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("FSSI HRMS");
        toolbar.setTitleTextColor(Color.WHITE);

        toolbar.setNavigationIcon(R.drawable.lef);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // getActivity().onBackPressed();
                Intent i = new Intent(demoleave3.this,HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

         /*final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);*/

        // Connect the ViewPager to our custom PagerAdapter. The PagerAdapter supplies the pages
        // (fragments) to the ViewPager, which the ViewPager needs to display.
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));

        // Connect the tabs with the ViewPager (the setupWithViewPager method does this for us in
        // both directions, i.e. when a new tab is selected, the ViewPager switches to this page,
        // and when the ViewPager switches to a new page, the corresponding tab is selected)
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();


    }//End of Oncreate

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);

    }


    /* PagerAdapter for supplying the ViewPager with the pages (fragments) to display. */
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
                    return new Applyleave();
                case 1:
                    // Games fragment activity
                    return new Leavehistoryfragment();

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


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }*/

    public void onBackPressed()
    {
       // if(navbutton >= 1)
        {
            Intent i = new Intent(demoleave3.this,HomeActivity.class);
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
            Intent i = new Intent(demoleave3.this,MainActivity.class);
            startActivity(i);
            finish();
           // return true;
        }else{

            Intent i = new Intent(demoleave3.this,HomeActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}// end of class
