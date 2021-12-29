package com.fssihrms;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import static com.fssihrms.Constant_webservice.URL;
//import hrms.com.hrm.R;


/**
 * Created by User on 6/21/2017.
 */

public class BirthdaybuddyFragment extends Fragment {

    int count1;
    int noOfobjects = 0;
    private LinearLayout birthdaybuddyfragmentViewLayout;
    private ListView listView;
    byte[] imageAsBytes;
    Bitmap decodedByteBitmap;


    BirthdaybuddyClass[] birthdaybuddyclass_arryObj;

ImageView birthdaybuddypic_iv;
    Context context;
    Resources resource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        birthdaybuddyfragmentViewLayout=(LinearLayout) inflater.inflate(R.layout.fragment_birthdaybuddy, container, false);

        context =birthdaybuddyfragmentViewLayout.getContext();
        resource = context.getResources();

       // img= (ImageView)birthdaybuddyfragmentViewLayout.findViewById(R.id.img);

        listView = (ListView) birthdaybuddyfragmentViewLayout.findViewById(R.id.customlistview_birthdaybuddy);


      //  birthdaybuddypic_iv= (ImageView)birthdaybuddyfragmentViewLayout.findViewById(R.id.birthdaybuddypic_IV);


        AsyncCallWS2 task = new AsyncCallWS2(context);
        task.execute();

        return birthdaybuddyfragmentViewLayout;

    }// end of OncreateView


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
            Log.i("df", "doInBackground");

          //  String URL ="http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";  // WSDL file
            String METHOD_NAME = "GetBirthdayList";//leave
            String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/GetBirthdayList";

            try{
                SoapObject request = new SoapObject(Namespace, METHOD_NAME);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                //Set output SOAP object
                envelope.setOutputSoapObject(request);
                //Create HTTP call object
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                try
                {

                    androidHttpTransport.call(SOAPACTION, envelope);
                    SoapObject response = (SoapObject) envelope.getResponse();
                    Log.i("buddy response", response.toString());

                    if(response.equals("There are no birthday")){

                }
                   else
                       {
                    count1 = response.getPropertyCount();  // number of count in array in response 6,0-5,5

                    Log.i("number of rows", "" + count1);

                    noOfobjects = count1;

                    System.out.println("Number of object" + noOfobjects);


                    birthdaybuddyclass_arryObj = new BirthdaybuddyClass[count1];
                    for (int i = 0; i < count1; i++) {
                        SoapObject list1 = (SoapObject) response.getProperty(i);
                        SoapPrimitive responseName, responsePic;

                        responseName = (SoapPrimitive) list1.getProperty("Name");
                        responsePic = (SoapPrimitive) list1.getProperty("User_Image");

                        BirthdaybuddyClass innerObj = new BirthdaybuddyClass();

                        innerObj.setName(responseName.toString());

                        String urlImageBase64 = responsePic.toString();

                        final String pureBase64Encoded = urlImageBase64.substring(urlImageBase64.indexOf(",") + 1);
                        imageAsBytes = Base64.decode(pureBase64Encoded, Base64.NO_PADDING);
                        //decodedByte = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

                        innerObj.setBirthdayPic(imageAsBytes);
                        birthdaybuddyclass_arryObj[i] = innerObj;


                    }//end of for loop
                }

                }
                catch (Throwable t) {
                    //Toast.makeText(MainActivity.this, "Request failed: " + t.toString(),
                    //		Toast.LENGTH_LONG).show();
                    Log.e("request fail", "> " + t.getMessage());
                }
            }catch (Throwable t) {
                Log.e("UnRegister  Error", "> " + t.getMessage());

            }

            return null;
        }

        public AsyncCallWS2(Context context1) {
            context = context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected void onPostExecute(Void result) {

            dialog.dismiss();


            System.out.println("onPostExecute Birthday");

            if(birthdaybuddyclass_arryObj!=null)
            {
                CustomAdapter adapter = new CustomAdapter();
                listView.setAdapter(adapter);

                int x1 = birthdaybuddyclass_arryObj.length;

                System.out.println("Inside the if list adapter" + x1);
            }




           /* img.setImageBitmap(
                    BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
            );*/

        }//end of onPostExecute

    }// end Async task

    //Holder for customAdapter
    private class Holder {
        TextView holder_names;
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

            String x = Integer.toString(birthdaybuddyclass_arryObj.length);
            Log.d("class_arrayObj.length", x);
            return birthdaybuddyclass_arryObj.length;

        }

        @Override
        public Object getItem(int position) {
            String x = Integer.toString(position);
            //System.out.println("getItem position" + x);
            Log.d("getItem position", "x");
            return birthdaybuddyclass_arryObj[position];
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
            // holder = new Holder();
            if (convertView == null) {
                holder = new Holder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.row_item_birthdaybuddy, parent, false);



                holder.holder_names=(TextView) convertView.findViewById(R.id.birthdaybuddyname_TV);
                holder.holder_image=(ImageView) convertView.findViewById(R.id.birthdaybuddypic_IV);

                Log.d("Inside If convertView", "Inside If convertView");

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
                Log.d("Inside else convertView", "Inside else convertView");
            }

            BirthdaybuddyClass detail =(BirthdaybuddyClass) getItem(position);

            if(detail!=null) {
                holder.holder_names.setText(detail.getName());

                byte[] x = detail.getBirthdayPic();
                decodedByteBitmap = BitmapFactory.decodeByteArray(x, 0, x.length);

                holder.holder_image.setImageBitmap(decodedByteBitmap);
            }
            else{
                holder.holder_names.setText("There are no birthday today");
            }

            return convertView;
            }

    }// End of CustomAdapter

}// end of class
