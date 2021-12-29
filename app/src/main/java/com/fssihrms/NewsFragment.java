package com.fssihrms;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Vector;
import static com.fssihrms.Constant_webservice.URL;
//import hrms.com.hrm.R;

/**
 * Created by User on 6/21/2017.
 */

public class NewsFragment extends Fragment {


    //private ScrollView newsfragmentViewLayout;
    private LinearLayout newsfragmentViewLayout;

    String username,pwd,emp_id,internet_issue="empty";
    Context context;
    Resources resource;

    int noOfobjects = 0;
    int count1;

    long Employeeidlong;
    ImageView Image1;
    byte[] imageInByte;
    Bitmap mIcon11;

    //NewsClass newsObj;

    NewsClass[] newsclass_arrayObj;
    private ListView listView;
    //Holder holder;


    byte[] x;
    Bitmap bitmap1;

    String urldisplay2 = "http://dethrms.cloudapp.net/Uploads/WhatsApp%20Image%202017-06-15%20at%2010.01.09%20AM.jpeg";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        newsfragmentViewLayout=(LinearLayout) inflater.inflate(R.layout.newsfragment, container, false);

        SharedPreferences myprefs= this.getActivity().getSharedPreferences("user", Context.MODE_WORLD_READABLE);
        //Toast.makeText(Instant.this,myprefs.getAll().toString(),Toast.LENGTH_LONG).show();
        username = myprefs.getString("user1", "nothing");
        pwd = myprefs.getString("pwd", "nothing");
        emp_id = myprefs.getString("emp_id", "nothing");
        context =newsfragmentViewLayout.getContext();

        listView = (ListView) newsfragmentViewLayout.findViewById(R.id.customlistview_news);


        Employeeidlong = Long.parseLong(emp_id); // for web service

      //  Image1 =(ImageView) newsfragmentViewLayout.findViewById(R.id.image1);


        resource = context.getResources();
        AsyncCallWS2 task = new AsyncCallWS2(context);
        task.execute();


