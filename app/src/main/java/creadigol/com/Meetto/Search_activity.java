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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.Search_Adapter;
import Models.SearchObject;
import Models.Searchitem;
import Utils.AppUrl;
import Utils.CommonUtils;
import Utils.Jsonkey;
import Utils.ParamsKey;
import Utils.PreferenceSettings;

/**
 * Created by Creadigol on 17-09-2016.
 */
public class Search_activity extends AppCompatActivity {
    EditText ed_search;
    RecyclerView rv_search;
    ProgressBar pb_search;
    TextView txt_searchmsg,txt_search;
    ArrayList<Searchitem> searchitems;
    Search_Adapter search_adapter;
    PreferenceSettings mpreferenceSettings;
    private final String TAG = Search_activity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mpreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();
        if (mpreferenceSettings.getLUNGAUGE()) {
            MeettoApplication.language("ja");
        } else {
            MeettoApplication.language("en");
        }
        setContentView(R.layout.activity_search);
        ed_search = (EditText) findViewById(R.id.ed_search);
        rv_search = (RecyclerView) findViewById(R.id.rv_search);
        pb_search = (ProgressBar) findViewById(R.id.progress_search);
        txt_searchmsg = (TextView) findViewById(R.id.txt_searchmsg);
        txt_search = (TextView) findViewById(R.id.txt_search);

        ImageView goback = (ImageView) findViewById(R.id.back_imaView);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ed_search.getText().toString().trim().length() == 0) {
                    canclePreviousSearchRequest();
                    pb_search.setVisibility(View.GONE);
                    rv_search.setVisibility(View.GONE);
                    txt_search.setVisibility(View.VISIBLE);

                } else {
                    if (s.toString().trim() != null) {
                        getsearchdata(ed_search.getText().toString().trim());
                    }

                }
            }
        });
    }

    View.OnClickListener onClickData = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            Searchitem searchitem = searchitems.get(position);
            String type = searchitem.getSearch_type();

            if (type.equalsIgnoreCase("seminar")) {
                Intent I = new Intent(getApplicationContext(), SeminarDetail_Activity.class);
                I.putExtra(SeminarDetail_Activity.EXTRA_KEY_SEMINAR_ID, searchitem.getSearch_id());
                startActivity(I);
                finish();
            } else if (type.equalsIgnoreCase("city")) {
                Intent I = new Intent(getApplicationContext(), SeminarList_Activity.class);
                I.putExtra("cityid", searchitem.getSearch_id());
                I.putExtra("cityname", searchitem.getSearch_name());
                startActivity(I);
                finish();
            }
        }
    };

    public void setResultView() {
        if (searchitems != null && searchitems.size() > 0) {
            rv_search.setVisibility(View.VISIBLE);
            txt_searchmsg.setVisibility(View.GONE);
            txt_search.setVisibility(View.GONE);
            setTagOutlateList();
        } else {
            rv_search.setVisibility(View.GONE);
            txt_searchmsg.setVisibility(View.VISIBLE);
            txt_search.setVisibility(View.GONE);

            if (search_adapter != null) {
                search_adapter.modifyDataSet(searchitems);
            }
        }
    }

    void setTagOutlateList() {
        if (search_adapter == null) {
            rv_search.setVisibility(View.VISIBLE);
            txt_searchmsg.setVisibility(View.GONE);
            rv_search.setHasFixedSize(true);
            rv_search.setItemAnimator(new DefaultItemAnimator());
            rv_search.setLayoutManager(new LinearLayoutManager(this));
            search_adapter = new Search_Adapter(this, searchitems, onClickData);
            rv_search.setAdapter(search_adapter);
        } else {
            search_adapter.modifyDataSet(searchitems);
        }
    }


    public void canclePreviousSearchRequest() {
        MeettoApplication.getInstance().cancelPendingRequests(TAG);
    }

    void getsearchdata(final String search) {

        final ProgressDialog mProgressDialog = new ProgressDialog(Search_activity.this);
        mProgressDialog.dismiss();
        pb_search.setVisibility(View.VISIBLE);
        final StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, AppUrl.URL_SEARCH_BY_STRING, new Response.Listener<String>() {
            @Override
            public void onResponse(String Response) {
                mProgressDialog.dismiss();
                Log.e("Place Response", Response);
                pb_search.setVisibility(View.GONE);

                try {
                    JSONObject jsonObj = new JSONObject(Response);
                    SearchObject searchObject = new SearchObject();

                    try {
                        searchObject.setStatus_code(jsonObj.getInt(Jsonkey.statusCode));
                    } catch (JSONException e) {
                        searchObject.setStatus_code(0);

                    }
                    searchObject.setMessage(jsonObj.getString(Jsonkey.message));

                    if (searchObject.getStatus_code() == 1) {

                        try {
                            searchObject.setSearchitems(jsonObj.getJSONArray(Jsonkey.searchkey));
                        } catch (JSONException e) {
                            searchObject.setSearchitems(null);
                        }

                        searchitems = searchObject.getSearchitems();

                        setResultView();

                    } else if (searchObject.getStatus_code() == 0) {
                        try {
                            searchitems.clear();
                            pb_search.setVisibility(View.INVISIBLE);
                            txt_searchmsg.setVisibility(View.VISIBLE);
                            setResultView();

                        } catch (Exception e) {
                            Log.e("Error", "" + e);
                        }
                    } else {

                        pb_search.setVisibility(View.INVISIBLE);
                        Log.e("Error_in", "else");
                    }
                } catch (JSONException e) {
                    pb_search.setVisibility(View.INVISIBLE);
                    Log.e("Error_in", "onErrorResponse");

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error_in", "onErrorResponse");
                pb_search.setVisibility(View.INVISIBLE);
                showTryAgainAlert("" + getResources().getString(R.string.network1) + "", getResources().getString(R.string.network2) + "", search);
                Log.e("Place", "Error Response: " + error.getMessage());
            }
        })


        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_SEARCH, search.toString());
                if (mpreferenceSettings.getLUNGAUGE()) {
                    params.put(ParamsKey.KEY_USER_LANG, "ja");
                }
                if (mpreferenceSettings.getLUNGAUGE() == false) {
                    params.put(ParamsKey.KEY_USER_LANG, "en");

                }
                Log.e("search", "Posting params: " + params.toString());
                return params;

            }
        };


        // Adding request to request queue

        MeettoApplication.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
    }

    public void showTryAgainAlert(String title, String message, final String search) {
        CommonUtils.showAlertWithNegativeButton(Search_activity.this, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (CommonUtils.isNetworkAvailable())
                    getsearchdata(search);
                else
                    CommonUtils.showToast(getResources().getString(R.string.network3));
            }
        });

    }
}
