package Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.Jsonkey;
import creadigol.com.Meetto.Add_Seminar_Activity;

/**
 * Created by Creadigol on 23-09-2016.
 */
public class ProfileItems extends BaseObject {
    String fname, lname, email, gender, dob, phone_no, address, yourself, photo, company_name, compnay_desc, time_zone;
    String uid, countryid, stateid, cityid, organization, faxno, url, language, email_verify="";

    public String getFaxno() {
        return faxno;
    }

    public String getEmail_verify() {
        return email_verify;
    }

    public void setEmail_verify(String email_verify) {
        this.email_verify = email_verify;
    }

    public void setFaxno(String faxno) {
        this.faxno = faxno;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountryid() {
        return countryid;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public void setCountryid(String countryid) {
        this.countryid = countryid;
    }

    public String getStateid() {
        return stateid;
    }

    public void setStateid(String stateid) {
        this.stateid = stateid;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompnay_desc() {
        return compnay_desc;
    }

    public void setCompnay_desc(String compnay_desc) {
        this.compnay_desc = compnay_desc;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public String getYourself() {
        return yourself;
    }

    public void setYourself(String yourself) {
        this.yourself = yourself;
    }

    private ArrayList<Profile_item> profile_items;

    public ArrayList<Profile_item> getProfile() {
        return profile_items;
    }

    public void setProfile(JSONArray jsonimage) {
        profile_items = new ArrayList<Profile_item>();

        if (jsonimage != null && jsonimage.length() > 0) {
            for (int i = 0; i < jsonimage.length(); i++) {
                Profile_item profile_item = new Profile_item();
                try {
                    JSONObject jsonObjectImage = jsonimage.getJSONObject(i);

                    profile_item.setLanguage(jsonObjectImage.optString(Jsonkey.language_name));
                    profile_items.add(profile_item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
