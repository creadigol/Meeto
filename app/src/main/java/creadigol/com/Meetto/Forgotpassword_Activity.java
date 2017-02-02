package creadigol.com.Meetto;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Models.UserObject;
import Utils.AppUrl;
import Utils.CommonUtils;
import Utils.Jsonkey;
import Utils.ParamsKey;
import Utils.PreferenceSettings;

/**
 * Created by Creadigol on 16-09-2016.
 */
public class Forgotpassword_Activity extends AppCompatActivity implements View.OnClickListener{
    EditText email;
    String forgot_email;
    private static final String TAG=Forgotpassword_Activity.class.getSimpleName();
    PreferenceSettings preferenceSettings;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceSettings= MeettoApplication.getInstance().getPreferenceSettings();
        if(preferenceSettings.getLUNGAUGE()){
            MeettoApplication.language("ja");
        }else{
            MeettoApplication.language("en");
        }
        setContentView(R.layout.activity_forgotpassword);
        email = (EditText) findViewById(R.id.edt_forgotmail);
       Button send = (Button) findViewById(R.id.btn_send);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send:
                forgotprocess();
                break;
        }
    }
    public void forgotprocess()
    {
        forgot_email=email.getText().toString().trim();

        if(!CommonUtils.isValidEmail(forgot_email)||forgot_email.equalsIgnoreCase(""))
        {
            email.setError(getResources().getString(R.string.rg_emailvalid));
        }else
        {
            reqForgotPass();
        }
    }

    private void reqForgotPass() {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                AppUrl.URL_FORGOTPASSWORD, new Response.Listener<String>() {

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
                        Toast.makeText(Forgotpassword_Activity.this, userObject.getMessage(), Toast.LENGTH_LONG).show();
                        preferenceSettings.setIsForgot(false);
                        preferenceSettings.clearSession();
                        finish();
                    } else if (userObject.getStatus_code() == 0) {
                        CommonUtils.showToast(userObject.getMessage());
                    }
                } catch (JSONException e) {
                    pDialog.dismiss();
                    Log.e("Error_in","catch");
                }

                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.e("Error_in","onErrorResponse");
                pDialog.dismiss();
                showTryAgainAlert(""+getResources().getString(R.string.network1)+"", getResources().getString(R.string.network2)+"");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_EMAIL,forgot_email.trim());
                if(preferenceSettings.getLUNGAUGE()){
                    params.put(ParamsKey.KEY_USER_LANG, "ja");
                }
                if (preferenceSettings.getLUNGAUGE()==false){
                    params.put(ParamsKey.KEY_USER_LANG, "en");

                }
                Log.e(TAG, "reqUserLogin params: " + params.toString());

                return params;
            }

        };

        // Adding request to request queue
        MeettoApplication.getInstance().addToRequestQueue(jsonObjReq, TAG);
    }

    public void showTryAgainAlert(String title, String message) {
        CommonUtils.showAlertWithNegativeButton(Forgotpassword_Activity.this, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (CommonUtils.isNetworkAvailable())
                    reqForgotPass();
                else
                    CommonUtils.showToast(getResources().getString(R.string.network3));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MeettoApplication.getInstance().cancelPendingRequests(TAG);
    }
}
