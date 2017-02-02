package Models;

/**
 * Created by Creadigol on 22-09-2016.
 */
public class Searchitem extends  BaseObject{

    String search_name,search_id,search_image,search_type;

    public String getSearch_id() {
        return search_id;
    }

    public void setSearch_id(String search_id) {
        this.search_id = search_id;
    }

    public String getSearch_image() {
        return search_image;
    }

    public void setSearch_image(String search_image) {
        this.search_image = search_image;
    }

    public String getSearch_name() {
        return search_name;
    }

    public void setSearch_name(String search_name) {
        this.search_name = search_name;
    }

    public String getSearch_type() {
        return search_type;
    }

    public void setSearch_type(String search_type) {
        this.search_type = search_type;
    }
}
