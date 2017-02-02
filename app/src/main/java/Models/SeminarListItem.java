package Models;

/**
 * Created by Creadigol on 07-09-2016.
 */
public class SeminarListItem extends BaseObject{

    String seminar_id,seminar_name,seminar_addrress,uid,seminar_image,host_image,wishlist;


    public String getWishlist() {
        return wishlist;
    }

    public void setWishlist(String wishlist) {
        this.wishlist = wishlist;
    }

    public SeminarListItem() {

    }

    public String getHost_image() {
        return host_image;
    }

    public String getSeminar_addrress() {
        return seminar_addrress;
    }

    public void setSeminar_addrress(String seminar_addrress) {
        this.seminar_addrress = seminar_addrress;
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

    public void setHost_image(String host_image) {

        this.host_image = host_image;
    }

    public String getSeminar_image() {
        return seminar_image;
    }

    public void setSeminar_image(String seminar_image) {
        this.seminar_image = seminar_image;
    }

    public String getUid() {
        return uid;

    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    int image;
    public SeminarListItem(String s, Integer integer) {
    }

}
