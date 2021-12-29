package com.fssihrms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.*;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

import com.google.firebase.iid.FirebaseInstanceId;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.UUID;
import java.util.Vector;


import static com.fssihrms.Constant_webservice.URL;
import static java.lang.System.exit;

//import hrms.com.hrm.R;

public class MainActivity extends AppCompatActivity {


    Button loginbt;

    EditText username_et, password_et;
String usernamestring,passwordstring;
    String versioncode, approve_key = "no", Approve_result = "no", Reject_result = "no";
    String Approveleavecancel_result = "no",Rejectleavecancel="no";

    ProgressBar spinner;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;

    String login_result = "empty", employee_info = "empty", Prefusername = "nothing", Prefpwd;

    String MessegValue = "Message", MobileValue = "MobileNo", release_not = "nothing", continue_login = "No Update";
    SoapPrimitive messeg, Mobile, valueAt0, release_not1, version, messeg_getalldetail;

    private static final String TAG = MainActivity.class.getSimpleName();
    final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;

    String  myVersion, deviceBRAND, deviceHARDWARE, devicePRODUCT, deviceUSER, deviceModelName, deviceId, tmDevice, tmSerial, androidId, simOperatorName, sdkver, mobileNumber;
    int sdkVersion, Measuredwidth = 0, Measuredheight = 0, update_flage = 0;
    AsyncTask<Void, Void, Void> mRegisterTask;

    TelephonyManager tm1=null;

String regId ="dvxz";

    TextView rejectreason_et,version_tv;



    int versionCodes;
    boolean versionvalue=true;
    String versioncodeInString;
    String versiontext="Version ";
   // boolean versionval=true;

    boolean sub=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


version_tv=(TextView)findViewById(R.id.version_TV);

        myVersion="";
        deviceBRAND="";
        deviceHARDWARE="";
        devicePRODUCT="";
        deviceUSER="";
        deviceModelName="";
        deviceId="";
        tmDevice="";
        tmSerial="";
        androidId="";
        simOperatorName="";
        sdkver="";
        mobileNumber="";









        String notMsg = "";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {


            //notMsg = extras.getString("messege");
            notMsg = extras.getString("2");

               // extras.getString("PMCancel").toString();


            if (notMsg != null && !notMsg.isEmpty()) {

                if(notMsg.contains("PMNote"))
                {
                    showCustomDialog(notMsg);
                }
                else if(notMsg.contains("PMOnduty"))
                {
                    showOndutyCustomDialog(notMsg);
                }
                else if(notMsg.contains("PMCancel"))
                {
                    leavecancelCustomDialog(notMsg);
                }
                else
                {
                   Normalnotification(notMsg);
                }
            }
        }

       // FirebaseApp.initializeApp(this);
        username_et = (EditText) findViewById(R.id.username_ET);
        password_et = (EditText) findViewById(R.id.password_ET);

        SharedPreferences myprefs = getSharedPreferences("user", MODE_WORLD_READABLE);
        //Toast.makeText(Instant.this,myprefs.getAll().toString(),Toast.LENGTH_LONG).show();
        Prefusername = myprefs.getString("user1", "nothing");
        Prefpwd = myprefs.getString("pwd", "nothing");

        System.out.println("Prefusername:"+Prefusername);

        if (!Prefusername.equals("nothing")) {
            username_et.setText(Prefusername);
        }



        loginbt= (Button) findViewById(R.id.loginBtn);

        loginbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               /* Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);*/


                cd = new ConnectionDetector(getApplicationContext());
                isInternetPresent = cd.isConnectingToInternet();


