package applications.apps.celsoft.com.showoff.Utilities.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import applications.apps.celsoft.com.showoff.R;
import applications.apps.celsoft.com.showoff.UserProfile;
import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.AppUser;

import im.ene.lab.toro.ToroAdapter;
import im.ene.lab.toro.ToroViewHolder;

/**
 * Created by User on 3/5/2016.
 */
public class UsersAdapter extends ToroAdapter<UsersAdapter.ContentViewHolder> {


    private IconDrawable img_canceldrawable;
    private Context context;
    private Activity activity;
    private IconDrawable connectdrawable;

    public String getReason() {
        return reason;
    }

    public UsersAdapter setReason(String reason) {
        this.reason = reason;
        return this;
    }

    private String reason="friends";



    public UsersAdapter(List<AppUser> appUserList, Context context) {
        this.context = context;
        this.appUsersList = appUserList;
    }

    public Activity getActivity() {
        return activity;
    }

    public UsersAdapter setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    private List<AppUser> appUsersList;
    AppUser appUser;
    private IconDrawable draws;
    private ContentViewHolder vhItem;
    public Integer position=0;

    public Context getContext() {
        return context;
    }

    public UsersAdapter setContext(Context context) {
        this.context = context;
        return this;
    }

    public List<AppUser> getAppUsersList() {
        return appUsersList;
    }

    public UsersAdapter setAppUsersList(List<AppUser> appUsersList) {
        this.appUsersList = appUsersList;
        return this;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public UsersAdapter setAppUser(AppUser appUser) {
        this.appUser = appUser;
        return this;
    }

    public IconDrawable getDraws() {
        return draws;
    }

    public UsersAdapter setDraws(IconDrawable draws) {
        this.draws = draws;
        return this;
    }

    private static final int TYPE_ITEM = 0;
    @Override
    public int getItemViewType(int position) {


        return TYPE_ITEM;
    }


    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.layout_user_list, parent, false);
            // Inflating
            // the
            // layout

            vhItem = new ContentViewHolder(v, viewType, context); // Creating
            // ViewHolder
            // and passing
            // the object of
            // type view

            return vhItem;
            // Returning the created object

