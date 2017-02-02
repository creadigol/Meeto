package creadigol.com.Meetto;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import Models.UserObject;
import Utils.AppUrl;
import Utils.CommonUtils;
import Utils.Constants;
import Utils.Jsonkey;
import Utils.ParamsKey;
import Utils.PreferenceSettings;
import ch.boye.httpclientandroidlib.androidextra.Base64;


/**
 * Created by Ashfaq on 31-08-2016.
 */
public class Login_Activity extends AppCompatActivity {
    private EditText mEditTextLogin;
    private EditText mEditTextPassword;
    private static final String TAG = Login_Activity.class.getSimpleName();
    String pattern = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";
    PreferenceSettings mPreferenceSettings;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferenceSettings= MeettoApplication.getInstance().getPreferenceSettings();
        if(mPreferenceSettings.getLUNGAUGE()){
            MeettoApplication.language("ja");
        }else{
            MeettoApplication.language("en");
        }
        setContentView(R.layout.activity_login);
        mEditTextLogin = (EditText) findViewById(R.id.edt_emailOrMobile);
        mEditTextPassword = (EditText) findViewById(R.id.edt_password);
//        mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();
        Button mBottonLogin = (Button) findViewById(R.id.btn_Login);

        mBottonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        TextView forgotpassword = (TextView) findViewById(R.id.Forgotpass);
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgot = new Intent(Login_Activity.this, Forgotpassword_Activity.class);
                startActivity(forgot);
            }
        });

    }

    private void validate() {
        boolean isValid = true;
        String strEmailOrMobile = mEditTextLogin.getText().toString().trim();
        String strPassword = mEditTextPassword.getText().toString().trim();

        if (strEmailOrMobile == null && strEmailOrMobile.equals("")) {
            showPhoneErrorText(getResources().getString(R.string.email_error));
            isValid = false;
        } else if (!validateMobileNo(strEmailOrMobile)) {
            if (!CommonUtils.isValidEmail(strEmailOrMobile)) {
                showPhoneErrorText(getResources().getString(R.string.email_error_2));
                isValid = false;
            } else {
                isValid = true;
            }
        } else {
            isValid = true;
        }
        if (strPassword == null && strPassword.equals("")) {
            isValid = false;
            showPasswordErrorText(getResources().getString(R.string.password_error));
        }
        if (isValid) {
            reqUserLogin(strEmailOrMobile, strPassword);
        }
    }

    void showPasswordErrorText(String text) {
        mEditTextPassword.setError(text);
    }

    void showPhoneErrorText(String text) {
        mEditTextLogin.setError(text);
    }

    public boolean validateMobileNo(String input) {
        return android.util.Patterns.PHONE.matcher(input).matches();
    }

    private void reqUserLogin(final String input, final String password) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        HttpsTrustManager.allowAllSSL();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                AppUrl.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, response.toString());
                pDialog.dismiss();
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
                        mPreferenceSettings.setPassword(password);
                        // set user detail in preference
                        saveUserDetail(userObject);

                        Intent i = new Intent(Login_Activity.this, Home_Activity.class);
                        startActivity(i);
                        Login_Activity.this.finish();

                    } else if (userObject.getStatus_code() == 0 || userObject.getStatus_code() == 3) {
                        CommonUtils.showToast(userObject.getMessage());
                    } else if (userObject.getStatus_code() == 9) {
                        Toast.makeText(Login_Activity.this, "" + userObject.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("Error_in", "catch");
                    pDialog.dismiss();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.e("Error_in", "onErrorResponse");
                error.printStackTrace();
                showTryAgainAlert(""+getResources().getString(R.string.network1)+"", getResources().getString(R.string.network2)+"", input, password);
                pDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(ParamsKey.KEY_EMAIL, input);
                params.put(ParamsKey.KEY_PASSWORD, password);
                params.put(ParamsKey.KEY_LOGIN_TYPE, Constants.TYPE_LOGIN_LOCAL);
                if(mPreferenceSettings.getLUNGAUGE()){
                    params.put(ParamsKey.KEY_USER_LANG, "ja");
                }
                if (mPreferenceSettings.getLUNGAUGE()==false){
                    params.put(ParamsKey.KEY_USER_LANG, "en");

                }
                Log.e(TAG, "reqUserLogin params: " + params.toString());

                return params;
            }

        };

        // Adding request to request queue
        MeettoApplication.getInstance().addToRequestQueue(jsonObjReq, TAG);
    }

    public void saveUserDetail(UserObject userObject) {

        String pic_path = userObject.getPhoto();
        mPreferenceSettings.setUserEmail(userObject.getEmail());
        mPreferenceSettings.setUSER_FIRSTNAME(userObject.getFname());
        mPreferenceSettings.setUSER_LASTNAME(userObject.getLname());
        mPreferenceSettings.setEmailverify(userObject.getEmail_verify());
        mPreferenceSettings.setUserId(userObject.getUserid());
        mPreferenceSettings.setBirthDate(userObject.getDob());
        mPreferenceSettings.setAddress(userObject.getAddress());
        mPreferenceSettings.setGender(userObject.getGender());
        mPreferenceSettings.setMobileNumber(userObject.getPhoneno());
        mPreferenceSettings.setAboutuserself(userObject.getYourself());
        mPreferenceSettings.setUserPic(pic_path);
        mPreferenceSettings.setCompanyName(userObject.getCompnat_name());
        mPreferenceSettings.setCompanyDesc(userObject.getCompnat_desc());
        mPreferenceSettings.setTimezone(userObject.getTimezone());
        mPreferenceSettings.setOrganization(userObject.getOrganization());
        mPreferenceSettings.setUrl(userObject.getUrl());
        mPreferenceSettings.setFaxnumber(userObject.getFax_number());
        mPreferenceSettings.setIslogin(true);

    }

    public void showTryAgainAlert(String title, String message, final String input, final String password) {
        CommonUtils.showAlertWithNegativeButton(Login_Activity.this, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (CommonUtils.isNetworkAvailable())
                    reqUserLogin(input, password);
                else
                    CommonUtils.showToast(getResources().getString(R.string.network3));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            Intent i = new Intent(Login_Activity.this, Splash_Activity.class);
            i.putExtra("Login", "true");
            startActivity(i);
            finish();

    }
}
