package creadigol.com.Meetto;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import Adapters.SimilerSeminar_Adapter;
import Helper.TextSliderView;
import Models.SeminarDetailsarrayitem;
import Models.Seminardetailsitem;
import Models.SimilerSeminaritem;
import Utils.AppUrl;
import Utils.CommonUtils;
import Utils.Jsonkey;
import Utils.ParamsKey;
import Utils.PreferenceSettings;

/**
 * Created by Creadigol on 05-09-2016.
 */
public class SeminarDetail_Activity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private SliderLayout mDemoSlider;
    String seminar_id, seminar_name, st_review;
    public static final String EXTRA_KEY_SEMINAR_ID = "outletid";
    SimilerSeminar_Adapter simileradapter;
    ImageView img_address;
    ArrayList<SeminarDetailsarrayitem> seminarDetailsarrayitems = new ArrayList<>();
    LinearLayout linear_container;
    private static final String TAG = SeminarDetail_Activity.class.getSimpleName();
    PreferenceSettings mPreferenceSettings;
    int imagesize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();
        if (mPreferenceSettings.getLUNGAUGE()) {
            MeettoApplication.language("ja");
        } else {
            MeettoApplication.language("en");
        }
        setContentView(R.layout.activity_seminatdetails);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setExpandedTitleGravity(View.TEXT_ALIGNMENT_CENTER);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_place_details);
        appBarLayout.addOnOffsetChangedListener(this);
        linear_container = (LinearLayout) findViewById(R.id.ll_add_review);
        Toolbar();
        try {
            Bundle extras = getIntent().getExtras();
            seminar_id = extras.getString("outletid");
            seminar_name = extras.getString("seminar_name");

            reqseminardetail();
            //getOutletDetails();
        } catch (Exception e) {
            Log.e("Exception", " " + e);
        }


    }

    public void Toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        TextView tvTitle = (TextView) findViewById(R.id.toolbar_text);
        if (i == (-maxScroll)) {
            tvTitle.setText(CommonUtils.getCapitalize(seminar_name));
        } else {
            tvTitle.setText("");
        }

    }


    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
