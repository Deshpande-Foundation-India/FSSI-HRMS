package com.fssihrms;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import static com.fssihrms.Constant_webservice.URL;

//import hrms.com.hrm.R;

/**
 * Created by User on 6/26/2017.
 */

public class fragment_applyonduty extends Fragment {

    private ScrollView lLayoutApplyonduty;


    Context context;

    String username, pwd, emp_id, internet_issue = "empty";
    String slow_intenet = "no";
    String str_error="yes";

    TextView clickfromdateonduty_tv, clicktodateonduty_tv;
    EditText locationonduty_et, reasononduty_et;
    Spinner typeofonduty_sp;
    Button applyonduty_bt;

    long Employeeidlong;
    SoapPrimitive response;

    String str_response;
    String yyyyMMdd_todate = "";
    String yyyyMMdd_fromdate = "";
    ConnectionDetector internetDectector;
    Boolean isInternetPresent = false;

    String locationonduty_et_string, typeofonduty_sp_string, reasononduty_et_string;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        lLayoutApplyonduty = (ScrollView) inflater.inflate(R.layout.fragment_applyonduty, container, false);

        context = lLayoutApplyonduty.getContext();
        SharedPreferences myprefs = this.getActivity().getSharedPreferences("user", Context.MODE_WORLD_READABLE);
        //Toast.makeText(Instant.this,myprefs.getAll().toString(),Toast.LENGTH_LONG).show();
        username = myprefs.getString("user1", "nothing");
        pwd = myprefs.getString("pwd", "nothing");
        emp_id = myprefs.getString("emp_id", "nothing");

        Employeeidlong = Long.parseLong(emp_id); // for web service

        clickfromdateonduty_tv = (TextView) lLayoutApplyonduty.findViewById(R.id.clickfromdateonduty_TV);
        clicktodateonduty_tv = (TextView) lLayoutApplyonduty.findViewById(R.id.clicktodateonduty_TV);
        locationonduty_et = (EditText) lLayoutApplyonduty.findViewById(R.id.locationonduty_ET);
        reasononduty_et = (EditText) lLayoutApplyonduty.findViewById(R.id.reasononduty_ET);
        typeofonduty_sp = (Spinner) lLayoutApplyonduty.findViewById(R.id.typeofonduty_SP);
        applyonduty_bt = (Button) lLayoutApplyonduty.findViewById(R.id.applyonduty_BT);

        String[] typeofonduty_spinnerdropdown = {
                "Marketing", "Orientation", "Field Visit", "Awareness", "Presentation"};


