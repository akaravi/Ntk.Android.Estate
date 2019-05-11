package ntk.android.estate.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import java.util.HashMap;
import java.util.Map;
import ntk.android.estate.BuildConfig;
import ntk.android.estate.utill.AppUtill;

public class ConfigRestHeader {

    @SuppressLint("HardwareIds")
    public Map<String, String> GetHeaders(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Map<String, String> headers = new HashMap<>();
        headers.put("Token", "");
        headers.put("LocationLong", "0");
        headers.put("LocationLat", "0");
        headers.put("DeviceId", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        headers.put("DeviceBrand", AppUtill.GetDeviceName());
        headers.put("Country", context.getResources().getConfiguration().locale.getDisplayCountry());
        headers.put("Language", context.getResources().getConfiguration().locale.getLanguage());
        headers.put("SimCard", manager.getSimOperatorName());
        headers.put("PackageName", BuildConfig.APPLICATION_ID);
        headers.put("AppBuildVer", String.valueOf(BuildConfig.VERSION_CODE));
        headers.put("AppSourceVer", BuildConfig.VERSION_NAME);
//        String NotId = FirebaseInstanceId.getInstance().getToken();
        String NotId = "";

        if (NotId != null && !NotId.isEmpty() && !NotId.toLowerCase().equals("null"))
            headers.put("NotificationId", NotId);

        return headers;
    }
}
