package creadigol.com.Meetto;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Utils.AppUrl;
import Utils.CommonUtils;
import Utils.ParamsKey;
import Utils.PreferenceSettings;

/**
 * Created by Ashfaq on 31-08-2016.
 */
public class Registration_Activity extends AppCompatActivity implements View.OnClickListener {
    TextView loginbtn;
    EditText fname, lname, email, password, c_password;
    String first_name, last_name, email_id, u_password, confirm_password, type;
    String mnoPref, unamePref, emlPref, regidPref;
    PreferenceSettings mPreferenceSettings;
    private static final String TAG = Registration_Activity.class.getSimpleName();
    String pattern = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mPreferenceSettings= MeettoApplication.getInstance().getPreferenceSettings();
        if(mPreferenceSettings.getLUNGAUGE()){
            MeettoApplication.language("ja");
        }else{
            MeettoApplication.language("en");
        }
        setContentView(R.layout.activity_registration);
        loginbtn = (TextView) findViewById(R.id.login_btn);
        loginbtn.setOnClickListener(this);

        unamePref = mPreferenceSettings.getUSER_FIRSTNAME();
        emlPref = mPreferenceSettings.getUserEmail();
        type = mPreferenceSettings.getLoginType();
        regidPref = mPreferenceSettings.getRegId();

        Log.e(TAG, "registration mno" + mnoPref + " " + unamePref + " " + emlPref + " " + type+ " " + regidPref);

