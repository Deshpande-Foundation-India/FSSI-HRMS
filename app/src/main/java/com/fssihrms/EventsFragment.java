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

public class EventsFragment  extends Fragment {

    private LinearLayout layout_eventsfragment;

    String username,pwd,emp_id,internet_issue="empty";
    Context context;
    Resources resource;

    int noOfobjects = 0;
    int count1;

    long Employeeidlong;
    private ListView listView;

    EventsClass[] eventsclass_arrayObj;

    byte[] imageInByte;
    Bitmap mIcon11;
    byte[] x;
    Bitmap bitmap1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        layout_eventsfragment=(LinearLayout) inflater.inflate(R.layout.fragment_events, container, false);

        SharedPreferences myprefs= this.getActivity().getSharedPreferences("user", Context.MODE_WORLD_READABLE);
        //Toast.makeText(Instant.this,myprefs.getAll().toString(),Toast.LENGTH_LONG).show();
        username = myprefs.getString("user1", "nothing");
        pwd = myprefs.getString("pwd", "nothing");
        emp_id = myprefs.getString("emp_id", "nothing");
        context =layout_eventsfragment.getContext();

        listView = (ListView) layout_eventsfragment.findViewById(R.id.customlistview_events);

        Employeeidlong = Long.parseLong(emp_id); // for web service

        resource = context.getResources();
        AsyncCallWS2 task = new AsyncCallWS2(context);
        task.execute();

        return layout_eventsfragment;

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
            //  GetAllEvents();

            Events();  // call of details


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

