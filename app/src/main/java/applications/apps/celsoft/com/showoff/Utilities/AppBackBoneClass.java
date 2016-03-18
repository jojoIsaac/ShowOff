package applications.apps.celsoft.com.showoff.Utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.util.FileUtility;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import applications.apps.celsoft.com.showoff.R;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.AppUser;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.showoffItems;

/**
 * Created by User on 1/31/2016.
 */
public class AppBackBoneClass extends ApplicationContants {

    private static Bitmap data;
    public static Context context;
    // Folder for data Storage on users phone
    public static String AppFolder = "ShowOff";
    public static DBHelper dbhelper;
    public static SharedPreferences myPrefs; // Global Shared preference


    private static String coverDefault = "COVER_LOCAL_DEFAULT";
    private static boolean coverIsLocal = true;

   // {

    public static String filePath ="";
    public static File files_temp,files_userdp,file_media_videos,file_media_images;

    public static Integer currentAppPage=0;
    public static  Integer tempCurrentPage=0;
    /*
    currentAppPage => 0: Start page,One of the Start page Fragment is showing
                      1: User profile Page
     */



    public static  void initApp()
    {
        filePath = Environment.getExternalStorageDirectory().getPath();
        files_userdp = new File(filePath + "/" + AppBackBoneClass.appParentFolder + "/userDP/");
       files_temp = new File(filePath + "/" + AppBackBoneClass.appParentFolder + "/temp/");
        file_media_videos = new File(filePath + "/" + AppBackBoneClass.appParentFolder + "/media/videos/");

        file_media_images = new File(filePath + "/" + AppBackBoneClass.appParentFolder + "/media/images/");

        if (!files_userdp.exists() ) {

            files_userdp.mkdirs();
            // Log.d("D",files.getAbsolutePath());
        }

        if (!files_temp.exists() ) {

            files_temp.mkdirs();
            // Log.d("D",files.getAbsolutePath());
        }


        if (!file_media_videos.exists() ) {

            file_media_videos.mkdirs();
            // Log.d("D",files.getAbsolutePath());
        }

        if (!file_media_images.exists() ) {

            file_media_images.mkdirs();
            // Log.d("D",files.getAbsolutePath());
        }

        getUserCurrentDetails();
    }

