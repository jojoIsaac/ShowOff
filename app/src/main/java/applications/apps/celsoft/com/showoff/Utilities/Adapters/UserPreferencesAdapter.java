package applications.apps.celsoft.com.showoff.Utilities.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import applications.apps.celsoft.com.showoff.R;
import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.UserPreferences;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.showOffComments;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.showoffItems;
import butterknife.Bind;
import butterknife.ButterKnife;
import im.ene.lab.toro.ToroAdapter;
import im.ene.lab.toro.ToroViewHolder;

/**
 * Created by User on 3/12/2016.
 */
public class UserPreferencesAdapter extends ToroAdapter<UserPreferencesAdapter.preferenceViewholder> {

    private Context context;
    private List<UserPreferences> preferencesList;
    //UserPreferences preference;
    private IconDrawable draws;
    private static final int TYPE_HEADER = 0; // Declaring Variable to
    // Understand which View is
    // being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;
    private preferenceViewholder vhItem;

    public UserPreferencesAdapter(List<UserPreferences> preferencesList, Context context) {
        this.preferencesList = preferencesList;
        this.context = context;
    }


    @Nullable
    @Override
    protected Object getItem(int position) {
        return preferencesList.get(position);
    }

    @Override
    public preferenceViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.user_preferences_layout, parent, false);
        // Inflating
        // the
        // layout

        vhItem = new  preferenceViewholder(v, viewType, context); // Creating
        // ViewHolder
        // and passing
        // the object of
        // type view

        return vhItem;
    }

    @Override
    public int getItemCount() {
        return preferencesList.size();
    }


    @Override
    public int getItemViewType(int position) {


        return TYPE_ITEM;
    }

    class preferenceViewholder extends ToroViewHolder implements
            MediaPlayer.OnCompletionListener, View.OnClickListener, View.OnCreateContextMenuListener {
        @Bind(R.id.btn_like)
        ImageButton btn_like;
        @Bind(R.id.cate_image)
        ImageView cate_image;
        @Bind(R.id.btn_dislike)
        ImageButton btn_dislike;
        @Bind(R.id.summary)
        TextView summary;
        @Bind(R.id.txt_title)
        TextView txt_title;


        UserPreferences preference;
        private IconDrawable drawlike,drawDislike;

        public preferenceViewholder(View itemView) {
            super(itemView);
        }

        public preferenceViewholder(View view, int viewType, Context context) {
            super(view);
            ButterKnife.bind(this, view);
            btn_like .setOnClickListener( this);
            btn_dislike.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId())
            {
                case R.id.btn_like:
                    saveUserLikePreference("like");
                    break;
                case R.id.btn_dislike:
                    saveUserLikePreference("dislike");
                    break;
            }


        }

        private void saveUserLikePreference(String status) {

            Ion.with(context)
                    .load(AppBackBoneClass.parentUrL + AppBackBoneClass.feedUrl
                    )
                    .setBodyParameter("reason", "Toggle Preferences")
                    .setBodyParameter("currentStatus", (status.equalsIgnoreCase("like"))?"1":"2")
                    .setBodyParameter("prefID",preference.getCategory_id())

                    .setBodyParameter("userID", AppBackBoneClass.getUserId())
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            if(e!=null)
                            {
                                e.printStackTrace();
                            }
                            else {

                                if (result!=null&&!result.trim().equalsIgnoreCase("Error") || !result.trim().equalsIgnoreCase("Err")) {
                                    handleResultsChange(result);
                                }
                            }
                            //}
                        }
                    });

        }

        private void handleResultsChange(String result) {
//Toggle Preferences

            JSONObject object = null;
            try {
                object = new JSONObject(result);
                String status= object.getString("status");
                String Fstatus= object.getString("Fstatus");

                if(status.equalsIgnoreCase("Success"))
                {
                    preference.setUserlikePreference(Fstatus);
                    toggleUserLikePreference(Fstatus);
                }
                else
                {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }

        @Override
        public boolean wantsToPlay() {
            return false;
        }

        @Override
        public boolean isAbleToPlay() {
            return false;
        }

        @Nullable
        @Override
        public String getVideoId() {
            return null;
        }

        @NonNull
        @Override
        public View getVideoView() {
            return null;
        }

        @Override
        public void start() {

        }

        @Override
        public void pause() {

        }

        @Override
        public int getDuration() {
            return 0;
        }

        @Override
        public int getCurrentPosition() {
            return 0;
        }

        @Override
        public void seekTo(int pos) {

        }

        @Override
        public boolean isPlaying() {
            return false;
        }

        @Override
        public void bind(@Nullable Object object) {
              if (object instanceof UserPreferences)
              {
                  preference = (UserPreferences) object;
                  toggleUserLikePreference(preference.getUserlikePreference());
                  txt_title.setText(preference.getTitle());
                  if (!TextUtils.isEmpty(preference.getSummary()))
                       summary.setText(preference.getSummary());
                  else
                      summary.setVisibility(View.GONE);


              }
        }

        private void toggleUserLikePreference(String userlikePreference) {
            Integer likeState= Integer.parseInt(userlikePreference);


           int activeColor= context.getResources().getColor(R.color.ColorPrimary);
           int neutralColor= context.getResources().getColor(R.color.colorPrimaryDark);

            int likecolor = 0,dislike = 0;
            switch (likeState)
            {
                case 1:
                      likecolor = activeColor;
                    dislike = neutralColor;
                    break;
                case 2:
                      likecolor = neutralColor;
                      dislike = activeColor;
                    break;
                case 0:
                      likecolor= activeColor;
                      dislike= neutralColor;
                    break;

            }
            drawlike = new IconDrawable(context, FontAwesomeIcons.fa_thumbs_up).color(
                    likecolor).actionBarSize();
            drawDislike = new IconDrawable(context, FontAwesomeIcons.fa_thumbs_down).color(
            dislike).actionBarSize();


            btn_dislike.setImageDrawable(drawDislike);
            btn_like.setImageDrawable(drawlike);
        }
    }

}
