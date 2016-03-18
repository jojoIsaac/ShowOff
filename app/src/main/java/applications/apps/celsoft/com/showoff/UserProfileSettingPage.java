package applications.apps.celsoft.com.showoff;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.ByteArrayOutputStream;
import java.io.File;

import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.AppUser;
import butterknife.Bind;
import butterknife.ButterKnife;

import static applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass.context;

/**
 * Created by User on 2/28/2016.
 */
public class UserProfileSettingPage extends AppCompatActivity {
    private static Fragment fragment;
    private static ProgressDialog pdialog;
    public static String purpose="";
   public static boolean specific=false;



    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        outState.putString("PURPOSE", purpose);
        outState.putBoolean("SPECIFIC", specific);

        // created_by = savedInstanceState.getString("created_by");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_start);
        ButterKnife.bind(this);
        context = this;
        AppBackBoneClass.myPrefs = this.getSharedPreferences(AppBackBoneClass.prefernceName,
                Context.MODE_PRIVATE);


        if (savedInstanceState != null) {
           purpose= savedInstanceState.getString("PURPOSE");
            specific=savedInstanceState.getBoolean("SPECIFIC");

        }
        else {

            if (getIntent().hasExtra("PURPOSE")) {
                purpose = getIntent().getStringExtra("PURPOSE");
                specific = true;
            } else {
                purpose = "";
                specific = false;
            }

        }

        if (savedInstanceState == null) {

            if(purpose.equalsIgnoreCase("changeDp") && specific)
            fragment = new SetProfilePicture();
            else if(purpose.equalsIgnoreCase("changeCover") && specific)
                fragment = new SetBackground();
            else
            {
                fragment = new SetProfilePicture();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
            // changeFragment(1,0);
        }

    }

    static final int REQUEST_IMAGE_CAPTURE = 1, REQUEST_VIDEO_CHOOSE = 2, REQUEST_IMAGE_CHOOSE = 3, REQUEST_VIDEO_CAPTURE = 4;
    ;

