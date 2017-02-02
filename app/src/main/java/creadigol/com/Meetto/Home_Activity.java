package creadigol.com.Meetto;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crittercism.app.Crittercism;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.Home_Adapter;
import App.Config;
import App.MyApplication;
import Models.HomeObject;
import Models.Homelist;
import Utils.AppUrl;
import Utils.CommonUtils;
import Utils.Jsonkey;
import Utils.ParamsKey;
import Utils.PreferenceSettings;
import creadigol.com.Meetto.Database.DataBaseHelper;
import creadigol.com.Meetto.gcm.GcmIntentService;


public class Home_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, View.OnClickListener {
    ImageView cityimage, iv_profilepic;
    public static LinearLayout city, searchseminar, topbar;
    ArrayList<HomeObject> homeitemobject;
    private static final String TAG = Home_Activity.class.getSimpleName();
    NavigationView navigationView;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    PreferenceSettings mPreferenceSettings;
    public static boolean isNeedResponse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();
        if (mPreferenceSettings.getLUNGAUGE()) {
            MeettoApplication.language("ja");
        } else {
            MeettoApplication.language("en");
        }
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        //genrate gcm key
        GCM();
//0f7855d109b84b8d8573e08829d1928000555300
        Crittercism.initialize(getApplicationContext(), "0f7855d109b84b8d8573e08829d1928000555300");
        cityimage = (ImageView) findViewById(R.id.citybgimage);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        //Linearlayouts
        searchseminar = (LinearLayout) findViewById(R.id.searchseminar);
        topbar = (LinearLayout) findViewById(R.id.topbarlayout);
        searchseminar.setOnClickListener(this);
        reqHomeData();
        ImageView notification = (ImageView) findViewById(R.id.iv_notification);
        notification.setOnClickListener(this);
        TextView notificationcount = (TextView) findViewById(R.id.notificationCount);
        if (MyApplication.notifycount != 0) {
            notificationcount.setVisibility(View.VISIBLE);
            notificationcount.setText(MyApplication.notifycount);
        }

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


         navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigationView, false);
        LinearLayout ll_profile, ll_login;
        ll_login = (LinearLayout) headerView.findViewById(R.id.ll_login);
        ll_profile = (LinearLayout) headerView.findViewById(R.id.ll_profile);
        Button btn_homelogin = (Button) headerView.findViewById(R.id.btn_homelogin);
        TextView tv_username = (TextView) headerView.findViewById(R.id.tv_UserName);
        TextView tv_email = (TextView) headerView.findViewById(R.id.tv_email);
        iv_profilepic = (ImageView) headerView.findViewById(R.id.home_profilepic);

        tv_username.setText(mPreferenceSettings.getUSER_FIRSTNAME());
        tv_email.setText(mPreferenceSettings.getUserEmail());

        if (MeettoApplication.getInstance().getPreferenceSettings().getIsLogin()) {
            ll_profile.setVisibility(View.VISIBLE);
            ll_login.setVisibility(View.GONE);

        }
        btn_homelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home_Activity.this, Splash_Activity.class);
                i.putExtra(Splash_Activity.EXTRA_KEY, true);
                startActivity(i);
                drawer.closeDrawers();
                finish();
            }
        });
        navigationView.addHeaderView(headerView);
        navigationView.getMenu().getItem(0).setChecked(true);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO open profile
                if (MeettoApplication.getInstance().getPreferenceSettings().getIsLogin()) {
                    Intent i = new Intent(Home_Activity.this, Profile_Activity.class);
                    startActivity(i);
                    drawer.closeDrawers();
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(this);

    }

    public void GCM() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications

                    String token = intent.getStringExtra("token");
                    Log.e("key", "" + token);
                } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                    Toast.makeText(getApplicationContext(), "GCM registration token is stored in server!", Toast.LENGTH_LONG).show();
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    Toast.makeText(getApplicationContext(), "Push notification is received!", Toast.LENGTH_LONG).show();
                }
            }
        };

        if (checkPlayServices()) {
            registerGCM();
        }
    }

    private void registerGCM() {
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }

    private boolean checkPlayServices() {
        //In this Method checking of device is support to google play service or not....

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    public void setProfileImage(String userImageUrl) {

        com.nostra13.universalimageloader.core.ImageLoader imageLoader = MeettoApplication.getInstance().getImageLoader();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.profile_icon)
                .showImageOnFail(R.drawable.profile_icon)
                .showImageOnLoading(R.drawable.profile_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(100))
                .build();
        //download and display image from url
        imageLoader.displayImage(userImageUrl, iv_profilepic, options);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Home) {
            // Handle the camera action
        } else if (id == R.id.nav_listing) {
            if (MeettoApplication.getInstance().getPreferenceSettings().getIsLogin()) {
                Intent i = new Intent(Home_Activity.this, YourListing_Activity.class);
                startActivity(i);
            } else {
                Toast.makeText(this, getResources().getString(R.string.Login_msg), Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_Booking) {
            if (MeettoApplication.getInstance().getPreferenceSettings().getIsLogin()) {
                Intent i = new Intent(Home_Activity.this, YourBooking_Activity.class);
                startActivity(i);
            } else {
                Toast.makeText(this, getResources().getString(R.string.Login_msg), Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_Setting) {
            if (MeettoApplication.getInstance().getPreferenceSettings().getIsLogin()) {
                Intent i = new Intent(Home_Activity.this, Setting_Activity.class);
                startActivity(i);
            } else {
                Toast.makeText(this, getResources().getString(R.string.Login_msg), Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_Wishlist) {
            if (MeettoApplication.getInstance().getPreferenceSettings().getIsLogin()) {
                Intent i = new Intent(Home_Activity.this, Wishlist_Activity.class);
                startActivity(i);
            } else {
                Toast.makeText(this, getResources().getString(R.string.Login_msg), Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_Review) {
            if (MeettoApplication.getInstance().getPreferenceSettings().getIsLogin()) {
                Intent i = new Intent(Home_Activity.this, Reviews_activity.class);
                startActivity(i);
            } else {
                Toast.makeText(this, getResources().getString(R.string.Login_msg), Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_Share) {
            Bitmap bit = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.meeto_logohome);
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Meeto");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Meeto are provide information about the seminar available in yout city");
            startActivity(Intent.createChooser(sharingIntent, "Share Meeto via"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchseminar:
                Intent list = new Intent(Home_Activity.this, Search_activity.class);
                startActivity(list);
                break;
            case R.id.fab:
                if (MeettoApplication.getInstance().getPreferenceSettings().getIsLogin()) {
                    Intent add = new Intent(Home_Activity.this, Add_Seminar_Activity.class);
                    mPreferenceSettings.setEdit(false);
                    startActivity(add);
                } else {
                    Toast.makeText(this, getResources().getString(R.string.Login_msg), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_notification:
                Intent noti = new Intent(Home_Activity.this, Notification_Activity.class);
                startActivity(noti);
                break;
        }
    }

    public void citylist() {
        RecyclerView citylist = (RecyclerView) findViewById(R.id.tvcity_list);

        citylist.setHasFixedSize(true);
        citylist.setItemAnimator(new DefaultItemAnimator());

        //Set RecyclerView type according to intent value
        GridLayoutManager gridLayoutManager = new GridLayoutManager(Home_Activity.this, 2);
        citylist.setLayoutManager(gridLayoutManager);

        Home_Adapter adapter = new Home_Adapter(Home_Activity.this, homeitemobject);
        citylist.setAdapter(adapter);// set adapter on recyclerview
        adapter.notifyDataSetChanged();// Notify the adapter
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPreferenceSettings.getLUNGAUGE()) {
            MeettoApplication.language("ja");
        } else {
            MeettoApplication.language("en");
        }
//Logout process
        navigationView.getMenu().getItem(0).setChecked(true);
        Log.e("In resume", "");
        if (getIntent().getBooleanExtra(Profile_Activity.KEY_EXTRA_LOGOUT, false)) {
            Intent i = new Intent(Home_Activity.this, Splash_Activity.class);
            DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
            dataBaseHelper.Logout();
            startActivity(i);
            mPreferenceSettings.clearSession();
            finish();
        }
        if (isNeedResponse) {
            isNeedResponse = false;
            Intent intent = getIntent();
            overridePendingTransition(0, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
        }


        //setprofile
        if (mPreferenceSettings.getUserPic() != null) {
            setProfileImage(mPreferenceSettings.getUserPic());
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    public void reqHomeData() {
        final ProgressDialog mProgressDialog = new ProgressDialog(Home_Activity.this);
        mProgressDialog.setMessage("Loading.....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, AppUrl.URL_HOME_PAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e(TAG, "Response: " + response);

                    JSONObject jsonObject = new JSONObject(response);
                    Homelist homeObject = new Homelist();
                    try {
                        homeObject.setStatus_code(jsonObject.getInt(Jsonkey.statusCode));
                    } catch (JSONException e) {
                        homeObject.setStatus_code(0);
                    }

                    homeObject.setMessage(jsonObject.optString(Jsonkey.message));

                    if (homeObject.getStatus_code() == 1) {
                        homeObject.setNotificationcount(jsonObject.optInt(Jsonkey.notificationcount));
                        homeObject.setEmailverfy(jsonObject.optString(Jsonkey.emailverfy));
                        homeObject.setSlider(jsonObject.optString(Jsonkey.Slider));
                        MyApplication.notifycount = homeObject.getNotificationcount();
                        MeettoApplication.getInstance().getImageLoader().displayImage(homeObject.getSlider(), cityimage, getDisplayImageOptions());

                        mPreferenceSettings.setEmailverify(homeObject.getEmailverfy());
                        try {
                            homeObject.setHomeObjects(jsonObject.getJSONArray(Jsonkey.homekey));
                        } catch (JSONException e) {
                            homeObject.setHomeObjects(null);
                        }

                        homeitemobject = homeObject.getHomelist();


                        Log.e("size of deals", "" + homeitemobject.size());
                        // set location in list
                        citylist();

                    } else if (homeObject.getStatus_code() == 0) {
                        mProgressDialog.dismiss();
                        showTryAgainAlert("Info", homeObject.getMessage() + " try again!");
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
        CommonUtils.showAlertWithNegativeButton(Home_Activity.this, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (CommonUtils.isNetworkAvailable())
                    reqHomeData();
                else
                    CommonUtils.showToast(getResources().getString(R.string.network3));
            }
        });
    }

    public DisplayImageOptions getDisplayImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.purplecity)
                .showImageOnFail(R.drawable.purplecity)
                .showImageOnLoading(R.drawable.purplecity)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(0))
                .build();
        return options;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MeettoApplication.getInstance().cancelPendingRequests(TAG);
    }
}
