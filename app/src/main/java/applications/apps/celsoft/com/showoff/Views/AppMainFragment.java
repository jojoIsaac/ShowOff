package applications.apps.celsoft.com.showoff.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import applications.apps.celsoft.com.showoff.R;
import applications.apps.celsoft.com.showoff.Utilities.Adapters.IssuesAdapter;
import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.AppUser;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.showoffItems;
import butterknife.Bind;
import butterknife.ButterKnife;
import im.ene.lab.toro.Toro;

import static applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass.context;

public class AppMainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.swipe_container)
    SwipeRefreshLayout  swipeLayout;
    @Bind(R.id.rv)
    RecyclerView recyclerView;


    public static IssuesAdapter adapter;
    public static List<showoffItems> issuesList;
    private int startAT;
    private Bundle data;
    String userJsonData=null;
    AppUser userObject;
    private IconDrawable draws;
    private IconDrawable connectdrawable;
    private View rootView;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        outState.putString("USER_JSON", userJsonData);

        // created_by = savedInstanceState.getString("created_by");
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_health, container, false);
        context = getActivity();
        AppBackBoneClass.myPrefs = getActivity().getSharedPreferences(AppBackBoneClass.prefernceName,
                Context.MODE_PRIVATE);

        ButterKnife.bind(this,rootView);

        swipeLayout.setOnRefreshListener(this);

        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        issuesList= new ArrayList<showoffItems>();


        adapter= new IssuesAdapter(issuesList, currentIssues,getActivity(),getActivity());

        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new OnVerticalScrollListener() {

        });
        return rootView;
    }

    private void  unfollowUser()
    {

    }



    static  String friendStatus = "-100";


    public static Integer currentIssues=-1;
    public void loadInitialFeeds() {


        Ion.with(this)
                .load(AppBackBoneClass.parentUrL + AppBackBoneClass.feedUrl
                )
                .setBodyParameter("reason", "Load showoffs")
                .setBodyParameter("startPage", "1")
                .setBodyParameter("type", "-1")
                .setBodyParameter("UID", AppBackBoneClass.getUserId())
                .asString()

                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        // do stuff with the result or error
                        if (e == null) {
                            swipeLayout.setRefreshing(false);
                            Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();

                            issuesList = new ArrayList<showoffItems>();
                            adapter = new IssuesAdapter(issuesList, currentIssues,getActivity(), getActivity());

                            recyclerView.setAdapter(adapter);
                            processIssuesJson(result);
                            Log.d("red", result);


                            // Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();
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




    public void processIssuesJson(String response) {
        // TODO Auto-generated method stub
        swipeLayout.setRefreshing(false);
        if (!TextUtils.isEmpty(response)
                && !response.contains("No ShowOFF Found")) {

            JSONObject objects;

            try {

                objects = new JSONObject(response.trim());
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

                                Log.e("POLUT","NULLLLLL345");
                            }
                            else
                            {

                            }
                        }

                    }
                    else
                    {
                        Log.e("POLUT","NULLLLLL100298292");
                    }
                    startAT = issuesList.size() + 1;
                  //adapter.updateshowOffList(issuesList);
                }
                else
                {
                    Log.e("POLUT","NIs NUll");
                }

            }catch(Exception e)
            {
                Log.e("POLUT","NULLLLLL9087");
                e.printStackTrace();
            }
        }
        else
        {
               Log.e("POLUT","NULLLLLL");

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
                .setBodyParameter("foruser","-1")
                .setBodyParameter("type", "-1")
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
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            //e.printStackTrace();
                        }
                    }


                });

    }




    @Override public void onResume() {
        super.onResume();
        Toro.register(recyclerView);
    }


    @Override public void onPause() {
        Toro.unregister(recyclerView);
        super.onPause();
    }




}
