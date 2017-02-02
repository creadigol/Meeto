package Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.Jsonkey;

/**
 * Created by Creadigol on 7/26/2016.
 */
public class YourBookingObject extends BaseObject {
    ArrayList<YourBookinglist_item> yourBookinglist;

    public ArrayList<YourBookinglist_item> getHomelist() {
        return yourBookinglist;
    }

    public void setHomeObjects(JSONArray jsonLocationArray) {
        yourBookinglist = new ArrayList<>();
        if (jsonLocationArray != null) {
            for (int i = 0; i < jsonLocationArray.length(); i++) {
                YourBookinglist_item bookiglist= new YourBookinglist_item();
                try {
                    JSONObject jsonObjectOutlet = jsonLocationArray.getJSONObject(i);

                    bookiglist.setBooking_id(jsonObjectOutlet.optString(Jsonkey.booking_id));
                    bookiglist.setSeminar_id(jsonObjectOutlet.optString(Jsonkey.seminarid));
                    bookiglist.setSeminar_name(jsonObjectOutlet.optString(Jsonkey.title));
                    bookiglist.setDate(jsonObjectOutlet.optString(Jsonkey.from_date));
                    bookiglist.setHost_name(jsonObjectOutlet.optString(Jsonkey.host_name));
                    bookiglist.setHost_approval(jsonObjectOutlet.optString(Jsonkey.status));
                    bookiglist.setBooking_pic(jsonObjectOutlet.optString(Jsonkey.listimage));
                    bookiglist.setBookingdate(jsonObjectOutlet.optString(Jsonkey.Bookedon));
                    bookiglist.setBook_seat(jsonObjectOutlet.optString(Jsonkey.book_seat));
                    bookiglist.setFromdate(jsonObjectOutlet.optString(Jsonkey.from_date));
                    bookiglist.setTodate(jsonObjectOutlet.optString(Jsonkey.todates));
                    bookiglist.setBooking_no(jsonObjectOutlet.optString(Jsonkey.booking_no));



                    yourBookinglist.add(bookiglist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
