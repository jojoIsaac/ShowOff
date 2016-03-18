package applications.apps.celsoft.com.showoff.Views;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import applications.apps.celsoft.com.showoff.EditAboutUser;
import applications.apps.celsoft.com.showoff.LaunchPage;
import applications.apps.celsoft.com.showoff.R;
import applications.apps.celsoft.com.showoff.UserPreference_activity;
import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by User on 2/25/2016.
 */
public class ApplicationSettings  extends Fragment implements View.OnClickListener {

    private View rootView;

        @Bind(R.id.layoutaccount)
        LinearLayout layoutaccount;
        @Bind(R.id.layout_notification)
        LinearLayout layout_notification;
        @Bind(R.id.layoutfacebook)
        LinearLayout layoutfacebook;
        @Bind(R.id.layout_favorites)
        LinearLayout layout_favorites;
        @Bind(R.id.layoutinsta)
        LinearLayout layoutinsta;
        @Bind(R.id.layouttwitter)
        LinearLayout layouttwitter;
        @Bind(R.id.layout_FAQs)
        LinearLayout layout_FAQs;
        @Bind(R.id.layout_terms_policy)
        LinearLayout layout_terms_policy;
        @Bind(R.id.layout_about)
        LinearLayout layout_about;
    @Bind(R.id.btnlogout)
    Button btnlogout;
    String AppName_fontPathfacefear = "fonts/Face Your Fears.ttf";
    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_settings,null);
        ButterKnife.bind(this,rootView);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), AppName_fontPathfacefear);

        layoutaccount.setOnClickListener(this);
        btnlogout.setTypeface(tf);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage("Log out?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppBackBoneClass.setisLoggedIn(getActivity(), false);
                        Intent intent = new Intent(getActivity(), LaunchPage.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();


            }
        });

        layout_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserPreference_activity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return rootView;
    }

    @Override
    public void onClick(View v) {
        Intent settingIntent = null;
        switch (v.getId()) {
            case R.id.layoutaccount:
                EditAboutUser.accountState ="edit";
                settingIntent = new Intent(getActivity(), EditAboutUser.class);

                break;
            case R.id.layout_notification:
                break;
            case R.id.layoutfacebook:
                break;
            case R.id.layout_favorites:
                break;
            case R.id.layoutinsta:
                break;
            case R.id.layouttwitter:
                break;
            case R.id.layout_terms_policy:
                break;
            case R.id.layout_FAQs:
                break;
            case R.id.layout_about:
                break;
        }

        if(settingIntent!=null)
        {
            startActivity(settingIntent);
        }
    }
}
