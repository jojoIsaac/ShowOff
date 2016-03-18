package applications.apps.celsoft.com.showoff;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import applications.apps.celsoft.com.showoff.Utilities.Adapters.IssueCommentsAdapter;
import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.showOffComments;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.showoffItems;

public class FeedDetailActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

	List<showOffComments> content;
	RecyclerView myRecycler;
	LinearLayoutManager layoutManager;
	showoffItems issue;
	String issueJson;
	Toolbar toolbar;
	private LinearLayoutManager mLayoutManager;
	IssueCommentsAdapter commentAdpater;
	// submit_btn emojicon_edit_text
	ImageView submit_btn;
	EditText emojicon_edit_text;
	String likes="0";
    String userLikeItem="0";
    private SwipeRefreshLayout swipeLayout;

    @Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		outState.putString("issueJson", issueJson);
		outState.putString("user_like", userLikeItem);
		outState.putString("likes", likes);


		// created_by = savedInstanceState.getString("created_by");
	}

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
		issueJson = getIntent().getStringExtra("json");
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("ShowOff");
		setSupportActionBar(toolbar);
        AppBackBoneClass.context = this;
        AppBackBoneClass.myPrefs = getSharedPreferences(AppBackBoneClass.prefernceName,
                Context.MODE_PRIVATE);
        startAT=1;
       //getSupportActionBar().setHideOnContentScrollEnabled(true);

		if (savedInstanceState != null) {
			issueJson = savedInstanceState.getString("issueJson");


		} else

		{

			issueJson = getIntent().getStringExtra("json");
		}

		try {
			issue = showoffItems.getItem(issueJson);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		content = new ArrayList<showOffComments>();
		myRecycler = (RecyclerView) findViewById(R.id.myRecycler);
		mLayoutManager = new LinearLayoutManager(this); // Creating a layout
														// Manager

		myRecycler.setLayoutManager(mLayoutManager);
		myRecycler.addOnScrollListener(new OnVerticalScrollListener() {

		});
		if (issue != null) {
			// createAdapter(myRecycler);
			setTitle(issue.getTitle());
			commentAdpater = new IssueCommentsAdapter(content, issue, this);
			myRecycler.setAdapter(commentAdpater);
			loadInitialFeeds(issue.getItemID());

		} else {
			Toast.makeText(this, "Sorry Operation failed", Toast.LENGTH_LONG)
					.show();
			finish();
		}
		
		emojicon_edit_text= (EditText)findViewById(R.id.emojicon_edit_text);
		submit_btn =(ImageView) findViewById(R.id.submit_btn);
		
		submit_btn.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(emojicon_edit_text.getText().toString()))
				saveCommentOnserver(emojicon_edit_text.getText().toString());
			}
		});

	}

	protected void saveCommentOnserver(String string) {
		final ProgressDialog pd= new ProgressDialog(this);
        startAT = content.size() + 1;
		pd.setMessage("Saving comment ...");
		pd.show();
		JsonObject item= new JsonObject();
		item.addProperty("comment", string);
		Ion.with(this)
		.load(AppBackBoneClass.parentUrL+AppBackBoneClass.feedUrl)

                .setBodyParameter("reason","Add Comment")
                .setBodyParameter("dataID", issue.getItemID())
                .setBodyParameter("UID", AppBackBoneClass.getUserId())
                .setBodyParameter("comment",string.trim())
                .setBodyParameter("index",startAT+"")


                .asString()

		.setCallback(new FutureCallback<String>() {
			@Override
			public void onCompleted(Exception e, String result) {
				// do stuff with the result or error
				pd.dismiss();
				if (e == null) {
				
					if(!result.equalsIgnoreCase("No Comment Found")&&!result.equalsIgnoreCase("err"))
					{
                        /*
						showOffComments comments= showOffComments.processIssuesCommentJson(result);
						if(comments!=null)
						{
							
							content.add(comments);
							
							commentAdpater.notifyItemInserted(content.size());
							
							startAT= startAT+1;
							emojicon_edit_text.setText("");
						}
						*/
                        processIssuesCommentJson(result);
						Log.d("red", result);
					}
				} else {
					try {
						Toast.makeText(FeedDetailActivity.this,
								e.getMessage(), Toast.LENGTH_LONG)
								.show();

					} catch (Exception es) {

					}

					// e.printStackTrace();
				}
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.feed_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Now set up the page

	// List<IssueComments> content = new ArrayList<IssueComments>();
	public  int startAT=1;

	// Initial comments load
	public void loadInitialFeeds(String ItemID) {
		// Toast.makeText(getActivity(), "hello", Toast.LENGTH_LONG).show();
		Ion.with(this)
				.load(AppBackBoneClass.parentUrL+AppBackBoneClass.feedUrl
						)
				.setBodyParameter("reason","Load showOff Comments")
				.setBodyParameter("dataID",ItemID)
				.setBodyParameter("UID",AppBackBoneClass.getUserId())

				.asString()

				.setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception e, String result) {
						// do stuff with the result or error
						if (e == null) {
							processIssuesCommentJson(result);
							Log.d("red", result);
						} else {
							try {
								Toast.makeText(FeedDetailActivity.this,
										e.getMessage(), Toast.LENGTH_LONG)
										.show();

							} catch (Exception es) {

							}

							// e.printStackTrace();
						}
					}

				});

	}

	boolean checkAlreadyAdded(showOffComments comment)
	{
		boolean found=false;
		for (showOffComments comments: content) {
			if(comments.getCommentID().equalsIgnoreCase(comment.getCommentID()))
				
				{
				found=true;
				break;
				}
			else
			{
				found=false;
			}
			
		}
		return found;
	}
	
	// Load More Comments from the server
	protected void LoadMoreData() {

		Ion.with(this)
				.load(AppBackBoneClass.parentUrL+AppBackBoneClass.feedUrl
						)
                .setBodyParameter("reason","Load showOff Comments")
                .setBodyParameter("dataID", issue.getItemID())
                .setBodyParameter("UID", AppBackBoneClass.getUserId())
                .setBodyParameter("index",startAT+"")
				.asString()

				.setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception e, String result) {
						// do stuff with the result or error
						if (e == null) {
							processIssuesCommentJson(result);
							//Log.d("red", result);
						} else {
							Toast.makeText(FeedDetailActivity.this,
									e.getMessage(), Toast.LENGTH_LONG).show();
							// e.printStackTrace();
						}
					}

				});

	}

	// Processing the Json of the comment fetched
	protected void processIssuesCommentJson(String response) {
		// TODO Auto-generated method stub
		// swipeLayout.setRefreshing(false);
		Log.d("XX", response);
		if (!TextUtils.isEmpty(response)
				&& !response.contains("No comment Found")) {

			JSONObject objects;
            emojicon_edit_text.setText("");
			try {

				objects = new JSONObject(response);
				if (objects != null) {
					JSONArray feedArray = objects.getJSONArray("ShowOffComment");

					if (feedArray != null) {

						// JSONArray feedArray = response.getJSONArray("feed");

						for (int i = 0; i < feedArray.length(); i++) {
							JSONObject feedObj = (JSONObject) feedArray.get(i);

							showOffComments issues = showOffComments.processIssuesCommentJson(feedObj.toString());

                          int index = content.indexOf(issue);
                if(issues!=null &&!checkAlreadyAdded(issues))
				    content.add(issues);
						}

					}
					startAT = content.size() + 1;
					commentAdpater.notifyDataSetChanged();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

    @Override
    public void onRefresh() {

    }



    // Setting the scrolling listener for the recyclerView
	abstract class OnVerticalScrollListener extends
			RecyclerView.OnScrollListener {

		@SuppressLint("NewApi")
		@Override
		public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			if (!recyclerView.canScrollVertically(1)) {
				onScrolledToEnd();
			} else if (dy < 0) {
				onScrolledUp();
			} else if (dy > 0) {
				onScrolledDown();
			}
		}

		public void onScrolledUp() {

		}

		public void onScrolledDown() {

		}

		public void onScrolledToEnd() {
			LoadMoreData();
			// Toast.makeText(getActivity(),
			// "Scrolled to the end",Toast.LENGTH_LONG).show();
		}

	}

    @Override
    protected void onStop() {
        Intent intent = new Intent();
        intent.putExtra("json",commentAdpater.getShowItem().getIssueJson());
        intent.putExtra("index","1");
        setResult(RESULT_OK, intent);
        finish();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FeedDetailActivity.this,StartActivity.class);
        intent.putExtra("json",commentAdpater.getShowItem().getIssueJson());
        intent.putExtra("index","1");
        setResult(RESULT_OK, intent);
        finish();

    }
}
