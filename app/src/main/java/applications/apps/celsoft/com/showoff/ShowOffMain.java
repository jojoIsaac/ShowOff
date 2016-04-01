package applications.apps.celsoft.com.showoff;

import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;

import static applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass.context;

/**
 * Created by User on 3/14/2016.
 */
public class ShowOffMain extends AppCompatActivity {

    String tempFilename="tempfile_";
    public void handleDataPicker() {
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





    public  void dispatchTakeVideoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    public  void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    public  void dispatchChooseImageIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_CHOOSE);
    }

    public  void dispatchChooseVideoIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_VIDEO_CHOOSE);
    }

    final int REQUEST_IMAGE_CAPTURE = 1,REQUEST_VIDEO_CHOOSE = 2,REQUEST_IMAGE_CHOOSE = 3,REQUEST_VIDEO_CAPTURE = 4;;

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
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    Bundle bundle = new Bundle();

                    Bitmap _bitmap; // your bitmap
                    ByteArrayOutputStream _bs = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, _bs);
                    chooseMedia.putExtra("byteArrayImage", _bs.toByteArray());
                    AppBackBoneClass.savetempFile(AppBackBoneClass.files_temp.getAbsolutePath() + "/" + tempFilename + "img.jpg", _bs, "img");
                    startActivity(chooseMedia);


                } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode ==   RESULT_OK) {
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
                            Intent setFullscreen = new Intent(  this, VideoPlayer.class);
                            setFullscreen.putExtra("URL", selectedVideoPath);
                            setFullscreen.putExtra("CURP", "0");
                            setFullscreen.putExtra("FILE_NAME", file.getName());
                            setFullscreen.putExtra("REASON","UPLOAD");
                            startActivity(setFullscreen);
                        }
                        else
                        {
                            Toast.makeText(this, "File too large", Toast.LENGTH_LONG).show();
                        }
                    }



                } else if (requestCode == REQUEST_VIDEO_CHOOSE && resultCode ==   RESULT_OK) {
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
                            Intent setFullscreen = new Intent(  this, VideoPlayer.class);
                            setFullscreen.putExtra("URL", selectedVideoPath);
                            setFullscreen.putExtra("CURP", "0");
                            setFullscreen.putExtra("FILE_NAME", file.getName());
                            setFullscreen.putExtra("REASON","UPLOAD");
                            startActivity(setFullscreen);
                        }
                        else
                        {
                            Toast.makeText(  this,"File too large",Toast.LENGTH_LONG).show();
                        }
                    }






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
                            Intent chooseMedia = new Intent(
                                    this, ChooseMedia.class
                            );
                            chooseMedia.putExtra("URL", selectedVideoPath);
                            chooseMedia.putExtra("CURP", "0");
                            chooseMedia.putExtra("FILE_NAME", file.getName());

                            startActivity(chooseMedia);
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



}
