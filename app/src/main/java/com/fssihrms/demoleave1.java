package com.fssihrms;

/*
public class demoleave1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demoleave1);
    }
}
*/

//Starts here


/*
@SuppressLint("NewApi")
public class demoleave1 extends FragmentActivity implements
        ActionBar.TabListener {

   */
/* public class demoleave1 extends ActionBarActivity implements
            ActionBar.TabListener {
*//*

    private ViewPager viewPager;
    private TabPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Apply", "History"};

    Button top_bt1;
    ProgressDialog PD;

    private int[] tabIcons = {
            R.drawable.apply,
            R.drawable.history,

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demoleave1);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);

        actionBar = getActionBar();

         //actionBar = getSupportActionBar();


        mAdapter = new TabPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);





        // Adding Tabs
		*/
/*for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}*//*




        for (int i=0; i < tabs.length; i++)
        {
            actionBar.addTab(actionBar.newTab().setText(tabs[i]).setIcon(demoleave1.this.getResources().getDrawable(tabIcons[i]))
                    .setTabListener(this));
        }



        */
/**
         * on swiping the viewpager make respective tab selected
         * *//*

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);

				*/
/*if(position==1)
				{
					new MyAsync().execute();
				}*//*



            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }



    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view

        int x =tab.getPosition();
        System.out.println("X="+x);


        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }





}// end of demoleave1*/






