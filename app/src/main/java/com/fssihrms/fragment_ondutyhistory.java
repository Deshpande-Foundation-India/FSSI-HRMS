package com.fssihrms;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Vector;
import static com.fssihrms.Constant_webservice.URL;
//import hrms.com.hrm.R;

/**
 * Created by User on 6/27/2017.
 */

public class fragment_ondutyhistory extends Fragment {

    private LinearLayout lLayoutondutyhistory;

    String username,pwd,emp_id,internet_issue="empty";
    String slow_intenet="no";
    long Employeeidlong;

    private String str_dayscheck;

    OndutyhistoryClass[] ondutyhistoryclass_arrayObj;

    Context context;
    Resources resource;
    private ListView listView;
    int count1;
    int noOfobjects = 0;

    LinearLayout od_history_LL,no_od_history_LL;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        lLayoutondutyhistory=(LinearLayout) inflater.inflate(R.layout.fragment_ondutyhistory, container, false);
       // context = lLayoutondutyhistory.getContext();
        SharedPreferences myprefs= this.getActivity().getSharedPreferences("user", Context.MODE_WORLD_READABLE);
        //Toast.makeText(Instant.this,myprefs.getAll().toString(),Toast.LENGTH_LONG).show();
        username = myprefs.getString("user1", "nothing");
        pwd = myprefs.getString("pwd", "nothing");
        emp_id = myprefs.getString("emp_id", "nothing");


        listView = (ListView) lLayoutondutyhistory.findViewById(R.id.customlistview_ondutyhistory);

        Employeeidlong = Long.parseLong(emp_id); // for web service


        context = lLayoutondutyhistory.getContext();
        resource = context.getResources();
        AsyncCallWS2 task = new AsyncCallWS2(context);
        task.execute();



