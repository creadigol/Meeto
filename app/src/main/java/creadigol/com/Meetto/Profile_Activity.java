package creadigol.com.Meetto;

import android.app.AlarmManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.facebook.login.LoginManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import App.MyApplication;
import Models.CityModel;
import Models.CountryModel;
import Models.ProfileItems;
import Models.Profile_item;
import Models.SeminarDetailsarrayitem;
import Models.StateModel;
import Utils.AppUrl;
import Utils.CommonUtils;
import Utils.Jsonkey;
import Utils.ParamsKey;
import Utils.PreferenceSettings;

/**
 * Created by Creadigol on 05-09-2016.
 */
public class Profile_Activity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    //profile
    private static final int ACTION_TAKE_PHOTO = 1;
    private static final int ACTION_SELECT_PHOTO = 2;
    File f, mProFile, newFile1;
    private String mCameraPhotoPath, mGallaryPhotoPath;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    public static Bitmap mBitmap = null,mBitmapCrop = null;
    EditText fname, lname, dob, mobileno, address, yourself, company_name, company_desc, fax_number, url, email;
    PreferenceSettings mPreferenceSettings;
    String st_fname, st_lname, st_email, st_gender = "", st_address, st_dob, st_yourself, st_mobile, st_company_name, st_company_desc, st_faxnumber, st_url, st_timezone;
    Button save_profile, verifyemail;
    RadioGroup rg_gender;
    LinearLayout chooseimage;
    FrameLayout change_image;
    ImageView iv_profile;
    TextView txt_english, txt_japanese,logout;
    String st_langauge = "", st_organisation = "";
    public static final String KEY_EXTRA_LOGOUT = "logout";
    private final String TAG = Profile_Activity.class.getSimpleName();
    public static ArrayList<CountryModel> countrylist;
    public static ArrayList<StateModel> statelist;
    public static ArrayList<CityModel> citylist;
    public static int countryId, stateId, cityId, countryPos, statePos, cityPos;
    String Country_id, State_id, City_id;
    Spinner spCountry, spState, spCity;
    int statecountryId = 0, cityStateId = 0;
    String[] arrayForSpinner = {"(UTC+09:00) japan"};
    private Spinner sp_timezone;
    private ArrayAdapter<String> idAdapter;
    private SimpleDateFormat sdf;
    public static ArrayList<String> language= new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();
        if (mPreferenceSettings.getLUNGAUGE()) {
            MeettoApplication.language("ja");
        } else {
            MeettoApplication.language("en");
        }
        setContentView(R.layout.activity_pofile);
        Toolbar();
        getprofile();
    }

    public void getuserdata() {
        fname = (EditText) findViewById(R.id.displayfName);
        lname = (EditText) findViewById(R.id.displaylName);
        email = (EditText) findViewById(R.id.displayEmail);
        dob = (EditText) findViewById(R.id.displaylbd);
        mobileno = (EditText) findViewById(R.id.tv_mobile);
        address = (EditText) findViewById(R.id.edt_address);
        yourself = (EditText) findViewById(R.id.edt_yourself);
        company_name = (EditText) findViewById(R.id.edt_company_name);
        company_desc = (EditText) findViewById(R.id.edt_compnay_desc);
        fax_number = (EditText) findViewById(R.id.edt_fax_number);
        url = (EditText) findViewById(R.id.edt_url);
        sp_timezone = (Spinner) findViewById(R.id.sp_timezone);

        txt_english = (TextView) findViewById(R.id.tv_english);
        txt_japanese = (TextView) findViewById(R.id.tv_japanise);

        chooseimage = (LinearLayout) findViewById(R.id.chanegimagelayout);
        change_image = (FrameLayout) findViewById(R.id.change_image);
        if (mPreferenceSettings.getFacebook()) {
            change_image.setVisibility(View.GONE);
        }else{
            change_image.setVisibility(View.VISIBLE);
        }

        iv_profile = (ImageView) findViewById(R.id.iv_profilepic);
        chooseimage.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        save_profile = (Button) findViewById(R.id.btn_save);
        verifyemail = (Button) findViewById(R.id.verifyemail);


        ImageView iv_email = (ImageView) findViewById(R.id.iv_email);

        if (mPreferenceSettings.getEmailverify() != null && mPreferenceSettings.getEmailverify().equals("1")) {
            iv_email.setVisibility(View.VISIBLE);
            verifyemail.setVisibility(View.GONE);
        }

        chooseimage.setOnClickListener(this);
        save_profile.setOnClickListener(this);
        dob.setOnClickListener(this);
        iv_profile.setOnClickListener(this);
        verifyemail.setOnClickListener(this);
    }

    public void organization() {
        final TextView txt_profit_org = (TextView) findViewById(R.id.txt_profite);
        final TextView txt_non_profit_org = (TextView) findViewById(R.id.txt_nonprofit);
        if (mPreferenceSettings.getOrganization().equalsIgnoreCase(getResources().getString(R.string.profile_Profit_Organization))) {
            txt_profit_org.setTextColor(Color.WHITE);
            txt_profit_org.setBackgroundResource(R.drawable.textview_click);
            txt_non_profit_org.setTextColor(Color.GRAY);
            txt_non_profit_org.setBackgroundResource(R.drawable.textview_background);
            st_organisation = getResources().getString(R.string.profile_Profit_Organization);
        } else if (mPreferenceSettings.getOrganization().equalsIgnoreCase(getResources().getString(R.string.profile_non_Profit_Organization))) {
            txt_non_profit_org.setTextColor(Color.WHITE);
            txt_non_profit_org.setBackgroundResource(R.drawable.textview_click);
            txt_profit_org.setTextColor(Color.GRAY);
            txt_profit_org.setBackgroundResource(R.drawable.textview_background);
            st_organisation = getResources().getString(R.string.profile_non_Profit_Organization);
        }

        txt_profit_org.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_profit_org.setTextColor(Color.WHITE);
                txt_profit_org.setBackgroundResource(R.drawable.textview_click);
                txt_non_profit_org.setTextColor(Color.GRAY);
                txt_non_profit_org.setBackgroundResource(R.drawable.textview_background);
                st_organisation = getResources().getString(R.string.profile_Profit_Organization);
            }
        });
        txt_non_profit_org.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_non_profit_org.setTextColor(Color.WHITE);
                txt_non_profit_org.setBackgroundResource(R.drawable.textview_click);
                txt_profit_org.setTextColor(Color.GRAY);
                txt_profit_org.setBackgroundResource(R.drawable.textview_background);
                st_organisation = getResources().getString(R.string.profile_non_Profit_Organization);
            }
        });
    }

    public void language() {

        txt_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_english.getCurrentTextColor() != Color.WHITE) {
                    txt_english.setTextColor(Color.WHITE);
                    txt_english.setBackgroundResource(R.drawable.textview_click);
                    language.add("3");
                } else {
                    txt_english.setTextColor(Color.GRAY);
                    txt_english.setBackgroundResource(R.drawable.textview_background);
                    language.remove(language.indexOf("3"));
                }
            }
        });

        txt_japanese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_japanese.getCurrentTextColor() != Color.WHITE) {
                    txt_japanese.setTextColor(Color.WHITE);
                    txt_japanese.setBackgroundResource(R.drawable.textview_click);
                    language.add("8");
                } else {
                    txt_japanese.setTextColor(Color.GRAY);
                    txt_japanese.setBackgroundResource(R.drawable.textview_background);
                    language.remove(language.indexOf("8"));
                }
            }
        });
    }

    public void Toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView tvHistory = (TextView) toolbar.findViewById(R.id.tv_app_name);
        tvHistory.setText(R.string.profile_toolbar);
        logout = (TextView) toolbar.findViewById(R.id.tv_logout);
        logout.setOnClickListener(this);
    }

    public void setprofile() {
        mProFile = MyApplication.getInstance(Profile_Activity.this).getProfile();
        Log.e("profile in PullZoom", mProFile.getAbsolutePath());
        if (mProFile.exists()) {
            Bitmap bMap = BitmapFactory.decodeFile(mProFile.getAbsolutePath());
            iv_profile.setImageBitmap(bMap);
        } else {
            setProfileImage("file:///" + mPreferenceSettings.getUserPic());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }


    public void showDatePicker() {
        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(this,
                now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        dpd.setThemeDark(false);
        dpd.vibrate(true);
        dpd.show(Profile_Activity.this.getFragmentManager(), "Datepickerdialog");
    }

    //for profile pic set open dailog
    public void setProfileImage(String userImageUrl) {

        com.nostra13.universalimageloader.core.ImageLoader imageLoader = MeettoApplication.getInstance().getImageLoader();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.back)
                .showImageOnFail(R.drawable.back)
                .showImageOnLoading(R.drawable.back)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(0))
                .build();
        //download and display image from url
        imageLoader.displayImage(userImageUrl, iv_profile, options);

    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    private void showEditProfileDialogDailog() {
        final CharSequence[] items = {getResources().getString(R.string.dialog_photo1), getResources().getString(R.string.dialog_photo2),
                getResources().getString(R.string.dialog_photo3)};
        AlertDialog.Builder builder = new AlertDialog.Builder(
                Profile_Activity.this);
        builder.setTitle(getResources().getString(R.string.dialog_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getResources().getString(R.string.dialog_photo1))) {
                    dispatchTakePictureIntent(ACTION_TAKE_PHOTO);
                } else if (items[item].equals(getResources().getString(R.string.dialog_photo2))) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            ACTION_SELECT_PHOTO);
                } else if (items[item].equals(getResources().getString(R.string.dialog_photo3))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void dispatchTakePictureIntent(int actionCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        switch (actionCode) {
            case ACTION_TAKE_PHOTO:
                File f = null;

                try {
                    f = setUpCameraPhotoFile();
                    mCameraPhotoPath = f.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(f));
                } catch (IOException e) {
                    e.printStackTrace();
                    f = null;
                    mCameraPhotoPath = null;
                }
                break;

            default:
                break;
        } // switch

        startActivityForResult(takePictureIntent, actionCode);
    }

    private File setUpCameraPhotoFile() throws IOException {

        f = createImageFile();
        mCameraPhotoPath = f.getAbsolutePath();
        return f;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX,
                albumF);
        return imageF;
    }

    private String getAlbumName() {
        return getString(R.string.album_name);
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name),
                    "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTION_TAKE_PHOTO: {
                if (resultCode == Profile_Activity.this.RESULT_OK) {
                    handleBigCameraPhoto();
                }
                break;
            } // ACTION_TAKE_PHOTO

            case ACTION_SELECT_PHOTO: {
                if (resultCode == Profile_Activity.this.RESULT_OK) {
                    Uri pickedImage = data.getData();
                    // Let's read picked image path using content resolver
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(pickedImage,
                            filePath, null, null, null);
                    cursor.moveToFirst();
                    String imagePath = cursor.getString(cursor
                            .getColumnIndex(filePath[0]));

                    // Now we need to set the GUI ImageView data with data read from
                    // the picked file.
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    setGalleryPic(bitmap);
                    // At the end remember to close the cursor or you will end with
                    // the RuntimeException!
                    cursor.close();
                }
                break;
            }

        }

    }

    private void handleBigCameraPhoto() {

        if (mCameraPhotoPath != null) {
            setCameraPic();
            // galleryAddPic(mCameraPhotoPath);
        }
    }

    private void setCameraPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
        /* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        f = new File(mCameraPhotoPath);
        if (f.exists()) {
            Toast.makeText(getApplicationContext(), "Image file is exist",
                    Toast.LENGTH_LONG).show();
            Log.e("", "mCameraPhotoPath :: " + mCameraPhotoPath);
        }

        Bitmap bitmap = BitmapFactory.decodeFile(mCameraPhotoPath);

        bitmap = resize(bitmap);
        createFileFromBitmap(bitmap, mCameraPhotoPath);
        /* Associate the Bitmap to the ImageView */
        // iv_profile.setImageBitmap(bitmap);
        mBitmap = bitmap;
        if (mBitmap != null) {
            Intent intent = new Intent(getApplicationContext(), DisplayCropingImage.class);
            startActivity(intent);
        }
    }

    public void setGalleryPic(Bitmap bitmap) {

        try {
            f = setUpGalleryPhotoFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mGallaryPhotoPath != null && mGallaryPhotoPath.trim().length() > 0) {

            bitmap = resize(bitmap);
            createFileFromBitmap(bitmap, mGallaryPhotoPath);
            /* Associate the Bitmap to the ImageView */
            // iv_profile.setImageBitmap(bitmap);
            mBitmap = bitmap;
            if (mBitmap != null) {
                Intent intent = new Intent(getApplicationContext(), DisplayCropingImage.class);
                startActivity(intent);

            }
        }
    }

    public void createFileFromBitmap(Bitmap _bitmapScaled, String imgPath) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        _bitmapScaled.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

        // you can create a new file name "test.jpg" in sdcard folder.
        if (imgPath != null && imgPath.trim().length() > 0) {
            try {
                f = new File(imgPath);
                if (!f.exists())
                    f.createNewFile();
                // write the bytes in file
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());

                // remember close de FileOutput
                fo.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            galleryAddPic(imgPath);
        }

    }

    private void galleryAddPic(String imgPath) {
        Intent mediaScanIntent = new Intent(
                "android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(imgPath);
        if (f.exists()) {
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);
        }
    }

    public Bitmap resize(Bitmap btmp) {
        int bitmapWidth = btmp.getWidth();
        Bitmap bitmap = null;

        int targetW = iv_profile.getWidth();
        int targetH = iv_profile.getHeight();

        // scale According to WIDTH
        int scaledWidth;
        if (targetW > 0) {
            scaledWidth = targetW;
        } else {
            scaledWidth = bitmapWidth / 2;
        }

        int scaledHeight = (scaledWidth * btmp.getHeight()) / bitmapWidth;
        try {
            bitmap = Bitmap.createScaledBitmap(btmp, scaledWidth, scaledHeight,
                    true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private File setUpGalleryPhotoFile() throws IOException {
        f = createImageFile();
        mGallaryPhotoPath = f.getAbsolutePath();
        return f;
    }

    private void reqemailverifiaction() {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                AppUrl.URL_VERIFYEMAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response.toString());
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("response", "" + jsonObject);
                    int status_code = jsonObject.getInt("status_code");
                    Log.e("code", "" + status_code);
                    String message = jsonObject.getString("message");
                    if (status_code == 1) {
                        pDialog.dismiss();
                        Toast.makeText(Profile_Activity.this, message, Toast.LENGTH_LONG).show();
                    } else if (status_code == 0) {
                        pDialog.dismiss();
                        Toast.makeText(Profile_Activity.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    pDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.e("Error_in", "onErrorResponse");
                pDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_USERID, mPreferenceSettings.getUserId());
                params.put(ParamsKey.KEY_EMAIL, mPreferenceSettings.getUserEmail());

                Log.e(TAG, "reqUserLogin params: " + params.toString());

                return params;
            }

        };
        MeettoApplication.getInstance().addToRequestQueue(jsonObjReq, TAG);
    }

    public void getprofile() {
        final ProgressDialog mProgressDialog = new ProgressDialog(Profile_Activity.this);
        mProgressDialog.setMessage("Loading.....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, AppUrl.URL_USER_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgressDialog.dismiss();
                try {
                    Log.e(TAG, "Response: " + response);
                    JSONObject jsonObject = new JSONObject(response);
                    ProfileItems profileItems = new ProfileItems();

                    try {
                        profileItems.setStatus_code(jsonObject.getInt(Jsonkey.statusCode));
                    } catch (JSONException e) {
                        profileItems.setStatus_code(0);
                    }

                    profileItems.setMessage(jsonObject.optString(Jsonkey.message));


                    if (profileItems.getStatus_code() == 1) {
                        JSONObject jsonObjectOutlet = jsonObject.getJSONObject(Jsonkey.Userprofile_Key);

                        profileItems.setUid(jsonObjectOutlet.optString(Jsonkey.userUserid));
                        profileItems.setFname(jsonObjectOutlet.optString(Jsonkey.userFirstName));
                        profileItems.setLname(jsonObjectOutlet.optString(Jsonkey.userLastName));
                        profileItems.setEmail(jsonObjectOutlet.optString(Jsonkey.userEmail));
                        profileItems.setGender(jsonObjectOutlet.optString(Jsonkey.gender));
                        profileItems.setDob(jsonObjectOutlet.optString(Jsonkey.userdob));
                        profileItems.setPhone_no(jsonObjectOutlet.optString(Jsonkey.userMobile));
                        profileItems.setAddress(jsonObjectOutlet.optString(Jsonkey.useraddress));
                        profileItems.setYourself(jsonObjectOutlet.optString(Jsonkey.userself));
                        profileItems.setCountryid(jsonObjectOutlet.optString(Jsonkey.countryid));
                        profileItems.setStateid(jsonObjectOutlet.optString(Jsonkey.stateid));
                        profileItems.setCityid(jsonObjectOutlet.optString(Jsonkey.homecityid));
                        profileItems.setPhoto(jsonObjectOutlet.optString(Jsonkey.userphoto));
                        profileItems.setCompany_name(jsonObjectOutlet.optString(Jsonkey.companyname));
                        profileItems.setCompnay_desc(jsonObjectOutlet.optString(Jsonkey.company_desc));
                        profileItems.setTime_zone(jsonObjectOutlet.optString(Jsonkey.timezone));
                        profileItems.setOrganization(jsonObjectOutlet.optString(Jsonkey.organization));
                        profileItems.setFaxno(jsonObjectOutlet.optString(Jsonkey.faxnumber));
                        profileItems.setUrl(jsonObjectOutlet.optString(Jsonkey.url));
                        profileItems.setEmail_verify(jsonObjectOutlet.optString(Jsonkey.emailverify));

                        try {
                            JSONArray launage = jsonObjectOutlet.getJSONArray(Jsonkey.language);
                            profileItems.setProfile(launage);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //set outlet detail
                        setprofiledetails(profileItems);

                        mProgressDialog.dismiss();
                    } else if (profileItems.getStatus_code() == 0 || profileItems.getStatus_code() == 2) {
                        mProgressDialog.dismiss();
                        CommonUtils.showToast(profileItems.getMessage());
                        finish();
                        //showTryAgainAlert("Info", outletObject.getMessage() + " Try again!");
                    } else {
                        mProgressDialog.dismiss();

                        Log.e("Error_in", "Error_else");

                    }
                } catch (JSONException e) {
                    mProgressDialog.dismiss();
                    Log.e("Error_in", "catch");

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Log.e("Error_in", "onErrorResponse");

                showTryAgainAlert("" + getResources().getString(R.string.network1) + "", getResources().getString(R.string.network2) + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                PreferenceSettings mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();

                Map<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_TYPE, "VIEW");
                params.put(ParamsKey.KEY_USERID, mPreferenceSettings.getUserId());
                if (mPreferenceSettings.getLUNGAUGE()) {
                    params.put(ParamsKey.KEY_USER_LANG, "ja");
                }
                if (mPreferenceSettings.getLUNGAUGE() == false) {
                    params.put(ParamsKey.KEY_USER_LANG, "en");
                }
                Log.e(TAG, "reqOutletData params: " + params.toString());

                return params;
            }
        };

        MeettoApplication.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
    }

    public void setprofiledetails(final ProfileItems profileItems) {
        getuserdata();
        setprofile();
        if (profileItems.getCountryid() != null) {
            Country_id = profileItems.getCountryid();
        }
        if (profileItems.getStateid() != null) {
            State_id = profileItems.getStateid();
        }
        if (profileItems.getCityid() != null) {
            City_id = profileItems.getCityid();
        }

        GetCountry();

        fname.setText(profileItems.getFname());
        lname.setText(profileItems.getLname());
        email.setText(profileItems.getEmail());
        dob.setText(profileItems.getDob());
        mobileno.setText(profileItems.getPhone_no());
        address.setText(profileItems.getAddress());
        yourself.setText(profileItems.getYourself());
        company_name.setText(profileItems.getCompany_name());
        company_desc.setText(profileItems.getCompnay_desc());
        fax_number.setText(profileItems.getFaxno());
        if (!profileItems.getEmail_verify().equalsIgnoreCase("")) {
            mPreferenceSettings.setEmailverify(profileItems.getEmail_verify());
        }
        if (profileItems.getOrganization() != null) {
            mPreferenceSettings.setOrganization(profileItems.getOrganization());
        }

        organization();
        if (profileItems.getProfile() != null && profileItems.getProfile().size() > 0) {
            setlungeage(profileItems.getProfile());
        }
        url.setText(profileItems.getUrl());
        language();

        if (profileItems.getGender() != null) {
            if (profileItems.getGender().equalsIgnoreCase("Male")) {
                RadioButton rb_male = (RadioButton) findViewById(R.id.rb_male);
                rb_male.setChecked(true);
            } else if (profileItems.getGender().equalsIgnoreCase("Female")) {
                RadioButton rb_female = (RadioButton) findViewById(R.id.rb_female);
                rb_female.setChecked(true);
            }
        }


        if (profileItems.getPhoto() != null) {
            mPreferenceSettings.setUserPic(profileItems.getPhoto());
            setProfileImage(profileItems.getPhoto());
        }
        if (Profile_Activity.mBitmapCrop != null)

            iv_profile.setImageBitmap(Profile_Activity.mBitmapCrop);
        else if (Profile_Activity.mBitmap != null)
            iv_profile.setImageBitmap(Profile_Activity.mBitmap);

        sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");

        idAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayForSpinner);

        idAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_timezone.setAdapter(idAdapter);

        sp_timezone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
                am.setTimeZone("Japan");

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


    }

    public void setlungeage(ArrayList<Profile_item> profile_items) {
        if (profile_items != null) {
            language.clear();
            for (Profile_item profile_item : profile_items) {
                if (profile_item.getLanguage().equalsIgnoreCase("English")) {
                    txt_english.setTextColor(Color.WHITE);
                    txt_english.setBackgroundResource(R.drawable.textview_click);
                    language.add("3");
                }
                if (profile_item.getLanguage().equalsIgnoreCase("japanese")) {
                    txt_japanese.setTextColor(Color.WHITE);
                    txt_japanese.setBackgroundResource(R.drawable.textview_click);
                    language.add("8");
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_logout:
                mPreferenceSettings.setIslogin(false);
                mPreferenceSettings.clearSession();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(Profile_Activity.this, Home_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(KEY_EXTRA_LOGOUT, true);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_save:
                try {
                    rg_gender = (RadioGroup) findViewById(R.id.radiogroup);
                    int Check = rg_gender.getCheckedRadioButtonId();
                    RadioButton rb_male = (RadioButton) findViewById(Check);
                    String gender;
                    gender = (String) rb_male.getText();
                    if (gender.equalsIgnoreCase(getResources().getString(R.string.profile_Gender_Male))) {
                        st_gender = "Male";
                    } else if (gender.equalsIgnoreCase(getResources().getString(R.string.profile_Gender_Female))) {
                        {
                            st_gender = "Female";
                        }
                    }
                } catch (Exception ex) {

                }
                if (language != null && language.size() > 0) {
                    for (int i = 0; i < language.size(); i++) {
                        st_langauge = st_langauge + language.get(i) + ",";
                        mPreferenceSettings.setLanguage(st_langauge);

                    }
                }

                st_fname = fname.getText().toString().trim();
                st_lname = lname.getText().toString().trim();
                st_email = email.getText().toString().trim();
                st_dob = dob.getText().toString().trim();
                st_mobile = mobileno.getText().toString().trim();
                st_yourself = yourself.getText().toString().trim();
                st_address = address.getText().toString().trim();
                st_company_name = company_name.getText().toString().trim();
                st_company_desc = company_desc.getText().toString().trim();
                st_faxnumber = fax_number.getText().toString().trim();
                st_url = url.getText().toString().trim();
                reqsavedata();
                break;
            case R.id.displaylbd:
                showDatePicker();
                break;

            case R.id.chanegimagelayout:
                showEditProfileDialogDailog();
                break;

            case R.id.iv_profilepic:
                break;

            case R.id.verifyemail:
                reqemailverifiaction();
                break;
        }
    }


    String profileimage = "";


    public void reqsavedata() {
        //get path from select image
        newFile1 = MyApplication.getInstance(getApplicationContext()).getProfile();
        Log.e("newprofile", " " + newFile1);
        try {
            if (DisplayCropingImage.f != null) {
                copy(DisplayCropingImage.f, newFile1);
            } else {
                copy(mProFile, newFile1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //set path into file for volley
        if (DisplayCropingImage.f != null) {
            try {
                ByteArrayOutputStream profile = new ByteArrayOutputStream();
                MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(newFile1)).compress(Bitmap.CompressFormat.JPEG, 100, profile);
                byte[] imagepath = profile.toByteArray();
                profileimage = Base64.encodeToString(imagepath, Base64.DEFAULT);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            profileimage = "nochange";
        }

        final ProgressDialog mProgressDialog = new ProgressDialog(Profile_Activity.this);
        mProgressDialog.setMessage("Loading.....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, AppUrl.URL_USER_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("User update response", response);
                mProgressDialog.dismiss();

                try {
                    JSONObject responseObj = new JSONObject(response);
                    String message = responseObj.getString("message");

                    int status_code = responseObj.getInt("status_code");
                    if (status_code == 1) {
                        getprofile();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } else if (status_code == 0) {
                        mProgressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } else {
                        mProgressDialog.dismiss();
                        Log.e("Error_in", "else");
                    }


                } catch (JSONException e) {
                    mProgressDialog.dismiss();
                    Log.e("Error_in", "catch");
                    showTryAgainAlert("" + getResources().getString(R.string.network1) + "", getResources().getString(R.string.network2) + "");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Log.e("Error_in", "onErrorResponse");
                showTryAgainAlert("" + getResources().getString(R.string.network1) + "", getResources().getString(R.string.network2) + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_USERID, mPreferenceSettings.getUserId());
                params.put(ParamsKey.KEY_FNAME, st_fname);
                params.put(ParamsKey.KEY_LNAME, st_lname);
                params.put(ParamsKey.KEY_MOBILE, st_mobile);
                params.put(ParamsKey.KEY_GENDER, st_gender);
                params.put(ParamsKey.KEY_EMAIL, st_email);
                params.put(ParamsKey.KEY_DOB, st_dob);
                params.put(ParamsKey.KEY_ADDRESS, st_address);
                params.put(ParamsKey.KEY_YOURSELF, st_yourself);
                params.put(ParamsKey.KEY_COMPANY_NAME, st_company_name);
                params.put(ParamsKey.KEY_COMPANY_DESC, st_company_desc);
                params.put(ParamsKey.KEY_TIMEZONE, "1");
                params.put(ParamsKey.KEY_ORGANIZATION_TYPE, st_organisation);
                params.put(ParamsKey.KEY_LANGUAGE, st_langauge);
                params.put(ParamsKey.KEY_TYPE, "EDIT");
                params.put(ParamsKey.KEY_FAXNUMBER, st_faxnumber);
                params.put(ParamsKey.KEY_URL, st_url);
                params.put(ParamsKey.KEY_COUNTRY, String.valueOf(countryId));
                params.put(ParamsKey.KEY_STATE, String.valueOf(stateId));
                params.put(ParamsKey.KEY_CITY, String.valueOf(cityId));
                params.put(ParamsKey.KEY_USER_PHOTO, profileimage);
                if (mPreferenceSettings.getLUNGAUGE()) {
                    params.put(ParamsKey.KEY_USER_LANG, "ja");
                }
                if (mPreferenceSettings.getLUNGAUGE() == false) {
                    params.put(ParamsKey.KEY_USER_LANG, "en");

                }
                Log.e(TAG, "reqOutletData params: " + params.toString());

                return params;
            }
        };

        MeettoApplication.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
    }

    public void showTryAgainAlert(String title, String message) {
        CommonUtils.showAlertWithNegativeButton(Profile_Activity.this, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                if (CommonUtils.isNetworkAvailable())
                    reqsavedata();
                else
                    CommonUtils.showToast(getResources().getString(R.string.network3));

            }
        });
    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = +dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

        dob.setText(date.trim());
        st_dob = dob.getText().toString();
    }


    private void GetCountry() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        String url = AppUrl.URL_COUNTRY;
        StringRequest countryreq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.e("Country Response", response);

                try {
                    JSONObject responseobj = new JSONObject(response);
                    JSONObject countrydetail = responseobj.getJSONObject("country_detail");

                    int Status_code = countrydetail.getInt("status_code");
                    countrylist = new ArrayList<CountryModel>();
                    if (Status_code == 1) {
                        JSONArray Countryarray = countrydetail.getJSONArray("country_detail");
                        for (int i = 0; i < Countryarray.length(); i++) {
                            JSONObject Countryarrayobj = Countryarray.getJSONObject(i);

                            CountryModel countryModel = new CountryModel();

                            countryModel.setId(Countryarrayobj.getInt("id"));
                            countryModel.setSortname(Countryarrayobj.getString("sortname"));
                            countryModel.setName(Countryarrayobj.getString("name"));
                            countrylist.add(countryModel);
                        }
                        SetCountrySpinner();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("Country Response", error.toString());

            }
        });
        countryreq.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance(this).addToRequestQueue(countryreq);
    }

    public ArrayList<String> getCounrtyNameList() {

        ArrayList<String> countryName = new ArrayList<>();
        for (int i = 0; i < countrylist.size(); i++) {
            countryName.add(countrylist.get(i).getName());
        }
        return countryName;
    }

    public String getData(int id, int type) {
        String brea = "";
        if (type == 1 && countrylist != null && countrylist.size() > 0) {
            for (int i = 0; i < countrylist.size(); i++) {
                if (countrylist.get(i).getId() == id) {
                    brea = countrylist.get(i).getName();
                    break;
                }
            }
        } else if (type == 2 && statelist != null && statelist.size() > 0) {
            for (int i = 0; i < statelist.size(); i++) {
                if (statelist.get(i).getId() == id) {
                    brea = statelist.get(i).getName();
                    break;
                }
            }
        } else if (type == 3 && citylist != null && citylist.size() > 0) {
            for (int i = 0; i < citylist.size(); i++) {
                if (citylist.get(i).getId() == id) {
                    brea = citylist.get(i).getName();
                    break;
                }
            }
        }

        return brea;
    }

    private void SetCountrySpinner() {
        ArrayAdapter a1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, getCounrtyNameList());

        spCountry = (Spinner) findViewById(R.id.sp_country);

        spCountry.setAdapter(a1);
        if (Country_id != null && Country_id.length() > 0) {
            if (getData(Integer.parseInt(Country_id), 1).length() > 3)
                spCountry.setSelection(a1.getPosition(getData(Integer.parseInt(Country_id), 1)));
        }

        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("position", position + "");
                Log.e("in spi countrylist size", countrylist.size() + "");
                if (position > -1) {
                    countryPos = position;
                    countryId = countrylist.get(position).getId();

                    Log.e("Country SelectedId", position + "");

                    if (statecountryId == countryId && countrylist != null
                            && countrylist.size() > 0) {
                        Log.e("In If block", statelist.size() + "");
                        SetStateSpinner();
                    } else {
                        statecountryId = countrylist.get(position).getId();
                        Log.e("In else block", countryId + "" + "statecountryId ::" + statecountryId);
                        GetState(countryId);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (countryPos != -1 && countryPos > 0) {
            Log.e("countryPos", countryPos + "");
            spCountry.setSelection(countryPos);
        }
    }

    public ArrayList<String> getStateNameList() {

        ArrayList<String> stateName = new ArrayList<>();
        for (int i = 0; i < statelist.size(); i++) {
            stateName.add(statelist.get(i).getName());
        }
        return stateName;
    }

    private void SetStateSpinner() {
        ArrayAdapter a1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, getStateNameList());
        spState = (Spinner) findViewById(R.id.sp_state);

//        spState.setAdapter(a1);
        if (State_id != null && State_id.length() > 0) {
            if (getData(Integer.parseInt(State_id), 2).length() > 3) {
                spState.setSelection(a1.getPosition(getData(Integer.parseInt(State_id), 2)));
            } else {
                spState.setSelection(0);
            }
        } else {
            spState.setSelection(0);
        }

        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.e("position", position + "");
                Log.e("in spi statelist size", statelist.size() + "");
                if (position > -1) {
                    statePos = position;
                    stateId = statelist.get(position).getId();

                    Log.e("State SelectedId", position + "");

                    cityStateId = statelist.get(position).getId();
                    Log.e("In else block", stateId + "" + "cityStateId ::" + cityStateId);
                    GetCity(stateId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spState.setAdapter(a1);
       /* if (statePos != -1 && statePos > 0) {
            Log.e("statePos", statePos + "");
            spState.setSelection(statePos);
        }*/
    }

    public ArrayList<String> getCityNameList() {
        ArrayList<String> cityName = new ArrayList<>();
        for (int i = 0; i < citylist.size(); i++) {
            cityName.add(citylist.get(i).getName());
        }

        return cityName;
    }

    private void setCityspinner() {
        spCity = (Spinner) findViewById(R.id.sp_city);
        ArrayAdapter a1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, getCityNameList());

        if (City_id != null && City_id.length() > 0) {
            if (getData(Integer.parseInt(City_id), 3).length() > 3) {
                spCity.setSelection(a1.getPosition(getData(Integer.parseInt(City_id), 3)));
            } else {
                spCity.setSelection(0);
            }
        } else {
            spCity.setSelection(0);
        }

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.e("position", position + "");
                Log.e("in spi citylist size", citylist.size() + "");


                if (position > -1) {
                    try {
                        cityPos = position;
                        cityId = citylist.get(position).getId();
                        Log.e("City SelectedId", position + "");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCity.setAdapter(a1);

    }

    private void GetCity(final int selectedId) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        String url = AppUrl.URL_CITY;
        StringRequest cityreq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("City Response", response);

                try {
                    dialog.dismiss();
                    JSONObject responseobj = new JSONObject(response);
                    JSONObject citydetail = responseobj.getJSONObject("city_detail");

                    int Status_code = citydetail.getInt("status_code");
                    String Message = citydetail.getString("message");

                    citylist = new ArrayList<CityModel>();
                    if (Status_code == 1) {
                        JSONArray cityArray = citydetail.getJSONArray("city_detail");
                        if (cityArray != null && cityArray.length() > 0) {
                            citylist.clear();
                        }
                        for (int k = 0; k < cityArray.length(); k++) {
                            JSONObject cityobj = cityArray.getJSONObject(k);

                            CityModel cityModel = new CityModel();
                            cityModel.setId(cityobj.getInt("id"));
                            cityModel.setName(cityobj.getString("name"));
                            citylist.add(cityModel);
                        }

                        setCityspinner();
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("City Response", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("state_id", selectedId + "");
                return params;
            }
        };
        cityreq.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance(this).addToRequestQueue(cityreq);
    }

    private void GetState(final int selectedId) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        String url = AppUrl.URL_STATE;
        StringRequest Statereq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("State Response", response);
                dialog.dismiss();
                try {

                    JSONObject responseobj = new JSONObject(response);
                    JSONObject Statedetail = responseobj.getJSONObject("state_detail");

                    int Status_code = Statedetail.getInt("status_code");
                    String Message = Statedetail.getString("message");
                    statelist = new ArrayList<StateModel>();
                    if (Status_code == 1) {
                        JSONArray StateArray = Statedetail.getJSONArray("state_detail");

                        for (int j = 0; j < StateArray.length(); j++) {
                            JSONObject Stateobj = StateArray.getJSONObject(j);

                            StateModel stateModel = new StateModel();

                            stateModel.setName(Stateobj.getString("name"));
                            stateModel.setId(Stateobj.getInt("id"));


                            statelist.add(stateModel);
                        }
                        SetStateSpinner();
                    }

                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
//                dialog.dismiss();
                Log.e("State Response", error.toString());

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("country_id", selectedId + "");
                return params;
            }
        };
        Statereq.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance(this).addToRequestQueue(Statereq);
    }
}
