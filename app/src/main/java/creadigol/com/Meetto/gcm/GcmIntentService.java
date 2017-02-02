package creadigol.com.Meetto.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import App.Config;
import Models.BaseObject;
import Utils.AppUrl;
import Utils.Constants;
import Utils.Jsonkey;
import Utils.ParamsKey;
import Utils.PreferenceSettings;
import creadigol.com.Meetto.MeettoApplication;
import creadigol.com.Meetto.R;


public class GcmIntentService extends IntentService
{
    private static final String TAG = GcmIntentService.class.getSimpleName();

    public GcmIntentService()
    {
        super(TAG);
    }

    public static final String KEY = "key";
    public static final String TOPIC = "topic";
    public static final String SUBSCRIBE = "subscribe";
    public static final String UNSUBSCRIBE = "unsubscribe";
    private int countTimeOut = 0;
    @Override
    protected void onHandleIntent(Intent intent)
    {
        String key = intent.getStringExtra(KEY);

        switch (key)
        {
            case SUBSCRIBE:
                // subscribe to a topic
                String topic = intent.getStringExtra(TOPIC);
                subscribeToTopic(topic);
                break;

            case UNSUBSCRIBE:
                String topic1 = intent.getStringExtra(TOPIC);
                unsubscribeFromTopic(topic1);
                break;

            default:
                // if key is not specified, register with GCM
                registerGCM();
        }
    }

    /**
     * Registering with GCM and obtaining the gcm registration id
     */

    private void registerGCM()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = null;

        try
        {
            InstanceID instanceID = InstanceID.getInstance(this);
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.e(TAG, "GCM Registration Token: " + token);
            reqSetGcmId(token);

            // sending the registration id to our server
            sendRegistrationToServer(token);

            sharedPreferences.edit().putBoolean(Config.SENT_TOKEN_TO_SERVER, true).apply();
        }
        catch (Exception e)
        {
            Log.e(TAG, "Failed to complete token refresh", e);
            sharedPreferences.edit().putBoolean(Config.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", token);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token)
    {
        // Send the registration token to our server
        // to keep it in MySQL
    }

    /**
     * Subscribe to a topic
     */
    public void subscribeToTopic(String topic)
    {
        GcmPubSub pubSub = GcmPubSub.getInstance(getApplicationContext());
        InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
        String token = null;
        try
        {
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            if (token != null)
            {
                pubSub.subscribe(token, "/topics/" + topic, null);
                Log.e(TAG, "Subscribed to topic: " + topic);
            }
            else
            {
                Log.e(TAG, "error: gcm registration id is null");
            }
        }
        catch (IOException e)
        {
            Log.e(TAG, "Topic subscribe error. Topic: " + topic + ", error: " + e.getMessage());
//            Toast.makeText(getApplicationContext(), "Topic subscribe error. Topic: " + topic + ", error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void unsubscribeFromTopic(String topic)
    {
        GcmPubSub pubSub = GcmPubSub.getInstance(getApplicationContext());
        InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
        String token = null;
        try
        {
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            if (token != null)
            {
                pubSub.unsubscribe(token, "");
                Log.e(TAG, "Unsubscribed from topic: " + topic);
            } else {
                Log.e(TAG, "error: gcm registration id is null");
            }
        } catch (IOException e)
        {
            Log.e(TAG, "Topic unsubscribe error. Topic: " + topic + ", error: " + e.getMessage());
//            Toast.makeText(getApplicationContext(), "Topic subscribe error. Topic: " + topic + ", error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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