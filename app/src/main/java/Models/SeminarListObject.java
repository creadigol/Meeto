package Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.Jsonkey;

/**
 * Created by Creadigol on 7/26/2016.
 */
public class SeminarListObject extends BaseObject {
    ArrayList<SeminarListItem> seminarListItems;

    public ArrayList<SeminarListItem> getHomelist() {
        return seminarListItems;
    }

    public void setHomeObjects(JSONArray jsonLocationArray) {
        seminarListItems = new ArrayList<>();
        if (jsonLocationArray != null) {
            for (int i = 0; i < jsonLocationArray.length(); i++) {
                SeminarListItem seminarItem= new SeminarListItem();
                try {
                    JSONObject jsonObjectOutlet = jsonLocationArray.getJSONObject(i);

                    seminarItem.setSeminar_id(jsonObjectOutlet.optString(Jsonkey.seminarid));
                    seminarItem.setSeminar_name(jsonObjectOutlet.optString(Jsonkey.seminarname));
                    seminarItem.setSeminar_addrress(jsonObjectOutlet.optString(Jsonkey.seminaraddress));
                    seminarItem.setSeminar_image(jsonObjectOutlet.optString(Jsonkey.seminarimage));
                    seminarItem.setWishlist(jsonObjectOutlet.optString(Jsonkey.Wishlist));

                    seminarListItems.add(seminarItem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
