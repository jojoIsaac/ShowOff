package applications.apps.celsoft.com.showoff.Utilities.table_interfaces;

import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by User on 3/12/2016.
 */
public class UserPreferences {
//`cat_id`,`title``category_img`
    private String category_id;
    private String title;
    private String categoryImg;
    private String summary;

    public String getUserlikePreference() {
        return userlikePreference;
    }

    public UserPreferences setUserlikePreference(String userlikePreference) {
        this.userlikePreference = userlikePreference;
        return this;
    }

    private String userlikePreference;

    public String getCategory_id() {
        return category_id;
    }

    public UserPreferences setCategory_id(String category_id) {
        this.category_id = category_id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public UserPreferences setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getCategoryImg() {
        return categoryImg;
    }

    public UserPreferences setCategoryImg(String categoryImg) {
        this.categoryImg = categoryImg;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public UserPreferences setSummary(String summary) {
        this.summary = summary;
        return this;
    }


    public static List<UserPreferences> getUserPreferencesList(String result, List<UserPreferences> preferencesList) throws JSONException {
        JSONArray jarray = new JSONArray(result);
        UserPreferences preferences= null;
        List<UserPreferences> userPreferencesList = null;
        if(jarray!=null)
        {
            for (int i=0;i<jarray.length();i++)
            {
                JSONObject jsonObject = jarray.getJSONObject(i);
                preferences= new UserPreferences();
                preferences.setCategory_id(jsonObject.getString("id"))
                        .setUserlikePreference(jsonObject.getString("userlike"))
                        .setTitle(jsonObject.getString("title"))
                        .setSummary(jsonObject.getString("summary"))
                        .setCategoryImg(jsonObject.getString("banner"));
if(preferencesList.indexOf(preferences)<0)
                preferencesList.add(preferences);


            }


        }

        return preferencesList;


    }
}
