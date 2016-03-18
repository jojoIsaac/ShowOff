package applications.apps.celsoft.com.showoff;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.AppUser;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import static applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass.context;

/**
 * Created by User on 2/28/2016.
 */
public class LaunchPage extends AppCompatActivity {

    String AppName_fontPathfacefear = "fonts/Face Your Fears.ttf";
    String AppName_fontPathcrazy = "fonts/CircleD_Font_by_CrazyForMusic.ttf";
    String AppName_fontPathdigits = "fonts/DS-DIGIT.TTF";
    @Bind(R.id.txtappname)
    TextView txtappname;
    @Bind(R.id.btnLogin)
    Button btnLogin;
    @Bind(R.id.btncreateAccount)
    Button btncreateAccount;

    @Bind(R.id.edtusername)
    EditText edtusername;
    @Bind(R.id.edtpassword)
    EditText edtpassword;
    private ProgressDialog saveDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launchpage);
        ButterKnife.bind(this);
        context = this;
        AppBackBoneClass.myPrefs = this.getSharedPreferences(AppBackBoneClass.prefernceName,
                Context.MODE_PRIVATE);
        AppBackBoneClass.initApp();
        Typeface tf = Typeface.createFromAsset(getAssets(), AppName_fontPathfacefear);

        // Applying font
        txtappname.setTypeface(tf);


        //Set the button listeners
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleApplicationLogin();
            }
        });
        btncreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleApplicationRegister();
            }
        });


        if(AppBackBoneClass.getisLoggedIn())
        {
            Intent intent = new Intent(this, ShowOff_startPage.class);
            startActivity(intent);
            finish();

        }
    }

    private void handleApplicationRegister() {
        Intent intent = new Intent(this, EditAboutUser.class);
        intent.putExtra("state","register");
        startActivity(intent);

    }

    private void handleApplicationLogin() {
        String username = edtusername.getText().toString().trim();
        String password = edtpassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(LaunchPage.this, "Please enter username/email", Toast.LENGTH_SHORT).show();
            edtusername.requestFocus();
        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(LaunchPage.this, "Please enter password", Toast.LENGTH_SHORT).show();
            edtpassword.requestFocus();
        }
        else
        {
            saveDialog = new ProgressDialog(AppBackBoneClass.context);
            saveDialog.setMessage("Please wait ...");
            saveDialog.setCancelable(false);
            try
            {
                saveDialog.show();
            }
            catch (Exception e)
            {

            }

            Ion.with(this)
                    .load(AppBackBoneClass.parentUrL+AppBackBoneClass.feedUrl)
                    .setBodyParameter("username",username)
                    .setBodyParameter("password",password)
                    .setBodyParameter("reason","Login Application")
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            saveDialog.dismiss();
                            if (e == null ) {
                                Log.e("LOGPS", result);
if(!("No Data Found".equalsIgnoreCase(result))){
                                AppUser user = AppUser.processUserJson(result);
                                if (user != null) {
                                    AppBackBoneClass.setUserID(LaunchPage.this, user.getUserID());
                                    AppBackBoneClass.setUserInfo(user.getFullName(), user.getEmail_address(), user.getPhone(), user.getUser_name());
                                    AppBackBoneClass.setUserJson(user.getUserJson());
                                    Log.e("REST", result);
                                    AppBackBoneClass.setisLoggedIn(LaunchPage.this, true);
                                    Intent intent = new Intent(LaunchPage.this, ShowOff_startPage.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
        }

    }
}
