package ntk.android.estate.view.component;

import android.content.Context;
import android.graphics.Typeface;

import java.text.DecimalFormat;

public class Utility {

    private static Context contex;

    public static String priceWithSign(String str) {
        return (str == "" || str.length() == 0) ? "0" : new DecimalFormat("#,###,###,###").format(Long.parseLong(str));
    }

    public static String convertToEnglishDigits(String str) {
        return str.replace("١", "1").replace("٢", "2").replace("٣", "3").replace("٤", "4").replace("٥", "5").replace("٦", "6").replace("٧", "7").replace("٨", "8").replace("٩", "9").replace("٠", "0").replace("۱", "1").replace("۲", "2").replace("۳", "3").replace("۴", "4").replace("۵", "5").replace("۶", "6").replace("۷", "7").replace("۸", "8").replace("۹", "9").replace("۰", "0");
    }

    public static Typeface getLightTypeFace() {
        if (SharedPreferencesHelper.getInstance().getLanguage().equalsIgnoreCase("fa")) {
            return Typeface.createFromAsset(getContext().getAssets(), "fonts/IRAN-Sans-Light.ttf");
        }
        return Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
    }

    public static Typeface getMediumTypeFace() {
        if (SharedPreferencesHelper.getInstance().getLanguage().equalsIgnoreCase("fa")) {
            return Typeface.createFromAsset(getContext().getAssets(), "fonts/IRAN-Sans-Medium.ttf");
        }
        return Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
    }

    public static Typeface getRegularTypeFace() {
        if (SharedPreferencesHelper.getInstance().getLanguage().equalsIgnoreCase("fa")) {
            return Typeface.createFromAsset(getContext().getAssets(), "fonts/IRAN-Sans-Regular.ttf");
        }
        return Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
    }

    public static Context getContext() {
        return contex;
    }

    public static void setcontext(Context actSplash) {
        contex = actSplash;
    }
}