                if (isInternetPresent) {
                    if (username_et.getText().toString().equals("") || password_et.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Blank Fields", Toast.LENGTH_SHORT).show();
                    } else {
                        usernamestring = username_et.getText().toString();
                        passwordstring= password_et.getText().toString();



                      /*  AsyncCallForceUpdate task1 = new AsyncCallForceUpdate(MainActivity.this);
                        task1.execute();*/


                        AsyncCallNorLogin task = new  AsyncCallNorLogin(MainActivity.this);
                        task.execute();

                    }
			/*		 Intent i  = new Intent (getApplicationContext(),Slide_MainActivity.class);
							startActivity(i);
							finish();*/

                } else {

                   // Notificationdialogue(login.this, "No Internet Connection", "");

                    Toast.makeText(getApplicationContext(),"No internet",Toast.LENGTH_LONG).show();


                }

            }
        });



        if (ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    android.Manifest.permission.READ_PHONE_STATE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);



                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        // Marshmellow fix end



        try {
            versionCodes = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        versioncodeInString=Integer.toString(versionCodes);
        //version_tv.setText(versiontext+versioncodeInString);


    }//end of oncreate





    public void onBackPressed() {
        //if(navbutton >= 1)
        {
            finish();
            exit(0);
        }

    }

    //Normal login AsyncTask
    private class AsyncCallNorLogin extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        Context context;

        boolean   versionval;

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate---tab2");
        }

        public AsyncCallNorLogin(MainActivity activity) {
            context = activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");


           /* ChekVersion();
            if (continue_login.equals("No Update"))

            {
                Login_Verify(usernamestring, passwordstring);

                if (login_result.equals("sucess")) {
                   // setGCM();
                    //setGCM();
                }
            }*/
            versionval =	appversioncheck(versioncodeInString);

            Login_Verify(usernamestring, passwordstring);

            if (login_result.equals("sucess")){
               /* setGCM();
                setGCM();*/
                call();



            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();

            }
            Log.i(TAG, "onPostExecute");

            if (login_result.equals("slow internet")) {
                Notificationdialogue(MainActivity.this, "slow Internet Connection", "");
            } else {
                if (continue_login.equals("No Update")) {
                    if (login_result.equals("sucess"))
                    {
                        if(versionval) {
                            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                            SharedPreferences myprefs = MainActivity.this.getSharedPreferences("user", MODE_WORLD_READABLE);
                            myprefs.edit().putString("pwd", password_et.getText().toString()).commit();
                            myprefs.edit().putString("manual", "no").commit();
                            myprefs.edit().putString("user1", username_et.getText().toString()).commit();
                            myprefs.edit().putString("emp_id", employee_info).commit();
                            startActivity(i);
                            finish();
                        }
                        else{
                           // alerts();
                            alerts_dialog();
                        }
                    } else {
                        if (login_result.equals("Fail")) {
                            Toast.makeText(MainActivity.this, "Wrong user Id or password", Toast.LENGTH_LONG).show();
                        } else {
                            if (login_result.equals("error")) {
                                Toast.makeText(MainActivity.this, "error while retriving data", Toast.LENGTH_LONG).show();
                            }
                        }
                    }


                } else {
                    if (continue_login.equals("Force Update")) {
                        //ForcefullUpdate("A newer version of this app is  available. Please update it now.");
                    } else {
                        if (continue_login.equals("Normal Update")) {
                           // NormalUpdate("A newer version of this app is  available. You can update it now.", context);
                        }
                    }
                }

            }


        }
    }//end of normal login AsynTask


    //Login from normal login web service
    public void Login_Verify(String username1, String password1) {
        Vector<SoapObject> result1 = null;
                                   //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
        // String METHOD_NAME = "intCount";//"NewAppReleseDetails";
        // String Namespace="http://www.example.com", SOAPACTION="http://www.example.com/intCount";
        // String URL = "http://192.168.1.196:8080/deterp_ws/server4.php?wsdl";//"Login.asmx?WSDL";
                                    //  String METHOD_NAME = "ValidateLogin";//"NewAppReleseDetails";
                                     // String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/ValidateLogin";

        //for dfhrms.cloudapp.net webservice

       /* String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
        String METHOD_NAME = "ValidateLogin";//"NewAppReleseDetails";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/ValidateLogin";
*/

        //for dfhrms.cloudapp.net webservice



        //for dethrms.cloudapp.net webservice
        //String URL = "http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";
        String METHOD_NAME = "ValidateLogin";//"NewAppReleseDetails";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/ValidateLogin";
        //for dethrms.cloudapp.net webservice

        try {
            // String  versioncode = this.getPackageManager()
            //		    .getPackageInfo(this.getPackageName(), 0).versionName;
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("userName", username1);
            request.addProperty("Password", password1);

            //	request.addProperty("to", 9);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAPACTION, envelope);
                //	Log.i(TAG, "GetAllLoginDetails is running");
                //		result1 = (Vector<SoapObject>) envelope.getResponse();
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i("value at response", response.toString());
                //	 Object valueAt0 = response.getProperty(0);
                //	 Object user_id = response.getProperty("user_id");
                SoapPrimitive messege = (SoapPrimitive) response.getProperty("Message");
                login_result = messege.toString();
                System.out.println("login_result response:"+login_result);

                if (login_result.equals("sucess")) {
                    SoapPrimitive employee_id = (SoapPrimitive) response.getProperty("Employee_id");
                    employee_info = employee_id.toString();


                }
                //Log.i("string value at response[0]",valueAt0.toString());
                //	 SoapPrimitive messege = (SoapPrimitive)response.getProperty("Status");
                // version = (SoapPrimitive)response.getProperty("AppVersion");
                // release_not = (SoapPrimitive)response.getProperty("ReleseNote");


                //Log.i("string value at messeg",messeg.toString());


            } catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                login_result = "slow internet";

            }
        } catch (Throwable t) {
            Log.e("UnRegister Receiver ", "> " + t.getMessage());

        }

    }







 //--------------------Checkversion--------------------------------------------------------------------------------
    public void ChekVersion() {
        String URL111 = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
        String METHOD_NAME = "NewAppReleseDetails";
        String MAIN_NAMESPACE1 = "http://tempuri.org/";
        String SOAP_ACTION1 = "http://tempuri.org/NewAppReleseDetails";
        try {
            versioncode = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0).versionName;
            SoapObject request = new SoapObject(MAIN_NAMESPACE1, METHOD_NAME);
            request.addProperty("UserAppVersion", versioncode);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL111);

            try {

                androidHttpTransport.call(SOAP_ACTION1, envelope);
                //	Log.i(TAG, "GetAllLoginDetails is running");
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i("at response", response.toString());
                //	 valueAt0 = (SoapPrimitive)response.getProperty(0);
                messeg = (SoapPrimitive) response.getProperty("Response");
                version = (SoapPrimitive) response.getProperty("AppVersion");
                release_not1 = (SoapPrimitive) response.getProperty("ReleseNote");

                release_not = release_not1.toString();


                Log.i("string value at messeg", messeg.toString());


                continue_login = messeg.toString();


            } catch (Exception t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
            }
        } catch (Exception t) {
            Log.e("UnRegister Receiver ", "> " + t.getMessage());

        }
    }//check version




    //Notificationdialogue
    protected void Notificationdialogue(Context context, String title, String msg1) {
        // TODO Auto-generated method stub
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setCancelable(true);
        alertDialog.setTitle(title);

        // Setting Dialog Message
        //	alertDialog.setMessage(msg1);

	    /*	 alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    	       public void onClick(DialogInterface dialog, int which) {
	    	          // TODO Add your code for the button here.
	    	        //   Toast.makeText(getApplicationContext(), "well come", 1).show();
	    	    	   dialog.dismiss();
	    	    	   AsyncCallWS2 task2 = new AsyncCallWS2();
						task2.execute();
	    	       }
	    	    });*/
        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                // TODO Auto-generated method stub
                // Toast.makeText(getApplicationContext(), "yoy have pressed cancel", 1).show();
                dialog.dismiss();
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                exit(1);

            }
        });
        alertDialog.show();
    }
    //-----------------------------------------------------------------------------------------------------------------


