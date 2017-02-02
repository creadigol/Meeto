package creadigol.com.Meetto;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Utils.ParamsKey;
import Utils.PreferenceSettings;
import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntityBuilder;
import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;
import creadigol.com.Meetto.Add_Seminar_Activity;
import creadigol.com.Meetto.MeettoApplication;


/**
 * Created by albert on 14-3-21.
 */
public class MultipartRequest extends Request<String> {

    PreferenceSettings mPreferenceSettings;
    Context context;
    private HttpEntity mHttpEntity;
    private Response.Listener mListener;

    public MultipartRequest(String url,
                            Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        mHttpEntity = buildMultipartEntity();

    }

    private HttpEntity buildMultipartEntity() {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();
        Log.e("userid", mPreferenceSettings.getUserId());
        Log.e("name ", "==" + Add_Seminar_Activity.seminardetailsitem.getSeminar_name());
        Log.e("tagline ", "==" + Add_Seminar_Activity.seminardetailsitem.getTagline());
        Log.e("desc ", "==" + Add_Seminar_Activity.seminardetailsitem.getSeminar_description());
        Log.e("seat ", "==" + Add_Seminar_Activity.seminardetailsitem.getSeminar_total_seat());
        Log.e("address", "==" + Add_Seminar_Activity.seminardetailsitem.getSeminar_address());
        Log.e("coutry ", "==" + Add_Seminar_Activity.seminardetailsitem.getCounty());
        Log.e("state ", "==" + Add_Seminar_Activity.seminardetailsitem.getState());
        Log.e("city ", "==" + Add_Seminar_Activity.seminardetailsitem.getCity());
        Log.e("Zipcode ", "==" + Add_Seminar_Activity.seminardetailsitem.getZipcode());
        Log.e("number ", "==" + Add_Seminar_Activity.seminardetailsitem.getContact_no());
        Log.e("host name ", "==" + Add_Seminar_Activity.seminardetailsitem.getSeminar_host_name());
        Log.e("email ", "==" + Add_Seminar_Activity.seminardetailsitem.getContact_email());
        Log.e("attendace ", "==" + Add_Seminar_Activity.seminardetailsitem.getAttendencePref());
        Log.e("faciltty ", "==" + Add_Seminar_Activity.seminardetailsitem.getSetFacilitiesPref());
        Log.e("industry ", "==" + Add_Seminar_Activity.seminardetailsitem.getIndustryPref());
        Log.e("todate ", "==" + Add_Seminar_Activity.seminardetailsitem.getTodate());
        Log.e("fromdate ", "==" + Add_Seminar_Activity.seminardetailsitem.getFromdate());

        builder.addTextBody(ParamsKey.KEY_USERID, mPreferenceSettings.getUserId());
        builder.addTextBody(ParamsKey.KEY_TITLE, Add_Seminar_Activity.seminardetailsitem.getSeminar_name());
        builder.addTextBody(ParamsKey.KEY_TAGLINE, Add_Seminar_Activity.seminardetailsitem.getTagline());
        builder.addTextBody(ParamsKey.KEY_DESC, Add_Seminar_Activity.seminardetailsitem.getSeminar_description());
        builder.addTextBody(ParamsKey.KEY_TOTALSEAT, Add_Seminar_Activity.seminardetailsitem.getSeminar_total_seat());
        builder.addTextBody(ParamsKey.KEY_ADDRESS, Add_Seminar_Activity.seminardetailsitem.getSeminar_address());
        builder.addTextBody(ParamsKey.KEY_SEMINAR_TYPE, MeettoApplication.seminattype.get(0));
        builder.addTextBody(ParamsKey.KEY_COUNTRYID, Add_Seminar_Activity.seminardetailsitem.getCounty());
        builder.addTextBody(ParamsKey.KEY_STATEID, Add_Seminar_Activity.seminardetailsitem.getState());
        builder.addTextBody(ParamsKey.KEY_CITYID, Add_Seminar_Activity.seminardetailsitem.getCity());
        builder.addTextBody(ParamsKey.KEY_ZIPCODE, Add_Seminar_Activity.seminardetailsitem.getZipcode());
        builder.addTextBody(ParamsKey.KEY_PHONENUMBER, Add_Seminar_Activity.seminardetailsitem.getContact_no());
        builder.addTextBody(ParamsKey.KEY_HOSTNAME, Add_Seminar_Activity.seminardetailsitem.getSeminar_host_name());
        builder.addTextBody(ParamsKey.KEY_CONTACTEMAIL, Add_Seminar_Activity.seminardetailsitem.getContact_email());
        builder.addTextBody(ParamsKey.KEY_PURPOSEID, Add_Seminar_Activity.seminardetailsitem.getAttendencePref());

        if (Add_Seminar_Activity.deleteImage != null) {
            Log.e("Delete Image", Add_Seminar_Activity.deleteImage.replace("null,", ""));
            builder.addTextBody(ParamsKey.KEY_DELETE_IMAGE, Add_Seminar_Activity.deleteImage.replace("null,", ""));
            Add_Seminar_Activity.deleteImage = null;
        }

        String givenDateTodate = Add_Seminar_Activity.seminardetailsitem.getTodate();
        String givenDateFromdate = Add_Seminar_Activity.seminardetailsitem.getFromdate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date = null;
        Date date2 = null;
        try {
            date = sdf.parse(givenDateFromdate);
            date2 = sdf.parse(givenDateTodate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("Date in milli :: " + date);
        System.out.println("Date in milli :: " + date2);
        if (date != null) {
            builder.addTextBody(ParamsKey.KEY_FROMDATE, String.valueOf(date.getTime()));
        } else {
            builder.addTextBody(ParamsKey.KEY_FROMDATE, "");
        }
        if (date2 != null) {
            builder.addTextBody(ParamsKey.KEY_TODATE, String.valueOf(date2.getTime()));

        } else {
            builder.addTextBody(ParamsKey.KEY_TODATE, "");
        }
        builder.addTextBody(ParamsKey.KEY_FACILITYID, Add_Seminar_Activity.seminardetailsitem.getSetFacilitiesPref());
        builder.addTextBody(ParamsKey.KEY_INDUSTRY_ID, Add_Seminar_Activity.seminardetailsitem.getIndustryPref());
        if (MeettoApplication.getInstance().getLocationTracker() != null) {
            builder.addTextBody("latitude", String.valueOf(MeettoApplication.getInstance().getLocationTracker().getLatitude()));
            builder.addTextBody("longitude", String.valueOf(MeettoApplication.getInstance().getLocationTracker().getLongitude()));
        } else {
            builder.addTextBody("latitude", "0.0");
            builder.addTextBody("longitude", "0.0");
        }

        if (mPreferenceSettings.getLUNGAUGE()) {
            builder.addTextBody(ParamsKey.KEY_USER_LANG, "ja");
        }
        if (mPreferenceSettings.getLUNGAUGE() == false) {
            builder.addTextBody(ParamsKey.KEY_USER_LANG, "en");

        }
        if (mPreferenceSettings.getEdit()) {
            builder.addTextBody(ParamsKey.KEY_EDIT, "true");
            builder.addTextBody(ParamsKey.KEY_SEMINAR_ID, mPreferenceSettings.getSeminar_id());
        }


        if (Add_Seminar_Activity.arrayList != null && Add_Seminar_Activity.arrayList.size() > 0) {
            for (int k = 0; k < Add_Seminar_Activity.arrayList.size(); k++) {
                File file1 = new File(Add_Seminar_Activity.arrayList.get(k));
                FileBody Fbody1 = new FileBody(file1);
                int i = k + 1;
                builder.addPart(ParamsKey.KEY_PHOTO + i, Fbody1);
                Log.e("meetoapp", Fbody1.getFilename() + "" + file1.getAbsolutePath() + " " + file1.length() + "i" + i);
            }

            builder.addTextBody(ParamsKey.KEY_PHOTOCOUNT, Add_Seminar_Activity.arrayList.size() + "");

            for (int k = 0; k < MeettoApplication.count.size(); k++) {
                int i = k + 1;
                builder.addTextBody(ParamsKey.KEY_Rotate + i, String.valueOf(MeettoApplication.count.get(k)));
                Log.e("count", "" + ParamsKey.KEY_Rotate + i + "all" + String.valueOf(MeettoApplication.count.get(k)));
            }
        } else {
            builder.addTextBody(ParamsKey.KEY_PHOTOCOUNT, "0");
        }

        Add_Seminar_Activity.arrayList.clear();
        if (MeettoApplication.count != null)
            MeettoApplication.count.clear();
        Add_Seminar_Activity.seminardetailsitem = null;

        return builder.build();
    }

    @Override
    public String getBodyContentType() {
        return mHttpEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mHttpEntity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(new String(response.data, "UTF-8"),
                    getCacheEntry());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            return Response.success(new String(response.data), getCacheEntry());
        }
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

}