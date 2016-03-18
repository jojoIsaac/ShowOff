package applications.apps.celsoft.com.showoff.Utilities.table_interfaces;

/**
 * Created by User on 1/31/2016.
 */
public class contentLikes {
    private String like_id ="";
    private String sender;
    private String likeValue="0";
    private String itemID;

    public String getLike_id() {
        return like_id;
    }

    public void setLike_id(String like_id) {
        this.like_id = like_id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getLikeValue() {
        return likeValue;
    }

    public void setLikeValue(String likeValue) {
        this.likeValue = likeValue;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }
}
