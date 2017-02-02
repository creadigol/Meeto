package Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.Jsonkey;
import creadigol.com.Meetto.Add_Seminar_Activity;

/**
 * Created by Creadigol on 19-09-2016.
 */
public class SeminarObject extends BaseObject {

    String place = "", hostName = "", emailContact = "", number = "",
            title = "", tagLine = "", discription = "", addressStreet = "",
            zipCode = "", seminarId = "0";
    ArrayList<String> listPhotoPath = new ArrayList<>();
    ArrayList<Integer> facilities = new ArrayList<>();
    ArrayList<Integer> industries = new ArrayList<>();
    ArrayList<Integer> attendees = new ArrayList<>();
    ArrayList<String> listPhotoUrl = new ArrayList<>();
    long dateFrom = 0, dateTo = 0, Timefrom = 0, TimeTo = 0;
    int totalSeat = 0, country = 0, state = 0, city = 0;
    boolean isAdmin = false;

    public long getTimefrom() {
        return Timefrom;
    }

    public void setTimefrom(long timefrom) {
        Timefrom = timefrom;
    }

    public long getTimeTo() {
        return TimeTo;
    }

    public void setTimeTo(long timeTo) {
        TimeTo = timeTo;
    }

    public void addAttendees(int value) {
        if (!attendees.contains(value))

            attendees.add(value);
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void addIndustry(int value) {
        if (!industries.contains(value))
            industries.add(value);
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getEmailContact() {
        return emailContact;
    }

    public void setEmailContact(String emailContact) {
        this.emailContact = emailContact;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public void addFacilities(int value) {
        if (!facilities.contains(value))
            facilities.add(value);
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public ArrayList<String> getListPhotoPath() {
        return listPhotoPath;
    }

    public void addPhotoPath(String path) {
        if (path != null && path.length() > 0 && !listPhotoPath.contains(path))
            listPhotoPath.add(path);
    }

    public void setListPhotoPath(ArrayList<String> listPhotoPath) {
        this.listPhotoPath = listPhotoPath;
    }

    public ArrayList<String> getListPhotoUrl() {
        return listPhotoUrl;
    }

    public void addPhotoUrl(String url) {
        if (url != null && url.length() > 0 && !listPhotoUrl.contains(url))
            listPhotoUrl.add(url);
    }

    public void setListPhotoUrl(ArrayList<String> listPhotoUrl) {
        this.listPhotoUrl = listPhotoUrl;
    }

    public long getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(long dateFrom) {
        this.dateFrom = dateFrom;
    }

    public long getDateTo() {
        return dateTo;
    }

    public void setDateTo(long dateTo) {
        this.dateTo = dateTo;
    }

    public int getTotalSeat() {
        return totalSeat;
    }

    public void setTotalSeat(int totalSeat) {
        this.totalSeat = totalSeat;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public String getSeminarId() {
        return seminarId;
    }

    public void setSeminarId(String seminarId) {
        this.seminarId = seminarId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    // set from json //

    public void setSeminarPicture(JSONArray jsonArray) {

        if (jsonArray == null || jsonArray.length() <= 0)
            return;

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                String url = jsonArray.getString(i);
                addPhotoUrl(url);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSeminarFacility(JSONArray jsonArray) {

        if (jsonArray == null || jsonArray.length() <= 0)
            return;

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                String strFacility = jsonArray.getString(i);
                int facility = 0;
                if (strFacility != null && strFacility.length() > 0) {
                    try {
                        facility = Integer.parseInt(strFacility);
                    } catch (Exception e) {
                    }
                }
                if (facility != 0)
                    addFacilities(facility);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<SeminarDetailsarrayitem> facilitestems;

    public ArrayList<SeminarDetailsarrayitem> getFacilitiesitems() {
        return facilitestems;
    }

    public void setFacilitestems(JSONArray jsonfacilities) {
        facilitestems = new ArrayList<SeminarDetailsarrayitem>();

        if (jsonfacilities != null && jsonfacilities.length() > 0) {
            for (int i = 0; i < jsonfacilities.length(); i++) {
                SeminarDetailsarrayitem facilitiesobject = new SeminarDetailsarrayitem();
                try {
                    JSONObject jsonObjectImage = jsonfacilities.getJSONObject(i);

                    facilitiesobject.setFacilities(jsonObjectImage.optString(Jsonkey.facilites));
                    facilitestems.add(facilitiesobject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private ArrayList<SeminarDetailsarrayitem> Attendess;

    public void setSeminarAttendees(JSONArray jsonArray) {

        if (jsonArray == null || jsonArray.length() <= 0)
            return;

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                String strAttendees = jsonArray.getString(i);
                int attendees = 0;
                if (strAttendees != null && strAttendees.length() > 0) {
                    try {
                        attendees = Integer.parseInt(strAttendees);
                    } catch (Exception e) {
                    }
                }
                if (attendees != 0)
                    addAttendees(attendees);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<SeminarDetailsarrayitem> getattendees() {
        return Attendess;
    }

    public void setattendees(JSONArray jsonimage) {
        Attendess = new ArrayList<SeminarDetailsarrayitem>();

        if (jsonimage != null && jsonimage.length() > 0) {
            for (int i = 0; i < jsonimage.length(); i++) {
                SeminarDetailsarrayitem attendess = new SeminarDetailsarrayitem();
                try {
                    JSONObject jsonObjectImage = jsonimage.getJSONObject(i);

                    attendess.setSeminar_purpose(jsonObjectImage.optString(Jsonkey.seminar_purpose));
                    Attendess.add(attendess);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setSeminarIndustry(JSONArray jsonArray) {

        if (jsonArray == null || jsonArray.length() <= 0)
            return;

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                String strIndustry = jsonArray.getString(i);
                int industry = 0;
                if (strIndustry != null && strIndustry.length() > 0) {
                    try {
                        industry = Integer.parseInt(strIndustry);
                    } catch (Exception e) {
                    }
                }
                if (industry != 0)
                    addIndustry(industry);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<SeminarDetailsarrayitem> Industry;

    public ArrayList<SeminarDetailsarrayitem> getIndustry() {
        return Industry;
    }

    public void setIndustry(JSONArray jsonimage) {
        Industry = new ArrayList<SeminarDetailsarrayitem>();

        if (jsonimage != null && jsonimage.length() > 0) {
            for (int i = 0; i < jsonimage.length(); i++) {
                SeminarDetailsarrayitem industry = new SeminarDetailsarrayitem();
                try {
                    JSONObject jsonObjectImage = jsonimage.getJSONObject(i);

                    industry.setIndustrys(jsonObjectImage.optString(Jsonkey.industry));
                    Industry.add(industry);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private ArrayList<SeminarDetailsarrayitem> imagesitems;

    public ArrayList<SeminarDetailsarrayitem> getImagesitems() {
        return imagesitems;
    }

    public void setImagesitems(JSONArray jsonimage) {
        imagesitems = new ArrayList<SeminarDetailsarrayitem>();
        if(Add_Seminar_Activity.editImageList != null)
            Add_Seminar_Activity.editImageList.clear();

        if (jsonimage != null && jsonimage.length() > 0)
        {
            for (int i = 0; i < jsonimage.length(); i++) {
                SeminarDetailsarrayitem imagesobject = new SeminarDetailsarrayitem();
                try {
                    JSONObject jsonObjectImage = jsonimage.getJSONObject(i);

                    imagesobject.setSeminar_pic(jsonObjectImage.optString(Jsonkey.seminar_Image));

                    imagesitems.add(imagesobject);
                    if(Add_Seminar_Activity.editImageList != null)
                        Add_Seminar_Activity.editImageList.add(jsonObjectImage.optString(Jsonkey.seminar_Image));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if (Add_Seminar_Activity.deleteImage!=null)
        {
            String[] deleteImage = Add_Seminar_Activity.deleteImage.replace("null,","").split(",");
            if (deleteImage!=null)
            {
                for (int i = 0 ; i<deleteImage.length;i++)
                {
                    Add_Seminar_Activity.editImageList.remove(deleteImage[i]);
                }
            }
        }
    }

    public void setSeminarDetail(JSONObject jsonObject) {

        setSeminarId(jsonObject.optString("seminar_id"));
        setTitle(jsonObject.optString("seminar_name"));
        setTagLine(jsonObject.optString("tagline"));
        setZipCode(jsonObject.optString("zipcode"));
        setEmailContact(jsonObject.optString("contact_email"));
        setNumber(jsonObject.optString("contact_no"));
        setAddressStreet(jsonObject.optString("seminar_adress"));
        setHostName(jsonObject.optString("seminar_host_name"));
        setDiscription(jsonObject.optString("seminar_description"));
        setPlace(jsonObject.optString("seminar_type"));


        String strCountryId = jsonObject.optString("countryid");
        if (strCountryId != null && strCountryId.length() > 0) {
            try {
                setCountry(Integer.parseInt(strCountryId));
            } catch (Exception e) {
                setCountry(0);
            }
        }

        String strStateId = jsonObject.optString("stateid");
        if (strStateId != null && strStateId.length() > 0) {
            try {
                setState(Integer.parseInt(strStateId));
            } catch (Exception e) {
                setState(0);
            }
        }


        String strCityId = jsonObject.optString("cityid");
        if (strCityId != null && strCityId.length() > 0) {
            try {
                setCity(Integer.parseInt(strCityId));
            } catch (Exception e) {
                setCity(0);
            }
        }

        String strTotalSeat = jsonObject.optString("seminar_total_seat");
        if (strTotalSeat != null && strTotalSeat.length() > 0) {
            try {
                setTotalSeat(Integer.parseInt(strTotalSeat));
            } catch (Exception e) {
                setTotalSeat(0);
            }
        }

        String strFromDate = jsonObject.optString("from_date");
        if (strFromDate != null && strFromDate.length() > 0) {
            try {
                setDateFrom(Long.parseLong(strFromDate));
            } catch (Exception e) {
                setDateFrom(0);
            }
        }

        String strToDate = jsonObject.optString("to_date");
        if (strToDate != null && strToDate.length() > 0) {
            try {
                setDateTo(Long.parseLong(strToDate));
            } catch (Exception e) {
                setDateTo(0);
            }
        }

        String strFromtime = jsonObject.optString("from_time");
        if (strFromtime != null && strFromtime.length() > 0) {
            try {
                setTimefrom(Long.parseLong(strFromtime));
            } catch (Exception e) {
                setDateTo(0);
            }
        }


        String strTimeTo = jsonObject.optString("to_time");
        if (strTimeTo != null && strTimeTo.length() > 0) {
            try {
                setTimeTo(Long.parseLong(strTimeTo));
            } catch (Exception e) {
                setDateTo(0);
            }
        }

    }
}
