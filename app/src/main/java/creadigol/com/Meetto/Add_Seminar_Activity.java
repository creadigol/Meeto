package creadigol.com.Meetto;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import Fragments.AddSeminarBasicDetailFragment;
import Fragments.AddSeminarDetailsList_Fragment;
import Fragments.AddSeminarDetailOverview_Fragment;
import Models.SeminarDetailsarrayitem;
import Models.SeminarObject;
import Models.Seminardetailsitem;
import Utils.AppUrl;
import Utils.CommonUtils;
import Utils.Jsonkey;
import Utils.ParamsKey;
import Utils.PreferenceSettings;
import callbacks.CallbackRent;

/**
 * Created by Creadigol on 05-09-2016.
 */

public class Add_Seminar_Activity extends AppCompatActivity implements CallbackRent {
    PreferenceSettings mPreferenceSettings;
    public static Seminardetailsitem seminardetailsitem;
    public static ArrayList<String> arrayList;
    public static ArrayList<String> editImageList;
    private static android.support.v4.app.FragmentTransaction mFragmentTransaction;
    public static String deleteImage;
    private static Fragment objFragment;
    private static Fragments selectedFrag;
    public static String deleteImageSdCard;
    public static SeminarObject seminarObject;
    private int container;
    public static int value = 0;
    public static int addprefrence = 0;


