package Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.Jsonkey;

/**
 * Created by Creadigol on 7/26/2016.
 */
public class YourListObject extends BaseObject {
    ArrayList<Yourlist_item> youritems;

    public ArrayList<Yourlist_item> getHomelist() {
        return youritems;
    }

    public void setHomeObjects(JSONArray jsonLocationArray) {
        youritems = new ArrayList<>();
        if (jsonLocationArray != null) {
            for (int i = 0; i < jsonLocationArray.length(); i++) {
                Yourlist_item list= new Yourlist_item();
                try {
                    JSONObject jsonObjectOutlet = jsonLocationArray.getJSONObject(i);

                    list.setSeminar_id(jsonObjectOutlet.optString(Jsonkey.seminarid));
                    list.setSeminar_name(jsonObjectOutlet.optString(Jsonkey.seminarname));
                    list.setDescription(jsonObjectOutlet.optString(Jsonkey.listdesc));
                    list.setSeminar_image(jsonObjectOutlet.optString(Jsonkey.listimage));
                    list.setTagline(jsonObjectOutlet.optString(Jsonkey.tagline));

                    youritems.add(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
