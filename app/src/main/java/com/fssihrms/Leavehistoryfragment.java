package com.fssihrms;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.Vector;
import static com.fssihrms.Constant_webservice.URL;

//import hrms.com.hrm.R;

/**
 * Created by User on 6/13/2017.
 */

public class Leavehistoryfragment extends Fragment {




    private LinearLayout linearLayout;

    LeaveDetail[] leavelist;
    Context context;
    Resources resource;
    private ListView listView;

    Holder holder;
    int count1;
    int noOfobjects = 0;
    String username,pwd;

    TextView durationlabel_tv,datelabel_tv,statuslabel_tv,inchargelabel_tv,reasonlabel_tv;
    String str_dayscheck;

    LinearLayout leavehistory_LL,noleavehistory_LL;

    String leaveID,emp_id;
    long leaveidlong;
    long leavidlong_delete;

    LeaveDetail detail;

    String str_leaveid;

    LinearLayout linearLayoutmain;

    public Leavehistoryfragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {


        linearLayout=(LinearLayout) inflater.inflate(R.layout.leavehistoryfragment, container, false);

        linearLayoutmain = (LinearLayout) linearLayout.findViewById(R.id.leavehistorymain_LL);

        SharedPreferences myprefs = this.getActivity().getSharedPreferences("user", Context.MODE_WORLD_READABLE);
        username = myprefs.getString("user1", "nothing");
        pwd = myprefs.getString("pwd", "nothing");
        emp_id = myprefs.getString("emp_id", "nothing");
       // Employeeidlong = Long.parseLong(emp_id); // for web service


        listView = (ListView) linearLayout.findViewById(R.id.customlistview1);


      context = linearLayout.getContext();
        resource = context.getResources();

        AsyncCallWS2 task = new AsyncCallWS2(context);
        task.execute();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                /*String leaveidString = leavelist[position].getLeave_Id();

                leaveidlong = Long.parseLong(leaveidString);
                //Toast.makeText(getActivity(),"LeaveID:"+leaveidlong,Toast.LENGTH_LONG).show();

                final Dialog dialog = new Dialog(getActivity());



                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.leaverollback_dialog);
                dialog.setCancelable(false);
                Button dialogokbutton = (Button) dialog.findViewById(R.id.rollback_BT);
                Button dialogcancelbutton =(Button) dialog.findViewById(R.id.cancelrollback_BT);

                dialogokbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(getActivity(),"Request sent to your reporting manager please wait for approval",Toast.LENGTH_LONG).show();
                        //   Toast.makeText(getActivity(),"Request sent",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                      //  Toast.makeText(getActivity(),"Leave ID:"+leaveID,Toast.LENGTH_LONG).show();
                        AsyncCalltoleaverollback task = new AsyncCalltoleaverollback(context);
                        task.execute();
                    }
                });

                dialogcancelbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();*/

            }
        });

        return linearLayout;

    }// end of onCreate
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

            if (leavelist != null) {
                CustomAdapter adapter = new CustomAdapter();
                listView.setAdapter(adapter);

                int x = leavelist.length;

                //System.out.println("Inside the if list adapter" + x);
            } else {
                Log.d("onPostExecute", "leavelist == null");
            }

            System.out.println("Reached the onPostExecute");

        }//end of onPostExecute
    }// end Async task



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
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/GetEmployeeDetails";*/

        //dftestbed ends

        //dethrms
       // String URL="http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";
        String METHOD_NAME = "GetEmployeeDetails";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/GetEmployeeDetails";
        //dethrms

        try {
            // String  versioncode = this.getPackageManager()
            //        .getPackageInfo(this.getPackageName(), 0).versionName;
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("email", username);
           // request.addProperty("email", "amrit.tech@dfmail.org");
            //request.addProperty("email", "raghavendra.tech@dfmail.org");
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
                Log.i("value at response", response.toString());
                count1 = response.getPropertyCount();  // number of count in array in response 6,0-5,5

                Log.i("number of rows", "" + count1);

                noOfobjects = count1;


                System.out.println("Number of object" + noOfobjects);
                // leavelist[0].setName("kya");
                leavelist = new LeaveDetail[count1];

                for (int i = 0; i < count1; i++) {
                    SoapObject list = (SoapObject) response.getProperty(i);
                    SoapPrimitive no_of_Days, Leave_Id, Name, email, Program, Employee_Id, Opening_Balance, AvailedLeaves, manager_email, From_Date, To_Date, Reason, LEave_Status, Approved_On,RecallStatus;
                    String approved = "";

                    str_dayscheck = (String) list.getProperty("No_of_Days").toString();

                    if(str_dayscheck.equals("0"))
                    {
                        //employee is fresher
                    }
                    else
                    {

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
                        RecallStatus = (SoapPrimitive) list.getProperty("Recall_Status");
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

                        project.setRecallStatus(RecallStatus.toString());

                        project.setFrom_DateC(From_Date.toString());
                        project.setTo_DateC(To_Date.toString());
                        //   leavelist[i].setApproved_On(Approved_On.toString());
                        leavelist[i] = project;

                    }


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


    //Holder for customAdapter
    private class Holder {
        TextView holder_fromView;
        TextView holder_toView;
        TextView date1;
        TextView status;
        TextView days;
        TextView reason;
        TextView incharge;
        TextView holder_rollback;
        TextView holder_leaveid;
        TextView holder_recallstatus;
        Button holder_rollback_BT;
        Button holder_deleteleave_BT;
    }
    //End of Holder

    public class CustomAdapter extends BaseAdapter {


        public CustomAdapter() {

            super();
            Log.d("Inside CustomAdapter()", "Inside CustomAdapter()");
        }

        @Override
        public int getCount() {

            String x = Integer.toString(leavelist.length);
            Log.d("leavelist.length", x);
            return leavelist.length;

        }

        @Override
        public Object getItem(int position) {
            String x = Integer.toString(position);
            System.out.println("getItem position" + x);
            Log.d("getItem position", "x");
            return leavelist[position];
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.row_item_new, parent, false);

               /* holder.date = (TextView) convertView.findViewById(R.id.approved_on2);
                holder.days = (TextView) convertView.findViewById(R.id.days2);  //durationofdays_TV
                holder.fromView = (TextView) convertView.findViewById(R.id.from2);//Fdate_TV
                holder.toView = (TextView) convertView.findViewById(R.id.to2);//Tdate_TV
                holder.reason = (TextView) convertView.findViewById(R.id.reason2);//
                holder.status = (TextView) convertView.findViewById(R.id.status2);//statusofleave_TV
                */

               // holder.cancel = (TextView) convertView.findViewById(R.id.cancelLeave_TV);

                //Typeface facebold = Typeface.createFromAsset(getResources().getAssets(), "fonts/centurygothic.ttf");
                //durationlabel_TV
                leavehistory_LL =(LinearLayout)convertView.findViewById(R.id.leavehistory_LL);
                noleavehistory_LL =(LinearLayout)convertView.findViewById(R.id.noleavehistory_LL);
                durationlabel_tv=(TextView)convertView.findViewById(R.id.durationlabel_TV);
                statuslabel_tv=(TextView)convertView.findViewById(R.id. statuslabel_TV);
                //inchargelabel_tv=(TextView)convertView.findViewById(R.id. inchargelabel_TV);
                reasonlabel_tv= (TextView)convertView.findViewById(R.id. reasonlabel_TV);
                datelabel_tv=(TextView)convertView.findViewById(R.id.datelabel_TV);
                font_change_row_item_new();



                TextView d =(TextView) convertView.findViewById(R.id.leavestatus_TV);

              //  holder.date = (TextView) convertView.findViewById(R.id.approvedOn_TV);//approvedOn_TV
                holder.date1 =d;
                holder.days = (TextView) convertView.findViewById(R.id.durationofdays_TV);  //durationofdays_TV
                holder.holder_fromView = (TextView) convertView.findViewById(R.id.Fdate_TV);//Fdate_TV
                holder.holder_toView = (TextView) convertView.findViewById(R.id.Tdate_TV);//Tdate_TV
                holder.reason = (TextView) convertView.findViewById(R.id.reasonforleave_TV);//reasonforleave_TV
               // holder.status = (TextView) convertView.findViewById(R.id.statusofleave_TV);//statusofleave_TV
               // holder.incharge=(TextView) convertView.findViewById(R.id.inchargename_TV);
               // holder.holder_rollback=(TextView) convertView.findViewById(R.id.cancelLeaves_TV);
                holder.holder_rollback_BT=(Button)convertView.findViewById(R.id.cancelLeaves_BT);
                holder.holder_deleteleave_BT=(Button)convertView.findViewById(R.id.deleteLeaves_BT);

                holder.holder_leaveid =(TextView)convertView.findViewById(R.id.leaveid_TV);
                holder.holder_recallstatus =(TextView)convertView.findViewById(R.id.recallstatus_TV);




                holder.holder_rollback_BT.setVisibility(convertView.INVISIBLE);
                holder.holder_deleteleave_BT.setVisibility(convertView.GONE);

                leavehistory_LL.setVisibility(View.VISIBLE);


                Log.d("Inside If convertView", "Inside If convertView");

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
                Log.d("Inside else convertView", "Inside else convertView");
            }

            detail = (LeaveDetail) getItem(position);

        if(str_dayscheck.equals("0"))
        {
            leavehistory_LL.setVisibility(View.GONE);
            noleavehistory_LL.setVisibility(View.VISIBLE);
        }
        else
         {
            if (detail != null) {
                holder.holder_fromView.setText(detail.getFrom_Date());
                holder.holder_toView.setText(detail.getTo_Date());
                holder.days.setText(detail.getno_days());
                holder.date1.setText(detail.getLEave_Status());//Unapproved
                holder.reason.setText(detail.getReason());
                holder.holder_recallstatus.setText(detail.getRecallStatus());

                holder.holder_leaveid.setText(detail.getLeave_Id());

                // holder.incharge.setText("Incharge");


                String formateTodate = "";// in format 05 Mar 2017
                formateTodate = detail.getTo_DateC().toString();
                formateTodate = formateTodate.substring(5).trim();
                String formateTodate_changed = Dateformatechange(formateTodate);

                leaveID = detail.getLeave_Id().toString();
                leaveidlong = Long.parseLong(leaveID);



                if (compareEndDate2CurrentDate(formateTodate_changed)&& holder.holder_recallstatus.getText().equals("0")&& holder.date1.getText().equals("Approved"))
                {

                    holder.holder_rollback_BT.setVisibility(convertView.VISIBLE);
                    holder.holder_deleteleave_BT.setVisibility(convertView.GONE);

                    holder.holder_rollback_BT.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            //Toast.makeText(getActivity(), ""+holder.holder_leaveid.getText().toString(), Toast.LENGTH_SHORT).show();

                            leaveidlong = Long.parseLong(holder.holder_leaveid.getText().toString());
                            final Dialog dialog = new Dialog(getActivity());
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.leaverollback_dialog);
                            dialog.setCancelable(false);
                            Button dialogokbutton = (Button) dialog.findViewById(R.id.rollback_BT);
                            Button dialogcancelbutton =(Button) dialog.findViewById(R.id.cancelrollback_BT);


                            dialogokbutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    //Toast.makeText(getActivity(),"Request sent to your reporting manager please wait for approval",Toast.LENGTH_LONG).show();
                                    //   Toast.makeText(getActivity(),"Request sent",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    //  Toast.makeText(getActivity(),"Leave ID:"+leaveID,Toast.LENGTH_LONG).show();
                                    AsyncCalltoleaverollback task = new AsyncCalltoleaverollback(context);
                                    task.execute();
                                }
                            });

                            dialogcancelbutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();

                        }
                    });



                    // leaveID= detail.getLeave_Id().toString();

                    // Log.d("leaveID",leaveID.toString());
                    // leaveidlong = Long.parseLong(leaveID);

         /*           holder.holder_rollback.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           // Toast.makeText(getActivity(), ""+leaveID, Toast.LENGTH_SHORT).show();
                            final Dialog dialog = new Dialog(getActivity());



                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.leaverollback_dialog);
                            dialog.setCancelable(false);
                            Button dialogokbutton = (Button) dialog.findViewById(R.id.rollback_BT);
                            Button dialogcancelbutton =(Button) dialog.findViewById(R.id.cancelrollback_BT);

                            dialogokbutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    //Toast.makeText(getActivity(),"Request sent to your reporting manager please wait for approval",Toast.LENGTH_LONG).show();
                                    //   Toast.makeText(getActivity(),"Request sent",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(),"Leave ID:"+leaveID,Toast.LENGTH_LONG).show();
                                    AsyncCalltoleaverollback task = new AsyncCalltoleaverollback(context);
                                    task.execute();
                                }
                            });

                            dialogcancelbutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();

                        }//end of onClick(View v)
                    });*/


                }//End of 1st innerIf
                else if(compareEndDate2CurrentDate(formateTodate_changed)&& holder.holder_recallstatus.getText().equals("0")&& holder.date1.getText().equals("Unapproved"))
                {


                    holder.holder_rollback_BT.setVisibility(convertView.GONE);
                    holder.holder_deleteleave_BT.setVisibility(convertView.VISIBLE);

                    holder.holder_deleteleave_BT.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                           // Toast.makeText(getActivity(), ""+holder.holder_leaveid.getText().toString(), Toast.LENGTH_SHORT).show();

                            leavidlong_delete = Long.parseLong(holder.holder_leaveid.getText().toString());

                            final Dialog dialog = new Dialog(getActivity());
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.leavedelete_dialog);
                            dialog.setCancelable(false);
                            Button dialogokbutton = (Button) dialog.findViewById(R.id.deleteleave_BT);
                            Button dialogcancelbutton =(Button) dialog.findViewById(R.id.canceldeleteleave_BT);


                            dialogokbutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    //Toast.makeText(getActivity(),"Request sent to your reporting manager please wait for approval",Toast.LENGTH_LONG).show();
                                    //   Toast.makeText(getActivity(),"Request sent",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    //  Toast.makeText(getActivity(),"Leave ID:"+leaveID,Toast.LENGTH_LONG).show();

                                    AsyncCall_LeaveDelete task = new AsyncCall_LeaveDelete(context);
                                    task.execute();
                                }
                            });

                            dialogcancelbutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();

                        }
                    });



                }

                    else {
                    holder.holder_rollback_BT.setVisibility(convertView.GONE);
                    holder.holder_deleteleave_BT.setVisibility(convertView.GONE);
                }


                /* leaveID= detail.getLeave_Id().toString();

                System.out.println("TodateC"+detail.getTo_DateC().toString());

                System.out.println("leaveNo"+leaveID);
                if(leaveID.equals("4054")) {

                    System.out.println("leaveNo Inside"+leaveID);
                    holder.holder_rollback.setVisibility(convertView.VISIBLE);

                }else
                {
                    holder.holder_rollback.setVisibility(convertView.INVISIBLE);
                }

                holder.reason.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), leaveID, Toast.LENGTH_SHORT).show();
                    }
                });
                */

            }// end of if

        }


