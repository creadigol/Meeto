package creadigol.com.Meetto;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.Locale;

import Helper.LocationTracker;
import Models.UserObject;
import Utils.PreferenceSettings;

/**
 * Created by Ashfaq on 7/15/2016.
 */

public class MeettoApplication extends Application {

    private static MeettoApplication mInstance;
    private static ImageLoader mImageLoader;
    private static Context mContext;
    private static LocationTracker locationTracker;
    private PreferenceSettings mPreferenceSettings;
    private RequestQueue mRequestQueue;
    private UserObject userObject;
    private int MY_SOCKET_TIMEOUT_MS = 50000;
    public static ArrayList<String> photo=new ArrayList<>();
    public static ArrayList<String> count=new ArrayList<>();
    public static ArrayList<String> facility = new ArrayList<>();
    public static ArrayList<String> attendess= new ArrayList<>();
    public static ArrayList<String> seminattype= new ArrayList<>();
    public static ArrayList<String> industry= new ArrayList<>();
    public static String fromdate="";
    public static String todate="";
    public static final String TAG = MeettoApplication.class
            .getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = getApplicationContext();
        locationTracker = new LocationTracker(mContext);
        mPreferenceSettings = getPreferenceSettings();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    public static synchronized MeettoApplication getInstance() {
        return mInstance;
    }

    public UserObject getUserObject(){
        if(userObject == null)
            userObject = new UserObject();
        return userObject;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public static void language(String languageToLoad){
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        mContext.getResources().updateConfiguration(config, mContext.getResources().getDisplayMetrics());
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setTag(TAG);
        getRequestQueue().add(req);
    }


    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    public LocationTracker getLocationTracker(){

        if(locationTracker == null){
            locationTracker = new LocationTracker(mContext);
        }

        return locationTracker;
    }

    public ImageLoader getImageLoader(){
        if(mImageLoader == null){
            // UNIVERSAL IMAGE LOADER SETUP
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                    mContext)
                    .defaultDisplayImageOptions(getDefaultOptions())
                    .memoryCache(new WeakMemoryCache())
                    .diskCacheSize(100 * 1024 * 1024).build();

            mImageLoader = ImageLoader.getInstance();
            mImageLoader.init(config);
            // END - UNIVERSAL IMAGE LOADER SETUP
        }
        return mImageLoader;
    }

    public DisplayImageOptions getDefaultOptions(){
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        return defaultOptions;
    }

    public PreferenceSettings getPreferenceSettings() {
        if (mPreferenceSettings == null) {
            mPreferenceSettings = new PreferenceSettings(mContext);
        }
        return mPreferenceSettings;
    }

}
