package creadigol.com.Meetto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import Models.UserObject;
import Utils.AppUrl;
import Utils.CommonUtils;
import Utils.Constants;
import Utils.Jsonkey;
import Utils.ParamsKey;
import Utils.PreferenceSettings;


public class Splash_Activity extends AppCompatActivity {
    Handler handler;
    CallbackManager callbackManager;
    PreferenceSettings preferenceSettings;
    private int RC_SIGN_IN = 100;
    private GoogleApiClient mGoogleApiClient;
    private Button signOutButton;
    String fname,lname;
    ProgressDialog pDialog;
    private final String TAG = Splash_Activity.class.getSimpleName();

    public static final String EXTRA_KEY = "isfromhome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();
        try {
            String lang = Locale.getDefault().getDisplayLanguage();
            if (lang.equalsIgnoreCase("English")) {
                preferenceSettings.setLUNGAUGE(false);
            } else if (lang.equalsIgnoreCase("日本語")) {
                preferenceSettings.setLUNGAUGE(true);
            }
            if (preferenceSettings.getLUNGAUGE()) {
                MeettoApplication.language("ja");
            } else {
                MeettoApplication.language("en");
            }
            Log.e("lang", "" + lang);
        } catch (Exception ex) {

        }

        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String login = intent.getStringExtra("Login");
        boolean isFromHome = intent.getBooleanExtra(EXTRA_KEY, false);
        if (login != null) {
            handler = new Handler();
            handler.postDelayed(runnable, 0000);
        } else if (isFromHome){
            handler = new Handler();
            handler.postDelayed(runnable, 0000);
        } else {
            handler = new Handler();
            handler.postDelayed(runnable, 4000);
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (MeettoApplication.getInstance().getPreferenceSettings().getIsLogin()) {
                Intent i = new Intent(Splash_Activity.this, Home_Activity.class);
                startActivity(i);
                Log.e("login", "true");
                finish();
            } else {

                buttonshow();

                AppEventsLogger.activateApp(Splash_Activity.this);
                FacebookSdk.sdkInitialize(getApplicationContext());
                callbackManager = CallbackManager.Factory.create();


                getHashkey();

                setFacebookLogin();

            }
        }

    };

    public void getHashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("creadigol.com.Meetto",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e(TAG, "KeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
    private void showProgressDialog() {
        if (pDialog== null) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("loading");
            pDialog.setCancelable(false);
            pDialog.setIndeterminate(true);
        }
        pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.hide();
        }
    }

    public void setFacebookLogin() {
        if (!CommonUtils.isNetworkAvailable()) {
            CommonUtils.showToast(Constants.MSG_NO_INTERNET_CONNECTION);
        } else {
            LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
            loginButton.setReadPermissions(Arrays.asList("email", "user_birthday", "user_status"));

            // Callback registration
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
//                    showProgressDialog();
                    AccessToken accessToken = loginResult.getAccessToken();

                    String userId = loginResult.getAccessToken().getUserId();
                    String profileImgUrl = "https://graph.facebook.com/" + userId + "/picture?type=large";

                    Log.e(TAG, "facebook: " + profileImgUrl);

                    MeettoApplication.getInstance().getUserObject().setPhoto(profileImgUrl);
                    MeettoApplication.getInstance().getPreferenceSettings().setUserPic(profileImgUrl);

                    GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {

                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                MeettoApplication.getInstance().getUserObject().setType(Constants.TYPE_LOGIN_FB);
                                MeettoApplication.getInstance().getUserObject().setId_fb(object.getString("id"));
                                MeettoApplication.getInstance().getUserObject().setEmail(object.getString("email"));
                                MeettoApplication.getInstance().getUserObject().setFname(object.getString("name"));
                                MeettoApplication.getInstance().getPreferenceSettings().setLoginType(Constants.TYPE_LOGIN_FB);


                                String[] time = MeettoApplication.getInstance().getUserObject().getFname().split(" ");
                                if (time.length > 1) {
                                    fname = time[0];
                                    lname = time[1];

                                }
                                MeettoApplication.getInstance().getPreferenceSettings().setRegId(object.getString("id"));
                                MeettoApplication.getInstance().getPreferenceSettings().setUserEmail(object.getString("email"));
                                MeettoApplication.getInstance().getPreferenceSettings().setUSER_FIRSTNAME(object.getString("name"));

                                reqUserLogin();

                            } catch (Exception e) {
                                Log.e("Error_in", "catch");
                                e.printStackTrace();
                            }

                        }

                    });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,link,email");
                    Log.e("pare", "" + parameters);
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {
                    // App code
                    CommonUtils.showToast(getResources().getString(R.string.fb_msg));
                }

                @Override
                public void onError(FacebookException exception) {
                    Log.e(TAG, "exception: " + exception);
                    Log.e("Error_in", "onErrorResponse");
                    CommonUtils.showToast(Constants.MSG_LOGIN_FAIL);
                }
            });
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        //  signInButton.setVisibility(View.VISIBLE);
                        signOutButton.setVisibility(View.GONE);
                    }
                });
    }

    public void buttonshow() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_login_options);
        linearLayout.setVisibility(View.VISIBLE);


        Button btn_LoginHome = (Button) findViewById(R.id.btn_LoginHome);
        Button btn_registrationHome = (Button) findViewById(R.id.btn_Registration);
        TextView tv_setlang = (TextView) findViewById(R.id.tv_setlang);
        TextView skip = (TextView) findViewById(R.id.skip);

        btn_LoginHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Splash_Activity.this, Login_Activity.class);
                startActivity(i);
                finish();
            }
        });

        btn_registrationHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MeettoApplication.getInstance().getPreferenceSettings().setLoginType("LOCAL");
                Intent i = new Intent(Splash_Activity.this, Registration_Activity.class);
                startActivity(i);
                finish();
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Splash_Activity.this, Home_Activity.class);
                startActivity(i);
                finish();

            }
        });
        tv_setlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Splash_Activity.this, Language_Selection_activity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void reqUserLogin() {
//        hideProgressDialog();
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, AppUrl.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    UserObject userObject = new UserObject();

                    try {
                        userObject.setStatus_code(jsonObject.getInt(Jsonkey.statusCode));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        userObject.setStatus_code(0);
                    }

                    userObject.setMessage(jsonObject.optString(Jsonkey.message));

                    if (userObject.getStatus_code() == 1) {
                        JSONObject userJsonObject = jsonObject.getJSONObject("user");
                        userObject.setUserid(userJsonObject.optString(Jsonkey.userUserid));
                        userObject.setFName(userJsonObject.optString(Jsonkey.userFirstName));
                        userObject.setLname(userJsonObject.optString(Jsonkey.userLastName));
                        userObject.setEmail(userJsonObject.optString(Jsonkey.userEmail));
                        userObject.setGender(userJsonObject.optString(Jsonkey.gender));
                        userObject.setDob(userJsonObject.optString(Jsonkey.userdob));
                        userObject.setPhoneno(userJsonObject.optString(Jsonkey.userMobile));
                        userObject.setAddress(userJsonObject.optString(Jsonkey.useraddress));
                        userObject.setYourself(userJsonObject.optString(Jsonkey.userself));
                        userObject.setPhoto(userJsonObject.optString(Jsonkey.userphoto));
                        userObject.setEmail_verify(userJsonObject.optString(Jsonkey.emailverify));

                        userObject.setCompnat_name(userJsonObject.optString(Jsonkey.companyname));
                        userObject.setCompnat_desc(userJsonObject.optString(Jsonkey.company_desc));
                        userObject.setTimezone(userJsonObject.optString(Jsonkey.timezone));

                        userObject.setOrganization(userJsonObject.optString(Jsonkey.organization));
                        userObject.setUrl(userJsonObject.optString(Jsonkey.url));
                        userObject.setFax_number(userJsonObject.optString(Jsonkey.faxnumber));

                        // set user detail in preference
                        saveUserDetail(userObject);
                        preferenceSettings.setFacebook(true);
                        Intent i = new Intent(Splash_Activity.this, Home_Activity.class);
                        startActivity(i);
                        finish();

                    } else if (userObject.getStatus_code() == 0) {
                        Toast.makeText(Splash_Activity.this, "" + userObject.getMessage(), Toast.LENGTH_SHORT).show();
                        LoginManager.getInstance().logOut();
                    } else if (userObject.getStatus_code() == 2) {
                        Toast.makeText(Splash_Activity.this, "" + userObject.getMessage(), Toast.LENGTH_SHORT).show();
                        LoginManager.getInstance().logOut();
                    } else {
                        LoginManager.getInstance().logOut();
                        CommonUtils.showToast(Constants.MSG_LOGIN_FAIL);
                        Log.e("Error_in", "else");
                    }
                } catch (JSONException e) {
                    pDialog.dismiss();
                    Log.e("Error_in", "catch");
                }

                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.e("Error_in", "onErrorResponse");
                CommonUtils.showToast(Constants.MSG_LOGIN_FAIL);
                pDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(ParamsKey.KEY_FB_ID, MeettoApplication.getInstance().getUserObject().getId_fb());
                params.put(ParamsKey.KEY_FB_NAME, fname);
                params.put(ParamsKey.KEY_FB_LNAME, lname);
                params.put(ParamsKey.KEY_EMAIL, MeettoApplication.getInstance().getUserObject().getEmail());
                params.put(ParamsKey.KEY_FB_PIC, MeettoApplication.getInstance().getPreferenceSettings().getUserPic());
                params.put(ParamsKey.KEY_LOGIN_TYPE, Constants.TYPE_LOGIN_FB);
                if (preferenceSettings.getLUNGAUGE()) {
                    params.put(ParamsKey.KEY_USER_LANG, "ja");
                }
                if (preferenceSettings.getLUNGAUGE() == false) {
                    params.put(ParamsKey.KEY_USER_LANG, "en");

                }
                Log.e("parameter", "" + params);
                return params;
            }

        };

