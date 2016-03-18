package applications.apps.celsoft.com.showoff.Utilities.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import applications.apps.celsoft.com.showoff.FeedDetailActivity;
import applications.apps.celsoft.com.showoff.R;
import applications.apps.celsoft.com.showoff.ShowOff_startPage;
import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import applications.apps.celsoft.com.showoff.Utilities.models.VideoSource;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.showoffItems;
import applications.apps.celsoft.com.showoff.VideoPlayer;
import applications.apps.celsoft.com.showoff.Views.ApplicationFragments;
import im.ene.lab.toro.ToroAdapter;
import im.ene.lab.toro.ToroViewHolder;
import im.ene.lab.toro.widget.ToroVideoView;

public class IssuesAdapter extends ToroAdapter<IssuesAdapter.issuesViewHolder> {

	Context context;
	public  List<showoffItems> issuesList;
	Integer issueType;
	private IconDrawable draws;
	Activity activity;

	public IssuesAdapter(List<showoffItems> issuesList, Integer issueType,
						 Context context, Activity activity) {
		// TODO Auto-generated constructor stub
		this.issuesList = issuesList;
		this.context = context;
		this.issueType = issueType;
		this.activity = activity;

	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
        Log.e("ErrorPosition", issuesList.size() + "");
        return issuesList.size();
	}

    public void add(showoffItems item) {
        if(!issuesList.contains(item)){
            issuesList.add(item);
            notifyItemInserted(getItemCount() - 1);
        }
    }


    issuesViewHolder pvh;
	@Override

    public issuesViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
		// TODO Auto-generated method stub
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(
				R.layout.issueslistitem_layout, viewGroup, false);

