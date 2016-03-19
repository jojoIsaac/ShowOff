package applications.apps.celsoft.com.showoff;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import applications.apps.celsoft.com.showoff.Utilities.Base64;
import applications.apps.celsoft.com.showoff.Views.ApplicationFragments;
import butterknife.Bind;
import me.drakeet.materialdialog.MaterialDialog;

import static applications.apps.celsoft.com.showoff.ChooseMedia.categories;

/**
 * Created by User on 2/8/2016.
 */
public class ChooseMedia extends AppCompatActivity {


    @Bind(R.id.mediaController)
    MediaController mediaController;
    String dataname;
    static String tempFilename="tempfile_";
    private String filePath="";
    ImageView imgdata;
    private RotateAnimation rotate;
    private static String fileType="";
    static Bitmap _bitmap;
    static Spinner cat_spinner;
    String imageURL="";
    private ProgressDialog pdialog;
    private File Imagefile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        AppBackBoneClass.myPrefs= getSharedPreferences(AppBackBoneClass.prefernceName, Context.MODE_PRIVATE);
        AppBackBoneClass.context=this;

        filePath = Environment.getExternalStorageDirectory().getPath();
        loadCategories();
        if(getIntent().hasExtra("byteArrayImage")) {

          _bitmap = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArrayImage"), 0, getIntent().getByteArrayExtra("byteArrayImage").length);
            dataname= getIntent().getExtras().getString("name");
            setContentView(R.layout.activity_choose_media);
            fileType= "img";
            tempFilename= tempFilename+"_img.jpg";

            imgdata= (ImageView) findViewById(R.id.imgdata);

            imgdata.setImageBitmap(_bitmap);




        }
        else if (getIntent().hasExtra("URL"))
        {
            Imagefile= new File(getIntent().getStringExtra("URL"));
            if(Imagefile!=null)
            {
                setContentView(R.layout.activity_choose_media);
                fileType= "img";
                try {
                    _bitmap = BitmapFactory.decodeFile(Imagefile.getAbsolutePath());
                    imgdata = (ImageView) findViewById(R.id.imgdata);

                    imgdata.setImageBitmap(_bitmap);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }

        else  if(getIntent().hasExtra("byteArrayVideo"))
        {

            dataname= getIntent().getExtras().getString("name");
            setContentView(R.layout.activity_choose_media_video);
            fileType= "video";

        }

        categories= new ArrayList<String>();
        categoryIDs  = new ArrayList<String>();







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.choosemedia, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {

            case R.id.sendItem:
                sendFile("");
                break;

                case R.id.rotateclockItem:
                    _bitmap= AppBackBoneClass.rotateImage(_bitmap,90);
                   imgdata.setImageBitmap(_bitmap);
                   try
                   {
                       ByteArrayOutputStream _bs = new ByteArrayOutputStream();
                       _bitmap.compress(Bitmap.CompressFormat.JPEG, 90, _bs);
                       AppBackBoneClass.savetempFile(AppBackBoneClass.files_temp.getAbsolutePath()+"/"+tempFilename, _bs, "img");
                   }
                   catch (Exception e)
                   {
                       e.printStackTrace();
                   }
                    break;
        }
        return super.onOptionsItemSelected(item);
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

                       if(e==null) {
                           Log.e("Cate",result);
                           try {
                               categories = new ArrayList<String>();
                               categoryIDs = new ArrayList<String>();
                               processCategoryJson(result);
                           } catch (JSONException e1) {
                               e1.printStackTrace();
                           }

                       }
                   }
               });



   }
    static ArrayAdapter<String> dataAdapter;
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

    FragmentManager fm = getSupportFragmentManager();
    private void sendFile(String type) {




        showBragNowDialog();






    }




    private void saveImageOnServer(final Bitmap images,final String fileType,final String fullpath,final String content) {
       // String url= AppBackBoneClass.parentUrL+ AppBackBoneClass.videoUploader;

        if(Imagefile.exists() && Imagefile.isFile()) {
            File upload = new File(fullpath);
            Integer val = cat_spinner.getSelectedItemPosition();
            String category = categoryIDs.get(val);
            final String finalCategory = category;
            pdialog = new ProgressDialog(AppBackBoneClass.context);
            pdialog.setMessage("Please wait. Time to showOff...");
            pdialog.setCancelable(false);
            pdialog.show();

            String userID = AppBackBoneClass.getUserId();
            String url = AppBackBoneClass.parentUrL + AppBackBoneClass.uploadUrl;

            Ion.with(this)
                    .load(url)
                    .setMultipartParameter("name", AppBackBoneClass.generateRandomString(userID + fileType) + ".jpg")
                    .setMultipartParameter("UN", AppBackBoneClass.getUserDetails().get(1))
                    .setMultipartParameter("userID", AppBackBoneClass.getUserId())
                    .setMultipartParameter("data_cate", finalCategory)
                    .setMultipartParameter("userpost", content)
                    .setMultipartParameter("data_type", fileType)
                    .setMultipartFile("data", Imagefile)

                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            if (e != null) {
                                Toast.makeText(ChooseMedia.this, "Error Uploading", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                                return;
                            } else {
                                Log.e("ERRORUPLOAD", result);
                                pdialog.dismiss();
                                ApplicationFragments.layout_newStory.setVisibility(View.VISIBLE);
                                Toast.makeText(ChooseMedia.this, result, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });

        }
    }






    private static void saveImageOnservers(final Bitmap images,final String fileType,final String fullpath,final String content) {
        // TODO Auto-generated method stub
       // final File file= new File(AppBackBoneClass.files_temp.getAbsolutePath() + "/" + tempFilename);
        String category="";

        Integer val= cat_spinner.getSelectedItemPosition();
        category= categoryIDs.get(val);
        final String finalCategory = category;
        AsyncTask<Void, Integer, String> saveuserimage= new AsyncTask<Void, Integer, String>() {

            private InputStream is;
            ProgressDialog pdialog;
            private String filePath;
            String userID= AppBackBoneClass.getUserId();
            String url= AppBackBoneClass.parentUrL+ AppBackBoneClass.uploadUrl;
            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                pdialog= new ProgressDialog(AppBackBoneClass.context);
                pdialog.setMessage("Please wait. Time to showOff...");
                pdialog.setCancelable(false);
                pdialog.show();
                super.onPreExecute();


            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                pdialog.dismiss();
                filePath= Environment.getExternalStorageDirectory().getPath();

                if(result.trim().contains("Data Saved"))
                {
                    //if(file.exists() && file.isFile())
                   // {
                    //    file.delete();
                    //}
                    Toast.makeText(AppBackBoneClass.context, "Saved", Toast.LENGTH_LONG).show();
                    ApplicationFragments.layout_newStory.setVisibility(View.VISIBLE);
                            ((Activity) AppBackBoneClass.context).finish();
                }
                else
                {
                    Toast.makeText(AppBackBoneClass.context, result+"Image update failed", Toast.LENGTH_LONG).show();
                }
                super.onPostExecute(result);
            }
            String data="";

            @Override
            protected String doInBackground(Void... arg0) {
                // TODO Auto-generated method stub

                ByteArrayOutputStream bao = new ByteArrayOutputStream();

                images.compress(Bitmap.CompressFormat.JPEG, 90, bao);

                byte[] ba = bao.toByteArray();

                String ba1 = Base64.encodeBytes(ba);

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair> ();
//	        params.put("GID", id);
                /*
                .setMultipartParameter("data_type", fileType)
                .setMultipartParameter("userID", userID)
                .setMultipartParameter("name", AppBackBoneClass.generateRandomString(userID + fileType) + ".jpg")
                .setMultipartFile("data",file )
                 */


                nameValuePairs.add(new BasicNameValuePair("data_type", fileType));
                nameValuePairs.add(new BasicNameValuePair("data_cate", finalCategory));
                nameValuePairs.add(new BasicNameValuePair("data", ba1));
                nameValuePairs.add(new BasicNameValuePair("UN",AppBackBoneClass.getUserDetails().get(1) ));
                nameValuePairs.add(new BasicNameValuePair("name",AppBackBoneClass.generateRandomString(userID + fileType) + ".jpg" ));
                nameValuePairs.add(new BasicNameValuePair("userID",AppBackBoneClass.getUserId() ));
                nameValuePairs.add( new BasicNameValuePair("userpost",content ));
	        /*
	         * 	params.put("GID", id);
							 params.put("senderid", Connector.getUserId());
							 params.put("UN", Connector.getUserDetails().get(1));
							params.put("userpost", body.getText().toString());

							params.put("reason","Save Nsore Group Feed");
	         */
                //userID


                try {

                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new

                            HttpPost(url);

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpclient.execute(httppost);

                    HttpEntity entity = response.getEntity();

                    is = (InputStream) entity.getContent();
                    data= AppBackBoneClass.processStream(is);
                    //is = (InputStream) entity.getContent();
                    return data;
                } catch (Exception e) {

                    Log.e("log_tag", "Error in http connection " + e.toString());

                }



                return data;
            }
        };

        saveuserimage.execute();
    }

    MaterialDialog mMaterialDialog;
    void showBragNowDialog()
    {
        View rootView = getLayoutInflater(). inflate(R.layout.video_detail,null,
                false);
        final EditText edt_brag = (EditText) rootView.findViewById(R.id.edt_brag);
        //Toast.makeText(ChooseMedia.this, categories.size()+"", Toast.LENGTH_SHORT).show();
        cat_spinner = (Spinner) rootView.findViewById(R.id.cat_spinner);
        cat_spinner.setAdapter(dataAdapter);


         mMaterialDialog = new MaterialDialog(this)
                .setTitle("MaterialDialog")
               .setView(rootView)
                .setPositiveButton("BRAG NOW!", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if( Imagefile.exists() && Imagefile.isFile())
                          saveImageOnServer(_bitmap, fileType,  Imagefile.getAbsolutePath(), edt_brag.getText().toString());

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


}