// Marshmellow fix


    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    // marshmellow fix


    public void setGCM() {


        try {
            versioncode = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        // Fetch Device info

        final TelephonyManager tm = (TelephonyManager) getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        //   final String tmDevice, tmSerial, androidId;
        String NetworkType;
        //TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
        simOperatorName = tm.getSimOperatorName();
        Log.v("Operator", "" + simOperatorName);
        NetworkType = "GPRS";

        int simSpeed = tm.getNetworkType();
        if (simSpeed == 1)
            NetworkType = "Gprs";
        else if (simSpeed == 4)
            NetworkType = "Edge";
        else if (simSpeed == 8)
            NetworkType = "HSDPA";
        else if (simSpeed == 13)
            NetworkType = "LTE";
        else if (simSpeed == 3)
            NetworkType = "UMTS";
        else
            NetworkType = "Unknown";


        Log.v("SIM_INTERNET_SPEED", "" + NetworkType);
        tmDevice = "" + tm.getDeviceId();
        Log.v("DeviceIMEI", "" + tmDevice);
        mobileNumber = "" + tm.getLine1Number();
        Log.v("getLine1Number value", "" + mobileNumber);

        String mobileNumber1 = "" + tm.getPhoneType();
        Log.v("getPhoneType value", "" + mobileNumber1);
        tmSerial = "" + tm.getSimSerialNumber();
        //  Log.v("GSM devices Serial Number[simcard] ", "" + tmSerial);
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        Log.v("androidId CDMA devices", "" + androidId);
        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        deviceId = deviceUuid.toString();
        //  Log.v("deviceIdUUID universally unique identifier", "" + deviceId);
        deviceModelName = android.os.Build.MODEL;
        Log.v("Model Name", "" + deviceModelName);
        deviceUSER = android.os.Build.USER;
        Log.v("Name USER", "" + deviceUSER);
        devicePRODUCT = android.os.Build.PRODUCT;
        Log.v("PRODUCT", "" + devicePRODUCT);
        deviceHARDWARE = android.os.Build.HARDWARE;
        Log.v("HARDWARE", "" + deviceHARDWARE);
        deviceBRAND = android.os.Build.BRAND;
        Log.v("BRAND", "" + deviceBRAND);
        myVersion = android.os.Build.VERSION.RELEASE;
        Log.v("VERSION.RELEASE", "" + myVersion);
        sdkVersion = android.os.Build.VERSION.SDK_INT;
        Log.v("VERSION.SDK_INT", "" + sdkVersion);
        sdkver = Integer.toString(sdkVersion);
        // Get display details

        Measuredwidth = 0;
        Measuredheight = 0;
        Point size = new Point();
        WindowManager w = getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //   w.getDefaultDisplay().getSize(size);
            Measuredwidth = w.getDefaultDisplay().getWidth();//size.x;
            Measuredheight = w.getDefaultDisplay().getHeight();//size.y;
        } else {
            Display d = w.getDefaultDisplay();
            Measuredwidth = d.getWidth();
            Measuredheight = d.getHeight();
        }

        Log.v("SCREEN_Width", "" + Measuredwidth);
        Log.v("SCREEN_Height", "" + Measuredheight);

        // Fetch Device info


        // GCM Registration

        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        //GCMRegistrar.checkManifest(this);

        //lblMessage = (TextView) findViewById(R.id.lblMessage);

        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                CommonUtilities.DISPLAY_MESSAGE_ACTION));

        // Get GCM registration id
        regId = GCMRegistrar.getRegistrationId(this);



        Log.v("regId", "" + regId);

        // Check if regid already presents
        if (regId.equals("")) {
            // Registration is not present, register now with GCM
            mRegisterTask = new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    // Register on our server
                    // On server creates a new user
                    //  GCMRegistrar.register(this, SENDER_ID);
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    mRegisterTask = null;
                }

            };
            mRegisterTask.execute(null, null, null);
            GCMRegistrar.register(this, CommonUtilities.SENDER_ID);

            regId = GCMRegistrar.getRegistrationId(this);
            if (regId.equals("")) {
                regId = GCMRegistrar.getRegistrationId(this);
                regId.equals("");
            }
            Log.v("regId", "" + regId);
        } else {
            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
                Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                        // On server creates a new user
                        //  GCMRegistrar.register(this, SENDER_ID);

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }
            // GCM Registration
            //  AsyncCallWS5 task5 = new AsyncCallWS5();
            //	task5.execute();


        }
        if (!regId.equals("")) {
           // String WEBSERVICE_NAME = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
            String SOAP_ACTION1 = "http://tempuri.org/SaveDeviceDetails";
            String METHOD_NAME1 = "SaveDeviceDetails";
            String MAIN_NAMESPACE = "http://tempuri.org/";
          //  String URI = "http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";

            /*String SOAP_ACTION1 = "http://tempuri.org/SaveDeviceDetails";
            String METHOD_NAME1 = "SaveDeviceDetails";
            String MAIN_NAMESPACE = "http://tempuri.org/";
            String URI = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
*/



            SoapObject request = new SoapObject(MAIN_NAMESPACE, METHOD_NAME1);
            //	request.addProperty("LeadId", Password1);
            //request.addProperty("emailId", username.getText().toString());
            request.addProperty("emailId", employee_info);
           // request.addProperty("emailId", "amrit.tech@detedu.org");
            request.addProperty("DeviceId", regId);
            request.addProperty("OSVersion", myVersion);
            request.addProperty("Manufacturer", deviceBRAND);
            request.addProperty("ModelNo", deviceModelName);
            request.addProperty("SDKVersion", sdkver);
            request.addProperty("DeviceSrlNo", tmDevice);
            request.addProperty("ServiceProvider", simOperatorName);
            request.addProperty("SIMSrlNo", tmSerial);
            request.addProperty("DeviceWidth", Measuredwidth);
            request.addProperty("DeviceHeight", Measuredheight);
            request.addProperty("AppVersion", versioncode);
            //request.addProperty("AppVersion","4.0");


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            // Set output SOAP object
            envelope.setOutputSoapObject(request);
            // Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {
                androidHttpTransport.call(SOAP_ACTION1, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                System.out.println("Device Res"+response);

                 Log.i("sending device detail", response.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }//end of GCM()

    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(CommonUtilities.EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping

            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */

            // Showing received message
            //  lblMessage.append(newMessage + "\n");
            Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

        }
    };


    @Override
    protected void onDestroy() {
       /* if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiv", "> " + e.getMessage());
        }*/
        super.onDestroy();
    }

    protected void showCustomDialog(final String msg1) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.leaverejectcustomdailog);
