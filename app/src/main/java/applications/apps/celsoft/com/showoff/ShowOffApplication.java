package applications.apps.celsoft.com.showoff;

import android.app.Application;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;



import im.ene.lab.toro.Toro;

/**
 * Created by User on 1/31/2016.
 */
public class ShowOffApplication extends Application {

    private static ShowOffApplication sApp;




    @Override
    public void onCreate() {

        super.onCreate();
        Toro.init(this);
        sApp = this;
        Iconify
                .with(new FontAwesomeModule());

        // Or, you can define it manually.
       // UploadService.NAMESPACE = "applications.apps.celsoft.com.showoff";

    }
}
