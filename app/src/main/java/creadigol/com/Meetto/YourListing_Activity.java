package creadigol.com.Meetto;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.YourListing_Adapter;
import Models.YourListObject;
import Models.Yourlist_item;
import Utils.AppUrl;
import Utils.CommonUtils;
import Utils.Jsonkey;
import Utils.ParamsKey;
import Utils.PreferenceSettings;

/**
 * Created by Creadigol on 03-09-2016.
 */
public class YourListing_Activity extends AppCompatActivity {
    RecyclerView rv_listing;
    TextView tv_msg;
    String seminar_id;
    YourListing_Adapter Listing_adapter;
    ArrayList<Yourlist_item> listing_items;
    private final String TAG = YourListing_Activity.class.getSimpleName();
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
        setContentView(R.layout.activity_yourlisting);
        rv_listing = (RecyclerView) findViewById(R.id.rv_listing);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        Toolbar();
        Yourlist();

    }

    public void Toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_listing);
        setSupportActionBar(toolbar);
        TextView tvCatTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_name);
        tvCatTitle.setText(R.string.your_listing_toolbar);
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

    View.OnClickListener onClickListing = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            int position = (int) v.getTag();
            Yourlist_item yourlistItem = listing_items.get(position);
            String type = yourlistItem.getSeminar_id();
            Intent i=new Intent(YourListing_Activity.this,Add_Seminar_Activity.class);
            mPreferenceSettings.setEdit(true);
            mPreferenceSettings.setSeminar_id(type);
            startActivity(i);
        }
    };
    View.OnClickListener onclickshow= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            Yourlist_item yourlistItem = listing_items.get(position);
            seminar_id=yourlistItem.getSeminar_id();
            Intent i=new Intent(YourListing_Activity.this,Bookinglist_activity.class);
           i.putExtra("seminar_id",seminar_id);
            startActivity(i);
        }
    };
    View.OnClickListener onclickdelete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            Yourlist_item yourlistItem = listing_items.get(position);
            seminar_id=yourlistItem.getSeminar_id();
            dialogBox();
        }
    };

    void setListing() {
        if (listing_items.size() > 0 || listing_items != null) {
            if (Listing_adapter == null) {
                rv_listing.setHasFixedSize(true);
                rv_listing.setItemAnimator(new DefaultItemAnimator());
                rv_listing.setLayoutManager(new LinearLayoutManager(this));
                Listing_adapter = new YourListing_Adapter(this, listing_items,onClickListing,onclickshow, onclickdelete);
                rv_listing.setAdapter(Listing_adapter);
            } else {
                    Listing_adapter.modifyDataSet(listing_items);
            }
        }else{
                tv_msg.setVisibility(View.VISIBLE);
                rv_listing.setVisibility(View.GONE);
            }
        }

    public void Yourlist() {
        final ProgressDialog mProgressDialog = new ProgressDialog(YourListing_Activity.this);
        mProgressDialog.setMessage("Loading.....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, AppUrl.URL_USER_LISTING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e(TAG, "Response: " + response);

                    JSONObject jsonObject = new JSONObject(response);
                    YourListObject yourListObject = new YourListObject();
                    try {
                        yourListObject.setStatus_code(jsonObject.getInt(Jsonkey.statusCode));
                    } catch (JSONException e) {
                        yourListObject.setStatus_code(0);
                    }

                    yourListObject.setMessage(jsonObject.optString(Jsonkey.message));

                    if (yourListObject.getStatus_code() == 1) {

                        try {
                            yourListObject.setHomeObjects(jsonObject.getJSONArray(Jsonkey.listKey));
                        } catch (JSONException e) {
                            yourListObject.setHomeObjects(null);
                        }

                        listing_items = yourListObject.getHomelist();


                        Log.e("size of deals", "" + listing_items.size());
                        // set location in list
                        setListing();
                    } else if (yourListObject.getStatus_code() == 0) {
                        mProgressDialog.dismiss();
                        tv_msg.setVisibility(View.VISIBLE);
                        rv_listing.setVisibility(View.GONE);
                    } else {
                        mProgressDialog.dismiss();
                        tv_msg.setVisibility(View.VISIBLE);
                        rv_listing.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    mProgressDialog.dismiss();
                    tv_msg.setVisibility(View.VISIBLE);
                    rv_listing.setVisibility(View.GONE);
                }
                mProgressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Log.e("Error_in", "onErrorResponse");
                showTryAgainAlert(""+getResources().getString(R.string.network1)+"", getResources().getString(R.string.network2)+"");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //when user select city or location in select location activity
                params.put(ParamsKey.KEY_USERID,mPreferenceSettings.getUserId());
//                params.put(ParamsKey.KEY_USERID, "18");
                if(mPreferenceSettings.getLUNGAUGE()){
                    params.put(ParamsKey.KEY_USER_LANG, "ja");
                }
                if (mPreferenceSettings.getLUNGAUGE()==false){
                    params.put(ParamsKey.KEY_USER_LANG, "en");
                }
                Log.e(TAG, "reqSearchLocationData params: " + params.toString());

                return params;
            }
        };

        MeettoApplication.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
    }

    public void showTryAgainAlert(String title, String message) {
        CommonUtils.showAlertWithNegativeButton(YourListing_Activity.this, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (CommonUtils.isNetworkAvailable())
                    Yourlist();
                else
                    CommonUtils.showToast(""+getResources().getString(R.string.network3));
            }
        });
    }
    public void dialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(YourListing_Activity.this);

        // set title
        alertDialogBuilder.setTitle(getResources().getString(R.string.your_listing_Delete_Item));

        // set dialog message

        alertDialogBuilder
                .setMessage(getResources().getString(R.string.your_listing_Delete_msg))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.your_listing_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        reqDelete();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.your_listing_Cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        alertDialogBuilder.show();
    }

    public void reqDelete() {
        final String url = AppUrl.URL_DELETE_LISTING;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("list", response.toString());

                try {
                    JSONObject responseObj = new JSONObject(response);

                    int status_code = responseObj.getInt("status_code");
                    String msg = responseObj.getString("message");

                    Log.e("list", "Response: " + response);
                    if (status_code == 1) {
                        Yourlist();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    } else if (status_code == 0) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("Error_in", "else");
                    }

                } catch (JSONException e) {
                    Log.e("Error_in", "catch");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Booking", "Error Response: " + error.getMessage());
                Log.e("Error_in", "onErrorResponse");
                showTryAgainAlert(""+getResources().getString(R.string.network1)+"", getResources().getString(R.string.network2)+"");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_SEMINAR_ID, seminar_id);
                params.put(ParamsKey.KEY_USERID, mPreferenceSettings.getUserId());
                if(mPreferenceSettings.getLUNGAUGE()){
                    params.put(ParamsKey.KEY_USER_LANG, "ja");
                }
                if (mPreferenceSettings.getLUNGAUGE()==false){
                    params.put(ParamsKey.KEY_USER_LANG, "en");

                }
                Log.e("Wishlist", "params: " + params.toString());

                return params;
            }
        };


        MeettoApplication.getInstance().addToRequestQueue(strReq, "call");
    }
}
