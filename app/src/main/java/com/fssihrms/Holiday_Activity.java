package com.fssihrms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.util.List;
import static com.fssihrms.Constant_webservice.URL;
//import hrms.com.hrm.R;

public class Holiday_Activity extends AppCompatActivity implements OnPageChangeListener,OnLoadCompleteListener{

    public static final String SAMPLE_FILE = "sandboxholidaylist.pdf";
    ImageView imgs;

    Context context;

    int count1;
    int noOfobjects = 0;

    BirthdaybuddyClass obj1;

    byte[] imageAsBytes;
    Bitmap decodedByte;


    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;

    Button mypdf;

    int navbutton=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_);

        context = getApplicationContext();

//imgs=(ImageView) findViewById(R.id.imgs);



        /*String urlimage="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADMAAAAmCAYAAABpuqMCAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYxIDY0LjE0MDk0OSwgMjAxMC8xMi8wNy0xMDo1NzowMSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNS4xIFdpbmRvd3MiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6Njc1RTkzRDkyRjBEMTFFN0E3MEVDNUM5QTk3QzZDMzIiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6Njc1RTkzREEyRjBEMTFFN0E3MEVDNUM5QTk3QzZDMzIiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo2NzVFOTNENzJGMEQxMUU3QTcwRUM1QzlBOTdDNkMzMiIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo2NzVFOTNEODJGMEQxMUU3QTcwRUM1QzlBOTdDNkMzMiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PlUfPHAAAANISURBVHja7JlbTupQFIYBRUQo6Ks+Ybi0QROJ4gScwGECnUCdAE6ACdgJcAbAmQAToAk3A1RI9EmftXgBb5z1G5oQU+m9GsJOkCqbvfe31trrpn8ymfgWZQR8CzQWCmbV7Q0GgwE7Ho+5l5cXlkya+dx0dfUmFApJ9OolEomhU3v53bgznU6n8PT0dPL+/v55+EAgMFxbW+tFIpFqKpWS1XndbvdEURQ+GAzeRKPRf5lMpvZrNNNut3nSwjEERCDbpIHbra2tUjKZvNGa//z8DOAdguUJKiZJEk9QFY7jqj+mGVmWj4fDIY9nWk+gA1YYhvm7t7dX0ftuo9EoElSetFOY/kkkOOXw8PDMc5hWqyXQfeDoUcDvBFKOx+NlM9K9uLjgSRh/ZoHwgwRSZlm25ok3g1RnQSAYuhuyWTPZ398vh8Nh6e3tTdUk1hOgbdwr12GazaZAm++oIBgfHx/lzc3Ncyvr5XK5kiqQmSE8Pj4W+v0+6xoMpPX6+srNgqhjd3fXsqtdWVm51TB74f7+/tQ1GEhLC8R2nPD7v/2sXq8XHYfBPfmB4A4PuaNnbqZhvt4TL4HgEK6urhhHYBAU3TwtmZky73NyMMWHh4eCIzCI7vO0ouZeVgdMyc4cx7JmkppvfX1dsrMGYpSdIG4YRu/yAQZ5lR0YRHxP6pnv4oqGc7CjXcYTzWAjvRiBmsUODFIjT2D0NqFM1zcajY7twJCDyVMW4D4MCiw9zQD48vLSEtD19TVDMCyE4joMSl01NZ8DzJN28lYOMo0fBU8cgNG6YloSmB4wUTsm5nh3BqaGut+sqaHpQd5y246Jme4BwNTI/eoB8Xd3d3g0XCHS/FP6XsGgwBRHNDMNaqKedug1tKJVA3NKqEgdgUGXRS8ZtJKjwcQMAIlIl+a1o0wbKZoVOtoRY7GYqbRkY2OjSgKozItlEKJet8c0DJp4wWCwpwWEw5BHUsw2NA4ODkQ1v7MjIEvuA5tPzU2cBUF3BW1XK2uSgGT027RA0A1Np9Oyq30zSZLO1e4lLj283dHR0ZnV9Wq12rm6FgIwvYtYU+3cuAqjHgA95Gw2ayv9/1rRonWL+sYoiCMwv2ks/9m0hFnCmBv/BRgAst21WASCIYcAAAAASUVORK5CYII";
        final String pureBase64Encoded = urlimage.substring(urlimage.indexOf(",")  + 1);
        imageAsBytes = Base64.decode(pureBase64Encoded, Base64.NO_PADDING);
*/
        // byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        /*decodedByte = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

        imgs.setImageBitmap(decodedByte);*/

       /* AsyncCalltoSelfservice taskSelfservice = new AsyncCalltoSelfservice(Holiday_Activity.this);
        taskSelfservice.execute();*/

        //setTitle("FSSI HRMS");

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.holidaylist_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("FSSI HRMS");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.lef);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getActivity().onBackPressed();
                Intent i = new Intent(Holiday_Activity.this,HomeActivity.class);
                startActivity(i);
                finish();
            }
        });*/



        pdfView= (PDFView)findViewById(R.id.pdfView);
        displayFromAsset(SAMPLE_FILE);
       // mypdf = (Button)findViewById(R.id.mypdf);


       /* mypdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pdfView= (PDFView)findViewById(R.id.pdfView);
                displayFromAsset(SAMPLE_FILE);

            }
        });*/



    }// End of Oncreate



    private void displayFromAsset(String assetFileName) {
        pdfFileName = assetFileName;

        pdfView.fromAsset(SAMPLE_FILE)
                .defaultPage(pageNumber)
                .enableSwipe(true)

                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }


    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }


    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

           // Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }


    public void onBackPressed()
    {
        //if(navbutton >= 1)
        {
            Intent i = new Intent(Holiday_Activity.this,HomeActivity.class);
            startActivity(i);
            finish();
        }
       /* else
        {
            Toast.makeText(this, "Kindly press once again", Toast.LENGTH_SHORT).show();
            navbutton++;
        }*/


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

             //   String URL ="http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";  // WSDL file


                String METHOD_NAME = "GetBirthdayList";//leave
                String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/GetBirthdayList";

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

                   /* request.addProperty("empId", 90);   //<empId>long</empId>
                    request.addProperty("ServiceCode", requestedforString); //<ServiceCode>string</ServiceCode>
                    request.addProperty("ServiceDescription", descriptiontoString);*/

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
                        count1 = response.getPropertyCount();  // number of count in array in response 6,0-5,5

                        Log.i("number of rows", "" + count1);

                        noOfobjects = count1;


                        System.out.println("Number of object" + noOfobjects);

                        SoapObject list = (SoapObject) response.getProperty(0);
                        SoapPrimitive NewsTitle, Types,NewsImage;

                        NewsTitle=(SoapPrimitive) list.getProperty("Name");
                        Types =(SoapPrimitive) list.getProperty("User_Image");

                       // String urlimage="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADMAAAAmCAYAAABpuqMCAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYxIDY0LjE0MDk0OSwgMjAxMC8xMi8wNy0xMDo1NzowMSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNS4xIFdpbmRvd3MiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6Njc1RTkzRDkyRjBEMTFFN0E3MEVDNUM5QTk3QzZDMzIiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6Njc1RTkzREEyRjBEMTFFN0E3MEVDNUM5QTk3QzZDMzIiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo2NzVFOTNENzJGMEQxMUU3QTcwRUM1QzlBOTdDNkMzMiIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo2NzVFOTNEODJGMEQxMUU3QTcwRUM1QzlBOTdDNkMzMiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PlUfPHAAAANISURBVHja7JlbTupQFIYBRUQo6Ks+Ybi0QROJ4gScwGECnUCdAE6ACdgJcAbAmQAToAk3A1RI9EmftXgBb5z1G5oQU+m9GsJOkCqbvfe31trrpn8ymfgWZQR8CzQWCmbV7Q0GgwE7Ho+5l5cXlkya+dx0dfUmFApJ9OolEomhU3v53bgznU6n8PT0dPL+/v55+EAgMFxbW+tFIpFqKpWS1XndbvdEURQ+GAzeRKPRf5lMpvZrNNNut3nSwjEERCDbpIHbra2tUjKZvNGa//z8DOAdguUJKiZJEk9QFY7jqj+mGVmWj4fDIY9nWk+gA1YYhvm7t7dX0ftuo9EoElSetFOY/kkkOOXw8PDMc5hWqyXQfeDoUcDvBFKOx+NlM9K9uLjgSRh/ZoHwgwRSZlm25ok3g1RnQSAYuhuyWTPZ398vh8Nh6e3tTdUk1hOgbdwr12GazaZAm++oIBgfHx/lzc3Ncyvr5XK5kiqQmSE8Pj4W+v0+6xoMpPX6+srNgqhjd3fXsqtdWVm51TB74f7+/tQ1GEhLC8R2nPD7v/2sXq8XHYfBPfmB4A4PuaNnbqZhvt4TL4HgEK6urhhHYBAU3TwtmZky73NyMMWHh4eCIzCI7vO0ouZeVgdMyc4cx7JmkppvfX1dsrMGYpSdIG4YRu/yAQZ5lR0YRHxP6pnv4oqGc7CjXcYTzWAjvRiBmsUODFIjT2D0NqFM1zcajY7twJCDyVMW4D4MCiw9zQD48vLSEtD19TVDMCyE4joMSl01NZ8DzJN28lYOMo0fBU8cgNG6YloSmB4wUTsm5nh3BqaGut+sqaHpQd5y246Jme4BwNTI/eoB8Xd3d3g0XCHS/FP6XsGgwBRHNDMNaqKedug1tKJVA3NKqEgdgUGXRS8ZtJKjwcQMAIlIl+a1o0wbKZoVOtoRY7GYqbRkY2OjSgKozItlEKJet8c0DJp4wWCwpwWEw5BHUsw2NA4ODkQ1v7MjIEvuA5tPzU2cBUF3BW1XK2uSgGT027RA0A1Np9Oyq30zSZLO1e4lLj283dHR0ZnV9Wq12rm6FgIwvYtYU+3cuAqjHgA95Gw2ayv9/1rRonWL+sYoiCMwv2ks/9m0hFnCmBv/BRgAst21WASCIYcAAAAASUVORK5CYII";

                        String urlimage=Types.toString();

                        final String pureBase64Encoded = urlimage.substring(urlimage.indexOf(",")  + 1);
                        imageAsBytes = Base64.decode(pureBase64Encoded, Base64.NO_PADDING);
                        decodedByte = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

                        //imageAsBytes = Base64.decode(urlimage, Base64.DEFAULT);

                       // byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                       decodedByte = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

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

        public AsyncCalltoSelfservice(Holiday_Activity activity) {
            context = activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPostExecute(Void result) {

           /* if ((this.dialog != null) && this.dialog.isShowing()) {
                dialog.dismiss();

            }*/

            dialog.dismiss();

           System.out.println("End of Post") ;
          imgs.setImageBitmap(decodedByte);


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
            Intent i = new Intent(Holiday_Activity.this,MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        /*else
        {
            Intent i = new Intent(Holiday_Activity.this,HomeActivity.class);
            startActivity(i);
            finish();

        }
*/
        return super.onOptionsItemSelected(item);
    }




}//End of class
