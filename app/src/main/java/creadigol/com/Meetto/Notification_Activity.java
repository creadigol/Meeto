package creadigol.com.Meetto;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import Adapters.Notification_Adapter;
import App.MyApplication;
import Models.NotificationItem;
import Models.NotificationObject;
import Utils.AppUrl;
import Utils.CommonUtils;
import Utils.Jsonkey;
import Utils.ParamsKey;
import Utils.PreferenceSettings;
import creadigol.com.Meetto.Database.DataBaseHelper;

/**
 * Created by Creadigol on 12-09-2016.
 */
public class Notification_Activity extends AppCompatActivity {
    ArrayList<NotificationItem> notification_items;
    Notification_Adapter notification_adapter;
    DataBaseHelper dataBaseHelper;
    private final String TAG = Notification_Activity.class.getSimpleName();
    PreferenceSettings mPreferenceSettings;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferenceSettings= MeettoApplication.getInstance().getPreferenceSettings();
        if(mPreferenceSettings.getLUNGAUGE()){
            MeettoApplication.language("ja");
        }else{
            MeettoApplication.language("en");
        }
        setContentView(R.layout.activity_notification);
        Toolbar();
        MyApplication.notifycount = 0;

        dataBaseHelper = new DataBaseHelper(this);
        Calendar calendar = Calendar.getInstance();
        if (CommonUtils.isNetworkAvailable())
            getNotificationData(calendar.getTimeInMillis());
        else
            showNetworkConnection(""+getResources().getString(R.string.network1)+"", getResources().getString(R.string.network2)+"");

    }

    public void Toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_notification);
        setSupportActionBar(toolbar);
        TextView tvCatTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_name);
        tvCatTitle.setText(R.string.notification_toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    void setNotificationData(ArrayList<NotificationItem> notificationItems) {

        LinearLayout linear_noNotification = (LinearLayout) findViewById(R.id.linear_noNotification);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_notificataion);

        if (notificationItems != null && notificationItems.size() > 0) {

            if (notification_adapter == null) {

                recyclerView.setVisibility(View.VISIBLE);
                linear_noNotification.setVisibility(View.GONE);
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

                notification_adapter = new Notification_Adapter(this, notificationItems);
                recyclerView.setAdapter(notification_adapter);
            } else {
                notification_adapter.modifyDataSet(notificationItems);
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            linear_noNotification.setVisibility(View.VISIBLE);
        }
    }
    private void getNotificationData(final long newSyncTime) {
        String url = AppUrl.URL_NOTIFICATION;
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        final StringRequest notificationReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String Response) {
                Log.e("Response", Response.toString());
                try {
                    JSONObject jsonObj = new JSONObject(Response);
                    NotificationObject notificationObject = new NotificationObject();

                    try {
                        notificationObject.setStatus_code(jsonObj.getInt(Jsonkey.statusCode));
                    } catch (JSONException e) {
                        notificationObject.setStatus_code(0);

                    }
                    notificationObject.setMessage(jsonObj.getString(Jsonkey.message));

                    if (notificationObject.getStatus_code() == 1) {

                        try {
                            notificationObject.setNotificationObjects(jsonObj.getJSONArray(Jsonkey.NotificationKey));
                        } catch (JSONException e) {
                            notificationObject.setNotificationObjects(null);
                        }

                        // Add new notifications in database
                        addNotificationInDatabase(notificationObject.getNotificationItems());

                        // Set new sync time in preference
                        MeettoApplication.getInstance().getPreferenceSettings().setLastSyncTime(newSyncTime);

                        pDialog.dismiss();
                    } else if (notificationObject.getStatus_code() == 0) {
                        addNotificationInDatabase(null);
                        pDialog.dismiss();
                    } else if (notificationObject.getStatus_code() == 2) {
                        MeettoApplication.getInstance().getPreferenceSettings().setLastSyncTime(newSyncTime);
                        addNotificationInDatabase(null);
                        pDialog.dismiss();
                    }  else {
                        addNotificationInDatabase(null);
                        pDialog.dismiss();
                        Log.e("Error_in", "else");
                    }
                } catch (Exception e) {
                    addNotificationInDatabase(null);
                    Log.e("Error_in", "catch");
                    pDialog.dismiss();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("getNotificationData", "Error Response: " + error.getMessage());
                Log.e("Error_in", "onErrorResponse");
                addNotificationInDatabase(null);
                pDialog.dismiss();
            }
        })


        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_USERID, mPreferenceSettings.getUserId());
//                params.put(ParamsKey.KEY_USERID, m);
                params.put(ParamsKey.KEY_SYNCTIME, String.valueOf(mPreferenceSettings.getLastSyncTime()));
                if(mPreferenceSettings.getLUNGAUGE()){
                    params.put(ParamsKey.KEY_USER_LANG, "ja");
                }
                if (mPreferenceSettings.getLUNGAUGE()==false){
                    params.put(ParamsKey.KEY_USER_LANG, "en");

                }
                Log.e("getNotificationData", "Posting params: " + params.toString());
                return params;

            }
        };
        // Adding request to request queue
        MeettoApplication.getInstance().addToRequestQueue(notificationReq, TAG);
    }

    public void addNotificationInDatabase(ArrayList<NotificationItem> notificationItems) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getBaseContext());
        if (notificationItems != null && notificationItems.size() > 0) {
            for (NotificationItem notificationItem : notificationItems) {
                dataBaseHelper.createNotification(notificationItem);
            }
        }

        notificationItems = dataBaseHelper.getNotifications();
        setNotificationData(notificationItems);

    }
    public void showNetworkConnection(String title, String message) {
        CommonUtils.showAlertWithNegativeButton(Notification_Activity.this, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                if (CommonUtils.isNetworkAvailable()) {

                } else
                    CommonUtils.showToast(getResources().getString(R.string.network3));
            }
        });
    }


}