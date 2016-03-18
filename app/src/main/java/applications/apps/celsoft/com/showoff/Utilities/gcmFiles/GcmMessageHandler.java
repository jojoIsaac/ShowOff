package applications.apps.celsoft.com.showoff.Utilities.gcmFiles;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;


import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONObject;

import applications.apps.celsoft.com.showoff.R;
import applications.apps.celsoft.com.showoff.ShowOff_startPage;

/**
 * Created by User on 3/6/2016.
 */
public class GcmMessageHandler

        extends GcmListenerService {
    public static final int MESSAGE_NOTIFICATION_ID = 435345;

    @Override
    public void onMessageReceived(String from, Bundle data) {

        processAlert(data,from);

    }

    private void processAlert(Bundle data,String from) {
        String message = data.getString("message");
        Log.d("HELP", message);
        String type=data.getString("Type");
        String alertMessage= data.getString("alert");
        try {
            JSONObject gcmObject = new JSONObject(message);
            if(gcmObject!=null)
            {
                JSONObject alertBodyObject= new JSONObject(gcmObject.getString("message"));

                createNotification(alertMessage,alertBodyObject.toString(),type);
                if(alertBodyObject!=null)
                {
                    //Log.d("HELP", message);
                }
            }
        }
        catch(Exception e)
        {
            createNotification(from,alertMessage,type);
          e.printStackTrace();
        }
    }

    // Creates notification based on title and body received
    private void createNotification(String alertText, String body,String type) {
        Context context = getBaseContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle("ShowOff")
                .setContentText(alertText);

        mBuilder.setAutoCancel(true);

        Intent resultIntent = new Intent(context, ShowOff_startPage.class);
        resultIntent.putExtra("MESSAGE", body);
        resultIntent.putExtra("TYPE",type);



        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                resultIntent.FLAG_ACTIVITY_SINGLE_TOP);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ShowOff_startPage.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);


        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);




        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
        //mNotificationManager.cancel(0);
    }
}