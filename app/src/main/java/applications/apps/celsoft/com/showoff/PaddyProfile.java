package applications.apps.celsoft.com.showoff;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.AppUser;
import butterknife.Bind;
import butterknife.ButterKnife;

import static applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass.context;

/**
 * Created by User on 4/1/2016.
 */
public class PaddyProfile extends AppCompatActivity {
    private View rootView;

    private IconDrawable draws;
    private IconDrawable connectdrawable,editImagedrawable;


    @Bind(R.id.imgprofile)
    ImageView userAvatar;
    @Bind(R.id.txt_name)
    TextView txtfullname;
    @Bind(R.id.txt_phone)
    TextView txt_phone;
    @Bind(R.id.txt_email)
    TextView txt_email;
    @Bind(R.id.txt_username)
    TextView txt_username;
    @Bind(R.id.txt_requests)
    TextView txt_requests;
    @Bind(R.id.txt_friends)
    TextView txt_friends;
    @Bind(R.id.txt_my_shows)
    TextView txt_my_shows;
    @Bind(R.id.txt_my_images)
    TextView txt_my_images;
    @Bind(R.id.layout_shows)
    LinearLayout layout_shows;
    @Bind(R.id.layout_images)
    LinearLayout layout_images;
    @Bind(R.id.layout_friends)
    LinearLayout layout_friends;
    @Bind(R.id.layout_requests)
    LinearLayout layout_requests;
    @Bind(R.id.layout_blocklist)
    LinearLayout layout_blocklist;
    @Bind(R.id.layout_cover_photo)
    RelativeLayout layout_cover_photo;
    private String userJsonData;
    private AppUser userObject;



    private IconDrawable drawsFriends;
    private IconDrawable drawsRequests;
    private IconDrawable drawsShows;
    private String friendStatus="";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        outState.putString("USER_JSON", userJsonData);
        //outState.putString("REASON", reason);
        // created_by = savedInstanceState.getString("created_by");
    }

    void disableControls()
    {
        layout_requests.setVisibility(View.GONE);
        layout_requests.setVisibility(View.GONE);
        layout_requests.setVisibility(View.GONE);
        layout_requests.setVisibility(View.GONE);
        layout_requests.setVisibility(View.GONE);
        layout_requests.setVisibility(View.GONE);
    }

    private void setUpUserDetails(String result) {
        AppUser user = AppUser.processUserJson(result);
        if(user!=null) {
            userObject = user;

        }


    }

    private void fetchUserInfo()
    {

        Ion.with(this).load(AppBackBoneClass.parentUrL+AppBackBoneClass.feedUrl)
                .setBodyParameter("reason","Get User Details")
                .setBodyParameter("userID",userObject.getUserID())
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



    private void setUpuserView() throws  Exception
    {
        layout_blocklist.setVisibility(View.GONE);

        if(userObject!=null)
        {
            txtfullname.setText(userObject.getFullName());
            txt_username.setText(userObject.getUser_name());
            txt_email.setText(userObject.getEmail_address());
            txt_phone.setText(userObject.getPhone());

            //Set up the userImage
            TextDrawable drawable;
            draws = new IconDrawable(this, FontAwesomeIcons.fa_user).color(
                    Color.GRAY).actionBarSize();
            if(userObject.getFullName()!=null) {
                drawable = TextDrawable.builder().buildRound(
                        userObject.getFullName().substring(0, 1),
                        Color.RED);
            }
            else
            {
                drawable = TextDrawable.builder().buildRound(
                        "UN",
                        Color.RED);
            }
            if (!userObject.getImagePath().equalsIgnoreCase("null")) {
                AppBackBoneClass.loadUserImage( userAvatar,
                        AppBackBoneClass.parentUrL+AppBackBoneClass.usersimageURL+ "/"
                                + userObject.getImagePath(),this,
                        draws);
                // Toast.makeText(context, "lol",Toast.LENGTH_LONG).show();
            } else {
                userAvatar.setImageDrawable(drawable);
            }



//Set the Icons of the TextViews


            drawsFriends = new IconDrawable(this, FontAwesomeIcons.fa_envelope_o).color(
                    getResources().getColor(R.color.colorPrimaryDark)).actionBarSize();
            drawsRequests = new IconDrawable(this, FontAwesomeIcons.fa_user_plus).color(
                    getResources().getColor(R.color.colorPrimaryDark)).actionBarSize();
            drawsShows = new IconDrawable(this, FontAwesomeIcons.fa_flag).color(
                    getResources().getColor(R.color.colorPrimaryDark)).actionBarSize();
            editImagedrawable = new IconDrawable(this, FontAwesomeIcons.fa_ban).color(
                    getResources().getColor(R.color.colorPrimaryDark)).actionBarSize();

            txt_my_images.setCompoundDrawables(editImagedrawable,null,null,null);



            txt_friends.setCompoundDrawablesRelativeWithIntrinsicBounds(drawsFriends, null, null, null);
            txt_requests.setCompoundDrawablesRelativeWithIntrinsicBounds(drawsRequests,null,null,null);
            txt_my_shows.setCompoundDrawablesRelativeWithIntrinsicBounds(drawsShows,null,null,null);

            layout_shows.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   AppBackBoneClass.flagContent(PaddyProfile.this,userObject.getUserID(),"AppUser","Flag User");

                }
            });
            layout_friends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            layout_requests.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            layout_images.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    blockUser();
                }
            });



        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_details);
        ButterKnife.bind(this);
        AppBackBoneClass.myPrefs= getSharedPreferences(AppBackBoneClass.prefernceName, Context.MODE_PRIVATE);
        AppBackBoneClass.context=this;
        disableControls();

        if (savedInstanceState != null) {
            userJsonData = savedInstanceState.getString("USER_JSON");
            //reason=savedInstanceState.getString("REASON");

        } else {

            userJsonData = getIntent().getStringExtra("USER_JSON");
            //reason= getIntent().getStringExtra("REASON");

        }


        if(!TextUtils.isEmpty(userJsonData)) {

            Log.e("UJSON", userJsonData);
            userObject = AppUser.processUserJson(userJsonData);
            setUpUI(rootView);
        }
    }

    private void setUpUI(View rootView) {

        txt_friends.setText("Private Message");
        txt_my_images.setText("Block User");
        txt_my_shows.setText("Report User");
        //userJsonData = AppBackBoneClass.getUserJson();
        if(!TextUtils.isEmpty(userJsonData)) {

            Log.e("UJSON", userJsonData);
            userObject = AppUser.processUserJson(userJsonData);


            try {
                setUpuserView();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }



    void blockUser()
    {

        String [] blockType = {"Unfollow Posts","Disable Access"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(blockType, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Ion.with(PaddyProfile.this).load(AppBackBoneClass.parentUrL + AppBackBoneClass.feedUrl)
                        .setBodyParameter("reason", "Block User Access")
                        .setBodyParameter("userID", userObject.getUserID())
                        .setBodyParameter("blocktype", which+"")
                        .setBodyParameter("myID", AppBackBoneClass.getUserId())

                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                if (e == null) {

                                    Log.e("sss",result);
                                    if (!result.equalsIgnoreCase("No Data Found")) {

                                        Toast.makeText(PaddyProfile.this, result, Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }
                        })
                        ;

            }
        }).show();

    }



    private void handleFriendStatusChange(String result)
    {

        try {

            try {

                JSONObject object = new JSONObject(result);

                if (object != null) {
                    if (object.optString("status").equalsIgnoreCase("Success")) {
                        friendStatus = object.optString("Fstatus", "-1");


                    } else {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }

                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }


        }
        catch (Exception e)
        {

        }

    }
}
