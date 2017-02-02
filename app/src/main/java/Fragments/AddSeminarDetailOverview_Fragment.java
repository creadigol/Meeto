package Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Adapters.Userfacilitiy_Adapter;
import App.MyApplication;
import Models.CityModel;
import Models.CountryModel;
import Models.SeminarDetailsarrayitem;
import Models.SeminarObject;
import Models.Seminardetailsitem;
import Models.StateModel;
import Models.User_data_Object;
import Models.User_data_item;
import Utils.AppUrl;
import Utils.CommonUtils;
import Utils.Jsonkey;
import Utils.ParamsKey;
import Utils.PreferenceSettings;
import callbacks.CallbackRent;
import creadigol.com.Meetto.Add_Seminar_Activity;
import creadigol.com.Meetto.MeettoApplication;
import creadigol.com.Meetto.R;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Creadigol on 09-09-2016.
 */
public class AddSeminarDetailOverview_Fragment extends android.support.v4.app.Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final int ACTION_TAKE_PHOTO = 1111; // License photo
    private static final int ACTION_SELECT_PHOTO = 1112;
    public static ArrayList<CountryModel> countrylist;
    public static ArrayList<StateModel> statelist;
    public static ArrayList<CityModel> citylist;
    public static int countryId, stateId, cityId, countryPos, statePos, cityPos;
    public static int imageViewWidth = 0;
    public static int imageViewHeight = 0;
    public static ArrayList<String> arrayAllImagePath;
    public static ArrayList<String> arraylist = new ArrayList<>();
    public static ArrayList<String> countval = new ArrayList<>();
    static int imagecount = 0, i = 0;
    public String rotet_count;
    PreferenceSettings mPreferenceSettings;
    String value, s_fromdate, s_todate, s_titel, s_tagline, s_seminardesc, s_address, s_zipcode, Country_id, State_id, City_id;
    RelativeLayout rl_contact, rl_datedetail, rl_overview, rl_photo, rl_facilities, rl_listpro, rl_location;
    String Flag, flagtime, s_hostname, s_contactemail, s_contactno;
    EditText host_name, contact_email, contact_no, fromdate, todate, starttime, endtime, ed_titel, ed_tagline, ed_seminardesc, address, zipcode;
    Button btn_contactsave, btn_clnsave, btn_overviewsave, btn_photosave, add_photo, btn_facilitysave, btn_locsave;
    LinearLayout linear_container;
    ImageView Image_select;
    ArrayList<User_data_item> userfacility;
    Userfacilitiy_Adapter userfacilitiy_adapter;
    RecyclerView rv_facilities;
    View itemList;
    CallbackRent callbackRent;
    Spinner spCountry, spState, spCity;
    int statecountryId = 0, cityStateId = 0;
    private Uri fileUri;
    private String strSetDate = "", strSetTime = "";
    private String mCurrentPhotoPath;

    /*********************
     * Section Images
     **********************/


    public static AddSeminarDetailOverview_Fragment newInstance() {
        AddSeminarDetailOverview_Fragment f = new AddSeminarDetailOverview_Fragment();
        return f;
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

    public View.OnClickListener onAddImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Image_select.setVisibility(View.VISIBLE);
            selectImage();
        }
    };
    private DisplayImageOptions options;

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Add_Seminar_Activity.addprefrence = 3;
        value = getArguments().getString("seminar");
        mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();

    }

    View view1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mPreferenceSettings.getLUNGAUGE()) {
            MeettoApplication.language("ja");
        } else {
            MeettoApplication.language("en");
        }
        View view = inflater.inflate(R.layout.fragment_addseminar_details, container, false);
        view1 = view;
        rl_contact = (RelativeLayout) view.findViewById(R.id.rl_contact);
        rl_datedetail = (RelativeLayout) view.findViewById(R.id.rl_datedetail);
        rl_overview = (RelativeLayout) view.findViewById(R.id.rl_overview);
        rl_photo = (RelativeLayout) view.findViewById(R.id.rl_photodeatils);
        rl_facilities = (RelativeLayout) view.findViewById(R.id.rl_facilities);
        rl_listpro = (RelativeLayout) view.findViewById(R.id.rl_listpro);
        rl_location = (RelativeLayout) view.findViewById(R.id.rl_location);

        //contact function
        host_name = (EditText) view.findViewById(R.id.add_host_name);
        contact_email = (EditText) view.findViewById(R.id.contact_email);
        contact_no = (EditText) view.findViewById(R.id.contact_No);
        btn_contactsave = (Button) view.findViewById(R.id.btn_contactsave);
        btn_contactsave.setOnClickListener(this);

        //calander function
        fromdate = (EditText) view.findViewById(R.id.add_fromdate);
        todate = (EditText) view.findViewById(R.id.add_enddate);
        starttime = (EditText) view.findViewById(R.id.start_time);
        endtime = (EditText) view.findViewById(R.id.end_time);
        btn_clnsave = (Button) view.findViewById(R.id.btn_clnsave);
        btn_clnsave.setOnClickListener(this);
        if (Add_Seminar_Activity.seminardetailsitem.getSeminar_host_name() != null) {
            host_name.setText(Add_Seminar_Activity.seminardetailsitem.getSeminar_host_name());
        }
        if (Add_Seminar_Activity.seminardetailsitem.getContact_email() != null) {
            contact_email.setText(Add_Seminar_Activity.seminardetailsitem.getContact_email());
        }
        if (Add_Seminar_Activity.seminardetailsitem.getContact_no() != null) {
            contact_no.setText(Add_Seminar_Activity.seminardetailsitem.getContact_no());
        }

        //set calander data
        fromdate.setOnClickListener(this);
        todate.setOnClickListener(this);
        starttime.setOnClickListener(this);
        endtime.setOnClickListener(this);

        if (Add_Seminar_Activity.seminardetailsitem.getFromdate() != null) {
            fromdate.setText(Add_Seminar_Activity.seminardetailsitem.getFromdate());
        }
        if (Add_Seminar_Activity.seminardetailsitem.getTodate() != null) {
            todate.setText(Add_Seminar_Activity.seminardetailsitem.getTodate());
        }

        //Overview function
        ed_titel = (EditText) view.findViewById(R.id.ed_title);
        ed_tagline = (EditText) view.findViewById(R.id.ed_tagline);
        ed_seminardesc = (EditText) view.findViewById(R.id.ed_seminardesc);
        btn_overviewsave = (Button) view.findViewById(R.id.btn_overviewsave);
        btn_overviewsave.setOnClickListener(this);

        if (Add_Seminar_Activity.seminardetailsitem.getSeminar_name() != null) {
            ed_titel.setText(Add_Seminar_Activity.seminardetailsitem.getSeminar_name());
        }
        if (Add_Seminar_Activity.seminardetailsitem.getTagline() != null) {
            ed_tagline.setText(Add_Seminar_Activity.seminardetailsitem.getTagline());
        }
        if (Add_Seminar_Activity.seminardetailsitem.getSeminar_description() != null) {
            ed_seminardesc.setText(Add_Seminar_Activity.seminardetailsitem.getSeminar_description());
        }

        //facilities function
        rv_facilities = (RecyclerView) view.findViewById(R.id.rv_facilities);

        btn_facilitysave = (Button) view.findViewById(R.id.btn_facilitysave);
        btn_facilitysave.setOnClickListener(this);

        //photo function
        btn_photosave = (Button) view.findViewById(R.id.btn_photosave);
        add_photo = (Button) view.findViewById(R.id.btn_addphoto);
        add_photo.setOnClickListener(onAddImageClickListener);
        btn_photosave.setOnClickListener(this);
        linear_container = (LinearLayout) view.findViewById(R.id.image_container);
        arrayAllImagePath = new ArrayList<String>();
        imagecount = 0;

        //set location data
        spCountry = (Spinner) view.findViewById(R.id.spi_country);
        spState = (Spinner) view.findViewById(R.id.spi_state);
        spCity = (Spinner) view.findViewById(R.id.spi_city);
        address = (EditText) view.findViewById(R.id.seminaraddress);
        zipcode = (EditText) view.findViewById(R.id.zipcode_name);
        btn_locsave = (Button) view.findViewById(R.id.btn_locationsave);
        btn_locsave.setOnClickListener(this);

        if (Add_Seminar_Activity.seminardetailsitem.getSeminar_address() != null && !Add_Seminar_Activity.seminardetailsitem.getSeminar_address().equalsIgnoreCase("")) {
            address.setText(Add_Seminar_Activity.seminardetailsitem.getSeminar_address());
        }
        if (Add_Seminar_Activity.seminardetailsitem.getZipcode() != null && !Add_Seminar_Activity.seminardetailsitem.getZipcode().equalsIgnoreCase("")) {
            zipcode.setText(Add_Seminar_Activity.seminardetailsitem.getZipcode());
        }
        int j = 0;
        DisplayDetail();
        if (Add_Seminar_Activity.arrayList.size() != 0) {
            for (i = 0; i < Add_Seminar_Activity.arrayList.size(); i++) {
                addView(i);
            }

        }

        if (mPreferenceSettings.getEdit()) {
            setOutletdetail();
        }
        return view;
    }

    //display layout when user select list
    public void DisplayDetail() {

        if (value.equalsIgnoreCase("calender")) {
            rl_datedetail.setVisibility(View.VISIBLE);
        } else if (value.equalsIgnoreCase("contact")) {
            rl_contact.setVisibility(View.VISIBLE);
        } else if (value.equalsIgnoreCase("overview")) {
            rl_overview.setVisibility(View.VISIBLE);
        } else if (value.equalsIgnoreCase("photo")) {
            rl_photo.setVisibility(View.VISIBLE);
        } else if (value.equalsIgnoreCase("facilities")) {
            rl_facilities.setVisibility(View.VISIBLE);
            User_data();

        } else if (value.equalsIgnoreCase("location")) {
            rl_location.setVisibility(View.VISIBLE);
            GetCountry();
        }
    }
    //used for calender

    public static boolean saveImage(Bitmap bitmap, String pathToSave) {
        boolean result = false;
        String prefix = "file://";
        Log.e("pathToSave", pathToSave + "");
        if (pathToSave.contains(prefix))
            pathToSave = pathToSave.substring(prefix.length());
        File file = new File(pathToSave);
        Log.e("saveImage", "pathToSave : " + pathToSave);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /*Convert to mili to date*/

    public void showDatePicker() {
        Date d = new Date();
        CharSequence s = DateFormat.format("yyyy-MM-dd", d.getTime());

        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(this,
                now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        dpd.setThemeDark(false);
        dpd.vibrate(true);
        dpd.setMinDate(now);

//        dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");


    }

    public void showTimePicker() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(this,
                now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
        tpd.setThemeDark(false);
        tpd.vibrate(true);
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("TimePicker", "Dialog was cancelled");
            }
        });

        tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String dayString = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
        String monthString = ++monthOfYear < 10 ? "0" + monthOfYear : ""
                + monthOfYear;
