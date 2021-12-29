package com.fssihrms;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;

//import android.support.v4.app.DialogFragment;
import android.os.AsyncTask;

import android.app.DialogFragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.app.Fragment;
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
import java.util.Vector;
import static com.fssihrms.Constant_webservice.URL;
//import hrms.com.hrm.R;

public class Applyleave extends Fragment {

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applyleave);
    }*/
    int count1;
    Spinner typeofleave_sp,durationofleave_sp,incharge_sp;

    String typeofleave_sp_selected,durationofleave_sp_selected, alternativecell_et_String,leavereason_et_string,send_leave_type,taskdetails_et_string;

    private ScrollView lLayoutFrgEmpresas;

    Button applyleave_bt;
    String username,pwd,emp_id,internet_issue="empty";
    int noOfobjects = 0;

    String yyyyMMdd_fromdate="";
    String yyyyMMdd_todate="";

    ConnectionDetector internetDectector;
    Boolean isInternetPresent = false;

TextView remaning_tv,out_tv,fromdate_tv,todate_tv,clickfromdate_tv,clicktodate_tv,typeofleave_tv,duration_tv,incharge_tv,alternative_tv,reason_tv,taskdetails_tv;

  TextView balanc_tv,openingbalance_tv;
    Context context;
    String slow_intenet="no";
    int Teamcount;
    AllTeamList[] ash;
    AllTeamList st;
    LeaveDetail[] leavelist;

EditText alternativecell_et,leavereason_et,taskdetails_et;

    SoapPrimitive  response;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        lLayoutFrgEmpresas=(ScrollView) inflater.inflate(R.layout.applyleavefragment, container, false);

        context = lLayoutFrgEmpresas.getContext();

        SharedPreferences myprefs= this.getActivity().getSharedPreferences("user", Context.MODE_WORLD_READABLE);
        //Toast.makeText(Instant.this,myprefs.getAll().toString(),Toast.LENGTH_LONG).show();
        username = myprefs.getString("user1", "nothing");
        pwd = myprefs.getString("pwd", "nothing");
        emp_id = myprefs.getString("emp_id", "nothing");



        balanc_tv =(TextView)lLayoutFrgEmpresas.findViewById(R.id.balanc_TV);
        openingbalance_tv=(TextView)lLayoutFrgEmpresas.findViewById(R.id.openingbalance_TV);



        String[] typeofleave_spinnerdropdown = {
                "Casual leave","Sick leave","Earned Leave"};

        String[] duration_spinnerdropdown = {
                "First half","Second half","Full day"};

        String[] incharge_spinnerdropdown={"Sunilkumar","Raghavendra J","Anada Ingale","Arsheen Naaz Kazi"};

        typeofleave_sp = (Spinner) lLayoutFrgEmpresas.findViewById(R.id.typeofleave_SP);
        ArrayAdapter<String> typeofleave_adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.leavetypespinneritems, typeofleave_spinnerdropdown);
        typeofleave_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeofleave_sp.setAdapter(typeofleave_adapter);



        durationofleave_sp = (Spinner) lLayoutFrgEmpresas.findViewById(R.id.durationofleave_SP);
      //  ArrayAdapter<String> durationofleave_adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, duration_spinnerdropdown);
        ArrayAdapter<String> durationofleave_adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.leavetypespinneritems, duration_spinnerdropdown);
        durationofleave_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        durationofleave_sp.setAdapter(durationofleave_adapter);



       /* incharge_sp = (Spinner) lLayoutFrgEmpresas.findViewById(R.id.incharge_SP);
       // ArrayAdapter<String> incharge_adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, incharge_spinnerdropdown);
        ArrayAdapter<String> incharge_adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.leavetypespinneritems, incharge_spinnerdropdown);
        incharge_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        incharge_sp.setAdapter(incharge_adapter);*/



        applyleave_bt=(Button) lLayoutFrgEmpresas.findViewById(R.id.applyleave_BT);
        alternativecell_et=(EditText) lLayoutFrgEmpresas.findViewById(R.id.alternativecell_ET);
        leavereason_et =(EditText) lLayoutFrgEmpresas.findViewById(R.id.leavereason_ET);
        taskdetails_et =(EditText) lLayoutFrgEmpresas.findViewById(R.id.taskdetailsreason_ET);

        allthetextview_Applyleave();
        changethefont_Applyleave();




        clicktodate_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // settodate();
                DialogFragment dFragment = new DatePickerFragment();
                // Show the date picker dialog fragment
                dFragment.show(getFragmentManager(), "Date Picker");



            }
        });

        clickfromdate_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment fromdateFragment = new DatePickerFragmentFromDate();
                fromdateFragment.show(getFragmentManager(), "Date Picker");
            }
        });

   //*****************************************incharge spinner***************************************
        incharge_sp = (Spinner) lLayoutFrgEmpresas.findViewById(R.id.incharge_SP);
        //incharge_SP
        GetTeamlistAsyncCallWS task = new GetTeamlistAsyncCallWS(context);
        task.execute();


        incharge_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                st = (AllTeamList)incharge_sp.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
