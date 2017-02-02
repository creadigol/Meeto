package App;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.io.File;

import Helper.MyPreferenceManager;
import Utils.PreferenceSettings;

/**
 * Created by Ashfaq on 27-11-2015.
 */
public class MyApplication {

    public static final String TAG = MyApplication.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private static MyApplication mInstance;
    public static Activity smsActivity = null;
    public static PreferenceSettings mPreferenceSettings;
    private static Context mCtx;
    private ImageLoader mImageLoader;
    private File mProfile;
    public static int notifycount=0;
    private MyPreferenceManager pref;

/*
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        ps = new PreferenceSettings(getApplicationContext());
    }*/

    private MyApplication(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
        mPreferenceSettings = getPreferenceSettings();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public File getProfile() {

        File Dire = new File(mCtx.getExternalCacheDir(), "Meetto");
        if (!Dire.exists())
            Dire.mkdir();

        if (mProfile == null) {
            mProfile= new File(mCtx.getExternalCacheDir() + "/Meetto", "Profile.jpg");
        }
        return mProfile;
    }

    public static synchronized MyApplication getInstance(Context context)
    {
        if(mInstance == null)
        {
            mInstance = new MyApplication(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }

        return mRequestQueue;
    }

    public PreferenceSettings getPreferenceSettings() {
        if (mPreferenceSettings == null) {
            mPreferenceSettings = new PreferenceSettings(mCtx);
        }
        return mPreferenceSettings;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public MyPreferenceManager getPrefManager()
    {
        if (pref == null)
        {
            pref = new MyPreferenceManager(mCtx);
        }
        return pref;
    }


}
