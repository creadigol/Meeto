package Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.Jsonkey;

/**
 * Created by Creadigol on 7/26/2016.
 */
public class User_data_Object extends BaseObject {
    ArrayList<User_data_item> user_data_items;

    public ArrayList<User_data_item> getLanguage() {
        return user_data_items;
    }

    public void setLanguage(JSONArray jsonLocationArray) {
        user_data_items = new ArrayList<>();
        if (jsonLocationArray != null) {
            for (int i = 0; i < jsonLocationArray.length(); i++) {
                User_data_item data_item= new User_data_item();
                try {
                    JSONObject jsonObjectOutlet = jsonLocationArray.getJSONObject(i);

                    data_item.setLang_id(jsonObjectOutlet.optString(Jsonkey.user_langid));
                    data_item.setLang_name(jsonObjectOutlet.optString(Jsonkey.user_langid));

                    user_data_items.add(data_item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    ArrayList<User_data_item> user_facality;

    public ArrayList<User_data_item> getFacilitie() {
        return user_facality;
    }

    public void setFacilitie(JSONArray jsonLocationArray) {
        user_facality = new ArrayList<>();
        if (jsonLocationArray != null) {
            for (int i = 0; i < jsonLocationArray.length(); i++) {
                User_data_item data_item= new User_data_item();
                try {
                    JSONObject jsonObjectOutlet = jsonLocationArray.getJSONObject(i);

                    data_item.setFacility_id(jsonObjectOutlet.optString(Jsonkey.user_facilityid));
                    data_item.setFacility_name(jsonObjectOutlet.optString(Jsonkey.user_facilityname));

                    user_facality.add(data_item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    ArrayList<User_data_item> user_data_type;

    public ArrayList<User_data_item> getSemtype() {
        return user_data_type;
    }

    public void setSemtype(JSONArray jsonLocationArray) {
        user_data_type = new ArrayList<>();
        if (jsonLocationArray != null) {
            for (int i = 0; i < jsonLocationArray.length(); i++) {
                User_data_item data_item= new User_data_item();
                try {
                    JSONObject jsonObjectOutlet = jsonLocationArray.getJSONObject(i);

                    data_item.setSem_id(jsonObjectOutlet.optString(Jsonkey.user_semid));
                    data_item.setSem_name(jsonObjectOutlet.optString(Jsonkey.user_semname));

                    user_data_type.add(data_item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    ArrayList<User_data_item> user_data_attendess;
    public ArrayList<User_data_item> getAttendess() {
        return user_data_attendess;
    }

    public void setAttendess(JSONArray jsonLocationArray) {
        user_data_attendess= new ArrayList<>();
        if (jsonLocationArray != null) {
            for (int i = 0; i < jsonLocationArray.length(); i++) {
                User_data_item data_item= new User_data_item();
                try {
                    JSONObject jsonObjectOutlet = jsonLocationArray.getJSONObject(i);

                    data_item.setAttendess_id(jsonObjectOutlet.optString(Jsonkey.user_attendessid));
                    data_item.setAttendess_name(jsonObjectOutlet.optString(Jsonkey.user_attendessname));

                    user_data_attendess.add(data_item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    ArrayList<User_data_item> user_data_industry;
    public ArrayList<User_data_item> getIndustry() {
        return user_data_industry;
    }

    public void setgetIndustry(JSONArray jsonLocationArray) {
        user_data_industry= new ArrayList<>();
        if (jsonLocationArray != null) {
            for (int i = 0; i < jsonLocationArray.length(); i++) {
                User_data_item data_item= new User_data_item();
                try {
                    JSONObject jsonObjectOutlet = jsonLocationArray.getJSONObject(i);

                    data_item.setIndustry_id(jsonObjectOutlet.optString(Jsonkey.user_industryid));
                    data_item.setIndustry_name(jsonObjectOutlet.optString(Jsonkey.user_industryname));

                    user_data_industry.add(data_item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
