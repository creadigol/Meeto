package creadigol.com.Meetto;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
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

import Adapters.SeminarList_Adapter;
import Models.SeminarListItem;
import Models.SeminarListObject;
import Utils.AppUrl;
import Utils.CommonUtils;
import Utils.Jsonkey;
import Utils.ParamsKey;
import Utils.PreferenceSettings;

/**
 * Created by Creadigol on 05-09-2016.
 */
public class SeminarList_Activity extends AppCompatActivity implements View.OnClickListener {
    public static RecyclerView recyclerView;
    FloatingActionButton filter;
    String cityid, cityname, from_date, to_date, purpose, propertytype, facilities, type, industry;
    PreferenceSettings mPreferenceSettings;
    ArrayList<SeminarListItem> seminarListItems;
    SeminarList_Adapter seminarListAdapter;
    String Seminar_id;
    TextView tv_msg;
    private static final String TAG = SeminarList_Activity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();
        if (mPreferenceSettings.getLUNGAUGE()) {
            MeettoApplication.language("ja");
        } else {
            MeettoApplication.language("en");
        }
        setContentView(R.layout.activity_seminarlist);

        Intent intent = getIntent();
        cityid = intent.getStringExtra("cityid");
        from_date = intent.getStringExtra("fromdate");
        to_date = intent.getStringExtra("todate");
        purpose = intent.getStringExtra("purpose");
        propertytype = intent.getStringExtra("property");
//        facilities = intent.getStringExtra("facilitie");
        cityname = intent.getStringExtra("cityname");
        type = intent.getStringExtra("type");
        industry = intent.getStringExtra("industry");


        Seminarlist();
        Toolbar();

        filter = (FloatingActionButton) findViewById(R.id.fab_filter);
        filter.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.rvseminar_list);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && filter.isShown())
                    filter.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    filter.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });


    }

    public void Toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_seminarlist);
        setSupportActionBar(toolbar);
        TextView tvCatTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_name);
        tvCatTitle.setText(R.string.seminar_list_name);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                MeettoApplication.attendess.clear();
                MeettoApplication.seminattype.clear();
                MeettoApplication.industry.clear();
                finish();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        MeettoApplication.attendess.clear();
