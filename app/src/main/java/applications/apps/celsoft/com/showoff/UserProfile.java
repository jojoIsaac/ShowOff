package applications.apps.celsoft.com.showoff;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import applications.apps.celsoft.com.showoff.Utilities.Adapters.IssuesAdapter;
import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.AppUser;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.showoffItems;
import butterknife.Bind;
import butterknife.ButterKnife;
import im.ene.lab.toro.Toro;

import static applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass.context;

public class UserProfile extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.userAvatar)
    ImageView userAvatar;
    @Bind(R.id.txtfullname)
    TextView txtfullname;
    @Bind(R.id.txtuserlocation)
    TextView txtuserlocation;
    @Bind(R.id.album_overflow)
    ImageView album_overflow;
    @Bind(R.id.swipe_container)
    SwipeRefreshLayout  swipeLayout;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.btnconnect)
    Button btnconnect;
    @Bind(R.id.btnAbout)
    Button btnAbout;
    @Bind(R.id.btn_friends)
    Button btn_friends;
    @Bind(R.id.main_layout)
    RelativeLayout main_layout;

    public static IssuesAdapter adapter;
    public static List<showoffItems> issuesList;
    private int startAT;
    private Bundle data;
    String userJsonData=null;
    AppUser userObject;
    private IconDrawable draws;
    private IconDrawable connectdrawable;
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        outState.putString("USER_JSON",userJsonData);

        // created_by = savedInstanceState.getString("created_by");
    }

    protected ActionBar getActionBars() {
        return this.getSupportActionBar();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_wall);
        context = this;
        AppBackBoneClass.myPrefs = this.getSharedPreferences(AppBackBoneClass.prefernceName,
                Context.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);
        AppBackBoneClass.currentAppPage=1;
        if (savedInstanceState != null) {
            userJsonData = savedInstanceState.getString("USER_JSON");

        } else

            userJsonData = getIntent().getStringExtra("USER_JSON");
       //Toast.makeText(UserProfile.this, userJsonData, Toast.LENGTH_LONG).show();



