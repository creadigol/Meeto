package Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.Jsonkey;

/**
 * Created by Creadigol on 7/26/2016.
 */
public class WishlistObject extends BaseObject {
    ArrayList<Wishlist_item> wishlist_items;

    public ArrayList<Wishlist_item> getHomelist() {
        return wishlist_items;
    }

    public void setHomeObjects(JSONArray jsonLocationArray) {
        wishlist_items = new ArrayList<>();
        if (jsonLocationArray != null) {
            for (int i = 0; i < jsonLocationArray.length(); i++) {
                Wishlist_item list = new Wishlist_item();
                try {
                    JSONObject jsonObjectOutlet = jsonLocationArray.getJSONObject(i);

                    list.setSeminar_id(jsonObjectOutlet.optString(Jsonkey.seminarid));
                    list.setSeminar_name(jsonObjectOutlet.optString(Jsonkey.seminarname));
                    list.setDate(jsonObjectOutlet.optString(Jsonkey.date));
                    list.setTotal_seat(jsonObjectOutlet.optString(Jsonkey.total_seat));
                    list.setBooked_seat(jsonObjectOutlet.optString(Jsonkey.booked_seat));
                    list.setSeminar_image(jsonObjectOutlet.optString(Jsonkey.listimage));
                    list.setWishlist_id(jsonObjectOutlet.optString(Jsonkey.wishlistid));

                    wishlist_items.add(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
