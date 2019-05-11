package ntk.android.estate;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import ntk.android.estate.model.theme.Drawer;
import ntk.android.estate.model.theme.DrawerChild;
import ntk.android.estate.utill.FontManager;

public class EState extends MultiDexApplication {

    public static boolean Inbox = false;
    public static String JsonThemeExmaple = "";

    @Override
    public void onCreate() {
        super.onCreate();
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
                .setToastTypeface(FontManager.GetTypeface(getApplicationContext(), FontManager.IranSans))
                .setTextSize(14).apply();

        CreateJson();
    }

    private void CreateJson() {

        Drawer drawer = new Drawer();
        drawer.Type = 1;
        List<DrawerChild> childs = new ArrayList<>();
        childs.add(new DrawerChild(1, 1, "صندوق پیام", "https://image.flaticon.com/icons/png/512/107/107822.png"));
        childs.add(new DrawerChild(2, 2, "اخبار", "https://image.flaticon.com/icons/png/512/31/31866.png"));
        childs.add(new DrawerChild(11, 2, "مجلات", "https://cocreatr.typepad.com/.a/6a010534a7bc55970b01156fd9830b970b-800wi"));
        childs.add(new DrawerChild(3, 2, "نظرسنجی", "https://image.flaticon.com/icons/png/512/120/120114.png"));
        childs.add(new DrawerChild(4, 2, "تنظیمات", "https://image.flaticon.com/icons/png/512/57/57047.png"));
        childs.add(new DrawerChild(5, 2, "دعوت از دوستان", "https://image.flaticon.com/icons/png/512/109/109534.png"));
        childs.add(new DrawerChild(6, 2, "درباره ما", "https://image.flaticon.com/icons/png/512/97/97849.png"));
        childs.add(new DrawerChild(7, 2, "تماس با ما", "https://image.flaticon.com/icons/png/512/13/13936.png"));
        childs.add(new DrawerChild(8, 2, "بازخورد", "https://image.flaticon.com/icons/png/512/87/87702.png"));
        childs.add(new DrawerChild(9, 2, "پرسش های متداول", "https://image.flaticon.com/icons/png/512/43/43392.png"));
        childs.add(new DrawerChild(10, 2, "راهنما", "http://cdn.onlinewebfonts.com/svg/img_137547.png"));
        drawer.Child = childs;

        JsonThemeExmaple = new Gson().toJson(drawer);
        Log.i("JsonTheme", JsonThemeExmaple);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
