package applications.apps.celsoft.com.showoff.Utilities.Adapters;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.squareup.picasso.Picasso;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import applications.apps.celsoft.com.showoff.R;
import applications.apps.celsoft.com.showoff.Utilities.models.SimpleVideoObject;
import applications.apps.celsoft.com.showoff.Utilities.models.VideoSource;
import applications.apps.celsoft.com.showoff.Utilities.table_interfaces.showoffItems;
import im.ene.lab.toro.ToroAdapter;
import im.ene.lab.toro.ToroViewHolder;
import im.ene.lab.toro.widget.ToroVideoView;

/**
 * Created by User on 2/14/2016.
 */
public class ShowOffAdapter extends ToroAdapter<ToroAdapter.ViewHolder> {

    public static final int VIEW_TYPE_NO_VIDEO = 1;

    public static final int VIEW_TYPE_VIDEO = 1 << 1;

    public static final int VIEW_TYPE_VIDEO_MIXED = 1 << 2;

    Context context;
    List<showoffItems> issuesList;
    Integer issueType;
    private IconDrawable draws;
    Activity activity;


    @Nullable
    @Override
    protected Object getItem(int position) {
        return null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @IntDef({
            VIEW_TYPE_NO_VIDEO, VIEW_TYPE_VIDEO, VIEW_TYPE_VIDEO_MIXED
    }) @Retention(RetentionPolicy.SOURCE) public @interface Type {
    }









}
