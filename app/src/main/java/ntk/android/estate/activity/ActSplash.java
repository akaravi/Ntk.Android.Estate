package ntk.android.estate.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ntk.android.estate.BuildConfig;
import ntk.android.estate.R;
import ntk.android.estate.config.ConfigRestHeader;
import ntk.android.estate.config.ConfigStaticValue;
import ntk.android.estate.utill.AppUtill;
import ntk.android.estate.utill.EasyPreference;
import ntk.android.estate.utill.FontManager;
import ntk.base.api.core.interfase.ICore;
import ntk.base.api.core.model.MainCoreResponse;
import ntk.base.api.utill.RetrofitManager;

public class ActSplash extends AppCompatActivity {

    @BindViews({R.id.lblAppNameActSplash,
            R.id.lblVersionActSplash})
    List<TextView> Lbls;

    @BindView(R.id.btnRefreshActSplash)
    Button BtnRefresh;

    @BindView(R.id.AnimationActSplash)
    LottieAnimationView Loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        ButterKnife.bind(this);
        init();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        Lbls.get(0).setTypeface(FontManager.GetTypeface(this, FontManager.IranSansBold));
        Lbls.get(1).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));

        Lbls.get(1).setText("نسخه  " + (int) Float.parseFloat(BuildConfig.VERSION_NAME)+"." + BuildConfig.VERSION_CODE);
        BtnRefresh.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EasyPreference.with(this).getString("configapp", "").isEmpty()) {
            if (EasyPreference.with(this).getBoolean("Intro", false)) {
                new Handler().postDelayed(() -> {
                    Loading.setVisibility(View.GONE);
                    startActivity(new Intent(ActSplash.this, ActMain.class));
                    finish();
                }, 3000);
            } else {
                new Handler().postDelayed(() -> {
                    Loading.setVisibility(View.GONE);
                    startActivity(new Intent(ActSplash.this, ActIntro.class));
                    finish();
                }, 3000);
            }
        } else {
            HandelData();
        }
    }

    private void HandelData() {
        if (AppUtill.isNetworkAvailable(this)) {
            RetrofitManager manager = new RetrofitManager(this);
            ICore iCore = manager.getCachedRetrofit(new ConfigStaticValue(this).GetApiBaseUrl()).create(ICore.class);
            Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);
            Observable<MainCoreResponse> observable = iCore.GetResponseMain(headers);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<MainCoreResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(MainCoreResponse mainCoreResponse) {
                            EasyPreference.with(ActSplash.this).addString("configapp", new Gson().toJson(mainCoreResponse.Item));
                            Loading.cancelAnimation();
                            Loading.setVisibility(View.GONE);
                            if (EasyPreference.with(ActSplash.this).getBoolean("Intro", false)) {
                                new Handler().postDelayed(() -> {
                                    Loading.setVisibility(View.GONE);
                                    startActivity(new Intent(ActSplash.this, ActMain.class));
                                    finish();
                                }, 3000);
                            } else {
                                new Handler().postDelayed(() -> {
                                    Loading.setVisibility(View.GONE);
                                    startActivity(new Intent(ActSplash.this, ActIntro.class));
                                    finish();
                                }, 3000);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Loading.cancelAnimation();
                            Loading.setVisibility(View.GONE);
                            BtnRefresh.setVisibility(View.VISIBLE);
                            Toasty.warning(ActSplash.this, "خطای سامانه مجددا تلاش کنید", Toasty.LENGTH_LONG, true).show();

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            Loading.setVisibility(View.GONE);
            BtnRefresh.setVisibility(View.VISIBLE);
            Toasty.warning(this, "عدم دسترسی به اینترنت", Toasty.LENGTH_LONG, true).show();
        }
    }

    @OnClick(R.id.btnRefreshActSplash)
    public void ClickRefresh() {
        Loading.playAnimation();
        Loading.setVisibility(View.VISIBLE);
        BtnRefresh.setVisibility(View.GONE);
        HandelData();
    }
}
