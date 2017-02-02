package Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.Jsonkey;

/**
 * Created by Creadigol on 7/26/2016.
 */
public class SearchObject extends BaseObject {
    ArrayList<Searchitem> searchitems;

    public ArrayList<Searchitem> getSearchitems() {
        return searchitems;
    }

    public void setSearchitems(JSONArray jsonLocationArray) {
        searchitems = new ArrayList<>();
        if (jsonLocationArray != null) {
            for (int i = 0; i < jsonLocationArray.length(); i++) {
                Searchitem searchitem= new Searchitem();
                try {
                    JSONObject jsonObjectOutlet = jsonLocationArray.getJSONObject(i);

                    searchitem.setSearch_id(jsonObjectOutlet.optString(Jsonkey.search_id));
                    searchitem.setSearch_image(jsonObjectOutlet.optString(Jsonkey.search_image));
                    searchitem.setSearch_name(jsonObjectOutlet.optString(Jsonkey.search_name));
                    searchitem.setSearch_type(jsonObjectOutlet.optString(Jsonkey.search_type));

                    searchitems.add(searchitem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
