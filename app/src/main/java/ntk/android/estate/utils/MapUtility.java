package ntk.android.estate.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import ntk.android.estate.R;

public class MapUtility {
    public static final String MAP_URL = "https://maps.googleapis.com";

    public static String apiKey = "";
    public static Location currentLocation = null;
    public static Dialog popupWindow;
    public static String ADDRESS = "address";
    public static String LATITUDE = "lat";
    public static String LONGITUDE = "long";
    /**
     * Two Letters county ISO code like PK, US, AU, AE etc
     */
    public static String COUNTRY_ISO_CODE = "";


    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = null;
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception ex) {

        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showToast(Context context, String message) {
        try {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        } catch (Exception ex) {

        }
    }


    public static void showProgress(final Context context) {
        try {
            if (!((Activity) context).isFinishing()) {
                //todo change
                View layout = LayoutInflater.from(context).inflate(R.layout.dialog_load, null);
                popupWindow = new Dialog(context, android.R.style.Theme_Translucent);
                popupWindow.requestWindowFeature(Window.FEATURE_NO_TITLE);
                popupWindow.setContentView(layout);
                popupWindow.setCancelable(false);
                if (!((Activity) context).isFinishing()) {
                    popupWindow.show();
                }
            }

        } catch (Exception e)

        {
            e.printStackTrace();
        }
    }
    public static String stringInsert( String  bag,  int index,String marble) {
        String bagBegin = bag.substring(0,index);
        String bagEnd = bag.substring(index);
        return bagBegin + marble + bagEnd;
    }
public  static String convertSizeThumbnailImage(String imageUrl,  int width,int height)
{
    if(imageUrl.toLowerCase().indexOf(".png")>0||imageUrl.toLowerCase().indexOf(".jpg")>0||imageUrl.toLowerCase().indexOf(".jpeg")>0) {
        String retOut = stringInsert(imageUrl, imageUrl.lastIndexOf("."), "."+width + "x" + height);
        retOut=retOut.replace("/images/","/thumbnails/");
        return retOut;
    }
    return  imageUrl;
}
    public static void hideProgress() {
        try {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


}