		 pvh = new issuesViewHolder(v,context,issuesList);
		return pvh;
	}







	@Nullable
	@Override
	protected Object getItem(int position) {
        if(pvh!=null)
         pvh.position=position % issuesList.size();
		return issuesList.get(position );
	}





    public class issuesViewHolder
            extends ToroViewHolder implements MediaPlayer.OnCompletionListener,MediaPlayer.OnPreparedListener {

        private  IconDrawable fullScreenIco;
        CardView cv;
        TextView userName, issue_title, txt_content, txt_likes;
        TextView timeShared;
        ImageView profileimage;
        ImageView issue_image;
        ImageButton btn_share, btn_like, btn_bookmark;
        WebView issue_image_video;
        View overflow ;
        RelativeLayout videoplayerLayout;
        private ToroVideoView mVideoView;
        private ImageView mThumbnail,playVideo,restartVideo,fullScreen;
        showoffItems Item;
        private boolean mPlayable = true;
        showoffItems mItem;
        LinearLayout layout_header;
        private IconDrawable draws;

        public Integer position=0;


        public issuesViewHolder(View view) {

            super(view);
            // TODO Auto-generated constructor stub
            formatUI(view, context);
            //issue_image_video= (WebView) view.findViewById(R.id.issue_image_video);

        }
        public issuesViewHolder(View v, Context context, List<showoffItems> issuesList) {
            super(v);
            formatUI(v, context);
        }

        public issuesViewHolder(View view, final Context contexts) {
            super(view);

            formatUI(view,contexts);


        }


        void formatUI(View view, final Context context)
        {
            layout_header =(LinearLayout ) view.findViewById(R.id.layout_header);
            cv = (CardView) view.findViewById(R.id.card_view);
            userName = (TextView) view.findViewById(R.id.userName);
            issue_title = (TextView) view.findViewById(R.id.issue_title);
            txt_content = (TextView) view.findViewById(R.id.txt_content);
            timeShared = (TextView) view.findViewById(R.id.timeShared);
            profileimage = (ImageView) view.findViewById(R.id.profileimage);
            issue_image = (ImageView) view.findViewById(R.id.issue_image);
            btn_share = (ImageButton) view.findViewById(R.id.btn_share);
            btn_like = (ImageButton) view.findViewById(R.id.btn_like);
            btn_bookmark = (ImageButton) view.findViewById(R.id.btn_bookmark);
            txt_likes = (TextView) view.findViewById(R.id.txt_likes);
            mVideoView = (ToroVideoView) view.findViewById(R.id.videoplayer);
            mThumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            videoplayerLayout =(RelativeLayout) view.findViewById(R.id.videoplayerLayout);
            restartVideo=(ImageView) view.findViewById(R.id.restartVideo);
            restartVideo.setVisibility(View.INVISIBLE);
            playVideo=(ImageView) view.findViewById(R.id.playVideo);
            fullScreen=(ImageView) view.findViewById(R.id.fullScreen);


            fullScreenIco = new IconDrawable(context, FontAwesomeIcons.fa_expand).color(
                    Color.GRAY).actionBarSize();
            fullScreen.setImageDrawable(fullScreenIco);
            mThumbnail.setVisibility(View.INVISIBLE);
            mVideoView.setOnCompletionListener(this);
            fullScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent setFullscreen = new Intent(context, VideoPlayer.class);
                    setFullscreen.putExtra("URL", VideoSource.videoBaseUrl + mItem.getFilename());
                    setFullscreen.putExtra("CURP", mVideoView.getCurrentPosition());
                    setFullscreen.putExtra("FILE_NAME", mItem.getFilename());
                    setFullscreen.putExtra("REASON", "DOWNLOAD");
                    context.startActivity(setFullscreen);
                }
            });
            playVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mVideoView.isPlaying())
                    {
                        mVideoView.pause();
                        playVideo.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_media_play));
                        mThumbnail.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        mVideoView.start();
                        playVideo.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_media_pause));
                    }

                }
            });

        }



        void handleContentLikeChanged(final showoffItems issue)
        {
            IconDrawable drawlike;
            // Set the drawables for the buttons
            if (issue.getUserLike().equalsIgnoreCase("1")) {
                drawlike = new IconDrawable(context, FontAwesomeIcons.fa_heart).color(
                        Color.RED).actionBarSize();
            } else {
                drawlike = new IconDrawable(context, FontAwesomeIcons.fa_heart).color(
                        Color.GRAY).actionBarSize();
            }

            if (!TextUtils.isEmpty(issue.getLikes())
                    && !issue.getLikes().equalsIgnoreCase("0")) {
                txt_likes.setText(issue.getLikes());
                txt_likes.setVisibility(View.VISIBLE);
            } else
                txt_likes.setVisibility(View.GONE);

            btn_like.setImageDrawable(drawlike);

        }


        View.OnClickListener  showUserProfileonclickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ((ShowOff_startPage)context).userProfileActivity(showItem.getFileOwner(),showItem.getLikes(),showItem.getUserLike());
            }
        };
        showoffItems showItem=null;
        void loadDataContent(final showoffItems issue)
        {

            if (issue != null) {
                showItem = issue;
                userName.setText(issue.getFileOwner().getFullName());


                if(!TextUtils.isEmpty(issue.getTitle()) && !issue.getTitle().equalsIgnoreCase("null"))
                {
                    issue_title.setText(issue.getTitle());
                    issue_title.setVisibility(View.VISIBLE);
                }
                else
                    issue_title.setVisibility(View.GONE);
                if (issue.getContent().length() <= 150)
                    txt_content.setText(issue.getContent());
                else
                    txt_content.setText(issue.getContent().substring(0,
                            150)
                            + " ...");
                timeShared.setText(issue.getDate_uploaded());
                String mediatype= issue.getFiletype();

                if(mediatype.equalsIgnoreCase("Image") || mediatype.equalsIgnoreCase("img"))
                {
                    issue_image.setVisibility(View.VISIBLE);
                    issue_image.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            String menu[]= {"Download"};
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setItems(menu, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   // Intent intent = new Intent(context, UserProfileSettingPage.class);
                                    switch (which)
                                    {
                                        case 0:

                                           // intent.putExtra("PURPOSE","changeDp");

                                            break;
                                        case 1:
                                            //intent.putExtra("PURPOSE","changeBanner");
                                            break;


                                    }
                                    //startActivity(intent);
                                }
                            })
                                    .show();

                            return true;
                        }
                    });
                    videoplayerLayout.setVisibility(View.GONE);
                    //Then set the image url of the image attached
                    if (!TextUtils.isEmpty(issue.getFilename())
                            && !issue.getFilename().equalsIgnoreCase("null")) {

                        Drawable tempdrawable = issue_image.getDrawable();
                        if (tempdrawable!=null)
                        {
                            Picasso.with(context)
                                    .load(AppBackBoneClass.parentUrL+AppBackBoneClass.issueAttachmentFolder
                                            + issue.getFilename()).resize(250, 250)
                                    .centerCrop().error(R.drawable.navigationbanner)
                                    .placeholder(tempdrawable)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .into(issue_image);
                        }
                        else
                        {
                            Picasso.with(context)
                                    .load(AppBackBoneClass.parentUrL+AppBackBoneClass.issueAttachmentFolder
                                            + issue.getFilename()).resize(250, 250)
                                    .centerCrop().error(R.drawable.navigationbanner)
                                    .placeholder(R.drawable.navigationbanner)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .into(issue_image);
                        }

                        issue_image.setVisibility(View.VISIBLE);
                    } else {
                        issue_image.setVisibility(View.GONE);
                    }

                }
                else if(mediatype.equalsIgnoreCase("TEXT"))
                {
                    videoplayerLayout.setVisibility(View.GONE);
                    issue_image.setVisibility(View.GONE);
                }
                else
                {
                    videoplayerLayout.setVisibility(View.VISIBLE);
                    issue_image.setVisibility(View.GONE);
                    Log.e("VIPATH", VideoSource.videoBaseUrl + issue.getFilename());
                    mVideoView.setVideoPath(VideoSource.videoBaseUrl+issue.getFilename());

                    //Incase the filetype is Videos

                    //manageVideoUrls(issue,viewHolder.issue_image_video);
                }

                handleContentLikeChanged(issue);

                draws = new IconDrawable(context, FontAwesomeIcons.fa_user).color(
                        Color.GRAY).actionBarSize();
                TextDrawable drawable;
                if(issue.getFileOwner().getFullName()!=null) {
                    drawable = TextDrawable.builder().buildRound(
                            issue.getFileOwner().getFullName().substring(0, 1),
                            Color.RED);
                }
                else
                {
                    drawable = TextDrawable.builder().buildRound(
                            "UN",
                            Color.RED);
                }
                if (!issue.getFileOwner().getImagePath().equalsIgnoreCase("null")) {
                    AppBackBoneClass.loadUserImage(profileimage,
                            AppBackBoneClass.parentUrL+AppBackBoneClass.usersimageURL + "/"
                                    + issue.getFileOwner().getImagePath(), context,
                            draws);
                    // Toast.makeText(context, "lol",Toast.LENGTH_LONG).show();
                } else {
                    profileimage.setImageDrawable(drawable);
                }

                btn_share.setOnClickListener(new OnAlbumOverflowSelectedListener
                        (AppBackBoneClass.context, issue, position));
            }

            IconDrawable drawshare = new IconDrawable(context, FontAwesomeIcons.fa_ellipsis_v)
                    .color(Color.GRAY).actionBarSize();
            IconDrawable drawbookmark = new IconDrawable(context,
                    FontAwesomeIcons.fa_comment).color(Color.GRAY).actionBarSize();

            btn_share.setImageDrawable(drawshare);
            btn_bookmark.setImageDrawable(drawbookmark);

