package applications.apps.celsoft.com.showoff.Utilities.table_interfaces;

import org.json.JSONObject;

/**
 * Created by User on 1/31/2016.
 * <p> This class models the comments table. </p>
 */
public class showOffComments  {
    private String commentID="";
    private AppUser sender;
    private String content;
    private String itemID;
    private String date_sent;
    private String comment;
    private Integer commentLevel = 0; // This is to indicate the level of the comment ie. if its a comment on another comment etc
    private String parentComment = ""; // Points to the Id of the parent comment.
    private String attachment="";
    private String commentLike;
    private String userLikeItem="0";
    private String commentJson="";

    public String getCommentJson() {
        return commentJson;
    }

    public showOffComments setCommentJson(String commentJson) {
        this.commentJson = commentJson;
        return this;
    }

    public String getUserLikeItem() {
        return userLikeItem;
    }

    public showOffComments setUserLikeItem(String userLikeItem) {
        this.userLikeItem = userLikeItem;
        return this;
    }

    public showOffComments setCommentID(String commentID) {
        this.commentID = commentID;
        return this;
    }

    public String getCommentLike() {
        return commentLike;
    }

    public showOffComments setCommentLike(String commentLike) {
        this.commentLike = commentLike;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public showOffComments setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getAttachment() {
        return attachment;
    }


    public String getParentComment() {
        return parentComment;
    }

    public String getCommentID() {
        return commentID;
    }



    public AppUser getSender() {
        return sender;
    }

    public void setSender(AppUser sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }


    public String getItemID() {
        return itemID;
    }


    public String getDate_sent() {
        return date_sent;
    }

    public showOffComments setContent(String content) {
        this.content = content;
        return this;
    }

    public showOffComments setItemID(String itemID) {
        this.itemID = itemID;
        return this;
    }

    public showOffComments setDate_sent(String date_sent) {
        this.date_sent = date_sent;
        return this;
    }

    public showOffComments setCommentLevel(Integer commentLevel) {
        this.commentLevel = commentLevel;
        return this;
    }

    public showOffComments setParentComment(String parentComment) {
        this.parentComment = parentComment;
        return this;
    }

    public showOffComments setAttachment(String attachment) {
        this.attachment = attachment;
        return this;
    }

    public Integer getCommentLevel() {
        return commentLevel;
    }


    public showOffComments(AppUser sender, String content, String itemID, Integer commentLevel, String parentComment) {
        this.sender = sender;
        this.content = content;
        this.itemID = itemID;
        this.commentLevel = commentLevel;
        this.parentComment = parentComment;
    }


    public showOffComments() {

    }

    public static showOffComments processIssuesCommentJson(String result) {

        try
        {
            JSONObject feedObj =  new JSONObject(result);
            if(feedObj!=null) {
                String sender= feedObj.optString("user");
                showOffComments comment = new showOffComments();
                AppUser user= AppUser.processUserJson(sender);

                if(user==null)
                {
                    return null;
                }
                else {

                     /*
        {"member_id":"100","comment_id":"1","user":{"id":"100","memberID":"100",
        "cover":"","name":"XoODO","avatar":"null","email":"admin@gmail.com","phone":"",
        "about-user":"","deviceID":"","date_joined":"2 weeks ago","location":0,"connection":0,"isConnected":-10,
        "mutualFriends":0},"date_sent":"23 min ago",
        "comments":"Hahahaha. Well done bro","group_post_id":"21","commentlikes":"1","user_like":"1"}
         */
                    comment.setSender(user);
                    comment.setCommentID(feedObj.getString("comment_id"))
                            .setCommentLike(feedObj.getString("commentlikes"))
                            .setComment(feedObj.getString("comments"))
                            .setDate_sent(feedObj.getString("date_sent"))
                            .setItemID(feedObj.getString("group_post_id"))
                            .setCommentJson(feedObj.toString())
                            .setUserLikeItem(feedObj.getString("user_like"));
                    return comment;
                }
            }
        }
        catch (Exception e)
        {

        }

        return null;
    }
}
