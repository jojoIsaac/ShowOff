package applications.apps.celsoft.com.showoff;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.joanzapata.iconify.IconDrawable;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import applications.apps.celsoft.com.showoff.Utilities.Adapters.UsersAdapter;
import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.AppUser;
import butterknife.Bind;
import butterknife.ButterKnife;
import im.ene.lab.toro.Toro;

import static applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass.context;

/**
 * Created by User on 3/5/2016.
 */
public class ShowOffPaddies extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.rv)
    protected RecyclerView recyclerView;
    @Bind(R.id.swipe_container)
    protected SwipeRefreshLayout swipeLayout;
    private IconDrawable draws;
    //public static Integer currentIssues;
    public static UsersAdapter adapter;
    public static List<AppUser> appUserList;
    private static int startAT;
    private Bundle data;
    public static Integer ITEM_CHANGED_RESULTS=90;
    @Bind(R.id.layout_newStory)
   LinearLayout layout_newStory;

    @Bind(R.id.main_layout)
    FrameLayout main_layout;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;
    private String userJsonData;
    private AppUser userObject;

    String reason ="friends";


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        outState.putString("USER_JSON", userJsonData);
        outState.putString("REASON", reason);
        // created_by = savedInstanceState.getString("created_by");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_health);

        context = this;
        AppBackBoneClass.myPrefs = this.getSharedPreferences(AppBackBoneClass.prefernceName,
                Context.MODE_PRIVATE);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            userJsonData = savedInstanceState.getString("USER_JSON");
            reason=savedInstanceState.getString("REASON");

        } else {

            userJsonData = getIntent().getStringExtra("USER_JSON");
            reason= getIntent().getStringExtra("REASON");

        }



        if(!reason.equalsIgnoreCase("friends"))
        {
           setTitle("Friend Requests");
        }
        else if(reason.equalsIgnoreCase("Blocked_friends"))
        {
            setTitle("Blocked Users");
        }
        else {
            setTitle("Friends");
        }

        if(!TextUtils.isEmpty(userJsonData)) {

            Log.e("UJSON", userJsonData);
            userObject = AppUser.processUserJson(userJsonData);
        }

        floatingActionButton.setVisibility(View.GONE);



        final LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //LayoutParams
//		StaggeredGridLayoutManager gaggeredGridLayoutManager= new StaggeredGridLayoutManager(2, 1);
//		rv.setLayoutManager(gaggeredGridLayoutManager);
        //setupfloatingMenu1(0) ;
        //setupfloatingMenu1(1) ;

        appUserList= new ArrayList<AppUser>();
        adapter= new UsersAdapter(appUserList,this)
                .setReason(reason)
                .setActivity(this)
                .setContext(this);
       // adapter= new IssuesAdapter(issuesList, currentIssues, AppBackBoneClass.context, (AppCompatActivity) AppBackBoneClass.context);

        recyclerView.setAdapter(adapter);
        onRefresh();






        recyclerView.addOnScrollListener(new OnVerticalScrollListener() {

        });

        layout_newStory.setVisibility(View.GONE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        context = this;
        swipeLayout.setRefreshing(true);
        onRefresh();
        Toro.register(recyclerView);


    }


    @Override public void onPause() {
        Toro.unregister(recyclerView);
        super.onPause();
    }
    @Override
    public void onRefresh() {


        loadInitialFeeds();
    }

abstract class OnVerticalScrollListener
            extends RecyclerView.OnScrollListener {

        @SuppressLint("NewApi")
        @Override
        public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!recyclerView.canScrollVertically(1)) {
                onScrolledToEnd();
            }
            else if (dy < 0) {
                //onScrolledUp();
            }
            else if (dy > 0) {
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
    public  void loadInitialFeeds() {

        Ion.with(this)
                .load(AppBackBoneClass.parentUrL + AppBackBoneClass.feedUrl
                )
                //Blocked_friends
                .setBodyParameter("reason", (reason.equalsIgnoreCase("requests")) ? "Load friend Request" : (reason.equalsIgnoreCase("Blocked_friends"))? "Blocked Friends" :"Load Friends")
                .setBodyParameter("myID",AppBackBoneClass.getUserId())

                .setBodyParameter("UID", userObject.getUserID())
                .asString()

                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        // do stuff with the result or error
                        swipeLayout.setRefreshing(false);
                        if (e == null) {
                            //swipeLayout.setRefreshing(false);
                            //Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();

                            appUserList = new ArrayList<AppUser>();
                            adapter = new UsersAdapter(appUserList, context)
                                    .setReason(reason)
                                    .setActivity((ShowOffPaddies) context)
                            ;
                            // adapter = new IssuesAdapter(issuesList, currentIssues, AppBackBoneClass.context, (AppCompatActivity) AppBackBoneClass.context);

                            recyclerView.setAdapter(adapter);
                            processUsersJson(result);
                            Log.d("red", result);


                            // Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                Toast.makeText(AppBackBoneClass.context, e.getMessage(), Toast.LENGTH_LONG).show();

                            } catch (Exception es) {

                            }

                            //e.printStackTrace();
                        }
                    }


                });

    }





    protected void LoadMoreData() {
        Ion.with(this)
                .load(AppBackBoneClass.parentUrL + AppBackBoneClass.feedUrl)
               // .setBodyParameter("reason",  (reason.equalsIgnoreCase("requests"))?"Load friend Request": "Load Friends")
                .setBodyParameter("reason", (reason.equalsIgnoreCase("requests")) ? "Load friend Request" : (reason.equalsIgnoreCase("Blocked_friends")) ? "Blocked Friends" : "Load Friends")

                .setBodyParameter("myID", AppBackBoneClass.getUserId())
                .setBodyParameter("UID", userObject.getUserID())
                .setBodyParameter("index", startAT + "")
                .asString()

                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        // do stuff with the result or error
                        if (e == null) {
                            processUsersJson(result);
                            Log.d("red", result);
                        } else {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            //e.printStackTrace();
                        }
                    }


                });

    }

    private void processUsersJson(String response)
    {
        // TODO Auto-generated method stub
        adapter.setReason(reason);
        swipeLayout.setRefreshing(false);
     layout_newStory.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(response)
                && !response.trim().contains("No Friend Found") && !response.trim().contains("No Friend Found")) {

            JSONObject objects;

            try {

                objects = new JSONObject(response);
                if (objects != null) {
                    JSONArray feedArray = objects.getJSONArray("ShowOFFUsers");

                    if (feedArray != null) {

                        // JSONArray feedArray = response.getJSONArray("feed");

                        for (int i = 0; i < feedArray.length(); i++) {
                            JSONObject feedObj = (JSONObject) feedArray.get(i);
                            //String sender= feedObj.optString("sender");

                            AppUser user = AppUser.processUserJson(feedObj.toString());
                            if(user!=null)
                            {



                                    if(!TextUtils.isEmpty(user
                                            .getFullName()))
                                       appUserList.add(user);

                            }
                        }

                    }
                    startAT = appUserList.size() + 1;
                    adapter.notifyDataSetChanged();
                }

            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {

                if( appUserList.size()<=0)
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




    String tempFilename="tempfile_";
}




