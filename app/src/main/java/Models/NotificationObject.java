package Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.Jsonkey;

/**
 * Created by Ashfaq on 27-07-2016.
 */
public class NotificationObject extends BaseObject {

    ArrayList<NotificationItem> notificationItems;

    public ArrayList<NotificationItem> getNotificationItems() {
        return notificationItems;
    }

    public void setNotificationObjects(JSONArray jsonLocationArray) {
        notificationItems = new ArrayList<>();
        if (jsonLocationArray != null) {
            for (int i = 0; i < jsonLocationArray.length(); i++) {
                NotificationItem notificationobject = new NotificationItem();
                try {
                    JSONObject jsonObjectOutlet = jsonLocationArray.getJSONObject(i);

                    notificationobject.setSeminarId(jsonObjectOutlet.optString(Jsonkey.Notificationseminarid));
                    notificationobject.setSeminarname(jsonObjectOutlet.optString(Jsonkey.Notificationseminar_name));
                    notificationobject.setType(jsonObjectOutlet.optString(Jsonkey.NotificationType));
                    notificationobject.setCreatedTime(jsonObjectOutlet.optString(Jsonkey.NotificationCreatedTime));
                    notificationobject.setDescription(jsonObjectOutlet.optString(Jsonkey.NotificationDesc));
                    notificationobject.setImage(jsonObjectOutlet.optString(Jsonkey.NotificationImage));
                    notificationobject.setStatus(jsonObjectOutlet.optString(Jsonkey.NotificationStatus));
                    notificationobject.setTransactionId(jsonObjectOutlet.optString(Jsonkey.NotificationTransactionId));
                    notificationobject.setReason(jsonObjectOutlet.optString(Jsonkey.NotificationReason));

                    notificationItems.add(notificationobject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
