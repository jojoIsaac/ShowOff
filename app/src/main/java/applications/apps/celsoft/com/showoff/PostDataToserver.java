package applications.apps.celsoft.com.showoff;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import applications.apps.celsoft.com.showoff.Utilities.Base64;
import applications.apps.celsoft.com.showoff.Views.ApplicationFragments;

public class PostDataToserver extends DialogFragment {
	private View rootView;
	ImageButton ibtnCancel,imbtnsendPost,btn_video_add;
	private ImageButton btnloadImage;
	private EditText edtitle;
	
	
	static File compressedfile;
	//Prarameters
	private static String imagename="",imagepath="";
	private static int RESULT_LOAD_IMAGE=90;
	 static ImageView imgprofile;
	private static ProgressDialog saveDialog;
	 String oldimage="";
		final static int PIC_CROP = 1;
		static String currentIssues="-1";
	 // Static final constants
	    private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
	    private static final int ROTATE_NINETY_DEGREES = 90;
	    private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
	    private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
	    private static final int ON_TOUCH = 1;

	    // Instance variables
	    private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
	    private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;
		private IconDrawable drawCancel;
		private IconDrawable drawSend;
		private IconDrawable drawVideo;
		private IconDrawable drawImage;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme);
		 getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout to use as dialog or embedded fragment
		rootView = inflater.inflate(R.layout.fragment_postinfo,
				container, false);
		
		final EditText body= (EditText) rootView.findViewById(R.id.body);
edtitle= (EditText) rootView.findViewById(R.id.edtitle);
		ibtnCancel=(ImageButton) rootView.findViewById(R.id.ibtnCancel);
		imbtnsendPost= (ImageButton) rootView.findViewById(R.id.imbtnsendPost);
		btnloadImage=(ImageButton) rootView.findViewById(R.id.btn_image_add);
		btn_video_add=(ImageButton) rootView.findViewById(R.id.btn_video_add);
		drawCancel = new IconDrawable(getActivity(), FontAwesomeIcons.fa_remove).color(
				Color.WHITE).actionBarSize();
		
		
		drawSend = new IconDrawable(getActivity(), FontAwesomeIcons.fa_send).color(
				Color.WHITE).actionBarSize();
		
		drawVideo = new IconDrawable(getActivity(), FontAwesomeIcons.fa_video_camera).color(
				Color.GRAY).actionBarSize();
		
		drawImage = new IconDrawable(getActivity(), FontAwesomeIcons.fa_picture_o).color(
				Color.GRAY).actionBarSize();
		
		btn_video_add.setImageDrawable(drawVideo);
		btnloadImage.setImageDrawable(drawImage);
		
		imbtnsendPost.setImageDrawable(drawSend);
		
		ibtnCancel.setImageDrawable(drawCancel);
