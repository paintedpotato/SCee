package com.example.sawe.scee;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;


public class FaceDetService extends Service {
    private NotificationManager mNM;

    private int NOTIFICATION = 0;

    public class LocalBinder extends Binder{
        FaceDetService getService(){
            return FaceDetService.this;
        }
    }

    @Override
    public void onCreate(){
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId){

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        //Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "fd stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        //CharSequence text = getText(R.string.local_service_started);
        CharSequence text = "fd started";

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                //new Intent(this, CameraFragment.Controller.class), 0);
                new Intent(this, CameraFragment.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                //.setSmallIcon(R.drawable.stat_sample)  // the status icon
                .setSmallIcon(R.drawable.ic_launcher_background)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                //.setContentTitle(getText(R.string.local_service_label))  // the label of the entry
                .setContentTitle("Maiora premunt")  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
    }

}
    /*public class FaceDetService extends IntentService {
    // The constructor is required
    public FaceDetService() {
        super("FaceDetService");
    }

    @Override
    protected void onHandleIntent(Intent intent){
        //try{

            // Cause randomi does not change, random2 does, then this can be recorded upon every new
            // face - ie random2
            if(CameraFragment.random2 == 1){
                Toast.makeText(this, "Spotted a face", Toast.LENGTH_SHORT).show();
            }

            // Would've needed the below code had I been working with randomi
            //Thread.sleep(5000); // so it checks every 5s if there is a face

            // Must add code that sees whether the 1 turned to a zero to reset the process,
            // rather than having to check every time.
            // ie to give the msg only when 0 becomes 1..
        //}catch (InterruptedException e){
          //  Thread.currentThread().interrupt();
        //}
    }
}
    /*public void onReceive(Context context, Intent intent){
        Bundle bundle = intent.getExtras();

        if(bundle!=null){

        }
    }

}
//*/