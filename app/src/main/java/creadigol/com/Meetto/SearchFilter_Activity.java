package creadigol.com.Meetto;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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

import Adapters.UserAttendess_Adapter;
import Adapters.Userindustry_Adapter;
import Adapters.Userseminartype_Adapter;
import Models.User_data_Object;
import Models.User_data_item;
import Utils.AppUrl;
import Utils.CommonUtils;
import Utils.Jsonkey;
import Utils.ParamsKey;
import Utils.PreferenceSettings;

/**
 * Created by Creadigol on 07-09-2016.
 */
public class SearchFilter_Activity extends AppCompatActivity implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener, View.OnClickListener {
    TextView fromdate, todate;
    String Flag, s_fromdate = "", s_todate = "", cityid, name;
    String seminarpurpose = "", seminartype = "", seminar_industry = "";

    ArrayList<User_data_item> userDataObjects;
    ArrayList<User_data_item> userseminartype;
    ArrayList<User_data_item> userindustry;

    UserAttendess_Adapter userAttendess_adapter;
    Userseminartype_Adapter userseminartype_adapter;
    Userindustry_Adapter userindustry_adapter;


    private static final String TAG = SearchFilter_Activity.class.getSimpleName();
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
        setContentView(R.layout.activity_searchfilter);

        fromdate = (TextView) findViewById(R.id.fromdate);
        todate = (TextView) findViewById(R.id.todate);
        Button find = (Button) findViewById(R.id.btnfind);
        find.setOnClickListener(this);
        fromdate.setOnClickListener(this);
        todate.setOnClickListener(this);
        Intent intent = getIntent();
        cityid = intent.getStringExtra("cityid");
        name = intent.getStringExtra("cityname");
        Toolbar();

