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
public class Seminardetailsitem extends BaseObject {
    String seminar_image = "";
    String seminar_id = "";
    String seminar_name="";
    String seminar_address="";
    String seminar_host_name="";
    String company_description="";
    String fromdate="";
    String todate="";
    String contact_email="";
    String contact_no="";
    String admin="";
    String tagline="";
    String qualification="";
    String zipcode="";
    String county="";
    String state="";
    String city="";
    String fromtime="";
    String totime="";
    String company_name="";
    String seminar_description="";
    String seminar_total_seat="";
    String seminar_host_description="";
    String seminar_host_pic="";
    String organization="";
    String seminar_type="";
    String book="";
    String IndustryPref="";
    String setFacilitiesPref="";
    String Availableseats="";

    public String getAvailableseats() {
        return Availableseats;
    }

    public void setAvailableseats(String availableseats) {
        Availableseats = availableseats;
    }

    public String getPhotoCount() {
        return photoCount;
    }

    public void setPhotoCount(String photoCount) {
        this.photoCount = photoCount;
    }

    String photoCount;

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    String photoPath;

    public String getProperttype() {
        return Properttype;
    }

    public void setProperttype(String properttype) {
        Properttype = properttype;
    }

    String Properttype;
    String attendencePref;
    //set seminar image in arraylist
    private ArrayList<SeminarDetailsarrayitem> imagesitems;
    //set seminar attendees
    private ArrayList<SeminarDetailsarrayitem> Attendess;
    //set industry
    private ArrayList<SeminarDetailsarrayitem> Industry;
    //set facilites
    private ArrayList<SeminarDetailsarrayitem> facilitestems;
    //user review
    private ArrayList<SeminarDetailsarrayitem> Review;
    //set for semilier seminar
    private ArrayList<SimilerSeminaritem> similerlist;

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getFromtime() {
        return fromtime;
    }

    public void setFromtime(String fromtime) {
        this.fromtime = fromtime;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public String getTotime() {
        return totime;
    }

    public void setTotime(String totime) {
        this.totime = totime;
    }

    public String getCompany_description() {
        return company_description;
    }

    public void setCompany_description(String company_description) {
        this.company_description = company_description;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getSeminar_image() {
        return seminar_image;
    }

    public void setSeminar_image(String seminar_image) {
        this.seminar_image = seminar_image;
    }

    public String getIndustryPref() {
        return IndustryPref;
    }

    public void setIndustryPref(String industryPref) {
        IndustryPref = industryPref;
    }

    public String getSetFacilitiesPref() {
        return setFacilitiesPref;
    }

    public void setSetFacilitiesPref(String setFacilitiesPref) {
        this.setFacilitiesPref = setFacilitiesPref;
    }

    public String getAttendencePref() {
        return attendencePref;
    }

    public void setAttendencePref(String attendencePref) {
        this.attendencePref = attendencePref;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getSeminar_address() {
        return seminar_address;
    }

    public void setSeminar_address(String seminar_address) {
        this.seminar_address = seminar_address;
    }

    public String getSeminar_description() {
        return seminar_description;
    }

    public void setSeminar_description(String seminar_description) {
        this.seminar_description = seminar_description;
    }

    public String getSeminar_host_description() {
        return seminar_host_description;
    }

    public void setSeminar_host_description(String seminar_host_description) {
        this.seminar_host_description = seminar_host_description;
    }

    public String getSeminar_host_name() {
        return seminar_host_name;
    }

    public void setSeminar_host_name(String seminar_host_name) {
        this.seminar_host_name = seminar_host_name;
    }

    public String getSeminar_host_pic() {
        return seminar_host_pic;
    }

    public void setSeminar_host_pic(String seminar_host_pic) {
        this.seminar_host_pic = seminar_host_pic;
    }

    public String getSeminar_id() {
        return seminar_id;
    }

    public void setSeminar_id(String seminar_id) {
        this.seminar_id = seminar_id;
    }

    public String getSeminar_name() {
        return seminar_name;
    }

    public void setSeminar_name(String seminar_name) {
        this.seminar_name = seminar_name;
    }

    public String getorganization() {
        return organization;
    }

    public void setorganization(String seminar_purpose) {
        this.organization = seminar_purpose;
    }

    public String getSeminar_total_seat() {
        return seminar_total_seat;
    }

    public void setSeminar_total_seat(String seminar_total_seat) {
        this.seminar_total_seat = seminar_total_seat;
    }

    public String getSeminar_type() {
        return seminar_type;
    }

    public void setSeminar_type(String seminar_type) {
        this.seminar_type = seminar_type;
    }

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

    public ArrayList<SeminarDetailsarrayitem> getReview() {
        return Review;
    }

    public void setReview(JSONArray jsonimage) {
        Review = new ArrayList<SeminarDetailsarrayitem>();

        if (jsonimage != null && jsonimage.length() > 0) {
            for (int i = 0; i < jsonimage.length(); i++) {
                SeminarDetailsarrayitem Reviews = new SeminarDetailsarrayitem();
                try {
                    JSONObject jsonObjectImage = jsonimage.getJSONObject(i);
                    Reviews.setReview(jsonObjectImage.optString(Jsonkey.Review));
                    Reviews.setUser_name(jsonObjectImage.optString(Jsonkey.user_name));
                    Review.add(Reviews);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<SimilerSeminaritem> getsimilerList() {
        return similerlist;
    }

    public void setSimilerList(JSONArray jsonDeals) {
        similerlist = new ArrayList<SimilerSeminaritem>();

        if (jsonDeals != null && jsonDeals.length() > 0) {
            for (int i = 0; i < jsonDeals.length(); i++) {
                SimilerSeminaritem similerSeminaritem = new SimilerSeminaritem();
                try {
                    JSONObject jsonObjectOutlet = jsonDeals.getJSONObject(i);

                    similerSeminaritem.setSeminar_id(jsonObjectOutlet.optString(Jsonkey.similerseminar_id));
                    similerSeminaritem.setSeminar_name(jsonObjectOutlet.optString(Jsonkey.similerseminar_name));
                    similerSeminaritem.setSeminar_pic(jsonObjectOutlet.optString(Jsonkey.similerseminarpic));

                    similerlist.add(similerSeminaritem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
