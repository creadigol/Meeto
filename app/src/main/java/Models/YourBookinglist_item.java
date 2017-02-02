package Models;

/**
 * Created by Ashfaq on 7/18/2016.
 */

public class YourBookinglist_item extends BaseObject {

   String bookingdate,seminar_name,date,host_name,host_approval,booking_id,seminar_id,booking_pic,book_seat,booking_no,fromdate,todate;

    public String getBooking_id() {
        return booking_id;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public String getBook_seat() {
        return book_seat;
    }

    public void setBook_seat(String book_seat) {
        this.book_seat = book_seat;
    }

    public String getBooking_no() {
        return booking_no;
    }

    public void setBooking_no(String booking_no) {
        this.booking_no = booking_no;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getBooking_pic() {
        return booking_pic;
    }

    public void setBooking_pic(String booking_pic) {
        this.booking_pic = booking_pic;
    }

    public String getSeminar_id() {
        return seminar_id;
    }

    public void setSeminar_id(String seminar_id) {
        this.seminar_id = seminar_id;
    }

    public String getBookingdate() {
        return bookingdate;
    }

    public void setBookingdate(String bookingdate) {
        this.bookingdate = bookingdate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHost_approval() {
        return host_approval;
    }

    public void setHost_approval(String host_approval) {
        this.host_approval = host_approval;
    }

    public String getHost_name() {
        return host_name;
    }

    public void setHost_name(String host_name) {
        this.host_name = host_name;
    }

    public String getSeminar_name() {
        return seminar_name;
    }

    public void setSeminar_name(String seminar_name) {
        this.seminar_name = seminar_name;
    }
}
