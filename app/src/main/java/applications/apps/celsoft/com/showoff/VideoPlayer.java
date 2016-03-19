package applications.apps.celsoft.com.showoff;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import applications.apps.celsoft.com.showoff.Views.ApplicationFragments;
import applications.apps.celsoft.com.showoff.Utilities.models.VideoSource;
import im.ene.lab.toro.widget.ToroVideoView;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VideoPlayer extends AppCompatActivity implements MediaPlayer.OnCompletionListener,MediaPlayer.OnPreparedListener{
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;



    private static final String TAG = "UploadService";
    private static final String USER_AGENT = "UploadService/" + BuildConfig.VERSION_NAME;
    private static final int FILE_CODE = 1;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    //private View mContentView;
    private ToroVideoView mVideoView;
    private ImageView mThumbnail,playVideo,restartVideo,downloadVideo;
    RelativeLayout videoplayerLayout;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            videoplayerLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    private IconDrawable downloadIco;
    private ProgressDialog pdialog;
    private ArrayAdapter<String> dataAdapter;
    private Spinner cat_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_player);
        AppBackBoneClass.myPrefs= getSharedPreferences(AppBackBoneClass.prefernceName, Context.MODE_PRIVATE);
        AppBackBoneClass.context=this;
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        videoplayerLayout =(RelativeLayout) findViewById(R.id.videoplayerLayout);
        loadCategories();
        mVideoView = (ToroVideoView)findViewById(R.id.videoplayer);
        // Set up the user interaction to manually show or hide the system UI.



        mThumbnail = (ImageView) findViewById(R.id.thumbnail);
        restartVideo=(ImageView) findViewById(R.id.restartVideo);
        restartVideo.setVisibility(View.INVISIBLE);
        playVideo=(ImageView) findViewById(R.id.playVideo);
        downloadVideo=(ImageView) findViewById(R.id.fullScreen);



        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
        mThumbnail.setVisibility(View.INVISIBLE);

        setupVideoplayer();

        playVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                    playVideo.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
                    mThumbnail.setVisibility(View.INVISIBLE);
                } else {
                    mVideoView.start();
                    playVideo.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
                }

            }
        });



        downloadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (forDownload) {

                    final ProgressDialog pdialog;
                    String filePath = AppBackBoneClass.file_media_videos.getAbsolutePath() + file_name;
                    pdialog = new ProgressDialog(AppBackBoneClass.context);
                    pdialog.setMessage("Downloading ...");
                    pdialog.setCancelable(false);
                    pdialog.show();
                    Log.d("URL", VideoSource.videoBaseUrl + file_name);
                    Ion.with(VideoPlayer.this)
                            .load(videoUrl)
                            .progressDialog(pdialog)
                            .progress(new ProgressCallback() {
                                @Override
                                public void onProgress(long downloaded, long total) {
                                    float percent = (downloaded / total) * 100;
                                    pdialog.setMessage("Downloading ..." + percent + "%");
                                }
                            })

                            .write(new File(filePath))
                            .setCallback(new FutureCallback<File>() {
                                @Override
                                public void onCompleted(Exception e, File result) {
                                    pdialog.dismiss();
                                    Toast.makeText(VideoPlayer.this, "Video Downloaded", Toast.LENGTH_SHORT).show();
                                }
                            })
                    ;
                }
                else
                {
                   uploadNewVideo();

                }

            }

        });


    }



    MaterialDialog mMaterialDialog;
    void showBragNowDialog()
    {
        View rootView = getLayoutInflater(). inflate(R.layout.video_detail,null,
                false);
        final EditText edt_brag = (EditText) rootView.findViewById(R.id.edt_brag);

        cat_spinner = (Spinner) rootView.findViewById(R.id.cat_spinner);
        cat_spinner.setAdapter(dataAdapter);


        mMaterialDialog = new MaterialDialog(this)
                .setTitle("MaterialDialog")
                .setView(rootView)
                .setPositiveButton("BRAG NOW!", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //saveImageOnserver(_bitmap, fileType, "", edt_brag.getText().toString());
                        saveVideoOnServer( edt_brag.getText().toString());
                    }
                })
                .setNegativeButton("CANCEL", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mMaterialDialog.dismiss();
                    }
                });

        mMaterialDialog.show();
    }


    private void uploadNewVideo() {
        String [] menu =  new String[categories.size()];
        menu= categories.toArray(menu);
        showBragNowDialog();
    }


