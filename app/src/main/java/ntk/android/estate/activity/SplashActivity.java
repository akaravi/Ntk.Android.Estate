package ntk.android.estate.activity;

import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import ntk.android.base.BaseNtkApplication;
import ntk.android.base.activity.common.BaseSplashActivity;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;

public class SplashActivity extends BaseSplashActivity {
    @Override
    protected void onCreated() {
        setContentView(R.layout.activity_estate_splash);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(R.id.AnimationActSplash2);
        lottieAnimationView.setImageAssetsFolder("images/");
        findViewById(R.id.splash_debugView).setOnClickListener(v -> showDebugView(v));
        TextView Lb2 = findViewById(ntk.android.base.R.id.lblWorkActSplash);
        TextView Lbl = findViewById(ntk.android.base.R.id.lblVersionActSplash);
        findViewById(ntk.android.base.R.id.splash_debugView).setOnClickListener(this::showDebugView);
        if (Lb2 != null)
            Lb2.setTypeface(FontManager.T1_Typeface(this));
        Lbl.setTypeface(FontManager.T1_Typeface(this));
        Lbl.setText(getString(ntk.android.base.R.string.version_number) + "  " + (BaseNtkApplication.get().getApplicationParameter().VERSION_NAME())
                + "    " + getString(ntk.android.base.R.string.build_number) + "  " + BaseNtkApplication.get().getApplicationParameter().VERSION_CODE());
    }

//    @Override
//    protected void onCreated() {
//        super.onCreated();
//        Intent intent = new Intent(this, ProfileActivity.class);
//        intent.putExtra(Extras.EXTRA_FIRST_ARG, 91356);
//        startActivity(intent);
//    }
}
