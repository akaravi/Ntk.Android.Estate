package ntk.android.estate.activity;

import com.airbnb.lottie.LottieAnimationView;

import ntk.android.base.activity.common.BaseSplashActivity;
import ntk.android.estate.R;

public class SplashActivity extends BaseSplashActivity {
    @Override
    protected void onCreated() {
        setContentView(R.layout.activity_estate_splash);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(R.id.AnimationActSplash2);
        lottieAnimationView.setImageAssetsFolder("images/");
        findViewById(R.id.splash_debugView).setOnClickListener(v -> showDebugView(v));
    }

//    @Override
//    protected void onCreated() {
//        super.onCreated();
//        Intent intent = new Intent(this, ProfileActivity.class);
//        intent.putExtra(Extras.EXTRA_FIRST_ARG, 91356);
//        startActivity(intent);
//    }
}
