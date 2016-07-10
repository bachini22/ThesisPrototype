package data;

import android.net.Uri;

import java.io.File;

/**
 * Created by dorothylu on 7/3/16.
 */
public class FeedItem {
    private String id;
    private String fName,lName, status, timeStamp, category;
    private Uri image;
    public FeedItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String name) {
        this.lName = name;
    }

    public String getFName(){ return fName; }

    public void setFName(String fName){ this.fName = fName;}

    public Uri getImge() {
        return image;
    }

    public void setImge(Uri image) {
        this.image = image;
    }

    public String getMessage() {
        return status;
    }

    public void setMessage(String status) {
        this.status = status;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String c) {
        this.category = c;
    }
}