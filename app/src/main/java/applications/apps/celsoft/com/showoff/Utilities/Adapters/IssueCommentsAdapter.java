package applications.apps.celsoft.com.showoff.Utilities.Adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
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

import applications.apps.celsoft.com.showoff.R;
import applications.apps.celsoft.com.showoff.UserProfile;
import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import applications.apps.celsoft.com.showoff.Views.ApplicationFragments;
import applications.apps.celsoft.com.showoff.Utilities.models.VideoSource;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.AppUser;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.showOffComments;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.showoffItems;
import applications.apps.celsoft.com.showoff.VideoPlayer;
import im.ene.lab.toro.ToroAdapter;
import im.ene.lab.toro.ToroViewHolder;
import im.ene.lab.toro.widget.ToroVideoView;

public class IssueCommentsAdapter extends ToroAdapter<IssueCommentsAdapter.commentViewholder> {

	private Context context;
	private List<showOffComments> comments;
	showoffItems lifeIssue;
	private IconDrawable draws;
	private static final int TYPE_HEADER = 0; // Declaring Variable to
	// Understand which View is
	// being worked on
	// IF the view under inflation and population is header or Item
	private static final int TYPE_ITEM = 1;



    @Nullable
    @Override
    protected Object getItem(int position) {
        //if(pvh!=null)
            //pvh.position=position % issuesList.size();
        Log.e("CountErr",position +"~~~~ "+ comments.size() );
        if(position>0) {


            return comments.get(position - 1);
        }
        else
            return lifeIssue;
    }


    public IssueCommentsAdapter(List<showOffComments> commentsList,
								showoffItems issue, Context passedContext) {

		this.context = passedContext;
		comments = commentsList;
		lifeIssue = issue;
	}

	public IssueCommentsAdapter() {
		// TODO Auto-generated constructor stub
	}

	// This method returns the number of items present in the list
	@Override
	public int getItemCount() {
		return comments.size() + 1; // the number of items in the list will be
									// +1 the titles including the header
									// view.
	}

	// Witht the following method we check what type of view is being passed
	@Override
	public int getItemViewType(int position) {
		if (isPositionHeader(position))
			return TYPE_HEADER;

		return TYPE_ITEM;
	}
    public showoffItems getShowItem()
    {
        return lifeIssue;
    }

	private boolean isPositionHeader(int position) {
		return position == 0;
	}


    commentViewholder vhItem;
	@Override
	public commentViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
		// TODO Auto-generated method stub

		if (viewType == TYPE_ITEM) {
			View v = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.comment_itemlayout, parent, false); // Inflating
																	// the
																	// layout

		vhItem = new commentViewholder(v, viewType,
					context); // Creating
			// ViewHolder
			// and passing
			// the object of
			// type view

			return vhItem; // Returning the created object