            // inflate your layout and pass it to view holder


    }



        @Override
    public int getItemCount() {
        return appUsersList.size();
    }

    @Nullable
    @Override
    protected Object getItem(int position) {
        //if(pvh!=null)
        //pvh.position=position % issuesList.size();
        Log.e("CountErr", position + "~~~~ " + appUsersList.size());

            return appUsersList.get(position);


    }


    public  class ContentViewHolder extends ToroViewHolder
    {


        View.OnClickListener  showCommentUserProfileonclickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                if(!appUser.getUserID().equalsIgnoreCase(AppBackBoneClass.getUserId()))
                {
                    //Toast.makeText(context,showItem.getFileOwner().getUserJson() , Toast.LENGTH_LONG).show();
                    //Create an intent to open the user profile page
                    Intent userprofileIntent= new Intent(context, UserProfile.class);
                    userprofileIntent.putExtra("USER_JSON", appUser.getUserJson());
                    context.startActivity(userprofileIntent);
                }
            }
        };


        AppUser appUser;
        public TextView cuserName, txt_no_follows, ctxt_content, txt_timeago;
        ImageView img_status,cprofileimage,img_cancel;
        private TextView txt_deletes;
        private TextView txt_report;
        public ContentViewHolder(View itemView) {
            super(itemView);
        }

        public ContentViewHolder(View view, int viewType, Context context) {
            super(view);
            cuserName = (TextView) view.findViewById(R.id.userName);
            cuserName.setOnClickListener(showCommentUserProfileonclickListener);
            ctxt_content = (TextView) view.findViewById(R.id.txt_content);
            txt_timeago = (TextView) view.findViewById(R.id.txt_timeago);
            cprofileimage = (ImageView) view.findViewById(R.id.profileimage);
            img_status =(ImageView) view.findViewById(R.id.img_status);
            cprofileimage.setOnClickListener(showCommentUserProfileonclickListener);
            txt_no_follows = (TextView) view.findViewById(R.id.txt_no_follows);
            img_cancel =(ImageView) view.findViewById(R.id.img_cancel);
            img_canceldrawable = new IconDrawable(context, FontAwesomeIcons.fa_remove).color(
                    Color.RED).actionBarSize();

            img_cancel.setImageDrawable( img_canceldrawable);


            if(getReason().equalsIgnoreCase("friends"))
            {
                img_cancel.setVisibility(View.GONE);
                img_status.setVisibility(View.VISIBLE);
            }
            else
            {
                img_status.setVisibility(View.VISIBLE);
                img_cancel.setVisibility(View.VISIBLE);
            }
            txt_no_follows.setVisibility(View.GONE);
                }

        @Override
        public void bind(@Nullable Object object) {
            if(object instanceof  AppUser) {
                appUser = (AppUser) object;
                position = appUsersList.indexOf(appUser);

                setUpView(appUser);
            }



        }

        @Override
        public boolean wantsToPlay() {
            return false;
        }

        @Override
        public boolean isAbleToPlay() {
            return false;
        }

        @Nullable
        @Override
        public String getVideoId() {
            return null;
        }

        @NonNull
        @Override
        public View getVideoView() {
            return null;
        }

        @Override
        public void start() {

        }

        @Override
        public void pause() {

        }

        @Override
        public int getDuration() {
            return 0;
        }

        @Override
        public int getCurrentPosition() {
            return 0;
        }

        @Override
        public void seekTo(int pos) {

        }

        @Override
        public boolean isPlaying() {
            return false;
        }

        private void setUpView(final AppUser appUser) {
            if(appUser !=null)
            {
                Log.e("UserID", appUser.getUserID());

                if(appUser.getUserID().equalsIgnoreCase(AppBackBoneClass.getUserId()))
                {
                    txt_no_follows.setVisibility(View.VISIBLE);
                }



                cuserName.setText(appUser.getFullName());
                draws = new IconDrawable(context, FontAwesomeIcons.fa_user).color(
                        Color.GRAY).actionBarSize();
                TextDrawable drawable = TextDrawable.builder().buildRound(
                        appUser.getFullName().substring(0, 1),
                        Color.RED);
                if (!appUser.getImagePath().equalsIgnoreCase("null")) {
                    AppBackBoneClass.loadUserImage(cprofileimage,
                            AppBackBoneClass.parentUrL+AppBackBoneClass.usersimageURL + "/"
                                    + appUser.getImagePath(),context,
                            draws);
                    // Toast.makeText(context, "lol",Toast.LENGTH_LONG).show();
                } else {
                   cprofileimage.setImageDrawable(drawable);
                }

                Integer mutualFriends =  Integer.parseInt(appUser.getMutualFriends());
                Integer friends =  Integer.parseInt(appUser.getConnections());

                if(friends>0)
                {
                    txt_no_follows.setVisibility(View.VISIBLE);
                    txt_no_follows.setText(friends + ((friends >= 2)?" friends":" friend"));
                }
                else
                {
                    txt_no_follows.setVisibility(View.GONE);
                }

                if(mutualFriends>0)
                {
                    ctxt_content.setVisibility(View.VISIBLE);
                    ctxt_content.setText(appUser.getMutualFriends() + " mutual " + ((mutualFriends >= 2)?"friends":"friend"));
                }
                else
                {
                    ctxt_content.setVisibility(View.GONE);
                }

                changeFriendButton(appUser.getConnected());
                img_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // toggleFriendshipStatus(appUser,0);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setMessage("Reject request?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        toggleFriendshipStatus(appUser,1);
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                    }
                });
                img_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (appUser.getConnected().equalsIgnoreCase("1")) {
                            String menu[] = {"Unfriend", "Unfollow"};
                            //String [] menu = {"New Image","New Video","Upload Image","Upload Video"};
                            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                            dialog.setItems(menu, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    switch (which) {
                                        case 0:

                                            toggleFriendshipStatus(appUser,0);


                                            break;
                                        case 1:
                                            // Start the camera for video
                                            unfollowUser();
                                            break;


                                        default:
                                            break;


                                    }


                                }
                            })

                                    .show();
                        } else {
                            toggleFriendshipStatus(appUser,0);


                        }
                    }
                });


            }
        }
        private void  unfollowUser()
        {

        }

        private void changeFriendButton(String currentStatus)
        {
            Integer connected = Integer.parseInt(currentStatus);
            //Toast.makeText(UserProfile.this, connected+"", Toast.LENGTH_SHORT).show();
            img_status.setVisibility(View.VISIBLE);

            if(connected>-2)
            {
                img_cancel.setVisibility(View.GONE);
                img_status.setVisibility(View.VISIBLE);
            }
            else
            {
                img_cancel.setVisibility(View.VISIBLE);
                img_status.setVisibility(View.VISIBLE);
            }

            switch (connected)
            {
                case -1:

                    connectdrawable = new IconDrawable(context, FontAwesomeIcons.fa_user_plus).color(
                            context.getResources().getColor(R.color.text_like_comment)).actionBarSize();

                    img_status.setImageDrawable(connectdrawable);
                    // No request has been sent between the users
                    break;
                case -10:
                    //The same user profile opened
                    img_status.setVisibility(View.GONE);
                    img_cancel.setVisibility(View.GONE);
                    break;
                case 2:
                    connectdrawable =  new IconDrawable(context,FontAwesomeIcons.fa_user_times).color(
                            Color.RED).actionBarSize();
                    img_status.setImageDrawable(connectdrawable);
                    //Request sent but not yet accepted
                    break;
                case 1:
                    //The users are connected
                    connectdrawable = new IconDrawable(context, FontAwesomeIcons.fa_user).color(
                            context.getResources().getColor(R.color.colorPrimaryDark)).actionBarSize();
                    img_status.setImageDrawable(connectdrawable);
                    break;
                case -2:
                    connectdrawable = new IconDrawable(context, FontAwesomeIcons.fa_check).color(
                            context.getResources().getColor(R.color.colorPrimaryDark)).actionBarSize();
                    img_status.setImageDrawable(connectdrawable);
                    break;
            }

            //btnconnect.setCompoundDrawablesWithIntrinsicBounds(connectdrawable, null    , null, null);
        }



        void toggleFriendshipStatus(AppUser userObject,int reject)
        {

            Ion.with(context)
                    .load(AppBackBoneClass.parentUrL + AppBackBoneClass.feedUrl
                    )
                    .setBodyParameter("reason", (reject<1) ?"Toggle Friendship":"Delete Request")
                    .setBodyParameter("currentStatus",userObject.getConnected())
                    .setBodyParameter("friendID",userObject.getUserID())
                    .setBodyParameter("userID",AppBackBoneClass.getUserId())
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            // if(e==null)
                            // {

//                            Log.e("POLP",result);
                            if(e!=null)
                            {
                                e.printStackTrace();
                            }
                            else {

                                if (result!=null&&!result.trim().equalsIgnoreCase("Error") || !result.trim().equalsIgnoreCase("Err")) {
                                   handleFriendStatusChange(result);
                                }
                            }
                            //}
                        }
                    });

        }

String  friendStatus="";
        private void handleFriendStatusChange(String result)
        {

            try {

                try {

                    JSONObject object = new JSONObject(result);

                    if (object != null) {
                        if (object.optString("status").equalsIgnoreCase("Success")) {
                            friendStatus = object.optString("Fstatus", "-1");
                            //handleFriendStatusChange(friendStatus);
                            appUser.setConnected(friendStatus);
                            changeFriendButton(friendStatus);

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


}
