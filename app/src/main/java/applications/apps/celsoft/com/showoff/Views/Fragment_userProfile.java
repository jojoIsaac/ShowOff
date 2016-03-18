package applications.apps.celsoft.com.showoff.Views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

import applications.apps.celsoft.com.showoff.Activity_myShowOffs;
import applications.apps.celsoft.com.showoff.R;
import applications.apps.celsoft.com.showoff.ShowOffPaddies;
import applications.apps.celsoft.com.showoff.UserProfileSettingPage;
import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.AppUser;
import butterknife.Bind;
import butterknife.ButterKnife;

import static applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass.context;

/**
 * Created by User on 2/25/2016.
 */
public class Fragment_userProfile extends Fragment {

    private View rootView;
    String userJsonData=null;
    AppUser userObject;
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
    @Bind(R.id.layout_cover_photo)
    RelativeLayout layout_cover_photo;

    private IconDrawable drawsFriends;
    private IconDrawable drawsRequests;
    private IconDrawable drawsShows;
    private  Fragment fragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_user_details, null);
        ButterKnife.bind(this, rootView);
        setUpUI(rootView);
        return rootView;
        //layout_shows
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
        if(user!=null) {
            userObject = user;
            AppBackBoneClass.setUserJson(result);
          AppBackBoneClass.
                  DownloadImageFiles(AppBackBoneClass.parentUrL + AppBackBoneClass.usersimageURL + userObject.getUserCover()
                          , userObject.getUserCover(), context, layout_cover_photo);

        }


    }
    private void setUpUI(View rootView) {


        userJsonData = AppBackBoneClass.getUserJson();
        if(!TextUtils.isEmpty(userJsonData)) {

            Log.e("UJSON", userJsonData);
            userObject = AppUser.processUserJson(userJsonData);


            try {
                setUpuserView();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        Drawable aboutdrawable = new IconDrawable(getActivity(), FontAwesomeIcons.fa_info_circle).color(
                Color.GRAY).actionBarSize();

    }



    private void setUpuserView() throws  Exception
    {
        if(userObject!=null)
        {
            txtfullname.setText(userObject.getFullName());
            txt_username.setText(userObject.getUser_name());
            txt_email.setText(userObject.getEmail_address());
            txt_phone.setText(userObject.getPhone());

            //Set up the userImage
            TextDrawable drawable;
            draws = new IconDrawable(getActivity(), FontAwesomeIcons.fa_user).color(
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
                                + userObject.getImagePath(),getActivity(),
                        draws);
                // Toast.makeText(context, "lol",Toast.LENGTH_LONG).show();
            } else {
                userAvatar.setImageDrawable(drawable);
            }



//Set the Icons of the TextViews


            drawsFriends = new IconDrawable(getActivity(), FontAwesomeIcons.fa_users).color(
                    getResources().getColor(R.color.colorPrimaryDark)).actionBarSize();
            drawsRequests = new IconDrawable(getActivity(), FontAwesomeIcons.fa_user_plus).color(
                    getResources().getColor(R.color.colorPrimaryDark)).actionBarSize();
            drawsShows = new IconDrawable(getActivity(), FontAwesomeIcons.fa_play_circle).color(
                    getResources().getColor(R.color.colorPrimaryDark)).actionBarSize();
            editImagedrawable = new IconDrawable(getActivity(), FontAwesomeIcons.fa_image).color(
                    getResources().getColor(R.color.colorPrimaryDark)).actionBarSize();

txt_my_images.setCompoundDrawables(editImagedrawable,null,null,null);



            txt_friends.setCompoundDrawablesRelativeWithIntrinsicBounds(drawsFriends, null, null, null);
            txt_requests.setCompoundDrawablesRelativeWithIntrinsicBounds(drawsRequests,null,null,null);
            txt_my_shows.setCompoundDrawablesRelativeWithIntrinsicBounds(drawsShows,null,null,null);

            layout_shows.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ints = new Intent(context, Activity_myShowOffs.class);
                    startActivity(ints);
                }
            });
            layout_friends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ints= new Intent(context,ShowOffPaddies.class);
                    ints.putExtra("USER_JSON",AppBackBoneClass.getUserJson());
                    ints.putExtra("REASON","friends");

                    startActivity(ints);
                }
            });
            layout_requests.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ints= new Intent(context,ShowOffPaddies.class);
                    ints.putExtra("USER_JSON",AppBackBoneClass.getUserJson());
                    ints.putExtra("REASON","requests");

                    startActivity(ints);
                }
            });

            layout_images.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String menu[]= {"Change Profile Image","Change cover/banner"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setItems(menu, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), UserProfileSettingPage.class);
                            switch (which)
                            {
                                case 0:

                                    intent.putExtra("PURPOSE","changeDp");

                                    break;
                                case 1:
                                    intent.putExtra("PURPOSE","changeBanner");
                                    break;


                            }
                            startActivity(intent);
                        }
                    })
                            .show();
                }
            });



        }
    }


    @Override
    public void onResume() {
        fetchUserInfo();
        super.onResume();

    }
}