            if (eventsclass_arrayObj!= null) {
               CustomAdapter adapter = new CustomAdapter();
                listView.setAdapter(adapter);

                int x1 = 0;
                x1 =  eventsclass_arrayObj.length;

                System.out.println("Inside the if list adapter" + x1);
            } else {
                Log.d("onPostExecute", "leavelist == null");
            }

        }//end of onPostExecute
    }// end Async task

    public void Events()
    {
        Vector<SoapObject> result1 = null;

        //dethrms
       // String URL="http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";
        String METHOD_NAME = "GetAnnouncementContent";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/GetAnnouncementContent";
        //dethrms

        try {
            // String  versioncode = this.getPackageManager()
            //        .getPackageInfo(this.getPackageName(), 0).versionName;
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("id", Employeeidlong);

            System.out.println("Employeeidlong"+Employeeidlong);
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
                Log.i("value at event response", response.toString());
                count1 = response.getPropertyCount();  // number of count in array in response 6,0-5,5

                Log.i("number event rows", "" + count1);

                noOfobjects = count1;


                System.out.println("Number event object" + noOfobjects);
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

/*<NewsID>0</NewsID>
 <News_Title></News_Title>
  <News_Details></News_Details>
  <View_Role>All</View_Role>
  <image_path>http://dethrms.cloudapp.net/Uploads/ramdan.png</image_path>
    <Type>Event</Type>*/




                eventsclass_arrayObj= new EventsClass[count1];
                for (int i = 0; i < count1; i++) {
                    SoapObject list1 = (SoapObject) response.getProperty(i);
                    SoapPrimitive EventsTitle, Types, EventImage, EventsDetails;
                    String urlpath;

                    EventsTitle = (SoapPrimitive) list1.getProperty("News_Title");
                    EventsDetails = (SoapPrimitive) list1.getProperty("News_Details");
                    //Types =(SoapPrimitive) list1.getProperty("Type");
                   // EventImage = (SoapPrimitive) list1.getProperty("image_path");
                    urlpath=(String) list1.getProperty("image_path").toString();

                    EventsClass innerObj = new EventsClass();


                    innerObj.setEventsTitle(EventsTitle.toString());
                    innerObj.setEventsDetails(EventsDetails.toString());


                    //String urlImage = EventImage.toString().trim();

                    String urlImage="";
                    urlImage  = urlpath.toString().trim();



                    System.out.println("urlImage"+urlImage);
                    //http://dethrms.cloudapp.net/Uploads/WhatsApp Image 2017-06-15 at 10.01.09 AM.jpeg
                    // mIcon11 = null;

                    //anyType{};

                    //if(urlImage!=null || !urlImage.isEmpty()) {
                    if (urlImage.equals("anyType{}"))
                    {
                        imageInByte = null;
                    } else

                        {

                        Log.d("image path",urlImage.toString());
                        urlImage = urlImage.replaceAll(" ", "%20").toString();

                        Log.d("urlimage%20", urlImage.toString());

                        // System.out.println("Title"+EventsTitle.toString());
                        // System.out.println("Descrpition"+EventsDetails.toString());


                        try {
                            InputStream in = new java.net.URL(urlImage).openStream();
                            mIcon11 = BitmapFactory.decodeStream(in);
                            Log.e("No Error Image conv", "e.getMessage()");
                        } catch (Exception e) {
                            Log.e("Error Image conversion", e.getMessage());
                            e.printStackTrace();
                        }


                        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                        mIcon11.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
                        imageInByte = stream1.toByteArray();


                        innerObj.setEventsImage(imageInByte);
                    }


                    eventsclass_arrayObj[i] = innerObj;



                }//end of for loop



            } catch (Throwable t) {

                Log.e("request fail", "> " + t.getMessage());
            }
        } catch (Throwable t) {
            Log.e("UnRegister  Error", "> " + t.getMessage());

        }

    }//end of newsandEvents

    //Holder for customAdapter
    private class Holder {
        TextView holder_eventstitle;
        TextView holder_eventsdetails;
        ImageView holder_image;

    }
    //End of Holder

    public class CustomAdapter extends BaseAdapter {


        public CustomAdapter() {

            super();
            Log.d("Inside CustomAdapter()", "Inside CustomAdapter()");
        }

        @Override
        public int getCount() {

            String x = Integer.toString(eventsclass_arrayObj.length);
            Log.d("eventsclass.length", x);
            return eventsclass_arrayObj.length;
        }

        @Override
        public Object getItem(int position) {
            String x = Integer.toString(position);
            //System.out.println("getItem position" + x);
            Log.d("getItem position", "x");
            return eventsclass_arrayObj[position];
        }


        @Override
        public long getItemId(int position) {
            String x = Integer.toString(position);
            //System.out.println("getItemId position" + x);
            Log.d("getItemId position", x);
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final Holder holder;

            Log.d("CustomAdapter", "position: " + position);

            if (convertView == null) {
                holder = new Holder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.row_item_events, parent, false);

                holder.holder_eventstitle = (TextView) convertView.findViewById(R.id.eventstitle_TV);
                holder.holder_eventsdetails = (TextView) convertView.findViewById(R.id.eventsdetails_TV);
                holder.holder_image = (ImageView) convertView.findViewById(R.id.eventsimagedisplay_IV);
                Log.d("Inside If convertView", "Inside If convertView");

                convertView.setTag(holder);

            } else {
                holder = (Holder) convertView.getTag();
                Log.d("Inside else convertView", "Inside else convertView");
            }

            EventsClass detail = (EventsClass) getItem(position);


            if(detail!=null) {

            //System.out.println("" + detail.getEventsTitle());
            holder.holder_eventstitle.setText(detail.getEventsTitle());
            holder.holder_eventsdetails.setText(Html.fromHtml(detail.getEventsDetails()));

            x = detail.getEventsImage();

            if(x!=null) {
                holder.holder_image.setVisibility(View.VISIBLE);
                bitmap1 = BitmapFactory.decodeByteArray(x, 0, x.length);
                holder.holder_image.setImageBitmap(bitmap1);
            }
            else{
                    holder.holder_image.setVisibility(View.GONE);
                }

            }
            else{
                holder.holder_eventstitle.setText("There are No Events Today");
            }

            return convertView;
        }

        }// end of customAdapter

}//End of Class
