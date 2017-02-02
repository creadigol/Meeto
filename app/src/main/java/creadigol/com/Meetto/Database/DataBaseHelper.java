package creadigol.com.Meetto.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import Models.NotificationItem;

/**
 * Created by Ashfaq on 4/14/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Meetto";

    private static Context mContext;
    private static final String TABLE_NOTIFICATION = "Notification";

    private static final String FIELD_ID = "id";
    private static final String FIELD_SEMINAR_NAME = "seminarName";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_TRANSACTION_ID = "transactionId";
    private static final String FIELD_CREATED_TIME = "createdTime";
    private static final String FIELD_REASON = "reason";
    private static final String FIELD_DEAL_ID = "dealId";
    private static final String FIELD_SEMINAR_ID = "seminar_Id";
    private static final String FIELD_IMAGE = "image";
    private static final String FIELD_IS_READ = "isRead";

    public static int countExist = 0, countNotExist = 0, countTotal = 0;

    public static final String IS_READ = "1";
    public static final String IS_NOT_READ = "0";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_SEARCH_TABLE = "CREATE TABLE " + TABLE_NOTIFICATION + " ( "
                + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FIELD_SEMINAR_NAME+ " TEXT, "
                + FIELD_DESCRIPTION + " TEXT, "
                + FIELD_TYPE + " TEXT, "
                + FIELD_STATUS + " TEXT, "
                + FIELD_TRANSACTION_ID + " TEXT, "
                + FIELD_CREATED_TIME + " INTEGER, "
                + FIELD_REASON + " TEXT, "
                + FIELD_DEAL_ID + " TEXT, "
                + FIELD_SEMINAR_ID+ " TEXT, "
                + FIELD_IMAGE + " TEXT, "
                + FIELD_IS_READ + " TEXT "
                + ");";
        Log.e("", "CREATE_SEARCH_TABLE >> " + CREATE_SEARCH_TABLE);
        sqLiteDatabase.execSQL(CREATE_SEARCH_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
        onCreate(sqLiteDatabase);
    }

    // check if a record exists so it won't insert the next time you run this code
    public boolean checkIfExists(NotificationItem notificationItem) {

        boolean recordExists = false;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + FIELD_ID + " FROM " + TABLE_NOTIFICATION + " WHERE " + FIELD_TYPE + " = '" + notificationItem.getType()
                + "' AND " + FIELD_CREATED_TIME + " = '" + notificationItem.getCreatedTime()
                + "'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                recordExists = true;
                Log.e("TAG", notificationItem.getType() + ":" + notificationItem.getCreatedTime() + " recordExists.");
            }
        }

        cursor.close();
        db.close();

        return recordExists;
    }


    // createNotification new record
    // @param myObj contains details to be added as single row.
    public boolean createNotification(NotificationItem  notificationItem) {

        boolean createSuccessful = false;

        if (!checkIfExists(notificationItem)) {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(FIELD_SEMINAR_NAME, notificationItem.getSeminarname());
            values.put(FIELD_DESCRIPTION, notificationItem.getDescription());
            values.put(FIELD_TYPE, notificationItem.getType());
            values.put(FIELD_STATUS, notificationItem.getStatus());
            values.put(FIELD_TRANSACTION_ID, notificationItem.getTransactionId());
            values.put(FIELD_CREATED_TIME, notificationItem.getCreatedTime());
            values.put(FIELD_REASON, notificationItem.getReason());
            values.put(FIELD_SEMINAR_ID, notificationItem.getSeminarId());
            values.put(FIELD_IMAGE, notificationItem.getImage());
            values.put(FIELD_IS_READ, IS_NOT_READ);

            createSuccessful = db.insert(TABLE_NOTIFICATION, null, values) > 0;

            db.close();

            if (createSuccessful) {
                Log.e("TAG", notificationItem.getType() + ":" + notificationItem.getCreatedTime() + " created.");
            } else {
                Log.e("TAG", notificationItem.getType() + ":" + notificationItem.getCreatedTime() + " not created.");
            }
        }

        return createSuccessful;
    }

    public boolean makeItRead(NotificationItem notificationItem) {

        boolean readSuccessful = false;

        if (checkIfExists(notificationItem)) {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(FIELD_IS_READ, IS_READ);

            readSuccessful = db.update(TABLE_NOTIFICATION, values, FIELD_ID + "=" + notificationItem.getDbId(), null) > 0;

            db.close();

            if (readSuccessful) {
                Log.e("TAG", notificationItem.getType() + ":" + notificationItem.getCreatedTime() + " is read.");
            } else {
                Log.e("TAG", notificationItem.getType() + ":" + notificationItem.getCreatedTime() + " is not read.");
            }
        }

        return readSuccessful;
    }

    // Read records related to the search term
    public ArrayList<NotificationItem> getNotifications() {
        // select query
        String sql = "";
        sql += "SELECT * FROM " + TABLE_NOTIFICATION;
        //sql += " WHERE " + FIELD_TYPE + "='" + TYPE_ROUTE + "' AND ";
        //sql += FIELD_IS_SYNCED + "='" + IS_NOT_SYNCED + "'";
        sql += " ORDER BY " + FIELD_CREATED_TIME + " DESC";
        //sql += " LIMIT 0,5";

        Log.e("getNotifications", sql);
        SQLiteDatabase db = this.getWritableDatabase();

        // execute the query
        Cursor cursor = db.rawQuery(sql, null);

        int recCount = cursor.getCount();
        Log.e("TAG", recCount + " recCount.");

        ArrayList<NotificationItem> notificationItems = new ArrayList<>();
        int x = 0;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                NotificationItem notificationItem = new NotificationItem();

                notificationItem.setDbId(cursor.getInt(cursor.getColumnIndex(FIELD_ID)));
                notificationItem.setSeminarname(cursor.getString(cursor.getColumnIndex(FIELD_SEMINAR_NAME)));
                notificationItem.setDescription(cursor.getString(cursor.getColumnIndex(FIELD_DESCRIPTION)));
                notificationItem.setType(cursor.getString(cursor.getColumnIndex(FIELD_TYPE)));
                notificationItem.setStatus(cursor.getString(cursor.getColumnIndex(FIELD_STATUS)));
                notificationItem.setTransactionId(cursor.getString(cursor.getColumnIndex(FIELD_TRANSACTION_ID)));
                notificationItem.setCreatedTime(cursor.getLong(cursor.getColumnIndex(FIELD_CREATED_TIME)));
                notificationItem.setReason(cursor.getString(cursor.getColumnIndex(FIELD_REASON)));
                notificationItem.setSeminarId(cursor.getString(cursor.getColumnIndex(FIELD_SEMINAR_ID)));
                notificationItem.setImage(cursor.getString(cursor.getColumnIndex(FIELD_IMAGE)));
                notificationItem.setIsRead(cursor.getString(cursor.getColumnIndex(FIELD_IS_READ)));

                Log.e("TAG", notificationItem.getType() + ":" + notificationItem.getCreatedTime() + " GetRow.");
                notificationItems.add(notificationItem);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return notificationItems;

    }

    public int deleteNotification(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String SQLQuery = "delete from Notification where dealId ='" + id + "'";
        Log.e("dealId",""+id);
        db.execSQL(SQLQuery);
        db.close();
        return 1;
    }
    public int deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        String SQLQuery = "delete from Notification";
        db.execSQL(SQLQuery);
        db.close();
        return 1;
    }

    public void Logout()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NOTIFICATION,null,null);
    }
    public int select()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String sqlqry="SELECT * FROM " + TABLE_NOTIFICATION;
        Cursor cursor = db.rawQuery(sqlqry, null);
        Log.e("cursor",""+cursor.getCount());
        return cursor.getCount();
    }

}