btnloadImage.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(
	        			Intent.ACTION_PICK,
	        			
	        			android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	        	i.setType("image/*");
	        			startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
		ibtnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		
		//
imbtnsendPost.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				
				
				if(selectedBitmap!=null)
				{
				saveImageOnserver(selectedBitmap,imagename,imagepath,body.getText().toString().trim());
				
				}
				else
				{
					
				
					
					String URL_FEED = "https://nsoredevotional.com/mobile/devotionHandler.php";
				if(!TextUtils.isEmpty(body.getText().toString().trim()) )
					{
						
						// Adding request to volley request queue
						//org.apache.http.util.CharsetUtils d;
						
					
					savePost(body.getText().toString().trim());
					//	saveDialog.show();
					}
					
					
				}
				
				
				
			
				
				
			}

						});
		
		
		return rootView;
	}
	
	
	
	
	 protected void savePost(String trim) {
		// TODO Auto-generated method stub
		 saveDialog = new ProgressDialog(AppBackBoneClass.context);
			saveDialog.setMessage("Posting Infomation ...");
			saveDialog.setCancelable(false);
			saveDialog.show();
			
			
			Ion.with(getActivity())
			.load(AppBackBoneClass.feedUrl
					
					)
           .setBodyParameter("UID", AppBackBoneClass.getUserId())
.setBodyParameter("reason", "Add Issue")
.setBodyParameter("type", ApplicationFragments.currentIssues.toString())
.setBodyParameter("content", trim)
.setBodyParameter("title", edtitle.getText().toString().trim())
.setBodyParameter("appID", AppBackBoneClass.ApplicationCode)

			.asString()
			
			.setCallback(new FutureCallback<String>() {
			   @Override
			    public void onCompleted(Exception e, String result) {
			        // do stuff with the result or error
				   if(e==null)
				   {
				       Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
					   saveDialog.dismiss();
					   
					   close();
					   
				   }
				   else
				   {
				    try
				    {
				    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
					  
				    }
				    catch(Exception es)
				    {
				    
				    }
				    
					   //e.printStackTrace();
				   }
			    }
			   
			   
			});
	}




	Bitmap selectedBitmap=null;
	 
	 
		/** The system calls this only when creating the layout in a dialog. */
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final RelativeLayout root = new RelativeLayout(getActivity());
		    root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

			final Dialog dialog = new Dialog(getActivity());
		    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    dialog.setContentView(root);
		    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
		    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		    //getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 
		    return dialog;
		}
		
		
		
		
		
		
		
		
		
		
		void close()
		 {
			 dismiss();
		 }
		 private  void saveImageOnserver(final Bitmap image,final String name,final String fullpath,final String content) {
				// TODO Auto-generated method stub
				AsyncTask<Void, Integer, String> saveuserimage= new AsyncTask<Void, Integer, String>() {
					
					private InputStream is;
					ProgressDialog pdialog;
					private String filePath;
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				pdialog= new ProgressDialog(AppBackBoneClass.context);
				pdialog.setMessage("Posting to group wall ...");
				pdialog.setCancelable(false);
				pdialog.show();
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				pdialog.dismiss();
				filePath= Environment.getExternalStorageDirectory().getPath();

			//Toast.makeText(AppBackBoneClass.context, result, Toast.LENGTH_LONG).show();
				if(result.trim().contains("Data Saved"))
				{
					if(compressedfile.exists() && compressedfile.isFile())
					{
						compressedfile.delete();
					}
					
				}
				else
				{
					Toast.makeText(AppBackBoneClass.context, result + "Image update failed", Toast.LENGTH_LONG).show();
				}
				
				close();
				super.onPostExecute(result);
			}
			String data="";

					@Override
					protected String doInBackground(Void... arg0) {
						// TODO Auto-generated method stub
						
						ByteArrayOutputStream bao = new ByteArrayOutputStream();
						 
				       image.compress(Bitmap.CompressFormat.JPEG, 90, bao);
				 
				        byte[] ba = bao.toByteArray();
				 
				        String ba1 = Base64.encodeBytes(ba);
				 
				        ArrayList<NameValuePair> nameValuePairs = new

								ArrayList<NameValuePair>();
//				        params.put("GID", id);
				        nameValuePairs.add( new BasicNameValuePair("GID", currentIssues));
				        nameValuePairs.add(new BasicNameValuePair("reason","Save Nsore Group Feed"));
				        nameValuePairs.add(new BasicNameValuePair("image", ba1));
				        nameValuePairs.add(new BasicNameValuePair("UN",AppBackBoneClass.getUserDetails().get(1) ));
				        nameValuePairs.add(new BasicNameValuePair("name",name ));
				        nameValuePairs.add(new BasicNameValuePair("senderid",AppBackBoneClass.getUserId() ));
				        nameValuePairs.add( new BasicNameValuePair("userpost",content ));

				        try {
				 
				            HttpClient httpclient = new DefaultHttpClient();
				 
				            HttpPost httppost = new
				 
				            HttpPost("https://nsoredevotional.com/mobile/handlemediapost.php");
				 
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
				try
				{
				saveuserimage.execute();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
}
