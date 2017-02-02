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

import Adapters.Wishlist_Adapter;
import Models.WishlistObject;
import Models.Wishlist_item;
import Utils.AppUrl;
import Utils.CommonUtils;
import Utils.Jsonkey;
import Utils.ParamsKey;
import Utils.PreferenceSettings;

/**
 * Created by Creadigol on 03-09-2016.
 */
public class Wishlist_Activity extends AppCompatActivity {
    ArrayList<Wishlist_item> wishlist_items;
    RecyclerView rv_wishlist;
    TextView tv_wishlistmsg;
    String seminar_id;
    Wishlist_Adapter wishlist_adapter;
    PreferenceSettings mPreferenceSettings;
    private final String TAG = Wishlist_Activity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferenceSettings= MeettoApplication.getInstance().getPreferenceSettings();
        if(mPreferenceSettings.getLUNGAUGE()){
            MeettoApplication.language("ja");
        }else{
            MeettoApplication.language("en");
        }
        setContentView(R.layout.activity_wishlist);
        tv_wishlistmsg = (TextView) findViewById(R.id.tv_wishlistmsg);
        rv_wishlist = (RecyclerView) findViewById(R.id.rv_wishlist);
        Toolbar();
        reqwishlist();


    }

    public void Toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_wishlist);
        setSupportActionBar(toolbar);
        TextView tvCatTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_name);
        tvCatTitle.setText(R.string.wishlist_toolbar);
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
    View.OnClickListener onclickwishlist = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            Wishlist_item  wishlistItem = wishlist_items.get(position);
            Intent I = new Intent(getApplicationContext(), SeminarDetail_Activity.class);
            I.putExtra(SeminarDetail_Activity.EXTRA_KEY_SEMINAR_ID, wishlistItem.getSeminar_id());
            I.putExtra("seminar_name",wishlistItem.getSeminar_name());
            startActivity(I);
            finish();
        }
    };
    View.OnLongClickListener onLongclick=new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            int position=(int) v.getTag();
            Wishlist_item  wishlistItem = wishlist_items.get(position);
            seminar_id=wishlistItem.getSeminar_id();
            dialogBox();
            return false;
        }
    };

    public void dispalywishlist() {
        if (wishlist_items.size() > 0 || wishlist_items != null) {
            if (wishlist_adapter == null) {
                rv_wishlist.setHasFixedSize(true);
                rv_wishlist.setItemAnimator(new DefaultItemAnimator());
                rv_wishlist.setLayoutManager(new LinearLayoutManager(this));
                wishlist_adapter = new Wishlist_Adapter(this, wishlist_items,onclickwishlist,onLongclick);
                rv_wishlist.setAdapter(wishlist_adapter);
            } else {
             wishlist_adapter.modifyDataSet(wishlist_items);
            }
        } else {
            tv_wishlistmsg.setVisibility(View.VISIBLE);
            rv_wishlist.setVisibility(View.GONE);
        }
    }

    public void reqwishlist() {
        final ProgressDialog mProgressDialog = new ProgressDialog(Wishlist_Activity.this);
        mProgressDialog.setMessage("Loading.....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, AppUrl.URL_WISHLIST_VIEW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e(TAG, "Response: " + response);

                    JSONObject jsonObject = new JSONObject(response);
                    WishlistObject wishlistObject = new WishlistObject();
                    try {
                        wishlistObject.setStatus_code(jsonObject.getInt(Jsonkey.statusCode));
                    } catch (JSONException e) {
                        wishlistObject.setStatus_code(0);
                    }

                    wishlistObject.setMessage(jsonObject.optString(Jsonkey.message));

                    if (wishlistObject.getStatus_code() == 1) {

                        try {
                            wishlistObject.setHomeObjects(jsonObject.getJSONArray(Jsonkey.wishlistkey));
                        } catch (JSONException e) {
                            wishlistObject.setHomeObjects(null);
                        }

                        wishlist_items = wishlistObject.getHomelist();


                        Log.e("size of deals", "" + wishlist_items.size());
                        // set location in list
                        dispalywishlist();
                    } else if (wishlistObject.getStatus_code() == 0) {
                        mProgressDialog.dismiss();
                        tv_wishlistmsg.setVisibility(View.VISIBLE);
                        rv_wishlist.setVisibility(View.GONE);
                    } else {
                        mProgressDialog.dismiss();
                        tv_wishlistmsg.setVisibility(View.VISIBLE);
                        rv_wishlist.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    mProgressDialog.dismiss();
                    tv_wishlistmsg.setVisibility(View.VISIBLE);
                    rv_wishlist.setVisibility(View.GONE);
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
        CommonUtils.showAlertWithNegativeButton(Wishlist_Activity.this, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (CommonUtils.isNetworkAvailable())
                    reqwishlist();
                else
                    CommonUtils.showToast(getResources().getString(R.string.network3));
            }
        });
    }

    public void dialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Wishlist_Activity.this);

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
        final String url = AppUrl.URL_DELETE_WISHLIST;
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
                        reqwishlist();
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
//                params.put("user_id", "18");

                Log.e("Wishlist", "params: " + params.toString());

                return params;
            }

        };


        MeettoApplication.getInstance().addToRequestQueue(strReq, "call");
    }
}
