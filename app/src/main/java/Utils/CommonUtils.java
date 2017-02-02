package Utils;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import creadigol.com.Meetto.MeettoApplication;
import creadigol.com.Meetto.R;

public class CommonUtils {

    private static ProgressDialog progressDialog;

    public static Bitmap img;
    public static boolean IS_GPS_ON = false;
    public static final String KEY_EXTRA_LOGOUT = "logout";

    public static boolean isNetworkAvailable() {
        boolean isNetworkAvailable = false;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) MeettoApplication.getInstance()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            isNetworkAvailable = true;
        }

        return isNetworkAvailable;
    }

    public static void initGps(final Context context) {
        PackageManager pm = context.getPackageManager();
        boolean hasGps = pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
        //Toast.makeText(context,"initGps called",Toast.LENGTH_SHORT).show();
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Ask the user to enable GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Location Manager");
            builder.setMessage("Your app need to know your location,Would you like to enable GPS?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Launch settings, allowing user to make a change
                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(i);
                    dialog.dismiss();

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //No location service, no Activity
                    //finish();
                    Toast.makeText(context, "App needs to know your Location. Please turn on Gps Settings", Toast.LENGTH_LONG).show();
                    initGps(context);
                }
            });
            Dialog dialog = builder.create();
            dialog.show();

            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
        }

        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            IS_GPS_ON = false;
        else
            IS_GPS_ON = true;
    }


    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }
   /* public static void logout(final Context context)
    {
        PreferenceSettings mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();
            mPreferenceSettings.clearSession();
            Intent i = new Intent(context, Home_Activity.class);
            i.putExtra(KEY_EXTRA_LOGOUT,true);
            i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);
             ((Activity)context).finish();
    }*/
    public static Bitmap globalbitmap = null;

    public static void showAlertWithNegativeButton(Context context, String title, String message, DialogInterface.OnClickListener positiveButtonListener, DialogInterface.OnClickListener negativeButtonListener) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, android.support.v7.appcompat.R.style.Base_Theme_AppCompat_Dialog_Alert);

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Ok", positiveButtonListener);

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", negativeButtonListener);

        // Showing Alert Message
        alertDialog.show();
        alertDialog.setCancelable(false);
    }

    public static void showAlertWithNegativeButton(final Context context, String title, String message, DialogInterface.OnClickListener positiveButtonListener) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, android.support.v7.appcompat.R.style.Base_Theme_AppCompat_Light_Dialog_Alert);

        // Setting Dialog Title
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton(R.string.your_listing_ok, positiveButtonListener);

        // on pressing cancel button
        alertDialog.setNegativeButton(R.string.your_listing_Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                //to colse activity
                ((Activity)context).finish();

            }
        });

        // Showing Alert Message
        alertDialog.show();

    }

    public static void showAlert(Context context, String title, String message, DialogInterface.OnClickListener positiveButtonListener) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, android.support.v7.appcompat.R.style.Base_Theme_AppCompat_Dialog_Alert);

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Ok", positiveButtonListener);

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public static void showAlert(Context context, String title, String message) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, android.support.v7.appcompat.R.style.Base_Theme_AppCompat_Dialog_Alert);

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public static void showToast(String message) {
        Toast.makeText(MeettoApplication.getInstance(), message, Toast.LENGTH_LONG).show();
    }

    public static String getCapitalize(String input) {
        if (input != null && input.trim().length() > 0)
            return Character.toString(input.charAt(0)).toUpperCase() + input.substring(1);
        else
            return "";
    }
    public static String getFormatedDate(long milliSeconds, String dateFormat)
    {
        DateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());

    }


}