//PMNote:" + lId.Name + " has sent leave cancel request.
        final TextView editText = (TextView)dialog.findViewById(R.id.editText1);

        final String[] parts = msg1.split("approval.");
        editText.setText(parts[0]+" approval.");
        approve_key= parts[1];
        Log.d("approveleavekey:",approve_key.toString());
        editText.setGravity(Gravity.CENTER);
       // Button Later = (Button)dialog.findViewById(R.id.button1);
        Button Approve = (Button)dialog.findViewById(R.id.approve);
        Button Reject = (Button)dialog.findViewById(R.id.reject);

        /*Later.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialog.dismiss();
            }
        });*/

        Approve.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialog.dismiss();


                ApproveAsyncCallWS2 task=new ApproveAsyncCallWS2(MainActivity.this);
                task.execute();

            }
        });
        Reject.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialog.dismiss();


                showCustomDialogReject(msg1);

               /* RejectAsyncCallWS2 task=new RejectAsyncCallWS2(MainActivity.this);
                task.execute();*/

            }
        });
        dialog.show();
    }

    /*********************ApproveAsyncCallWS2*******************************************************************/
    private class ApproveAsyncCallWS2 extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        Context context;

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate---tab2");
        }

        public ApproveAsyncCallWS2(MainActivity activity) {
            context = activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");

            if(!approve_key.equals("no")) {
                approveLeave();
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();

            }
            if(approve_key.equals("no")) {
                Toast.makeText(MainActivity.this, "sending String not available ",Toast.LENGTH_LONG).show();
            }
            else
            {
                if(Approve_result.equals("no"))
                {
                    Toast.makeText(MainActivity.this, "error in webservice connection due to slow network",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(Approve_result.equals("success"))
                    {
                        Toast.makeText(MainActivity.this, "Approved",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Not Approved",Toast.LENGTH_LONG).show();
                    }
                }
            }
            Log.i(TAG, "onPostExecute");
        }

    }
    /***************************************************************************************/

    public void approveLeave()
    {
        Vector<SoapObject> result1 = null;
       // String URL = "http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";

       // http://dethrms.cloudapp.net/PMSservice.asmx?op=ApproveLeaveCancelRequest

        // String METHOD_NAME = "intCount";//"NewAppReleseDetails";
        // String Namespace="http://www.example.com", SOAPACTION="http://www.example.com/intCount";
        // String URL = "http://192.168.1.196:8080/deterp_ws/server4.php?wsdl";//"Login.asmx?WSDL";
        String METHOD_NAME = "ApproveLeave";//"NewAppReleseDetails";
        String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/ApproveLeave";
        Log.d("approve_key",approve_key);
        try{
            // String  versioncode = this.getPackageManager()
            //		    .getPackageInfo(this.getPackageName(), 0).versionName;
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("id", approve_key);


            //	request.addProperty("to", 9);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try
            {

                androidHttpTransport.call(SOAPACTION, envelope);
                //	Log.i(TAG, "GetAllLoginDetails is running");
                //		result1 = (Vector<SoapObject>) envelope.getResponse();
                SoapPrimitive  response = (SoapPrimitive ) envelope.getResponse();
                //	Log.i("string value at response",response.toString());
                //	 Object valueAt0 = response.getProperty(0);
                //	 Object user_id = response.getProperty("user_id");
                //	SoapPrimitive messege = (SoapPrimitive)response.getProperty("ApproveLeaveResult");
                Approve_result= response.toString();
                Log.d("Approve_result",Approve_result);


            }
            catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                login_result="slow internet";

            }
        }catch (Throwable t) {
            //Log.e("UnRegister Receiver Error", "> " + t.getMessage());

        }

    }//end of approveleave
  // *********************ApproveAsyncCallWS2*******************************************************************/

    // *********************RejectAsyncCallWS2******************************************************************/
    private class RejectAsyncCallWS2 extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        Context context;

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate---tab2");
        }

        public RejectAsyncCallWS2(MainActivity activity) {
            context = activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");

            if(!approve_key.equals("no")) {
                RejectLeave();
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {



                dialog.dismiss();

            }
            if(approve_key.equals("no")) {
                Toast.makeText(MainActivity.this, "sending String not available ",Toast.LENGTH_LONG).show();
            }
            else
            {
                if(Reject_result.equals("no"))
                {
                    Toast.makeText(MainActivity.this, "error in webservice connection due to slow network",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(Reject_result.equals("success"))
                    {
                        Toast.makeText(MainActivity.this, "Rejected",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Not Rejected",Toast.LENGTH_LONG).show();
                    }
                }
            }
            Log.i(TAG, "onPostExecute");
        }

    }

    public void RejectLeave()
    {
        Vector<SoapObject> result1 = null;
       // String URL = "http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";

        //http://dethrms.cloudapp.net/PMSservice.asmx
        // String METHOD_NAME = "intCount";//"NewAppReleseDetails";
        // String Namespace="http://www.example.com", SOAPACTION="http://www.example.com/intCount";
        // String URL = "http://192.168.1.196:8080/deterp_ws/server4.php?wsdl";//"Login.asmx?WSDL";
        String METHOD_NAME = "RejectLeaveCancelRequest";//"NewAppReleseDetails";
        String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/RejectLeaveCancelRequest";
        try{
            // String  versioncode = this.getPackageManager()
            //		    .getPackageInfo(this.getPackageName(), 0).versionName;

            /*<id>string</id>
      <message>string</message>*/

            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("id", approve_key);
            request.addProperty("message",rejectreason_et.getText().toString());

            //	request.addProperty("to", 9);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try
            {

                androidHttpTransport.call(SOAPACTION, envelope);
                //	Log.i(TAG, "GetAllLoginDetails is running");
                //		result1 = (Vector<SoapObject>) envelope.getResponse();
                SoapPrimitive  response = (SoapPrimitive ) envelope.getResponse();
                //	Log.i("string value at response",response.toString());
                //	 Object valueAt0 = response.getProperty(0);
                //	 Object user_id = response.getProperty("user_id");
                //SoapPrimitive messege = (SoapPrimitive)response.getProperty("RejectLeaveResult");
                Reject_result= response.toString();


            }
            catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());


            }
        }catch (Throwable t) {
            //Log.e("UnRegister Receiver Error", "> " + t.getMessage());

        }

    }

