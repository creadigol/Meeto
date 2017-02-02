package Models;

import java.io.Serializable;

/**
 * Created by Ashfaq on 7/16/2016.
 */

public class BaseObject implements IObjaec, Serializable{

    private int status_code;
    private String message;

    public String getSeminar_boooking_no() {
        return seminar_boooking_no;
    }

    public void setSeminar_boooking_no(String seminar_boooking_no) {
        this.seminar_boooking_no = seminar_boooking_no;
    }

    private String seminar_boooking_no;
    private int count;

    public BaseObject(){

    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