@Override
protected void onResume() {
        super.onResume();
        //uploadReceiver.register(this);
        }

@Override
protected void onPause() {
        super.onPause();
       // uploadReceiver.unregister(this);
        }



    private void saveVideoOnServer(String content) {
        String url= AppBackBoneClass.parentUrL+ AppBackBoneClass.videoUploader;
        File upload= new File(videoUrl);
        Integer val= cat_spinner.getSelectedItemPosition();
        String category= categoryIDs.get(val);
        final String finalCategory = category;
        pdialog= new ProgressDialog(AppBackBoneClass.context);
        pdialog.setMessage("Please wait. Time to showOff...");
        pdialog.setCancelable(false);
        pdialog.show();
         Ion.with(this)
                .load(url)
                .setMultipartParameter("name",file_name)
                 .setMultipartParameter("userID",AppBackBoneClass.getUserId())
                 .setMultipartParameter("data_cate",category)
                .setMultipartParameter("userpost", content)
                 .setMultipartParameter("data_type","video")
                 .setMultipartFile("data", upload)

                 .asString()
                 .setCallback(new FutureCallback<String>() {
                     @Override
                     public void onCompleted(Exception e, String result) {
                         if (e != null) {
                             Toast.makeText(VideoPlayer.this, "Error Uploading", Toast.LENGTH_SHORT).show();
                             e.printStackTrace();
                             return;
                         } else {
                             Log.e("ERRORUPLOAD", result);
                             pdialog.dismiss();
                             ApplicationFragments.layout_newStory.setVisibility(View.VISIBLE);
                             Toast.makeText(VideoPlayer.this,  result, Toast.LENGTH_SHORT).show();
                             finish();
                         }
                     }
                 });
    }


    static String videoUrl="";
int currentposition=0;
    boolean forDownload=true;
    String file_name;

    private void setupVideoplayer() {
        /*
          setFullscreen.putExtra("URL",mItem.getFilename());
                setFullscreen.putExtra("CURP",mVideoView.getCurrentPosition());
         */

        file_name= getIntent().getStringExtra("FILE_NAME");
        videoUrl= getIntent().getStringExtra("URL");
        currentposition= getIntent().getIntExtra("CURP", 0);
        mVideoView.setVideoPath(videoUrl);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnPreparedListener(this);


        if(getIntent().getStringExtra("REASON").equalsIgnoreCase("DOWNLOAD")) {

            downloadIco = new IconDrawable(this, FontAwesomeIcons.fa_download).color(
                    Color.RED + Color.BLUE).actionBarSize();
            forDownload= true;
        }
        else
        {
            downloadIco = new IconDrawable(this, FontAwesomeIcons.fa_upload).color(
                    Color.RED + Color.BLUE).actionBarSize();
            forDownload= false;
        }
        downloadVideo.setImageDrawable(downloadIco);


    }


    static ArrayList<String> categories,categoryIDs ;
    private void loadCategories()
    {
        Ion.with(this)
                .load(AppBackBoneClass.parentUrL + AppBackBoneClass.feedUrl)
                .setBodyParameter("reason", "get categories")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        try {
                            categories = new ArrayList<String>();
                            categoryIDs = new ArrayList<String>();
                            processCategoryJson(result);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });



    }

    private void processCategoryJson(String result) throws JSONException {
        //[{"id":"1","title":"Sports"},{"id":"2","title":"Music"},{"id":"3","title":"Dance"},{"id":"4","title":"Visual Arts"},{"id":"5","title":"Gamer"}]

        JSONArray jarray = new JSONArray(result);

        if(jarray!=null)
        {
            for (int i=0;i<jarray.length();i++)
            {
                JSONObject jsonObject = jarray.getJSONObject(i);
                categories.add(jsonObject.getString("title"));
                categoryIDs.add(jsonObject.getString("id"));
            }
            dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        }



    }




    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mVideoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playVideo.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
mVideoView.seekTo(currentposition);

    }


















}
