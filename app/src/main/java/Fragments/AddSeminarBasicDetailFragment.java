package Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import Adapters.UserAttendess_Adapter;
import Adapters.Userindustry_Adapter;
import Adapters.Userseminartype_Adapter;
import Models.SeminarObject;
import Models.User_data_Object;
import Models.User_data_item;
import Utils.AppUrl;
import Utils.Jsonkey;
import Utils.ParamsKey;
import Utils.PreferenceSettings;
import callbacks.CallbackRent;
import creadigol.com.Meetto.Add_Seminar_Activity;
import creadigol.com.Meetto.MeettoApplication;
import creadigol.com.Meetto.R;


/**
 * Created by Creadigol on 09-09-2016.
 */
public class AddSeminarBasicDetailFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    Button btnCountinue;
    EditText ed_totalseat;
    CallbackRent callbackRent;
    RecyclerView rv_attendess, rv_seminattype, rv_industry;
    String totalseat, properttype;
    PreferenceSettings mPreferenceSettings;
    ArrayList<User_data_item> userDataObjects;
    ArrayList<User_data_item> userseminartype;
    ArrayList<User_data_item> userindustry;
    UserAttendess_Adapter userAttendess_adapter;
    Userseminartype_Adapter userseminartype_adapter;
    Userindustry_Adapter userindustry_adapter;

    public static AddSeminarBasicDetailFragment newInstance() {
        AddSeminarBasicDetailFragment fragment = new AddSeminarBasicDetailFragment();
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
        Add_Seminar_Activity.addprefrence = 1;
        mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();
        super.onCreate(savedInstanceState);
        mPreferenceSettings.setIS_VISIBLE(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mPreferenceSettings.getLUNGAUGE()) {
            MeettoApplication.language("ja");
        } else {
            MeettoApplication.language("en");
        }
        View view = inflater.inflate(R.layout.fragment_addseminar, container, false);
        btnCountinue = (Button) view.findViewById(R.id.btncountinue);
        btnCountinue.setOnClickListener(this);

        rv_attendess = (RecyclerView) view.findViewById(R.id.rv_seminartype);
        rv_seminattype = (RecyclerView) view.findViewById(R.id.rv_addattendess);
        rv_industry = (RecyclerView) view.findViewById(R.id.rv_addindustry);
        ed_totalseat = (EditText) view.findViewById(R.id.ed_totalseat);

        ed_totalseat.setText(Add_Seminar_Activity.seminardetailsitem.getSeminar_total_seat());
        User_data();

        return view;
    }

    void setattendess() {
        if (userDataObjects.size() > 0 || userDataObjects != null) {
            if (userAttendess_adapter == null) {

                rv_attendess.setHasFixedSize(true);
                //Set RecyclerView type according to intent value
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

                rv_attendess.setHasFixedSize(true);
                rv_attendess.setLayoutManager(gridLayoutManager);
                userAttendess_adapter = new UserAttendess_Adapter(getActivity(), userDataObjects);
                rv_attendess.setAdapter(userAttendess_adapter);
            } else {
                userAttendess_adapter.modifyDataSet(userDataObjects);
            }
        }
    }

    void setSeminattype() {
        if (userseminartype.size() > 0 || userseminartype != null) {
            if (userseminartype_adapter == null) {

                rv_seminattype.setHasFixedSize(true);
                //Set RecyclerView type according to intent value
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

                rv_seminattype.setHasFixedSize(true);
                rv_seminattype.setLayoutManager(gridLayoutManager);
                userseminartype_adapter = new Userseminartype_Adapter(getActivity(), userseminartype);
                rv_seminattype.setAdapter(userseminartype_adapter);
            } else {
                userseminartype_adapter.modifyDataSet(userseminartype);
            }
        }
    }

    void setIndustrys() {
        if (userindustry.size() > 0 || userindustry != null) {
            if (userindustry_adapter == null) {

                rv_industry.setHasFixedSize(true);
                //Set RecyclerView type according to intent value
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

                rv_industry.setHasFixedSize(true);
                rv_industry.setLayoutManager(gridLayoutManager);
                userindustry_adapter = new Userindustry_Adapter(getActivity(), userindustry);
                rv_industry.setAdapter(userindustry_adapter);
            } else {
                userindustry_adapter.modifyDataSet(userindustry);
            }
        }
    }

    public void User_data() {
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading.....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, AppUrl.URL_USER_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("addseminar", "Response: " + response);

                    JSONObject jsonObject = new JSONObject(response);
                    User_data_Object user_data_object = new User_data_Object();
                    try {
                        user_data_object.setStatus_code(jsonObject.getInt(Jsonkey.statusCode));
                    } catch (JSONException e) {
                        user_data_object.setStatus_code(0);
                    }

                    user_data_object.setMessage(jsonObject.optString(Jsonkey.message));

                    try {
                        user_data_object.setSemtype(jsonObject.getJSONArray(Jsonkey.seminartype));
                    } catch (JSONException e) {
                        user_data_object.setSemtype(null);
                    }
                    try {
                        user_data_object.setAttendess(jsonObject.getJSONArray(Jsonkey.purpose));
                        Log.e("Response purpose", jsonObject.getJSONArray(Jsonkey.purpose) + "test");
                    } catch (JSONException e) {
                        user_data_object.setSemtype(null);
                    }

                    try {
                        user_data_object.setgetIndustry(jsonObject.getJSONArray(Jsonkey.industrys));
                    } catch (JSONException e) {
                        user_data_object.setSemtype(null);
                    }
                    userseminartype = user_data_object.getSemtype();
                    userDataObjects = user_data_object.getAttendess();
                    userindustry = user_data_object.getIndustry();
                    setattendess();
                    setSeminattype();
                    setIndustrys();

                    mProgressDialog.dismiss();
                } catch (JSONException e) {
                    mProgressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Log.e("Error_in", "onErrorResponse");
//                showTryAgainAlert("" + getResources().getString(R.string.network1) + "", getResources().getString(R.string.network2) + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                if (mPreferenceSettings.getLUNGAUGE()) {
                    params.put(ParamsKey.KEY_USER_LANG, "ja");
                }
                if (mPreferenceSettings.getLUNGAUGE() == false) {
                    params.put(ParamsKey.KEY_USER_LANG, "en");

                }
                Log.e("addseminar", "reqSearchLocationData params: " + params.toString());

                return params;
            }
        };

        MeettoApplication.getInstance().addToRequestQueue(jsonObjectRequest, "addseminar");
    }


    public void validation() {
        totalseat = ed_totalseat.getText().toString().trim();
        if (MeettoApplication.attendess != null && MeettoApplication.attendess.size() <= 0) {
            Toast.makeText(getActivity(), "Please select seminar Attendess!!!", Toast.LENGTH_SHORT).show();
        } else if (MeettoApplication.seminattype != null && MeettoApplication.seminattype.size() <= 0) {
            Toast.makeText(getActivity(), "Please select seminar place !!!", Toast.LENGTH_SHORT).show();
        } else if (MeettoApplication.industry != null && MeettoApplication.industry.size() <= 0) {
            Toast.makeText(getActivity(), "Please select seminar Industry!!!", Toast.LENGTH_SHORT).show();
        } else {
            PreferenceSettings mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();
            mPreferenceSettings.setProperttype(properttype);
            Add_Seminar_Activity.seminardetailsitem.setProperttype(properttype);
            Log.e("property", "" + mPreferenceSettings.getProperttype());

            String seminarattendees = "", seminarindustry = "";
            if (MeettoApplication.attendess != null && MeettoApplication.attendess.size() > 0) {
                for (int i = 0; i < MeettoApplication.attendess.size(); i++) {
                    seminarattendees = seminarattendees + MeettoApplication.attendess.get(i) + ",";
                    Add_Seminar_Activity.seminardetailsitem.setAttendencePref(seminarattendees);
                }
                Log.e("attendess", "" + mPreferenceSettings.getPurpose());
                Log.e("attendess", "" + seminarattendees);
            } else {
                Add_Seminar_Activity.seminardetailsitem.setAttendencePref("");
            }
            if (MeettoApplication.industry != null && MeettoApplication.industry.size() > 0) {
                for (int i = 0; i < MeettoApplication.industry.size(); i++) {
                    seminarindustry = seminarindustry + MeettoApplication.industry.get(i) + ",";
                    Add_Seminar_Activity.seminardetailsitem.setIndustryPref(seminarindustry);
                }
            } else {
                Add_Seminar_Activity.seminardetailsitem.setIndustryPref("");
            }
            Add_Seminar_Activity.seminardetailsitem.setSeminar_total_seat(totalseat);
            callbackRent.onNextSelected(Add_Seminar_Activity.Fragments.BASIC_DETAILS, true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btncountinue:
                validation();
                break;
        }
    }
}