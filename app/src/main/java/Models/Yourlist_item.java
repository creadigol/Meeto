package Models;

/**
 * Created by Creadigol on 12-09-2016.
 */
public class Yourlist_item  extends BaseObject{
    String seminar_image,seminar_name,tagline,description,seminar_id;

    public String getSeminar_image() {
        return seminar_image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }


    public void setSeminar_image(String seminar_image) {
        this.seminar_image = seminar_image;
    }
}