        return  lLayoutondutyhistory;
    }






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
            //  get OD History
            onduty_history();  // call OD History
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

            if (ondutyhistoryclass_arrayObj != null) {
                CustomAdapter adapter = new CustomAdapter();
                listView.setAdapter(adapter);

                int x = ondutyhistoryclass_arrayObj.length;

                System.out.println("Inside the if list adapter" + x);
            } else {
                Log.d("onPostExecute", "ondutyhistoryclass_arrayObj == null");
            }

            System.out.println("Reached the onPostExecute");

        }//end of onPostExecute
    }// end Async task


    public void onduty_history() {
        Vector<SoapObject> result1 = null;

//dethrms
       // String URL="http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";
        String METHOD_NAME = "GetODRequests";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/GetODRequests";
        //dethrms

        try {
            // String  versioncode = this.getPackageManager()
            //        .getPackageInfo(this.getPackageName(), 0).versionName;
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            //request.addProperty("email", username);
            request.addProperty("id", Employeeidlong);


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
                Log.i("value at response", response.toString());
                count1 = response.getPropertyCount();  // number of count in array in response 6,0-5,5

                Log.i("number of rows", "" + count1);

                noOfobjects = count1;


                System.out.println("Number of object" + noOfobjects);


                /*<Id>0</Id>
                <Employee_Id>117</Employee_Id>
                <Location>hubli</Location>
                <Reason>Marketing</Reason>
                <Description>testingnztesting</Description>
                <No_of_days>0</No_of_days>
                <Od_status>Unapproved</Od_status>*/
                ondutyhistoryclass_arrayObj = new OndutyhistoryClass[count1];

                for (int i = 0; i < count1; i++) {

                    SoapObject list = (SoapObject) response.getProperty(i);
                    SoapPrimitive location, days, status, typeofod, description,From_Date,TodateOD;
                    String approved = "";

                    str_dayscheck = (String) list.getProperty("No_of_days").toString();

                    Log.e("str_dayscheck",str_dayscheck.toString());



                    if(str_dayscheck.equals("0"))
                    {
                        //employee haven't applied OD
                    }
                    else {

                        location = (SoapPrimitive) list.getProperty("Location");
                        days = (SoapPrimitive) list.getProperty("No_of_days");
                        status = (SoapPrimitive) list.getProperty("Od_status");
                        typeofod = (SoapPrimitive) list.getProperty("Reason");
                        description = (SoapPrimitive) list.getProperty("Description");
                        From_Date = (SoapPrimitive) list.getProperty("From_Date");
                        TodateOD = (SoapPrimitive) list.getProperty("To_Date");

                        OndutyhistoryClass innerObj = new OndutyhistoryClass();
                        Log.i("onduty location", location.toString());
                        innerObj.setLocation(location.toString());
                        innerObj.setDays(days.toString());
                        innerObj.setStatus(status.toString());
                        innerObj.setTypeofOD(typeofod.toString());
                        innerObj.setDescription(description.toString());
                        innerObj.setfromOD_date(From_Date.toString().substring(4, From_Date.toString().length() - 5));
                        innerObj.settoOD_date(TodateOD.toString().substring(4, TodateOD.toString().length() - 5));
                        ondutyhistoryclass_arrayObj[i] = innerObj;

                    }

                }//End of for loop
            }
            catch (Throwable t) {
                //Toast.makeText(MainActivity.this, "Request failed: " + t.toString(),
                //    Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
            }
        } catch (Throwable t) {
            Log.e("UnRegister  Error", "> " + t.getMessage());

        }

    }//End of onduty_history method

    private class Holder {
        TextView holder_location;
        TextView holder_noofdays;
        TextView holder_fromdate;
        TextView holder_todate;
        TextView holder_status;
        TextView holder_typeofod;
        TextView holder_description;

    }

    public class CustomAdapter extends BaseAdapter {


        public CustomAdapter() {

            super();
            Log.d("Inside CustomAdapter()", "Inside CustomAdapter()");
        }

        @Override
        public int getCount() {

            String x = Integer.toString(ondutyhistoryclass_arrayObj.length);
            System.out.println("ondutyhistoryclass_arrayObj.length" + x);
            return ondutyhistoryclass_arrayObj.length;
        }

        @Override
        public Object getItem(int position) {
            String x = Integer.toString(position);
            System.out.println("getItem position" + x);
            Log.d("getItem position", "x");
            return ondutyhistoryclass_arrayObj[position];
        }


        @Override
        public long getItemId(int position) {
            String x = Integer.toString(position);
            System.out.println("getItemId position" + x);
            Log.d("getItemId position", x);
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final Holder holder;

            Log.d("CustomAdapter", "position: " + position);

            if (convertView == null) {
                holder = new Holder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.row_item_onduty, parent, false);



                od_history_LL =(LinearLayout)convertView.findViewById(R.id.od_history_LL);
                no_od_history_LL =(LinearLayout)convertView.findViewById(R.id.no_od_history_LL);

                no_od_history_LL.setVisibility(View.GONE);
                holder.holder_location = (TextView) convertView.findViewById(R.id.locationondutyhistory_TV);
                holder.holder_noofdays = (TextView) convertView.findViewById(R.id.noofdaysondutyhistory_TV);  //durationofdays_TV
                holder.holder_status = (TextView) convertView.findViewById(R.id.statusondutyhistory_TV);//Fdate_TV
                holder.holder_typeofod = (TextView) convertView.findViewById(R.id.typeofodondutyhistory_TV);//Tdate_TV
                holder.holder_description = (TextView) convertView.findViewById(R.id.descriptionondutyhistory_TV);//reasonforleave_TV

                holder.holder_fromdate=(TextView) convertView.findViewById(R.id.FdateOD_TV);
                holder.holder_todate=(TextView) convertView.findViewById(R.id.TdateOD_TV);
                Log.d("Inside If convertView", "Inside If convertView");

                convertView.setTag(holder);

            } else {
                holder = (Holder) convertView.getTag();
                Log.d("Inside else convertView", "Inside else convertView");
            }

            OndutyhistoryClass detail = (OndutyhistoryClass) getItem(position);



            if(str_dayscheck.equals("0"))
            {
                od_history_LL.setVisibility(View.GONE);
                no_od_history_LL.setVisibility(View.VISIBLE);
            }
            else
           {

                if (detail != null) {
                    holder.holder_location.setText(detail.getLocation());
                    holder.holder_noofdays.setText(detail.getDays());
                    holder.holder_status.setText(detail.getStatus());
                    holder.holder_typeofod.setText(detail.getTypeofOD());
                    holder.holder_description.setText(detail.getDescription());
                    holder.holder_fromdate.setText(detail.getfromOD_date());
                    holder.holder_todate.setText(detail.gettoOD_date());
                }
            }
            return convertView;

        }//End of custom getView
    }//End of CustomAdapter
}//End of fragment_ondutyhistory