//         mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    public void imagesilde() {
        ImageView Leftslide = (ImageView) findViewById(R.id.leftslide);
        ImageView Rightslide = (ImageView) findViewById(R.id.rightslide);

        Leftslide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = mDemoSlider.getCurrentPosition();
                if (tab > 0) {
                    tab--;
                    Log.e("tab--", "" + tab);
                    mDemoSlider.setCurrentPosition(tab);
                } else if (tab == 0) {
                    Log.e("tabelse--", "" + tab);
                    mDemoSlider.setCurrentPosition(tab);
                }
            }
        });

        // Images right navigatin
        Rightslide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab1 = mDemoSlider.getCurrentPosition();
                Log.e("tababove", "" + tab1);
                if (tab1 == 0) {
                    Log.e("tabelse++", "" + tab1);
                    mDemoSlider.setCurrentPosition(tab1);
                } else if (tab1 < (imagesize - 1)) {
                    tab1++;
                    Log.e("tab", "" + tab1);
                    mDemoSlider.setCurrentPosition(tab1);
                }
            }
        });
    }

    void setseminatlist(ArrayList<SimilerSeminaritem> similerlist) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        TextView no_seminar = (TextView) findViewById(R.id.noseminar);
        if (similerlist != null && similerlist.size() > 0) {

            if (simileradapter == null) {
                recyclerView.setVisibility(View.VISIBLE);
                no_seminar.setVisibility(View.GONE);
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setLayoutManager(new LinearLayoutManager(SeminarDetail_Activity.this, LinearLayoutManager.HORIZONTAL, false));
                simileradapter = new SimilerSeminar_Adapter(this, similerlist);
                recyclerView.setAdapter(simileradapter);
            } else {

                simileradapter.modifyDataSet(similerlist);
            }
        } else {
            no_seminar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void reqseminardetail() {
        final ProgressDialog mProgressDialog = new ProgressDialog(SeminarDetail_Activity.this);
        mProgressDialog.setMessage("Loading.....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, AppUrl.URL_SEMINAR_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgressDialog.dismiss();
                try {
                    Log.e(TAG, "Response: " + response);
                    JSONObject jsonObject = new JSONObject(response);
                    Seminardetailsitem seminardetailsitem = new Seminardetailsitem();

                    try {
                        seminardetailsitem.setStatus_code(jsonObject.getInt(Jsonkey.statusCode));
                    } catch (JSONException e) {
                        seminardetailsitem.setStatus_code(0);
                    }

                    seminardetailsitem.setMessage(jsonObject.optString(Jsonkey.message));


                    if (seminardetailsitem.getStatus_code() == 1) {
                        JSONObject jsonObjectOutlet = jsonObject.getJSONObject(Jsonkey.seminardeatil_key);

                        seminardetailsitem.setSeminar_id(jsonObjectOutlet.optString(Jsonkey.seminarid));
                        seminardetailsitem.setSeminar_name(jsonObjectOutlet.optString(Jsonkey.seminar_name));
                        seminardetailsitem.setSeminar_address(jsonObjectOutlet.optString(Jsonkey.seminar_address));
                        seminardetailsitem.setSeminar_host_name(jsonObjectOutlet.optString(Jsonkey.seminar_hostname));
                        seminardetailsitem.setSeminar_description(jsonObjectOutlet.optString(Jsonkey.seminar_desc));
                        seminardetailsitem.setSeminar_total_seat(jsonObjectOutlet.optString(Jsonkey.seminar_seat));
                        seminardetailsitem.setSeminar_host_description(jsonObjectOutlet.optString(Jsonkey.seminar_hostdesc));
                        seminardetailsitem.setSeminar_host_pic(jsonObjectOutlet.optString(Jsonkey.seminarhostimage));
                        seminardetailsitem.setorganization(jsonObjectOutlet.optString(Jsonkey.organization));
                        seminardetailsitem.setSeminar_type(jsonObjectOutlet.optString(Jsonkey.seminar_type));
                        seminardetailsitem.setCompany_name(jsonObjectOutlet.optString(Jsonkey.companyname));
                        seminardetailsitem.setCompany_description(jsonObjectOutlet.optString(Jsonkey.company_desc));
                        seminardetailsitem.setFromdate(jsonObjectOutlet.optString(Jsonkey.fromdate));
                        seminardetailsitem.setTodate(jsonObjectOutlet.optString(Jsonkey.todates));
                        seminardetailsitem.setFromtime(jsonObjectOutlet.optString(Jsonkey.fromtime));
                        seminardetailsitem.setTotime(jsonObjectOutlet.optString(Jsonkey.totime));
                        seminardetailsitem.setContact_email(jsonObjectOutlet.optString(Jsonkey.contact_email));
                        seminardetailsitem.setContact_no(jsonObjectOutlet.optString(Jsonkey.contact_no));
                        seminardetailsitem.setTagline(jsonObjectOutlet.optString(Jsonkey.tagline));
                        seminardetailsitem.setQualification(jsonObjectOutlet.optString(Jsonkey.qualification));
                        seminardetailsitem.setZipcode(jsonObjectOutlet.optString(Jsonkey.zipcode));
                        seminardetailsitem.setCounty(jsonObjectOutlet.optString(Jsonkey.country));
                        seminardetailsitem.setState(jsonObjectOutlet.optString(Jsonkey.state));
                        seminardetailsitem.setCity(jsonObjectOutlet.optString(Jsonkey.city));
                        seminardetailsitem.setAdmin(jsonObjectOutlet.optString(Jsonkey.Admin));
                        seminardetailsitem.setAvailableseats(jsonObjectOutlet.optString(Jsonkey.Available_seat));

                        try {
                            JSONArray jsonimage = jsonObject.getJSONArray(Jsonkey.seminar_Key);
                            seminardetailsitem.setImagesitems(jsonimage);
                            Log.e("imageslide", " b" + seminardetailsitem.getImagesitems().size());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            JSONArray attendees = jsonObject.getJSONArray(Jsonkey.seminar_purpose);
                            seminardetailsitem.setattendees(attendees);
                            Log.e("attendees", " b" + seminardetailsitem.getattendees().size());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            JSONArray industry = jsonObject.getJSONArray(Jsonkey.industry);
                            seminardetailsitem.setIndustry(industry);
                            Log.e("industry", " b" + seminardetailsitem.getIndustry().size());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONArray jsonfacilities = jsonObject.getJSONArray(Jsonkey.Facilities_Key);
                            seminardetailsitem.setFacilitestems(jsonfacilities);
                            Log.e("facilities", " b" + seminardetailsitem.getFacilitiesitems().size());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            JSONArray Review = jsonObject.getJSONArray(Jsonkey.Review);
                            seminardetailsitem.setReview(Review);
                            Log.e("facilities", " b" + seminardetailsitem.getReview().size());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            JSONArray dealArray = jsonObject.getJSONArray(Jsonkey.similer_seminarKey);
                            seminardetailsitem.setSimilerList(dealArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //set outlet detail
                        setOutletdetail(seminardetailsitem);

                        mProgressDialog.dismiss();
                    } else if (seminardetailsitem.getStatus_code() == 0 || seminardetailsitem.getStatus_code() == 2) {
                        mProgressDialog.dismiss();
                        CommonUtils.showToast(seminardetailsitem.getMessage());
                        finish();
                        //showTryAgainAlert("Info", outletObject.getMessage() + " Try again!");
                    } else {
                        mProgressDialog.dismiss();

                        Log.e("Error_in", "Error_else");

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
                Log.e("Error_in", "onErrorResponse");

                showTryAgainAlert("" + getResources().getString(R.string.network1) + "", getResources().getString(R.string.network2) + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                PreferenceSettings mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();

                Map<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_SEMINAR_ID, seminar_id);
                params.put(ParamsKey.KEY_USERID, mPreferenceSettings.getUserId());
                if (mPreferenceSettings.getLUNGAUGE()) {
                    params.put(ParamsKey.KEY_USER_LANG, "ja");
                }
                if (mPreferenceSettings.getLUNGAUGE() == false) {
                    params.put(ParamsKey.KEY_USER_LANG, "en");

                }
                Log.e(TAG, "reqOutletData params: " + params.toString());

                return params;
            }
        };

        MeettoApplication.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
    }

    public void showTryAgainAlert(String title, String message) {
        CommonUtils.showAlertWithNegativeButton(SeminarDetail_Activity.this, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                if (CommonUtils.isNetworkAvailable())
                    reqseminardetail();
                else
                    CommonUtils.showToast(getResources().getString(R.string.network3));

            }
        });
    }

    public void setOutletdetail(final Seminardetailsitem seminardetails) {

        seminar_id = seminardetails.getSeminar_id();

        TextView tv_seminarname = (TextView) findViewById(R.id.txt_seminar_name);
        TextView tv_SeminarAddress = (TextView) findViewById(R.id.txt_seminaraddress);
        TextView tv_hostname = (TextView) findViewById(R.id.txt_host_name);
        TextView tv_tagline = (TextView) findViewById(R.id.txt_tagline);

        ImageView img_propertytype = (ImageView) findViewById(R.id.property_image);
        TextView tv_propertytype = (TextView) findViewById(R.id.txt_propertytype);
        ImageView img_purposetype = (ImageView) findViewById(R.id.purpose_image);
        TextView tv_purposetype = (TextView) findViewById(R.id.txt_purposetype);
        TextView tv_totalseat = (TextView) findViewById(R.id.txt_totalseat);
        TextView tv_semianr_desc = (TextView) findViewById(R.id.txt_seminar_desc);
        TextView tv_host_desc = (TextView) findViewById(R.id.txt_host_desc);
        TextView tv_host_namedesc = (TextView) findViewById(R.id.host_name);

        TextView tv_comopany_name = (TextView) findViewById(R.id.txt_companyname);

        final TextView tv_readmore = (TextView) findViewById(R.id.seminar_desc_more);
        TextView tv_fromdate = (TextView) findViewById(R.id.fromdates);
        TextView tv_todate = (TextView) findViewById(R.id.todates);
        TextView tv_fromtime = (TextView) findViewById(R.id.fromtimes);
        TextView tv_totime = (TextView) findViewById(R.id.totimes);

        TextView tv_contact_no = (TextView) findViewById(R.id.txt_contact_no);
        TextView tv_contact_email = (TextView) findViewById(R.id.txt_contact_email);


        LinearLayout ll_address = (LinearLayout) findViewById(R.id.ll_address);
        img_address = (ImageView) findViewById(R.id.img_address);
        final LinearLayout ll_readmore = (LinearLayout) findViewById(R.id.ll_readmore);

        tv_seminarname.setText(CommonUtils.getCapitalize(seminardetails.getSeminar_name()));
        tv_tagline.setText(CommonUtils.getCapitalize(seminardetails.getTagline()));
        tv_hostname.setText(CommonUtils.getCapitalize(seminardetails.getSeminar_host_name()));
        tv_semianr_desc.setText(CommonUtils.getCapitalize(seminardetails.getSeminar_description()));
        tv_host_desc.setText(CommonUtils.getCapitalize(seminardetails.getCompany_description()));
        tv_totalseat.setText(CommonUtils.getCapitalize(seminardetails.getSeminar_total_seat() + " " + getResources().getString(R.string.seminardetails_saet)));
        tv_host_namedesc.setText(getResources().getString(R.string.seminardetails_abouthost) + " " + CommonUtils.getCapitalize(seminardetails.getSeminar_host_name()));

//        if (!seminardetails.getCompany_name().equalsIgnoreCase("")) {
        tv_comopany_name.setText(CommonUtils.getCapitalize(seminardetails.getCompany_name()));
//        } else {
//            tv_comopany_name.setVisibility(View.GONE);
//        }
        if (!seminardetails.getContact_no().equalsIgnoreCase("")) {
            tv_contact_no.setText(seminardetails.getContact_no());
        } else {
            tv_contact_no.setVisibility(View.GONE);
        }
        if (!seminardetails.getContact_email().equalsIgnoreCase("")) {
            tv_contact_email.setText(seminardetails.getContact_email());
        } else {
            tv_contact_email.setVisibility(View.GONE);
        }

        //click on address to show map
        if (!seminardetails.getSeminar_address().equalsIgnoreCase("")) {
            tv_SeminarAddress.setText(CommonUtils.getCapitalize(seminardetails.getSeminar_address()));
            img_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + seminardetails.getSeminar_address());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            });

        }
        // set host image
        ImageView imvhostpic = (ImageView) findViewById(R.id.host_image);
        ImageView host_pic = (ImageView) findViewById(R.id.host_pic);

        MeettoApplication.getInstance().getImageLoader().displayImage(seminardetails.getSeminar_host_pic(), imvhostpic, getDisplayImageOptions());
        MeettoApplication.getInstance().getImageLoader().displayImage(seminardetails.getSeminar_host_pic(), host_pic, getDisplayImageOptions());


        if (seminardetails.getorganization().equalsIgnoreCase(getResources().getString(R.string.profile_Profit_Organization))) {
            img_purposetype.setImageResource(R.drawable.profit_organisation);
            tv_purposetype.setText(seminardetails.getorganization());
        } else if (seminardetails.getorganization().equalsIgnoreCase(getResources().getString(R.string.profile_non_Profit_Organization))) {
            img_purposetype.setImageResource(R.drawable.nonprofit_organisation);
            tv_purposetype.setText(seminardetails.getorganization());
        }

        if (seminardetails.getSeminar_type().equalsIgnoreCase(getResources().getString(R.string.private_office))) {
            img_propertytype.setImageResource(R.drawable.privateoffice);
            tv_propertytype.setText(seminardetails.getSeminar_type());
        } else if (seminardetails.getSeminar_type().equalsIgnoreCase(getResources().getString(R.string.Public_Place))) {
            img_propertytype.setImageResource(R.drawable.openspace);
            tv_propertytype.setText(seminardetails.getSeminar_type());
        }
        if (!seminardetails.getFromdate().equalsIgnoreCase("") && !seminardetails.getTodate().equalsIgnoreCase("")) {
            tv_fromdate.setText(getResources().getString(R.string.seminardetails_Fromdate) + ": " + getDate(Long.parseLong(seminardetails.getFromdate()), "yyyy-MM-dd hh:mm"));
            tv_todate.setText(getResources().getString(R.string.seminardetails_todate) + ": " + getDate(Long.parseLong(seminardetails.getTodate()), "yyyy-MM-dd hh:mm"));
            tv_fromtime.setText(getResources().getString(R.string.seminardetails_fromtime) + ": " + seminardetails.getFromtime());
            tv_totime.setText(getResources().getString(R.string.seminardetails_totime) + ": " + seminardetails.getTotime());
            tv_readmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv_readmore.getText().equals(getResources().getString(R.string.seminardetails_read))) {
                        tv_readmore.setText(getResources().getString(R.string.seminardetails_readless));
                        ll_readmore.setVisibility(View.VISIBLE);
                    } else {
                        tv_readmore.setText(getResources().getString(R.string.seminardetails_read));
                        ll_readmore.setVisibility(View.GONE);
                    }
                }
            });
        }

        //tImage(data.getString("image"));

        if (seminardetails.getsimilerList() != null && seminardetails.getsimilerList().size() > 0) {
            setseminatlist(seminardetails.getsimilerList());
        }

        final TextView addreview = (TextView) findViewById(R.id.link_add);
        LinearLayout ll_review = (LinearLayout) findViewById(R.id.ll_noreview);
        final LinearLayout ll_addreview = (LinearLayout) findViewById(R.id.ll_reviewvisible);
        addreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_addreview.setVisibility(View.VISIBLE);
                addreview.setVisibility(View.GONE);
            }
        });
        Button btn_review = (Button) findViewById(R.id.btn_review);
        final EditText ed_review = (EditText) findViewById(R.id.ed_review);
        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                st_review = ed_review.getText().toString().trim();
                if (st_review.equalsIgnoreCase("")) {
                    ed_review.setError("Enter text");
                } else {
                    Addreview();
                    ll_addreview.setVisibility(View.GONE);
                    addreview.setVisibility(View.VISIBLE);
                }

            }
        });

        Button booking = (Button) findViewById(R.id.btnbooking);
        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MeettoApplication.getInstance().getPreferenceSettings().getIsLogin()) {
                    if (seminardetails.getAdmin().equalsIgnoreCase("false")) {
                        Intent intent = new Intent(SeminarDetail_Activity.this, Booking_Activity.class);
                        intent.putExtra("seminar_id", seminardetails.getSeminar_id());
                        intent.putExtra("seminar_name", seminardetails.getSeminar_name());
                        intent.putExtra("host_name", seminardetails.getSeminar_host_name());
                        intent.putExtra("seminar_pic", seminardetails.getImagesitems());
                        intent.putExtra("seminar_type", seminardetails.getSeminar_type());
                        intent.putExtra("organization", seminardetails.getorganization());
                        intent.putExtra("fromdate", seminardetails.getFromdate());
                        intent.putExtra("todate", seminardetails.getTodate());
                        intent.putExtra("seat", seminardetails.getAvailableseats());
                        startActivity(intent);
                    } else {
                        Toast.makeText(SeminarDetail_Activity.this, getResources().getString(R.string.seminardetails_Toastadmin), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(SeminarDetail_Activity.this, getResources().getString(R.string.Login_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (seminardetails.getImagesitems() != null && seminardetails.getImagesitems().size() > 0) {
            setImageslide(seminardetails.getImagesitems());
        }

        if (seminardetails.getFacilitiesitems() != null && seminardetails.getFacilitiesitems().size() > 0) {
            LinearLayout ll_facility = (LinearLayout) findViewById(R.id.ll_facilities);
            ll_facility.setVisibility(View.VISIBLE);
            setFacilites(seminardetails.getFacilitiesitems());
        }
        if (seminardetails.getattendees() != null && seminardetails.getattendees().size() > 0) {
            LinearLayout ll_attendees = (LinearLayout) findViewById(R.id.ll_attendess);
            ll_attendees.setVisibility(View.VISIBLE);
            setAttendees(seminardetails.getattendees());
        }

        if (seminardetails.getReview() != null && seminardetails.getReview().size() > 0) {
            ll_review.setVisibility(View.GONE);
            setReview(seminardetails.getReview());
        }
        if (seminardetails.getIndustry() != null && seminardetails.getIndustry().size() > 0) {
            LinearLayout ll_industry = (LinearLayout) findViewById(R.id.ll_industry);
            ll_industry.setVisibility(View.VISIBLE);
            setIndustry(seminardetails.getIndustry());
        }

    }

    public DisplayImageOptions getDisplayImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.profile_icon)
                .showImageForEmptyUri(R.drawable.profile_icon)
                .showImageOnFail(R.drawable.profile_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(100))
                .build();
        return options;
    }

      /*Convert to mili to date*/


    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public void setAttendees(ArrayList<SeminarDetailsarrayitem> attendees) {
        LinearLayout ll_category = (LinearLayout) findViewById(R.id.bis_category);
        LinearLayout ll_graduate = (LinearLayout) findViewById(R.id.ll_graduate);
        LinearLayout ll_student = (LinearLayout) findViewById(R.id.ll_student);
        LinearLayout ll_experience = (LinearLayout) findViewById(R.id.ll_experience);
        LinearLayout ll_career = (LinearLayout) findViewById(R.id.ll_career);
        if (attendees != null) {

            for (SeminarDetailsarrayitem attendeesobject : attendees) {

                if (attendeesobject.getSeminar_purpose().equalsIgnoreCase(getResources().getString(R.string.Welcome_Foreign_student))) {
                    ll_student.setVisibility(View.VISIBLE);
                }
                if (attendeesobject.getSeminar_purpose().equalsIgnoreCase(getResources().getString(R.string.New_graduate))) {
                    ll_graduate.setVisibility(View.VISIBLE);
                }
                if (attendeesobject.getSeminar_purpose().equalsIgnoreCase(getResources().getString(R.string.Business_category))) {
                    ll_category.setVisibility(View.VISIBLE);
                }
                if (attendeesobject.getSeminar_purpose().equalsIgnoreCase(getResources().getString(R.string.mid_career_recruiting))) {
                    ll_career.setVisibility(View.VISIBLE);
                }
                if (attendeesobject.getSeminar_purpose().equalsIgnoreCase(getResources().getString(R.string.inexperienced_person))) {
                    ll_experience.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void setFacilites(ArrayList<SeminarDetailsarrayitem> facilites) {
        LinearLayout ll_wifi = (LinearLayout) findViewById(R.id.ll_wifi);
        LinearLayout ll_pro = (LinearLayout) findViewById(R.id.ll_projector);
        LinearLayout ll_tea = (LinearLayout) findViewById(R.id.ll_tea);
        LinearLayout ll_ac = (LinearLayout) findViewById(R.id.ll_ac);
        LinearLayout ll_scanner = (LinearLayout) findViewById(R.id.ll_scanner);
        LinearLayout ll_printer = (LinearLayout) findViewById(R.id.ll_printer);
        LinearLayout ll_copy = (LinearLayout) findViewById(R.id.ll_copy_machine);
        LinearLayout ll_pub_parking = (LinearLayout) findViewById(R.id.ll_pub_parking);
        LinearLayout ll_pri_parking = (LinearLayout) findViewById(R.id.ll_pri_parking);
        LinearLayout ll_board = (LinearLayout) findViewById(R.id.ll_bord);
        LinearLayout ll_reciepniest = (LinearLayout) findViewById(R.id.ll_Receptionist);
        if (facilites != null) {

            for (SeminarDetailsarrayitem facilitesobject : facilites) {

                if (facilitesobject.getFacilities().equalsIgnoreCase(getResources().getString(R.string.WiFi))) {
                    ll_wifi.setVisibility(View.VISIBLE);
                }
                if (facilitesobject.getFacilities().equalsIgnoreCase(getResources().getString(R.string.Coffee_Tea))) {
                    ll_tea.setVisibility(View.VISIBLE);
                }
                if (facilitesobject.getFacilities().equalsIgnoreCase(getResources().getString(R.string.LCD_Projector))) {
                    ll_pro.setVisibility(View.VISIBLE);
                }
                if (facilitesobject.getFacilities().equalsIgnoreCase(getResources().getString(R.string.Scanner))) {
                    ll_scanner.setVisibility(View.VISIBLE);
                }
                if (facilitesobject.getFacilities().equalsIgnoreCase(getResources().getString(R.string.Air_Conditioner))) {
                    ll_ac.setVisibility(View.VISIBLE);
                }
                if (facilitesobject.getFacilities().equalsIgnoreCase(getResources().getString(R.string.Public_Parking))) {
                    ll_pub_parking.setVisibility(View.VISIBLE);
                }
                if (facilitesobject.getFacilities().equalsIgnoreCase(getResources().getString(R.string.Private_Parking))) {
                    ll_pri_parking.setVisibility(View.VISIBLE);
                }
                if (facilitesobject.getFacilities().equalsIgnoreCase(getResources().getString(R.string.Whiteboard))) {
                    ll_board.setVisibility(View.VISIBLE);
                }
                if (facilitesobject.getFacilities().equalsIgnoreCase(getResources().getString(R.string.Receptionist))) {
                    ll_reciepniest.setVisibility(View.VISIBLE);
                }
                if (facilitesobject.getFacilities().equalsIgnoreCase(getResources().getString(R.string.Printer))) {
                    ll_printer.setVisibility(View.VISIBLE);
                }
                if (facilitesobject.getFacilities().equalsIgnoreCase(getResources().getString(R.string.Copy_Machine))) {
                    ll_copy.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void setIndustry(ArrayList<SeminarDetailsarrayitem> industry) {
        final LinearLayout tv_service = (LinearLayout) findViewById(R.id.ll_service);
        final LinearLayout tv_manufacture = (LinearLayout) findViewById(R.id.ll_manufacture);
        final LinearLayout tv_trade = (LinearLayout) findViewById(R.id.ll_trade);
        final LinearLayout tv_finacebusiness = (LinearLayout) findViewById(R.id.ll_finacebusiness);
        final LinearLayout tv_building = (LinearLayout) findViewById(R.id.ll_building);
        final LinearLayout tv_real = (LinearLayout) findViewById(R.id.ll_realestate);
        final LinearLayout tv_it = (LinearLayout) findViewById(R.id.ll_it);
        final LinearLayout tv_education = (LinearLayout) findViewById(R.id.ll_education);
        final LinearLayout tv_food = (LinearLayout) findViewById(R.id.ll_food);
        final LinearLayout tv_publishing = (LinearLayout) findViewById(R.id.ll_publishing);
        final LinearLayout tv_agricultureindustry = (LinearLayout) findViewById(R.id.ll_agricultureindustry);
        final LinearLayout tv_timber = (LinearLayout) findViewById(R.id.ll_timber);
        final LinearLayout tv_fishings = (LinearLayout) findViewById(R.id.ll_fishings);
        final LinearLayout tv_marinproduct = (LinearLayout) findViewById(R.id.ll_marinproduct);
        final LinearLayout tv_stockfarming = (LinearLayout) findViewById(R.id.ll_stockfarming);
        final LinearLayout tv_medical = (LinearLayout) findViewById(R.id.ll_medical);
        if (industry != null) {

            for (SeminarDetailsarrayitem Industryobject : industry) {

                if (Industryobject.getIndustry().equalsIgnoreCase(getResources().getString(R.string.Service))) {
                    tv_service.setVisibility(View.VISIBLE);
                }
                if (Industryobject.getIndustry().equalsIgnoreCase(getResources().getString(R.string.Menufacturing))) {
                    tv_manufacture.setVisibility(View.VISIBLE);
                }
                if (Industryobject.getIndustry().equalsIgnoreCase(getResources().getString(R.string.Trade))) {
                    tv_trade.setVisibility(View.VISIBLE);
                }
                if (Industryobject.getIndustry().equalsIgnoreCase(getResources().getString(R.string.Finance_Business))) {
                    tv_finacebusiness.setVisibility(View.VISIBLE);
                }
                if (Industryobject.getIndustry().equalsIgnoreCase(getResources().getString(R.string.Building))) {
                    tv_building.setVisibility(View.VISIBLE);
                }
                if (Industryobject.getIndustry().equalsIgnoreCase(getResources().getString(R.string.Real_estate))) {
                    tv_real.setVisibility(View.VISIBLE);
                }
                if (Industryobject.getIndustry().equalsIgnoreCase(getResources().getString(R.string.IT))) {
                    tv_it.setVisibility(View.VISIBLE);
                }
                if (Industryobject.getIndustry().equalsIgnoreCase(getResources().getString(R.string.Education))) {
                    tv_education.setVisibility(View.VISIBLE);
                }
                if (Industryobject.getIndustry().equalsIgnoreCase(getResources().getString(R.string.Food))) {
                    tv_food.setVisibility(View.VISIBLE);
                }
                if (Industryobject.getIndustry().equalsIgnoreCase(getResources().getString(R.string.Publishing))) {
                    tv_publishing.setVisibility(View.VISIBLE);
                }
                if (Industryobject.getIndustry().equalsIgnoreCase(getResources().getString(R.string.Agricultural))) {
                    tv_agricultureindustry.setVisibility(View.VISIBLE);
                }
                if (Industryobject.getIndustry().equalsIgnoreCase(getResources().getString(R.string.Timber))) {
                    tv_timber.setVisibility(View.VISIBLE);
                }
                if (Industryobject.getIndustry().equalsIgnoreCase(getResources().getString(R.string.Fishing))) {
                    tv_fishings.setVisibility(View.VISIBLE);
                }
                if (Industryobject.getIndustry().equalsIgnoreCase(getResources().getString(R.string.Marin_Product))) {
                    tv_marinproduct.setVisibility(View.VISIBLE);
                }
                if (Industryobject.getIndustry().equalsIgnoreCase(getResources().getString(R.string.Stock_Farming))) {
                    tv_stockfarming.setVisibility(View.VISIBLE);
                }
                if (Industryobject.getIndustry().equalsIgnoreCase(getResources().getString(R.string.Medical))) {
                    tv_medical.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void setImageslide(ArrayList<SeminarDetailsarrayitem> imageslide) {
        if (imageslide != null) {
            imagesilde();
            mDemoSlider = (SliderLayout) findViewById(R.id.slider);
            for (SeminarDetailsarrayitem imagesitem : imageslide) {
                imagesize = imageslide.size();
                TextSliderView textSliderView = new Helper.TextSliderView(this);
                // initialize a SliderLayout
                textSliderView
                        .image(imagesitem.getSeminar_pic())
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putSerializable("image", imagesitem);

                mDemoSlider.addSlider(textSliderView);
            }
            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(10000);
            mDemoSlider.addOnPageChangeListener(this);
        }
    }

    public void setReview(ArrayList<SeminarDetailsarrayitem> Review) {
        if (Review != null) {
//            Review.clear();
            for (SeminarDetailsarrayitem imagesitem : Review) {
                final View itemList = SeminarDetail_Activity.this.getLayoutInflater().inflate(
                        R.layout.activity_review, linear_container, false);

                TextView review = (TextView) itemList.findViewById(R.id.review);
                TextView txt_u_name = (TextView) itemList.findViewById(R.id.txt_u_name);
                txt_u_name.setText(imagesitem.getUser_name() + ":");
                review.setText(imagesitem.getReview());
                linear_container.addView(itemList);
            }
        }
    }

    public void Addreview() {
        final String url = AppUrl.URL_REVIEW;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("list", response.toString());
                try {
                    JSONObject responseObj = new JSONObject(response);

                    int status_code = responseObj.getInt("status_code");
                    String msg = responseObj.getString("message");

                    Log.e("list", "Response: " + response);
                    if (status_code == 1) {
                    if(linear_container!=null) {
                        linear_container.removeAllViews();
                    }
                        Toast.makeText(SeminarDetail_Activity.this, msg, Toast.LENGTH_LONG).show();
                        reqseminardetail();
                    } else if (status_code == 0) {
                        Toast.makeText(SeminarDetail_Activity.this, msg, Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("Error_in", "else");
                    }

                } catch (JSONException e) {
                    Log.e("Error_in", "catch");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Booking", "Error Response: " + error.getMessage());
                Log.e("Error_in", "onErrorResponse");
                showTryAgainAlert("Info", "Network error, Please try again!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                PreferenceSettings mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();
                Map<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_REVIEW, st_review);
                params.put(ParamsKey.KEY_SEMINAR_ID, seminar_id);
                params.put(ParamsKey.KEY_USERID, mPreferenceSettings.getUserId());
                if (mPreferenceSettings.getLUNGAUGE()) {
                    params.put(ParamsKey.KEY_USER_LANG, "ja");
                }
                if (mPreferenceSettings.getLUNGAUGE() == false) {
                    params.put(ParamsKey.KEY_USER_LANG, "en");

                }
                Log.e("Wishlist", "params: " + params.toString());
                return params;
            }
        };
        MeettoApplication.getInstance().addToRequestQueue(strReq, TAG);
    }
}