//
        if (Flag.equalsIgnoreCase("1")) {

            strSetDate = year + "-" + monthString + "-" + dayString;
            flagtime = "1";
            showTimePicker();
        } else if (Flag.equalsIgnoreCase("2")) {

            strSetDate = year + "-" + monthString + "-" + dayString;
            flagtime = "2";
            showTimePicker();
        }
    }

    //set location details

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        strSetTime = hourString + ":" + minuteString;

        if (flagtime.equalsIgnoreCase("1")) {
            if (fromdate != null) {
                fromdate.setText(strSetDate + " " + strSetTime);
            }
        } else if (flagtime.equalsIgnoreCase("2")) {
            if (todate != null) {
                todate.setText(strSetDate + " " + strSetTime);
            }
        }


    }


    private void GetCountry() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        String url = AppUrl.URL_COUNTRY;
        StringRequest countryreq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.e("Country Response", response);

                try {
                    JSONObject responseobj = new JSONObject(response);
                    JSONObject countrydetail = responseobj.getJSONObject("country_detail");

                    int Status_code = countrydetail.getInt("status_code");
                    String Message = countrydetail.getString("message");
                    countrylist = new ArrayList<CountryModel>();
                    if (Status_code == 1) {
                        JSONArray Countryarray = countrydetail.getJSONArray("country_detail");
                        for (int i = 0; i < Countryarray.length(); i++) {
                            JSONObject Countryarrayobj = Countryarray.getJSONObject(i);

                            CountryModel countryModel = new CountryModel();

                            countryModel.setId(Countryarrayobj.getInt("id"));
                            countryModel.setSortname(Countryarrayobj.getString("sortname"));
                            countryModel.setName(Countryarrayobj.getString("name"));
                            Log.e("Country Name", Countryarrayobj.getString("name"));
                            countrylist.add(countryModel);
                            Log.e("countrylist size", countrylist.size() + "");
                        }
                        SetCountrySpinner();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("Country Response", error.toString());

            }
        });
        countryreq.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance(getActivity()).addToRequestQueue(countryreq);
    }

    public ArrayList<String> getCounrtyNameList() {

        ArrayList<String> countryName = new ArrayList<>();
        for (int i = 0; i < countrylist.size(); i++) {
            countryName.add(countrylist.get(i).getName());
        }
        return countryName;
    }

    public String getData(int id, int type) {
        String brea = "";
        if (type == 1 && countrylist != null && countrylist.size() > 0) {
            for (int i = 0; i < countrylist.size(); i++) {
                if (countrylist.get(i).getId() == id) {
                    brea = countrylist.get(i).getName();
                    break;

                }
            }
        } else if (type == 2 && statelist != null && statelist.size() > 0) {
            for (int i = 0; i < statelist.size(); i++) {
                if (statelist.get(i).getId() == id) {
                    brea = statelist.get(i).getName();
                    break;
                }
            }
        } else if (type == 3 && citylist != null && citylist.size() > 0) {
            for (int i = 0; i < citylist.size(); i++) {
                if (citylist.get(i).getId() == id) {
                    brea = citylist.get(i).getName();
                    break;
                }
            }
        }

        return brea;
    }

    private void SetCountrySpinner() {
        ArrayAdapter a1 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, getCounrtyNameList());

        spCountry.setAdapter(a1);
        if (Add_Seminar_Activity.seminardetailsitem.getCounty() != null && Add_Seminar_Activity.seminardetailsitem.getCounty().length() > 0) {
            if (getData(Integer.parseInt(Add_Seminar_Activity.seminardetailsitem.getCounty()), 1).length() > 3)
                spCountry.setSelection(a1.getPosition(getData(Integer.parseInt(Add_Seminar_Activity.seminardetailsitem.getCounty()), 1)));
        }
        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.e("position", position + "");
                Log.e("in spi countrylist size", countrylist.size() + "");
                if (position > -1) {
                    countryPos = position;
                    countryId = countrylist.get(position).getId();

                    Log.e("Country SelectedId", position + "");
                    Log.e("Countryid", "" + countryId);

                    if (statecountryId == countryId && countrylist != null
                            && countrylist.size() > 0) {
                        Log.e("In If block", statelist.size() + "");
                        SetStateSpinner();
                    } else {
                        statecountryId = countrylist.get(position).getId();
                        Log.e("In else block", countryId + "" + "statecountryId ::" + statecountryId);
                        GetState(countryId);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (countryPos != -1 && countryPos > 0) {
            Log.e("countryPos", countryPos + "");
            spCountry.setSelection(countryPos);
        }
    }

    public ArrayList<String> getStateNameList() {

        ArrayList<String> stateName = new ArrayList<>();
        for (int i = 0; i < statelist.size(); i++) {
            stateName.add(statelist.get(i).getName());
        }
        return stateName;
    }

    private void SetStateSpinner() {
        ArrayAdapter a1 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, getStateNameList());
        if (Add_Seminar_Activity.seminardetailsitem.getState() != null && Add_Seminar_Activity.seminardetailsitem.getState().length() > 0) {
            if (getData(Integer.parseInt(Add_Seminar_Activity.seminardetailsitem.getState()), 2).length() > 3) {
                spState.setSelection(a1.getPosition(getData(Integer.parseInt(Add_Seminar_Activity.seminardetailsitem.getState()), 2)));
            } else {
                spState.setSelection(0);
            }
        } else {
            spState.setSelection(0);
        }
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.e("position", position + "");
                Log.e("in spi statelist size", statelist.size() + "");
                if (position > -1) {
                    statePos = position;
                    stateId = statelist.get(position).getId();

                    Log.e("State SelectedId", position + "");

                    cityStateId = statelist.get(position).getId();
                    Log.e("In else block", stateId + "" + "cityStateId ::" + cityStateId);
                    GetCity(stateId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spState.setAdapter(a1);

    }

    public ArrayList<String> getCityNameList() {
        ArrayList<String> cityName = new ArrayList<>();
        for (int i = 0; i < citylist.size(); i++) {
            cityName.add(citylist.get(i).getName());
        }
        return cityName;
    }

    private void setCityspinner() {
        ArrayAdapter a1 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, getCityNameList());
        if (Add_Seminar_Activity.seminardetailsitem.getCity() != null && Add_Seminar_Activity.seminardetailsitem.getCity().length() > 0) {
            if (getData(Integer.parseInt(Add_Seminar_Activity.seminardetailsitem.getCity()), 3).length() > 3) {
                spCity.setSelection(a1.getPosition(getData(Integer.parseInt(Add_Seminar_Activity.seminardetailsitem.getCity()), 3)));
            } else {
                spCity.setSelection(0);
            }
        } else {
            spCity.setSelection(0);
        }
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.e("position", position + "");
                Log.e("in spi citylist size", citylist.size() + "");
                if (position > -1) {
                    cityPos = position;
                    cityId = citylist.get(position).getId();

                    Log.e("City SelectedId", position + "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spCity.setAdapter(a1);
    }


    private void GetCity(final int selectedId) {
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading.....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        String url = AppUrl.URL_CITY;
        StringRequest cityreq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("City Response", response);

                try {
                    mProgressDialog.dismiss();
                    JSONObject responseobj = new JSONObject(response);
                    JSONObject citydetail = responseobj.getJSONObject("city_detail");

                    int Status_code = citydetail.getInt("status_code");
                    String Message = citydetail.getString("message");

                    citylist = new ArrayList<CityModel>();
                    if (Status_code == 1) {
                        JSONArray cityArray = citydetail.getJSONArray("city_detail");

                        for (int k = 0; k < cityArray.length(); k++) {
                            JSONObject cityobj = cityArray.getJSONObject(k);

                            CityModel cityModel = new CityModel();
                            cityModel.setId(cityobj.getInt("id"));
                            cityModel.setName(cityobj.getString("name"));

                            Log.e("city Name", cityobj.getString("name"));
                            //   cityName.add(cityobj.getString("name"));
                            citylist.add(cityModel);
                            setCityspinner();
                        }
                    } else if (Status_code == 0) {
                        Toast.makeText(getActivity(), "no city found", Toast.LENGTH_SHORT).show();
                        citylist.clear();
                        mProgressDialog.dismiss();

                    }
                } catch (JSONException e) {
                    mProgressDialog.dismiss();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Log.e("City Response", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("state_id", selectedId + "");
                return params;
            }
        };
        cityreq.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance(getActivity()).addToRequestQueue(cityreq);
    }

    private void GetState(final int selectedId) {
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading.....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        String url = AppUrl.URL_STATE;
        StringRequest Statereq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("State Response", response);

                try {
                    mProgressDialog.dismiss();
                    JSONObject responseobj = new JSONObject(response);
                    JSONObject Statedetail = responseobj.getJSONObject("state_detail");

                    int Status_code = Statedetail.getInt("status_code");
                    String Message = Statedetail.getString("message");
                    statelist = new ArrayList<StateModel>();
                    if (Status_code == 1) {
                        JSONArray StateArray = Statedetail.getJSONArray("state_detail");

                        for (int j = 0; j < StateArray.length(); j++) {
                            JSONObject Stateobj = StateArray.getJSONObject(j);

                            StateModel stateModel = new StateModel();

                            stateModel.setName(Stateobj.getString("name"));
                            stateModel.setId(Stateobj.getInt("id"));


                            Log.e("State Name", Stateobj.getString("name"));
                            //     StateName.add(Stateobj.getString("name"));
                            statelist.add(stateModel);
                            SetStateSpinner();
                        }

                    } else if (Status_code == 0) {
                        mProgressDialog.dismiss();
                        statelist.clear();
                    }

                } catch (JSONException e) {
                    mProgressDialog.dismiss();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("State Response", error.toString());
                mProgressDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("country_id", selectedId + "");
                return params;
            }
        };
        Statereq.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance(getActivity()).addToRequestQueue(Statereq);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_contactsave:
                s_hostname = host_name.getText().toString().trim();
                s_contactemail = contact_email.getText().toString().trim();
                s_contactno = contact_no.getText().toString().trim();
                if (!CommonUtils.isValidEmail(s_contactemail) || s_contactemail.equalsIgnoreCase("")) {
                    contact_email.setError(getResources().getString(R.string.rg_emailvalid));

                } else {
                    Add_Seminar_Activity.seminardetailsitem.setSeminar_host_name(s_hostname);
                    Add_Seminar_Activity.seminardetailsitem.setContact_email(s_contactemail);
                    Add_Seminar_Activity.seminardetailsitem.setContact_no(s_contactno);
//                    callbackRent.onNextSelected(
//                            Add_Seminar_Activity.Fragments.DETAILS_LIST, true);
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                    android.support.v4.app.FragmentManager fManager = getFragmentManager();
                    android.support.v4.app.FragmentTransaction fTransaction = fManager.beginTransaction();
                    AddSeminarDetailsList_Fragment contact = new AddSeminarDetailsList_Fragment();
                    fTransaction.add(R.id.fragment_container, contact, "Addseminar_Fragment");
                    fTransaction.commit();
                }
                break;
            case R.id.btn_clnsave:
                s_fromdate = fromdate.getText().toString().trim();
                s_todate = todate.getText().toString().trim();
                Log.e("dater", "" + s_todate);
                if (s_fromdate.equals("")) {
                    fromdate.setError(getResources().getString(R.string.booking_fromdate_error));
                } else if (s_todate.equals("")) {
                    todate.setError(getResources().getString(R.string.booking_todate_error));

                } else {

//                    mPreferenceSettings.setIS_VISIBLE(true);
                    Add_Seminar_Activity.seminardetailsitem.setFromdate(s_fromdate);
                    Add_Seminar_Activity.seminardetailsitem.setTodate(s_todate);

                    Log.e("check", "" + s_fromdate + "   " + s_todate);

                    Log.e("todate", "" + mPreferenceSettings.getTodate());
//                    callbackRent.onNextSelected(
//                            Add_Seminar_Activity.Fragments.DETAILS_LIST, true);
//
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    AddSeminarDetailsList_Fragment Add = new AddSeminarDetailsList_Fragment();
                    fragmentTransaction.add(R.id.fragment_container, Add, "Addseminar_Fragment");
                    fragmentTransaction.commit();
                }
                break;

            case R.id.add_fromdate:
                Flag = "1";
                showDatePicker();
                break;

            case R.id.add_enddate:
                Flag = "2";
                showDatePicker();
                break;

            case R.id.start_time:
                flagtime = "1";
                showTimePicker();
                break;

            case R.id.end_time:
                flagtime = "2";
                showTimePicker();
                break;

            //Overview button click event
            case R.id.btn_overviewsave:
                s_titel = ed_titel.getText().toString().trim();
                s_tagline = ed_tagline.getText().toString().trim();
                s_seminardesc = ed_seminardesc.getText().toString().trim();
                if (s_titel.equals("")) {
                    ed_titel.setError("Enter seminar title");
                } else if (s_tagline.equals("")) {
                    ed_tagline.setError("Enter seminar tagline");
                } else {
                    Add_Seminar_Activity.seminardetailsitem.setSeminar_name(s_titel);

                    Add_Seminar_Activity.seminardetailsitem.setTagline(s_tagline);

                    Add_Seminar_Activity.seminardetailsitem.setSeminar_description(s_seminardesc);

//                    mPreferenceSettings.setIS_VISIBLE(true);
//                    callbackRent.onNextSelected(
//                            Add_Seminar_Activity.Fragments.DETAILS_LIST, true);
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    AddSeminarDetailsList_Fragment Add = new AddSeminarDetailsList_Fragment();
                    fragmentTransaction.add(R.id.fragment_container, Add, "Addseminar_Fragment");
                    fragmentTransaction.commit();
                }
                break;

            //photo add
            case R.id.btn_photosave:
                arrayAllImagePath.removeAll(Arrays.asList(null, ""));
                countval.removeAll(Arrays.asList(null, ""));
                arrayAllImagePath.removeAll(Arrays.asList("", ""));
                countval.removeAll(Arrays.asList("", ""));
                MeettoApplication.count = countval;
                if (Add_Seminar_Activity.deleteImage != null) {
                    String[] deleteImage = Add_Seminar_Activity.deleteImage.replace("null,", "").split(",");
                    if (deleteImage != null) {
                        for (int i = 0; i < deleteImage.length; i++) {
                            Add_Seminar_Activity.editImageList.remove(deleteImage[i]);
                        }
                    }
                }
                if (Add_Seminar_Activity.deleteImageSdCard != null) {
                    String[] deleteImage = Add_Seminar_Activity.deleteImageSdCard.replace("null,", "").split(",");
                    if (deleteImage != null) {
                        for (int i = 0; i < deleteImage.length; i++) {
                            Add_Seminar_Activity.arrayList.remove(deleteImage[i]);
                        }
                    }
                }
                if (view1 != null) {
                    linear_container = (LinearLayout) view1.findViewById(R.id.image_container);
                }
                Log.e("VIEw value", view1 + " test:");
                if (arrayAllImagePath != null && arrayAllImagePath.size() > 0) {
                    for (int i = 0; i < arrayAllImagePath.size(); i++) {
                        if (arrayAllImagePath.get(i).contains("file://")) {
                            arraylist.add(arrayAllImagePath.get(i).replace("file://", ""));
                            Log.e("arrayAllImagePath", "=" + arraylist);
                        } else {
                            arraylist.add(arrayAllImagePath.get(i));
                        }
                        Log.e("count", "" + MeettoApplication.count);
                        Log.e("count", "" + MeettoApplication.count.size());
                    }
                }
//                callbackRent.onNextSelected(
//                        Add_Seminar_Activity.Fragments.DETAILS_LIST, true);
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AddSeminarDetailsList_Fragment Add = new AddSeminarDetailsList_Fragment();
                fragmentTransaction.add(R.id.fragment_container, Add, "Addseminar_Fragment");
                fragmentTransaction.commit();
                break;
            //facility button click event
            case R.id.btn_facilitysave:
                String seminarfacility = "";
                if (MeettoApplication.facility != null && MeettoApplication.facility.size() > 0) {
                    for (int i = 0; i < MeettoApplication.facility.size(); i++) {
                        if (!(seminarfacility.contains(MeettoApplication.facility.get(i) + ","))) {
                            seminarfacility = seminarfacility + MeettoApplication.facility.get(i) + ",";
                            Add_Seminar_Activity.seminardetailsitem.setSetFacilitiesPref(seminarfacility);
                        }
                    }
                } else {
                    Add_Seminar_Activity.seminardetailsitem.setSetFacilitiesPref("");
                }

//                callbackRent.onNextSelected(
//                        Add_Seminar_Activity.Fragments.DETAILS_LIST, true);
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                android.support.v4.app.FragmentManager fragmentManagerf = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransactionf = fragmentManagerf.beginTransaction();
                AddSeminarDetailsList_Fragment facility = new AddSeminarDetailsList_Fragment();
                fragmentTransactionf.add(R.id.fragment_container, facility, "Addseminar_Fragment");
                fragmentTransactionf.commit();
                break;

            //location set
            case R.id.btn_locationsave:
                s_address = address.getText().toString().trim();
                s_zipcode = zipcode.getText().toString().trim();
                Add_Seminar_Activity.seminardetailsitem.setSeminar_address(s_address);
                Add_Seminar_Activity.seminardetailsitem.setZipcode(s_zipcode);

                Add_Seminar_Activity.seminardetailsitem.setCounty(String.valueOf(countryId));
                Add_Seminar_Activity.seminardetailsitem.setState(String.valueOf(stateId));
                Add_Seminar_Activity.seminardetailsitem.setCity(String.valueOf(cityId));
                Country_id = Add_Seminar_Activity.seminardetailsitem.getCounty();
//                State_id = Add_Seminar_Activity.seminardetailsitem.getState();
//                City_id = Add_Seminar_Activity.seminardetailsitem.getCity();
//                mPreferenceSettings.setIS_VISIBLE(true);

//  callbackRent.onNextSelected(

//                        Add_Seminar_Activity.Fragments.DETAILS_LIST, true);
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                android.support.v4.app.FragmentManager fragmentManager1 = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                AddSeminarDetailsList_Fragment location = new AddSeminarDetailsList_Fragment();
                fragmentTransaction1.add(R.id.fragment_container, location, "Addseminar_Fragment");
                fragmentTransaction1.commit();
                break;
        }
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
                    callbackRent.onNextSelected(Add_Seminar_Activity.Fragments.DEATAILS_OVERVIEW, false);
                    return true;
                }
                return false;
            }
        });
    }

    public void showTryAgainAlert(String title, String message) {
        CommonUtils.showAlertWithNegativeButton(getActivity(), title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                if (CommonUtils.isNetworkAvailable())
                    User_data();
                else
                    CommonUtils.showToast("" + getResources().getString(R.string.network3));

            }
        });
    }

    public void setOutletdetail() {
        if (!Add_Seminar_Activity.seminardetailsitem.getSeminar_host_name().equalsIgnoreCase("")) {
            host_name.setText(Add_Seminar_Activity.seminardetailsitem.getSeminar_host_name());
        }
        if (!Add_Seminar_Activity.seminardetailsitem.getContact_email().equalsIgnoreCase("")) {
            contact_email.setText(Add_Seminar_Activity.seminardetailsitem.getContact_email());
        }
        if (!Add_Seminar_Activity.seminardetailsitem.getContact_no().equalsIgnoreCase("")) {
            contact_no.setText(Add_Seminar_Activity.seminardetailsitem.getContact_no());
        }

        if (!Add_Seminar_Activity.seminardetailsitem.getCounty().equalsIgnoreCase("0")) {
            Country_id = Add_Seminar_Activity.seminardetailsitem.getCounty();
            Log.e("Country_id ", "=" + Country_id);
        }
        if (!Add_Seminar_Activity.seminardetailsitem.getState().equalsIgnoreCase("0")) {
            State_id = Add_Seminar_Activity.seminardetailsitem.getState();
            Log.e("state_id ", "=" + State_id);

        }
        if (!Add_Seminar_Activity.seminardetailsitem.getCity().equalsIgnoreCase("0")) {
            City_id = Add_Seminar_Activity.seminardetailsitem.getCity();
            Log.e("city_id ", "=" + City_id);
        }
        if (!Add_Seminar_Activity.seminardetailsitem.getFromdate().equalsIgnoreCase("0"))
            fromdate.setText(Add_Seminar_Activity.seminardetailsitem.getFromdate());
        if (!Add_Seminar_Activity.seminardetailsitem.getTodate().equalsIgnoreCase("0"))
            todate.setText(Add_Seminar_Activity.seminardetailsitem.getTodate());
        if (!Add_Seminar_Activity.seminardetailsitem.getFromtime().equalsIgnoreCase("0"))
            starttime.setText(Add_Seminar_Activity.seminardetailsitem.getFromtime());
        if (!Add_Seminar_Activity.seminardetailsitem.getTotime().equalsIgnoreCase("0"))
            endtime.setText(Add_Seminar_Activity.seminardetailsitem.getTotime());

        ed_titel.setText(Add_Seminar_Activity.seminardetailsitem.getSeminar_name());
        ed_tagline.setText(Add_Seminar_Activity.seminardetailsitem.getTagline());
        ed_seminardesc.setText(Add_Seminar_Activity.seminardetailsitem.getSeminar_description());

        address.setText(Add_Seminar_Activity.seminardetailsitem.getSeminar_address());
        zipcode.setText(Add_Seminar_Activity.seminardetailsitem.getZipcode());

        if (Add_Seminar_Activity.editImageList != null) {
            for (int t = 0; t < Add_Seminar_Activity.editImageList.size(); t++) {
                addView1(t);
                Log.e("editImageList", +Add_Seminar_Activity.editImageList.size() + "");
            }
        }

    }


    public DisplayImageOptions getDisplayImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.nophoto)
                .showImageOnFail(R.drawable.nophoto)
                .showImageOnLoading(R.drawable.nophoto)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(0))
                .build();
        return options;
    }

    void setFacility() {
        if (userfacility.size() > 0 || userfacility != null) {
            if (userfacilitiy_adapter == null) {

                rv_facilities.setHasFixedSize(true);
                //Set RecyclerView type according to intent value
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

                rv_facilities.setHasFixedSize(true);
                rv_facilities.setLayoutManager(gridLayoutManager);
                userfacilitiy_adapter = new Userfacilitiy_Adapter(getActivity(), userfacility);
                rv_facilities.setAdapter(userfacilitiy_adapter);
            } else {
                userfacilitiy_adapter.modifyDataSet(userfacility);
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
                        user_data_object.setFacilitie(jsonObject.getJSONArray(Jsonkey.facility));
                    } catch (JSONException e) {
                        user_data_object.setSemtype(null);
                    }

                    userfacility = user_data_object.getFacilitie();
                    setFacility();

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
                showTryAgainAlert("" + getResources().getString(R.string.network1) + "", getResources().getString(R.string.network2) + "");
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

    private void selectImage() {

        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setCancelable(false);
        // add items to the action Bar
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    dispatchTakePictureIntent();
                } else if (items[item].equals("Choose from Library")) {
                    dispatchSelectPictureIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(true);
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, ACTION_TAKE_PHOTO);
            } else {
                Toast.makeText(getActivity(), "Opps! Storage is not available.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchSelectPictureIntent() {

        File photoFile = null;
        try {
            photoFile = createImageFile(true);
        } catch (IOException ex) {
            // Error occurred while creating the File
            ex.printStackTrace();
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select File"), ACTION_SELECT_PHOTO);
        } else {
            Toast.makeText(getActivity(), "Opps! Storage is not available.", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile(boolean isCurrent) throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = null;

        if (storageDir == null)
            storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = null;
        if (storageDir.exists()) {
            try {
                image = new File(storageDir, "Meeto" + timeStamp + ".jpg");
                // Save a file: path for use with ACTION_VIEW intents

                if (isCurrent)
                    mCurrentPhotoPath = image.getAbsolutePath();
            } catch (Exception e) {

            }
        }
        return image;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("image", mCurrentPhotoPath);
        outState.putParcelable("file_uri", fileUri);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ACTION_TAKE_PHOTO:
                    handlePhoto(mCurrentPhotoPath);
                    break;

                case ACTION_SELECT_PHOTO:
                    Uri selectedImageUri = data.getData();
                    String selectedPath;
                    try {
                        String[] projection = {MediaStore.MediaColumns.DATA};
                        CursorLoader cursorLoader = new CursorLoader(getActivity(), selectedImageUri, projection, null, null,
                                null);
                        Cursor cursor = cursorLoader.loadInBackground();
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                        cursor.moveToFirst();
                        selectedPath = cursor.getString(column_index);
                    } catch (Exception e) {
                        e.printStackTrace();
                        selectedPath = "";
                    }

                    if (selectedPath == "")
                        try {
                            selectedPath = selectedImageUri.toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                            selectedPath = "";
                        }

                    Log.e("selectedImageUri", selectedPath.toString());
                    handlePhoto(selectedPath);
                    break;
            }
        }
    }

    private void handlePhoto(String path) {
        Log.e("Inside Handlephoto", path);
        showRotateImageDialog(path);
    }

    public void showRotateImageDialog(final String path) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Choose Action");
        LayoutInflater inflater = getActivity().getLayoutInflater().from(getActivity());
        View view = inflater.inflate(R.layout.activity_custom_dialog, null);

        dialog.setContentView(view);
        final ImageView dialogImage = (ImageView) view.findViewById(R.id.selectedImage);

        dialogImage.setImageURI(Uri.parse(path));

        Log.e("hilength at", "Height: "
                + imageViewHeight + " Width: "
                + imageViewWidth + ":  path " + path);

        Button RotateButton = (Button) view.findViewById(R.id.RotateButton);
        Button DoneButton = (Button) view.findViewById(R.id.DoneButton);


        if (!setPicUniversalImageLoader(path, dialogImage, false, false)) {

            dialog.cancel();
        }
        DoneButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bitmap rotatedBitmap = ((BitmapDrawable) dialogImage.getDrawable()).getBitmap();

                if (saveImage(rotatedBitmap, mCurrentPhotoPath)) {
                    Log.e("Done click ", " : " + mCurrentPhotoPath);
                    setPicUniversalImageLoader(mCurrentPhotoPath, null, true, true);
                    Add_Seminar_Activity.arrayList.add(mCurrentPhotoPath);
                    addView(Add_Seminar_Activity.arrayList.size() - 1);
                } else {
                    Toast.makeText(getActivity(),
                            "Image has not been saved, please try again!",
                            Toast.LENGTH_LONG).show();
                }
                rotatedBitmap = null;
                mCurrentPhotoPath = null;
                dialog.cancel();
                if (i == 0) {
                    rotet_count
                            = "0";
                } else if (i == 1) {
                    rotet_count = "90";
                } else if (i == 2) {
                    rotet_count = "180";
                } else if (i == 3) {
                    rotet_count = "360";
                }
                countval.add(rotet_count);
                i = 0;
                Log.e("count", "" + MeettoApplication.count);
            }
        });

        RotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap dialog_imageBitmap = ((BitmapDrawable) dialogImage.getDrawable()).getBitmap();
                int bitmapWidth = dialog_imageBitmap.getWidth();
                int bitmapHeight = dialog_imageBitmap.getHeight();
                Matrix matrix = new Matrix();
                matrix.preRotate(90);
                if (i < 4) {
                    i = i + 1;
                } else if (i == 4) {
                    i = 0;
                }
                dialog_imageBitmap = Bitmap.createBitmap(dialog_imageBitmap, 0,
                        0, bitmapWidth, bitmapHeight, matrix, true);
                BitmapDrawable b = new BitmapDrawable(dialog_imageBitmap);
                dialogImage.setImageBitmap(dialog_imageBitmap);
                dialog_imageBitmap = null;
            }
        });
        dialog.show();
    }

    public boolean setPicUniversalImageLoader(String path, ImageView imageView, boolean needAddNewView, boolean needToAddPath) {

        setDisplayOption();
        if (imageView == null) {
            Log.e("image", "" + Image_select);
            imageView = Image_select;
        }
        if (imageView != null) {
            if (path != null && path.length() > 0) {
                Log.e("setPicUnivermageLoader", " : " + path);
                if (!path.contains("file://")) {
                    path = "file://" + path;
                }
                imageView.setTag(path);
                try {
                    ImageLoader.getInstance().displayImage(path, imageView, options);
                    if (needToAddPath && !arrayAllImagePath.contains(path)) {
                        arrayAllImagePath.add(path);
                        Log.e("Array Size After Add", arrayAllImagePath.size() + "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this.getActivity(), "Please select proper image file.", Toast.LENGTH_LONG).show();
                    return false;
                }
            } else {
                Toast.makeText(this.getActivity(), "Please select proper image file.", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public void setDisplayOption() {
        if (options == null)
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.ic_launcher)
                    .showImageForEmptyUri(R.mipmap.ic_launcher)
                    .showImageOnFail(R.mipmap.ic_launcher)
                    .cacheInMemory(false)
                    .cacheOnDisk(false)
                    .considerExifParams(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
//                    .displayer(new RoundedBitmapDisplayer(20))
                    .build();
    }

    ImageView img;

    void addView(final int pos) {
        itemList = null;
        itemList = getActivity().getLayoutInflater().inflate(R.layout.row_show_photo, linear_container, false);
        img = (ImageView) itemList.findViewById(R.id.iv_photo);
        img.setTag(pos);
        img.setImageBitmap(filepathTobitmap(Add_Seminar_Activity.arrayList.get(pos)));

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage("Are you sure, You wanted to delete this image ?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Add_Seminar_Activity.deleteImageSdCard = Add_Seminar_Activity.deleteImageSdCard + "," + Add_Seminar_Activity.arrayList.get(pos);
                                v.setVisibility(View.GONE);
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        linear_container.addView(itemList);
    }

    public static Bitmap filepathTobitmap(String filepath) {
        File imgFile = new File(filepath);
        Bitmap myBitmap = null;
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return myBitmap;
    }

    void addView1(final int pos) {
        itemList = null;

        itemList = getActivity().getLayoutInflater().inflate(
                R.layout.row_show_photo, linear_container, false);
        img = (ImageView) itemList.findViewById(R.id.iv_photo);

        img.setTag(pos);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
        imageLoader.getInstance().displayImage(Add_Seminar_Activity.editImageList.get(pos) + "", img, options);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage("Are you sure, You wanted to delete this image ?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Add_Seminar_Activity.deleteImage = Add_Seminar_Activity.deleteImage + "," + Add_Seminar_Activity.editImageList.get(pos);
                                v.setVisibility(View.GONE);
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        linear_container.addView(itemList);
    }
}