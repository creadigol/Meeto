package creadigol.com.Meetto;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import Utils.AppUrl;
import Utils.ParamsKey;
import Utils.PreferenceSettings;

/**
 * Created by Creadigol on 17-09-2016.
 */
public class Setting_Activity extends AppCompatActivity {
    LinearLayout ll_changepassword, ll_changelng,ll_change;
    ImageView iv_changepass, iv_changelng;
    private static final String TAG = Setting_Activity.class.getSimpleName();
    String old_pwd, new_pwd, confirm_pwd;
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
        setContentView(R.layout.activity_setting);
        ll_changepassword = (LinearLayout) findViewById(R.id.ll_changepass);
        ll_change= (LinearLayout) findViewById(R.id.ll_change);
        iv_changepass = (ImageView) findViewById(R.id.iv_change);
        ll_changelng = (LinearLayout) findViewById(R.id.ll_lng);
        iv_changelng = (ImageView) findViewById(R.id.iv_lng);
        Toolbar();
        if(mPreferenceSettings.getFacebook()){
            ll_change.setVisibility(View.GONE);
        }
        try {
            EditProfile();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void Toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        TextView tvCatTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_name);
        tvCatTitle.setText(R.string.setting_toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    public void EditProfile() throws ParseException {
        final Dialog dialog = new Dialog(Setting_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        ll_changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.setContentView(R.layout.dialog_password);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                final EditText oldText = (EditText) dialog.findViewById(R.id.editOldPassword);
                final EditText newText = (EditText) dialog.findViewById(R.id.editNewPassword);
                final EditText confirmText = (EditText) dialog.findViewById(R.id.editConfirmPassword);

                Button mButtonCancel = (Button) dialog.findViewById(R.id.buttonCancel);
                mButtonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button dialogButton = (Button) dialog.findViewById(R.id.buttonOk);
                dialog.getWindow().getAttributes().windowAnimations =
                        R.style.dialog_animation;


                // if button is clicked, close the custom dialog

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        old_pwd = oldText.getText().toString();
                        new_pwd = newText.getText().toString();
                        confirm_pwd = confirmText.getText().toString();
                        if (old_pwd.equals("")) {
                            oldText.setError(getResources().getString(R.string.change_oldpassword));
                        }
                        else if (new_pwd.equals("")) {
                            newText.setError(getResources().getString(R.string.change_newpassword));
                        } else if (new_pwd.length()<8) {
                            newText.setError("enter minimum 8 digits");
                        }
                        else if (new_pwd.equals(old_pwd)) {
                            newText.setError(getResources().getString(R.string.change_conpass));
                        }
                        else if (!confirm_pwd.equals(new_pwd) | confirm_pwd.equals("")) {
                            confirmText.setError(getResources().getString(R.string.change_confirm));
                        } else {
                            changePassword();
                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();
            }
            //  }
        });
        ll_changelng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.setContentView(R.layout.dialog_language);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));

                Button mButtoneng = (Button) dialog.findViewById(R.id.btn_eng);
                mButtoneng.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PreferenceSettings preferenceSettings = new PreferenceSettings(Setting_Activity.this);
                        preferenceSettings.setLUNGAUGE(false);
                        Home_Activity.isNeedResponse=true;
                        Intent i = new Intent(Setting_Activity.this, Setting_Activity.class);
                        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        dialog.dismiss();
                    }
                });
                Button dialogButton = (Button) dialog.findViewById(R.id.btn_jp);
                dialog.getWindow().getAttributes().windowAnimations =
                        R.style.dialog_animation;


                // if button is clicked, close the custom dialog

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PreferenceSettings preferenceSettings = new PreferenceSettings(Setting_Activity.this);
                        preferenceSettings.setLUNGAUGE(true);
                        Home_Activity.isNeedResponse=true;
                        Intent i = new Intent(Setting_Activity.this, Setting_Activity.class);
                        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
            //  }
        });
    }

    private void changePassword() {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                AppUrl.URL_CHANGE_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response.toString());
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("response", "" + jsonObject);
                    int status_code = jsonObject.getInt("status_code");
                    Log.e("code", "" + status_code);
                    String message = jsonObject.getString("message");

                    if (status_code == 1) {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.change_msg), Toast.LENGTH_SHORT).show();
                    } else if (status_code == 0) {
                        pDialog.dismiss();
                        Toast.makeText(Setting_Activity.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    pDialog.dismiss();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.e("Error_in", "onErrorResponse");
                pDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_USER_ID, mPreferenceSettings.getUserId());
                params.put(ParamsKey.KEY_OLD_PASSWORD, old_pwd);
                params.put(ParamsKey.KEY_NEW_PASSWORD, new_pwd);
                if (mPreferenceSettings.getLUNGAUGE()) {
                    params.put(ParamsKey.KEY_USER_LANG, "ja");
                }
                if (mPreferenceSettings.getLUNGAUGE() == false) {
                    params.put(ParamsKey.KEY_USER_LANG, "en");

                }
                Log.e(TAG, "reqUserLogin params: " + params.toString());

                return params;
            }

        };
        MeettoApplication.getInstance().addToRequestQueue(jsonObjReq, TAG);
    }
}
