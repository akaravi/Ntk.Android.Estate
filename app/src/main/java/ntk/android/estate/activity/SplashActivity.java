package ntk.android.estate.activity;

import com.airbnb.lottie.LottieAnimationView;

import io.sentry.Sentry;
import ntk.android.base.activity.common.AuthWithSmsActivity;
import ntk.android.base.activity.common.AuthWithSmsConfirmActivity;
import ntk.android.base.activity.common.BaseSplashActivity;
import ntk.android.estate.R;

public class SplashActivity extends BaseSplashActivity {
    @Override
    protected void onCreated() {
        setContentView(R.layout.activity_estate_splash);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(R.id.AnimationActSplash2);
        lottieAnimationView.setImageAssetsFolder("images/");
    }
}
