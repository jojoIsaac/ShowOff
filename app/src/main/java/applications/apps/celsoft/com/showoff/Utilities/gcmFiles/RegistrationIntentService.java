package applications.apps.celsoft.com.showoff.Utilities.gcmFiles;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.IOException;

import applications.apps.celsoft.com.showoff.R;
import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;

/**
 * Created by User on 3/6/2016.
 */
public class RegistrationIntentService extends IntentService {

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String GCM_TOKEN = "gcmToken";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RegistrationIntentService(String name) {
        super(name);
    }

    // abbreviated tag name
    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Make a call to Instance API
        InstanceID instanceID = InstanceID.getInstance(this);
        String senderId = getResources().getString(R.string.gcm_defaultSenderId);
        try {
            // request token that will be used by the server to send push notifications
            String token = instanceID.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE);
            Log.d(TAG, "GCM Registration Token: " + token);
            try {
                // save token
                AppBackBoneClass.myPrefs.edit().putString(GCM_TOKEN, token).apply();
                // pass along this data
                sendRegistrationToServer(token);
            } catch (Exception es) {
                Log.d(TAG, "Failed to complete token refresh", es);
                // If an exception happens while fetching the new token or updating our registration data
                // on a third-party server, this ensures that we'll attempt the update at a later time.
                AppBackBoneClass.myPrefs.edit().putBoolean( AppBackBoneClass.SENT_TOKEN_TO_SERVER, false).apply();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

        Ion.with(this)
                .load(AppBackBoneClass.parentUrL+AppBackBoneClass. gcmServerRegister)
                .setBodyParameter("regId",token)
                .setBodyParameter("email","")
                .setBodyParameter("userID", AppBackBoneClass.getUserId())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e == null) {
                            ///If registration was successful

                            AppBackBoneClass.myPrefs.edit().putBoolean( AppBackBoneClass.SENT_TOKEN_TO_SERVER, true).apply();
                        } else {
                            ///If registration wasn't successful
                            AppBackBoneClass.myPrefs.edit().putBoolean( AppBackBoneClass.SENT_TOKEN_TO_SERVER, false).apply();
                        }
                    }
                });


    }
}