//Handle comment Button Click
            btn_bookmark.setOnClickListener( new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    showPostDetails(issue);
                }
            });


            if(AppBackBoneClass.currentAppPage<1) {
                userName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        ((ShowOff_startPage)context).userProfileActivity(showItem.getFileOwner(),showItem.getLikes(),showItem.getUserLike());
                    }
                });
                profileimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        ((ShowOff_startPage)context).userProfileActivity(showItem.getFileOwner(),showItem.getLikes(),showItem.getUserLike());
                    }
                });
                layout_header.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        ((ShowOff_startPage)context).userProfileActivity(showItem.getFileOwner(),showItem.getLikes(),showItem.getUserLike());
                    }
                });
            }

            // Now Handle PostLikes

            btn_like.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    String reason = issue.getUserLike().equalsIgnoreCase("1") ? "UnLike%20Post"
                            : "Like%20Post";
                    String url = AppBackBoneClass.parentUrL+AppBackBoneClass.feedUrl + "?reason=" + reason + "&PID="
                            + issue.getItemID() + "&UID="+ AppBackBoneClass.getUserId();

                    Ion.with(context).load(url)

                            .asString()

                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {
                                    // do stuff with the result or error
                                    if (e == null) {
                                        // Toast.makeText(context, result+"hello",
                                        // Toast.LENGTH_LONG).show();

                                        try {
                                            handleResults(result,position);
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                        Log.d("redHat", result);
                                    } else {
                                        Toast.makeText(context, e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                        // e.printStackTrace();
                                    }
                                }

                            });


                    // Toast.makeText(context, reason, Toast.LENGTH_LONG).show();
                }

                private void handleResults(String result,int position) throws JSONException {
                    // TODO Auto-generated method stub
                    IconDrawable drawlike;

                    Integer likes = Integer.parseInt(issue.getLikes());
                    if(likes<0)
                        likes=0;
                    if (result.replace(" ", "").equalsIgnoreCase("err")) {
                        // Error occured so nothing is done
                    } else {

                        JSONObject object = new JSONObject(result);
                        String status= object.getString("status");
                        String count= object.getString("count");
                        if (status.replace(" ", "").equalsIgnoreCase("Liked")) {
                            // UserLiked the content


                            likes = likes + 1;
                            issue.setUserLike("1").setLikes(count + "");
                            handleContentLikeChanged(issue);
                            //adapter.notifyItemChanged(position);
                        } else if (status.replace(" ", "").equalsIgnoreCase("Unliked")) {
                            // Unliked so restore button to gray
                            // drawlike = new IconDrawable(context, FontAwesomeIcons.fa_heart)
                            // .color(Color.GRAY).actionBarSize();
                            //btn_like.setImageDrawable(drawlike);

                            likes = Integer.parseInt(issue.getLikes());
                            if (likes <= 0)
                                likes = 1;
                            likes = likes - 1;
                            issue.setUserLike("0").setLikes(count + "");
                            handleContentLikeChanged(issue);
                            //adapter.notifyItemChanged(position);
                        }

                    }

                }


            });

            txt_content.setOnClickListener( new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    //Toast.makeText(activity, "Hello", Toast.LENGTH_LONG).show();
                    showPostDetails(issue);
                }


            });

            issue_title.setOnClickListener( new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    //Toast.makeText(activity, "Hello", Toast.LENGTH_LONG).show();
                    showPostDetails(issue);
                }


            });
        }


        public void bind(@Nullable Object object) {
            if (mVideoView != null && object instanceof showoffItems) {
                mItem = (showoffItems) object;

                loadDataContent(mItem);


















            }
        }


        void handlePostShare() {

        }


        @Override
        public void onViewHolderBound() {
            Picasso.with(itemView.getContext())
                    .load(R.drawable.navigationbanner)
                    .fit()
                    .centerInside()
                    .into(mThumbnail
                    );
        }

        @Override
        public void onVideoPrepared(MediaPlayer mp) {
            mPlayable = true;

            mp.setVolume(0f, 0f);
            mp.setLooping(false);
        }

        @Override
        public void onPlaybackError(MediaPlayer mp, int what, int extra) {
            mPlayable = false;
        }

        @Override
        public void onPlaybackStarted() {
            mThumbnail.setVisibility(View.INVISIBLE);
            playVideo.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_media_pause));
            mItem.setPlayState(showoffItems.videoplayState.PLAYING);
            restartVideo.setVisibility(View.INVISIBLE);
            //Toast.makeText(context,"Started",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPlaybackPaused() {
            mThumbnail.setVisibility(View.VISIBLE);
            mThumbnail.setVisibility(View.INVISIBLE);
            mItem.setPlayState(showoffItems.videoplayState.PAUSED);
            playVideo.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_media_play));
            restartVideo.setVisibility(View.INVISIBLE);
            //Toast.makeText(context,"pause",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPlaybackStopped() {
            mThumbnail.setVisibility(View.INVISIBLE);
            //restartVideo.setVisibility(View.VISIBLE);
            mItem.setPlayState(showoffItems.videoplayState.STOPPED);
            //Toast.makeText(context,"Stopped",Toast.LENGTH_LONG).show();
            playVideo.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_media_play));
        }


        @Override
        public boolean wantsToPlay() {
            return false;
            //return super.visibleAreaOffset() >= 0.5;
        }

        @Override
        public boolean isAbleToPlay() {
            return mPlayable;
        }





        @Nullable
        @Override
        public String getVideoId() {
            return (long) mItem.hashCode() + " - " + getAdapterPosition();
        }

        @NonNull
        @Override
        public View getVideoView() {
            return mVideoView;
        }

        @Override
        public void start() {
            mVideoView.start();
        }

        @Override
        public void pause() {
            mVideoView.pause();
        }

        @Override
        public int getDuration() {
            return mVideoView.getDuration();
        }

        @Override
        public int getCurrentPosition() {
            return mVideoView.getCurrentPosition();
        }

        @Override
        public void seekTo(int pos) {
            mVideoView.seekTo(pos);
        }




        @Override
        public boolean isPlaying() {
            return mVideoView.isPlaying();
        }











        private void showPostDetails(showoffItems issue) {
            // TODO Auto-generated method stub
            //Toast.makeText(activity, "Hello", Toast.LENGTH_LONG).show();
            try
            {
                JSONObject object= showItem.getShowItemJson();
                if(object!=null)
                {
                   // Toast.makeText(context, showItem.getLikes()+" ~~~ "+showItem.getUserLike(), Toast.LENGTH_SHORT).show();
                    AppBackBoneClass.updateJsonObject(object, "likes", showItem.getLikes());
                    AppBackBoneClass.updateJsonObject(object,"user_like",showItem.getUserLike());
                }
            }
            catch (Exception e)
            {

            }
            Intent intent= new Intent(context, FeedDetailActivity.class);
            intent.putExtra("json", issue.getIssueJson());
            intent.putExtra("index", issuesList.indexOf(issue) + "");
            ((Activity)context).startActivityForResult(intent, ApplicationFragments.ITEM_CHANGED_RESULTS);

        }

        private class MyBrowser extends WebViewClient {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        }



        class OnAlbumOverflowSelectedListener  implements View.OnClickListener,PreferenceManager.OnActivityDestroyListener {

            private showoffItems issues;
            private Context mContext;
            //ViewGroup vGroup;
            private Integer position;
            PopupMenu popupMenu;
            public OnAlbumOverflowSelectedListener(Context context, showoffItems item,Integer itemPosition) {
                mContext = context;
                //MenuItem
                this.issues = item;
                //this.vGroup = vgroup;
                this.position= itemPosition;
            }

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub



                popupMenu = new PopupMenu(mContext, v) {


                    @Override
                    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {


                        if(issues!=null && issues.getItemID()!=null && !TextUtils.isEmpty(issues.getFileOwner().getFullName()))
                        {
                            switch (item.getItemId()) {
                                case 4:
                                    //Bookmarking issue

                                    break;
                                case R.id.action_cancel:
                                    //Bookmarking issue

                                    break;
                                case R.id.action_share:
                                    //Bookmarking issue
                                    AppBackBoneClass.handlePostShare(issues, context);
                                    break;
                                case 3:
                                    AlertDialog.Builder builder= new AlertDialog.Builder(context);
                                    builder.setMessage("Are you sure?")
                                            .setTitle("Delete post")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // TODO Auto-generated method stub
                                                    deletePost(issues);
                                                    //deletRequest(mPrayer.getFeedID(),mPrayer, position);
                                                }


                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // TODO Auto-generated method stub

                                                }
                                            })
                                            .show();

                                    break;

                                case 2: ///This will show response from the church

                                    break;

                                default:
                                    break;
                            }
                        }
                        return true;

                    }
                };

                popupMenu.inflate(R.menu.issuefeedoverflow);


                IconDrawable drawdelete = new IconDrawable(context, FontAwesomeIcons.fa_remove)
                        .color(Color.red(200)).actionBarSize();
                //viewHolder.btn_like.setImageDrawable(drawlike);

                if(issues!=null)
                {
//				if(!mPrayer.getStatus().equalsIgnoreCase("pending"))
//				   popupMenu.getMenu().add(1, 2, 10, "Responses");



                    if(!AppBackBoneClass.getUserId().equalsIgnoreCase(issues.getFileOwner().getUserID()))
                    {
                        popupMenu.getMenu().add(1, 2, 10, "Report");
                    }
                    else
                    {
                        popupMenu.getMenu().add(1, 3, 10, "Delete").setIcon(drawdelete);
                        popupMenu.getMenu().add(1, 4, 10, "Bookmark").setIcon(drawdelete);
                    }

                }



                popupMenu.show();




            }

            void deletePost(final showoffItems issue) {


                String reason =  "DeletePost";
                String url = AppBackBoneClass.parentUrL+AppBackBoneClass.feedUrl + "?reason=" + reason + "&app_code=4Celsoft5&post="
                        + issue.getItemID() + "&UID="+ AppBackBoneClass.getUserId()+"&file_name="+ issue.getFilename();

                Ion.with(context).load(url)

                        .asString()

                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                // do stuff with the result or error
                                if (e == null) {

                                    handleResults(result,position,issue);

                                    Log.d("redTage", result);
                                } else {
                                    Toast.makeText(context, e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                    // e.printStackTrace();
                                }
                            }



                        });



            }

            void status()
            {
                //getMenuInflater().inflate(R.menu.global, menu);


            }

            @Override
            public void onActivityDestroy() {
                // TODO Auto-generated method stub
                if(popupMenu!=null)
                    popupMenu.dismiss();
            }


        }



        private void handleResults(String result, Integer position, showoffItems issue) {
            // TODO Auto-generated method stub
            Toast.makeText(context,result,Toast.LENGTH_LONG).show();
            if(result.equalsIgnoreCase("Deleted"))
            {
                int index = issuesList.indexOf(issue);
                issuesList.remove(issue);
                notifyItemRemoved(index);
                //Toast.makeText(context, position+"", Toast.LENGTH_LONG).show();
            }
        }


    }
}