//*****************************************incharge spinner***************************************


//*****************************************Balance leave/Opening leave***************************************
        AsyncCallWS2 task1 = new AsyncCallWS2(context);
        task1.execute();
//*****************************************Balance leave/Opening leave***************************************

 //*****************************************Apply leave***************************************
        applyleave_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeofleave_sp_selected=typeofleave_sp.getSelectedItem().toString();
                durationofleave_sp_selected=durationofleave_sp.getSelectedItem().toString();

                leavereason_et_string=leavereason_et.getText().toString();
                taskdetails_et_string =taskdetails_et.getText().toString();



               if(Validation()) {

                  internetDectector = new ConnectionDetector(getActivity());
                  isInternetPresent = internetDectector.isConnectingToInternet();
                   if(durationofleave_sp_selected.equals("Full day"))
                   {
                       send_leave_type ="1";
                   }
                   else
                   {
                       send_leave_type="0.5";
                   }

                  if (isInternetPresent) {
                       AsyncCallWS3 task = new AsyncCallWS3(context);
                       task.execute();
                   }else{
                       Toast.makeText(getActivity(),"No internet",Toast.LENGTH_LONG).show();
                   }

               }

            }//
        });//applyleave_bt





//*****************************************Apply leave***************************************










        return lLayoutFrgEmpresas;
    }//End of Oncreate()


   /* public void settodate()
    {
       // DatePickerFragment fragment = new DatePickerFragment();
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(), "date");
        fragment.show();
    }*/


//***********************************Validation*************************************************************************


public boolean Validation()
{
    boolean validationresult=true;
    boolean datevalidationresult1=true,datevalidationresult2=true,cellvalidationresult1=true,cellvalidationresult2=true,reasonvalidationresult=true,taskdetailsreasonvalidationresult=true;

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
                }
                else{
                    Toast.makeText(getActivity(), "Kindly enter valid date", Toast.LENGTH_SHORT).show();
                    datevalidationresult2=false;
                }


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }//end of try catch

    if(alternativecell_et.getText().toString().length()==0)
    {
        alternativecell_et.setError("Empty not allowed!");
        alternativecell_et.requestFocus();cellvalidationresult1=false;
    }
    if(alternativecell_et.getText().toString().length()<10){
        alternativecell_et.setError("10 digit is required");
        alternativecell_et.requestFocus();cellvalidationresult2=false;
    }


    if(leavereason_et.getText().toString().trim().length()==0||leavereason_et.getText().toString().trim().length()<10)
    {
        leavereason_et.setError("Minimum 10 character is required");
        leavereason_et.requestFocus();
        reasonvalidationresult=false;
    }

    if(taskdetails_et.getText().toString().length()==0||taskdetails_et.getText().toString().trim().length()<5)
    {
        taskdetails_et.setError("Minimum 5 character is required");
        taskdetails_et.requestFocus();
        taskdetailsreasonvalidationresult=false;
    }

