package ntk.android.estate.utill;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class AppUtill {
    private final static AtomicInteger ID = new AtomicInteger(0);

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static String CheckingCellPhone(String PhoneNumber) {
        if (!PhoneNumber.equals("")) {
            if (PhoneNumber.startsWith("09"))
                PhoneNumber = "98" + PhoneNumber.substring(1);
            else if (PhoneNumber.startsWith("+98"))
                PhoneNumber = PhoneNumber.substring(1);
            else if (PhoneNumber.startsWith("0098"))
                PhoneNumber = PhoneNumber.substring(2);
            else if (PhoneNumber.startsWith("98"))
                PhoneNumber = PhoneNumber.substring(0);
            else if (PhoneNumber.startsWith("98"))
                PhoneNumber = PhoneNumber.substring(0);
            else if (PhoneNumber.startsWith("9"))
                PhoneNumber = "98" + PhoneNumber;
        } else PhoneNumber = "NotValid";
        return PhoneNumber;
    }

    public static String ReplaceNumberEn(String string) {
        string = string.replace('۰', '0');
        string = string.replace('۱', '1');
        string = string.replace('۲', '2');
        string = string.replace('۳', '3');
        string = string.replace('۴', '4');
        string = string.replace('۵', '5');
        string = string.replace('۶', '6');
        string = string.replace('۷', '7');
        string = string.replace('۸', '8');
        string = string.replace('۹', '9');
        return string;
    }

    public static String Convert(String Data) {
        try {
            NumberFormat nf = NumberFormat.getCurrencyInstance();
            String result = nf.format(new BigDecimal(Data));
            result = result.replace("$", "");
            result = result.replace(".00", "");
            return result;
        } catch (Exception e) {
            return Data;
        }
    }

    public static String ReplacePersian(String input) {
        input = input.replace("ي", "ی");
        input = input.replace("ك", "ک");
        return input;
    }
}
