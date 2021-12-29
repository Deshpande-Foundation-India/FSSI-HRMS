package com.fssihrms;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import static com.fssihrms.Constant_webservice.URL;
//import hrms.com.hrm.R;

public class Selfservice extends AppCompatActivity {

    Spinner onlinerequest_sp;

    Button selfservice_bt;
    EditText description_et;
    String emp_idServicestring="";
    long  emp_idServicelong;

    String requestedforString="";
    String  descriptiontoString="";

    int navbutton=0;


    TextView onlinerequest_tv,description_tv;


    Context context;

    String username, pwd, emp_id, internet_issue = "empty";
   /* ImageView img;
    byte[] imageAsBytes;*/


    long Employeeidlong;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfservice);

        context = getApplicationContext();

      /*  // get action bar
         actionBar = getActionBar();

        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);*/


       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // what do you want here
            }
        });
*/




        selfservice_bt= (Button)findViewById(R.id.selfservicesubmit_BT);
        description_et =(EditText)findViewById(R.id.description_ET);

        /*img =(ImageView)findViewById(R.id.img);*/

        allthetextview_selfservice();
        changethefont_selfservice();



        SharedPreferences myprefs = this.getApplicationContext().getSharedPreferences("user", Context.MODE_WORLD_READABLE);
        username = myprefs.getString("user1", "nothing");
        pwd = myprefs.getString("pwd", "nothing");
        emp_id = myprefs.getString("emp_id", "nothing");

        Employeeidlong = Long.parseLong(emp_id); // for web service


        String[] spinnerdropdown = {
                "Any Policy Related","Confirmation Letter","ESI Letter","ID Card","Internet Credential, If Forgot","Payslip", "Service Letter", "TA Bill Pending", "Variation in Stipend / Salary"};

        onlinerequest_sp = (Spinner) findViewById(R.id.onlinerequest_SP);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerdropdown);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        onlinerequest_sp.setAdapter(adapter);



        selfservice_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestedforString=onlinerequest_sp.getSelectedItem().toString();

                descriptiontoString =description_et.getText().toString();

                //  Toast.makeText(getActivity(), requestedforString,Toast.LENGTH_LONG).show();

                validation();

                if(validation()) {

                    if (netconnection()) {
                       AsyncCalltoSelfservice taskSelfservice = new AsyncCalltoSelfservice(Selfservice.this);
                        taskSelfservice.execute();
                       // Toast.makeText(getApplicationContext(), "Request submitted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Check the internet connection", Toast.LENGTH_LONG).show();
                    }

                }//end of validation
            }
        });


    }// end of oncreate



    //allthetextview_selfservice() declaration  and font assign


    public void allthetextview_selfservice() {
        onlinerequest_tv = (TextView) findViewById(R.id.onlinerequest_TV);
        description_tv =(TextView)findViewById(R.id.description_TV);
    }
    public void changethefont_selfservice(){
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/centurygothicbold.ttf");

        onlinerequest_tv.setTypeface(face);
        description_tv.setTypeface(face);
    }
    // End of  allthetextview_selfservice() declaration  and font assign



    public void onBackPressed()
    {
        //if(navbutton >= 1)
        {
            Intent i = new Intent(Selfservice.this,HomeActivity.class);
            startActivity(i);
            finish();
        }
        /*else
        {
            Toast.makeText(this, "Press the back button once again to go back.", Toast.LENGTH_SHORT).show();
            navbutton++;
        }*/


    }






    public boolean validation()
    {



        boolean validationresultdescription1=true,validationresultdescription2=true,validationresultdescription3=true;

        if(description_et.getText().toString().length()==0)
        {
            description_et.setError("Empty not allowed!");
            description_et.requestFocus();validationresultdescription1=false;
        }
        if( (description_et.getText().toString().length()<10 )||(description_et.getText().toString().trim().length()<10 ))
        {
            description_et.setError("Minimum 10 character required");
            description_et.requestFocus();validationresultdescription2=false;
        }
        if(description_et.getText().toString().length()>100)
        {
            description_et.setError("Maximum 100 character only");
            description_et.requestFocus();validationresultdescription3=false;
        }

        if(validationresultdescription1&&validationresultdescription2&&validationresultdescription3)
        {
            return true;
        }
        else{ return false;}

    }



    public boolean netconnection()
    {
        Boolean net=false;
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (null != activeNetwork) {

            //WiFi connectivity
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                return (net=true);
            }
            //Mobile Network
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                return (net=true);}
        }
        if(activeNetwork==null) {return (net=false);}
        return net;
    }


    private class AsyncCalltoSelfservice extends AsyncTask<String, Void, Void>
    {

        String response_AsyncCalltoSelfservice="";

        ProgressDialog dialog;






        Context context;
        @Override
        protected void onPreExecute() {
            //  Log.i(TAG, "onPreExecute---tab2");
           dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //Log.i(TAG, "onProgressUpdate---tab2");
        }


        @Override
        protected Void doInBackground(String... params) {
            Log.i("HRMS selfservice", "doInBackground");
            //  GetAllEvents();
            // call of the leavecancelwebservice
            if(2>1)
            {

                //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";

                //http://dftestbed.cloudapp.net/PMSservice.asmx  //list of webservice
                //http://dftestbed.cloudapp.net/PMSservice.asmx?op=LeaveCancelRequest  //contains detailas of LeaveCancelRequest

               /* String URL ="http://dftestbed.cloudapp.net/PMSservice.asmx?WSDL";  // WSDL file
                String METHOD_NAME = "RequestEmployeeService";//leave
                String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/RequestEmployeeService";
*/

               //http://dethrms.cloudapp.net/PMSservice.asmx?op=RequestEmployeeService

               // String URL ="http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";  // WSDL file
                String METHOD_NAME = "RequestEmployeeService";//leave
                String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/RequestEmployeeService";


                try{

                    SoapObject request = new SoapObject(Namespace, METHOD_NAME);
                    /*request.addProperty("empId", emp_idServicelong);   //<empId>long</empId>
                    request.addProperty("ServiceCode", requestedforString); //<ServiceCode>string</ServiceCode>
                    request.addProperty("ServiceDescription", descriptiontoString);//<ServiceDescription>string</ServiceDescription>
*/
                    /*request.addProperty("empId", 90);   //<empId>long</empId>
                    request.addProperty("ServiceCode", "ID card"); //<ServiceCode>string</ServiceCode>
                    request.addProperty("ServiceDescription", "ID card missing");
*/

                    request.addProperty("empId", Employeeidlong);   //<empId>long</empId>
                    request.addProperty("ServiceCode", requestedforString); //<ServiceCode>string</ServiceCode>
                    request.addProperty("ServiceDescription", descriptiontoString);

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    //Set output SOAP object
                    envelope.setOutputSoapObject(request);
                    //Create HTTP call object
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                    try
                    {

                        androidHttpTransport.call(SOAPACTION, envelope);
                        SoapPrimitive response = (SoapPrimitive ) envelope.getResponse();

                        response_AsyncCalltoSelfservice=response.toString();

                        System.out.println("Selfservice response:"+response_AsyncCalltoSelfservice);
                        Log.d("response Selfservice",response.toString());
                    }
                    catch (Throwable t) {
                        //Toast.makeText(MainActivity.this, "Request failed: " + t.toString(),
                        //		Toast.LENGTH_LONG).show();
                        Log.e("request fail", "> " + t.getMessage());
                    }
                }catch (Throwable t) {
                    Log.e("UnRegister  Error", "> " + t.getMessage());

                }


            }// end of if statement


            return null;
        }

        public AsyncCalltoSelfservice(Selfservice activity) {
            context = activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPostExecute(Void result) {

           /* if ((this.dialog != null) && this.dialog.isShowing()) {
                dialog.dismiss();

            }*/

            dialog.dismiss();


            if(response_AsyncCalltoSelfservice.equals("Request sent to HR"))
            {
                Toast.makeText(getApplicationContext(),requestedforString+" request has been sent to HR",Toast.LENGTH_LONG).show();
                description_et.setText("");
                Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(i);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Server busy,Please try later...",Toast.LENGTH_LONG).show();
            }



        }



    }//End of AsyncCalltoSelfservice






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
            Intent i = new Intent(Selfservice.this,MainActivity.class);
            startActivity(i);
            finish();
           // return true;
        }
        else
        {
            Intent i = new Intent(Selfservice.this,HomeActivity.class);
            startActivity(i);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }





}// end of class




   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
*/

//}// end of class