        ArrayAdapter<String> typeofonduty_adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.leavetypespinneritems, typeofonduty_spinnerdropdown);
        typeofonduty_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeofonduty_sp.setAdapter(typeofonduty_adapter);

        clicktodateonduty_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // settodate();
                DialogFragment dFragment = new DatePickerFragment();
                // Show the date picker dialog fragment
                dFragment.show(getFragmentManager(), "Date Picker");


            }
        });

        clickfromdateonduty_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment fromdateFragment = new DatePickerFragmentFromDate();
                fromdateFragment.show(getFragmentManager(), "Date Picker");
            }
        });


        applyonduty_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                locationonduty_et_string = locationonduty_et.getText().toString();
                typeofonduty_sp_string = typeofonduty_sp.getSelectedItem().toString();
                reasononduty_et_string = reasononduty_et.getText().toString();

                //Toast.makeText(getActivity(), "Server busy", Toast.LENGTH_SHORT).show();
                if (Validation()) {
                    internetDectector = new ConnectionDetector(getActivity());
                    isInternetPresent = internetDectector.isConnectingToInternet();

                    if (isInternetPresent) {
                        AsyncCallWS3 task = new AsyncCallWS3(context);
                        task.execute();
                    }
                    else{
                        Toast.makeText(getActivity(),"No internet",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        return lLayoutApplyonduty;
    }// end of oncreate()


    public boolean Validation()
    {
        boolean validationresult=true;
        boolean datevalidationresult1=true,datevalidationresult2=true,locationvalidationresult=true,reasonvalidationresult=true;
        if(yyyyMMdd_fromdate.toString().length()==0||yyyyMMdd_todate.toString().length()==0)
        {
            Toast.makeText(getActivity(), "Kindly enter the date", Toast.LENGTH_SHORT).show();
            datevalidationresult1=false;
        }


        if((yyyyMMdd_fromdate.toString().length()!=0)&&(yyyyMMdd_todate.toString().length()!=0) )
        {
        /*if(date1.compareTo(date2)<0){ //0 comes when two date are same,
            //1 comes when date1 is higher then date2
            //-1 comes when date1 is lower then date2 }*/

            SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd");  //2017-06-22

            try {
                Date fromdate = mdyFormat.parse(yyyyMMdd_fromdate);
                Date todate = mdyFormat.parse(yyyyMMdd_todate);

                if(fromdate.compareTo(todate)<=0)
                {
                    datevalidationresult2=true;
                    /*if(fromdate.compareTo(todate)<=7)
                    {
                        datevalidationresult2=true;
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "OD only for 7 days", Toast.LENGTH_SHORT).show();
                        datevalidationresult2=false;
                    }*/

                }
                else{
                    Toast.makeText(getActivity(), "Kindly enter valid date", Toast.LENGTH_SHORT).show();
                    datevalidationresult2=false;
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }//end of if

        if(locationonduty_et.getText().toString().length()==0||locationonduty_et.getText().toString().trim().length()<3)
        {
            locationonduty_et.setError("Minimum 3 character is required");
            locationonduty_et.requestFocus();
            locationvalidationresult=false;
        }

        if(reasononduty_et.getText().toString().length()==0||reasononduty_et.getText().toString().trim().length()<10)
        {
            reasononduty_et.setError("Minimum 10 character is required");
            reasononduty_et.requestFocus();
            reasonvalidationresult=false;
        }


        if(datevalidationresult1&&datevalidationresult2&&locationvalidationresult&&reasonvalidationresult)
        { return true; }
        else{return false; }

    }// End of validation








   //******************************************* Date ****************************************************

    // Start of to date
    @SuppressLint("ValidFragment")
    public class DatePickerFragment extends  DialogFragment
            implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            /*return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener)
                            getActivity(), year, month, day);*/





            DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                    this, year, month, day);

            // dialog.getDatePicker().setMaxDate(c.getTimeInMillis()); // this will set maximum date validation
            //dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            //dialog.getDatePicker().setMaxDate(c.getTimeInMillis());

            // dialog.getDatePicker().setMaxDate(c.getTimeInMillis()+((1000*60*60*24*90)));//Error:fromDate: Sun Jun 18 12:50:44 GMT+05:30 2017 does not precede toDate: Fri Jun 09 02:45:11 GMT+05:30 2017


           /* dialog.getDatePicker().setMinDate(c.getTimeInMillis());
            c.add(Calendar.DAY_OF_MONTH,150);
            dialog.getDatePicker().setMaxDate(c.getTimeInMillis());*/

            return dialog;


        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar cal = new GregorianCalendar(year, month, day);
            setDate(cal);
        }

        public void setDate(final Calendar calendar) {
            final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
            //((TextView) findViewById(R.id.showDate)).setText(dateFormat.format(calendar.getTime()));

            clicktodateonduty_tv.setText(dateFormat.format(calendar.getTime()));

            SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd");  //2017-06-22

            yyyyMMdd_todate = mdyFormat.format(calendar.getTime());

            //System.out.println("To Date:"+ yyyyMMdd_todate);
        }

    }



    // start of from date
    @SuppressLint("ValidFragment")
    public class DatePickerFragmentFromDate extends  DialogFragment
            implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            /*return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener)
                            getActivity(), year, month, day);*/





            DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                    this, year, month, day);


       /* dialog.getDatePicker().setMinDate(c.getTimeInMillis());
        dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
*/

       /* dialog.getDatePicker().setMinDate(c.getTimeInMillis());
        c.add(Calendar.DAY_OF_MONTH,150);
        dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
*/
            return dialog;


        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar cal = new GregorianCalendar(year, month, day);
            setDate(cal);
        }

        public void setDate(final Calendar calendar) {
            final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
            //((TextView) findViewById(R.id.showDate)).setText(dateFormat.format(calendar.getTime()));


            clickfromdateonduty_tv.setText(dateFormat.format(calendar.getTime()));

            // SimpleDateFormat mdyFormat = new SimpleDateFormat("MM/dd/yyyy");//2017-06-22

            SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd");
            yyyyMMdd_fromdate = mdyFormat.format(calendar.getTime());

           // System.out.println("From date:"+ yyyyMMdd_fromdate);
        }

    }





  //****************************************** Date *******************************************************













    // Submit Async Leave request
    private class AsyncCallWS3 extends AsyncTask<String, Void, Void>
    {

        ProgressDialog dialog;

        Context context;
        protected void onPreExecute()
        {
            //	Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values)
        {
            //Log.i(TAG, "onProgressUpdate---tab2");
        }

        @Override
        protected Void doInBackground(String... params)
        {
            Log.i("DF", "doInBackground");



            applyonduty();



            return null;
        }

        public AsyncCallWS3(Context context1) {
            context =  context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected void onPostExecute(Void result)

        {
            if (dialog.isShowing()) {
                dialog.dismiss();

            }
            if(internet_issue.equals("slow internet"))
            {
                Toast.makeText(context, "Error:"+str_error.toString() ,Toast.LENGTH_LONG).show();
            }
            else
            {

                if(str_response.toString().equals("Already OD/Leave Applied."))
                {
                    Toast.makeText(context, "Already OD/Leave applied on this date" ,Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(str_response.toString().equals("Fail"))
                    {
                        Toast.makeText(context, "Failed to apply OD" ,Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(str_response.toString().equals("Request sent."))
                        {
                           /* Toast.makeText(context, "successfully applied leave" ,Toast.LENGTH_LONG).show();
                            FindPeopleFragment fragment = new FindPeopleFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frame_container, fragment).commit();*/

                            Toast.makeText(context, "Request has been sent" ,Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getActivity(),HomeActivity.class);
                            startActivity(i);
                            getActivity().getFragmentManager().popBackStack();//OD Maximum 7 Days can be Applied.
                        }

                        else if(str_response.toString().equals("OD Maximum 7 Days can be Applied."))
                        {
                            Toast.makeText(context, "OD Maximum 7 Days can be Applied." ,Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(context, "Error:"+str_error.toString() ,Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }
            Log.i("DF", "onPostExecute");


        }
    }// End of AsyncCallWS3


    public void applyonduty()
    {
        //det hrms
        //http://dethrms.cloudapp.net/PMSservice.asmx
      //  String URL ="http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";
        String METHOD_NAME = "RequestforOD";
        String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/RequestforOD";
        //det Hrms
 /*<empId>long</empId>
      <From_Date>string</From_Date>
      <To_Date>string</To_Date>
      <Location>string</Location>
      <Reason>string</Reason>
      <Description>string</Description>*/
        try {

            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("empId", Employeeidlong);


            /*request.addProperty("fromDate", format.format(Fromcalendar1.getTime()).toString());
            request.addProperty("to", format.format(Tocalendar1.getTime()).toString());*/


            request.addProperty("From_Date", yyyyMMdd_fromdate.trim().toString());
            request.addProperty("To_Date", yyyyMMdd_todate.trim().toString());
            request.addProperty("Location", locationonduty_et_string);
            request.addProperty("Reason", typeofonduty_sp_string);
            request.addProperty("Description", reasononduty_et_string);

           // System.out.println("Onduty message: "+ yyyyMMdd_fromdate);

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
                SoapPrimitive soapprimitive_response;

                soapprimitive_response = (SoapPrimitive ) envelope.getResponse();
               // response = (SoapPrimitive ) envelope.getResponse();
                str_response = soapprimitive_response.toString().trim();
                Log.e("string Onduty response",str_response.toString());

            }
            catch (Throwable t) {
                //Toast.makeText(context, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                str_error=t.getMessage().toString();
                internet_issue="slow internet";
            }
        }catch (Throwable t) {
            //Toast.makeText(context, "UnRegister Receiver Error " + t.toString(),
            //		Toast.LENGTH_LONG).show();
            Log.e("UnRegister Recei Error", "> " + t.getMessage());
            internet_issue="slow internet";
            str_error=t.getMessage().toString();

        }

    }//End of applyonduty


}//end of class fragment_applyonduty
