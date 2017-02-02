package creadigol.com.Meetto;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Utils.AppUrl;
import Utils.CommonUtils;
import Utils.ParamsKey;
import Utils.PreferenceSettings;

/**
 * Created by Creadigol on 12-09-2016.
 */
public class Booking_Activity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {


    TextView bookid, type, organization, seat;
    EditText totalseat, message, fromdate, todate;
    String seminar_id, no1, no2, s_total_seat, s_message, s_fromdate, s_todate, semibnar_pic, Random_number, fromDate, toDate,host_name;
    private final String TAG = Booking_Activity.class.getSimpleName();
    String bookseat;
    String Flag;
    PreferenceSettings mPreferenceSettings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();
        if (mPreferenceSettings.getLUNGAUGE()) {
            MeettoApplication.language("ja");
        } else {
            MeettoApplication.language("en");
        }
        setContentView(R.layout.activity_booking);


        Button book_now = (Button) findViewById(R.id.btn_booknow);
        book_now.setOnClickListener(this);
        Toolbar();

        TextView seminarname = (TextView) findViewById(R.id.txt_bookname);
        TextView username = (TextView) findViewById(R.id.txt_username);
        totalseat = (EditText) findViewById(R.id.edt_totalseat);
        message = (EditText) findViewById(R.id.et_msg);
        fromdate = (EditText) findViewById(R.id.ed_fromdate);
        todate = (EditText) findViewById(R.id.ed_todate);
        bookid = (TextView) findViewById(R.id.bookingno);
        type = (TextView) findViewById(R.id.txt_type);
        organization = (TextView) findViewById(R.id.txt_organization);
        seat = (TextView) findViewById(R.id.availableseat);

        fromdate.setOnClickListener(this);
        todate.setOnClickListener(this);
        Intent intent = getIntent();
        seminar_id = intent.getStringExtra("seminar_id");
        semibnar_pic = intent.getStringExtra("seminar_pic");
        fromDate = intent.getStringExtra("fromdate");
        toDate = intent.getStringExtra("todate");
        host_name = intent.getStringExtra("host_name");
        seminarname.setText(CommonUtils.getCapitalize(intent.getStringExtra("seminar_name")));
        username.setText(CommonUtils.getCapitalize(host_name));

        String availableSeats = intent.getStringExtra("seat");
        seat.setText(availableSeats + " " + getResources().getString(R.string.availableseat));
        bookseat = intent.getStringExtra("seat");

        LinearLayout ll_organization = (LinearLayout) findViewById(R.id.ll_organization);
        LinearLayout ll_seminar_type = (LinearLayout) findViewById(R.id.ll_seminar_type);
        String seminar_type, organizations;
        seminar_type = intent.getStringExtra("seminar_type");
        organizations = intent.getStringExtra("organization");
        if (!seminar_type.equals("")) {
            type.setText(CommonUtils.getCapitalize(seminar_type));
        } else {
            ll_seminar_type.setVisibility(View.GONE);
        }
        if (!organizations.equals("")) {
            organization.setText(CommonUtils.getCapitalize(organizations));
        } else {
            ll_organization.setVisibility(View.GONE);
        }

        makeid();

        totalseat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((totalseat.getText().toString() != null && seat.getText().toString().length() > 0)) {
                    if (Integer.parseInt(bookseat.toString()) > 0 && (totalseat.getText().toString().length()) > 0) {
                        if ((Integer.parseInt(bookseat.toString()) - Integer.parseInt(totalseat.getText().toString().trim())) >= 0) {
                            seat.setText(String.valueOf(Integer.parseInt(bookseat.toString()) - ((Integer.parseInt(totalseat.getText().toString())))));
                        } else {
                            Toast.makeText(Booking_Activity.this, "Do not book more then available seats", Toast.LENGTH_SHORT).show();
                            totalseat.setText("");
                        }
                    } else {
                        seat.setText(bookseat);
                    }

                } else {
                    Toast.makeText(Booking_Activity.this, "No seat available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public DisplayImageOptions getDisplayImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.profile_icon)
                .showImageForEmptyUri(R.drawable.profile_icon)
                .showImageOnFail(R.drawable.profile_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(100))
                .build();
        return options;
    }

    public void makeid() {
        no1 = "";
        String possible1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < 2; i++)
            no1 += possible1.charAt((int) Math.floor(Math.random() * possible1.length()));
        no2 = "";
        String possible = "0123456789";
        for (int i = 0; i < 7; i++)
            no2 += possible.charAt((int) Math.floor(Math.random() * possible.length()));
        Random_number = no1 + no2;
        bookid.setText(Random_number);
    }

