package ntk.android.estate;

import android.content.Context;

import androidx.multidex.MultiDex;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.maplibre.android.MapLibre;

import java.io.File;

import es.dmoral.toasty.Toasty;
import ntk.android.base.ApplicationParameter;
import n.android.base.ApplicationStaticParameter;
import ntk.android.base.NTKApplication;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.BuildConfig;


public class MyApplication extends NTKApplication {

    @Override
    public void onCreate() {
        applicationStyle = new MyAppStyle();
        super.onCreate();
        MapLibre.getInstance(this, "");

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
//        applicationStaticParameter.URL = "https://tn9wf781-2390.euw.devtunnels.ms";
        return applicationStaticParameter;
    }

    @Override
    public ApplicationParameter getApplicationParameter() {
        String applicationId = BuildConfig.APPLICATION_ID;
        return new ApplicationParameter(applicationId, getString(R.string.app_name), BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
    }

}
