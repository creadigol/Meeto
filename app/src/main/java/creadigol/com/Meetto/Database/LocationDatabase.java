package creadigol.com.Meetto.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import Models.Coutryitem;

/**
 * Created by Ashfaq on 4/14/2016.
 */
public class LocationDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Meetto";

    private static Context mContext;
    private static final String TABLE_COUNTRY= "Country";

    private static final String FIELD_ID = "id";
    private static final String FIELD_SHORT_NAME = "sortname";
    private static final String FIELD_COUNTRY_NAME= "name";
    private static final String FIELD_COUNTRY_ID= "id";
    private static final String FIELD_IS_READ = "isRead";

    public static int countExist = 0, countNotExist = 0, countTotal = 0;

    public static final String IS_READ = "1";
    public static final String IS_NOT_READ = "0";

    public LocationDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_SEARCH_TABLE = "CREATE TABLE " + TABLE_COUNTRY+ " ( "
                + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FIELD_SHORT_NAME+ " TEXT, "
                + FIELD_COUNTRY_ID + " TEXT, "
                + FIELD_COUNTRY_NAME + " TEXT "
                + ");";
        Log.e("", "CREATE_SEARCH_TABLE >> " + CREATE_SEARCH_TABLE);
        sqLiteDatabase.execSQL(CREATE_SEARCH_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRY);
        onCreate(sqLiteDatabase);
    }

    // check if a record exists so it won't insert the next time you run this code
    public boolean checkIfExists(Coutryitem coutryitem) {

        boolean recordExists = false;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + FIELD_ID + " FROM " + TABLE_COUNTRY+ " WHERE " + FIELD_COUNTRY_ID+ " = '" + coutryitem.getCountry_id()
                + "' AND " + FIELD_COUNTRY_NAME+ " = '" + coutryitem.getCountry_name()
                + "'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                recordExists = true;
//                Log.e("TAG", notificationItem.getType() + ":" + notificationItem.getCreatedTime() + " recordExists.");
            }
        }

        cursor.close();
        db.close();

        return recordExists;
    }


    // createNotification new record
    // @param myObj contains details to be added as single row.
    public boolean createNotification(Coutryitem  coutryitem) {

        boolean createSuccessful = false;

        if (!checkIfExists(coutryitem)) {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(FIELD_COUNTRY_ID, coutryitem.getCountry_id());
            values.put(FIELD_COUNTRY_NAME, coutryitem.getCountry_name());
            values.put(FIELD_IS_READ, IS_NOT_READ);

            createSuccessful = db.insert(TABLE_COUNTRY, null, values) > 0;
            db.close();

            if (createSuccessful) {
                Log.e("TAG", coutryitem.getCountry_id() + ":" + coutryitem.getCountry_name() + " created.");
            } else {
                Log.e("TAG", coutryitem.getCountry_id() + ":" + coutryitem.getCountry_name() + "Not created.");
            }
        }

        return createSuccessful;
    }

    public boolean makeItRead(Coutryitem coutryitem) {

        boolean readSuccessful = false;

        if (checkIfExists(coutryitem)) {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(FIELD_IS_READ, IS_READ);

            readSuccessful = db.update(TABLE_COUNTRY, values, FIELD_ID + "=" + coutryitem.getDb_id(), null) > 0;

            db.close();

            if (readSuccessful) {
                Log.e("TAG", coutryitem.getCountry_id() + ":" + coutryitem.getCountry_name() + " read.");
            } else {
                Log.e("TAG", coutryitem.getCountry_id() + ":" + coutryitem.getCountry_name() + " not reasd.");
            }
        }

        return readSuccessful;
    }

    // Read records related to the search term
    public ArrayList<Coutryitem> getNotifications() {
        // select query
        String sql = "";
        sql += "SELECT * FROM " + TABLE_COUNTRY;
        //sql += " WHERE " + FIELD_TYPE + "='" + TYPE_ROUTE + "' AND ";
        //sql += FIELD_IS_SYNCED + "='" + IS_NOT_SYNCED + "'";
        sql += " ORDER BY " + FIELD_COUNTRY_ID+ " DESC";
        //sql += " LIMIT 0,5";

        Log.e("getNotifications", sql);
        SQLiteDatabase db = this.getWritableDatabase();

        // execute the query
        Cursor cursor = db.rawQuery(sql, null);

        int recCount = cursor.getCount();
        Log.e("TAG", recCount + " recCount.");

        ArrayList<Coutryitem> coutryitems= new ArrayList<>();
        int x = 0;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Coutryitem coutryitem1= new Coutryitem();

                coutryitem1.setDb_id(cursor.getString(cursor.getColumnIndex(FIELD_ID)));
                coutryitem1.setCountry_id(cursor.getString(cursor.getColumnIndex(FIELD_COUNTRY_ID)));
                coutryitem1.setCountry_name(cursor.getString(cursor.getColumnIndex(FIELD_COUNTRY_NAME)));

                Log.e("TAG", coutryitem1.getCountry_id()+ ":" + coutryitem1.getCountry_name() + " GetRow.");
                coutryitems.add(coutryitem1);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return coutryitems;

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
        db.delete(TABLE_COUNTRY,null,null);
    }
    public int select()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String sqlqry="SELECT * FROM " + TABLE_COUNTRY;
        Cursor cursor = db.rawQuery(sqlqry, null);
        Log.e("cursor",""+cursor.getCount());
        return cursor.getCount();
    }

}
