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
public class Review_object_item extends BaseObject {
    ArrayList<Review_items> reviewItemses;

    public ArrayList<Review_items> getreviewlist() {
        return reviewItemses;
    }

    public void setReview(JSONArray jsonLocationArray) {
        reviewItemses = new ArrayList<>();
        if (jsonLocationArray != null) {
            for (int i = 0; i < jsonLocationArray.length(); i++) {
                Review_items review_items= new Review_items();
                try {
                    JSONObject jsonObjectOutlet = jsonLocationArray.getJSONObject(i);

                    review_items.setSeminar_id(jsonObjectOutlet.optString(Jsonkey.seminarid));
                    review_items.setSeminar_name(jsonObjectOutlet.optString(Jsonkey.seminar_name));
                    review_items.setUser_name(jsonObjectOutlet.optString(Jsonkey.user_name));
                    review_items.setReview(jsonObjectOutlet.optString(Jsonkey.Review));
                    review_items.setSeminar_pic(jsonObjectOutlet.optString(Jsonkey.seminar_Image));
                    review_items.setType(jsonObjectOutlet.optString(Jsonkey.search_type));
                    review_items.setReview_id(jsonObjectOutlet.optString(Jsonkey.Review_id));
                    review_items.setUser_id(jsonObjectOutlet.optString(Jsonkey.user_id));

                    Log.e("seminar_name",""+review_items.getSeminar_name());
                    reviewItemses.add(review_items);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
