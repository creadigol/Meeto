package creadigol.com.Meetto;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.YourBooking_Adapter;
import Models.YourBookingObject;
import Models.YourBookinglist_item;
import Utils.AppUrl;
import Utils.CommonUtils;
import Utils.Jsonkey;
import Utils.ParamsKey;
import Utils.PreferenceSettings;

/**
 * Created by Creadigol on 03-09-2016.
 */
public class YourBooking_Activity extends AppCompatActivity {

    ArrayList<YourBookinglist_item> yourBookinglistItems;
    RecyclerView rv_bookinglist;
    TextView tv_bookmsg;
    String seminar_id, seminar_name, booking_date, booking_seat, booking_no, download_id, booking_id;
    YourBooking_Adapter booking_adapter;
    private final String TAG = YourBooking_Activity.class.getSimpleName();
    PreferenceSettings mPreferenceSettings;
    File folder, file;
    String download_file_url;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();
        if (mPreferenceSettings.getLUNGAUGE()) {
            MeettoApplication.language("ja");
        } else {
            MeettoApplication.language("en");
        }
        setContentView(R.layout.activity_yourbooking);
        tv_bookmsg = (TextView) findViewById(R.id.tv_bookmsg);
        rv_bookinglist = (RecyclerView) findViewById(R.id.rvbooking_list);
        Toolbar();
        YourBokking();
        String extStorageDirectory = Environment.getExternalStorageDirectory()
                .toString();
        folder = new File(extStorageDirectory, "pdf");
        folder.mkdir();
        file = new File(folder, "Read.pdf");
        try {
            file.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void Toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_yourbooking);
        setSupportActionBar(toolbar);
        TextView tvCatTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_name);
        tvCatTitle.setText(R.string.yourbooking_toolbar);
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


    View.OnClickListener onclickdownload = (new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int position = (int) v.getTag();
            YourBookinglist_item bookinglistItem = yourBookinglistItems.get(position);
            download_id = bookinglistItem.getSeminar_id();
            seminar_name = bookinglistItem.getSeminar_name();
            booking_id = bookinglistItem.getBooking_id();
            booking_date = CommonUtils.getFormatedDate(Long.parseLong((bookinglistItem.getDate())), "yyyy-MM-dd hh:mm");
            booking_seat = bookinglistItem.getBook_seat();
            booking_no = bookinglistItem.getBooking_no();
            try {
                EditProfile();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    });
    View.OnClickListener onClickBokking = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            YourBookinglist_item bookinglistItem = yourBookinglistItems.get(position);
            Intent I = new Intent(getApplicationContext(), SeminarDetail_Activity.class);
            I.putExtra(SeminarDetail_Activity.EXTRA_KEY_SEMINAR_ID, bookinglistItem.getSeminar_id());
            I.putExtra("seminar_name", bookinglistItem.getSeminar_name());
            startActivity(I);
            finish();
        }
    };
    View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            int position = (int) v.getTag();
            YourBookinglist_item bookinglistItem = yourBookinglistItems.get(position);
            seminar_id = bookinglistItem.getSeminar_id();
            booking_id = bookinglistItem.getBooking_id();
