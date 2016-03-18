package applications.apps.celsoft.com.showoff.Utilities.table_interfaces;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 1/31/2016.
 */
public class AppUser {
    private String email_address;
    private String user_name;
    private String location;
    private String deviceId;
    private String ImagePath;
    private String fullName;
    private String userJson;
    private String mutualFriends;
    private String connected;
    private String about_user;

    public String getUserCover() {
        return userCover;
    }

    public AppUser setUserCover(String userCover) {
        this.userCover = userCover;
        return this;
    }

    private String userCover;

    public String getPhone() {
        return phone;
    }

    public AppUser setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    private String phone;

    private String connections;

    public String getUserID() {
        return UserID;
    }

    public String getConnections() {
        return connections;
    }

    public AppUser setConnections(String connections) {
        this.connections = connections;
        return this;
    }

    public AppUser setUserID(String userID) {
        UserID = userID;
        return this;

    }

    private String UserID;

    public String getAbout_user() {
        return about_user;
    }

    public AppUser setAbout_user(String about_user) {
        this.about_user = about_user;
        return this;
    }

    public AppUser setEmail_address(String email_address) {
        this.email_address = email_address;
        return this;
    }

    public AppUser setUser_name(String user_name) {
        this.user_name = user_name;
        return this;
    }

    public AppUser setLocation(String location) {
        this.location = location;
        return this;
    }

    public AppUser setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public AppUser setImagePath(String imagePath) {
        ImagePath = imagePath;
        return this;
    }

    public AppUser setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserJson() {
        return userJson;
    }

    public String getMutualFriends() {
        return mutualFriends;
    }

    public String getConnected() {
        return connected;
    }

    public AppUser setUserJson(String userJson) {
        this.userJson = userJson;
        return this;
    }

    public AppUser setMutualFriends(String mutualFriends) {
        this.mutualFriends = mutualFriends;
        return this;
    }

    public AppUser setConnected(String connected) {
        this.connected = connected;
        return this;
    }

    @Override

    public String toString() {
        return "ShowUser{" +
                "email_address='" + email_address + '\'' +
                ", user_name='" + user_name + '\'' +
                ", location='" + location + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", ImagePath='" + ImagePath + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUser)) return false;

        AppUser appUser = (AppUser) o;

        if (!getEmail_address().equals(appUser.getEmail_address())) return false;
        if (!getUser_name().equals(appUser.getUser_name())) return false;
        if (!getLocation().equals(appUser.getLocation())) return false;
        if (!getDeviceId().equals(appUser.getDeviceId())) return false;
        return getImagePath().equals(appUser.getImagePath());

    }

    @Override
    public int hashCode() {
        int result = getEmail_address().hashCode();
        result = 31 * result + getUser_name().hashCode();
        result = 31 * result + getLocation().hashCode();
        result = 31 * result + getDeviceId().hashCode();
        result = 31 * result + getImagePath().hashCode();
        return result;
    }

    public AppUser(String email_address, String user_name, String location) {
        this.email_address = email_address;
        this.user_name = user_name;
        this.location = location;
    }

    public AppUser()
    {

    }



    public String getUser_name() {
        return user_name;
    }

    public String getLocation() {
        return location;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public String getEmail_address() {
        return email_address;
    }

    public String getDeviceId() {
        return deviceId;
    }


    /**
     * This function returns a single object from the showoffData.
     * it takes as argument a JsonObject representing the Json retrieved from the Json request
     * @param objectJson
     * @return  showoffItems
     */
    public static AppUser getItem(JSONObject objectJson)
    {
        AppUser showOffUser = new AppUser();

        return showOffUser;
    }

    /**
     * This function returns a single object from the showoffData.
     * it takes as argument a string representing the information retrieved from the server
     * @param objectString
     * @return  showoffItems
     */
    public static AppUser getItem(String objectString)
    {
        AppUser showOffUser = new AppUser();

        return showOffUser;
    }


    public static AppUser processUserJson(String sender) {

        AppUser showOffUser = new AppUser();


        if(!TextUtils.isEmpty(sender))
        {
            try {
                JSONObject object= new JSONObject(sender);
                /*
                ":{"id":"2","memberID":"2","cover":"","name":"Getru Essel","avatar":"null","email":"esselMatg@gmail.com","phone":"01046770820",
                "about-user":"sjsjkajasjsajkasjkasjkashjkasdhjasdh","deviceID":"","date_joined":"9 min ago","location":0,"connection":0,"isConnected":0,"mutualFriends":0},"
                 */

                if(object!=null)
                {
                    showOffUser.setUserID(object.optString("id"))
                            .setImagePath(object.optString("avatar"))
                            .setDeviceId(object.optString("deviceID"))
                            .setEmail_address(object.optString("email"))
                            .setFullName(object.optString("name"))
                            .setAbout_user(object.optString("about-user"))
                            .setUser_name(object.optString("user_name"))
                            .setUserJson(sender)
                            .setPhone(object.optString("phone"))
                            .setConnected(object.optString("isConnected"))
                            .setMutualFriends(object.optString("mutualFriends"))
                            .setConnections(object.optString("connection"))
                            .setUserCover(object.optString("userCover",""))
                            .setLocation(object.optString("location"))
                            ;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return showOffUser;
    }
}
