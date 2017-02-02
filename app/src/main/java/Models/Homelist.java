package Models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.Jsonkey;

/**
 * Created by Creadigol on 7/26/2016.
 */
public class Homelist extends BaseObject {
    String emailverfy;
    String Slider;

    public String getSlider() {
        return Slider;
    }

    public void setSlider(String slider) {
        Slider = slider;
    }

    ArrayList<HomeObject> homeObjects;
int notificationcount;
    public String getEmailverfy() {
        return emailverfy;
    }

    public void setEmailverfy(String emailverfy) {
        this.emailverfy = emailverfy;
    }

    public int getNotificationcount() {
        return notificationcount;
    }

    public void setNotificationcount(int notificationcount) {
        this.notificationcount = notificationcount;
    }

    public ArrayList<HomeObject> getHomelist() {
        return homeObjects;
    }

    public void setHomeObjects(JSONArray jsonLocationArray) {
        homeObjects = new ArrayList<>();
        if (jsonLocationArray != null) {
            for (int i = 0; i < jsonLocationArray.length(); i++) {
                HomeObject homeObjectlist= new HomeObject();
                try {
                    JSONObject jsonObjectOutlet = jsonLocationArray.getJSONObject(i);

                    homeObjectlist.setCityid(jsonObjectOutlet.optString(Jsonkey.homecityid));
                    homeObjectlist.setCityname(jsonObjectOutlet.optString(Jsonkey.homecityname));
                    homeObjectlist.setCityimage(jsonObjectOutlet.optString(Jsonkey.homecityimage));

                    homeObjects.add(homeObjectlist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