    public static Bitmap getBitmapFromResources(Resources resources, int resImage) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inSampleSize = 1;
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        return BitmapFactory.decodeResource(resources, resImage, options);
    }


    public static void getUserCurrentDetails()
    {
        //Check current friend Status
        Ion.with(context)
                .load(AppBackBoneClass.parentUrL + AppBackBoneClass.feedUrl
                )
                .setBodyParameter("reason", "Get User Details")

                .setBodyParameter("userID", AppBackBoneClass.getUserId())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        // if(e==null)
                        // {
                        if(e!=null)
                        {
                            e.printStackTrace();
                        }
                        else {

                            if (!result.equalsIgnoreCase("No Data Found") || !result.equalsIgnoreCase("Err")) {
                                AppUser user= AppUser.processUserJson(result);
                                if(user!=null)
                                  AppBackBoneClass.setUserJson(result);
                                else
                                    AppBackBoneClass.setUserJson("");
                            }
                        }
                        //}
                    }
                });
    }

    public static String generateRandomString(String basestr)
    {
        String uuid = UUID.randomUUID().toString();

        return basestr+uuid.substring(0,9).replace("-","").trim();
    }

    public static void savetempFile(String filePath,ByteArrayOutputStream data,String fileType)
    {
        try {
            FileOutputStream tempFile = new FileOutputStream(filePath);
            tempFile.write(data.toByteArray());
            tempFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Functions to handle some dates issues
    public static String getDate() {
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyy");
        Date date = new Date();
        String c = "";

        c = ft.format(date);

        return c;
    }

    public static String getDay() {
        SimpleDateFormat ft = new SimpleDateFormat("dd");
        Date date = new Date();
        String c = "";

        c = ft.format(date);

        return c;
    }

    public static String getYear() {
        SimpleDateFormat ft = new SimpleDateFormat("yyy");
        Date date = new Date();
        String c = "";

        c = ft.format(date);

        return c;
    }

    public static String getMonth() {
        SimpleDateFormat ft = new SimpleDateFormat("MM");
        Date date = new Date();
        String c = "";

        c = ft.format(date);

        return c;
    }

    public static String getdate(String format) {
        SimpleDateFormat ft = new SimpleDateFormat(format);
        Date date = new Date();
        String c = "";

        c = ft.format(date);

        return c;
    }

//}



    @SuppressLint("NewApi")
    public static void toggleBackground(boolean b,View baselayout)
    {
        if(b)
        {

            baselayout.setBackgroundColor( AppBackBoneClass.context.getResources().getColor(R.color.ColorPrimary));
            //Toast.makeText(context, "LOL",Toast.LENGTH_LONG).show();
        }
        else
        {
            data=  BitmapFactory.decodeResource(AppBackBoneClass.context.getResources(), R.mipmap.ic_launcher);
            //data=	scaleBitmap(data, layout_cover_photo.getWidth(), layout_cover_photo.getHeight()/2);
            Drawable img = new BitmapDrawable(AppBackBoneClass.context.getResources(),data);
            baselayout.setBackground(img);
        }
    }


    /**
     * This function randomly picks and image to set as the default user Header Banner
     * @param name
     * @return
     */
    public static Integer getDefaultCoverIndex(String name)
    {
        int index=-1;
        String [] coverArray= {"back1.jpg","back2.png","back3.jpg"
                ,"back4.png","back5.png","back6.png","back7.jpg"
                ,"back8.jpg","back9.jpg","back10.png","back11.png","back12.png"
        };
        // public static Favorite respondedDriver=null;


        for (int i = 0; i < coverArray.length; i++) {
            if(coverArray[i].equalsIgnoreCase(name))
            {
                index=i;
                return index;

            }
            else
            {
                index=-1;
            }
            //coverImageList.add(coverArray[i]);
        }

        return index;
    }

    /**
     * This function opens the connection to the sqlite database
     * @param context
     */
    public static void openDB(Context context) {
       dbhelper = new DBHelper(context);
    }

    /**
     * This function closes the sqllite DB connection
     */
    public static void closeDB() {
        dbhelper.close();

    }
    // Below classes are for persisting data on to the users phone
    public static void setUserJson(String value) {
        SharedPreferences.Editor edits = AppBackBoneClass.myPrefs.edit();
        edits.putString(pref_UserJson, value);

        edits.commit();
    }
    //pref_Json
    public static void setCoverISDefault(String value) {
        SharedPreferences.Editor edits = AppBackBoneClass.myPrefs.edit();
        edits.putString(coverDefault, value);

        edits.commit();
    }
    public static String getDefaultCover() {
        String val = AppBackBoneClass.myPrefs.getString(coverDefault, "");
        return val;
    }
    public static String getFaceaccountusedalready() {
        String val = AppBackBoneClass.myPrefs.getString(faceAccountUsedAlready, "");
        return val;
    }


    // functions to manipulate specific data in the Sharedpreference
    public static void setUserInfo(String name, String email,
                                   String phone_number, String username) {
        SharedPreferences.Editor edits = myPrefs.edit();
        edits.putString(pref_Name, name);

        edits.putString(pref_UserName, username);

        edits.putString(pref_Email, email);

        edits.putString(pref_Phone, phone_number);

        edits.commit();
    }


    public static void setUserCover(String cover) {
        SharedPreferences.Editor edits = myPrefs.edit();

        edits.putString(pref_UserCover, cover);

        edits.commit();
    }

    public static String getUserCover() {
        String val = "";
        val = myPrefs.getString(pref_UserCover, "");
        return val;
    }
    public static String getUserJson() {
        String val = "";
        val = myPrefs.getString(pref_UserJson, "");
        return val;
    }

    public static void setUserName(String username) {
        SharedPreferences.Editor edits = myPrefs.edit();

        edits.putString(pref_UserName, username);

        edits.commit();
    }

    public static List<String> getUserDetails() {
        List<String> user = new ArrayList<String>();
        String val = "";
        val = myPrefs.getString(pref_Name, "");
        user.add(val);
        val = myPrefs.getString(pref_UserName, "");
        user.add(val);
        val = myPrefs.getString(pref_Email, "");
        user.add(val);
        val = myPrefs.getString(pref_Phone, "");
        user.add(val);
        return user;
    }

    public static void setUserdp(String userdp) {
        // pref_BranchCount
        SharedPreferences.Editor edits =myPrefs.edit();
        edits.putString(USERDP, userdp);
        edits.commit();

    }

    public static String getUserdp() {
        String val = "";
        val = myPrefs.getString(USERDP, "NOT_SET");
        return val;
    }


    public static void setUserID(Context c, String ID) {
        SharedPreferences.Editor edits = myPrefs.edit();
        edits.putString(pref_USER_ID, ID);
        edits.commit();
    }

    public static String getUserId() {
        String val = "";
        val = myPrefs.getString(pref_USER_ID, "0");
        return val;
    }


    /**
	 * Sets the font on all TextViews in the ViewGroup. Searches recursively for
	 * all inner ViewGroups as well. Just add a check for any other views you
	 * want to set as well (EditText, etc.)
	 */
    public static void setFont(ViewGroup group, Typeface font) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof EditText
                    || v instanceof Button) {
                ((TextView) v).setTypeface(font);
            } else if (v instanceof ViewGroup)
                setFont((ViewGroup) v, font);
        }
    }


    public static void setIsSplashDone(boolean b) {
        SharedPreferences.Editor edits = myPrefs.edit();
        edits.putBoolean(SPLASH_DONE, b);
        edits.commit();
    }

