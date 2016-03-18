package applications.apps.celsoft.com.showoff.Utilities;

/**
 * Created by User on 1/30/2016.
 * This class contains the Constants used within the application such as
 * 1. The server URL
 * 2. the Base url for some activities
 */
public class ApplicationContants {
    public static boolean devMode=false;
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    //public static  String parentUrL="http://celsoft.netai.net/ShowOff/";  //The base url for almost every single Urls used with this application
    public static String parentUrL="https://nsoredevotional.com/showOffApp/showOffApp/";
    public  static String feedUrl="modules/operator.php";
    public  static String gcmServerRegister="modules/register.php";
    public static String uploadUserProfile="modules/uploaduser_profilepix.php";
    public static  String uploadUrl="modules/uploadshow.php",videoUploader="modules/video_uploader.php";
    public static  String appParentFolder="showOff";
    public static String usersimageURL="appImages/usersAvatar/"; //points to the users Image on the server
    public static String issueAttachmentFolder="membersfiles/"; //Points to the folder on the server housing all files shared
    public static String prefernceName = "SHOWOFF_USER_DATA_STATS";// The Name of the
    public static String pref_Phone = "phone";
    public static String pref_Email = "email";
    public static String pref_UserName = "UName";
    public static String pref_UserCover = "USER_COVER";
    public static String pref_UserJson = "USER_JSON";
    public static final String pref_Name = "Name";
    public static final String IS_LOGGED_IN = "IS_LOGGED_IN",
            REG_TIME = "REG_TIME";
    public static final String USERDP = "user_dp",pref_USER_ID="user_id";
    public final static String faceAccountUsedAlready = "FacebookAlready",SPLASH_DONE="splash_is_done";

    //Unique code to identify this application
    public static String ApplicationCode="";
    public static int NEW_ITEM_CODE=901;
    public static String EXTRA_MESSAGE_TYPE="Type";
    public static String EXTRA_MESSAGE="message";


    public static final String DISPLAY_MESSAGE_ACTION =
            "com.google.android.c2dm.intent.RECEIVE";
    //public static String EXTRA_MESSAGE="message";

    //public static  String

}
