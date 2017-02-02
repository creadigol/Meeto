package Models;

/**
 * Created by Creadigol on 27-09-2016.
 */
public class SeminarDetailsImagesitem extends BaseObject  {
    String seminar_pic,facilities;

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
