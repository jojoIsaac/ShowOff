package applications.apps.celsoft.com.showoff.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import applications.apps.celsoft.com.showoff.R;
import applications.apps.celsoft.com.showoff.Utilities.Adapters.IssuesAdapter;
import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.showoffItems;
import butterknife.Bind;
import butterknife.ButterKnife;
import im.ene.lab.toro.Toro;

import static applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass.context;

/**
 * Created by User on 3/25/2016.
 */
public class ShowFeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View rootView;
    // setContentView(R.layout.fragment_user_wall);

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;
    private ArrayList<showoffItems> issuesLists;
    private IssuesAdapter adapters;
    private static int startAT;
    private Bundle data;
    public static Integer ITEM_CHANGED_RESULTS=90;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.layout_show_feed,null);
        context = getActivity();
        AppBackBoneClass.myPrefs = getActivity().getSharedPreferences(AppBackBoneClass.prefernceName,
                Context.MODE_PRIVATE);
        ButterKnife.bind(this,rootView);
        floatingActionButton.setVisibility(View.VISIBLE);

        setUpUI();
        return rootView;
    }

    private void setUpUI() {
        swipeLayout.setOnRefreshListener(this);

        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        issuesLists= new ArrayList<showoffItems>();
        adapters= new IssuesAdapter(issuesLists, currentIssues,getActivity()    ,getActivity());
        recyclerView.setAdapter(adapters);



        recyclerView.addOnScrollListener(new OnVerticalScrollListener() {

        });
    }

    @Override
    public void onRefresh() {

        AppBackBoneClass.currentAppPage=0;
        loadShowPage();
    }

    @Override public void onResume() {
        super.onResume();
        AppBackBoneClass.currentAppPage=0;
        AppBackBoneClass.context = getActivity();
        Toro.register(recyclerView);


    }


    @Override public void onPause() {
        Toro.unregister(recyclerView);
        super.onPause();
    }



    public static Integer currentIssues=-1; // The number indicate the category of Items to be shown on a page. -1 indicate unsorted



    String tempFilename="tempfile_";

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

    private void LoadMoreData() {

    }




    void loadShowPage()
    {
        Ion.with(this)
                .load(AppBackBoneClass.parentUrL + AppBackBoneClass.feedUrl
                )
                .setBodyParameter("reason", "Load showoffs")

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


                            processIssuesJson(result);
                            Log.d("red", result);


                            // Toast.makeText(this,result,Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

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


                            }
                        })
                        ;

                // Changing message text color
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
            }
        }
    }



}
