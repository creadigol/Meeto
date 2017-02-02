package Models;

/**
 * Created by Ashfaq on 7/16/2016.
 */

public class UserObject extends BaseObject {

    private String fname,lname, mobile, email, userid, user_image,gender,birthdate,phoneno,address,yourself,photo,compnat_name,compnat_desc,timezone,email_verify,type,organization,url,fax_number;

    private String id_google, id_fb,dob,password;

    public String getFax_number() {
        return fax_number;
    }

    public void setFax_number(String fax_number) {
        this.fax_number = fax_number;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail_verify() {
        return email_verify;
    }

    public void setEmail_verify(String email_verify) {
        this.email_verify = email_verify;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getCompnat_desc() {
        return compnat_desc;
    }

    public void setCompnat_desc(String compnat_desc) {
        this.compnat_desc = compnat_desc;
    }

    public String getCompnat_name() {
        return compnat_name;
    }

    public void setCompnat_name(String compnat_name) {
        this.compnat_name = compnat_name;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getYourself() {
        return yourself;
    }

    public void setYourself(String yourself) {
        this.yourself = yourself;
    }

    public String getDob() {
        return dob;
    }



    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserObject(String fname,String lname, String mobile, String email, String userid, String user_image, String birthdate, String address, String gender, String company_name,  String company_desc, String password) {
        this.fname = fname;
        this.lname = lname;
        this.mobile = mobile;
        this.email = email;
        this.userid = userid;
        this.photo = user_image;
        this.birthdate=birthdate;
        this.address=address;
        this.compnat_name=company_name;
        this.compnat_desc=company_desc;
        this.gender=gender;
        this.password=password;
    }

    public UserObject(){

    }

    public String getFname() {
        return fname;
    }

    public void setFName(String fname) {
        this.fname = fname;
    }
    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }


    public String getId_google() {
        return id_google;
    }

    public void setId_google(String id_google) {
        this.id_google = id_google;
    }

    public String getId_fb() {
        return id_fb;
    }

    public void setId_fb(String id_fb) {
        this.id_fb = id_fb;
    }
}
