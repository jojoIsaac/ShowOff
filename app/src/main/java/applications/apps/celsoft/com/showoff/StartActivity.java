package applications.apps.celsoft.com.showoff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import applications.apps.celsoft.com.showoff.Views.DancerFragment;

public class StartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String tempFilename="tempfile_";
List<IconDrawable> menuIcons= new ArrayList<IconDrawable>();
static Fragment  fragment;
    View vx;
    private String filePath ="";
    public static String mTitle;
    static NavigationView navigationView;
    private BroadcastReceiver networkReceiver =new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            debugIntent(intent, "AppSet");
        }
    };

    @Override public void onPause() {
        try{
            this.unregisterReceiver(networkReceiver);
        }
        catch (Exception e)
        {

        }

        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        AppBackBoneClass.currentAppPage=0;
        AppBackBoneClass.myPrefs= getSharedPreferences( AppBackBoneClass.prefernceName, Context.MODE_PRIVATE);
        AppBackBoneClass.context=this;
        AppBackBoneClass.initApp();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

       navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        //Change the Icons of the Navigation drawer
        menuIcons.add(new IconDrawable(this, FontAwesomeIcons.fa_futbol_o).color(Color.red(200)).actionBarSize());
        menuIcons.add(new IconDrawable(this, FontAwesomeIcons.fa_music).color(Color.red(200)).actionBarSize());
        menuIcons.add(new IconDrawable(this, FontAwesomeIcons.fa_film).color(Color.red(200)).actionBarSize());

        menuIcons.add(new IconDrawable(this, FontAwesomeIcons.fa_paint_brush).color(Color.red(200)).actionBarSize());

        menuIcons.add(new IconDrawable(this, FontAwesomeIcons.fa_gamepad).color(Color.red(200)).actionBarSize());
        menuIcons.add(new IconDrawable(this, FontAwesomeIcons.fa_user).color(Color.red(200)).actionBarSize());
        menuIcons.add(new IconDrawable(this, FontAwesomeIcons.fa_gears).color(Color.red(200)).actionBarSize());


        for (int i=0;i<menuIcons.size();i++)
        {
            navigationView.getMenu().getItem(i).setIcon(menuIcons.get(i));
        }
        //navigationView.getMenu().getItem(5).getSubMenu().getItem(0).setIcon();
        //navigationView.getMenu().getItem(5).getSubMenu().getItem(1).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_gears).color(Color.red(200)).actionBarSize());

        // navigationView.getMenu().getItem(0).setIcon();


        registerReceiver(networkReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));


        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("AC98C820A50B4AD8A2106EDE96FB87D4")  // An example device ID
                .addTestDevice("884F19014D974599185816201FA42FA4")
                .build();

        AdView mAdView = (AdView) findViewById(R.id.adView);
        mAdView.loadAd(request);




        /**
         *
         */


        if(AppBackBoneClass.devMode)
        {
            AppBackBoneClass.setUserID(this,"100");
        }

        boolean isLoggedIn=  AppBackBoneClass.getisLoggedIn();

        if (savedInstanceState == null) {
           fragment = new  MainAppFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
            // changeFragment(1,0);
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
                Toast.makeText(StartActivity.this,"No Connection",Toast.LENGTH_LONG).show();
            }

        }
        else {
            Log.v(tag, "no extras");
        }
    }

    @Override
    public void onBackPressed() {
        AppBackBoneClass.currentAppPage=0;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.newItem) {
          handleDataPicker();
        }

        return super.onOptionsItemSelected(item);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1,REQUEST_VIDEO_CHOOSE = 2,REQUEST_IMAGE_CHOOSE = 3,REQUEST_VIDEO_CAPTURE = 4;;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_VIDEO_CAPTURE);
        }
    }


    private void dispatchChooseImageIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_CHOOSE);
    }

    private void dispatchChooseVideoIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_VIDEO_CHOOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent chooseMedia = new Intent(
                this, ChooseMedia.class
        );


        try {

            if (data != null) {
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    Bundle bundle = new Bundle();

                    Bitmap _bitmap; // your bitmap
                    ByteArrayOutputStream _bs = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, _bs);
                    chooseMedia.putExtra("byteArrayImage", _bs.toByteArray());
                    AppBackBoneClass.savetempFile(AppBackBoneClass.files_temp.getAbsolutePath() + "/" + tempFilename + "img.jpg", _bs, "img");
                    startActivity(chooseMedia);


                } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
                    Uri selectedVideoUri = data.getData();
                    String[] projection = {MediaStore.MediaColumns.DATA};
                    CursorLoader cursorLoader = new CursorLoader(this, selectedVideoUri, projection, null, null, null);
                    Cursor cursor = cursorLoader.loadInBackground();
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    cursor.moveToFirst();
                    String selectedVideoPath = cursor.getString(column_index);
                    File file= new File(selectedVideoPath);

                    if(file.exists())
                    {
                        boolean sizeissues=true;
                        long filesize= file.length()/(1024*1024);
                        sizeissues = (AppBackBoneClass.devMode)?false:true;
                        if(sizeissues || filesize <=5 ) {
                            Intent setFullscreen = new Intent(this, VideoPlayer.class);
                            setFullscreen.putExtra("URL", selectedVideoPath);
                            setFullscreen.putExtra("CURP", "0");
                            setFullscreen.putExtra("FILE_NAME", file.getName());
                            setFullscreen.putExtra("REASON","UPLOAD");
                            startActivity(setFullscreen);
                        }
                        else
                        {
                            Toast.makeText(this,"File too large",Toast.LENGTH_LONG).show();
                        }
                    }



                } else if (requestCode == REQUEST_VIDEO_CHOOSE && resultCode == RESULT_OK) {
                    Uri selectedVideoUri = data.getData();
                    String[] projection = {MediaStore.MediaColumns.DATA};
                    CursorLoader cursorLoader = new CursorLoader(this, selectedVideoUri, projection, null, null, null);
                    Cursor cursor = cursorLoader.loadInBackground();
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    cursor.moveToFirst();
                    String selectedVideoPath = cursor.getString(column_index);
                    File file= new File(selectedVideoPath);

                    if(file.exists())
                    {
                        boolean sizeissues=true;
                        long filesize= file.length()/(1024*1024);
                        sizeissues = (AppBackBoneClass.devMode)?false:true;
                        if(sizeissues || filesize <=5 ) {
                            Intent setFullscreen = new Intent(this, VideoPlayer.class);
                            setFullscreen.putExtra("URL", selectedVideoPath);
                            setFullscreen.putExtra("CURP", "0");
                            setFullscreen.putExtra("FILE_NAME", file.getName());
                            setFullscreen.putExtra("REASON","UPLOAD");
                            startActivity(setFullscreen);
                        }
                        else
                        {
                            Toast.makeText(this,"File too large",Toast.LENGTH_LONG).show();
                        }
                    }






                }
                else if (requestCode == REQUEST_IMAGE_CHOOSE && resultCode == RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    String[] projection = {MediaStore.MediaColumns.DATA};
                    CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null, null);
                    Cursor cursor = cursorLoader.loadInBackground();
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    cursor.moveToFirst();
                    String selectedImagePath = cursor.getString(column_index);
                    Bitmap bm;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(selectedImagePath, options);
                    final int REQUIRED_SIZE = 250;
                    int scale = 1;
                    while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                        scale *= 2;
                    options.inSampleSize = scale;
                    options.inJustDecodeBounds = false;
                    bm = BitmapFactory.decodeFile(selectedImagePath, options);

                    ByteArrayOutputStream _bs = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, _bs);
                    chooseMedia.putExtra("byteArrayImage", _bs.toByteArray());
                    AppBackBoneClass.savetempFile(AppBackBoneClass.files_temp.getAbsolutePath() + "/" + tempFilename + "img.jpg", _bs, "img");
                    startActivity(chooseMedia);
                }

            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }



    private void handleDataPicker() {
        String [] menu = {"New Image","New Video","Upload Image","Upload Video"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setItems(menu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which)
                {
                    case 0:
                        //Start the camera for picture
                        dispatchTakePictureIntent();

                        break;
                    case 1:
                        // Start the camera for video
                        dispatchTakeVideoIntent();
                        break;

                    case 2:
                        dispatchChooseImageIntent();
                        break;

                    case 3:
                        dispatchChooseVideoIntent();
                        break;

                    default:
                        break;



                }


            }
        })
                .setTitle("Choose Action")
                .show();




    }



    @Override
    public void setTitle(CharSequence title) {
        mTitle = title.toString();
        getSupportActionBar().setTitle(mTitle);
    }

    String title="";

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sports) {
            // Handle the camera action
            mTitle="Sports";
            //fragment = new SportFragment();


        } else if (id == R.id.nav_dance) {
            mTitle="Dance Moves";
            fragment = new DancerFragment();

        } else if (id == R.id.nav_music) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_user) {
mTitle="Profile";
           Intent intent = new Intent(this,UserProfile.class)
                   ;
            intent .putExtra("USER_JSON",AppBackBoneClass.getUserJson());
            startActivity(intent);
        }

        if (StartActivity.fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            try
            {
                setTitle(mTitle);

//			if (currentFragment != null) {
//				fragmentManager
//						.beginTransaction()
//						.replace(
//								((ViewGroup) (currentFragment.getView().getParent()))
//										.getId(), fragment)
//						.addToBackStack(null).commit();
//
//			} else {
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment)
                        .addToBackStack(null).commit();
                //}

                supportInvalidateOptionsMenu();
            }
            catch( Exception e)
            {

            }


        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public static void setMenuIndex(Integer menuIdex) {
        if(menuIdex>0)
        navigationView.setCheckedItem(menuIdex);
        else
            navigationView.setCheckedItem(-1);
    }





}