			// inflate your layout and pass it to view holder

		} else if (viewType == TYPE_HEADER) {

			View v = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.issueheaders, parent, false); // Inflating the
															// layout

			commentViewholder vhHeader = new commentViewholder(v, viewType,
					context); // Creating
			// ViewHolder
			// and passing
			// the object of
			// type view

			return vhHeader; // returning the object created

		}
		return null;
	}

	class commentViewholder extends ToroViewHolder implements MediaPlayer.OnCompletionListener, OnClickListener,View.OnCreateContextMenuListener {




        //ONclick listeners
        private OnClickListener myDeleteListener= new OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog  build = new AlertDialog.Builder(context)
                        .setMessage("Delete Comment?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Ion.with(context)
                                        .load(AppBackBoneClass.parentUrL+AppBackBoneClass.feedUrl)
                                        .setBodyParameter("reason","Delete ShowOff Comment")
                                        .setBodyParameter("commentID",comment.getCommentID())
                                        .setBodyParameter("userID",comment.getSender().getUserID())
                                        .asString()
                                        .setCallback(new FutureCallback<String>() {
                                            @Override
                                            public void onCompleted(Exception e, String result) {
                                                Toast.makeText(context  , result, Toast.LENGTH_SHORT).show();
                                                if(e==null)
                                                {
                                                    //
                                                    //

                                                    if (!TextUtils.isEmpty(result))
                                                    {
                                                        try
                                                        {
                                                            JSONObject object = new JSONObject(result);
                                                            if(object!=null)
                                                            {
                                                                String status= object.getString("status");
                                                                if(status.equalsIgnoreCase("Success"))
                                                                {
                                                                    comments.remove(comment);
                                                                    notifyItemRemoved(position+1);
                                                                }
                                                            }
                                                        }
                                                        catch (Exception ex)
                                                        {
                                                            ex.printStackTrace();
                                                        }
                                                    }
                                                }
                                                else
                                                {
                                                    e.printStackTrace();
                                                }
                                            }
                                        })
                                ;

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

            }
        };
        private OnClickListener commentLikeListener= new OnClickListener() {

            @Override

                public void onClick (View v){
                    // TODO Auto-generated method stub
                    String reason = comment.getUserLikeItem().equalsIgnoreCase("1") ? "UnLike Post"
                            : "Like Post";
                    String url = AppBackBoneClass.parentUrL + AppBackBoneClass.feedUrl ;

                    Ion.with(context).load(url)
                            .setBodyParameter("reason",reason)
                            .setBodyParameter("PID",comment.getCommentID())
                            .setBodyParameter("UID",AppBackBoneClass.getUserId())
                            .setBodyParameter("object_type","FeedObjectComment")
                            .asString()

                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {
                                    // do stuff with the result or error
                                    if (e == null) {
                                        // Toast.makeText(context, result+"hello",
                                        // Toast.LENGTH_LONG).show();

                                        try {
                                            handleResults(result, position);
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

            private void handleResults(String result, int position) throws JSONException {
                // TODO Auto-generated method stub
                IconDrawable drawlike;

                Integer likes = Integer.parseInt(comment.getCommentLike());
                if (likes < 0)
                    likes = 0;
                if (result.replace(" ", "").equalsIgnoreCase("err")) {
                    // Error occured so nothing is done
                } else {

                    JSONObject object = new JSONObject(result);
                    String status = object.getString("status");
                    String count = object.getString("count");
                    if (status.replace(" ", "").equalsIgnoreCase("Liked")) {
                        // UserLiked the content


                        likes = likes + 1;
                        comment.setUserLikeItem("1").setCommentLike(count + "");
                        handleContentLikeChanged( comment);
                        //adapter.notifyItemChanged(position);
                    } else if (status.replace(" ", "").equalsIgnoreCase("Unliked")) {
                        // Unliked so restore button to gray
                        // drawlike = new IconDrawable(context, FontAwesomeIcons.fa_heart)
                        // .color(Color.GRAY).actionBarSize();
                        //btn_like.setImageDrawable(drawlike);

                        likes = Integer.parseInt(comment.getCommentLike());
                        if (likes <= 0)
                            likes = 1;
                        likes = likes - 1;
                        comment.setUserLikeItem("0").setCommentLike(count + "");
                        handleContentLikeChanged(comment);
                        //adapter.notifyItemChanged(position);
                    }

                }

            }


        };

        void  handleContentLikeChanged(Object item)
        {
            if(item instanceof  showOffComments)
            {
                //Change likes if user clicks on the like button
                if(comment.getUserLikeItem().equalsIgnoreCase("1"))
                {
                     txt_likecomment.setTextColor(context.getResources().getColor(R.color.text_liked_comment));

                }
                else
                {
                    txt_likecomment.setTextColor(context.getResources().getColor(R.color.text_like_comment));
                }
                if(!comment.getCommentLike().equalsIgnoreCase("")&&!comment.getCommentLike().equalsIgnoreCase("0"))
                  txt_likecomment.setText("Like ("+comment.getCommentLike()+")");
                else
                   txt_likecomment.setText("Like");
            }
            else if(item instanceof showoffItems)
            {

            }
        }
        private OnClickListener commentReportListener= new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        };




        private  IconDrawable fullScreenIco;
        int Holderid;
		private Context contxt;

		// The Header Controls
        RelativeLayout videoplayerLayout;
        private ToroVideoView mVideoView;
        private ImageView mThumbnail,playVideo,restartVideo,fullScreen;
		CardView cv;
		TextView userName, issue_title, txt_content, txt_likes;
		TextView timeShared;
		ImageView profileimage;
		ImageView issue_image;
		ImageButton btn_share;
		ImageButton btn_like;
		ImageButton btn_bookmark;
		LinearLayout layout_actionarea;
		 ContextMenuInfo info;
		// Comment Items
		public TextView cuserName, ctxt_likes, ctxt_content, txt_timeago,txt_likecomment;
		ImageView cprofileimage;
		private TextView txt_deletes;
		private TextView txt_report;
		
		showOffComments comment;

        private boolean mPlayable = true;
        showoffItems mItem;
        LinearLayout layout_header;
        private IconDrawable draws;

        public Integer position=0;
		public void setComment(showOffComments comment) {
			this.comment = comment;
		}
		public showOffComments getComment() {
			return comment;
		}

		public commentViewholder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
		}

        View.OnClickListener  showUserProfileonclickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                if(!lifeIssue.getFileOwner().getUserID().equalsIgnoreCase(AppBackBoneClass.getUserId()))
                {
                    //Toast.makeText(context,showItem.getFileOwner().getUserJson() , Toast.LENGTH_LONG).show();
                    //Create an intent to open the user profile page
                  openUserProfile(lifeIssue.getFileOwner());
                }
            }
        };

        View.OnClickListener  showCommentUserProfileonclickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                if(!comment.getSender().getUserID().equalsIgnoreCase(AppBackBoneClass.getUserId()))
                {
                    //Toast.makeText(context,showItem.getFileOwner().getUserJson() , Toast.LENGTH_LONG).show();
                    //Create an intent to open the user profile page
                    openUserProfile(comment.getSender());
                }
            }
        };

        void openUserProfile(AppUser user)
        {
            Intent userprofileIntent= new Intent(context, UserProfile.class);
            userprofileIntent.putExtra("USER_JSON",user.getUserJson());
            context.startActivity(userprofileIntent);
        }
		public commentViewholder(View view, int viewType, final Context context) {
			// TODO Auto-generated constructor stub
			super(view);
			contxt = context;

			// itemView.setOnClickListener(this);
			// Here we set the appropriate view in accordance with the the view
			// type as passed when the holder object is created

			if (viewType == TYPE_ITEM) {
			// The comment item control here
				cuserName = (TextView) view.findViewById(R.id.userName);
                cuserName.setOnClickListener(showCommentUserProfileonclickListener);
				//ctxt_likes = (TextView) view.findViewById(R.id.txt_likes);
				ctxt_content = (TextView) view.findViewById(R.id.txt_content);
				txt_timeago = (TextView) view.findViewById(R.id.txt_timeago);
				cprofileimage = (ImageView) view
						.findViewById(R.id.profileimage);
                cprofileimage.setOnClickListener(showCommentUserProfileonclickListener);
				txt_likecomment = (TextView) view.findViewById(R.id.txt_likecomment);
				txt_deletes=( TextView) view.findViewById(R.id.txt_deletes);
				txt_report=(TextView) view.findViewById(R.id.txt_report);
				Holderid = 1; // setting holder id as 1 as the object being
								// populated are of type item row
				
				
				txt_deletes.setOnClickListener( myDeleteListener);
				txt_likecomment.setOnClickListener(commentLikeListener);
				txt_report.setOnClickListener( commentReportListener);


                //comment = (showOffComments) comments.get(this.position);
                try {
                    //onBindViewHolders(this);
                    //Toast.makeText(contxt, "Errororr", Toast.LENGTH_SHORT).show();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

			} else { // That for the header
                 mItem = lifeIssue;
				cv = (CardView) view.findViewById(R.id.card_view);
				userName = (TextView) view.findViewById(R.id.userName);
				issue_title = (TextView) view.findViewById(R.id.issue_title);
				txt_content = (TextView) view.findViewById(R.id.txt_content);
				timeShared = (TextView) view.findViewById(R.id.timeShared);
				profileimage = (ImageView) view.findViewById(R.id.profileimage);
				issue_image = (ImageView) view.findViewById(R.id.issue_image);
				btn_share = (ImageButton) view.findViewById(R.id.btn_share);
				btn_like = (ImageButton) view.findViewById(R.id.btn_like);
				btn_bookmark = (ImageButton) view
						.findViewById(R.id.btn_bookmark);
				txt_likes = (TextView) view.findViewById(R.id.txt_likes);
				layout_actionarea = (LinearLayout) view
						.findViewById(R.id.layout_actionarea);
                layout_header=(LinearLayout) view.findViewById(R.id.layout_header);

				Holderid = 0; // Setting holder id = 0 as the object being
								// populated are of type header view
                if(!AppBackBoneClass.getUserId().equalsIgnoreCase(lifeIssue.getFileOwner().getUserID())) {
                    userName.setOnClickListener(showUserProfileonclickListener);
                    profileimage.setOnClickListener(showUserProfileonclickListener);
                    layout_header.setOnClickListener(showUserProfileonclickListener);
                }
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




		}

        private void setHeaderContent(final commentViewholder view,
                                      final showoffItems issue,final int position) {
            // TODO Auto-generated method stub

            view.userName.setText(issue.getFileOwner().getFullName());
            view.issue_title.setText(issue.getTitle());
            if(!TextUtils.isEmpty(issue.getTitle()) && !issue.getTitle().equalsIgnoreCase("null"))
            {
                view.issue_title.setText(issue.getTitle());
                view.issue_title.setVisibility(View.VISIBLE);
            }
            else
                view.issue_title.setVisibility(View.GONE);

            view.txt_content.setText(issue.getContent());

            view.timeShared.setText(issue.getDate_uploaded());


            String mediatype= issue.getFiletype();

            if(mediatype.equalsIgnoreCase("Image") || mediatype.equalsIgnoreCase("img"))
            {
                view.issue_image.setVisibility(View.VISIBLE);
                //view.issue_image_video.setVisibility(View.GONE);
                //Then set the image url of the image attached
                if (!TextUtils.isEmpty(issue.getFilename())
                        && !issue.getFilename().equalsIgnoreCase("null") ) {
                    Picasso.with(context)
                            .load(AppBackBoneClass.parentUrL+AppBackBoneClass.issueAttachmentFolder
                                    + issue.getFilename()).resize(250, 250)
                            .centerCrop().error(R.drawable.navigationbanner)
                            .placeholder(R.drawable.navigationbanner)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(view.issue_image);
                    view.issue_image.setVisibility(View.VISIBLE);
                } else {
                    view.issue_image.setVisibility(View.GONE);
                }
                videoplayerLayout.setVisibility(View.GONE);

            }
            else if(mediatype.equalsIgnoreCase("TEXT"))
            {
                //view.issue_image_video.setVisibility(View.GONE);
                view.issue_image.setVisibility(View.GONE);
                view.videoplayerLayout.setVisibility(View.GONE);
                view.issue_image.setVisibility(View.GONE);
            }
            else
            {
                videoplayerLayout.setVisibility(View.VISIBLE);
                issue_image.setVisibility(View.GONE);
                Log.e("VIPATH", VideoSource.videoBaseUrl+issue.getFilename());
                mVideoView.setVideoPath(VideoSource.videoBaseUrl+issue.getFilename());
                //Incase the filetype is Videos

            }





            IconDrawable draws = new IconDrawable(context, FontAwesomeIcons.fa_user)
                    .color(Color.GRAY).actionBarSize();
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(
                            issue.getFileOwner().getFullName().substring(0, 1),
                            Color.RED);
            if (!issue.getFileOwner().getImagePath().equalsIgnoreCase("null")) {
                AppBackBoneClass.loadUserImage(view.profileimage, AppBackBoneClass.parentUrL+AppBackBoneClass.usersimageURL
                        + "/" + issue.getFileOwner().getImagePath(), context, draws);
                // Toast.makeText(context, "lol",Toast.LENGTH_LONG).show();
            } else {
                view.profileimage.setImageDrawable(drawable);
            }

            view.btn_share.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    AppBackBoneClass.handlePostShare(issue, context);
                }
            });

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
                view.txt_likes.setText(issue.getLikes());
                view.txt_likes.setVisibility(View.VISIBLE);
            } else
                view.txt_likes.setVisibility(View.GONE);




            IconDrawable drawshare = new IconDrawable(context, FontAwesomeIcons.fa_ellipsis_v)
                    .color(Color.GRAY).actionBarSize();
            IconDrawable drawbookmark = new IconDrawable(context,
                    FontAwesomeIcons.fa_comment).color(Color.GRAY).actionBarSize();

            view.btn_share.setImageDrawable(drawshare);
            view.btn_bookmark.setImageDrawable(drawbookmark);
            view.btn_like.setImageDrawable(drawlike);

            view.btn_share.setImageDrawable(drawshare);
            view.btn_bookmark.setImageDrawable(drawbookmark);
            view.btn_like.setImageDrawable(drawlike);
            view.btn_bookmark.setVisibility(View.GONE);
            btn_share.setOnClickListener(new OnAlbumOverflowSelectedListener
                    (AppBackBoneClass.context, issue, position));


            // Now Handle PostLikes
            view.btn_like.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    String reason = lifeIssue.getUserLike().equalsIgnoreCase("1") ? "UnLike%20Post"
                            : "Like%20Post";
                    String url = AppBackBoneClass.parentUrL+AppBackBoneClass.feedUrl + "?reason=" + reason + "&PID="
                            + lifeIssue.getItemID() + "&UID="+ AppBackBoneClass.getUserId();

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
                                        Log.d("red", result);
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
                            lifeIssue.setUserLike("1").setLikes(count + "");
                            handleShowOffItemContentLikeChanged(lifeIssue);
                            //adapter.notifyItemChanged(position);
                        } else if (status.replace(" ", "").equalsIgnoreCase("Unliked")) {
                            // Unliked so restore button to gray
                            // drawlike = new IconDrawable(context, FontAwesomeIcons.fa_heart)
                            // .color(Color.GRAY).actionBarSize();
                            //btn_like.setImageDrawable(drawlike);

                            likes = Integer.parseInt(lifeIssue.getLikes());
                            if (likes <= 0)
                                likes = 1;
                            likes = likes - 1;
                            lifeIssue.setUserLike("0").setLikes(count + "");
                            //handleContentLikeChanged(lifeIssue);
                            handleShowOffItemContentLikeChanged(lifeIssue);
                            //adapter.notifyItemChanged(position);
                        }

                    }

                }

            });

        }


        void handleShowOffItemContentLikeChanged(final showoffItems issue)
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



            try {
                ApplicationFragments.showOffItemUpdated(issue,1); // update entry
               UserProfile.showOffItemUpdated(issue, 1); // update entry

            }
            catch (Exception e)
            {

            }

        }

        public void bind(@Nullable Object object) {
            if (object instanceof showOffComments ) {
                //Toast.makeText(contxt, "Errororr233", Toast.LENGTH_SHORT).show();
                try {
                    comment = (showOffComments) object;
                    setComment(comment);
                    position = comments.indexOf(comment);


                    onBindViewHolders(this);
                    //
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                if(object!=null && object instanceof showoffItems) {
                    try {
                        setHeaderContent(this, lifeIssue, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        public void onBindViewHolders(commentViewholder holder) {


                if(comment!=null)
                {
                    cuserName.setText(comment.getSender().getFullName() );
                    holder.ctxt_content.setText(comment.getComment());
                    holder.txt_timeago.setText(comment.getDate_sent());

                    //holder.ctxt_likes.setText("");



                  handleContentLikeChanged(comment);
                    draws = new IconDrawable(context, FontAwesomeIcons.fa_user).color(
                            Color.GRAY).actionBarSize();
                    TextDrawable drawable = TextDrawable.builder().buildRound(
                            comment.getSender().getFullName().substring(0, 1),
                            Color.RED);
                    if (!comment.getSender().getImagePath().equalsIgnoreCase("null")) {
                        AppBackBoneClass.loadUserImage(holder.cprofileimage,
                                AppBackBoneClass.parentUrL+AppBackBoneClass.usersimageURL + "/"
                                        + comment.getSender().getImagePath(),context,
                                draws);
                        // Toast.makeText(context, "lol",Toast.LENGTH_LONG).show();
                    } else {
                        holder.cprofileimage.setImageDrawable(drawable);
                    }

                    //txt_report
                    if(!AppBackBoneClass.getUserId().equalsIgnoreCase(comment.getSender().getUserID())) {
                        holder.txt_report.setVisibility(View.VISIBLE);
                        holder.txt_deletes.setVisibility(View.GONE);
                    }
                    else
                    {
                        holder.txt_report.setVisibility(View.GONE);
                        holder.txt_deletes.setVisibility(View.VISIBLE);
                    }

                    holder.setComment(comment);
                }
            else
                {
                    Log.e("NOT FOUND","jksajksajsjjkasjsajhasjkhkjhsdjkasdh");
                }


        }

        @Override
        public void onViewHolderBound() {
            if(mThumbnail!=null) {
                Picasso.with(itemView.getContext())
                        .load(R.drawable.navigationbanner)
                        .fit()
                        .centerInside()
                        .into(mThumbnail
                        );
            }
        }

        @Override
        public void onVideoPrepared(MediaPlayer mp) {
            mPlayable = true;
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
            return super.visibleAreaOffset() >= 0.8;
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











        @Override
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
			// TODO Auto-generated method stub
		
//	            menu.setHeaderTitle("Select The Action");
//	            menu.add(0, R.id.call, 0, "Call");//groupId, itemId, order, title
//	            menu.add(0, R.id.msg, 0, "SMS");
			
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
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
             ApplicationFragments.showOffItemUpdated(issue,2);
            ((Activity)context).finish();
        }
    }



}
