package applications.apps.celsoft.com.showoff;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.w3c.dom.Text;

import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.AppUser;
import butterknife.Bind;
import butterknife.ButterKnife;

import static applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass.context;

/**
 * Created by User on 2/26/2016.
 */
public class EditAboutUser extends AppCompatActivity{

    @Bind(R.id.edtFullname)
    EditText edtFullname;
    @Bind(R.id.edtphonenumber)
    EditText edtphonenumber;
    @Bind(R.id.edtconfpassword)
    EditText edtconfpassword;
    @Bind(R.id.edtpassword)
   EditText edtpassword;
    @Bind(R.id.edtusername)
   EditText edtusername;
    @Bind(R.id.edtlocation)
   EditText edtlocation;
    @Bind(R.id.edtemail)
     EditText edtemail;
    @Bind(R.id.edtaboutme)
    EditText edtaboutme;
    @Bind(R.id.layout_password)
    LinearLayout layout_password;
    @Bind(R.id.layout_confpassword)
    LinearLayout layout_confpassword;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btnSave)
    Button btnSave;
    private ProgressDialog saveDialog;

    public static String accountState="edit";
    String AppName_fontPathfacefear = "fonts/Face Your Fears.ttf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_about_useredit);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        context = this;
        AppBackBoneClass.myPrefs = this.getSharedPreferences(AppBackBoneClass.prefernceName,
                Context.MODE_PRIVATE);
        AppBackBoneClass.initApp();
        Typeface tf = Typeface.createFromAsset(getAssets(), AppName_fontPathfacefear);

btnSave.setTypeface(tf);
        if(getIntent().hasExtra("state"))
        {
            String state= getIntent().getStringExtra("state");
            if(state.equalsIgnoreCase("register"))
            {
                accountState = "register";
            }
            else
            {
                accountState ="edit";
            }
        }
        else
        {
            accountState ="edit";
        }


        if(!accountState.equalsIgnoreCase("register")&&AppBackBoneClass.getUserJson()!=null && !TextUtils.isEmpty(AppBackBoneClass.getUserJson()))
        {
            setUpUserDetails(AppBackBoneClass.getUserJson());
        }

btnSave.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(edtFullname.getText().toString())) {
            Toast.makeText(EditAboutUser.this, "Please provide your fullname", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(edtemail.getText().toString())) {
            Toast.makeText(EditAboutUser.this, "Please provide your email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(edtphonenumber.getText().toString())) {
            Toast.makeText(EditAboutUser.this, "Please provide your phone number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(edtusername.getText().toString())) {
            Toast.makeText(EditAboutUser.this, "Please provide your user name", Toast.LENGTH_SHORT).show();
        } else if (AppBackBoneClass.getUserJson() == null && TextUtils.isEmpty(AppBackBoneClass.getUserJson())) {
            if (TextUtils.isEmpty(edtpassword.getText().toString())) {
                Toast.makeText(EditAboutUser.this, "Please provide your password", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(edtconfpassword.getText().toString())) {
                Toast.makeText(EditAboutUser.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
            }
            if (!edtpassword.getText().toString().equalsIgnoreCase(edtconfpassword.getText().toString())) {
                Toast.makeText(EditAboutUser.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            }
        } else {
            saveUserInfo();
        }
    }
});

        if(!accountState.equalsIgnoreCase("register"))
           fetchUserInfo();

    }

    private void fetchUserInfo()
    {
       Ion.with(this).load(AppBackBoneClass.parentUrL+AppBackBoneClass.feedUrl)
               .setBodyParameter("reason","Get User Details")
               .setBodyParameter("userID",AppBackBoneClass.getUserId())
               .asString()
               .setCallback(new FutureCallback<String>() {
                   @Override
                   public void onCompleted(Exception e, String result) {
                       if (e == null) {
                           if (!result.equalsIgnoreCase("No Data Found")) {

                               setUpUserDetails(result);

                           }
                       }
                   }
               });
    }

    private void setUpUserDetails(String result) {
        AppUser user = AppUser.processUserJson(result);
        if(user!=null)
        {
            edtFullname.setText(user.getFullName());
            edtlocation.setText(user.getLocation());
            edtphonenumber.setText(user.getPhone());
            edtemail.setText(user.getEmail_address());
            edtaboutme.setText(user.getAbout_user());
            edtusername.setText(user.getUser_name());


        }
        else
        {
            layout_confpassword.setVisibility(View.GONE);
            layout_password.setVisibility(View.GONE);
        }

        if(!accountState.equalsIgnoreCase("register")&&AppBackBoneClass.getUserJson()!=null && !TextUtils.isEmpty(AppBackBoneClass.getUserJson()))
        {
            layout_confpassword.setVisibility(View.GONE);
            layout_password.setVisibility(View.GONE);
        }
        else
        {
            layout_confpassword.setVisibility(View.VISIBLE);
            layout_password.setVisibility(View.VISIBLE);
        }
    }

    private void saveUserInfo()
    {
        saveDialog = new ProgressDialog(AppBackBoneClass.context);
        saveDialog.setMessage("Saving Infomation ...");
        saveDialog.setCancelable(false);
        saveDialog.show();
        Ion.with(this).load(AppBackBoneClass.parentUrL+AppBackBoneClass.feedUrl)
                .setBodyParameter("reason","Save User Details")
                .setBodyParameter("fname",edtFullname.getText().toString())
                .setBodyParameter("location",edtlocation.getText().toString())
                .setBodyParameter("phone",edtphonenumber.getText().toString())
                .setBodyParameter("email",edtemail.getText().toString())
                .setBodyParameter("about",edtaboutme.getText().toString())
                .setBodyParameter("pswd",edtpassword.getText().toString())
                .setBodyParameter("cpswd",edtconfpassword.getText().toString())
                .setBodyParameter("username",edtusername.getText().toString())
                .setBodyParameter("userID", AppBackBoneClass.getUserId())
                .setBodyParameter("type",(accountState.equalsIgnoreCase("register"))?"1":"2")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e == null) {
                            saveDialog.dismiss();
                            if (!result.equalsIgnoreCase("No Data Found")) {
                                Log.e("log_tag", "Error in http connection " + result);
                                AppUser user = AppUser.processUserJson(result);
                                if (user != null) {
                                    AppBackBoneClass.setUserJson(user.getUserJson());
                                    AppBackBoneClass.setUserID(EditAboutUser.this, user.getUserID());

                                    if(accountState.equalsIgnoreCase("register"))
                                    {
                                        Intent intent = new Intent(EditAboutUser.this,UserProfileSettingPage.class);
                                        AppBackBoneClass.setisLoggedIn(context, true);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                       finish();

                                }
                                // Toast.makeText(EditAboutUser.this, result, Toast.LENGTH_SHORT).show();
                                //setUpUserDetails(result);

                            }
                        }
                    }
                });
    }


}