// *********************RejectAsyncCallWS2******************************************************************/

    protected void Normalnotification(String msg1) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.normal_notification);

        final TextView editText = (TextView)dialog.findViewById(R.id.editText1);
        editText.setText(msg1);
        editText.setGravity(Gravity.CENTER);
        Button button = (Button)dialog.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialog.dismiss();

            }
        });

        dialog.show();
    }


    protected void showOndutyCustomDialog(String msg1) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ondutycustomdialog);

        final TextView editText = (TextView)dialog.findViewById(R.id.ondutynotification_ET);
        final String[] parts = msg1.split("approval.");
        editText.setText(parts[0]+" approval.");
        approve_key= parts[1];
        editText.setGravity(Gravity.CENTER);
       // Button lateronduty_bt = (Button)dialog.findViewById(R.id.LaterOnduty_BT);
        Button approveonduty_bt = (Button)dialog.findViewById(R.id.ApproveOnduty_BT);
        Button rejectonduty_bt = (Button)dialog.findViewById(R.id.RejectOnduty_BT);

       /* lateronduty_bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialog.dismiss();
            }
        });*/

        approveonduty_bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialog.dismiss();
                ApproveOndutyAsyncCallWS2 task=new ApproveOndutyAsyncCallWS2(MainActivity.this);
                task.execute();

            }
        });
        rejectonduty_bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialog.dismiss();

                RejectOndutyAsyncCallWS2 task=new RejectOndutyAsyncCallWS2(MainActivity.this);
                task.execute();

            }
        });
        dialog.show();
    }

    // *********************ApproveOndutyAsyncCallWS2*******************************************************************/


    private class ApproveOndutyAsyncCallWS2 extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        Context context;

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate---tab2");
        }

        public ApproveOndutyAsyncCallWS2(MainActivity activity) {
            context = activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");

            if(!approve_key.equals("no")) {
                approveOnduty();
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();

            }
            if(approve_key.equals("no")) {
                Toast.makeText(MainActivity.this, "sending String not available ",Toast.LENGTH_LONG).show();
            }
            else
            {
                if(Approve_result.equals("no"))
                {
                    Toast.makeText(MainActivity.this, "error in webservice connection due to slow network",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(Approve_result.equals("success"))
                    {
                        Toast.makeText(MainActivity.this, "Approved",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Not Approved",Toast.LENGTH_LONG).show();
                    }
                }
            }
            Log.i(TAG, "onPostExecute");
        }

    }

    public void approveOnduty()
    {
        Vector<SoapObject> result1 = null;
      //  String URL = "http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";


        // String METHOD_NAME = "intCount";//"NewAppReleseDetails";
        // String Namespace="http://www.example.com", SOAPACTION="http://www.example.com/intCount";
        // String URL = "http://192.168.1.196:8080/deterp_ws/server4.php?wsdl";//"Login.asmx?WSDL";
        String METHOD_NAME = "ApproveOD";//"NewAppReleseDetails";
        String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/ApproveOD";
        try{
            // String  versioncode = this.getPackageManager()
            //		    .getPackageInfo(this.getPackageName(), 0).versionName;
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("id", approve_key);


            //	request.addProperty("to", 9);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try
            {

                androidHttpTransport.call(SOAPACTION, envelope);
                //	Log.i(TAG, "GetAllLoginDetails is running");
                //		result1 = (Vector<SoapObject>) envelope.getResponse();
                SoapPrimitive  response = (SoapPrimitive ) envelope.getResponse();
                //	Log.i("string value at response",response.toString());
                //	 Object valueAt0 = response.getProperty(0);
                //	 Object user_id = response.getProperty("user_id");
                //	SoapPrimitive messege = (SoapPrimitive)response.getProperty("ApproveLeaveResult");
                Approve_result= response.toString();


            }
            catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                login_result="slow internet";

            }
        }catch (Throwable t) {
            //Log.e("UnRegister Receiver Error", "> " + t.getMessage());

        }

    }//end of approveleave


