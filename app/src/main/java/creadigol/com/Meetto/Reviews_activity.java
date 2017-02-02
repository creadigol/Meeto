package creadigol.com.Meetto;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

import Fragments.Review_Fragment;
import Models.Review_object_item;
import Utils.AppUrl;
import Utils.CommonUtils;
import Utils.Jsonkey;
import Utils.ParamsKey;
import Utils.PreferenceSettings;

/**
 * Created by Creadigol on 15-10-2016.
 */
public class Reviews_activity extends AppCompatActivity {

    private static final String TAG = Reviews_activity.class.getSimpleName();
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
        setContentView(R.layout.activity_reviewlist);
        Toolbar();
        Yourlist();
    }

    public void Toolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_mytag);
        setSupportActionBar(toolbar);
        TextView tvCatTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_name);
        tvCatTitle.setText(getResources().getString(R.string.review_toolbar));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void setData(Review_object_item data) {

        Log.e(TAG, "setData");
        if (data.getStatus_code() == 1) {
            setUpViewPager(data);
            Log.e(TAG, "setupViewPager Done");
        } else if (data.getStatus_code() == 0) {
            Log.e(TAG, "setData elsse");
            TextView taghistory = (TextView) findViewById(R.id.taghistorymsg);
            taghistory.setVisibility(View.VISIBLE);
            Log.e("Error", "");
        }
    }


    public void setUpViewPager(Review_object_item Review_object_item){

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        SampleFragmentPagerAdapter sampleFragmentPagerAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                Reviews_activity.this);
//to set tabview heading
        sampleFragmentPagerAdapter.add(Review_Fragment.newInstance(Review_object_item, Review_Fragment.TAB_TYPE.YOURREVIEW), getResources().getString(R.string.Your_Review));
        sampleFragmentPagerAdapter.add(Review_Fragment.newInstance(Review_object_item, Review_Fragment.TAB_TYPE.USERRIVIEW), getString(R.string.user_Review));

        viewPager.setAdapter(sampleFragmentPagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        ArrayList<String> tabTitles = new ArrayList<String>();
        private Context context;

        public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        public void add(Fragment fragment, String title){
            fragments.add(fragment);
            tabTitles.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles.get(position);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    public void Yourlist() {
        final ProgressDialog mProgressDialog = new ProgressDialog(Reviews_activity.this);
        mProgressDialog.setMessage("Loading.....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, AppUrl.URL_GETREVIEW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e(TAG, "Response: " + response);

                    JSONObject jsonObject = new JSONObject(response);
                    Review_object_item review_object_item = new Review_object_item();
                    try {
                        review_object_item.setStatus_code(jsonObject.getInt(Jsonkey.statusCode));
                    } catch (JSONException e) {
                        review_object_item.setStatus_code(0);
                    }

                    review_object_item.setMessage(jsonObject.optString(Jsonkey.message));

                    if (review_object_item.getStatus_code() == 1) {

                        try {
                            review_object_item.setReview(jsonObject.getJSONArray(Jsonkey.Review));
                        } catch (JSONException e) {
                            review_object_item.setReview(null);
                        }
                        Log.e("reviewsize",""+review_object_item.getreviewlist().size());
//                        review_itemses.add(review_object_item);
                        setUpViewPager(review_object_item);

                        // set location in list
                    } else if (review_object_item.getStatus_code() == 0) {
                        setData(review_object_item);
                        Toast.makeText(Reviews_activity.this,""+jsonObject.getString(Jsonkey.message),Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();
                       } else {
                        mProgressDialog.dismiss();
                       }
                } catch (JSONException e) {
                    mProgressDialog.dismiss();
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
                params.put(ParamsKey.KEY_USERID,mPreferenceSettings.getUserId());
//                params.put(ParamsKey.KEY_USERID,"15");
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
        CommonUtils.showAlertWithNegativeButton(Reviews_activity.this, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (CommonUtils.isNetworkAvailable())
                    Yourlist();
                else
                    CommonUtils.showToast(getResources().getString(R.string.network3));
            }
        });
    }
}
