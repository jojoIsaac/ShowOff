package applications.apps.celsoft.com.showoff.Views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.github.clans.fab.FloatingActionButton;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import applications.apps.celsoft.com.showoff.ChooseMedia;
import applications.apps.celsoft.com.showoff.PostDataToserver;
import applications.apps.celsoft.com.showoff.R;
import applications.apps.celsoft.com.showoff.ShowOff_startPage;
import applications.apps.celsoft.com.showoff.Utilities.Adapters.IssuesAdapter;
import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.AppUser;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.showoffItems;
import applications.apps.celsoft.com.showoff.VideoPlayer;
import butterknife.Bind;
import butterknife.ButterKnife;
import im.ene.lab.toro.Toro;

public abstract class ApplicationFragments extends Fragment implements OnRefreshListener {

    public abstract void setMenuIndex(Integer index);
    protected ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }
	protected View rootView;
    @Bind(R.id.rv)
	protected  RecyclerView recyclerView;
	@Bind(R.id.swipe_container)
	protected  SwipeRefreshLayout swipeLayout;
	private IconDrawable draws;
	//public static Integer currentIssues;
	public static IssuesAdapter adapter;
    public static List<showoffItems> issuesList;
	private static int startAT;
	private Bundle data;
    public static Integer ITEM_CHANGED_RESULTS=90;

    public static   LinearLayout layout_newStory;
	@Bind(R.id.fab)
	FloatingActionButton floatingActionButton;
	@Nullable
	public View onCreateParentView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		rootView= inflater.inflate(R.layout.fragment_health, container, false);
		ButterKnife.bind(this,rootView);
     layout_newStory= (LinearLayout) rootView.findViewById(R.id.layout_newStory);
        layout_newStory.setVisibility(View.GONE);
        layout_newStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadInitialFeeds();
            }
        });

		//recyclerView = (RecyclerView)rootView.findViewById(R.id.rv);
	    final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
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
	  
		issuesList= new ArrayList<showoffItems>();
		adapter= new IssuesAdapter(issuesList, currentIssues, AppBackBoneClass.context, (AppCompatActivity) AppBackBoneClass.context);
		
		recyclerView.setAdapter(adapter);
        onRefresh();

		
		
		
		
		
		recyclerView.addOnScrollListener(new OnVerticalScrollListener() {

        });
		floatingActionButton.setImageDrawable(new IconDrawable(getActivity(), FontAwesomeIcons.fa_plus).actionBarSize().color(Color.WHITE));
		floatingActionButton
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ShowOff_startPage)(getActivity())).handleDataPicker();
                    }
                });
		
		return rootView;
	}

    public static final int REQUEST_IMAGE_CAPTURE = 1,REQUEST_VIDEO_CHOOSE = 2,REQUEST_IMAGE_CHOOSE = 3,REQUEST_VIDEO_CAPTURE = 4;;


    static ImageView icon;
	
	/*
	
	public void setupfloatingMenu1(int show) {
	
		if (show > 0 ) {
			// Create an icon
			
			
			   MainActivity.floaticon = new ImageView(getActivity());
		MainActivity.floaticon.setVisibility(View.VISIBLE);
		        IconDrawable draws= 
						new IconDrawable(getActivity(), IconValue.fa_edit).colorRes(R.color.ColorPrimary)
						   .actionBarSize();
				MainActivity.floaticon.setImageDrawable(draws);
				
			centerActionButton = new FloatingActionButton.Builder(getActivity())
					.setTheme(FloatingActionButton.THEME_LIGHT)
					.setContentView(MainActivity.floaticon)
					.setPosition(FloatingActionButton.POSITION_BOTTOM_RIGHT)
					.build();
				
			

			SubActionButton.Builder itemBuilder = new SubActionButton.Builder(
					getActivity());
			ImageView itemIcon3 = new ImageView(getActivity());
			 draws= 
						new IconDrawable(getActivity(), IconValue.fa_pencil).color(Color.rgb(200, 100, 100))
						   .actionBarSize();
			itemIcon3.setImageDrawable(draws);
			btnnote = itemBuilder.setContentView(itemIcon3).build();
			btnnote.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					handleFloatButtons(v, 1);
				}
			});
			// repeat many times:
			ImageView itemIcon = new ImageView(getActivity());
			 draws= 
						new IconDrawable(getActivity(), IconValue.fa_image).color(Color.GRAY)
						   .actionBarSize();
			itemIcon.setImageDrawable(draws);
			btncomment = itemBuilder.setContentView(itemIcon).build();
			// handleFloatButtons(btncomment, 2);
			btncomment.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					handleFloatButtons(v, 2);
				}
			});

			ImageView itemIcon4 = new ImageView(getActivity());
			 draws= 
						new IconDrawable(getActivity(), IconValue.fa_info).color(Color.GRAY)
						   .actionBarSize();
			itemIcon4.setImageDrawable(draws);
			SubActionButton btnSecrete = itemBuilder.setContentView(itemIcon4)
					.build();
			btnSecrete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//handleFloatButtons(v, 3);
				}
			});
			// btncomment.setOnClickListener(l)

			actionMenu = new FloatingActionMenu.Builder(getActivity())
					// .setStartAngle(0) // A whole circle!
					// .setEndAngle(180)
					.setRadius(
							getResources().getDimensionPixelSize(
									R.dimen.radius_small))
					.addSubActionView(btnnote).addSubActionView(btncomment)

					// .addSubActionView(btnshare)
					.addSubActionView(btnSecrete).attachTo(centerActionButton)
					.enableAnimations()

					.build();
		} else {
			if (centerActionButton != null) {
				actionMenu.close(true);
				// centerActionButton.removeAllViews();
				centerActionButton.setVisibility(View.GONE);
				/*
				 * btnSecrete.setVisibility(View.GONE); btnnote
				 * .setVisibility(View.GONE);
				 * btncomment.setVisibility(View.GONE);
				 */
			/*	MainActivity.floaticon.setVisibility(View.GONE);
			}
			if (actionMenu != null) {

			}
		}

	}
*/







	public void showDialog(String intent) {
		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		PostDataToserver newFragment = new PostDataToserver();
		data = new Bundle();
		data.putString("issues", currentIssues.toString());
		data.putString("intent", intent);
		

		newFragment.setArguments(data);
		// if (mIsLargeLayout) {
		// The device is using a large layout, so show the fragment as a dialog
		newFragment.show(fragmentManager, "dialog");
		// }

		/*
		 * else { // The device is smaller, so show the fragment fullscreen
		 * FragmentTransaction transaction = fragmentManager.beginTransaction();
		 * // For a little polish, specify a transition animation
		 * transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		 * // To make it fullscreen, use the 'content' root view as the
		 * container // for the fragment, which is always the root view for the
		 * activity transaction.add(R.id.container, newFragment)
		 * .addToBackStack(null).commit(); }
		 */
	}

	protected void handleFloatButtons(View v, int i) {
		// TODO Auto-generated method stub
		if(i==1)
		{
			showDialog("");
		}
		else if(i==2)
		{
			showDialog("");
		}
	}




	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
        AppBackBoneClass.currentAppPage=0;
		loadInitialFeeds();
	}
	

	
	
	


	protected void showErrorDialog(String error) {
		// mInError = true;

		AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
		if (!TextUtils.isEmpty(error)) {
			b.setMessage("Response : " + error);
		} else {
			b.setMessage("Response : " + "Unable to perform requested action");
		}
		b.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                //handleServerRequest(churchgroup, btntoggle_status);
            }

        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }

        }).show();
	}
	
	
	public  void loadInitialFeeds() {

		Ion.with(this)
		.load(AppBackBoneClass.parentUrL + AppBackBoneClass.feedUrl
				)
				.setBodyParameter("reason", "Load showoffs")
				.setBodyParameter("type", currentIssues+"")
				.setBodyParameter("UID", AppBackBoneClass.getUserId())
				.setBodyParameter("startPage", "1")
				.asString()

                .setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception e, String result) {
						// do stuff with the result or error
						if (e == null) {
							swipeLayout.setRefreshing(false);
							//Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();

							issuesList = new ArrayList<showoffItems>();
							adapter = new IssuesAdapter(issuesList, currentIssues, AppBackBoneClass.context, (AppCompatActivity) AppBackBoneClass.context);

							recyclerView.setAdapter(adapter);
							processIssuesJson(result);
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




	public  void processIssuesJson(String response) {
		// TODO Auto-generated method stub
		swipeLayout.setRefreshing(false);
        layout_newStory.setVisibility(View.GONE);
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

						    if(issues!=null && issuesList.indexOf(issues)<0 )
						    {


								if(issues.getFiletype().equalsIgnoreCase("Image") && issues.getFiletype().equalsIgnoreCase("Img")&&
                                       issues.getFiletype().equalsIgnoreCase("Text")  ) {
									issuesList.add(issues);
								}
                                else
                                {
                                    if(!TextUtils.isEmpty(issues.getFilename()) ) {
										issuesList.add(issues);

									}
                                }
							}
                            else
                            {
                                Log.e("KLOP","NULLLLLLLL");
                            }
						}
						
					}
					startAT = issuesList.size() + 1;
					adapter.notifyDataSetChanged();
				}
                else
                {
                    Log.e("POL",90000000+"");
                }
               // Toast.makeText(getActivity(), adapter.issuesList.size()+"", Toast.LENGTH_SHORT).show();
            }catch(Exception e)
			{
				e.printStackTrace();
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
	    Ion.with(getActivity())
		.load(AppBackBoneClass.parentUrL + AppBackBoneClass.feedUrl)
                .setBodyParameter("reason", "Load showoffs")
                .setBodyParameter("type", currentIssues + "")
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
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    //e.printStackTrace();
                }
            }


        });
	
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

/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== ApplicationFragments.ITEM_CHANGED_RESULTS && resultCode == getActivity().RESULT_OK )
        {
            String itemString = data.getStringExtra("json");
            String itemIndex= data.getStringExtra("index");
            Log.e("JSONYU", itemString);
        }
    }
    */

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
	
	//Abstract method for all derieved classes
	public abstract void restoreTitle(); //This function changes the title shown on the Toolbar
	public static Integer currentIssues=-1; // The number indicate the category of Items to be shown on a page. -1 indicate unsorted



 String tempFilename="tempfile_";
}
