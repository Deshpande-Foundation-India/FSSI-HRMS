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

public class OndutyActivity extends AppCompatActivity {

    TabLayout tablayout_onduty;

    int navbutton=0;


    // Titles of the individual pages (displayed in tabs)
    private final String[] PAGE_TITLES = new String[] {
            "APPLY ONDUTY",
            "HISTORY"
            //"Page 2"
    };

    // The fragments that are used as the individual pages
    private final Fragment[] PAGES = new Fragment[] {
            new fragment_applyonduty(),
            new fragment_ondutyhistory()
           /* new Page1Fragment(),
            new Page2Fragment()*/

            //  new Page3Fragment()
    };


    // The ViewPager is responsible for sliding pages (fragments) in and out upon user input
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onduty);

        Toolbar toolbar = (Toolbar) findViewById(R.id.onduty_toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.onduty_viewpager);
        mViewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));


        tablayout_onduty= (TabLayout) findViewById(R.id.Onduty_tablayout);
        tablayout_onduty.setupWithViewPager(mViewPager);

        toolbar.setTitle("FSSI HRMS");
        toolbar.setTitleTextColor(Color.WHITE);

        toolbar.setNavigationIcon(R.drawable.lef);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getActivity().onBackPressed();
                Intent i = new Intent(OndutyActivity.this,HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

    }// end of Oncreate


    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            // return PAGES[position];
            switch (position) {
                case 0:
                    // fragment_applyonduty
                    return new fragment_applyonduty();
                case 1:
                    // fragment_ondutyhistory
                    return new fragment_ondutyhistory();

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

    }// end of MyPagerAdapter


    public void onBackPressed()
    {
       // if(navbutton >= 1)
        {
            Intent i = new Intent(OndutyActivity.this,HomeActivity.class);
            startActivity(i);
            finish();
        }
       /* else
        {
            Toast.makeText(this, "Press the back button once again to go back.", Toast.LENGTH_SHORT).show();
            navbutton++;
        }
*/

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
            Intent i = new Intent(OndutyActivity.this,MainActivity.class);
            startActivity(i);
            finish();
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }


}// End of class