    public static void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            ((AppCompatActivity) context).startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public static void dispatchChooseImageIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((AppCompatActivity) context).startActivityForResult(intent, REQUEST_IMAGE_CHOOSE);
    }


    public static void handleDataPicker() {
        String[] menu = {"New Image", "Upload Image"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setItems(menu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:
                        //Start the camera for picture
                        dispatchTakePictureIntent();

                        break;


                    case 1:
                        dispatchChooseImageIntent();
                        break;


                    default:
                        break;


                }


            }
        })
                .setTitle("Choose Action")
                .show();


    }

    public static class SetProfilePicture extends Fragment implements View.OnClickListener

    {

        private View rootView;
        @Bind(R.id.btnChooseMedia)
        Button btnChooseMedia;
        @Bind(R.id.btnSkip)
        Button btnSkip;
        @Bind(R.id.btnSave)
        Button btnSave;
        @Bind(R.id.imgprofilepix)
        ImageView imgprofilepix;

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.choosemedia, menu);
            menu.getItem(0).setVisible(false);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId())
            {

                case R.id.sendItem:

                    break;

                case R.id.rotateclockItem:
                    if(imageBitmap!=null) {
                        imageBitmap = AppBackBoneClass.rotateImage(imageBitmap, 90);
                        imgprofilepix.setImageBitmap(imageBitmap);
                        try {
                            ByteArrayOutputStream _bs = new ByteArrayOutputStream();
                            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, _bs);
                            AppBackBoneClass.savetempFile(AppBackBoneClass.files_temp.getAbsolutePath() + "/" + tempFilename, _bs, "img");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
            return super.onOptionsItemSelected(item);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.activity_userprofilepicssettings, null);
            ButterKnife.bind(this, rootView);
            setHasOptionsMenu(true);
            profilePix = imgprofilepix;
            imageBitmap=null;
            btnChooseMedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleDataPicker();
                }
            });
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(imageBitmap !=null)
                    {
                        UserProfileSettingPage.saveProfilePicture(1);
                    }

                }
            });
            btnSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(specific)
                    {
                        getActivity().finish();
                    }
                    else
                    {


                    fragment = new SetBackground();
                    Bundle bundle = new Bundle();
                    bundle.putString("","");
                    fragment.setArguments(bundle);
                            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                    }
                }
            });
            return rootView;
        }

        @Override
        public void onClick(View v) {

        }
    }




    public static ImageView profilePix;
    public static class SetBackground extends Fragment {
        private View rootView;
        @Bind(R.id.btnChooseMedia)
        Button btnChooseMedia;
        @Bind(R.id.btnSkip)
        Button btnSkip;
        @Bind(R.id.btnSave)
        Button btnSave;
        @Bind(R.id.imgprofilepix)
        ImageView imgprofilepix;



        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.activity_userprofilepicssettings, null);
            ButterKnife.bind(this, rootView);
            imageBitmap=null;
            profilePix = imgprofilepix;
            btnChooseMedia.setText("Choose Cover Image");
            btnChooseMedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleDataPicker();
                }
            });
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(imageBitmap !=null) {
                        UserProfileSettingPage.saveProfilePicture(3);
                    }
                }
            });
            btnSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(specific)
                    {
                        getActivity().finish();
                    }
                    else {
                        Intent intent = new Intent(context, UserPreference_activity.class);
                        (context).startActivity(intent);
                        ((AppCompatActivity) context).finish();
                    }
                }
            });
            return rootView;
        }

    }

    static String url= AppBackBoneClass.parentUrL+ AppBackBoneClass.uploadUserProfile;

    private static void saveProfilePicture(final int state) {


        ByteArrayOutputStream _bs = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, _bs);
        AppBackBoneClass.savetempFile(AppBackBoneClass.files_temp.getAbsolutePath() + "/" + tempFilename + "img.jpg", _bs, "img");

        File upload= new File(AppBackBoneClass.files_temp.getAbsolutePath() + "/" + tempFilename + "img.jpg");
        pdialog= new ProgressDialog(AppBackBoneClass.context);
        pdialog.setMessage("Please wait...");
        pdialog.setCancelable(false);
        pdialog.show();
        Ion.with(context)
                .load(url)
                .setMultipartParameter("name",upload.getName())
                .setMultipartParameter("userID", AppBackBoneClass.getUserId())
                .setMultipartParameter("reason", (state < 2) ? "avatar" : "background")
                .setMultipartFile("data", upload)

                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            Toast.makeText(context, "Error Uploading", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            return;
                        } else {
                            Log.e("ERRORUPLOAD", result);
                            pdialog.dismiss();

                            //Toast.makeText(context,  result, Toast.LENGTH_SHORT).show();
                            AppUser user = AppUser.processUserJson(result);

                            if (user != null) {

                                AppBackBoneClass.setUserJson(result);
                                if (specific) {
                                    ((Activity)context).finish();
                                } else {

                                    if (state < 2) {
                                        fragment = new SetBackground();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("", "");
                                        fragment.setArguments(bundle);
                                        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                                    } else {
                                        Intent intent = new Intent(context, UserPreference_activity.class);
                                        (context).startActivity(intent);
                                        ((AppCompatActivity) context).finish();
                                    }
                                }

                            }


                        }
                    }
                });

    }




static  File filename;
    static  Bitmap imageBitmap=null;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {





        try {

            if (data != null) {
                //Toast.makeText(ShowOff_startPage.this, ""+requestCode+" "+" "+ RESULT_OK, Toast.LENGTH_SHORT).show();
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode ==   RESULT_OK) {
                    Intent chooseMedia = new Intent(
                            this, ChooseMedia.class
                    );

                    Bundle extras = data.getExtras();
                   imageBitmap = (Bitmap) extras.get("data");
                    Bundle bundle = new Bundle();

                    Bitmap _bitmap; // your bitmap
                    ByteArrayOutputStream _bs = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, _bs);
                    handleImageBitmap( imageBitmap);
                }
                else if (requestCode ==REQUEST_IMAGE_CHOOSE && resultCode ==   RESULT_OK) {


                    Uri selectedVideoUri = data.getData();
                    String[] projection = {MediaStore.MediaColumns.DATA};
                    CursorLoader cursorLoader = new CursorLoader(  this, selectedVideoUri, projection, null, null, null);
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
                           imageBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            ByteArrayOutputStream _bs = new ByteArrayOutputStream();
                            imageBitmap .compress(Bitmap.CompressFormat.JPEG, 90, _bs);
                            handleImageBitmap(imageBitmap );
                        }
                        else
                        {
                            Toast.makeText(  this,"File too large",Toast.LENGTH_LONG).show();
                        }
                    }
                    //startActivity(chooseMedia);
                }


            }
            else {
                //Toast.makeText(ShowOff_startPage.this, "NUll", Toast.LENGTH_SHORT).show();
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    private void handleImageBitmap(Bitmap bs) {
        if(profilePix!=null)
        {
            profilePix.setImageBitmap(bs);
        }
    }


    private Resources res;
   static String tempFilename="tempfile_";
}


