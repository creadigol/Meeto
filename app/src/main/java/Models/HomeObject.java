package Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.Jsonkey;

/**
 * Created by Vj on 7/18/2016.
 */

public class HomeObject extends BaseObject {

    private String cityid,cityname,cityimage,emailvarify;

    public String getEmailvarify() {
        return emailvarify;
    }

    public void setEmailvarify(String emailvarify) {
        this.emailvarify = emailvarify;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getCityimage() {
        return cityimage;
    }

    public void setCityimage(String cityimage) {
        this.cityimage = cityimage;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public int getNotificationcount() {
        return notificationcount;
    }

    public void setNotificationcount(int notificationcount) {
        this.notificationcount = notificationcount;
    }

    private int notificationcount;

}
