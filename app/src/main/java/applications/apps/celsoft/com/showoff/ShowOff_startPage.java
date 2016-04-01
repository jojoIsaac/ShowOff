package applications.apps.celsoft.com.showoff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import org.json.JSONObject;

import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import applications.apps.celsoft.com.showoff.Utilities.WakeLocker;
import applications.apps.celsoft.com.showoff.Utilities.gcmFiles.RegistrationIntentService;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.AppUser;
import applications.apps.celsoft.com.showoff.Views.ApplicationFragments;
import applications.apps.celsoft.com.showoff.Views.ApplicationSettings;
import applications.apps.celsoft.com.showoff.Views.Fragment_userProfile;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

import static applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass.context;

/**
 * Created by User on 2/25/2016.
 */
public class ShowOff_startPage extends ShowOffMain implements MaterialTabListener {
    private ViewPager pager;
    private ViewPagerAdapter pagerAdapter;
    RelativeLayout main_layout;
    MaterialTabHost tabHost;
    private Resources res;


    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private boolean isReceiverRegistered;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

Fragment fragment;

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    fragment = new MainAppFragment();
                    break;
                case 1:
                    fragment = new Fragment_userProfile();
                    break;
                case 2:
                    fragment = new ApplicationSettings();
                    break;
            }

            return fragment;
        }
        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            String title="";
            switch (position)
            {
                case 0:
                    title="Live";
                    break;
                case 1:
                    title="Profile";
                    break;
                case 2:
                    title="Settings";
                    break;
            }
            return title;
        }

        public Drawable getIcon(int position) {
            return res.getDrawable(R.drawable.ic_menu_share);
        }
    }
    /*
    * It doesn't matter the color of the icons, but they must have solid colors
    */


  public  void handleDataPicker()
  {
      super.handleDataPicker();
  }
    IconDrawable drawableIco;
    private Drawable getIcon(int position) {
        switch (position)
        {
            case 0:
                drawableIco= new IconDrawable(this, FontAwesomeIcons.fa_list_alt).color(Color.red(200)).actionBarSize();
               break;
            case 1:
                drawableIco= new IconDrawable(this, FontAwesomeIcons.fa_user).color(Color.red(200)).actionBarSize();
                break;
            case 2:
                drawableIco= new IconDrawable(this, FontAwesomeIcons.fa_gears).color(Color.red(200)).actionBarSize();
                break;
        }
        return drawableIco;
    }



    @Override
    public void onTabSelected(MaterialTab tab) {
// when the tab is clicked the pager swipe content to the tab position
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {
    }

    @Override
    public void onTabUnselected(MaterialTab tab) {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        AppBackBoneClass.myPrefs = this.getSharedPreferences(AppBackBoneClass.prefernceName,
                Context.MODE_PRIVATE);

        setGCMMethods();
        AppBackBoneClass.initApp();
        setContentView(R.layout.showoff_main_page);
        main_layout=(RelativeLayout)findViewById(R.id.main_layout);

        if(AppBackBoneClass.devMode)
        {
            AppBackBoneClass.setUserID(this,"100");
        }

        boolean isLoggedIn=  AppBackBoneClass.getisLoggedIn();

        if(!isLoggedIn)
        {
            Intent intent = new Intent(this, LaunchPage.class);
            startActivity(intent);
            finish();
        }
        res = this.getResources();
        // init toolbar (old action bar)

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);


        tabHost = (MaterialTabHost) this.findViewById(R.id.tabHost);
        pager = (ViewPager) this.findViewById(R.id.pager);
        // init view pager
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);
            }
        });
        // insert all tabs from pagerAdapter data
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setIcon(getIcon(i))
                            .setTabListener(this)
            );
        }



        registerReceiver(networkReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));


        handleGcmOperation();
        GCMStuffs();
    }


    void GCMStuffs()
    {
        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                AppBackBoneClass.DISPLAY_MESSAGE_ACTION));
    }

    private final BroadcastReceiver mHandleMessageReceiver
            = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String newMessage = intent.getExtras().getString(AppBackBoneClass.EXTRA_MESSAGE);
            String type=intent.getExtras().getString(AppBackBoneClass.EXTRA_MESSAGE_TYPE);
            String alert= intent.getExtras().getString("alert");
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */
            //Toast.makeText(ShowOff_startPage.this, newMessage, Toast.LENGTH_SHORT).show();
            // Showing received message
            processNotifications(newMessage, type, context, intent,2,alert);

            // Releasing wake lock
            WakeLocker.release();
        }


    };

    private void processNotifications(final String newMessage, final String type, Context context, Intent intent, int i,String alert) {

        if(!TextUtils.isEmpty(newMessage))
        {
            //


            try {
                JSONObject gcmObject = new JSONObject(newMessage);
                if(gcmObject!=null)
                {
                    final JSONObject alertBodyObject= new JSONObject(gcmObject.getString("message"));


                    if(alertBodyObject!=null)
                    {
                        handleGCM(alertBodyObject.toString(),type,alert);
                    }
                }
            }
            catch(Exception e)
            {

                e.printStackTrace();
            }

Log.e("DATAPACL", newMessage);

            //

        }

    }


    private void handleGcmOperation() {
        String message="";
        String type="";
        String alert="";
        if(getIntent().hasExtra("MESSAGE"))
        {
            message = getIntent().getStringExtra("MESSAGE");

        }

        if(getIntent().hasExtra("TYPE"))
        {
            type= getIntent().getStringExtra("TYPE");
        }

        if(getIntent().hasExtra("alert"))
        {
           alert= getIntent().getStringExtra("alert");
        }

        if(!TextUtils.isEmpty(message))
        {
            //




            //Log.e("DATAPACL", newMessage);
            if(type.equalsIgnoreCase("OBJECT_COMMENT"))
            {
                Intent  intent = new Intent(ShowOff_startPage.this,FeedDetailActivity.class);
                intent.putExtra("json", message);
                intent.putExtra("index", "-1");
                ShowOff_startPage.this.startActivityForResult(intent, ApplicationFragments.ITEM_CHANGED_RESULTS);
            }
            //

        }

        //Toast.makeText(ShowOff_startPage.this, message, Toast.LENGTH_SHORT).show();
    }


    void handleGCM(final String intentPackage,String type,String alert)
    {
        try {

            if(intentPackage
                    !=null)
            {
                final String finalType = type;
                final Snackbar snackbar = Snackbar
                        .make(main_layout, alert, Snackbar.LENGTH_LONG)
                        .setAction("OPEN", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {



                                if(finalType.equalsIgnoreCase("OBJECT_COMMENT"))
                                {
                                    Intent  intent = new Intent(ShowOff_startPage.this,FeedDetailActivity.class);
                                    intent.putExtra("json", intentPackage);
                                    intent.putExtra("index", "-1");
                                    ShowOff_startPage.this.startActivityForResult(intent, ApplicationFragments.ITEM_CHANGED_RESULTS);
                                }
                            }
                        })
                        ;

                // Changing message text color
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
            }

        }
        catch(Exception e)
        {

            e.printStackTrace();
        }
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(AppBackBoneClass.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    @Override
    protected void onPause() {
        try{
            this.unregisterReceiver(networkReceiver);
        }
        catch (Exception e)
        {

        }

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;

        super.onPause();
    }

    private void setGCMMethods() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                boolean sentToken = AppBackBoneClass.myPrefs
                        .getBoolean(AppBackBoneClass.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    //mInformationTextView.setText(getString(R.string.gcm_send_message));
                } else {
                    //mInformationTextView.setText(getString(R.string.token_error_message));
                }
            }
        };


        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    public void userProfileActivity(AppUser user,String likes,String userlike)
{
    if(!user.getUserID().equalsIgnoreCase(AppBackBoneClass.getUserId()))
    {
        //Toast.makeText(this,user.getUserJson() , Toast.LENGTH_LONG).show();
        //Create an intent to open the user profile page
        if(context==null)
            Log.e("NOP","NULLLLL");


        Intent userprofileIntent= new Intent( this, UserProfile.class);
        userprofileIntent.putExtra("USER_JSON",user.getUserJson());
        userprofileIntent.putExtra("USER_LIKE",userlike);
        userprofileIntent.putExtra("LIKES",likes);
        startActivity(userprofileIntent);
    }
    else
    {
        Toast.makeText(this, user.getUser_name(), Toast.LENGTH_SHORT).show();
    }
}

    private void debugIntent(Intent intent, String tag) {
        Log.v(tag, "action: " + intent.getAction());
        Log.v(tag, "component: " + intent.getComponent());
        Bundle extras = intent.getExtras();
        if (extras != null) {


            // Show a toast when there is no internet connection
            if(extras.get("noConnectivity")!=null&&extras.get("noConnectivity").equals(true))
            {
                Snackbar snackbar = Snackbar
                        .make(main_layout, "No internet connection!", Snackbar.LENGTH_LONG)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        })
                       ;

                // Changing message text color
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
                //Toast.makeText(this, "No Connection", Toast.LENGTH_LONG).show();
            }

        }
        else {
            Log.v(tag, "no extras");
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        AppBackBoneClass.context=this;
    }

    private BroadcastReceiver networkReceiver =new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            debugIntent(intent, "AppSet");
        }
    };








    @Override
    protected void onResume() {
        super.onResume();
        AppBackBoneClass.currentAppPage=0;
    }


    @Override
    protected void onDestroy() {

        try {
            unregisterReceiver(mHandleMessageReceiver);
            //GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        fragment=null;
        super.onDestroy();
    }

}