//        MeettoApplication.seminattype.clear();
//        MeettoApplication.industry.clear();
    }

    View.OnClickListener onclickaddwishlist = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            SeminarListItem listItem = seminarListItems.get(position);
            Seminar_id = listItem.getSeminar_id();
            reqAddWishlist();
        }
    };

    private void seminaradapter() {
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        if (seminarListItems != null) {
            tv_msg.setVisibility(View.GONE);

            recyclerView.setHasFixedSize(true);
            //Set RecyclerView type according to intent value
            GridLayoutManager gridLayoutManager = new GridLayoutManager(SeminarList_Activity.this, 2);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(gridLayoutManager);

            seminarListAdapter = new SeminarList_Adapter(SeminarList_Activity.this, seminarListItems, onclickaddwishlist);
            recyclerView.setAdapter(seminarListAdapter);// set adapter on recyclerview
            seminarListAdapter.notifyDataSetChanged();// Notify the adapter
        } else {
            tv_msg.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_filter:
                Intent filter = new Intent(SeminarList_Activity.this, SearchFilter_Activity.class);
                filter.putExtra("cityid", cityid);
                filter.putExtra("cityname", cityname);
                startActivity(filter);

        }
    }

    public void Seminarlist() {
        final ProgressDialog mProgressDialog = new ProgressDialog(SeminarList_Activity.this);
        mProgressDialog.setMessage("Loading.....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, AppUrl.URL_SEMINAR_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e(TAG, "Response: " + response);

                    JSONObject jsonObject = new JSONObject(response);
                    SeminarListObject seminarListObject = new SeminarListObject();
                    try {
                        seminarListObject.setStatus_code(jsonObject.getInt(Jsonkey.statusCode));
                    } catch (JSONException e) {
                        seminarListObject.setStatus_code(0);
                    }

                    seminarListObject.setMessage(jsonObject.optString(Jsonkey.message));

                    if (seminarListObject.getStatus_code() == 1) {

                        try {
                            seminarListObject.setHomeObjects(jsonObject.getJSONArray(Jsonkey.seminarlistkey));
                        } catch (JSONException e) {
                            seminarListObject.setHomeObjects(null);
                        }

                        seminarListItems = seminarListObject.getHomelist();


                        Log.e("size of deals", "" + seminarListItems.size());
                        // set location in list
                        seminaradapter();
                    } else if (seminarListObject.getStatus_code() == 0) {
                        mProgressDialog.dismiss();
                        seminaradapter();
//                        showTryAgainAlert("Info", seminarListObject.getMessage() + " try again!");
                    } else {
                        mProgressDialog.dismiss();
                        showTryAgainAlert("Opps", "something are wrong!");
                        Log.e("Error_in", "else");
                    }
                } catch (JSONException e) {
                    mProgressDialog.dismiss();
                    showTryAgainAlert("Opps", "something are wrong!");
                    Log.e("Error_in", "catch");
                }
                mProgressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Log.e("Error_in", "onErrorResponse");
                showTryAgainAlert("" + getResources().getString(R.string.network1) + "", getResources().getString(R.string.network2) + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //when user select city or location in select location activity
                if (cityid != null && mPreferenceSettings.getfilter() == false) {
                    params.put(ParamsKey.KEY_CITY_ID, cityid);
                    params.put(ParamsKey.KEY_TYPE, "CITY");
                    params.put(ParamsKey.KEY_USERID, mPreferenceSettings.getUserId());
                    if (mPreferenceSettings.getLUNGAUGE()) {
                        params.put(ParamsKey.KEY_USER_LANG, "ja");
                    }
                    if (mPreferenceSettings.getLUNGAUGE() == false) {
                        params.put(ParamsKey.KEY_USER_LANG, "en");
                    }
                } else {
                    mPreferenceSettings.setfilter(false);
                    params.put(ParamsKey.KEY_CITY_ID, cityname);
                    params.put(ParamsKey.KEY_TYPE, type);
//                    params.put(ParamsKey.KEY_FACILITIE, facilities);
                    params.put(ParamsKey.KEY_PROPERTYTYPE, propertytype);
                    params.put(ParamsKey.KEY_PURPOSE, purpose);
                    params.put(ParamsKey.KEY_FROM_DATE, from_date);
                    params.put(ParamsKey.KEY_INDUSTRY, industry);
                    params.put(ParamsKey.KEY_TO_DATE, to_date);
                    params.put(ParamsKey.KEY_USERID, mPreferenceSettings.getUserId());
                    if (mPreferenceSettings.getLUNGAUGE()) {
                        params.put(ParamsKey.KEY_USER_LANG, "ja");
                    }
                    if (mPreferenceSettings.getLUNGAUGE() == false) {
                        params.put(ParamsKey.KEY_USER_LANG, "en");

                    }
                }
                Log.e(TAG, "reqSearchLocationData params: " + params.toString());

                return params;
            }

        };

        MeettoApplication.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
    }

    public void showTryAgainAlert(String title, String message) {
        CommonUtils.showAlertWithNegativeButton(SeminarList_Activity.this, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (CommonUtils.isNetworkAvailable())
                    Seminarlist();
                else
                    CommonUtils.showToast(getResources().getString(R.string.network3));
            }
        });
    }

    public void reqAddWishlist() {
        final String url = AppUrl.URL_ADD_WISHLIST;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("list", response.toString());
//                mProgressDialog.dismiss();

                try {
                    JSONObject responseObj = new JSONObject(response);

                    int status_code = responseObj.getInt("status_code");
                    String msg = responseObj.getString("message");

                    Log.e("list", "Response: " + response);
                    if (status_code == 1) {
                        Seminarlist();
                        Toast.makeText(SeminarList_Activity.this, msg, Toast.LENGTH_LONG).show();

                    } else if (status_code == 0) {
                        Toast.makeText(SeminarList_Activity.this, msg, Toast.LENGTH_LONG).show();
//                        mProgressDialog.dismiss();
                    } else {
//                        mProgressDialog.dismiss();
                        Log.e("Error_in", "else");
                    }

                } catch (JSONException e) {
//                    mProgressDialog.dismiss();
                    Log.e("Error_in", "catch");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Booking", "Error Response: " + error.getMessage());
//                mProgressDialog.dismiss();
                Log.e("Error_in", "onErrorResponse");
                showTryAgainAlert("" + getResources().getString(R.string.network1) + "", getResources().getString(R.string.network2) + "");


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_SEMINAR_ID, Seminar_id);
                params.put(ParamsKey.KEY_USERID, mPreferenceSettings.getUserId());
//                params.put("user_id", "18");
                if (mPreferenceSettings.getLUNGAUGE()) {
                    params.put(ParamsKey.KEY_USER_LANG, "ja");
                }
                if (mPreferenceSettings.getLUNGAUGE() == false) {
                    params.put(ParamsKey.KEY_USER_LANG, "en");

                }
                Log.e("Wishlist", "params: " + params.toString());

                return params;
            }

        };


        MeettoApplication.getInstance().addToRequestQueue(strReq, "call");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MeettoApplication.attendess.clear();
        MeettoApplication.seminattype.clear();
        MeettoApplication.industry.clear();
        MeettoApplication.fromdate = "";
        MeettoApplication.todate = "";
    }
}