    public void Toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_booking);
        setSupportActionBar(toolbar);
        TextView tvCatTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_name);
        tvCatTitle.setText(R.string.booking_toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void validation() {
        s_total_seat = totalseat.getText().toString().trim();
        s_message = message.getText().toString().trim();
        s_fromdate = fromdate.getText().toString().trim();
        s_todate = todate.getText().toString().trim();

        if (s_total_seat.equals("")) {
            totalseat.setError(getResources().getString(R.string.booking_seat_error));
        }
        if (s_fromdate.equals("")) {
            fromdate.setError(getResources().getString(R.string.booking_fromdate_error));
        }
        if (s_todate.equals("")) {
            todate.setError(getResources().getString(R.string.booking_todate_error));
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date datefrom = sdf.parse(s_fromdate);
            Date dateon = sdf.parse(s_todate);
            if (datefrom.equals(dateon)) {
                Log.e("date", "date are same");
                reqBooking();
            }
            if (datefrom.after(dateon)) {
                Toast.makeText(Booking_Activity.this, getResources().getString(R.string.booking_date_error), Toast.LENGTH_LONG).show();
            }

            if (datefrom.before(dateon)) {
                Log.e("date3", "datefrom befor dateon");
                reqBooking();
            }

        } catch (ParseException ex) {
            Log.e("tag", "parse exception");
        }


    }

    public void reqBooking() {
        final String url = AppUrl.URL_BOOKING;
        final ProgressDialog mProgressDialog = new ProgressDialog(Booking_Activity.this);
        mProgressDialog.setMessage(getResources().getString(R.string.booking_booking_req));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response.toString());
                mProgressDialog.dismiss();

                try {
                    JSONObject responseObj = new JSONObject(response);

                    int status_code = responseObj.getInt("status_code");
                    String msg = responseObj.getString("message");

                    Log.e(TAG, "Response: " + response);
                    if (status_code == 1) {

                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        finish();

                    } else if (status_code == 0) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
                showTryAgainAlert("" + getResources().getString(R.string.network1) + "", getResources().getString(R.string.network2) + "");


            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_SEMINAR_ID, seminar_id);
                params.put(ParamsKey.KEY_USERID, mPreferenceSettings.getUserId());
                params.put(ParamsKey.KEY_BOOKING_NO, Random_number);
                params.put(ParamsKey.KEY_TOTAL_SEAT, s_total_seat.trim());

                String givenDateFromdate = s_fromdate;
                String givenDateTodate = s_todate;
                long timeInMilliseconds = 0;
                long timeInMilliseconds2 = 0;
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date mDate = sdf.parse(givenDateFromdate);
                    Date mDate2 = sdf.parse(givenDateTodate);
                    timeInMilliseconds = mDate.getTime();
                    timeInMilliseconds2 = mDate2.getTime();
                    System.out.println("Date in milli :: " + timeInMilliseconds);
                    System.out.println("Date in milli :: " + timeInMilliseconds2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                params.put(ParamsKey.KEY_FROM_DATE, String.valueOf(timeInMilliseconds));
                params.put(ParamsKey.KEY_TO_DATE, String.valueOf(timeInMilliseconds2));
                params.put(ParamsKey.KEY_MESSAGE, s_message);

                if (mPreferenceSettings.getLUNGAUGE()) {
                    params.put(ParamsKey.KEY_USER_LANG, "ja");
                }
                if (mPreferenceSettings.getLUNGAUGE() == false) {
                    params.put(ParamsKey.KEY_USER_LANG, "en");

                }
                Log.e("Booking", "params: " + params.toString());

                return params;
            }

        };


        MeettoApplication.getInstance().addToRequestQueue(strReq, TAG);
    }

    public void showTryAgainAlert(String title, String message) {
        CommonUtils.showAlertWithNegativeButton(Booking_Activity.this, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (CommonUtils.isNetworkAvailable())
                    reqBooking();
                else
                    CommonUtils.showToast(getResources().getString(R.string.network1));
            }
        });
    }


    public void showDatePicker() {

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendarfromDate = Calendar.getInstance();
        Calendar calendarToDate = Calendar.getInstance();
        calendarfromDate.setTimeInMillis(Long.parseLong(fromDate));
        calendarToDate.setTimeInMillis(Long.parseLong(toDate));

        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(this,
                now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        dpd.setThemeDark(false);
        dpd.vibrate(true);
        dpd.setMinDate(calendarfromDate);
        dpd.setMaxDate(calendarToDate);
        dpd.show(Booking_Activity.this.getFragmentManager(), "Datepickerdialog");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_booknow:
                validation();
                break;
            case R.id.ed_fromdate:
                Flag = "1";
                showDatePicker();
                break;
            case R.id.ed_todate:
                Flag = "2";
                showDatePicker();
                break;

        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = +dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

        if (Flag.equalsIgnoreCase("1")) {
            fromdate.setText(date.trim());
            s_fromdate = fromdate.getText().toString();
        } else if (Flag.equalsIgnoreCase("2")) {
            todate.setText(date.trim());
            s_todate = todate.getText().toString();
        }

    }
}
