package Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Ashfaq on 28-10-2015.
 */
public class PreferenceSettings {
    private String CASHTAG = "CashTag";

    private String PRE_IS_LOGIN = "is_login";
    private String PRE_IS_FORGOT = "is_login";
    private String PRE_IS_VALID = "is_valid";
    private String PRE_filter = "filter";
    private String PRE_Facebook= "is_valid";

    private String PRE_IS_VISIBLE = "is_visible";
    private String PRE_IS_Edit = "is_edit";
    private String PRE_LUNGAUGE = "lang";
    private String REG_ID = "regid";

    private String PRE_USER_ID = "User_Id";
    //home page
    private String PRE_CITY_ID = "cityid";
    private String PRE_CITY_NAME = "cityname";
    private String PRE_LOGIN_TYPE = "regtype";

    //profile data
    private String PRE_USER_FIRSTNAME = "fname";
    private String PRE_USER_LASTNAME = "lname";
    private String PRE_EMAIL = "email";
    private String PRE_USER_PIC = "user_pic";
    private static final String KEY_BIRTH_DATE = "dob";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_EMAILVERIFY = "emailverify";
    private static final String KEY_ABOUTUSERSELF = "yourself";
    private static final String KEY_COMPANY_NAME = "company_name";
    private static final String KEY_COMPANY_DESC = "company_desc";
    private static final String KEY_TIMEZONE = "timezone";
    private static final String KEY_FAXNUMBER = "faxnumber";
    private static final String KEY_URL = "url";
    private static final String KEY_ORGANIZATION = "org";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_MOBILE_NUMBER = "mobile_number";

    private static final String KEY_Seminar_id = "seminar name";
    private static final String KEY_Seminar_pic = "photo";
    private static final String KEY_photocount = "count";


    private static final String KEY_COUNTRY = "country";
    private static final String KEY_STATE = "state";
    private static final String KEY_CITY = "city";

    //add seminar
    private static final String KEY_PROPERTTYPE = "property";
    private static final String KEY_PURPOSE = "purpose";
    private static final String KEY_INDUSTRY = "industry";
    private static final String KEY_TOTALSEAT = "totalseat";
    private static final String KEY_SELECTLOCATION = "selectloc";
    private static final String KEY_Count = "count";

    //contact
    private static final String KEY_HOSTNAME = "hostname";
    private static final String KEY_CONTACTEMAIL = "contactemail";
    private static final String KEY_CONTACTNO = "contactno";

    //addseminar calander
    private static final String KEY_FROMDATE = "fromdate";
    private static final String KEY_TODATE = "todate";
    private static final String KEY_STARTTIME = "starttime";
    private static final String KEY_ENDTIME = "endtime";

    //set addseminar overview
    private static final String KEY_TITLE = "title";
    private static final String KEY_TAGLINE = "tagline";
    private static final String KEY_QUALIFICATION = "qualification";
    private static final String KEY_SEMINARDESC = "seminardesc";

    //set addseminar functionality
    private static final String KEY_FACILITY = "functionality";

    //location
    private static final String KEY_SEMINARADDRESS = "s_address";
    private static final String KEY_ZIPCODE = "zipcode";


    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_IS_FORGOT_IN = "isLoggedIn";

    private static final String KEY_NOTIFICATION_LAST_TIME = "last_sync_time";
    private static final String KEY_HOME_NOTIFYCOUNT = "notificationcount";
    Context _context;


    private SharedPreferences sp;
    private SharedPreferences.Editor editor;


    public PreferenceSettings(Context context) {
        this._context = context;
        sp = _context.getSharedPreferences(CASHTAG, context.MODE_PRIVATE);
        editor = sp.edit();
    }


    public void setnotifycount(String notificationcount) {
        editor.putString(KEY_HOME_NOTIFYCOUNT, notificationcount).commit();
    }

    public String getnotifycount() {
        return sp.getString(KEY_HOME_NOTIFYCOUNT, "");
    }

    //set coutry
    public void setCountry(String country) {
        editor.putString(KEY_COUNTRY, country).commit();
    }

    public String getCountry() {
        return sp.getString(KEY_COUNTRY, "");
    }

    //set state
    public void setState(String state) {
        editor.putString(KEY_STATE, state).commit();
    }

    public String getState() {
        return sp.getString(KEY_STATE, "");
    }

