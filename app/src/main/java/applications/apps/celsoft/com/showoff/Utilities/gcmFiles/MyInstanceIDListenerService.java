package applications.apps.celsoft.com.showoff.Utilities.gcmFiles;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by User on 3/6/2016.
 */
public class MyInstanceIDListenerService


        extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify of changes
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }

}
