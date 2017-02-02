package creadigol.com.Meetto;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;

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

import Adapters.SeminarBookList_Adapter;
import Models.SeminarBookListitem;
import Models.SeminarBooklistObject;
import Utils.AppUrl;
import Utils.CommonUtils;
import Utils.Jsonkey;
import Utils.ParamsKey;
import Utils.PreferenceSettings;

/**
 * Created by Creadigol on 05-10-2016.
 */
public class Bookinglist_activity extends AppCompatActivity{
    RecyclerView rv_seminarbook;
    TextView tv_msg;
    String seminar_id,seminar_booking_no;
    SeminarBookList_Adapter Booklist_adapter;
    ArrayList<SeminarBookListitem> booklisting_items;
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
        setContentView(R.layout.activity_bookinglist);
        rv_seminarbook = (RecyclerView) findViewById(R.id.rv_seminarbooklist);
        tv_msg = (TextView) findViewById(R.id.tv_booklistmsg);
        Toolbar();
        Intent intent=getIntent();
        seminar_id=intent.getStringExtra("seminar_id");
        Yourbooklist();
    }

    public void Toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_seminarbooklist);
        setSupportActionBar(toolbar);
        TextView tvCatTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_name);
        tvCatTitle.setText(R.string.seminar_booklist_toolbar);
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


    View.OnClickListener onclick= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            SeminarBookListitem seminarBookListitem =booklisting_items .get(position);
            seminar_id= seminarBookListitem.getSeminar_id();
            seminar_booking_no= seminarBookListitem.getSeminar_boooking_no();
            if(seminarBookListitem.getStatus().equalsIgnoreCase("pending")) {
                Intent intent = new Intent(Bookinglist_activity.this, SeminarApprovedCancel_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(SeminarApprovedCancel_Activity.EXTRA_BOOK, seminarBookListitem);
                startActivity(intent);
            }
        }
    };
    void setListing() {
        if (booklisting_items.size() > 0 || booklisting_items != null) {
            if (Booklist_adapter == null) {
                rv_seminarbook.setHasFixedSize(true);
                rv_seminarbook.setItemAnimator(new DefaultItemAnimator());
                rv_seminarbook.setLayoutManager(new LinearLayoutManager(this));
                Booklist_adapter = new SeminarBookList_Adapter(this, booklisting_items,onclick);
                rv_seminarbook.setAdapter(Booklist_adapter);
            } else {
                Booklist_adapter.modifyDataSet(booklisting_items);
            }
        }else{
            tv_msg.setVisibility(View.VISIBLE);
            rv_seminarbook.setVisibility(View.GONE);
        }
    }

    public void Yourbooklist() {
        final ProgressDialog mProgressDialog = new ProgressDialog(Bookinglist_activity.this);
        mProgressDialog.setMessage("Loading.....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, AppUrl.URL_SEMINARBOOKING_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e(TAG, "Response: " + response);

                    JSONObject jsonObject = new JSONObject(response);
                    SeminarBooklistObject yourBookObject = new SeminarBooklistObject();
                    try {
                        yourBookObject.setStatus_code(jsonObject.getInt(Jsonkey.statusCode));

                    } catch (JSONException e) {
                        yourBookObject.setStatus_code(0);
                    }

                    yourBookObject.setMessage(jsonObject.optString(Jsonkey.message));
                    if (yourBookObject.getStatus_code() == 1) {

                        try {
                            yourBookObject.setHomeObjects(jsonObject.getJSONArray(Jsonkey.KEY_LIST));
                        } catch (JSONException e) {
                            yourBookObject.setHomeObjects(null);
                        }

                        booklisting_items = yourBookObject.getHomelist();


                        Log.e("size of deals", "" + booklisting_items.size());
                        // set location in list
                        setListing();
                    } else if (yourBookObject.getStatus_code() == 0) {
                        mProgressDialog.dismiss();
                        tv_msg.setVisibility(View.VISIBLE);
                        rv_seminarbook.setVisibility(View.GONE);
                    } else {
                        mProgressDialog.dismiss();
                        tv_msg.setVisibility(View.VISIBLE);
                        rv_seminarbook.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    mProgressDialog.dismiss();
                    tv_msg.setVisibility(View.VISIBLE);
                    rv_seminarbook.setVisibility(View.GONE);
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
                params.put(ParamsKey.KEY_SEMINAR_ID,seminar_id);
                params.put(ParamsKey.KEY_USERID, mPreferenceSettings.getUserId());
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
        CommonUtils.showAlertWithNegativeButton(Bookinglist_activity.this, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (CommonUtils.isNetworkAvailable())
                    Yourbooklist();
                else
                    CommonUtils.showToast(getResources().getString(R.string.network3));
            }
        });
    }
}