        Log.e("type", "" + type);
        initEditText();
        register();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Registration_Activity.this, Splash_Activity.class);
        i.putExtra("Login", "true");
        startActivity(i);
        finish();

    }

    private void initEditText() {
        fname = (EditText) findViewById(R.id.edittextUserName);
        lname = (EditText) findViewById(R.id.edittextlname);
        email = (EditText) findViewById(R.id.edittextemail);
        password = (EditText) findViewById(R.id.edittextPassword);
        c_password = (EditText) findViewById(R.id.edittextConfirmPassword);

        if (type.equalsIgnoreCase("LOCAL")) {
            password.setVisibility(View.VISIBLE);
            c_password.setVisibility(View.VISIBLE);

            email.setEnabled(true);
        } else {
            password.setVisibility(View.GONE);
            c_password.setVisibility(View.GONE);
            email.setText(emlPref);
            email.setEnabled(false);
            fname.setText(unamePref);
        }

    }

    void register() {
       Button buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonRegister .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateRegister();

            }
        });
    }

    void validateRegister() {

        first_name=fname.getText().toString().trim();
        last_name=lname.getText().toString().trim();
        email_id=email.getText().toString().trim();

        if (type.equalsIgnoreCase("LOCAL")) {
            u_password=password.getText().toString().trim();
            confirm_password=c_password.getText().toString().trim();
        }

        boolean flag = true;
        if (first_name.equals("")) {
            flag = false;
            showfNameErrorText(getResources().getString(R.string.rg_fisrterror));
        }
        if (last_name.equals("")) {
            flag = false;
            showLNameErrorText(getResources().getString(R.string.rg_lasterror));
        }

        if (email_id.equals("")) {
            flag = false;
            showEmailErrorText(getResources().getString(R.string.rg_emailerror));
        }

        if (type.equalsIgnoreCase("LOCAL")) {
            if (u_password.equals("")) {
                flag = false;
                showPasswordErrorText(getResources().getString(R.string.rg_passworderror));

            } else if (u_password.length() < 8) {
                flag = false;
                showPasswordErrorText(getResources().getString(R.string.rg_passworderror_2));
            }


            if (!confirm_password.equals(u_password)) {
                flag = false;
                showConfirmPasswordError(getResources().getString(R.string.rg_confpassworderror));
            }
        }

        if (!email_id.matches(pattern)) {
            flag = false;
            showEmailValidationErrorText(getResources().getString(R.string.rg_emailvalid));
        }
        if (flag) {
            reqRegistration(type, regidPref);
        }


    }

    void showfNameErrorText(String text) {
        fname.setError(text);
    }
    void showLNameErrorText(String text) {
        lname.setError(text);
    }

    void showEmailErrorText(String text) {
        email.setError(text);
    }

    void showPasswordErrorText(String text) {
        password.setError(text);
    }

    void showEmailValidationErrorText(String text) {
        email.setError(text);
    }


    void showConfirmPasswordError(String text) {
        c_password.setError(text);
    }

    public void reqRegistration(final String type, final String uid) {
        final String url = AppUrl.URL_REGISTRATION;
        final ProgressDialog mProgressDialog = new ProgressDialog(Registration_Activity.this);
        mProgressDialog.setMessage(getResources().getString(R.string.rg_reqmsg));
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
                        JSONObject user = responseObj.getJSONObject("user");

                        String fname = user.getString("fname");
                        String email = user.getString("email");
                        String  uid = user.getString("id");
                        String  lname= user.getString("lname");

                        Log.e(TAG, "Details: " + fname + " " + email +  " " + lname + " " + uid);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();


                        mPreferenceSettings.setIslogin(true);
                        mPreferenceSettings.setUSER_FIRSTNAME(fname);
                        mPreferenceSettings.setUSER_LASTNAME(lname);
                        mPreferenceSettings.setUserEmail(email);
                        mPreferenceSettings.setUserId(uid);
                        mPreferenceSettings.setPassword(u_password);
                        Log.e("id",""+mPreferenceSettings.getUserId());

                        startActivity(new Intent(Registration_Activity.this, Home_Activity.class));
                        finish();

                    } else if (status_code == 2) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        // Log.e("RegistrationActivity Detials", "" + msg);
                        mProgressDialog.dismiss();
                    } else if (status_code == 3) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        //Log.e("RegistrationActivity Detials", "" + msg);
                        mProgressDialog.dismiss();
                    } else if (status_code == 4) {
                        //Log.e("RegistrationActivity Detials", "" + msg);
                        mProgressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }else
                    {
                        mProgressDialog.dismiss();
                        Log.e("Error_in","else");
                    }

                } catch (JSONException e) {
                    mProgressDialog.dismiss();
                    Log.e("Error_in","catch");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RegistrationActivity", "Error Response: " + error.getMessage());
                mProgressDialog.hide();
                Log.e("Error_in","onErrorResponse");
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                showTryAgainAlert(""+getResources().getString(R.string.network1)+"", getResources().getString(R.string.network2)+"",type, uid);


            }
        }) {

            /**
             * Passing user parameters to our server
             *
             * @return
             */
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (type.equalsIgnoreCase("LOCAL")) {
                    params.put("fname", first_name);
                    params.put("lname", last_name);
                    params.put("email", email_id);
                    params.put("password", u_password);
                    if(mPreferenceSettings.getLUNGAUGE()){
                        params.put(ParamsKey.KEY_USER_LANG, "ja");
                    }
                    params.put("type", type);
                    Log.e("type",""+type);
                } else {
                    params.put("fname", first_name);
                    params.put("email", email_id);
                    params.put("lname", last_name);
                    params.put("type", type);
                    params.put("fb_id", uid);
                    if(mPreferenceSettings.getLUNGAUGE()){
                        params.put(ParamsKey.KEY_USER_LANG, "ja");
                    }
                    if (mPreferenceSettings.getLUNGAUGE()==false){
                        params.put(ParamsKey.KEY_USER_LANG, "en");

                    }
                    Log.e("typeaaa",""+type);
                }

               /* if (mPreferenceSettings.getUserPic() != null) {
                    params.put("user_image", mPreferenceSettings.getUserPic());
                }*/


                Log.e("RegistrationActivity", "Posting params: " + params.toString());

                return params;
            }

        };


        MeettoApplication.getInstance().addToRequestQueue(strReq, TAG);
    }
    public void showTryAgainAlert(String title, String message, final String type, final String uid) {
        CommonUtils.showAlertWithNegativeButton(Registration_Activity.this, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (CommonUtils.isNetworkAvailable())
                    reqRegistration(type, uid);
                else
                    CommonUtils.showToast(getResources().getString(R.string.network3));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                Intent intent = new Intent(Registration_Activity.this, Login_Activity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
