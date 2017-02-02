package Models;

/**
 * Created by Creadigol on 17-10-2016.
 */
public class Review_items  extends BaseObject{
    String seminar_name,seminar_id,user_name,seminar_pic,type,review,review_id,user_id;

    public String getReview() {
        return review;
    }

    public String getReview_id() {
        return review_id;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSeminar_pic() {
        return seminar_pic;
    }

    public void setSeminar_pic(String seminar_pic) {
        this.seminar_pic = seminar_pic;
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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