    //set city
    public void setCity(String city) {
        editor.putString(KEY_CITY, city).commit();
    }

    public String getCity() {
        return sp.getString(KEY_CITY, "");
    }

    public void setIslogin(boolean flag) {
        editor.putBoolean(PRE_IS_LOGIN, flag).commit();
    }

    public boolean getIsForgot() {
        return sp.getBoolean(PRE_IS_FORGOT, false);
    }

    public void setIsForgot(boolean flag) {
        editor.putBoolean(PRE_IS_FORGOT, flag).commit();
    }

    public boolean getIsValid() {
        return sp.getBoolean(PRE_IS_VALID, false);
    }

    public void setIsValid(boolean flag) {
        editor.putBoolean(PRE_IS_VALID, flag).commit();
    }

    public boolean getfilter() {
        return sp.getBoolean(PRE_filter, false);
    }

    public void setfilter(boolean flag) {
        editor.putBoolean(PRE_filter, flag).commit();
    }
    public boolean getFacebook() {
        return sp.getBoolean(PRE_Facebook, false);
    }

    public void setFacebook(boolean flag) {
        editor.putBoolean(PRE_Facebook, flag).commit();
    }

    public boolean getIS_VISIBLE() {
        return sp.getBoolean(PRE_IS_VISIBLE, false);
    }

    public void setIS_VISIBLE(boolean flag) {
        editor.putBoolean(PRE_IS_VISIBLE, flag).commit();
    }

    public boolean getEdit() {
        return sp.getBoolean(PRE_IS_Edit, false);
    }

    public void setEdit(boolean flag) {
        editor.putBoolean(PRE_IS_Edit, flag).commit();
    }

    public boolean getLUNGAUGE() {
        return sp.getBoolean(PRE_LUNGAUGE, false);
    }

    public void setLUNGAUGE(boolean flag) {
        editor.putBoolean(PRE_LUNGAUGE, flag).commit();
    }

    public boolean getIsLogin() {
        return sp.getBoolean(PRE_IS_LOGIN, false);
    }


    public void setUserId(String userId) {
        editor.putString(PRE_USER_ID, userId).commit();
    }

    public String getUserId() {
        return sp.getString(PRE_USER_ID, "");
    }

    public void setLastSyncTime(long id) {
        editor.putLong(KEY_NOTIFICATION_LAST_TIME, id).commit();
    }

    public long getLastSyncTime() {
        return sp.getLong(KEY_NOTIFICATION_LAST_TIME, 0);
    }


    public void setBirthDate(String dob) {
        editor.putString(KEY_BIRTH_DATE, dob).commit();
    }

    public String getBirthdater() {
        return sp.getString(KEY_BIRTH_DATE, "");
    }

    public void setAddress(String address) {
        editor.putString(KEY_ADDRESS, address).commit();
    }

    public String getAddress() {
        return sp.getString(KEY_ADDRESS, "");
    }

    public void setEmailverify(String emailverify) {
        editor.putString(KEY_EMAILVERIFY, emailverify).commit();
    }

    public String getEmailverify() {
        return sp.getString(KEY_EMAILVERIFY, "");
    }


    //set add seminar property purposr.....
    public void setPurpose(String purpose) {
        editor.putString(KEY_PURPOSE, purpose).commit();
    }

    public String getPurpose() {
        return sp.getString(KEY_PURPOSE, "");
    }

    public void setIndustry(String industry) {
        editor.putString(KEY_INDUSTRY, industry).commit();
    }

    public String getIndustry() {
        return sp.getString(KEY_INDUSTRY, "");
    }


    public void setProperttype(String properttype) {
        editor.putString(KEY_PROPERTTYPE, properttype).commit();
    }

    public String getProperttype() {
        return sp.getString(KEY_PROPERTTYPE, "");
    }

    public void setTotalseat(String totalseat) {
        editor.putString(KEY_TOTALSEAT, totalseat).commit();
    }

    public String getTotalseat() {
        return sp.getString(KEY_TOTALSEAT, "");
    }

    public void setSelectlocation(String selectlocation) {
        editor.putString(KEY_SELECTLOCATION, selectlocation).commit();
    }

    public String getSelectlocation() {
        return sp.getString(KEY_SELECTLOCATION, "");
    }