    public static enum Fragments {
        BASIC_DETAILS, DETAILS_LIST, DEATAILS_OVERVIEW
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addseminar);
        seminardetailsitem = new Seminardetailsitem();
        seminarObject = new SeminarObject();
        arrayList = new ArrayList<String>();
        editImageList = new ArrayList<String>();
        mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();
        Toolbar();
        if (mPreferenceSettings.getEdit()) {
            getSeminarDetail();
        } else {
            container = R.id.fragment_container;
            showFragment(Fragments.BASIC_DETAILS);
        }
    }

    public void Toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_addseminar);
        setSupportActionBar(toolbar);
        TextView tvCatTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_name);
        tvCatTitle.setText(R.string.add_Toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MeettoApplication.attendess.clear();
        MeettoApplication.industry.clear();
        MeettoApplication.seminattype.clear();
        mPreferenceSettings.setEdit(false);
        mPreferenceSettings.setSeminar_id("");
        seminardetailsitem = null;
        MeettoApplication.facility.clear();

    }

    private void showFragment(Fragments fragmentType) {
        switch (fragmentType) {
            case BASIC_DETAILS:
                objFragment = AddSeminarBasicDetailFragment.newInstance(); // make Instant of
                Log.e("Customer", "Fragment b4");
                selectedFrag = Fragments.BASIC_DETAILS;
                Log.e("Customer", "Fragment aftr");
                break;
            case DETAILS_LIST:
                objFragment = AddSeminarDetailsList_Fragment.newInstance();// make Instant of
                selectedFrag = Fragments.DETAILS_LIST;
                break;

            case DEATAILS_OVERVIEW:
                objFragment = AddSeminarDetailOverview_Fragment.newInstance();
                selectedFrag = Fragments.DEATAILS_OVERVIEW;
                break;

        }
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(container, objFragment);
        mFragmentTransaction.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        mFragmentTransaction.commit();
    }

    @Override
    public void onNextSelected(Fragments fragmentType, boolean isNext) {
        switch (fragmentType) {
            case BASIC_DETAILS:
                if (isNext) {
                    showFragment(Fragments.DETAILS_LIST); // show document fragment
                } else if (isNext == false) {
                    finish();
                }
                break;

            case DETAILS_LIST:
                if (isNext) {
                    showFragment(Fragments.DEATAILS_OVERVIEW); // show document fragment
                } else if (isNext == false) {
                    showFragment(Fragments.BASIC_DETAILS); // show document fragment
                }
                break;


            case DEATAILS_OVERVIEW:
                // TODO showFragment(Fragments.LoanerCarDetail); // show
                if (isNext == false)
                    showFragment(Fragments.DETAILS_LIST);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (addprefrence == 1) {
                    finish();
                } else if (addprefrence == 2) {
                    onNextSelected(Fragments.DETAILS_LIST, false);
                } else if (addprefrence == 3) {
                    onNextSelected(Fragments.DEATAILS_OVERVIEW, false);
                }
        }
        return true;
    }

    public void getSeminarDetail() {

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, AppUrl.URL_SEMINAR_DETAILS2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("add", "Response: " + response);
                    JSONObject jsonObject = new JSONObject(response);
                    Seminardetailsitem seminardetailsitem = new Seminardetailsitem();
                    seminarObject = new SeminarObject();
                    try {
                        seminardetailsitem.setStatus_code(jsonObject.getInt(Jsonkey.statusCode));
                        seminarObject.setStatus_code(jsonObject.getInt(Jsonkey.statusCode));
                    } catch (JSONException e) {
                        seminardetailsitem.setStatus_code(0);
                    }

                    seminardetailsitem.setMessage(jsonObject.optString(Jsonkey.message));
                    seminarObject.setMessage(jsonObject.optString(Jsonkey.message));

                    if (seminarObject.getStatus_code() == 1) {
                        JSONObject jsonObjectBasicDetail = jsonObject.getJSONObject(Jsonkey.seminardeatil_key);
                        seminarObject.setSeminarDetail(jsonObjectBasicDetail);

                        JSONArray jsonArrayPic = jsonObject.getJSONArray(Jsonkey.seminar_Image);
                        seminarObject.setSeminarPicture(jsonArrayPic);

                        JSONArray jsonArrayFacility = jsonObject.getJSONArray(Jsonkey.facilites);
                        seminarObject.setSeminarFacility(jsonArrayFacility);
                        seminarObject.setFacilitestems(jsonArrayFacility);

                        JSONArray jsonArrayAtendies = jsonObject.getJSONArray(Jsonkey.seminar_purpose);
                        seminarObject.setSeminarAttendees(jsonArrayAtendies);
                        seminarObject.setattendees(jsonArrayAtendies);

                        JSONArray jsonArrayIndustry = jsonObject.getJSONArray(Jsonkey.industry);
                        seminarObject.setSeminarIndustry(jsonArrayIndustry);
                        seminarObject.setIndustry(jsonArrayIndustry);

                        try {
                            JSONArray jsonimage = jsonObject.getJSONArray(Jsonkey.seminar_Key);
                            Add_Seminar_Activity.seminardetailsitem.setImagesitems(jsonimage);
                            Log.e("alreadyImage", "" + jsonimage);
                            Log.e("imageslide", " b" + Add_Seminar_Activity.seminardetailsitem.getImagesitems().size());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mPreferenceSettings.setIS_VISIBLE(true);
                        setOutletdetail(seminarObject);
                        //set outlet detail
                    } else if (seminardetailsitem.getStatus_code() == 0 || seminardetailsitem.getStatus_code() == 2) {
                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("Error_in", "Error_else");
                    }
                } catch (JSONException e) {
                    Log.e("Error_in", "catch");

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error_in", "onErrorResponse");

                showTryAgainAlert("" + getResources().getString(R.string.network1) + "", getResources().getString(R.string.network2) + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                PreferenceSettings mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();

                Map<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_SEMINAR_ID, mPreferenceSettings.getSeminar_id());
                params.put(ParamsKey.KEY_USERID, mPreferenceSettings.getUserId());
                if (mPreferenceSettings.getLUNGAUGE()) {
                    params.put(ParamsKey.KEY_USER_LANG, "ja");
                }
                if (mPreferenceSettings.getLUNGAUGE() == false) {
                    params.put(ParamsKey.KEY_USER_LANG, "en");

                }
                Log.e("Add", "reqOutletData params: " + params.toString());

                return params;
            }
        };

        MeettoApplication.getInstance().addToRequestQueue(jsonObjectRequest, "add");
    }

    public void showTryAgainAlert(String title, String message) {
        CommonUtils.showAlertWithNegativeButton(getApplicationContext(), title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                if (CommonUtils.isNetworkAvailable())
                    getSeminarDetail();
                else
                    CommonUtils.showToast("" + getResources().getString(R.string.network3));

            }
        });
    }

    public void setOutletdetail(final SeminarObject seminarObject) {
        if (seminarObject.getTotalSeat() > 0) {
            seminardetailsitem.setSeminar_total_seat(String.valueOf(seminarObject.getTotalSeat()));
        }
        Add_Seminar_Activity.seminardetailsitem.setSeminar_id(seminarObject.getSeminarId());
        Add_Seminar_Activity.seminardetailsitem.setSeminar_name(seminarObject.getTitle());
        Add_Seminar_Activity.seminardetailsitem.setTagline(seminarObject.getTagLine());
        Add_Seminar_Activity.seminardetailsitem.setZipcode(seminarObject.getZipCode());
        Add_Seminar_Activity.seminardetailsitem.setContact_email(seminarObject.getEmailContact());
        Add_Seminar_Activity.seminardetailsitem.setContact_no(seminarObject.getNumber());
        Add_Seminar_Activity.seminardetailsitem.setSeminar_address(seminarObject.getAddressStreet());
        Add_Seminar_Activity.seminardetailsitem.setSeminar_host_name(seminarObject.getHostName());
        Add_Seminar_Activity.seminardetailsitem.setSeminar_description(seminarObject.getDiscription());
        Add_Seminar_Activity.seminardetailsitem.setCounty(String.valueOf(seminarObject.getCountry()));
        Add_Seminar_Activity.seminardetailsitem.setState(String.valueOf(seminarObject.getState()));
        Add_Seminar_Activity.seminardetailsitem.setCity(String.valueOf(seminarObject.getCity()));
        Add_Seminar_Activity.seminardetailsitem.setFromdate(CommonUtils.getFormatedDate((seminarObject.getDateFrom()), "yyyy-MM-dd hh:mm"));
        Add_Seminar_Activity.seminardetailsitem.setTodate(CommonUtils.getFormatedDate((seminarObject.getDateTo()), "yyyy-MM-dd hh:mm"));
        if (seminarObject.getPlace() != null) {
            MeettoApplication.seminattype.add(seminarObject.getPlace());
        }
        if (seminarObject.getattendees() != null && seminarObject.getattendees().size() > 0) {
            setAttendees(seminarObject.getattendees());
        }
        if (seminarObject.getIndustry() != null && seminarObject.getIndustry().size() > 0) {
            setIndustry(seminarObject.getIndustry());
        }
        if (seminarObject.getFacilitiesitems() != null && seminarObject.getFacilitiesitems().size() > 0) {
            setFacility(seminarObject.getFacilitiesitems());
        }
        container = R.id.fragment_container;
        showFragment(Fragments.BASIC_DETAILS);
    }

    public void setAttendees(ArrayList<SeminarDetailsarrayitem> attendees) {
        if (attendees != null) {

            for (SeminarDetailsarrayitem attendeesobject : attendees) {
                String attendess = attendeesobject.getSeminar_purpose();
                MeettoApplication.attendess.add(attendess);
            }
        }
    }

    public void setIndustry(ArrayList<SeminarDetailsarrayitem> industrys) {

        if (industrys != null) {

            for (SeminarDetailsarrayitem Industryobject : industrys) {
                String industry = Industryobject.getIndustry();
                MeettoApplication.industry.add(industry);
            }
        }

    }

    public void setFacility(ArrayList<SeminarDetailsarrayitem> facility) {

        if (facility != null) {

            for (SeminarDetailsarrayitem facilityobject : facility) {
                String facilities = facilityobject.getFacilities();
                MeettoApplication.facility.add(facilities);
            }
        }

    }

}