// *********************ApproveOndutyAsyncCallWS2*******************************************************************/

    // *********************RejectOndutyAsyncCallWS2******************************************************************/
    private class RejectOndutyAsyncCallWS2 extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        Context context;

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate---tab2");
        }

        public RejectOndutyAsyncCallWS2(MainActivity activity) {
            context = activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");

            if(!approve_key.equals("no")) {
                RejectOnduty();
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {



                dialog.dismiss();

            }
            if(approve_key.equals("no")) {
                Toast.makeText(MainActivity.this, "sending String not available ",Toast.LENGTH_LONG).show();
            }
            else
            {
                if(Reject_result.equals("no"))
                {
                    Toast.makeText(MainActivity.this, "error in webservice connection due to slow network",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(Reject_result.equals("success"))
                    {
                        Toast.makeText(MainActivity.this, "Rejected",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Not Rejected",Toast.LENGTH_LONG).show();
                    }
                }
            }
            Log.i(TAG, "onPostExecute");
        }

    }

    public void RejectOnduty()
    {
        Vector<SoapObject> result1 = null;
       // String URL = "http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";


        // String METHOD_NAME = "intCount";//"NewAppReleseDetails";
        // String Namespace="http://www.example.com", SOAPACTION="http://www.example.com/intCount";
        // String URL = "http://192.168.1.196:8080/deterp_ws/server4.php?wsdl";//"Login.asmx?WSDL";
        String METHOD_NAME = "RejectOD";//"NewAppReleseDetails";
        String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/RejectOD";
        try{
            // String  versioncode = this.getPackageManager()
            //		    .getPackageInfo(this.getPackageName(), 0).versionName;
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("id", approve_key);


            //	request.addProperty("to", 9);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try
            {

                androidHttpTransport.call(SOAPACTION, envelope);
                //	Log.i(TAG, "GetAllLoginDetails is running");
                //		result1 = (Vector<SoapObject>) envelope.getResponse();
                SoapPrimitive  response = (SoapPrimitive ) envelope.getResponse();
                //	Log.i("string value at response",response.toString());
                //	 Object valueAt0 = response.getProperty(0);
                //	 Object user_id = response.getProperty("user_id");
                //	SoapPrimitive messege = (SoapPrimitive)response.getProperty("ApproveLeaveResult");
                Reject_result= response.toString();


            }
            catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                login_result="slow internet";

            }
        }catch (Throwable t) {
            //Log.e("UnRegister Receiver Error", "> " + t.getMessage());

        }

    }//end of approveleave



// *********************RejectOndutyAsyncCallWS2******************************************************************/

    public void call()
    {
        AsyncCallFCM task = new AsyncCallFCM(MainActivity.this);
        task.execute();
    }





    private class AsyncCallFCM extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        Context context;
        boolean versionval;
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute---tab2");
           /* dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();*/

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate---tab2");
        }

        public AsyncCallFCM(MainActivity activity) {
            context = activity;
            // dialog = new ProgressDialog(activity);
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");






            setGCM1();
            setGCM1();


          //  versionval =	appversioncheck(versioncodeInString);


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            /*if (dialog.isShowing()) {
                dialog.dismiss();

            }*/
            // Log.i(TAG, "onPostExecute");

           /* if(versionval)
            {	}else{alerts();}*/



        }
    }//end of normal login AsynTask










    public void setGCM1() {


        try {
            versioncode = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        // Fetch Device info

       /* final TelephonyManager tm = (TelephonyManager) getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);*/

        tm1 = (TelephonyManager) getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);

        //   final String tmDevice, tmSerial, androidId;
        String NetworkType;
        //TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
        simOperatorName = tm1.getSimOperatorName();
        Log.v("Operator", "" + simOperatorName);
        NetworkType = "GPRS";



        int simSpeed = tm1.getNetworkType();
        if (simSpeed == 1)
            NetworkType = "Gprs";
        else if (simSpeed == 4)
            NetworkType = "Edge";
        else if (simSpeed == 8)
            NetworkType = "HSDPA";
        else if (simSpeed == 13)
            NetworkType = "LTE";
        else if (simSpeed == 3)
            NetworkType = "UMTS";
        else
            NetworkType = "Unknown";

        Log.v("SIM_INTERNET_SPEED", "" + NetworkType);
        tmDevice = "" + tm1.getDeviceId();
        Log.v("DeviceIMEI", "" + tmDevice);
        mobileNumber = "" + tm1.getLine1Number();
        Log.v("getLine1Number value", "" + mobileNumber);

        String mobileNumber1 = "" + tm1.getPhoneType();
        Log.v("getPhoneType value", "" + mobileNumber1);
        tmSerial = "" + tm1.getSimSerialNumber();
        //  Log.v("GSM devices Serial Number[simcard] ", "" + tmSerial);
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        Log.v("androidId CDMA devices", "" + androidId);
        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        deviceId = deviceUuid.toString();
        //  Log.v("deviceIdUUID universally unique identifier", "" + deviceId);


        deviceModelName = android.os.Build.MODEL;
        Log.v("Model Name", "" + deviceModelName);
        deviceUSER = android.os.Build.USER;
        Log.v("Name USER", "" + deviceUSER);
        devicePRODUCT = android.os.Build.PRODUCT;
        Log.v("PRODUCT", "" + devicePRODUCT);
        deviceHARDWARE = android.os.Build.HARDWARE;
        Log.v("HARDWARE", "" + deviceHARDWARE);
        deviceBRAND = android.os.Build.BRAND;
        Log.v("BRAND", "" + deviceBRAND);
        myVersion = android.os.Build.VERSION.RELEASE;
        Log.v("VERSION.RELEASE", "" + myVersion);
        sdkVersion = android.os.Build.VERSION.SDK_INT;
        Log.v("VERSION.SDK_INT", "" + sdkVersion);
        sdkver = Integer.toString(sdkVersion);
        // Get display details

        Measuredwidth = 0;
        Measuredheight = 0;
        Point size = new Point();
        WindowManager w = getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //   w.getDefaultDisplay().getSize(size);
            Measuredwidth = w.getDefaultDisplay().getWidth();//size.x;
            Measuredheight = w.getDefaultDisplay().getHeight();//size.y;
        } else {
            Display d = w.getDefaultDisplay();
            Measuredwidth = d.getWidth();
            Measuredheight = d.getHeight();
        }

        Log.v("SCREEN_Width", "" + Measuredwidth);
        Log.v("SCREEN_Height", "" + Measuredheight);

        // Fetch Device info


        // GCM Registration

        // Make sure the device has the proper dependencies.
       /* GCMRegistrar.checkDevice(this);*/

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        //GCMRegistrar.checkManifest(this);

        //lblMessage = (TextView) findViewById(R.id.lblMessage);

        /*registerReceiver(mHandleMessageReceiver, new IntentFilter(
                DISPLAY_MESSAGE_ACTION));*/

        // Get GCM registration id
        regId = FirebaseInstanceId.getInstance().getToken();



        Log.v("regId", "" + regId);
