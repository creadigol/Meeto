package Models;

/**
 * Created by Creadigol on 12-09-2016.
 */
public class Wishlist_item {
    String seminar_image,seminar_name,seminar_id,user,wishlist_id,date,booked_seat,total_seat;

    public String getWishlist_id() {
        return wishlist_id;
    }

    public void setWishlist_id(String wishlist_id) {
        this.wishlist_id = wishlist_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBooked_seat() {
        return booked_seat;
    }

    public void setBooked_seat(String booked_seat) {
        this.booked_seat = booked_seat;
    }

    public String getTotal_seat() {
        return total_seat;
    }

    public void setTotal_seat(String total_seat) {
        this.total_seat = total_seat;
    }

    public String getSeminar_id() {
        return seminar_id;
    }

    public void setSeminar_id(String seminar_id) {
        this.seminar_id = seminar_id;
    }

    public String getSeminar_image() {
        return seminar_image;
    }

    public void setSeminar_image(String seminar_image) {
        this.seminar_image = seminar_image;
    }

    public String getSeminar_name() {
        return seminar_name;
    }

    public void setSeminar_name(String seminar_name) {
        this.seminar_name = seminar_name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
