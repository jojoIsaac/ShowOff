package applications.apps.celsoft.com.showoff;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import applications.apps.celsoft.com.showoff.Utilities.Adapters.IssueCommentsAdapter;
import applications.apps.celsoft.com.showoff.Utilities.Adapters.UserPreferencesAdapter;
import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.UserPreferences;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.showOffComments;

import static applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass.context;

/**
 * Created by User on 3/12/2016.
 */
public class UserPreference_activity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    private LinearLayoutManager mLayoutManager;
    UserPreferencesAdapter userPreferencesAdapter;
    // submit_btn emojicon_edit_text
    ImageView submit_btn;
    EditText emojicon_edit_text;
    String likes="0";
    String userLikeItem="0";
    private SwipeRefreshLayout swipeLayout;
    RecyclerView myRecycler;
    LinearLayoutManager layoutManager;
    private Toolbar toolbar;
    List<UserPreferences> preferencesList;
    LinearLayout laysend,laycontinue;
    Button btn_continue;
    String AppName_fontPathfacefear = "fonts/Face Your Fears.ttf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        AppBackBoneClass.context = this;
        AppBackBoneClass.myPrefs = getSharedPreferences(AppBackBoneClass.prefernceName,
                Context.MODE_PRIVATE);
        Typeface tf = Typeface.createFromAsset(getAssets(), AppName_fontPathfacefear);
        btn_continue =(Button) findViewById(R.id.btn_continue);
        btn_continue.setTypeface(tf);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowOff_startPage.class);
                (context).startActivity(intent);
                ((AppCompatActivity) context).finish();
            }
        });
        myRecycler = (RecyclerView) findViewById(R.id.myRecycler);
        mLayoutManager = new LinearLayoutManager(this); // Creating a layout
        // Manager

        myRecycler.setLayoutManager(mLayoutManager);
        laysend = (LinearLayout) findViewById(R.id.laysend);
        laycontinue = (LinearLayout) findViewById(R.id.laycontinue);

        laycontinue.setVisibility(View.VISIBLE);
        laysend.setVisibility(View.GONE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Choose Favorites");
        setSupportActionBar(toolbar);


        loadCategories();


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onRefresh() {
        loadCategories();
    }

    private void loadCategories()
    {
        Ion.with(this)
                .load(AppBackBoneClass.parentUrL + AppBackBoneClass.feedUrl)
                .setBodyParameter("reason", "get categories")
                .setBodyParameter("UID",AppBackBoneClass.getUserId())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        try {
                            preferencesList = new ArrayList<UserPreferences>();
                            userPreferencesAdapter = new UserPreferencesAdapter(preferencesList,AppBackBoneClass.context);
                            myRecycler.setAdapter(userPreferencesAdapter);
                            processPreferenceJson(result);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });



    }

    private void processPreferenceJson(String result) {
        try
        {
            List<UserPreferences> userPreferencesList = null;
            swipeLayout.setRefreshing(false);
            if(!TextUtils.isEmpty(result)&&!result.equalsIgnoreCase("empty"))
          {
              userPreferencesList =  UserPreferences.getUserPreferencesList(result,preferencesList);
              if(userPreferencesList!=null) {
                  preferencesList = userPreferencesList;
                  userPreferencesAdapter.notifyDataSetChanged();
              }


          }
        }
        catch (Exception e)
        {
 e.printStackTrace();
        }
    }
}