//        album_overflow.setVisibility(View.GONE);
        ButterKnife.bind(this);
     ActionBar bar = getSupportActionBar();

        swipeLayout.setOnRefreshListener(this);

        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        final LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        issuesList= new ArrayList<showoffItems>();
        adapter= new IssuesAdapter(issuesList, currentIssues,this,this);

        recyclerView.setAdapter(adapter);
        if(!TextUtils.isEmpty(userJsonData)) {

            Log.e("UJSON", userJsonData);
            userObject = AppUser.processUserJson(userJsonData);

            if(userObject!=null && userObject.getUserID().equalsIgnoreCase(AppBackBoneClass.getUserId()))
                btnconnect.setVisibility(View.GONE);
            else
               btnconnect.setVisibility(View.VISIBLE);
            setUpuserView();

            //LayoutParams
//		StaggeredGridLayoutManager gaggeredGridLayoutManager= new StaggeredGridLayoutManager(2, 1);
//		rv.setLayoutManager(gaggeredGridLayoutManager);
            //setupfloatingMenu1(0) ;
            //setupfloatingMenu1(1) ;



            loadInitialFeeds();







            recyclerView.addOnScrollListener( new OnVerticalScrollListener() {

            });
        }

        Drawable aboutdrawable = new IconDrawable(this, FontAwesomeIcons.fa_info_circle).color(
                Color.WHITE).actionBarSize();
        btnAbout.setCompoundDrawablesWithIntrinsicBounds(aboutdrawable, null, null, null);



        Drawable friendsdrawable = new IconDrawable(this, FontAwesomeIcons.fa_users).color(
                Color.WHITE).actionBarSize();
        btn_friends.setCompoundDrawablesWithIntrinsicBounds(friendsdrawable,null,null,null);

        btn_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ints = new Intent(context, ShowOffPaddies.class);
                ints.putExtra("USER_JSON", userJsonData);
                ints.putExtra("REASON","friends");
                startActivity(ints);
            }
        });


        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ints = new Intent(context, PaddyProfile.class);
                ints.putExtra("USER_JSON", userJsonData);
                ints.putExtra("REASON","friends");
                startActivity(ints);
            }
        });

    }

    private void changeFriendButton(String currentStatus)
    {
        Integer connected = Integer.parseInt(currentStatus);
        //Toast.makeText(UserProfile.this, connected+"", Toast.LENGTH_SHORT).show();
        switch (connected)
        {
            case -1:

                connectdrawable = new IconDrawable(this, FontAwesomeIcons.fa_user_plus).color(
                     Color.WHITE).actionBarSize();
btnconnect.setTextColor(Color.WHITE);
                btnconnect.setText("Connect");
                // No request has been sent between the users
                break;
            case -10:
                //The same user profile opened
                break;
            case 2:
                connectdrawable =  new IconDrawable(this, FontAwesomeIcons.fa_user_times).color(
                        Color.WHITE).actionBarSize();
                btnconnect.setText("Requested");
                btnconnect.setTextColor( Color.WHITE);
                //Request sent but not yet accepted
                break;
            case 1:
                //The users are connected
                connectdrawable = new IconDrawable(this, FontAwesomeIcons.fa_user).color(
                        Color.WHITE).actionBarSize();
                btnconnect.setText("Connected");
                btnconnect.setTextColor(Color.WHITE);
                break;
        }

        btnconnect.setCompoundDrawablesWithIntrinsicBounds(connectdrawable, null    , null, null);
    }


    private void setUpuserView()
    {
        if(userObject!=null)
        {
            txtfullname.setText(userObject.getFullName());
            txtuserlocation.setText(userObject.getLocation());

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




            //Check if users are connected
           // Integer connected = Integer.parseInt(userObject.getConnected());
          changeFriendButton(userObject.getConnected());



            btnconnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(userObject.getConnected().equalsIgnoreCase("1"))
                    {
                        String menu[]= {"Unfriend","Unfollow"};
                        //String [] menu = {"New Image","New Video","Upload Image","Upload Video"};
                        AlertDialog.Builder dialog = new AlertDialog.Builder(UserProfile.this);
                        dialog.setItems(menu, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                switch (which) {
                                    case 0:

                                         toggleFriendshipStatus(userObject);


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
                    }
                    else
                    {
                        toggleFriendshipStatus(userObject);
                        

                    }
                }
            });

        }
    }



    void toggleFriendshipStatus(AppUser userObject)
    {

        Ion.with(this)
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
                        // if(e==null)
                        // {
                        if(e!=null)
                        {
                            e.printStackTrace();
                        }
                        else {

                            if (!result.equalsIgnoreCase("Error") || !result.equalsIgnoreCase("Err")) {
                                handleFriendStatusChange(result);
                            }
                        }
                        //}
                    }
                });

    }



    private void handleFriendStatusChange(String result)
    {
        try {

            try {
                JSONObject object = new JSONObject(result);
                if (object != null) {
                    if (object.optString("status").equalsIgnoreCase("Success")) {
                        friendStatus = object.optString("Fstatus", "-1");
                        //handleFriendStatusChange(friendStatus);
                        userObject.setConnected(friendStatus);
                        changeFriendButton(friendStatus);

                    } else {
                        Toast.makeText(UserProfile.this, "Error", Toast.LENGTH_SHORT).show();
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


    private void  unfollowUser()
{

}



    static  String friendStatus = "-100";


    public static Integer currentIssues=-1;
    public void loadInitialFeeds() {

        //Check current friend Status
        Ion.with(this)
                .load(AppBackBoneClass.parentUrL + AppBackBoneClass.feedUrl
                )
                .setBodyParameter("reason", "Check friends Status")
                .setBodyParameter("currentStatus",userObject.getConnected())
                .setBodyParameter("friendID",userObject.getUserID())
                .setBodyParameter("userID",AppBackBoneClass.getUserId())
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

                            if (!result.equalsIgnoreCase("Error") || !result.equalsIgnoreCase("Err")) {
                                handleFriendStatusChange(result);
                            }
                        }
                        //}
                    }
                });

        Ion.with(this)
                .load(AppBackBoneClass.parentUrL + AppBackBoneClass.feedUrl
                )
                .setBodyParameter("reason", "Load showoffs")
                .setBodyParameter("foruser",userObject.getUserID())
                .setBodyParameter("type", "-1")
                .setBodyParameter("UID", AppBackBoneClass.getUserId())
                .asString()

                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        // do stuff with the result or error
                        if (e == null) {
                            swipeLayout.setRefreshing(false);
                            //Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();

                            issuesList = new ArrayList<showoffItems>();
                            adapter = new IssuesAdapter(issuesList, currentIssues, UserProfile.this, UserProfile.this);

                            recyclerView.setAdapter(adapter);
                            processIssuesJson(result);
                            Log.d("red", result);


                            // Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                Toast.makeText(UserProfile.this, e.getMessage(), Toast.LENGTH_LONG).show();

                            } catch (Exception es) {

                            }

                            //e.printStackTrace();
                        }
                    }


                });

    }




    public void processIssuesJson(String response) {
        // TODO Auto-generated method stub
        swipeLayout.setRefreshing(false);
        if (!TextUtils.isEmpty(response)
                && !response.contains("No ShowOFF Found")) {

            JSONObject objects;

            try {

                objects = new JSONObject(response);
                if (objects != null) {
                    JSONArray feedArray = objects.getJSONArray("ShowOFF");

                    if (feedArray != null) {

                        // JSONArray feedArray = response.getJSONArray("feed");

                        for (int i = 0; i < feedArray.length(); i++) {
                            JSONObject feedObj = (JSONObject) feedArray.get(i);
                            String sender= feedObj.optString("sender");
                            showoffItems issues= new showoffItems();
                            AppUser user= new AppUser();
                            user= AppUser.processUserJson(sender);
                            if(user!=null)
                            {
                                issues.setFileOwner(user);


                                issues.setItemID(feedObj.getString("id"))
                                        .setShowItemJson(feedObj)
                                        .setTitle(feedObj.getString("title"))
                                        .setFiletype(feedObj.getString("file_type"))
                                        .setContent(feedObj.getString("content"))

                                        .setDate_uploaded(feedObj.getString("datesent"))
                                        .setFilename(feedObj.getString("file_uploaded"))
                                        .setTopicID(feedObj.getString("data_cate"))
                                        .setLikes(feedObj.getString("likes"))
                                        .setIssueJson(feedObj.toString())
                                        .setUserLike(feedObj.getString("user_like"))
                                ;

                                if(issues.getFiletype().equalsIgnoreCase("Image") && issues.getFiletype().equalsIgnoreCase("Img")&&
                                        issues.getFiletype().equalsIgnoreCase("Text") )

                                    issuesList.add(issues);
                                else
                                {
                                    if(!TextUtils.isEmpty(issues.getFilename()))
                                        issuesList.add(issues);
                                }
                            }
                        }

                    }
                    startAT = issuesList.size() + 1;
                    adapter.notifyDataSetChanged();
                }

            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {

            if( issuesList.size()<=0)
            {
                Snackbar snackbar = Snackbar
                        .make(main_layout, "Sorry no data found!", Snackbar.LENGTH_LONG)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();

                            }
                        })
                        ;

                // Changing message text color
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
            }
        }
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        loadInitialFeeds();
    }


    public static void showOffItemUpdated(showoffItems item,int state)
    {
        Integer index = issuesList.indexOf(item);
        if(index>-1)
        {
            switch (state)
            {
                case 1:
                    //Update comment, and likes counts
                    issuesList.remove(index);
                    issuesList.set(index, item);
                    adapter.notifyItemChanged(index);
                    break;
                case 2:
                    //Remove element from list because user deleted it
                    issuesList.remove(index);

                    adapter.notifyItemRemoved(index);
                    break;
            }

        }
    }

    abstract class OnVerticalScrollListener
            extends RecyclerView.OnScrollListener {

        @SuppressLint("NewApi") @Override
        public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!recyclerView.canScrollVertically(1)) {
                onScrolledToEnd();
            } else if (dy < 0) {
                //onScrolledUp();
            } else if (dy > 0) {
                //onScrolledDown();
            }
        }
        /*
        public void onScrolledUp() {
            MainActivity.floaticon.setVisibility(View.VISIBLE);
            centerActionButton.setVisibility(View.VISIBLE);
        }

        public void onScrolledDown() {
            actionMenu.close(true);
            MainActivity.floaticon.setVisibility(View.GONE);
            centerActionButton.setVisibility(View.GONE);

        }
        */
        public void onScrolledToEnd() {
            LoadMoreData();
            //Toast.makeText(getActivity(), "Scrolled to the end",Toast.LENGTH_LONG).show();
        }


    }




    protected void LoadMoreData() {
        Ion.with(this)
                .load(AppBackBoneClass.parentUrL + AppBackBoneClass.feedUrl)
                .setBodyParameter("reason", "Load showoffs")
                .setBodyParameter("foruser",userObject.getUserID())
                .setBodyParameter("type",  "-1")
                .setBodyParameter("UID", AppBackBoneClass.getUserId())
                .setBodyParameter("index",startAT+"")
                .asString()

                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        // do stuff with the result or error
                        if(e==null)
                        {
                            processIssuesJson(result);
                            Log.d("red", result);
                        }
                        else
                        {
                            Toast.makeText(UserProfile.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            //e.printStackTrace();
                        }
                    }


                });

    }




    @Override public void onResume() {
        super.onResume();
        onRefresh();
        Toro.register(recyclerView);

    }


    @Override public void onPause() {
        Toro.unregister(recyclerView);
        super.onPause();
    }




}
