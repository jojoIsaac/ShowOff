package applications.apps.celsoft.com.showoff;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import applications.apps.celsoft.com.showoff.Utilities.Adapters.IssuesAdapter;
import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.AppUser;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.showoffItems;
import butterknife.Bind;
import butterknife.ButterKnife;

import static applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass.context;

/**
 * Created by User on 2/25/2016.
 */
public class Activity_myShowOffs extends ShowOffMain implements SwipeRefreshLayout.OnRefreshListener {


    private Integer currentIssues=-1;

    public   IssuesAdapter adapters;
    public  List<showoffItems> issuesLists;
    private int startAT;
    private Bundle data;
    String userJsonData=null;
    AppUser userObject;
    private IconDrawable draws;
    private IconDrawable connectdrawable;


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
    @Bind(R.id.linearLayout1)
    LinearLayout linearLayout1;


    @Override
    public String toString() {
        return super.toString();
    }
    View rootView;
    @Nullable
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     setContentView(R.layout.fragment_user_wall);
        context = this;
        AppBackBoneClass.myPrefs = this.getSharedPreferences(AppBackBoneClass.prefernceName,
                Context.MODE_PRIVATE);
        ButterKnife.bind(this);
        setUpUI(rootView);

    }
    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        AppBackBoneClass.currentAppPage=0;
        loadUserPage();
    }



    private void setUpUI(View rootView) {
        AppBackBoneClass.currentAppPage=0;




        //swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        //recyclerView=(RecyclerView) rootView.findViewById(R.id.rv);
        swipeLayout.setOnRefreshListener(this);

        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        final LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        issuesLists= new ArrayList<showoffItems>();
        adapters= new IssuesAdapter(issuesLists, currentIssues,this,this);
        recyclerView.setAdapter(adapters);
        userJsonData = AppBackBoneClass.getUserJson();
        userAvatar.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

                                          }
                                      }
        );
        if(!TextUtils.isEmpty(userJsonData)) {



            linearLayout1.setVisibility(View.GONE);
           //twister(userJsonData);

            //LayoutParams
//		StaggeredGridLayoutManager gaggeredGridLayoutManager= new StaggeredGridLayoutManager(2, 1);
//		rv.setLayoutManager(gaggeredGridLayoutManager);
            //setupfloatingMenu1(0) ;
            //setupfloatingMenu1(1) ;



       loadUserPage();







            recyclerView.addOnScrollListener( new OnVerticalScrollListener() {

            });
        }

        Drawable aboutdrawable = new IconDrawable(this, FontAwesomeIcons.fa_info_circle).color(
                Color.GRAY).actionBarSize();
        btnAbout.setCompoundDrawablesWithIntrinsicBounds(aboutdrawable, null, null, null);
        setTitle("My ShowOffs");
    }

    private void twister(String userJsonData) {
        userObject = AppUser.processUserJson(userJsonData);

        if(userObject!=null && userObject.getUserID().equalsIgnoreCase(AppBackBoneClass.getUserId()))
            btnconnect.setVisibility(View.GONE);
        else
            btnconnect.setVisibility(View.VISIBLE);
        //setUpuserView();
    }



    @Override
    public void onResume() {
        super.onResume();
        twister(AppBackBoneClass.getUserJson());
        onRefresh();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }



    void loadUserPage()
    {
        Ion.with(this)
                .load(AppBackBoneClass.parentUrL + AppBackBoneClass.feedUrl
                )
                .setBodyParameter("reason", "Load showoffs")
                .setBodyParameter("foruser",AppBackBoneClass.getUserId())
                .setBodyParameter("type", "-1")
                .setBodyParameter("UID", AppBackBoneClass.getUserId())
                .asString()

                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        // do stuff with the result or error
                        if (e == null) {
                            swipeLayout.setRefreshing(false);
                            //Toast.makeText(this, result, Toast.LENGTH_LONG).show();

                            issuesLists = new ArrayList<showoffItems>();
                            adapters = new IssuesAdapter(issuesLists, currentIssues, Activity_myShowOffs.this, Activity_myShowOffs.this);

                            recyclerView.setAdapter(adapters);
                            processIssuesJson(result);
                            Log.d("red", result);


                            // Toast.makeText(this,result,Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                Toast.makeText(Activity_myShowOffs.this, e.getMessage(), Toast.LENGTH_LONG).show();

                            } catch (Exception es) {

                            }

                            //e.printStackTrace();
                        }
                    }


                });
    }

    public  void processIssuesJson(String response) {
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
                            //String sender= feedObj.optString("sender");
                            showoffItems issues= showoffItems.getItem(feedObj.toString());

                            if(issues!=null)
                            {


                                if(issues.getFiletype().equalsIgnoreCase("Image") && issues.getFiletype().equalsIgnoreCase("Img")&&
                                        issues.getFiletype().equalsIgnoreCase("Text") )

                                    issuesLists.add(issues);
                                else
                                {
                                    if(!TextUtils.isEmpty(issues.getFilename()))
                                        issuesLists.add(issues);
                                }
                            }
                        }

                    }
                    startAT = issuesLists.size() + 1;
                    adapters.notifyDataSetChanged();
                }

            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            if(issuesLists.size()<=0)
            {
              Snackbar snackbar = Snackbar
                .make(recyclerView, "Show Off Now!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           Activity_myShowOffs.super.handleDataPicker();

                        }
                    })
            ;

                // Changing message text color
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
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

        public void onScrolledToEnd() {
            LoadMoreData();
            //Toast.makeText(this, "Scrolled to the end",Toast.LENGTH_LONG).show();
        }


    }

    protected void LoadMoreData() {
        Ion.with(this)
                .load(AppBackBoneClass.parentUrL + AppBackBoneClass.feedUrl)
                .setBodyParameter("reason", "Load showoffs")
                .setBodyParameter("foruser", AppBackBoneClass.getUserId())
                .setBodyParameter("type", "-1")
                .setBodyParameter("UID", AppBackBoneClass.getUserId())

                .setBodyParameter("index", startAT + "")
                .asString()

                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        // do stuff with the result or error
                        if (e == null) {
                            processIssuesJson(result);
                            Log.d("red", result);
                        } else {
                            Toast.makeText(Activity_myShowOffs.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            //e.printStackTrace();
                        }
                    }


                });

    }



}
