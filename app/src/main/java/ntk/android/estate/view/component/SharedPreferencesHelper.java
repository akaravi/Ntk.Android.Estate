package ntk.android.estate.view.component;

import android.content.SharedPreferences;


public class SharedPreferencesHelper {

    /* renamed from: o */
    private static SharedPreferencesHelper f16289o;
    public final String ARAVM_LAT;
    public final String ARAVM_LONG;
    public final String HELP_HOME = "helpHome";
    public final String HELP_MAP = "helpMap";

    /* renamed from: a */
    private final SharedPreferences f16290a;

    /* renamed from: b */
    private final SharedPreferences.Editor f16291b;

    /* renamed from: c */
    private final SharedPreferences f16292c;

    /* renamed from: d */
    private final SharedPreferences.Editor f16293d;

    /* renamed from: e */
    private final SharedPreferences f16294e;

    /* renamed from: f */
    private final SharedPreferences.Editor f16295f;

    /* renamed from: g */
    private final SharedPreferences f16296g;

    /* renamed from: h */
    private final SharedPreferences.Editor f16297h;

    /* renamed from: i */
    private final SharedPreferences f16298i;

    /* renamed from: j */
    private final SharedPreferences.Editor f16299j;

    /* renamed from: k */
    private final SharedPreferences f16300k;

    /* renamed from: l */
    private final SharedPreferences.Editor f16301l;

    /* renamed from: m */
    private final SharedPreferences f16302m;

    /* renamed from: n */
    private final SharedPreferences.Editor f16303n;

    public SharedPreferencesHelper() {
        SharedPreferences sharedPreferences = Utility.getContext().getSharedPreferences("SignUpInfo", 0);
        this.f16290a = sharedPreferences;
        this.f16291b = sharedPreferences.edit();
        SharedPreferences sharedPreferences2 = Utility.getContext().getSharedPreferences("Settings", 0);
        this.f16292c = sharedPreferences2;
        this.f16293d = sharedPreferences2.edit();
        SharedPreferences sharedPreferences3 = Utility.getContext().getSharedPreferences("PushNotification", 0);
        this.f16294e = sharedPreferences3;
        this.f16295f = sharedPreferences3.edit();
        SharedPreferences sharedPreferences4 = Utility.getContext().getSharedPreferences("UserInfoV2", 0);
        this.f16296g = sharedPreferences4;
        this.f16297h = sharedPreferences4.edit();
        SharedPreferences sharedPreferences5 = Utility.getContext().getSharedPreferences("SearchSettings", 0);
        this.f16298i = sharedPreferences5;
        this.f16299j = sharedPreferences5.edit();
        SharedPreferences sharedPreferences6 = Utility.getContext().getSharedPreferences("Help", 0);
        this.f16300k = sharedPreferences6;
        this.f16301l = sharedPreferences6.edit();
        SharedPreferences sharedPreferences7 = Utility.getContext().getSharedPreferences("LastLatLongForARAVM", 0);
        this.f16302m = sharedPreferences7;
        this.f16303n = sharedPreferences7.edit();
        this.ARAVM_LAT = "aravmLat";
        this.ARAVM_LONG = "aravmLong";
    }

    public static SharedPreferencesHelper getInstance() {
        if (f16289o == null) {
            f16289o = new SharedPreferencesHelper();
        }
        return f16289o;
    }

    public String getARAVMLat() {
        return this.f16302m.getString("aravmLat", null);
    }

    public String getARAVMLong() {
        return this.f16302m.getString("aravmLong", null);
    }

    public long getDefaultCityIdForSearch() {
        return this.f16298i.getLong("defaultCityId", 2301021576L);
    }

    public String getDefaultCityNameForSearch() {
        return this.f16298i.getString("defaultCityName", ("string.home_default_city_for_search"));
    }

    public boolean getHelp(String str) {
        return this.f16300k.getBoolean(str, false);
    }

    public String getLanguage() {
        return this.f16292c.getString("language", "fa");
    }

    public int getPushNotificationPlayTime() {
        return this.f16294e.getInt("playTime", 0);
    }

    public String getPushNotificationPlayerId() {
        return this.f16294e.getString("playerId", null);
    }

    public String getSignUpPhoneNumber() {
        return this.f16290a.getString("phone", null);
    }

    public String getToken() {
        return this.f16296g.getString("token", null);
    }

    public GetAccountResponse getUserInfo() {
//        Gson create = new GsonBuilder().create();
//        String string = this.f16296g.getString("userInfo", (String) null);
//        if (string != null) {
//            return (GetAccountResponse) create.fromJson(string, GetAccountResponse.class);
//        }
        return new GetAccountResponse();
    }

    public void savePushNotificationPlayTime(boolean z) {
//        this.f16295f.putInt("playTime", !z ? ((int) (System.currentTimeMillis() - MyApplication.startAppTimestamp)) / 1000 : 0);
        this.f16295f.apply();
    }

    public void savePushNotificationPlayerId(String str) {
        this.f16295f.putString("playerId", str);
        this.f16295f.apply();
    }

    public void saveToken(String str) {
        this.f16297h.putString("token", str);
        this.f16297h.apply();
    }

    public void saveUserInfo(GetAccountResponse getAccountResponse) {
//        this.f16297h.putString("userInfo", new GsonBuilder().create().toJson((Object) getAccountResponse));
        this.f16297h.apply();
    }

    public void setARAVMLat(String str) {
        this.f16303n.putString("aravmLat", str);
        this.f16303n.apply();
    }

    public void setARAVMLong(String str) {
        this.f16303n.putString("aravmLong", str);
        this.f16303n.apply();
    }

    public void setDefaultCityIdForSearch(long j) {
        this.f16299j.putLong("defaultCityId", j);
        this.f16299j.apply();
    }

    public void setDefaultCityNameForSearch(String str) {
        this.f16299j.putString("defaultCityName", str);
        this.f16299j.apply();
    }

    public void setHelp(String str, boolean z) {
        this.f16301l.putBoolean(str, z);
        this.f16301l.apply();
    }

    public void setLanguage(String str) {
        this.f16293d.putString("language", str);
        this.f16293d.apply();
    }

    public void setSignUpPhoneNumber(String str) {
        this.f16291b.putString("phone", str);
        this.f16291b.apply();
    }
}