/*
        // Check if regid already presents
        if (regId.equals("")) {
            // Registration is not present, register now with GCM
            mRegisterTask = new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    // Register on our server
                    // On server creates a new user
                    //  GCMRegistrar.register(this, SENDER_ID);
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    mRegisterTask = null;
                }

            };
            mRegisterTask.execute(null, null, null);
            GCMRegistrar.register(this, SENDER_ID);

            regId = GCMRegistrar.getRegistrationId(this);
            if (regId.equals("")) {
                regId = GCMRegistrar.getRegistrationId(this);
                regId.equals("");
            }
            Log.v("regId", "" + regId);
        } else {
            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
                Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                        // On server creates a new user
                        //  GCMRegistrar.register(this, SENDER_ID);

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }
            // GCM Registration
            //  AsyncCallWS5 task5 = new AsyncCallWS5();
            //	task5.execute();


        }*/


        //if (!regId.equals("")){
            if (2>1){
            // String WEBSERVICE_NAME = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
            String SOAP_ACTION1 = "http://tempuri.org/SaveDeviceDetails";
            String METHOD_NAME1 = "SaveDeviceDetails";
            String MAIN_NAMESPACE = "http://tempuri.org/";
          //  String URL = "http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";

            /*String SOAP_ACTION1 = "http://tempuri.org/SaveDeviceDetails";
            String METHOD_NAME1 = "SaveDeviceDetails";
            String MAIN_NAMESPACE = "http://tempuri.org/";
            String URI = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
*/



            SoapObject request = new SoapObject(MAIN_NAMESPACE, METHOD_NAME1);
            //	request.addProperty("LeadId", Password1);
            request.addProperty("emailId", username_et.getText().toString());
           // request.addProperty("emailId", "employee_info2");
            // request.addProperty("emailId", "amrit.tech@detedu.org");
            request.addProperty("DeviceId", regId);
            request.addProperty("OSVersion", myVersion);
            request.addProperty("Manufacturer", deviceBRAND);
            request.addProperty("ModelNo", deviceModelName);
            request.addProperty("SDKVersion", sdkver);
            request.addProperty("DeviceSrlNo", tmDevice);
            request.addProperty("ServiceProvider", simOperatorName);
            request.addProperty("SIMSrlNo", tmSerial);
            request.addProperty("DeviceWidth", Measuredwidth);
            request.addProperty("DeviceHeight", Measuredheight);
            request.addProperty("AppVersion", versioncode);
            //request.addProperty("AppVersion","4.0");


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            // Set output SOAP object
            envelope.setOutputSoapObject(request);
            // Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {
                androidHttpTransport.call(SOAP_ACTION1, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                System.out.println("Device Res"+response);

                Log.i("sending device detail", response.toString());

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("err",e.toString());
            }
        }






    }//end of GCM()



    protected void showCustomDialogReject(String msg1) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rejectreasoncustomdailog);

        rejectreason_et = (TextView)dialog.findViewById(R.id.rejectreason_ET);

        //final String[] parts = msg1.split("approval.");
        //editText.setText(parts[0]+" approval.");

       // approve_key= parts[1];

       // editText.setGravity(Gravity.CENTER);


        Button submit_bt = (Button)dialog.findViewById(R.id.submit_BT);



        submit_bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(RejectctReasonValidation()) {

                    dialog.dismiss();
                    RejectAsyncCallWS2 task=new RejectAsyncCallWS2(MainActivity.this);
                    task.execute();

                }//End of if RejectctReasonValidation()

            }
        });

        dialog.show();
    }



    public boolean RejectctReasonValidation()
    {
        boolean validationResultRejectreason_et1=true,validationResultRejectreason_et2=true,validationResultRejectreason_et3=true;

        if(rejectreason_et.getText().toString().length()==0)
        {
            rejectreason_et.setError("Empty not allowed!");
            rejectreason_et.requestFocus();validationResultRejectreason_et1=false;
        }
        if( (rejectreason_et.getText().toString().length()<5 )||(rejectreason_et.getText().toString().trim().length()<5 ))
        {
            rejectreason_et.setError("Minimum 5 character required");
            rejectreason_et.requestFocus();validationResultRejectreason_et2=false;
        }
        if(rejectreason_et.getText().toString().length()>30)
        {
            rejectreason_et.setError("Maximum 100 character only");
            rejectreason_et.requestFocus();validationResultRejectreason_et3=false;
        }

        if(validationResultRejectreason_et1&&validationResultRejectreason_et2&&validationResultRejectreason_et3)
        {   return true;    }
        else{ return false;}

    }//End of RejectctReasonValidation method








//leave cancel reject


    protected void leavecancelCustomDialog(String msg1) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.leavecancelcustomdialog);

        final TextView editText = (TextView)dialog.findViewById(R.id.leavecancelnotification_ET);
        final String[] parts = msg1.split("request.");
        editText.setText(parts[0]+" request.");
        approve_key= parts[1];
        editText.setGravity(Gravity.CENTER);




        // Button lateronduty_bt = (Button)dialog.findViewById(R.id.LaterOnduty_BT);
        Button approveleavecancel_bt = (Button)dialog.findViewById(R.id.Approveleavecancel_BT);
        Button rejectleavecancel_bt = (Button)dialog.findViewById(R.id.Rejectleavecancel_BT);

       /* lateronduty_bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialog.dismiss();
            }
        });*/

        approveleavecancel_bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialog.dismiss();
                leavecancelAsyncCallWS2 task=new leavecancelAsyncCallWS2(MainActivity.this);
                task.execute();

            }
        });
        rejectleavecancel_bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialog.dismiss();

                leavecancelrejectAsyncCallWS2 task=new leavecancelrejectAsyncCallWS2(MainActivity.this);
                task.execute();

            }
        });
        dialog.show();
    }


 // leave cancel reject

    private class leavecancelAsyncCallWS2 extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        Context context;

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate---tab2");
        }

        public leavecancelAsyncCallWS2(MainActivity activity) {
            context = activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");

            if(!approve_key.equals("no")) {
                leavecancel_approve();
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {



                dialog.dismiss();

            }
            if(approve_key.equals("no")) {
                Toast.makeText(MainActivity.this, "sending String not available ",Toast.LENGTH_LONG).show();
            }
            else
            {
                if(Reject_result.equals("no"))
                {
                    Toast.makeText(MainActivity.this, "error in webservice connection due to slow network",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(Reject_result.equals("success"))
                    {
                        Toast.makeText(MainActivity.this, "Approved",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "approveError",Toast.LENGTH_LONG).show();
                    }
                }
            }
            Log.i(TAG, "onPostExecute");
        }

    }

    public void leavecancel_approve()
    {
        Vector<SoapObject> result1 = null;
       // String URL = "http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";


        // String METHOD_NAME = "intCount";//"NewAppReleseDetails";
        // String Namespace="http://www.example.com", SOAPACTION="http://www.example.com/intCount";
        // String URL = "http://192.168.1.196:8080/deterp_ws/server4.php?wsdl";//"Login.asmx?WSDL";
        String METHOD_NAME = "ApproveLeaveCancelRequest";//"NewAppReleseDetails";
        String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/ApproveLeaveCancelRequest";
        try{
            // String  versioncode = this.getPackageManager()
            //		    .getPackageInfo(this.getPackageName(), 0).versionName;
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("id", approve_key);


            //	request.addProperty("to", 9);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try
            {

                androidHttpTransport.call(SOAPACTION, envelope);
                //	Log.i(TAG, "GetAllLoginDetails is running");
                //		result1 = (Vector<SoapObject>) envelope.getResponse();
                SoapPrimitive  response = (SoapPrimitive ) envelope.getResponse();
                //	Log.i("string value at response",response.toString());
                //	 Object valueAt0 = response.getProperty(0);
                //	 Object user_id = response.getProperty("user_id");
                //	SoapPrimitive messege = (SoapPrimitive)response.getProperty("ApproveLeaveResult");
                Reject_result= response.toString();


            }
            catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                login_result="slow internet";

            }
        }catch (Throwable t) {
            //Log.e("UnRegister Receiver Error", "> " + t.getMessage());

        }

    }//end of leavecancel