        return newsfragmentViewLayout;

    }//// end of onCreate


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

            NewsandEvents();  // call of details


            /*String urldisplay = "http://dethrms.cloudapp.net/Uploads/WhatsApp%20Image%202017-06-15%20at%2010.01.09%20AM.jpeg";
             mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }*/


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

            /*if (leavelist != null) {
                Leavehistoryfragment.CustomAdapter adapter = new Leavehistoryfragment.CustomAdapter();
                listView.setAdapter(adapter);

                int x = leavelist.length;

                System.out.println("Inside the if list adapter" + x);
            } else {
                Log.d("onPostExecute", "leavelist == null");
            }
*/

           // Image1.setImageBitmap(mIcon11);

           /* byte[] x=newsObj.NewsImage;
            Bitmap bitmap = BitmapFactory.decodeByteArray(x, 0, x.length);
            Image1.setImageBitmap(bitmap);*/

            System.out.println("Reached the onPostExecute");

            if (newsclass_arrayObj != null) {
                CustomAdapter adapter = new CustomAdapter();
                listView.setAdapter(adapter);

                int x1 = newsclass_arrayObj.length;

                System.out.println("Inside the if list adapter" + x1);
            } else {
                Log.d("onPostExecute", "leavelist == null");
            }






        }//end of onPostExecute
    }// end Async task




    public void NewsandEvents()
    {
        Vector<SoapObject> result1 = null;

        //dethrms
       // String URL="http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";
        String METHOD_NAME = "GetDailyNewsDisplay";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/GetDailyNewsDisplay";
        //dethrms

        try {
            // String  versioncode = this.getPackageManager()
            //        .getPackageInfo(this.getPackageName(), 0).versionName;
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("id", Employeeidlong);
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
                Log.i("value at news response", response.toString());
                count1 = response.getPropertyCount();  // number of count in array in response 6,0-5,5

                Log.i("number news rows", "" + count1);

                noOfobjects = count1;


                System.out.println("Number news object" + noOfobjects);
               /* {
                    URL url;
                    url = new URL("http://dethrms.cloudapp.net/Uploads/WhatsApp%20Image%202017-06-15%20at%2010.01.09%20AM.jpeg");
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    Image1.setImageBitmap(bmp);
                }*/

               /* SoapObject list = (SoapObject) response.getProperty(0);
                SoapPrimitive NewsTitle, Types,NewsImage;

                NewsTitle=(SoapPrimitive) list.getProperty("News_Title");
                Types =(SoapPrimitive) list.getProperty("Type");*/


                //http://dethrms.cloudapp.net/Uploads/WhatsApp%20Image%202017-06-15%20at%2010.01.09%20AM.jpeg


               /* newsObj = new NewsClass();

                NewsClass obj1 = new NewsClass();
                obj1.setNewsTitle("Hi");
                obj1.setTypes("News");

                String urldisplay = "http://dethrms.cloudapp.net/Uploads/WhatsApp%20Image%202017-06-15%20at%2010.01.09%20AM.jpeg";
*/
               // String urldisplay = "http://dethrms.cloudapp.net/Uploads/ramdan.png";


               /* mIcon11 = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mIcon11.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                imageInByte = stream.toByteArray();
                obj1.setNewsImage (imageInByte);
                newsObj=obj1;*/

/*
<NewsID>0</NewsID>
 <News_Title>Celebration</News_Title>
 <News_Details></News_Details>
 <View_Role>All</View_Role>
  <image_path>http://dethrms.cloudapp.net/Uploads/WhatsApp Image 2017-06-15 at 10.01.09 AM.jpeg</image_path>
  <Type>News</Type>
*/




                newsclass_arrayObj= new NewsClass[count1];
                for (int i = 0; i < count1; i++) {
                    SoapObject list1 = (SoapObject) response.getProperty(i);
                    SoapPrimitive NewsTitle, Types,NewsImage,NewsDetails;
                    String urlpathnews;

                    NewsTitle=(SoapPrimitive) list1.getProperty("News_Title");
                    NewsDetails=(SoapPrimitive)list1.getProperty("News_Details");
                    Types =(SoapPrimitive) list1.getProperty("Type");
                    //NewsImage=(SoapPrimitive) list1.getProperty("image_path");
                    urlpathnews=(String) list1.getProperty("image_path").toString();

                    NewsClass innerObj = new NewsClass();
                    innerObj.setNewsTitle(NewsTitle.toString());
                    innerObj.setNewsDetails(NewsDetails.toString());
                    innerObj.setTypes("news");


                   // String urlImage=NewsImage.toString();

                    String urlImage="";
                    urlImage  =urlpathnews.toString();

                    System.out.println("urlImage"+urlImage);
                    //http://dethrms.cloudapp.net/Uploads/WhatsApp Image 2017-06-15 at 10.01.09 AM.jpeg
                   // mIcon11 = null;

                    if (urlImage.equals("anyType{}"))
                    {
                        imageInByte = null;
                    }

                    else

                    {

                        urlImage = urlImage.replaceAll(" ", "%20").toString();


                        try {
                            InputStream in = new java.net.URL(urlImage).openStream();
                            mIcon11 = BitmapFactory.decodeStream(in);
                            Log.e("Error Image conversion", "e.getMessage()");
                        } catch (Exception e) {
                            Log.e("Error Image conversion", e.getMessage());
                            e.printStackTrace();
                        }


                        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                        mIcon11.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
                        imageInByte = stream1.toByteArray();


                        innerObj.setNewsImage(imageInByte);
                    }

                    newsclass_arrayObj[i] = innerObj;



                }//end of for loop




                // leavelist[0].setName("kya");
                /*leavelist = new LeaveDetail[count1];

                for (int i = 0; i < co
                unt1; i++) {
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

                    project.setFrom_DateC(From_Date.toString());
                    project.setTo_DateC(To_Date.toString());
                    //   leavelist[i].setApproved_On(Approved_On.toString());
                    leavelist[i] = project;

                }// End of for loop

*/                //   version = (SoapPrimitive)response.getProperty("AppVersion");
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

    }//end of newsandEvents


    //Holder for customAdapter
    private class Holder {
        TextView holder_newstitle;
        TextView holder_newsdetails;
        ImageView holder_image;
        TextView holder_types;
    }
    //End of Holder

    public class CustomAdapter extends BaseAdapter {


        public CustomAdapter() {

            super();
            Log.d("Inside CustomAdapter()", "Inside CustomAdapter()");
        }

        @Override
        public int getCount() {

            String x = Integer.toString(newsclass_arrayObj.length);
            Log.d("class_arrayObj.length", x);
            return newsclass_arrayObj.length;

        }

        @Override
        public Object getItem(int position) {
            String x = Integer.toString(position);
            System.out.println("getItem position" + x);
            Log.d("getItem position", "x");
            return newsclass_arrayObj[position];
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
           // holder = new Holder();
            if (convertView == null) {
                holder = new Holder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.row_item_news, parent, false);

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







                holder.holder_newstitle=(TextView) convertView.findViewById(R.id.newstitle_TV);
                holder.holder_newsdetails=(TextView)convertView.findViewById(R.id.newsdetails_TV);
                holder.holder_image=(ImageView) convertView.findViewById(R.id.newsimagedisplay_IV);

                Log.d("Inside If convertView", "Inside If convertView");

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
                Log.d("Inside else convertView", "Inside else convertView");
            }

            //LeaveDetail detail = (LeaveDetail) getItem(position);

            NewsClass detail =(NewsClass) getItem(position);


            if(detail!=null) {

                System.out.println("" + detail.getNewsTitle());
                holder.holder_newstitle.setText(detail.getNewsTitle());
                holder.holder_newsdetails.setText(Html.fromHtml(detail.getNewsDetails()));


                x = detail.getNewsImage();

                if(x!=null) {
                    holder.holder_image.setVisibility(View.VISIBLE);
                bitmap1 = BitmapFactory.decodeByteArray(x, 0, x.length);
                holder.holder_image.setImageBitmap(bitmap1);

            } else{
                    holder.holder_image.setVisibility(View.GONE);
                }

            }
            else{
                holder.holder_newstitle.setText("There are No News Today");
            }




            return convertView;


        }


    }//End of CustomAdapter



}// end of class