// Adding request to request queue
        MeettoApplication.getInstance().addToRequestQueue(jsonObjReq, TAG);
    }

    public void saveUserDetail(UserObject userObject) {
        PreferenceSettings mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();

        mPreferenceSettings.setUserEmail(userObject.getEmail());
        mPreferenceSettings.setUSER_FIRSTNAME(userObject.getFname());
        mPreferenceSettings.setUSER_LASTNAME(userObject.getLname());
        mPreferenceSettings.setEmailverify(userObject.getEmail_verify());
        mPreferenceSettings.setLoginType(userObject.getType());
        mPreferenceSettings.setUserId(userObject.getUserid());
        mPreferenceSettings.setBirthDate(userObject.getDob());
        mPreferenceSettings.setAddress(userObject.getAddress());
        mPreferenceSettings.setGender(userObject.getGender());
        mPreferenceSettings.setMobileNumber(userObject.getPhoneno());
        mPreferenceSettings.setAboutuserself(userObject.getYourself());
        mPreferenceSettings.setUserPic(userObject.getPhoto());
        mPreferenceSettings.setCompanyName(userObject.getCompnat_name());
        mPreferenceSettings.setCompanyDesc(userObject.getCompnat_desc());
        mPreferenceSettings.setTimezone(userObject.getTimezone());
        mPreferenceSettings.setOrganization(userObject.getOrganization());
        mPreferenceSettings.setUrl(userObject.getUrl());
        mPreferenceSettings.setFaxnumber(userObject.getFax_number());
        mPreferenceSettings.setIslogin(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        MeettoApplication.getInstance().cancelPendingRequests(TAG);
    }
}
