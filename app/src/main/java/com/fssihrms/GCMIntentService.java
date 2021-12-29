package com.fssihrms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

//import hrms.com.hrm.R;

/**
 * Created by User on 7/12/2017.
 */

public class GCMIntentService extends GCMBaseIntentService {


    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(CommonUtilities.SENDER_ID);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {

        Log.i(TAG, "Received message");
        String message = intent.getExtras().getString("message");
        Context context1 = getApplicationContext();
        CommonUtilities.displayMessage(context1, message);
        // notifies user
        generateNotification1(context1, message);
        //   Toast.makeText(context1,message,Toast.LENGTH_LONG).show();
                          //    Log.i(TAG, message);
        //    Intent i = new Intent(getApplicationContext(), MainActivity.class);
        //    i.putExtra("messege",message);
        //   startActivity(i);
  /*      Context context1 = getApplicationContext();

        NotificationManager mNotificationManager = (NotificationManager) context1
                .getSystemService(Context.NOTIFICATION_SERVICE);



        Intent notificationIntent = new Intent(context1, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        notificationIntent.putExtra("notificationMsg", message);

        PendingIntent contentIntent = PendingIntent.getActivity(context1, 0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context1);

        Notification notification = mBuilder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher).setTicker(getResources().getString(R.string.app_name)).setWhen(0)
                .setAutoCancel(true).setContentTitle(getResources().getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setVibrate(new long[] { 1000, 1000, 1000 })
                .setContentText(message).build();

        mNotificationManager.notify(1, notification);
*/


        //showCustomDialog(context,message);

    }

    @Override
    protected void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        CommonUtilities.displayMessage(context, getString(R.string.gcm_error, errorId));

    }

    @Override
    protected void onRegistered(Context context, String registrationId) {

        Log.i(TAG, "Device registered: regId = " + registrationId);
        CommonUtilities.displayMessage(context, "Your device registered with GCM");

    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        CommonUtilities.displayMessage(context, getString(R.string.gcm_unregistered));
    }


    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        CommonUtilities.displayMessage(context, message);
        // notifies user
        generateNotification1(context, message);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        CommonUtilities.displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    private static void generateNotification1(Context context, String message) {


        Notification.Builder builder = new Notification.Builder(context);
        Intent notificationIntent = new Intent(context,MainActivity.class);
        notificationIntent.putExtra("messege", message);
        String title = context.getString(R.string.app_name);
        // int id1 = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,notificationIntent, 0);
        int id1 = (int) System.currentTimeMillis();
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(notificationIntent);
        // Gets a PendingIntent containing the entire back stack

        PendingIntent contentIntent1 = stackBuilder.getPendingIntent(id1,
                PendingIntent.FLAG_UPDATE_CURRENT| PendingIntent.FLAG_ONE_SHOT);
        builder.setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(contentIntent1);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.getNotification();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;

        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        int id = (int) System.currentTimeMillis();
        notificationManager.notify(id, notification);



    }


}// End of GCMIntentService