if(datevalidationresult1&&datevalidationresult2&&cellvalidationresult1&&cellvalidationresult2&&reasonvalidationresult&&taskdetailsreasonvalidationresult)
{
    return true;
}else{
    return false;
}

}//end of validation







//***********************************Validation*************************************************************************




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

            clicktodate_tv.setText(dateFormat.format(calendar.getTime()));

            SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd");  //2017-06-22

            yyyyMMdd_todate = mdyFormat.format(calendar.getTime());

           // System.out.println("To Date:"+ yyyyMMdd_todate);
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


        clickfromdate_tv.setText(dateFormat.format(calendar.getTime()));

       // SimpleDateFormat mdyFormat = new SimpleDateFormat("MM/dd/yyyy");//2017-06-22

        SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-d");
        yyyyMMdd_fromdate = mdyFormat.format(calendar.getTime());

       // System.out.println("From date:"+ yyyyMMdd_fromdate);
    }

}

// end of from date
    //allthetextview_Applyleave() declaration  and font assign  changethefont_Applyleave()


    public void allthetextview_Applyleave()
    {
         remaning_tv = (TextView)lLayoutFrgEmpresas.findViewById(R.id.remaning_TV);
        out_tv = (TextView) lLayoutFrgEmpresas.findViewById(R.id.out_TV);
        fromdate_tv = (TextView) lLayoutFrgEmpresas.findViewById(R.id.fromdate_TV);
        todate_tv=(TextView) lLayoutFrgEmpresas.findViewById(R.id.todate_TV);
        clickfromdate_tv=(TextView) lLayoutFrgEmpresas.findViewById(R.id.clickfromdate_TV);
        clicktodate_tv=(TextView) lLayoutFrgEmpresas.findViewById(R.id.clicktodate_TV);
        typeofleave_tv=(TextView) lLayoutFrgEmpresas.findViewById(R.id.typeofleave_TV);
        duration_tv=(TextView) lLayoutFrgEmpresas.findViewById(R.id. duration_TV);
        incharge_tv=(TextView) lLayoutFrgEmpresas.findViewById(R.id. incharge_TV);
        alternative_tv=(TextView) lLayoutFrgEmpresas.findViewById(R.id.alternative_TV);
        reason_tv=(TextView) lLayoutFrgEmpresas.findViewById(R.id.reasons_TV);
        taskdetails_tv=(TextView) lLayoutFrgEmpresas.findViewById(R.id.taskdetails_TV);

    }

    public void changethefont_Applyleave()
    {
        Typeface face = Typeface.createFromAsset(getResources().getAssets(), "fonts/centurygothic.ttf");
        Typeface facebold = Typeface.createFromAsset(getResources().getAssets(), "fonts/centurygothic.ttf");
        remaning_tv.setTypeface(facebold);
        out_tv.setTypeface(facebold);
        fromdate_tv.setTypeface(face);
        todate_tv.setTypeface(face);
        clickfromdate_tv.setTypeface(face);
        clicktodate_tv.setTypeface(face);
        typeofleave_tv.setTypeface(face);
        duration_tv.setTypeface(face);
        incharge_tv.setTypeface(face);
         alternative_tv.setTypeface(face);
        reason_tv.setTypeface(face);
        applyleave_bt.setTypeface(face);
        taskdetails_tv.setTypeface(face);
    }

    //End of allthetextview_Applyleave() changethefont_Applyleave()






    private class GetTeamlistAsyncCallWS extends AsyncTask<String, Void, Void>
    {
        ProgressDialog dialog;

        Context context;

        @Override
        protected void onPreExecute()
        {
            Log.i("TAG", "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values)
        {
            Log.i("", "onProgressUpdate---tab2");
        }

        public GetTeamlistAsyncCallWS(Context context1) {
            context =  context1;
            dialog = new ProgressDialog(context1);
        }
        @Override
        protected Void doInBackground(String... params)
        {
            Log.i("TAG", "doInBackground---tab5");

            getTeamList();

            //	AddprojectDetail();


            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            Log.i("TAG", "onPostExecute---tab5");

            if (dialog.isShowing())
            {
                dialog.dismiss();

            }

            if(slow_intenet.equals("no"))
            {
                if(Teamcount<1)
                {
                    Toast.makeText(context,"No TeamMember", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    ArrayAdapter dataAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, ash);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    incharge_sp.setAdapter(dataAdapter);
                }


            }
            else
            {
                Toast.makeText(context,"You are on slow internet", Toast.LENGTH_SHORT).show();
            }


        }


    }//End of GetTeamlistAsyncCallWS


    public void getTeamList()
    {
		/*String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
		String METHOD_NAME = "TeamList";//"NewAppReleseDetails";
		String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/TeamList";*/

        //Dftestbed starts
       /* String URL ="http://dftestbed.cloudapp.net/PMSservice.asmx?WSDL";
        String METHOD_NAME = "TeamList";
        String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/TeamList";*/
        //dftestbed end here

        //http://dethrms.cloudapp.net/PMSservice.asmx
        //DET Hrms
      //  String URL ="http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";
        String METHOD_NAME = "TeamList";
        String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/TeamList";
        //DET Hrms


        SoapObject request = new SoapObject(Namespace, METHOD_NAME);
       request.addProperty("id", emp_id);

       // request.addProperty("id", "90");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        //Set output SOAP object
        envelope.setOutputSoapObject(request);
        //Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try
        {
            androidHttpTransport.call(SOAPACTION, envelope);
            SoapObject  response = (SoapObject ) envelope.getResponse();
            Log.i("response empoylee list",response.toString());
            Teamcount = response.getPropertyCount();



            //	 Log.i("string value at respose count",count+"");



            if(Teamcount>0)

            {
                ash= new AllTeamList[Teamcount];
                for (int i = 0; i < Teamcount; i++) {
                    SoapObject messeg1 = (SoapObject) response.getProperty(i);
                    //		 tt=  +i+" Title " +messeg1.getProperty("Title").toString()+ " Status "+messeg1.getProperty("ProjectStatus").toString();
                    //		 Log.i("Project N0",tt);
                    AllTeamList project = new AllTeamList();
                    project.setid(messeg1.getProperty("Id").toString()); ;
                    project.setemployeeName(messeg1.getProperty("Name").toString()); ;
                    // ash[i].setproject_title(messeg1.getProperty("Title").toString()) ;
                    project.setemail(messeg1.getProperty("email").toString()); ;
                    project.setemployee_id(messeg1.getProperty("Employee_Id").toString()); ;
                    // ash[i].setStatus(messeg1.getProperty("ProjectStatus").toString()) ;
                    ash[i] = project;
                }



            }



        }
        catch (Exception e)
        {
            slow_intenet="yes";
            e.printStackTrace();
        }
    }// End of getTeamList





    private class AsyncCallWS2 extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog;

        Context context;

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
            Log.i("Ashraf", "doInBackground");
            //  GetAllEvents();
            leave_detaile();  // call of details
            return null;
        }

        public AsyncCallWS2(Context context1) {
            context = context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected void onPostExecute(Void result) {

           /* if ((this.dialog != null) && this.dialog.isShowing()) {
                dialog.dismiss();

            }*/

            dialog.dismiss();


           System.out.println("Reached the onPostExecute");


            if (count1 > 0) {


               // opening_balance_bt.setText(leavelist[0].getOpening_Balance() + "  ");


                Log.i("response Forward list",leavelist[0].getBrought_Forword().toString());
                Log.i("response Balance list",leavelist[0].getOpening_Balance().toString());
                Log.i("response Availed list",leavelist[0].getAvailedLeaves().toString());

               double cl= Double.parseDouble(leavelist[0].getCasualleave());
                double cl_carryforward =Double.parseDouble(leavelist[0].getCLcarryforward());
                double sl=Double.parseDouble(leavelist[0].getSickleave());
               double sl_carryforward=Double.parseDouble(leavelist[0].getSLcarryforward());
                double el=Double.parseDouble(leavelist[0].getEarnedleave());
                double el_carryforward=Double.parseDouble(leavelist[0].getELcarryforward());

                double total=cl+cl_carryforward+sl+sl_carryforward+el+el_carryforward;

                String openingbalance="";
                        openingbalance=Double.toString(total);

            // balanc_tv.setText((Double.parseDouble(leavelist[0].getBrought_Forword()) + Double.parseDouble(leavelist[0].getOpening_Balance()) - Double.parseDouble(leavelist[0].getAvailedLeaves()) +""));

              balanc_tv.setText(((total) - Double.parseDouble(leavelist[0].getAvailedLeaves()) +""));

               // openingbalance_tv.setText(leavelist[0].getOpening_Balance());

                openingbalance_tv.setText(openingbalance);

                System.out.println("balanc_tv"+balanc_tv);
               /* Brought_Forword_bt.setText(leavelist[0].getBrought_Forword() + "  ");
                avil_leave_bt.setText(leavelist[0].getAvailedLeaves() + "  ");*/

               // balanceleave_bt.setText((Double.parseDouble(leavelist[0].getBrought_Forword()) + Double.parseDouble(leavelist[0].getOpening_Balance()) - Double.parseDouble(leavelist[0].getAvailedLeaves()) + "  "));

            }


        }//end of onPostExecute
    }//End of Async task

    public void leave_detaile() {
        Vector<SoapObject> result1 = null;


        // String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL"; //com on June6,2017

        // String METHOD_NAME = "intCount";//"NewAppReleseDetails";
        // String Namespace="http://www.example.com", SOAPACTION="http://www.example.com/intCount";
        // String URL = "http://192.168.1.196:8080/deterp_ws/server4.php?wsdl";//"Login.asmx?WSDL";

        //http://dfhrms.cloudapp.net/PMSservice.asmx
        //http://dfhrms.cloudapp.net/PMSservice.asmx?op=LeaveCancelRequest

        // String METHOD_NAME = "GetEmployeeDetails";//"NewAppReleseDetails";  //com on June6,2017
        // String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/GetEmployeeDetails";  //com on June6,2017


        //dftestbed starts
       /* String URL="http://dftestbed.cloudapp.net/PMSservice.asmx?WSDL";
        String METHOD_NAME = "GetEmployeeDetails";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/GetEmployeeDetails";
*/
        //dftestbed ends


        //dethrms
      //  String URL="http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";
        String METHOD_NAME = "GetEmployeeDetails";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/GetEmployeeDetails";
        //dethrms

        try {
            // String  versioncode = this.getPackageManager()
            //        .getPackageInfo(this.getPackageName(), 0).versionName;
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("email", username);
            // request.addProperty("email", "raghavendra.tech@dfmail.org");
            //  request.addProperty("to", 9);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAPACTION, envelope);
                //  Log.i(TAG, "GetAllLoginDetails is running");
                //    result1 = (Vector<SoapObject>) envelope.getResponse();
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i("value at response DET", response.toString());
                count1 = response.getPropertyCount();  // number of count in array in response 6,0-5,5

                Log.i("number of rows", "" + count1);

                noOfobjects = count1;


                System.out.println("Number of object" + noOfobjects);
                // leavelist[0].setName("kya");
                leavelist = new LeaveDetail[count1];

                for (int i = 0; i < count1; i++) {
                    SoapObject list = (SoapObject) response.getProperty(i);
                    SoapPrimitive no_of_Days, Leave_Id, Name, email, Program, Employee_Id, Opening_Balance, AvailedLeaves, manager_email, From_Date, To_Date, Reason, LEave_Status, Approved_On;
                    String approved = "";
                    Leave_Id = (SoapPrimitive) list.getProperty("Leave_Id");
                    Name = (SoapPrimitive) list.getProperty("Name");
                    email = (SoapPrimitive) list.getProperty("email");
                    Program = (SoapPrimitive) list.getProperty("Program");
                    Employee_Id = (SoapPrimitive) list.getProperty("Employee_Id");
                    // Opening_Balance = (SoapPrimitive) list.getProperty("Opening_Balance");
                    AvailedLeaves = (SoapPrimitive) list.getProperty("AvailedLeaves");
                    // manager_email= (SoapPrimitive)list.getProperty("manager_email");
                    From_Date = (SoapPrimitive) list.getProperty("From_Date");
                    To_Date = (SoapPrimitive) list.getProperty("To_Date");
                    Reason = (SoapPrimitive) list.getProperty("Reason");
                    no_of_Days = (SoapPrimitive) list.getProperty("No_of_Days");
                    LEave_Status = (SoapPrimitive) list.getProperty("LEave_Status");
                    if (list.getProperty("Approved_On").toString().equals("anyType{}")) {
                        // Approved_On.add("");
                        // Approved_On = (SoapPrimitive);
                    } else {
                        approved = list.getProperty("Approved_On").toString();
                    }
                    LeaveDetail project = new LeaveDetail();
                    // leavelist[i].setLeave_Id(Leave_Id.toString());
                    Log.i("value at name premitive", Name.toString());
                    project.setLeave_Id(Leave_Id.toString());
                    project.setName(Name.toString());
                    project.setemail(email.toString());
                    project.setProgram(Program.toString());
                    project.setEmployee_Id(Employee_Id.toString());

                    //project.setOpening_Balance(Opening_Balance.toString());

                    project.setOpening_Balance("20");

                    project.setAvailedLeaves(AvailedLeaves.toString());
                    project.setno_days(no_of_Days.toString());
                    //   leavelist[i].setmanager_email(manager_email.toString());
                    project.setFrom_Date(From_Date.toString().substring(4, From_Date.toString().length() - 5));
                    // Log.i("string value at messege",From_Date.toString().substring(3));
                    project.setTo_Date(To_Date.toString().substring(4, To_Date.toString().length() - 5));
                    project.setReason(Reason.toString());
                    project.setLEave_Status(LEave_Status.toString());
                    project.setApproved_On(approved);
                    project.setBrought_Forword(list.getProperty("carry_forward").toString());

                    project.setCasualleave(list.getProperty("Casual_Leave").toString());
                    project.setCLcarryforward(list.getProperty("CL_carry_forward").toString());
                    project.setSickleave(list.getProperty("Sick_Leave").toString());
                    project.setSLcarryforward(list.getProperty("SL_carry_forward").toString());
                    project.setEarnedleave(list.getProperty("Earned_Leave").toString());
                    project.setELcarryforward(list.getProperty("EL_Carry_Forward").toString());

                    project.setFrom_DateC(From_Date.toString());
                    project.setTo_DateC(To_Date.toString());
                    //   leavelist[i].setApproved_On(Approved_On.toString());
                    leavelist[i] = project;

                }// End of for loop


                //   version = (SoapPrimitive)response.getProperty("AppVersion");
                // release_not = (SoapPrimitive)response.getProperty("ReleseNote");


                //Log.i("string value at messeg",messeg.toString());


            } catch (Throwable t) {
                //Toast.makeText(MainActivity.this, "Request failed: " + t.toString(),
                //    Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
            }
        } catch (Throwable t) {
            Log.e("UnRegister  Error", "> " + t.getMessage());

        }

    }//End of leaveDetail method


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


            //	GetAllEvents();
            applyleave();



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

            }
            else
            {
                if(response.toString().equals("Already Leave Applied"))
                {
                    Toast.makeText(context, response.toString() ,Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(response.toString().equals("Fail"))
                    {
                        Toast.makeText(context, "Fail to apply leave" ,Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(response.toString().equals("Success"))
                        {
                           /* Toast.makeText(context, "successfully applied leave" ,Toast.LENGTH_LONG).show();
                            FindPeopleFragment fragment = new FindPeopleFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frame_container, fragment).commit();*/

                            Toast.makeText(context, "Leave applied" ,Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getActivity(),HomeActivity.class);
                            startActivity(i);
                            getActivity().getFragmentManager().popBackStack();
                        }
                        else if(response.toString().equals("Not able to Apply Leave on weekoff"))
                        {
                            Toast.makeText(context, response.toString().trim() ,Toast.LENGTH_LONG).show();
                            /*Intent i = new Intent(getActivity(),HomeActivity.class);
                            startActivity(i);
                            getActivity().getFragmentManager().popBackStack();*/
                        }
                        else
                        {
                            Toast.makeText(context, "Network issue" ,Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }
            Log.i("DF", "onPostExecute");


        }
    }// End of AsyncCallWS3

    public void applyleave()
    {

        // String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";  //comJ06
        // String METHOD_NAME = "intCount";//"NewAppReleseDetails";
        // String Namespace="http://www.example.com", SOAPACTION="http://www.example.com/intCount";
        // String URL = "http://192.168.1.196:8080/deterp_ws/server4.php?wsdl";//"Login.asmx?WSDL";

        // String METHOD_NAME = "ApplyLeavefortesting";//"NewAppReleseDetails"; //comJ06
        //String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/ApplyLeavefortesting";  //comJ06

        //df testbed
        /*String URL ="http://dftestbed.cloudapp.net/PMSservice.asmx?WSDL";
        String METHOD_NAME = "ApplyLeavefortesting";
        String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/ApplyLeavefortesting";*/
        //df testbed

//det hrms
        //http://dethrms.cloudapp.net/PMSservice.asmx
       // String URL ="http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";
        String METHOD_NAME = "ApplyLeavefortesting";
        String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/ApplyLeavefortesting";
        //det Hrms




        try{
            // String  versioncode = this.getPackageManager()
            //		    .getPackageInfo(this.getPackageName(), 0).versionName;
//
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("employeeId", emp_id);

            /*request.addProperty("fromDate", format.format(Fromcalendar1.getTime()).toString());
            request.addProperty("to", format.format(Tocalendar1.getTime()).toString());*/


           /* request.addProperty("fromDate", "2017-06-22");
            request.addProperty("to", "2017-06-23");*/

            request.addProperty("to", yyyyMMdd_todate);
            request.addProperty("fromDate", yyyyMMdd_fromdate);

           // System.out.println("Web service"+yyyyMMdd_todate);

            request.addProperty("LeaveType", typeofleave_sp_selected);
            request.addProperty("Duration", send_leave_type);//durationofleave_sp_selected , 1 or .5
            request.addProperty("Incharge", st.getid()+"-"+st.getemployeeName() );
            //request.addProperty("InchargeDetails", task.getText().toString() );
            request.addProperty("InchargeDetails", taskdetails_et_string);
            request.addProperty("reason", leavereason_et_string);


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
                response = (SoapPrimitive ) envelope.getResponse();
                Log.i("string value atsponse",response.toString());



                //	 SoapPrimitive messege = (SoapPrimitive)response.getProperty("Status");
                // version = (SoapPrimitive)response.getProperty("AppVersion");
                // release_not = (SoapPrimitive)response.getProperty("ReleseNote");



                //Log.i("string value at messeg",messeg.toString());
            }
            catch (Throwable t) {
                //Toast.makeText(context, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                internet_issue="slow internet";
            }
        }catch (Throwable t) {
            //Toast.makeText(context, "UnRegister Receiver Error " + t.toString(),
            //		Toast.LENGTH_LONG).show();
            Log.e("UnRegister Recei Error", "> " + t.getMessage());

        }

    }






}// end of class


