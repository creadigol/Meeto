package Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import Utils.AppUrl;
import Utils.CommonUtils;
import Utils.Jsonkey;
import Utils.ParamsKey;
import Utils.PreferenceSettings;
import callbacks.CallbackRent;
import creadigol.com.Meetto.Add_Seminar_Activity;
import creadigol.com.Meetto.MeettoApplication;
import creadigol.com.Meetto.R;
import creadigol.com.Meetto.YourListing_Activity;
import creadigol.com.Meetto.MultipartRequest;

public class AddSeminarDetailsList_Fragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    AddSeminarDetailOverview_Fragment objfragmnet;
    android.support.v4.app.FragmentTransaction ft;
    android.support.v4.app.FragmentManager fragment;
    Bundle intent;
    CallbackRent callbackRent;
    PreferenceSettings mPreferenceSettings;
    LinearLayout ll_contact, ll_calender, ll_overview, ll_photo, ll_facilities, ll_location;

    public static AddSeminarDetailsList_Fragment newInstance() {
        AddSeminarDetailsList_Fragment fragment = new AddSeminarDetailsList_Fragment();
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callbackRent = (CallbackRent) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CallbackRent");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Add_Seminar_Activity.addprefrence = 2;

        mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();
        objfragmnet = new AddSeminarDetailOverview_Fragment();
        fragment = getFragmentManager();
        ft = fragment.beginTransaction();
        intent = new Bundle();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mPreferenceSettings.getLUNGAUGE()) {
            MeettoApplication.language("ja");
        } else {
            MeettoApplication.language("en");
        }
        View view = inflater.inflate(R.layout.fragment_addseminar_list, container, false);

        ll_contact = (LinearLayout) view.findViewById(R.id.ll_contact);
        ll_calender = (LinearLayout) view.findViewById(R.id.ll_calandarlist);
        ll_overview = (LinearLayout) view.findViewById(R.id.ll_overviewlist);
        ll_photo = (LinearLayout) view.findViewById(R.id.ll_photolist);
        ll_facilities = (LinearLayout) view.findViewById(R.id.ll_Amenitieslist);
        ll_location = (LinearLayout) view.findViewById(R.id.ll_locationlist);

        ll_contact.setOnClickListener(this);
        ll_calender.setOnClickListener(this);
        ll_overview.setOnClickListener(this);
        ll_photo.setOnClickListener(this);
        ll_facilities.setOnClickListener(this);
        ll_location.setOnClickListener(this);

        Button btn_save = (Button) view.findViewById(R.id.btn_addsave);
        if (Add_Seminar_Activity.value!=0&&Add_Seminar_Activity.value==4 ||mPreferenceSettings.getEdit()) {
            btn_save.setVisibility(View.VISIBLE);
        }
        btn_save.setOnClickListener(this);
        if(!Add_Seminar_Activity.seminardetailsitem.getFromdate().equalsIgnoreCase("")&&!Add_Seminar_Activity.seminardetailsitem.getTodate().equalsIgnoreCase("")&&!Add_Seminar_Activity.seminardetailsitem.getSeminar_name().equalsIgnoreCase("")&&!Add_Seminar_Activity.seminardetailsitem.getTagline().equalsIgnoreCase("")
                &&!Add_Seminar_Activity.seminardetailsitem.getSeminar_address().equalsIgnoreCase("")){
            btn_save.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_contact:
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                intent.putString("seminar", "contact");
                objfragmnet.setArguments(intent);
                ft.add(R.id.fragment_container, objfragmnet);
                ft.commit();
                break;

            case R.id.ll_calandarlist:
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                intent.putString("seminar", "calender");
                objfragmnet.setArguments(intent);
                ft.add(R.id.fragment_container, objfragmnet);
                ft.commit();
                break;

            case R.id.ll_overviewlist:
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                intent.putString("seminar", "overview");
                objfragmnet.setArguments(intent);
                ft.replace(R.id.fragment_container, objfragmnet);
                ft.commit();
                break;
            case R.id.ll_photolist:
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                intent.putString("seminar", "photo");
                objfragmnet.setArguments(intent);
                ft.replace(R.id.fragment_container, objfragmnet);
                ft.commit();
                break;

            case R.id.ll_Amenitieslist:
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                intent.putString("seminar", "facilities");
                objfragmnet.setArguments(intent);
                ft.replace(R.id.fragment_container, objfragmnet);
                ft.commit();
                break;

            case R.id.ll_locationlist:
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                intent.putString("seminar", "location");
                objfragmnet.setArguments(intent);
                ft.replace(R.id.fragment_container, objfragmnet);
                ft.commit();
                break;

            case R.id.btn_addsave:
                try {
                    addseminar();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }


    public void addseminar() {
        //get path from select
        String url = AppUrl.URL_ADDSEMINAR;
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading.....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        MultipartRequest jsonObjectRequest = new MultipartRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("User update response", response);
                mProgressDialog.dismiss();

                try {
                    JSONObject responseObj = new JSONObject(response);
                    String message = responseObj.getString("message");

                    int status_code = responseObj.getInt("status_code");
                    if (status_code == 1) {
                        MeettoApplication.photo = null;
                        MeettoApplication.count = null;
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), YourListing_Activity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        getActivity().finish();
                        mProgressDialog.dismiss();
                    } else if (status_code == 0) {
                        mProgressDialog.dismiss();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    } else {
                        mProgressDialog.dismiss();
                        Log.e("Error_in", "else");
                    }
                } catch (JSONException e) {
                    mProgressDialog.dismiss();
                    Log.e("Error_in", "catch");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                error.printStackTrace();
                Log.e("Error_in", "onErrorResponse" + error.getMessage());
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MeettoApplication.getInstance().addToRequestQueue(jsonObjectRequest, "addseminar");
    }


    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    callbackRent.onNextSelected(Add_Seminar_Activity.Fragments.DETAILS_LIST, false);
                    return true;
                }
                return false;
            }
        });
    }

}