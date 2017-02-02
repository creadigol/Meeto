package Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.Jsonkey;

/**
 * Created by Creadigol on 7/26/2016.
 */
public class SeminarBooklistObject extends BaseObject {
    ArrayList<SeminarBookListitem> seminarbook;

    public ArrayList<SeminarBookListitem> getHomelist() {
        return seminarbook;
    }

    public void setHomeObjects(JSONArray jsonLocationArray) {
        seminarbook = new ArrayList<>();
        if (jsonLocationArray != null) {
            for (int i = 0; i < jsonLocationArray.length(); i++) {
                SeminarBookListitem list= new SeminarBookListitem();
                try {
                    JSONObject jsonObjectOutlet = jsonLocationArray.getJSONObject(i);

                    list.setSeminar_boooking_no(jsonObjectOutlet.optString(Jsonkey.seminar_booking_no));
                    list.setSeminar_id(jsonObjectOutlet.optString(Jsonkey.seminarid));
                    list.setUser_name(jsonObjectOutlet.optString(Jsonkey.username));
                    list.setUser_id(jsonObjectOutlet.optString(Jsonkey.user_id));
                    list.setSeminar_name(jsonObjectOutlet.optString(Jsonkey.seminar_name));
                    list.setBooking_date(jsonObjectOutlet.optString(Jsonkey.BOOKINGDATE));
                    list.setEmail(jsonObjectOutlet.optString(Jsonkey.email));
                    list.setTotal_seat(jsonObjectOutlet.optString(Jsonkey.book_seat));
                    list.setUser_image(jsonObjectOutlet.optString(Jsonkey.user_pic));
                    list.setStatus(jsonObjectOutlet.optString(Jsonkey.status));

                    seminarbook.add(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