    //count
    public void setCount(String count) {
        editor.putString(KEY_Count, count).commit();
    }

    public String getKEY_Count() {
        return sp.getString(KEY_Count, "");
    }

    //set contact
    public void setHostname(String hostname) {
        editor.putString(KEY_HOSTNAME, hostname).commit();
    }

    public String getHostname() {
        return sp.getString(KEY_HOSTNAME, "");
    }

    public void setContactemail(String contactemail) {
        editor.putString(KEY_CONTACTEMAIL, contactemail).commit();
    }


    public String getContactemail() {
        return sp.getString(KEY_CONTACTEMAIL, "");
    }

    public void setContactno(String contactno) {
        editor.putString(KEY_CONTACTNO, contactno).commit();
    }

    public String getContactno() {
        return sp.getString(KEY_CONTACTNO, "");
    }

    //set add seminar Calander
    public void setFromdate(String fromdate) {
        editor.putString(KEY_FROMDATE, fromdate).commit();
    }

    public String getFromdate() {
        return sp.getString(KEY_FROMDATE, "");
    }

    public void setTodate(String todate) {
        editor.putString(KEY_TODATE, todate).commit();
    }

    public String getTodate() {
        return sp.getString(KEY_TODATE, "");
    }

    public void setStarttime(String starttime) {
        editor.putString(KEY_STARTTIME, starttime).commit();
    }

    //set addseminar overview
    public String getStarttime() {
        return sp.getString(KEY_STARTTIME, "");
    }

    public void setEndtime(String endtime) {
        editor.putString(KEY_ENDTIME, endtime).commit();
    }

    public String getEndtime() {
        return sp.getString(KEY_ENDTIME, "");
    }

    //overview
    public void setTitle(String title) {
        editor.putString(KEY_TITLE, title).commit();
    }

    public String getTitle() {
        return sp.getString(KEY_TITLE, "");
    }

    public void setTagline(String tagline) {
        editor.putString(KEY_TAGLINE, tagline).commit();
    }

    public String getTagline() {
        return sp.getString(KEY_TAGLINE, "");
    }

    public void setQualification(String qualification) {
        editor.putString(KEY_QUALIFICATION, qualification).commit();

    }

    public String getQualification() {
        return sp.getString(KEY_QUALIFICATION, "");
    }


    public void setSeminardesc(String seminardesc) {
        editor.putString(KEY_SEMINARDESC, seminardesc).commit();
    }

    public String getSeminardesc() {
        return sp.getString(KEY_SEMINARDESC, "");
    }

    public void setFacility(String facility) {
        editor.putString(KEY_FACILITY, facility).commit();
    }

    public String getFacility() {
        return sp.getString(KEY_FACILITY, "");
    }


    public void setAboutuserself(String aboutuserself) {
        editor.putString(KEY_ABOUTUSERSELF, aboutuserself).commit();
    }

    public String getAboutuserself() {
        return sp.getString(KEY_ABOUTUSERSELF, "");
    }


    public void setCompanyName(String companyName) {
        editor.putString(KEY_COMPANY_NAME, companyName).commit();
    }

    public String getCompanyName() {
        return sp.getString(KEY_COMPANY_NAME, "");
    }

    public void setGender(String gender) {
        editor.putString(KEY_GENDER, gender).commit();
    }

    public String getGender() {
        return sp.getString(KEY_GENDER, "");
    }


    public void setTimezone(String timezone) {
        editor.putString(KEY_TIMEZONE, timezone).commit();
    }

    public String getTimezone() {
        return sp.getString(KEY_TIMEZONE, "");
    }


    public void setOrganization(String organization) {
        editor.putString(KEY_ORGANIZATION, organization).commit();
    }

    public String getOrganization() {
        return sp.getString(KEY_ORGANIZATION, "");
    }


    public void setFaxnumber(String faxnumber) {
        editor.putString(KEY_FAXNUMBER, faxnumber).commit();
    }

    public String getFaxnumber() {
        return sp.getString(KEY_FAXNUMBER, "");
    }

    public void setUrl(String url) {
        editor.putString(KEY_URL, url).commit();
    }

    public String getUrl() {
        return sp.getString(KEY_URL, "");
    }

    //set language
    public void setLanguage(String language) {
        editor.putString(KEY_LANGUAGE, language).commit();
    }