// *********************RejectOndutyAsyncCallWS2******************************************************************/









    private class leavecancelrejectAsyncCallWS2 extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        Context context;

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate---tab2");
        }

        public leavecancelrejectAsyncCallWS2(MainActivity activity) {
            context = activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");

            if(!approve_key.equals("no")) {
                leavecancel_reject();
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {



                dialog.dismiss();

            }
            if(approve_key.equals("no")) {
                Toast.makeText(MainActivity.this, "sending String not available ",Toast.LENGTH_LONG).show();
            }
            else
            {
                if(Reject_result.equals("no"))
                {
                    Toast.makeText(MainActivity.this, "error in webservice connection due to slow network",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(Reject_result.equals("success"))
                    {
                        Toast.makeText(MainActivity.this, "Rejected",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Not Rejected",Toast.LENGTH_LONG).show();
                    }
                }
            }
            Log.i(TAG, "onPostExecute");
        }

    }

    public void leavecancel_reject()
    {
        Vector<SoapObject> result1 = null;
       // String URL = "http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";


        // String METHOD_NAME = "intCount";//"NewAppReleseDetails";
        // String Namespace="http://www.example.com", SOAPACTION="http://www.example.com/intCount";
        // String URL = "http://192.168.1.196:8080/deterp_ws/server4.php?wsdl";//"Login.asmx?WSDL";
        String METHOD_NAME = "RejectLeaveCancelRequest";//"NewAppReleseDetails"; RejectLeaveCancelRequest
        String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/RejectLeaveCancelRequest";
        try{
            // String  versioncode = this.getPackageManager()
            //		    .getPackageInfo(this.getPackageName(), 0).versionName;
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("id", approve_key);


            //	request.addProperty("to", 9);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try
            {

                androidHttpTransport.call(SOAPACTION, envelope);
                //	Log.i(TAG, "GetAllLoginDetails is running");
                //		result1 = (Vector<SoapObject>) envelope.getResponse();
                SoapPrimitive  response = (SoapPrimitive ) envelope.getResponse();
                //	Log.i("string value at response",response.toString());
                //	 Object valueAt0 = response.getProperty(0);
                //	 Object user_id = response.getProperty("user_id");
                //	SoapPrimitive messege = (SoapPrimitive)response.getProperty("ApproveLeaveResult");
                Reject_result= response.toString();


            }
            catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                login_result="slow internet";

            }
        }catch (Throwable t) {
            //Log.e("UnRegister Receiver Error", "> " + t.getMessage());

        }

    }//end of leavecancelReject



// *********************LeavecancelRejectAsyncCallWS2******************************************************************/




//---------------------------------

    private class AsyncCallForceUpdate extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        Context context;
       boolean versionval;
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate---tab2");
        }

        public AsyncCallForceUpdate(MainActivity activity) {
            context = activity;
             dialog = new ProgressDialog(activity);
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");

             versionval =	appversioncheck(versioncodeInString);


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();

            }
            // Log.i(TAG, "onPostExecute");

            if(versionval)
            {

            }
            else{
                //Toast.makeText(getApplicationContext(),"Update from playstore",Toast.LENGTH_LONG).show();
               // alerts();
                alerts_dialog();
            }



        }
    }//end of normal login AsynTask
//--------------------------------

    //ForceUpdate
    public boolean appversioncheck(String versionNos)
    {
        String mailresponse="flag";
        int verInt=0;
        //http://dethrms.cloudapp.net/PMSservice.asmx?op=NewAppReleseDetails
      //  String URL="http://dethrms.cloudapp.net/PMSservice.asmx?wsdl";  //xml code
        String METHOD_NAMEMail = "NewAppReleseDetails";
        String NamespaceMail="http://tempuri.org/", SOAPACTIONMail="http://tempuri.org/NewAppReleseDetails";//namespace+methodname

		    /*try{*/

        SoapObject request = new SoapObject(NamespaceMail, METHOD_NAMEMail);

        request.addProperty("UserAppVersion", versionNos);//S

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        //Set output SOAP object
        envelope.setOutputSoapObject(request);
        //Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try
        {
            androidHttpTransport.call(SOAPACTIONMail, envelope);

            SoapObject  response = (SoapObject ) envelope.getResponse();
            Log.i("force response",response.toString());


            String object2string = response.getProperty(0).toString();

            mailresponse=object2string.toString();
            verInt=Integer.valueOf(object2string);

            //Toast.makeText(getApplicationContext(), "hi"+object2string, Toast.LENGTH_LONG).show();
            Log.i("force verInt",object2string.toString());
        }
        catch (Throwable t) {

            Log.e("request fail", "> " + t.getMessage());
        }

					/*}catch (Throwable t) {
		          Log.e("UnRegister Receiver Error", "> " + t.getMessage());

		        }*/


       // if(versionCodes>verInt) //9>10
       if(versionCodes>=verInt) //9>=10
        {

            return true;}
        else
        {return false;}

    }

    //VersionCheck>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public void alerts()
    {
        //AlertDialog.Builder dialogs = new AlertDialog.Builder(MainActivity.this);

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.updatedialog);
        dialog.setCancelable(false);

        Button dialogupdatebutton = (Button) dialog.findViewById(R.id.updatedialog_BT);

        dialogupdatebutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent	intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=hrms.com.hrm"));
                startActivity(intent);
            }
        });
        dialog.show();

	    	/*Builder builder=new Builder(this);
	    	builder.setCancelable(false);
	    	builder.setMessage("Kindly update the New version from Play Store");
	    	builder.show();*/
    }





    public  void alerts_dialog()
    {

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("FSSI HRMS");
       // dialog.setMessage("Kindly update from playstore");
        dialog.setMessage("Kindly contact HR-for new version");
        dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
               /* Intent	intent = new Intent(android.content.Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.leadcampusapp"));
                startActivity(intent);*/
               finish();
            }
        });


        final AlertDialog alert = dialog.create();
        alert.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#004D40"));
            }
        });
        alert.show();

    }
























}// End of class