/*
            leaveID= detail.getLeave_Id().toString();

            System.out.println("TodateC"+detail.getTo_DateC().toString());

            System.out.println("leaveNo"+leaveID);
            if(leaveID.equals("4054")) {

                System.out.println("leaveNo Inside"+leaveID);
                holder.holder_rollback.setVisibility(convertView.VISIBLE);

            }else
            {
                holder.holder_rollback.setVisibility(convertView.INVISIBLE);
            }

            holder.reason.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), leaveID, Toast.LENGTH_SHORT).show();
                }
            });*/


            //holder.status.setText(detail.getLEave_Status());

            /*String formateTodate = "";// in format 05 Mar 2017
            formateTodate = detail.getTo_DateC();
            formateTodate = formateTodate.substring(5).trim();*/


           /* String formateTodate_changed = Dateformatechange(formateTodate);

            System.out.println("detail.getTo_DateC():" + formateTodate_changed);


            if (compareEndDate2CurrentDate(formateTodate_changed)) {

                holder.cancel.setText("Cancel");
                System.out.println("compareEndDate2CurrentDateCancelText:" + formateTodate_changed);
            }*/

            // holder.cancel.setText("Cancel");


            return convertView;


        }


    }//End of CustomAdapter



    public void font_change_row_item_new()
    {
        Typeface facebold = Typeface.createFromAsset(getResources().getAssets(), "fonts/centurygothic.ttf");
        durationlabel_tv.setTypeface(facebold);
        statuslabel_tv.setTypeface(facebold);
       // inchargelabel_tv.setTypeface(facebold);
        reasonlabel_tv.setTypeface(facebold);

    }



    public String Dateformatechange(String receviedDate) {
        //DateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy"); // for parsing input
        DateFormat df1 = new SimpleDateFormat("dd MMM yyyy"); // for parsing input
        DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");  // for formatting output
        //String inputDate = "03 mar 2016";
        String inputDate = receviedDate;
        Date d = null;
        try {
            d = (Date) df1.parse(inputDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String outputDate = df2.format(d); // => "30/03/2016"
        System.out.println("output" + outputDate);

        return outputDate;
    }// end of DateformatChange


    public Boolean compareEndDate2CurrentDate(String receviedTodate) {

        String receviedTodateWithAdd = "";
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendarObj = null;
        calendarObj = Calendar.getInstance();
        try {
            calendarObj.setTime(sdf1.parse(receviedTodate));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        calendarObj.add(Calendar.DATE, 8);
        //sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date resultdate = new Date(calendarObj.getTimeInMillis());
        receviedTodateWithAdd = sdf1.format(resultdate);
        //System.out.println("receviedTodateWithAddCancel:" + receviedTodateWithAdd);


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String pattern = "dd/MM/yyyy";
        String currentDate2compareTodate = "";
        currentDate2compareTodate = sdf.format(new Date());
       // System.out.println("currentDateInformat:" + currentDate2compareTodate);

        Date date4 = null;
        Date date5 = null;

        SimpleDateFormat sdformat1 = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date4 = sdformat1.parse(receviedTodateWithAdd);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            date5 = sdformat1.parse(currentDate2compareTodate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (date4.compareTo(date5) >= 0) {
            //System.out.println("date4 is greater than/equal to date5"+date4 +"###"+date5);
            return true;

        } else {
            //System.out.println("Date4 is less than to Date5"+date4+"###"+date5);\
            return false;
        }


    }// End of comapareEndDate2currentDate


    private class AsyncCalltoleaverollback extends AsyncTask<String, Void, Void>
    {

        String response_AsyncCalltoleaverollback="";

        ProgressDialog dialog;



        Context context;

        protected void onPreExecute() {
            //  Log.i(TAG, "onPreExecute---tab2");

            String wait="Please wait..";
            SpannableString ss2=  new SpannableString(wait);
            //ss2.setSpan(new ForegroundColorSpan(#00635a), 0, ss2.length(), 0);



            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f2f2f2")));

            ss2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.greenTextcolor)), 0, ss2.length(), 0);
            dialog.setMessage(ss2);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //Log.i(TAG, "onProgressUpdate---tab2");
        }


        @Override
        protected Void doInBackground(String... params) {
            Log.i("df leaverollback", "doInBackground");
            //  GetAllEvents();
            // call of the leavecancelwebservice
            if(2>1)
            {

                //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";

                //http://dftestbed.cloudapp.net/PMSservice.asmx  //list of webservice
                //http://dftestbed.cloudapp.net/PMSservice.asmx?op=LeaveCancelRequest  //contains detailas of LeaveCancelRequest

               /* String URL ="http://dftestbed.cloudapp.net/PMSservice.asmx?WSDL";  // WSDL file
                String METHOD_NAME = "LeaveCancelRequest";//leave
                String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/LeaveCancelRequest";
*/
               // String URL ="http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";
                String METHOD_NAME = "LeaveCancelRequest";//leave
                String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/LeaveCancelRequest";

                try{

                    SoapObject request = new SoapObject(Namespace, METHOD_NAME);
                    request.addProperty("id", leaveidlong);   // <id>long</id>
                    //request.addProperty("id", 4015);

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    //Set output SOAP object
                    envelope.setOutputSoapObject(request);
                    //Create HTTP call object
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                    try
                    {

                        androidHttpTransport.call(SOAPACTION, envelope);
                        SoapPrimitive  response = (SoapPrimitive ) envelope.getResponse();

                        response_AsyncCalltoleaverollback=response.toString();
                       // System.out.println("leaveid in long:"+leaveidlong);
                       // System.out.println("cancel response:"+response_AsyncCalltoleaverollback);
                        Log.d("response leavecancel",response.toString());
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

        public AsyncCalltoleaverollback(Context context1) {
            context = context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected void onPostExecute(Void result) {

           /* if ((this.dialog != null) && this.dialog.isShowing()) {
                dialog.dismiss();

            }*/

            dialog.dismiss();


            if(response_AsyncCalltoleaverollback.equals("Request sent"))
            {
                Toast.makeText(getActivity(),"LeaveRoll back,sent to your Functional Head",Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(),HomeActivity.class);
                startActivity(i);
            }
            else
            {
                Toast.makeText(getActivity(),"Server busy,Please try later...",Toast.LENGTH_LONG).show();
            }



        }
    }//End of AsyncCalltoleaverollback







    private class AsyncCall_LeaveDelete extends AsyncTask<String, Void, Void>
    {

        String response_AsyncCall_LeaveDelete="";

        ProgressDialog dialog;



        Context context;

        protected void onPreExecute() {
            //  Log.i(TAG, "onPreExecute---tab2");

            String wait="Please wait..";
            SpannableString ss2=  new SpannableString(wait);
            //ss2.setSpan(new ForegroundColorSpan(#00635a), 0, ss2.length(), 0);



            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f2f2f2")));

            ss2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.greenTextcolor)), 0, ss2.length(), 0);
            dialog.setMessage(ss2);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //Log.i(TAG, "onProgressUpdate---tab2");
        }


        @Override
        protected Void doInBackground(String... params) {
            Log.i("df leavedelete", "doInBackground");
            //  GetAllEvents();
            // call of the leavecancelwebservice
            if(2>1)
            {


                String METHOD_NAME = "LeaveDeleteRequest";//leave
                String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/LeaveDeleteRequest";

                try{

                    SoapObject request = new SoapObject(Namespace, METHOD_NAME);
                    request.addProperty("id", leavidlong_delete);   // <id>long</id>
                    //request.addProperty("id", 4015);

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    //Set output SOAP object
                    envelope.setOutputSoapObject(request);
                    //Create HTTP call object
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                    try
                    {

                        androidHttpTransport.call(SOAPACTION, envelope);
                        SoapPrimitive  response = (SoapPrimitive ) envelope.getResponse();

                        response_AsyncCall_LeaveDelete=response.toString();
                        // System.out.println("leaveid in long:"+leaveidlong);
                        // System.out.println("cancel response:"+response_AsyncCall_LeaveDelete);
                        Log.e("response leavedelete",response.toString());
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

        public AsyncCall_LeaveDelete(Context context1) {
            context = context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected void onPostExecute(Void result) {

           /* if ((this.dialog != null) && this.dialog.isShowing()) {
                dialog.dismiss();

            }*/

            dialog.dismiss();


            if(response_AsyncCall_LeaveDelete.equals("Leave Deleted Successfully"))
            {
                Toast.makeText(getActivity(),"Leave Deleted Successfully ",Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(),HomeActivity.class);
                startActivity(i);
            }
            else
            {
                Toast.makeText(getActivity(),"Server busy,Please try later...",Toast.LENGTH_LONG).show();
            }



        }
    }//End of AsyncCall_LeaveDelete
















}// End of class
