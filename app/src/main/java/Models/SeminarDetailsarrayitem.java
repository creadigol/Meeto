package Models;

/**
 * Created by Creadigol on 27-09-2016.
 */
public class SeminarDetailsarrayitem extends BaseObject  {
    String seminar_pic,facilities,seminar_purpose,industry,user_name,review;

    public String getIndustry() {
        return industry;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setIndustrys(String industry) {
        this.industry = industry;
    }

    public String getSeminar_purpose() {
        return seminar_purpose;
    }

    public void setSeminar_purpose(String seminar_purpose) {
        this.seminar_purpose = seminar_purpose;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public String getSeminar_pic() {
        return seminar_pic;
    }

    public void setSeminar_pic(String seminar_pic) {
        this.seminar_pic = seminar_pic;
    }
}