        if (!MeettoApplication.fromdate.equalsIgnoreCase("")) {
            fromdate.setText(MeettoApplication.fromdate);
        }
        if (!MeettoApplication.todate.equalsIgnoreCase("")) {
            todate.setText(MeettoApplication.todate);
        }
        User_data();

    }

    public void Toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        TextView tvCatTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_name);
        tvCatTitle.setText(R.string.filter_toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                MeettoApplication.attendess.clear();
                MeettoApplication.industry.clear();
                MeettoApplication.seminattype.clear();
                finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fromdate:
                Flag = "1";
                showDatePicker();
                break;
            case R.id.todate:
                Flag = "2";
                showDatePicker();
                break;
            case R.id.btnfind:

                if (!s_fromdate.equalsIgnoreCase("")) {
                    MeettoApplication.fromdate = s_fromdate;
                }
                if (!s_todate.equalsIgnoreCase("")) {
                    MeettoApplication.todate = s_todate;
                }

                if (MeettoApplication.attendess != null && MeettoApplication.attendess.size() > 0) {
                    for (int i = 0; i < MeettoApplication.attendess.size(); i++) {
                        seminarpurpose = seminarpurpose + MeettoApplication.attendess.get(i) + ",";
                    }
                }
                if (MeettoApplication.seminattype != null && MeettoApplication.seminattype.size() > 0) {
                    for (int i = 0; i < MeettoApplication.seminattype.size(); i++) {
                        seminartype = seminartype + MeettoApplication.seminattype.get(i) + ",";
                    }
                }
                if (MeettoApplication.industry != null && MeettoApplication.industry.size() > 0) {
                    for (int i = 0; i < MeettoApplication.industry.size(); i++) {
                        seminar_industry = seminar_industry + MeettoApplication.industry.get(i) + ",";
                        Log.e("fac", "" + seminar_industry);
                    }
                }
                mPreferenceSettings.setfilter(true);
                Intent i = new Intent(SearchFilter_Activity.this, SeminarList_Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("type", "FILTER");
                i.putExtra("cityname", name);
                i.putExtra("cityid", cityid);
                i.putExtra("fromdate", MeettoApplication.fromdate);
                i.putExtra("todate", MeettoApplication.todate);
                i.putExtra("purpose", seminarpurpose);
                i.putExtra("property", seminartype);
                i.putExtra("industry", seminar_industry);
                startActivity(i);
                break;
        }
    }

    public void showDatePicker() {
        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(this,
                now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        dpd.setThemeDark(false);
        dpd.vibrate(true);
        dpd.show(SearchFilter_Activity.this.getFragmentManager(), "Datepickerdialog");
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

    void setattendess() {
        if (userDataObjects.size() > 0 || userDataObjects != null) {
            if (userAttendess_adapter == null) {
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_attendess);

                recyclerView.setHasFixedSize(true);
                //Set RecyclerView type according to intent value
                GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchFilter_Activity.this, 2);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(gridLayoutManager);
                userAttendess_adapter = new UserAttendess_Adapter(this, userDataObjects);
                recyclerView.setAdapter(userAttendess_adapter);
            } else {
                userAttendess_adapter.modifyDataSet(userDataObjects);
            }
        }
    }

    void setSeminattype() {
        if (userseminartype.size() > 0 || userseminartype != null) {
            if (userseminartype_adapter == null) {
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_seminattype);

                recyclerView.setHasFixedSize(true);
                //Set RecyclerView type according to intent value
                GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchFilter_Activity.this, 2);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(gridLayoutManager);
                userseminartype_adapter = new Userseminartype_Adapter(this, userseminartype);
                recyclerView.setAdapter(userseminartype_adapter);
            } else {
                userseminartype_adapter.modifyDataSet(userseminartype);
            }
        }
    }

    void setIndustry() {
        if (userindustry.size() > 0 || userindustry != null) {
            if (userindustry_adapter == null) {
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_industry);

                recyclerView.setHasFixedSize(true);
                //Set RecyclerView type according to intent value
                GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchFilter_Activity.this, 2);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(gridLayoutManager);
                userindustry_adapter = new Userindustry_Adapter(this, userindustry);
                recyclerView.setAdapter(userindustry_adapter);
            } else {
                userindustry_adapter.modifyDataSet(userindustry);
            }
        }
    }

    public void User_data() {
        final ProgressDialog mProgressDialog = new ProgressDialog(SearchFilter_Activity.this);
        mProgressDialog.setMessage("Loading.....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, AppUrl.URL_USER_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e(TAG, "Response: " + response);

                    JSONObject jsonObject = new JSONObject(response);
                    User_data_Object user_data_object = new User_data_Object();
                    try {
                        user_data_object.setStatus_code(jsonObject.getInt(Jsonkey.statusCode));
                    } catch (JSONException e) {
                        user_data_object.setStatus_code(0);
                    }

                    user_data_object.setMessage(jsonObject.optString(Jsonkey.message));

                    try {
                        user_data_object.setSemtype(jsonObject.getJSONArray(Jsonkey.seminartype));
                    } catch (JSONException e) {
                        user_data_object.setSemtype(null);
                    }
                    try {
                        user_data_object.setAttendess(jsonObject.getJSONArray(Jsonkey.purpose));
                    } catch (JSONException e) {
                        user_data_object.setSemtype(null);
                    }

                    try {
                        user_data_object.setgetIndustry(jsonObject.getJSONArray(Jsonkey.industrys));
                    } catch (JSONException e) {
                        user_data_object.setSemtype(null);
                    }
                    userseminartype = user_data_object.getSemtype();
                    userDataObjects = user_data_object.getAttendess();
                    userindustry = user_data_object.getIndustry();
                    setattendess();
                    setSeminattype();
                    setIndustry();

                    mProgressDialog.dismiss();
                } catch (JSONException e) {
                    mProgressDialog.dismiss();
                }
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
        CommonUtils.showAlertWithNegativeButton(SearchFilter_Activity.this, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                if (CommonUtils.isNetworkAvailable())
                    User_data();
                else
                    CommonUtils.showToast(getResources().getString(R.string.network3));

            }
        });
    }

}
