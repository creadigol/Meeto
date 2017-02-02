package creadigol.com.Meetto;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Models.SeminarBookListitem;
import Utils.AppUrl;
import Utils.CommonUtils;
import Utils.ParamsKey;
import Utils.PreferenceSettings;

/**
 * Created by Creadigol on 08-10-2016.
 */
public class SeminarApprovedCancel_Activity extends AppCompatActivity implements View.OnClickListener {
    Button btn_approved, btn_cancel;
    public static final String EXTRA_BOOK = "offer";
    public static final String SEMINAR_NO = "seminar_no";
    SeminarBookListitem SeminarBookListitem;
    String seminar_id,user_id,username,seminarbooking_no;
    String type;
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
        setContentView(R.layout.activity_seminarapprovedcencal);
        Intent intent = getIntent();
        SeminarBookListitem = (SeminarBookListitem) intent.getSerializableExtra(EXTRA_BOOK);

        Toolbar();
        TextView tv_username = (TextView) findViewById(R.id.username);
        TextView tv_bookingdate = (TextView) findViewById(R.id.bookingdate);
        TextView tv_total_seat = (TextView) findViewById(R.id.totalseat);
        ImageView image_book = (ImageView) findViewById(R.id.image_view);
        seminar_id = SeminarBookListitem.getSeminar_id();
        user_id=SeminarBookListitem.getUser_id();
        username=SeminarBookListitem.getUser_name();
        seminarbooking_no=SeminarBookListitem.getSeminar_boooking_no();
        tv_username.setText(CommonUtils.getCapitalize(username));
        tv_bookingdate.setText(CommonUtils.getCapitalize(SeminarBookListitem.getBooking_date()));
        tv_total_seat.setText(CommonUtils.getCapitalize(SeminarBookListitem.getTotal_seat()));
        MeettoApplication.getInstance().getImageLoader().displayImage(SeminarBookListitem.getUser_image(), image_book, getDisplayImageOptions());


//        Toast.makeText(this,"in"+SeminarBookListitem.getSeminar_boooking_no(),Toast.LENGTH_LONG).show();

        btn_approved = (Button) findViewById(R.id.btn_approved);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_approved.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

    }

    public void Toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_approval);
        setSupportActionBar(toolbar);
        TextView tvCatTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_name);
        tvCatTitle.setText(CommonUtils.getCapitalize(SeminarBookListitem.getSeminar_name()));
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
    public DisplayImageOptions getDisplayImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.nophoto)
                .showImageOnFail(R.drawable.nophoto)
                .showImageOnLoading(R.drawable.nophoto)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(0))
                .build();
        return options;
    }


    public void reqApprovel() {

        final ProgressDialog mProgressDialog = new ProgressDialog(SeminarApprovedCancel_Activity.this);
        mProgressDialog.setMessage("Loading.....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        final String url = AppUrl.URL_ACCEPT_DECLINE;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("list", response.toString());
                mProgressDialog.dismiss();

                try {
                    JSONObject responseObj = new JSONObject(response);

                    int status_code = responseObj.getInt("status_code");
                    String msg = responseObj.getString("message");

                    Log.e("list", "Response: " + response);
                    if (status_code == 1) {

                        Toast.makeText(SeminarApprovedCancel_Activity.this, msg, Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(SeminarApprovedCancel_Activity.this,Bookinglist_activity.class);
                        intent.putExtra("seminar_id",seminar_id);
                        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else if (status_code == 0) {
                        Toast.makeText(SeminarApprovedCancel_Activity.this, msg, Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();
                    } else {
                        mProgressDialog.dismiss();
                        Log.e("Error_in", "else");
                    }

                } catch (JSONException e) {
                    mProgressDialog.dismiss();
                    Log.e("Error_in", "catch");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Booking", "Error Response: " + error.getMessage());
                mProgressDialog.dismiss();
                Log.e("Error_in", "onErrorResponse");
                showTryAgainAlert(""+getResources().getString(R.string.network1)+"", getResources().getString(R.string.network2)+"");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_SEMINAR_ID, seminar_id);
                params.put(ParamsKey.KEY_APPROVAL, type);
                params.put(ParamsKey.KEY_USERID, user_id);
                params.put(ParamsKey.KEY_SEMINARBOOKING_NO, seminarbooking_no);
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

    public void showTryAgainAlert(String title, String message) {
        CommonUtils.showAlertWithNegativeButton(SeminarApprovedCancel_Activity.this, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (CommonUtils.isNetworkAvailable())
                    reqApprovel();
                else
                    CommonUtils.showToast(getResources().getString(R.string.network3));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_approved:
                type="accepted";
                reqApprovel();
                break;

            case R.id.btn_cancel:
                type="declined";
                reqApprovel();
                break;
        }
    }
}
