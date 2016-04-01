package applications.apps.celsoft.com.showoff.Utilities.table_interfaces;

import org.json.JSONException;
import org.json.JSONObject;

import applications.apps.celsoft.com.showoff.Utilities.models.SimpleVideoObject;

/**
 * Created by User on 1/31/2016.
 * This class represent the table housing the videos,images, or any item used
 */
public class showoffItems  {
    private String  filename;
    private String  filetype;
    private String  date_uploaded;
    private String  average_rating;
    private AppUser fileOwner;
    private String itemID;
    private String title;
    private String likes;
    private String userLike;
    private String issueJson;
    private String topicID;
    private String Content;
    private JSONObject showItemJson;

    public JSONObject getShowItemJson() {
        return showItemJson;
    }

    public showoffItems setShowItemJson(JSONObject showItemJson) {
        this.showItemJson = showItemJson;
        return this;
    }

    public static enum videoplayState {PLAYING,PAUSED,ENDED,NA,STOPPED};
    private videoplayState playState;

    public videoplayState getPlayState() {
        return playState;
    }

    public showoffItems setPlayState(videoplayState playState) {
        this.playState = playState;
        return this;
    }

    public String getContent() {
        return Content;
    }

    public showoffItems setContent(String content) {
        Content = content;
        return this;
    }

    public String getTopicID() {
        return topicID;
    }

    public showoffItems setTopicID(String topicID) {
        this.topicID = topicID;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public showoffItems setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getLikes() {
        return likes;
    }

    public showoffItems setLikes(String likes) {
        this.likes = likes;
        return this;
    }

    public String getUserLike() {
        return userLike;
    }

    public showoffItems setUserLike(String userLike) {
        this.userLike = userLike;
        return this;
    }

    public String getIssueJson() {
        return getShowItemJson().toString();
    }

    public showoffItems setIssueJson(String issueJson) {
        this.issueJson = issueJson;
        return this;
    }

    public String getItemID() {
        return itemID;
    }

    public showoffItems setItemID(String itemID) {
        this.itemID = itemID;
        return this;
    }

    public showoffItems() {
        playState= videoplayState.NA;
    }

    public showoffItems(String filename, AppUser fileOwner, String filetype) {
        this.filename = filename;
        this.fileOwner = fileOwner;
        this.filetype = filetype;
        playState= videoplayState.NA;
    }

    //Set the data fields

    public showoffItems setAverage_rating(String average_rating) {
        this.average_rating = average_rating;

        return this;
    }

    public showoffItems setDate_uploaded(String date_uploaded) {
        this.date_uploaded = date_uploaded;
        return this;
    }

    public showoffItems setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public showoffItems setFileOwner(AppUser fileOwner) {
        this.fileOwner = fileOwner;
        return this;
    }

    public showoffItems setFiletype(String filetype) {
        this.filetype = filetype;
        return  this;
    }

    public String getAverage_rating() {
        return average_rating;
    }


    public String getDate_uploaded() {
        return date_uploaded;
    }

    public String getFilename() {
        return filename;
    }


    public AppUser getFileOwner() {
        return fileOwner;
    }

    public String getFiletype() {
        return filetype;
    }




    /**
     * This function returns a single object from the showoffData.
     * it takes as argument a JsonObject representing the Json retrieved from the Json request
     * @param objectJson
     * @return  showoffItems
     */
    public static showoffItems getItem(JSONObject objectJson)
    {
        showoffItems showOffdata = new showoffItems();

        return showOffdata;
    }

    /**
     * This function returns a single object from the showoffData.
     * it takes as argument a string representing the information retrieved from the server
     * @param objectString
     * @return  showoffItems
     */
    public static showoffItems getItem(String objectString)
    {
        showoffItems showOffdata = new showoffItems();
          try
          {
              JSONObject feedObj =  new JSONObject(objectString);
              if(feedObj!=null) {
              String sender= feedObj.optString("sender");
              showoffItems issues= new showoffItems();
              AppUser user= new AppUser();
              user= AppUser.processUserJson(sender);

                  if(user==null)
                  {
                      return null;
                  }
                  else {
                      issues.setFileOwner(user);


                      issues
                              .setShowItemJson(feedObj)
                              .setItemID(feedObj.getString("id"))
                              .setTitle(feedObj.getString("title"))
                              .setFiletype(feedObj.getString("file_type"))
                              .setContent(feedObj.getString("content"))

                              .setDate_uploaded(feedObj.getString("datesent"))
                              .setFilename(feedObj.getString("file_uploaded"))
                              .setTopicID(feedObj.getString("data_cate"))
                              .setLikes(feedObj.getString("likes"))
                              .setUserLike(feedObj.getString("user_like"))


                      ;


                      return issues;
                  }
              }
          }
          catch (Exception e)
          {
e.printStackTrace();
          }
        return showOffdata;
    }


















    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        showoffItems that = (showoffItems) o;

        if (!filename.equals(that.filename)) return false;
        return filename.equals(that.filename);
    }

    @Override public int hashCode() {
        int result = filename.hashCode();
        result = 31 * result + filename.hashCode();
        return result;
    }

    @Override public String toString() {
        return "SimpleVideoObject{" +
                "name='" + filename + '\'' +
                ", video='" + filename + '\'' +
                '}';
    }
}