//            dialogBox();
            return false;
        }
    };

    void setbookinglist() {
        if (yourBookinglistItems.size() > 0 || yourBookinglistItems != null) {
            if (booking_adapter == null) {
                rv_bookinglist.setHasFixedSize(true);
                rv_bookinglist.setItemAnimator(new DefaultItemAnimator());
                rv_bookinglist.setLayoutManager(new LinearLayoutManager(this));
                booking_adapter = new YourBooking_Adapter(this, yourBookinglistItems, onClickBokking, onclickdownload, onLongClickListener);
                rv_bookinglist.setAdapter(booking_adapter);
            } else {
                booking_adapter.modifyDataSet(yourBookinglistItems);
            }
        } else {
            tv_bookmsg.setVisibility(View.VISIBLE);
            rv_bookinglist.setVisibility(View.GONE);
        }
    }

    public void YourBokking() {
        final ProgressDialog mProgressDialog = new ProgressDialog(YourBooking_Activity.this);
        mProgressDialog.setMessage("Loading.....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, AppUrl.URL_USER_BOOKING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e(TAG, "Response: " + response);

                    JSONObject jsonObject = new JSONObject(response);
                    YourBookingObject yourBookingObject = new YourBookingObject();
                    try {
                        yourBookingObject.setStatus_code(jsonObject.getInt(Jsonkey.statusCode));
                    } catch (JSONException e) {
                        yourBookingObject.setStatus_code(0);
                    }

                    yourBookingObject.setMessage(jsonObject.optString(Jsonkey.message));

                    if (yourBookingObject.getStatus_code() == 1) {

                        try {
                            yourBookingObject.setHomeObjects(jsonObject.getJSONArray(Jsonkey.yourbooking_key));
                        } catch (JSONException e) {
                            yourBookingObject.setHomeObjects(null);
                        }

                        yourBookinglistItems = yourBookingObject.getHomelist();


                        Log.e("size of deals", "" + yourBookinglistItems.size());
                        // set location in list
                        setbookinglist();
                    } else if (yourBookingObject.getStatus_code() == 0) {
                        mProgressDialog.dismiss();
                        tv_bookmsg.setVisibility(View.VISIBLE);
                        rv_bookinglist.setVisibility(View.GONE);
                    } else {
                        mProgressDialog.dismiss();
                        tv_bookmsg.setVisibility(View.VISIBLE);
                        rv_bookinglist.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    mProgressDialog.dismiss();
                    tv_bookmsg.setVisibility(View.VISIBLE);
                    rv_bookinglist.setVisibility(View.GONE);
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
//                params.put("uid", mPreferenceSettings.getUserId());
                params.put(ParamsKey.KEY_USERID, mPreferenceSettings.getUserId());
                if (mPreferenceSettings.getLUNGAUGE()) {
                    params.put(ParamsKey.KEY_USER_LANG, "ja");
                }
                if (mPreferenceSettings.getLUNGAUGE() == false) {
                    params.put(ParamsKey.KEY_USER_LANG, "en");

                }
                Log.e(TAG, "reqSearchLocationData params: " + params.toString());

                return params;
            }
        };

        MeettoApplication.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
    }

    public void showTryAgainAlert(String title, String message) {
        CommonUtils.showAlertWithNegativeButton(YourBooking_Activity.this, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (CommonUtils.isNetworkAvailable())
                    YourBokking();
                else
                    CommonUtils.showToast(getResources().getString(R.string.network3));
            }
        });
    }


    public void EditProfile() throws ParseException {
        dialog = new Dialog(YourBooking_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_download);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final TextView db_seminar_name = (TextView) dialog.findViewById(R.id.db_seminar_name);
        final TextView db_user_name = (TextView) dialog.findViewById(R.id.db_username);
        final TextView db_bookingdate = (TextView) dialog.findViewById(R.id.db_bookingdate);
        final TextView db_bookingseat = (TextView) dialog.findViewById(R.id.db_bookingseat);
        final TextView db_bookingid = (TextView) dialog.findViewById(R.id.db_bookingid);

        db_seminar_name.setText(seminar_name);
        db_user_name.setText(mPreferenceSettings.getUSER_FIRSTNAME());
        db_bookingdate.setText(booking_date);
        db_bookingseat.setText(booking_seat);
        db_bookingid.setText(booking_no);

        Button mButtonCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button dialogButton = (Button) dialog.findViewById(R.id.btndownload);
        dialog.getWindow().getAttributes().windowAnimations =
                R.style.dialog_animation;
        // if button is clicked, close the custom dialog

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(YourBooking_Activity.this, "Download success", Toast.LENGTH_SHORT).show();
                reqDowbload();
            }
        });

        dialog.show();
    }

    //  }
    public void reqDowbload() {
        final ProgressDialog mProgress = new ProgressDialog(YourBooking_Activity.this);
        mProgress.setMessage("Downloading.....");
        mProgress.setCancelable(false);
        mProgress.show();

        final String url = AppUrl.URL_DOWNLOAD;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("list", response.toString());
                mProgress.dismiss();
                try {
                    JSONObject responseObj = new JSONObject(response);

                    int status_code = responseObj.getInt("status_code");
                    String msg = responseObj.getString("message");

                    Log.e("list", "Response: " + response);
                    if (status_code == 1) {
                        mProgress.dismiss();
                        download_file_url = responseObj.getString("pdf_url");
                        Log.e("url", "" + download_file_url);
                        dialog.dismiss();
                        String url = download_file_url;
                        new DownloadFileAsync().execute(url);
                    } else if (status_code == 0) {
                        mProgress.dismiss();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("Error_in", "else");
                        mProgress.dismiss();
                    }

                } catch (JSONException e) {
                    Log.e("Error_in", "catch");
                    mProgress.dismiss();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.dismiss();
                Log.e("Booking", "Error Response: " + error.getMessage());
                Log.e("Error_in", "onErrorResponse");
                showTryAgainAlert("" + getResources().getString(R.string.network1) + "", getResources().getString(R.string.network2) + "");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_SEMINAR_ID, download_id);
                params.put(ParamsKey.KEY_USERID, mPreferenceSettings.getUserId());
                params.put(ParamsKey.KEY_BOOKING_ID, booking_id);
                Log.e("Wishlist", "params: " + params.toString());

                return params;
            }

        };
        MeettoApplication.getInstance().addToRequestQueue(strReq, "call");
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Downloading file..");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;

            try {
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();

                String PATH = Environment.getExternalStorageDirectory() + "/meeto/";
                File file = new File(PATH);
                file.mkdirs();
                File outputFile = new File(file, seminar_name + ".pdf");
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = conexion.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;

                while ((count = is.read(buffer)) != -1) {
                    len1 += count;
                    publishProgress("" + (int) ((len1 * 100) / lenghtOfFile));
                    fos.write(buffer, 0, count);
                }

                fos.close();
                is.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        protected void onProgressUpdate(String... progress) {
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            Toast.makeText(YourBooking_Activity.this, getResources().getString(R.string.downloadmsg), Toast.LENGTH_LONG).show();

        }
    }
}
