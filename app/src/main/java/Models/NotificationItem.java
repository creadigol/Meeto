package Models;



/**
 * Created by Ashfaq on 27-07-2016.
 */
public class NotificationItem extends BaseObject {

    int dbId;
    String seminarname, description, type, status, transactionId, reason, seminarId, image, isRead;
    long createdTime;


    public String getSeminarId() {
        return seminarId;
    }

    public void setSeminarId(String seminarId) {
        this.seminarId = seminarId;
    }

    public String getSeminarname() {
        return seminarname;
    }

    public void setSeminarname(String seminarname) {
        this.seminarname = seminarname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        //Log.e("setTransactionDate", "input transactionDate:" + createdTime);

        long dateMilliSeconds = 0;
        try {
            dateMilliSeconds = Long.parseLong(createdTime);
            //  Log.e("setTransactionDate", "input dateMilliSeconds:" + dateMilliSeconds);
            //dateMilliSeconds = dateMilliSeconds * 100;

//            this.createdTime = CommonUtils.getFormatedDate(dateMilliSeconds, "dd MMM yyyy hh:mm aaa");
            this.createdTime = dateMilliSeconds;
        } catch (Exception e) {
            this.createdTime = 0;
        }
        //Log.e("setTransactionDate", "this.transactionDate:" + this.createdTime);

    }

    public void setCreatedTime(long createdTime) {
        //Log.e("setTransactionDate", "input transactionDate:" + createdTime);
        this.createdTime = createdTime;
        //Log.e("setTransactionDate", "this.transactionDate:" + this.createdTime);
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
}