//Check the login status of the user
    public static void setisLoggedIn(Context c, boolean name) {
        SharedPreferences.Editor edits =myPrefs.edit();
        edits.putBoolean(IS_LOGGED_IN, name);
        edits.commit();
    }

    public static Boolean getisLoggedIn() {
        Boolean val = false;
        val = myPrefs.getBoolean(IS_LOGGED_IN, false);
        return val;
    }








    public static Bitmap download_Image(String url, String destination) {

        Bitmap bmp = null;
        try {
            URL ulrn = new URL(url);
            HttpURLConnection con = (HttpURLConnection) ulrn.openConnection();
            InputStream is = con.getInputStream();
            bmp = BitmapFactory.decodeStream(is);
            if (null != bmp) {
                // imagepath= filePath+
                // "/"+Connector.AppFolder+"/church_logo.jpg";

                // FileOutputStream fout= new FileOutputStream(destination);
                ByteArrayOutputStream bstream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.JPEG, 70, bstream);
                File f = new File(destination);
                f.createNewFile();
                FileOutputStream fouts = new FileOutputStream(f);
                fouts.write(bstream.toByteArray());
                fouts.close();
                is.close();

                return bmp;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    public static void handlePostShare(showoffItems issues, Context context) {

    }


    public static class EmailFormatValidator {

        private Pattern pattern;
        private Matcher matcher;

        private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        public EmailFormatValidator() {
            pattern = Pattern.compile(EMAIL_PATTERN);
        }

        public boolean validate(final String email) {

            matcher = pattern.matcher(email);
            return matcher.matches();

        }
    }


    public static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size/2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
    public static void loadUserImage(final ImageView imageView,String url,Context context,Drawable onErrorImage)
    {
      Picasso.with(context)

                .load(url)

                .transform(new CircleTransform())
                .error(onErrorImage)
                .placeholder(onErrorImage)
              .memoryPolicy(MemoryPolicy.NO_CACHE)


                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        if(((BitmapDrawable) imageView.getDrawable()).getBitmap()!=null) {
                           /*
                            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                            bitmap = sharpenImage(bitmap, 50);
                            imageView.setImageBitmap(bitmap);
                            */
                        }
                    }

                    @Override
                    public void onError() {

                    }
                })

                ;



    }

    public static Bitmap rotateImage(Bitmap src, float degree) {
        // create new matrix object
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(degree);
        // return new bitmap rotated using matrix
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }


    public static Bitmap sharpenImage(Bitmap src, double weight) {
        // set sharpness configuration
        double[][] SharpConfig = new double[][] {
                { 0 , -2    , 0  },
                { -2, weight, -2 },
                { 0 , -2    , 0  }
        };
        //create convolution matrix instance
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        //apply configuration
        convMatrix.applyConfig(SharpConfig);
        //set weight according to factor
        convMatrix.Factor = weight - 8;
        return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
    }


    public static void DownloadImageFiles(String url,String filename, final Context context, final View Intendedview) {
        String filePath = AppBackBoneClass.file_media_images.getAbsolutePath() + filename;
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            if(file!=null) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (bitmap!=null && Intendedview!=null)
                {
                    Intendedview.setBackground(new BitmapDrawable(context.getResources(), bitmap));
                }
            }
        }
        else
        {
            Ion.with(context)
                    .load(url)

                    .write(new File(filePath))
                    .setCallback(new FutureCallback<File>() {
                        @Override
                        public void onCompleted(Exception e, File file) {
                            // download done...
                            // do stuff with the File or error

                            if(file!=null) {
                                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                if (bitmap!=null&& Intendedview!=null)
                                {
                                    Intendedview.setBackground(new BitmapDrawable(context.getResources(), bitmap));
                                }
                            }
                        }
                    });
        }
    }



    // Functions Handling server interaction
    public static String processStream(InputStream ins) {
        // InputStream ins = response.getEntity().getContent();
        if (ins != null) {
            String line = "";
            StringBuffer sb = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    ins));
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return sb.toString();
        }
        return "";
    }



    static  String friendStatus = "-100";
    public static String toggleFriendshipStatus(AppUser userObject)
    {

        Ion.with(context)
                .load(AppBackBoneClass.parentUrL + AppBackBoneClass.feedUrl
                )
                .setBodyParameter("reason", "Toggle Friendship")
                .setBodyParameter("currentStatus",userObject.getConnected())
                .setBodyParameter("friendID",userObject.getUserID())
                .setBodyParameter("userID",AppBackBoneClass.getUserId())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if(e==null)
                        {

                            if(!result.equalsIgnoreCase("Error") || !result.equalsIgnoreCase("Err"))
                            try {
                                JSONObject object = new JSONObject(result);
                                if (object!=null) {
                                    if(object.optString("status").equalsIgnoreCase("Success"))
                                    {
                                        friendStatus = object.optString("Fstatus", "-1");

                                    }

                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
        return friendStatus;
    }


    public static JSONObject updateJsonObject(JSONObject object,String key,String newValue)
    {
        try {
            object.remove(key);
            object.put(key , newValue);
            return object;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return object;
    }

    public static JSONObject function(JSONObject obj, String keyMain,String valueMain, String newValue) throws Exception {
        // We need to know keys of Jsonobject
        JSONObject json = new JSONObject();
        Iterator iterator = obj.keys();
        String key = null;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            // if object is just string we change value in key
            if ((obj.optJSONArray(key)==null) && (obj.optJSONObject(key)==null)) {
                if ((key.equals(keyMain)) && (obj.get(key).toString().equals(valueMain))) {
                    // put new value
                    obj.put(key, newValue);
                    return obj;
                }
            }

            // if it's jsonobject
            if (obj.optJSONObject(key) != null) {
                function(obj.getJSONObject(key), keyMain, valueMain, newValue);
            }

            // if it's jsonarray
            if (obj.optJSONArray(key) != null) {
                JSONArray jArray = obj.getJSONArray(key);
                for (int i=0;i<jArray.length();i++) {
                    function(jArray.getJSONObject(i), keyMain, valueMain, newValue);
                }
            }
        }
        return obj;
    }
 public static class ConvolutionMatrix
 {
     public static final int SIZE = 3;

     public double[][] Matrix;
     public double Factor = 1;
     public double Offset = 1;

     //Constructor with argument of size
     public ConvolutionMatrix(int size) {
         Matrix = new double[size][size];
     }

     public void setAll(double value) {
         for (int x = 0; x < SIZE; ++x) {
             for (int y = 0; y < SIZE; ++y) {
                 Matrix[x][y] = value;
             }
         }
     }

     public void applyConfig(double[][] config) {
         for(int x = 0; x < SIZE; ++x) {
             for(int y = 0; y < SIZE; ++y) {
                 Matrix[x][y] = config[x][y];
             }
         }
     }

     public static Bitmap computeConvolution3x3(Bitmap src, ConvolutionMatrix matrix) {
         int width = src.getWidth();
         int height = src.getHeight();
         Bitmap result = Bitmap.createBitmap(width, height, src.getConfig());

         int A, R, G, B;
         int sumR, sumG, sumB;
         int[][] pixels = new int[SIZE][SIZE];

         for(int y = 0; y < height - 2; ++y) {
             for(int x = 0; x < width - 2; ++x) {

                 // get pixel matrix
                 for(int i = 0; i < SIZE; ++i) {
                     for(int j = 0; j < SIZE; ++j) {
                         pixels[i][j] = src.getPixel(x + i, y + j);
                     }
                 }

                 // get alpha of center pixel
                 A = Color.alpha(pixels[1][1]);

                 // init color sum
                 sumR = sumG = sumB = 0;

                 // get sum of RGB on matrix
                 for(int i = 0; i < SIZE; ++i) {
                     for(int j = 0; j < SIZE; ++j) {
                         sumR += (Color.red(pixels[i][j]) * matrix.Matrix[i][j]);
                         sumG += (Color.green(pixels[i][j]) * matrix.Matrix[i][j]);
                         sumB += (Color.blue(pixels[i][j]) * matrix.Matrix[i][j]);
                     }
                 }

                 // get final Red
                 R = (int)(sumR / matrix.Factor + matrix.Offset);
                 if(R < 0) { R = 0; }
                 else if(R > 255) { R = 255; }

                 // get final Green
                 G = (int)(sumG / matrix.Factor + matrix.Offset);
                 if(G < 0) { G = 0; }
                 else if(G > 255) { G = 255; }

                 // get final Blue
                 B = (int)(sumB / matrix.Factor + matrix.Offset);
                 if(B < 0) { B = 0; }
                 else if(B > 255) { B = 255; }

                 // apply new pixel
                 result.setPixel(x + 1, y + 1, Color.argb(A, R, G, B));
             }
         }

         // final image
         return result;
     }

 }
}
