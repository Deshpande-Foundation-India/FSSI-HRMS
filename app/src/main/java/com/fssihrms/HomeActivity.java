package com.fssihrms;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import hrms.com.hrm.R;

public class HomeActivity extends AppCompatActivity {


ImageView leave_iv,selfservice_iv,announcement_iv, holiday_iv,od_iv;


    TextView employeename_tv,designation_tv,leave_tv,selfservice_tv,announcement_tv,holiday_tv;


    String pwd,username,emp_id;
    int navbutton=0;

    Fragment fragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SharedPreferences myprefs= this.getSharedPreferences("user", Context.MODE_WORLD_READABLE);
        username = myprefs.getString("user1", "nothing");
        pwd = myprefs.getString("pwd", "nothing");
        emp_id = myprefs.getString("emp_id", "nothing");


        leave_iv=(ImageView) findViewById(R.id.leave_IV);
        selfservice_iv=(ImageView)findViewById(R.id.selfservice_IV);
        announcement_iv=(ImageView)findViewById(R.id.announcement_IV);
        holiday_iv=(ImageView)findViewById(R.id.holiday_IV);
        od_iv=(ImageView)findViewById(R.id.OD_IV);

        allthetextview_homeactivity();
        changethefont();


        employeename_tv.setText(username);


        leave_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent i  = new Intent (getApplicationContext(),demoleave1.class);
                startActivity(i);
                finish();*/

                //startActivity(new Intent(getApplicationContext(), demoleave1.class));

                //Toast.makeText(getApplicationContext(),"leave",Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(),demoleave3.class);
                startActivity(i);
                finish();

            }
        });

        selfservice_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i  = new Intent (getApplicationContext(),Selfservice.class);
                startActivity(i);
                finish();




            }
        });


        announcement_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent (getApplicationContext(),AnnouncementActivity.class);
                startActivity(i);
                finish();
            }
        });


        holiday_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent (getApplicationContext(),Holiday_Activity.class);
                startActivity(i);
                finish();
            }
        });



        od_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i  = new Intent (getApplicationContext(),OndutyActivity.class);
                    startActivity(i);
                    finish();
            }
        });
    }// End of Oncreate()


    public void allthetextview_homeactivity()
    {
        employeename_tv=(TextView) findViewById(R.id.employeename_TV);
        designation_tv = (TextView)findViewById(R.id.designation_TV);


        leave_tv = (TextView)findViewById(R.id.leave_TV);
        selfservice_tv = (TextView)findViewById(R.id.selfservice_TV);
        announcement_tv = (TextView)findViewById(R.id.announcement_TV);
        holiday_tv= (TextView)findViewById(R.id.holiday_TV);
    }

    public void changethefont()
    {
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/centurygothic.ttf");

        employeename_tv.setText("Venkatesh K");
        employeename_tv.setTypeface(face);

       /* designation_tv.setText("Software Developer");
        designation_tv.setTypeface(face);*/

        leave_tv.setTypeface(face);
       selfservice_tv.setTypeface(face);
        announcement_tv.setTypeface(face);
        holiday_tv.setTypeface(face);


    }


    public void onBackPressed()
    {
        if(navbutton >= 1)
        {

            Toast.makeText(this, "Kindly press Logout button", Toast.LENGTH_SHORT).show();
            /*Intent i = new Intent(HomeActivity.this,MainActivity.class);
            startActivity(i);
            finish();*/
        }
        else
        {
            Toast.makeText(this, "Kindly press Logout button", Toast.LENGTH_SHORT).show();
           navbutton++;
        }


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
            Intent i = new Intent(HomeActivity.this,MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }

                return super.onOptionsItemSelected(item);
        }




}// End of class
