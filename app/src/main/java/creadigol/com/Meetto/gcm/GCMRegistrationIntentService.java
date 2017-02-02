package creadigol.com.Meetto.gcm;

/**
 * Created by Vj on 7/28/2016.
 */

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Models.BaseObject;
import Utils.AppUrl;
import Utils.Constants;
import Utils.Jsonkey;
import Utils.ParamsKey;
import Utils.PreferenceSettings;
import creadigol.com.Meetto.MeettoApplication;
import creadigol.com.Meetto.R;


/**
 * Created by Belal on 4/15/2016.
 */
public class GCMRegistrationIntentService extends IntentService {
    //Constants for success and errors
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";

    private final String TAG = GCMRegistrationIntentService.class.getSimpleName();
    private int countTimeOut = 0;

    //Class constructor
    public GCMRegistrationIntentService() {
        super("");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        //Registering gcm to the device
        registerGCM();
    }

    private void registerGCM() {
        //Registration complete intent initially null
        //Intent registrationComplete = null;

        //Register token is also null
        //we will get the token on successfull registration
        String token = null;
        try {
            //Creating an instanceid
            InstanceID instanceID = InstanceID.getInstance(this);

            //Getting the token from the instance id
            token = instanceID.getToken(getString(R.string.gcm_sender_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            //Displaying the token in the log so that we can copy it to send push notification
            //You can also extend the app by storing the token in to your server
            Log.w("GCMRegIntentService", "Gcm Id: " + token);
            // send gcm id to server
            reqSetGcmId(token);
            //on registration complete creating intent with success
            //registrationComplete = new Intent(REGISTRATION_SUCCESS);

            //Putting the token to the intent
            //registrationComplete.putExtra("token", token);
        } catch (Exception e) {
            //If any error occurred
            Log.w("GCMRegIntentService", "Registration error");
            //registrationComplete = new Intent(REGISTRATION_ERROR);
        }

        //Sending the broadcast that registration is completed
        //LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    public void reqSetGcmId(final String gcmToken) {
      StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, AppUrl.URL_SET_GCM_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e(TAG, "Response: " + response);
                    JSONObject jsonObject = new JSONObject(response);
                    BaseObject baseObject = new BaseObject();
                    try {
                        baseObject.setStatus_code(jsonObject.getInt(Jsonkey.statusCode));
                    } catch (JSONException e) {
                        baseObject.setStatus_code(0);
                    }

                    baseObject.setMessage(jsonObject.optString(Jsonkey.message));

                    if (baseObject.getStatus_code() == 1) {

                    } else {

                    }
                } catch (JSONException e) {

                }
                //mProgressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (countTimeOut >= Constants.LIMIT_MEX_TIMEOUT) {
                    countTimeOut = 0;
                } else {
                    countTimeOut++;
                    reqSetGcmId(gcmToken);
                }
                //mProgressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                PreferenceSettings mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();

                params.put(ParamsKey.KEY_GCM_TOKEN, gcmToken);
                params.put(ParamsKey.KEY_USERID, mPreferenceSettings.getUserId());
                Log.e(TAG, "reqSetGcmId params: " + params.toString());

                return params;
            }
        };

        MeettoApplication.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
    }

}