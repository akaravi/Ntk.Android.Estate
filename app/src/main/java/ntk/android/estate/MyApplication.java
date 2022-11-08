package ntk.android.estate;

import android.content.Context;

import androidx.multidex.MultiDex;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

import es.dmoral.toasty.Toasty;
import ir.map.sdk_map.Mapir;
import ntk.android.base.ApplicationParameter;
import ntk.android.base.ApplicationStaticParameter;
import ntk.android.base.NTKApplication;
import ntk.android.base.utill.FontManager;

//import ntk.android.base.view.ViewController;

public class MyApplication extends NTKApplication {

    @Override
    public void onCreate() {
        applicationStyle = new MyAppStyle();
        super.onCreate();
        Mapir.getInstance(this, "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjUwMGRmMjgyZjZkYjZkYmQ4ZDdjZTQxM2EyZDY0ZDg1NG" +
                "ZmOTMyMTc3NWQxOGFkNWI4ODk4NTRjOTE5MjZlYWQ1MDk0ZmMzMWY2N2FmMzIxIn0.eyJhdWQiOiIxNzE5MiIsImp0aSI6IjUwMGRmMjg" +
                "yZjZkYjZkYmQ4ZDdjZTQxM2EyZDY0ZDg1NGZmOTMyMTc3NWQxOGFkNWI4ODk4NTRjOTE5MjZlYWQ1MDk0ZmMzMWY2N2FmMzIxIiwiaWF0IjoxN" +
                "jQ1ODA1MDMzLCJuYmYiOjE2NDU4MDUwMzMsImV4cCI6MTY0ODIyMDYzMywic3ViIjoiIiwic2NvcGVzIjpbImJhc2ljIl19.GiAE2GIo0vz1RNF9JloT" +
                "pB4WbpTS6y4P7KVH4QpuJWih4QBWhz9tZKSk9RXIODYkUe5qa8KZJfYjr4N0VOQVEQLSfhX-hn64Imjx1Y-4cW9x0hq8NskTgMtcovHg59qrVJdzM" +
                "EcpJRWuUl3V8wyPfXj9lm-OY_h2Cp0AGlc9s2mKHA3UpNP5-JMYLX25r4HZGQBlkVI4xPiM_FY2CaX2lueY6xUFQZbcsII3kS-VNWYyy5aND6OB" +
                "8O6139k1kxbQLz0xBwKBWsnLOKFqva6IHhr8xmY-Wmb06YPvYajZpbR1Vy8JflGKNFZ-pJUP_Uf0whhdrpqjhN_0jkdcu7Ci0Q");

        DEBUG = true;
        if (!new File(getCacheDir(), "image").exists()) {
            new File(getCacheDir(), "image").mkdirs();
        }
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .diskCache(new UnlimitedDiskCache(new File(getCacheDir(), "image")))
                .diskCacheFileNameGenerator(imageUri -> {
                    String[] Url = imageUri.split("/");
                    return Url[Url.length];
                })
                .build();
        ImageLoader.getInstance().init(config);

        Toasty.Config.getInstance()
                .setToastTypeface(FontManager.T1_Typeface(getApplicationContext()))
                .setTextSize(14).apply();

    }

    @Override
    protected void attachBaseContext(Context base) {
        instance = this;
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    protected ApplicationStaticParameter getConfig() {
        ApplicationStaticParameter applicationStaticParameter = new ApplicationStaticParameter();
        //todo server ngrok replacement
//        applicationStaticParameter.URL = "https://f3ea-94-183-160-75.ngrok.io";
        return applicationStaticParameter;
    }

    @Override
    public ApplicationParameter getApplicationParameter() {
        return new ApplicationParameter(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
    }
}