    public String getLanguage() {
        return sp.getString(KEY_LANGUAGE, "");
    }

    //set company desc
    public void setCompanyDesc(String companyDesc) {
        editor.putString(KEY_COMPANY_DESC, companyDesc).commit();
    }

    public String getCompanyDesc() {
        return sp.getString(KEY_COMPANY_DESC, "");
    }


    public void setPassword(String Password) {
        editor.putString(KEY_PASSWORD, Password).commit();
    }

    public String getPassword() {
        return sp.getString(KEY_PASSWORD, "");
    }

    public void setSeminar_id(String seminar_id) {
        editor.putString(KEY_Seminar_id, seminar_id).commit();
    }

    public String getSeminar_id() {
        return sp.getString(KEY_Seminar_id, "");
    }


    public void setSeminar_pic(String pic) {
        editor.putString(KEY_Seminar_pic, pic).commit();
    }

    public String getseminar_pic() {
        return sp.getString(KEY_Seminar_pic, "");
    }

    public void setphotocount(String count) {
        editor.putString(KEY_photocount, count).commit();
    }

    public String getphotocount() {
        return sp.getString(KEY_photocount, "");
    }

    public void setMobileNumber(String mobileNumber) {
        editor.putString(KEY_MOBILE_NUMBER, mobileNumber).commit();
    }

    public String getMobileNumber() {
        return sp.getString(KEY_MOBILE_NUMBER, "");
    }

    public void setCityId(String cityId) {
        editor.putString(PRE_CITY_ID, cityId).commit();
    }

    public String getCityId() {
        return sp.getString(PRE_CITY_ID, "");
    }

    public void setCityName(String cityName) {
        editor.putString(PRE_CITY_NAME, cityName).commit();
    }

    public String getCityName() {
        return sp.getString(PRE_CITY_NAME, "");
    }


    public void setUSER_FIRSTNAME(String username) {
        editor.putString(PRE_USER_FIRSTNAME, username).commit();
    }

    public String getUSER_FIRSTNAME() {
        return sp.getString(PRE_USER_FIRSTNAME, "");
    }

    public void setUSER_LASTNAME(String lastname) {
        editor.putString(PRE_USER_LASTNAME, lastname).commit();
    }

    public String getUSER_LASTNAME() {
        return sp.getString(PRE_USER_LASTNAME, "");
    }

    public void setUserEmail(String email) {
        editor.putString(PRE_EMAIL, email).commit();
    }

    public String getUserEmail() {
        return sp.getString(PRE_EMAIL, "");
    }

    public void setUserPic(String pic) {
        editor.putString(PRE_USER_PIC, pic).commit();
    }

    public String getUserPic() {
        return sp.getString(PRE_USER_PIC, "");
    }

    public String setLoginType(String type) {
        editor.putString(PRE_LOGIN_TYPE, type).commit();
        return type;
    }

    public String getLoginType() {
        return sp.getString(PRE_LOGIN_TYPE, "");
    }

    public void setRegId(String id) {
        editor.putString(REG_ID, id).commit();
    }

    public String getRegId() {
        return sp.getString(REG_ID, "");
    }

    public void createLogin(String mobile) {
//        editor.putString(KEY_NAME, name);
//        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return sp.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void createForgot(String mobile) {
//        editor.putString(KEY_NAME, name);
//        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.putBoolean(KEY_IS_FORGOT_IN, true);
        editor.commit();
    }

    public boolean isForgoteIn() {
        return sp.getBoolean(KEY_IS_FORGOT_IN, false);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> profile = new HashMap<>();
//        profile.put("name", pref.getString(KEY_NAME, null));
//        profile.put("email", pref.getString(KEY_EMAIL, null));
        profile.put("mobile", sp.getString(KEY_MOBILE, null));
        return profile;
    }

    public void setSeminaraddress(String seminaraddress) {
        editor.putString(KEY_SEMINARADDRESS, seminaraddress).commit();
    }

    public String getSeminaraddress() {
        return sp.getString(KEY_SEMINARADDRESS, "");
    }

    public void setZipcode(String zipcode) {
        editor.putString(KEY_ZIPCODE, zipcode).commit();
    }

    public String getZipcode() {
        return sp.getString(KEY_ZIPCODE, "");
    }
}
